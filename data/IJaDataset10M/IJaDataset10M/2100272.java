package metric.gui.swt.core.decorator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import metric.core.report.decorator.ReportDecorator;
import metric.core.report.visitor.ReportVisitor;
import metric.core.vocabulary.Version;

/**
 * The GraphicalDecorator is an extention to the ReportDecorator meant specifically for graphical decorations.
 * Because all GUI implementation is in SWT at this time and JFreeChart is used for charting a GraphicalDecorator
 * should be used for JFreeChart decorations.
 * 
 * @author Joshua Hayes,Swinburne University (ICT),2007
 */
public abstract class GraphicalDecorator extends ReportDecorator {

    private Composite composite;

    private ChartComposite cc;

    private JFreeChart chart;

    public GraphicalDecorator(ReportVisitor decoratedReport) {
        super(decoratedReport);
    }

    public GraphicalDecorator(ReportVisitor decoratedReport, Composite composite) {
        super(decoratedReport);
        this.composite = composite;
    }

    @Override
    public void display() {
        if (chart == null) {
            chart = createChart();
        }
        if (composite != null) {
            if (cc == null || cc.isDisposed()) {
                cc = new ChartComposite(composite, SWT.NONE, chart);
            } else {
            }
        }
    }

    public void dispose() {
        cc.dispose();
    }

    protected abstract JFreeChart createChart();

    public JFreeChart getChart() {
        return chart;
    }
}
