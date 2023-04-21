package geovista.geoviz.scatterplot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * put your documentation comment here
 */
public class BivariateLegendWithScatterPlot extends ScatterPlotWithBackground {

    public static final String COMMAND_BOUNDARIES_MOVED = "cmdMov";

    public static final String COMMAND_BOUNDARIES_NUMBERCHANGED = "cmdChg";

    transient double xScale;

    transient double yScale;

    /**
	 * put your documentation comment here
	 */
    public BivariateLegendWithScatterPlot() {
        super();
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param String
	 *            attributeXName
	 * @param String
	 *            attributeYName
	 * @param double[] dataX
	 * @param double[] dataY
	 * @param boolean axisOn
	 */
    public BivariateLegendWithScatterPlot(Object[] dataObject, int[] dataIndices, boolean axisOn, Color c) {
        super();
        this.dataObject = dataObject;
        attributeArrayNames = (String[]) dataObject[0];
        int len = attributeArrayNames.length;
        if (dataObject[len + 1] == null) {
            observNames = null;
        } else {
            observNames = (String[]) dataObject[len + 1];
        }
        this.dataIndices = dataIndices;
        axisDataSetup();
        this.axisOn = axisOn;
        background = c;
        if (c == Color.black) {
            foreground = Color.white;
        } else {
            foreground = Color.black;
        }
        initialize();
    }

    public void setBoundaries(double[] boundariesX, double[] boundariesY) {
        xBoundaries = boundariesX;
        yBoundaries = boundariesY;
    }

    public void setClassColors(Color[][] classColors) {
        this.classColors = classColors;
    }

    public double[] getBoundariesX() {
        return xBoundaries;
    }

    public double[] getBoundariesY() {
        return yBoundaries;
    }

    @Override
    public String getShortDiscription() {
        return "XYP";
    }

    /**
	 * Set up data and axis for drawing the scatter plot.
	 */
    @Override
    protected void initialize() {
        setBackground(background);
        dataArrayX = new DataArray(dataX);
        dataArrayY = new DataArray(dataY);
        conditionArray = new int[dataX.length];
        setBorder(BorderFactory.createLineBorder(Color.gray));
        xAxisExtents[0] = dataArrayX.getExtent()[0];
        xAxisExtents[1] = dataArrayX.getExtent()[1];
        yAxisExtents[0] = dataArrayY.getExtent()[0];
        yAxisExtents[1] = dataArrayY.getExtent()[1];
        size = this.getSize();
        setupDataforDisplay();
    }

    /**
	 * Draw the scatter plot.
	 * 
	 * @param g
	 */
    @Override
    public void paintComponent(Graphics g) {
        if (dataX == null) {
            return;
        }
        g.setColor(background);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(foreground);
        paintBorder(g);
        if (axisOn) {
            drawClassBoundaryLabels(g);
        }
        drawPlot(g);
        Graphics2D g2 = (Graphics2D) g;
        if (exLabels != null && axisOn == true) {
            setToolTipText("");
            exLabels.paint(g2);
        }
    }

    /**
	 * Draw pot (points) on the screen.
	 * 
	 * @param g
	 */
    @Override
    protected void drawPlot(Graphics g) {
        int plotWidth, plotHeight;
        plotWidth = getWidth();
        plotHeight = getHeight();
        int size;
        size = (plotWidth < plotHeight) ? plotWidth : plotHeight;
        pointSize = (size < 360) ? size / 60 : 6;
        pointSize = (pointSize < 3) ? 3 : pointSize;
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("attribute equal? " + attributeXName.equals(attributeYName));
        }
        int len = dataArrayX.length();
        drawClassBackground(g);
        g.setColor(foreground);
        for (int i = 0; i < len; i++) {
            if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY) && (whyint[i] >= plotEndY) && (conditionArray[i] > -1)) {
                g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
            }
        }
    }

    @Override
    protected void drawClassBackground(Graphics g) {
        if (classColors != null) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("classer is not null");
            }
            for (int i = 0; i < classColors.length; i++) {
                for (int j = 0; j < classColors[0].length; j++) {
                    g.setColor(classColors[i][j]);
                    g.fillRect(xBoundariesInt[i], yBoundariesInt[j + 1], xBoundariesInt[i + 1] - xBoundariesInt[i], yBoundariesInt[j] - yBoundariesInt[j + 1]);
                }
            }
        }
        g.setColor(Color.white);
        for (int element : xBoundariesInt) {
            g.drawLine(element, plotOriginY, element, plotEndY);
        }
        for (int element : yBoundariesInt) {
            g.drawLine(plotOriginX, element, plotEndX, element);
        }
    }

    protected void drawClassBoundaryLabels(Graphics g) {
        int plotWidth, plotHeight;
        plotWidth = getWidth();
        plotHeight = getHeight();
        g.setColor(foreground);
        int fontSize;
        fontSize = (plotWidth < plotHeight) ? plotWidth : plotHeight;
        fontSize = ((fontSize / 32) < 9) ? 9 : fontSize / 32;
        Font font = new Font("", Font.PLAIN, fontSize);
        g.setFont(font);
        g.setColor(foreground);
        String label;
        for (int i = 0; i < xBoundariesInt.length; i++) {
            label = Double.toString(xBoundaries[i]);
            if (label.length() >= 8) {
                label = label.substring(0, 7);
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(Math.PI / 4, xBoundariesInt[i] - 2, plotOriginY + 8);
            g.drawString(label, xBoundariesInt[i] - 2, plotOriginY + 8);
            g2d.rotate(-Math.PI / 4, xBoundariesInt[i] - 2, plotOriginY + 8);
        }
        for (int j = 0; j < yBoundariesInt.length; j++) {
            label = Double.toString(yBoundaries[j]);
            if (label.length() >= 8) {
                label = label.substring(0, 7);
            }
            g.drawString(label, plotOriginX - (int) (plotWidth * AXISSPACEPORTION * 3 / 4), yBoundariesInt[j] + 2);
        }
    }

    protected double getValueFromScreenValue(int data, double scale, int min, double dataMin) {
        double value;
        value = ((data - min)) / scale + dataMin;
        return value;
    }

    @Override
    protected void setupDataforDisplay() {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("In setup data for display ..." + xAxisExtents[0]);
        }
        setVisibleAxis(axisOn);
        if (dataArrayX == null) {
            return;
        }
        int len = dataArrayX.length();
        if (len != dataArrayY.length()) {
            return;
        }
        xScale = getScale(plotOriginX, plotEndX, xAxisExtents[0], xAxisExtents[1]);
        exsint = getValueScreen(dataX, xScale, plotOriginX, xAxisExtents[0]);
        yScale = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
        whyint = getValueScreen(dataY, yScale, plotOriginY, yAxisExtents[0]);
        if (xBoundaries != null && yBoundaries != null) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("x and y boundaries are not null.");
            }
            xBoundariesInt = new int[xBoundaries.length];
            yBoundariesInt = new int[yBoundaries.length];
            xBoundariesInt = getValueScreen(xBoundaries, xScale, plotOriginX, xAxisExtents[0]);
            yBoundariesInt = getValueScreen(yBoundaries, yScale, plotOriginY, yAxisExtents[0]);
        }
    }

    /**
	 * Begin the drawing of selection region (box).
	 * 
	 * @param e
	 */
    @Override
    public void mousePressed(MouseEvent e) {
        if (dataX == null || dataY == null) {
            return;
        }
        if (e.isPopupTrigger()) {
            maybeShowPopup(e);
        }
        mouseX1 = e.getX();
        mouseY1 = e.getY();
    }

    /**
	 * Work with mouseDragged to draw a selection region (box) for selection.
	 * 
	 * @param e
	 */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (dataX == null) {
            return;
        }
        mouseX2 = e.getX();
        mouseY2 = e.getY();
        int v, v1;
        int lenX = xBoundariesInt.length;
        int lenY = yBoundariesInt.length;
        int[] pixelValues = xBoundariesInt.clone();
        double[] values = xBoundaries.clone();
        for (int i = 0; i < lenX; i++) {
            if (lenX > xBoundariesInt.length) {
                return;
            }
            v = xBoundariesInt[i];
            if (Math.abs(mouseX1 - v) <= 5) {
                if ((mouseX2 - mouseX1) > 0 && (i + 1) < lenX) {
                    v1 = xBoundariesInt[i + 1];
                    if (Math.abs(mouseX2 - v1) <= 5) {
                        xBoundariesInt = new int[lenX - 1];
                        xBoundaries = new double[lenX - 1];
                        for (int r = 0; r < i; r++) {
                            xBoundariesInt[r] = pixelValues[r];
                            xBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenX - 1; r++) {
                            xBoundariesInt[r] = pixelValues[r + 1];
                            xBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != 0 && mouseX2 > xBoundariesInt[i - 1] && mouseX2 < xBoundariesInt[i + 1]) {
                        xBoundariesInt[i] = mouseX2;
                        xBoundaries[i] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                } else if ((mouseX2 - mouseX1) < 0 && (i - 1) >= 0) {
                    v1 = xBoundariesInt[i - 1];
                    if (Math.abs(mouseX2 - v1) <= 3) {
                        xBoundariesInt = new int[lenX - 1];
                        xBoundaries = new double[lenX - 1];
                        for (int r = 0; r < i; r++) {
                            xBoundariesInt[r] = pixelValues[r];
                            xBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenX - 1; r++) {
                            xBoundariesInt[r] = pixelValues[r + 1];
                            xBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != lenX - 1 && mouseX2 > xBoundariesInt[i - 1] && mouseX2 < xBoundariesInt[i + 1]) {
                        xBoundariesInt[i] = mouseX2;
                        xBoundaries[i] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                }
                if (i == 0) {
                    v1 = xBoundariesInt[1];
                    if ((mouseX2 - mouseX1) > 0 && (mouseX2 < v1)) {
                        xBoundariesInt = new int[lenX + 1];
                        xBoundaries = new double[lenX + 1];
                        xBoundariesInt[0] = pixelValues[0];
                        xBoundaries[0] = values[0];
                        xBoundariesInt[1] = mouseX2;
                        xBoundaries[1] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        for (int r = 2; r < lenX + 1; r++) {
                            xBoundariesInt[r] = pixelValues[r - 1];
                            xBoundaries[r] = values[r - 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
                if (i == lenX - 1) {
                    v1 = xBoundariesInt[i - 1];
                    if ((mouseX2 - mouseX1) < 0 && (mouseX2 > v1)) {
                        xBoundariesInt = new int[lenX + 1];
                        xBoundaries = new double[lenX + 1];
                        for (int r = 0; r < lenX - 1; r++) {
                            xBoundariesInt[r] = pixelValues[r];
                            xBoundaries[r] = values[r];
                        }
                        xBoundariesInt[i] = mouseX2;
                        xBoundaries[i] = getValueFromScreenValue(mouseX2, xScale, plotOriginX, xBoundaries[0]);
                        xBoundariesInt[lenX] = pixelValues[lenX - 1];
                        xBoundaries[lenX] = values[lenX - 1];
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
            }
        }
        pixelValues = yBoundariesInt.clone();
        values = yBoundaries.clone();
        for (int i = 0; i < lenY; i++) {
            if (lenY > yBoundariesInt.length) {
                return;
            }
            v = yBoundariesInt[i];
            if (Math.abs(mouseY1 - v) <= 5) {
                if ((mouseY2 - mouseY1) > 0 && (i + 1) < lenY) {
                    v1 = yBoundariesInt[i + 1];
                    if (Math.abs(mouseY2 - v1) <= 5) {
                        yBoundariesInt = new int[lenY - 1];
                        yBoundaries = new double[lenY - 1];
                        for (int r = 0; r < i; r++) {
                            yBoundariesInt[r] = pixelValues[r];
                            yBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenY - 1; r++) {
                            yBoundariesInt[r] = pixelValues[r + 1];
                            yBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != 0 && mouseY2 < yBoundariesInt[i - 1] && mouseY2 > yBoundariesInt[i + 1]) {
                        yBoundariesInt[i] = mouseY2;
                        yBoundaries[i] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                } else if ((mouseY2 - mouseY1) < 0 && (i - 1) >= 0) {
                    v1 = yBoundariesInt[i - 1];
                    if (Math.abs(mouseY2 - v1) <= 3) {
                        yBoundariesInt = new int[lenY - 1];
                        yBoundaries = new double[lenY - 1];
                        for (int r = 0; r < i; r++) {
                            yBoundariesInt[r] = pixelValues[r];
                            yBoundaries[r] = values[r];
                        }
                        for (int r = i; r < lenY - 1; r++) {
                            yBoundariesInt[r] = pixelValues[r + 1];
                            yBoundaries[r] = values[r + 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    } else if (i != lenY - 1 && mouseY2 < yBoundariesInt[i - 1] && mouseY2 > yBoundariesInt[i + 1]) {
                        yBoundariesInt[i] = mouseY2;
                        yBoundaries[i] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_MOVED);
                    }
                }
                if (i == 0) {
                    v1 = yBoundariesInt[1];
                    if ((mouseY2 - mouseY1) < 0 && (mouseY2 > v1)) {
                        yBoundariesInt = new int[lenY + 1];
                        yBoundaries = new double[lenY + 1];
                        yBoundariesInt[0] = pixelValues[0];
                        yBoundaries[0] = values[0];
                        yBoundariesInt[1] = mouseY2;
                        yBoundaries[1] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        for (int r = 2; r < lenY + 1; r++) {
                            yBoundariesInt[r] = pixelValues[r - 1];
                            yBoundaries[r] = values[r - 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
                if (i == lenY - 1) {
                    v1 = yBoundariesInt[i - 1];
                    if ((mouseY2 - mouseY1) > 0 && (mouseY2 < v1)) {
                        yBoundariesInt = new int[lenY + 1];
                        yBoundaries = new double[lenY + 1];
                        for (int r = 0; r < lenY - 1; r++) {
                            yBoundariesInt[r] = pixelValues[r];
                            yBoundaries[r] = values[r];
                        }
                        yBoundariesInt[i] = mouseY2;
                        yBoundaries[i] = getValueFromScreenValue(mouseY2, yScale, plotOriginY, yBoundaries[0]);
                        yBoundariesInt[lenY] = pixelValues[lenY - 1];
                        yBoundaries[lenY] = values[lenY - 1];
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                    }
                    break;
                }
            }
        }
        this.repaint();
    }

    /**
	 * Mouse click for selecting or brushing points (observations).
	 * 
	 * @param e
	 */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("mouse clicked: ");
        }
        int count = e.getClickCount();
        int[] mousePos = new int[2];
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        if (dataX == null) {
            return;
        }
        if (count == 1) {
            if (Arrays.equals(dataX, dataY)) {
                for (int i = 0; i < dataX.length; i++) {
                    if ((exsint[i] - 5 < mousePos[0]) && (mousePos[0] < exsint[i] + 5) && (whyint[i] - 5 < mousePos[1]) && (mousePos[1] < whyint[i] + 5) && (conditionArray[i] > -1)) {
                        selections[i] = 1;
                    }
                }
                fireActionPerformed(COMMAND_POINT_SELECTED);
            }
        }
        if (count == 2) {
            int v1, v2;
            int lenY = yBoundariesInt.length;
            int lenX = xBoundariesInt.length;
            int[] pixelValues = yBoundariesInt.clone();
            double[] values = yBoundaries.clone();
            if ((mousePos[0] - xBoundariesInt[0]) < 50) {
                for (int i = 0; i < yBoundariesInt.length - 1; i++) {
                    v1 = yBoundariesInt[i];
                    v2 = yBoundariesInt[i + 1];
                    if ((mousePos[1] - v1) < -5 && (v2 - mousePos[1]) < -5) {
                        yBoundariesInt = new int[lenY + 1];
                        yBoundaries = new double[lenY + 1];
                        for (int j = 0; j <= i; j++) {
                            yBoundariesInt[j] = pixelValues[j];
                            yBoundaries[j] = values[j];
                        }
                        yBoundariesInt[i + 1] = mousePos[1];
                        yBoundaries[i + 1] = getValueFromScreenValue(mousePos[1], yScale, plotOriginY, yBoundaries[0]);
                        for (int j = i + 2; j < lenY + 1; j++) {
                            yBoundariesInt[j] = pixelValues[j - 1];
                            yBoundaries[j] = values[j - 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                        break;
                    }
                }
            }
            if ((yBoundariesInt[0] - mousePos[1]) < 50) {
                pixelValues = xBoundariesInt.clone();
                values = xBoundaries.clone();
                for (int i = 0; i < xBoundariesInt.length - 1; i++) {
                    v1 = xBoundariesInt[i];
                    v2 = xBoundariesInt[i + 1];
                    if ((mousePos[0] - v1) > 5 && (v2 - mousePos[0]) > 5) {
                        xBoundariesInt = new int[lenX + 1];
                        xBoundaries = new double[lenX + 1];
                        for (int j = 0; j <= i; j++) {
                            xBoundariesInt[j] = pixelValues[j];
                            xBoundaries[j] = values[j];
                        }
                        xBoundariesInt[i + 1] = mousePos[0];
                        xBoundaries[i + 1] = getValueFromScreenValue(mousePos[0], xScale, plotOriginX, xBoundaries[0]);
                        for (int j = i + 2; j < lenX + 1; j++) {
                            xBoundariesInt[j] = pixelValues[j - 1];
                            xBoundaries[j] = values[j - 1];
                        }
                        fireActionPerformed(BivariateLegendWithScatterPlot.COMMAND_BOUNDARIES_NUMBERCHANGED);
                        break;
                    }
                }
            }
        }
    }

    /**
	 * New data ranges setup dialog.
	 * 
	 * @param x
	 * @param y
	 */
    @Override
    public void showDialog(int x, int y) {
        JFrame dummyFrame = new JFrame();
        JDialog dialog = new JDialog(dummyFrame, "Data Range Configuer", true);
        JButton actionButton;
        JButton resetButton;
        dialog.setLocation(x, y);
        dialog.setSize(300, 150);
        dialog.getContentPane().setLayout(new GridLayout(5, 2));
        xAxisMinField.setText(Double.toString(xAxisExtents[0]));
        xAxisMaxField.setText(Double.toString(xAxisExtents[1]));
        yAxisMinField.setText(Double.toString(yAxisExtents[0]));
        yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
        actionButton = new JButton("OK");
        actionButton.addActionListener(new java.awt.event.ActionListener() {

            /**
			 * Button to set up new data ranges shown up in scatter plot.
			 * 
			 * @param e
			 */
            public void actionPerformed(ActionEvent e) {
                try {
                    actionButton_actionPerformed(e);
                } catch (Exception exception) {
                }
            }
        });
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {

            /**
			 * put your documentation comment here
			 * 
			 * @param e
			 */
            public void actionPerformed(ActionEvent e) {
                resetButton_actionPerformed(e);
            }
        });
        dialog.getContentPane().add(new JLabel((attributeXName + " Min")));
        dialog.getContentPane().add(xAxisMinField);
        dialog.getContentPane().add(new JLabel((attributeXName + " Max")));
        dialog.getContentPane().add(xAxisMaxField);
        dialog.getContentPane().add(new JLabel((attributeYName + " Min")));
        dialog.getContentPane().add(yAxisMinField);
        dialog.getContentPane().add(new JLabel((attributeYName + " Max")));
        dialog.getContentPane().add(yAxisMaxField);
        dialog.getContentPane().add(actionButton);
        dialog.getContentPane().add(resetButton);
        dialog.setVisible(true);
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
    @Override
    public void maybeShowPopup(MouseEvent e) {
        {
            getPopup().show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
	 * Set up new data ranges to show.
	 * 
	 * @param e
	 */
    @Override
    public void actionButton_actionPerformed(ActionEvent e) {
        xAxisExtents[0] = Double.parseDouble(xAxisMinField.getText());
        xAxisExtents[1] = Double.parseDouble(xAxisMaxField.getText());
        yAxisExtents[0] = Double.parseDouble(yAxisMinField.getText());
        yAxisExtents[1] = Double.parseDouble(yAxisMaxField.getText());
        dataArrayX.setExtent(xAxisExtents);
        dataArrayY.setExtent(yAxisExtents);
        setupDataforDisplay();
        fireActionPerformed(COMMAND_DATARANGE_SET);
        logger.finest("ok, fire event.");
        repaint();
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
    @Override
    public void resetButton_actionPerformed(ActionEvent e) {
        dataArrayX.setDataExtent();
        dataArrayY.setDataExtent();
        if (axisOn) {
            xAxisExtents = dataArrayX.getMaxMinCoorValue().clone();
            yAxisExtents = dataArrayY.getMaxMinCoorValue().clone();
        } else {
            xAxisExtents[0] = dataArrayX.getExtent()[0];
            xAxisExtents[1] = dataArrayX.getExtent()[1];
            yAxisExtents[0] = dataArrayY.getExtent()[0];
            yAxisExtents[1] = dataArrayY.getExtent()[1];
        }
        xAxisMinField.setText(Double.toString(xAxisExtents[0]));
        xAxisMaxField.setText(Double.toString(xAxisExtents[1]));
        yAxisMinField.setText(Double.toString(yAxisExtents[0]));
        yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
        setupDataforDisplay();
        fireActionPerformed(COMMAND_DATARANGE_SET);
        repaint();
    }
}
