package edu.umn.cs5115.scheduler.framework.gui;

/**
 * Transforms values.  Used by binders to format data for display.
 * @author grant
 */
public interface ValueTransformer {

    /**
     * Transforms a value.
     * @param value An object to transfrom the value of.
     * @return A transformed version of that value.
     */
    public Object transform(Object value);

    /**
     * Reverses the transformation of a value (if possible).  The ideal behavior
     * would be that reverseTransform(transform(value)).equals(value) would
     * be true.
     * 
     * @param value The value to reverse transform.
     * @return The reverse transformed value.
     */
    public Object reverseTransform(Object value);

    /**
     * Transformer that doesn't do anything.  It just returns it's input value
     * as the transformed value.
     */
    public ValueTransformer IDENTITY_TRANSFORM = new ValueTransformer() {

        public Object reverseTransform(Object value) {
            return value;
        }

        public Object transform(Object value) {
            return value;
        }
    };
}
