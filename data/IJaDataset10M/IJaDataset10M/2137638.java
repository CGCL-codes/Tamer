package de.toolforge.googlechartwrapper.style;

import java.util.List;

/**
 * The interface for chart types which supports shape markers.
 * 
 * @author steffan
 *
 */
public interface IShapeMarkable {

    /**
	 * Adds the new ShapeMArker object to the lsit.
	 * 
	 * @param shapeMarker
	 */
    public void addShapeMarker(ShapeMarker shapeMarker);

    /**
	 * Returns a unmodifiable list of ShapeMarkers.
	 * 
	 * @return list of ShapeMarkers, can be empty
	 */
    public List<ShapeMarker> getShapeMarkers();

    /**
	 * Removes a ShapeMaker at the given index.
	 * 
	 * @param index
	 * 
	 * @return the removed ShapeMarker
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
    public ShapeMarker removeShapeMarker(int index);

    /**
	 * Removes a given ShapeMaker object.
	 * 
	 * @param sm the object to remove
	 * 
	 * @return true if success
	 */
    public boolean removeShapeMarker(ShapeMarker sm);

    /**
	 * Removes all ShapeAMrkers in the list.
	 * 
	 */
    public void removeAllShapeMarkers();
}
