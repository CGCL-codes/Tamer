package ru.spbspu.staub.components.renderkit;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import ru.spbspu.staub.components.html.HtmlSelectBooleanRadio;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Renders the current value of <code>UISelectBoolean<code> as a radio.
 *
 * @author Konstantin Grigoriev
 */
public class BooleanRadioRenderer extends HtmlBasicInputRenderer {

    public static final String RENDERER_TYPE = "ru.spbspu.BooleanRadio";

    private static final String[] ATTRIBUTES = new String[] { "accesskey", "dir", "lang", "onblur", "onchange", "onclick", "ondblclick", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onselect", "style", "tabindex", "title" };

    @Override
    public void decode(FacesContext context, UIComponent component) {
        rendererParamsNotNull(context, component);
        if (!shouldDecode(component)) {
            return;
        }
        String clientId = component.getClientId(context);
        assert (clientId != null);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String group = ((HtmlSelectBooleanRadio) component).getGroup();
        Boolean isSelected = Boolean.FALSE;
        if (group != null) {
            String selectedClientId = requestParameterMap.get(group);
            isSelected = clientId.equals(selectedClientId);
        }
        setSubmittedValue(component, String.valueOf(isSelected));
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "new value after decoding: {0}", isSelected);
        }
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        String newValue = (String) submittedValue;
        return Boolean.valueOf(newValue);
    }

    @Override
    protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        String styleClass;
        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("value", component.getClientId(context), null);
        writer.writeAttribute("type", "radio", "type");
        if ("true".equals(currentValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, "value");
        }
        String group = ((HtmlSelectBooleanRadio) component).getGroup();
        if (group != null) {
            writer.writeAttribute("name", group, "group");
        }
        if (null != (styleClass = (String) component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        RenderKitUtils.renderPassThruAttributes(writer, component, ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);
        writer.endElement("input");
    }
}
