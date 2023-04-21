package com.selcukcihan.android.xface.xface;

import com.selcukcihan.android.xface.xmath.Vector3;

public class RaisedCosInfluenceWaveY extends RaisedCosInfluence {

    public RaisedCosInfluenceWaveY() {
        super();
    }

    public RaisedCosInfluenceWaveY(float weight, int fapID) {
        super(weight, fapID);
    }

    protected float calculateDistance(final Vector3 p1, final Vector3 p2) {
        return (float) Math.abs(p1.y - p2.y);
    }

    public String getTypeName() {
        return this.getClass().getName();
    }
}
