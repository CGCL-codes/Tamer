package org.apache.batik.dom.anim;

/**
 * A listener class for animation targets.  This will be for the animation
 * engine to be notified of updates to dependencies such as font size
 * changes and viewport sizes.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id: AnimationTargetListener.java 475477 2006-11-15 22:44:28Z cam $
 */
public interface AnimationTargetListener {

    /**
     * Invoked to indicate that base value of the specified attribute
     * or property has changed.
     */
    void baseValueChanged(AnimationTarget t, String ns, String ln, boolean isCSS);
}
