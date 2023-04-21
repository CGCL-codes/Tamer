package simulator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import javax.vecmath.*;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

class JointForce {

    Vector3d f1;

    Vector3d t1;

    Vector3d f2;

    Vector3d t2;

    JointForce() {
        f1 = new Vector3d();
        t1 = new Vector3d();
        f2 = new Vector3d();
        t2 = new Vector3d();
    }
}

/**
 * A constraint between two connected bodies.
 * 
 * @author Yung-Chang Tan
 *
 */
public abstract class Joint {

    protected static final int REVERSE = 2;

    public int m, nub;

    public double fps, erp;

    public DoubleMatrix2D J1l, J1a, J2l, J2a;

    public DoubleMatrix1D c, cfm;

    public DoubleMatrix1D lo, hi;

    public int[] findex;

    public int tag;

    int flags;

    Body[] body = new Body[2];

    DoubleMatrix1D lambda = new DenseDoubleMatrix1D(6);

    World world;

    boolean print;

    JointForce cforce;

    public Joint(World w) {
        world = w;
        world.addJoint(this);
        print = false;
    }

    public void setPrint(boolean p) {
        print = p;
        if (print) cforce = new JointForce();
    }

    public void attach(Body b1, Body b2) {
        if (b1 == null) {
            b1 = b2;
            b2 = null;
            flags |= REVERSE;
        } else {
            flags &= (~REVERSE);
        }
        body[0] = b1;
        body[1] = b2;
        if (b1 != null) {
            b1.addJointNode(this, b2);
        }
        if (b2 != null) {
            b2.addJointNode(this, b1);
        }
    }

    abstract void getInfo1();

    abstract void getInfo2();

    abstract void printState(PrintWriter out);

    protected void printState(RandomAccessFile out) throws IOException {
        out.writeDouble(cforce.f1.x);
        out.writeDouble(cforce.f1.y);
        out.writeDouble(cforce.f1.z);
        out.writeDouble(cforce.t1.x);
        out.writeDouble(cforce.t1.y);
        out.writeDouble(cforce.t1.z);
        out.writeDouble(cforce.f2.x);
        out.writeDouble(cforce.f2.y);
        out.writeDouble(cforce.f2.z);
        out.writeDouble(cforce.t2.x);
        out.writeDouble(cforce.t2.y);
        out.writeDouble(cforce.t2.z);
    }

    protected void planeSpace(Vector3d n, Vector3d p, Vector3d q) {
        assert n != null && p != null && q != null;
        if (Math.abs(n.z) > 1 / Math.sqrt(2)) {
            double a = n.y * n.y + n.z * n.z;
            double k = 1 / Math.sqrt(a);
            p.x = 0;
            p.y = -n.z * k;
            p.z = n.y * k;
            q.x = a * k;
            q.y = -n.x * p.z;
            q.z = n.x * p.y;
        } else {
            double a = n.x * n.x + n.y * n.y;
            double k = 1 / Math.sqrt(a);
            p.x = -n.y * k;
            p.y = n.x * k;
            p.z = 0;
            q.x = -n.z * p.y;
            q.y = n.z * p.x;
            q.z = a * k;
        }
    }

    /** 
      compute axes relative to bodies. either axis1 or axis2 can be null.
  */
    protected void setAxes(double x, double y, double z, Vector3d axis1, Vector3d axis2) {
        if (body[0] != null) {
            Vector3d q = new Vector3d(x, y, z);
            q.normalize();
            if (axis1 != null) {
                Matrix3d R = new Matrix3d();
                R.transpose(body[0].R);
                R.transform(q, axis1);
            }
            if (axis2 != null) {
                if (body[1] != null) {
                    Matrix3d R = new Matrix3d();
                    R.transpose(body[1].R);
                    R.transform(q, axis2);
                } else axis2.set(x, y, z);
            }
        }
    }

    protected void getAxis(Vector3d result, Vector3d axis1) {
        if (body[0] != null) body[0].R.transform(axis1, result);
    }

    protected final void fillJ(Vector3d J1l, Vector3d J1a, Vector3d J2l, Vector3d J2a, int r) {
        double[] tmp = new double[3];
        J1l.get(tmp);
        this.J1l.viewRow(r).assign(tmp);
        J1a.get(tmp);
        this.J1a.viewRow(r).assign(tmp);
        if (this.J2l != null) {
            J2l.get(tmp);
            this.J2l.viewRow(r).assign(tmp);
            J2a.get(tmp);
            this.J2a.viewRow(r).assign(tmp);
        }
    }
}
