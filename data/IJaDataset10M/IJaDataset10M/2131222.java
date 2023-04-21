package com.jme.scene.state.jogl.records;

import java.nio.DoubleBuffer;
import java.util.Arrays;
import com.jme.scene.state.ClipState;
import com.jme.scene.state.StateRecord;
import com.jme.util.geom.BufferUtils;

public class ClipStateRecord extends StateRecord {

    public boolean[] planeEnabled = new boolean[ClipState.MAX_CLIP_PLANES];

    public DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);

    @Override
    public void invalidate() {
        super.invalidate();
        Arrays.fill(planeEnabled, false);
    }
}
