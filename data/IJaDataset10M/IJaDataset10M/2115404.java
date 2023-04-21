package uk.ac.soton.grophysics;

import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.bulletphysics.linearmath.Transform;
import de.grogra.graph.impl.Node;
import de.grogra.rgg.Library;

public class PhysicsUtils {

    /**
     * Offset the input matrix by the point offset
     */
    public Matrix4d offsetMatrix(Matrix4d in, Point3d offset) {
        return (offsetMatrix(in, new Vector3d(offset)));
    }

    /**
     * offset the input matrix by the vector offset
     */
    public Matrix4d offsetMatrix(Matrix4d in, Vector3d offset) {
        Matrix4d result = new Matrix4d();
        result.set(in);
        Matrix4d trans = getTranslation(offset);
        result.mul(trans);
        return result;
    }

    /**
     * Utility to get a double matrix from a transform
     */
    public Matrix4d getMatrix4d(Transform transform) {
        Matrix4f mf = new Matrix4f();
        transform.getMatrix(mf);
        Matrix4d result = new Matrix4d(mf);
        return result;
    }

    /**
     * Utility to get a translation matrix from a vector
     */
    public Matrix4d getTranslation(Vector3d v) {
        Matrix4d result = new Matrix4d();
        result.setIdentity();
        result.setTranslation(v);
        return result;
    }

    /**
     * Utility to get the difference between two transforms
     * By multiplying the inverse of the first by the second
     */
    public Matrix4d getDifference(Matrix4d mNew, Matrix4d mOld) {
        Matrix4d result = new Matrix4d();
        result.invert(mOld);
        result.mul(mNew);
        return result;
    }

    /**
	 * Display some info about a Node
	 * @param node
	 */
    public void showNodeInfo(Node node) {
        Library.println("Node " + node.getId() + " " + node.getName() + " has " + node.getDirectChildCount() + " children");
    }
}
