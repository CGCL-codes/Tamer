package com.sun.j3d.utils.scenegraph.io.state.javax.media.j3d;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import javax.media.j3d.RotationPathInterpolator;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Quat4f;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;
import com.sun.j3d.utils.scenegraph.io.retained.SymbolTableData;

public class RotationPathInterpolatorState extends PathInterpolatorState {

    private Quat4f[] quats;

    public RotationPathInterpolatorState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    public void writeConstructorParams(DataOutput out) throws IOException {
        super.writeConstructorParams(out);
        quats = new Quat4f[knots.length];
        for (int i = 0; i < quats.length; i++) {
            quats[i] = new Quat4f();
        }
        ((RotationPathInterpolator) node).getQuats(quats);
        for (int i = 0; i < quats.length; i++) {
            control.writeQuat4f(out, quats[i]);
        }
    }

    public void readConstructorParams(DataInput in) throws IOException {
        super.readConstructorParams(in);
        quats = new Quat4f[knots.length];
        for (int i = 0; i < quats.length; i++) {
            quats[i] = control.readQuat4f(in);
        }
    }

    public SceneGraphObject createNode(Class j3dClass) {
        return createNode(j3dClass, new Class[] { javax.media.j3d.Alpha.class, TransformGroup.class, Transform3D.class, knots.getClass(), quats.getClass() }, new Object[] { null, null, new Transform3D(), knots, quats });
    }

    protected javax.media.j3d.SceneGraphObject createNode() {
        return new RotationPathInterpolator(null, null, new Transform3D(), knots, quats);
    }
}
