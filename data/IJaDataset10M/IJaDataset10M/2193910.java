package com.risertech.xdav.internal.webdav.xml.element;

import com.risertech.xdav.internal.webdav.xml.WebDAVConverter;
import com.risertech.xdav.webdav.element.PropName;

/**
 * <!ELEMENT propname EMPTY >
 * 
 * @author phil
 */
public class PropNameConverter extends WebDAVConverter {

    public PropNameConverter() {
        super(PropName.class, "propname");
    }
}
