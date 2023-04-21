package com.risertech.xdav.internal.webdav.xml.element;

import com.risertech.xdav.internal.webdav.xml.WebDAVConverter;
import com.risertech.xdav.webdav.element.AllProp;

/**
 * <!ELEMENT allprop EMPTY >
 * 
 * @author phil
 */
public class AllPropConverter extends WebDAVConverter {

    public AllPropConverter() {
        super(AllProp.class, "allprop");
    }
}
