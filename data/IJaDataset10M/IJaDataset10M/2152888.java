package org.processmining.mining.fuzzymining.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.deckfour.slickerbox.components.AutoFocusButton;
import org.deckfour.slickerbox.components.NiceIntegerSlider;
import org.deckfour.slickerbox.components.RoundedPanel;
import org.deckfour.slickerbox.ui.SlickerCheckBoxUI;
import org.deckfour.slickerbox.ui.SlickerComboBoxUI;
import org.processmining.framework.log.LogFilter;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.log.filter.TimestampInjectionFilter;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.framework.plugin.Provider;
import org.processmining.framework.ui.MainUI;
import org.processmining.framework.util.RuntimeUtils;
import org.processmining.mining.fuzzymining.filter.FuzzyGraphProjectionFilter;
import org.processmining.mining.fuzzymining.graph.FuzzyGraph;
import org.processmining.mining.fuzzymining.vis.ui.AnimationFrame;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 * 
 */
public class FuzzyAnimationPanel extends JPanel {

    protected FuzzyGraph graph = null;

    protected LogReader log = null;

    protected boolean discreteAnimation = false;

    protected JComboBox logSelector;

    protected NiceIntegerSlider lookaheadSlider;

    protected NiceIntegerSlider extraLookaheadSlider;

    protected AnimationFrame animationFrame = null;

    protected HashMap<String, LogReader> logMap;

    public FuzzyAnimationPanel(FuzzyGraph graph, LogReader log) {
        this.addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
                updateResources();
                logSelector.setModel(new DefaultComboBoxModel(logMap.keySet().toArray()));
            }

            public void ancestorMoved(AncestorEvent event) {
            }

            public void ancestorRemoved(AncestorEvent event) {
            }
        });
        setLog(log);
        updateResources();
        this.graph = graph;
        discreteAnimation = false;
        animationFrame = null;
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        RoundedPanel innerPanel = new RoundedPanel(20);
        innerPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(new Color(140, 140, 140));
        innerPanel.setMinimumSize(new Dimension(350, 250));
        innerPanel.setMaximumSize(new Dimension(350, 250));
        innerPanel.setPreferredSize(new Dimension(350, 250));
        final JCheckBox discreteAnimBox = new JCheckBox("discrete animation (inject timestamps)");
        discreteAnimation = false;
        if (log != null) {
            try {
                discreteAnimation = (log.getInstance(0).getAuditTrailEntryList().get(0).getTimestamp() == null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        discreteAnimBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
        discreteAnimBox.setSelected(discreteAnimation);
        discreteAnimBox.setForeground(new Color(40, 40, 40));
        discreteAnimBox.setUI(new SlickerCheckBoxUI());
        discreteAnimBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                discreteAnimation = discreteAnimBox.isSelected();
            }
        });
        JButton animButton = new AutoFocusButton("view animation");
        animButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        animButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createAnimation();
            }
        });
        logSelector = new JComboBox(logMap.keySet().toArray());
        logSelector.setUI(new SlickerComboBoxUI());
        if (log == null && logSelector.getItemCount() > 0) {
            logSelector.setSelectedIndex(0);
            setLog(logMap.get(logSelector.getSelectedItem()));
        }
        for (String logKey : logMap.keySet()) {
            if (logMap.get(logKey) == log) {
                logSelector.setSelectedItem(logKey);
                break;
            }
        }
        logSelector.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                setLog(logMap.get(logSelector.getSelectedItem()));
            }
        });
        JLabel graphLabel = new JLabel("Select Fuzzy Model to animate:");
        graphLabel.setOpaque(false);
        graphLabel.setForeground(new Color(30, 30, 30));
        graphLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JLabel logLabel = new JLabel("Select Log for animation:");
        logLabel.setOpaque(false);
        logLabel.setForeground(new Color(30, 30, 30));
        logLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lookaheadSlider = new NiceIntegerSlider("Lookahead", 1, 25, 5);
        lookaheadSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                int max = lookaheadSlider.getValue() - 1;
                extraLookaheadSlider.getSlider().setMaximum(max);
                if (extraLookaheadSlider.getValue() > max) {
                    extraLookaheadSlider.setValue(max);
                }
            }
        });
        extraLookaheadSlider = new NiceIntegerSlider("Extra lookahead", 0, 15, 3);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(logLabel);
        innerPanel.add(Box.createVerticalStrut(8));
        innerPanel.add(logSelector);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(discreteAnimBox);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(lookaheadSlider);
        innerPanel.add(Box.createVerticalStrut(8));
        innerPanel.add(extraLookaheadSlider);
        innerPanel.add(Box.createVerticalStrut(15));
        innerPanel.add(animButton);
        innerPanel.add(Box.createVerticalStrut(15));
        this.add(Box.createHorizontalGlue());
        this.add(innerPanel);
        this.add(Box.createHorizontalGlue());
    }

    public void setGraph(FuzzyGraph graph) {
        this.graph = graph;
    }

    public void setLog(LogReader log) {
        if (log != null) {
            this.log = log;
        }
    }

    protected void createAnimation() {
        if (graph == null || log == null) {
            JOptionPane.showMessageDialog(MainUI.getInstance(), "This feature needs a Fuzzy Model\n" + "and a log to work!", "Cannot create animation!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LogFilter projectionFilter = new FuzzyGraphProjectionFilter(graph);
        projectionFilter.setLowLevelFilter(log.getLogFilter());
        if (discreteAnimation == true) {
            TimestampInjectionFilter tsFilter = new TimestampInjectionFilter();
            tsFilter.setLowLevelFilter(projectionFilter);
            projectionFilter = tsFilter;
        }
        try {
            LogReader filteredLog = LogReaderFactory.createInstance(projectionFilter, log);
            animationFrame = new AnimationFrame(filteredLog, graph, lookaheadSlider.getValue(), extraLookaheadSlider.getValue());
            MainUI.getInstance().createFrame("Fuzzy Model Animation", animationFrame);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    protected void updateResources() {
        logMap = new HashMap<String, LogReader>();
        JInternalFrame[] frames = MainUI.getInstance().getDesktop().getAllFrames();
        for (JInternalFrame frame : frames) {
            if (frame instanceof Provider) {
                ProvidedObject[] providedObjects = ((Provider) frame).getProvidedObjects();
                for (ProvidedObject providedObject : providedObjects) {
                    for (Object object : providedObject.getObjects()) {
                        if (object instanceof LogReader) {
                            logMap.put(frame.getTitle() + " - " + providedObject.getName(), (LogReader) object);
                        }
                    }
                }
            }
        }
    }
}
