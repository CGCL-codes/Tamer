package org.apache.fop.fo.properties;

import java.util.Hashtable;
import org.apache.fop.datatypes.*;
import org.apache.fop.fo.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class BorderColorMaker extends ListProperty.Maker {

    public static Property.Maker maker(String propName) {
        return new BorderColorMaker(propName);
    }

    protected BorderColorMaker(String name) {
        super(name);
    }

    public boolean isInherited() {
        return false;
    }
}
