package dviz.visualSystem.impl;

import java.util.Collection;
import java.util.HashMap;

public class AniModelMap {

    HashMap<Object, AnimationObject> object2Animation;

    HashMap<AnimationObject, Object> animation2Model;

    public AniModelMap() {
        object2Animation = new HashMap<Object, AnimationObject>();
        animation2Model = new HashMap<AnimationObject, Object>();
    }

    public AnimationObject getAnimationObject(Object model) {
        return object2Animation.get(model);
    }

    public Collection<AnimationObject> getAnimationObjects() {
        return animation2Model.keySet();
    }

    public Collection<Object> getKeys() {
        return object2Animation.keySet();
    }

    public Object getModelObject(Object animation) {
        return animation2Model.get(animation);
    }

    public void put(Object model, AnimationObject animationObject) {
        object2Animation.put(model, animationObject);
        animation2Model.put(animationObject, model);
    }

    public void removeAnimation(AnimationObject animationObject) {
        Object model = animation2Model.get(animationObject);
        animation2Model.remove(animationObject);
        object2Animation.remove(model);
    }

    public void removeModelObject(Object obj) {
        Object model = object2Animation.get(obj);
        animation2Model.remove(model);
        object2Animation.remove(obj);
    }
}
