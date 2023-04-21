package gbl.ui;

import gbl.common.entity.TSProblemModel;
import gbl.vrp.common.entity.VRProblemModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class GeneralSettings extends JDialog {

    /**
	 * The unique instance of this class.
	 */
    protected static GeneralSettings instance;

    /**
	 * The requested clients from UI.
	 */
    TSProblemModel tModel = TSProblemModel.getInstance();

    /**
	 * The requested clients from UI.
	 */
    VRProblemModel vrpModel = VRProblemModel.getInstance();

    /**
	 * Properties panel. Where the properties will be asked for.
	 */
    JPanel centerPanel;

    /** 
	 *  
	 */
    JCheckBox saExecutionMeasureBox;

    JCheckBox lsdExecutionMeasureBox;

    JCheckBox pfihExecutionMeasureBox;

    JCheckBox gaExecutionMeasureBox;

    JCheckBox aStarExecutionMeasureBox;

    final Action OK_ACTION = new AbstractAction("OK") {

        public void actionPerformed(ActionEvent evt) {
            if (aStarExecutionMeasureBox.isSelected()) {
                tModel.setAStarExecutionMeasure(true);
            } else {
                tModel.setAStarExecutionMeasure(false);
            }
            if (gaExecutionMeasureBox.isSelected()) {
                tModel.setGaExecutionMeasure(true);
            } else {
                tModel.setGaExecutionMeasure(false);
            }
            if (pfihExecutionMeasureBox.isSelected()) {
                vrpModel.setPfihExecutionMeasure(true);
            } else {
                vrpModel.setPfihExecutionMeasure(false);
            }
            if (lsdExecutionMeasureBox.isSelected()) {
                vrpModel.setLsdExecutionMeasure(true);
            } else {
                vrpModel.setLsdExecutionMeasure(false);
            }
            if (saExecutionMeasureBox.isSelected()) {
                vrpModel.setSAExecutionMeasure(true);
            } else {
                vrpModel.setGlobalBestLSD(false);
            }
            setVisible(false);
        }
    };

    final Action RESTORE_DEFAULT_ACTION = new AbstractAction("Default Values") {

        public void actionPerformed(ActionEvent evt) {
            centerPanel = new JPanel(new FlowLayout());
            aStarExecutionMeasureBox = new JCheckBox("AStar Execution Measure", TSProblemModel.ASTAR_DEFAULT_MEASURE_EXECUTION);
            gaExecutionMeasureBox = new JCheckBox("GA Execution Measure", TSProblemModel.GA_DEFAULT_MEASURE_EXECUTION);
            pfihExecutionMeasureBox = new JCheckBox("PFIH Execution Measure", VRProblemModel.PFIH_DEFAULT_MEASURE_EXECUTION);
            lsdExecutionMeasureBox = new JCheckBox("LSD Execution Measure", VRProblemModel.LSD_DEFAULT_MEASURE_EXECUTION);
            saExecutionMeasureBox = new JCheckBox("SA Execution Measure", VRProblemModel.SA_DEFAULT_MEASURE_EXECUTION);
            centerPanel.add(aStarExecutionMeasureBox);
            centerPanel.add(gaExecutionMeasureBox);
            centerPanel.add(pfihExecutionMeasureBox);
            centerPanel.add(lsdExecutionMeasureBox);
            centerPanel.add(saExecutionMeasureBox);
            centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            centerPanel.setBackground(Color.getColor("202,198,202"));
            getContentPane().add(centerPanel, BorderLayout.CENTER);
        }
    };

    final Action CANCEL_ACTION = new AbstractAction("Cancel") {

        public void actionPerformed(ActionEvent evt) {
            setVisible(false);
        }
    };

    /**
	 * Creates an instance of this class.
	 */
    protected GeneralSettings() {
        setTitle("General Configuration");
        setBounds(50, 50, 320, 320);
        centerPanel = new JPanel(new FlowLayout());
        JPanel southPanel = new JPanel(new FlowLayout());
        aStarExecutionMeasureBox = new JCheckBox("AStar Execution Measure", TSProblemModel.ASTAR_DEFAULT_MEASURE_EXECUTION);
        gaExecutionMeasureBox = new JCheckBox("GA Execution Measure", TSProblemModel.GA_DEFAULT_MEASURE_EXECUTION);
        pfihExecutionMeasureBox = new JCheckBox("PFIH Execution Measure", VRProblemModel.PFIH_DEFAULT_MEASURE_EXECUTION);
        lsdExecutionMeasureBox = new JCheckBox("LSD Execution Measure", VRProblemModel.LSD_DEFAULT_MEASURE_EXECUTION);
        saExecutionMeasureBox = new JCheckBox("SA Execution Measure", VRProblemModel.SA_DEFAULT_MEASURE_EXECUTION);
        centerPanel.add(aStarExecutionMeasureBox);
        centerPanel.add(gaExecutionMeasureBox);
        centerPanel.add(pfihExecutionMeasureBox);
        centerPanel.add(lsdExecutionMeasureBox);
        centerPanel.add(saExecutionMeasureBox);
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        centerPanel.setBackground(Color.getColor("202,198,202"));
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(new JButton(RESTORE_DEFAULT_ACTION));
        buttonsPanel.add(new JButton(OK_ACTION));
        buttonsPanel.add(new JButton(CANCEL_ACTION));
        southPanel.add(buttonsPanel, BorderLayout.EAST);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    /**
	 * Retrieves the unique instance to this class.
	 * @return the instance to this class
	 */
    public static GeneralSettings getInstance() {
        if (instance == null) instance = new GeneralSettings();
        return instance;
    }
}
