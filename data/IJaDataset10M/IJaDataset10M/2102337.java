package org.orbeon.faces.renderkit.xml;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Renderer for a UIInput Secret.
 */
public class InputSecretRenderer extends com.sun.faces.renderkit.html_basic.SecretRenderer {

    public InputSecretRenderer() {
    }

    protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue, StringBuffer buffer) {
        boolean redisplay = "true".equals(component.getAttribute("redisplay"));
        buffer.append(XmlRenderKitUtils.getStartElement("input_secret"));
        buffer.append(XmlRenderKitUtils.getAttribute("class", (String) component.getAttribute("inputClass")));
        buffer.append(XmlRenderKitUtils.getAttribute("id", component.getClientId(context)));
        if (redisplay) buffer.append(XmlRenderKitUtils.getAttribute("value", currentValue));
        buffer.append(XmlRenderKitUtils.getHtmlAttributes(context, component));
        buffer.append("/>");
    }
}
