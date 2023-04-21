package edu.washington.assist.gui.stateview;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import edu.washington.assist.report.Report;
import edu.washington.assist.report.ReportManager;
import edu.washington.assist.report.event.ReportSelectionModel;

/**
 * Implements report selection on behalf of row-per-report viewers such as
 * the activity viewer.
 * 
 * @author andrew
 *
 */
public class SelectionController extends MouseAdapter {

    private final JComponent component;

    private final ReportManager manager;

    public SelectionController(ReportManager manager, JComponent component) {
        this.component = component;
        this.manager = manager;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() >= 2) selectReport(me);
    }

    private void selectReport(MouseEvent me) {
        int y = me.getY();
        int numReports = manager.getNumEnabledReports();
        if (numReports <= 0) return;
        int rowHeight = component.getHeight() / numReports;
        if (rowHeight <= 0) return;
        int row = y / rowHeight;
        int i = 0;
        for (Report report : manager.getEnabledReports()) {
            if (i == row) {
                ReportSelectionModel.getInstance().setSelection(report);
                return;
            }
            i++;
        }
    }
}
