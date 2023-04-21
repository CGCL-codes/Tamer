package com.zycus.dotproject.ui;

import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.ui.event.ViewChangeListener.ViewType;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.MenuUtility;

public class DotProjectPopupMenu extends JPopupMenu {

    private JMenuItem mnuAddChild = null;

    private JMenuItem mnuAddTaskLog = null;

    private JMenuItem mnuProperties = null;

    private JMenuItem mnuExpandAll = null;

    private JMenuItem mnuCollapseAll = null;

    private JPopupMenu.Separator mnuSeparator = null;

    private ProjectTaskArea projectTaskArea = null;

    public DotProjectPopupMenu(ProjectTaskArea projectTaskArea) {
        initComponents(projectTaskArea);
        this.projectTaskArea = projectTaskArea;
    }

    @Override
    public void show(Component invoker, int x, int y) {
        int selectedIndex = projectTaskArea.getSelectedRow();
        Object selectedObject = (selectedIndex >= 0) ? projectTaskArea.getValueAt(selectedIndex, -1) : null;
        mnuAddChild.setVisible(selectedIndex >= 0 && ApplicationContext.getViewTYpe() == ViewType.TreeView && canAddTask(selectedObject));
        mnuProperties.setVisible(selectedIndex > 0 && selectedObject instanceof BOTask);
        mnuAddTaskLog.setVisible(selectedIndex > 0 && (selectedObject instanceof BOTask) && ((BOTask) selectedObject).getChildTasks().size() <= 0 && ((BOTask) selectedObject).canAddTaskLog(ApplicationContext.getCurrentUser()));
        mnuSeparator.setVisible(mnuAddChild.isVisible() || mnuProperties.isVisible() || mnuAddTaskLog.isVisible());
        super.show(invoker, x, y);
    }

    private boolean canAddTask(Object currentObject) {
        if (currentObject == null) {
            return false;
        }
        if (currentObject instanceof BOProject) {
            return ((BOProject) currentObject).canAddTasks(ApplicationContext.getCurrentUser());
        }
        if (currentObject instanceof BOTask) {
            return ((BOTask) currentObject).canAddTasks(ApplicationContext.getCurrentUser());
        }
        return false;
    }

    private void initComponents(ProjectTaskArea projectTaskArea) {
        add(mnuAddChild = MenuUtility.getMenuItem("Add child", new ProjectTaskArea.AddChildAction(projectTaskArea)));
        add(mnuAddTaskLog = MenuUtility.getMenuItem("Add task log", new ProjectTaskArea.AddTaskLogAction(projectTaskArea)));
        add(mnuProperties = MenuUtility.getMenuItem("Task properties", new ProjectTaskArea.TaskDetailsAction(projectTaskArea)));
        add(mnuSeparator = new JPopupMenu.Separator());
        add(mnuExpandAll = MenuUtility.getMenuItem("Expand", new ProjectTaskArea.ExpandAllAction(projectTaskArea.getTree())));
        add(mnuCollapseAll = MenuUtility.getMenuItem("Collapse", new ProjectTaskArea.CollapseAllAction(projectTaskArea.getTree())));
    }
}
