package org.tolven.passwordrecovery.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 *
 * @author  Joseph Isaac
 */
public class PropertyEditorDialog extends JDialog {

    public static final int OK_STATUS = 0;

    public static final int CANCEL_STATUS = 1;

    private int status = CANCEL_STATUS;

    public PropertyEditorDialog(java.awt.Frame frame, String title, String propertyName, boolean propertyNameEditable, String propertyValue, boolean propertyValueEditable) {
        super(frame, title, true);
        initComponents();
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int) screenDimension.getWidth() - this.getWidth()) / 2, ((int) screenDimension.getHeight() - this.getHeight()) / 2);
        propertyNameTextField.setEditable(propertyNameEditable);
        propertyValueTextField.setEditable(propertyValueEditable);
        setPropertyName(propertyName);
        setPropertyValue(propertyValue);
        propertyValueTextField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                enter();
            }
        });
        propertyValueTextField.selectAll();
        if (propertyNameTextField.isEditable()) {
            propertyNameTextField.requestFocus();
        } else {
            propertyValueTextField.requestFocus();
        }
    }

    public String getPropertyName() {
        return propertyNameTextField.getText();
    }

    public void setPropertyName(String propertyName) {
        propertyNameTextField.setText(propertyName);
    }

    public String getPropertyNameLabel() {
        return propertyLabel.getText();
    }

    public void setPropertyNameLabel(String propertyNameLabel) {
        propertyLabel.setText(propertyNameLabel);
    }

    public String getPropertyValue() {
        return propertyValueTextField.getText();
    }

    public void setPropertyValueLabel(String propertyValueLabel) {
        valueLabel.setText(propertyValueLabel);
    }

    public String getPropertyValueLabel() {
        return valueLabel.getText();
    }

    public void setPropertyValue(String propertyValue) {
        propertyValueTextField.setText(propertyValue);
    }

    public int getStatus() {
        return status;
    }

    private void setStatus(int status) {
        this.status = status;
    }

    private void enter() {
        setStatus(OK_STATUS);
        setVisible(false);
    }

    private void cancel() {
        setStatus(CANCEL_STATUS);
        setVisible(false);
    }

    private void initComponents() {
        propertyLabel = new javax.swing.JLabel();
        propertyNameTextField = new javax.swing.JTextField();
        valueLabel = new javax.swing.JLabel();
        propertyValueTextField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        enterButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        propertyLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        propertyLabel.setText("Property:");
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valueLabel.setText("Value:");
        enterButton.setText("Enter");
        enterButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(412, Short.MAX_VALUE).add(enterButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancelButton)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cancelButton).add(enterButton)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(propertyLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(propertyNameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(valueLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(propertyValueTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.linkSize(new java.awt.Component[] { propertyLabel, valueLabel }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(propertyNameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(propertyLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(valueLabel).add(propertyValueTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        cancel();
    }

    private void enterButtonActionPerformed(java.awt.event.ActionEvent evt) {
        enter();
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton enterButton;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel propertyLabel;

    private javax.swing.JTextField propertyNameTextField;

    private javax.swing.JTextField propertyValueTextField;

    private javax.swing.JLabel valueLabel;
}
