package org.voidness.squaretilesframework;

import java.util.Vector;

/** A "tileset" to hold the collection of objects used by a map */
public class ObjectTileSet {

    /** The vector holding the objects */
    private Vector<MapObject> objects = null;

    /**
     * Default constructor
     */
    public ObjectTileSet() {
        objects = new Vector<MapObject>();
    }

    /**
     * Adds a new object to the object set
     * 
     * @param mNewObject The new object to add
     */
    public void addObject(MapObject mNewObject) {
        objects.add(mNewObject);
    }

    /**
     * Returns an element at a given index
     * 
     * @param mIndex The index of the element to return
     * @return Element at mIndex in the vector
     */
    public MapObject elementAt(int mIndex) {
        if (mIndex > -1 && mIndex < objects.size()) return (MapObject) objects.elementAt(mIndex);
        return null;
    }

    /**
     * Returns the number of objects in the object set
     * 
     * @return The number of objects
     */
    public int size() {
        return objects.size();
    }

    /**
     * Removes an element from the object set
     * 
     * @param mIndex The index of the element to remove
     */
    public void removeAt(int mIndex) {
        if (mIndex > -1 && mIndex < objects.size()) objects.removeElementAt(mIndex);
    }

    /**
     * Tries to find the object with a give filename
     * 
     * @param mFileName The filename to search in the vector
     * @return The object's index, if found, or -1 if no object is found
     */
    public int find(String mFileName) {
        for (int i = 0; i < objects.size(); i++) {
            MapObject object = (MapObject) objects.elementAt(i);
            if (object.getFilename().equals(mFileName)) return i;
        }
        return -1;
    }
}
