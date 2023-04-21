package org.apache.batik.gvt.renderer;

/**
 * Interface for GVT renderer factory.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: RendererFactory.java,v 1.1 2005/11/21 09:51:40 dev Exp $
 */
public interface RendererFactory {

    /**
     * Creates a new renderer.
     */
    Renderer createRenderer();
}
