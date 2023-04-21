package net.sf.myfacessandbox.components.ajax.autocompleteTextArea;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import net.sf.myfacessandbox.components.ajax.autocomplete.AjaxTextField;
import net.sf.myfacessandbox.components.ajax.autocomplete.AjaxTextFieldRenderer;

/**
 * Text area renderer for the ajaxed text area
 * 
 * @author Werner Punz werpu@gmx.at
 * $Log$
 * Revision 1.5  2005/08/14 22:17:00  werpu12
 * fixed a bug in the text area renderer which cause the values not to be displayed
 *
 */
public class AjaxTextareaRenderer extends AjaxTextFieldRenderer {

    /**
	 * callback which is called around the entire ajax infrastructure from the
	 * parent class
	 * 
	 * all this method has to do is to render itself and the surrounding ajax
	 * infrastructure
	 * 
	 */
    protected void renderInputElement(FacesContext facesContext, UIComponent component, ResponseWriter writer, String clientId, String value, String errorColor, Boolean multiselect, String nonErrorColor, String pattern, String menuId, String scriptVarId) throws IOException {
        AjaxTextarea ajaxcomponent = (AjaxTextarea) component;
        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get("styleClass");
        Integer width = ajaxcomponent.getWidth();
        Integer height = ajaxcomponent.getHeight();
        writer.startElement(HTML.TEXTAREA_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.WIDTH_ATTR, width.toString(), null);
        writer.writeAttribute(HTML.HEIGHT_ATTR, height.toString(), null);
        if (style != null && !style.trim().equals("")) writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        if (styleClass != null && !styleClass.trim().equals("")) writer.writeAttribute(HTML.STYLE_CLASS_ATTR, styleClass, null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT, null);
        HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, component)) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
        AjaxTextField comp = (AjaxTextField) component;
        String methodName = comp.getCompletionMethod();
        String startScript = scriptVarId + ".doCompletion(event,'" + clientId + "','" + menuId + "','" + methodName + "'," + ((comp.getOnchoose() != null) ? comp.getOnchoose() : "null") + "," + ((comp.getOndisplay() != null) ? comp.getOndisplay() : "null") + "," + ((pattern != null) ? pattern : "null") + "," + ((errorColor != null) ? errorColor : "null") + "," + ((nonErrorColor != null) ? nonErrorColor : "null") + "," + ((multiselect != null) ? ("" + multiselect.booleanValue()) : "null") + ");";
        String stopScript = scriptVarId + ".stopCompletionDelayed();";
        writer.writeAttribute(HTML.ONFOCUS_ATTR, startScript, null);
        writer.writeAttribute(HTML.ONKEYUP_ATTR, startScript, null);
        writer.writeAttribute(HTML.ONBLUR_ATTR, stopScript, null);
        if (value != null) {
            writer.write(value);
        }
        writer.endElement(HTML.TEXTAREA_ELEM);
    }

    public void encodeChildren(FacesContext arg0, UIComponent arg1) throws IOException {
        super.encodeChildren(arg0, arg1);
    }
}
