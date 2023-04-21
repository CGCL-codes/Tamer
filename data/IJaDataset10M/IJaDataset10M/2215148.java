package com.icesoft.faces.component.outputdeclaration;

import com.icesoft.faces.context.DOMResponseWriter;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class OutputDeclarationRenderer extends DomBasicRenderer {

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Map attributes = component.getAttributes();
        String doctypeRoot = (String) attributes.get("doctypeRoot");
        String doctypePublic = (String) attributes.get("doctypePublic");
        String doctypeSystem = (String) attributes.get("doctypeSystem");
        String output = (String) attributes.get("output");
        String prettyPrinting = (String) attributes.get("prettyPrinting");
        Map requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(DOMResponseWriter.DOCTYPE_PUBLIC, doctypePublic);
        requestMap.put(DOMResponseWriter.DOCTYPE_SYSTEM, doctypeSystem);
        requestMap.put(DOMResponseWriter.DOCTYPE_ROOT, doctypeRoot);
        requestMap.put(DOMResponseWriter.DOCTYPE_OUTPUT, output);
        requestMap.put(DOMResponseWriter.DOCTYPE_PRETTY_PRINTING, prettyPrinting);
    }
}
