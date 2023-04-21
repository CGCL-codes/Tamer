package org.axsl.linebreak;

/**
 * A "glue" item in the Knuth-Plass linebreaking model, conceptually a "spring", and sometimes referred to as a "skip".
 * Glue items are the items that can vary in size to get a line justified.
 * Whitespace between words is an example of an item that is generally considered to be glue.
 */
public interface LbGlue extends LbContent {

    /**
     * Returns the optimal width of this glue item, in millipoints.
     * @return The optimal width.
     */
    int getIdealWidth();

    /**
     * Returns the amount by which the ideal width of this item can be increased, in millipoints.
     * The maximum size of this glue item is the sum of {@link #getIdealWidth()} and the stretchability.
     * @return The stretchability.
     */
    int getStretchability();

    /**
     * Returns the amount by which the idial width of this item can be decreased, in millipoints.
     * The minimum size of this glue item is {@link #getIdealWidth()} less the shrinkability.
     * @return The shrinkability.
     */
    int getShrinkability();
}
