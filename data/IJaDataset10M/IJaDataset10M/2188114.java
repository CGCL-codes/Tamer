package com.cosylab.vdct.model.property;

/**
 *
 * @author jgolob
 */
public class LongArrayProperty extends BaseProperty {

    public LongArrayProperty(String name, boolean settable) {
        super(name, Long[].class, settable);
    }
}
