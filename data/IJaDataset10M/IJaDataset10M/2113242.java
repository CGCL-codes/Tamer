package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import javax.media.ding3d.Billboard;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.TransformGroup;
import javax.media.ding3d.vecmath.Point3f;
import javax.media.ding3d.vecmath.Vector3f;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public class BillboardState extends BehaviorState {

    private int target;

    public BillboardState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
        if (node != null) target = control.getSymbolTable().addReference(((Billboard) node).getTarget());
    }

    public void writeObject(DataOutput out) throws IOException {
        super.writeObject(out);
        out.writeInt(((Billboard) node).getAlignmentMode());
        Vector3f vec = new Vector3f();
        ((Billboard) node).getAlignmentAxis(vec);
        Point3f point = new Point3f();
        ((Billboard) node).getRotationPoint(point);
        control.writeVector3f(out, vec);
        control.writePoint3f(out, point);
        out.writeInt(target);
    }

    public void readObject(DataInput in) throws IOException {
        super.readObject(in);
        ((Billboard) node).setAlignmentMode(in.readInt());
        ((Billboard) node).setAlignmentAxis(control.readVector3f(in));
        ((Billboard) node).setRotationPoint(control.readPoint3f(in));
        target = in.readInt();
    }

    public void buildGraph() {
        ((Billboard) node).setTarget((TransformGroup) control.getSymbolTable().getDing3dNode(target));
        super.buildGraph();
    }

    protected SceneGraphObject createNode() {
        return new Billboard();
    }
}
