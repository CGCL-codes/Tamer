package org.encog.workbench.tabs.visualize.weights;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;
import org.encog.neural.networks.structure.AnalyzeNetwork;
import org.encog.util.Format;
import org.encog.workbench.WorkbenchFonts;

public class WeightInfo extends JPanel {

    private AnalyzeWeightsTab owner;

    public WeightInfo(AnalyzeWeightsTab owner) {
        this.owner = owner;
    }

    public Dimension getPreferredSize() {
        return new Dimension(620, 100);
    }

    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setFont(WorkbenchFonts.getTitle2Font());
        FontMetrics fm = g.getFontMetrics();
        int y = fm.getHeight();
        g.drawString("Total Connections:", 20, y);
        g.drawString("Disabled Connections:", 300, y);
        y += fm.getHeight();
        g.drawString("All:", 20, y);
        g.drawString("#:", 100, y);
        g.drawString("Low:", 175, y);
        g.drawString("High:", 300, y);
        g.drawString("Mean:", 410, y);
        g.drawString("S.Dev:", 530, y);
        y += fm.getHeight();
        g.drawString("Bias:", 20, y);
        g.drawString("#:", 100, y);
        g.drawString("Low:", 175, y);
        g.drawString("High:", 300, y);
        g.drawString("Mean:", 410, y);
        g.drawString("S.Dev:", 530, y);
        y += fm.getHeight();
        g.drawString("Weight:", 20, y);
        g.drawString("#:", 100, y);
        g.drawString("Low:", 175, y);
        g.drawString("High:", 300, y);
        g.drawString("Mean:", 410, y);
        g.drawString("S.Dev:", 530, y);
        y += fm.getHeight();
        AnalyzeNetwork analyze = this.owner.getAnalyze();
        y = fm.getHeight();
        g.setFont(WorkbenchFonts.getTextFont());
        g.drawString(Format.formatInteger(analyze.getTotalConnections()), 200, y);
        g.drawString(Format.formatInteger(analyze.getDisabledConnections()), 450, y);
        y += fm.getHeight();
        g.drawString(Format.formatInteger(analyze.getWeightsAndBias().getSamples()), 120, y);
        g.drawString(Format.formatDouble(analyze.getWeightsAndBias().getLow(), 6), 220, y);
        g.drawString(Format.formatDouble(analyze.getWeightsAndBias().getHigh(), 6), 350, y);
        g.drawString(Format.formatDouble(analyze.getWeightsAndBias().getMean(), 6), 460, y);
        g.drawString(Format.formatDouble(analyze.getWeightsAndBias().getStandardDeviation(), 6), 580, y);
        y += fm.getHeight();
        g.drawString(Format.formatInteger(analyze.getBias().getSamples()), 120, y);
        g.drawString(Format.formatDouble(analyze.getBias().getLow(), 6), 220, y);
        g.drawString(Format.formatDouble(analyze.getBias().getHigh(), 6), 350, y);
        g.drawString(Format.formatDouble(analyze.getBias().getMean(), 6), 460, y);
        g.drawString(Format.formatDouble(analyze.getBias().getStandardDeviation(), 6), 580, y);
        y += fm.getHeight();
        g.drawString(Format.formatInteger(analyze.getWeights().getSamples()), 120, y);
        g.drawString(Format.formatDouble(analyze.getWeights().getLow(), 6), 220, y);
        g.drawString(Format.formatDouble(analyze.getWeights().getHigh(), 6), 350, y);
        g.drawString(Format.formatDouble(analyze.getWeights().getMean(), 6), 460, y);
        g.drawString(Format.formatDouble(analyze.getWeights().getStandardDeviation(), 6), 580, y);
    }
}
