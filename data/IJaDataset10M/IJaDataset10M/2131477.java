package jarcade.collision;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Owner
 */
public class CollisionDetector {

    private ArrayList<CollisionListener> listeners;

    public CollisionDetector() {
        listeners = new ArrayList<CollisionListener>();
    }

    public void addCollisionListener(CollisionListener listener) {
        listeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    public void detectBoundsCollisions(CollisionEntity a, CollisionEntity b) {
        if (a == b) {
            return;
        }
        if (a.isCollisionActive() && b.isCollisionActive()) {
            if (a.getBounds().intersects(b.getBounds())) {
                notifyCollision(a, b);
            }
        }
    }

    public void detectBoundsCollisions(List<? extends CollisionEntity> entities) {
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            CollisionEntity a = entities.get(i);
            for (int j = i; j < size; j++) {
                CollisionEntity b = entities.get(j);
                detectBoundsCollisions(a, b);
            }
        }
    }

    public void detectBoundsCollisions(CollisionEntity entity, List<? extends CollisionEntity> entities) {
        for (CollisionEntity other : entities) {
            detectBoundsCollisions(entity, other);
        }
    }

    public void detectBoundsCollisions(List<? extends CollisionEntity> list_a, List<? extends CollisionEntity> list_b) {
        for (CollisionEntity a : list_a) {
            for (CollisionEntity b : list_b) {
                detectBoundsCollisions(a, b);
            }
        }
    }

    public void detectRadiusCollisions(CollisionEntity a, CollisionEntity b) {
        if (a == b) {
            return;
        }
        if (a.isCollisionActive() && b.isCollisionActive()) {
            double distance = a.getCenter().distance(b.getCenter());
            double minimum = a.getRadius() + b.getRadius();
            if (distance <= minimum) {
                notifyCollision(a, b);
            }
        }
    }

    public void detectRadiusCollisions(List<? extends CollisionEntity> entities) {
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            CollisionEntity a = entities.get(i);
            for (int j = i + 1; j < size; j++) {
                CollisionEntity b = entities.get(j);
                detectRadiusCollisions(a, b);
            }
        }
    }

    public void detectRadiusCollisions(CollisionEntity entity, List<? extends CollisionEntity> entities) {
        for (CollisionEntity other : entities) {
            detectRadiusCollisions(entity, other);
        }
    }

    public void detectRadiusCollisions(List<? extends CollisionEntity> list_a, List<? extends CollisionEntity> list_b) {
        for (CollisionEntity a : list_a) {
            for (CollisionEntity b : list_b) {
                detectRadiusCollisions(a, b);
            }
        }
    }

    public void detectBoundsAreaExit(CollisionEntity area, CollisionEntity entity) {
        if (entity == area) {
            return;
        }
        if (area.isCollisionActive() && entity.isCollisionActive()) {
            Rectangle2D areaBnds = area.getBounds();
            Rectangle2D entityBnds = entity.getBounds();
            if (entityBnds.getMinX() > areaBnds.getMinX() + areaBnds.getWidth()) {
                notifyAreaExit(area, entity);
            } else if (entityBnds.getMinY() > areaBnds.getMinY() + areaBnds.getHeight()) {
                notifyAreaExit(area, entity);
            } else if (entityBnds.getMinX() + entityBnds.getWidth() < areaBnds.getMinX()) {
                notifyAreaExit(area, entity);
            } else if (entityBnds.getMinY() + entityBnds.getHeight() < areaBnds.getMinY()) {
                notifyAreaExit(area, entity);
            }
        }
    }

    public void detectBoundsAreaExit(CollisionEntity area, List<? extends CollisionEntity> entities) {
        for (CollisionEntity entity : entities) {
            detectBoundsAreaExit(area, entity);
        }
    }

    public void detectBoundsAreaEnter(CollisionEntity area, CollisionEntity entity) {
        if (entity == area) {
            return;
        }
        if (area.isCollisionActive() && entity.isCollisionActive()) {
            if (area.getBounds().contains(entity.getBounds())) {
                notifyAreaEnter(entity, area);
            }
        }
    }

    public void detectBoundsAreaEnter(CollisionEntity area, List<CollisionEntity> entities) {
        for (CollisionEntity entity : entities) {
            detectBoundsAreaEnter(area, entity);
        }
    }

    public void detectRadiusAreaEnter(CollisionEntity area, CollisionEntity entity) {
    }

    public void detectRadiusAreaExit(CollisionEntity area, CollisionEntity entity) {
    }

    private void notifyCollision(CollisionEntity a, CollisionEntity b) {
        for (CollisionListener c : listeners) {
            c.intersectDetected(a, b);
        }
    }

    private void notifyAreaExit(CollisionEntity area, CollisionEntity entity) {
        for (CollisionListener c : listeners) {
            c.areaExitDetected(area, entity);
        }
    }

    private void notifyAreaEnter(CollisionEntity a, CollisionEntity b) {
        for (CollisionListener c : listeners) {
            c.areaEnterDetected(a, b);
        }
    }
}
