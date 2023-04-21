package au.edu.swin.rp.micro;

import java.util.Vector;
import javax.microedition.lcdui.Image;

/**
 * 
 * Represent a tile in a map
 * @author Sandun
 * @since 07-Nov-2007
 */
public class Tile {

    /** tile number on x direction */
    private int xTileNumber;

    /** tile number on y direction */
    private int yTileNumber;

    /** zoom level direction */
    private int zoom;

    /** openstreet image of this tile's x and y values */
    private Image image;

    private Vector routePoints;

    public String getImageName() {
        return zoom + "/" + xTileNumber + "/" + yTileNumber;
    }

    public int getXTileNumber() {
        return xTileNumber;
    }

    public void setXTileNumber(int tileNumber) {
        xTileNumber = tileNumber;
    }

    public int getYTileNumber() {
        return yTileNumber;
    }

    public void setYTileNumber(int tileNumber) {
        yTileNumber = tileNumber;
    }

    public Tile(int x, int y, int z) {
        super();
        xTileNumber = x;
        yTileNumber = y;
        zoom = z;
        this.image = null;
        routePoints = new Vector();
    }

    public Tile() {
    }

    public int getZoom() {
        return zoom;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Vector getRoutePoints() {
        return routePoints;
    }

    public void addRoutePoint(MapPosition p) {
        routePoints.addElement(p);
    }
}
