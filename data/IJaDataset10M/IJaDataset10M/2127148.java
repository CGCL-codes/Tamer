package de.tum.in.botl.model.impl;

import de.tum.in.botl.metamodel.Attribute;
import de.tum.in.botl.model.ObjectAttribute;

/**
 * <p>Title: BOTL</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ObjectAttributeImpl implements ObjectAttribute {

    private String value;

    private Attribute attType;

    protected ObjectAttributeImpl(Attribute att) {
        this.setAttType(att);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String val) {
        value = val;
    }

    public Attribute getAttType() {
        return attType;
    }

    private void setAttType(Attribute att) {
        attType = att;
    }
}
