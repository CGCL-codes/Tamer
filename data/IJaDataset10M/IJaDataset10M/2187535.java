package net.java.dev.joode.collision.collider;

import net.java.dev.joode.geom.Geom;
import net.java.dev.joode.geom.Ray;
import net.java.dev.joode.geom.Rectangle;
import net.java.dev.joode.collision.ContactGeom;
import net.java.dev.joode.util.Vector2;
import net.java.dev.joode.util.ParametricSegment2D;
import net.java.dev.joode.util.Vector3;

/**
 * NOT A PROPER COLLIDER!!! todo
 * At the moment only works with 2D rays (no x or y component, and used for picking 2D scenes)
 * @author Tom Larkworthy
 */
public class RayRectangleCollider extends Collider {

    public static final RayRectangleCollider INSTANCE = new RayRectangleCollider();

    private RayRectangleCollider() {
    }

    public static final ParametricSegment2D ray2D = new ParametricSegment2D();

    public static final ParametricSegment2D[] e = new ParametricSegment2D[4];

    public static final Vector2[] pts = new Vector2[4];

    public static final Vector2[] intersections = new Vector2[4];

    static {
        for (int i = 0; i < 4; i++) {
            pts[i] = new Vector2();
            e[i] = new ParametricSegment2D();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int collide(Geom o1, Geom o2, ContactGeom[] contact, int contactIndex, int skip) {
        final Ray ray = (Ray) o1;
        final Rectangle rect = (Rectangle) o2;
        contact[contactIndex].setGeom1(ray);
        contact[contactIndex].setGeom2(rect);
        return collideRayRectangle(ray, rect, contact, contactIndex);
    }

    public int collideRayRectangle(Ray ray, Rectangle rect, ContactGeom[] contacts, int contacIndex) {
        final Vector3 rayPos = ray.getPosition();
        float depth = rect.pointDepth(rayPos.getX(), rayPos.getY(), rayPos.getZ());
        if (depth > 0f) {
            contacts[contacIndex].setDepth(depth);
            contacts[contacIndex].setNormal(0f, 0f, 1f);
            contacts[contacIndex].setPosition(rayPos.getX(), rayPos.getY(), 0f);
            return 1;
        } else {
            return 0;
        }
    }
}
