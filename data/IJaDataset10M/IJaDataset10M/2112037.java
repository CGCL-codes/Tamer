package edu.ucla.stat.SOCR.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import edu.ucla.stat.SOCR.core.Modeler;
import edu.ucla.stat.SOCR.distributions.Domain;
import edu.ucla.stat.SOCR.distributions.IntervalData;
import edu.ucla.stat.SOCR.modeler.ModelerColor;
import edu.ucla.stat.SOCR.modeler.ModelerConstant;

/**This class models an interactive histogram.  The user can click on the horizontal axes to add points
 to the data set.*/
public class ModelerHistogram extends varHistogram implements MouseListener {

    private int count;

    static final double pi = 3.14159265358979323846;

    public Vector<Double> rawDat;

    Dimension winSize;

    private IntervalData data;

    private Vector<Double> values = new Vector<Double>();

    private int valCount = 0;

    public double[] modelX = null, modelY = null;

    public double[] modelX1 = null, modelY1 = null;

    public double[] modelX2 = null, modelY2 = null;

    private double left = -5;

    private double right = 5;

    protected double graphLeft = ModelerConstant.GRAPH_LOWER_LIMIT;

    protected double graphRight = ModelerConstant.GRAPH_UPPER_LIMIT;

    private double width = 1;

    protected int modelCount = 1;

    public double maxY = 10;

    private static boolean zoomIn = false;

    private static boolean zoomOut = false;

    protected static int MULTIPLE_DEFAULT_VALUE = 3;

    private Color outlineColor;

    private Color outlineColor1;

    private Color outlineColor2;

    protected boolean drawUserClicks = true;

    /**
	 * This general constructor creates a new interactive histogram
	 * corresponding to a specified domain.
	 */
    public ModelerHistogram(double a, double b, double w) {
        this.addMouseListener(this);
        winSize = this.getSize();
        winSize.width = (int) w;
        left = a;
        right = b;
        width = w;
        setIntervalData();
        rawDat = new Vector<Double>(10, 10);
        count = data.getDomain().getSize();
    }

    public ModelerHistogram() {
        super();
    }

    public void setDrawUserClicks(boolean flag) {
        this.drawUserClicks = flag;
    }

    public void setLeft(double input) {
        this.left = input;
    }

    public void setRight(double input) {
        this.right = input;
    }

    public void setGraphLeft(double input) {
        this.left = input;
    }

    public void setGraphRight(double input) {
        this.right = input;
    }

    public void setBarWidth(double input) {
        this.width = input;
    }

    public void setIntervalData() {
        data = new IntervalData(left, right, width);
        super.setIntervalData(data);
    }

    public void setZoomInIntervalData() {
        zoomIn = true;
        data = new IntervalData(this.graphLeft, this.graphRight, 2 * width);
        this.data = data;
        repaint();
    }

    public void setZoomOutIntervalData() {
        zoomOut = true;
        data = new IntervalData(graphLeft, graphRight, .5 * width);
        super.setIntervalData(data);
    }

    public void setBins(int n) {
        width = (this.graphRight - this.graphLeft) / n;
        width /= 2;
        setIntervalData();
    }

    public void panRight() {
        right = right + width;
        left = left + width;
        setIntervalData();
        setHist();
        repaint();
    }

    public void panLeft() {
        left = left - width;
        right = right - width;
        setIntervalData();
        setHist();
        repaint();
    }

    public int zoomIn() {
        int ret = 0;
        if (right - left > width) {
            left = left + width;
            right = right - width;
            setZoomInIntervalData();
            ret = 1;
        }
        return ret;
    }

    int getPanelHeight() {
        return winSize.height / 3;
    }

    public int zoomOut() {
        int ret = 0;
        left = left - width;
        right = right + width;
        setZoomOutIntervalData();
        ret = 1;
        return ret;
    }

    public void setModelType(int modelType) {
        super.setModelType(modelType);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D G2 = (Graphics2D) g;
        G2.setStroke(new BasicStroke(3));
        float[] HSBVal = new float[3];
        if (modelX != null && modelX.length > 0) {
            double maxXVal = modelX[0], maxYVal = modelY[0];
            int terms = modelY.length;
            for (int j = 1; j < terms; j++) {
                if (modelX[j] > maxXVal) maxXVal = modelX[j];
                if (modelY[j] > maxYVal) maxYVal = modelY[j];
            }
            maxY = maxYVal;
            boolean rescale = false;
            int MULTIPLE = MULTIPLE_DEFAULT_VALUE;
            if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
                MULTIPLE = MULTIPLE_DEFAULT_VALUE;
                rescale = true;
            }
            if (rescale) {
                for (int j = 1; j < terms; j++) {
                    if (modelX[j] == maxXVal) modelX[j] = MULTIPLE; else modelX[j] = (float) (modelX[j] / maxXVal) * MULTIPLE;
                    if (modelY[j] == maxYVal) modelY[j] = MULTIPLE; else modelY[j] = (float) (modelY[j] / maxYVal) * MULTIPLE;
                }
            }
            for (int j = 1; j < terms; j++) {
                if (modelY[j] == Float.POSITIVE_INFINITY) {
                    modelY[j] = (float) (yMax - 0.01);
                }
            }
            double xa, ya;
            int subLth = 0;
            double[] modelLineOfY = new double[modelY.length];
            double[] dataLineOfY = new double[modelY.length];
            double[] modelLineOfX = new double[modelY.length];
            if (modelType == Modeler.FOURIER_TYPE || modelType == Modeler.WAVELET_TYPE) {
                subLth = (int) (modelX.length);
                double xMaxData = 0, yMaxData = -10000, yMinData = 10000;
                double xMaxModel = 0, yMaxModel = -10000, yMinModel = 10000;
                double yMaxPlot = 0, yMinPlot = 0, xMinPlot = 0, xMaxPlot = subLth + 1;
                for (int i = 0; i < subLth; i++) {
                    dataLineOfY[i] = modelX[i];
                    modelLineOfY[i] = modelY[i];
                    modelLineOfX[i] = i;
                    yMaxData = Math.max(yMaxData, Math.max(dataLineOfY[i], yMaxData));
                    yMinData = Math.min(yMinData, Math.min(dataLineOfY[i], yMinData));
                    yMaxModel = Math.max(yMaxData, Math.max(modelLineOfY[i], yMaxData));
                    yMinModel = Math.min(yMinData, Math.min(modelLineOfY[i], yMinModel));
                }
                yMaxPlot = Math.max(yMaxData, yMaxModel);
                yMinPlot = Math.max(yMinData, yMinModel);
                super.setPlotXMin(xMinPlot);
                super.setPlotXMax(xMaxPlot);
                super.setPlotYMin(yMinPlot - 1);
                super.setPlotYMax(yMaxPlot + 1);
                double x1 = (double) modelX[0];
                double y1 = (double) modelY[0];
                for (int j = 1; j < subLth; j++) {
                    x1 = modelLineOfX[j - 1];
                    y1 = dataLineOfY[j - 1];
                    xa = modelLineOfX[j];
                    ya = dataLineOfY[j];
                    G2.setColor(ModelerColor.HISTOGRAM_FOURIER_DATA);
                    G2.setStroke(new BasicStroke(2f));
                    drawLine(G2, xa, ya, x1, y1);
                }
                int m = 1;
                for (int j = 1; j < subLth; j++) {
                    x1 = modelLineOfX[j - 1];
                    y1 = modelLineOfY[j - 1];
                    xa = modelLineOfX[j];
                    ya = modelLineOfY[j];
                    G2.setColor(ModelerColor.HISTOGRAM_FOURIER_MODEL);
                    G2.setStroke(new BasicStroke(2f));
                    drawLine(G2, xa, ya, x1, y1);
                }
            } else {
                subLth = (int) (modelX.length / modelCount);
                for (int j = 0; j < modelCount; j++) {
                    if (j == 0) {
                        G2.setStroke(new BasicStroke(3f));
                        G2.setColor(this.getOutlineColor());
                    } else {
                        G2.setStroke(new BasicStroke(2f));
                        Color.RGBtoHSB((int) Math.floor((Math.random() * 256)), (int) Math.floor((Math.random() * 256)), (int) Math.floor((Math.random() * 256)), HSBVal);
                        G2.setColor(Color.getHSBColor(HSBVal[0], HSBVal[1], HSBVal[2]));
                    }
                    double x1 = (double) modelX[0 + j * subLth];
                    double y1 = (double) modelY[0 + j * subLth];
                    for (int i = 1; i < subLth; i++) {
                        xa = modelX[i + j * subLth];
                        ya = modelY[i + j * subLth];
                        drawLine(G2, x1, y1, xa, ya);
                        x1 = xa;
                        y1 = ya;
                    }
                }
            }
        }
    }

    /**
	 * This method sets the width of the domain. A new interval data set is
	 * created, and the values are added to the data set.
	 */
    public void setWidth(double width) {
        data.setDomain(new Domain(data.getDomain().getLowerBound(), data.getDomain().getUpperBound(), width));
        for (int i = 0; i < values.size(); i++) {
            data.setValue(((Double) values.elementAt(i)).doubleValue());
        }
        repaint();
    }

    /** This method resets the interactive histogram. */
    public void clear() {
        values.removeAllElements();
        data.reset();
        modelX = null;
        modelY = null;
        repaint();
    }

    public void resetGraphLimits() {
        this.graphLeft = ModelerConstant.GRAPH_LOWER_LIMIT;
        this.graphRight = ModelerConstant.GRAPH_UPPER_LIMIT;
    }

    /** This method returns the values from the data set. */
    public double getValue(int i) {
        if (i < 0) i = 0; else if (i >= values.size()) i = values.size() - 1;
        return ((Double) values.elementAt(i)).doubleValue();
    }

    /** This method returns the last value. */
    public double getValue() {
        return getValue(values.size() - 1);
    }

    /** This method handles the events corresponding to mouse clicks. */
    public void mouseClicked(MouseEvent event) {
        if (drawUserClicks) {
            int y0 = yGraph(0);
            int y = (int) Math.ceil(yScale(event.getY()));
            double x = xScale(event.getX());
            double XDom = data.getDomainValue(x);
            int ydash = y - data.getFreq(XDom);
            valCount = valCount + ydash;
            if (ydash > 0) {
                for (int i = 0; i < ydash; i++) {
                    values.add(new Double(XDom));
                }
                setHist();
            } else {
                for (int i = 0; i < -ydash; i++) {
                    values.removeElement(new Double(XDom));
                }
                setHist();
                repaint();
            }
        }
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    private void setHist() {
        data.reset();
        if (values.size() < 1) return;
        double min = Double.valueOf(values.elementAt(0).toString()).doubleValue();
        double max = Double.valueOf(values.elementAt(0).toString()).doubleValue();
        double temp = 0;
        for (int i = 0; i < values.size(); i++) {
            temp = Double.valueOf(values.elementAt(i).toString()).doubleValue();
            if (min > temp) min = temp;
            if (max < temp) max = temp;
            data.setValue(temp);
        }
        repaint();
    }

    public void addDataPoint() {
    }

    public void setModel(int cnt, double[] xMod, double[] yMod) {
        modelX = xMod;
        modelY = yMod;
        repaint();
    }

    public void setTwoModel(int cnt, double[] xMod1, double[] yMod1, double[] xMod2, double[] yMod2) {
        int length1 = xMod1.length;
        int length2 = xMod2.length;
        int lengthSum = length1 + length2;
        modelX = new double[lengthSum];
        modelY = new double[lengthSum];
        for (int i = 0; i < length1; i++) {
            modelX[i] = xMod1[i];
            modelY[i] = yMod1[i];
        }
        for (int i = 0; i < length2; i++) {
            modelX[length1 + i] = xMod2[i];
            modelY[length1 + i] = yMod2[i];
        }
        repaint();
    }

    public void setTwoModel(int cnt, double[] xMod1, double[] yMod1, double[] xMod2, double[] yMod2, Color color1, Color color2) {
        this.setOutlineColor1(color1);
        this.setOutlineColor2(color2);
        modelX1 = xMod1;
        modelY1 = yMod1;
        modelX2 = xMod2;
        modelY2 = yMod2;
        repaint();
    }

    public int getdataCursor() {
        return values.size();
    }

    public float[] getXData() {
        float[] xRang = new float[data.getDomain().getSize()];
        for (int i = 0; i < data.getDomain().getSize(); i++) {
            xRang[i] = (float) data.getDomain().getValue(i);
        }
        return xRang;
    }

    public float[] getYData() {
        float[] yRang = new float[data.getDomain().getSize()];
        for (int i = 0; i < data.getDomain().getSize(); i++) {
            yRang[i] = (float) data.getFreq(data.getDomain().getValue(i));
        }
        return yRang;
    }

    public void setxy(float[] raw) {
        clear();
        double XDom = 0;
        for (int i = 0; i < raw.length; i++) {
            if (modelType == Modeler.DISCRETE_DISTRIBUTION_TYPE) {
                XDom = raw[i] + 0.5;
            } else {
                XDom = data.getDomainValue(raw[i]);
            }
            values.add(new Double(XDom));
        }
        setHist();
    }

    public void setXExtrema(float a, float b) {
    }

    public void setYExtrema(float a, float b) {
    }

    public double getMinX() {
        return (float) left;
    }

    public double getMaxX() {
        return (float) right;
    }

    public double getMinY() {
        return 0;
    }

    public double getMaxY() {
        return maxY;
    }

    public double minV(Vector<Double> vals) {
        double temp = Double.valueOf(vals.elementAt(0).toString()).doubleValue();
        for (int i = 0; i < vals.size(); i++) {
            if (Double.valueOf(vals.elementAt(i).toString()).doubleValue() < temp) temp = Double.valueOf(vals.elementAt(i).toString()).doubleValue();
        }
        return temp;
    }

    public double maxV(Vector<Double> vals) {
        double temp = Double.valueOf(vals.elementAt(0).toString()).doubleValue();
        for (int i = 0; i < vals.size(); i++) {
            if (Double.valueOf(vals.elementAt(i).toString()).doubleValue() > temp) temp = Double.valueOf(vals.elementAt(i).toString()).doubleValue();
        }
        return temp;
    }

    public void setModelCount(int ct) {
        modelCount = ct;
    }

    public void setModelX(double[] input) {
        this.modelX = input;
    }

    public void setModelY(double[] input) {
        this.modelY = input;
    }

    public void setOutlineColor(Color color) {
        this.outlineColor = color;
    }

    public Color getOutlineColor() {
        return this.outlineColor;
    }

    public void setOutlineColor1(Color color) {
        this.outlineColor1 = color;
    }

    public Color getOutlineColor1() {
        return this.outlineColor1;
    }

    public void setOutlineColor2(Color color) {
        this.outlineColor2 = color;
    }

    public Color getOutlineColor2() {
        return this.outlineColor2;
    }

    public void setHistogramRight(double input) {
        this.graphRight = input;
    }

    public void setHistogramLeft(double input) {
        this.graphLeft = input;
    }
}
