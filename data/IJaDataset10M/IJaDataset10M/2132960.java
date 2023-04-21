package com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;
import com.sun.j3d.utils.scenegraph.io.retained.SymbolTableData;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Switch;

public class SwitchState extends GroupState {

    public SwitchState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    public void writeObject(DataOutput out) throws IOException {
        super.writeObject(out);
        Switch attr = (Switch) node;
        control.writeSerializedData(out, attr.getChildMask());
        out.writeInt(attr.getWhichChild());
    }

    public void readObject(DataInput in) throws IOException {
        super.readObject(in);
        Switch attr = (Switch) node;
        attr.setChildMask((java.util.BitSet) control.readSerializedData(in));
        attr.setWhichChild(in.readInt());
    }

    protected javax.media.j3d.SceneGraphObject createNode() {
        return new Switch();
    }
}
