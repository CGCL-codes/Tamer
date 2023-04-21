package net.sourceforge.jetdog.gfx;

import net.sourceforge.jetdog.gfx.collision.CollisionStrategy;

/**
 * A CollisionManager performs collision detection given two ActorGroups
 * and a CollisionStrategy. 
 * 
 * @author Jonathan Chung
 */
public class CollisionManager {

    private ActorGroup group1;

    private ActorGroup group2;

    private CollisionStrategy strategy;

    /**
	 * Creates a Collision manager given two groups and a strategy.
	 */
    public CollisionManager(ActorGroup group1, ActorGroup group2, CollisionStrategy strategy) {
        this.group1 = group1;
        this.group2 = group2;
        this.strategy = strategy;
    }

    /**
	 * Returns the first group.
	 */
    public ActorGroup getGroup1() {
        return group1;
    }

    /**
	 * Returns the second group.
	 */
    public ActorGroup getGroup2() {
        return group2;
    }

    /**
	 * Returns the collision strategy used.
	 */
    public CollisionStrategy getStrategy() {
        return strategy;
    }
}
