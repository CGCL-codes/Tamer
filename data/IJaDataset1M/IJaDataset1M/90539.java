package de.renier.jkeepass.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import de.renier.jkeepass.Messages;
import de.renier.jkeepass.components.SymbolComboBox;
import de.renier.jkeepass.util.IconPool;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * CreateContainerElementDialog
 *
 * @author <a href="mailto:software@renier.de">Renier Roth</a>
 */
public class CreateContainerElementDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public static final int RESULT_CANCEL = 0;

    public static final int RESULT_OK = 1;

    private JPanel jContentPane = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JLabel jLabel = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JTextField jTextField = null;

    private int result = RESULT_CANCEL;

    private JComboBox symbolComboBox = null;

    /**
   * @param owner
   */
    public CreateContainerElementDialog(Frame owner) {
        super(owner);
        initialize();
    }

    /**
   * This method initializes this
   * 
   * @return void
   */
    private void initialize() {
        this.setSize(300, 126);
        this.setTitle(Messages.getString("CreateContainerElementDialog.0"));
        this.setModal(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
        setLocationRelativeTo(getOwner());
        getRootPane().setDefaultButton(getOkButton());
    }

    /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.SOUTH);
            jContentPane.add(getJPanel1(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
   * This method initializes jPanel	
   * 	
   * @return javax.swing.JPanel	
   */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new FlowLayout());
            jPanel.add(getOkButton(), null);
            jPanel.add(getCancelButton(), null);
        }
        return jPanel;
    }

    /**
   * This method initializes jPanel1	
   * 	
   * @return javax.swing.JPanel	
   */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.NONE;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new Insets(0, 10, 0, 10);
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.gridx = 2;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 0;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new Insets(0, 10, 0, 10);
            gridBagConstraints11.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 10);
            gridBagConstraints.gridy = 0;
            jLabel = new JLabel();
            jLabel.setText(Messages.getString("CreateContainerElementDialog.1"));
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(jLabel, gridBagConstraints);
            jPanel1.add(getJTextField(), gridBagConstraints11);
            jPanel1.add(getSymbolComboBox(), gridBagConstraints2);
        }
        return jPanel1;
    }

    /**
   * This method initializes okButton	
   * 	
   * @return javax.swing.JButton	
   */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText(Messages.getString("CreateContainerElementDialog.2"));
            okButton.setIcon(new ImageIcon(getClass().getResource("/org/javalobby/icons/20x20/Save.gif")));
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    result = RESULT_OK;
                    dispose();
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
            cancelButton.setText(Messages.getString("CreateContainerElementDialog.4"));
            cancelButton.setIcon(new ImageIcon(getClass().getResource("/org/javalobby/icons/20x20/Delete.gif")));
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return cancelButton;
    }

    /**
   * This method initializes jTextField	
   * 	
   * @return javax.swing.JTextField	
   */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setPreferredSize(new Dimension(100, 19));
        }
        return jTextField;
    }

    /**
   * getResult
   *
   * @return Returns the result.
   */
    public int getResult() {
        return result;
    }

    /**
   * setResult
   *
   * @param result The result to set.
   */
    public void setResult(int result) {
        this.result = result;
    }

    /**
   * getContainerName
   *
   * @return
   */
    public String getContainerName() {
        return jTextField.getText();
    }

    /**
   * getSymbol
   *
   * @return
   */
    public String getSymbol() {
        return (String) symbolComboBox.getSelectedItem();
    }

    /**
   * This method initializes symbolComboBox	
   * 	
   * @return javax.swing.JComboBox	
   */
    private JComboBox getSymbolComboBox() {
        if (symbolComboBox == null) {
            symbolComboBox = new SymbolComboBox();
            symbolComboBox.setSelectedItem(IconPool.DEFAULT_CONTAINER_ID);
        }
        return symbolComboBox;
    }
}
