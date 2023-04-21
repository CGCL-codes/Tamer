package de.jaret.examples.timebars.linechart.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import de.jaret.util.ui.timebars.swt.TimeBarViewer;

/**
 * Control panel for the line chart example.
 * 
 * @author Peter Kliem
 * @version $Id: LineChartControlPanel.java 766 2008-05-28 21:36:48Z kliem $
 */
public class LineChartControlPanel extends Composite {

    private TimeBarViewer _tbv;

    public LineChartControlPanel(Composite parent, int style, TimeBarViewer tbv) {
        super(parent, style);
        _tbv = tbv;
        createControls(this);
    }

    /**
     * @param panel
     */
    private void createControls(LineChartControlPanel panel) {
        panel.setLayout(new RowLayout());
        final Scale pixPerSecondsScale = new Scale(this, SWT.HORIZONTAL);
        pixPerSecondsScale.setMaximum(700);
        pixPerSecondsScale.setMinimum(1);
        if (_tbv.getPixelPerSecond() * (24.0 * 60.0 * 60.0) > 700) {
            pixPerSecondsScale.setMaximum((int) (_tbv.getPixelPerSecond() * (24.0 * 60.0 * 60.0)));
        }
        pixPerSecondsScale.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent ev) {
                int val = pixPerSecondsScale.getSelection();
                double pps = ((double) val) / (24.0 * 60.0 * 60.0);
                System.out.println("scale " + val + "pps " + pps);
                _tbv.setPixelPerSecond(pps);
            }
        });
        pixPerSecondsScale.setSelection((int) (_tbv.getPixelPerSecond() * (24.0 * 60.0 * 60.0)));
        RowData rd = new RowData(800, 40);
        pixPerSecondsScale.setLayoutData(rd);
        final Button optScrollingCheck = new Button(this, SWT.CHECK);
        optScrollingCheck.setText("Use optimized scrolling");
        optScrollingCheck.setSelection(_tbv.getOptimizeScrolling());
        optScrollingCheck.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                _tbv.setOptimizeScrolling(optScrollingCheck.getSelection());
            }
        });
    }
}
