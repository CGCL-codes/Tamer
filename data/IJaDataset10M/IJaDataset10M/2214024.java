package org.jquery4jsf.custom.commandlink;

import java.lang.String;
import org.jquery4jsf.renderkit.JQueryBaseRenderer;
import javax.faces.context.FacesContext;

public abstract class CommandLinkBaseRenderer extends JQueryBaseRenderer {

    protected String encodeOptionComponent(StringBuffer options, CommandLink commandLink, FacesContext context) {
        options.append(" {\n");
        encodeOptionComponentByType(options, commandLink.getTarget(), "target", null);
        encodeOptionComponentByType(options, commandLink.getCharset(), "charset", null);
        encodeOptionComponentByType(options, commandLink.getCoords(), "coords", null);
        encodeOptionComponentByType(options, commandLink.getHreflang(), "hreflang", null);
        encodeOptionComponentByType(options, commandLink.getName(), "name", null);
        encodeOptionComponentByType(options, commandLink.getRel(), "rel", null);
        encodeOptionComponentByType(options, commandLink.getRev(), "rev", null);
        encodeOptionComponentByType(options, commandLink.getShape(), "shape", null);
        if (options.toString().endsWith(", \n")) {
            String stringa = options.substring(0, options.length() - 3);
            options = new StringBuffer(stringa);
        }
        boolean noParams = false;
        if (options.toString().endsWith(" {\n")) {
            String stringa = options.substring(0, options.length() - 3);
            options = new StringBuffer(stringa);
            noParams = true;
        }
        if (!noParams) {
            options.append(" }");
        }
        return options.toString();
    }
}
