package org.apache.webdav.lib.properties;

import org.apache.webdav.lib.ResponseEntity;
import org.w3c.dom.Element;

/**
 * <code>DAV:creationdate</code> property
 */
public class CreationDateProperty extends DateProperty {

    public static final String TAG_NAME = "creationdate";

    public CreationDateProperty(ResponseEntity response, Element element) {
        super(response, element);
    }
}
