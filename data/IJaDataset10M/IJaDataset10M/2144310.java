package net.sf.daro.timetracker.viewer;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import net.sf.daro.binding.adapter.SingleListSelectionUpdater;
import net.sf.daro.core.viewer.AbstractViewer;
import net.sf.daro.core.viewer.ListViewer;
import net.sf.daro.core.viewer.Viewer;
import net.sf.daro.swing.JXTable;
import net.sf.daro.swing.table.FormatTableCellRenderer;
import net.sf.daro.timetracker.domain.Activity;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

/**
 * A viewer for a list of activities.
 * 
 * @author daniel
 */
public class ActivityListViewer extends AbstractViewer implements ListViewer {

    /**
	 * activity selection
	 */
    private SelectionInList<Activity> activitySelection;

    /**
	 * activity table
	 */
    private JXTable activityTable;

    /**
	 * Creates a new ActivityListViewer with an empty list model.
	 */
    public ActivityListViewer() {
        this(new SelectionInList<Activity>((List<Activity>) new ArrayListModel<Activity>()));
    }

    /**
	 * Creates a new ActivityListViewer with the given list model.
	 * 
	 * @param listModel
	 *            the list model
	 */
    public ActivityListViewer(SelectionInList<Activity> listModel) {
        this.activitySelection = listModel;
    }

    /**
	 * Returns the selected activity.
	 * 
	 * @return the selection
	 */
    public Activity getSelection() {
        return activitySelection.getSelection();
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Viewer#getValueModel()
	 */
    @Override
    public ValueModel getValueModel() {
        return activitySelection;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see ListViewer#getListModel()
	 */
    @Override
    public ListModel getListModel() {
        return activitySelection;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see AbstractViewer#createComponent()
	 */
    @Override
    protected JComponent createComponent() {
        initComponents();
        initComponentBindings();
        initComponentAnnotations();
        initEventHandling();
        return new JScrollPane(activityTable);
    }

    private void initComponents() {
        activityTable = new JXTable();
        activityTable.setName("ActivityTable");
        activityTable.setFillsViewportHeight(true);
        activityTable.setVisibleRowCount(9);
    }

    private void initComponentBindings() {
        activityTable.getSelectionModel().addListSelectionListener(new SingleListSelectionUpdater(activityTable, activitySelection.getSelectionIndexHolder()));
        activityTable.setModel(new ActivityTableModel(activitySelection));
        TableColumn column0 = activityTable.getColumnModel().getColumn(0);
        column0.setCellRenderer(new FormatTableCellRenderer(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)));
        TableColumn column1 = activityTable.getColumnModel().getColumn(1);
        column1.setCellRenderer(new FormatTableCellRenderer(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)));
        TableColumn column2 = activityTable.getColumnModel().getColumn(2);
        column2.setCellRenderer(new FormatTableCellRenderer(NumberFormat.getNumberInstance()));
    }

    private void initComponentAnnotations() {
    }

    private void initEventHandling() {
    }

    private class ActivityTableModel extends AbstractTableAdapter<Activity> {

        /**
		 * serial version UID
		 */
        private static final long serialVersionUID = -6649266317974403337L;

        /**
		 * Creates a new ActivityTableModel.
		 * 
		 * @param listModel
		 *            the list model
		 */
        public ActivityTableModel(ListModel listModel) {
            super(listModel, "Start Time", "Stop Time", "Duration", "Category", "Summary");
        }

        /**
		 * {@inheritDoc}
		 * 
		 * @see AbstractTableModel#getColumnClass(int)
		 */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                case 1:
                    return Date.class;
                case 2:
                    return Double.class;
                case 3:
                case 4:
                    return String.class;
                default:
                    throw new IllegalArgumentException("column " + columnIndex + " not supported");
            }
        }

        /**
		 * {@inheritDoc}
		 * 
		 * @see AbstractTableAdapter#getColumnName(int)
		 */
        @Override
        public String getColumnName(int columnIndex) {
            return getResourceBundle().getString("Column" + columnIndex + ".Name");
        }

        /**
		 * {@inheritDoc}
		 * 
		 * @see TableModel#getValueAt(int, int)
		 */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Activity rowValue = getRow(rowIndex);
            switch(columnIndex) {
                case 0:
                    return rowValue.getStartTime();
                case 1:
                    return rowValue.getStopTime();
                case 2:
                    return rowValue.getDuration(TimeUnit.MINUTES) / 60D;
                case 3:
                    return rowValue.getCategory();
                case 4:
                    return rowValue.getSummary();
                default:
                    throw new IllegalArgumentException("column " + columnIndex + " not supported");
            }
        }
    }
}
