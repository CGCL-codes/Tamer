package br.inf.ufrgs.renderxml4web.rendering;

import br.inf.ufrgs.renderxml4web.rendering.components.UsiXMLJavaWebGridBoxRenderer;
import br.inf.ufrgs.renderxml4web.rendering.components.UsiXMLJavaWebInterfaceRenderer;
import br.inf.ufrgs.renderxml4web.rendering.components.UsiXMLJavaWebWindowRenderer;

/**
 * Factory class responsible for creating the first
 * renderer in the chain of responsability (GoF).
 * 
 * Can return the first renderer in line for the two 
 * existing type of renderers, element renderers and 
 * layout manager renderers
 */
public class RendererFactory {

    private static RendererFactory _instance;

    public static RendererFactory getInstance() {
        if (_instance == null) {
            _instance = new RendererFactory();
        }
        return _instance;
    }

    /**
     * Returns the first element renderer in the chain
     */
    public UsiXMLElementRenderer getFirstElementRenderer(UsiXMLJavaWebInterfaceRenderer usiXMLInterfaceRenderer) {
        return new UsiXMLJavaWebWindowRenderer(usiXMLInterfaceRenderer);
    }

    /**
     * Returns the first layout manager renderer in the chain
     */
    public UsiXMLLayoutManagerRenderer getFirstLayoutManagerRenderer(UsiXMLJavaWebInterfaceRenderer usiXMLInterfaceRenderer) {
        return new UsiXMLJavaWebGridBoxRenderer(usiXMLInterfaceRenderer);
    }
}
