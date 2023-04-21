package engine.collide;

import engine.shapes.Body;
import engine.shapes.Circle;
import engine.shapes.Line;
import engine.vector.Vector;

/**
 * Collision routines betwene a circle and a line.
 * 
 * @author Jeff Ahern
 * @author Matt DePortor
 * @author Keith Kowalski
 * @author Mcomber, Kevin
 */
public strictfp class LineCircleCollider implements Collider {

    /**
	 * @see engine.collide.Collider#collide(engine.collide.Contact[],
	 *      engine.shapes.Body, engine.shapes.Body)
	 */
    public int collide(Contact[] contacts, Body bodyA, Body bodyB) {
        Line line = (Line) bodyA.getShape();
        Circle circle = (Circle) bodyB.getShape();
        Vector[] vertsA = line.getVertices(bodyA.getPosition(), bodyA.getRotation());
        Vector startA = vertsA[0];
        Vector endA = vertsA[1];
        Vector startB = bodyB.getPosition();
        Vector endB = new Vector(endA);
        endB.sub(startA);
        endB.set(endB.y, -endB.x);
        float d = endB.y * (endA.x - startA.x);
        d -= endB.x * (endA.y - startA.y);
        float uA = endB.x * (startA.y - startB.getY());
        uA -= endB.y * (startA.x - startB.getX());
        uA /= d;
        Vector position = null;
        if (uA < 0) {
            position = startA;
        } else if (uA > 1) {
            position = endA;
        } else {
            position = new Vector(startA.x + uA * (endA.x - startA.x), startA.y + uA * (endA.y - startA.y));
        }
        Vector normal = endB;
        normal.set(startB);
        normal.sub(position);
        float distSquared = normal.lengthSquared();
        float radiusSquared = circle.getRadius() * circle.getRadius();
        if (distSquared < radiusSquared) {
            contacts[0].setPosition(position);
            contacts[0].setFeature(new FeaturePair());
            normal.normalise();
            contacts[0].setNormal(normal);
            float separation = (float) Math.sqrt(distSquared) - circle.getRadius();
            contacts[0].setSeparation(separation);
            return 1;
        }
        return 0;
    }
}
