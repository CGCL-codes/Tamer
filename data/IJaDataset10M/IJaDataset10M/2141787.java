package gov.sns.apps.slacs;

import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import gov.sns.tools.swing.*;
import gov.sns.tools.plot.*;
import gov.sns.tools.apputils.*;

/**
 * This class contains the components internal to the analysis of measured data
 * for the SCL cavity setting.
 * @author  jdg
 */
public class AnalysisDisplay {

    /** the analysisStuff object to display in this panel */
    private AnalysisStuff analysisStuff;

    /** the main panel containing the analysis stuff to display */
    private JSplitPane analysisPane;

    /** checkboxes to indicate whether the BPMs will be used in analysis. */
    protected JTextField errorField;

    protected JComboBox algorithmChooser;

    /** label to display design amp + phase */
    protected JTextArea designValLabel = new JTextArea("Design Values");

    /** area to display the calculated setpoint values */
    protected JTextArea setpointValArea = new JTextArea("");

    /** the progress bar for the solver */
    protected JProgressBar progressBar = new JProgressBar();

    protected JButton setupButton, spButton, matchButton, setPntButton, sendSetPntButton;

    private DoubleInputTextField timeoutTextField, minScanPhaseField, maxScanPhaseField, minBPMAmpField, nModelStepsField;

    /** button to also plot the output energy */
    protected JToggleButton plotFitButton = new JToggleButton("show fit");

    /** checkbox to use beam loading in solution */
    protected JCheckBox useBeamLoadingBox = new JCheckBox();

    /** checkbox to unwrap data*/
    protected JCheckBox unWrapDataBox = new JCheckBox();

    /** checkbox to force consistent cavity position on solution */
    protected JCheckBox forceConsistentCavPosBox = new JCheckBox();

    /**graph panel to display scanned data */
    protected FunctionGraphsJPanel graphAnalysis = new FunctionGraphsJPanel();

    static String RANDOM_ALG = "Random";

    static String SIMPLEX_ALG = "Simplex";

    /** Analysis panel components */
    private JTable analysisSetupTable;

    private Color buttonColor = new Color(0, 230, 230);

    /** Create an object */
    public AnalysisDisplay(AnalysisStuff as) {
        analysisStuff = as;
        analysisSetupTable = new JTable(analysisStuff.analysisTableModel);
        makeAnalysisPanel();
    }

    /** return the panel displayiong the analysis stuff */
    protected JSplitPane getAnalysisPanel() {
        return analysisPane;
    }

    /** construct the panel to control the analysis */
    private void makeAnalysisPanel() {
        graphAnalysis.setLegendButtonVisible(true);
        graphAnalysis.setAxisNames("Cavity Phase (deg)", "BPM Phase Diff (deg)");
        Insets sepInsets = new Insets(5, 0, 5, 0);
        Insets nullInsets = new Insets(0, 0, 0, 0);
        JPanel analysisSetupPanel = new JPanel(new BorderLayout());
        GridBagLayout anGridBag = new GridBagLayout();
        analysisSetupPanel.setLayout(anGridBag);
        analysisSetupPanel.setPreferredSize(new Dimension(250, 500));
        analysisSetupTable.getColumnModel().getColumn(0).setPreferredWidth(170);
        analysisSetupTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        int sumy = 0;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton analysisSetupButton = new JButton("Import Scan Data");
        analysisSetupButton.setBackground(buttonColor);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridwidth = 1;
        anGridBag.setConstraints(analysisSetupButton, gbc);
        analysisSetupPanel.add(analysisSetupButton);
        JButton initialGuessButton = new JButton("Initial Guess");
        initialGuessButton.setBackground(buttonColor);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridwidth = 1;
        anGridBag.setConstraints(initialGuessButton, gbc);
        analysisSetupPanel.add(initialGuessButton);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = nullInsets;
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        JLabel minBPMAmpLabel = new JLabel("Minimum BPM Amplitude (mA) : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(minBPMAmpLabel, gbc);
        analysisSetupPanel.add(minBPMAmpLabel);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        minBPMAmpField = new DoubleInputTextField((new Double(analysisStuff.minBPMAmp)).toString());
        minBPMAmpField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.minBPMAmp = minBPMAmpField.getValue();
            }
        });
        anGridBag.setConstraints(minBPMAmpField, gbc);
        analysisSetupPanel.add(minBPMAmpField);
        JLabel unWrapLabel = new JLabel("Unwrap data");
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(unWrapLabel, gbc);
        analysisSetupPanel.add(unWrapLabel);
        unWrapDataBox.setSelected(true);
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(unWrapDataBox, gbc);
        analysisSetupPanel.add(unWrapDataBox);
        JLabel beamLoadLabel = new JLabel("Use Beam Loading");
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(beamLoadLabel, gbc);
        analysisSetupPanel.add(beamLoadLabel);
        useBeamLoadingBox.setSelected(analysisStuff.useBeamLoading);
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(useBeamLoadingBox, gbc);
        analysisSetupPanel.add(useBeamLoadingBox);
        useBeamLoadingBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.useBeamLoading = useBeamLoadingBox.isSelected();
            }
        });
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = sepInsets;
        gbc.gridwidth = 2;
        gbc.weightx = 1.;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        JSeparator sep1 = new JSeparator(SwingConstants.HORIZONTAL);
        anGridBag.setConstraints(sep1, gbc);
        analysisSetupPanel.add(sep1);
        sep1.setVisible(true);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(designValLabel, gbc);
        analysisSetupPanel.add(designValLabel);
        gbc.insets = nullInsets;
        JLabel tableLabel = new JLabel("Matching Variables:");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(tableLabel, gbc);
        analysisSetupPanel.add(tableLabel);
        gbc.weightx = 1.;
        gbc.weighty = 1.;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(analysisSetupTable, gbc);
        analysisSetupPanel.add(analysisSetupTable);
        sumy += 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = sepInsets;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        JSeparator sep2 = new JSeparator(SwingConstants.HORIZONTAL);
        anGridBag.setConstraints(sep2, gbc);
        analysisSetupPanel.add(sep2);
        gbc.insets = nullInsets;
        JLabel setupLabel = new JLabel("Setup:");
        setupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(setupLabel, gbc);
        analysisSetupPanel.add(setupLabel);
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        JLabel minScanPhaseLabel = new JLabel("Minimum scan phase (deg) : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(minScanPhaseLabel, gbc);
        analysisSetupPanel.add(minScanPhaseLabel);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        minScanPhaseField = new DoubleInputTextField((new Double(analysisStuff.phaseModelMin)).toString());
        minScanPhaseField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.phaseModelMin = minScanPhaseField.getValue();
                analysisStuff.makeCalcPoints();
            }
        });
        anGridBag.setConstraints(minScanPhaseField, gbc);
        analysisSetupPanel.add(minScanPhaseField);
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        JLabel maxScanPhaseLabel = new JLabel("Maximum scan phase (deg) : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(maxScanPhaseLabel, gbc);
        analysisSetupPanel.add(maxScanPhaseLabel);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        maxScanPhaseField = new DoubleInputTextField((new Double(analysisStuff.phaseModelMax)).toString());
        maxScanPhaseField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.phaseModelMax = maxScanPhaseField.getValue();
                analysisStuff.makeCalcPoints();
            }
        });
        anGridBag.setConstraints(maxScanPhaseField, gbc);
        analysisSetupPanel.add(maxScanPhaseField);
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        JLabel nModelStepsLabel = new JLabel("Number of model steps/scan : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(nModelStepsLabel, gbc);
        analysisSetupPanel.add(nModelStepsLabel);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        nModelStepsField = new DoubleInputTextField((new Integer(analysisStuff.nCalcPoints)).toString());
        nModelStepsField.setDecimalFormat(new DecimalFormat("###"));
        nModelStepsField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.nCalcPoints = (int) nModelStepsField.getValue();
                analysisStuff.makeCalcPoints();
            }
        });
        anGridBag.setConstraints(nModelStepsField, gbc);
        analysisSetupPanel.add(nModelStepsField);
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        JLabel timeoutLabel = new JLabel("Solver time limit (sec) : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(timeoutLabel, gbc);
        analysisSetupPanel.add(timeoutLabel);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        timeoutTextField = new DoubleInputTextField((new Double(analysisStuff.timeoutPeriod)).toString());
        timeoutTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.timeoutPeriod = timeoutTextField.getValue();
            }
        });
        anGridBag.setConstraints(timeoutTextField, gbc);
        analysisSetupPanel.add(timeoutTextField);
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        JLabel algorithmLabel = new JLabel("Algorithm : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(algorithmLabel, gbc);
        analysisSetupPanel.add(algorithmLabel);
        String algNames[] = { "Random", "Simplex" };
        algorithmChooser = new JComboBox(algNames);
        algorithmChooser.setSelectedIndex(1);
        gbc.weightx = 1.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(algorithmChooser, gbc);
        analysisSetupPanel.add(algorithmChooser);
        algorithmChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.algorithmId = ((String) algorithmChooser.getSelectedItem());
            }
        });
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = sepInsets;
        gbc.gridheight = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        JSeparator sep4 = new JSeparator(SwingConstants.HORIZONTAL);
        anGridBag.setConstraints(sep4, gbc);
        analysisSetupPanel.add(sep4);
        gbc.gridwidth = 1;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        JLabel spLabel = new JLabel("Single Pass : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(spLabel, gbc);
        analysisSetupPanel.add(spLabel);
        gbc.weightx = 1.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        spButton = new JButton("Single Pass");
        spButton.setBackground(buttonColor);
        spButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcThread calcThread = new CalcThread(analysisStuff);
            }
        });
        anGridBag.setConstraints(spButton, gbc);
        analysisSetupPanel.add(spButton);
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        JLabel matchLabel = new JLabel("Do the Matching : ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(matchLabel, gbc);
        analysisSetupPanel.add(matchLabel);
        gbc.weightx = 1.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        matchButton = new JButton("Start Solver");
        matchButton.setBackground(buttonColor);
        matchButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Controller ctlr = analysisStuff.controller;
                ctlr.cavitySetAction(ctlr.tuneSet.getCavity(), Controller.ANALYZING);
                SolveThread solveThread = new SolveThread(analysisStuff);
            }
        });
        anGridBag.setConstraints(matchButton, gbc);
        analysisSetupPanel.add(matchButton);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(progressBar, gbc);
        analysisSetupPanel.add(progressBar);
        gbc.gridwidth = 1;
        gbc.weightx = 1.;
        gbc.weighty = 0.;
        JLabel errorLabel = new JLabel("Error: ");
        gbc.gridx = 0;
        gbc.gridy = sumy;
        anGridBag.setConstraints(errorLabel, gbc);
        analysisSetupPanel.add(errorLabel);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        errorField = new JTextField(prettyString(analysisStuff.errorTotal));
        anGridBag.setConstraints(errorField, gbc);
        analysisSetupPanel.add(errorField);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 0;
        gbc.gridy = sumy;
        setPntButton = new JButton("Find Setpoint");
        setPntButton.setBackground(buttonColor);
        setPntButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Controller ctlr = analysisStuff.controller;
                ctlr.cavitySetAction(ctlr.tuneSet.getCavity(), Controller.CALC_SETPOINT);
                analysisStuff.updateSetpoints();
                analysisStuff.controller.addResultSet();
            }
        });
        anGridBag.setConstraints(setPntButton, gbc);
        analysisSetupPanel.add(setPntButton);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.;
        gbc.weighty = 0.;
        gbc.gridx = 1;
        gbc.gridy = sumy++;
        anGridBag.setConstraints(setpointValArea, gbc);
        analysisSetupPanel.add(setpointValArea);
        gbc.weightx = 0.;
        gbc.weighty = 1.;
        gbc.gridx = 0;
        gbc.gridy = sumy;
        sendSetPntButton = new JButton("Send new Setpoint");
        sendSetPntButton.setBackground(buttonColor);
        sendSetPntButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.sendNewSetpoints();
            }
        });
        anGridBag.setConstraints(sendSetPntButton, gbc);
        analysisSetupPanel.add(sendSetPntButton);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout());
        graphPanel.setBorder(etchedBorder);
        graphPanel.add(graphAnalysis, BorderLayout.CENTER);
        SimpleChartPopupMenu.addPopupMenuTo(graphAnalysis);
        graphPanel.add(plotFitButton, BorderLayout.SOUTH);
        plotFitButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (analysisStuff.haveData) analysisStuff.plotUpdate();
            }
        });
        analysisSetupButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.initFromSelectedData();
            }
        });
        initialGuessButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisStuff.calcInitialGuess();
                CalcThread calcThread = new CalcThread(analysisStuff);
            }
        });
        graphPanel.setPreferredSize(new Dimension(250, 500));
        analysisPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, analysisSetupPanel, graphPanel);
    }

    /** make a nice displayable string from a number */
    public String prettyString(double num) {
        DecimalFormat fieldFormat;
        if (Math.abs(num) > 10000. || Math.abs(num) < 0.1) fieldFormat = new DecimalFormat("0.000E0"); else fieldFormat = new DecimalFormat("#####.#");
        return fieldFormat.format(num);
    }

    protected void updateInputFields() {
        minBPMAmpField.setValue(analysisStuff.minBPMAmp);
        minScanPhaseField.setValue(analysisStuff.phaseModelMin);
        maxScanPhaseField.setValue(analysisStuff.phaseModelMax);
        nModelStepsField.setValue(analysisStuff.nCalcPoints);
        timeoutTextField.setValue(analysisStuff.timeoutPeriod);
    }

    /** update the design val label to reflect the values for this cavity
    */
    protected void updateDesignValLabel() {
        String label = "Design start phase (deg) = " + (new Double(analysisStuff.controller.tuneSet.targetStartCavPhase)).toString();
        label += "\nDesign avg phase (deg) = " + (new Double(analysisStuff.controller.tuneSet.targetAvgCavPhase)).toString();
        label += "\nDesign Amp (MV/m) = " + (new Double(analysisStuff.controller.tuneSet.targetCavAmp)).toString();
        label += "\nInput Energy (MeV) = " + (new Double(analysisStuff.controller.tuneSet.defaultWIn)).toString();
        designValLabel.setText(label);
    }
}
