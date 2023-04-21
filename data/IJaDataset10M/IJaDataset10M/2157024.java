package geovista.geoviz.scatterplot;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.EventListenerList;
import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.ui.ExcentricLabelClient;
import geovista.common.ui.ExcentricLabels;
import geovista.common.ui.VisualSettingsPopupAdapter;
import geovista.common.ui.VisualSettingsPopupListener;
import geovista.common.ui.VisualSettingsPopupMenu;
import geovista.image_blur.image.BoxBlurFilter;
import geovista.symbolization.BivariateColorClassifier;
import geovista.symbolization.BivariateColorClassifierSimple;
import geovista.symbolization.ColorInterpolator;

public class ScatterPlotBasic extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, ExcentricLabelClient, IndicationListener, VisualSettingsPopupListener {

    private boolean useSelectionFade = true;

    private boolean useSelectionBlur = true;

    public static final double AXISSPACEPORTION = 1.0 / 6.0;

    public static final String COMMAND_POINT_SELECTED = "cmdSel";

    public static final String COMMAND_DATARANGE_SET = "cmdset";

    protected static final int RADIUS = 5;

    protected int pointSize = RADIUS;

    protected transient int plotOriginX;

    protected transient int plotOriginY;

    protected transient int plotEndX;

    protected transient int plotEndY;

    protected transient Object[] dataObject;

    protected transient DataSetForApps dataSet;

    protected transient int[] dataIndices;

    protected transient double[] dataX;

    protected transient double[] dataY;

    protected transient int[] exsint;

    protected transient int[] whyint;

    protected String[] attributeArrayNames;

    protected String[] observNames;

    protected transient String attributeXName;

    protected transient String attributeYName;

    protected transient boolean axisOn;

    protected transient Dimension size;

    protected transient Color background;

    protected Color foreground;

    protected Color selectionColor = Color.blue;

    protected Color indicationColor = Color.RED;

    protected boolean selOriginalColorMode = true;

    protected transient Color[] multipleSelectionColors;

    protected transient DataArray dataArrayX;

    protected transient DataArray dataArrayY;

    protected double[] xAxisExtents = new double[2];

    protected double[] yAxisExtents = new double[2];

    protected transient int selectX = 0;

    protected transient int selectY = 0;

    protected transient int selectWidth = 0;

    protected transient int selectHeight = 0;

    protected transient BasicStroke strokeDashed;

    protected transient int[] selections;

    protected transient Vector selRecords;

    protected transient double[] selectedDataX;

    protected transient double[] selectedDataY;

    transient boolean pointSelected = false;

    protected transient int[] conditionArray;

    protected transient int mouseX1, mouseX2, mouseY1, mouseY2;

    protected transient JPopupMenu popup;

    protected JTextField xAxisMinField = new JTextField(16);

    protected JTextField xAxisMaxField = new JTextField(16);

    protected JTextField yAxisMinField = new JTextField(16);

    protected JTextField yAxisMaxField = new JTextField(16);

    protected EventListenerList listenerListAction = new EventListenerList();

    protected transient Color[] pointColors;

    protected transient BivariateColorClassifier bivarColorClasser;

    protected Histogram histogram;

    protected transient ExcentricLabels exLabels;

    transient int count = 0;

    transient int firstBar;

    transient int lastBar;

    transient double yBarDistance;

    transient double xBarDistance;

    protected transient ScatterPlotBasic detailSP;

    protected transient JFrame dlgSP;

    protected boolean plotLine = true;

    protected transient double slope;

    protected transient double intercept;

    protected transient double correlation;

    protected transient double rSquare;

    protected transient double yStartPosition;

    protected transient double yEndPosition;

    protected boolean plotLineForSelections = true;

    protected transient double slopeForSelections;

    protected transient double interceptForSelections;

    protected transient double correlationForSelections;

    protected transient double rSquareForSelections;

    protected transient double yStartPositionSelections;

    protected transient double yEndPositionSelections;

    private final BufferedImage indicationStamp;

    private final int stampSize = 80;

    private transient Image drawingBuff;

    static final Logger logger = Logger.getLogger(ScatterPlotBasic.class.getName());

    protected int indiationId = -1;

    /**
	 * put your documentation comment here
	 */
    public ScatterPlotBasic() {
        super();
        bivarColorClasser = new BivariateColorClassifierSimple();
        VisualSettingsPopupMenu popMenu = new VisualSettingsPopupMenu(this);
        MouseAdapter listener = new VisualSettingsPopupAdapter(popMenu);
        popMenu.addMouseListener(listener);
        addMouseListener(listener);
        histogram = new Histogram();
        isDoubleBuffered();
        indicationStamp = GradientStamp.makeGradientStamp(stampSize);
        setPreferredSize(new Dimension(300, 300));
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        initialize();
    }

    /**
	 * @param data
	 * 
	 *            This method is deprecated becuase it wants to create its very
	 *            own pet DataSetForApps. This is no longer allowed, to allow
	 *            for a mutable, common data set. Use of this method may lead to
	 *            unexpected program behavoir. Please use setDataSet instead.
	 */
    @Deprecated
    public void setData(Object[] data) {
        setDataSet(new DataSetForApps(data));
    }

    public void setDataSet(DataSetForApps data) {
        if (data == null) {
            logger.info("setting null data!");
            return;
        }
        dataSet = data;
        dataObject = data.getDataSetNumericAndSpatial();
        attributeArrayNames = (String[]) dataObject[0];
        int len = attributeArrayNames.length;
        if (dataObject[len + 1] == null) {
            observNames = null;
        } else {
            observNames = (String[]) dataObject[len + 1];
        }
        initExcentricLabels();
        initialize();
        this.repaint();
    }

    /**
	 * Set up x and y axises in each element by setting the attributes
	 * displayed.
	 * 
	 * @param indices
	 */
    public void setDataIndices(int[] indices) {
        dataIndices = indices;
        axisDataSetup();
        initialize();
    }

    protected void axisDataSetup() {
        int len = 0;
        dataX = dataSet.getNumericDataAsDouble(dataIndices[0]);
        len = dataX.length;
        dataY = dataSet.getNumericDataAsDouble(dataIndices[1]);
        attributeXName = attributeArrayNames[dataIndices[0]];
        attributeYName = attributeArrayNames[dataIndices[1]];
        selections = new int[len];
        exsint = new int[len];
        whyint = new int[len];
        if (dataX == null) {
            return;
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.info("*************");
            logger.info("Data 0 = " + dataIndices[0]);
            logger.info("X var = " + attributeXName);
            logger.info("X1 = " + dataX[0]);
            logger.info("Data 1 = " + dataIndices[1]);
            logger.info("y var = " + attributeYName);
            logger.info("y1 = " + dataY[0]);
            logger.info("*************");
        }
    }

    /**
	 * put your documentation comment here
	 * 
	 * @return
	 */
    public int[] getElementPosition() {
        return dataIndices;
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param attributeArrayNames
	 */
    public void setAttributeArrays(String[] attributeArrays) {
        attributeArrayNames = attributeArrays;
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param dataX
	 */
    public void setX(double[] dataX) {
        this.dataX = dataX;
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param dataY
	 */
    public void setY(double[] dataY) {
        this.dataY = dataY;
    }

    public void setAxisOn(boolean axisOn) {
        this.axisOn = axisOn;
    }

    public void setAttributeX(String attributeX) {
        attributeXName = attributeX;
    }

    public void setAttributeY(String attributeY) {
        attributeYName = attributeY;
    }

    public void setData(double[] dataX, double[] dataY) {
        this.dataX = dataX;
        this.dataY = dataY;
        int len = dataX.length;
        selections = new int[len];
        for (int i : selections) {
            selections[i] = 1;
        }
        exsint = new int[len];
        whyint = new int[len];
        if ((drawingBuff == null) && (getWidth() > 0) && (getHeight() > 0)) {
            drawingBuff = this.createImage(getWidth(), getHeight());
        }
        initialize();
    }

    /**
	 * Minimum and maximum values for xAxis. xAxisExtents[0] = min,
	 * xAxisExtents[1] = max.
	 * 
	 * @param xAxisExtents
	 */
    public void setXAxisExtents(double[] xAxisExtents) {
        this.xAxisExtents = xAxisExtents.clone();
        setupDataforDisplay();
        paintDrawingBuff();
        repaint();
    }

    /**
	 * Minimum and maximum values for yAxis. yAxisExtents[0] = min,
	 * yAxisExtents[1] = max.
	 * 
	 * @param yAxisExtents
	 */
    public void setYAxisExtents(double[] yAxisExtents) {
        this.yAxisExtents = yAxisExtents.clone();
        setupDataforDisplay();
        paintDrawingBuff();
        repaint();
    }

    /**
	 * return Minimum and maximum values for xAxis.
	 * 
	 * @return xAxisExtents
	 */
    public double[] getXAxisExtents() {
        return xAxisExtents;
    }

    /**
	 * return Minimum and maximum values for yAxis.
	 * 
	 * @return yAxisExtents
	 */
    public double[] getYAxisExtents() {
        return yAxisExtents;
    }

    /**
	 * pass conditioning into scatterplot. Observations which are shown are
	 * marked as 1.
	 * 
	 * @param conditionArray
	 */
    public void setConditionArray(int[] conditionArray) {
        this.conditionArray = conditionArray;
    }

    /**
	 * defind background color for graph.
	 * 
	 * @param c
	 */
    @Override
    public void setBackground(Color c) {
        if (c == null) {
            return;
        }
        background = c;
        int colorTotal = c.getRed() + c.getGreen() + c.getBlue();
        int greyColor = 128 * 3;
        if (colorTotal < greyColor) {
            foreground = Color.white;
        } else {
            foreground = Color.black;
        }
        this.repaint();
    }

    /**
	 * defind selection color for graph.
	 * 
	 * @param c
	 */
    public void setSelectionColor(Color c) {
        selectionColor = c;
        paintDrawingBuff();
        this.repaint();
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setPointSelected(boolean pointSelected) {
        this.pointSelected = pointSelected;
    }

    public void setMultipleSelectionColors(Color[] c) {
        multipleSelectionColors = c;
    }

    public void setSelOriginalColorMode(boolean selOriginalColorMode) {
        this.selOriginalColorMode = selOriginalColorMode;
        paintDrawingBuff();
        repaint();
    }

    public String getShortDiscription() {
        return "XYP";
    }

    public JToolBar getTools() {
        return null;
    }

    /**
	 * Set up data and axis for drawing the scatter plot.
	 */
    protected void initialize() {
        if (dataX == null) {
            return;
        }
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
        if (dataIndices[0] == dataIndices[1]) {
            histogram.setSize(this.getSize());
            histogram.setVariableName(attributeXName);
            histogram.setData(dataX);
        }
        size = this.getSize();
        setBackground(background);
        setVisiblePlotLine(dataX, dataY, true);
        setupDataforDisplay();
    }

    /**
	 * Set the size of plot area for both axis on and axis off situations.
	 * 
	 * @param axisOn
	 */
    protected void setVisibleAxis(boolean axisOn) {
        if (dataArrayX == null) {
            return;
        }
        if (axisOn) {
            plotOriginX = (int) (getWidth() * AXISSPACEPORTION);
            plotOriginY = (int) (getHeight() * (1 - AXISSPACEPORTION));
            plotEndX = (int) (this.getSize().getWidth()) - (int) (this.getSize().getWidth() * AXISSPACEPORTION / 2);
            plotEndY = (int) (this.getSize().getHeight() / 12);
        } else {
            plotOriginX = 3;
            plotOriginY = (int) (this.getSize().getHeight() - 3);
            plotEndX = (int) (this.getSize().getWidth()) - 3;
            plotEndY = 3;
        }
    }

    private void paintDrawingBuff() {
        if (drawingBuff == null) {
            return;
        }
        Graphics g = drawingBuff.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(foreground);
        paintBorder(g);
        if (useSelectionBlur) {
            paintOnlyDeselected(g2);
            BoxBlurFilter filter = new BoxBlurFilter();
            filter.setHRadius(2);
            filter.setVRadius(2);
            filter.setIterations(2);
            BufferedImage blurBuff = new BufferedImage(drawingBuff.getWidth(this), drawingBuff.getHeight(this), BufferedImage.TYPE_INT_ARGB);
            blurBuff.getGraphics().drawImage(drawingBuff, 0, 0, this);
            filter.filter(blurBuff, blurBuff);
            g2.drawImage(blurBuff, null, 0, 0);
            paintOnlySelected(g2);
        } else {
            paintOnlyDeselected(g2);
            paintOnlySelected(g2);
        }
        if (axisOn) {
            drawAxis(g);
        }
        if (exLabels != null && axisOn == true) {
            setToolTipText("");
            exLabels.paint(g2);
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("in scatterplotbasic, plotLine = " + plotLine);
        }
        if (plotLine) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("in scatterplotbasic, going to paint line.");
            }
            if (dataIndices != null && dataIndices[0] != dataIndices[1] && regressionClass != null) {
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
            if (dataIndices != null && dataIndices[0] != dataIndices[1] && regressionClass != null) {
                if (pointSelected == true) {
                    drawCorrelationValue(g, correlationForSelections, rSquareForSelections);
                    g.setColor(Color.red);
                    drawPlotLine(g, yStartPositionSelections, yEndPositionSelections);
                }
            }
        }
    }

    private void paintOnlySelected(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (dataArrayX == null || pointColors == null) {
            return;
        }
        if (selections.length == 0) {
            selections = new int[dataArrayX.length()];
            for (int i : selections) {
                selections[i] = 1;
            }
        }
        for (int i = 0; i < dataArrayX.length(); i++) {
            if (selections[i] == 1) {
                if (selectionOutline) {
                    Stroke stro = g2.getStroke();
                    g2.setColor(selectionColor);
                    g2.setStroke(selectionStroke);
                    g2.drawOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
                    g2.setStroke(stro);
                }
                Color drawColor = pointColors[i];
                g2.setColor(drawColor);
                g2.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
            }
        }
    }

    private void paintOnlyDeselected(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        Color colorBlur = new Color(248, 248, 248, 255);
        if (dataArrayX == null || pointColors == null) {
            return;
        }
        if (selections.length == 0) {
            selections = new int[dataArrayX.length()];
            for (int i : selections) {
                selections[i] = 1;
            }
        }
        if (pointColors.length != dataArrayX.length()) {
            makeColors();
        }
        for (int i = 0; i < dataArrayX.length(); i++) {
            if (selections[i] == 0) {
                Color drawColor = pointColors[i];
                if (useSelectionFade) {
                    drawColor = ColorInterpolator.mixColorsRGB(colorBlur, drawColor);
                }
                g2.setColor(drawColor);
                g2.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
            }
        }
    }

    /**
	 * Draw the scatter plot.
	 * 
	 * @param g
	 */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (dataIndices == null) {
            return;
        }
        if (dataIndices[0] == dataIndices[1]) {
            logger.info("painting histogram inside SP");
            drawPlot(g);
            return;
        }
        if (drawingBuff == null) {
            drawingBuff = this.createImage(getWidth(), getHeight());
            paintDrawingBuff();
        }
        if (drawingBuff != null) {
            g.drawImage(drawingBuff, 0, 0, this);
        }
        if (indiationId >= 0 && dataIndices[0] != dataIndices[1]) {
            drawIndication(g2, indiationId);
        }
        Rectangle rec = new Rectangle(selectX, selectY, selectWidth, selectHeight);
        Graphics2D g2d = (Graphics2D) g;
        drawSelectRectangle(g2d, rec);
        if (exLabels != null && exLabels.isVisible()) {
            exLabels.paint(g2);
        }
    }

    /**
	 * Draw pot (points) on the screen.
	 * 
	 * @param g
	 */
    protected void drawPlot(Graphics g) {
        if (dataIndices == null) {
            int[] data = { 0, 1 };
            dataIndices = data;
        }
        int plotWidth, plotHeight;
        plotWidth = getWidth();
        plotHeight = getHeight();
        int size;
        size = (plotWidth < plotHeight) ? plotWidth : plotHeight;
        pointSize = (size < 360) ? size / 36 : 10;
        pointSize = (pointSize < 3) ? 4 : pointSize + 1;
        if (dataIndices[0] == dataIndices[1]) {
            histogram.setAxisOn(false);
            histogram.setVariableName(attributeXName);
            histogram.setData(dataX);
            histogram.setXAxisExtents(xAxisExtents);
            histogram.setBackground(background);
            histogram.setSize(getWidth(), getHeight());
            IndicationEvent e = new IndicationEvent(this, indiationId);
            histogram.indicationChanged(e);
            histogram.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Color half = new Color(255, 255, 255, 100);
            g2.setColor(half);
            g.fillRect(0, 0, getSize().width, getSize().height);
            g2.setColor(foreground);
            Font font = new Font("", Font.PLAIN, size / 8);
            g.setFont(font);
            Font font1 = new Font("", Font.PLAIN, size / 12);
            g.setFont(font1);
            g.drawLine(0, 0, 5, 5);
            String maxString = Float.toString((float) (xAxisExtents[1]));
            g.drawString(maxString, 6, (int) (plotHeight * AXISSPACEPORTION / 2) + 2);
            g.drawLine(0, plotHeight, 5, plotHeight - 5);
            String minString = Float.toString((float) (xAxisExtents[0]));
            g.drawString(minString, 6, plotHeight - 5);
            g.drawLine(plotWidth, plotHeight, plotWidth - 5, plotHeight - 5);
            g.drawString(maxString, plotWidth - (int) (plotWidth * AXISSPACEPORTION + 5), plotHeight - 5);
        } else {
            if (dataArrayX != null) {
                int len = dataArrayX.length();
                drawSelections(g, len);
            }
        }
    }

    /**
	 * Add by jin Chen: Given a data id, draw the data in the plot given
	 * indication specific code by frank hardisty
	 */
    protected void drawIndication(Graphics2D g, int i) {
        if (pointColors == null || pointColors.length <= i || pointColors[i] == null) {
            return;
        }
        Graphics2D g2 = g;
        int x = exsint[i];
        int y = whyint[i];
        int pointDrawX = x - (pointSize / 2) + 1;
        int pointDrawY = y - (pointSize / 2) + 1;
        Stroke tempStroke = g2.getStroke();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        BasicStroke secondStroke = new BasicStroke(6f);
        BasicStroke underStroke = new BasicStroke(20f);
        g2.setColor(new Color(255, 0, 0, 68));
        g2.setStroke(underStroke);
        g2.drawOval(pointDrawX, pointDrawY, pointSize, pointSize);
        g2.drawImage(indicationStamp, x - (stampSize / 2), y - (stampSize / 2), this);
        g2.setStroke(secondStroke);
        g2.setColor(Color.black);
        g2.drawOval(pointDrawX, pointDrawY, pointSize, pointSize);
        g2.setStroke(tempStroke);
        g2.setColor(pointColors[i]);
        g2.fillOval(pointDrawX, pointDrawY, pointSize, pointSize);
    }

    protected void drawSelections(Graphics g, int len) {
        logger.info("in scatterplotbasic, drawslections");
        g.setColor(foreground);
        for (int i = 0; i < len; i++) {
            if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY) && (whyint[i] >= plotEndY) && (conditionArray[i] > -1)) {
                drawObs(g, i);
            }
        }
        if (pointSelected) {
            for (int i = 0; i < len; i++) {
                g.setColor(selectionColor);
                if ((exsint[i] <= plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY) && (whyint[i] >= plotEndY) && (conditionArray[i] > -1)) {
                    if (selections[i] == 1) {
                        drawObs(g, i);
                    }
                }
            }
        }
    }

    private void drawObs(Graphics g, int i) {
        g.drawOval(exsint[i] - pointSize / 2, whyint[i] - pointSize / 2, pointSize, pointSize);
    }

    /**
	 * Draw axises for scatterplot.
	 * 
	 * @param g
	 */
    protected void drawAxis(Graphics g) {
        if (dataArrayY == null) {
            return;
        }
        int plotWidth, plotHeight;
        plotWidth = getWidth();
        plotHeight = getHeight();
        if (!(dataIndices[0] == dataIndices[1])) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("plotOriginX = " + plotOriginX);
            }
            g.setColor(foreground);
            g.drawLine(plotOriginX, plotEndY, plotOriginX, plotOriginY);
            g.drawLine(plotOriginX, plotOriginY, plotEndX, plotOriginY);
            int fontSize;
            fontSize = (plotWidth < plotHeight) ? plotWidth : plotHeight;
            fontSize = ((fontSize / 32) < 9) ? 9 : fontSize / 32;
            Font font = new Font("", Font.PLAIN, fontSize);
            g.setFont(font);
            String scaleStringY;
            int i;
            int realBarNum = 0;
            double barNumber = dataArrayY.getTickNumber();
            int firstBar = (int) (yAxisExtents[0] / (dataArrayY.getMajorTick()));
            int lastBar = (int) (yAxisExtents[1] / (dataArrayY.getMajorTick()));
            double yBarDistance = ((plotOriginY - plotEndY) / barNumber);
            for (i = firstBar; i <= lastBar + 0.00001; i++) {
                g.drawLine(plotOriginX - 3, plotEndY + (int) (realBarNum * yBarDistance), plotOriginX, plotEndY + (int) (realBarNum * yBarDistance));
                if (Math.abs(dataArrayY.getMajorTick()) <= 1) {
                    scaleStringY = Float.toString((float) (yAxisExtents[1] - realBarNum * dataArrayY.getMajorTick()));
                } else {
                    scaleStringY = Integer.toString((int) (yAxisExtents[1] - realBarNum * dataArrayY.getMajorTick()));
                }
                g.drawString(scaleStringY, plotOriginX - (int) (plotWidth * AXISSPACEPORTION / 2), plotEndY + (int) (realBarNum * yBarDistance + yBarDistance * 1 / 6));
                g.setColor(Color.lightGray);
                g.drawLine(plotOriginX, plotEndY + (int) (realBarNum * yBarDistance), plotEndX, plotEndY + (int) (realBarNum * yBarDistance));
                g.setColor(foreground);
                realBarNum++;
            }
            realBarNum = 0;
            barNumber = dataArrayX.getTickNumber();
            double xBarDistance = ((plotEndX - plotOriginX) / barNumber);
            String scaleStringX;
            for (i = (int) (xAxisExtents[0] / (dataArrayX.getMajorTick())); i <= (int) (xAxisExtents[1] / (dataArrayX.getMajorTick())) + 0.0001; i++) {
                g.drawLine(plotOriginX + (int) (realBarNum * xBarDistance), plotOriginY, plotOriginX + (int) (realBarNum * xBarDistance), plotOriginY + 3);
                if (Math.abs(dataArrayX.getMajorTick()) <= 1) {
                    scaleStringX = Float.toString((float) (xAxisExtents[0] + realBarNum * dataArrayX.getMajorTick()));
                } else {
                    scaleStringX = Integer.toString((int) (xAxisExtents[0] + realBarNum * dataArrayX.getMajorTick()));
                }
                Graphics2D g2d = (Graphics2D) g;
                g2d.rotate(-Math.PI / 4, plotOriginX - 2 + (int) (realBarNum * xBarDistance), plotOriginY + plotHeight * AXISSPACEPORTION * 2 / 3);
                g.drawString(scaleStringX, plotOriginX - 5 + (int) (realBarNum * xBarDistance), plotOriginY + (int) (plotHeight * AXISSPACEPORTION * 2 / 3 - 1));
                g2d.rotate(Math.PI / 4, plotOriginX - 2 + (int) (realBarNum * xBarDistance), plotOriginY + plotHeight * AXISSPACEPORTION * 2 / 3);
                g.setColor(Color.lightGray);
                g.drawLine(plotOriginX + (int) (realBarNum * xBarDistance), plotOriginY, plotOriginX + (int) (realBarNum * xBarDistance), plotEndY);
                g.setColor(foreground);
                realBarNum++;
            }
            font = new Font("", Font.PLAIN, fontSize + 3);
            g.setFont(font);
            g.drawString(attributeXName, plotOriginX + (plotEndX - plotOriginX) / 2 - plotWidth / 12, plotOriginY + plotHeight / 6 - 5);
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(-Math.PI / 2, plotOriginX - plotWidth / 9, plotOriginY - (plotOriginY - plotEndY) / 3);
            g2d.drawString(attributeYName, plotOriginX - plotWidth / 9, plotOriginY - (plotOriginY - plotEndY) / 3);
            g2d.rotate(+Math.PI / 2, plotOriginX - plotWidth / 9, plotOriginY - (plotOriginY - plotEndY) / 3);
        }
    }

    /**
	 * Return selections from this scatterplot.
	 * 
	 * @return
	 */
    public Vector getSelectedObservations() {
        return selRecords;
    }

    /**
	 * Set up selections from other components.
	 * 
	 * @param selectedObservations
	 */
    public void setSelectedObservations(Vector selectedObservations) {
        selRecords = selectedObservations;
    }

    public void setSelections(int[] selectedObservations) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.info("sp sel " + Arrays.toString(selectedObservations));
        }
        if (selections == null || selectedObservations == null) {
            return;
        }
        if (selections.length != selectedObservations.length) {
            for (int i = 0; i < selections.length; i++) {
                selections[i] = 0;
            }
            for (int i = 0; i < selectedObservations.length; i++) {
                selections[selectedObservations[i]] = -1;
            }
        } else {
            selections = selectedObservations;
        }
        int j = 0;
        for (int element : selections) {
            if (element == 1) {
                j++;
            }
        }
        pointSelected = false;
        if (j != 0) {
            selectedDataX = new double[j];
            selectedDataY = new double[j];
            int selIndex = 0;
            for (int i = 0; i < dataX.length; i++) {
                if (selections[i] == 1) {
                    selectedDataX[selIndex] = dataX[i];
                    selectedDataY[selIndex] = dataY[i];
                    selIndex++;
                }
            }
            setVisiblePlotLine(selectedDataX, selectedDataY, false);
            setUpRegressionLine(slopeForSelections, interceptForSelections, false);
            pointSelected = true;
        } else {
            setVisiblePlotLine(dataX, dataY, true);
        }
        paintDrawingBuff();
        this.repaint();
    }

    /**
	 * Return selections from this scatterplot.
	 * 
	 * @return
	 */
    public int[] getSelections() {
        return selections;
    }

    /**
	 * @param indication
	 *            the id the data to be indicated
	 */
    public void setIndication(int indication) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("indicate:" + indication);
        }
        if (dataIndices == null) {
            return;
        }
        indiationId = indication;
        IndicationEvent e = new IndicationEvent(this, indication);
        if (dataIndices[0] == dataIndices[1]) {
            histogram.indicationChanged(e);
        }
        this.repaint();
    }

    /**
	 * Calculate scale between real data and integer data for showing up on
	 * screen.
	 * 
	 * @param min
	 * @param max
	 * @param dataMin
	 * @param dataMax
	 * @return scale
	 */
    protected double getScale(int min, int max, double dataMin, double dataMax) {
        double scale;
        scale = (max - min) / (dataMax - dataMin);
        return scale;
    }

    /**
	 * Convert the single value to integer value worked on screen.
	 * 
	 * @param data
	 * @param scale
	 * @param min
	 * @param dataMin
	 * @return valueScreen
	 */
    protected int getValueScreen(double data, double scale, int min, double dataMin) {
        int valueScreen;
        if (Double.isNaN(data)) {
            valueScreen = Integer.MIN_VALUE;
        } else {
            valueScreen = (int) ((data - dataMin) * scale + min);
        }
        return valueScreen;
    }

    /**
	 * Convert the numeric values of observations to integer value worked on
	 * screen.
	 * 
	 * @param dataArray
	 * @param scale
	 * @param min
	 * @param dataMin
	 * @return valueScreen
	 */
    protected int[] getValueScreen(double[] dataArray, double scale, int min, double dataMin) {
        int[] valueScreen = new int[dataArray.length];
        for (int i = 0; i < dataArray.length; i++) {
            if (Double.isNaN(dataArray[i])) {
                valueScreen[i] = Integer.MIN_VALUE;
            } else {
                valueScreen[i] = (int) ((dataArray[i] - dataMin) * scale + min);
            }
        }
        return valueScreen;
    }

    protected void setupDataforDisplay() {
        setVisibleAxis(axisOn);
        if (dataArrayX == null || dataArrayY == null) {
            logger.info("ScatterPlotBasic null array");
            return;
        }
        int len = dataArrayX.length();
        if (len != dataArrayY.length()) {
            logger.info("ScatterPlotBasic handed arrays of unequal length");
            return;
        }
        double xScale;
        double yScale;
        xScale = getScale(plotOriginX, plotEndX, xAxisExtents[0], xAxisExtents[1]);
        exsint = getValueScreen(dataX, xScale, plotOriginX, xAxisExtents[0]);
        yScale = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
        whyint = getValueScreen(dataY, yScale, plotOriginY, yAxisExtents[0]);
        if (pointSelected && plotLineForSelections) {
            setUpRegressionLine(slope, intercept, true);
            setUpRegressionLine(slopeForSelections, interceptForSelections, false);
        } else {
            setUpRegressionLine(slope, intercept, true);
        }
        paintDrawingBuff();
        this.repaint();
    }

    protected void initExcentricLabels() {
        exLabels = new ExcentricLabels();
        exLabels.setComponent(this);
        exLabels.setOpaque(true);
        Color halfWhite = new Color(255, 255, 255, 123);
        exLabels.setBackgroundColor(halfWhite);
        addMouseListener(exLabels);
    }

    public String getObservationLabel(int i) {
        String[] labels = observNames;
        if (labels != null && labels.length - 1 >= i) {
            return labels[i];
        }
        return "";
    }

    public Shape getShapeAt(int i) {
        int x = exsint[i];
        int y = whyint[i];
        Ellipse2D circle = new Ellipse2D.Float(x, y, pointSize, pointSize);
        return circle;
    }

    public int[] pickAll(Rectangle2D hitBox) {
        Vector hits = new Vector();
        for (int i = 0; i < dataX.length; i++) {
            if (hitBox.contains(exsint[i], whyint[i]) && (conditionArray[i] > -1)) {
                Integer bigI = new Integer(i);
                hits.add(bigI);
            }
        }
        int[] intHits = new int[hits.size()];
        for (int i = 0; i < hits.size(); i++) {
            intHits[i] = ((Integer) hits.get(i)).intValue();
        }
        return intHits;
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        if (getWidth() > 0 && (getHeight() > 0)) {
            drawingBuff = this.createImage(getWidth(), getHeight());
        }
        setupDataforDisplay();
        paintDrawingBuff();
        this.repaint();
    }

    public void componentShown(ComponentEvent e) {
    }

    /**
	 * Begin the drawing of selection region (box).
	 * 
	 * @param e
	 */
    public void mousePressed(MouseEvent e) {
        if (dataIndices == null || dataIndices == null) {
            return;
        }
        if (dataIndices[0] == dataIndices[1]) {
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
    public void mouseReleased(MouseEvent e) {
        if (dataIndices == null || dataIndices[0] == dataIndices[1]) {
            return;
        }
        if (e.isPopupTrigger()) {
            maybeShowPopup(e);
        }
        mouseX2 = e.getX();
        mouseY2 = e.getY();
        if ((Math.abs(mouseX1 - mouseX2) < 3) && (Math.abs(mouseY1 - mouseY2) < 3)) {
            return;
        }
        if (e.isShiftDown() == false && e.isControlDown() == false) {
            for (int i = 0; i < selections.length; i++) {
                selections[i] = 0;
            }
        }
        if (mouseX1 <= mouseX2 && mouseY1 <= mouseY2) {
            selectX = mouseX1;
            selectY = mouseY1;
            selectWidth = mouseX2 - mouseX1;
            selectHeight = mouseY2 - mouseY1;
        }
        if (mouseX2 < mouseX1 && mouseY1 <= mouseY2) {
            selectX = mouseX2;
            selectY = mouseY1;
            selectWidth = mouseX1 - mouseX2;
            selectHeight = mouseY2 - mouseY1;
        }
        if (mouseX1 <= mouseX2 && mouseY2 < mouseY1) {
            selectX = mouseX1;
            selectY = mouseY2;
            selectWidth = mouseX2 - mouseX1;
            selectHeight = mouseY1 - mouseY2;
        }
        if (mouseX2 < mouseX1 && mouseY2 < mouseY1) {
            selectX = mouseX2;
            selectY = mouseY2;
            selectWidth = mouseX1 - mouseX2;
            selectHeight = mouseY1 - mouseY2;
        }
        Rectangle rec = new Rectangle(selectX, selectY, selectWidth, selectHeight);
        pointSelected = false;
        for (int i = 0; i < dataX.length; i++) {
            if (rec.contains(exsint[i], whyint[i]) && (conditionArray[i] > -1)) {
                if (e.isControlDown()) {
                    if (selections[i] >= 1) {
                        selections[i] = 0;
                    } else {
                        selections[i] = 1;
                        pointSelected = true;
                    }
                } else {
                    selections[i] = 1;
                    pointSelected = true;
                }
            }
        }
        int hitCount = 0;
        for (int element : selections) {
            if (element == 1) {
                hitCount++;
            }
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.info("hit count = " + hitCount + ", pointSelected = " + pointSelected);
        }
        if (hitCount != 0) {
            selectedDataX = new double[hitCount];
            selectedDataY = new double[hitCount];
            int selIndex = 0;
            for (int i = 0; i < dataX.length; i++) {
                if (selections[i] == 1) {
                    selectedDataX[selIndex] = dataX[i];
                    selectedDataY[selIndex] = dataY[i];
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.finest(i + " " + selectedDataX[selIndex]);
                    }
                    selIndex++;
                }
            }
            setVisiblePlotLine(selectedDataX, selectedDataY, false);
            setUpRegressionLine(slopeForSelections, interceptForSelections, false);
        } else {
            for (int i = 0; i < selections.length; i++) {
                selections[i] = 1;
            }
            hitCount = selections.length;
            setVisiblePlotLine(dataX, dataY, true);
        }
        selectWidth = 0;
        selectHeight = 0;
        multipleSelectionColors = null;
        paintDrawingBuff();
        repaint();
        fireActionPerformed(COMMAND_POINT_SELECTED);
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
    public void mouseExited(MouseEvent e) {
        setIndication(-1);
        fireIndicationChanged(-1);
    }

    /**
	 * Work with mouseReleased to draw a selection region (box) for selection.
	 * 
	 * @param e
	 */
    public void mouseDragged(MouseEvent e) {
        if (dataIndices == null || dataIndices[0] == dataIndices[1]) {
            return;
        }
        mouseX2 = e.getX();
        mouseY2 = e.getY();
        if ((Math.abs(mouseX1 - mouseX2) < 3) && (Math.abs(mouseY1 - mouseY2) < 3)) {
            return;
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("mouse released: " + "mouseX2" + mouseX2 + "mouseY2" + mouseY2);
        }
        if (mouseX1 <= mouseX2 && mouseY1 <= mouseY2) {
            selectX = mouseX1;
            selectY = mouseY1;
            selectWidth = mouseX2 - mouseX1;
            selectHeight = mouseY2 - mouseY1;
        }
        if (mouseX2 < mouseX1 && mouseY1 <= mouseY2) {
            selectX = mouseX2;
            selectY = mouseY1;
            selectWidth = mouseX1 - mouseX2;
            selectHeight = mouseY2 - mouseY1;
        }
        if (mouseX1 <= mouseX2 && mouseY2 < mouseY1) {
            selectX = mouseX1;
            selectY = mouseY2;
            selectWidth = mouseX2 - mouseX1;
            selectHeight = mouseY1 - mouseY2;
        }
        if (mouseX2 < mouseX1 && mouseY2 < mouseY1) {
            selectX = mouseX2;
            selectY = mouseY2;
            selectWidth = mouseX1 - mouseX2;
            selectHeight = mouseY1 - mouseY2;
        }
        repaint();
    }

    /**
	 * Mouse over, it will show the values for current point by tool tip.
	 * 
	 * @param e
	 */
    public void mouseMoved(MouseEvent e) {
        if (dataIndices == null) {
            return;
        }
        if (dataIndices[0] == dataIndices[1]) {
            histogram.mouseMoved(e);
            return;
        }
        if (e != null && axisOn == false) {
            makeToolTip(e.getX(), e.getY());
            e.consume();
        }
        int id = findCoveredDataId(e.getX(), e.getY());
        setIndication(id);
        this.repaint();
        fireIndicationChanged(id);
    }

    /**
	 * Add by Jin Chen. Given mouse position, find the id of the data covered by
	 * mouse
	 * 
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
    protected int findCoveredDataId(int x, int y) {
        int arrayIndex = -1;
        if (dataIndices == null) {
            return -1;
        }
        if (exsint == null) {
            return -1;
        }
        int tolerance = 3;
        if (pointSize > tolerance) {
            tolerance = pointSize;
        }
        if ((dataIndices[0] != dataIndices[1]) && (exsint != null) && (whyint != null)) {
            for (int i = 0; i < dataX.length; i++) {
                if ((exsint[i] - tolerance < x) && (x <= exsint[i] + tolerance) && (whyint[i] - tolerance < y) && (y <= whyint[i] + tolerance) && (conditionArray[i] > -1)) {
                    arrayIndex = i;
                    return arrayIndex;
                }
            }
        }
        return arrayIndex;
    }

    protected void selectAPoint(int[] mousePos) {
        if (dataIndices[0] != dataIndices[1]) {
            for (int i = 0; i < dataX.length; i++) {
                if ((exsint[i] - 5 < mousePos[0]) && (mousePos[0] < exsint[i] + 5) && (whyint[i] - 5 < mousePos[1]) && (mousePos[1] < whyint[i] + 5) && (conditionArray[i] > -1)) {
                    selections[i] = 1;
                }
            }
            fireActionPerformed(COMMAND_POINT_SELECTED);
        }
    }

    protected void popupADetailedPlot(String className) {
        if (dataIndices[0] != dataIndices[1]) {
            ScatterPlotBasic detailSP;
            Class detailPlot;
            try {
                detailPlot = (className != null) ? Class.forName(className) : null;
                detailSP = (ScatterPlotBasic) detailPlot.newInstance();
                detailSP.setDataSet(dataSet);
                detailSP.setAxisOn(true);
                detailSP.setDataIndices(dataIndices);
                dlgSP = new JFrame("Detailed Scatter Plot");
                dlgSP.setLocation(300, 300);
                dlgSP.setSize(300, 300);
                dlgSP.getContentPane().setLayout(new BorderLayout());
                dlgSP.getContentPane().add(detailSP, BorderLayout.CENTER);
                detailSP.setBackground(background);
                detailSP.setBivarColorClasser(bivarColorClasser, false);
                detailSP.setSelectionColor(selectionColor);
                detailSP.setSelOriginalColorMode(selOriginalColorMode);
                detailSP.setSelections(selections);
                detailSP.setXAxisExtents(xAxisExtents);
                detailSP.setYAxisExtents(yAxisExtents);
                detailSP.addActionListener(new ActionListener() {

                    /**
					 * put your documentation comment here
					 * 
					 * @param e
					 */
                    public void actionPerformed(ActionEvent e) {
                        if (logger.isLoggable(Level.FINEST)) {
                            logger.finest("something came from detailed one.");
                        }
                        ScatterPlotBasic detailSP = (ScatterPlotBasic) e.getSource();
                        String command = e.getActionCommand();
                        if (command.compareTo(ScatterPlotBasic.COMMAND_POINT_SELECTED) == 0) {
                            logger.finest("SPMC.plotUnitPanel.actionPerformed(), point selected");
                            int[] selections = detailSP.getSelections();
                            ScatterPlotBasic.this.setSelections(selections);
                            ScatterPlotBasic.this.fireActionPerformed(COMMAND_POINT_SELECTED);
                        } else if (command.compareTo(ScatterPlotBasic.COMMAND_DATARANGE_SET) == 0) {
                            double[] dataArrayX = detailSP.getXAxisExtents();
                            double[] dataArrayY = detailSP.getYAxisExtents();
                            ScatterPlotBasic.this.setXAxisExtents(dataArrayX);
                            ScatterPlotBasic.this.setYAxisExtents(dataArrayY);
                            fireActionPerformed(COMMAND_DATARANGE_SET);
                        }
                    }
                });
                dlgSP.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Histogram histogram = new Histogram();
            histogram.setVariableName(attributeXName);
            histogram.setData(dataX);
            histogram.setBackground(background);
            JFrame dummyFrame = new JFrame();
            JDialog dlgSP = new JDialog(dummyFrame, "Histogram", true);
            dlgSP.setLocation(300, 300);
            dlgSP.setSize(300, 300);
            dlgSP.getContentPane().setLayout(new BorderLayout());
            dlgSP.getContentPane().add(histogram, BorderLayout.CENTER);
            histogram.addActionListener(new ActionListener() {

                /**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
                public void actionPerformed(ActionEvent e) {
                    Histogram histogram = (Histogram) e.getSource();
                    BitSet bSet = histogram.getSelections();
                    int[] selObs = new int[bSet.size()];
                    for (int i = 0; i < bSet.size(); i++) {
                        if (bSet.get(i)) {
                            selObs[i] = 1;
                        } else {
                            selObs[i] = -1;
                        }
                    }
                    ScatterPlotBasic.this.setSelections(selObs);
                    ScatterPlotBasic.this.fireActionPerformed(COMMAND_POINT_SELECTED);
                }
            });
            dlgSP.setVisible(true);
        }
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
    public void mouseEntered(MouseEvent e) {
    }

    /**
	 * Mouse click for selecting or brushing points (observations).
	 * 
	 * @param e
	 */
    public void mouseClicked(MouseEvent e) {
        int count = e.getClickCount();
        int[] mousePos = new int[2];
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
        if (dataIndices == null) {
            return;
        }
        if (count == 1) {
            selectAPoint(mousePos);
        }
        if (count == 2) {
            String className = this.getClass().getName();
            popupADetailedPlot(className);
        }
    }

    protected void drawSelectRectangle(Graphics2D g2d, Rectangle rec) {
        Stroke tempStroke = g2d.getStroke();
        float[] dash = new float[3];
        dash[0] = (float) 5.0;
        dash[1] = (float) 7.0;
        dash[2] = (float) 5.0;
        BasicStroke dashStroke = new BasicStroke((float) 1.0, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, (float) 10.0, dash, 0);
        g2d.setStroke(dashStroke);
        g2d.setPaintMode();
        g2d.setColor(foreground);
        g2d.drawRect(selectX, selectY, selectWidth, selectHeight);
        g2d.drawRect(selectX, selectY, selectWidth, selectHeight);
        g2d.setStroke(tempStroke);
    }

    protected void makeToolTip(int x, int y) {
        int arrayIndex = -1;
        if (dataIndices == null) {
            return;
        }
        if (exsint == null) {
            return;
        }
        if ((dataIndices[0] != dataIndices[1]) && (exsint != null) && (whyint != null)) {
            for (int i = 0; i < dataX.length; i++) {
                if ((exsint[i] - 3 < x) && (x < exsint[i] + 3) && (whyint[i] - 3 < y) && (y < whyint[i] + 3) && (conditionArray[i] > -1)) {
                    arrayIndex = i;
                }
            }
        }
        if (arrayIndex >= 0) {
            String xVal = Double.toString(dataX[arrayIndex]);
            String yVal = Double.toString(dataY[arrayIndex]);
            String s = "<html> ";
            if (observNames != null) {
                s = s + "Name = " + observNames[arrayIndex] + "<br>";
            }
            s = s + attributeXName + " = " + xVal + "<br>" + attributeYName + " = " + yVal + "</html>";
            setToolTipText(s);
        }
    }

    JFrame dummyFrame1;

    JDialog dialog;

    /**
	 * New data ranges setup dialog.
	 * 
	 * @param x
	 * @param y
	 */
    protected void showDialog(int x, int y) {
        dummyFrame1 = new JFrame();
        dialog = new JDialog(dummyFrame1, "Data Range Configuer", true);
        JButton actionButton;
        JButton resetButton;
        dialog.setLocation(x, y);
        dialog.setSize(300, 150);
        dialog.getContentPane().setLayout(new GridLayout(5, 2));
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
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        dialog.getContentPane().add(resetButton);
        dialog.getContentPane().add(actionButton);
        xAxisMinField.setText(Double.toString(xAxisExtents[0]));
        xAxisMaxField.setText(Double.toString(xAxisExtents[1]));
        yAxisMinField.setText(Double.toString(yAxisExtents[0]));
        yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
        dialog.setVisible(true);
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
    protected void maybeShowPopup(MouseEvent e) {
        {
        }
    }

    /**
	 * Set up new data ranges to show.
	 * 
	 * @param e
	 */
    protected void actionButton_actionPerformed(ActionEvent e) {
        xAxisExtents[0] = Double.parseDouble(xAxisMinField.getText());
        xAxisExtents[1] = Double.parseDouble(xAxisMaxField.getText());
        yAxisExtents[0] = Double.parseDouble(yAxisMinField.getText());
        yAxisExtents[1] = Double.parseDouble(yAxisMaxField.getText());
        dataArrayX.setExtent(xAxisExtents);
        dataArrayY.setExtent(yAxisExtents);
        setupDataforDisplay();
        fireActionPerformed(COMMAND_DATARANGE_SET);
        paintDrawingBuff();
        repaint();
        dialog.setVisible(false);
    }

    /**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
    protected void resetButton_actionPerformed(ActionEvent e) {
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
        paintDrawingBuff();
        repaint();
    }

    /**
	 * adds an IndicationListener to the button
	 */
    public void addIndicationListener(IndicationListener l) {
        listenerList.add(IndicationListener.class, l);
    }

    /**
	 * removes an IndicationListener from the button
	 */
    public void removeIndicationListener(IndicationListener l) {
        listenerList.remove(IndicationListener.class, l);
    }

    /**
	 * adds an ActionListener to the button
	 */
    public void addActionListener(ActionListener l) {
        listenerListAction.add(ActionListener.class, l);
    }

    /**
	 * removes an ActionListener from the button
	 */
    public void removeActionListener(ActionListener l) {
        listenerListAction.remove(ActionListener.class, l);
    }

    /**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
    public void fireActionPerformed(String command) {
        Object[] listeners = listenerListAction.getListenerList();
        ActionEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                }
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    /**
	 * If AxisOn is true, it will be a detailed version of scatterplot with
	 * axises.
	 * 
	 * @return
	 */
    public boolean isAxisOn() {
        return axisOn;
    }

    /**
	 * Sets colors for the current data.
	 */
    public void setBivarColorClasser(BivariateColorClassifier bivarColorClasser, boolean reverseColor) {
        this.bivarColorClasser = bivarColorClasser;
        makeColors();
        paintDrawingBuff();
        this.repaint();
    }

    public BivariateColorClassifier getBivarColorClasser() {
        return bivarColorClasser;
    }

    /***************************************************************************
	 * add by jin chen for indication
	 **************************************************************************/
    public void indicationChanged(IndicationEvent e) {
        int id = e.getIndication();
        setIndication(id);
    }

    /**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see javax.swing.event.EventListenerList
	 */
    public void fireIndicationChanged(int indicator) {
        Object[] listeners = listenerList.getListenerList();
        IndicationEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == IndicationListener.class) {
                if (e == null) {
                    e = new IndicationEvent(this, indicator);
                }
                ((IndicationListener) listeners[i + 1]).indicationChanged(e);
            }
        }
    }

    /***************************************************************************
	 * * added by ywkim for regression line *
	 **************************************************************************/
    protected Class regressionClass;

    protected String regressionClassName = LinearRegression.class.getName();

    protected Regression regressionInterface;

    public void setRegressionClass(Object obj) {
        setRegressionClassName((obj != null) ? obj.getClass().getName() : null);
    }

    public void setRegressionClassName(String classname) {
        regressionClassName = classname;
    }

    public void setRegressionClass(Class clazz) {
        regressionClass = clazz;
    }

    public void setPlotLine(boolean plotLine) {
        this.plotLine = plotLine;
    }

    public void setVisiblePlotLine(double[] dataX, double[] dataY, boolean allData) {
        try {
            setRegressionClass((regressionClassName != null) ? Class.forName(regressionClassName) : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (regressionClass != null) {
                plotLine = true;
                regressionInterface = (Regression) regressionClass.newInstance();
            }
            if (regressionClass == null) {
                plotLine = false;
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        double[] moransRegression = new double[2];
        if (dataX != null && dataY != null) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("in regression");
            }
            moransRegression = regressionInterface.getRegression(dataX, dataY);
        }
        if (allData == true) {
            slope = moransRegression[0];
            intercept = moransRegression[1];
            rSquare = regressionInterface.getRSquare(dataX, dataY);
            correlation = DescriptiveStatistics.correlationCoefficient(dataX, dataY, false);
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("scatterPlotBasic all: " + slope + "," + intercept);
            }
        } else {
            slopeForSelections = moransRegression[0];
            interceptForSelections = moransRegression[1];
            correlationForSelections = DescriptiveStatistics.correlationCoefficient(dataX, dataY, true);
            rSquareForSelections = regressionInterface.getRSquare(dataX, dataY);
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("selected:" + slopeForSelections + "," + interceptForSelections);
            }
        }
    }

    protected void setUpRegressionLine(double slope, double intercept, boolean allData) {
        if (dataX == null) {
            return;
        }
        double xStart, xEnd;
        xStart = xAxisExtents[0];
        xEnd = xAxisExtents[1];
        double yEstimateStart, yEstimateEnd;
        yEstimateStart = (xStart * slope) + intercept;
        yEstimateEnd = (xEnd * slope) + intercept;
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("drawPlot: " + xStart + " " + xEnd + "," + yEstimateStart + " " + yEstimateEnd);
        }
        int yEstStartInt, yEstEndInt;
        yEstStartInt = this.getValueScreen(yEstimateStart, getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]), plotOriginY, yAxisExtents[0]);
        yEstEndInt = this.getValueScreen(yEstimateEnd, getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]), plotOriginY, yAxisExtents[0]);
        if (allData == true) {
            yStartPosition = yEstStartInt - plotOriginY;
            yEndPosition = yEstEndInt - plotOriginY;
        } else {
            yStartPositionSelections = yEstStartInt - plotOriginY;
            yEndPositionSelections = yEstEndInt - plotOriginY;
        }
    }

    protected void drawPlotLine(Graphics g, double yStartPosition, double yEndPosition) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("drawPlot: " + yStartPosition + yEndPosition);
        }
        if (plotLine == false) {
            return;
        }
        int xRegStart, yRegStart, xRegEnd, yRegEnd;
        xRegStart = plotOriginX;
        yRegStart = plotOriginY + (int) (yStartPosition);
        xRegEnd = plotEndX;
        yRegEnd = plotOriginY + (int) (yEndPosition);
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("drawPlot: " + xRegStart + " " + xRegEnd + "," + yRegStart + " " + yRegEnd);
        }
        g.drawLine(xRegStart, yRegStart, xRegEnd, yRegEnd);
    }

    protected void drawCorrelationValue(Graphics g, double correlation, double rSquare) {
        int plotWidth, plotHeight;
        plotWidth = getWidth();
        plotHeight = getHeight();
        int fontSize;
        fontSize = (plotWidth < plotHeight) ? plotWidth : plotHeight;
        fontSize = ((fontSize / 32) < 9) ? 9 : fontSize / 25;
        Font font = new Font("", Font.PLAIN, fontSize);
        g.setFont(font);
        g.setColor(Color.blue);
        String slopeTitle, rSquareTitle;
        if ((Double.toString(correlation)).length() > 7) {
            slopeTitle = "Cor: " + (Double.toString(correlation)).substring(0, 6);
        } else {
            slopeTitle = "Cor: " + Double.toString(correlation);
        }
        slopeTitle = "";
        if ((Double.toString(rSquare)).length() > 7) {
            rSquareTitle = "rSquare: " + (Double.toString(rSquare)).substring(0, 6);
        } else {
            rSquareTitle = "rSquare: " + Double.toString(rSquare);
        }
        g.drawString(rSquareTitle, (plotWidth / 2), (plotHeight / 15));
        g.drawString(slopeTitle, 2, (plotHeight / 15));
    }

    public JPopupMenu getPopup() {
        if (popup == null) {
            JPopupMenu popup = null;
            popup = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("Set Range");
            popup.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                /**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
                public void actionPerformed(ActionEvent e) {
                    showDialog(400, 400);
                }
            });
            menuItem = new JMenuItem("Edit");
            popup.add(menuItem);
            menuItem = new JMenuItem("Regression");
            popup.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                /**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
                public void actionPerformed(ActionEvent e) {
                    plotLine = !plotLine;
                    ScatterPlotBasic.this.paintDrawingBuff();
                    repaint();
                }
            });
            menuItem = new JMenuItem("Regression For Selection");
            popup.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                /**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
                public void actionPerformed(ActionEvent e) {
                    plotLineForSelections = !plotLineForSelections;
                    ScatterPlotBasic.this.paintDrawingBuff();
                    repaint();
                }
            });
            this.popup = popup;
        }
        return popup;
    }

    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    public Color getIndicationColor() {
        return null;
    }

    public boolean isSelectionBlur() {
        return useSelectionBlur;
    }

    public boolean isSelectionFade() {
        return useSelectionFade;
    }

    public void setIndicationColor(Color indColor) {
    }

    public void useMultiIndication(boolean useMultiIndic) {
    }

    public void useSelectionBlur(boolean selBlur) {
        if (useSelectionBlur != selBlur) {
            useSelectionBlur = selBlur;
            paintDrawingBuff();
            this.repaint();
        }
    }

    public void useSelectionFade(boolean selFade) {
        if (useSelectionFade != selFade) {
            useSelectionFade = selFade;
            paintDrawingBuff();
            this.repaint();
        }
    }

    public void makeColors() {
        if (dataX != null && dataY != null) {
            pointColors = bivarColorClasser.symbolize(dataX, dataY);
        }
    }

    public void processCustomCheckBox(boolean value, String text) {
    }

    private boolean selectionOutline;

    private int selectionLineWidth = 5;

    private BasicStroke selectionStroke = new BasicStroke(5);

    public boolean isSelectionOutline() {
        return selectionOutline;
    }

    public void useSelectionOutline(boolean selOutline) {
        if (selectionOutline != selOutline) {
            selectionOutline = selOutline;
            paintDrawingBuff();
            this.repaint();
        }
    }

    public int getSelectionLineWidth() {
        return selectionLineWidth;
    }

    public void setSelectionLineWidth(int width) {
        if (selectionLineWidth != width) {
            selectionLineWidth = width;
            selectionStroke = new BasicStroke(width);
            paintDrawingBuff();
            this.repaint();
        }
    }
}
