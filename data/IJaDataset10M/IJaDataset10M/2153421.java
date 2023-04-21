package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Component;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;

public class TaskDueTodayHighlightPredicate implements HighlightPredicate {

    @Override
    public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
        Object value = adapter.getFilteredValueAt(adapter.row, adapter.getColumnIndex(TaskColumn.MODEL));
        if (value == null || !(value instanceof Task)) return false;
        Task task = (Task) value;
        boolean useDueTime = Main.getSettings().getBooleanProperty("date.use_due_time");
        return task.isDueToday(!useDueTime) && !task.isCompleted();
    }
}
