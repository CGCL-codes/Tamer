package com.google.gwt.animation.client;

import com.google.gwt.dom.client.Element;

/**
 * This class provides task scheduling for animations. Any exceptions thrown by
 * the command objects executed by the scheduler will be passed to the
 * {@link com.google.gwt.core.client.GWT.UncaughtExceptionHandler} if one is
 * installed.
 */
public abstract class AnimationScheduler {

    /**
   * The callback used when an animation frame becomes available.
   */
    public interface AnimationCallback {

        /**
     * Invokes the command.
     * 
     * @param timestamp the current timestamp
     */
        void execute(double timestamp);
    }

    /**
   * A handle to the requested animation frame created by
   * {@link #requestAnimationFrame(AnimationCallback, Element)}.
   */
    public abstract static class AnimationHandle {

        /**
     * Cancel the requested animation frame. If the animation frame is already
     * canceled, do nothing.
     */
        public abstract void cancel();
    }

    /**
   * Returns the default implementation of the AnimationScheduler API.
   */
    public static AnimationScheduler get() {
        return AnimationSchedulerImpl.INSTANCE;
    }

    /**
   * Schedule an animation, letting the browser decide when to trigger the next
   * step in the animation.
   * 
   * <p>
   * NOTE: If you are animating an element, use
   * {@link #requestAnimationFrame(AnimationCallback, Element)} instead so the
   * browser can optimize for the specified element.
   * </p>
   * 
   * <p>
   * Using this method instead of a timeout is preferred because the browser is
   * in the best position to decide how frequently to trigger the callback for
   * an animation of the specified element. The browser can balance multiple
   * animations and trigger callbacks at the optimal rate for smooth
   * performance.
   * </p>
   * 
   * @param callback the callback to fire
   * @return a handle to the requested animation frame
   * @see #requestAnimationFrame(AnimationCallback, Element)
   */
    public AnimationHandle requestAnimationFrame(AnimationCallback callback) {
        return requestAnimationFrame(callback, null);
    }

    /**
   * Schedule an animation, letting the browser decide when to trigger the next
   * step in the animation.
   * 
   * <p>
   * Using this method instead of a timeout is preferred because the browser is
   * in the best position to decide how frequently to trigger the callback for
   * an animation of the specified element. The browser can balance multiple
   * animations and trigger callbacks at the optimal rate for smooth
   * performance.
   * </p>
   * 
   * @param callback the callback to fire
   * @return a handle to the requested animation frame
   * @param element the element being animated
   */
    public abstract AnimationHandle requestAnimationFrame(AnimationCallback callback, Element element);
}
