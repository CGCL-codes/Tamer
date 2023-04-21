package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionAddSubTaskAtSameLevel extends ActionAddSubTask {

    public ActionAddSubTaskAtSameLevel() {
        this(32, 32);
    }

    public ActionAddSubTaskAtSameLevel(int width, int height) {
        super(width, height);
        this.putValue(NAME, Translations.getString("action.add_subtask_at_same_level"));
        this.putValue(SHORT_DESCRIPTION, Translations.getString("action.add_subtask_at_same_level"));
        this.putValue(ACCELERATOR_KEY, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ViewUtils.getSelectedTasks().length != 1) return;
        Task parent = ViewUtils.getSelectedTasks()[0];
        if (parent.getParent() != null) parent = parent.getParent();
        ActionAddSubTask.addSubTask(parent, true);
    }
}
