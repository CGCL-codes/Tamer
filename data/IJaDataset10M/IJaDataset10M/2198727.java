package sonia.mapper;

import java.awt.Color;
import java.awt.Shape;
import java.util.Set;
import java.util.Vector;

public abstract class Shapemapper extends Object {

    protected Object attributeKey = null;

    /**
	 * get the color this value is mapped to 
	 * @param value
	 * @return
	 */
    public abstract Shape getShapeFor(Object value);

    /**
	 * sets the mapping for a specific value
	 * @param value
	 * @param shape
	 */
    public abstract void mapShape(Object value, Shape shape);

    /**
	 * takes a set of objects and creates a default mapping of values to colors
	 * @param values
	 */
    public abstract void createMapping(Set<Object> values);

    /**
	 * returns the set of values that have mappings.  may be an empty set if the mapper uses
	 * some function of the data to compute the shape.  
	 * @return
	 */
    public abstract Set getValues();

    /**
	 * sets the key to be used when looking up data from node attributes
	 * @param key
	 */
    public void setKey(Object key) {
        attributeKey = key;
    }

    /**
	 * gets the object used as a key for looking up data elements of node attributes that are mapped to shapes
	 * @return
	 */
    public Object getKey() {
        return attributeKey;
    }

    public abstract String getMapperName();
}
