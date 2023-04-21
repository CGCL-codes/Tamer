package com.leclercb.taskunifier.gui.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.SortOrder;
import org.apache.commons.lang3.StringEscapeUtils;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.commons.values.StringValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModelId;
import com.leclercb.taskunifier.gui.commons.values.StringValueModelOrder;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskLength;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskProgress;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeat;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.commons.values.StringValueTimer;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;

public final class TaskUtils {

    private TaskUtils() {
    }

    public static boolean isSortByOrder(TaskSorter sorter) {
        if (sorter.getElementCount() >= 1) if (sorter.getElement(0).getProperty() == TaskColumn.ORDER && sorter.getElement(0).getSortOrder() == SortOrder.ASCENDING) return true;
        return false;
    }

    public static void updateOrder(int index, Task[] tasksToOrder, Task[] displayedTasks) {
        Synchronizing.setSynchronizing(true);
        try {
            int newOrder = 0;
            if (index > 0 && index <= displayedTasks.length) newOrder = displayedTasks[index - 1].getOrder() + 1;
            List<Task> tasks = TaskFactory.getInstance().getList();
            main: for (Task task : tasks) {
                if (!task.getModelStatus().isEndUserStatus()) continue;
                for (Task t : tasksToOrder) if (EqualsUtils.equals(task, t)) continue main;
                for (Task t : displayedTasks) if (EqualsUtils.equals(task, t)) continue main;
                if (task.getOrder() >= newOrder) task.setOrder(task.getOrder() + 1 + tasksToOrder.length);
            }
            for (int i = 0; i < displayedTasks.length; i++) {
                Task task = displayedTasks[i];
                if (task == null) continue;
                if (i >= index) task.setOrder(task.getOrder() + 1 + tasksToOrder.length);
            }
            for (int i = 0; i < tasksToOrder.length; i++) {
                Task task = tasksToOrder[i];
                if (task == null) continue;
                task.setOrder(newOrder + i);
            }
        } finally {
            Synchronizing.setSynchronizing(false);
        }
    }

    public static boolean isInStartDateReminderZone(Task task) {
        if (task.getStartDate() == null) return false;
        if (task.isCompleted()) return false;
        if (task.getStartDateReminder() == 0) return false;
        Calendar startDate = task.getStartDate();
        if (!Main.getSettings().getBooleanProperty("date.use_start_time")) {
            startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        }
        Calendar now = Calendar.getInstance();
        Calendar startDateReminder = DateUtils.cloneCalendar(startDate);
        startDateReminder.add(Calendar.MINUTE, -task.getStartDateReminder());
        if (now.compareTo(startDateReminder) >= 0 && now.compareTo(startDate) <= 0) return true;
        Calendar exitDate = Main.getSettings().getCalendarProperty("general.last_exit_date");
        if (exitDate != null) if (now.compareTo(startDateReminder) >= 0 && exitDate.compareTo(startDateReminder) <= 0) return true;
        return false;
    }

    public static boolean isInDueDateReminderZone(Task task) {
        if (task.getDueDate() == null) return false;
        if (task.isCompleted()) return false;
        if (task.getDueDateReminder() == 0) return false;
        Calendar dueDate = task.getDueDate();
        if (!Main.getSettings().getBooleanProperty("date.use_due_time")) {
            dueDate.set(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        }
        Calendar now = Calendar.getInstance();
        Calendar dueDateReminder = DateUtils.cloneCalendar(dueDate);
        dueDateReminder.add(Calendar.MINUTE, -task.getDueDateReminder());
        if (now.compareTo(dueDateReminder) >= 0 && now.compareTo(dueDate) <= 0) return true;
        Calendar exitDate = Main.getSettings().getCalendarProperty("general.last_exit_date");
        if (exitDate != null) if (now.compareTo(dueDateReminder) >= 0 && exitDate.compareTo(dueDateReminder) <= 0) return true;
        return false;
    }

    public static int getOverdueTaskCount() {
        int count = 0;
        List<Task> tasks = TaskFactory.getInstance().getList();
        for (Task task : tasks) {
            if (!task.getModelStatus().isEndUserStatus()) continue;
            if (task.isCompleted()) continue;
            if (!task.isOverDue(false)) continue;
            count++;
        }
        return count;
    }

    public static String toText(Task[] tasks, TaskColumn[] columns, boolean html) {
        return toText(tasks, columns, html, null, null);
    }

    public static String toText(Task[] tasks, TaskColumn[] columns, boolean html, String header, String footer) {
        String[][] data = toStringData(tasks, columns);
        StringBuffer buffer = new StringBuffer();
        if (data == null) return null;
        if (html) buffer.append("<html>");
        if (header != null) buffer.append(header);
        int i = 0;
        for (String[] row : data) {
            if (i == 0) {
                i++;
                continue;
            }
            for (int j = 0; j < row.length; j++) {
                if (!html) {
                    buffer.append(data[0][j] + ": ");
                    buffer.append(row[j]);
                } else {
                    buffer.append("<b>" + data[0][j] + ":</b> ");
                    String text = row[j];
                    if (columns[j] == TaskColumn.NOTE) text = Text2HTML.convert(text); else text = StringEscapeUtils.escapeHtml3(text);
                    buffer.append(text);
                }
                if (!html) buffer.append(System.getProperty("line.separator")); else buffer.append("<br />");
            }
            if (!html) buffer.append(System.getProperty("line.separator")); else buffer.append("<hr /><br />");
            i++;
        }
        if (footer != null) buffer.append(footer);
        if (html) buffer.append("</html>");
        return buffer.toString();
    }

    public static String toHtml(Task[] tasks, TaskColumn[] columns) {
        String[][] data = toStringData(tasks, columns);
        StringBuffer buffer = new StringBuffer();
        if (data == null) return null;
        buffer.append("<html>");
        buffer.append("<table>");
        int i = 0;
        for (String[] row : data) {
            if (i == 0) buffer.append("<tr style=\"font-weight:bold;\">"); else buffer.append("<tr>");
            for (int j = 0; j < row.length; j++) {
                String text = row[j];
                if (columns[j] == TaskColumn.NOTE) text = Text2HTML.convert(text); else text = StringEscapeUtils.escapeHtml3(text);
                buffer.append("<td>" + text + "</td>");
            }
            buffer.append("</tr>");
            i++;
        }
        buffer.append("</table>");
        buffer.append("</html>");
        return buffer.toString();
    }

    public static String[][] toStringData(Task[] tasks, TaskColumn[] columns) {
        CheckUtils.isNotNull(tasks);
        CheckUtils.isNotNull(columns);
        boolean useDueTime = Main.getSettings().getBooleanProperty("date.use_due_time");
        boolean useStartTime = Main.getSettings().getBooleanProperty("date.use_start_time");
        List<String[]> data = new ArrayList<String[]>();
        int i = 0;
        String[] row = new String[columns.length];
        for (TaskColumn column : columns) {
            if (column == null) continue;
            row[i++] = column.getLabel();
        }
        data.add(row);
        for (Task task : tasks) {
            if (task == null) continue;
            i = 0;
            row = new String[columns.length];
            for (TaskColumn column : columns) {
                if (column == null) continue;
                String content = null;
                Object value = column.getProperty(task);
                switch(column) {
                    case CONTACTS:
                        content = (value == null ? null : value.toString());
                        break;
                    case COMPLETED:
                        content = StringValueBoolean.INSTANCE.getString(value);
                        break;
                    case COMPLETED_ON:
                        content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
                        break;
                    case CONTEXT:
                    case FOLDER:
                    case GOAL:
                    case LOCATION:
                    case PARENT:
                        content = StringValueModel.INSTANCE.getString(value);
                        break;
                    case DUE_DATE:
                        if (useDueTime) content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value); else content = StringValueCalendar.INSTANCE_DATE.getString(value);
                        break;
                    case FILES:
                        content = (value == null ? null : value.toString());
                        break;
                    case IMPORTANCE:
                        content = (value == null ? null : value.toString());
                        break;
                    case LENGTH:
                        content = StringValueTaskLength.INSTANCE.getString(value);
                        break;
                    case MODEL:
                        content = StringValueModelId.INSTANCE.getString(value);
                        break;
                    case MODEL_EDIT:
                        content = null;
                        break;
                    case MODEL_CREATION_DATE:
                        content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
                        break;
                    case MODEL_UPDATE_DATE:
                        content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
                        break;
                    case NOTE:
                        content = (value == null ? null : "\n" + value.toString());
                        break;
                    case ORDER:
                        content = StringValueModelOrder.INSTANCE.getString(value);
                        break;
                    case PRIORITY:
                        content = StringValueTaskPriority.INSTANCE.getString(value);
                        break;
                    case PROGRESS:
                        content = StringValueTaskProgress.INSTANCE.getString(value);
                        break;
                    case START_DATE_REMINDER:
                        content = StringValueTaskReminder.INSTANCE.getString(value);
                        break;
                    case DUE_DATE_REMINDER:
                        content = StringValueTaskReminder.INSTANCE.getString(value);
                        break;
                    case REPEAT:
                        content = StringValueTaskRepeat.INSTANCE.getString(value);
                        break;
                    case REPEAT_FROM:
                        content = StringValueTaskRepeatFrom.INSTANCE.getString(value);
                        break;
                    case SHOW_CHILDREN:
                        content = StringValueBoolean.INSTANCE.getString(value);
                        break;
                    case STAR:
                        content = StringValueBoolean.INSTANCE.getString(value);
                        break;
                    case START_DATE:
                        if (useStartTime) content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value); else content = StringValueCalendar.INSTANCE_DATE.getString(value);
                        break;
                    case STATUS:
                        content = StringValueTaskStatus.INSTANCE.getString(value);
                        break;
                    case TAGS:
                        content = (value == null ? null : value.toString());
                        break;
                    case TASKS:
                        content = (value == null ? null : value.toString());
                        break;
                    case TIMER:
                        content = StringValueTimer.INSTANCE.getString(value);
                        break;
                    case TITLE:
                        content = (value == null ? null : value.toString());
                        break;
                }
                if (content == null) content = "";
                row[i++] = content;
            }
            data.add(row);
        }
        return data.toArray(new String[0][]);
    }

    public static int getImportance(Task task) {
        CheckUtils.isNotNull(task);
        int importance = 2;
        switch(task.getPriority()) {
            case NEGATIVE:
                importance += -1;
                break;
            case LOW:
                importance += 0;
                break;
            case MEDIUM:
                importance += 1;
                break;
            case HIGH:
                importance += 2;
                break;
            case TOP:
                importance += 3;
                break;
        }
        importance += (task.isStar() ? 1 : 0);
        if (task.getDueDate() != null) {
            boolean useTime = Main.getSettings().getBooleanProperty("date.use_due_time");
            double diffDays = DateUtils.getDiffInDays(Calendar.getInstance(), task.getDueDate(), useTime);
            if (diffDays > 14) importance += 0; else if (diffDays >= 7) importance += 1; else if (diffDays >= 2) importance += 2; else if (diffDays >= 1) importance += 3; else if (diffDays >= 0) importance += 5; else importance += 6;
        }
        return importance;
    }

    public static boolean badgeTask(Task task, TaskFilter filter) {
        return showTask(task, filter, containsCompleted(filter), false, true, false);
    }

    public static boolean showTask(Task task, TaskFilter filter) {
        return showTask(task, filter, containsCompleted(filter), false, false, false);
    }

    public static boolean showUnindentTask(Task task, TaskFilter filter) {
        return showTask(task, filter, containsCompleted(filter), true, true, true);
    }

    private static boolean showTask(Task task, TaskFilter filter, boolean containsCompleted, boolean skipParentCheck, boolean skipShowChildren, boolean skipShowIfParentShown) {
        if (!task.getModelStatus().isEndUserStatus()) {
            return false;
        }
        if (!skipParentCheck) {
            boolean result = false;
            List<Task> children = task.getAllChildren();
            for (Task child : children) {
                if (showTask(child, filter, containsCompleted, false, true, skipShowIfParentShown)) {
                    result = true;
                    break;
                }
            }
            if (result) {
                if (!skipShowChildren) {
                    for (Task parent : task.getAllParents()) if (!((GuiTask) parent).isShowChildren()) return false;
                }
                return true;
            }
        }
        if (!skipShowChildren) {
            for (Task parent : task.getAllParents()) if (!((GuiTask) parent).isShowChildren()) return false;
        }
        if (!Main.getSettings().getBooleanProperty("tasksearcher.show_completed_tasks")) {
            if (task.isCompleted() && !containsCompleted) return false;
        }
        if (!skipShowIfParentShown && task.getParent() != null) {
            if (showTask(task.getParent(), filter, containsCompleted, true, skipShowChildren, skipShowIfParentShown)) return true;
        }
        if (filter == null) return true;
        return filter.include(task);
    }

    private static boolean containsCompleted(TaskFilter filter) {
        if (filter == null) return false;
        List<TaskFilterElement> elements = filter.getElements();
        List<TaskFilter> filters = filter.getFilters();
        for (TaskFilterElement e : elements) {
            if (e.getProperty() == TaskColumn.COMPLETED) {
                return true;
            }
        }
        for (TaskFilter f : filters) {
            if (containsCompleted(f)) return true;
        }
        return false;
    }
}
