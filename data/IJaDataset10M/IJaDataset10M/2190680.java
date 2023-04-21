package com.jmedemos.stardust.scene;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jmedemos.stardust.util.SDUtils;

/**
 * Fires a ray straight ahead and checks if it hits any Entity.
 * If we hit an entity, we set it as new target if the target is not locked.
 */
public class TargetDevice extends Controller {

    private static final long serialVersionUID = 1L;

    private Node origin;

    private Node scene;

    private Node currentTarget = null;

    private Boolean locked = false;

    private Boolean foundTarget = false;

    /**
     * creates a new TargetDevice
     * @param origin the node which fires the Ray
     * @param scene the Scene to check for targets
     * @param player 
     */
    public TargetDevice(Node origin, Node scene) {
        this.origin = origin;
        this.scene = scene;
    }

    /**
     * casts a ray and checks for targets.
     */
    @Override
    public void update(float time) {
        if (locked == true) {
            return;
        }
        foundTarget = false;
        Ray ray = new Ray(origin.getLocalTranslation(), origin.getLocalRotation().getRotationColumn(2));
        PickResults results = new BoundingPickResults();
        results.setCheckDistance(true);
        scene.findPick(ray, results);
        if (results.getNumber() <= 0) {
            currentTarget = null;
            return;
        }
        for (int i = 0; i < results.getNumber(); i++) {
            Node geom = results.getPickData(i).getTargetMesh().getParent();
            if (SDUtils.containsNode(geom, origin) == true) {
                continue;
            }
            if (isEntity(geom)) {
                foundTarget = true;
                break;
            }
        }
        results.clear();
        if (foundTarget == false) {
            currentTarget = null;
        }
    }

    /**
     * TODO hack, don't target projectiles
     * @param toCheck node to check
     * @return true if we target an entity which is not a projectile
     */
    private boolean isEntity(Node toCheck) {
        if (EntityManager.get().isEntity(toCheck) && !toCheck.getName().startsWith("projectil")) {
            currentTarget = toCheck;
            return true;
        }
        if (toCheck.getParent() != null) {
            return isEntity(toCheck.getParent());
        } else {
            return false;
        }
    }

    public Boolean getLocked() {
        return locked;
    }

    public void toggleLock() {
        locked = !locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Node getCurrentTarget() {
        return currentTarget;
    }
}
