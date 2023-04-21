package ar.uba.dc.rfm.dynalloy.visualization.gui;

import java.util.Map;
import javax.swing.table.AbstractTableModel;
import ar.uba.dc.rfm.alloy.ast.expressions.AlloyExpression;
import ar.uba.dc.rfm.dynalloy.visualization.DynalloyExecutionTrace;

public class VariablesTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -2185571038437061079L;

    private DynalloyExecutionTrace traceNode;

    private Map<AlloyExpression, Object> traceValues;

    private static String[] COLUMN_NAMES = new String[] { "Variable", "Value" };

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return traceNode == null ? 0 : traceNode.getTraceValues().size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (traceNode == null) {
            return null;
        }
        Object[] keys = traceValues.keySet().toArray();
        if (column == 0) {
            return keys[row];
        } else {
            return traceValues.get(keys[row]);
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public void setTraceNode(DynalloyExecutionTrace traceNode) {
        this.traceNode = traceNode;
        this.traceValues = traceNode.getTraceValues();
        fireTableDataChanged();
    }

    public DynalloyExecutionTrace getTraceNode() {
        return traceNode;
    }
}
