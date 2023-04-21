package org.xith3d.input.modules;

import java.util.ArrayList;
import org.openmali.vecmath2.Point3f;
import org.xith3d.physics.collision.Collideable;
import org.xith3d.physics.collision.CollideableGroup;
import org.xith3d.physics.collision.Collision;
import org.xith3d.physics.collision.CollisionEngine;

/**
 * This ColliderCheckCallback make the avatar slide at walls.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class SlidingColliderCheckCallback implements ColliderCheckCallback {

    private final CollisionEngine collEngine;

    private CollideableGroup collGroup;

    private final ArrayList<Collision> collisions = new ArrayList<Collision>();

    private final ArrayList<Collideable> walls = new ArrayList<Collideable>();

    private final ArrayList<Collision> collisions2 = new ArrayList<Collision>();

    public final void setCollideableGroup(CollideableGroup collGroup) {
        this.collGroup = collGroup;
    }

    public final CollideableGroup getCollideableGroup() {
        return (collGroup);
    }

    private static final void resolveCollision(final Collision collision, Collideable avatarCollider) {
        Point3f pos = Point3f.fromPool();
        avatarCollider.getPosition(pos);
        pos.add(collision.getScaledNormal());
        avatarCollider.setPosition(pos);
        Point3f.toPool(pos);
    }

    public boolean checkCollision(final Collideable avatarCollider) {
        if ((avatarCollider == null) || (collGroup == null) || !collEngine.isEnabled()) {
            return (false);
        }
        collEngine.checkCollisions(avatarCollider, collGroup, false, collisions);
        if (collisions.size() == 0) {
            return (false);
        }
        resolveCollision(collisions.get(0), avatarCollider);
        if (collisions.size() == 1) {
            return (true);
        }
        walls.clear();
        for (int i = 1; i < collisions.size(); i++) {
            walls.add(collisions.get(i).getCollideable2());
        }
        for (int i = 0; i < walls.size(); i++) {
            collEngine.checkCollisions(avatarCollider, walls.get(i), false, collisions2);
            if (collisions2.size() > 0) {
                resolveCollision(collisions2.get(0), avatarCollider);
            }
        }
        return (true);
    }

    public SlidingColliderCheckCallback(CollisionEngine collEngine, CollideableGroup collGroup) {
        this.collEngine = collEngine;
        this.collGroup = collGroup;
    }
}
