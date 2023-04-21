package org.neuroph.easyneurons.samples.perceptron;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.JTextField;
import javax.swing.JPanel;

/**
 *
 * @author Marko
 */
public class ImagePanel extends JPanel {

    public static int cDia = 50;

    public static int cRad = cDia / 2;

    protected int width;

    protected int height;

    protected Color backgroundColor;

    public JTextField neuronOutput;

    protected JTextField weightedSum;

    protected JTextField threshold;

    protected JTextField weight1;

    protected JTextField weight2;

    protected JTextField input1;

    protected JTextField input2;

    protected Dimension tfD;

    protected int tfH, tfW, tfHH, tfHW;

    protected int rX, rY, cX, cY;

    protected int arrowH;

    protected int noX, noY, wsX, wsY, thX, thY, w1X, w1Y, w2X, w2Y, i1X, i1Y, i2X, i2Y;

    private int a1X2, a1Y2;

    private int a2X1, a2Y1;

    private int a3X1, a3Y1, a3X2, a3Y2;

    private int a4X1, a4Y1, a4X2, a4Y2;

    private int a5X1, a5Y1, a5X2, a5Y2;

    private int l1X, l1Y, l2X, l2Y, l3X, l3Y, l4X, l4Y, l5X, l5Y, l6X, l6Y, l7X, l7Y;

    public ImagePanel() {
        init(350, 300);
        disableNeuronFields();
    }

    public void init(int w, int h) {
        width = w;
        height = h;
        cDia = height / 5;
        if (cDia > 65) {
            cDia = 65;
        } else if (cDia < 50) {
            cDia = 50;
        }
        cRad = cDia / 2;
        backgroundColor = Color.WHITE;
        initComponents();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.blue);
        drawArrow(g, a2X1, a2Y1, a1X2, a1Y2);
        g.setColor(Color.white);
        g.setColor(Color.blue);
        g.fillOval(cX, cY, cDia, cDia);
        drawArrow(g, a3X1, a3Y1, a3X2, a3Y2);
        drawArrow(g, a4X1, a4Y1, a4X2, a4Y2);
        drawArrow(g, a5X1, a5Y1, a5X2, a5Y2);
        g.setColor(Color.black);
        g.drawString("y", l1X, l1Y);
        g.drawString("-1", l2X, l2Y);
        g.drawString("u", l3X, l3Y);
        g.drawString("w1", l4X, l4Y);
        g.drawString("w2", l5X, l5Y);
        g.drawString("x1", l6X, l6Y);
        g.drawString("x2", l7X, l7Y);
    }

    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        Polygon p;
        p = new Polygon();
        p.addPoint(x2, y2);
        if (x2 == x1) {
            if (y2 > y1) {
                p.addPoint(x2 - 5, y2 - 10);
                p.addPoint(x2 + 5, y2 - 10);
            } else {
                p.addPoint(x2 - 5, y2 + 10);
                p.addPoint(x2 + 5, y2 + 10);
            }
        } else if (y2 == y1) {
            if (x2 > x1) {
                p.addPoint(x2 - 10, y2 - 5);
                p.addPoint(x2 - 10, y2 + 5);
            } else {
                p.addPoint(x2 + 10, y2 - 5);
                p.addPoint(x2 + 10, y2 + 5);
            }
        } else {
            double t1 = Math.abs(new Integer(y2 - y1).doubleValue());
            double t2 = Math.abs(new Integer(x2 - x1).doubleValue());
            double theta = Math.atan(t1 / t2);
            if (x2 < x1) {
                if (y2 < y1) {
                    theta = Math.PI + theta;
                } else {
                    theta = -(Math.PI + theta);
                }
            } else if (x2 > x1 && y2 < y1) {
                theta = 2 * Math.PI - theta;
            }
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);
            Point p2 = new Point(-10, -5);
            Point p3 = new Point(-10, +5);
            int x = new Long(Math.round((cosTheta * p2.x) - (sinTheta * p2.y))).intValue();
            p2.y = new Long(Math.round((sinTheta * p2.x) + (cosTheta * p2.y))).intValue();
            p2.x = x;
            x = new Long(Math.round((cosTheta * p3.x) - (sinTheta * p3.y))).intValue();
            p3.y = new Long(Math.round((sinTheta * p3.x) + (cosTheta * p3.y))).intValue();
            p3.x = x;
            p2.translate(x2, y2);
            p3.translate(x2, y2);
            p.addPoint(p2.x, p2.y);
            p.addPoint(p3.x, p3.y);
        }
        g.fillPolygon(p);
        g.drawLine(x1, y1, x2, y2);
    }

    public int getFielcdAsInt(JTextField t) {
        try {
            return (Integer.valueOf(t.getText())).intValue();
        } catch (NumberFormatException e) {
            setFieldToInt(t, 0);
            return 0;
        }
    }

    public double getFieldAsDouble(JTextField t) {
        try {
            return (Double.valueOf(t.getText())).doubleValue();
        } catch (NumberFormatException e) {
            setFieldToDouble(t, 0);
            return 0;
        }
    }

    public double[] getInputFields() {
        double[] temp;
        temp = new double[2];
        temp[0] = getFieldAsDouble(input1);
        temp[1] = getFieldAsDouble(input2);
        return temp;
    }

    public void setFieldToInt(JTextField t, int i) {
        String s;
        try {
            s = String.valueOf((int) i);
        } catch (NumberFormatException e) {
            s = "0";
        }
        if (s != t.getText()) {
            t.setText(s);
        }
        t.select(0, 0);
    }

    public void setFieldToDouble(JTextField t, double f) {
        String s;
        try {
            s = String.valueOf((double) f);
        } catch (NumberFormatException e) {
            s = "0";
        }
        if (s != t.getText()) {
            t.setText(s);
        }
        t.select(0, 0);
    }

    public void setNeuronFields(double neuronO, double weightS, double tresh, double w1, double w2) {
        setFieldToDouble(neuronOutput, neuronO);
        setFieldToDouble(weightedSum, weightS);
        setFieldToDouble(threshold, tresh);
        setFieldToDouble(weight1, w1);
        setFieldToDouble(weight2, w2);
    }

    public void setInputFields(double[] inps) {
        setFieldToDouble(input1, inps[0]);
        setFieldToDouble(input2, inps[1]);
    }

    public void setOutputField(double out) {
        setFieldToDouble(neuronOutput, out);
        if (out > 0) {
            neuronOutput.setBackground(Color.GREEN);
        } else {
            neuronOutput.setBackground(Color.RED);
        }
    }

    public void disableNeuronFields() {
        neuronOutput.setEnabled(false);
        weightedSum.setEnabled(false);
        threshold.setEnabled(false);
        weight1.setEnabled(false);
        weight2.setEnabled(false);
        input1.setEnabled(false);
        input2.setEnabled(false);
    }

    /**
     * Enables the neuron text fields.
     */
    public void enableNeuronFields() {
        neuronOutput.setEnabled(true);
        weightedSum.setEnabled(true);
        threshold.setEnabled(true);
        weight1.setEnabled(true);
        weight2.setEnabled(true);
        input1.setEnabled(true);
        input2.setEnabled(true);
    }

    public void initComponents() {
        setLayout(null);
        neuronOutput = new JTextField("0", 5);
        add(neuronOutput);
        neuronOutput.setSize(cDia - 10, neuronOutput.getPreferredSize().height);
        tfD = neuronOutput.getSize();
        neuronOutput.setBackground(Color.WHITE);
        calculateDrawingCoordinates();
        neuronOutput.setLocation(noX, noY);
        weightedSum = new JTextField("0", 5);
        add(weightedSum);
        weightedSum.setSize(tfD);
        weightedSum.setLocation(wsX, wsY);
        weightedSum.setBackground(Color.WHITE);
        threshold = new JTextField("0", 5);
        add(threshold);
        threshold.setSize(tfD);
        threshold.setLocation(thX, thY);
        threshold.setBackground(Color.WHITE);
        weight1 = new JTextField("0", 5);
        add(weight1);
        weight1.setSize(tfD);
        weight1.setLocation(w2X, w2Y);
        weight1.setBackground(Color.WHITE);
        weight2 = new JTextField("0", 5);
        add(weight2);
        weight2.setSize(tfD);
        weight2.setLocation(w1X, w1Y);
        weight2.setBackground(Color.WHITE);
        input1 = new JTextField("0", 5);
        add(input1);
        input1.setSize(tfD);
        input1.setLocation(i2X, i2Y);
        input1.setBackground(Color.WHITE);
        input2 = new JTextField("0", 5);
        add(input2);
        input2.setSize(tfD);
        input2.setLocation(i1X, i1Y);
        input2.setBackground(Color.WHITE);
        validate();
    }

    private void calculateDrawingCoordinates() {
        int xPos, yPos;
        int dx1, dy1, dx2, dy2;
        int hGap;
        tfH = tfD.height;
        tfW = tfD.width;
        tfHH = Math.round(tfH / 2);
        tfHW = Math.round(tfW / 2);
        arrowH = (height - ((cDia * 2) + (3 * tfH))) / 4;
        xPos = (width / 2);
        yPos = 0;
        dx1 = (int) (cRad * Math.cos(Math.PI / 4));
        dy1 = (int) (cRad * Math.sin(Math.PI / 4));
        dx2 = dy2 = (arrowH + arrowH + tfH + cRad - dy1);
        noX = xPos - tfHW;
        noY = yPos;
        yPos += (tfH + arrowH + arrowH + cDia + cRad - tfHH);
        wsX = xPos - tfHW;
        wsY = yPos;
        hGap = (xPos - (cRad + 20 + tfW)) / 2;
        thX = hGap + 20;
        thY = yPos;
        w2X = xPos - arrowH - tfHW - dx1 - dx1;
        w2Y = yPos + tfHH + cRad + arrowH;
        w1X = xPos + arrowH - tfHW + dx1 + dx1;
        w1Y = w2Y;
        xPos = width / 2;
        yPos = tfH;
        a1X2 = xPos;
        a1Y2 = yPos;
        yPos += arrowH;
        rX = xPos - cRad;
        rY = yPos;
        yPos += cDia;
        a2X1 = xPos;
        a2Y1 = yPos + arrowH;
        yPos += arrowH;
        cX = xPos - cRad;
        cY = yPos;
        xPos = 20;
        yPos += cRad;
        a3X1 = xPos;
        a3Y1 = yPos;
        a3X2 = rX;
        a3Y2 = yPos;
        xPos = width / 2;
        yPos += dy1;
        a4X1 = xPos - dx1 - dx2;
        a4Y1 = yPos + dy2;
        a4X2 = xPos - dx1;
        a4Y2 = yPos;
        a5X1 = xPos + dx1 + dx2;
        a5Y1 = yPos + dy2;
        a5X2 = xPos + dx1;
        a5Y2 = yPos;
        i2X = a4X1 - tfHW;
        i2Y = a4Y1;
        i1X = width - i2X - tfW;
        i1Y = i2Y;
        l1X = noX - 10;
        l1Y = noY + tfHH + 5;
        l2X = 5;
        l2Y = thY + tfHH + 5;
        l3X = thX + tfW + 5;
        l3Y = thY + 5;
        l4X = w2X + -20;
        l4Y = w2Y + 5;
        l5X = w1X + tfW + 5;
        l5Y = w1Y + 5;
        l6X = i2X + tfW + 5;
        l6Y = i2Y + 5;
        l7X = i1X - 15;
        l7Y = i1Y + 5;
    }
}
