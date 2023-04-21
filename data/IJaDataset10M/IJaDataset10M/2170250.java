package jme3test.bullet;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Tests attaching ghost nodes to physicsnodes via the scenegraph
 * @author normenhansen
 */
public class TestAttachGhostObject extends SimpleApplication implements AnalogListener {

    private HingeJoint joint;

    private GhostControl ghostControl;

    private Node collisionNode;

    private Node hammerNode;

    private Vector3f tempVec = new Vector3f();

    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        TestAttachGhostObject app = new TestAttachGhostObject();
        app.start();
    }

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Lefts", "Rights", "Space");
    }

    public void onAnalog(String binding, float value, float tpf) {
        if (binding.equals("Lefts")) {
            joint.enableMotor(true, 1, .1f);
        } else if (binding.equals("Rights")) {
            joint.enableMotor(true, -1, .1f);
        } else if (binding.equals("Space")) {
            joint.enableMotor(false, 0, 0);
        }
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        setupKeys();
        setupJoint();
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public void setupJoint() {
        Node holderNode = PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f(.1f, .1f, .1f)), 0);
        holderNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f, 0, 0f));
        rootNode.attachChild(holderNode);
        getPhysicsSpace().add(holderNode);
        Node hammerNode = PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f(.3f, .3f, .3f)), 1);
        hammerNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f, -1, 0f));
        rootNode.attachChild(hammerNode);
        getPhysicsSpace().add(hammerNode);
        collisionNode = PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f(.3f, .3f, .3f)), 0);
        collisionNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(1.8f, 0, 0f));
        rootNode.attachChild(collisionNode);
        getPhysicsSpace().add(collisionNode);
        ghostControl = new GhostControl(new SphereCollisionShape(0.7f));
        hammerNode.addControl(ghostControl);
        getPhysicsSpace().add(ghostControl);
        joint = new HingeJoint(holderNode.getControl(RigidBodyControl.class), hammerNode.getControl(RigidBodyControl.class), Vector3f.ZERO, new Vector3f(0f, -1, 0f), Vector3f.UNIT_Z, Vector3f.UNIT_Z);
        getPhysicsSpace().add(joint);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (ghostControl.getOverlappingObjects().contains(collisionNode.getControl(PhysicsControl.class))) {
            fpsText.setText("collide");
        }
    }
}
