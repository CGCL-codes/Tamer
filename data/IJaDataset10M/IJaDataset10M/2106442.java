package au.edu.swin.rp.micro;

/**
 * Represents the position of the a point on the map image
 * x and y are pixels
 * @author Sandun
 * @since 07-Nov-2007
 */
public class MapPosition {

    private double x;

    private double y;

    public MapPosition(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public MapPosition() {
    }

    public MapPosition(MapPosition currentMapPosition) {
        x = currentMapPosition.getX();
        y = currentMapPosition.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
