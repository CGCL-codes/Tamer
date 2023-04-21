package com.tms.webservices.applications.xtvd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.tms.webservices.applications.datatypes.AbstractDataType;
import com.tms.webservices.applications.datatypes.LineupTypes;

/**
 * A <code>bean</code> that represents a lineup record.
 *
 * @author Rakesh Vidyadharan 19<sup><small>th</small></sup> February, 2004
 *
 * <p>Copyright 2004, Tribune Media Services</p>
 *
 * $Id: Lineup.java,v 1.1.1.1 2005/07/19 04:28:19 shawndr Exp $
 */
public class Lineup extends AbstractDataType {

    /**
   * A variable that represents the <code>id</code> value associated
   * with a lineup.  Since the <code>id</code> attribute is a 
   * <code>required</code> attribute for the <code>lineup</code>
   * element, the <code>constructor's</code> have been modified to 
   * include this value.
   *
   * @since ddclient version 1.3
   */
    private String id = null;

    /**
   * A variable that represents the <code>name</code> value associated 
   * with a lineup.
   */
    private String name = null;

    /**
   * A variable that represents the <code>location</code> value 
   * with a lineup.  Since the <code>id</code> attribute is a 
   * <code>required</code> attribute for the <code>lineup</code>
   * element, the <code>constructor's</code> have been modified to 
   * include this value.
   *
   * @since ddclient version 1.3
   */
    private String location = null;

    /**
   * A variable that represents the <code>type</code> value associated 
   * with a lineup.
   */
    private LineupTypes type = null;

    /**
   * A variable that represents the <code>device</code> value associated
   * with a lineup.
   */
    private String device = null;

    /**
   * A variable that represents the <code>postalCode</code> value 
   * associated with a lineup.
   */
    private String postalCode = null;

    /**
   * A <code>Collection</code> of {@link Map} instances that are
   * associated with the lineup.
   */
    private Collection map = null;

    /**
   * Default constructor.  Not particularly useful, except if you
   * wish to create an instance of the class, that will be re-used
   * to associate with different lineup records.
   */
    public Lineup() {
        super();
        map = new ArrayList();
    }

    /**
   * Create a new instance of the class with the specified values of
   * {@link #name} and {@link #type}.
   *
   * @since ddclient version 1.3
   *
   * @param String id - The {@link #id} value to set.
   * @param String name - The {@link #name} value to set.
   * @param String location - The {@link #location} value to set.
   * @param LineupTypes type - The {@link #type} value to set.
   */
    public Lineup(String id, String name, String location, LineupTypes type) {
        this();
        setId(id);
        setName(name);
        setLocation(location);
        setType(type);
    }

    /**
   * Create a new instance of the class, and initiliase the instance
   * variables with the specified values.
   *
   * @since ddclient version 1.3
   *
   * @param String id - The {@link #id} value to set.
   * @param String name - The {@link #name} value to set.
   * @param String location - The {@link #location} value to set.
   * @param LineupTypes type - The {@link #type} value to set.
   * @param String device - The {@link #device} value to set.
   * @param String postalCode - The {@link #postalCode} value to set.
   */
    public Lineup(String id, String name, String location, LineupTypes type, String device, String postalCode) {
        this(id, name, location, type);
        setDevice(device);
        setPostalCode(postalCode);
    }

    /**
   * Create a new instance of the class, and initiliase the instance
   * variables with the specified values.
   *
   * @since ddclient version 1.3
   *
   * @param String id - The {@link #id} value to set.
   * @param String name - The {@link #name} value to set.
   * @param String location - The {@link #location} value to set.
   * @param LineupTypes type - The {@link #type} value to set.
   * @param String device - The {@link #device} value to set.
   * @param String postalCode - The {@link #postalCode} value to set.
   * @param Collection map - The {@link #map} reference to set.
   */
    public Lineup(String id, String name, String location, LineupTypes type, String device, String postalCode, Collection map) {
        this(id, name, location, type, device, postalCode);
        setMap(map);
    }

    /**
   * Reset all the instance variables to empty values.
   */
    public void reset() {
        setId(null);
        setName(null);
        setType(null);
        setDevice(null);
        setPostalCode(null);
        if (map != null) map.clear();
    }

    /**
   * Over-ridden implementation.  Return an <code>XML representation
   * </code> of the class fields in the same format as in the original
   * <code>XTVD document</code>.
   *
   * @return String - The XML representation of the lineup record.
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer(4192);
        buffer.append("<lineup id='");
        buffer.append(id).append("' name='");
        replaceSpecialCharacters(name, buffer);
        buffer.append("' location='");
        replaceSpecialCharacters(location, buffer);
        buffer.append("' type='").append(type.toString());
        if (device != null && device.length() > 0) {
            buffer.append("' device='").append(device);
        }
        if (postalCode != null && postalCode.length() > 0) {
            buffer.append("' postalCode='").append(postalCode);
        }
        buffer.append("'>");
        buffer.append(AbstractDataType.END_OF_LINE);
        for (Iterator iterator = map.iterator(); iterator.hasNext(); ) {
            Map map = (Map) iterator.next();
            buffer.append(map.toString());
            buffer.append(AbstractDataType.END_OF_LINE);
        }
        buffer.append("</lineup>");
        return buffer.toString();
    }

    /**
   * Returns {@link #id}.
   *
   * @return String - The value/reference of/to id.
   */
    public final String getId() {
        return id;
    }

    /**
   * Set {@link #id}.
   *
   * @param String id - The value to set.
   */
    public final void setId(String id) {
        this.id = id;
    }

    /**
   * Returns {@link #name}.
   *
   * @return String - The value/reference of/to name.
   */
    public final String getName() {
        return name;
    }

    /**
   * Set {@link #name}.
   *
   * @param String name - The value to set.
   */
    public final void setName(String name) {
        this.name = name;
    }

    /**
   * Returns {@link #location}.
   *
   * @return String - The value/reference of/to location.
   */
    public final String getLocation() {
        return location;
    }

    /**
   * Set {@link #location}.
   *
   * @param String location - The value to set.
   */
    public final void setLocation(String location) {
        this.location = location;
    }

    /**
   * Returns {@link #type}.
   *
   * @return LineupTypes - The value/reference of/to type.
   */
    public final LineupTypes getType() {
        return type;
    }

    /**
   * Set {@link #type}.
   *
   * @param LineupTypes type - The value to set.
   */
    public final void setType(LineupTypes type) {
        this.type = type;
    }

    /**
   * Returns {@link #device}.
   *
   * @return String - The value/reference of/to device.
   */
    public final String getDevice() {
        return device;
    }

    /**
   * Set {@link #device}.
   *
   * @param String device - The value to set.
   */
    public final void setDevice(String device) {
        this.device = device;
    }

    /**
   * Returns {@link #postalCode}.
   *
   * @return String - The value/reference of/to postalCode.
   */
    public final String getPostalCode() {
        return postalCode;
    }

    /**
   * Set {@link #postalCode}.
   *
   * @param String postalCode - The value to set.
   */
    public final void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
   * Returns {@link #map}.
   *
   * @return Collection - The value/reference of/to map.
   */
    public final Collection getMap() {
        return map;
    }

    /**
   * Set {@link #map}.
   *
   * @param Collection map - The value to set.
   */
    public final void setMap(Collection map) {
        this.map = map;
    }

    /**
   * Add the specified instance of {@link Map} to the {@link #map}
   * collection.
   *
   * @param Map map - The map that is to be added to the collection.
   */
    public final void addMap(Map map) {
        this.map.add(map);
    }
}
