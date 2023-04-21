package net.java.dev.joode.collision.collider;

import net.java.dev.joode.collision.ContactGeom;
import net.java.dev.joode.geom.*;
import net.java.dev.joode.util.*;

/**
 * @author Harald Dietrich
 */
public class BoxPlaneCollider extends Collider {

    /**
     *  static variables
     */
    private static Vector3 Q = new Vector3(), A = new Vector3(), B = new Vector3(), p = new Vector3();

    public static final BoxPlaneCollider INSTANCE = new BoxPlaneCollider();

    /**
     * {@inheritDoc}
     */
    @Override
    public int collide(Geom o1, Geom o2, ContactGeom[] contact, int contactIndex, int skip) {
        final Box box = (Box) o1;
        final Plane plane = (Plane) o2;
        final Vector3 boxPos = box.getPosition();
        final Matrix3 boxRot = box.getRotation();
        final Vector4 n = plane.p;
        contact[contactIndex].setGeom1(o1);
        contact[contactIndex].setGeom2(o2);
        int ret = 0;
        Q.setX(n.dot(boxRot.getColumn(0)));
        Q.setY(n.dot(boxRot.getColumn(1)));
        Q.setZ(n.dot(boxRot.getColumn(2)));
        A.setX(box.getSide().getX() * Q.getX());
        A.setY(box.getSide().getY() * Q.getY());
        A.setZ(box.getSide().getZ() * Q.getZ());
        B.setX(Math.abs(A.getX()));
        B.setY(Math.abs(A.getY()));
        B.setZ(Math.abs(A.getZ()));
        float depth = plane.p.m[3] + 0.5f * (B.m[0] + B.m[1] + B.m[2]) - n.dot(boxPos);
        if (depth < 0) return 0;
        int maxc = contact.length - contactIndex;
        if (maxc < 1) maxc = 1;
        if (maxc > 3) maxc = 3;
        p.set(boxPos);
        int i = 0;
        float hs;
        for (i = 0; i < 3; i++) {
            hs = box.getSide().m[i] * 0.5f;
            if (A.m[i] > 0) {
                p.m[0] -= hs * boxRot.m[0 + i];
                p.m[1] -= hs * boxRot.m[4 + i];
                p.m[2] -= hs * boxRot.m[8 + i];
            } else {
                p.m[0] += hs * boxRot.m[0 + i];
                p.m[1] += hs * boxRot.m[4 + i];
                p.m[2] += hs * boxRot.m[8 + i];
            }
        }
        contact[contactIndex].setPosition(p);
        contact[contactIndex].setNormal(n);
        contact[contactIndex].setDepth(depth);
        ret = 1;
        if (maxc > 1) {
            contact[contactIndex + 1].setNormal(n);
            if (maxc == 3) {
                contact[contactIndex + 2].setNormal(n);
            }
            boolean next;
            if (B.m[0] < B.m[1]) {
                if (B.m[2] < B.m[0]) {
                    next = testContact(1, 2, 2, A, B, p, boxRot, box, depth, contact, contactIndex);
                    if (next) {
                        ret++;
                        if (maxc > 2) {
                            if (B.m[0] < B.m[1]) {
                                next = testContact(2, 0, 0, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            } else {
                                next = testContact(2, 1, 1, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            }
                        }
                    }
                } else {
                    next = testContact(1, 0, 0, A, B, p, boxRot, box, depth, contact, contactIndex);
                    if (next) {
                        ret++;
                        if (maxc > 2) {
                            if (B.m[1] < B.m[0]) {
                                next = testContact(2, 1, 1, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            } else {
                                next = testContact(2, 2, 2, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            }
                        }
                    }
                }
            } else {
                if (B.m[2] < B.m[1]) {
                    next = testContact(1, 2, 2, A, B, p, boxRot, box, depth, contact, contactIndex);
                    if (next) {
                        ret++;
                        if (maxc > 2) {
                            if (B.m[0] < B.m[1]) {
                                next = testContact(2, 0, 0, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            } else {
                                next = testContact(2, 1, 1, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            }
                        }
                    }
                } else {
                    next = testContact(1, 1, 1, A, B, p, boxRot, box, depth, contact, contactIndex);
                    if (next) {
                        ret++;
                        if (maxc > 2) {
                            if (B.m[0] < B.m[2]) {
                                next = testContact(2, 0, 0, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            } else {
                                next = testContact(2, 2, 2, A, B, p, boxRot, box, depth, contact, contactIndex);
                                if (next) ret++;
                            }
                        }
                    }
                }
            }
        }
        for (i = 0; i < ret; i++) {
            contact[contactIndex + i].setGeom1(o1);
            contact[contactIndex + i].setGeom2(o2);
        }
        return ret;
    }

    private boolean testContact(int ctact, int side, int sideinc, Vector3 A, Vector3 B, Vector3 p, Matrix3 R, Box box, float depth, ContactGeom[] contact, int contactIndex) {
        depth -= B.m[sideinc];
        if (depth < 0f) return false;
        if (A.m[sideinc] > 0f) {
            contact[contactIndex + ctact].getPosition().set(p.getX() + box.getSide().m[side] * R.m[0 + side], p.getY() + box.getSide().m[side] * R.m[4 + side], p.getZ() + box.getSide().m[side] * R.m[8 + side]);
        } else {
            contact[contactIndex + ctact].getPosition().set(p.getX() - box.getSide().m[side] * R.m[0 + side], p.getY() - box.getSide().m[side] * R.m[4 + side], p.getZ() - box.getSide().m[side] * R.m[8 + side]);
        }
        contact[contactIndex + ctact].setDepth(depth);
        return true;
    }
}
