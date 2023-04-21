package net.sf.fallfair.view.actions;

import net.sf.fallfair.view.abstracts.AAction;
import net.sf.fallfair.view.abstracts.APanel;
import net.sf.fallfair.view.utils.ResourceBundleWrapper;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 *
 * @author nathanj
 */
public class DivisionMaintenance extends AAction {

    /** Creates a new instance of DivisionMaintenance */
    public DivisionMaintenance() {
    }

    public void actionPerformed(ActionEvent e) {
        APanel tmpPanel;
        try {
            if (getMenu().containsComponent(clazz)) {
                getMenu().selectComponent(clazz);
            } else {
                tmpPanel = (APanel) (Class.forName(clazz).newInstance());
                getMenu().addPanel(tmpPanel, getTabName());
                getMenu().selectComponent(tmpPanel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getClazz() {
        return this.clazz;
    }

    public String getTabName() {
        ResourceBundleWrapper rbw = new ResourceBundleWrapper(ResourceBundle.getBundle(labelResource));
        return rbw.getString(tabName);
    }

    public static final String labelResource = "net.sf.fallfair.view.configs.labels";

    public static final String clazz = "net.sf.fallfair.view.DivisionMaintenance";

    public static final String tabName = "divisionMaintenance";
}
