package org.nsu.learn.gui.client.dialog.view;

import org.nsu.learn.gui.client.window.model.TaskNameModel;
import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * @author makarov
 * @version 1.0.20.02.2011
 *
 */
public class TaskNameViewDialog extends AbstractViewDialog {

    public TaskNameViewDialog(TaskNameModel baseModel) {
        super(baseModel);
        setHeading("Просмотр обозначения задания \"" + baseModel.getTaskName() + "\"");
        addText("<h1>Обозначение задания</h1><br/>");
        addText("<b>ИД:</b> " + baseModel.getId());
        addText("<b>Обозначение задания: </b>" + baseModel.getTaskName());
    }
}
