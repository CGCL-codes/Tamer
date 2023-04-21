package org.snipsnap.render.macro;

import org.radeox.util.logging.Logger;
import org.radeox.util.i18n.ResourceManager;
import org.snipsnap.render.macro.parameter.SnipMacroParameter;
import snipsnap.api.snip.SnipSpaceFactory;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class RecentChangesMacro extends ListOutputMacro {

    public String getName() {
        return "recent-changes";
    }

    public String getDescription() {
        return ResourceManager.getString("i18n.messages", "macro.recentchanges.description");
    }

    public String[] getParamDescription() {
        return ResourceManager.getString("i18n.messages", "macro.recentchanges.params").split(";");
    }

    public void execute(Writer writer, SnipMacroParameter params) throws IllegalArgumentException, IOException {
        String type = "Vertical";
        boolean showSize = false;
        int length = 10;
        if (params.getLength() > 0) {
            try {
                length = Integer.parseInt(params.get("0"));
            } catch (NumberFormatException e) {
                Logger.warn("RecentChangesMacro: illegal parameter count='" + params.get("1") + "'");
            }
        }
        if (params.getLength() > 1) {
            type = params.get("1");
        }
        if (params.getLength() <= 3) {
            List changed = SnipSpaceFactory.getInstance().getChanged(length);
            output(writer, params.getSnipRenderContext().getSnip(), ResourceManager.getString("i18n.messages", "macro.recentchanges.title"), changed, ResourceManager.getString("i18n.messages", "macro.recentchanges.nochanges"), type, showSize);
        } else {
            throw new IllegalArgumentException("Number of arguments does not match");
        }
    }
}
