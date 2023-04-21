package ge.telasi.tasks.ui.flowpermission;

import ge.telasi.tasks.model.Task;
import ge.telasi.tasks.ui.Platform;
import ge.telasi.tasks.ui.log.LoggerUtils;
import ge.telasi.tasks.ui.task.TaskDecontationUtils;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import static ge.telasi.tasks.model.Task.*;

/**
 * @author dimitri
 */
public class TaskStatusCombo extends JComboBox {

    public TaskStatusCombo() {
        setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Integer) {
                    int stat = (Integer) value;
                    setText(TaskDecontationUtils.getStatusName(stat));
                    setIcon(TaskDecontationUtils.getStatusIcon(stat));
                }
                return this;
            }
        });
    }

    public Integer getSelection() {
        Object selection = getSelectedItem();
        if (selection instanceof Integer) {
            return (Integer) selection;
        } else {
            return null;
        }
    }

    public void setSelection(Integer num) {
        setSelectedItem(num);
    }

    public void setDefaultModel() {
        setModel(new DefaultComboBoxModel(new Integer[] { STATUS_DRAFT, STATUS_OPEN, STATUS_PAUSED, STATUS_COMPLETE_OK, STATUS_COMPLETE_CANCEL, STATUS_CLOSED_OK, STATUS_CLOSED_CANCEL }));
    }

    public boolean setAllowedStatuses(Task task) {
        if (Platform.getDefault().validateConnection()) {
            try {
                Platform pl = Platform.getDefault();
                Integer[] statuses = pl.getFacade().getAllowedTaskStatuses(pl.getCredentials(), task);
                if (statuses != null && statuses.length > 0) {
                    setModel(new DefaultComboBoxModel(statuses));
                    return true;
                }
            } catch (Exception ex) {
                LoggerUtils.manageException(this, ex);
                return false;
            }
        }
        return false;
    }
}
