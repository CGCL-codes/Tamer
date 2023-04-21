package geovista.geoviz.scatterplot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import javax.swing.BorderFactory;
import geovista.common.data.StatisticsVectors;

public class ScatterPlotWithDistContour extends ScatterPlot {

    private double eigenAngle;

    private double meanX, meanY;

    private double stdEigenVector1, stdEigenVector2;

    private int meanXInt, meanYInt;

    private int ellipseMajorXInt, ellipseMajorYInt;

    private int ellipseMinorXInt, ellipseMinorYInt;

    private int ellipseMajorX2Int, ellipseMajorY2Int;

    private int ellipseMinorX2Int, ellipseMinorY2Int;

    private EigenValuesVectors eigen;

    private EigenValuesVectors eigenDisplay;

    private double[][] covariance;

    private double[] stdInt;

    public ScatterPlotWithDistContour() {
        super();
    }

    /**
	 * Set up data and axis for drawing the scatter plot.
	 */
    @Override
    public void initialize() {
        setRegressionClassName("geovista.geoviz.scatterplot.LinearRegression");
        dataArrayX = new DataArray(dataX);
        dataArrayY = new DataArray(dataY);
        conditionArray = new int[dataX.length];
        setBorder(BorderFactory.createLineBorder(Color.gray));
        if (axisOn) {
            xAxisExtents = dataArrayX.getMaxMinCoorValue().clone();
            yAxisExtents = dataArrayY.getMaxMinCoorValue().clone();
        } else {
            xAxisExtents[0] = dataArrayX.getExtent()[0];
            xAxisExtents[1] = dataArrayX.getExtent()[1];
            yAxisExtents[0] = dataArrayY.getExtent()[0];
            yAxisExtents[1] = dataArrayY.getExtent()[1];
        }
        size = this.getSize();
        makeColors();
        setVisiblePlotLine(dataX, dataY, true);
        setUpDistributionContour();
        setupDataforDisplay();
        setBackground(background);
    }

    private void setUpDistributionContour() {
        eigen = new EigenValuesVectors();
        eigenDisplay = new EigenValuesVectors();
        eigen.setData(dataX, dataY);
        meanX = StatisticsVectors.mean(dataX);
        meanY = StatisticsVectors.mean(dataY);
        double[] stds = eigen.getStd();
        stdEigenVector1 = stds[1];
        stdEigenVector2 = stds[0];
        covariance = eigen.getCovariance();
    }

    @Override
    protected void setupDataforDisplay() {
        super.setupDataforDisplay();
        if (this.getSize().width == 0) {
            return;
        }
        double xScale;
        double yScale;
        double[][] covarianceDisplay = new double[2][2];
        if (attributeXName.equals("pcincome")) {
            xScale = 0;
        }
        xScale = getScale(plotOriginX, plotEndX, xAxisExtents[0], xAxisExtents[1]);
        yScale = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
        covarianceDisplay[0][0] = covariance[0][0] * xScale * xScale;
        covarianceDisplay[0][1] = covariance[0][1] * xScale * yScale;
        covarianceDisplay[1][0] = covariance[1][0] * yScale * xScale;
        covarianceDisplay[1][1] = covariance[1][1] * yScale * yScale;
        eigenDisplay.setCovariance(covarianceDisplay);
        stdInt = eigenDisplay.getStd();
        eigenAngle = Math.PI - Math.atan(eigenDisplay.getEigenVectors()[1][1] / eigenDisplay.getEigenVectors()[0][1]);
        meanXInt = this.getValueScreen(meanX, xScale, plotOriginX, xAxisExtents[0]);
        meanYInt = this.getValueScreen(meanY, yScale, plotOriginY, yAxisExtents[0]);
        ellipseMajorXInt = (int) (meanXInt - stdInt[1] * eigenDisplay.getEigenVectors()[0][1]);
        ellipseMajorYInt = (int) (meanYInt - stdInt[1] * eigenDisplay.getEigenVectors()[1][1]);
        ellipseMinorXInt = (int) (meanXInt - stdInt[0] * eigenDisplay.getEigenVectors()[0][0]);
        ellipseMinorYInt = (int) (meanYInt - stdInt[0] * eigenDisplay.getEigenVectors()[1][0]);
        ellipseMajorX2Int = (int) (meanXInt + stdInt[1] * eigenDisplay.getEigenVectors()[0][1]);
        ellipseMajorY2Int = (int) (meanYInt + stdInt[1] * eigenDisplay.getEigenVectors()[1][1]);
        ellipseMinorX2Int = (int) (meanXInt + stdInt[0] * eigenDisplay.getEigenVectors()[0][0]);
        ellipseMinorY2Int = (int) (meanYInt + stdInt[0] * eigenDisplay.getEigenVectors()[1][0]);
    }

    /**
	 * Draw the scatter plot.
	 * 
	 * @param g
	 */
    @Override
    public void paintComponent(Graphics g) {
        if (dataIndices == null) {
            return;
        }
        g.setColor(background);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(foreground);
        paintBorder(g);
        if (axisOn) {
            drawAxis(g);
        }
        drawPlot(g);
        drawContour(g);
        drawEigenVectorsCross(g);
        Graphics2D g2 = (Graphics2D) g;
        if (exLabels != null && axisOn == true) {
            setToolTipText("");
            exLabels.paint(g2);
        }
        if (plotLine) {
            if (dataIndices[0] != dataIndices[1] && regressionClass != null) {
                if (pointSelected == true && plotLineForSelections) {
                    g.setColor(Color.gray);
                } else {
                    drawCorrelationValue(g, correlation, rSquare);
                    g.setColor(Color.red);
                }
                drawPlotLine(g, yStartPosition, yEndPosition);
            }
        }
        if (plotLineForSelections) {
            if (dataIndices[0] != dataIndices[1] && regressionClass != null) {
                if (pointSelected == true) {
                    drawCorrelationValue(g, correlationForSelections, rSquareForSelections);
                    g.setColor(Color.red);
                    drawPlotLine(g, yStartPositionSelections, yEndPositionSelections);
                }
            }
        }
        if (indiationId >= 0 && dataIndices[0] != dataIndices[1]) {
            drawIndication(g2, indiationId);
        }
    }

    private void drawContour(Graphics g) {
        if (dataIndices[0] == dataIndices[1] || stdInt == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.blue);
        g2d.rotate(-eigenAngle, meanXInt, meanYInt);
        g2d.drawOval(meanXInt - (int) stdInt[1], meanYInt - (int) stdInt[0], 2 * (int) stdInt[1], 2 * (int) stdInt[0]);
        g2d.rotate(eigenAngle, meanXInt, meanYInt);
    }

    private void drawEigenVectorsCross(Graphics g) {
        if (dataIndices[0] == dataIndices[1]) {
            return;
        }
        g.setColor(Color.black);
        g.drawLine(ellipseMajorXInt, ellipseMajorYInt, ellipseMajorX2Int, ellipseMajorY2Int);
        g.setColor(Color.gray);
        g.drawLine(ellipseMinorXInt, ellipseMinorYInt, ellipseMinorX2Int, ellipseMinorY2Int);
    }

    public Shape toShape() {
        Ellipse2D.Double e = new Ellipse2D.Double(meanXInt - stdEigenVector1, meanYInt - stdEigenVector2, stdEigenVector1 * 2, stdEigenVector2 * 2);
        AffineTransform at = AffineTransform.getRotateInstance(eigenAngle, meanXInt, meanYInt);
        return at.createTransformedShape(e);
    }
}
