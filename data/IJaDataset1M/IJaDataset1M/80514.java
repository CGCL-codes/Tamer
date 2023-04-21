package com.enerjy.analyzer.java.rules.testfiles.T0238;

public class PTest02 implements java.lang.Cloneable {

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class PTest02_1 implements java.lang.Cloneable {

    int x1 = 1;

    int x2 = 2;

    public Object clone() throws CloneNotSupportedException {
        PTest02_1 rval = (PTest02_1) super.clone();
        rval.x1 = 0;
        rval.x2 = 0;
        return rval;
    }
}

class FTest02_1 implements java.lang.Cloneable {

    int x1 = 1;

    int x2 = 2;

    public Object clone() throws CloneNotSupportedException {
        FTest02_1 rval = (FTest02_1) super.clone();
        rval.x1 = 0;
        rval.x2 = 0;
        Object rval1 = null;
        rval1 = super.clone();
        return rval1;
    }
}

class PTest02_2 implements java.lang.Cloneable {

    int x1 = 1;

    int x2 = 2;

    public Object clone() throws CloneNotSupportedException {
        PTest02_2[] array = new PTest02_2[2];
        array[0] = (PTest02_2) super.clone();
        array[0].x1 = 0;
        array[0].x2 = 0;
        array[1] = (PTest02_2) super.clone();
        array[1].x1 = 0;
        array[1].x2 = 0;
        return array[0];
    }
}

class PTest02_3 implements java.lang.Cloneable {

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
