package gear.widgets;

/**
 * Class rappresenting a bounding box used for hit testing with pointer interfaces
 * @author Stefano Driussi
 *
 */
public class HitRect {

    private int minX;

    private int minY;

    private int maxX;

    private int maxY;

    /**
	 * Public constructor. Sets all values to 0
	 */
    public HitRect() {
        this(0, 0, 0, 0);
    }

    /**
	 * Public constructor
	 * @param minX Minimum x value
	 * @param minY Minimum y value
	 * @param maxX Maximum x value
	 * @param maxY Maximum y value
	 */
    public HitRect(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
	 * Returns minimum value for x
	 * @return value
	 */
    public int getMinX() {
        return minX;
    }

    /**
	 * Sets minimum value for x
	 * @param x value
	 */
    public void setMinX(int x) {
        this.minX = x;
    }

    /**
	 * Returns minimum value for y
	 * @return value
	 */
    public int getMinY() {
        return minY;
    }

    /**
	 * Sets minimum value for y
	 * @param y value
	 */
    public void setMinY(int y) {
        this.minY = y;
    }

    /**
	 * Returns maximum value for x
	 * @return value
	 */
    public int getMaxX() {
        return maxX;
    }

    /**
	 * Sets maximum value for x
	 * @param maxX value
	 */
    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    /**
	 * Returns maximum value for y
	 * @return value
	 */
    public int getMaxY() {
        return maxY;
    }

    /**
	 * Sets maximum value for y
	 * @param maxY value
	 */
    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    /**
	 * Hit test method determining whether a point belongs to this bounding rect
	 * @param x
	 * @param y
	 * @return True if the point belongs to this bounding rect, False elsewhere
	 */
    public boolean contains(int x, int y) {
        return x >= this.minX && x <= maxX && y >= this.minY && y <= maxY;
    }

    /**
	 * Sets all four parameters defining the bounding box.
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 */
    public void setRect(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
}
