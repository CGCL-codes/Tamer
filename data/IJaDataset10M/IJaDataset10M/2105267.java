package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import javax.media.ding3d.DecalGroup;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public class DecalGroupState extends GroupState {

    /** Creates new BranchGroupState */
    public DecalGroupState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    protected SceneGraphObject createNode() {
        return new DecalGroup();
    }
}
