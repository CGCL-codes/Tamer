package uk.ac.imperial.ma.metric.tools.newRecurrenceRelationsPlottingTool;

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
public class NewRecurrenceRelationsPlottingTool extends JPanel implements ExplorationInterface, ActionListener, KeyListener, MathPainterPanelListener {

    private static final ExtendedGridBagLayout mainPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout windowSettingsPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout controlPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout stylePanelLayout = new ExtendedGridBagLayout();

    MathPainterPanel graphicsPanel;

    MathPainter mathPainter;

    GridPlotter gridPlotter;

    AxesPlotter axesPlotter;

    RecurrenceRelationsPlotCSP contentSettingsPanel = new RecurrenceRelationsPlotCSP(this, "Plot 1", Color.BLACK, Color.GRAY, TEXT_SIZE, MATH_SIZE, LABEL_FONT, FIELD_FONT, "-0.5", "20.5        ");

    JPanel windowSettingsPanel = new JPanel(true);

    JPanel controlPanel = new JPanel(true);

    JTabbedPane contentSettingsTabbedPane = new JTabbedPane();

    JFrame styleFrame = new JFrame();

    JPanel stylePanel = new JPanel(true);

    Dimension imageDimension;

    private static final int TEXT_SIZE = 16;

    private static final int MATH_SIZE = 18;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, TEXT_SIZE - 2);

    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, TEXT_SIZE);

    protected int currentTextSize;

    protected int currentMathSize;

    protected Font currentLabelFont;

    protected Font currentFieldFont;

    String[] parseVariables = { "a", "n" };

    Color[] defaultSequenceColorWheel = { Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN };

    Color[] defaultSeriesColorWheel = { Color.GRAY, Color.PINK, Color.BLUE.brighter(), Color.GREEN.brighter(), Color.ORANGE.brighter(), Color.MAGENTA.brighter(), Color.CYAN.brighter() };

    int defaultColorWheelSize = 7;

    int defaultColorIndex = 0;

    int plotIndex = 1;

    JLabel windowXMinLbl = new JLabel("xMin");

    JTextField windowXMinTFd = new JTextField("-1.5        ");

    JLabel windowXMaxLbl = new JLabel("xMax");

    JTextField windowXMaxTFd = new JTextField("10.5        ");

    JLabel windowYMinLbl = new JLabel("yMin");

    JTextField windowYMinTFd = new JTextField("-0.5       ");

    JLabel windowYMaxLbl = new JLabel("yMax");

    JTextField windowYMaxTFd = new JTextField("1.5        ");

    JButton globalStylesBtn = new JButton("Global Styles...");

    private double windowXMin;

    private double windowXMax;

    private double windowYMin;

    private double windowYMax;

    JButton eraseCurvesBtn = new JButton("Erase All");

    JButton drawCurvesBtn = new JButton("Draw All");

    ;

    JButton newPlotBtn = new JButton("New Plot");

    ;

    JButton deletePlotBtn = new JButton("Delete This");

    ;

    JButton autoscaleBtn = new JButton("Scale");

    ;

    JButton textPlusBtn = new JButton("   Text+   ");

    JButton textMinusBtn = new JButton("   Text-   ");

    public NewRecurrenceRelationsPlottingTool() {
        super(true);
        currentTextSize = TEXT_SIZE;
        currentMathSize = MATH_SIZE;
        currentLabelFont = LABEL_FONT;
        currentFieldFont = FIELD_FONT;
        graphicsPanel = new MathPainterPanel("Sequence Plots");
        graphicsPanel.addMathPainterPanelListener(this);
        contentSettingsTabbedPane.setFont(LABEL_FONT);
        contentSettingsTabbedPane.add("Plot 1", contentSettingsPanel);
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
        drawCurvesBtn.setFont(LABEL_FONT);
        eraseCurvesBtn.setFont(LABEL_FONT);
        newPlotBtn.setFont(LABEL_FONT);
        deletePlotBtn.setFont(LABEL_FONT);
        autoscaleBtn.setFont(LABEL_FONT);
        controlPanelLayout.add(drawCurvesBtn, controlPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(eraseCurvesBtn, controlPanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(newPlotBtn, controlPanel, 2, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(deletePlotBtn, controlPanel, 3, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(autoscaleBtn, controlPanel, 4, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        drawCurvesBtn.addActionListener(this);
        eraseCurvesBtn.addActionListener(this);
        newPlotBtn.addActionListener(this);
        deletePlotBtn.addActionListener(this);
        autoscaleBtn.addActionListener(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.add(contentSettingsTabbedPane, this, 0, 0, 1, 1, 10, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(windowSettingsPanel, this, 0, 1, 1, 1, 10, 10, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(graphicsPanel, this, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(controlPanel, this, 1, 1, 1, 1, 100, 10, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        textPlusBtn.setFont(LABEL_FONT);
        textMinusBtn.setFont(LABEL_FONT);
        textPlusBtn.addActionListener(this);
        textMinusBtn.addActionListener(this);
    }

    public void init() {
        mathPainter = graphicsPanel.init();
        this.initializeGraphics();
        contentSettingsPanel.setupGraphics();
        this.drawGraphPaper();
        this.drawPlots();
        this.graphicsPanel.setBase();
        this.graphicsPanel.update();
    }

    public void initializeGraphics() {
        this.gridPlotter = new GridPlotter(this.mathPainter);
        this.axesPlotter = new AxesPlotter(this.mathPainter);
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
        axesPlotter.drawTicks(new Font("SansSerif", Font.PLAIN, currentTextSize));
    }

    public void draw() {
        graphicsPanel.clearCompletely();
        initializeGraphics();
        drawGraphPaper();
        drawPlots();
        graphicsPanel.setBase();
        graphicsPanel.update();
    }

    public void drawPlots() {
        int nTabs = this.contentSettingsTabbedPane.getTabCount();
        for (int i = 0; i < nTabs; i++) {
            RecurrenceRelationsPlotCSP thiscsp = (RecurrenceRelationsPlotCSP) this.contentSettingsTabbedPane.getComponentAt(i);
            String funcString = thiscsp.funcTFd.getText();
            if (thiscsp.inheritRanges) {
                thiscsp.nMinTFd.setText("0");
                double xMax = new Double(windowXMaxTFd.getText()).doubleValue();
                int nMaxLocal = (int) (Math.floor(xMax));
                thiscsp.nMaxTFd.setText("" + nMaxLocal);
            }
            double nMin = 1.000001 * (new Double(thiscsp.nMinTFd.getText())).doubleValue();
            double nMax = (new Double(thiscsp.nMaxTFd.getText())).doubleValue();
            try {
                Parser funcParser = new Parser(funcString, parseVariables);
                double a = new Double(thiscsp.a0TFd.getText()).doubleValue();
                int nPoints = (int) (nMax - nMin);
                double[] xPoints = new double[nPoints + 1];
                double[] yPoints = new double[nPoints + 1];
                if (nMin == 0.0) {
                    xPoints[0] = 0.0;
                    yPoints[0] = a;
                }
                for (int j = 1; j <= nPoints; j++) {
                    double[] anValues = new double[2];
                    anValues[0] = a;
                    anValues[1] = j;
                    a = funcParser.getValue(anValues);
                    if (nMin <= j) {
                        xPoints[j] = j;
                        yPoints[j] = a;
                    }
                }
                thiscsp.sequenceMC.setPoints(xPoints, yPoints);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            contentSettingsTabbedPane.setTitleAt(i, thiscsp.plotLabelTFd.getText());
            thiscsp.drawPlot();
            thiscsp.settingsAltered = false;
        }
    }

    public Component getComponent() {
        return this;
    }

    public void mathPainterPanelResized() {
        System.out.println("resized");
        draw();
    }

    public void mathPainterPanelAction(MathPainterPanelEvent mppe) {
    }

    public void keyPressed(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) draw();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == newPlotBtn) {
            plotIndex++;
            defaultColorIndex++;
            if (defaultColorIndex == 7) defaultColorIndex = 0;
            RecurrenceRelationsPlotCSP newcsp = new RecurrenceRelationsPlotCSP(this, "Plot " + plotIndex, defaultSequenceColorWheel[defaultColorIndex], defaultSeriesColorWheel[defaultColorIndex], currentTextSize, currentMathSize, currentLabelFont, currentFieldFont, windowXMinTFd.getText(), windowXMaxTFd.getText());
            newcsp.setupGraphics();
            this.contentSettingsTabbedPane.add("Plot " + plotIndex, newcsp);
            this.contentSettingsTabbedPane.setSelectedIndex(this.contentSettingsTabbedPane.indexOfTab("Plot " + plotIndex));
            draw();
        } else if (ae.getSource() == deletePlotBtn) {
            this.contentSettingsTabbedPane.remove(contentSettingsTabbedPane.getSelectedIndex());
            graphicsPanel.clearCompletely();
            initializeGraphics();
            drawGraphPaper();
            drawPlots();
            graphicsPanel.setBase();
            graphicsPanel.update();
        } else if (ae.getSource() == eraseCurvesBtn) {
            graphicsPanel.clearCompletely();
            initializeGraphics();
            drawGraphPaper();
            graphicsPanel.setBase();
            graphicsPanel.update();
        } else if (ae.getSource() == drawCurvesBtn) {
            draw();
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
            System.out.println("autoscale button pressed");
            double newYMin = Double.POSITIVE_INFINITY;
            double newYMax = Double.NEGATIVE_INFINITY;
            int nTabs = this.contentSettingsTabbedPane.getTabCount();
            for (int i = 0; i < nTabs; i++) {
                RecurrenceRelationsPlotCSP thiscsp = (RecurrenceRelationsPlotCSP) this.contentSettingsTabbedPane.getComponentAt(i);
                thiscsp.sequenceMC.autoScale(MathCoords.Y_AXIS, MathCoords.UNCONSTRAINED);
                newYMin = Math.min(newYMin, this.mathPainter.getYMin());
                newYMax = Math.max(newYMax, this.mathPainter.getYRange() + this.mathPainter.getYMin());
            }
            newYMin = Math.round(1000.0 * newYMin) / 1000.0;
            newYMax = Math.round(1000.0 * newYMax) / 1000.0;
            windowYMinTFd.setText("" + newYMin);
            windowYMaxTFd.setText("" + newYMax);
            draw();
        } else {
            try {
                draw();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateFonts() {
        currentLabelFont = new Font("SansSerif", Font.BOLD, currentTextSize - 2);
        currentFieldFont = new Font("SansSerif", Font.PLAIN, currentTextSize);
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
        drawCurvesBtn.setFont(currentLabelFont);
        eraseCurvesBtn.setFont(currentLabelFont);
        newPlotBtn.setFont(currentLabelFont);
        deletePlotBtn.setFont(currentLabelFont);
        autoscaleBtn.setFont(currentLabelFont);
        textPlusBtn.setFont(currentLabelFont);
        textMinusBtn.setFont(currentLabelFont);
        graphicsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Sequence Plots", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
        contentSettingsTabbedPane.setFont(currentLabelFont);
        int nTabs = this.contentSettingsTabbedPane.getTabCount();
        for (int i = 0; i < nTabs; i++) {
            RecurrenceRelationsPlotCSP thiscsp = (RecurrenceRelationsPlotCSP) this.contentSettingsTabbedPane.getComponentAt(i);
            thiscsp.updateFonts();
        }
    }
}

class RecurrenceRelationsPlotCSP extends JPanel implements CaretListener, KeyListener, ItemListener, ActionListener {

    ExtendedGridBagLayout layout;

    private static final ExtendedGridBagLayout stylePanelLayout = new ExtendedGridBagLayout();

    JLabel funcLbl;

    JTextField funcTFd;

    JEditorPane funcJEP;

    JCheckBox inheritRangesCBx;

    JLabel nMinLbl;

    JTextField nMinTFd;

    JLabel nMaxLbl;

    JTextField nMaxTFd;

    JLabel a0Lbl;

    JTextField a0TFd;

    JButton localStylesBtn;

    JFrame plotStyleFrame;

    JPanel plotStylePanel;

    JLabel plotLabelLbl;

    JTextField plotLabelTFd;

    JCheckBox lineCBx;

    JCheckBox pointCBx;

    SelectColorButton plotColorBtn;

    String plotLabel;

    Color sequenceColor;

    Color seriesColor;

    boolean settingsAltered;

    boolean inheritRanges = true;

    NewRecurrenceRelationsPlottingTool parentPanel;

    MathCoords sequenceMC;

    CurvePlotter sequenceCP;

    PointPlotter sequencePP;

    double nMin;

    double nMax;

    public RecurrenceRelationsPlotCSP(NewRecurrenceRelationsPlottingTool parentPanel, String plotLabel, Color sequenceColor, Color seriesColor, int textSize, int mathSize, Font labelFont, Font fieldFont, String xMinString, String xMaxString) {
        super();
        this.parentPanel = parentPanel;
        this.layout = new ExtendedGridBagLayout();
        this.setLayout(layout);
        this.plotLabel = plotLabel;
        this.sequenceColor = sequenceColor;
        this.seriesColor = seriesColor;
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Content Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, labelFont));
        this.funcLbl = new JLabel("a -> ");
        this.funcTFd = new JTextField("(1+a)/2");
        try {
            this.funcJEP = new JEditorPane("text/html", MathsStyleHelper.getStyledHTML(TreeFormatter.format2D("(1+a)/2"), textSize, mathSize));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.inheritRangesCBx = new JCheckBox("inherit ranges", true);
        double xMin = new Double(xMinString).doubleValue();
        int nMinLocal = (int) (Math.ceil(xMin) + 1);
        double xMax = new Double(xMaxString).doubleValue();
        int nMaxLocal = (int) (Math.floor(xMax));
        this.nMinLbl = new JLabel("xMin");
        this.nMinTFd = new JTextField("" + nMinLocal);
        this.nMaxLbl = new JLabel("xMax");
        this.nMaxTFd = new JTextField("" + nMaxLocal);
        this.a0Lbl = new JLabel("a0");
        this.a0TFd = new JTextField("0.3");
        this.localStylesBtn = new JButton("Plot Styles...");
        this.plotLabelLbl = new JLabel("Plot label");
        this.plotLabelTFd = new JTextField(this.plotLabel);
        this.lineCBx = new JCheckBox("show line", false);
        this.pointCBx = new JCheckBox("show points", true);
        this.plotColorBtn = new SelectColorButton(this.sequenceColor, "Plot Colour...");
        plotLabelLbl.setFont(labelFont);
        plotLabelTFd.setFont(fieldFont);
        lineCBx.setFont(labelFont);
        pointCBx.setFont(labelFont);
        plotColorBtn.setButtonFont(labelFont);
        funcLbl.setFont(labelFont);
        inheritRangesCBx.setFont(labelFont);
        nMinLbl.setFont(labelFont);
        nMaxLbl.setFont(labelFont);
        a0Lbl.setFont(labelFont);
        localStylesBtn.setFont(labelFont);
        funcTFd.setFont(fieldFont);
        nMinTFd.setFont(fieldFont);
        nMaxTFd.setFont(fieldFont);
        a0TFd.setFont(fieldFont);
        funcTFd.addCaretListener(this);
        funcTFd.addKeyListener(this);
        nMinTFd.addKeyListener(this);
        nMaxTFd.addKeyListener(this);
        a0TFd.addKeyListener(this);
        plotLabelTFd.addKeyListener(this);
        inheritRangesCBx.addItemListener(this);
        localStylesBtn.addActionListener(this);
        nMinTFd.setEditable(false);
        nMaxTFd.setEditable(false);
        layout.add(funcLbl, this, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        layout.add(funcTFd, this, 1, 0, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        layout.add(funcJEP, this, 1, 1, 3, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        layout.add(inheritRangesCBx, this, 1, 2, 3, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        layout.add(nMinLbl, this, 0, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        layout.add(nMinTFd, this, 1, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        layout.add(nMaxLbl, this, 0, 4, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        layout.add(nMaxTFd, this, 1, 4, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        layout.add(a0Lbl, this, 0, 5, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        layout.add(a0TFd, this, 1, 5, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        layout.add(localStylesBtn, this, 1, 6, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
    }

    public void caretUpdate(CaretEvent ce) {
        if (ce.getSource() == funcTFd) {
            update2DFormattingArea();
        }
    }

    protected void update2DFormattingArea() {
        String funcString = funcTFd.getText();
        try {
            funcJEP.setText(MathsStyleHelper.getStyledHTML(TreeFormatter.format2D(funcString), parentPanel.currentTextSize, parentPanel.currentMathSize));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            parentPanel.draw();
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == inheritRangesCBx) {
            setInheritRanges(inheritRangesCBx.isSelected());
        } else parentPanel.draw();
    }

    public void setupGraphics() {
        this.sequenceMC = new MathCoords(parentPanel.mathPainter);
        this.sequenceCP = new CurvePlotter(parentPanel.mathPainter, this.sequenceMC);
        this.sequencePP = new PointPlotter(parentPanel.mathPainter, this.sequenceMC);
    }

    public void drawPlot() {
        boolean showLine = lineCBx.isSelected();
        boolean showPoints = pointCBx.isSelected();
        parentPanel.mathPainter.setPaint(plotColorBtn.getColor());
        if (showLine) sequenceCP.plot();
        if (showPoints) sequencePP.plot();
    }

    public void setInheritRanges(boolean inheritRanges) {
        this.inheritRanges = inheritRanges;
        nMinTFd.setEditable(!inheritRanges);
        nMaxTFd.setEditable(!inheritRanges);
        if (inheritRanges) {
            double xMin = new Double(parentPanel.windowXMinTFd.getText()).doubleValue();
            int nMinLocal = (int) (Math.ceil(xMin) - 1);
            double xMax = new Double(parentPanel.windowXMaxTFd.getText()).doubleValue();
            int nMaxLocal = (int) (Math.floor(xMax) + 1);
            nMinTFd.setText("" + nMinLocal);
            nMaxTFd.setText("" + nMaxLocal);
        }
    }

    public void updateFonts() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Content Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, parentPanel.currentLabelFont));
        update2DFormattingArea();
        funcLbl.setFont(parentPanel.currentLabelFont);
        inheritRangesCBx.setFont(parentPanel.currentLabelFont);
        nMinLbl.setFont(parentPanel.currentLabelFont);
        nMaxLbl.setFont(parentPanel.currentLabelFont);
        a0Lbl.setFont(parentPanel.currentLabelFont);
        localStylesBtn.setFont(parentPanel.currentLabelFont);
        funcTFd.setFont(parentPanel.currentFieldFont);
        nMinTFd.setFont(parentPanel.currentFieldFont);
        nMaxTFd.setFont(parentPanel.currentFieldFont);
        a0TFd.setFont(parentPanel.currentFieldFont);
        plotLabelLbl.setFont(parentPanel.currentLabelFont);
        plotLabelTFd.setFont(parentPanel.currentFieldFont);
        lineCBx.setFont(parentPanel.currentLabelFont);
        pointCBx.setFont(parentPanel.currentLabelFont);
        plotColorBtn.setButtonFont(parentPanel.currentLabelFont);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == localStylesBtn) {
            plotStyleFrame = new JFrame("Plot Style Settings: " + parentPanel.contentSettingsTabbedPane.getTitleAt(parentPanel.contentSettingsTabbedPane.getSelectedIndex()));
            plotStylePanel = new JPanel();
            plotStylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(plotLabelLbl, plotStylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotLabelTFd, plotStylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(lineCBx, plotStylePanel, 0, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(pointCBx, plotStylePanel, 1, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotColorBtn, plotStylePanel, 0, 3, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            plotStyleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            plotStyleFrame.add(plotStylePanel);
            plotStyleFrame.setSize(300, 300);
            plotStyleFrame.setVisible(true);
        }
    }
}
