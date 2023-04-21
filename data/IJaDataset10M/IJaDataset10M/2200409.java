package net.entropysoft.dashboard.plugin.chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Control that displays a chart
 * 
 * @author cedric
 *
 */
public class VariableChartControl extends Canvas {

    private VariableChartPainter variableChartPainter;

    public VariableChartControl(Composite parent, int style) {
        super(parent, style | SWT.DOUBLE_BUFFERED);
        addListener(SWT.Paint, new Listener() {

            public void handleEvent(Event event) {
                if (variableChartPainter != null) {
                    variableChartPainter.paint(event.gc, getBounds());
                }
            }
        });
        getDisplay().timerExec(1000, new Runnable() {

            public void run() {
                if (isDisposed()) return;
                redraw();
                getDisplay().timerExec(1000, this);
            }
        });
    }

    public IVariableChart getVariableChart() {
        return variableChartPainter.getVariableChart();
    }

    public void setVariableChart(IVariableChart variableChart) {
        if (variableChartPainter != null) {
            variableChartPainter.dispose();
        }
        this.variableChartPainter = new VariableChartPainter(variableChart);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (variableChartPainter != null) {
            variableChartPainter.dispose();
        }
    }
}
