package com.gr.staffpm.tasks.model;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import com.gr.staffpm.datatypes.TaskImportance;

/**
 * Tells a DropDownChoice how to display a TaskImportance.
 * @author Graham Rhodes 23 Dec 2010 19:28:56
 */
public class TaskImportanceChoiceRenderer implements IChoiceRenderer<TaskImportance> {

    private static final long serialVersionUID = 1L;

    @Override
    public String getDisplayValue(TaskImportance taskImp) {
        return taskImp.getValue();
    }

    @Override
    public String getIdValue(TaskImportance taskImp, int index) {
        return String.valueOf(taskImp.getImportanceId());
    }
}
