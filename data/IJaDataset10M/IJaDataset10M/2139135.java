package au.csiro.atnf.rpfits.tools;

/**
 * RPFITS coordinates and baseline number.
 * 
 * Copyright 2010-2011, CSIRO Australia All rights reserved.
 */
public class RP_DataGroup {

    float u;

    float v;

    float w;

    float baseline;

    RP_DataGroup() {
    }

    public float getBaseline() {
        return baseline;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getW() {
        return w;
    }
}
