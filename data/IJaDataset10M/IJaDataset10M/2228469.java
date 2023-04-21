package com.mockturtlesolutions.snifflib.datatypes;

import java.io.*;
import java.util.Vector;

/**
GenMatrix is an abstract matrix class which can hold any kind of object.
*/
public abstract class GenMatrix {

    public int[] Size;

    public MatrixSetterGetter setget;

    public Object EmptyValue;

    public GenMatrix() {
        this.Size = new int[2];
        this.Size[0] = 0;
        this.Size[1] = 0;
        this.setget = new DenseSetterGetter();
        this.EmptyValue = null;
    }

    public int getN() {
        int out = 1;
        for (int k = 0; k < this.Size.length; k++) {
            out *= this.Size[k];
        }
        return (out);
    }

    public boolean isEmpty() {
        boolean out = true;
        if (this.getN() != 0) {
            out = false;
        }
        return (out);
    }

    public abstract boolean isEqual(GenMatrix X);
}
