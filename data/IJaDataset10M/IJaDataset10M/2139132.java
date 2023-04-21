package sk.tuke.ess.lib.gui.output;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.ChartEditor;
import org.jfree.chart.editor.ChartEditorManager;
import sk.tuke.ess.lib.output.Graph;
import sk.tuke.ess.lib.output.GraphScope;

/**
 *
 * @author Marek
 */
public class MyChartPanel extends ChartPanel {

    private ChartEditor editor;

    private JFreeChart chart;

    private ChartStepTab steptab;

    private Graph graph;

    public MyChartPanel(JFreeChart chart, Graph graph, boolean properties, boolean save, boolean print, boolean zoom, boolean tooltips) {
        super(chart, properties, save, print, zoom, tooltips);
        this.chart = chart;
        this.graph = graph;
        editor = ChartEditorManager.getChartEditor(chart);
        JComponent component = (JComponent) editor;
        JTabbedPane pane = (JTabbedPane) ((JPanel) component.getComponent(0)).getComponent(0);
        steptab = new ChartStepTab();
        steptab.setStep(graph.getStep());
        pane.addTab("Step", steptab);
    }

    @Override
    public void doEditChartProperties() {
        int result = JOptionPane.showConfirmDialog(this, editor, localizationResources.getString("Chart_Properties"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            graph.setStep(steptab.getStep());
            editor.updateChart(chart);
        }
    }
}
