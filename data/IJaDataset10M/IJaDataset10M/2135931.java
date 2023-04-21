package com.risertech.xdav.webdav.element;

import com.risertech.xdav.internal.webdav.IPropertyUpdate;
import com.risertech.xdav.internal.webdav.WebDAVElement;

/**
 * 12.13.1 remove XML element
 * 
 * Name:       remove
 * Namespace:  DAV:
 * Purpose:    Lists the DAV properties to be removed from a resource.
 * Description: Remove instructs that the properties specified in prop
 * should be removed.  Specifying the removal of a property that does
 * not exist is not an error.  All the XML elements in a prop XML
 * element inside of a remove XML element MUST be empty, as only the
 * names of properties to be removed are required.
 * 
 * <!ELEMENT remove (prop) >
 * 
 * @author phil
 */
public class Remove extends WebDAVElement implements IPropertyUpdate {

    private final Prop prop;

    public Remove(Prop prop) {
        super("remove");
        this.prop = prop;
    }

    public Prop getProp() {
        return prop;
    }
}
