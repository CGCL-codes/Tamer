package net.sf.jqueryfaces.component.droppable;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import net.sf.jqueryfaces.util.JSFUtility;
import net.sf.jqueryfaces.util.JavaScriptFunction;

/**
 * This component <code>Droppable</code> holds all the options than can be
 * applied to the jQuery ui object.  It also handles the rendering of the
 * component.
 * 
 * @author Jeremy Buis
 */
public class Droppable extends UIComponentBase {

    protected static final String COMPONENT_TYPE = "net.sf.jqueryfaces.Droppable";

    protected static final String COMPONENT_FAMILY = "net.sf.jqueryfaces.Droppable";

    protected static final String STYLE = "style";

    protected static final String STYLECLASS = "styleClass";

    public static final String ACCEPT = "accept";

    public static final String ACTIVECLASS = "activeClass";

    public static final String ADDCLASSES = "addClasses";

    public static final String GREEDY = "greedy";

    public static final String HOVERCLASS = "hoverClass";

    public static final String SCOPE = "scope";

    public static final String TOLERANCE = "tolerance";

    public static final JavaScriptFunction ONACTIVATE = new JavaScriptFunction("activate");

    public static final JavaScriptFunction ONDEACTIVATE = new JavaScriptFunction("deactivate");

    public static final JavaScriptFunction ONOVER = new JavaScriptFunction("over");

    public static final JavaScriptFunction ONOUT = new JavaScriptFunction("out");

    public static final JavaScriptFunction ONDROP = new JavaScriptFunction("drop");

    /**
     * Default constructor
     */
    public Droppable() {
        super();
    }

    /**
      * @return Gets the <code>COMPONENT_FAMILY</code>
      */
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
      * @param context
      * @return
      */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[1];
        values[0] = super.saveState(context);
        return (values);
    }

    /**
      * @param context
      * @param state
      */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
    }

    /**
     * @param context
     */
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        JSFUtility.renderScriptOnce(writer, this, context);
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("$(document).ready(function(){\n", null);
        String clientId = getClientId(context);
        clientId = clientId.replace(":", "\\\\:");
        writer.writeText("$(\"#" + clientId + "\").droppable({", null);
        Map attr = this.getAttributes();
        JSFUtility.writeJSObjectOptions(writer, attr, Droppable.class);
        writer.writeText("});\n" + "});", null);
        writer.endElement("script");
        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(context), null);
        if (attr.get(STYLE) != null) {
            writer.writeAttribute("style", attr.get(STYLE), STYLE);
        }
        if (attr.get(STYLECLASS) != null) {
            writer.writeAttribute("class", attr.get(STYLECLASS), STYLECLASS);
        }
        writer.flush();
    }

    /**
      * @param context
      */
    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        writer.flush();
    }
}
