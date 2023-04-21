package org.pojosoft.lms.web.gwt.client.menu;

import org.pojosoft.ria.gwt.client.ui.SearchComposite;
import java.util.HashMap;

/**
 * Launches the Role Search page when the Role menu item is clicked.
 *
 * @author POJO Software
 */
public class RoleMenuCommand extends AbstractModuleSearchCommand {

    public void execute() {
        super.execute();
        render("RoleSearch", SearchComposite.SEARCH_TYPE_DEFAULT, new HashMap());
    }
}
