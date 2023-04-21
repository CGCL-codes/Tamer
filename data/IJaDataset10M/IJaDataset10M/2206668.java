package com.alexmcchesney.notifications;

/**
 * Interface for objects that are interested in being notified
 * when an animation terminates.
 * @author amcchesney
 *
 */
public interface IAnimationListener {

    public void onAnimationComplete();
}
