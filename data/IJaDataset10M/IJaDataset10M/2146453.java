package org.xmldb.api.reference.modules;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.sdk.modules.*;

/**
 * Provides access to XML resources stored in the file system. This is a default
 * implementation derived from SimpleXMLResource that is part of the SDK.
 */
public class XMLResourceImpl extends SimpleXMLResource {

    public XMLResourceImpl(Collection parent, String id, String documentID) {
        this(parent, id, documentID, null);
    }

    public XMLResourceImpl(Collection parent, String id, String documentID, String content) {
        super(parent, id, documentID, content);
    }
}
