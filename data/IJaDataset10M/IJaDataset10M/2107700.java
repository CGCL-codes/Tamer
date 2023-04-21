package com.gr.staffpm.pages.tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import com.gr.staffpm.datatypes.Group;
import com.gr.staffpm.datatypes.Role;
import com.gr.staffpm.datatypes.Task;
import com.gr.staffpm.datatypes.TaskImportance;
import com.gr.staffpm.datatypes.TaskStatus;
import com.gr.staffpm.datatypes.User;
import com.gr.staffpm.model.UserModel;
import com.gr.staffpm.pages.behaviour.FocusOnLoadBehavior;
import com.gr.staffpm.projects.model.TaskPermission;
import com.gr.staffpm.security.constants.CRUDTaskPermission;
import com.gr.staffpm.security.service.UserService;
import com.gr.staffpm.tasks.model.TaskImportanceChoiceRenderer;
import com.gr.staffpm.tasks.model.TaskStatusChoiceRenderer;
import com.gr.staffpm.tasks.model.UserChoiceRenderer;
import com.gr.staffpm.tasks.service.TaskService;
import com.gr.staffpm.widget.jqgrid.EditableGrid;
import com.gr.staffpm.widget.jqgrid.column.AbstractEditablePropertyColumn;
import com.gr.staffpm.widget.jqgrid.column.EditableCellPanel;
import com.gr.staffpm.widget.jqgrid.column.EditableDropDownCellPanel;

public class EditTaskForm extends Form<Task> {

    private static final long serialVersionUID = 1L;

    private final TaskService taskService;

    private final UserService userService;

    private final Task task;

    private final TextField<String> summary;

    private final DatePicker<Date> dueDate;

    private final DropDownChoice<TaskImportance> importance;

    private final DropDownChoice<TaskStatus> status;

    private final DropDownChoice<User> assignee;

    private final TextArea<String> description;

    public EditTaskForm(String id, final UserService userService, TaskService taskService, final Task task) {
        super(id, new CompoundPropertyModel<Task>(task));
        this.task = task;
        this.taskService = taskService;
        this.userService = userService;
        summary = new TextField<String>("summary", new Model<String>(task.getSummary()));
        summary.add(new FocusOnLoadBehavior());
        summary.setEscapeModelStrings(true);
        summary.setRequired(true);
        add(summary);
        dueDate = new DatePicker<Date>("dueDate", Date.class);
        dueDate.setEscapeModelStrings(true);
        dueDate.setRequired(true);
        add(dueDate);
        add(importance = new DropDownChoice<TaskImportance>("importance", new Model<TaskImportance>(task.getImportance()), taskService.getAllImportances(), new TaskImportanceChoiceRenderer()));
        importance.setRequired(true);
        add(status = new DropDownChoice<TaskStatus>("status", new Model<TaskStatus>(task.getStatus()), taskService.getAllStatuses(), new TaskStatusChoiceRenderer()));
        status.setRequired(true);
        User currUser = userService.getCurrentUser();
        Set<Group> groups = currUser.getGroups();
        add(assignee = new DropDownChoice<User>("assignee", new UserModel(userService.getCurrentUser()), userService.getUsersInGroupByGroupName(groups.iterator().next().getName(), currUser, userService.getUsersHighestLevel(currUser)), new UserChoiceRenderer()));
        assignee.setRequired(true);
        add(new EditableGrid<TaskPermission>("grid", getColumns(), new ArrayList<TaskPermission>(), new TaskPermission()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSave(TaskPermission pp, AjaxRequestTarget target) {
                userService.createPermission(pp.getRole(), pp.getPermission(), task);
            }

            @Override
            protected void onDelete(TaskPermission pp, AjaxRequestTarget target) {
                userService.deletePermission(pp.getPermission(), task);
            }
        });
        add(description = new TextArea<String>("description", new Model<String>(task.getDescription())));
        description.setEscapeModelStrings(true);
        Button cancel = new Button("cancelButton") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                setResponsePage(ViewTask.class, new PageParameters("id=" + String.valueOf(task.getTaskId())));
            }
        };
        cancel.setDefaultFormProcessing(false);
        add(cancel);
    }

    private List<AbstractEditablePropertyColumn<TaskPermission>> getColumns() {
        List<AbstractEditablePropertyColumn<TaskPermission>> columns = new ArrayList<AbstractEditablePropertyColumn<TaskPermission>>();
        columns.add(new AbstractEditablePropertyColumn<TaskPermission>(new Model<String>("Role"), "role") {

            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public EditableCellPanel getEditableCellPanel(String componentId) {
                return new EditableDropDownCellPanel(componentId, this, userService.getAllRoles(), new ChoiceRenderer<Role>("role", "roleId"));
            }
        });
        columns.add(new AbstractEditablePropertyColumn<TaskPermission>(new Model<String>("Permission"), "permission") {

            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public EditableCellPanel getEditableCellPanel(String componentId) {
                return new EditableDropDownCellPanel(componentId, this, CRUDTaskPermission.valuesAsList(), new ChoiceRenderer<CRUDTaskPermission>("name", "ordinal"));
            }
        });
        return columns;
    }

    /**
     * Called upon form submit. Attempts to authenticate the user.
     */
    @Override
    protected void onSubmit() {
        task.setSummary(summary.getValue());
        task.setAssignee(assignee.getModelObject());
        task.setDueDate(dueDate.getModelObject());
        task.setDescription(description.getValue());
        task.setImportance(importance.getModelObject());
        task.setStatus(status.getModelObject());
        task.setLastUpdated(Calendar.getInstance().getTime());
        task.setUpdatedBy(userService.getCurrentUser());
        taskService.updateTask(task);
        setResponsePage(ViewTask.class, new PageParameters("id=" + String.valueOf(task.getTaskId())));
    }
}
