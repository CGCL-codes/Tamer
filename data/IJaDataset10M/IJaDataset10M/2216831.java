package org.interlogy.component;

import org.interlogy.help.HelpItem;
import org.interlogy.help.HelpSystem;
import org.jboss.seam.Component;
import javax.faces.component.UIComponentBase;

/**
 * JSF component class
 *
 */
public abstract class UIHelp extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.interlogy.Help";

    public static final String COMPONENT_FAMILY = "org.interlogy.Help";

    public abstract String getHelpKey();

    public abstract void setHelpKey(String helpKey);

    public abstract HelpItem getHelpItem();

    public abstract void setHelpItem(HelpItem helpItem);

    public String getKeyToCreate() {
        String ret = getHelpKey();
        if (ret == null) {
            ret = getHelpSystem().getCurrentViewId();
        }
        return ret;
    }

    public HelpItem getRealHelpItem() {
        HelpItem ret = getHelpItem();
        if (ret == null) {
            String key = getHelpKey();
            ret = getHelpSystem().getHelpItemForKey(key);
            if (ret == null) {
                ret = getHelpSystem().getHelpItem();
            }
        }
        return ret;
    }

    private HelpSystem getHelpSystem() {
        return (HelpSystem) Component.getInstance("helpSystem");
    }

    public String getViewUrl() {
        return "/help/help.seam?helpItemId=" + getRealHelpItem().getHelpItemId();
    }

    public String getCreateUrl() {
        return "/help/edithelp.seam?helpKey=" + getKeyToCreate();
    }

    public String getEditUrl() {
        return "/help/edithelp.seam?helpItemId=" + getRealHelpItem().getHelpItemId();
    }
}
