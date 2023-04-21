package be.vds.jtb.taskmanager.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import be.vds.jtb.taskmanager.controller.RunTasksAction;
import be.vds.jtb.taskmanager.controller.TasksManagerActionsController;
import be.vds.jtb.taskmanager.model.Task;
import be.vds.jtb.taskmanager.model.TaskChangedEvent;
import be.vds.jtb.taskmanager.model.TaskManagerFacade;
import be.vds.jtb.taskmanager.utils.ResourceManager;
import be.vds.jtb.taskmanager.view.util.WindowUtils;

public class ConfigurationPanel extends JXPanel implements Observer {

    private static final Logger logger = Logger.getLogger(ConfigurationPanel.class);

    private TaskManagerFacade facade;

    private JXTable tasksTable;

    private TasksTableModel tableModel;

    private TasksManagerActionsController controller;

    private Action runAction;

    public ConfigurationPanel(TasksManagerActionsController controller, TaskManagerFacade facade) {
        this.controller = controller;
        this.facade = facade;
        init();
        setTasks(facade.getTasks());
        facade.addObserver(this);
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(createButtonsPanel(), BorderLayout.NORTH);
        this.add(createTablePanel(), BorderLayout.CENTER);
    }

    private JComponent createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton newButton = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                TaskDialog dlg = new TaskDialog(WindowUtils.getParentFrame(ConfigurationPanel.this));
                dlg.setLocationRelativeTo(null);
                int i = dlg.showTask();
                if (i == TaskDialog.OPTION_OK) {
                    facade.addTask(dlg.getTask());
                }
            }
        });
        newButton.setIcon(ResourceManager.getInstance().getImageIcon("add24.png"));
        newButton.setToolTipText("New");
        JButton removeButton = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                facade.removeTasks(getSelectedTasks());
            }
        });
        removeButton.setIcon(ResourceManager.getInstance().getImageIcon("delete24.png"));
        removeButton.setToolTipText("Remove");
        JButton editButton = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                TaskDialog dlg = new TaskDialog(WindowUtils.getParentFrame(ConfigurationPanel.this));
                Task task = getSelectedTask();
                dlg.setTask(task);
                dlg.setLocationRelativeTo(null);
                int i = dlg.showTask();
                if (i == TaskDialog.OPTION_OK) {
                    facade.replaceTask(task, dlg.getTask());
                }
            }
        });
        editButton.setIcon(ResourceManager.getInstance().getImageIcon("edit24.png"));
        editButton.setToolTipText("Edit");
        runAction = controller.getAction(RunTasksAction.class.getName());
        JButton runButton = new JButton(runAction);
        runButton.setText(null);
        panel.add(newButton);
        panel.add(removeButton);
        panel.add(runButton);
        panel.add(editButton);
        return panel;
    }

    public List<Task> getSelectedTasks() {
        int[] rows = tasksTable.getSelectedRows();
        List<Task> result = new ArrayList<Task>();
        for (int index : rows) {
            result.add(tableModel.getTaskAt(tasksTable.convertRowIndexToModel(index)));
        }
        return result;
    }

    private Task getSelectedTask() {
        int row = tasksTable.getSelectedRow();
        return (Task) tableModel.getTaskAt(tasksTable.convertRowIndexToModel(row));
    }

    private JComponent createTablePanel() {
        tableModel = new TasksTableModel();
        tasksTable = new JXTable(tableModel);
        tasksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int i = tasksTable.getSelectedRow();
                    Task t = null;
                    if (-1 < i) {
                        t = tableModel.getTaskAt(tasksTable.convertRowIndexToModel(i));
                    }
                    runAction.setEnabled(t != null);
                    facade.taskSelected(t);
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tasksTable);
        return scroll;
    }

    public void setTasks(List<Task> tasks) {
        tableModel.setData(tasks);
        tasksTable.setModel(tableModel);
        tasksTable.packAll();
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof TaskChangedEvent) {
            TaskChangedEvent event = (TaskChangedEvent) arg;
            if (event.getTaskType().equals(TaskChangedEvent.TASKS_LOADED)) {
                setTasks(facade.getTasks());
            } else if (event.getTaskType().equals(TaskChangedEvent.TASKS_REPLACED)) {
                setTasks(facade.getTasks());
            } else if (event.getTaskType().equals(TaskChangedEvent.TASKS_REMOVED)) {
                removeTask((Set<Task>) event.getOldValue());
            } else if (event.getTaskType().equals(TaskChangedEvent.TASK_ADDED)) {
                addTask((Task) event.getNewValue());
            }
        }
    }

    private void addTask(Task task) {
        tableModel.addTask(task);
    }

    private void removeTask(Set<Task> tasks) {
        tableModel.removeTask(tasks);
    }
}
