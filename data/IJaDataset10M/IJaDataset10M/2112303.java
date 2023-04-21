package bomberman.server;

import bomberman.server.api.Element;
import java.io.Serializable;

/**
 * Logical Playground where the Server manages a game.
 * This class is serializable and takes most of the network bandwidth.
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class Playground implements Serializable {

    public static final int DEFAULT_WIDTH = 17;

    public static final int DEFAULT_HEIGHT = 15;

    /**
   * 3D-matrix. The third level has size 5. 
   * [][][0] is for Bombs and Extras
   * [][][1-4] is for Players.
   */
    private Element[][][] matrix = null;

    private int cols, rows;

    public Playground(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.matrix = new Element[cols][rows][5];
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if ((x == 0) || (x == cols - 1) || (y == 0) || (y == rows - 1)) {
                    this.matrix[x][y][0] = new SolidWall(x, y);
                } else if ((y % 2 == 0) && (x % 2 == 0)) {
                    this.matrix[x][y][0] = new SolidWall(x, y);
                } else if ((x == 1 && (y == 1 || y == 2)) || (x == 2 && y == 1) || (x == cols - 2 && (y == 1 || y == 2)) || (x == cols - 3 && y == 1) || (x == 1 && (y == rows - 2 || y == rows - 3)) || (x == 2 && y == rows - 2) || (x == cols - 2 && (y == rows - 2 || y == rows - 3)) || (x == cols - 3 && y == rows - 2)) {
                    continue;
                } else if (Math.random() >= 0.2) {
                    matrix[x][y][0] = new ExplodableWall(x, y);
                }
            }
        }
    }

    /**
   * Rename tp getColumns
   * @return
   */
    public int getWidth() {
        return cols;
    }

    /**
   * TODO: rename to getRows
   * @return
   */
    public int getHeight() {
        return rows;
    }

    /**
   * Get Element at x,y 
   * @param x
   * @param y
   * @return Element[] at the specific position
   */
    public Element[] getElement(int x, int y) {
        try {
            return this.matrix[x][y];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
   * Sets Element e at x,y on layer layer
   * @param x
   * @param y
   * @param layer
   * @param e
   */
    public void setElement(int x, int y, int layer, Element e) {
        this.matrix[x][y][layer] = e;
    }
}
