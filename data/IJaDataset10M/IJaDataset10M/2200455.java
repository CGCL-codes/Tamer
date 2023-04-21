package org.pojosoft.lms.web.gwt.client.menu;

import org.pojosoft.ria.gwt.client.ui.ApplicationContext;
import org.pojosoft.ria.gwt.client.ui.ModuleComposite;
import java.util.HashMap;

/**
 * Launches the Learning History page when the Learning History menu item is clicked.
 *
 * @author POJO Software
 */
public class LearningHistoryMenuCommand extends AbstractModuleCommand {

    public void execute() {
        super.execute();
        HashMap params = new HashMap();
        params.put("modelObjectId", ApplicationContext.TOKEN_CURRENT_USER);
        render("LearningHistory", ModuleComposite.MODE_VIEW, params);
    }
}
