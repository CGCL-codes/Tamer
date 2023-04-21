package dsr.effects;

import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import dsr.AppletMain;
import dsr.GameObject;
import dsr.start.StartupNew;
import dsrwebserver.tables.EquipmentTypesTable;

public final class Bullet extends GameObject {

    private static final float SPEED = 5.0f;

    private static final float RAD = 0.05f;

    private static final float LENGTH = 1.2f;

    private static final long serialVersionUID = 1L;

    private static TextureState ts;

    private Vector3f move_dir;

    private float max_dist;

    private float dist_so_far = 0;

    private boolean explosion_at_end;

    private Vector3f end;

    public Bullet(AppletMain m, Vector3f start, Vector3f _end, boolean explode) {
        super(m, "Bullet", false, false, false, start.x, start.z, start.y);
        move_dir = _end.subtract(start).normalize().mult(SPEED);
        max_dist = _end.subtract(start).length();
        end = _end;
        explosion_at_end = explode;
        if (max_dist == Float.POSITIVE_INFINITY) {
            return;
        }
        Cylinder s = new Cylinder("Bullet_Cylinder", 2, 5, RAD, LENGTH);
        if (ts == null) {
            ts = main.getTextureState("data/textures/cells3.png");
        }
        s.setRenderState(ts);
        CullState cs = main.getDisplay().getRenderer().createCullState();
        BlendState as = main.getDisplay().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.One);
        ColorRGBA color = new ColorRGBA(1f, 0.15f, 0.15f, 0.2f);
        MaterialState mat = main.getDisplay().getRenderer().createMaterialState();
        mat.setAmbient(color);
        mat.setEmissive(color);
        mat.setDiffuse(color);
        this.setLightCombineMode(Spatial.LightCombineMode.Replace);
        this.setTextureCombineMode(Spatial.TextureCombineMode.Off);
        this.setRenderState(mat);
        this.setRenderState(as);
        this.setRenderState(cs);
        this.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        this.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
        this.updateRenderState();
        s.updateRenderState();
        s.setModelBound(new BoundingSphere());
        this.updateModelBound();
        this.attachChild(s);
        main.attachToRootNode(this, true);
        this.lookAt(_end, Vector3f.UNIT_Y);
        this.updateGeometricState(0, true);
    }

    public void onDraw(Renderer r) {
        if (move_dir.length() == 0) {
            main.addObjectForRemoval(this);
            if (explosion_at_end && StartupNew.hi_mem) {
                new Explosion(main, end.x, end.y, end.z, 0.2f, EquipmentTypesTable.ET_GRENADE);
            }
        } else {
            Vector3f act_dir = move_dir.mult(main.getInterpolation());
            this.getLocalTranslation().addLocal(act_dir);
            dist_so_far += act_dir.length();
            if (dist_so_far > max_dist) {
                if (explosion_at_end && StartupNew.hi_mem) {
                    new Explosion(main, end.x, end.y, end.z, 0.2f, EquipmentTypesTable.ET_GRENADE);
                }
                main.addObjectForRemoval(this);
            } else {
                this.updateGeometricState(main.getInterpolation(), true);
            }
        }
        super.onDraw(r);
    }
}
