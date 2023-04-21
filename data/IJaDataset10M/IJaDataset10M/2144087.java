package com.mw3d.core.entity.data;

import java.util.HashMap;
import java.util.Map;
import com.jmex.model.animation.KeyframeController;
import com.mw3d.core.entity.ComplexEntity;

/**
 * This class represents the animation for a complex entity.
 * This will be the control of the animation for this entity.
 * 
 * @author ndebruyn
 * Created on Jul 18, 2005
 */
public class EntityAnimation {

    /** List of keyframes for this entities */
    private Map keyframes = new HashMap();

    private ComplexEntity complexEntity;

    public EntityAnimation(ComplexEntity complexEntity) {
        keyframes = new HashMap();
        this.complexEntity = complexEntity;
    }

    /**
     * A method which will add
     * @param index
     */
    public void addKeyframe(KeyframeIndex index) {
        keyframes.put(index.getName(), index);
    }

    public Map getKeyframes() {
        return keyframes;
    }

    public void deleteKeyframe(String name) {
        keyframes.remove(name);
    }

    public boolean hasAnimation() {
        boolean succ = false;
        if (complexEntity != null && complexEntity.getModel() != null) {
            try {
                if (complexEntity.getModel().getChild(0).getController(0) instanceof KeyframeController) {
                    succ = true;
                }
            } catch (Exception e) {
            }
        }
        return succ;
    }

    public int getTotalFrames() {
        if (complexEntity != null && complexEntity.getModel() != null) {
            try {
                if (complexEntity.getModel().getChild(0).getController(0) instanceof KeyframeController) {
                    return ((KeyframeController) complexEntity.getModel().getChild(0).getController(0)).keyframes.size() - 1;
                }
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public void activateKeyframe(String name) {
        if (name != null && keyframes != null) {
            KeyframeIndex index = (KeyframeIndex) keyframes.get(name);
            if (index != null) {
                ((KeyframeController) complexEntity.getModel().getChild(0).getController(0)).setNewAnimationTimes(index.getStartFrame(), index.getEndFrame());
            }
        }
    }

    public void activateFrame(int frame) {
        ((KeyframeController) complexEntity.getModel().getChild(0).getController(0)).setNewAnimationTimes(frame, frame);
    }
}
