package de.iph.arbeitsgruppenassistent.client.resourcemanagement.utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * 
 * 
 * @author Andreas Bruns
 */
public class DlgEditNameAndDesc extends JDialog {

    private static final long serialVersionUID = 1L;

    private boolean isConfirmed;

    private String name;

    private String desc;

    private JTextField txtDescription;

    private JTextField txtName;

    private JButton btnOk;

    private JButton btnCancel;

    private JLabel lblName;

    private JLabel lblDescription;

    public DlgEditNameAndDesc(String title) {
        isConfirmed = false;
        this.setTitle(title);
        initGUI();
        setLocationRelativeTo(this.getParent());
    }

    public DlgEditNameAndDesc(String name, String description, String title) {
        isConfirmed = false;
        this.setTitle(title);
        initGUI();
        setLocationRelativeTo(this.getParent());
        txtName.setText(name);
        txtDescription.setText(description);
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setName("dlgEditNameAndDesc");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setModal(true);
            {
                txtDescription = new JTextField();
            }
            {
                txtName = new JTextField();
            }
            {
                lblDescription = new JLabel();
                lblDescription.setText("Beschreibung");
                lblDescription.setLayout(null);
            }
            {
                lblName = new JLabel();
                GroupLayout lblNameLayout = new GroupLayout((JComponent) lblName);
                btnCancel = new JButton();
                btnCancel.setText("Abbrechen");
                btnCancel.setMnemonic(java.awt.event.KeyEvent.VK_A);
                btnCancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        btnCancelActionPerformed(evt);
                    }
                });
                lblName.setText("Name");
                lblName.setLayout(lblNameLayout);
                lblNameLayout.setVerticalGroup(lblNameLayout.createSequentialGroup());
                lblNameLayout.setHorizontalGroup(lblNameLayout.createSequentialGroup());
            }
            {
                btnOk = new JButton();
                GroupLayout btnOkLayout = new GroupLayout((JComponent) btnOk);
                btnOk.setLayout(btnOkLayout);
                btnOk.setText("Ok");
                btnOk.setMnemonic(java.awt.event.KeyEvent.VK_O);
                btnOk.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        btnOkActionPerformed(evt);
                    }
                });
                btnOkLayout.setVerticalGroup(btnOkLayout.createSequentialGroup());
                btnOkLayout.setHorizontalGroup(btnOkLayout.createSequentialGroup());
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap().add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, lblName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, txtDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, lblDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.UNRELATED).add(thisLayout.createParallelGroup(GroupLayout.BASELINE).add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.BASELINE, btnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, thisLayout.createSequentialGroup().add(btnCancel, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE).add(0, 51, Short.MAX_VALUE).add(btnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).add(thisLayout.createSequentialGroup().add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, lblDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(GroupLayout.LEADING, lblName, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(thisLayout.createParallelGroup().add(GroupLayout.LEADING, txtDescription, 0, 181, Short.MAX_VALUE).add(GroupLayout.LEADING, txtName, 0, 181, Short.MAX_VALUE)))).addContainerGap());
            thisLayout.linkSize(new Component[] { btnCancel, btnOk }, GroupLayout.HORIZONTAL);
            {
            }
            {
                this.setSize(309, 129);
                getRootPane().setDefaultButton(btnOk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnOkActionPerformed(ActionEvent evt) {
        isConfirmed = false;
        while (true) {
            if (txtName.getText() == null || txtName.getText().equals("")) {
                CmnFunctions.inputFailureDialog(this, lblName.getText(), "Wert darf nicht leer sein.");
                break;
            } else {
                name = txtName.getText();
            }
            desc = txtDescription.getText();
            isConfirmed = true;
            setVisible(false);
            break;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        isConfirmed = false;
        setVisible(false);
    }
}
