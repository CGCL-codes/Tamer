package org.ogre4j;

import org.xbig.base.*;

public interface IAnimationStateSet extends INativeObject, org.ogre4j.IAnimationAllocatedObject {

    /** 
    Create a new  instance. **/
    public org.ogre4j.IAnimationState createAnimationState(String animName, float timePos, float length, float weight, boolean enabled);

    /** **/
    public org.ogre4j.IAnimationState getAnimationState(String name);

    /** **/
    public boolean hasAnimationState(String name);

    /** **/
    public void removeAnimationState(String name);

    /** **/
    public void removeAllAnimationStates();

    /** 
    Get an iterator over all the animation states in this set. **/
    public void getAnimationStateIterator(org.ogre4j.IAnimationStateIterator returnValue);

    /** **/
    public void getAnimationStateIterator_const(org.ogre4j.IConstAnimationStateIterator returnValue);

    /** **/
    public void copyMatchingState(org.ogre4j.IAnimationStateSet target);

    /** **/
    public void _notifyDirty();

    /** **/
    public long getDirtyFrameNumber();

    /** **/
    public void _notifyAnimationStateEnabled(org.ogre4j.IAnimationState target, boolean enabled);

    /** **/
    public boolean hasEnabledAnimationState();

    /** 
    Get an iterator over all the enabled animation states in this set **/
    public void getEnabledAnimationStateIterator(org.ogre4j.IConstEnabledAnimationStateIterator returnValue);
}
