package org.jquery4jsf.custom.outputhtml;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jquery4jsf.renderkit.HtmlBasicRenderer;
import org.jquery4jsf.renderkit.html.HTML;
import org.jquery4jsf.utilities.MessageFactory;

public class OutputHtmlRenderer extends HtmlBasicRenderer {

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null || context == null) throw new NullPointerException(MessageFactory.getMessage("com.sun.faces.NULL_PARAMETERS_ERROR"));
        if (!component.isRendered()) return;
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.endElement(HTML.TAG_HTML);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (context == null || context == null) throw new NullPointerException(MessageFactory.getMessage("com.sun.faces.NULL_PARAMETERS_ERROR"));
        if (!component.isRendered()) return;
        OutputHtml outputHtml = null;
        if (component instanceof OutputHtml) outputHtml = (OutputHtml) component;
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement(HTML.TAG_HTML, outputHtml);
    }
}
