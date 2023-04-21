package com.google.gwt.animation.client;

import com.google.gwt.core.client.GWT;

/**
 * Base class for animation implementations.
 */
abstract class AnimationSchedulerImpl extends AnimationScheduler {

    /**
   * The singleton instance of animation scheduler.
   */
    static final AnimationScheduler INSTANCE;

    static {
        AnimationScheduler impl = GWT.create(AnimationScheduler.class);
        if (impl instanceof AnimationSchedulerImpl) {
            if (!((AnimationSchedulerImpl) impl).isNativelySupported()) {
                impl = new AnimationSchedulerImplTimer();
            }
        }
        INSTANCE = impl;
    }

    /**
   * Check if the implementation is natively supported.
   * 
   * @return true if natively supported, false if not
   */
    protected abstract boolean isNativelySupported();
}
