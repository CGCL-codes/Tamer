package za.org.meraka.dictionarymaker.ui;

import java.awt.*;
import javax.swing.*;
import za.org.meraka.dictionarymaker.model.Dictionary;

/**
 * RuleExtractionProgressDialog
 * 
 * A modal dialog that shows a progress indicator while a full rule set
 * extraction takes place.
 * 
 * MT's comment and changes 10/08/08
 * This dialog is necessary when the rule extraction takes a while, but not in the initial stages 
 * when full rule extraction -should- be quick, and happens after every word.
 * I removed or shortened all sleep states.
 *
 * @author Thomas Fogwill <tfogwill@users.sourceforge.net>
 * @date	   Mar 9, 2006
 */
public class RuleExtractionProgressDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -4067865074003244682L;

    private JPanel jPanel = null;

    private WrappingLabel lblMessage = null;

    private JProgressBar progressBar = null;

    private JPanel jPanel2 = null;

    /**
     * @param owner
     * @throws HeadlessException
     */
    public RuleExtractionProgressDialog(JFrame owner) throws HeadlessException {
        super(owner);
        initialize();
    }

    public void showProgress(final Dictionary dictionary) {
        Runnable extraction = new Runnable() {

            public void run() {
                dictionary.runFullRuleExtraction();
            }
        };
        final Thread extractionThread = new Thread(extraction);
        extractionThread.start();
        Thread progressCheckThread = new Thread(new Runnable() {

            public void run() {
                while (extractionThread.isAlive()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
                try {
                    Thread.sleep(1);
                    while (!isVisible()) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                }
                dispose();
            }
        });
        progressCheckThread.start();
        Utilities.center(getOwner(), this);
        setVisible(true);
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setModal(true);
        this.setSize(250, 120);
        this.setTitle(Messages.getString("RuleExtractionProgressDialog.Title"));
        this.setResizable(true);
        this.setContentPane(getJPanel());
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout(5, 5));
            jPanel.add(getProgressBar(), java.awt.BorderLayout.SOUTH);
            jPanel.add(getJPanel2(), java.awt.BorderLayout.CENTER);
        }
        return jPanel;
    }

    /**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar();
        }
        progressBar.setIndeterminate(true);
        return progressBar;
    }

    /**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridLayout());
            lblMessage = new WrappingLabel();
            lblMessage.setText(Messages.getString("RuleExtractionProgressDialog.Message"));
            lblMessage.setMargin(new Insets(15, 15, 5, 15));
            jPanel2.add(lblMessage);
            jPanel2.add(lblMessage);
        }
        return jPanel2;
    }
}
