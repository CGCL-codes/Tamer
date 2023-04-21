package com.jvito.plot.multivariat;

import java.awt.Font;
import java.awt.Graphics;
import com.jvito.data.ExampleColoring;
import com.jvito.plot.PlotPanel;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.tools.Ontology;

/**
 * A JPanel for drawing a GridViz.
 * 
 * @author Daniel Hakenjos
 * @version $Id: GridVizPanel.java,v 1.2 2008/04/12 14:28:12 djhacker Exp $
 */
public class GridVizPanel extends PlotPanel {

    private static final long serialVersionUID = 8857190383752128808L;

    private float[][] samples;

    private float[] attributvektorx, attributvektory;

    private float[] samplex, sampley;

    private int width, height, pixelgesamt, midx, midy, pointsize = 1, gridsize, griddelta;

    private boolean labelaxis;

    private String[] attrnames;

    private ExampleSet set;

    private ExampleColoring coloring;

    private int[] attribute_index;

    private int number_atts;

    private double scale;

    /**
	 * Constructs new GridVizPanel.
	 */
    public GridVizPanel(ExampleSet set, ExampleColoring coloring, String[] attrnames, float[][] samples, double pointsize, boolean labelaxis, int[] attribute_index, int number_atts, double scale) {
        this.set = set;
        this.coloring = coloring;
        this.attrnames = attrnames;
        this.samples = samples;
        this.pointsize = (int) pointsize;
        this.attribute_index = attribute_index;
        this.number_atts = number_atts;
        this.scale = scale;
        this.initcolortable = false;
        this.labelaxis = labelaxis;
    }

    /**
	 * Calculates the points for the samples.
	 */
    public void calculateSamplePoints() {
        samplex = new float[samples[0].length];
        sampley = new float[samples[0].length];
        float[] w = new float[number_atts];
        float sumx;
        for (int sample = 0; sample < samples[0].length; sample++) {
            samplex[sample] = 0.0f;
            sampley[sample] = 0.0f;
            sumx = 0.0f;
            for (int d = 0; d < number_atts; d++) {
                sumx += samples[attribute_index[d]][sample];
            }
            for (int d = 0; d < number_atts; d++) {
                w[d] = samples[attribute_index[d]][sample] / sumx;
            }
            for (int d = 0; d < number_atts; d++) {
                samplex[sample] += w[d] * attributvektorx[d];
                sampley[sample] += w[d] * attributvektory[d];
            }
            samplex[sample] = (float) (samplex[sample] * scale);
            sampley[sample] = (float) (sampley[sample] * scale);
        }
    }

    private void calculateAttributeVectors() {
        int dim = number_atts;
        attributvektorx = new float[dim];
        attributvektory = new float[dim];
        gridsize = (int) Math.sqrt(dim);
        if (gridsize * gridsize < dim) {
            gridsize += 1;
        }
        griddelta = pixelgesamt / gridsize;
        int gridx = 0, gridy = pixelgesamt;
        for (int dimindex = 0; dimindex < number_atts; dimindex++) {
            if ((dimindex) % gridsize == 0) {
                gridx = 0;
                gridy -= griddelta;
            }
            attributvektorx[dimindex] = gridx;
            attributvektory[dimindex] = gridy;
            gridx += griddelta;
        }
    }

    /**
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        width = dimension.width;
        height = dimension.height;
        g.setColor(foreground);
        pixelgesamt = Math.min(width, height) - 120;
        calculateAttributeVectors();
        midx = 60;
        midy = 60 + gridsize * griddelta;
        int x = midx, y = midy;
        g.setColor(foreground);
        Font font = new Font("Monospaced", Font.PLAIN, 15);
        g.setFont(font);
        for (int i = 0; i < number_atts; i++) {
            x = midx;
            y = midy;
            x += attributvektorx[i];
            y -= attributvektory[i];
            g.drawLine(getX(x), getY(y), getX(x + griddelta), getY(y));
            g.drawLine(getX(x), getY(y), getX(x), getY(y - griddelta));
            if (labelaxis) {
                g.drawString(attrnames[attribute_index[i]], getX(x + 5), getY(y - 5));
            }
        }
        calculateSamplePoints();
        for (int sample = 0; sample < samplex.length; sample++) {
            drawPoint(g, sample);
        }
    }

    /**
	 * Draws one sample.
	 */
    public void drawPoint(Graphics g, int sample) {
        int x = midx;
        int y = midy;
        x = x + ((int) (samplex[sample]));
        y = y - ((int) (sampley[sample]));
        if (colorbyattribute) {
            if ((set.getAttributes().getLabel() != null) && (set.getAttributes().getLabel().getName().equals(colorattr.getName()))) {
                g.setColor(coloring.getColorOfLabelValue(colorattr, colorvalues[sample]));
            } else if ((set.getAttributes().getPredictedLabel() != null) && (set.getAttributes().getPredictedLabel().getName().equals(colorattr.getName()))) {
                if (this.colorattr.isNominal()) {
                    g.setColor(colortable[(int) colorvalues[sample]]);
                } else {
                    int index = (int) (((colorvalues[sample] - set.getStatistics(colorattr, Statistics.MINIMUM)) / (set.getStatistics(colorattr, Statistics.MAXIMUM) - set.getStatistics(colorattr, Statistics.MINIMUM))) * 99.0f);
                    if (index < 0) index = 0;
                    if (index > 99) index = 99;
                    g.setColor(colortable[index]);
                }
            } else {
                if (colorattr.isNominal()) {
                    float max = (colorattr.getMapping().size() - 1);
                    int index = (int) (colorvalues[sample] * max);
                    g.setColor(colortable[index]);
                }
                if (colorattr.getValueType() == Ontology.NUMERICAL) {
                    int index = (int) (colorvalues[sample] * 99.0f);
                    if (index < 0) index = 0;
                    if (index > 99) index = 99;
                    g.setColor(colortable[index]);
                }
            }
        }
        g.fillRect(getX(x - pointsize / 2), getY(y - pointsize / 2), pointsize, pointsize);
    }
}
