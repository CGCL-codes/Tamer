package pcgen.gui;

import pcgen.core.Constants;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Greg Bingleman <byngl@hotmail.com>
 * @version $Revision: 1.19 $
 */
class NewPurchaseMethodDialog extends JDialog {

    static final long serialVersionUID = -5321303573914291162L;

    private JButton cancelButton;

    private JButton okButton;

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JPanel buttonPanel;

    private JPanel jPanel1;

    private JPanel jPanel2;

    private JTextField nameEdit;

    private JTextField pointsEdit;

    private boolean wasCancelled = true;

    /** Creates new form JDialog
	 * @param parent
	 * @param modal
	 */
    public NewPurchaseMethodDialog(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /** Creates new form JDialog
	 * @param parent
	 * @param modal
	 */
    private NewPurchaseMethodDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public String getEnteredName() {
        return nameEdit.getText().trim();
    }

    public int getEnteredPoints() {
        try {
            final int points = Integer.parseInt(pointsEdit.getText());
            return points;
        } catch (Exception exc) {
        }
        return -1;
    }

    public boolean getWasCancelled() {
        return wasCancelled;
    }

    /**
	 * @param args the command line arguments
	 */
    public static void main(String[] args) {
        new NewPurchaseMethodDialog(new JFrame(), true).setVisible(true);
    }

    private void cancelButtonActionPerformed() {
        wasCancelled = true;
        setVisible(false);
        this.dispose();
    }

    /** Closes the dialog */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }

    /** This method is called from within the constructor to
	 * initialize the form.
	 */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        nameEdit = new JTextField();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        pointsEdit = new JTextField();
        buttonPanel = new JPanel();
        cancelButton = new JButton();
        okButton = new JButton();
        getContentPane().setLayout(new GridBagLayout());
        setTitle("Enter name and points for Purchase Method");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                closeDialog();
            }
        });
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jLabel1.setText("Name:");
        jLabel1.setPreferredSize(new Dimension(140, 15));
        jPanel1.add(jLabel1);
        nameEdit.setPreferredSize(new Dimension(140, 20));
        jPanel1.add(nameEdit);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jLabel2.setText("Points:");
        jLabel2.setPreferredSize(new Dimension(140, 15));
        jPanel2.add(jLabel2);
        pointsEdit.setPreferredSize(new Dimension(30, 20));
        jPanel2.add(pointsEdit);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed();
            }
        });
        okButton.setMnemonic('O');
        okButton.setText("OK");
        buttonPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(buttonPanel, gridBagConstraints);
        pack();
    }

    private void okButtonActionPerformed() {
        if (getEnteredName().length() == 0) {
            ShowMessageDelegate.showMessageDialog("Please enter a name for this method.", Constants.s_APPNAME, MessageType.ERROR);
            return;
        }
        if (getEnteredPoints() <= 0) {
            ShowMessageDelegate.showMessageDialog("Invalid points value. Please try again.", Constants.s_APPNAME, MessageType.ERROR);
            return;
        }
        wasCancelled = false;
        setVisible(false);
        this.dispose();
    }
}
