package com.es;

import com.artemis.Entity;
import com.es.manager.EntityTemplate;
import com.es.manager.ResourceManager;
import com.es.state.GameWorld;
import org.newdawn.slick.geom.Vector2f;
import java.util.List;

/**
 * User: snorre Date: 30.10.11 Time: 13:06
 */
public class EventHub {

    private String entityTemplate;

    private int difficulty;

    private int eventRadius;

    private Vector2f position;

    private boolean isActive = false;

    public EventHub(String entityTemplate, int difficulty, int eventRadius) {
        this.entityTemplate = entityTemplate;
        this.difficulty = difficulty;
        this.eventRadius = eventRadius;
        position = new Vector2f();
    }

    public String getEntityTemplate() {
        return entityTemplate;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getEventRadius() {
        return eventRadius;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector2f(x, y);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Entity> createEntityHub(ResourceManager resourceManager, GameWorld world) {
        isActive = true;
        return resourceManager.getEntityTemplate(EntityTemplate.ELEMENT_TYPE_HELPER, entityTemplate).createEntities(world, 0, position.x, -position.y, 0);
    }
}
