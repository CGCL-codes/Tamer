package sudoku.engine;

import sudoku.Dimensions;
import sudoku.exceptions.CoordinateException;

public class Coordinate {

    private static final int MAX = Dimensions.DIM;

    int x;

    int y;

    public Coordinate(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void increment() throws CoordinateException {
        if (++y == MAX) {
            y = 0;
            x++;
        }
        if (x == MAX) {
            throw new CoordinateException();
        }
    }

    public void print() {
        System.out.println("(" + x + "," + y + ")");
    }
}
