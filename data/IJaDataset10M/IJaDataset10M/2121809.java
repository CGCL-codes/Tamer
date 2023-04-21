package edu.indiana.extreme.xbaya.graph.system.gui;

import edu.indiana.extreme.xbaya.XBayaEngine;
import edu.indiana.extreme.xbaya.graph.GraphException;
import edu.indiana.extreme.xbaya.graph.system.ReceiveNode;
import edu.indiana.extreme.xbaya.gui.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Satoshi Shirasuna
 */
public class ReceiveConfigurationDialog {

    private XBayaEngine engine;

    private ReceiveNode node;

    private XBayaDialog dialog;

    private XBayaTextField nameTextField;

    private XBayaTextField idTextField;

    private JSpinner numPorts;

    /**
     * Constructs an InputConfigurationWindow.
     * 
     * @param node
     * @param engine
     */
    public ReceiveConfigurationDialog(ReceiveNode node, XBayaEngine engine) {
        this.engine = engine;
        this.node = node;
        initGui();
    }

    /**
     * Shows the dialog.
     */
    @SuppressWarnings("boxing")
    public void show() {
        String name = this.node.getName();
        this.nameTextField.setText(name);
        this.idTextField.setText(this.node.getID());
        int number = this.node.getOutputPorts().size();
        this.numPorts.setValue(number);
        this.dialog.show();
    }

    /**
     * Hides the dialog.
     */
    private void hide() {
        this.dialog.hide();
    }

    private void setInput() {
        Integer value = (Integer) this.numPorts.getValue();
        int number = value.intValue();
        int current = this.node.getOutputPorts().size();
        try {
            if (number > current) {
                for (int i = 0; i < number - current; i++) {
                    this.node.addOutputPort();
                }
            } else if (number < current) {
                for (int i = 0; i < current - number; i++) {
                    this.node.removeOutputPort();
                }
            } else {
            }
        } catch (GraphException e) {
            this.engine.getErrorWindow().error(ErrorMessages.UNEXPECTED_ERROR, e);
        }
        hide();
        this.engine.getGUI().getGraphCanvas().repaint();
    }

    /**
     * Initializes the GUI.
     */
    private void initGui() {
        this.nameTextField = new XBayaTextField();
        this.nameTextField.setEditable(false);
        XBayaLabel nameLabel = new XBayaLabel("Name", this.nameTextField);
        this.idTextField = new XBayaTextField();
        this.idTextField.setEditable(false);
        XBayaLabel idLabel = new XBayaLabel("ID", this.idTextField);
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        this.numPorts = new JSpinner(model);
        XBayaLabel numPortLabel = new XBayaLabel("Number of Parameters", this.numPorts);
        GridPanel gridPanel = new GridPanel();
        gridPanel.add(nameLabel);
        gridPanel.add(this.nameTextField);
        gridPanel.add(idLabel);
        gridPanel.add(this.idTextField);
        gridPanel.add(numPortLabel);
        gridPanel.add(this.numPorts);
        gridPanel.layout(3, 2, GridPanel.WEIGHT_NONE, 1);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                setInput();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        this.dialog = new XBayaDialog(this.engine, "Receive Configuration", gridPanel, buttonPanel);
        this.dialog.setDefaultButton(okButton);
    }
}
