package org.jenia.faces.dynamic.renderkit.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jenia.faces.dynamic.component.html.HtmlCounterText;
import org.jenia.faces.util.Util;

public class CounterTextRenderer extends DynamicRenderer {

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        HtmlCounterText text = (HtmlCounterText) component;
        if (!text.isRendered()) {
            return;
        }
        UIForm uiform = getMyForm(context, text);
        if (uiform == null) {
            throw new RuntimeException("JSF h:Form needed to use this component");
        }
        ResponseWriter rw = context.getResponseWriter();
        commonJavascript(context, text);
        HtmlInputTextarea area = (HtmlInputTextarea) Util.getForComponent(context, text.getIdTextArea(), text);
        String textAreaId = area.getClientId(context);
        String jsFunctionName = textAreaId.replace(':', '_');
        rw.startElement("input", text);
        rw.writeAttribute("type", "text", null);
        rw.writeAttribute("readonly", "readonly", null);
        rw.writeAttribute("disabled", "disabled", null);
        rw.writeAttribute("name", text.getId(), null);
        rw.writeAttribute("id", text.getId(), null);
        if (text.getCounter() != null) {
            rw.writeAttribute("size", text.getCounter().toString().length(), null);
            rw.writeAttribute("maxlength", text.getCounter().toString().length(), null);
            int len = text.getCounter().intValue();
            try {
                String val = (String) area.getValue();
                rw.writeAttribute("value", Integer.toString(len - val.length()), null);
            } catch (Exception e) {
                rw.writeAttribute("value", Integer.toString(len), null);
            }
        }
        String cl = text.getStyleClass();
        if (cl != null && !cl.equals("")) {
            rw.writeAttribute("class", cl, "labelClass");
        }
        String style = text.getStyle();
        if (style != null) {
            rw.writeAttribute("style", style, "style");
        }
        String title = text.getTitle();
        if (title != null) {
            rw.writeAttribute("title", title, "title");
        }
        rw.endElement("input");
        rw.startElement("script", component);
        rw.writeAttribute("language", "JavaScript", null);
        rw.writeAttribute("type", "text/javascript", null);
        StringBuffer sb = new StringBuffer();
        sb.append("init" + jsFunctionName + "=window.onload;\n");
        sb.append("window.onload=function() {\n");
        sb.append("try{\n");
        sb.append("document.getElementById('" + textAreaId + "').onkeyup=function (event) {\n");
        sb.append("var field=document.getElementById('" + textAreaId + "');\n");
        sb.append("var cntfield=document.getElementById('" + text.getId() + "');\n");
        sb.append("var maxlimit=" + text.getCounter() + ";\n");
        sb.append("if (field.value.length>maxlimit) { ");
        sb.append("		field.value=field.value.substring(0,maxlimit);");
        sb.append("} else {\n");
        sb.append("		cntfield.value=maxlimit-field.value.length;\n");
        sb.append("}\n");
        sb.append("}\n");
        sb.append("}catch(error){}\n");
        sb.append("try{\n");
        sb.append("if (init" + jsFunctionName + "!=null) {\n");
        sb.append("		init" + jsFunctionName + "();\n");
        sb.append("}\n");
        sb.append("}catch(error){}\n");
        sb.append("}\n");
        rw.writeText(sb.toString(), null);
        rw.endElement("script");
        rw.flush();
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }
}
