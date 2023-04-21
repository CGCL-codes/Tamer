package org.yawlfoundation.yawl.editor.swing.element;

import org.yawlfoundation.yawl.editor.elements.model.YAWLCondition;
import org.yawlfoundation.yawl.editor.elements.model.YAWLVertex;
import org.yawlfoundation.yawl.editor.net.NetGraph;
import org.yawlfoundation.yawl.editor.specification.SpecificationUndoManager;
import org.yawlfoundation.yawl.editor.swing.JFormattedSafeXMLCharacterField;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LabelElementDialog extends AbstractVertexDoneDialog {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    protected JFormattedSafeXMLCharacterField labelField;

    protected JCheckBox cbxSynch;

    public LabelElementDialog() {
        super(null, true, true);
        setContentPanel(getLabelPanel());
        getDoneButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                YAWLVertex vertex = getVertex();
                String newLabel = labelField.getText();
                if (newLabel.length() == 0) newLabel = null;
                graph.setElementLabel(vertex, newLabel);
                if (cbxSynch.isSelected()) {
                    vertex.setEngineLabel(newLabel);
                }
                graph.clearSelection();
                SpecificationUndoManager.getInstance().setDirty(true);
            }
        });
        getRootPane().setDefaultButton(getDoneButton());
        labelField.requestFocus();
    }

    private JPanel getLabelPanel() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(gbl);
        panel.setBorder(new EmptyBorder(12, 12, 0, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel("Set label to:");
        label.setDisplayedMnemonic('S');
        panel.add(label, gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        labelField = getLabelField();
        label.setLabelFor(labelField);
        panel.add(labelField, gbc);
        gbc.gridx--;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        cbxSynch = new JCheckBox("Synchronise task name with label");
        cbxSynch.setSelected(true);
        panel.add(cbxSynch, gbc);
        pack();
        return panel;
    }

    private JFormattedSafeXMLCharacterField getLabelField() {
        labelField = new JFormattedSafeXMLCharacterField(15);
        labelField.setToolTipText(" Enter a label to go under this net element. ");
        labelField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getDoneButton().doClick();
            }
        });
        return labelField;
    }

    public void setVertex(YAWLVertex vertex, NetGraph graph) {
        super.setVertex(vertex, graph);
        labelField.setText(vertex.getLabel());
        String vType = (getVertex() instanceof YAWLCondition) ? "condition" : "task";
        cbxSynch.setText("Synchronise " + vType + " name with label");
    }

    public String getTitlePrefix() {
        return "Label ";
    }
}
