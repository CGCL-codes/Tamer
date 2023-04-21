package org.webthree.dictionary.base;

public class FloatType extends AbstractType {

    private Float value;

    public Class getTypeClass() {
        return Float.class;
    }

    public String getDescriptiveName() {
        return "float";
    }
}
