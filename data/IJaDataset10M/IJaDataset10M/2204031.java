package com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import javax.media.j3d.Font3D;
import javax.media.j3d.Text3D;
import javax.vecmath.Point3f;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;
import com.sun.j3d.utils.scenegraph.io.retained.SymbolTableData;

public class Text3DState extends GeometryState {

    private int font3d;

    private String string;

    public Text3DState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
        if (node != null) {
            font3d = control.getSymbolTable().addReference(((Text3D) node).getFont3D());
        }
    }

    public void writeObject(DataOutput out) throws IOException {
        super.writeObject(out);
        out.writeInt(((Text3D) node).getAlignment());
        out.writeFloat(((Text3D) node).getCharacterSpacing());
        out.writeInt(font3d);
        out.writeInt(((Text3D) node).getPath());
        Point3f pos = new Point3f();
        ((Text3D) node).getPosition(pos);
        control.writePoint3f(out, pos);
        out.writeUTF(((Text3D) node).getString());
    }

    public void readObject(DataInput in) throws IOException {
        super.readObject(in);
        ((Text3D) node).setAlignment(in.readInt());
        ((Text3D) node).setCharacterSpacing(in.readFloat());
        font3d = in.readInt();
        ((Text3D) node).setPath(in.readInt());
        ((Text3D) node).setPosition(control.readPoint3f(in));
        string = in.readUTF();
    }

    /**
     * Called when this component reference count is incremented. Allows this
     * component to update the reference count of any components that it
     * references.
     */
    public void addSubReference() {
        control.getSymbolTable().incNodeComponentRefCount(font3d);
    }

    public void buildGraph() {
        ((Text3D) node).setFont3D(((Font3D) control.getSymbolTable().getJ3dNode(font3d)));
        ((Text3D) node).setString(string);
        super.buildGraph();
    }

    protected javax.media.j3d.SceneGraphObject createNode() {
        return new Text3D();
    }
}
