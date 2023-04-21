package com.android1.amarena2d.nodes.behavior;

import com.android1.amarena2d.nodes.behavior.delegates.SyncDelegate;

public interface Syncable extends HasXY, HasSize {

    public SyncDelegate sync();
}
