package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.leclercb.taskunifier.gui.components.tasktemplates.TaskTemplateConfigurationDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionManageTaskTemplates extends AbstractAction {

    public ActionManageTaskTemplates() {
        this(32, 32);
    }

    public ActionManageTaskTemplates(int width, int height) {
        super(Translations.getString("action.manage_task_templates"), ImageUtils.getResourceImage("template.png", width, height));
        this.putValue(SHORT_DESCRIPTION, Translations.getString("action.manage_task_templates"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionManageTaskTemplates.manageTemplates();
    }

    public static void manageTemplates() {
        TaskTemplateConfigurationDialog.getInstance().setVisible(true);
    }
}
