package battleships;

/**
 *
 * @author Ben Station
 */
public class OpponentGrid2 extends AGrid {

    /** Creates a new instance of OpponentGrid2 */
    private Object[][] grid;

    private int x = 78, y = 112, xOffset = 0, yOffset = 0;

    public OpponentGrid2() {
        grid = new Object[9][10];
        for (int i = 0; i < 9; i++) {
            int tmpX = x;
            for (int j = 0; j < 10; j++) {
                grid[i][j] = new int[] { tmpX, y };
                tmpX += 8;
            }
            y -= 9;
        }
    }

    public int[] getCoordinate(int x, int y) {
        return (int[]) grid[y][x];
    }

    public void setOffset(int x1, int y1) {
        x += x1;
        y += y1;
    }
}
