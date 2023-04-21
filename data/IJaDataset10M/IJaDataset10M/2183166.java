package com.jme.scene.state.lwjgl.records;

import com.jme.scene.state.StateRecord;
import com.jme.scene.state.CullState.PolygonWind;

public class CullStateRecord extends StateRecord {

    public boolean enabled = false;

    public int face = -1;

    public PolygonWind windOrder = null;

    @Override
    public void invalidate() {
        super.invalidate();
        enabled = false;
        face = -1;
        windOrder = null;
    }
}
