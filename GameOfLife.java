

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GameOfLife extends Frame {
  public Life life;

  public static void main(String[] args) {
    GameOfLife gol = new GameOfLife();
    gol.setVisible(true);
    gol.loop();
  }

  public GameOfLife() {
    setSize(width(), height());
    this.life = new Life(width() / tileSize(), height() / tileSize());
  }

  public int width() {
    return 640;
  }

  public int height() {
    return 480;
  }

  public int tileSize() {
    return 2;
  }

  public class Life {
    int width;
    int height;
    double prob;
    boolean[][] grid;

    public Life(int width, int height, double prob) {
      initialize(width, height, prob);
    }

    public Life(int width, int height) {
      initialize(width, height, 0.5);
    }

    private void initialize(int width, int height, double prob) {
      this.width  = width;
      this.height = height;
      this.prob   = prob;

      this.grid   = new boolean[width][height];
      randomizeGrid(grid);
    }

    public void randomizeGrid(boolean[][] grid) {
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (Math.random() < prob) {
            grid[i][j] = true;
          } else {
            grid[i][j] = false;
          }
        }
      }
    }

    public int neighbors(int i, int j) {
      int si = Math.max(0, i-1);
      int ei = Math.min(i+1, width-1);
      int sj = Math.max(0, j-1);
      int ej = Math.min(j+1, height-1);

      int total = 0;
      for (int x = si; x <= ei; x++) {
        for (int y = sj; y <= ej; y++) {
          if (grid[x][y])
            total += 1;
        }
      }
      return total;
    }

    public void drawOnto(Graphics2D g2d, int size) {
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (grid[i][j]) {
            g2d.setColor(Color.white);
          } else {
            g2d.setColor(Color.black);
          }

          g2d.drawRect(i*size, j*size, size, size);
        }
      }
    }

    public boolean fate(int i, int j) {
      if (grid[i][j]) {
        if (neighbors(i, j) < 2) {
          return false; // Underpopulation
        } else if (neighbors(i, j) < 4) {
          return true; // Stable population
        } else if (neighbors(i, j) > 3) {
          return false; // Overpopulation
        } else {
          return grid[i][j];
        }
      } else {
        if (neighbors(i, j) == 3) {
          return true; // Reproduction
        } else {
          return grid[i][j];
        }
      }
    }

    public void nextState() {
      boolean[][] newGrid = new boolean[width][height];

      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          newGrid[i][j] = fate(i, j);
        }
      }

      this.grid = newGrid;
    }
  }

  public void loop() {
    while (true) {
      repaint();
      life.nextState();
    }
  }

  public void paint(Graphics g) {
    super.paint(g);

    Graphics2D g2d = (Graphics2D) g;
    life.drawOnto(g2d, tileSize());
  }
}
