package net.java.dev.joode.collision.collider;

import net.java.dev.joode.collision.ContactGeom;
import net.java.dev.joode.geom.*;
import net.java.dev.joode.util.Matrix3;
import net.java.dev.joode.util.Vector3;

/**
 * @author Harald Dietrich
 */
public class CapsuleBoxCollider extends Collider {

    public static final CapsuleBoxCollider INSTANCE = new CapsuleBoxCollider();

    /**
     * {@inheritDoc}
     */
    @Override
    public int collide(Geom o1, Geom o2, ContactGeom[] contact, int contactIndex, int skip) {
        final Capsule capsule = (Capsule) o1;
        final Box box = (Box) o2;
        final Vector3 capsPos = capsule.getPosition();
        final Matrix3 capsRot = capsule.getRotation();
        final Vector3 boxPos = box.getPosition();
        final Matrix3 boxRot = box.getRotation();
        contact[contactIndex].setGeom1(o1);
        contact[contactIndex].setGeom2(o2);
        float clen = capsule.getLength() * 0.5f;
        Vector3 p1 = new Vector3(capsPos.getX() + clen * capsRot.get(2, 0), capsPos.getY() + clen * capsRot.get(2, 1), capsPos.getZ() + clen * capsRot.get(2, 2));
        Vector3 p2 = new Vector3(capsPos.getX() - clen * capsRot.get(2, 0), capsPos.getY() - clen * capsRot.get(2, 1), capsPos.getZ() - clen * capsRot.get(2, 2));
        Vector3 pl = new Vector3();
        Vector3 pb = new Vector3();
        Colliders.closestLineBoxPoints(p1, p2, boxPos, boxRot, box.getSide(), pl, pb);
        return SphereSphereCollider.collideSpheres(pl, capsule.getRadius(), pb, 0, contact, contactIndex, o1.hashCode(), o2.hashCode());
    }
}
