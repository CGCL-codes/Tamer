package factories;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.scene.BillboardNode;
import com.jme.scene.Controller;
import com.jme.scene.shape.Quad;

public class SimpleExplosionController extends Controller {

    private static final long serialVersionUID = 1L;

    public static final float MAX_TIME = 1.5f;

    public static final float[] scaleFactors = new float[] { 1.1f, 0.8f, 0.5f };

    private Quad[] quads;

    private BillboardNode bbNode;

    private float currentTime;

    private Quaternion[] targetRots;

    public SimpleExplosionController(BillboardNode bbNode, Quad[] quads) {
        this.bbNode = bbNode;
        this.quads = quads;
        currentTime = MAX_TIME;
        int quadSize = quads.length;
        targetRots = new Quaternion[quadSize];
        float pitch = FastMath.PI - 0.01f;
        for (int i = 0; i < quadSize; i++) {
            Quad quad = quads[i];
            Quaternion targetRot = quad.getLocalRotation().clone();
            targetRot.fromAngles(0f, 0f, pitch);
            pitch = -pitch;
            targetRots[i] = targetRot;
        }
        bbNode.updateRenderState();
    }

    @Override
    public void update(float time) {
        currentTime -= time;
        if (currentTime <= 0.5f) bbNode.removeFromParent(); else {
            for (int i = 0; i < quads.length; i++) {
                Quad quad = quads[i];
                float size = quad.getWidth();
                size += size * scaleFactors[i] * currentTime * time;
                quad.resize(size, size);
                quad.getLocalRotation().slerp(targetRots[i], time * 0.2f);
            }
        }
    }
}
