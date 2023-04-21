package net.rptools.inittool.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.jeta.forms.components.panel.FormPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * This is an abstract dialog with a ok/cancel button. Just add a middle panel and OK support
 *  
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public abstract class AbstractDialog extends RPDialog implements ActionListener {

    /**
   * Button used to close the dialog and set the initiative values.
   */
    private AbstractButton okButton;

    /**
   * Button used to close the dialog and cancel starting the encounter.
   */
    private AbstractButton cancelButton;

    /**
   * The panel that contains all of the components.
   */
    private JPanel dialogPanel;

    /**
   * The form panel with the default dialog info
   */
    private FormPanel panel;

    /**
   * Create the dialog for the passed frame.
   * 
   * @param dialog The parent dialog of this dialog.
   * @param title Title for the dialog.
   * @param theInstructions The instructions displayed in the banner
   */
    public AbstractDialog(JDialog dialog, String title, String theInstructions) {
        super(dialog, true);
        construct(title, theInstructions);
    }

    /**
   * Create the dialog for the passed frame.
   * 
   * @param frame The parent frame of this dialog.
   * @param title Title for the dialog.
   * @param theInstructions The instructions displayed in the banner
   */
    public AbstractDialog(JFrame frame, String title, String theInstructions) {
        super(frame, true);
        construct(title, theInstructions);
    }

    /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == okButton) {
            if (!handleOK()) return;
            setVisible(false);
        } else if (event.getSource() == cancelButton) {
            handleCancel();
            setVisible(false);
        }
    }

    /**
   * Handle the OK button press.
   * 
   * @return Return <code>true</code> from this method if the dialog should close. Otherwise return 
   * <code>false</code>.
   */
    public abstract boolean handleOK();

    /**
   * Handle the Cancel button press, the default implementation does nothing.
   */
    public void handleCancel() {
    }

    /**
   * Construct the dialog internals
   * 
   * @param title Title for the dialog.
   * @param theInstructions The instructions displayed in the banner
   */
    private void construct(String title, String theInstructions) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panel = new FormPanel("net/rptools/inittool/resources/dialog.jfrm");
        add(panel);
        setTitle(title);
        setInstructions(theInstructions);
        okButton = panel.getButton("okButton");
        okButton.addActionListener(this);
        getRootPane().setDefaultButton((JButton) okButton);
        cancelButton = panel.getButton("cancelButton");
        cancelButton.addActionListener(this);
        dialogPanel = panel.getPanel("dialogPanel");
        dialogPanel.removeAll();
        dialogPanel.setLayout(new BorderLayout());
        pack();
    }

    /** @return Getter for dialogPanel */
    public JPanel getDialogPanel() {
        return dialogPanel;
    }

    /**
   * @see java.awt.Dialog#setTitle(java.lang.String)
   */
    @Override
    public void setTitle(String aTitle) {
        panel.setText("title", aTitle);
        if (aTitle == null) {
            ((FormLayout) panel.getFormContainer().getLayout()).setRowSpec(2, new RowSpec("0px"));
        } else {
            ((FormLayout) panel.getFormContainer().getLayout()).setRowSpec(2, new RowSpec("pref"));
        }
        super.setTitle(aTitle);
    }

    /**
   * Get the current instruction text.
   * 
   * @return The current instruction text.
   */
    public String getInstructions() {
        return panel.getText("instructions");
    }

    /**
   * Set the display instructions for the dialog
   * 
   * @param instructions New display instructions
   */
    public void setInstructions(String instructions) {
        panel.setText("instructions", instructions);
        if (instructions == null) {
            ((FormLayout) panel.getFormContainer().getLayout()).setRowSpec(3, new RowSpec("0px"));
        } else {
            ((FormLayout) panel.getFormContainer().getLayout()).setRowSpec(3, new RowSpec("pref"));
        }
    }
}
