package com.andrewj.parachute.ui;

import java.awt.event.*;
import javax.swing.*;
import com.andrewj.parachute.core.ParaPrefs;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Opens Options dialog when new object is made.
 *
 * @author Andy Jobson (andyjobson85@gmail.com) and Paul Kenny (abgorn@gmail.com)
 */
public class OptionsDialog extends JDialog {

    private ParaPrefs p;

    private JPanel pnlContent = null;

    private JPanel pnlButtons = null;

    private JButton btnOK = null;

    private JPanel pnlBottom = null;

    private JButton btnCancel = null;

    private JButton btnDefaults = null;

    private JPanel pnlTop = null;

    private JPanel pnlConn = null;

    private JPanel pnlConnCtrls = null;

    private JCheckBox chbUpnp = null;

    private JSpinner spnPort = null;

    private JPanel pnlGnrl = null;

    private JCheckBox chbUpdates = null;

    private JTextField txtfFolder = null;

    private JPanel pnlGnrlCtrls = null;

    private JPanel pnlFolder = null;

    private JButton btnBrowse = null;

    private JPanel pnlPort = null;

    private JPanel pnlPortInner = null;

    private JLabel lblRestart = null;

    public OptionsDialog(JFrame parent) {
        super(parent, true);
        p = new ParaPrefs();
        initialize();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setSize(new Dimension(335, 452));
        this.setContentPane(getPnlContent());
        this.setTitle("Options");
        this.setResizable(false);
        loadOptions();
    }

    /**
	 * Confirmation dialog whether setting should be reset to defaults.
	 */
    private boolean confirmReset() {
        return (JOptionPane.showConfirmDialog(this, "Are you sure you want to restore defaults?", "Confirmation", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION);
    }

    /**
	 * Ran when dialog opens, loads options values for input fields.
	 */
    private void loadOptions() {
        chbUpnp.setSelected(p.getUpnpPref());
        chbUpdates.setSelected(p.getUpdatePref());
        txtfFolder.setText(p.getSharedFolderPref());
        spnPort.setValue(p.getDataPortPref());
    }

    /**
	 * Saves options (changed or not) when OK button pressed.
	 */
    private void commitOptions() {
        p.setUpnpPref(chbUpnp.isSelected());
        p.setUpdatePref(chbUpdates.isSelected());
        p.setDataPortPref(Integer.parseInt(spnPort.getValue().toString()));
        p.setSharedFolderPref(txtfFolder.getText());
    }

    /**
	 * This method initializes pnlContent	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlContent() {
        if (pnlContent == null) {
            pnlContent = new JPanel();
            pnlContent.setLayout(new BorderLayout());
            pnlContent.add(getPnlBottom(), BorderLayout.SOUTH);
            pnlContent.add(getPnlTop(), BorderLayout.CENTER);
        }
        return pnlContent;
    }

    /**
	 * This method initializes pnlButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlButtons() {
        if (pnlButtons == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new Insets(5, 0, 5, 5);
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new Insets(5, 0, 5, 5);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(5, 0, 5, 5);
            pnlButtons = new JPanel();
            pnlButtons.setLayout(new GridBagLayout());
            pnlButtons.add(getBtnOK(), gridBagConstraints);
            pnlButtons.add(getBtnDefaults(), gridBagConstraints1);
            pnlButtons.add(getBtnCancel(), gridBagConstraints2);
        }
        return pnlButtons;
    }

    /**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JButton();
            btnOK.setText("OK");
            btnOK.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    commitOptions();
                    dispose();
                }
            });
        }
        return btnOK;
    }

    /**
	 * This method initializes pnlBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlBottom() {
        if (pnlBottom == null) {
            pnlBottom = new JPanel();
            pnlBottom.setLayout(new BorderLayout());
            pnlBottom.add(getPnlButtons(), BorderLayout.EAST);
        }
        return pnlBottom;
    }

    /**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton();
            btnCancel.setText("Cancel");
            btnCancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
        return btnCancel;
    }

    /**
	 * This method initializes btnDefaults	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnDefaults() {
        if (btnDefaults == null) {
            btnDefaults = new JButton();
            btnDefaults.setText("Restore Defaults");
            btnDefaults.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (confirmReset()) {
                        p.resetPrefs();
                        loadOptions();
                    }
                }
            });
        }
        return btnDefaults;
    }

    /**
	 * This method initializes pnlTop	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlTop() {
        if (pnlTop == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            gridLayout.setColumns(1);
            pnlTop = new JPanel();
            pnlTop.setLayout(gridLayout);
            pnlTop.add(getPnlConn(), null);
            pnlTop.add(getPnlGnrl(), null);
        }
        return pnlTop;
    }

    /**
	 * This method initializes pnlConn	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlConn() {
        if (pnlConn == null) {
            GridLayout gridLayout4 = new GridLayout();
            gridLayout4.setRows(1);
            pnlConn = new JPanel();
            pnlConn.setLayout(gridLayout4);
            pnlConn.setBorder(BorderFactory.createTitledBorder(null, "Connection", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            pnlConn.add(getPnlConnCtrls(), null);
        }
        return pnlConn;
    }

    /**
	 * This method initializes pnlConnCtrls	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlConnCtrls() {
        if (pnlConnCtrls == null) {
            lblRestart = new JLabel();
            lblRestart.setText("<html>Changes to these options will take effect after Parachute is restarted</html>");
            GridLayout gridLayout3 = new GridLayout();
            gridLayout3.setRows(3);
            gridLayout3.setColumns(1);
            pnlConnCtrls = new JPanel();
            pnlConnCtrls.setLayout(gridLayout3);
            pnlConnCtrls.add(getChbUpnp(), null);
            pnlConnCtrls.add(getPnlPort(), null);
            pnlConnCtrls.add(lblRestart, null);
        }
        return pnlConnCtrls;
    }

    /**
	 * This method initializes chbUpnp	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getChbUpnp() {
        if (chbUpnp == null) {
            chbUpnp = new JCheckBox();
            chbUpnp.setText("UPnP automatic port mapping");
        }
        return chbUpnp;
    }

    /**
	 * This method initializes spnPort	
	 * 	
	 * @return javax.swing.JSpinner	
	 */
    private JSpinner getSpnPort() {
        if (spnPort == null) {
            spnPort = new JSpinner(new SpinnerNumberModel(p.getDataPortPref(), 1, 65535, 1));
            spnPort.setEditor(new JSpinner.NumberEditor(spnPort, "#####"));
        }
        return spnPort;
    }

    /**
	 * This method initializes pnlGnrl	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlGnrl() {
        if (pnlGnrl == null) {
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(1);
            pnlGnrl = new JPanel();
            pnlGnrl.setLayout(gridLayout2);
            pnlGnrl.setBorder(BorderFactory.createTitledBorder(null, "General", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            pnlGnrl.add(getPnlGnrlCtrls(), null);
        }
        return pnlGnrl;
    }

    /**
	 * This method initializes chbUpdates	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getChbUpdates() {
        if (chbUpdates == null) {
            chbUpdates = new JCheckBox();
            chbUpdates.setText("Check for updates on startup");
        }
        return chbUpdates;
    }

    /**
	 * This method initializes txtfFolder	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTxtfFolder() {
        if (txtfFolder == null) {
            txtfFolder = new JTextField();
            txtfFolder.setText("some text");
            txtfFolder.setEditable(false);
        }
        return txtfFolder;
    }

    /**
	 * This method initializes pnlGnrlCtrls	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlGnrlCtrls() {
        if (pnlGnrlCtrls == null) {
            pnlGnrlCtrls = new JPanel();
            pnlGnrlCtrls.setLayout(new BorderLayout());
            pnlGnrlCtrls.add(getChbUpdates(), BorderLayout.NORTH);
            pnlGnrlCtrls.add(getPnlFolder(), BorderLayout.CENTER);
        }
        return pnlGnrlCtrls;
    }

    /**
	 * This method initializes pnlFolder	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlFolder() {
        if (pnlFolder == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new Insets(0, 5, 0, 5);
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints6.weightx = 1.0;
            pnlFolder = new JPanel();
            pnlFolder.setLayout(new GridBagLayout());
            pnlFolder.setBorder(BorderFactory.createTitledBorder(null, "Default shared folder for new connections", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            pnlFolder.add(getTxtfFolder(), gridBagConstraints6);
            pnlFolder.add(getBtnBrowse(), gridBagConstraints7);
        }
        return pnlFolder;
    }

    /**
	 * This method initializes btnBrowse	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnBrowse() {
        if (btnBrowse == null) {
            btnBrowse = new JButton();
            btnBrowse.setText("Browse...");
            btnBrowse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JFileChooser f = new JFileChooser();
                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (f.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        txtfFolder.setText(f.getSelectedFile().toString());
                        txtfFolder.setToolTipText(txtfFolder.getText());
                    }
                }
            });
        }
        return btnBrowse;
    }

    /**
	 * This method initializes pnlPort	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlPort() {
        if (pnlPort == null) {
            pnlPort = new JPanel();
            pnlPort.setLayout(new BorderLayout());
            pnlPort.setBorder(BorderFactory.createTitledBorder(null, "Data port", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            pnlPort.add(getPnlPortInner(), BorderLayout.WEST);
        }
        return pnlPort;
    }

    /**
	 * This method initializes pnlPortInner	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPnlPortInner() {
        if (pnlPortInner == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.ipadx = 50;
            gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
            pnlPortInner = new JPanel();
            pnlPortInner.setLayout(new GridBagLayout());
            pnlPortInner.add(getSpnPort(), gridBagConstraints3);
        }
        return pnlPortInner;
    }
}
