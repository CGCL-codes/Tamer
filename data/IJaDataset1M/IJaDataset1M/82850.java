package net.tralfamadore.component.content;

import net.tralfamadore.component.DynamicResourceLoader;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * User: billreh
 * Date: 1/6/11
 * Time: 11:45 AM
 */
@FacesRenderer(componentFamily = "javax.faves.Output", rendererType = "ContentResource")
public class ContentResourceRenderer extends Renderer {

    private static final String resourceLibrary = "cmfDynamicResources";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ContentResource content = (ContentResource) component;
        String name = content.getNamespace().replaceAll("\\.", "-") + "-" + content.getName() + ("style".equals(content.getType()) ? ".css" : ".js");
        String path = DynamicResourceLoader.encodeDynamicResource(context, resourceLibrary, name, content.getContent());
        context.getResponseWriter().write("<link type=\"text/css\" rel=\"stylesheet\" href=\"" + path + "\"/>");
    }
}
