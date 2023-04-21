package com.db4o.config;

import java.util.*;
import com.db4o.*;

/**
 * @exclude
 * @sharpen.ignore
 */
@decaf.Ignore(decaf.Platform.JDK11)
public class TTreeSet implements ObjectConstructor {

    public Object onStore(ObjectContainer con, Object object) {
        return ((TreeSet) object).comparator();
    }

    public void onActivate(ObjectContainer con, Object object, Object members) {
    }

    public Object onInstantiate(ObjectContainer container, Object storedObject) {
        if (storedObject instanceof Comparator) {
            return new TreeSet((Comparator) storedObject);
        }
        return new TreeSet();
    }

    public Class storedClass() {
        return Object.class;
    }
}
