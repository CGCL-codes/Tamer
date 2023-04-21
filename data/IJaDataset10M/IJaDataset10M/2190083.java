package ru.amse.ilyin.diagram;

/**
 * Link. Connects two elements on the diagram.
 *
 * @author Alexander Ilyin
 */
public interface Link {

    /**
	 * Draws a link.
	 * 
	 * @param renderer renderer to use.
	 */
    void draw(Renderer renderer);
}
