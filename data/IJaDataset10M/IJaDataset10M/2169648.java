package locusts.server.collisions;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import locusts.common.entities.Entity;
import locusts.server.Game;
import locusts.server.physics.RNTJoint;
import locusts.server.physics.TranslateJoint;

/**
 * A collision responce that will create a joint between the two entities
 * using the center of the second entity as the join point
 * 
 * @author Hamish Morgan
 */
public class AttachCR implements CollisionResponce {

    private final Game game;

    public AttachCR(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void doCollision(Collidable a, Collidable b, double period) {
        Entity ea = null;
        if (a instanceof CollidableEntityAdapter) {
            ea = ((CollidableEntityAdapter) a).getEntity();
        } else if (a instanceof Entity) {
            ea = ((Entity) a);
        } else {
            throw new IllegalArgumentException("The Collidable arguments to not represent known entities.");
        }
        Entity eb = null;
        if (b instanceof CollidableEntityAdapter) {
            eb = ((CollidableEntityAdapter) b).getEntity();
        } else if (b instanceof Entity) {
            eb = ((Entity) b);
        } else {
            throw new IllegalArgumentException("The Collidable arguments to not represent known entities.");
        }
        Area intersection = CollisionSystem.getIntersection(a, b);
        final double minRtsDist = a.getMaxRadius() * 0.2;
        final Rectangle2D bounds = intersection.getBounds2D();
        final double x = bounds.getCenterX();
        final double y = bounds.getCenterY();
        final double dx = x - a.getX();
        final double dy = y - a.getY();
        if (dx * dx + dy * dy < minRtsDist * minRtsDist) {
            game.getPhysicsSystem().addZJoint(new TranslateJoint(ea, eb, b.getX(), b.getY()));
        } else {
            game.getPhysicsSystem().addZJoint(new RNTJoint(ea, eb, b.getX(), b.getY()));
        }
    }
}
