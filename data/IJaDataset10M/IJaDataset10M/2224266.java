package net.sf.myfacessandbox.components.validatinginput;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import net.sf.myfacessandbox.common.Attributes;
import net.sf.myfacessandbox.components.coloredinput.ColorInputTextTag;
import net.sf.myfacessandbox.components.coloredinput.ColorInputTextTagRenderer;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

/**
 * 
 * @author Werner Punz werpu@gmx.at a client validating input text which does
 *         red black validation red for wrong input black for correct one this
 *         one triggers on regepxs 
 *         TODO the coloring will be moved into tags in
 *         the long run
 *         TODO the javascripts have to be moved into
 *         separate js files, and be put on top of the prototype
 *         lib, that will make things more readable in the long run
 */
public class RegexpValInputTextRenderer extends ColorInputTextTagRenderer {

    public static final String RENDERER_TYPE = "mediaData.RegexpValInputText";

    String errorColor = "";

    String nonErrorColor = "";

    String focusColor = "";

    public void decode(FacesContext arg0, UIComponent arg1) {
        super.decode(arg0, arg1);
    }

    public void encodeEnd(FacesContext arg0, UIComponent arg1) throws IOException {
        super.encodeEnd(arg0, arg1);
    }

    public Object getConvertedValue(FacesContext arg0, UIComponent arg1, Object arg2) throws ConverterException {
        return super.getConvertedValue(arg0, arg1, arg2);
    }

    protected void renderInput(FacesContext facesContext, UIComponent component) throws IOException {
        String regExp = (String) component.getAttributes().get(Attributes.ATTR_CHECK_VALUE);
        String focusColor = (String) component.getAttributes().get(ColorInputTextTag.ATTR_FOCUS_COLOR);
        String nonFocusColor = (String) component.getAttributes().get(ColorInputTextTag.ATTR_NON_FOCUS_COLOR);
        Boolean focusColored = (Boolean) component.getAttributes().get(ColorInputTextTag.ATTR_FOCUS_COLORED);
        setErrorColor((String) component.getAttributes().get(Attributes.ATTR_ERROR_COLOR));
        setNonErrorColor((String) component.getAttributes().get(Attributes.ATTR_TEXT_COLOR));
        boolean hasRegExp = !((regExp == null) || (regExp.trim().equals("")));
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        String value = RendererUtils.getStringValue(facesContext, component);
        writer.startElement(HTML.INPUT_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_TEXT, null);
        if (hasRegExp) {
            writer.writeAttribute(HTML.ONKEYUP_ATTR, "javascript:document.getElementsByName('" + clientId + "')[0].check_input(this,'" + regExp + "');", null);
        }
        if (value != null) {
            writer.writeAttribute(HTML.VALUE_ATTR, value, JSFAttr.VALUE_ATTR);
        }
        HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, component)) {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
        if (focusColored != null && focusColored.booleanValue()) super.renderFocusColors(component, focusColor, nonFocusColor, writer);
        writer.endElement(HTML.INPUT_ELEM);
        if (hasRegExp) {
            renderRegexpValidator(component, writer, clientId);
        }
    }

    public void renderRegexpValidator(UIComponent component, ResponseWriter writer, String clientId) throws IOException {
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeText("document.getElementsByName('" + clientId + "')[0].check_input = function(inputControl,regexpString) {\n", null);
        writer.writeText("  if(!inputControl.value) return; \n", null);
        writer.writeText("	if(!inputControl.value.match(regexpString)) {\n", null);
        writer.writeText("		inputControl.style.color='" + getErrorColor() + "';\n", null);
        writer.writeText("	} else {\n", null);
        writer.writeText("		inputControl.style.color='" + getNonErrorColor() + "';\n", null);
        writer.writeText("	}\n", null);
        writer.writeText("}", null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    public String getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(String errorColor) {
        this.errorColor = errorColor;
    }

    public String getFocusColor() {
        return focusColor;
    }

    public void setFocusColor(String focusColor) {
        this.focusColor = focusColor;
    }

    public String getNonErrorColor() {
        return nonErrorColor;
    }

    public void setNonErrorColor(String nonErrorColor) {
        nonErrorColor = (nonErrorColor == null || nonErrorColor.trim().equals("")) ? "black" : nonErrorColor;
    }
}
