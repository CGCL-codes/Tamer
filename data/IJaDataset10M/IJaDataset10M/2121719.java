package net.sf.jwan.jsf.form.renderer;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.render.Renderer;
import net.sf.jwan.jsf.form.ui.UIInputSelect;
import net.sf.jwan.jsf.form.ui.UISelectItems;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InputSelectRenderer extends Renderer {

    static Log logger = LogFactory.getLog(InputSelectRenderer.class);

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        String label = (String) component.getAttributes().get("label");
        UIInputSelect uiInputSelect = (UIInputSelect) component;
        List<?> lDm = (List<?>) component.getAttributes().get("value");
        DataModel dm = new ListDataModel(lDm);
        uiInputSelect.setValue(dm);
        logger.debug("uiInputSelect()=" + uiInputSelect.getRowCount());
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("li", component);
        writer.write(label);
        writer.startElement("span", component);
        writer.startElement("select", component);
        writer.writeAttribute("style", "float:right;margin:0px", null);
        writer.write(label);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("select");
        writer.endElement("span");
        writer.endElement("li");
        writer.write("\n");
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        UIInputSelect uiInputSelect = (UIInputSelect) component;
        if (component.getChildCount() == 0) {
            return;
        }
        for (UIComponent uiChild : uiInputSelect.getChildren()) {
            logger.debug(uiChild.getClass().getName());
            if (uiChild instanceof UISelectItems) {
                for (int index = 0; index < uiInputSelect.getRowCount(); index++) {
                    uiInputSelect.setRowIndex(index);
                    uiChild.encodeAll(context);
                }
            } else {
                uiChild.encodeAll(context);
            }
        }
    }
}
