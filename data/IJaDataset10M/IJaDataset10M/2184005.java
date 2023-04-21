package org.jquery4jsf.custom.resizable;

import java.lang.String;
import org.jquery4jsf.renderkit.JQueryBaseRenderer;
import javax.faces.context.FacesContext;

public abstract class ResizableBaseRenderer extends JQueryBaseRenderer {

    protected String encodeOptionComponent(StringBuffer options, Resizable resizable, FacesContext context) {
        options.append(" {\n");
        encodeOptionComponentByType(options, resizable.getAlsoResize(), "alsoResize", null);
        encodeOptionComponentByType(options, resizable.isAnimate(), "animate", null);
        encodeOptionComponentByType(options, resizable.getAnimateDuration(), "animateDuration", null);
        encodeOptionComponentByType(options, resizable.getAnimateEasing(), "animateEasing", null);
        encodeOptionComponentByType(options, resizable.getAspectRatio(), "aspectRatio", null);
        encodeOptionComponentByType(options, resizable.isAutoHide(), "autoHide", null);
        encodeOptionComponentByType(options, resizable.getCancel(), "cancel", null);
        encodeOptionComponentByType(options, resizable.getContainment(), "containment", null);
        encodeOptionComponentByType(options, resizable.getDelay(), "delay", null);
        encodeOptionComponentByType(options, resizable.getDistance(), "distance", null);
        encodeOptionComponentByType(options, resizable.isGhost(), "ghost", null);
        encodeOptionComponentByType(options, resizable.getGrid(), "grid", null);
        encodeOptionComponentByType(options, resizable.getHandles(), "handles", null);
        encodeOptionComponentByType(options, resizable.getHelper(), "helper", null);
        encodeOptionComponentByType(options, resizable.getMaxHeight(), "maxHeight", null);
        encodeOptionComponentByType(options, resizable.getMaxWidth(), "maxWidth", null);
        encodeOptionComponentByType(options, resizable.getMinWidth(), "minWidth", null);
        encodeOptionComponentByType(options, resizable.getMinHeight(), "minHeight", null);
        encodeOptionComponentFunction(options, resizable.getOnstart(), "onstart", "event,ui");
        encodeOptionComponentFunction(options, resizable.getOnresize(), "onresize", "event,ui");
        encodeOptionComponentFunction(options, resizable.getOnstop(), "onstop", "event,ui");
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
