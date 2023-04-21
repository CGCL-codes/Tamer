package visitpc.srcclient.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import visitpc.srcclient.GenericSrcClientConfig;
import javax.swing.*;
import visitpc.lib.gui.UI;
import visitpc.lib.gui.RowPane;
import visitpc.lib.gui.LongNumberField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import visitpc.lib.gui.*;

public class SrcClientConnectionDialog extends VisitPCJDialog implements ActionListener {

    private JButton okButton;

    private JButton cancelButton;

    private JLabel connectionNameLabel;

    private JTextField connectionNameField;

    private JLabel pcNameLabel = new JLabel("PC name");

    private JTextField pcNameField = new JTextField();

    private JLabel srcServerPortLabel;

    private LongNumberField srcServerPortField;

    private JLabel destAddressLabel;

    private JTextField destAddressField;

    private JLabel destPortLabel;

    private LongNumberField destPortField;

    private boolean okSelected;

    public SrcClientConnectionDialog(Frame parentFrame, String title) {
        super(parentFrame, title, true);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        connectionNameLabel = new JLabel("Connection name");
        connectionNameField = new JTextField();
        srcServerPortLabel = new JLabel("Source server port");
        srcServerPortField = new LongNumberField(10, 1, 65535);
        destAddressLabel = new JLabel("Destination address");
        destAddressField = new JTextField();
        destPortLabel = new JLabel("Destination server port");
        destPortField = new LongNumberField(10, 1, 65535);
        RowPane rowPane = new RowPane();
        rowPane.add(connectionNameLabel, connectionNameField);
        rowPane.add(pcNameLabel, pcNameField);
        srcServerPortField.setNumber(10000);
        rowPane.add(srcServerPortLabel, srcServerPortField);
        rowPane.add(destAddressLabel, destAddressField);
        rowPane.add(destPortLabel, destPortField);
        destPortField.setNumber(22);
        getContentPane().add(rowPane);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        UI.CenterOnScreen(this);
        pack();
    }

    public boolean isOkSelected() {
        return okSelected;
    }

    public void setConfig(GenericSrcClientConfig genericSrcClientConfig) {
        connectionNameField.setText(genericSrcClientConfig.configName);
        pcNameField.setText(genericSrcClientConfig.pcName);
        srcServerPortField.setNumber(genericSrcClientConfig.localListenPort);
        destAddressField.setText(genericSrcClientConfig.destAddress);
        destPortField.setNumber(genericSrcClientConfig.destPort);
    }

    public GenericSrcClientConfig getConfig(GenericSrcClientConfig genericSrcClientConfig) {
        genericSrcClientConfig.configName = connectionNameField.getText();
        genericSrcClientConfig.pcName = pcNameField.getText();
        genericSrcClientConfig.localListenPort = (int) srcServerPortField.getNumber();
        genericSrcClientConfig.destAddress = destAddressField.getText();
        genericSrcClientConfig.destPort = (int) destPortField.getNumber();
        return genericSrcClientConfig;
    }

    /**
   * Return true if input is valid
   */
    public boolean isInputValid() {
        if (connectionNameField.getText() != null && connectionNameField.getText().length() > 0 && srcServerPortField.getNumber() > 0 && srcServerPortField.getNumber() < 65536 && destAddressField.getText() != null && destAddressField.getText().length() > 0 && destPortField.getNumber() > 0 && destPortField.getNumber() < 65536) {
            return true;
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            okSelected = true;
        } else {
            okSelected = false;
        }
        setVisible(false);
    }
}
