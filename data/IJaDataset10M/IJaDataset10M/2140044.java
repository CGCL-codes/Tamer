package org.xith3d.physics.collision;

/**
 * A collision listener. It is implemented by a class,
 * which wants to be notified of collisions when calling some
 * checkCollisions() method in CollisionEngine.
 * 
 * @see CollisionEngine#checkCollisions(Collideable, Collideable, boolean)
 * @see CollisionEngine#checkCollisions(Collideable, Collideable, boolean, CollisionListener)
 * @see CollisionEngine#checkCollisions(CollideableGroup, boolean, CollisionListener)
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public interface CollisionListener {

    /**
	 * A collision has happened !
	 * 
	 * @param collision Information about this collision, say,
	 * contact position and normal, depth, geoms involved.
	 */
    public void onCollision(Collision collision);
}
