package jresearchtool.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jresearchtool.control.NewProjectControl;
import jresearchtool.model.NewProjectModel;

/**
 * View for the New Project dialog.
 * 
 * @author Anton Wolf
 */
public class NewProjectDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private NewProjectControl control;

    private NewProjectModel model;

    private JLabel projectNameLabel = null;

    private JTextField projectNameTextField = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JPanel fillerPanel = null;

    private JPanel buttonPanel = null;

    /**
	 * @param owner
	 */
    public NewProjectDialog(Frame owner) {
        super(owner);
        initialize();
    }

    public NewProjectDialog(Frame owner, NewProjectControl control, NewProjectModel model, boolean modal) {
        super(owner, modal);
        this.control = control;
        this.model = model;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 200);
        this.setTitle(Messages.getString("NewProjectDialog.title"));
        this.setContentPane(getJContentPane());
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                control.discardDialog();
            }
        });
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridwidth = 2;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 3;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridwidth = 2;
            gridBagConstraints4.weighty = 1.0D;
            gridBagConstraints4.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.gridy = 0;
            projectNameLabel = new JLabel();
            projectNameLabel.setText(Messages.getString("NewProjectDialog.projectNameLabel"));
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(projectNameLabel, gridBagConstraints);
            jContentPane.add(getProjectNameTextField(), gridBagConstraints1);
            jContentPane.add(getFillerPanel(), gridBagConstraints4);
            jContentPane.add(getButtonPanel(), gridBagConstraints5);
        }
        return jContentPane;
    }

    /**
	 * This method initializes projectNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getProjectNameTextField() {
        if (projectNameTextField == null) {
            projectNameTextField = new JTextField();
            projectNameTextField.setPreferredSize(new Dimension(200, 20));
            projectNameTextField.addCaretListener(new javax.swing.event.CaretListener() {

                public void caretUpdate(javax.swing.event.CaretEvent e) {
                    model.setProjectName(projectNameTextField.getText());
                }
            });
        }
        return projectNameTextField;
    }

    /**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText(Messages.getString("NewProjectDialog.okButton"));
            okButton.setPreferredSize(new Dimension(100, 26));
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    control.acceptDialog();
                }
            });
        }
        return okButton;
    }

    /**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("NewProjectDialog.cancelButton"));
            cancelButton.setPreferredSize(new Dimension(100, 26));
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    control.discardDialog();
                }
            });
        }
        return cancelButton;
    }

    /**
	 * This method initializes fillerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getFillerPanel() {
        if (fillerPanel == null) {
            fillerPanel = new JPanel();
            fillerPanel.setLayout(new GridBagLayout());
        }
        return fillerPanel;
    }

    /**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.weightx = 1.0D;
            gridBagConstraints3.fill = GridBagConstraints.NONE;
            gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints3.anchor = GridBagConstraints.EAST;
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.fill = GridBagConstraints.NONE;
            gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridy = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getOkButton(), gridBagConstraints2);
            buttonPanel.add(getCancelButton(), gridBagConstraints3);
        }
        return buttonPanel;
    }
}
