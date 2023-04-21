package org.jquery4jsf.custom.ajax;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jquery4jsf.javascript.JSAttribute;
import org.jquery4jsf.javascript.JSDocumentElement;
import org.jquery4jsf.javascript.JSElement;
import org.jquery4jsf.javascript.JSOperationElement;
import org.jquery4jsf.javascript.event.JSHideEvent;
import org.jquery4jsf.javascript.event.JSShowEvent;
import org.jquery4jsf.javascript.function.JSFunction;
import org.jquery4jsf.renderkit.RendererUtilities;
import org.jquery4jsf.utilities.MessageFactory;

public class AjaxStatusRenderer extends AjaxStatusBaseRenderer {

    private static final String[] FACETS = new String[] { "start", "error", "success", "complete", "stop" };

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) throw new NullPointerException(MessageFactory.getMessage("com.sun.faces.NULL_PARAMETERS_ERROR"));
        if (!component.isRendered()) return;
        AjaxStatus ajaxStatus = null;
        if (component instanceof AjaxStatus) ajaxStatus = (AjaxStatus) component;
        encodeResources(ajaxStatus);
        encodeAjaxStatusScript(context, ajaxStatus);
        encodeMarkupAjaxStatus(context, ajaxStatus);
    }

    private void encodeMarkupAjaxStatus(FacesContext context, AjaxStatus ajaxStatus) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        String clientId = ajaxStatus.getClientId(context);
        responseWriter.write("\n");
        responseWriter.startElement("div", null);
        responseWriter.writeAttribute("id", clientId, null);
        for (int i = 0; i < FACETS.length; i++) {
            String facetName = FACETS[i];
            encodeFacet(context, ajaxStatus, facetName);
        }
        responseWriter.endElement("div");
        responseWriter.write("\n");
    }

    private void encodeFacet(FacesContext context, AjaxStatus ajaxStatus, String facetName) throws IOException {
        UIComponent component = ajaxStatus.getFacet(facetName);
        if (component == null) return;
        ResponseWriter responseWriter = context.getResponseWriter();
        String clientId = ajaxStatus.getClientId(context);
        responseWriter.write("\n");
        responseWriter.startElement("span", null);
        responseWriter.writeAttribute("id", clientId + "_" + facetName, null);
        responseWriter.writeAttribute("style", "display:none", null);
        if (component != null) {
            RendererUtilities.renderChild(context, component);
        }
        responseWriter.endElement("span");
        responseWriter.write("\n");
    }

    private JSAttribute encodeAjaxStatusScriptFacet(FacesContext context, AjaxStatus ajaxStatus, String facetName, String facetEvent) {
        StringBuffer event = new StringBuffer();
        String clientId = ajaxStatus.getClientId(context);
        event.append("'" + facetEvent + "', ");
        JSAttribute attribute = new JSAttribute("bind", false);
        JSFunction function = new JSFunction();
        Map map = ajaxStatus.getFacets();
        if (map.containsKey(facetName)) {
            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); ) {
                String facetNameOut = (String) iterator.next();
                JSElement facetElement = new JSElement(clientId + "_" + facetNameOut);
                if (!facetNameOut.equals(facetName)) {
                    facetElement.addEvent(new JSHideEvent());
                } else {
                    facetElement.addEvent(new JSShowEvent());
                }
                function.addJSElement(facetElement);
            }
        } else {
            JSOperationElement operationElement = new JSOperationElement("");
            operationElement.addOperation(ajaxStatus.getScriptValueByEvent(facetName));
            function.addJSElement(operationElement);
        }
        event.append(function.toJavaScriptCode());
        attribute.addValue(event.toString());
        return attribute;
    }

    private void encodeAjaxStatusScript(FacesContext context, AjaxStatus ajaxStatus) throws IOException {
        JSDocumentElement documentElement = JSDocumentElement.getInstance();
        JSFunction function = new JSFunction();
        function.addJSElement(getJSElement(context, ajaxStatus));
        documentElement.addFunctionToReady(function);
    }

    public JSElement getJSElement(FacesContext context, UIComponent component) {
        AjaxStatus ajaxStatus = null;
        if (component instanceof AjaxStatus) ajaxStatus = (AjaxStatus) component;
        String clientId = ajaxStatus.getClientId(context);
        JSElement element = new JSElement(clientId);
        for (int i = 0; i < FACETS.length; i++) {
            String facetName = FACETS[i];
            if (isHandlerEvent(ajaxStatus, facetName)) {
                JSAttribute start = encodeAjaxStatusScriptFacet(context, ajaxStatus, facetName, getAjaxEvent(facetName));
                element.addAttribute(start);
            }
        }
        return element;
    }

    private String getAjaxEvent(String facet) {
        return "ajax".concat(facet.substring(0, 1).toUpperCase()).concat(facet.substring(1));
    }

    private boolean isHandlerEvent(AjaxStatus ajaxStatus, String event) {
        UIComponent facet = ajaxStatus.getFacet(event);
        if (facet == null && ajaxStatus.getScriptValueByEvent(event) == null) {
            return false;
        }
        return true;
    }
}
