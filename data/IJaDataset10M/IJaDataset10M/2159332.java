package imi.collision;

import org.jdesktop.mtgame.EntityComponent;

/**
 * This is an entity component that implements the collision interface of
 * an entity.
 *
 * @author Doug Twilleager
 */
public class CollisionComponent extends EntityComponent {

    /**
     * The collision system for this component
     */
    CollisionSystem collisionSystem = null;

    /**
     * A boolean to indicate whether or not this component is currently
     * enabled for picking
     */
    private boolean pickable = true;

    /**
     * A boolean to indicate whether or not this component is currently
     * enabled for collision
     */
    private boolean collidable = true;

    /**
     * The default constructor
     */
    public CollisionComponent(CollisionSystem cs) {
        collisionSystem = cs;
    }

    /**
     * Get the collision system for this component
     */
    public CollisionSystem getCollisionSystem() {
        return (collisionSystem);
    }

    /**
     * Set's whether or not this collision component should be considered
     * for pick queries
     */
    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    /**
     * Retuns whether or not this collision component is considered for pick
     * queries
     */
    public boolean isPickable() {
        return (pickable);
    }

    /**
     * Set's whether or not this collision component should be considered
     * for collision queries
     */
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    /**
     * Retuns whether or not this collision component is considered for collision
     * queries
     */
    public boolean isCollidable() {
        return (collidable);
    }
}
