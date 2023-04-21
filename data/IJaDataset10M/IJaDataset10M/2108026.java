package com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d;

import javax.media.j3d.DecalGroup;
import javax.media.j3d.SceneGraphObject;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;
import com.sun.j3d.utils.scenegraph.io.retained.SymbolTableData;

public class DecalGroupState extends GroupState {

    /** Creates new BranchGroupState */
    public DecalGroupState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    protected SceneGraphObject createNode() {
        return new DecalGroup();
    }
}
