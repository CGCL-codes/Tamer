package org.lightframework.mvc.render;

/**
 * a class implement this interface is renderable by itselft.
 * 
 * @author fenghm (live.fenghm@gmail.com)
 *
 * @since 1.0.0
 */
public interface IRenderable {

    /**
	 * encode <code>this</code> to string
	 * 
	 * @param context {@link IRenderContext}
	 * @return encoded result
	 */
    void encode(IRenderContext context, IRenderWriter writer, StringBuilder out);
}
