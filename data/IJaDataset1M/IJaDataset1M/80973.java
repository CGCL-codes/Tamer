package org.jal3d;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import cat.arestorm.math.MutableQuaternion;
import cat.arestorm.math.MutableVector3D;
import cat.arestorm.math.Quaternion;
import cat.arestorm.math.Vector3D;

public class Mixer extends AbstractMixer {

    protected Model model;

    protected Vector<Animation> vectorAnimation;

    protected List<AnimationAction> listAnimationAction;

    protected List<AnimationCycle> listAnimationCycle;

    protected float animationTime;

    protected float animationDuration;

    protected float timeFactor;

    public Mixer(final Model model) {
        if (model == null) {
            throw new IllegalArgumentException("Mixer can not be " + "build from a null model");
        }
        this.model = model;
        int coreAnimationCount = this.model.getCoreModel().getCoreAnimationCount();
        vectorAnimation = new Vector<Animation>(coreAnimationCount);
        animationTime = 0.0f;
        animationDuration = 0.0f;
        timeFactor = 1.0f;
    }

    public boolean isDefaultMixer() {
        return true;
    }

    /**
	 * Interpolates the weight of an animation cycle.
	 *
	 * This function interpolates the weight of an animation cycle to a new value
	 * in a given amount of time. If the specified animation cycle is not active
	 * yet, it is activated.
	 *
	 * @param id The ID of the animation cycle that should be blended.
	 * @param weight The weight to interpolate the animation cycle to.
	 * @param delay The time in seconds until the new weight should be reached.
	 *
	 * @return One of the following values: <ul>
	 *         <li><strong>true</strong> if successful</li>
	 *         <li><strong>false</strong> if an error happened</li>
	 *         </ul>
	 */
    public boolean blendCycle(int id, float weight, float delay) {
        if ((id < 0) || (id >= (int) vectorAnimation.size())) {
            return false;
        }
        Animation animation = vectorAnimation.get(id);
        if (animation == null) {
            if (weight == 0.0f) return true;
            CoreAnimation coreAnimation = model.getCoreModel().getCoreAnimation(id);
            if (coreAnimation == null) return false;
            addExtraKeyframeForLoopedAnim(coreAnimation);
            AnimationCycle animationCycle = new AnimationCycle(coreAnimation);
            if (animationCycle == null) {
                return false;
            }
            vectorAnimation.set(id, animationCycle);
            listAnimationCycle.add(animationCycle);
            animationCycle.blend(weight, delay);
            return true;
        }
        if (animation.getType() != Animation.Type.CYCLE) {
            return false;
        }
        if (weight == 0.0f) {
            vectorAnimation.set(id, null);
        }
        AnimationCycle animationCycle;
        animationCycle = (AnimationCycle) animation;
        animationCycle.blend(weight, delay);
        animationCycle.checkCallbacks(0, model);
        return true;
    }

    /**
	 * Fades an animation cycle out.
	 *
	 * This function fades an animation cycle out in a given amount of time.
	 *
	 * @param id The ID of the animation cycle that should be faded out.
	 * @param delay The time in seconds until the the animation cycle is
	 *              completely removed.
	 *
	 * @return One of the following values:<ul>
	 *         <li><strong>true</strong> if successful</li>
	 *         <li><strong>false</strong> if an error happened</li>
	 *         </ul>
	 */
    public boolean clearCycle(int id, float delay) {
        if ((id < 0) || (id >= (int) vectorAnimation.size())) {
            return false;
        }
        Animation animation;
        animation = vectorAnimation.get(id);
        if (animation == null) return true;
        if (animation.getType() != Animation.Type.CYCLE) {
            return false;
        }
        vectorAnimation.set(id, null);
        AnimationCycle animationCycle;
        animationCycle = (AnimationCycle) animation;
        animationCycle.setAsync(animationTime, animationDuration);
        animationCycle.blend(0.0f, delay);
        animationCycle.checkCallbacks(0, model);
        return true;
    }

    /**
	 * Executes an animation action.
	 *
	 * This function executes an animation action.
	 *
	 * @param id The ID of the animation action that should be blended.
	 * @param delayIn The time in seconds until the animation action reaches the
	 *                full weight from the beginning of its execution.
	 * @param delayOut The time in seconds in which the animation action reaches
	 *                 zero weight at the end of its execution.
	 * @param weightTarget The weight to interpolate the animation action to.
	 * @param autoLock     This prevents the Action from being reset and removed
	 *                     on the last keyframe if true.
	 *
	 * @return One of the following values:<ul>
	 *         <li><strong>true</strong> if successful</li>
	 *         <li><strong>false</strong> if an error happened</li>
	 *         </ul>
	 */
    public boolean executeAction(int id, float delayIn, float delayOut, float weightTarget, boolean autoLock) {
        CoreAnimation coreAnimation = model.getCoreModel().getCoreAnimation(id);
        if (coreAnimation == null) {
            return false;
        }
        AnimationAction animationAction = new AnimationAction(coreAnimation);
        listAnimationAction.add(animationAction);
        animationAction.execute(delayIn, delayOut, weightTarget, autoLock);
        animationAction.checkCallbacks(0, model);
        return true;
    }

    public boolean executeAction(int id, float delayIn, float delayOut, float weightTarget) {
        return executeAction(id, delayIn, delayOut, weightTarget, false);
    }

    public boolean executeAction(int id, float delayIn, float delayOut) {
        return executeAction(id, delayIn, delayOut, 1.0f, false);
    }

    /**
	 * Clears an active animation action.
	 *
	 * This function removes an animation action from the blend list.  This is
	 * particularly useful with auto-locked actions on their last frame.
	 *
	 * @param id The ID of the animation action that should be removed.
	 *
	 * @return One of the following values:<ul>
	 *         <li><strong>true</strong> if successful
	 *         <li><strong>false</strong> if an error happened or action was not found
	 *         </ul>
	 */
    public boolean removeAction(int id) {
        CoreAnimation coreAnimation = model.getCoreModel().getCoreAnimation(id);
        if (coreAnimation == null) {
            return false;
        }
        for (AnimationAction animationAction : listAnimationAction) {
            if (animationAction.getCoreAnimation().equals(coreAnimation)) {
                animationAction.completeCallbacks(model);
                listAnimationAction.remove(animationAction);
                return true;
            }
        }
        return false;
    }

    /**
	 * Updates all active animations.
	 *
	 * This function updates all active animations of the mixer instance for a
	 * given amount of time.
	 *
	 * @param deltaTime The elapsed time in seconds since the last update.
	 */
    public void updateAnimation(float deltaTime) {
        if (animationDuration == 0.0f) {
            animationTime = 0.0f;
        } else {
            animationTime += deltaTime * timeFactor;
            if (animationTime >= animationDuration || animationTime < 0) {
                animationTime = (float) Math.IEEEremainder(animationTime, animationDuration);
            }
            if (animationTime < 0) {
                animationTime += animationDuration;
            }
        }
        List<Animation> animationsToRemove = new LinkedList<Animation>();
        for (AnimationAction animationAction : listAnimationAction) {
            if (animationAction.update(deltaTime)) {
                animationAction.checkCallbacks(animationAction.getTime(), model);
            } else {
                animationAction.completeCallbacks(model);
                animationsToRemove.add(animationAction);
            }
        }
        float accumulatedWeight = 0.0f;
        float accumulatedDuration = 0.0f;
        for (AnimationCycle animationCycle : listAnimationCycle) {
            if (animationCycle.update(deltaTime)) {
                if (animationCycle.getState() == Animation.State.SYNC) {
                    accumulatedWeight += animationCycle.getWeight();
                    accumulatedDuration += animationCycle.getWeight() * animationCycle.getCoreAnimation().getDuration();
                }
                animationCycle.checkCallbacks(animationTime, model);
            } else {
                animationCycle.completeCallbacks(model);
                animationsToRemove.add(animationCycle);
            }
        }
        for (Animation animation : animationsToRemove) {
            listAnimationAction.remove(animation);
        }
        if (accumulatedWeight > 0.0f) {
            animationDuration = accumulatedDuration / accumulatedWeight;
        } else {
            animationDuration = 0.0f;
        }
    }

    public void updateSkeleton() {
        Skeleton skeleton = model.getSkeleton();
        if (skeleton == null) {
            return;
        }
        skeleton.clearState();
        Vector<Bone> vectorBone = skeleton.getVectorBone();
        for (AnimationAction animationAction : listAnimationAction) {
            CoreAnimation coreAnimation = animationAction.getCoreAnimation();
            List<CoreTrack> listCoreTrack = coreAnimation.getListCoreTrack();
            for (CoreTrack coreTrack : listCoreTrack) {
                Bone bone = vectorBone.get(coreTrack.getCoreBoneId());
                MutableVector3D translation = new MutableVector3D();
                MutableQuaternion rotation = new MutableQuaternion();
                coreTrack.getState(animationAction.getTime(), translation, rotation);
                bone.blendState(animationAction.getWeight(), translation, rotation);
            }
        }
        skeleton.lockState();
        for (AnimationCycle animationCycle : listAnimationCycle) {
            CoreAnimation coreAnimation = animationCycle.getCoreAnimation();
            float animationTime;
            if (animationCycle.getState() == Animation.State.SYNC) {
                if (animationDuration == 0.0f) {
                    animationTime = 0.0f;
                } else {
                    animationTime = this.animationTime * coreAnimation.getDuration() / animationDuration;
                }
            } else {
                animationTime = animationCycle.getTime();
            }
            List<CoreTrack> listCoreTrack = coreAnimation.getListCoreTrack();
            for (CoreTrack coreTrack : listCoreTrack) {
                Bone bone = vectorBone.get(coreTrack.getCoreBoneId());
                MutableVector3D translation = new MutableVector3D();
                MutableQuaternion rotation = new MutableQuaternion();
                coreTrack.getState(animationTime, translation, rotation);
                bone.blendState(animationCycle.getWeight(), translation, rotation);
            }
        }
        skeleton.lockState();
        skeleton.calculateState();
    }

    /**
	 * Returns the animation time.
	 *
	 * This function returns the animation time of the mixer instance.
	 *
	 * @return The animation time in seconds.
	 */
    public float getAnimationTime() {
        return animationTime;
    }

    /**
	 * Returns the animation duration.
	 *
	 * This function returns the animation duration of the mixer instance.
	 *
	 * @return The animation duration in seconds.
	 */
    public float getAnimationDuration() {
        return animationDuration;
    }

    /**
	 * Sets the animation time.
	 *
	 * This function sets the animation time of the mixer instance.
	 * 
	 * @param animationTime the new animation time
	 */
    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }

    /**
	 * Set the time factor.
	 * 
	 * This function sets the time factor of the mixer instance.
	 * this time factor affect only sync animation
	 * 
	 * @param timeFactor
	 */
    public void setTimeFactor(float timeFactor) {
        this.timeFactor = timeFactor;
    }

    /**
	 * Get the time factor.
	 * 
	 * This function return the time factor of the mixer instance.
	 * 
	 * @return 
	 */
    public float getTimeFactor() {
        return timeFactor;
    }

    /**
	 * Get the model.
	 * 
	 * This function return the CalModel of the mixer instance.
	 * @return
	 */
    public Model getCalModel() {
        return model;
    }

    /**
	 * Get the animation vector.
	 * 
	 * This function return the animation vector of the mixer instance.
	 * @return
	 */
    public Vector<Animation> getAnimationVector() {
        return vectorAnimation;
    }

    /**
	 * Get the list of the action animation.
	 * 
	 * This function return the list of the action animation of the mixer instance.
	 * @return
	 */
    public List<AnimationAction> getAnimationActionList() {
        return listAnimationAction;
    }

    /**
	 * Get the list of the cycle animation.
	 * 
	 * This function return the list of the cycle animation of the mixer instance.
	 * @return
	 */
    public List<AnimationCycle> getAnimationCycle() {
        return listAnimationCycle;
    }

    /**
	 * Examines the given animation and if the first and last keyframe of a given track
 	 * do not match up, the first key frame is duplicated and added to the end of the track
     * to ensure smooth looping.
	 */
    private static void addExtraKeyframeForLoopedAnim(final CoreAnimation coreAnimation) {
        List<CoreTrack> listCoreTrack = coreAnimation.getListCoreTrack();
        if (listCoreTrack.size() == 0) {
            return;
        }
        CoreTrack coreTrack0 = listCoreTrack.get(0);
        if (coreTrack0 == null) {
            return;
        }
        CoreKeyframe lastKeyframe = coreTrack0.getCoreKeyframe(coreTrack0.getCoreKeyframeCount() - 1);
        if (lastKeyframe == null) return;
        if (lastKeyframe.getTime() < coreAnimation.getDuration()) {
            for (CoreTrack coreTrack : listCoreTrack) {
                CoreKeyframe firstKeyframe = coreTrack.getCoreKeyframe(0);
                CoreKeyframe newKeyframe = new CoreKeyframe();
                newKeyframe.setTranslation(firstKeyframe.getTranslation());
                newKeyframe.setRotation(firstKeyframe.getRotation());
                newKeyframe.setTime(coreAnimation.getDuration());
                coreTrack.addCoreKeyframe(newKeyframe);
            }
        }
    }
}
