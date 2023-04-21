package issrg.utils.gui.timedate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Chris
 */
public class TimeDateDialog extends JDialog implements ActionListener {

    /**
     * The Buttons that are used for exiting the Dialog.
     */
    JButton cancelButton, okButton;

    JFrame owner;

    public TimeDatePanel tdp;

    ResourceBundle rb;

    String cancelStr;

    String okStr;

    /**
     * Creates a new instance of TimeDateDialog 
     */
    public TimeDateDialog(JFrame owner, String dialogTitle, ResourceBundle rb) {
        super(owner, dialogTitle, true);
        this.loadBundles(rb);
        this.owner = owner;
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getContentPanel(), BorderLayout.CENTER);
        this.pack();
        centerDialog();
        this.setResizable(false);
        this.setVisible(true);
    }

    private void loadBundles(ResourceBundle rb) {
        if (rb != null) {
            this.rb = rb;
            this.cancelStr = this.rb.getString("TimeDateDialog_Cancel");
            this.okStr = this.rb.getString("TimeDateDialog_Ok");
        } else {
            throw new IllegalArgumentException("Resource Bundle file cannot be null.");
        }
    }

    public JPanel getContentPanel() {
        GridBagConstraints c = new GridBagConstraints();
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        cancelButton = new JButton(cancelStr);
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("EXIT");
        okButton = new JButton(okStr);
        okButton.addActionListener(this);
        okButton.setActionCommand("KEEP");
        tdp = new TimeDatePanel(this.owner, this.rb);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new java.awt.Insets(5, 5, 0, 5);
        dialogPanel.add(tdp, c);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new java.awt.Insets(5, 0, 5, 5);
        dialogPanel.add(cancelButton, c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new java.awt.Insets(5, 5, 5, 0);
        dialogPanel.add(okButton, c);
        return dialogPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "EXIT") {
            tdp.setDateTime("");
            this.dispose();
        } else if (e.getActionCommand() == "KEEP") {
            this.dispose();
        }
    }

    protected void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
        Dimension size = this.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        this.setLocation(x, y);
    }
}
