package org.omg.tacsit.ui.entity;

import org.omg.tacsit.controller.Entity;

/**
 * A UI element which is capable of displaying multiple entities.
 * @author Matthew Child
 */
public interface EntityCollectionUI {

    /**
   * Adds an entity to the set of displayed entities in this UI element.  If the EntityCollectionUI does not allow 
   * the entity, it will be discarded.
   * @param entity The entity to add to the set of displayed entities.
   */
    public void addEntity(Entity entity);

    /**
   * Remove an entity from the set of displayed entities in this UI element.
   * @param entity The entity to remove from the set of displayed entities.
   */
    public void removeEntity(Entity entity);

    /**
   * Notifies the EntityCollectionUI that the entity has changed, and updates the entity in the UI's display.
   * @param entity The entity that has been modified.
   */
    public void updateEntity(Entity entity);

    /**
   * Clears all entities displayed in this UI element.
   */
    public void clearEntities();

    /**
   * Checks to see if the entity can be displayed in this UI element.
   * @param entity The entity to determine whether it can be displayed.
   * @return true if it can be displayed, false otherwise.
   */
    public boolean isEntityAllowed(Entity entity);
}
