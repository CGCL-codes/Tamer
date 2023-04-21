package frostcode.icetasks.gui.task.column;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import frostcode.icetasks.gui.TableColumn;
import frostcode.icetasks.gui.project.ProjectComboBox;
import frostcode.icetasks.util.EventRestrictedCellEditor;

@Singleton
public class ProjectColumn extends TableColumn {

    @Inject
    private ProjectComboBox projectBox;

    @Override
    public TableCellEditor getEditor() {
        DefaultCellEditor projectEditor = new EventRestrictedCellEditor(projectBox);
        projectEditor.setClickCountToStart(2);
        return projectEditor;
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final Component defComponent) {
        if (row == (table.getRowCount() - 1)) defComponent.setEnabled(false);
        if (defComponent instanceof JLabel) {
            final JLabel label = (JLabel) defComponent;
            label.setToolTipText(null);
            label.setIcon(null);
        }
        return defComponent;
    }

    @Override
    public int getPreferredWidth() {
        return 75;
    }
}
