package au.edu.qut.yawl.editor.swing.data;

import javax.swing.table.AbstractTableModel;
import java.util.Iterator;
import java.util.SortedSet;
import au.edu.qut.yawl.editor.elements.model.YAWLFlowRelation;
import au.edu.qut.yawl.editor.elements.model.YAWLTask;

public class FlowPriorityTableModel extends AbstractTableModel {

    protected SortedSet prioritisedFlows;

    private static final String[] COLUMN_LABELS = { "Target Task", "Predicate" };

    public static final int TARGET_TASK_COLUMN = 0;

    public static final int PREDICATE_COLUMN = 1;

    public FlowPriorityTableModel() {
        super();
    }

    public FlowPriorityTableModel(YAWLTask task) {
        super();
        assert task.hasSplitDecorator() : "task does not have a split decorator";
        setTask(task);
    }

    public void setTask(YAWLTask task) {
        this.prioritisedFlows = task.getSplitDecorator().getFlowsInPriorityOrder();
        refresh();
    }

    public int getColumnCount() {
        return COLUMN_LABELS.length;
    }

    public String getColumnName(int columnIndex) {
        return COLUMN_LABELS[columnIndex];
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
        if (prioritisedFlows != null) {
            return prioritisedFlows.size();
        }
        return 0;
    }

    public void refresh() {
        fireTableRowsUpdated(0, Math.max(0, getRowCount() - 1));
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case TARGET_TASK_COLUMN:
                {
                    return getTaskNameAt(row);
                }
            case PREDICATE_COLUMN:
                {
                    return getPredicateAt(row);
                }
            default:
                {
                    return null;
                }
        }
    }

    public String getTaskNameAt(int row) {
        YAWLFlowRelation flow = getFlowAt(row);
        if (flow == null) {
            return "";
        }
        return flow.getTargetLabel();
    }

    public String getPredicateAt(int row) {
        YAWLFlowRelation flow = getFlowAt(row);
        return flow.getPredicate();
    }

    public YAWLFlowRelation getFlowAt(int row) {
        Iterator flowIterator = this.prioritisedFlows.iterator();
        int posn = 0;
        while (flowIterator.hasNext()) {
            YAWLFlowRelation flow = (YAWLFlowRelation) flowIterator.next();
            if (posn == row) {
                return flow;
            }
            posn++;
        }
        return null;
    }

    public void increasePriorityOfFlow(int row) {
        increaseFlowPriority(row);
    }

    public void decreasePriorityOfFlow(int row) {
        increaseFlowPriority(row + 1);
    }

    private void increaseFlowPriority(int row) {
        YAWLFlowRelation increasingFlow = getFlowAt(row);
        YAWLFlowRelation decreasingFlow = getFlowAt(row - 1);
        prioritisedFlows.remove(increasingFlow);
        prioritisedFlows.remove(decreasingFlow);
        increasingFlow.setPriority(row - 1);
        decreasingFlow.setPriority(row);
        prioritisedFlows.add(increasingFlow);
        prioritisedFlows.add(decreasingFlow);
        fireTableRowsUpdated(row - 1, row);
    }
}
