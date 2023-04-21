package org.webguitoolkit.ui.controls.form.button;

import java.io.PrintWriter;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * @author i102415
 *
 */
public class JQueryButton extends Button implements IButton {

    private static final String CONTEXT_TYPE_INIT = "buttonI";

    private static final String CONTEXT_TYPE_OPTION = "buttonO";

    private static final String CONTEXT_TYPE_ENABLE = "buttonE";

    public static final String DISPLAY_MODE_BUTTON = "DISPLAY_MODE_BUTTON";

    public static final String DISPLAY_MODE_LINK = "DISPLAY_MODE_LINK";

    public static final String DISPLAY_MODE_INPUT = "DISPLAY_MODE_INPUT";

    public String displayMode = DISPLAY_MODE_BUTTON;

    @Override
    protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3d) {
        if (DISPLAY_MODE_BUTTON.equals(displayMode)) {
            org.apache.ecs.html.Button button = new org.apache.ecs.html.Button();
            stdParameter(button);
            button.addElement(text);
            button.output(out);
        } else if (DISPLAY_MODE_LINK.equals(displayMode)) {
            org.apache.ecs.html.A button = new org.apache.ecs.html.A();
            stdParameter(button);
            button.addElement(text);
            button.setHref("#");
            button.output(out);
        } else if (DISPLAY_MODE_INPUT.equals(displayMode)) {
            org.apache.ecs.html.Input button = new org.apache.ecs.html.Input();
            stdParameter(button);
            button.setValue(text);
            button.output(out);
        } else {
            throw new RuntimeException("No valid displayMode: " + displayMode);
        }
        getContext().add(getId(), getInitOptions(), CONTEXT_TYPE_INIT, IContext.STATUS_COMMAND);
    }

    private String getInitOptions() {
        String result = "{";
        if (getSrc() != null) result += "\"icons\": { \"primary\": \"" + getSrc() + "\"},";
        if (StringUtils.isEmpty(getLabel())) result += "\"text\": \"false\",";
        result += "}";
        return result;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public void setDisabled(boolean disabled) {
    }

    public void setAlignment(int pos) {
    }

    protected void init() {
        getPage().addWgtCSS("standard/jquery-ui.css");
        getPage().addWgtJS(ResourceServlet.PREFIX_WGT_CONTROLLER + "button.js");
    }
}
