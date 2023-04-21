package de.grogra.math;

import javax.vecmath.Matrix3d;

public interface Transform2D extends de.grogra.persistence.Manageable {

    void transform(Matrix3d in, Matrix3d out);
}
