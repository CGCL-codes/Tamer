package scamsoft.squadleader.rules;

import java.io.Serializable;
import scamsoft.util.Point;

/**
 * Captures features related to Hexagonal Geometry
 * User: Andreas Mross
 * Date: Oct 18, 2004
 * Time: 10:35:31 PM
 */
public class HexGeometry implements Serializable {

    private double hexWidth;

    private double hexHeight;

    /**
     * Create a new HexGeometry instance.
     *
     * @param hexWidth  The distance between the centres of two horizontally adjacent hexes.
     * @param hexHeight The distance between the centres of two vertically adjacent hexes.
     */
    public HexGeometry(double hexWidth, double hexHeight) {
        if (hexWidth < 0) throw new IllegalArgumentException("width must be positive: " + hexWidth);
        if (hexHeight < 0) throw new IllegalArgumentException("width must be positive: " + hexHeight);
        this.hexWidth = hexWidth;
        this.hexHeight = hexHeight;
    }

    /**
     * Get the HexCoordinate at the given Point
     *
     * @param p A Point on the map in Pixels
     * @return The Hex at that point. Returns null if there is no Hex there.
     */
    public HexCoordinate getHexCoordinate(Point p) {
        double hexsideLength = getHexPixelWidth() * 2 / 3;
        double halfHexsideLength = getHexPixelWidth() / 3;
        int adjustYHexes = 10;
        int modifiedY = (int) (p.getY() + getHexPixelHeight() * adjustYHexes);
        int modifiedX = (int) (p.getX() + halfHexsideLength + getHexPixelWidth() * adjustYHexes);
        int hexX = (int) (modifiedX / getHexPixelWidth());
        int hexXRemainder = (int) (modifiedX - hexX * getHexPixelWidth());
        if (hexX % 2 == 1) {
            modifiedY += getHexPixelHeight() / 2;
        }
        int hexY = (int) (modifiedY / getHexPixelHeight());
        int hexYRemainder = (int) (modifiedY - hexY * getHexPixelHeight());
        if (hexXRemainder > hexsideLength) {
            double x = hexXRemainder - hexsideLength;
            double height = (halfHexsideLength - x) * hexslope;
            if (hexYRemainder < getHexPixelHeight() / 2 - height) {
                hexX++;
                if (hexX % 2 == 0) {
                    hexY--;
                }
            }
            if (hexYRemainder > getHexPixelHeight() / 2 + height) {
                hexX++;
                if (hexX % 2 == 1) {
                    hexY++;
                }
            }
        }
        hexY -= adjustYHexes;
        hexX -= adjustYHexes;
        return new HexCoordinateValue(hexX + 1, hexY + 1);
    }

    /**
     * Gets the height in pixels of one Hex
     *
     * @return The distance from the centre of one hex to the centre of the hex immediately above it
     */
    public double getHexPixelHeight() {
        return hexHeight;
    }

    /**
     * Gets the width in pixels of one Hex.
     *
     * @return The number of pixels from one hex centre to the next horizontally
     */
    public double getHexPixelWidth() {
        return hexWidth;
    }

    /**
     * Returns the x location in pixels of the centre of the given hex
     *
     * @param hex
     * @return
     */
    public int getXPixel(HexCoordinate hex) {
        int x = hex.getX();
        return (int) ((x - 1) * getHexPixelWidth());
    }

    /**
     * Returns the y location in pixels of the centre of the given hex
     *
     * @param hex
     * @return
     */
    public int getYPixel(HexCoordinate hex) {
        int y = hex.getY();
        double result = ((y - 1) * getHexPixelHeight() + 1);
        if (hex.getX() % 2 == 1) {
            result += (getHexPixelHeight() / 2);
        }
        return (int) result;
    }

    /**
     * Gets the Point at the centre of the given Hex
     *
     * @param hex Description of Parameter
     * @return The Point value
     */
    public Point getPoint(HexCoordinate hex) {
        return new Point(getXPixel(hex), getYPixel(hex));
    }

    private static final double hexslope = Math.tan(Math.toRadians(60));

    static final long serialVersionUID = 3050581102113768679L;
}
