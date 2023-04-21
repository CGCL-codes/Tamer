package com.induslogic.uddi;

import java.util.*;

/**
 * IdentifierBag.java
 *
 *
 * Created: Wed Jul 04 13:32:18 2001
 *
 * @author <a href="mailto: "Satyendra Sharma</a>
 * @version
 */
public class SharedRelationships extends UddiObject {

    public SharedRelationships() {
        super(UddiTags.SHARED_RELATIONSHIPS);
    }

    public SharedRelationships(UddiObject o) {
        doc = o.doc;
    }

    public void setKeyedReference(KeyedReference keyRef) {
        addElement(keyRef);
    }

    public Enumeration getKeyedReference() {
        Vector v = new Vector();
        Enumeration e = getElementsNamed(UddiTags.KEYED_REFERENCE);
        while (e.hasMoreElements()) {
            KeyedReference kr = new KeyedReference((UddiObject) e.nextElement());
            v.addElement(kr);
        }
        return v.elements();
    }
}
