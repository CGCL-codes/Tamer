package factory.weapons;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Capsule;
import com.jmetest.physics.ragdoll.SimpleRagDoll;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.JointAxis;
import com.jmex.physics.PhysicsSpace;

/**
 * Builds different weapons:
 * <pre>
 * <--------|-- Sword
 * ############ Club
 * -------------------- Spear / Wand
 * *________|-- Mace
 * + Attach spikes or boulders
 * </pre>
 * Use {@link #getWeaponNode()} to obtain a node to attach to your scene.
 * Based on SimpleRagDoll class.
 * @author T8TSOSO
 */
public class WeaponCollection {

    private PhysicsSpace physicsSpace;

    private DynamicPhysicsNode handle;

    private DynamicPhysicsNode weaponStem;

    private DynamicPhysicsNode joinNode;

    public static final char WEAPON_SWORD = 0x1;

    public static final char WEAPON_CLUB = 0x2;

    public static final char WEAPON_SPEAR = 0x3;

    public static final char WEAPON_WAND = 0x4;

    public static final char WEAPON_MACE = 0x5;

    public static final char WEAPON_FRYINGPAN = 0x6;

    private static final char NODE_TYPE_CAPSULE = 0x1;

    private static final char NODE_TYPE_BOX = 0x2;

    private static final char NODE_TYPE_SPHERE = 0x3;

    private char weaponType;

    public WeaponCollection(PhysicsSpace physicsSpace) {
        this.physicsSpace = physicsSpace;
    }

    public void buildWeapon() {
        Vector3f joinPosition = joinNode.getLocalTranslation();
        handle = physicsSpace.createDynamicNode();
        final Capsule handleCapsule = new Capsule("handle", 9, 9, 9, .2f, .1f);
        handleCapsule.setModelBound(new BoundingBox());
        handleCapsule.updateModelBound();
        handle.attachChild(handleCapsule);
        handle.generatePhysicsGeometry();
        handle.getLocalTranslation().set(joinPosition.x, joinPosition.y + 0.5f, joinPosition.z);
        switch(weaponType) {
            case WEAPON_SWORD:
                weaponStem = createStem("sword", NODE_TYPE_CAPSULE, 0.2f, 4.05f, Vector3f.UNIT_Y, joinPosition.x, joinPosition.y + 3f, joinPosition.z);
                break;
            default:
                break;
        }
        this.join(handle, weaponStem, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), 1, 1);
    }

    private DynamicPhysicsNode createStem(String name, char type, float radius, float height, Vector3f rotate90Axis, float x, float y, float z) {
        DynamicPhysicsNode node = physicsSpace.createDynamicNode();
        TriMesh stem = null;
        switch(type) {
            case NODE_TYPE_CAPSULE:
                stem = new Capsule(name, 9, 9, 9, radius, height);
                break;
            default:
                break;
        }
        stem.setModelBound(new BoundingBox());
        stem.updateModelBound();
        node.attachChild(stem);
        if (rotate90Axis != null) {
            stem.getLocalRotation().fromAngleAxis(FastMath.PI / 2, rotate90Axis);
        }
        node.generatePhysicsGeometry();
        node.getLocalTranslation().set(x, y, z);
        return node;
    }

    private void join(DynamicPhysicsNode node1, DynamicPhysicsNode node2, Vector3f anchor, Vector3f direction, float min, float max) {
        Joint joint = physicsSpace.createJoint();
        joint.attach(node1, node2);
        joint.setAnchor(anchor);
        JointAxis leftShoulderAxis = joint.createRotationalAxis();
        leftShoulderAxis.setDirection(direction);
        leftShoulderAxis.setPositionMinimum(min);
        leftShoulderAxis.setPositionMaximum(max);
    }

    public void getWeaponNode(char inputWeaponType, DynamicPhysicsNode joinNode) {
        weaponType = inputWeaponType;
        this.joinNode = joinNode;
        buildWeapon();
    }

    public void joinWeapon(DynamicPhysicsNode join, Vector3f anchor, Vector3f direction, float min, float max) {
        join.getParent().attachChild(handle);
        join.getParent().attachChild(weaponStem);
        this.join(join, handle, anchor, direction, min, max);
    }
}
