package org.legzo.picreporter.model;

public class FocalLength extends Parameter implements Comparable<Parameter> {

    public FocalLength(float f) {
        super(f);
    }

    @Override
    public String toString() {
        return (int) value + "mm";
    }
}
