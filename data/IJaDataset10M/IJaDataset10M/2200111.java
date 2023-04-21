package org.eclipse.help.internal.webapp.data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class calls eclipse API's directly, so it should only be instantiated in
 * the workbench scenario, not in the infocenter.
 */
public class ToolbarButton {

    private String name;

    private String tooltip;

    private String image;

    private String action;

    private String param;

    private String styleClass;

    private boolean state;

    private boolean isSeparator;

    public ToolbarButton() {
        isSeparator = true;
    }

    public ToolbarButton(String name, String tooltip, String image, String action, String param, String state) {
        this.name = name;
        this.tooltip = tooltip;
        this.image = image;
        this.action = action;
        this.param = param;
        this.state = state.equalsIgnoreCase("on") ? true : false;
        if (state.startsWith("hid")) this.styleClass = "buttonHidden"; else if ("menu".equals(action)) {
            this.styleClass = "buttonMenu";
        } else this.styleClass = state.equalsIgnoreCase("on") ? "buttonOn" : "button";
    }

    public boolean isSeparator() {
        return isSeparator;
    }

    public boolean isMenu() {
        return "menu".equals(action);
    }

    public String getName() {
        return name;
    }

    public String[][] getMenuData() {
        List list = new ArrayList();
        StringTokenizer tok = new StringTokenizer(param, ",");
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            int index = token.indexOf('=');
            list.add(new String[] { token.substring(0, index), token.substring(index + 1) });
        }
        return (String[][]) list.toArray(new String[list.size()][]);
    }

    public String getTooltip() {
        return tooltip;
    }

    /**
	 * Returns the enabled gray image
	 * 
	 * @return String
	 */
    public String getImage() {
        int i = image.lastIndexOf('/');
        return image.substring(0, i) + "/e_" + image.substring(i + 1);
    }

    /**
	 * Returns the image when selected
	 * 
	 * @return String
	 */
    public String getOnImage() {
        return getImage();
    }

    public String getAction() {
        return action;
    }

    public String getParam() {
        return param;
    }

    public boolean isOn() {
        return state;
    }

    public String getStyleClass() {
        return styleClass;
    }
}
