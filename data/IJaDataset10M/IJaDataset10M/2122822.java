package Cosmo.util.cautils.datastructures;

public abstract class PairValues {

    private int x;

    private int y;

    public PairValues(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PairValues() {
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

    public boolean equals(PairValues d2) {
        if (this.x == d2.x && this.y == d2.y) return true; else return false;
    }

    public abstract String toString();
}
