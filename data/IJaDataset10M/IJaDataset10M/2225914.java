package fr.cantor.addressbook.ui.uiv1;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import fr.cantor.addressbook.utils.LoaderPropertiesAB;

/**
 * UIProperties version 1
 * ======================
 * Defined view of limp of link
 * @author Daniel DA COSTA.
 */
public class UIProperties {

    private JButton applyButton;

    private JLabel hostLabel;

    private JTextField hostTextField;

    private JLabel serviceNameLabel;

    private JTextField serviceNameTextField;

    private JTextField portTextField;

    private JLabel portLabel;

    private JLabel titleLabel;

    private JDialog jDialog;

    private final UIAddressBook uiAB;

    public UIProperties(JDialog jDialog, UIAddressBook uiAB) {
        this.jDialog = jDialog;
        this.uiAB = uiAB;
        initComponents();
    }

    private void initComponents() {
        applyButton = new JButton();
        titleLabel = new JLabel();
        serviceNameLabel = new JLabel();
        serviceNameTextField = new JTextField();
        hostLabel = new JLabel();
        hostTextField = new JTextField();
        portLabel = new JLabel();
        portTextField = new JTextField();
        jDialog.setName("Form");
        jDialog.setResizable(false);
        applyButton.setText("Apply");
        applyButton.addActionListener(new ApplyActionProperties(this, uiAB));
        applyButton.setName("applyButton");
        titleLabel.setText("Enter connection parameter");
        titleLabel.setName("titleLabel");
        hostLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        hostLabel.setText("HOST");
        hostLabel.setName("serverHostLabel");
        hostTextField.setText(LoaderPropertiesAB.getHost());
        hostTextField.setName("serverHostTextField");
        portLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        portLabel.setText("PORT");
        portLabel.setName("serverPortjLabel");
        portTextField.setText(LoaderPropertiesAB.getPort());
        portTextField.setName("serverPortTextField");
        serviceNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        serviceNameLabel.setText("Service Name");
        serviceNameLabel.setName("serverNameLabel");
        serviceNameTextField.setText(LoaderPropertiesAB.getServiceName());
        serviceNameTextField.setName("serverNameTextField");
        GroupLayout layout = new GroupLayout(jDialog.getContentPane());
        jDialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(hostLabel, GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(hostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(portLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(portTextField, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)).addComponent(applyButton, GroupLayout.Alignment.TRAILING).addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(serviceNameLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(serviceNameTextField, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(portLabel).addComponent(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(hostLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(serviceNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(serviceNameLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(applyButton).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jDialog.pack();
    }

    public JTextField getHostTextField() {
        return hostTextField;
    }

    public JTextField getServiceNameTextField() {
        return serviceNameTextField;
    }

    public JTextField getPortTextField() {
        return portTextField;
    }

    public JDialog getJDialog() {
        return jDialog;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public JButton getApplyButton() {
        return applyButton;
    }
}
