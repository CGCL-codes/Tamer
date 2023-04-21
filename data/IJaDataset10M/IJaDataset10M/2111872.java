package edu.washington.assist.gui.stateview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import edu.washington.assist.report.Report;
import edu.washington.assist.report.ReportDisplayPreferences;
import edu.washington.assist.report.ReportManager;
import edu.washington.assist.report.event.ReportSelectionEvent;
import edu.washington.assist.report.event.ReportSelectionListener;
import edu.washington.assist.report.event.ReportSelectionModel;

@SuppressWarnings("serial")
public class SelectionYLabelPanel extends YLabelPanel {

    private final ReportDisplayPreferences prefs;

    public SelectionYLabelPanel(ReportManager manager) {
        prefs = manager.getDisplayPrefs();
        ReportSelectionModel.getInstance().addSelectionListener(new ReportSelectionListener() {

            public void reportSelection(ReportSelectionEvent sre) {
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        Report selected = ReportSelectionModel.getInstance().getSelection();
        if (selected == null) {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
            return;
        }
        Color c = prefs.getReportColor(selected);
        assert (c != null);
        g.setColor(c);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
