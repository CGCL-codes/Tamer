package org.systemsbiology.chem.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.systemsbiology.chem.ISimulator;
import org.systemsbiology.chem.Reaction;
import org.systemsbiology.chem.SimulationController;
import org.systemsbiology.chem.SimulationProgressReporter;
import org.systemsbiology.chem.SimulationResults;
import org.systemsbiology.chem.SimulationResultsSet;
import org.systemsbiology.chem.SimulatorHybrid;
import org.systemsbiology.chem.SimulatorParameters;
import org.systemsbiology.chem.SimulatorStochasticLDM;
import org.systemsbiology.chem.app.SimulationLauncher.Listener;
import org.systemsbiology.chem.app.SimulationLauncher.SimulationProgressReportHandler;
import org.systemsbiology.chem.app.SimulationLauncher.SimulationRunner;
import org.systemsbiology.gui.ExceptionNotificationOptionPane;
import org.systemsbiology.math.ScientificNumberFormat;
import org.systemsbiology.math.SignificantDigitsCalculator;
import org.systemsbiology.util.DataNotFoundException;
import org.systemsbiology.util.OrderedVector;
import org.systemsbiology.util.QuickSort;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import junit.awtui.ProgressBar;

public class HybridConfigurationPanel extends JFrame {

    private JPanel centralPanel;

    private static HybridConfigurationPanel ConfigurationPanel;

    private SimulationLauncher parent;

    private JList reactionsList;

    private OrderedVector stochasticReactionsListVector;

    private JList deterministicReactionsList;

    private OrderedVector deterministicReactionsListVector;

    private Dimension sbd = new Dimension(80, 25);

    private JButton mStartButton;

    private JButton mPauseButton;

    private JButton mResumeButton;

    private JButton mCancelButton;

    private JButton mStopButton;

    private ActionListener backupParentStartAction;

    private SimulationRunner mSimulationRunner;

    private JTextField startTime;

    private JTextField stopTime;

    private JTextField numRuns;

    private JPanel progressBox;

    private SimulationProgressReporter mSimulationProgressReporter;

    private SimulationProgressReportHandler mSimulationProgressReportHandler;

    private SimulationController mSimulationController;

    private static org.systemsbiology.chem.Model mModel = null;

    private Object[] selectedReactions;

    private Object[] selectedDeterministicReactions;

    private JProgressBar mSimulationProgressBar;

    public JLabel mSecondsRemainingLabel;

    public JTextField mSecondsRemainingNumber;

    private JLabel presimTitle = new JLabel();

    private JPanel resultsPanel = new JPanel();

    private SimulationRunParameters runParameters = null;

    private CategoryPlot profileSSA;

    private SimulatorStochasticLDM simulatorStochastic;

    private JFreeChart mChart;

    private HybridConfigurationPanel(SimulationLauncher mMainFrame) {
        parent = mMainFrame;
        mModel = parent.mModel;
        init();
    }

    public void init() {
        mSimulationProgressReporter = new SimulationProgressReporter();
        mSimulationController = new SimulationController();
        this.setTitle("Hybrid Method Configuration");
        this.setBounds(200, 100, 850, 600);
        this.setResizable(false);
        this.setFocusable(false);
        centralPanel = new JPanel();
        centralPanel.setLayout(new BorderLayout());
        createSimulationRunnerThread();
        createSimulationProgressReporterThread();
        simulatorStochastic = new SimulatorStochasticLDM();
        try {
            simulatorStochastic.initialize(parent.getCurrentModel());
        } catch (Exception e) {
        }
        createProgressBox();
        JPanel northpanel = new JPanel();
        northpanel.setLayout(new BorderLayout());
        JPanel titleContainer = new JPanel();
        titleContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titleContainer.setLayout(new BorderLayout());
        JLabel lTitle = new JLabel();
        lTitle.setHorizontalTextPosition(JLabel.CENTER);
        lTitle.setHorizontalAlignment(JLabel.CENTER);
        lTitle.setText("Select Deterministic Reactions");
        titleContainer.add(lTitle, BorderLayout.CENTER);
        northpanel.add(titleContainer, BorderLayout.NORTH);
        JPanel reactionMovement = new JPanel();
        reactionMovement.setLayout(new BoxLayout(reactionMovement, BoxLayout.X_AXIS));
        northpanel.add(reactionMovement, BorderLayout.CENTER);
        JPanel reactionPanelList = new JPanel();
        reactionPanelList.setBorder(BorderFactory.createEtchedBorder());
        reactionPanelList.setLayout(new BorderLayout());
        JLabel label = new JLabel("Stochastic reactions:");
        reactionsList = createReactionList();
        updateSelectedReactionList();
        JScrollPane scrollPane = new JScrollPane(reactionsList);
        JPanel padding1 = new JPanel();
        padding1.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 1));
        padding1.add(label);
        JPanel padding2 = new JPanel();
        padding2.setLayout(new BorderLayout());
        padding2.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
        padding2.add(scrollPane, BorderLayout.CENTER);
        reactionPanelList.add(padding1, BorderLayout.NORTH);
        reactionPanelList.add(padding2, BorderLayout.CENTER);
        reactionMovement.add(reactionPanelList);
        JPanel buttonsContainer = new JPanel();
        buttonsContainer.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
        buttonsContainer.setLayout(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        JButton addReaction = new JButton("->");
        addReaction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleAddReaction();
            }
        });
        JButton removeReaction = new JButton("<-");
        removeReaction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleRemoveReaction();
            }
        });
        buttonsPanel.add(addReaction);
        buttonsPanel.add(removeReaction);
        JPanel paddingUp = new JPanel();
        paddingUp.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        buttonsContainer.add(paddingUp, BorderLayout.NORTH);
        buttonsContainer.add(buttonsPanel, BorderLayout.CENTER);
        reactionMovement.add(buttonsContainer);
        JPanel detReactionPanelList = new JPanel();
        detReactionPanelList.setBorder(BorderFactory.createEtchedBorder());
        detReactionPanelList.setLayout(new BorderLayout());
        JLabel label1 = new JLabel("Determintic reactions:");
        deterministicReactionsList = createDeterminsticReactionList();
        JScrollPane scrollPane1 = new JScrollPane(deterministicReactionsList);
        JPanel padding3 = new JPanel();
        padding3.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 1));
        padding3.add(label1);
        JPanel padding4 = new JPanel();
        padding4.setLayout(new BorderLayout());
        padding4.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
        padding4.add(scrollPane1, BorderLayout.CENTER);
        detReactionPanelList.add(padding3, BorderLayout.NORTH);
        detReactionPanelList.add(padding4, BorderLayout.CENTER);
        reactionMovement.add(detReactionPanelList, BorderLayout.EAST);
        centralPanel.add(northpanel, BorderLayout.NORTH);
        JPanel buttonPanel = createButtonPanel();
        centralPanel.add(buttonPanel, BorderLayout.EAST);
        JPanel resultPanelContainer = new JPanel();
        resultPanelContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultPanelContainer.setLayout(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createEtchedBorder());
        resultsPanel.setLayout(new BorderLayout());
        presimTitle.setFont(new Font(presimTitle.getFont().getFontName(), presimTitle.getFont().getStyle(), 15));
        presimTitle.setText("Run a full stochastic simulation");
        presimTitle.setHorizontalAlignment(JLabel.CENTER);
        presimTitle.setHorizontalTextPosition(JLabel.CENTER);
        resultsPanel.add(presimTitle, BorderLayout.NORTH);
        resultPanelContainer.add(resultsPanel, BorderLayout.CENTER);
        centralPanel.add(resultPanelContainer, BorderLayout.CENTER);
        this.add(centralPanel);
        this.repaint();
    }

    private void createProgressBox() {
        mSimulationProgressBar = new JProgressBar(0, 100);
        mSimulationProgressBar.setPreferredSize(new Dimension(300, 30));
        mSecondsRemainingLabel = new JLabel(" secs remaining: ");
        mSecondsRemainingNumber = new JTextField("", 15);
        mSecondsRemainingNumber.setMaximumSize(new Dimension(100, 30));
        mSecondsRemainingNumber.setEditable(false);
        progressBox = new JPanel();
        progressBox.setLayout(new BoxLayout(progressBox, BoxLayout.X_AXIS));
        progressBox.add(mSimulationProgressBar);
        progressBox.add(mSecondsRemainingLabel);
        progressBox.add(mSecondsRemainingNumber);
        JPanel mainButtons = new JPanel();
        mainButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        mainButtons.setLayout(new BoxLayout(mainButtons, BoxLayout.X_AXIS));
        JButton configureButton = new JButton();
        configureButton.setText("Configure");
        configureButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleConfigure();
            }
        });
        JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
        mainButtons.add(configureButton);
        mainButtons.add(cancelButton);
        progressBox.add(mainButtons);
        centralPanel.add(progressBox, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("controller:");
        JButton startButton = createStartButton();
        JButton cancelButton = createCancelButton();
        JButton pauseButton = createPauseButton();
        JButton resumeButton = createResumeButton();
        JButton stopButton = createStopButton();
        JPanel padding5 = new JPanel();
        padding5.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 1));
        padding5.add(label);
        JPanel padding2 = new JPanel();
        padding2.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        padding2.add(startButton);
        JPanel padding7 = new JPanel();
        padding7.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        padding7.add(stopButton);
        JPanel padding1 = new JPanel();
        padding1.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        padding1.add(cancelButton);
        JPanel padding3 = new JPanel();
        padding3.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        padding3.add(pauseButton);
        JPanel padding4 = new JPanel();
        padding4.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        padding4.add(resumeButton);
        JPanel padding6 = new JPanel();
        padding6.setBorder(BorderFactory.createEmptyBorder(5, 1, 1, 1));
        panel.add(padding5);
        panel.add(padding2);
        panel.add(padding7);
        panel.add(padding1);
        panel.add(padding3);
        panel.add(padding4);
        panel.add(padding6);
        mStartButton = startButton;
        mCancelButton = cancelButton;
        mPauseButton = pauseButton;
        mResumeButton = resumeButton;
        mStopButton = stopButton;
        mStartButton.setPreferredSize(sbd);
        mCancelButton.setPreferredSize(sbd);
        mPauseButton.setPreferredSize(sbd);
        mResumeButton.setPreferredSize(sbd);
        mStopButton.setPreferredSize(sbd);
        mStartButton.setEnabled(true);
        mCancelButton.setEnabled(false);
        mPauseButton.setEnabled(false);
        mResumeButton.setEnabled(false);
        mStopButton.setEnabled(false);
        JLabel labelStartTime = new JLabel("Start time");
        JLabel labelStopTime = new JLabel("Stop time");
        JLabel labelNumRuns = new JLabel("Ensamble size");
        startTime = new JTextField("0.0");
        stopTime = new JTextField("100");
        numRuns = new JTextField("1");
        panel.add(labelStartTime);
        panel.add(startTime);
        panel.add(labelStopTime);
        panel.add(stopTime);
        panel.add(labelNumRuns);
        panel.add(numRuns);
        JPanel simButtonContainer = new JPanel();
        simButtonContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        simButtonContainer.setLayout(new BorderLayout());
        simButtonContainer.add(panel, BorderLayout.CENTER);
        return (simButtonContainer);
    }

    public static HybridConfigurationPanel getInstance(SimulationLauncher mMainFrame) {
        if ((ConfigurationPanel == null) || mMainFrame.mModel != ConfigurationPanel.mModel) {
            ConfigurationPanel = new HybridConfigurationPanel(mMainFrame);
        }
        return ConfigurationPanel;
    }

    private JList createReactionList() {
        Reaction[] reactions = simulatorStochastic.getReactions();
        stochasticReactionsListVector = new OrderedVector();
        for (int i = 0; i < reactions.length; i++) {
            stochasticReactionsListVector.add(reactions[i]);
        }
        assert (reactions.length > 0) : "no reactions found";
        Object[] reactionsObjects = reactions;
        if (reactionsObjects.length == 0) {
            throw new IllegalStateException("there are no reactions available");
        }
        JList reactionsList = new JList();
        reactionsList.setListData(stochasticReactionsListVector.getVector());
        reactionsList.setSelectedIndex(0);
        reactionsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ListSelectionListener listSelectionListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    updateSelectedReactionList();
                }
            }
        };
        reactionsList.addListSelectionListener(listSelectionListener);
        return (reactionsList);
    }

    private JList createDeterminsticReactionList() {
        deterministicReactionsListVector = new OrderedVector();
        JList reactionsDetList = new JList(deterministicReactionsListVector.getVector());
        reactionsDetList.setSelectedIndex(0);
        reactionsDetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ListSelectionListener listSelectionListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    updateSelectedDetReactionList();
                }
            }
        };
        reactionsDetList.addListSelectionListener(listSelectionListener);
        return (reactionsDetList);
    }

    private void updateSelectedReactionList() {
        selectedReactions = reactionsList.getSelectedValues();
    }

    private void updateSelectedDetReactionList() {
        selectedDeterministicReactions = deterministicReactionsList.getSelectedValues();
    }

    private void createSimulationRunnerThread() {
        SimulationRunner simulationRunner = new SimulationRunner();
        Thread simulationRunnerThread = new Thread(simulationRunner);
        simulationRunnerThread.setDaemon(true);
        simulationRunnerThread.start();
        mSimulationRunner = simulationRunner;
    }

    private void handleStartButton() {
        if (!getSimulationInProgress()) {
            SimulationController simulationController = mSimulationController;
            simulationController.setCancelled(false);
            simulationController.setStopped(false);
            simulationController.setFinished(false);
            SimulationProgressReporter simulationProgressReporter = mSimulationProgressReporter;
            simulationProgressReporter.setSimulationFinished(false);
            String backupStart = parent.mStartTimeField.getText();
            String backupStop = parent.mStopTimeField.getText();
            String backupEnsamle = parent.mEnsembleField.getText();
            parent.mStartTimeField.setText(startTime.getText());
            parent.mStopTimeField.setText(stopTime.getText());
            parent.mEnsembleField.setText(numRuns.getText());
            boolean backupTable = parent.mTableCheckBox.isSelected();
            parent.mTableCheckBox.setSelected(false);
            boolean backupXY = parent.mXYChartCheckBox.isSelected();
            parent.mXYChartCheckBox.setSelected(false);
            boolean backupCS = parent.mCSChartCheckBox.isSelected();
            parent.mCSChartCheckBox.setSelected(false);
            boolean backupAR = parent.mARChartCheckBox.isSelected();
            parent.mARChartCheckBox.setSelected(false);
            boolean backupProfile = parent.mProfileCheckBox.isSelected();
            parent.mProfileCheckBox.setSelected(false);
            boolean backupPR = parent.mPrChartCheckBox.isSelected();
            parent.mPrChartCheckBox.setSelected(false);
            boolean backupSD = parent.mSDChartCheckBox.isSelected();
            parent.mSDChartCheckBox.setSelected(false);
            SimulationRunParameters simulationRunParameters = parent.createSimulationRunParameters();
            parent.mTableCheckBox.setSelected(backupTable);
            parent.mXYChartCheckBox.setSelected(backupXY);
            parent.mCSChartCheckBox.setSelected(backupCS);
            parent.mARChartCheckBox.setSelected(backupAR);
            parent.mProfileCheckBox.setSelected(backupProfile);
            parent.mPrChartCheckBox.setSelected(backupPR);
            parent.mSDChartCheckBox.setSelected(backupSD);
            parent.mStartTimeField.setText(backupStart);
            parent.mStopTimeField.setText(backupStop);
            parent.mEnsembleField.setText(backupEnsamle);
            if (null != simulationRunParameters) {
                runParameters = simulationRunParameters;
                if (parent.mListeners != null) {
                    Iterator listenerIter = parent.mListeners.iterator();
                    while (listenerIter.hasNext()) {
                        Listener listener = (Listener) listenerIter.next();
                        listener.simulationStarting();
                    }
                }
                synchronized (mSimulationRunner) {
                    mSimulationRunner.notifyAll();
                }
            }
        }
    }

    public void handleResumeButton() {
        if (getSimulationInProgress()) {
            SimulationController simulationController = mSimulationController;
            if (simulationController.getStopped()) {
                simulationController.setStopped(false);
                updateSimulationControlButtons(true);
            }
        }
    }

    private void handleAddReaction() {
        if (selectedReactions != null) {
            for (int i = 0; i < selectedReactions.length; i++) {
                deterministicReactionsListVector.add(selectedReactions[i]);
                stochasticReactionsListVector.remove(selectedReactions[i]);
            }
            deterministicReactionsList.setListData(deterministicReactionsListVector.getVector());
            reactionsList.setListData(stochasticReactionsListVector.getVector());
            this.validate();
            this.repaint();
        }
    }

    private void handleRemoveReaction() {
        if (selectedDeterministicReactions != null) {
            for (int i = 0; i < selectedDeterministicReactions.length; i++) {
                deterministicReactionsListVector.remove(selectedDeterministicReactions[i]);
                stochasticReactionsListVector.add(selectedDeterministicReactions[i]);
            }
            deterministicReactionsList.setListData(deterministicReactionsListVector.getVector());
            reactionsList.setListData(stochasticReactionsListVector.getVector());
            this.validate();
            this.repaint();
        }
    }

    public void handlePauseButton() {
        if (getSimulationInProgress()) {
            SimulationController simulationController = mSimulationController;
            if (!simulationController.getStopped()) {
                simulationController.setStopped(true);
                updateSimulationControlButtons(true);
                mSimulationProgressReportHandler.clearLastUpdateTime();
            }
        }
    }

    public void handleCancelButton() {
        if (getSimulationInProgress()) {
            SimulationController simulationController = mSimulationController;
            simulationController.setCancelled(true);
            updateSimulationControlButtons(false);
            showCancelledSimulationDialog();
        }
    }

    private void showCancelledSimulationDialog() {
        JOptionPane.showMessageDialog(this, "Your simulation has been cancelled", "simulation cancelled", JOptionPane.INFORMATION_MESSAGE);
    }

    public void handleStopButton() {
        if (getSimulationInProgress()) {
            SimulationController simulationController = mSimulationController;
            if (!simulationController.getStopped()) {
                simulationController.setFinished(true);
                updateSimulationControlButtons(true);
            }
        }
    }

    boolean getSimulationInProgress() {
        return (null != runParameters);
    }

    private void handleConfigure() {
        SimulatorHybrid simulator = (SimulatorHybrid) parent.getSimulatorInstance("hybrid-method");
        try {
            simulator.initialize(mModel);
            simulator.setController(parent.mSimulationController);
            simulator.setProgressReporter(parent.mSimulationProgressReporter);
            simulator.setReactionsPartitions(stochasticReactionsListVector.getVector(), deterministicReactionsListVector.getVector(), parent.mModel);
        } catch (Throwable e) {
            ExceptionNotificationOptionPane optionPane = new ExceptionNotificationOptionPane(e, "Sorry, the simulation failed.  The specific error message is:");
            optionPane.createDialog(this, "Problems in the model").setVisible(true);
            return;
        }
        parent.getStartButton().setEnabled(true);
        parent.getLauncherFrame().setEnabled(true);
        parent.getLauncherFrame().setFocusable(true);
        this.setVisible(false);
    }

    private void handleCancel() {
        parent.getLauncherFrame().setEnabled(true);
        parent.getLauncherFrame().setFocusable(true);
        this.setVisible(false);
    }

    private void runPreSimulation(SimulationRunParameters srp) {
        SimulationResults simulationResults = null;
        try {
            simulatorStochastic.initialize(parent.mModel);
            updateSimulationControlButtons(simulatorStochastic.allowsInterrupt());
            simulatorStochastic.setController(mSimulationController);
            simulatorStochastic.setProgressReporter(mSimulationProgressReporter);
            simulationResults = simulatorStochastic.simulate(srp.mStartTime, srp.mEndTime, srp.mSimulatorParameters, srp.mNumTimePoints, srp.mRequestedSymbolNames);
            if (simulationResults != null) processSimulationResults(simulationResults, simulatorStochastic);
        } catch (Throwable e) {
            simulationEndCleanup(simulatorStochastic);
            ExceptionNotificationOptionPane optionPane = new ExceptionNotificationOptionPane(e, "Sorry, the simulation failed.  The specific error message is:");
            optionPane.createDialog(this, "Failure running simulation").setVisible(true);
            return;
        }
        simulationEndCleanup(simulatorStochastic);
    }

    private void processSimulationResults(SimulationResults pSimulationResults, SimulatorStochasticLDM simulator) {
        SimulatorParameters simulatorParameters = pSimulationResults.getSimulatorParameters();
        if (null != pSimulationResults.getDeadlockList() && !pSimulationResults.getDeadlockList().isEmpty()) {
            JOptionPane alert = new JOptionPane();
            alert.showMessageDialog(null, "\n" + pSimulationResults.getDeadlockList().size() + " simulation(s) in deadlock, the first at time " + (Math.rint(((Double) pSimulationResults.getDeadlockList().get(0)).doubleValue() * 100) / 100));
        }
        String graphName;
        String resultsLabel = pSimulationResults.createLabel();
        try {
            mChart = generatePRChart(pSimulationResults, pSimulationResults.getTruncatePoint());
            ChartPanel mChartPanel = new ChartPanel(mChart, true, true, true, true, true);
            mChartPanel.setZoomTriggerDistance(1);
            mChart.fireChartChanged();
            mChartPanel.addChartMouseListener(new ChartMouseListener() {

                public void chartMouseClicked(ChartMouseEvent event) {
                    handleSelectBars(event);
                }

                public void chartMouseMoved(ChartMouseEvent event) {
                }
            });
            graphName = "Reactions profile at " + stopTime.getText() + "sec";
            presimTitle.setText(graphName);
            centralPanel.remove(resultsPanel);
            resultsPanel = new JPanel();
            resultsPanel.add(mChartPanel, BorderLayout.CENTER);
            centralPanel.add(resultsPanel, BorderLayout.CENTER);
            mChartPanel.setVisible(true);
            this.validate();
            this.repaint();
        } catch (Throwable e) {
            simulationEndCleanup(simulator);
            ExceptionNotificationOptionPane optionPane = new ExceptionNotificationOptionPane(e, "Sorry, the simulation failed.  The specific error message is:");
            optionPane.createDialog(this, "Failure running simulation").setVisible(true);
            return;
        }
    }

    private void handleSelectBars(ChartMouseEvent event) {
        if (event.getEntity() != null) {
            CategoryItemEntity selectedItem = ((CategoryItemEntity) (event.getEntity()));
            try {
                Reaction selectedReaction = simulatorStochastic.getReactionfromName((String) ((CategoryItemEntity) (event.getEntity())).getCategory());
                BarCategoryRenderer renderer = ((BarCategoryRenderer) ((CategoryPlot) (mChart.getPlot())).getRenderer());
                if (stochasticReactionsListVector.contains(selectedReaction)) {
                    renderer.setItemPaint(Color.GREEN, selectedItem.getSeries(), selectedItem.getCategoryIndex());
                    deterministicReactionsListVector.add(selectedReaction);
                    stochasticReactionsListVector.remove(selectedReaction);
                    deterministicReactionsList.setListData(deterministicReactionsListVector.getVector());
                    reactionsList.setListData(stochasticReactionsListVector.getVector());
                } else if (deterministicReactionsListVector.contains(selectedReaction)) {
                    renderer.setItemPaint(Color.RED, selectedItem.getSeries(), selectedItem.getCategoryIndex());
                    deterministicReactionsListVector.remove(selectedReaction);
                    stochasticReactionsListVector.add(selectedReaction);
                    deterministicReactionsList.setListData(deterministicReactionsListVector.getVector());
                    reactionsList.setListData(stochasticReactionsListVector.getVector());
                }
                this.validate();
                this.repaint();
                mChart.fireChartChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected JFreeChart generatePRChart(SimulationResultsSet pSimulationResults, int numTimePoints) {
        Object[] reactionFireCounter = pSimulationResults.getReactionFireCounter();
        int reactionFireCounterNum = reactionFireCounter.length;
        boolean error = false;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        long[] firedTimes = new long[reactionFireCounter.length];
        int[] indexesOrder = new int[reactionFireCounter.length];
        for (int i = 0; i < reactionFireCounterNum; ++i) {
            firedTimes[i] = ((FireCounter) reactionFireCounter[i]).getCounter();
            indexesOrder[i] = i;
        }
        QuickSort.quicksort(firedTimes, indexesOrder);
        for (int i = 0; i < reactionFireCounterNum; ++i) {
            dataset.addValue(((FireCounter) reactionFireCounter[indexesOrder[i]]).getCounter(), "Number of times that a reaction is fired", ((FireCounter) reactionFireCounter[indexesOrder[i]]).getName());
            if (((FireCounter) reactionFireCounter[indexesOrder[i]]).getCounter() == 0) error = true;
        }
        if (error) {
            JOptionPane alert = new JOptionPane();
            alert.showMessageDialog(null, "\none or more reactions have never been fired\nyou should check the profile chart for more information");
        }
        CategoryAxis categoryAxis = new CategoryAxis("");
        ValueAxis valueAxis = new NumberAxis("");
        categoryAxis.setVisible(false);
        if (reactionFireCounterNum > 5) categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createDownRotationLabelPositions(Math.PI / 5.0));
        BarCategoryRenderer renderer = this.new BarCategoryRenderer();
        renderer.setPaintListSize(1, reactionFireCounterNum, Color.RED);
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setItemLabelsVisible(false);
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
        Marker barMarker = new CategoryMarker("v001");
        JFreeChart chart = new JFreeChart(null, null, plot, true);
        return (chart);
    }

    private synchronized void updateSimulationControlButtons(boolean pAllowsInterrupt) {
        SimulationController simulationController = mSimulationController;
        if (!getSimulationInProgress() || simulationController.getCancelled()) {
            mStartButton.setEnabled(true);
            mPauseButton.setEnabled(false);
            mCancelButton.setEnabled(false);
            mResumeButton.setEnabled(false);
            mStopButton.setEnabled(false);
        } else {
            if (simulationController.getStopped()) {
                mStartButton.setEnabled(false);
                mPauseButton.setEnabled(false);
                mCancelButton.setEnabled(pAllowsInterrupt);
                mResumeButton.setEnabled(pAllowsInterrupt);
                mStopButton.setEnabled(pAllowsInterrupt);
            } else {
                mStartButton.setEnabled(false);
                mPauseButton.setEnabled(pAllowsInterrupt);
                mCancelButton.setEnabled(pAllowsInterrupt);
                mStopButton.setEnabled(pAllowsInterrupt);
                mResumeButton.setEnabled(false);
            }
        }
    }

    class SimulationRunner implements Runnable {

        private boolean mTerminate;

        public SimulationRunner() {
            mTerminate = false;
        }

        public synchronized void setTerminate(boolean pTerminate) {
            mTerminate = pTerminate;
            this.notifyAll();
        }

        private synchronized boolean getTerminate() {
            return (mTerminate);
        }

        public void run() {
            while (true) {
                SimulationRunParameters simulationRunParameters = runParameters;
                if (null != simulationRunParameters) {
                    runPreSimulation(simulationRunParameters);
                } else {
                    try {
                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (getTerminate()) {
                        return;
                    }
                }
            }
        }
    }

    private JButton createResumeButton() {
        JButton resumeButton = new JButton("resume");
        resumeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleResumeButton();
            }
        });
        return (resumeButton);
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("start");
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleStartButton();
            }
        });
        return (startButton);
    }

    private JButton createPauseButton() {
        JButton pauseButton = new JButton("pause");
        pauseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handlePauseButton();
            }
        });
        return (pauseButton);
    }

    private JButton createCancelButton() {
        JButton cancelButton = new JButton("cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleCancelButton();
            }
        });
        return (cancelButton);
    }

    private JButton createStopButton() {
        JButton stopButton = new JButton("stop");
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                handleStopButton();
            }
        });
        return (stopButton);
    }

    class SimulationProgressReportHandler implements Runnable {

        private static final long NULL_TIME_UPDATE_MILLIS = 0;

        private double mLastUpdateFractionComplete;

        private long mLastUpdateTimeMillis;

        private boolean mTerminate;

        private NumberFormat mNumberFormat;

        private synchronized void setLastUpdateTimeMillis(long pLastUpdateTimeMillis) {
            mLastUpdateTimeMillis = pLastUpdateTimeMillis;
        }

        private synchronized long getLastUpdateTimeMillis() {
            return (mLastUpdateTimeMillis);
        }

        public void clearLastUpdateTime() {
            setLastUpdateTimeMillis(NULL_TIME_UPDATE_MILLIS);
        }

        public SimulationProgressReportHandler() {
            clearLastUpdateTime();
            mTerminate = false;
            SignificantDigitsCalculator sigCalc = new SignificantDigitsCalculator();
            mNumberFormat = new ScientificNumberFormat(sigCalc);
        }

        public void setTerminate(boolean pTerminate) {
            synchronized (this) {
                mTerminate = pTerminate;
            }
            synchronized (mSimulationProgressReporter) {
                mSimulationProgressReporter.notifyAll();
            }
        }

        private synchronized boolean getTerminate() {
            return (mTerminate);
        }

        public void run() {
            SimulationProgressReporter reporter = mSimulationProgressReporter;
            while (true) {
                synchronized (reporter) {
                    reporter.waitForUpdate();
                    if (getTerminate()) {
                        return;
                    }
                    if (!reporter.getSimulationFinished() && !mSimulationController.getCancelled()) {
                        long updateTimeMillis = reporter.getTimeOfLastUpdateMillis();
                        double fractionComplete = reporter.getFractionComplete();
                        String estimatedTimeToCompletionStr = null;
                        long lastUpdateTimeMillis = getLastUpdateTimeMillis();
                        int percentComplete = (int) (100.0 * fractionComplete);
                        int lastPercentComplete = (int) (100.0 * mLastUpdateFractionComplete);
                        if (NULL_TIME_UPDATE_MILLIS != lastUpdateTimeMillis) {
                            double changeFraction = fractionComplete - mLastUpdateFractionComplete;
                            if (changeFraction > 0.0) {
                                long changeTimeMillis = updateTimeMillis - lastUpdateTimeMillis;
                                double changeTimeSeconds = (changeTimeMillis) / 1000;
                                double timeToCompletion = (1.0 - fractionComplete) * changeTimeSeconds / changeFraction;
                                estimatedTimeToCompletionStr = mNumberFormat.format(timeToCompletion);
                            } else {
                                estimatedTimeToCompletionStr = "STALLED";
                            }
                        } else {
                            estimatedTimeToCompletionStr = "UNKNOWN";
                            mSimulationProgressBar.setValue(0);
                        }
                        if (lastPercentComplete != percentComplete) {
                            mSimulationProgressBar.setValue(percentComplete);
                        }
                        setLastUpdateTimeMillis(updateTimeMillis);
                        mSecondsRemainingNumber.setText(estimatedTimeToCompletionStr);
                        mLastUpdateFractionComplete = fractionComplete;
                    } else {
                        clearLastUpdateTime();
                        mLastUpdateFractionComplete = 0.0;
                        mSecondsRemainingNumber.setText("");
                        mSimulationProgressBar.setValue(0);
                    }
                }
            }
        }
    }

    private void simulationEndCleanup(ISimulator pSimulator) {
        runParameters = null;
        if (parent.mListeners != null) {
            Iterator listenerIter = parent.mListeners.iterator();
            while (listenerIter.hasNext()) {
                Listener listener = (Listener) listenerIter.next();
                listener.simulationEnding();
            }
        }
        updateSimulationControlButtons(pSimulator.allowsInterrupt());
        mSimulationProgressReporter.setSimulationFinished(true);
        mSimulationProgressReportHandler.clearLastUpdateTime();
    }

    private void createSimulationProgressReporterThread() {
        SimulationProgressReportHandler handler = new SimulationProgressReportHandler();
        mSimulationProgressReportHandler = handler;
        Thread simulationProgressReportHandlerThread = new Thread(handler);
        simulationProgressReportHandlerThread.setDaemon(true);
        simulationProgressReportHandlerThread.start();
    }

    protected void processWindowEvent(WindowEvent arg0) {
        super.processWindowEvent(arg0);
        if (WindowEvent.WINDOW_CLOSING == arg0.getID()) {
            handleCancel();
        }
    }

    class BarCategoryRenderer extends BarRenderer3D {

        Paint paintArray[][];

        public Paint getItemPaint(int row, int column) {
            return paintArray[row][column];
        }

        public void setItemPaint(Paint paint, int row, int column) throws Exception {
            if (paintArray.length == 0) {
                throw new Exception("You must call setPaintListSize first.");
            }
            paintArray[row][column] = paint;
        }

        public void setPaintListSize(int rowCount, int columnCount, Paint defaultPaint) {
            paintArray = new Paint[rowCount][columnCount];
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    paintArray[row][col] = defaultPaint;
                }
            }
        }
    }
}
