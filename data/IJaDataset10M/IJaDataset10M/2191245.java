package jiso.map;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * @author Neil
 *
 */
public class MapTile {

    public static final int NORMAL = 0;

    public static final int UP_TO_NORTH = 1;

    public static final int UP_TO_EAST = 2;

    public static final int UP_TO_SOUTH = 3;

    public static final int UP_TO_WEST = 4;

    public static final int EMPTY = 5;

    private static final Color TOP_COLOR = Color.DARK_GRAY;

    private static final Color RIGHT_COLOR = Color.LIGHT_GRAY;

    private static final Color LEFT_COLOR = Color.GRAY;

    private static final Color SELECTED_COLOR = Color.BLUE;

    private static final Color TARGET_COLOR = Color.ORANGE;

    private static final Color PLAYER_MOVE_COLOR = Color.BLUE;

    private static final Color ENEMY_MOVE_COLOR = Color.RED;

    private int orientation;

    private float height;

    private float startHeight;

    private float endHeight;

    private boolean selected;

    private Color myColor;

    private Polygon top;

    private Point fieldLocation;

    /**
     * Constructor
     * @param orientation The orientation of this tile
     * @param startHeight The lower height of the tile (If slanted)
     * @param endHeight The upper hieght of the tile (If slanted)
     */
    public MapTile(int orientation, float startHeight, float endHeight) {
        this.orientation = orientation;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.height = (startHeight + endHeight) / 2;
        this.selected = false;
        this.fieldLocation = new Point();
        this.myColor = TOP_COLOR;
    }

    public MapTile(MapTile tile) {
        this.orientation = tile.orientation;
        this.startHeight = tile.startHeight;
        this.endHeight = tile.endHeight;
        this.height = tile.height;
        this.selected = tile.selected;
        this.fieldLocation = tile.fieldLocation;
        this.myColor = tile.myColor;
    }

    /**Get the tiles orientation
     * @return the orientation
     */
    public int getOrientation() {
        return orientation;
    }

    /**Get the tiles average height
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**Get the tiles lower height (if slanted else return height)
     * @return the startHeight
     */
    public float getStartHeight() {
        return startHeight;
    }

    /**Get the tiles upper height (if slanted else return height)
     * @return the endHeight
     */
    public float getEndHeight() {
        return endHeight;
    }

    /**Return true if tile is selected, else return false
     * @return selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**Get the polygon that is the top of the tile
     * @return the top
     */
    public Polygon getTop() {
        return top;
    }

    /**Get this tiles location on the field
     * @return the fieldLocation
     */
    public Point getFieldLocation() {
        return fieldLocation;
    }

    /**
     * Store the tiles location on the map (set when map is made)
     * @param p The point that represents the (x,y) coordinate of the tile
     */
    public void setFieldLocation(Point p) {
        fieldLocation = p;
    }

    /**Set the orientation of the tile. Any value that is not
     * an orientation value is treated as MapTile.EMPTY
     * @param orientation the orientation to set
     */
    public void setOrientation(int orientation) {
        if (orientation < 0 || orientation > 5) {
            orientation = EMPTY;
        }
        this.orientation = orientation;
    }

    /**Set the lower height of the tile (also updates average height)
     * @param startHeight the startHeight to set
     */
    public void setStartHeight(float startHeight) {
        System.out.println("Setting Start Height:\n" + this.startHeight + ", " + this.endHeight + ", " + this.height);
        this.startHeight = startHeight;
        this.height = (startHeight + endHeight) / 2;
        System.out.println(this.startHeight + ", " + this.endHeight + ", " + this.height);
    }

    /**Set the upper height of the tile (also updates average height)
     * @param endHeight the endHeight to set
     */
    public void setEndHeight(float endHeight) {
        System.out.println("Setting End Height:\n" + this.startHeight + ", " + this.endHeight + ", " + this.height);
        this.endHeight = endHeight;
        this.height = (startHeight + endHeight) / 2;
        System.out.println(this.startHeight + ", " + this.endHeight + ", " + this.height);
    }

    /**Set if the tile is selected of not
     * @param selected new state of the tiles selection status
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        myColor = MapTile.SELECTED_COLOR;
    }

    /**
     * Change the color of the top of the tile
     * @param myColor The new top color ofr this tile
     */
    private void setColor(Color myColor) {
        this.myColor = myColor;
    }

    /**Check to see if this tile was clicked on.
     * This method does not set the selected tile. That is handled in the Map class
     * @param click Location of the mouse click
     */
    public boolean wasClickedOn(Point click) {
        return top.contains(click);
    }

    /**
     * Decide what color to paint the top of this tile
     * (Called from Map class)
     */
    public void determineColor() {
        if (isSelected()) {
            setColor(MapTile.SELECTED_COLOR);
            return;
        } else {
            setColor(MapTile.TOP_COLOR);
        }
    }

    /**
     * Draw this MapTile object
     * @param x x-Location to draw tile
     * @param y y-Location to draw tile
     * @param g Graphics object to draw with
     */
    public void draw(int x, int y, Graphics g) {
        switch(orientation) {
            case NORMAL:
                drawNormal(x, y, g);
                break;
            case UP_TO_NORTH:
            case UP_TO_SOUTH:
                drawNorthSouth(x, y, g);
                break;
            case UP_TO_EAST:
            case UP_TO_WEST:
                drawEastWest(x, y, g);
                break;
            case EMPTY:
                break;
            default:
                System.out.println("That orientation doesn't exist!");
        }
    }

    /**
     * Draw a Standard Tile.  Standard Tiles do not have a slant to them.
     * Note that the drawing begins at (x,y - MapSettings.tileHeight*height).
     * @param x X-location of the tile.
     * @param y Y-location of the tile.
     * @param g The Graphcis object with which to draw.
     */
    public void drawNormal(int x, int y, Graphics g) {
        determineColor();
        int finalHeight = (int) (MapSettings.tileHeight * MapSettings.zoom * height);
        int horizontal = (int) (MapSettings.tileDiagonal * MapSettings.zoom);
        int vertical = (int) (MapSettings.tileDiagonal * MapSettings.pitch * MapSettings.zoom);
        Color oldColor = g.getColor();
        g.setColor(myColor);
        g.fillPolygon(top = new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y - finalHeight, y + vertical / 2 - finalHeight, y + vertical - finalHeight, y + vertical / 2 - finalHeight }, 4));
        g.setColor(RIGHT_COLOR);
        g.fillPolygon(new Polygon(new int[] { x, x + horizontal / 2, x + horizontal / 2, x }, new int[] { y - finalHeight + vertical, y - finalHeight + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(LEFT_COLOR);
        g.fillPolygon(new Polygon(new int[] { x, x - horizontal / 2, x - horizontal / 2, x }, new int[] { y - finalHeight + vertical, y - finalHeight + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(Color.BLACK);
        g.drawPolygon(new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y - finalHeight, y + vertical / 2 - finalHeight, y + vertical - finalHeight, y + vertical / 2 - finalHeight }, 4));
        g.drawPolygon(new Polygon(new int[] { x, x + horizontal / 2, x + horizontal / 2, x }, new int[] { y - finalHeight + vertical, y - finalHeight + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.drawPolygon(new Polygon(new int[] { x, x - horizontal / 2, x - horizontal / 2, x }, new int[] { y - finalHeight + vertical, y - finalHeight + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(oldColor);
    }

    /**
     * Draw a tile that slants to the north or the south.  The methods for drawing
     * are very similar, so they can be merged into one method.
     * Note that the drawing begins at (x,y - MapSettings.tileHeight*height).
     * @param x X-location of the tile.
     * @param y Y-location of the tile.
     * @param g The Graphcis object with which to draw.
     */
    public void drawNorthSouth(int x, int y, Graphics g) {
        determineColor();
        int finalHeight = (int) (MapSettings.tileHeight * MapSettings.zoom);
        int horizontal = (int) (MapSettings.tileDiagonal * MapSettings.zoom);
        int vertical = (int) (MapSettings.tileDiagonal * MapSettings.pitch * MapSettings.zoom);
        int HEIGHT1 = orientation == UP_TO_NORTH ? (int) (finalHeight * startHeight) : (int) (finalHeight * endHeight);
        int HEIGHT2 = orientation == UP_TO_NORTH ? (int) (finalHeight * endHeight) : (int) (finalHeight * startHeight);
        Color oldColor = g.getColor();
        g.setColor(myColor);
        g.fillPolygon(top = new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y - HEIGHT2, y - HEIGHT2 + vertical / 2, y - HEIGHT1 + vertical, y - HEIGHT1 + vertical / 2 }, 4));
        g.setColor(RIGHT_COLOR);
        g.fillPolygon(new Polygon(new int[] { x, x + horizontal / 2, x + horizontal / 2, x }, new int[] { y - HEIGHT1 + vertical, y - HEIGHT2 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(LEFT_COLOR);
        g.fillPolygon(new Polygon(new int[] { x, x - horizontal / 2, x - horizontal / 2, x }, new int[] { y - HEIGHT1 + vertical, y - HEIGHT1 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(Color.BLACK);
        g.drawPolygon(new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y - HEIGHT2, y - HEIGHT2 + vertical / 2, y - HEIGHT1 + vertical, y - HEIGHT1 + vertical / 2 }, 4));
        g.drawPolygon(new Polygon(new int[] { x, x + horizontal / 2, x + horizontal / 2, x }, new int[] { y - HEIGHT1 + vertical, y - HEIGHT2 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.drawPolygon(new Polygon(new int[] { x, x - horizontal / 2, x - horizontal / 2, x }, new int[] { y - HEIGHT1 + vertical, y - HEIGHT1 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(oldColor);
    }

    /**
     * Draw a tile that slants to the east or west.  The methods for drawing
     * are very similar, so they can be merged into one method.
     * Note that the drawing begins at (x,y - MapSettings.tileHeight*height).
     * @param x X-location of the tile.
     * @param y Y-location of the tile.
     * @param g The Graphcis object with which to draw.
     */
    public void drawEastWest(int x, int y, Graphics g) {
        determineColor();
        int finalHeight = (int) (MapSettings.tileHeight * MapSettings.zoom);
        int horizontal = (int) (MapSettings.tileDiagonal * MapSettings.zoom);
        int vertical = (int) (MapSettings.tileDiagonal * MapSettings.pitch * MapSettings.zoom);
        int HEIGHT1 = orientation == UP_TO_EAST ? (int) (finalHeight * startHeight) : (int) (finalHeight * endHeight);
        int HEIGHT2 = orientation == UP_TO_EAST ? (int) (finalHeight * endHeight) : (int) (finalHeight * startHeight);
        Color oldColor = g.getColor();
        g.setColor(myColor);
        g.fillPolygon(top = new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y - HEIGHT1, y - HEIGHT2 + vertical / 2, y - HEIGHT2 + vertical, y - HEIGHT1 + vertical / 2 }, 4));
        g.setColor(RIGHT_COLOR);
        g.fillPolygon(new Polygon(new int[] { x, x + horizontal / 2, x + horizontal / 2, x }, new int[] { y - HEIGHT2 + vertical, y - HEIGHT2 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(LEFT_COLOR);
        g.fillPolygon(new Polygon(new int[] { x, x - horizontal / 2, x - horizontal / 2, x }, new int[] { y - HEIGHT2 + vertical, y - HEIGHT1 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(Color.BLACK);
        g.drawPolygon(new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y - HEIGHT1, y - HEIGHT2 + vertical / 2, y - HEIGHT2 + vertical, y - HEIGHT1 + vertical / 2 }, 4));
        g.drawPolygon(new Polygon(new int[] { x, x + horizontal / 2, x + horizontal / 2, x }, new int[] { y - HEIGHT2 + vertical, y - HEIGHT2 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.drawPolygon(new Polygon(new int[] { x, x - horizontal / 2, x - horizontal / 2, x }, new int[] { y - HEIGHT2 + vertical, y - HEIGHT1 + vertical / 2, y + vertical / 2, y + vertical }, 4));
        g.setColor(oldColor);
    }

    /**
     * Get an exact copy of this tile.
     * @return An exacpt copy of this tile.
     */
    @Override
    public MapTile clone() {
        return new MapTile(orientation, startHeight, endHeight);
    }
}
