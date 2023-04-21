package de.bsd.x2svg.draw.renders;

/**
 * A marker interface to indicate that an object can be rendered 
 * by a <code>TreeRenderer</code> instance.
 * 
 * @author gfloodgate
 */
public interface Renderable {

    /**
	 * Render this object using the given <code>TreeRenderer</code> at the (x,y) point.
	 * 
	 * @param renderer The renderer instance that will be used to render.
	 * @param x The starting x-offset to render this element at.
	 * @param y The starting y-offset to render this element at.
	 */
    public void render(final TreeRenderer renderer, final int x, final int y);
}
