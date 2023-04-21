package org.apache.myfaces.trinidadinternal.renderkit.htmlBasic;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidadinternal.ui.AttributeKey;
import org.apache.myfaces.trinidadinternal.ui.MutableUINode;
import org.apache.myfaces.trinidadinternal.ui.Renderer;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidadinternal.ui.laf.base.PreAndPostRenderer;
import org.apache.myfaces.trinidadinternal.uinode.FacesRenderingContext;

/**
 * Base class for using a UINode to render a standard JSF component.
 * @todo Share common base class with UINodeRendererBase
 * @todo There's no pushing or popping of the UINode stack here.
 * <p>
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 */
public abstract class UINodeRenderer extends javax.faces.render.Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!getRendersChildren()) {
            UIXRenderingContext rContext = getRenderingContext(context, component);
            UINode node = createUINode(context, component);
            Renderer renderer = _getRenderer(rContext, node);
            assert (renderer instanceof PreAndPostRenderer);
            ((PreAndPostRenderer) renderer).prerender(rContext, node);
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) {
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        UIXRenderingContext rContext = getRenderingContext(context, component);
        if (getRendersChildren()) {
            createUINode(context, component).render(rContext);
        } else {
            UINode node = createUINode(context, component);
            Renderer renderer = _getRenderer(rContext, node);
            assert (renderer instanceof PreAndPostRenderer);
            ((PreAndPostRenderer) renderer).postrender(rContext, node);
        }
    }

    protected abstract UINode createUINode(FacesContext context, UIComponent component);

    protected UIXRenderingContext getRenderingContext(FacesContext context, UIComponent component) throws IOException {
        return FacesRenderingContext.getRenderingContext(context, component);
    }

    /**
   * Sets an attribute if it has not already been set.
   * @todo doc why the heck anyone would call this!?!
   */
    protected void setAttribute(UIComponent component, String attrName, MutableUINode node, AttributeKey attrKey) {
        Object value = component.getAttributes().get(attrName);
        if (value != null) node.setAttributeValue(attrKey, value);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private Renderer _getRenderer(UIXRenderingContext rContext, UINode node) {
        return rContext.getRendererManager().getRenderer(node);
    }
}
