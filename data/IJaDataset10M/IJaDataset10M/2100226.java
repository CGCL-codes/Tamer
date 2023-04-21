package uk.ac.imperial.ma.metric.explorations.calculus.integration;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import java.awt.Color;
import uk.ac.imperial.ma.metric.plotting.*;
import uk.ac.imperial.ma.metric.parsing.*;
import uk.ac.imperial.ma.metric.util.MathsStyleHelper;
import uk.ac.imperial.ma.metric.gui.*;
import uk.ac.imperial.ma.metric.explorations.ExplorationInterface;

/**
 *
 *
 * @author Phil Ramsden
 * @author Daniel J. R. May
 * @version 0.2.0 26 March 2004
 */
public class NewNumericalIntegrationExploration extends JPanel implements ExplorationInterface, ActionListener, KeyListener, CaretListener, ItemListener, MathPainterPanelListener, ChangeListener {

    private static final ExtendedGridBagLayout mainPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout contentSettingsPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout windowSettingsPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout controlPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout stylePanelLayout = new ExtendedGridBagLayout();

    ClickableMathPainterPanel graphicsPanel;

    MathPainter mathPainter;

    GridPlotter gridPlotter;

    AxesPlotter axesPlotter;

    CoordGenerator functionCoordGenerator;

    CurvePlotter functionCurvePlotter;

    CoordGenerator simpsonsCoordGenerator;

    CurvePlotter simpsonsCurvePlotter;

    RegionPlotter simpsonsRegionPlotter;

    JPanel contentSettingsPanel = new JPanel(true);

    JPanel windowSettingsPanel = new JPanel(true);

    JPanel controlPanel = new JPanel(true);

    JFrame styleFrame = new JFrame();

    JPanel stylePanel = new JPanel(true);

    Dimension mainImageDimension;

    Dimension derivativeImageDimension;

    private static final int TEXT_SIZE = 16;

    private static final int MATH_SIZE = 18;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, TEXT_SIZE - 2);

    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, TEXT_SIZE);

    private static final short MIDPOINT_RULE = 0;

    private static final short TRAPEZIUM_RULE = 1;

    private static final short SIMPSONS_RULE = 2;

    protected int currentTextSize;

    protected int currentMathSize;

    protected Font currentLabelFont;

    protected Font currentFieldFont;

    private short estimateOption = MIDPOINT_RULE;

    private int nIntervals = 10;

    private static final String[] estimateOptionComBxList = { "midpoint rule", "trapezium rule", "Simpson's rule" };

    String[] parseVariables = { "x" };

    Color[] defaultColorWheel = { Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN };

    int defaultColorWheelSize = 7;

    int defaultColorIndex = 0;

    int plotIndex = 1;

    JLabel functionLbl = new JLabel("f(x) = ");

    JTextField functionTFd = new JTextField("sin(x)");

    JEditorPane functionJEP;

    JCheckBox inheritRangesCBx = new JCheckBox("inherit ranges", true);

    JLabel xMinLbl = new JLabel("plot xMin");

    JTextField xMinTFd = new JTextField("-1.0        ");

    JLabel xMaxLbl = new JLabel("plot xMax");

    JTextField xMaxTFd = new JTextField("4.0        ");

    JLabel intXMinLbl = new JLabel("int xMin");

    JTextField intXMinTFd = new JTextField("0.0        ");

    JLabel intXMaxLbl = new JLabel("int xMax");

    JTextField intXMaxTFd = new JTextField("3.1415927    ");

    JLabel xNMeshLbl = new JLabel("number of points");

    JTextField xNMeshTFd = new JTextField("50");

    JComboBox estimateOptionComBx = new JComboBox(estimateOptionComBxList);

    JButton localStylesBtn = new JButton("Plot Styles...");

    JLabel resultLbl = new JLabel("result");

    JTextField resultTFd = new JTextField("");

    JFrame plotStyleFrame;

    JPanel plotStylePanel;

    JLabel plotLabelLbl = new JLabel("Plot label");

    JTextField plotLabelTFd = new JTextField("Plot");

    SelectColorButton functionColorBtn = new SelectColorButton(Color.BLACK, "Colour 2...");

    String plotLabel;

    Color plotColor = Color.BLACK;

    boolean settingsAltered;

    boolean inheritRanges = true;

    String functionString;

    double xMin;

    double xMax;

    int xNMesh;

    double intXMin;

    double intXMax;

    JLabel windowXMinLbl = new JLabel("xMin");

    JTextField windowXMinTFd = new JTextField("-1.0        ");

    JLabel windowXMaxLbl = new JLabel("xMax");

    JTextField windowXMaxTFd = new JTextField("4.0        ");

    JLabel windowYMinLbl = new JLabel("yMin");

    JTextField windowYMinTFd = new JTextField("-0.3        ");

    JLabel windowYMaxLbl = new JLabel("yMax");

    JTextField windowYMaxTFd = new JTextField("1.3        ");

    JButton globalStylesBtn = new JButton("Global Styles...");

    private double windowXMin;

    private double windowXMax;

    private double windowYMin;

    private double windowYMax;

    JSlider nIntervalsSld = new JSlider(2, 50, 10);

    JTextField nIntervalsTFd = new JTextField("10");

    JButton autoscaleBtn = new JButton("Scale");

    JButton textPlusBtn = new JButton("   Text+   ");

    JButton textMinusBtn = new JButton("   Text-   ");

    public NewNumericalIntegrationExploration() {
        super(true);
        currentTextSize = TEXT_SIZE;
        currentMathSize = MATH_SIZE;
        currentLabelFont = LABEL_FONT;
        currentFieldFont = FIELD_FONT;
        graphicsPanel = new ClickableMathPainterPanel();
        graphicsPanel.addMathPainterPanelListener(this);
        contentSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Content Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        contentSettingsPanel.setLayout(contentSettingsPanelLayout);
        try {
            this.functionJEP = new JEditorPane("text/html", MathsStyleHelper.getStyledHTML(TreeFormatter.format2D("sin(x)"), TEXT_SIZE, MATH_SIZE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        plotLabelLbl.setFont(LABEL_FONT);
        plotLabelTFd.setFont(FIELD_FONT);
        functionColorBtn.setButtonFont(LABEL_FONT);
        functionLbl.setFont(LABEL_FONT);
        inheritRangesCBx.setFont(LABEL_FONT);
        xMinLbl.setFont(LABEL_FONT);
        xMaxLbl.setFont(LABEL_FONT);
        intXMinLbl.setFont(LABEL_FONT);
        intXMaxLbl.setFont(LABEL_FONT);
        xNMeshLbl.setFont(LABEL_FONT);
        estimateOptionComBx.setFont(LABEL_FONT);
        localStylesBtn.setFont(LABEL_FONT);
        resultLbl.setFont(LABEL_FONT);
        functionTFd.setFont(FIELD_FONT);
        xMinTFd.setFont(FIELD_FONT);
        xMaxTFd.setFont(FIELD_FONT);
        intXMinTFd.setFont(FIELD_FONT);
        intXMaxTFd.setFont(FIELD_FONT);
        xNMeshTFd.setFont(FIELD_FONT);
        resultTFd.setFont(FIELD_FONT);
        functionTFd.addCaretListener(this);
        functionTFd.addKeyListener(this);
        xMinTFd.addKeyListener(this);
        xMaxTFd.addKeyListener(this);
        intXMinTFd.addKeyListener(this);
        intXMaxTFd.addKeyListener(this);
        xNMeshTFd.addKeyListener(this);
        plotLabelTFd.addKeyListener(this);
        estimateOptionComBx.addItemListener(this);
        inheritRangesCBx.addItemListener(this);
        localStylesBtn.addActionListener(this);
        xMinTFd.setEditable(false);
        xMaxTFd.setEditable(false);
        resultTFd.setEditable(false);
        contentSettingsPanelLayout.add(functionLbl, contentSettingsPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(functionTFd, contentSettingsPanel, 1, 0, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(functionJEP, contentSettingsPanel, 1, 1, 3, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(inheritRangesCBx, contentSettingsPanel, 1, 2, 3, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(xMinLbl, contentSettingsPanel, 0, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(xMinTFd, contentSettingsPanel, 1, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(xMaxLbl, contentSettingsPanel, 2, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(xMaxTFd, contentSettingsPanel, 3, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(xNMeshLbl, contentSettingsPanel, 0, 4, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(xNMeshTFd, contentSettingsPanel, 1, 4, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(intXMinLbl, contentSettingsPanel, 0, 5, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(intXMinTFd, contentSettingsPanel, 1, 5, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(intXMaxLbl, contentSettingsPanel, 2, 5, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(intXMaxTFd, contentSettingsPanel, 3, 5, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(resultLbl, contentSettingsPanel, 0, 7, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(resultTFd, contentSettingsPanel, 1, 7, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(localStylesBtn, contentSettingsPanel, 1, 6, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Visual Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        windowSettingsPanel.setLayout(windowSettingsPanelLayout);
        windowXMinLbl.setFont(LABEL_FONT);
        windowXMaxLbl.setFont(LABEL_FONT);
        windowYMinLbl.setFont(LABEL_FONT);
        windowYMaxLbl.setFont(LABEL_FONT);
        globalStylesBtn.setFont(LABEL_FONT);
        windowXMinTFd.setFont(FIELD_FONT);
        windowXMaxTFd.setFont(FIELD_FONT);
        windowYMinTFd.setFont(FIELD_FONT);
        windowYMaxTFd.setFont(FIELD_FONT);
        windowSettingsPanelLayout.add(windowXMinLbl, windowSettingsPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowXMinTFd, windowSettingsPanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowXMaxLbl, windowSettingsPanel, 2, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowXMaxTFd, windowSettingsPanel, 3, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowYMinLbl, windowSettingsPanel, 0, 1, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowYMinTFd, windowSettingsPanel, 1, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowYMaxLbl, windowSettingsPanel, 2, 1, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowYMaxTFd, windowSettingsPanel, 3, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(globalStylesBtn, windowSettingsPanel, 1, 2, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowXMinTFd.addKeyListener(this);
        windowXMaxTFd.addKeyListener(this);
        windowYMinTFd.addKeyListener(this);
        windowYMaxTFd.addKeyListener(this);
        globalStylesBtn.addActionListener(this);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        controlPanel.setLayout(controlPanelLayout);
        nIntervalsTFd.setFont(FIELD_FONT);
        autoscaleBtn.setFont(LABEL_FONT);
        controlPanelLayout.add(estimateOptionComBx, controlPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        controlPanelLayout.add(nIntervalsSld, controlPanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(nIntervalsTFd, controlPanel, 2, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(autoscaleBtn, controlPanel, 3, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        nIntervalsSld.addChangeListener(this);
        autoscaleBtn.addActionListener(this);
        graphicsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Graphics Area", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        this.setLayout(mainPanelLayout);
        mainPanelLayout.add(contentSettingsPanel, this, 0, 0, 1, 1, 10, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(windowSettingsPanel, this, 0, 1, 1, 1, 10, 10, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(graphicsPanel, this, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(controlPanel, this, 1, 1, 1, 1, 100, 10, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        textPlusBtn.setFont(LABEL_FONT);
        textMinusBtn.setFont(LABEL_FONT);
        textPlusBtn.addActionListener(this);
        textMinusBtn.addActionListener(this);
    }

    public void mathPainterPanelResized() {
        if (graphicsPanel == null) {
            graphicsPanel.revalidate();
        }
        this.graphicsPanel.clearCompletely();
        initializeGraphics();
        setupGraphics();
        drawGraphPaper();
        draw();
        drawApproximations();
    }

    public void setupGraphics() {
        this.functionCoordGenerator = new CoordGenerator(this.mathPainter);
        this.functionCurvePlotter = new CurvePlotter(this.mathPainter, this.functionCoordGenerator);
        this.simpsonsCoordGenerator = new CoordGenerator(this.mathPainter);
        this.simpsonsCurvePlotter = new CurvePlotter(this.mathPainter, this.simpsonsCoordGenerator);
        this.simpsonsRegionPlotter = new RegionPlotter(this.mathPainter, this.simpsonsCoordGenerator);
    }

    public void plotFunction() {
        Color functionColor = functionColorBtn.getColor();
        mathPainter.setPaint(functionColor);
        try {
            functionCoordGenerator.setPoints(functionTFd.getText(), "x", xMin, xMax - xMin, xNMesh);
            functionCurvePlotter.plot();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void plotAll() {
        System.out.println("plotAll called");
        if (this.inheritRanges) {
            this.xMinTFd.setText(windowXMinTFd.getText());
            this.xMaxTFd.setText(windowXMaxTFd.getText());
        }
        xMin = 1.000001 * (new Double(this.xMinTFd.getText())).doubleValue();
        xMax = (new Double(this.xMaxTFd.getText())).doubleValue();
        intXMin = (new Double(this.intXMinTFd.getText())).doubleValue();
        intXMax = (new Double(this.intXMaxTFd.getText())).doubleValue();
        xNMesh = (new Integer(this.xNMeshTFd.getText())).intValue();
        plotFunction();
        settingsAltered = false;
    }

    public void init() {
        System.out.println("init called");
        mathPainter = graphicsPanel.init();
        System.out.println("mathPainter " + mathPainter);
        initializeGraphics();
        setupGraphics();
        drawGraphPaper();
        plotAll();
        this.graphicsPanel.setBase();
        this.graphicsPanel.update();
        drawApproximations();
    }

    public void initializeGraphics() {
        System.out.println("initializeGraphics() called");
        this.gridPlotter = new GridPlotter(this.mathPainter);
        System.out.println("gridPlotter instantiated");
        this.axesPlotter = new AxesPlotter(this.mathPainter);
        System.out.println("axesPlotter instantiated");
        System.out.println("mathPainter " + mathPainter);
        windowXMin = new Double(windowXMinTFd.getText()).doubleValue();
        windowXMax = new Double(windowXMaxTFd.getText()).doubleValue();
        windowYMin = new Double(windowYMinTFd.getText()).doubleValue();
        windowYMax = new Double(windowYMaxTFd.getText()).doubleValue();
        mathPainter.setMathArea(windowXMin, windowYMin, windowXMax - windowXMin, windowYMax - windowYMin);
        mathPainter.setScales();
    }

    public void drawGraphPaper() {
        mathPainter.setPaint(Color.white);
        mathPainter.fillRect(windowXMin, windowYMin, windowXMax - windowXMin, windowYMax - windowYMin);
        mathPainter.setPaint(Color.lightGray);
        gridPlotter.drawFineGrid();
        mathPainter.setPaint(Color.gray);
        gridPlotter.drawGrid();
        mathPainter.setPaint(Color.blue);
        axesPlotter.drawAxes();
        axesPlotter.drawTicks();
    }

    public void draw() {
        graphicsPanel.clearCompletely();
        initializeGraphics();
        drawGraphPaper();
        plotAll();
        graphicsPanel.setBase();
        graphicsPanel.update();
    }

    public void drawApproximations() {
        try {
            graphicsPanel.clear();
            double h = (intXMax - intXMin) / nIntervals;
            double[] x = new double[21];
            double[] y = new double[21];
            double runningTotal = 0.0;
            if (estimateOption == MIDPOINT_RULE) {
                for (int i = 0; i < nIntervals; i++) {
                    x[0] = intXMin + i * h;
                    x[1] = x[0];
                    x[2] = x[0] + h;
                    x[3] = x[2];
                    y[0] = 0.0;
                    y[1] = functionCoordGenerator.func(x[0] + 0.5 * h);
                    y[2] = y[1];
                    y[3] = 0.0;
                    mathPainter.setPaint(new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), 150));
                    mathPainter.fillPolygon(x, y, 4);
                    mathPainter.setPaint(Color.BLUE);
                    mathPainter.drawPolygon(x, y, 4);
                    runningTotal += h * y[1];
                }
            } else if (estimateOption == TRAPEZIUM_RULE) {
                for (int i = 0; i < nIntervals; i++) {
                    x[0] = intXMin + i * h;
                    x[1] = x[0];
                    x[2] = x[0] + h;
                    x[3] = x[2];
                    y[0] = 0.0;
                    y[1] = functionCoordGenerator.func(x[1]);
                    y[2] = functionCoordGenerator.func(x[2]);
                    y[3] = 0.0;
                    mathPainter.setPaint(new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), 150));
                    mathPainter.fillPolygon(x, y, 4);
                    mathPainter.setPaint(Color.BLUE);
                    mathPainter.drawPolygon(x, y, 4);
                    runningTotal += 0.5 * (h * y[1] + h * y[2]);
                }
            } else if (estimateOption == SIMPSONS_RULE) {
                for (int i = 0; i < nIntervals; i += 2) {
                    x[0] = intXMin + i * h;
                    x[1] = x[0];
                    x[10] = intXMin + (i + 1) * h;
                    x[19] = intXMin + (i + 2) * h;
                    x[20] = x[19];
                    y[0] = 0.0;
                    y[1] = functionCoordGenerator.func(x[1]);
                    y[10] = functionCoordGenerator.func(x[10]);
                    y[19] = functionCoordGenerator.func(x[19]);
                    y[20] = 0.0;
                    double linearCoeff = (y[19] - y[1]) / (2 * h);
                    double quadraticCoeff = (y[19] - 2 * y[10] + y[1]) / (2 * h * h);
                    for (int j = 2; j < 10; j++) {
                        x[j] = x[j - 1] + h / 9;
                        y[j] = y[10] + linearCoeff * (x[j] - x[10]) + quadraticCoeff * (x[j] - x[10]) * (x[j] - x[10]);
                    }
                    for (int j = 11; j < 19; j++) {
                        x[j] = x[j - 1] + h / 9;
                        y[j] = y[10] + linearCoeff * (x[j] - x[10]) + quadraticCoeff * (x[j] - x[10]) * (x[j] - x[10]);
                    }
                    mathPainter.setPaint(new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), 150));
                    mathPainter.fillPolygon(x, y, 21);
                    mathPainter.setPaint(Color.BLUE);
                    mathPainter.drawPolygon(x, y, 21);
                    mathPainter.setPaint(Color.GREEN);
                    mathPainter.drawLine(x[10], 0.0, x[10], y[10]);
                    runningTotal += h * (y[1] + 4 * y[10] + y[19]) / 3.0;
                }
            }
            runningTotal = Math.round(1000000.0 * runningTotal) / 1000000.0;
            resultTFd.setText("" + runningTotal);
            graphicsPanel.update();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void update2DFormattingArea() {
        functionString = functionTFd.getText();
        try {
            functionJEP.setText(MathsStyleHelper.getStyledHTML(TreeFormatter.format2D(functionString), this.currentTextSize, this.currentMathSize));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setInheritRanges(boolean inheritRanges) {
        this.inheritRanges = inheritRanges;
        xMinTFd.setEditable(!inheritRanges);
        xMaxTFd.setEditable(!inheritRanges);
        if (inheritRanges) {
            xMinTFd.setText(this.windowXMinTFd.getText());
            xMaxTFd.setText(this.windowXMaxTFd.getText());
        }
    }

    public Component getComponent() {
        return this;
    }

    public void caretUpdate(CaretEvent ce) {
        if (ce.getSource() == functionTFd) {
            update2DFormattingArea();
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == inheritRangesCBx) {
            setInheritRanges(inheritRangesCBx.isSelected());
        } else if (ie.getSource() == estimateOptionComBx) {
            estimateOption = (short) (estimateOptionComBx.getSelectedIndex());
            nIntervals = nIntervalsSld.getValue();
            if (estimateOption == SIMPSONS_RULE) {
                nIntervals = 2 * (int) (Math.ceil((double) (nIntervals) / 2.0));
            }
            nIntervalsTFd.setText("" + nIntervals);
            drawApproximations();
        } else {
            resultTFd.setText("");
            this.draw();
            drawApproximations();
        }
    }

    public void stateChanged(ChangeEvent ce) {
        nIntervals = nIntervalsSld.getValue();
        if (estimateOption == SIMPSONS_RULE) {
            nIntervals = 2 * (int) (Math.ceil((double) (nIntervals) / 2.0));
        }
        nIntervalsTFd.setText("" + nIntervals);
        drawApproximations();
    }

    public void keyPressed(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            nIntervals = new Integer(nIntervalsTFd.getText()).intValue();
            draw();
            drawApproximations();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == localStylesBtn) {
            plotStyleFrame = new JFrame("Plot Style Settings");
            plotStylePanel = new JPanel();
            plotStylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(plotLabelLbl, plotStylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotLabelTFd, plotStylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(functionColorBtn, plotStylePanel, 0, 3, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            plotStyleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            plotStyleFrame.add(plotStylePanel);
            plotStyleFrame.setSize(300, 300);
            plotStyleFrame.setVisible(true);
        } else if (ae.getSource() == globalStylesBtn) {
            styleFrame = new JFrame("Global Style Settings");
            stylePanel = new JPanel();
            stylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(textPlusBtn, stylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(textMinusBtn, stylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            styleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            styleFrame.add(stylePanel);
            styleFrame.setSize(300, 300);
            styleFrame.setVisible(true);
        } else if (ae.getSource() == textPlusBtn) {
            currentTextSize++;
            currentMathSize++;
            updateFonts();
        } else if (ae.getSource() == textMinusBtn) {
            currentTextSize--;
            currentMathSize--;
            updateFonts();
        } else if (ae.getSource() == autoscaleBtn) {
            this.functionCoordGenerator.autoScale(MathCoords.Y_AXIS);
            double newYMin = mathPainter.getYMin();
            double newYMax = newYMin + mathPainter.getYRange();
            newYMin = Math.round(100000.0 * newYMin) / 100000.0;
            newYMax = Math.round(100000.0 * newYMax) / 100000.0;
            windowYMinTFd.setText("" + newYMin);
            windowYMaxTFd.setText("" + newYMax);
            resultTFd.setText("");
            draw();
            drawApproximations();
        }
    }

    public void updateFonts() {
        currentLabelFont = new Font("SansSerif", Font.BOLD, currentTextSize - 2);
        currentFieldFont = new Font("SansSerif", Font.PLAIN, currentTextSize);
        contentSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Content Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.currentLabelFont));
        update2DFormattingArea();
        functionLbl.setFont(this.currentLabelFont);
        inheritRangesCBx.setFont(this.currentLabelFont);
        xMinLbl.setFont(this.currentLabelFont);
        xMaxLbl.setFont(this.currentLabelFont);
        xNMeshLbl.setFont(this.currentLabelFont);
        intXMinLbl.setFont(this.currentLabelFont);
        intXMaxLbl.setFont(this.currentLabelFont);
        resultLbl.setFont(this.currentLabelFont);
        localStylesBtn.setFont(this.currentLabelFont);
        functionTFd.setFont(this.currentFieldFont);
        xMinTFd.setFont(this.currentFieldFont);
        xMaxTFd.setFont(this.currentFieldFont);
        xNMeshTFd.setFont(this.currentFieldFont);
        intXMinTFd.setFont(this.currentFieldFont);
        intXMaxTFd.setFont(this.currentFieldFont);
        resultTFd.setFont(this.currentFieldFont);
        plotLabelLbl.setFont(this.currentLabelFont);
        plotLabelTFd.setFont(this.currentFieldFont);
        functionColorBtn.setButtonFont(this.currentLabelFont);
        windowSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Visual Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
        windowXMinLbl.setFont(currentLabelFont);
        windowXMaxLbl.setFont(currentLabelFont);
        windowYMinLbl.setFont(currentLabelFont);
        windowYMaxLbl.setFont(currentLabelFont);
        globalStylesBtn.setFont(currentLabelFont);
        windowXMinTFd.setFont(currentFieldFont);
        windowXMaxTFd.setFont(currentFieldFont);
        windowYMinTFd.setFont(currentFieldFont);
        windowYMaxTFd.setFont(currentFieldFont);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
        nIntervalsTFd.setFont(currentFieldFont);
        autoscaleBtn.setFont(currentLabelFont);
        textPlusBtn.setFont(currentLabelFont);
        textMinusBtn.setFont(currentLabelFont);
        graphicsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Graphics Area", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
    }

    public void mathPainterPanelAction(MathPainterPanelEvent mppe) {
        MouseEvent me = mppe.getMouseEvent();
        int eventType = me.getID();
        if (eventType == MouseEvent.MOUSE_PRESSED) mousePressedAction(mppe); else if (eventType == MouseEvent.MOUSE_DRAGGED) mouseDraggedAction(mppe);
    }

    public void mousePressedAction(MathPainterPanelEvent mppe) {
    }

    public void mouseDraggedAction(MathPainterPanelEvent mppe) {
    }
}
