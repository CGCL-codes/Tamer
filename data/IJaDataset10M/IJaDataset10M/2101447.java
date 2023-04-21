package org.jquery4jsf.custom.syntaxhighlighting;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jquery4jsf.javascript.JSElement;
import org.jquery4jsf.renderkit.RendererUtilities;
import org.jquery4jsf.renderkit.html.HTML;
import org.jquery4jsf.utilities.MessageFactory;

public class SyntaxHighlightingRenderer extends SyntaxHighlightingBaseRenderer {

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) throw new NullPointerException(MessageFactory.getMessage("com.sun.faces.NULL_PARAMETERS_ERROR"));
        if (!component.isRendered()) return;
        SyntaxHighlighting syntax = null;
        if (component instanceof SyntaxHighlighting) syntax = (SyntaxHighlighting) component;
        encodeResources(syntax);
        encodeSyntaxHighlightingScript(context, syntax);
        encodeSyntaxHighlightingMarkup(context, syntax);
    }

    private void encodeSyntaxHighlightingMarkup(FacesContext context, SyntaxHighlighting syntax) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement(HTML.TAG_PRE, syntax);
        String styleClass = "brush: " + syntax.getType() + ";";
        responseWriter.writeAttribute("class", styleClass, null);
        RendererUtilities.renderChildren(context, syntax);
        responseWriter.endElement(HTML.TAG_PRE);
    }

    private void encodeSyntaxHighlightingScript(FacesContext context, SyntaxHighlighting syntax) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("\t\tSyntaxHighlighter.config.clipboardSwf = 'scripts/clipboard.swf';\n");
        sb.append("\t\tSyntaxHighlighter.all();\n");
        sb.append("\n");
        RendererUtilities.encodeImportJavascripScript(syntax, responseWriter, sb);
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    public JSElement getJSElement(FacesContext context, UIComponent component) {
        return null;
    }
}
