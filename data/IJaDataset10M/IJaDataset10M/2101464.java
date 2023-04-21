package org.thechiselgroup.choosel.core.client.util.callbacks;

import org.thechiselgroup.choosel.core.client.util.transform.Transformer;

/**
 * Wraps exceptions thrown during {@link Transformer#transform(Object)} and
 * contains the original value.
 * 
 * @author Lars Grammel
 */
public class TransformationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Object originalValue;

    public TransformationException(Object originalValue, Exception cause) {
        super("failed to transform '" + originalValue.toString() + "'", cause);
        this.originalValue = originalValue;
    }

    public Object getOriginalValue() {
        return originalValue;
    }
}
