package org.processmining.mining.dmcscanning.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.processmining.framework.log.LogSummary;
import org.processmining.mining.dmcscanning.DmcMiner;
import org.processmining.mining.dmcscanning.aggregation.AggregationMethod;
import org.processmining.mining.dmcscanning.equivalence.ObjectEquivalence;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author Christian W. Guenther (christian at deckfour dot org)
 */
public class DmcOptionsPanel extends JPanel implements ActionListener, ChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5607050805884987904L;

    protected DmcMiner miner = null;

    protected LogSummary summary = null;

    protected JTextField maxProxEvents = null;

    protected JTextField maxProxMillis = null;

    protected JTextField maxProxSeconds = null;

    protected JTextField maxProxMinutes = null;

    protected JCheckBox useBreakingProximity = null;

    protected JCheckBox enforceOriginator = null;

    protected JCheckBox enforceEventType = null;

    protected JCheckBox monitorDmcConsistency = null;

    protected JComboBox equivalences = null;

    protected JCheckBox consolidateAdmc = null;

    protected JComboBox aggregationMethod = null;

    protected JSlider balanceSlider = null;

    protected JTextField iterations = null;

    /**
	 * constructor
	 * 
	 * @param aMiner
	 */
    public DmcOptionsPanel(DmcMiner aMiner, LogSummary aSummary) {
        miner = aMiner;
        summary = aSummary;
        initializeGui();
    }

    /**
	 * sets up and configures the user interface
	 */
    public void initializeGui() {
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel proxEventsPanel = new JPanel();
        proxEventsPanel.setMaximumSize(new Dimension(1000, 25));
        proxEventsPanel.setLayout(new BoxLayout(proxEventsPanel, BoxLayout.X_AXIS));
        proxEventsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        proxEventsPanel.add(new JLabel("Maximum discrete proximity: "));
        proxEventsPanel.add(Box.createHorizontalStrut(10));
        maxProxEvents = new JTextField("1000", 3);
        maxProxEvents.setMaximumSize(new Dimension(100, 30));
        maxProxEvents.setHorizontalAlignment(JTextField.RIGHT);
        proxEventsPanel.add(maxProxEvents);
        proxEventsPanel.add(Box.createHorizontalStrut(5));
        proxEventsPanel.add(new JLabel("# events"));
        proxEventsPanel.add(Box.createGlue());
        this.add(proxEventsPanel);
        this.add(Box.createVerticalStrut(10));
        JPanel pTimeLabelPanel = new JPanel();
        pTimeLabelPanel.setLayout(new BoxLayout(pTimeLabelPanel, BoxLayout.X_AXIS));
        pTimeLabelPanel.setMaximumSize(new Dimension(1000, 25));
        pTimeLabelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 5));
        JLabel pTimeLabel = new JLabel("Maximum temporal proximity:");
        pTimeLabel.setMaximumSize(new Dimension(200, 25));
        pTimeLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        pTimeLabelPanel.add(pTimeLabel);
        pTimeLabelPanel.add(Box.createHorizontalGlue());
        this.add(pTimeLabelPanel);
        this.add(Box.createVerticalStrut(5));
        JPanel proxTimePanel = new JPanel();
        proxTimePanel.setLayout(new BoxLayout(proxTimePanel, BoxLayout.X_AXIS));
        proxTimePanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 5, 5));
        proxTimePanel.setMaximumSize(new Dimension(1000, 50));
        maxProxMinutes = new JTextField("5", 4);
        maxProxMinutes.setMaximumSize(new Dimension(100, 30));
        maxProxMinutes.setHorizontalAlignment(JTextField.RIGHT);
        maxProxSeconds = new JTextField("0", 4);
        maxProxSeconds.setMaximumSize(new Dimension(100, 30));
        maxProxSeconds.setHorizontalAlignment(JTextField.RIGHT);
        maxProxMillis = new JTextField("0", 4);
        maxProxMillis.setMaximumSize(new Dimension(100, 30));
        maxProxMillis.setHorizontalAlignment(JTextField.RIGHT);
        proxTimePanel.add(Box.createHorizontalStrut(15));
        proxTimePanel.add(maxProxMinutes);
        proxTimePanel.add(Box.createHorizontalStrut(5));
        proxTimePanel.add(new JLabel("minutes +"));
        proxTimePanel.add(Box.createHorizontalStrut(3));
        proxTimePanel.add(maxProxSeconds);
        proxTimePanel.add(Box.createHorizontalStrut(5));
        proxTimePanel.add(new JLabel("seconds +"));
        proxTimePanel.add(Box.createHorizontalStrut(3));
        proxTimePanel.add(maxProxMillis);
        proxTimePanel.add(Box.createHorizontalStrut(5));
        proxTimePanel.add(new JLabel("milliseconds."));
        proxTimePanel.add(Box.createGlue());
        this.add(proxTimePanel);
        this.add(Box.createVerticalStrut(20));
        JPanel breakPanel = new JPanel();
        breakPanel.setLayout(new BoxLayout(breakPanel, BoxLayout.X_AXIS));
        breakPanel.setMaximumSize(new Dimension(1000, 25));
        breakPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        useBreakingProximity = new JCheckBox("Interpret proximity as split-on-break threshold", false);
        breakPanel.add(useBreakingProximity);
        breakPanel.add(Box.createGlue());
        this.add(breakPanel);
        JPanel origPanel = new JPanel();
        origPanel.setLayout(new BoxLayout(origPanel, BoxLayout.X_AXIS));
        origPanel.setMaximumSize(new Dimension(1000, 25));
        origPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        enforceOriginator = new JCheckBox("Enforce uniform originators within clusters", true);
        origPanel.add(enforceOriginator);
        origPanel.add(Box.createGlue());
        this.add(origPanel);
        JPanel eTypePanel = new JPanel();
        eTypePanel.setLayout(new BoxLayout(eTypePanel, BoxLayout.X_AXIS));
        eTypePanel.setMaximumSize(new Dimension(1000, 25));
        eTypePanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        enforceEventType = new JCheckBox("Enforce uniform event type within clusters", true);
        eTypePanel.add(enforceEventType);
        eTypePanel.add(Box.createGlue());
        this.add(eTypePanel);
        JPanel monitorDmcConst = new JPanel();
        monitorDmcConst.setLayout(new BoxLayout(monitorDmcConst, BoxLayout.X_AXIS));
        monitorDmcConst.setMaximumSize(new Dimension(1000, 25));
        monitorDmcConst.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        monitorDmcConsistency = new JCheckBox("Monitor initial cluster set consistency continuously", false);
        monitorDmcConst.add(monitorDmcConsistency);
        monitorDmcConst.add(Box.createGlue());
        this.add(monitorDmcConst);
        this.add(Box.createVerticalStrut(15));
        JPanel equivalencePanel = new JPanel();
        equivalencePanel.setLayout(new BoxLayout(equivalencePanel, BoxLayout.X_AXIS));
        equivalencePanel.setMaximumSize(new Dimension(1000, 25));
        equivalencePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        equivalences = new JComboBox(DmcMiner.equivalenceRelations().toArray());
        equivalencePanel.add(new JLabel("Object equivalence relation: "));
        equivalencePanel.add(Box.createHorizontalStrut(5));
        equivalencePanel.add(equivalences);
        equivalencePanel.add(Box.createGlue());
        this.add(equivalencePanel);
        this.add(Box.createVerticalStrut(15));
        JPanel consAdmcPanel = new JPanel();
        consAdmcPanel.setLayout(new BoxLayout(consAdmcPanel, BoxLayout.X_AXIS));
        consAdmcPanel.setMaximumSize(new Dimension(1000, 25));
        consAdmcPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        consolidateAdmc = new JCheckBox("Consolidate aggregated set before deriving minimal conflict-free set", false);
        consAdmcPanel.add(consolidateAdmc);
        consAdmcPanel.add(Box.createGlue());
        this.add(consAdmcPanel);
        this.add(Box.createVerticalStrut(15));
        JPanel aggregatorPanel = new JPanel();
        aggregatorPanel.setLayout(new BoxLayout(aggregatorPanel, BoxLayout.X_AXIS));
        aggregatorPanel.setMaximumSize(new Dimension(1000, 25));
        aggregatorPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        aggregationMethod = new JComboBox(DmcMiner.aggregationMethods().toArray());
        aggregatorPanel.add(new JLabel("Method to be used for cluster aggregation: "));
        aggregatorPanel.add(Box.createHorizontalStrut(5));
        aggregatorPanel.add(aggregationMethod);
        aggregatorPanel.add(Box.createGlue());
        this.add(aggregatorPanel);
        this.add(Box.createVerticalStrut(30));
        JPanel balanceLabelPanel = new JPanel();
        balanceLabelPanel.setLayout(new BoxLayout(balanceLabelPanel, BoxLayout.X_AXIS));
        balanceLabelPanel.setMaximumSize(new Dimension(1000, 25));
        balanceLabelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel balanceLabel = new JLabel("minimal conflict-free set decision balance:");
        balanceLabel.setHorizontalAlignment(JLabel.LEFT);
        balanceLabelPanel.add(balanceLabel);
        balanceLabelPanel.add(Box.createHorizontalGlue());
        this.add(balanceLabelPanel);
        balanceSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
        Hashtable balanceTable = new Hashtable();
        balanceTable.put(new Integer(0), new JLabel("Footprint Size"));
        balanceTable.put(new Integer(500), new JLabel("Balanced (1:1)"));
        balanceTable.put(new Integer(1000), new JLabel("# Aggregated clusters"));
        balanceSlider.setLabelTable(balanceTable);
        balanceSlider.setMajorTickSpacing(100);
        balanceSlider.setMinorTickSpacing(20);
        balanceSlider.setPaintLabels(true);
        balanceSlider.setPaintTicks(true);
        this.add(balanceSlider);
        this.add(Box.createVerticalStrut(20));
        JPanel iterationPanel = new JPanel();
        iterationPanel.setLayout(new BoxLayout(iterationPanel, BoxLayout.X_AXIS));
        iterationPanel.setMaximumSize(new Dimension(1000, 25));
        iterationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        iterationPanel.add(new JLabel("Iterations in derivation of minimal conflict-free set: "));
        iterationPanel.add(Box.createHorizontalStrut(10));
        iterations = new JTextField("3", 3);
        iterations.setMaximumSize(new Dimension(100, 30));
        iterations.setHorizontalAlignment(JTextField.RIGHT);
        iterationPanel.add(iterations);
        iterationPanel.add(Box.createHorizontalStrut(15));
        iterationPanel.add(new JLabel("(>=1)"));
        this.add(iterationPanel);
        this.add(Box.createVerticalStrut(15));
        this.add(Box.createGlue());
        setReasonableDefaults();
        maxProxEvents.addActionListener(this);
        maxProxMillis.addActionListener(this);
        maxProxSeconds.addActionListener(this);
        maxProxMinutes.addActionListener(this);
        enforceOriginator.addActionListener(this);
        enforceEventType.addActionListener(this);
        equivalences.addActionListener(this);
        consolidateAdmc.addActionListener(this);
        aggregationMethod.addActionListener(this);
        balanceSlider.addChangeListener(this);
        iterations.addActionListener(this);
    }

    public void setReasonableDefaults() {
        maxProxEvents.setText("9999");
        maxProxMillis.setText("000");
        maxProxSeconds.setText("00");
        maxProxMinutes.setText("15");
        useBreakingProximity.setSelected(false);
        enforceOriginator.setSelected(true);
        enforceEventType.setSelected(true);
        for (Iterator it = DmcMiner.equivalenceRelations().iterator(); it.hasNext(); ) {
            ObjectEquivalence equiv = (ObjectEquivalence) it.next();
            if (equiv.matches((String) summary.getSource().getName())) {
                equivalences.setSelectedItem(equiv);
                break;
            }
        }
        consolidateAdmc.setSelected(true);
        aggregationMethod.setSelectedIndex(0);
        balanceSlider.setValue(200);
        iterations.setText("1");
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.getContentPane().add(new DmcOptionsPanel(null, null));
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void stateChanged(ChangeEvent e) {
        if ((e.getSource() == balanceSlider) && (balanceSlider.getValueIsAdjusting() == false)) {
        }
    }

    public void propagateChanges() {
        miner.setAggregationMethod((AggregationMethod) aggregationMethod.getSelectedItem());
        miner.setConsolidateADMC(consolidateAdmc.isSelected());
        miner.setUseBreakingProximity(useBreakingProximity.isSelected());
        miner.setEnforceEventType(enforceEventType.isSelected());
        miner.setEnforceOriginator(enforceOriginator.isSelected());
        miner.setMaxNoEvents(Long.parseLong(maxProxEvents.getText()));
        long maxProxTime = Long.parseLong(maxProxMillis.getText());
        maxProxTime += (Long.parseLong(maxProxSeconds.getText()) * 1000);
        maxProxTime += (Long.parseLong(maxProxMinutes.getText()) * 60000);
        miner.setMaxProximity(maxProxTime);
        miner.setEquivalence((ObjectEquivalence) equivalences.getSelectedItem());
        miner.setMdmcSelectionBalance((double) balanceSlider.getValue() / 1000.0);
        miner.setMdmcSelectionIterations(Integer.parseInt(iterations.getText()));
    }

    public AggregationMethod getAggregationMethod() {
        return (AggregationMethod) aggregationMethod.getSelectedItem();
    }

    public boolean isConsolidateAdmc() {
        return consolidateAdmc.isSelected();
    }

    public boolean isUsingBreakingProximity() {
        return useBreakingProximity.isSelected();
    }

    public boolean isEnforceEventType() {
        return enforceEventType.isSelected();
    }

    public boolean isEnforceOriginator() {
        return enforceOriginator.isSelected();
    }

    public boolean isMonitorDmcConsistency() {
        return monitorDmcConsistency.isSelected();
    }

    public long getMaxNumberOfEvents() {
        return Long.parseLong(maxProxEvents.getText());
    }

    public long getMaxProximityTime() {
        long maxProxTime = Long.parseLong(maxProxMillis.getText());
        maxProxTime += (Long.parseLong(maxProxSeconds.getText()) * 1000);
        maxProxTime += (Long.parseLong(maxProxMinutes.getText()) * 60000);
        return maxProxTime;
    }

    public ObjectEquivalence getObjectEquivalence() {
        return (ObjectEquivalence) equivalences.getSelectedItem();
    }

    public double getMdmcSelectionBalance() {
        return ((double) balanceSlider.getValue() / 1000.0);
    }

    public int getMdmcSelectionIterations() {
        return Integer.parseInt(iterations.getText());
    }
}
