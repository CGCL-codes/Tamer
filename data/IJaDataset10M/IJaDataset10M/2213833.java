package at.ac.tuwien.ifs.alviz.smallworld.control;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import org.grlea.log.SimpleLogger;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefusex.controls.AnchorUpdateControl;
import at.ac.tuwien.ifs.alviz.align.api.AlVizSimilarityClass;
import at.ac.tuwien.ifs.alviz.align.api.AlVizSimilarityRule;
import at.ac.tuwien.ifs.alviz.align.data.AlignmentData;
import at.ac.tuwien.ifs.alviz.smallworld.types.AlVizCluster;
import at.ac.tuwien.ifs.alviz.smallworld.types.Cluster;
import at.ac.tuwien.ifs.alviz.AlViz;
import at.ac.tuwien.ifs.alviz.AlVizClsesPanel;

/**
 * Always updates the anchor position, even if it is over
 * an item.  Constrast with AnchorUpdateControl.
 *
 * @author Stephen
 */
public class AnchorAlwaysUpdateControl extends AnchorUpdateControl implements ActionListener {

    private AlVizCluster lastClicked = null;

    private AlignmentData alignmentData = null;

    private static final SimpleLogger log = new SimpleLogger(AnchorAlwaysUpdateControl.class);

    public AnchorAlwaysUpdateControl(Layout layout) {
        super(layout, null);
    }

    public AnchorAlwaysUpdateControl(Layout layout, Activity update) {
        super(new Layout[] { layout }, update);
    }

    public AnchorAlwaysUpdateControl(Layout[] layout, Activity update) {
        super(layout, update);
    }

    public void itemMoved(VisualItem gi, MouseEvent e) {
        super.moveEvent(e);
    }

    public void itemClicked(VisualItem gi, MouseEvent e) {
        if (gi != null) {
            if (gi.getEntity() instanceof AlVizCluster) {
                AlVizCluster c = (AlVizCluster) gi.getEntity();
                this.lastClicked = c;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (this.clsesPanel.getViewMode() != AlVizClsesPanel.ViewMode.SIMILARITY) {
                        return;
                    } else if (this.clsesPanel.getLastSelectedInOtherPanel() == null) {
                        return;
                    }
                    if (this.clsesPanel.isOrigin()) {
                    } else {
                        if (this.alignmentData == null) return;
                        JPopupMenu popup = null;
                        if (AlViz.getAlignMode() == AlViz.AlignmentMode.CONFIDENCE) popup = new JPopupMenu("Set confidence value"); else popup = new JPopupMenu("Set rule-based-similarity value");
                        AlVizCluster.Similarity sim = this.clsesPanel.getSimilarityToNodeFromOtherPanel(c);
                        JMenuItem menuItem;
                        if (sim != null) {
                            AlVizSimilarityRule alVizSimRule = sim.getAlVizSimRule();
                            menuItem = new JMenuItem(c.getLabel() + " is " + alVizSimRule.getSimilarityClass().getName() + " (" + alVizSimRule.getShortDescription() + ", " + sim.getValue() + ")");
                        } else if (c.hasChildren()) {
                            menuItem = new JMenuItem("[Set of nodes]");
                        } else {
                            menuItem = new JMenuItem(c.getLabel() + " not related");
                        }
                        menuItem.setActionCommand("sim_edit");
                        menuItem.addActionListener(this);
                        popup.add(menuItem);
                        popup.addSeparator();
                        if (AlViz.getAlignMode() == AlViz.AlignmentMode.CONFIDENCE) {
                            menuItem = new JMenuItem(AlignmentData.DEFAULT_SIM_RULE_SIMILAR.getSimilarityClass().getIdentifier() + " (" + AlignmentData.DEFAULT_SIM_RULE_SIMILAR.getSimilarityClass().getName() + ")");
                            menuItem.setActionCommand("sim_" + AlignmentData.DEFAULT_SIM_RULE_SIMILAR.getSimilarityClass().getName());
                            menuItem.addActionListener(this);
                            popup.add(menuItem);
                            menuItem = new JMenuItem(AlignmentData.DEFAULT_SIM_RULE_UNRELATED.getSimilarityClass().getIdentifier() + " (" + AlignmentData.DEFAULT_SIM_RULE_UNRELATED.getSimilarityClass().getName() + ")");
                            menuItem.setActionCommand("sim_" + AlignmentData.DEFAULT_SIM_RULE_UNRELATED.getSimilarityClass().getName());
                            menuItem.addActionListener(this);
                            popup.add(menuItem);
                        } else {
                            for (AlVizSimilarityClass alVizSimClass : alignmentData.getAlVizClassSortedSimRules().keySet()) {
                                menuItem = new JMenuItem(alVizSimClass.getIdentifier() + " (" + alVizSimClass.getName() + ")");
                                menuItem.setActionCommand("sim_" + alVizSimClass.getName());
                                menuItem.addActionListener(this);
                                popup.add(menuItem);
                            }
                        }
                        popup.show(this.parent, e.getX(), e.getY());
                        log.info("Graph" + this.clsesPanel.getId() + "|ShowMapping|" + this.clsesPanel.getLastSelectedInOtherPanel().getURI() + "|" + c.getURI());
                    }
                } else {
                    boolean reset = true;
                    if ((e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK) {
                        reset = false;
                    }
                    this.clsesPanel.nodeSelected(c, true);
                }
            }
        }
    }

    private AlVizClsesPanel clsesPanel = null;

    private JComponent parent = null;

    public void setAlVizClsesPanel(AlVizClsesPanel clsesPanel) {
        this.clsesPanel = clsesPanel;
    }

    public void setParent(JComponent parent) {
        this.parent = parent;
    }

    public static void main(String[] args) {
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if (command.length() > 4 && command.substring(0, 4).equals("sim_")) {
            if (this.alignmentData == null) return;
            Collection<AlVizSimilarityRule> alVizSimRules = null;
            AlVizSimilarityRule alVizSimRule = null;
            double value = 1.0;
            String relation = "[ERROR]";
            boolean showDialog = true;
            if (command.equals("sim_edit")) {
                if (this.lastClicked.hasChildren()) return;
                AlVizCluster.Similarity sim = this.clsesPanel.getSimilarityToNodeFromOtherPanel(this.lastClicked);
                if (sim == null) return;
                alVizSimRule = sim.getAlVizSimRule();
                AlVizSimilarityClass alVizSimClass = alVizSimRule.getSimilarityClass();
                alVizSimRules = alignmentData.getAlVizClassSortedSimRules().get(alVizSimClass);
                value = sim.getValue();
                relation = alVizSimClass.getName();
                log.info("Graph" + this.clsesPanel.getId() + "|ShowSimilarityDialog|" + this.clsesPanel.getLastSelectedInOtherPanel().getURI() + "|" + this.lastClicked.getURI() + "|" + alVizSimClass.getName() + "|" + alVizSimRule + "|" + value);
            } else {
                String simClassName = command.substring(4);
                for (AlVizSimilarityClass alVizSimClass : alignmentData.getAlVizClassSortedSimRules().keySet()) {
                    if (alVizSimClass.getName().equals(simClassName)) {
                        alVizSimRules = alignmentData.getAlVizClassSortedSimRules().get(alVizSimClass);
                        alVizSimRule = alVizSimRules.iterator().next();
                        relation = alVizSimClass.getName();
                    }
                }
            }
            if (showDialog) {
                JPanel panel = new JPanel(new BorderLayout());
                JPanel iPanelL = null;
                JPanel iPanelR = null;
                JComboBox jcb_rule = null;
                if (AlViz.getAlignMode() == AlViz.AlignmentMode.RULE_BASED_SIMILARITY) {
                    panel.add(new JLabel("Set rule-based-similarity of " + this.lastClicked.getLabel() + " to \"" + relation + "\"?"), BorderLayout.NORTH);
                    iPanelL = new JPanel(new GridLayout(2, 1, 2, 2));
                    iPanelL.add(new JLabel("Rule:"));
                    iPanelR = new JPanel(new GridLayout(2, 1, 2, 2));
                    jcb_rule = new JComboBox(alVizSimRules.toArray());
                    jcb_rule.setSelectedItem(alVizSimRule);
                    iPanelR.add(jcb_rule);
                } else {
                    panel.add(new JLabel("Set confidence of " + this.lastClicked.getLabel() + " to \"" + relation + "\"?"), BorderLayout.NORTH);
                    iPanelL = new JPanel(new GridLayout(1, 1, 2, 2));
                    iPanelR = new JPanel(new GridLayout(1, 1, 2, 2));
                    alVizSimRules = new LinkedList<AlVizSimilarityRule>();
                    if (alVizSimRule.getSimilarityClass().equals(AlVizSimilarityClass.UNRELATED)) alVizSimRules.add(AlignmentData.DEFAULT_SIM_RULE_UNRELATED); else alVizSimRules.add(AlignmentData.DEFAULT_SIM_RULE_SIMILAR);
                    jcb_rule = new JComboBox(alVizSimRules.toArray());
                    jcb_rule.setSelectedIndex(0);
                }
                iPanelL.add(new JLabel("Value:"));
                SpinnerNumberModel snm = new SpinnerNumberModel((double) Math.round(value * 100.0) / 100.0, 0.01, 1.0, 0.01);
                JSpinner spinner = new JSpinner(snm);
                iPanelR.add(spinner);
                if (!alVizSimRule.getSimilarityClass().equals(AlVizSimilarityClass.UNRELATED)) {
                    panel.add(iPanelL, BorderLayout.WEST);
                    panel.add(iPanelR, BorderLayout.CENTER);
                }
                int answer = JOptionPane.showConfirmDialog(null, panel, (AlViz.getAlignMode() == AlViz.AlignmentMode.RULE_BASED_SIMILARITY) ? "Specify rule-based-similarity" : "Specify confidence", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer != JOptionPane.OK_OPTION) {
                    log.info("Graph" + this.clsesPanel.getId() + "|CancelSimilarity|" + this.clsesPanel.getLastSelectedInOtherPanel().getURI() + "|" + this.lastClicked.getURI());
                    return;
                }
                alVizSimRule = (AlVizSimilarityRule) jcb_rule.getSelectedItem();
                value = (Double) snm.getValue();
            }
            log.info("Graph" + this.clsesPanel.getId() + "|ChangeSimilarity|" + this.clsesPanel.getLastSelectedInOtherPanel().getURI() + "|" + this.lastClicked.getURI() + "|" + alVizSimRule.getClass().getName() + "|" + alVizSimRule.getId() + "|" + value);
            this.clsesPanel.updateSimilarityToNodeFromOtherPanel(this.clsesPanel.getOtherClsesPanel().getLastSelected(), this.lastClicked, alVizSimRule, value);
            if (this.clsesPanel.getOtherClsesPanel().getLastSelected() != null) {
                if (this.lastClicked.hasChildren()) {
                    for (AlVizCluster child : this.getAllChildren(this.lastClicked)) {
                        this.clsesPanel.updateSimilarityToNode(child, this.clsesPanel.getOtherClsesPanel().getLastSelected(), alVizSimRule, value);
                    }
                } else {
                    this.clsesPanel.updateSimilarityToNode(this.lastClicked, this.clsesPanel.getOtherClsesPanel().getLastSelected(), alVizSimRule, value);
                }
            }
        }
    }

    private List<AlVizCluster> getAllChildren(AlVizCluster c) {
        List<AlVizCluster> result = new LinkedList<AlVizCluster>();
        for (Iterator i = c.getChildren(); i.hasNext(); ) {
            AlVizCluster child = (AlVizCluster) i.next();
            if (!child.hasChildren()) {
                result.add(child);
            } else {
                result.addAll(getAllChildren(child));
            }
        }
        return result;
    }

    public AlignmentData getAlignmentData() {
        return alignmentData;
    }

    public void setAlignmentData(AlignmentData alignmentData) {
        this.alignmentData = alignmentData;
    }
}
