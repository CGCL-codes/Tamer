package net.phys2d.raw.collide;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.Contact;

/**
 * Collides two lines with oneanother.
 * 
 * @author Gideon Smeding
 *
 */
public class LineLineCollider implements Collider {

    /**
	 * @see net.phys2d.raw.collide.Collider#collide(net.phys2d.raw.Contact[], net.phys2d.raw.Body, net.phys2d.raw.Body)
	 */
    public int collide(Contact[] contacts, Body bodyA, Body bodyB) {
        return 0;
    }

    /**
	 * Gets the closest point to a given point on the indefinately extended line.
	 * TODO: move this somewhere in math package
	 * 
	 * @param startA Starting point of the line
	 * @param endA End point of the line
	 * @param point The point to get a closes point on the line for
	 * @return the closest point on the line or null if the lines are parallel
	 */
    public static Vector2f getClosestPoint(Vector2f startA, Vector2f endA, Vector2f point) {
        Vector2f startB = point;
        Vector2f endB = new Vector2f(endA);
        endB.sub(startA);
        endB.set(endB.y, -endB.x);
        float d = endB.y * (endA.x - startA.x);
        d -= endB.x * (endA.y - startA.y);
        if (d == 0) return null;
        float uA = endB.x * (startA.y - startB.getY());
        uA -= endB.y * (startA.x - startB.getX());
        uA /= d;
        return new Vector2f(startA.x + uA * (endA.x - startA.x), startA.y + uA * (endA.y - startA.y));
    }
}
