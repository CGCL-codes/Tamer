package mipt.crec.lab.common.modules.math;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import mipt.aaf.edit.form.data.ChoiceObjectByDataCreator;
import mipt.common.DoubleFormatter;
import mipt.crec.lab.common.modules.gui.GraphsModuleView;
import mipt.crec.lab.gui.Resources;
import mipt.data.Data;
import mipt.gui.DoubleTextField;
import mipt.gui.graph.Graph;
import mipt.gui.graph.function.LinearCurveRenderer;
import mipt.gui.graph.plot.Curve;
import mipt.gui.graph.plot.CurveStyle;
import mipt.gui.graph.plot.Plot;
import mipt.gui.graph.plot.legend.Legend;
import mipt.gui.graph.plot.legend.LegendRow;
import mipt.gui.graph.plot.legend.SimpleLegend;
import mipt.gui.graph.plot.legend.SimpleLegendRow;
import mipt.gui.graph.plot.legend.StandardLegendRow;
import mipt.gui.graph.primitives.LineStyle;
import mipt.gui.graph.primitives.dots.DotPrototype;
import mipt.gui.graph.primitives.dots.RhombDot;

/**
 * Reusable object responsible for creating/managing 2 plot:
 *  1) error depending on iteration;
 *  2) iteration count depending on iteration parameter (several curves with legend).
 * With AbstractSolutionModule, calls LinAESolver to add points to the second plot.
 * @author Evdokimov
 */
public class InexactitudePlots extends ErrorPlot {

    public InexactitudePlots(GraphsModuleView parent) {
        super(parent);
    }

    private Plot iterationPlot;

    private Legend legend;

    protected Dimension dotSize = new Dimension(9, 9);

    /**
	 * ����� �������� � ����������� �� ������������� ��������� (�������� �� ����������� �������
	 *  - ��������, actionPerformed � ��������� ����� �������� ������ ���������)
	 */
    public final Plot getIterationPlot() {
        if (iterationPlot == null) iterationPlot = createIterationPlot();
        return iterationPlot;
    }

    /**
	 * 
	 */
    protected Plot createIterationPlot() {
        Plot plot = parent.createPlot();
        plot.setToolTipText(parent.getString("SolutionIterationTip"));
        plot.setXName("tau");
        plot.setYName("iter");
        plot.getYAxisFormat().doubleFormat = new DoubleFormatter.FractionalFormat(0, false);
        return plot;
    }

    public Legend getLegend() {
        if (legend == null) legend = initLegend();
        return legend;
    }

    /**
	 * 
	 */
    protected Legend initLegend() {
        Legend legend = createLegend();
        legend.setColumnHeader(0, Resources.getInstance().getString(null, "ComputationsName"));
        legend.setFixedColumnsWidth(50);
        legend.setMaxVisibleRowCount(3);
        legend.addPlotUpdater(getIterationPlot());
        return legend;
    }

    /**
	 * Factory method 
	 */
    protected Legend createLegend() {
        return new SimpleLegend();
    }

    protected CurveStyle createCurveStyle(LegendRow row) {
        if (row instanceof SimpleLegendRow) {
            Color color = ((SimpleLegendRow) row).getColor();
            return new CurveStyle(new RhombDot(dotSize, true, color), new LineStyle(color));
        } else {
            StandardLegendRow r = (StandardLegendRow) row;
            DotPrototype dot = (DotPrototype) r.getDot();
            dot = dot.create(dot);
            dot.setStyle(true, r.getDotcolor());
            dot.size = dotSize;
            return new CurveStyle(dot, new LineStyle(r.getLinecolor()));
        }
    }

    protected void calcIterationsOnParameter(double param1, double param2) {
        LegendRow row = getLegend().addRow(getCurrentLegendName());
        LinearCurveRenderer renderer = new LinearCurveRenderer(null);
        getIterationPlot().addCurve(new Curve(renderer, createCurveStyle(row)));
        parent.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ((AbstractSolutionModule) parent.getModule()).calcIterationsOnParameter(renderer, param1, param2, getParameterCount());
    }

    protected String getCurrentLegendName() {
        AbstractSolutionModule module = (AbstractSolutionModule) parent.getModule();
        Data method = module.getLinAESolverData();
        String nameBase = module.getLinAESolverFieldName() + "." + method.getString("title");
        Data algorithm = ChoiceObjectByDataCreator.getSelectedData(method.getDataSet("algorithm"));
        String methodName = nameBase + ".algorithm." + algorithm.getString("title");
        Data convergence = ChoiceObjectByDataCreator.getSelectedData(method.getDataSet("convergence"));
        String convName = nameBase + ".convergence.falseConvergenceConsideration";
        double deviation = convergence.getData("constructor").getData("deviation").getDouble("constructor");
        String deviationName = parent.getString(convName + ".constructor.deviation.constructor");
        int i = deviationName.lastIndexOf(' ');
        if (i >= 0) deviationName = deviationName.substring(i + 1);
        StringBuffer result = new StringBuffer();
        result.append(parent.getString(methodName));
        result.append(" (");
        result.append(deviationName);
        result.append("=");
        result.append(Double.toString(deviation));
        if (convergence.getString("title").startsWith("false")) {
            result.append(", ");
            result.append(parent.getString(convName).toLowerCase());
        }
        result.append(")");
        return result.toString();
    }

    protected void deleteIterationsOnParameter() {
        getIterationPlot().removeAllCurves();
        getLegend().removeAll();
        layoutIterationPanel(getIterationPlot().getParent());
    }

    /**
	 * Called by AbstractSolutionModule n+1 times (the last with iteration = -1)
	 */
    public void iterationOnParameterCompleted(int iteration, boolean succeeded) {
        if (iteration < 0) {
            if (!succeeded) {
                int lastIndex = getIterationPlot().getCurvesCount() - 1;
                getIterationPlot().removeCurve(getIterationPlot().getCurve(lastIndex));
                getLegend().removeRow(lastIndex);
            }
            parent.getComponent().setCursor(Cursor.getDefaultCursor());
            layoutIterationPanel(getIterationPlot().getParent());
        } else {
            getIterationPlot().update();
            getIterationPlot().repaint();
        }
    }

    protected void layoutIterationPanel(Container panel) {
        panel.doLayout();
        getLegend().getTable().getParent().getParent().doLayout();
        panel.repaint();
    }

    /**
	 * ������ ��� ���������� ������� ����������� �� ����� �������� � �������������� ��������� �����
	 */
    public Component initIterationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(initIterationMinMaxPanel(), BorderLayout.NORTH);
        panel.add(getIterationPlot(), BorderLayout.CENTER);
        panel.add(new JScrollPane(getLegend().getTable()), BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(0, 0));
        return panel;
    }

    public Component initIterationMinMaxPanel() {
        Resources res = Resources.getInstance();
        JPanel minMax = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Insets ins1 = new Insets(4, 4, 4, 4), ins2 = new Insets(4, 0, 4, 4);
        c.insets = ins1;
        c.gridx = 0;
        minMax.add(new JLabel(res.getString(null, "MinIterationParameter")), c);
        c.gridx = 2;
        minMax.add(new JLabel(res.getString(null, "MaxIterationParameter")), c);
        final DoubleTextField field1 = initIterationField();
        final DoubleTextField field2 = initIterationField();
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                calcIterationsOnParameter(field1.getDoubleValue(), field2.getDoubleValue());
            }
        };
        field1.addActionListener(listener);
        field2.addActionListener(listener);
        field1.setDoubleValue(0.5);
        field1.setMin(Double.MIN_VALUE);
        field2.setDoubleValue(1.5);
        field2.setMax(2.0);
        field1.setMaxAndMin(field2);
        c.weightx = 1.;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.insets = ins2;
        minMax.add(field1, c);
        c.gridx = 3;
        minMax.add(field2, c);
        JButton deleteButton = new JButton(res.getIcon(null, "RemoveAll"));
        deleteButton.setToolTipText(res.getString(null, "RemoveAllCurvesTip"));
        deleteButton.setPreferredSize(new Dimension(20, 20));
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteIterationsOnParameter();
            }
        });
        c.insets = ins1;
        c.weightx = 0.;
        c.gridx = 4;
        minMax.add(deleteButton, c);
        return minMax;
    }

    protected DoubleTextField initIterationField() {
        DoubleTextField field = new DoubleTextField(0.1);
        field.setToolTipText(Resources.getInstance().getString(null, "IterationsOnParameterTip"));
        field.setDeltaIsFactor(false);
        return field;
    }

    @Override
    public Graph[] getGraphs() {
        return new Graph[] { getErrorPlot(), iterationPlot };
    }
}
