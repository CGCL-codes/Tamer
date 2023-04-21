package klient.model;

/**
 * Pozycja monety
 * @author Szaman
 *
 */
public class CoinPosition {

    /**
	 * Pozycja monety.
	 */
    private int x, y;

    /**
	 * Konstruktor pozycji monety
	 * @param x
	 * @param y
	 */
    public CoinPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
	 * @return the x
	 */
    public int getX() {
        return x;
    }

    /**
	 * @return the y
	 */
    public int getY() {
        return y;
    }
}
