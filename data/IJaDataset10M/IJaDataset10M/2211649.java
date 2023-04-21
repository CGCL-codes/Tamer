package kr.ac.ssu.imc.whitehole.report.designer.items.rdgraphs;

import java.awt.*;
import javax.swing.JPanel;

public class RDGLPanel extends JPanel {

    private RDGLegend legend;

    RDGLPanel(RDGLegend legend) {
        super();
        this.legend = legend;
    }

    RDGLPanel() {
        super();
    }

    public void setLegend(RDGLegend legend) {
        this.legend = legend;
    }

    public RDGLegend getLegend() {
        return this.legend;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        legend.drawChart(g2);
    }

    public void revalidateANDcalcDimen() {
        super.revalidate();
        this.legend.calcDimensions();
    }
}
