package life;

/** Unoptimised implementation of Conway's Life genetic algorithim.
 * Compare with Life.java.
 */
public abstract class CleanLife {

    /** Number of alive neighbours */
    protected final byte[][] count;

    /** Age of this cell.  0 is currently dead. */
    protected final byte[][] age;

    /** Dimensions of the field */
    protected final int dx, dy;

    /** Create a new Life board with the given width and height */
    public CleanLife(final int dx, final int dy) {
        this.dx = dx;
        this.dy = dy;
        count = new byte[dx][];
        age = new byte[dx][];
        for (int i = 0; i < dx; i++) {
            count[i] = new byte[dy];
            age[i] = new byte[dy];
        }
    }

    /** Implemented by the sub class to render the board */
    protected abstract void render();

    /** Touch a cell, increasing it's neighbour count. */
    protected final void touch(int x, int y) {
        if (x < 0) {
            x += dx;
        } else if (x >= dx) {
            x -= dx;
        }
        if (y < 0) {
            y += dy;
        } else if (y >= dy) {
            y -= dy;
        }
        count[x][y]++;
    }

    protected final void countNeighbours() {
        for (int x = 0; x < dx; x++) {
            for (int y = 0; y < dy; y++) {
                if (age[x][y] != 0) {
                    touch(x + 1, y - 1);
                    touch(x + 1, y);
                    touch(x + 1, y + 1);
                    touch(x, y + 1);
                    touch(x, y - 1);
                    touch(x - 1, y - 1);
                    touch(x - 1, y);
                    touch(x - 1, y + 1);
                }
            }
        }
    }

    protected final void evolve() {
        for (int x = 0; x < dx; x++) {
            for (int y = 0; y < dy; y++) {
                switch(count[x][y]) {
                    case 2:
                        if (age[x][y] != 0) {
                        } else {
                            age[x][y] = 0;
                        }
                        break;
                    case 3:
                        age[x][y] = 1;
                        break;
                    default:
                        age[x][y] = 0;
                        break;
                }
                count[x][y] = 0;
            }
        }
    }

    /** Scans the board, updating the neighbours count and computes
	the next board.
    */
    protected final void update() {
        countNeighbours();
        evolve();
    }
}
