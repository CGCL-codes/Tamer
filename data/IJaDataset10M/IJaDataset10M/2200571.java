package net.entropysoft.dashboard.plugin.table;

import net.entropysoft.dashboard.plugin.chart.IVariableChart;
import net.entropysoft.dashboard.plugin.chart.VariableChartPainter;
import net.entropysoft.dashboard.plugin.chart.VariableXYChart;
import net.entropysoft.dashboard.plugin.variables.IValue;
import net.entropysoft.dashboard.plugin.variables.IVariable;
import net.entropysoft.dashboard.plugin.variables.IVariableDescription;
import net.entropysoft.dashboard.plugin.variables.TypeUtils;
import net.entropysoft.dashboard.plugin.variables.VariableManager;

/**
 * model for a row in the VariableTable
 * 
 * @author cedric
 * 
 */
public class VariableTableModelRow {

    private IVariable variable;

    private IValue value;

    private IVariableDescription variableDescription;

    private boolean isAvailable = false;

    private int level = 0;

    private VariableChartPainter chartPainter = null;

    private boolean showChildren = false;

    private int rowHeight = VariableTableModel.DEFAULT_TEXT_ROW_HEIGHT;

    private IVariableChart variableChart = null;

    private boolean isLatestKnownValue = false;

    private boolean isLatestKnownVariableDescription;

    public VariableTableModelRow(IVariable variable, int level) {
        this.variable = variable;
        this.level = level;
        VariableManager.getInstance().monitorVariable(variable, false);
    }

    public void update() {
        this.isAvailable = VariableManager.getInstance().isAvailable(variable);
        this.value = VariableManager.getInstance().getValue(variable);
        this.variableDescription = VariableManager.getInstance().getVariableDescription(variable);
        if (isAvailable && value == null) {
            this.value = VariableManager.getInstance().getLatestKnownValue(variable);
            isLatestKnownValue = true;
        } else {
            isLatestKnownValue = false;
        }
        if (isAvailable && variableDescription == null) {
            this.variableDescription = VariableManager.getInstance().getLatestKnownVariableDescription(variable);
            isLatestKnownValue = true;
        } else {
            isLatestKnownVariableDescription = false;
        }
    }

    /**
	 * if true, this means that the value could not be updated and that
	 * {@link #getValue()} returns the latest known value
	 * 
	 * @return
	 */
    public boolean isLatestKnownValue() {
        return isLatestKnownValue;
    }

    /**
	 * if true, this means that the variable description could not be updated
	 * and that {@link #getVariableDescription()} returns the latest known
	 * variable description
	 * 
	 * @return
	 */
    public boolean isLatestKnownVariableDescription() {
        return isLatestKnownVariableDescription;
    }

    /**
	 * get the variable associated with this row
	 * 
	 * @return
	 */
    public IVariable getVariable() {
        return variable;
    }

    /**
	 * get the value associated with this row
	 * 
	 * @return
	 */
    public IValue getValue() {
        return value;
    }

    /**
	 * get the variable description associated with this row
	 * 
	 * @return
	 */
    public IVariableDescription getVariableDescription() {
        return variableDescription;
    }

    /**
	 * check if the variable is available ie if it has been updated at least
	 * once
	 * 
	 * @return
	 */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
	 * get the level in the tree
	 * 
	 * @return
	 */
    public int getLevel() {
        return level;
    }

    /**
	 * get the chart for this row
	 * 
	 * @return
	 */
    public IVariableChart getVariableChart() {
        if (variableChart == null) {
            IVariableDescription variableDescription = VariableManager.getInstance().getVariableDescription(variable);
            if (variableDescription != null && TypeUtils.isNumericType(variableDescription.getTypeAsString())) {
                variableChart = new VariableXYChart(variable, false);
            }
        }
        return variableChart;
    }

    public VariableChartPainter getChartPainter() {
        return chartPainter;
    }

    /**
	 * display chart for this row (if possible). This modifies the row height
	 */
    public void displayChart(boolean display) {
        if (display) {
            IVariableChart variableChart = getVariableChart();
            if (variableChart != null) {
                chartPainter = new VariableChartPainter(getVariableChart());
                setRowHeight(VariableTableModel.DEFAULT_CHART_ROW_HEIGHT);
            }
        } else {
            if (chartPainter != null) {
                chartPainter.dispose();
                chartPainter = null;
                setRowHeight(VariableTableModel.DEFAULT_TEXT_ROW_HEIGHT);
            }
        }
    }

    /**
	 * check if a chart is displayed for this row
	 * 
	 * @return
	 */
    public boolean isChartDisplayed() {
        return chartPainter != null;
    }

    /**
	 * check if this row is expanded
	 * 
	 * @return
	 */
    public boolean isExpanded() {
        if (value == null) {
            return false;
        }
        IVariable[] children;
        try {
            children = value.getChildren();
        } catch (Exception e) {
            return false;
        }
        return showChildren && children.length > 0;
    }

    public void setExpanded(boolean showChildren) {
        IVariable[] children;
        try {
            children = value.getChildren();
        } catch (Exception e) {
            return;
        }
        if (children.length > 0) {
            this.showChildren = showChildren;
        }
    }

    /**
	 * check if this row is collapsed. If a row is neither expanded nor
	 * collapsed, it means it has no children
	 * 
	 * @return
	 */
    public boolean isCollapsed() {
        if (value == null) {
            return false;
        }
        IVariable[] children;
        try {
            children = value.getChildren();
        } catch (Exception e) {
            return false;
        }
        return !showChildren && children.length > 0;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public void dispose() {
        if (chartPainter != null) {
            chartPainter.dispose();
        }
        if (variableChart != null) {
            variableChart.dispose();
        }
        VariableManager.getInstance().unmonitorVariable(variable);
    }
}
