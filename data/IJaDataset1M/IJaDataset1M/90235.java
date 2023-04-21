package org.apache.myfaces.custom.focus;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import org.apache.myfaces.shared_impl.renderkit.RendererUtils;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;

/**
 * @author Rogerio Pereira Araujo (latest modification by $Author: rpa_rio $)
 * @version $Revision: 511 $ $Date: 2006-06-16 18:24:55 -0400 (Fri, 16 Jun 2006) $
 */
public class HtmlFocusRenderer extends Renderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlFocus.class);
        HtmlFocus focus = (HtmlFocus) uiComponent;
        ValueBinding vbFocus = focus.getValueBinding("for");
        String _for = (String) vbFocus.getValue(facesContext);
        if (_for == null) _for = (String) focus.getAttributes().get("for");
        if (_for == null) _for = focus.getFor();
        UIComponent targetComponent = focus.findComponent(_for);
        if (targetComponent != null) {
            String clientId = targetComponent.getClientId(facesContext);
            if (targetComponent instanceof org.apache.myfaces.custom.date.HtmlInputDate) clientId += ".day";
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
            writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            writer.writeText("document.getElementById('" + clientId + "').focus()", null);
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }
}
