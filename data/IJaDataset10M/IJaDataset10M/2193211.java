package com.jvito.plot.svm;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import com.jvito.data.ExampleColoring;
import com.jvito.plot.PlotPanel;
import com.jvito.util.Utils;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;

/**
 * A <code>ScatterPlot2DPanel</code>, but draws the hyperplane points in another color.
 * 
 * @author Daniel Hakenjos
 * @version $Id: HyperplaneTourPanel.java,v 1.4 2008/04/19 09:21:44 djhacker Exp $
 */
public class HyperplaneTourPanel extends PlotPanel implements MouseListener {

    private static final long serialVersionUID = 584174265205737142L;

    private double[] x_samples;

    private double[] y_samples;

    private int width, height;

    private boolean mousepressed = false;

    private ExampleSet set;

    private ExampleColoring coloring;

    private double point_size;

    private double x_min, x_max, y_min, y_max;

    private Color lowerboundcolor, upperboundcolor;

    private boolean[] bound;

    private boolean showhyperplane;

    public HyperplaneTourPanel(ExampleSet set, ExampleColoring coloring, double x_min, double x_max, double y_min, double y_max, double[] x_samples, double[] y_samples, boolean[] bound, boolean showhyperplane, double point_size) {
        this.set = set;
        this.coloring = coloring;
        this.x_min = x_min;
        this.x_max = x_max;
        this.x_samples = x_samples;
        this.y_samples = y_samples;
        this.bound = bound;
        this.showhyperplane = showhyperplane;
        this.point_size = point_size;
        this.addMouseListener(this);
        this.addMouseMotionListener(new MyMouseMotionListener());
    }

    public void setLowerBoundColor(Color color) {
        this.lowerboundcolor = color;
    }

    public void setUpperBoundColor(Color color) {
        this.upperboundcolor = color;
    }

    public void setXAttribute(double x_min, double x_max, double[] samples) {
        this.x_min = x_min;
        this.x_max = x_max;
        this.x_samples = samples;
    }

    public void setYAttribute(double y_min, double y_max, double[] samples) {
        this.y_min = y_min;
        this.y_max = y_max;
        this.y_samples = samples;
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
        g.drawRect(getX(70), getY(70), width - 140, height - 140);
        g.drawLine(getX(60), getY(70), getX(60), getY(height - 70));
        g.drawLine(getX(70), getY(height - 60), getX(width - 70), getY(height - 60));
        g.drawLine(getX(53), getY(70), getX(67), getY(70));
        g.drawLine(getX(53), getY(height - 70), getX(67), getY(height - 70));
        g.drawLine(getX(70), getY(height - 53), getX(70), getY(height - 67));
        g.drawLine(getX(width - 70), getY(height - 53), getX(width - 70), getY(height - 67));
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int slen = fm.stringWidth("x vs y");
        g.drawString("x vs y", getX((width - slen) / 2), getY(height - 7));
        font = new Font("SansSerif", Font.PLAIN, 15);
        g.setFont(font);
        slen = fm.stringWidth(Float.toString((float) x_max));
        g.drawString(Float.toString((float) x_max), getX((width - 70 - slen / 2)), getY(height - 40));
        slen = fm.stringWidth(Float.toString((float) x_min));
        g.drawString(Float.toString((float) x_min), getX(70), getY(height - 40));
        slen = fm.stringWidth(Float.toString((float) y_max));
        g.drawString(Float.toString((float) y_max), getX(60 - slen / 2), getY(65));
        slen = fm.stringWidth(Float.toString((float) y_min));
        g.drawString(Float.toString((float) y_min), getX(60 - slen / 2), getY(height - 25));
        for (int sample = x_samples.length - 1; sample >= 0; sample--) {
            drawSample(g, sample);
        }
        paintMouseInfo(null);
    }

    /**
	 * Draws one sample.
	 */
    public void drawSample(Graphics g, int sample) {
        if ((!showhyperplane) || (sample + 1 <= x_samples.length / 2)) {
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
                        g.setColor(colortable[(int) colorvalues[sample]]);
                    }
                    if (Utils.isNumeric(colorattr)) {
                        int index = (int) (((colorvalues[sample] - set.getStatistics(colorattr, Statistics.MINIMUM)) / (set.getStatistics(colorattr, Statistics.MAXIMUM) - set.getStatistics(colorattr, Statistics.MINIMUM))) * 99.0f);
                        if (index < 0) index = 0;
                        if (index > 99) index = 99;
                        g.setColor(colortable[index]);
                    }
                }
            }
        } else {
            if (bound[sample]) {
                g.setColor(lowerboundcolor);
            } else {
                g.setColor(upperboundcolor);
            }
        }
        double x_breite = x_max - x_min;
        double y_breite = y_max - y_min;
        double x = 70 + (width - 140) * ((x_samples[sample] - x_min) / x_breite) - ((int) (point_size / 2));
        double y = height - 70 - (height - 140) * ((y_samples[sample] - y_min) / y_breite) - ((int) (point_size / 2));
        g.fillRect(getX((int) x), getY((int) y), (int) point_size, (int) point_size);
    }

    /**
	 * Paints the mouse-info.
	 */
    public void paintMouseInfo(MouseEvent event) {
        if ((mousepressed) && (event != null)) {
            int x = event.getX(), y = event.getY();
            Graphics g = getGraphics();
            g.setColor(background);
            g.fillRect(0, 0, width, 30);
            g.setColor(foreground);
            Font font = new Font("Monospaced", Font.PLAIN, 15);
            g.setFont(font);
            g.drawString("x: " + getValueX(getOrigin().x * (-1) + x), 5, 11);
            g.drawString("y: " + getValueY(getOrigin().y * (-1) + y), 5, 26);
        } else {
        }
    }

    /**
	 * Gets the value of the x_coordinate.
	 */
    public double getValueX(int x) {
        if (x < 70) {
            return x_min;
        }
        if (x > width - 70) {
            return x_max;
        }
        x = x - 70;
        double breite = x_max - x_min;
        double value = x_min + (breite / (width - 140)) * x;
        return value;
    }

    /**
	 * Gets the value of the y_coordinate.
	 */
    public double getValueY(int y) {
        if (y < 70) {
            return y_max;
        }
        if (y > height - 70) {
            return y_min;
        }
        y = y - 70;
        y = height - 140 - y;
        double breite = y_max - y_min;
        double value = y_min + (breite / (height - 140)) * y;
        return value;
    }

    /**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent event) {
    }

    /**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
    public void mouseEntered(MouseEvent event) {
    }

    /**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
    public void mouseExited(MouseEvent event) {
    }

    /**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON2) {
            mousepressed = true;
            paintMouseInfo(event);
        }
    }

    /**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent event) {
        mousepressed = false;
        repaint();
    }

    private class MyMouseMotionListener extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent event) {
            paintMouseInfo(event);
        }
    }
}
