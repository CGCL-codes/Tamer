package com.xavax.jsf.renderer;

import java.io.IOException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import org.apache.log4j.Logger;

/**
 * ColumnRenderer is a JSF Renderer for the "com.xavax.Column" component
 * type. It renders an HTML table tag.
 */
public class ColumnRenderer extends TableElementRenderer {

    /**
   * Construct a ColumnRenderer.
   */
    public ColumnRenderer() {
        super(mylogger);
    }

    /**
   * Construct a ColumnRenderer using the specified Logger.
   *
   * @param logger   the Logger to use for this object.
   */
    protected ColumnRenderer(Logger logger) {
        super(logger);
    }

    /**
   * Returns the HTML tag name to be used when starting and ending
   * the element.
   *
   * @return the HTML tag name.
   */
    public String getTagName() {
        return "td";
    }

    /**
   * Encode the attributes of the HTML tag.
   *
   * @param context  the JSF context.
   * @param component  the UI component.
   * @param out  the response writer.
   */
    public void encodeAttributes(FacesContext context, UIComponent component, ResponseWriter out) throws IOException {
        String method = "encodeAttributes";
        boolean trace = logger.isTraceEnabled();
        if (trace) {
            enter(method);
        }
        Map attributes = component.getAttributes();
        String background = (String) attributes.get("background");
        if (background != null) {
            out.writeAttribute("background", background, "background");
        }
        String columnSpan = (String) attributes.get("columnSpan");
        if (columnSpan != null) {
            out.writeAttribute("colspan", columnSpan, "columnSpan");
        }
        String rowSpan = (String) attributes.get("rowSpan");
        if (rowSpan != null) {
            out.writeAttribute("rowspan", rowSpan, "rowSpan");
        }
        String width = (String) attributes.get("width");
        if (width != null) {
            out.writeAttribute("width", width, "width");
        }
        String height = (String) attributes.get("height");
        if (height != null) {
            out.writeAttribute("height", height, "height");
        }
        super.encodeAttributes(context, component, out);
        if (trace) {
            leave(method);
        }
    }

    private static Logger mylogger = Logger.getLogger(ColumnRenderer.class);
}
