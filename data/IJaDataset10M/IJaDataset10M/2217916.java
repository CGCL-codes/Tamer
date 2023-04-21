package jmetest.jbullet.debug;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingCapsule;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Sphere;
import com.jme.util.GameTaskQueueManager;
import com.jmex.game.StandardGame;
import com.jmex.game.state.GameStateManager;
import com.jmex.jbullet.PhysicsSpace;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.jbullet.collision.shapes.BoxCollisionShape;
import com.jmex.jbullet.collision.shapes.CapsuleCollisionShape;
import com.jmex.jbullet.collision.shapes.CollisionShape;
import com.jmex.jbullet.collision.shapes.CompoundCollisionShape;
import com.jmex.jbullet.collision.shapes.CylinderCollisionShape;
import com.jmex.jbullet.collision.shapes.SphereCollisionShape;
import com.jmex.jbullet.debug.PhysicsDebugGameState;
import com.jmex.jbullet.nodes.PhysicsNode;
import java.util.concurrent.Callable;

/**
 * Testing out whether the PhysicsDebug view can correctly render CompoundCollisionShapes.
 *
 * @author CJ Hare
 */
public class TestDebuggerCoumpoundShape {

    public static void setupGame() {
        final PhysicsSpace pSpace = PhysicsSpace.getPhysicsSpace(PhysicsSpace.BroadphaseTypes.AXIS_SWEEP_3);
        PhysicsDebugGameState state = new PhysicsDebugGameState() {

            @Override
            public void update(float tpf) {
                super.update(tpf);
                pSpace.update(tpf);
            }
        };
        state.setText("A compound collision shape made from every primative");
        state.setDrawState(PhysicsDebugGameState.DrawState.Both);
        PhysicsNode compound = createCompoundCollisionShape();
        compound.setLocalTranslation(0f, 0f, 0f);
        state.getRootNode().attachChild(compound);
        compound.updateRenderState();
        pSpace.add(compound);
        PhysicsNode floor = new PhysicsNode(new Box("physicsfloor", Vector3f.ZERO, 20f, 0.2f, 20f), CollisionShape.ShapeTypes.BOX, 0);
        floor.setLocalTranslation(new Vector3f(0f, -6, 0f));
        state.getRootNode().attachChild(floor);
        floor.updateRenderState();
        pSpace.add(floor);
        GameStateManager.getInstance().attachChild(state);
        state.setActive(true);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("jme.stats", "set");
        StandardGame game = new StandardGame("Test the rendering of a Compound Collision Shape");
        if (GameSettingsPanel.prompt(game.getSettings())) {
            game.start();
            GameTaskQueueManager.getManager().update(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    setupGame();
                    return null;
                }
            });
        }
    }

    private static PhysicsNode createCompoundCollisionShape() {
        Node compoundSpatial = new Node();
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        Box box = new Box("Box", Vector3f.ZERO, 1f, 1f, 1f);
        box.setLocalTranslation(-3f, 0f, 0f);
        box.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.QUARTER_PI, Vector3f.UNIT_Y));
        box.setModelBound(new BoundingBox());
        box.updateModelBound();
        compoundSpatial.attachChild(box);
        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(1f, 1f, 1f));
        compoundShape.addChildShape(boxShape, box.getLocalTranslation(), box.getLocalRotation().toRotationMatrix());
        Sphere capsule = new Sphere("Capsule as Sphere", Vector3f.ZERO, 32, 32, 1f);
        capsule.setLocalTranslation(2f, -1f, 0f);
        capsule.setModelBound(new BoundingBox());
        capsule.updateModelBound();
        compoundSpatial.attachChild(capsule);
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1f, 1f);
        compoundShape.addChildShape(capsuleShape, capsule.getLocalTranslation());
        Cylinder cylinder = new Cylinder("Cylinder", 32, 32, 2f, 5f, true);
        cylinder.setLocalTranslation(0f, 1f, 1f);
        cylinder.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.QUARTER_PI, Vector3f.UNIT_X));
        cylinder.setModelBound(new BoundingBox());
        cylinder.updateModelBound();
        compoundSpatial.attachChild(cylinder);
        CylinderCollisionShape cylinderShape = new CylinderCollisionShape(new Vector3f(2f, 5f, 2.5f));
        compoundShape.addChildShape(cylinderShape, cylinder.getLocalTranslation(), cylinder.getLocalRotation().toRotationMatrix());
        Sphere sphere = new Sphere("Sphere", Vector3f.ZERO, 32, 32, 3f);
        sphere.setLocalTranslation(-4f, 3f, -2f);
        sphere.setModelBound(new BoundingBox());
        sphere.updateModelBound();
        compoundSpatial.attachChild(sphere);
        SphereCollisionShape sphereShape = new SphereCollisionShape(3f);
        compoundShape.addChildShape(sphereShape, sphere.getLocalTranslation());
        PhysicsNode compound = new PhysicsNode(compoundSpatial, compoundShape);
        compound.setKinematic(true);
        return compound;
    }
}
