package org.jquery4jsf.custom.droppable;

import java.lang.String;
import org.jquery4jsf.renderkit.JQueryBaseRenderer;
import javax.faces.context.FacesContext;

public class DroppableBaseRenderer extends JQueryBaseRenderer {

    protected String encodeOptionComponent(StringBuffer options, Droppable droppable, FacesContext context) {
        options.append(" {\n");
        encodeOptionComponentByType(options, droppable.getAccept(), "accept", null);
        encodeOptionComponentByType(options, droppable.getActiveClass(), "activeClass", null);
        encodeOptionComponentByType(options, droppable.isAddClasses(), "addClasses", "true");
        encodeOptionComponentByType(options, droppable.isGreedy(), "greedy", "false");
        encodeOptionComponentByType(options, droppable.getHoverClass(), "hoverClass", null);
        encodeOptionComponentByType(options, droppable.getScope(), "scope", null);
        encodeOptionComponentByType(options, droppable.getTolerance(), "tolerance", null);
        encodeOptionComponentFunction(options, droppable.getOndropactivate(), "ondropactivate", "event,ui");
        encodeOptionComponentFunction(options, droppable.getOndropdeactivate(), "ondropdeactivate", "event,ui");
        encodeOptionComponentFunction(options, droppable.getOndropover(), "ondropover", "event,ui");
        encodeOptionComponentFunction(options, droppable.getOndropout(), "ondropout", "event,ui");
        encodeOptionComponentFunction(options, droppable.getOndrop(), "ondrop", "event,ui");
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
