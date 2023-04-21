package com.xtech.xerp.project;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.xtech.common.Utils;
import com.xtech.common.entities.TaskOwnerFilter;
import com.xtech.common.ui.ProjectTaskModel;

/**
 * @author jscruz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UserTaskFilterAction extends AbstractAction {

    ProjectTaskModel model;

    TaskOwnerFilter filter;

    public UserTaskFilterAction(ProjectTaskModel cm) {
        super("", Utils.loadIcon("filter_people.gif", "Filtrar"));
        this.model = cm;
        putValue(AbstractAction.NAME, "Mios");
        putValue(AbstractAction.SHORT_DESCRIPTION, "Ver solo las tareas propias");
        filter = new TaskOwnerFilter(model.getDbHelper().getUserID());
        model.addFilter(filter);
    }

    public void actionPerformed(ActionEvent e) {
        filter.setEnabled(!filter.isEnabled());
        model.refreshData();
    }

    /**
	 * @return
	 * @author jscruz
	 * @since XERP
	 */
    public ProjectTaskModel getModel() {
        return model;
    }

    /**
	 * @param model
	 * @author jscruz
	 * @since XERP
	 */
    public void setModel(ProjectTaskModel model) {
        this.model = model;
    }
}
