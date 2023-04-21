package tjger.net;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tjger.MainFrame;
import tjger.lib.ConstantValue;
import hgb.gui.HGBaseDialog;
import hgb.gui.ProgressPanel;
import hgb.lib.HGBaseSettings;
import hgb.lib.HGBaseText;
import hgb.lib.HGBaseTools;

/**
 * With this class a dialog is displayed while trying to connect. 
 * If the user presses the cancel button, the method wasCancelled() returns true.
 * 
 * @author hagru
 */
public abstract class NetworkDialogAction {

    private int actionReturn = HGBaseTools.INVALID;

    private boolean userCancel = false;

    private String message;

    private MainFrame mainFrame;

    private ProgressPanel waitProgressBar;

    private JDialog waitDialog;

    public NetworkDialogAction(MainFrame mainFrame) {
        this(mainFrame, null);
    }

    public NetworkDialogAction(MainFrame mainFrame, String message) {
        super();
        this.message = message;
        this.mainFrame = mainFrame;
    }

    public static void run(final NetworkDialogAction action) {
        action.waitDialog = null;
        action.waitProgressBar = null;
        Thread t = new Thread() {

            public void run() {
                Object o = null;
                while (!(action.waitProgressBar != null && (o instanceof JButton)) && action.waitDialog == null) {
                    action.delay();
                    o = HGBaseDialog.getDialogButton(0);
                }
                if (action.waitDialog != null) {
                    action.mainFrame.setStatusProgress(ProgressPanel.STATE_WAITING);
                    action.actionReturn = action.doAction();
                    action.mainFrame.setStatusProgress(ProgressPanel.STATE_NORMAL);
                    action.waitDialog.setVisible(false);
                    action.waitDialog.dispose();
                } else if (o instanceof JButton) {
                    JButton bCancel = (JButton) o;
                    action.actionReturn = action.doAction();
                    if (action.userCancel) action.actionReturn = HGBaseTools.INVALID; else {
                        bCancel.doClick();
                    }
                }
            }
        };
        t.start();
        if (action.message == null) {
            action.waitDialog = new JDialog();
            action.waitDialog.setUndecorated(true);
            action.waitDialog.setSize(0, 0);
            action.waitDialog.setModal(true);
            action.waitDialog.setVisible(true);
        } else {
            action.waitProgressBar = new ProgressPanel();
            action.waitProgressBar.setState(ProgressPanel.STATE_WAITING);
            action.waitProgressBar.setPreferredSize(new Dimension(250, 20));
            JPanel pnProgress = new JPanel(new BorderLayout());
            pnProgress.add(action.waitProgressBar, BorderLayout.SOUTH);
            pnProgress.add(new JLabel(" "), BorderLayout.CENTER);
            if (action.message != null) {
                String text = HGBaseText.existsText(action.message) ? HGBaseText.getText(action.message) : action.message;
                pnProgress.add(new JLabel(text), BorderLayout.NORTH);
            }
            String[] options = { HGBaseText.getText("dlg.cancel") };
            HGBaseDialog.showOptionDialog(action.mainFrame, pnProgress, HGBaseSettings.get("appName"), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
            action.userCancel = true;
        }
    }

    /**
     * Ask for this method to know if the action was cancelled by the user.
     * 
     * @return True if the user presses the cancel button.
     */
    protected boolean wasCancelled() {
        return (actionReturn == HGBaseTools.INVALID);
    }

    /**
     * Wait the defined intervall.
     */
    protected void delay() {
        try {
            Thread.sleep(ConstantValue.NETWORK_WAITINTERVAL);
        } catch (Exception e) {
        }
    }

    /**
     * Implement the action that shall done while the dialog is displayed.
     * Use the wasCancelled()-method to stop the loop.
     * 
     * @return 0 if successful or an error code.
     */
    protected abstract int doAction();

    /**
     * The return value of the action or INVALID if the user pressed the cancel button.
     * 
     * @return The return value of the action or INVALID.
     */
    public int getReturnValue() {
        return actionReturn;
    }
}
