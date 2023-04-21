package jat.vr;

import java.applet.Applet;
import javax.media.j3d.*;
import javax.vecmath.*;
import jat.util.*;
import jat.matvec.data.*;

public class Body3D extends TransformGroup {

    Vector3f Vf = new Vector3f();

    Vector3d Vd = new Vector3d();

    Vector3d VRot = new Vector3d();

    Transform3D Trans = new Transform3D();

    double scale = 1.0;

    Applet myapplet;

    static String images_path, Lightwave_path, Wavefront_path, ThreeDStudio_path;

    public Body3D() {
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    }

    public Body3D(Applet myapplet) {
        this.myapplet = myapplet;
        String thisClassName = "Body3D";
        images_path = FileUtil.getClassFilePath("jat.vr", thisClassName) + "images_hires/";
        Wavefront_path = FileUtil.getClassFilePath("jat.vr", thisClassName) + "Wavefront\\";
        Lightwave_path = FileUtil.getClassFilePath("jat.vr", thisClassName) + "Lightwave\\";
        ThreeDStudio_path = FileUtil.getClassFilePath("jat.vr", thisClassName) + "3DStudio\\";
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    }

    public void set_scale(double scale) {
        getTransform(Trans);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    /** Set the position of the body in km
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
    public void set_position(double x, double y, double z) {
        getTransform(Trans);
        Vd.x = x;
        Vd.y = y;
        Vd.z = z;
        Trans.setTranslation(Vd);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    /**
	 * Set new body position.
	 * @param rv new position
	 */
    public void set_position(double[] rv) {
        getTransform(Trans);
        Vd.x = rv[0];
        Vd.y = rv[1];
        Vd.z = rv[2];
        Trans.setTranslation(Vd);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    /**
	 * Set new body position.
	 * @param r new position
	 */
    public void set_position(VectorN r) {
        getTransform(Trans);
        Vd.x = r.x[0];
        Vd.y = r.x[1];
        Vd.z = r.x[2];
        Trans.setTranslation(Vd);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    public void set_position(Point3d r) {
        getTransform(Trans);
        Vd.x = r.x;
        Vd.y = r.y;
        Vd.z = r.z;
        Trans.setTranslation(Vd);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    public void set_position(Vector3d r) {
        getTransform(Trans);
        Vd.x = r.x;
        Vd.y = r.y;
        Vd.z = r.z;
        Trans.setTranslation(Vd);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    /**
	 * Set_body attitude.using quaternion
	 * @param quatObject quaternion
	 */
    public void set_attitude(Transform3D quatObject) {
        setTransform(quatObject);
    }

    /** Set body attitude without changing position or scale using Euler angles
	 * @param alpha x angle
	 * @param beta y angle
	 * @param gamma z angle
	 */
    public void set_attitude(double alpha, double beta, double gamma) {
        getTransform(Trans);
        Trans.get(Vf);
        VRot.x = alpha;
        VRot.y = beta;
        VRot.z = gamma;
        Trans.setEuler(VRot);
        Trans.setTranslation(Vf);
        Trans.setScale(scale);
        setTransform(Trans);
    }

    /**
	 * Set body position and attitude
	 * @param x
	 * @param y
	 * @param z
	 * @param alpha
	 * @param beta
	 * @param gamma
	 */
    public void set_pos_attitude(double x, double y, double z, double alpha, double beta, double gamma) {
        getTransform(Trans);
        Trans.get(Vf);
        VRot.x = alpha;
        VRot.y = beta;
        VRot.z = gamma;
        Trans.setEuler(VRot);
        Vd.x = x;
        Vd.y = y;
        Vd.z = z;
        Trans.setTranslation(Vd);
        Trans.setScale(scale);
        setTransform(Trans);
    }
}
