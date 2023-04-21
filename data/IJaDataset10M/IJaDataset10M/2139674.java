package org.gdbi.util.rec;

import java.util.Vector;
import org.gdbi.api.*;

/**
 * Creates a final data structure with the contents of the 5.5.1
 * PLACE_STRUCTURE.
 */
public class URecPlaceStruct implements GdbiConstants {

    public static final URecPlaceStruct DEFAULT = new URecPlaceStruct("", GdbiContext.TOARRAY);

    public final String plac;

    public final String form;

    public final GdbiContext[] other;

    public URecPlaceStruct(GdbiContext place) {
        this(place.getValue(), place.getContexts());
    }

    private URecPlaceStruct(String placValue, GdbiContext[] contexts) {
        plac = placValue;
        final Vector<GdbiContext> vother = new Vector<GdbiContext>();
        String lForm = null;
        for (int i = 0; i < contexts.length; i++) {
            final String tag = contexts[i].getTag();
            final String val = contexts[i].getValue();
            if (false) ; else if (tag.equals(TAG_FORM)) lForm = val; else vother.add(contexts[i]);
        }
        other = vother.toArray(GdbiContext.TOARRAY);
        form = lForm;
    }
}
