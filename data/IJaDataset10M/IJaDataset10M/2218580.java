package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.PanelTag;

public class PortalTag extends PanelTag {

    public PortalTag() {
        setXtype("portal");
    }

    public void prepareConfig() {
        super.prepareConfig();
    }

    public String prepareOnReadyFunction() {
        StringBuilder sb = new StringBuilder();
        sb.append("var panel = new Ext.ux.Portal(").append(this.toJSON().toString()).append(");");
        sb.append(getReplacePanel()).append(".replacePanel(panel);\n");
        return sb.toString();
    }
}
