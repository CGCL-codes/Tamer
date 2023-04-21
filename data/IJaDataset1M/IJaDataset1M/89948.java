package javax.media.ding3d;

import javax.media.ding3d.vecmath.*;
import java.util.ArrayList;

class OrientedShape3DRetained extends Shape3DRetained {

    static final int ALIGNMENT_CHANGED = LAST_DEFINED_BIT << 1;

    static final int AXIS_CHANGED = LAST_DEFINED_BIT << 2;

    static final int ROTATION_CHANGED = LAST_DEFINED_BIT << 3;

    static final int CONSTANT_SCALE_CHANGED = LAST_DEFINED_BIT << 4;

    static final int SCALE_FACTOR_CHANGED = LAST_DEFINED_BIT << 5;

    int mode = OrientedShape3D.ROTATE_ABOUT_AXIS;

    Vector3f axis = new Vector3f(0.0f, 1.0f, 0.0f);

    Point3f rotationPoint = new Point3f(0.0f, 0.0f, 1.0f);

    private Vector3d nAxis = new Vector3d(0.0, 1.0, 0.0);

    private Point3d viewPosition = new Point3d();

    private Point3d yUpPoint = new Point3d();

    private Vector3d eyeVec = new Vector3d();

    private Vector3d yUp = new Vector3d();

    private Vector3d zAxis = new Vector3d();

    private Vector3d yAxis = new Vector3d();

    private Vector3d vector = new Vector3d();

    private AxisAngle4d aa = new AxisAngle4d();

    private Transform3D xform = new Transform3D();

    private Transform3D zRotate = new Transform3D();

    boolean constantScale = false;

    double scaleFactor = 1.0;

    private Transform3D left_xform = new Transform3D();

    private Transform3D right_xform = new Transform3D();

    Transform3D scaleXform = new Transform3D();

    private Vector4d im_vec[] = { new Vector4d(), new Vector4d() };

    private Vector4d lvec = new Vector4d();

    boolean orientedTransformDirty = true;

    Transform3D[] orientedTransforms = new Transform3D[1];

    static final double EPSILON = 1.0e-6;

    /**
     * Constructs a OrientedShape3D node with default parameters.
     * The default values are as follows:
     * <ul>
     * alignment mode : ROTATE_ABOUT_AXIS<br>
     * alignment axis : Y-axis (0,1,0)<br>
     * rotation point : (0,0,1)<br>
     *</ul>
     */
    public OrientedShape3DRetained() {
        super();
        this.nodeType = NodeRetained.ORIENTEDSHAPE3D;
    }

    void initAlignmentMode(int mode) {
        this.mode = mode;
    }

    /**
     * Sets the alignment mode.
     * @param mode one of: ROTATE_ABOUT_AXIS or ROTATE_ABOUT_POINT
     */
    void setAlignmentMode(int mode) {
        if (this.mode != mode) {
            initAlignmentMode(mode);
            sendChangedMessage(ALIGNMENT_CHANGED, new Integer(mode));
        }
    }

    /**
     * Retrieves the alignment mode.
     * @return one of: ROTATE_ABOUT_AXIS or ROTATE_ABOUT_POINT
     */
    int getAlignmentMode() {
        return (mode);
    }

    void initAlignmentAxis(Vector3f axis) {
        initAlignmentAxis(axis.x, axis.y, axis.z);
    }

    void initAlignmentAxis(float x, float y, float z) {
        this.axis.set(x, y, z);
        double invMag;
        invMag = 1.0 / Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        nAxis.x = (double) axis.x * invMag;
        nAxis.y = (double) axis.y * invMag;
        nAxis.z = (double) axis.z * invMag;
    }

    /**
     * Sets the new alignment axis.  This is the ray about which this
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_AXIS.
     * @param axis the new alignment axis
     */
    void setAlignmentAxis(Vector3f axis) {
        setAlignmentAxis(axis.x, axis.y, axis.z);
    }

    /**
     * Sets the new alignment axis.  This is the ray about which this
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_AXIS.
     * @param x the x component of the alignment axis
     * @param y the y component of the alignment axis
     * @param z the z component of the alignment axis
     */
    void setAlignmentAxis(float x, float y, float z) {
        initAlignmentAxis(x, y, z);
        if (mode == OrientedShape3D.ROTATE_ABOUT_AXIS) {
            sendChangedMessage(AXIS_CHANGED, new Vector3f(x, y, z));
        }
    }

    /**
     * Retrieves the alignment axis of this OrientedShape3D node,
     * and copies it into the specified vector.
     * @param axis the vector that will contain the alignment axis
     */
    void getAlignmentAxis(Vector3f axis) {
        axis.set(this.axis);
    }

    void initRotationPoint(Point3f point) {
        rotationPoint.set(point);
    }

    void initRotationPoint(float x, float y, float z) {
        rotationPoint.set(x, y, z);
    }

    /**
     * Sets the new rotation point.  This is the point about which the
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_POINT.
     * @param point the new rotation point
     */
    void setRotationPoint(Point3f point) {
        setRotationPoint(point.x, point.y, point.z);
    }

    /**
     * Sets the new rotation point.  This is the point about which the
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_POINT.
     * @param x the x component of the rotation point
     * @param y the y component of the rotation point
     * @param z the z component of the rotation point
     */
    void setRotationPoint(float x, float y, float z) {
        initRotationPoint(x, y, z);
        if (mode == OrientedShape3D.ROTATE_ABOUT_POINT) {
            sendChangedMessage(ROTATION_CHANGED, new Point3f(x, y, z));
        }
    }

    /**
     * Retrieves the rotation point of this OrientedShape3D node,
     * and copies it into the specified vector.
     * @param axis the point that will contain the rotation point
     */
    void getRotationPoint(Point3f point) {
        point.set(rotationPoint);
    }

    void setConstantScaleEnable(boolean enable) {
        if (constantScale != enable) {
            initConstantScaleEnable(enable);
            sendChangedMessage(CONSTANT_SCALE_CHANGED, new Boolean(enable));
        }
    }

    boolean getConstantScaleEnable() {
        return constantScale;
    }

    void initConstantScaleEnable(boolean cons_scale) {
        constantScale = cons_scale;
    }

    void setScale(double scale) {
        initScale(scale);
        if (constantScale) sendChangedMessage(SCALE_FACTOR_CHANGED, new Double(scale));
    }

    void initScale(double scale) {
        scaleFactor = scale;
    }

    double getScale() {
        return scaleFactor;
    }

    void sendChangedMessage(int component, Object attr) {
        Ding3dMessage changeMessage = new Ding3dMessage();
        changeMessage.type = Ding3dMessage.ORIENTEDSHAPE3D_CHANGED;
        changeMessage.threads = targetThreads;
        changeMessage.universe = universe;
        changeMessage.args[0] = getGeomAtomsArray(mirrorShape3D);
        changeMessage.args[1] = new Integer(component);
        changeMessage.args[2] = attr;
        OrientedShape3DRetained[] o3dArr = new OrientedShape3DRetained[mirrorShape3D.size()];
        mirrorShape3D.toArray(o3dArr);
        changeMessage.args[3] = o3dArr;
        changeMessage.args[4] = this;
        VirtualUniverse.mc.processMessage(changeMessage);
    }

    void updateImmediateMirrorObject(Object[] args) {
        int component = ((Integer) args[1]).intValue();
        if ((component & (ALIGNMENT_CHANGED | AXIS_CHANGED | ROTATION_CHANGED | CONSTANT_SCALE_CHANGED | SCALE_FACTOR_CHANGED)) != 0) {
            OrientedShape3DRetained[] msArr = (OrientedShape3DRetained[]) args[3];
            Object obj = args[2];
            if ((component & ALIGNMENT_CHANGED) != 0) {
                int mode = ((Integer) obj).intValue();
                for (int i = 0; i < msArr.length; i++) {
                    msArr[i].initAlignmentMode(mode);
                }
            } else if ((component & AXIS_CHANGED) != 0) {
                Vector3f axis = (Vector3f) obj;
                for (int i = 0; i < msArr.length; i++) {
                    msArr[i].initAlignmentAxis(axis);
                }
            } else if ((component & ROTATION_CHANGED) != 0) {
                Point3f point = (Point3f) obj;
                for (int i = 0; i < msArr.length; i++) {
                    msArr[i].initRotationPoint(point);
                }
            } else if ((component & CONSTANT_SCALE_CHANGED) != 0) {
                boolean bool = ((Boolean) obj).booleanValue();
                for (int i = 0; i < msArr.length; i++) {
                    msArr[i].initConstantScaleEnable(bool);
                }
            } else if ((component & SCALE_FACTOR_CHANGED) != 0) {
                double scale = ((Double) obj).doubleValue();
                for (int i = 0; i < msArr.length; i++) {
                    msArr[i].initScale(scale);
                }
            }
        } else {
            super.updateImmediateMirrorObject(args);
        }
    }

    Transform3D getOrientedTransform(int viewIndex) {
        synchronized (orientedTransforms) {
            if (viewIndex >= orientedTransforms.length) {
                Transform3D xform = new Transform3D();
                Transform3D[] newList = new Transform3D[viewIndex + 1];
                for (int i = 0; i < orientedTransforms.length; i++) {
                    newList[i] = orientedTransforms[i];
                }
                newList[viewIndex] = xform;
                orientedTransforms = newList;
            } else {
                if (orientedTransforms[viewIndex] == null) {
                    orientedTransforms[viewIndex] = new Transform3D();
                }
            }
        }
        return orientedTransforms[viewIndex];
    }

    synchronized void updateOrientedTransform(Canvas3D canvas, int viewIndex) {
        double angle = 0.0;
        double sign;
        boolean status;
        Transform3D orientedxform = getOrientedTransform(viewIndex);
        if (mode == OrientedShape3D.ROTATE_ABOUT_AXIS) {
            canvas.getCenterEyeInImagePlate(viewPosition);
            canvas.getImagePlateToVworld(xform);
            xform.transform(viewPosition);
            xform.set(getCurrentLocalToVworld());
            xform.invert();
            xform.transform(viewPosition);
            eyeVec.set(viewPosition);
            eyeVec.normalize();
            status = projectToPlane(eyeVec, nAxis);
            if (status) {
                zAxis.x = 0.0;
                zAxis.y = 0.0;
                zAxis.z = 1.0;
                status = projectToPlane(zAxis, nAxis);
            }
            if (status) {
                vector.cross(eyeVec, zAxis);
                if (vector.dot(nAxis) > 0.0) {
                    sign = 1.0;
                } else {
                    sign = -1.0;
                }
                double dot = eyeVec.dot(zAxis);
                if (dot > 1.0f) {
                    dot = 1.0f;
                } else if (dot < -1.0f) {
                    dot = -1.0f;
                }
                angle = sign * Math.acos(dot);
                aa.x = nAxis.x;
                aa.y = nAxis.y;
                aa.z = nAxis.z;
                aa.angle = -angle;
                orientedxform.set(aa);
            } else {
                orientedxform.setIdentity();
            }
        } else {
            canvas.getCenterEyeInImagePlate(viewPosition);
            yUpPoint.set(viewPosition);
            yUpPoint.y += 0.01;
            canvas.getImagePlateToVworld(xform);
            xform.transform(viewPosition);
            xform.transform(yUpPoint);
            xform.set(getCurrentLocalToVworld());
            xform.invert();
            xform.transform(viewPosition);
            xform.transform(yUpPoint);
            eyeVec.set(viewPosition);
            eyeVec.normalize();
            yUp.set(yUpPoint);
            yUp.sub(viewPosition);
            yUp.normalize();
            zAxis.x = 0.0;
            zAxis.y = 0.0;
            zAxis.z = 1.0;
            vector.cross(eyeVec, zAxis);
            double length = vector.length();
            if (length > 0.0001) {
                double dot = eyeVec.dot(zAxis);
                if (dot > 1.0f) {
                    dot = 1.0f;
                } else if (dot < -1.0f) {
                    dot = -1.0f;
                }
                angle = Math.acos(dot);
                aa.x = vector.x;
                aa.y = vector.y;
                aa.z = vector.z;
                aa.angle = -angle;
                zRotate.set(aa);
            } else {
                zRotate.set(1.0);
            }
            yAxis.x = 0.0;
            yAxis.y = 1.0;
            yAxis.z = 0.0;
            zRotate.transform(yAxis);
            status = projectToPlane(yAxis, eyeVec);
            if (status) {
                status = projectToPlane(yUp, eyeVec);
            }
            if (status) {
                double dot = yUp.dot(yAxis);
                if (dot > 1.0f) {
                    dot = 1.0f;
                } else if (dot < -1.0f) {
                    dot = -1.0f;
                }
                angle = Math.acos(dot);
                vector.cross(yUp, yAxis);
                if (eyeVec.dot(vector) < 0) {
                    angle *= -1;
                }
                aa.x = eyeVec.x;
                aa.y = eyeVec.y;
                aa.z = eyeVec.z;
                aa.angle = -angle;
                xform.set(aa);
                vector.x = rotationPoint.x;
                vector.y = rotationPoint.y;
                vector.z = rotationPoint.z;
                orientedxform.set(vector);
                orientedxform.mul(xform);
                orientedxform.mul(zRotate);
                vector.scale(-1.0);
                xform.set(vector);
                orientedxform.mul(xform);
            } else {
                orientedxform.setIdentity();
            }
        }
        if (constantScale) {
            canvas.getInverseVworldProjection(left_xform, right_xform);
            im_vec[0].set(0.0, 0.0, 0.0, 1.0);
            im_vec[1].set(1.0, 0.0, 0.0, 1.0);
            left_xform.transform(im_vec[0]);
            left_xform.transform(im_vec[1]);
            left_xform.set(getCurrentLocalToVworld());
            left_xform.invert();
            left_xform.transform(im_vec[0]);
            left_xform.transform(im_vec[1]);
            lvec.set(im_vec[1]);
            lvec.sub(im_vec[0]);
            lvec.normalize();
            im_vec[0].set(0.0, 0.0, 0.0, 1.0);
            im_vec[1].set(lvec);
            im_vec[1].w = 1.0;
            left_xform.set(getCurrentLocalToVworld());
            left_xform.transform(im_vec[0]);
            left_xform.transform(im_vec[1]);
            canvas.getVworldProjection(left_xform, right_xform);
            left_xform.transform(im_vec[0]);
            left_xform.transform(im_vec[1]);
            im_vec[0].x /= im_vec[0].w;
            im_vec[0].y /= im_vec[0].w;
            im_vec[0].z /= im_vec[0].w;
            im_vec[1].x /= im_vec[1].w;
            im_vec[1].y /= im_vec[1].w;
            im_vec[1].z /= im_vec[1].w;
            lvec.set(im_vec[1]);
            lvec.sub(im_vec[0]);
            double scale = 1 / lvec.length();
            scale *= scaleFactor * canvas.getPhysicalWidth() / 2;
            scaleXform.setScale(scale);
            orientedxform.mul(scaleXform);
        }
    }

    private boolean projectToPlane(Vector3d projVec, Vector3d planeVec) {
        double dis = planeVec.dot(projVec);
        projVec.x = projVec.x - planeVec.x * dis;
        projVec.y = projVec.y - planeVec.y * dis;
        projVec.z = projVec.z - planeVec.z * dis;
        double length = projVec.length();
        if (length < EPSILON) {
            return false;
        }
        projVec.scale(1 / length);
        return true;
    }

    void compile(CompileState compState) {
        super.compile(compState);
        mergeFlag = SceneGraphObjectRetained.DONT_MERGE;
        compState.keepTG = true;
    }

    void searchGeometryAtoms(UnorderList list) {
        list.add(getGeomAtom(getMirrorShape(key)));
    }
}
