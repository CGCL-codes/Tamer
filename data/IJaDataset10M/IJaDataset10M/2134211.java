package org.scidac.sam.eln.serverproxy;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

/**
 *  Title: AddUserDialog Description: Dialog that allows admin to add a user
 *  Copyright: Copyright (c) 2002 Company: Battelle
 *
 * @author     JD Myers
 * @created    June 11, 2003
 * @version
 */
public class SAMAddUserDialog extends JDialog {

    JLabel mUserNameLabel = new JLabel();

    JLabel mSAMNoticeLabel = new JLabel();

    JCheckBox mAdminCheckBox = new JCheckBox();

    JTextField mUserName = new JTextField();

    JLabel mIsAdminLabel = new JLabel();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JButton OKButton = new JButton();

    JButton Cancel = new JButton();

    String mOKString = "Grant";

    String mUserSelected = null;

    int mOKRow;

    boolean mOKButton = false;

    boolean mAddAsAdmin = false;

    /**
   *  Constructor for the SAMAddUserDialog object
   *
   * @param  theFrame  Description of Parameter
   * @param  theTitle  Description of Parameter
   */
    public SAMAddUserDialog(Frame theFrame, String theTitle) {
        super(theFrame, theTitle, true);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   *  Gets the selectedUser attribute of the SAMAddUserDialog object
   *
   * @return    The selectedUser value
   */
    public String getSelectedUser() {
        return mUserSelected;
    }

    /**
   *  Gets the addAsAdmin attribute of the SAMAddUserDialog object
   *
   * @return    The addAsAdmin value
   */
    public boolean getAddAsAdmin() {
        return mAddAsAdmin;
    }

    /**
   *  Description of the Method
   *
   * @return    Description of the Returned Value
   */
    public boolean userClickedAdd() {
        return mOKButton;
    }

    /**
   *  Description of the Method
   */
    protected void setupVars() {
        OKButton.setText(mOKString);
        OKButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OKButton_actionPerformed(e);
            }
        });
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Cancel_actionPerformed(e);
            }
        });
        mUserNameLabel.setText("User Name: ");
        mIsAdminLabel.setText("Grant as Admin: ");
        mSAMNoticeLabel.setText("Note: User must already have an\n account on the SAM ELN Server\n to be granted permission to this notebook");
    }

    /**
   *  Description of the Method
   *
   * @exception  Exception  Description of Exception
   */
    protected void initComponents() throws Exception {
        this.getContentPane().add(mSAMNoticeLabel, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        this.getContentPane().add(mIsAdminLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.getContentPane().add(mAdminCheckBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        mOKRow = 3;
    }

    /**
   *  Description of the Method
   *
   * @param  e  Description of Parameter
   */
    void OKButton_actionPerformed(ActionEvent e) {
        mUserSelected = (String) mUserName.getText();
        mAddAsAdmin = mAdminCheckBox.isSelected();
        this.setVisible(false);
        mOKButton = true;
    }

    /**
   *  Description of the Method
   *
   * @param  e  Description of Parameter
   */
    void Cancel_actionPerformed(ActionEvent e) {
        mOKButton = false;
        mUserSelected = null;
        this.setVisible(false);
    }

    /**
   *  Description of the Method
   *
   * @exception  Exception  Description of Exception
   */
    private void jbInit() throws Exception {
        setupVars();
        this.getContentPane().setLayout(gridBagLayout1);
        this.getContentPane().add(mUserNameLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        this.getContentPane().add(mUserName, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        mOKRow = 1;
        initComponents();
        this.getContentPane().add(OKButton, new GridBagConstraints(0, mOKRow, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.getContentPane().add(Cancel, new GridBagConstraints(1, mOKRow, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.pack();
    }
}
