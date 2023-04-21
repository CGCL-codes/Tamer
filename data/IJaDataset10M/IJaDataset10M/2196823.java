package de.grogra.imp3d.glsl.material.channel;

import javax.vecmath.Matrix4d;
import de.grogra.imp3d.glsl.utility.ShaderConfiguration;
import de.grogra.math.TMatrix4d;
import de.grogra.math.TVector3d;
import de.grogra.math.Transform3D;

public class GLSLTMatrix4d extends GLSLTransform3D {

    @Override
    public Class instanceFor() {
        return TMatrix4d.class;
    }

    String matrixToGLSLMatrix(Matrix4d m) {
        System.out.println(m);
        return "mat4(" + m.m00 + "," + m.m10 + "," + m.m20 + "," + m.m30 + "," + m.m01 + "," + m.m11 + "," + m.m21 + "," + m.m31 + "," + m.m02 + "," + m.m12 + "," + m.m22 + "," + m.m32 + "," + m.m03 + "," + m.m13 + "," + m.m23 + "," + m.m33 + ")";
    }

    @Override
    public Result process(Result input, Transform3D fkt, ShaderConfiguration sc) {
        assert (fkt instanceof TMatrix4d);
        TMatrix4d tm4d = (TMatrix4d) fkt;
        return new Result("(" + matrixToGLSLMatrix(tm4d) + "*" + input.convert(Result.ET_VEC4) + ").xyz", Result.ET_VEC3);
    }
}
