package ge.telasi.tasks.ui.task;

import ge.telasi.tasks.model.Task;
import ge.telasi.tasks.ui.UIUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * @author dimitri
 */
public class TaskRelationViewPanel extends JPanel {

    private Task task;

    private TaskRelationPanel pnParent;

    private TaskRelationPanel pnChild;

    public TaskRelationViewPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        pnParent = new TaskRelationPanel();
        pnChild = new TaskRelationPanel();
        pnParent.setBorder(BorderFactory.createTitledBorder("საფუძველი"));
        pnChild.setBorder(BorderFactory.createTitledBorder("ქვედავალებები"));
        pnChild.setToolbarVisible(false);
        UIUtils.correctTitleBorderFont(pnParent);
        UIUtils.correctTitleBorderFont(pnChild);
        removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(pnParent, c);
        add(pnChild, c);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
        pnParent.setTask(task, true);
        pnChild.setTask(task, false);
        pnParent.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("task".equals(evt.getPropertyName())) {
                    firePropertyChange("task", evt.getOldValue(), evt.getNewValue());
                }
            }
        });
    }
}
