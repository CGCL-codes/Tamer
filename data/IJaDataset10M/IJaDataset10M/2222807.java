package org.nakedobjects.noa.util;

import org.nakedobjects.noa.adapter.NakedObject;

public class NakedObjectUtil {

    private NakedObjectUtil() {
    }

    public static boolean exists(final NakedObject nakedObject) {
        return nakedObject != null && nakedObject.getObject() != null;
    }

    public static boolean wrappedEqual(final NakedObject nakedObject1, final NakedObject nakedObject2) {
        final boolean defined1 = exists(nakedObject1);
        final boolean defined2 = exists(nakedObject2);
        if (defined1 && !defined2) {
            return false;
        }
        if (!defined1 && defined2) {
            return false;
        }
        if (!defined1 && !defined2) {
            return true;
        }
        return nakedObject1.getObject().equals(nakedObject2.getObject());
    }

    public static Object unwrap(final NakedObject nakedObject) {
        return nakedObject != null ? nakedObject.getObject() : null;
    }

    public static Object[] unwrap(final NakedObject[] nakedObjects) {
        if (nakedObjects == null) {
            return null;
        }
        final Object[] unwrappedObjects = new Object[nakedObjects.length];
        int i = 0;
        for (final NakedObject nakedObject : nakedObjects) {
            unwrappedObjects[i++] = unwrap(nakedObject);
        }
        return unwrappedObjects;
    }

    public static String titleString(final NakedObject nakedObject) {
        return nakedObject != null ? nakedObject.titleString() : "";
    }
}
