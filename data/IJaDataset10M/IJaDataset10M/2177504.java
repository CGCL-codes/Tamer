package dsr.models.map;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import dsr.AppletMain;
import dsr.GameObject;
import dsr.data.AppletMapSquare;
import dsr.models.CollisionQuad;

public final class TransparentFloor extends GameObject {

    private static final long serialVersionUID = 1L;

    private static Quaternion q = new Quaternion();

    private static CullState cs_floor;

    private static BlendState alphaState;

    static {
        q.fromAngleAxis(FastMath.PI / 2, new Vector3f(-1, 0, 0));
    }

    public TransparentFloor(AppletMain main, AppletMapSquare sq) {
        super(main, "TransparentFloor", false, false, false, (float) sq.x + 0.5f, (float) sq.y + 0.5f, 0.01f);
        collider = new CollisionQuad(1, 1, this);
        collider.setLocalRotation(q);
        collider.setRenderState(main.getTextureState(sq.raised_texture_code));
        if (cs_floor == null) {
            cs_floor = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
            cs_floor.setEnabled(true);
            cs_floor.setCullFace(CullState.Face.Back);
        }
        collider.setRenderState(cs_floor);
        collider.updateRenderState();
        this.attachChild(collider);
        this.updateModelBound();
        if (alphaState == null) {
            alphaState = main.getDisplay().getRenderer().createBlendState();
            alphaState.setBlendEnabled(true);
            alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
            alphaState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
            alphaState.setTestEnabled(true);
            alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
            alphaState.setEnabled(true);
        }
        collider.setRenderState(alphaState);
        collider.updateRenderState();
        collider.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        sq.threeds.add(this);
    }
}
