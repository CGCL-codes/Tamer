package prealpha.curve;

import java.io.IOException;
import java.util.Stack;
import com.jme.intersection.CollisionResults;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.system.JmeException;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;

/**
 * <code>CurveWrapper</code> wraps a non-renderable prealpha.curve.Curve into a
 * com.jme.curve.Curve so that it can be rendered for Debugging Purposes.
 * Just pass the non-renderable Curve to the constructor and put this wrapper into the 
 * scene graph 
 * @author FAV
 * @version 
 */
public class CurveWrapper extends com.jme.curve.Curve {

    private static final long serialVersionUID = -9054481443596803396L;

    private prealpha.curve.Curve curve;

    public CurveWrapper(prealpha.curve.Curve curve) {
        super(curve.getName());
        this.curve = curve;
    }

    /**
    *
    * <code>getPoint</code> calculates a point on the curve based on
    * the time, where time is [0, 1]. How the point is calculated is
    * defined by the subclass.
    * @param time the time frame on the curve, [0, 1].
    * @return the point on the curve at a specified time.
    */
    @Override
    public Vector3f getPoint(float time) {
        return curve.getPoint(time);
    }

    @Override
    public Vector3f getPoint(float time, Vector3f store) {
        return curve.getPoint(time, store);
    }

    @Override
    public Matrix3f getOrientation(float time, float precision) {
        return curve.getOrientation(time, precision).toRotationMatrix();
    }

    @Override
    public Matrix3f getOrientation(float time, float precision, Vector3f up) {
        return curve.getOrientation(time, precision, up).toRotationMatrix();
    }

    public float getLength() {
        return curve.getLength();
    }

    public int getNumberOfSamplingPoints() {
        return curve.numberOfSamplingPoints;
    }

    @Override
    public void findCollisions(Spatial scene, CollisionResults results) {
    }

    @Override
    public boolean hasCollision(Spatial scene, boolean checkTriangles) {
        return false;
    }

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
    }
}
