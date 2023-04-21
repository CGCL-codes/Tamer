package net.java.dev.joode.metamorphic3D;

import org.openmali.FastMath;
import net.java.dev.joode.space.CollisionlessSpace;
import net.java.dev.joode.space.Space;
import net.java.dev.joode.geom.Box;
import net.java.dev.joode.joint.JointHinge;
import net.java.dev.joode.Body;
import net.java.dev.joode.World;
import net.java.dev.joode.test.xith.XithGeomAppearance;
import net.java.dev.joode.util.*;

/**
 * A segment has two ends. Each end can connect to a Node. Three rotary DOFs are present.
 * <p/>
 * geom and joint placement:-
 * EndA-rotA-rodA-rotCentral-rodB-rotB-endB      ---> y axis
 * <p/>
 * The default ording of axis for vector representations is A-Central-B
 */
public class Segment {

    public final SelfConfiguringSystem parent;

    /**
     * a space which contains all the geoms associated with the segment, which prevents collisions between the parts
     */
    final CollisionlessSpace segmentSpace;

    final Body endBodyA, endBodyB, rodBodyA, rodBodyB;

    final Box endA, endB;

    final Box rodA, rodB;

    final JointHinge rotA, rotB, rotCentral;

    public final End a, b;

    final SegmentState state = new SegmentState();

    final SegmentVelocities velocities = new SegmentVelocities();

    public SegmentControllor controllor;

    /**
     * turns the y axis from pointing up to pointing down
     */
    public static final Quaternion flipY = new Quaternion();

    static {
        flipY.setAxisAngle((float) Math.PI, Vector3.Z);
    }

    /**
     * we increase the stops a little farther than the analytical Pi/2 amount, to accomidate numerical errors
     */
    public static final float fudge_factor = 1.01f;

    /**
     * places a segment with all rotary joints zerod, and orientation unconfigurable
     *
     * @param w
     * @param s
     * @param position
     */
    public Segment(World w, Space s, SelfConfiguringSystem parent, Vector3 position) {
        this.parent = parent;
        endBodyA = new Body(w, Metamorphic3DParams.SEGMENT_END_MASS);
        endBodyB = new Body(w, Metamorphic3DParams.SEGMENT_END_MASS);
        rodBodyA = new Body(w, Metamorphic3DParams.SEGMENT_ROD_MASS);
        rodBodyB = new Body(w, Metamorphic3DParams.SEGMENT_ROD_MASS);
        endA = new Box(null, Metamorphic3DParams.UNIT_LENGTH * 3f / 4f, Metamorphic3DParams.UNIT_LENGTH / 2, Metamorphic3DParams.UNIT_LENGTH * 3f / 4f);
        endB = new Box(null, Metamorphic3DParams.UNIT_LENGTH * 3f / 4f, Metamorphic3DParams.UNIT_LENGTH / 2, Metamorphic3DParams.UNIT_LENGTH * 3f / 4f);
        rodA = new Box(null, Metamorphic3DParams.UNIT_LENGTH / 2, Metamorphic3DParams.UNIT_LENGTH / 2, Metamorphic3DParams.UNIT_LENGTH / 2);
        rodB = new Box(null, Metamorphic3DParams.UNIT_LENGTH / 2, Metamorphic3DParams.UNIT_LENGTH / 2, Metamorphic3DParams.UNIT_LENGTH / 2);
        endA.category_bits = endB.category_bits = CollisionHandler.SEGMENT_CONNECTOR_CATAGORY;
        endA.collide_bits = endB.collide_bits = CollisionHandler.SEGMENT_CONNECTOR_COLLIDE;
        rodA.category_bits = rodB.category_bits = CollisionHandler.SEGMENT_CATAGORY;
        rodA.collide_bits = rodB.collide_bits = CollisionHandler.SEGMENT_COLLIDE;
        endA.setBody(endBodyA);
        endB.setBody(endBodyB);
        rodA.setBody(rodBodyA);
        rodB.setBody(rodBodyB);
        new XithGeomAppearance(endA).setAppearance(Metamorphic3DParams.SEGMENT_END_XITH_APPEARANCE);
        new XithGeomAppearance(endB).setAppearance(Metamorphic3DParams.SEGMENT_END_XITH_APPEARANCE);
        new XithGeomAppearance(rodA).setAppearance(Metamorphic3DParams.SEGMENT_ROD_XITH_APPEARANCE);
        new XithGeomAppearance(rodB).setAppearance(Metamorphic3DParams.SEGMENT_ROD_XITH_APPEARANCE);
        Vector3 currentPosition = new Vector3(position);
        currentPosition.setY(currentPosition.getY() - Metamorphic3DParams.UNIT_LENGTH / 4);
        endBodyA.setPosition(currentPosition);
        currentPosition.setY(currentPosition.getY() + Metamorphic3DParams.UNIT_LENGTH / 2);
        rodBodyA.setPosition(currentPosition);
        currentPosition.setY(currentPosition.getY() + Metamorphic3DParams.UNIT_LENGTH / 2);
        rodBodyB.setPosition(currentPosition);
        currentPosition.setY(currentPosition.getY() + Metamorphic3DParams.UNIT_LENGTH / 2);
        endBodyB.setPosition(currentPosition);
        endBodyB.setRotation(flipY);
        segmentSpace = new CollisionlessSpace(s);
        segmentSpace.add(endA);
        segmentSpace.add(endB);
        segmentSpace.add(rodA);
        segmentSpace.add(rodB);
        rotA = new JointHinge(w);
        rotB = new JointHinge(w);
        rotCentral = new JointHinge(w);
        currentPosition = new Vector3(position);
        rotA.attach(endBodyA, rodBodyA);
        rotA.setAxis(1, 0, 0);
        rotA.setAnchor(currentPosition.getX(), currentPosition.getY(), currentPosition.getZ());
        currentPosition.setY(currentPosition.getY() + Metamorphic3DParams.UNIT_LENGTH / 2);
        rotCentral.attach(rodBodyA, rodBodyB);
        rotCentral.setAxis(0, 1, 0);
        rotCentral.setAnchor(currentPosition.getX(), currentPosition.getY(), currentPosition.getZ());
        currentPosition.setY(currentPosition.getY() + Metamorphic3DParams.UNIT_LENGTH / 2);
        rotB.attach(rodBodyB, endBodyB);
        rotB.setAxis(1, 0, 0);
        rotB.setAnchor(currentPosition.getX(), currentPosition.getY(), currentPosition.getZ());
        rotA.getLimitMotor().setMaxForce(Metamorphic3DParams.SEGMENT_MAX_TORQUE);
        rotB.getLimitMotor().setMaxForce(Metamorphic3DParams.SEGMENT_MAX_TORQUE);
        rotCentral.getLimitMotor().setMaxForce(Metamorphic3DParams.SEGMENT_MAX_TORQUE);
        rotA.getLimitMotor().setHighStop(FastMath.PI_HALF * fudge_factor);
        rotA.getLimitMotor().setLowStop(-FastMath.PI_HALF * fudge_factor);
        rotB.getLimitMotor().setHighStop(FastMath.PI_HALF * fudge_factor);
        rotB.getLimitMotor().setLowStop(-FastMath.PI_HALF * fudge_factor);
        rotCentral.getLimitMotor().setHighStop(FastMath.PI_HALF * fudge_factor);
        rotCentral.getLimitMotor().setLowStop(-FastMath.PI_HALF * fudge_factor);
        a = new End(endBodyA);
        b = new End(endBodyB);
        endA.setUserData(a);
        endB.setUserData(b);
    }

    /**
     * places a segment in the specified position and orientation for end a. The rest of the positions of elements
     * are configured via the angles provided
     *
     * @param w
     * @param s
     * @param position
     * @param orientation
     * @param A
     * @param central
     * @param B
     */
    public Segment(World w, Space s, SelfConfiguringSystem parent, Vector3 position, Quaternion orientation, float A, float central, float B) {
        this(w, s, parent, position);
        set(position, orientation, A, B, central);
    }

    /**
     * sets the position and orientation of end A, then then angles of each of the DOFs
     * @param position
     * @param orientation
     * @param A
     * @param B
     * @param central
     */
    public void set(Vector3 position, Quaternion orientation, float A, float B, float central) {
        Quaternion rotTransA = new Quaternion();
        Quaternion rotTransB = new Quaternion();
        Quaternion rotTransCentral = new Quaternion();
        rotTransA.setAxisAngle(-A, Vector3.X);
        rotTransB.setAxisAngle(-B, Vector3.X);
        rotTransCentral.setAxisAngle(-central, Vector3.Y);
        Vector3 displacement = new Vector3(0, Metamorphic3DParams.UNIT_LENGTH / 4, 0);
        Vector3 displacementNeg = new Vector3(0, -Metamorphic3DParams.UNIT_LENGTH / 4, 0);
        Quaternion NO_ROTATION = new Quaternion();
        Vector3 endAPos = new Vector3();
        Quaternion endAOr = new Quaternion();
        translate(position, orientation, displacementNeg, NO_ROTATION, endAPos, endAOr);
        Vector3 rotAnchorAPos = new Vector3();
        Quaternion rotAnchorAOr = new Quaternion();
        Vector3 rodAPos = new Vector3();
        Quaternion rodAOr = new Quaternion();
        translate(endAPos, endAOr, displacement, rotTransA, rotAnchorAPos, rotAnchorAOr);
        translate(rotAnchorAPos, rotAnchorAOr, displacement, NO_ROTATION, rodAPos, rodAOr);
        Vector3 rotAnchorCentralPos = new Vector3();
        Quaternion rotAnchorCentralOr = new Quaternion();
        Vector3 rodBPos = new Vector3();
        Quaternion rodBOr = new Quaternion();
        translate(rodAPos, rodAOr, displacement, rotTransCentral, rotAnchorCentralPos, rotAnchorCentralOr);
        translate(rotAnchorCentralPos, rotAnchorCentralOr, displacement, NO_ROTATION, rodBPos, rodBOr);
        Vector3 rotAnchorBPos = new Vector3();
        Quaternion rotAnchorBOr = new Quaternion();
        Vector3 endBPos = new Vector3();
        Quaternion endBOr = new Quaternion();
        translate(rodBPos, rodBOr, displacement, rotTransB, rotAnchorBPos, rotAnchorBOr);
        translate(rotAnchorBPos, rotAnchorBOr, displacement, flipY, endBPos, endBOr);
        endBodyA.setPosition(endAPos);
        endBodyA.setRotation(endAOr);
        endBodyB.setPosition(endBPos);
        endBodyB.setRotation(endBOr);
        rodBodyA.setPosition(rodAPos);
        rodBodyA.setRotation(rodAOr);
        rodBodyB.setPosition(rodBPos);
        rodBodyB.setRotation(rodBOr);
    }

    public static final Vector3 endOffset = new Vector3(0, -Metamorphic3DParams.UNIT_LENGTH / 4, 0);

    public static final Vector3 nodeOffset = new Vector3(0, -Metamorphic3DParams.UNIT_LENGTH * 3f / 4, 0);

    /**
     * class representing the end of a segment, for use with connectors etc
     */
    public class End {

        /**
         * the connector this end is joined to, or null if no connector is present
         */
        public Connector connector;

        public final Body body;

        public End(Body body) {
            this.body = body;
        }

        public boolean isConnected() {
            return connector != null;
        }

        /**
         * works out where the extreme end of the connector is. This would normall correspond to the center of a face
         * on a node
         * End bodies are orientated so that negative Y from the body, gets closer to the edge of the segment.
         * End geometries are half unit length. So their center is 3/4 unit length. So an offset of -1/4 unit length
         * grom the end conectors geometry will be the face connector point
         * @param passback
         * @return
         */
        public Vector3 getConnectionPosition(Vector3 passback) {
            passback.set(body.getPosition());
            body.getRotation().mulInc(endOffset, passback);
            return passback;
        }

        /**
         * works out where a node should be that would be compatible with connector
         * @param passback
         * @return
         */
        public Vector3 getNodePosition(Vector3 passback) {
            passback.set(body.getPosition());
            body.getRotation().mulInc(nodeOffset, passback);
            return passback;
        }

        public Quaternion getNodeOrientation(Quaternion passback) {
            passback.set(body.getQuaternion());
            return passback;
        }
    }

    public class SegmentState implements ROVector {

        public float get(int index) {
            if (index == 0) {
                return rotA.getHingeAngle();
            } else if (index == 1) {
                return rotCentral.getHingeAngle();
            } else if (index == 2) {
                return rotB.getHingeAngle();
            } else {
                throw new IllegalArgumentException();
            }
        }

        public int size() {
            return 3;
        }
    }

    private class SegmentVelocities implements Vector {

        public void set(int index, float val) {
            val = Math.max(-FastMath.PI_HALF, val);
            val = Math.min(FastMath.PI_HALF, val);
            if (index == 0) {
                rotA.getLimitMotor().setVelocity(val);
            } else if (index == 1) {
                rotCentral.getLimitMotor().setVelocity(val);
            } else if (index == 2) {
                rotB.getLimitMotor().setVelocity(val);
            } else {
                throw new IllegalArgumentException();
            }
        }

        public float get(int index) {
            if (index == 0) {
                return rotA.getLimitMotor().getVelocity();
            } else if (index == 1) {
                return rotCentral.getLimitMotor().getVelocity();
            } else if (index == 2) {
                return rotB.getLimitMotor().getVelocity();
            } else {
                throw new IllegalArgumentException();
            }
        }

        public int size() {
            return 3;
        }
    }

    /**
     * vector representation of the segments state. The rotary angles of
     * A, the Cetral and B are packed into a vector of size three in the order just mentiond.
     *
     * @return
     */
    public ROVector getState() {
        return state;
    }

    /**
     * provides an interface to the motors of the segment, allowing their velocities to be set directly. The values that
     * can be read from this vector just represent the desired velocities, rather than the actual physical velocities
     *
     * @return
     */
    public Vector getVelocities() {
        return velocities;
    }

    public float getAxisAngleA() {
        return rotA.getHingeAngle();
    }

    public float getAxisAngleB() {
        return rotB.getHingeAngle();
    }

    public float getAxisAngleCentral() {
        return rotCentral.getHingeAngle();
    }

    public void setAxisAVel(float velocity) {
        rotA.getLimitMotor().setVelocity(velocity);
    }

    public void setAxisBVel(float velocity) {
        rotB.getLimitMotor().setVelocity(velocity);
    }

    public void setAxisCentralVel(float velocity) {
        rotCentral.getLimitMotor().setVelocity(velocity);
    }

    /**
     * util method for translating applying a relative displacement and orientation to an absolute frame,
     * in order to calculate a new absolute frame
     * @param position
     * @param orientation
     * @param relativeDisplacement
     * @param relativeRotation
     * @param resultPos
     * @param resultOrientation
     */
    private void translate(Vector3 position, Quaternion orientation, Vector3 relativeDisplacement, Quaternion relativeRotation, Vector3 resultPos, Quaternion resultOrientation) {
        resultPos.set(relativeDisplacement);
        orientation.rotate(resultPos, resultPos);
        resultPos.add(position);
        orientation.mul(relativeRotation, resultOrientation);
    }
}
