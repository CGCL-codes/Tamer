package org.apache.batik.dom.svg;

import org.apache.batik.anim.values.AnimatableValue;
import org.apache.batik.dom.anim.AnimationTarget;

/**
 * An interface for {@link LiveAttributeValue}s that have an animated value
 * component.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id: AnimatedLiveAttributeValue.java 489964 2006-12-24 01:30:23Z cam $
 */
public interface AnimatedLiveAttributeValue extends LiveAttributeValue {

    /**
     * Returns the namespace URI of this animated live attribute.
     */
    String getNamespaceURI();

    /**
     * Returns the local name of this animated live attribute.
     */
    String getLocalName();

    /**
     * Returns the base value of the attribute as an {@link AnimatableValue}.
     */
    AnimatableValue getUnderlyingValue(AnimationTarget target);

    /**
     * Adds a listener for changes to the animated value.
     */
    void addAnimatedAttributeListener(AnimatedAttributeListener aal);

    /**
     * Removes a listener for changes to the animated value.
     */
    void removeAnimatedAttributeListener(AnimatedAttributeListener aal);
}
