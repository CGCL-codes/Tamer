package org.jenia.faces.datatools.renderkit.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jenia.faces.datatools.component.html.HtmlRowsChanger;
import org.jenia.faces.util.Util;

/**
 * @author Andrea Tessaro Porta
 */
public class RowsChangerRenderer extends DataToolsRenderer {

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        HtmlRowsChanger p = (HtmlRowsChanger) component;
        if (!p.isRendered()) {
            return;
        }
        String id = component.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        if (p.isDisabled()) {
            writer.startElement("div", component);
            if (p.getDisabledStyle() != null) writer.writeAttribute("style", p.getDisabledStyle(), null);
            if (p.getDisabledStyleClass() != null) writer.writeAttribute("class", p.getDisabledStyleClass(), null);
        } else {
            UIForm form = getMyForm(context, component);
            if (form == null) {
                throw new RuntimeException("JSF h:Form needed to use this component");
            }
            p.verifyRows();
            String formId = form.getClientId(context);
            writer.startElement("a", component);
            writer.writeAttribute("href", "#", null);
            writer.writeAttribute("onclick", onclickCode(formId, id, p.getRows()), null);
            if (p.getStyle() != null) writer.writeAttribute("style", p.getStyle(), null);
            if (p.getStyleClass() != null) writer.writeAttribute("class", p.getStyleClass(), null);
        }
    }

    private String onclickCode(String formId, String id, int value) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("document.getElementById('");
        buffer.append(formId);
        buffer.append("').elements['");
        buffer.append(id);
        buffer.append("'].value='");
        buffer.append(value);
        buffer.append("';");
        buffer.append("var ret=true;");
        buffer.append("if(document.getElementById('");
        buffer.append(formId);
        buffer.append("').onsubmit){ret=document.getElementById('");
        buffer.append(formId);
        buffer.append("').onsubmit();}");
        buffer.append("if(ret==true||ret==undefined)");
        buffer.append("document.getElementById('");
        buffer.append(formId);
        buffer.append("').submit(); ");
        buffer.append("document.getElementById('");
        buffer.append(formId);
        buffer.append("').elements['");
        buffer.append(id);
        buffer.append("'].value='';");
        buffer.append("return false;");
        return buffer.toString();
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        HtmlRowsChanger p = (HtmlRowsChanger) component;
        if (!p.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        if (p.isDisabled()) {
            writer.endElement("div");
        } else {
            writer.endElement("a");
        }
    }

    public void decode(FacesContext context, UIComponent component) {
        HtmlRowsChanger p = (HtmlRowsChanger) component;
        String id = component.getClientId(context);
        String response = (String) Util.getRequestParameter(id);
        if (response == null || response.equals("")) return;
        p.addFacesListener(p.new TableRowsListener());
        HtmlRowsChanger.TableRowsEvent event = p.new TableRowsEvent(component);
        p.queueEvent(event);
    }
}
