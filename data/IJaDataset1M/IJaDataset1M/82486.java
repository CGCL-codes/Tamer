package org.yawlfoundation.yawl.editor.swing.element;

import org.yawlfoundation.yawl.editor.YAWLEditor;
import org.yawlfoundation.yawl.editor.elements.model.YAWLCompositeTask;
import org.yawlfoundation.yawl.editor.elements.model.YAWLVertex;
import org.yawlfoundation.yawl.editor.net.NetGraph;
import org.yawlfoundation.yawl.editor.net.NetGraphModel;
import org.yawlfoundation.yawl.editor.specification.SpecificationModel;
import org.yawlfoundation.yawl.editor.specification.SpecificationUtilities;
import org.yawlfoundation.yawl.editor.swing.AbstractDoneDialog;
import org.yawlfoundation.yawl.editor.swing.JUtilities;
import org.yawlfoundation.yawl.editor.swing.YAWLEditorDesktop;
import org.yawlfoundation.yawl.editor.swing.data.NetDecompositionUpdateDialog;
import org.yawlfoundation.yawl.editor.swing.net.YAWLEditorNetPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Set;

public class SelectUnfoldingNetDialog extends AbstractDoneDialog {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    protected NetGraph graph;

    protected YAWLCompositeTask task;

    protected JComboBox netComboBox;

    public SelectUnfoldingNetDialog() {
        super("Composite Task Unfolding", true);
        setContentPanel(getUnfoldingNetPanel());
        getDoneButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NetGraphModel netModel = SpecificationUtilities.getNetModelFromName(SpecificationModel.getInstance(), (String) netComboBox.getSelectedItem());
                if (netModel != null) {
                    graph.setUnfoldingNet(task, netModel.getGraph());
                } else {
                    graph.setUnfoldingNet(task, null);
                }
            }
        });
    }

    protected void makeLastAdjustments() {
        pack();
        setResizable(false);
    }

    public void setVisible(boolean state) {
        if (state == true) {
            JUtilities.centreWindowUnderVertex(graph, this, (YAWLVertex) task, 10);
        }
        super.setVisible(state);
    }

    private JPanel getUnfoldingNetPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(gbl);
        panel.setBorder(new EmptyBorder(12, 12, 0, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel("This task unfolds to the net:");
        label.setDisplayedMnemonic('u');
        panel.add(label, gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        netComboBox = new JComboBox();
        label.setLabelFor(netComboBox);
        panel.add(netComboBox, gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(getCreateButton(), gbc);
        return panel;
    }

    public void setTask(NetGraph graph, YAWLCompositeTask task) {
        this.graph = graph;
        this.task = task;
        populateComboBox();
    }

    private void populateComboBox() {
        netComboBox.setEnabled(false);
        netComboBox.removeAllItems();
        netComboBox.addItem(null);
        Set netSet = SpecificationModel.getInstance().getNets();
        Object[] netSetArray = netSet.toArray();
        String[] netSetNames = new String[netSet.size()];
        for (int i = 0; i < netSet.size(); i++) {
            netSetNames[i] = ((NetGraphModel) netSetArray[i]).getName();
        }
        try {
            Arrays.sort(netSetNames);
        } catch (Exception e) {
        }
        ;
        for (int i = 0; i < netSetNames.length; i++) {
            netComboBox.addItem(netSetNames[i]);
            if (task.getUnfoldingNetName() != null && task.getUnfoldingNetName().equals(netSetNames[i])) {
                netComboBox.setSelectedItem(netSetNames[i]);
            }
        }
        netComboBox.setEnabled(true);
        pack();
    }

    private JButton getCreateButton() {
        JButton button = new JButton("Create...");
        button.setMnemonic(KeyEvent.VK_C);
        final AbstractDoneDialog dialog = this;
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                YAWLEditorNetPanel newFrame = YAWLEditorDesktop.getInstance().newNet();
                NetDecompositionUpdateDialog netDialog = new NetDecompositionUpdateDialog(newFrame.getNet().getNetModel().getDecomposition());
                dialog.setVisible(false);
                netDialog.setLocationRelativeTo(YAWLEditor.getInstance());
                netDialog.setModal(true);
                netDialog.setVisible(true);
                graph.setUnfoldingNet(task, newFrame.getNet());
            }
        });
        return button;
    }
}
