package gpsExtractor.gui;

import gpsExtractor.tools.chart.HeightsChart;
import gpsExtractor.tools.trk.TrackID;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChartPanel extends JPanel {

    private TrackID tid;

    private final ChartPanelMenu chartPopUpMenu = new ChartPanelMenu(this);

    public ChartPanel(TrackID tid) {
        this.tid = tid;
        this.setBackground(Color.white);
        this.addMouseListener(new PopupListener());
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        HeightsChart.createHeidhtsChart(tid, graphics, this.getSize());
    }

    private class PopupListener extends MouseAdapter {

        PopupListener() {
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                chartPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
