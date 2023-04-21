package com.hadeslee.audiotag.tag.id3.valuepair;

import com.hadeslee.audiotag.tag.datatype.AbstractIntStringValuePair;

public class InterpolationTypes extends AbstractIntStringValuePair {

    private static InterpolationTypes interpolationTypes;

    public static InterpolationTypes getInstanceOf() {
        if (interpolationTypes == null) {
            interpolationTypes = new InterpolationTypes();
        }
        return interpolationTypes;
    }

    private InterpolationTypes() {
        idToValue.put(0, "Band");
        idToValue.put(1, "Linear");
        createMaps();
    }
}
