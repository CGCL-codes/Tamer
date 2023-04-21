package dk.impact.sheeplifter.spatials.actors.dropzone;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.CullState;
import com.jme.renderer.Renderer;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;
import com.jme.image.Texture;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.bounding.BoundingBox;
import dk.impact.sheeplifter.audio.GameAudioSystem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

/**
 * @author Jeppe Schmidt <jeppe.schmidt@gmail.com>
 * @version $Revision$
 */
public class Centrifuge extends Node {

    public static final int MAX_Y_BEFORE_REMOVE = 500;

    public static final float DEFAULT_START_SPEED = 10;

    private static final int highPitchSheep[] = { 5, 9, 11 };

    private float scale;

    private enum States {

        normal, accelerating, deaccelerating
    }

    private States state;

    private Vector3f leadPos;

    private Quaternion rotQ = new Quaternion();

    private Vector3f rotDir = new Vector3f();

    private ArrayList<Spatial> trailingSpatials = new ArrayList<Spatial>();

    private ArrayList<Spatial> shootOutSpatials = new ArrayList<Spatial>();

    private Vector<Spatial> spatialsShotOut;

    private float shootOutdelay;

    private boolean shootOutStarted;

    private float timeSinceLastShootOut;

    private float rotDirAngle;

    private float speed;

    private float angleSpeedX = 1.4f;

    private float angleSpeedY = 2.8f;

    private float angleSpeedZ = 1.4f;

    private TrailMesh trailMesh;

    private int trailSpatialDistance;

    private LinkedList<TrailMesh.TrailData> trailData;

    private Vector3f tangent = new Vector3f();

    private Camera cam;

    private float startSpeed;

    private GameAudioSystem gameAudioSystem;

    public Centrifuge(int trailSpatialDistance, int trailSectionCount, float shootOutDelay, Camera cam, GameAudioSystem gameAudioSystem) {
        this.trailSpatialDistance = trailSpatialDistance;
        this.shootOutdelay = shootOutDelay;
        this.cam = cam;
        this.gameAudioSystem = gameAudioSystem;
        spatialsShotOut = new Vector<Spatial>();
        leadPos = new Vector3f(5, 0, 0);
        rotDirAngle = 0;
        startSpeed = DEFAULT_START_SPEED;
        resetSpeed();
        buildTrail(trailSectionCount);
    }

    private void buildTrail(int trailSectionCount) {
        trailMesh = new TrailMesh("TrailMesh", trailSectionCount);
        trailMesh.setUpdateSpeed(60.0f);
        trailMesh.setFacingMode(TrailMesh.FacingMode.Billboard);
        trailMesh.setUpdateMode(TrailMesh.UpdateMode.Step);
        trailMesh.setLightCombineMode(Spatial.LightCombineMode.Off);
        trailMesh.setCullHint(CullHint.Never);
        trailMesh.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        DisplaySystem display = DisplaySystem.getDisplaySystem();
        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        Texture t1 = TextureManager.loadTexture(Centrifuge.class.getClassLoader().getResource("res/geometry/trail.png"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        ts.setTexture(t1);
        trailMesh.setRenderState(ts);
        BlendState bs = display.getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.One);
        bs.setTestEnabled(true);
        trailMesh.setRenderState(bs);
        ZBufferState zs = display.getRenderer().createZBufferState();
        zs.setWritable(false);
        zs.setEnabled(true);
        trailMesh.setRenderState(zs);
        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.None);
        cs.setEnabled(true);
        trailMesh.setRenderState(cs);
        attachChild(trailMesh);
        trailData = trailMesh.getTrailVectors();
        trailMesh.setModelBound(new BoundingBox());
        trailMesh.updateModelBound();
    }

    public void update(float timeDelta) {
        rotDir.set(FastMath.sin(rotDirAngle * angleSpeedX), FastMath.cos(rotDirAngle * angleSpeedY), FastMath.cos(rotDirAngle * angleSpeedZ));
        rotDirAngle += 0.2f * timeDelta;
        rotQ.fromAngleAxis(speed * timeDelta, rotDir);
        if (state == States.accelerating) {
            speed += 4.0f * timeDelta;
            if (speed >= 22 && trailingSpatials.size() > 0 && timeSinceLastShootOut > shootOutdelay && (shootOutStarted || isLeadPosInVertAngle())) {
                shootOutSpatials.add(trailingSpatials.get(0));
                ejectNextSpatial();
                timeSinceLastShootOut = 0;
                shootOutStarted = true;
            }
            timeSinceLastShootOut += timeDelta;
        }
        if (state == States.deaccelerating) {
            speed -= 5.0 * timeDelta;
            if (speed <= DEFAULT_START_SPEED) {
                speed = DEFAULT_START_SPEED;
                state = States.normal;
            }
        }
        rotQ.multLocal(leadPos);
        spatialsShotOut.clear();
        for (Spatial spatial : shootOutSpatials) {
            spatial.getLocalTranslation().y += 700 * timeDelta;
            if (spatial.getLocalTranslation().y >= MAX_Y_BEFORE_REMOVE) {
                detachChild(spatial);
                spatialsShotOut.add(spatial);
                updateRenderState();
            }
        }
        for (int i = 0; i < spatialsShotOut.size(); i++) {
            shootOutSpatials.remove(spatialsShotOut.get(i));
        }
        int trailIndex = 0;
        for (Spatial trailingSpatial : trailingSpatials) {
            trailingSpatial.getLocalTranslation().set(trailData.get(trailIndex).position).multLocal(scale);
            trailIndex += trailSpatialDistance;
        }
        trailMesh.setTrailFront(leadPos, tangent, 0.4f, Timer.getTimer().getTimePerFrame());
        trailMesh.update(cam.getLocation());
    }

    private void ejectNextSpatial() {
        if (gameAudioSystem != null) {
            int sound = (int) (Math.random() * highPitchSheep.length);
            gameAudioSystem.addPositionalTrack("res/sound/maeh" + highPitchSheep[sound] + ".wav", trailingSpatials.get(0), 1.0f);
        }
        trailingSpatials.get(0).getLocalTranslation().set(0, 5, 0);
        trailingSpatials.remove(0);
        if (trailingSpatials.isEmpty()) {
            state = States.deaccelerating;
            shootOutStarted = false;
        }
    }

    private boolean isLeadPosInVertAngle() {
        float dot = rotDir.dot(Vector3f.UNIT_Y);
        return FastMath.abs(dot) <= 0.15f;
    }

    public Vector3f getLeadPos() {
        return leadPos;
    }

    public void setAccelerate(States state) {
        this.state = state;
    }

    public void resetSpeed() {
        speed = startSpeed;
    }

    public void resizeGeometry(float scale) {
        this.scale = scale;
        trailMesh.setLocalScale(scale);
    }

    public void addTrailingSpatial(Spatial spatial) {
        trailingSpatials.add(spatial);
        attachChild(spatial);
        spatial.setCullHint(CullHint.Never);
        state = States.accelerating;
    }

    public void setStartSpeed(float speed) {
        this.startSpeed = speed;
        this.speed = speed;
    }

    public boolean hasNoTrailingSpatials() {
        return trailingSpatials.isEmpty();
    }
}
