package ar.uba.dc.rfm.dynalloy.visualization.gui;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import ar.uba.dc.rfm.alloy.AlloyVariable;
import ar.uba.dc.rfm.alloy.ast.expressions.AlloyExpression;
import ar.uba.dc.rfm.alloy.ast.expressions.ExprVariable;
import ar.uba.dc.rfm.alloy.ast.formulas.AlloyFormula;
import ar.uba.dc.rfm.alloy.ast.formulas.FormulaVisitor;
import ar.uba.dc.rfm.alloy.parser.ExpressionLexer;
import ar.uba.dc.rfm.alloy.parser.ExpressionParser;
import ar.uba.dc.rfm.alloy.util.ExpressionPrinter;
import ar.uba.dc.rfm.alloy.util.FormulaPrinter;
import ar.uba.dc.rfm.alloy.util.PrefixedExpressionPrinter;
import ar.uba.dc.rfm.alloy.util.VarCollector;
import ar.uba.dc.rfm.dynalloy.ast.programs.DynalloyProgram;
import ar.uba.dc.rfm.dynalloy.parser.DynalloyLexer;
import ar.uba.dc.rfm.dynalloy.parser.DynalloyParser;
import ar.uba.dc.rfm.dynalloy.parser.DynalloyProgramParseContext;
import ar.uba.dc.rfm.dynalloy.visualization.DynAlloySolution;
import ar.uba.dc.rfm.dynalloy.visualization.DynalloyExecutionTrace;
import ar.uba.dc.rfm.dynalloy.visualization.DynalloyExecutionTraceNull;
import ar.uba.dc.rfm.dynalloy.visualization.DynalloyVisualizerController;
import ar.uba.dc.rfm.dynalloy.visualization.NameSpace;

class WatchesTableModel extends AbstractTableModel {

    private static final int COLUMN_VALUE_INDEX = 1;

    private static final long serialVersionUID = 1L;

    private DynalloyVisualizerController controller;

    public WatchesTableModel(DynalloyVisualizerController controller) {
        super();
        data = new Vector<Column>();
        data.add(new Column("", ""));
        this.controller = controller;
        highlightsRows = new ArrayList<Integer>();
    }

    private class Column {

        public Column(String expression, String value) {
            this.expression = expression;
            this.value = value;
        }

        String expression;

        String value;
    }

    private String[] columnNames = { "expression", "value" };

    private final Vector<Column> data;

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Column column = data.get(row);
        if (col == 0) return column.expression; else return column.value;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        int lastIndexOf = data.size() - 1;
        String expression = (String) value;
        if ((expression.trim().equals("")) && (row != lastIndexOf)) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        } else if ((!expression.trim().equals("")) && (row == lastIndexOf)) {
            Column column = data.get(row);
            column.expression = expression;
            column.value = eval(expression);
            data.add(new Column("", ""));
            fireTableCellUpdated(row, col);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        } else {
            if (!expression.trim().equals("")) {
                Column column = data.get(row);
                column.expression = expression;
                column.value = eval(expression);
                fireTableCellUpdated(row, col);
            }
        }
    }

    private DynAlloySolution solution;

    private DynalloyExecutionTrace currentStep = null;

    private String eval(String expression) {
        String value;
        if (currentStep.getProgram() == null) {
            value = "<<Empty trace>>";
            return value;
        }
        ExpressionLexer lexer = new ExpressionLexer(new StringReader(expression));
        ExpressionParser parser = new ExpressionParser(lexer);
        Set<AlloyVariable> variables = new HashSet<AlloyVariable>();
        for (String name : this.currentStep.getNameSpace().varNames()) {
            variables.add(new AlloyVariable(name));
        }
        Set<AlloyVariable> fields = Collections.<AlloyVariable>emptySet();
        DynalloyProgramParseContext ctx = new DynalloyProgramParseContext(variables, fields, true);
        try {
            AlloyExpression expr = parser.termExpression(ctx);
            if (currentStep == null) {
                value = "<<Empty trace>>";
            } else if (currentStep.getProgram() == null) {
                value = "<<Empty Program>>";
            } else {
                Object eval = solution.evaluate(expr, currentStep);
                if (eval == null) value = "<<Undef>>"; else value = eval.toString();
            }
        } catch (RecognitionException e) {
            value = "<<Can't parse>>";
        } catch (TokenStreamException e) {
            value = "<<Can't parse>>";
        }
        return value;
    }

    private void updateEvaluations() {
        highlightsRows.clear();
        for (int columnLine = 0; columnLine < data.size() - 1; columnLine++) {
            Column column = data.get(columnLine);
            String expression = column.expression;
            String value = eval(expression);
            if (!column.value.equals(value)) highlightsRows.add(columnLine);
            column.value = value;
            fireTableCellUpdated(columnLine, COLUMN_VALUE_INDEX);
        }
    }

    private List<Integer> highlightsRows;

    public List<Integer> getHighlightsRows() {
        return highlightsRows;
    }

    public void setTraceNode(DynalloyExecutionTrace traceNode) {
        this.currentStep = traceNode;
        updateEvaluations();
    }

    public void setDynAlloySolution(DynAlloySolution solution) {
        this.solution = solution;
        DynalloyExecutionTrace step = solution.getExecutionTrace();
        setTraceNode(step);
    }
}
