package org.dmpotter.mapper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import org.dmpotter.util.Application;

/**
 * A map - a collection of tiles that make up a map.
 * @author dmpotter
 * @version $Revision: 1.9 $
 */
public class Map {

    /**
     * Active floor property.  This property is the currently selected floor.
     */
    public static final String FLOOR = "floor";

    /**
     * Object selection property.  This property currently contains either
     * the selected object or an array of selected objects, or <code>null</code>
     * if no objects are selected.
     */
    public static final String OBJECT_SELECTION = "objectSelection";

    private int width, height, curFloor;

    private MapFloor floors[];

    private TileSet set;

    private ArrayList listeners;

    /**
     * Creates a new map of the given size.
     * <p>
     * Regardless of the tile type, maps are always internally a grid.
     * This works because a hex tiles set simply slides every other row
     * slightly forward or slightly backward, leaving each row with
     * the same number of tiles.
     * @param base the TileSet which will provide tiles for this map
     * @param width the width of the map
     * @param height the height of the map
     * @throws IllegalArgumentException if the width or height is less
     * than 1
     * @throws NullPointerException if the default tile is <code>null</code>
     */
    public Map(TileSet base, int width, int height) {
        this.width = width;
        this.height = height;
        set = base;
        if (width < 1 || height < 1) throw new IllegalArgumentException("Width/height must be positive");
        floors = new MapFloor[1];
        curFloor = 0;
        floors[0] = new MapFloor(this, "New Floor");
        listeners = new ArrayList(4);
    }

    /**
     * Sets a tile at the given location.
     * <p>
     * This sets the tile on the current floor.
     * @param t the tile to use at the given location
     * @param x,&nbsp;y the coordinate to set the tile, 0-based (the tile
     * in the upper-left-hand corner is 0, 0)
     * @throws NullPointerException if the tile is <code>null</code>
     * @throws IndexOutOfBoundsException if the x, y coordinate does not
     * lie within the map boundaries
     * @throws IllegalArgumentException if the tile does not exist within the
     * current tile set
     * @return <code>true</code> if the tile was changed, <code>false</code>
     * if the setting the tile kept the the tile the same
     */
    public boolean setTile(Tile t, int x, int y) {
        return floors[curFloor].setTile(t, x, y);
    }

    /**
     * Adds the specified object to the currently active floor in the map.
     * @param obj the object to add to the current floor in the map
     */
    public void addObject(MapObject obj) {
        floors[curFloor].addObject(obj);
    }

    /**
     * Gets the "physical" map size - the size that the map will take when
     * rendered fully.  This is based on the tile set.
     */
    public Dimension getPhysicalMapSize() {
        if (set.getType() == TileSet.HEX_TILE) {
            return new Dimension(set.getWidth() * width + set.getXOffset(), set.getYOffset() * (height - 1) + set.getHeight());
        } else return new Dimension(set.getWidth() * width, set.getHeight() * height);
    }

    /**
     * Gets the height of the map in tiles.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width of the map in tiles.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Selects the object at the given point, or unselects all objects if
     * the given point is not over any object.
     * @param x x-coordinate in map space
     * @param y y-coordinate in map space
     */
    public void selectObject(int x, int y) {
        MapObject oObj = floors[curFloor].getSelectedObject();
        floors[curFloor].selectObject(x, y);
        MapObject nObj = floors[curFloor].getSelectedObject();
        if (oObj != nObj) firePropertyChange(OBJECT_SELECTION, oObj, nObj);
    }

    public void activateObject(java.awt.Window parent, int x, int y) {
        floors[curFloor].activateObject(parent, x, y);
    }

    public boolean hasSelectedObject() {
        return floors[curFloor].hasSelectedObject();
    }

    public boolean hasObjects() {
        return floors[curFloor].hasObjects();
    }

    public void deleteSelectedObject() {
        MapObject oObj = floors[curFloor].getSelectedObject();
        if (oObj != null) {
            floors[curFloor].deleteSelectedObject();
            firePropertyChange(OBJECT_SELECTION, oObj, floors[curFloor].getSelectedObject());
        }
    }

    public MapFloor[] getFloors() {
        MapFloor buf[] = new MapFloor[floors.length];
        System.arraycopy(floors, 0, buf, 0, floors.length);
        return buf;
    }

    public int getNumberFloors() {
        return floors.length;
    }

    /**
     * Sets the floor to the given index.
     * @param i the new index of the floor
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public void setSelectedFloor(int i) {
        if (i >= floors.length || i < 0) throw new IndexOutOfBoundsException();
        if (curFloor != i) {
            Integer old = new Integer(curFloor);
            curFloor = i;
            firePropertyChange(FLOOR, old, new Integer(curFloor));
        }
    }

    public MapFloor getSelectedFloor() {
        return floors[curFloor];
    }

    public int getSelectedFloorIndex() {
        return curFloor;
    }

    public MapFloor getFloorAt(int index) {
        if (index < 0 || index >= floors.length) throw new IndexOutOfBoundsException();
        return floors[index];
    }

    /**
     * @deprecated use {@link #addNewFloor(java.lang.String)} instead
     */
    public void addNewFloor() {
        addNewFloor("New Floor");
    }

    /**
     * Adds a new floor to the bottom of the current map.
     * @param name the name of the new floor
     */
    public void addNewFloor(String name) {
        MapFloor buf[] = new MapFloor[floors.length + 1];
        System.arraycopy(floors, 0, buf, 0, floors.length);
        buf[floors.length] = new MapFloor(this, name);
        floors = buf;
    }

    /**
     * Deletes the floor at the given index.
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public void deleteFloor(int i) {
        if (i >= floors.length || i < 0) throw new IndexOutOfBoundsException();
        if (floors.length == 1) throw new RuntimeException("Cannot delete the last floor.");
        MapFloor buf[] = new MapFloor[floors.length - 1];
        if (i > 0) System.arraycopy(floors, 0, buf, 0, i);
        if (i < buf.length) System.arraycopy(floors, i + 1, buf, i, buf.length - i);
        floors = buf;
        if (curFloor >= floors.length) curFloor = floors.length - 1;
    }

    /**
     * Moves the floor at the given index to the given index.
     * <p>
     * All floors between the two given index are slid as if
     * the floor was removed from its original index and
     * replaced in the new index.
     * @throws IndexOutOfBoundsException if either index is
     * out of bounds
     * @param from the index of the floor to move
     * @param to the index where the floor should wind up
     */
    public void moveFloor(int from, int to) {
        if (from < 0 || from >= floors.length) throw new IndexOutOfBoundsException("from");
        if (to < 0 || to >= floors.length) throw new IndexOutOfBoundsException("to");
        if (from == to) return;
        MapFloor mover = floors[from];
        if (from > to) {
            for (int i = from; i > to; i--) floors[i] = floors[i - 1];
            floors[to] = mover;
        } else {
            for (int i = from; i < to; i++) floors[i] = floors[i + 1];
            floors[to] = mover;
        }
    }

    /**
     * Gets the current tile set making up the tiles for the map.
     * @return the TileSet that provides tiles for the map
     */
    public TileSet getTileSet() {
        return set;
    }

    /**
     * Requests the map draw itself in the given area using the given graphics
     * device.
     * @param g the graphics device
     * @param mapBounds the boundaries within map space to draw the map - these
     * coordinates are "virtual" within the entire map space - they may be
     * as large as a tile width * the tile height
     */
    public void drawMap(Graphics g, Rectangle mapBounds) {
        floors[curFloor].drawMap((Graphics2D) g, mapBounds);
    }

    /**
     * Adds a new PropertyChangeListener to this object.
     * @param listener the listener to add to this object
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an existing PropertyChangeListener to this object.
     * @param listener the listener to add to this object
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        int i = listeners.indexOf(listener);
        if (i == -1) return;
        listeners.remove(i);
    }

    /**
     * Fires a PropertyChangeEvent to all PropertyChangeListeners.
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (int i = listeners.size() - 1; i >= 0; i--) ((PropertyChangeListener) listeners.get(i)).propertyChange(event);
    }

    /**
     * Reads a saved map from an input stream.
     */
    public static Map loadMap(Application app, TileSetManager manager, java.io.InputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);
        if (!din.readUTF().equals("Mapper")) throw new IOException("Missing magic string.");
        int ver = din.readInt();
        boolean skipObjectSize = false;
        if (ver == 2) skipObjectSize = true; else if (ver != 3) throw new IOException("Bad version number.");
        int w = din.readInt();
        int h = din.readInt();
        int numFloors = din.readInt();
        int curFloor = din.readInt();
        TileSet set = manager.loadTileset(in);
        HashMap hmap = set.loadSetMap(in);
        Map rval = new Map(set, w, h);
        if (curFloor < 0 || curFloor >= numFloors) curFloor = 0;
        rval.curFloor = curFloor;
        rval.floors = new MapFloor[numFloors];
        for (int i = 0; i < numFloors; i++) rval.floors[i] = MapFloor.loadMapFloor(din, app, set, w, h, hmap, skipObjectSize);
        return rval;
    }

    /**
     * Saves a map to an output stream.
     */
    public void saveMap(TileSetManager manager, java.io.OutputStream out) throws IOException {
        DataOutputStream dout = new DataOutputStream(out);
        dout.writeUTF("Mapper");
        dout.writeInt(3);
        dout.writeInt(width);
        dout.writeInt(height);
        dout.writeInt(floors.length);
        dout.writeInt(curFloor);
        manager.saveTileset(out, set);
        HashMap hmap = set.saveSetMap(out);
        for (int i = 0; i < floors.length; i++) floors[i].saveMapFloor(dout, hmap);
    }
}
