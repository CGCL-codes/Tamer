package org.wam.layout;

/**
 * An element that may be split into multiple elements to accommodate layout constraints implements
 * this interface so that the layouts can perform these operations where appropriate. A prime
 * example of this functionality is text, which may be layed out around an image or other
 * obstruction.
 */
public interface Splittable {

    /**
	 * Splits a chunk of a given size off of this element.
	 * 
	 * @param width The width of the chunk to split off
	 * @param height The height of the chunk to split off
	 * @return An element that can accommodate the given size, or null if this element cannot be
	 *         split with the given arguments
	 */
    org.wam.core.WamElement split(int width, int height);

    /**
	 * Combines this element with another of the same type
	 * 
	 * @param split The element to combine with
	 */
    void combine(Splittable split);
}
