package com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;
import com.sun.j3d.utils.scenegraph.io.retained.SymbolTableData;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

public class TransparencyAttributesState extends NodeComponentState {

    public TransparencyAttributesState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    public void writeObject(DataOutput out) throws IOException {
        super.writeObject(out);
        TransparencyAttributes attr = (TransparencyAttributes) node;
        out.writeInt(attr.getDstBlendFunction());
        out.writeInt(attr.getSrcBlendFunction());
        out.writeFloat(attr.getTransparency());
        out.writeInt(attr.getTransparencyMode());
    }

    public void readObject(DataInput in) throws IOException {
        super.readObject(in);
        TransparencyAttributes attr = (TransparencyAttributes) node;
        attr.setDstBlendFunction(in.readInt());
        attr.setSrcBlendFunction(in.readInt());
        attr.setTransparency(in.readFloat());
        attr.setTransparencyMode(in.readInt());
    }

    protected javax.media.j3d.SceneGraphObject createNode() {
        return new TransparencyAttributes();
    }
}
