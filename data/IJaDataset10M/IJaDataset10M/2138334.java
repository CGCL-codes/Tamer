package inyo;

import javax.vecmath.*;

/**
 * @author Sascha Ledinsky
 *
 * Basic camera properties, currently supporting only a transformation matrix and a
 * focal length parameter.
 * The matrix can be manipulated easily with the setPosition and pointAt methods
 */
public class RtCamera {

    /** the transformation matrix **/
    private Matrix4d m4View = new Matrix4d();

    /** the focal length in mm, assuming a 35mm film camera **/
    private float fFocalLength;

    /**
	* Create a default camera with a 50mm lens, and an identity matrix
	* (i.e. positioned at 0,0,0 looking at positive z)
	**/
    public RtCamera() {
        fFocalLength = 50;
        m4View.setIdentity();
    }

    /**
	* Create a camera with specified position, orientation and focal length
	* @param position the position of the camera
	* @param pointAt the point to point the camera at
	* @param focalLength the folal lenght in mm (assuming this is a 35mm film camera)
	**/
    public RtCamera(Point3d position, Point3d pointAt, float focalLength) {
        m4View.setIdentity();
        setPosition(position.x, position.y, position.z);
        pointAt(pointAt.x, pointAt.y, pointAt.z, null);
        fFocalLength = focalLength;
    }

    /**
	* Create a camera specified by a camera string, containing the matrix and focal length
	* @param line string specifying the camera
	**/
    public RtCamera(String line) {
        String[] a = line.split("\\s");
        double[] v = new double[a.length - 2];
        for (int i = 1; i < a.length - 1; v[i - 1] = Double.parseDouble(a[i++])) ;
        fFocalLength = Float.parseFloat(a[a.length - 1]);
        m4View.set(v);
    }

    /**
	* Changes the position of the camera (by manipulating its transformation matrix)
	* @param x the new x position
	* @param y the new y position
	* @param z the new z position
	**/
    public void setPosition(double x, double y, double z) {
        m4View.setTranslation(new Vector3d(-x, -y, -z));
    }

    /**
	* Points the camera at the specified point (by manipulating its transformation matrix)
	* @param x the x position of the point to look at
	* @param y the y position of the point to look at
	* @param z the z position of the point to look at
	* @param up a vector pointing up (in camera space) - if null, positive y will be used
	**/
    public void pointAt(double x, double y, double z, Vector3d up) {
        if (up == null) up = new Vector3d(0, 1, 0);
        Vector3d position = new Vector3d();
        m4View.get(position);
        position.scale(-1.0);
        Vector3d forward = new Vector3d(x, y, z);
        forward.sub(position);
        forward.normalize();
        Vector3d right = new Vector3d();
        right.cross(up, forward);
        right.normalize();
        up.cross(right, forward);
        up.normalize();
        m4View.set(new double[] { right.x, right.y, right.z, 0.0, -up.x, -up.y, -up.z, 0.0, forward.x, forward.y, forward.z, 0.0, 0.0, 0.0, 0.0, 1.0 });
        Matrix4d m4Translation = new Matrix4d(new double[] { 1.0, 0.0, 0.0, -position.x, 0.0, 1.0, 0.0, -position.y, 0.0, 0.0, 1.0, -position.z, 0.0, 0.0, 0.0, 1.0 });
        m4View.mul(m4Translation);
    }

    /**
	* Sets the focal lenghts (in mm - assuming this is a 35mm film camera)
	* @param focalLenght the new folal lenght in mm
	**/
    public void setFocalLength(float focalLength) {
        fFocalLength = focalLength;
    }

    /**
	 * Setter method for the transformation matrix (a 4x4 matrix)
	 * @return
	 */
    public void setMatrix(Matrix4d m4view) {
        this.m4View = new Matrix4d(m4view);
    }

    /**
	* Getter method for the transformation matrix (a 4x4 matrix).
	* @return a pointer to the cameras transformation matrix. Note that this is not a defensive copy,
	* so changing the matrix does have an effect on this camera!
	**/
    public Matrix4d getMatrix() {
        return m4View;
    }

    /**
	* Computes the focal lenght in pixel.
	* @param screenWidth the width of the screen in pixel
	* @return the focal length in pixel. This implementation returns focalLenght / 35f * screenWidth -
	* i.e. it assumes that the focal length was provided in mm and that this is a 35mm film camera
	**/
    public float getFocalLength(float screenWidth) {
        return fFocalLength / 35f * screenWidth;
    }

    /**
	* Just a test routine
	**/
    public static void main(String[] args) {
        Matrix4f m1 = new Matrix4f();
        Matrix4f m2 = new Matrix4f();
        m1.setIdentity();
        m2.setIdentity();
        m1.setTranslation(new Vector3f(0, 0, 10));
        System.out.println(m1);
        m2.rotY((float) Math.PI / 2f);
        System.out.println(m2);
        m2.mul(m1);
        System.out.println(m2);
    }
}
