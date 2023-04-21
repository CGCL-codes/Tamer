package gov.sns.apps.ringmeasurement;

import java.io.*;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.ParseException;
import java.lang.Math.*;
import java.lang.String;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import gov.sns.tools.apputils.EdgeLayout;
import gov.sns.xal.smf.impl.*;
import gov.sns.xal.smf.Ring;
import gov.sns.tools.swing.DecimalField;
import gov.sns.ca.*;
import gov.sns.tools.plot.*;
import gov.sns.tools.apputils.files.*;
import gov.sns.tools.pvlogger.*;
import gov.sns.tools.pvlogger.query.*;
import gov.sns.tools.database.*;
import gov.sns.tools.beam.Twiss;
import gov.sns.xal.model.*;
import gov.sns.tools.LinearFit;
import gov.sns.xal.model.probe.TransferMapProbe;
import gov.sns.xal.model.probe.ProbeFactory;
import gov.sns.xal.model.alg.TransferMapTracker;
import gov.sns.xal.model.scenario.Scenario;
import gov.sns.xal.model.probe.Probe;
import gov.sns.xal.model.probe.traj.*;
import gov.sns.xal.model.probe.traj.BeamProbeState;
import gov.sns.xal.model.pvlogger.PVLoggerDataSource;
import gov.sns.xal.smf.*;
import gov.sns.tools.xml.*;
import gov.sns.tools.data.*;
import gov.sns.xal.smf.application.*;
import gov.sns.xal.smf.*;
import gov.sns.xal.smf.data.XMLDataManager;
import gov.sns.application.*;

/**
 * @author cp3, tep
 * Class for calculating beta functions via MIA analysis from live BPM data.   
 */
public class MIALive extends JPanel {

    EdgeLayout edgeLayout = new EdgeLayout();

    JPanel mainPanel = new JPanel();

    JButton startButton;

    JButton pauseButton;

    JLabel activeLabel;

    JButton exportButton;

    JFileChooser exportFile;

    JLabel xScaleElementLabel;

    JLabel yScaleElementLabel;

    JLabel xScaleValueLabel;

    JLabel yScaleValueLabel;

    private String[] xScaleElements = { "No Scaling" };

    private String[] yScaleElements = { "No Scaling" };

    private JComboBox xScaleSelector = new JComboBox(xScaleElements);

    private JComboBox yScaleSelector = new JComboBox(yScaleElements);

    DecimalField xScaleValue;

    DecimalField yScaleValue;

    NumberFormat numForm = NumberFormat.getNumberInstance();

    JLabel numTurnsLabel;

    DecimalField numTurnsField;

    int numTurns;

    JCheckBox xDesignCheck;

    JCheckBox yDesignCheck;

    Boolean xDesignFlag = false;

    Boolean yDesignFlag = false;

    JCheckBox xLiveDesignCheck;

    JCheckBox yLiveDesignCheck;

    Boolean xLiveDesignFlag = false;

    Boolean yLiveDesignFlag = false;

    JCheckBox xLinesCheck;

    JCheckBox yLinesCheck;

    Boolean xLinesFlag = true;

    Boolean yLinesFlag = true;

    JCheckBox xAvgCheck;

    JCheckBox yAvgCheck;

    Boolean xAvgFlag = false;

    Boolean yAvgFlag = false;

    JLabel betaXPlotLabel;

    JLabel betaYPlotLabel;

    FunctionGraphsJPanel betaXGraphPanel;

    FunctionGraphsJPanel betaYGraphPanel;

    BasicGraphData xBetaLive;

    BasicGraphData yBetaLive;

    BasicGraphData xBetaDesign;

    BasicGraphData yBetaDesign;

    BasicGraphData xBetaLiveDesign;

    BasicGraphData yBetaLiveDesign;

    private double[] BPMPositionPlot;

    private double[] xBetaPlot;

    private double[] yBetaPlot;

    double[] posDesignPlot;

    double[] xBetaDesignPlot;

    double[] yBetaDesignPlot;

    double[] posLiveDesignPlot;

    double[] xBetaLiveDesignPlot;

    double[] yBetaLiveDesignPlot;

    JScrollPane BPMSelectorPane;

    private JTable BPMTable;

    private DataTableModel BPMDataTableModel;

    private String[] BPMSelColNames = { "Name", "Position", "Use?" };

    public AcceleratorSeqCombo seq;

    Accelerator accl = new Accelerator();

    Probe probe;

    Scenario scenario;

    Trajectory traj;

    ProbeState state;

    Twiss[] twiss = new Twiss[2];

    ArrayList BPMList = new ArrayList();

    int numBPMs;

    double[][] xTBTArray;

    double[][] yTBTArray;

    BPMAgent[] BPMAgents;

    int refreshRate;

    ActionListener tasksOnTimer;

    Timer updateTimer;

    Matrix xBPMMatrix;

    Matrix yBPMMatrix;

    SingularValueDecomposition xSVD;

    SingularValueDecomposition ySVD;

    double[] xSigma;

    double[] ySigma;

    Matrix xUMatrix;

    Matrix yUMatrix;

    Matrix xVMatrix;

    Matrix yVMatrix;

    double[] xBeta;

    double[] yBeta;

    double[] BPMPosition;

    double xScaleFactor;

    double yScaleFactor;

    public MIALive() {
        makeComponents();
        addComponents();
        acclSetup();
        makeBPMConnections();
        makeTimer();
        makeScaleSelector();
        makeBPMSelectorTable();
        makeDesignData();
        makeLiveDesignData();
        setAction();
    }

    protected void addComponents() {
        edgeLayout.setConstraints(mainPanel, 0, 0, 0, 0, EdgeLayout.ALL_SIDES, EdgeLayout.GROW_BOTH);
        this.add(mainPanel);
        EdgeLayout newLayout = new EdgeLayout();
        mainPanel.setLayout(newLayout);
        newLayout.add(startButton, mainPanel, 5, 5, EdgeLayout.LEFT);
        newLayout.add(pauseButton, mainPanel, 10, 35, EdgeLayout.LEFT);
        newLayout.add(activeLabel, mainPanel, 47, 63, EdgeLayout.LEFT);
        newLayout.add(exportButton, mainPanel, 145, 35, EdgeLayout.LEFT);
        newLayout.add(xScaleElementLabel, mainPanel, 330, 10, EdgeLayout.LEFT);
        newLayout.add(xScaleSelector, mainPanel, 410, 5, EdgeLayout.LEFT);
        newLayout.add(xScaleValueLabel, mainPanel, 540, 10, EdgeLayout.LEFT);
        newLayout.add(xScaleValue, mainPanel, 600, 5, EdgeLayout.LEFT);
        newLayout.add(yScaleElementLabel, mainPanel, 330, 40, EdgeLayout.LEFT);
        newLayout.add(yScaleSelector, mainPanel, 410, 35, EdgeLayout.LEFT);
        newLayout.add(yScaleValueLabel, mainPanel, 540, 40, EdgeLayout.LEFT);
        newLayout.add(yScaleValue, mainPanel, 600, 35, EdgeLayout.LEFT);
        newLayout.add(numTurnsLabel, mainPanel, 145, 10, EdgeLayout.LEFT);
        newLayout.add(numTurnsField, mainPanel, 255, 5, EdgeLayout.LEFT);
        newLayout.add(xLinesCheck, mainPanel, 0, 80, EdgeLayout.LEFT);
        newLayout.add(yLinesCheck, mainPanel, 0, 430, EdgeLayout.LEFT);
        newLayout.add(xDesignCheck, mainPanel, 100, 80, EdgeLayout.LEFT);
        newLayout.add(yDesignCheck, mainPanel, 100, 430, EdgeLayout.LEFT);
        newLayout.add(xLiveDesignCheck, mainPanel, 210, 80, EdgeLayout.LEFT);
        newLayout.add(yLiveDesignCheck, mainPanel, 210, 430, EdgeLayout.LEFT);
        newLayout.add(xAvgCheck, mainPanel, 315, 80, EdgeLayout.LEFT);
        newLayout.add(yAvgCheck, mainPanel, 315, 430, EdgeLayout.LEFT);
        newLayout.add(betaXPlotLabel, mainPanel, 465, 85, EdgeLayout.LEFT);
        newLayout.add(betaXGraphPanel, mainPanel, 10, 100, EdgeLayout.LEFT);
        newLayout.add(betaYPlotLabel, mainPanel, 465, 435, EdgeLayout.LEFT);
        newLayout.add(betaYGraphPanel, mainPanel, 10, 450, EdgeLayout.LEFT);
        newLayout.add(BPMSelectorPane, mainPanel, 670, 0, EdgeLayout.LEFT);
    }

    protected void makeComponents() {
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(950, 900));
        startButton = new JButton("Start Plotting");
        pauseButton = new JButton("Pause Plots");
        activeLabel = new JLabel("");
        exportButton = new JButton("Export Data to File");
        betaXGraphPanel = new FunctionGraphsJPanel();
        betaYGraphPanel = new FunctionGraphsJPanel();
        betaXGraphPanel.setPreferredSize(new Dimension(930, 325));
        betaXGraphPanel.setGraphBackGroundColor(Color.WHITE);
        betaYGraphPanel.setPreferredSize(new Dimension(930, 325));
        betaYGraphPanel.setGraphBackGroundColor(Color.WHITE);
        betaXPlotLabel = new JLabel("Beta X");
        betaYPlotLabel = new JLabel("Beta Y");
        xBetaLive = new BasicGraphData();
        yBetaLive = new BasicGraphData();
        xBetaDesign = new BasicGraphData();
        yBetaDesign = new BasicGraphData();
        xBetaLiveDesign = new BasicGraphData();
        yBetaLiveDesign = new BasicGraphData();
        xScaleElementLabel = new JLabel("Beta X Scale: ");
        yScaleElementLabel = new JLabel("Beta Y Scale: ");
        xScaleValueLabel = new JLabel("to Value: ");
        yScaleValueLabel = new JLabel("to Value: ");
        xScaleValue = new DecimalField(15, 4, numForm);
        yScaleValue = new DecimalField(15, 4, numForm);
        numTurnsLabel = new JLabel("Number of turns: ");
        numTurnsField = new DecimalField(200, 4, numForm);
        numTurns = (int) numTurnsField.getValue();
        numForm.setMinimumFractionDigits(1);
        xDesignCheck = new JCheckBox("Static Design");
        yDesignCheck = new JCheckBox("Static Design");
        xLiveDesignCheck = new JCheckBox("Live Design");
        yLiveDesignCheck = new JCheckBox("Live Design");
        xLinesCheck = new JCheckBox("Draw Lines");
        xLinesCheck.setSelected(true);
        yLinesCheck = new JCheckBox("Draw Lines");
        yLinesCheck.setSelected(true);
        xAvgCheck = new JCheckBox("Use Averaging");
        yAvgCheck = new JCheckBox("Use Averaging");
        BPMDataTableModel = new DataTableModel(BPMSelColNames, 0);
        BPMTable = new JTable(BPMDataTableModel);
        BPMSelectorPane = new JScrollPane(BPMTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        BPMSelectorPane.setPreferredSize(new Dimension(275, 100));
        exportFile = new JFileChooser();
    }

    protected void setAction() {
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                activeLabel.setText("Active");
                SwingUtilities.updateComponentTreeUI(activeLabel);
                updateTimer.start();
            }
        });
        pauseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                updateTimer.stop();
                activeLabel.setText("Paused");
                SwingUtilities.updateComponentTreeUI(activeLabel);
            }
        });
        exportButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                updateTimer.stop();
                activeLabel.setText("Paused");
                SwingUtilities.updateComponentTreeUI(activeLabel);
                if (exportFile.showSaveDialog(MIALive.this) == exportFile.APPROVE_OPTION) {
                    File file = exportFile.getSelectedFile();
                    try {
                        exportData(file);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        });
        xLinesCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (xLinesFlag) {
                    xLinesFlag = false;
                } else {
                    xLinesFlag = true;
                }
            }
        });
        yLinesCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (yLinesFlag) {
                    yLinesFlag = false;
                } else {
                    yLinesFlag = true;
                }
            }
        });
        xDesignCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (xDesignFlag) {
                    xDesignFlag = false;
                } else {
                    xDesignFlag = true;
                }
            }
        });
        yDesignCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (yDesignFlag) {
                    yDesignFlag = false;
                } else {
                    yDesignFlag = true;
                }
            }
        });
        xLiveDesignCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (xLiveDesignFlag) {
                    xLiveDesignFlag = false;
                } else {
                    makeLiveDesignData();
                    xLiveDesignFlag = true;
                }
            }
        });
        yLiveDesignCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (yDesignFlag) {
                    yDesignFlag = false;
                } else {
                    makeLiveDesignData();
                    yDesignFlag = true;
                }
            }
        });
        xAvgCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (xAvgFlag) {
                    xAvgFlag = false;
                } else {
                    for (int i = 0; i < numBPMs; i++) {
                        BPMAgents[i].clearXAvg();
                    }
                    xAvgFlag = true;
                }
            }
        });
        yAvgCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (yAvgFlag) {
                    yAvgFlag = false;
                } else {
                    for (int i = 0; i < numBPMs; i++) {
                        BPMAgents[i].clearYAvg();
                    }
                    yAvgFlag = true;
                }
            }
        });
    }

    public void acclSetup() {
        accl = XMLDataManager.loadDefaultAccelerator();
        seq = accl.getComboSequence("Ring");
        BPMList = (ArrayList) seq.getNodesOfType("BPM");
        numBPMs = BPMList.size();
    }

    public void makeBPMConnections() {
        BPMAgents = new BPMAgent[numBPMs];
        for (int i = 0; i < numBPMs; i++) {
            BPMAgents[i] = new BPMAgent(seq, (BPM) BPMList.get(i), numTurns);
        }
    }

    public void makeScaleSelector() {
        xScaleSelector.removeAllItems();
        yScaleSelector.removeAllItems();
        xScaleSelector.addItem(new String("No Scaling"));
        yScaleSelector.addItem(new String("No Scaling"));
        for (int i = 0; i < numBPMs; i++) {
            if (BPMAgents[i].isOkay()) {
                xScaleSelector.addItem((String) BPMList.get(i).toString().substring(10));
                yScaleSelector.addItem((String) BPMList.get(i).toString().substring(10));
            }
        }
    }

    public void makeBPMSelectorTable() {
        BPMTable.getColumnModel().getColumn(0).setWidth(100);
        BPMTable.getColumnModel().getColumn(1).setWidth(100);
        BPMTable.getColumnModel().getColumn(2).setWidth(75);
        BPMTable.setPreferredScrollableViewportSize(BPMTable.getPreferredSize());
        BPMTable.setRowSelectionAllowed(false);
        BPMTable.setColumnSelectionAllowed(false);
        BPMTable.setCellSelectionEnabled(false);
        BPMSelectorPane.getHorizontalScrollBar().setValue(1);
        BPMDataTableModel.fireTableDataChanged();
        String BPMName;
        for (int i = 0; i < numBPMs; i++) {
            ArrayList tableData = new ArrayList();
            tableData.add(BPMAgents[i].name().substring(14));
            tableData.add(BPMAgents[i].getPosition());
            if (BPMAgents[i].isOkay()) {
                tableData.add(new Boolean(true));
            } else {
                tableData.add(new Boolean(false));
            }
            BPMDataTableModel.addTableData(tableData);
        }
        BPMDataTableModel.fireTableDataChanged();
    }

    public void makeDesignData() {
        int numSelected = numberSelected();
        posDesignPlot = new double[numSelected];
        xBetaDesignPlot = new double[numSelected];
        yBetaDesignPlot = new double[numSelected];
        try {
            probe = ProbeFactory.getTransferMapProbe(seq, new TransferMapTracker());
            scenario = Scenario.newScenarioFor(seq);
            scenario.setSynchronizationMode(Scenario.SYNC_MODE_DESIGN);
            scenario.setStartElementId("Ring_Inj:Foil");
            scenario.setProbe(probe);
            scenario.resync();
            scenario.run();
            traj = probe.getTrajectory();
            int j = 0;
            for (int i = 0; i < BPMTable.getRowCount(); i++) {
                if (((Boolean) BPMTable.getValueAt(i, 2)).booleanValue()) {
                    state = traj.stateForElement(BPMAgents[i].name());
                    twiss = ((TransferMapState) state).getTwiss();
                    posDesignPlot[j] = state.getPosition();
                    xBetaDesignPlot[j] = twiss[0].getBeta();
                    yBetaDesignPlot[j] = twiss[1].getBeta();
                    j++;
                }
            }
        } catch (ModelException e) {
            System.out.println(e);
        }
    }

    public void makeLiveDesignData() {
        int numSelected = numberSelected();
        posLiveDesignPlot = new double[numSelected];
        xBetaLiveDesignPlot = new double[numSelected];
        yBetaLiveDesignPlot = new double[numSelected];
        try {
            probe = ProbeFactory.getTransferMapProbe(seq, new TransferMapTracker());
            scenario = Scenario.newScenarioFor(seq);
            scenario.setSynchronizationMode(Scenario.SYNC_MODE_RF_DESIGN);
            scenario.setStartElementId("Ring_Inj:Foil");
            scenario.setProbe(probe);
            scenario.resync();
            scenario.run();
            traj = probe.getTrajectory();
            int j = 0;
            for (int i = 0; i < BPMTable.getRowCount(); i++) {
                if (((Boolean) BPMTable.getValueAt(i, 2)).booleanValue()) {
                    state = traj.stateForElement(BPMAgents[i].name());
                    twiss = ((TransferMapState) state).getTwiss();
                    posDesignPlot[j] = state.getPosition();
                    xBetaLiveDesignPlot[j] = twiss[0].getBeta();
                    yBetaLiveDesignPlot[j] = twiss[1].getBeta();
                    j++;
                }
            }
        } catch (ModelException e) {
            System.out.println(e);
        }
    }

    public void makeTimer() {
        refreshRate = 1000;
        tasksOnTimer = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                getTBTData();
                MIAAnalysis();
                plotBetas();
            }
        };
        updateTimer = new Timer(refreshRate, tasksOnTimer);
    }

    public void getTBTData() {
        numTurns = (int) numTurnsField.getValue();
        xTBTArray = new double[numTurns][numBPMs];
        yTBTArray = new double[numTurns][numBPMs];
        double[] xTBT = new double[numTurns];
        double[] yTBT = new double[numTurns];
        for (int i = 0; i < numBPMs; i++) {
            if (BPMAgents[i].isOkay()) {
                System.arraycopy(BPMAgents[i].getXTBT(), 0, xTBT, 0, xTBT.length);
                System.arraycopy(BPMAgents[i].getYTBT(), 0, yTBT, 0, yTBT.length);
                for (int j = 0; j < numTurns; j++) {
                    xTBTArray[j][i] = xTBT[j];
                    yTBTArray[j][i] = yTBT[j];
                }
            }
        }
    }

    public int numberSelected() {
        int numSelected = 0;
        for (int i = 0; i < BPMTable.getRowCount(); i++) {
            if (((Boolean) BPMTable.getValueAt(i, 2)).booleanValue()) {
                numSelected++;
            }
        }
        return numSelected;
    }

    public void plotBetas() {
        int numSelected = numberSelected();
        BPMPositionPlot = new double[numSelected];
        xBetaPlot = new double[numSelected];
        yBetaPlot = new double[numSelected];
        int j = 0;
        for (int i = 0; i < BPMTable.getRowCount(); i++) {
            if (((Boolean) BPMTable.getValueAt(i, 2)).booleanValue()) {
                BPMPositionPlot[j] = BPMPosition[i];
                xBetaPlot[j] = xBeta[i];
                yBetaPlot[j] = yBeta[i];
                j++;
            }
        }
        betaXGraphPanel.removeAllGraphData();
        betaYGraphPanel.removeAllGraphData();
        xBetaLive.addPoint(BPMPositionPlot, xBetaPlot);
        yBetaLive.addPoint(BPMPositionPlot, yBetaPlot);
        xBetaDesign.addPoint(posDesignPlot, xBetaDesignPlot);
        yBetaDesign.addPoint(posDesignPlot, yBetaDesignPlot);
        xBetaLiveDesign.addPoint(posLiveDesignPlot, xBetaLiveDesignPlot);
        yBetaLiveDesign.addPoint(posLiveDesignPlot, yBetaLiveDesignPlot);
        xBetaLive.setDrawLinesOn(xLinesFlag);
        xBetaLive.setDrawPointsOn(true);
        xBetaLive.setGraphColor(Color.RED);
        xBetaLive.setGraphProperty("Legend", "Measured");
        yBetaLive.setDrawLinesOn(yLinesFlag);
        yBetaLive.setDrawPointsOn(true);
        yBetaLive.setGraphColor(Color.RED);
        yBetaLive.setGraphProperty("Legend", "Measured");
        xBetaDesign.setDrawLinesOn(xLinesFlag);
        xBetaDesign.setDrawPointsOn(true);
        xBetaDesign.setGraphColor(Color.BLUE);
        xBetaDesign.setGraphProperty("Legend", "Static");
        yBetaDesign.setDrawLinesOn(yLinesFlag);
        yBetaDesign.setDrawPointsOn(true);
        yBetaDesign.setGraphColor(Color.BLUE);
        yBetaDesign.setGraphProperty("Legend", "Static");
        xBetaLiveDesign.setDrawLinesOn(xLinesFlag);
        xBetaLiveDesign.setDrawPointsOn(true);
        xBetaLiveDesign.setGraphColor(Color.LIGHT_GRAY);
        xBetaLiveDesign.setGraphProperty("Legend", "Live");
        yBetaLiveDesign.setDrawLinesOn(yLinesFlag);
        yBetaLiveDesign.setDrawPointsOn(true);
        yBetaLiveDesign.setGraphColor(Color.LIGHT_GRAY);
        yBetaLiveDesign.setGraphProperty("Legend", "Live");
        betaXGraphPanel.addGraphData(xBetaLive);
        if (xDesignFlag) betaXGraphPanel.addGraphData(xBetaDesign);
        if (xLiveDesignFlag) betaXGraphPanel.addGraphData(xBetaLiveDesign);
        betaXGraphPanel.setAxisNames("BPM Position", "Beta Value");
        betaXGraphPanel.setLegendButtonVisible(true);
        betaYGraphPanel.addGraphData(yBetaLive);
        if (yDesignFlag) betaYGraphPanel.addGraphData(yBetaDesign);
        if (yLiveDesignFlag) betaYGraphPanel.addGraphData(yBetaLiveDesign);
        betaYGraphPanel.setAxisNames("BPM Position", "Beta Value");
        betaYGraphPanel.setLegendButtonVisible(true);
    }

    public void MIAAnalysis() {
        for (int i = 0; i < numBPMs; i++) {
            double xsum = 0.0;
            double ysum = 0.0;
            double xoffset = 0.0;
            double yoffset = 0.0;
            for (int j = 0; j < numTurns; j++) {
                xsum += xTBTArray[j][i];
                ysum += yTBTArray[j][i];
            }
            xoffset = xsum / numTurns;
            for (int j = 0; j < numTurns; j++) {
                xTBTArray[j][i] -= xoffset;
                yTBTArray[j][i] -= yoffset;
            }
        }
        xBPMMatrix = new Matrix(xTBTArray);
        yBPMMatrix = new Matrix(yTBTArray);
        xSVD = new SingularValueDecomposition(xBPMMatrix);
        ySVD = new SingularValueDecomposition(yBPMMatrix);
        xSigma = xSVD.getSingularValues();
        ySigma = ySVD.getSingularValues();
        xUMatrix = xSVD.getU();
        yUMatrix = ySVD.getU();
        xVMatrix = xSVD.getV();
        yVMatrix = ySVD.getV();
        double[][] xVArray = xVMatrix.getArray();
        double[][] yVArray = yVMatrix.getArray();
        BPMPosition = new double[numBPMs];
        xBeta = new double[numBPMs];
        yBeta = new double[numBPMs];
        String BPMName;
        for (int i = 0; i < xVArray.length; i++) {
            if (BPMAgents[i].isOkay()) {
                BPMPosition[i] = BPMAgents[i].getPosition();
                xBeta[i] = (xSigma[0] * xVArray[i][0]) * (xSigma[0] * xVArray[i][0]) + (xSigma[1] * xVArray[i][1]) * (xSigma[1] * xVArray[i][1]);
                yBeta[i] = (ySigma[0] * yVArray[i][0]) * (ySigma[0] * yVArray[i][0]) + (ySigma[1] * yVArray[i][1]) * (ySigma[1] * yVArray[i][1]);
            }
        }
        if (xAvgFlag) {
            for (int i = 0; i < numBPMs; i++) {
                BPMAgents[i].addXPoint(xBeta[i]);
                xBeta[i] = BPMAgents[i].getXMean();
            }
        }
        if (yAvgFlag) {
            for (int i = 0; i < numBPMs; i++) {
                BPMAgents[i].addYPoint(yBeta[i]);
                yBeta[i] = BPMAgents[i].getYMean();
            }
        }
        if (xScaleSelector.getSelectedIndex() > 0) {
            xScaleFactor = xScaleValue.getValue() / xBeta[xScaleSelector.getSelectedIndex() - 1];
            for (int i = 0; i < numBPMs; i++) {
                xBeta[i] *= xScaleFactor;
            }
        }
        if (yScaleSelector.getSelectedIndex() > 0) {
            yScaleFactor = yScaleValue.getValue() / yBeta[yScaleSelector.getSelectedIndex() - 1];
            for (int i = 0; i < numBPMs; i++) {
                yBeta[i] *= yScaleFactor;
            }
        }
    }

    public void exportData(File file) throws IOException {
        FileOutputStream fOut = new FileOutputStream(file);
        String str = new String("#BPM Name \t\tPosition \t\tBeta X \t\t\t" + "Beta Y \n");
        for (int i = 0; i < numBPMs; i++) {
            str = str + BPMAgents[i].name() + "\t" + BPMPosition[i] + "\t\t" + xBeta[i] + "\t\t" + yBeta[i] + "\n";
        }
        fOut.write(str.getBytes());
        fOut.close();
    }
}
