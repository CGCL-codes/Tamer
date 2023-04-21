package net.sf.raptor.ui.swing;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import net.sf.raptor.ui.swing.actions.ActionUtils;
import net.sf.raptor.ui.swing.dialog.ExceptionDialog;
import net.sf.raptor.ui.swing.dialog.XDialog;
import net.sf.raptor.ui.swing.panel.XPanel;

/**
 * Und hier h�tte <em>Klaus Zimmermann</em> schreiben sollen, was die Klasse
 * <code>WindowUtils</code> denn so macht ... <code><pre>
 *   und hier sollte au�erdem ein Code-Beispiel rein ...
 * </pre></code>
 *
 * @author    Klaus Zimmermann
 * @created   31. Juli 2002
 * @version   experimental
 *
 * @todo add TestCase  
 */
public class WindowUtils {

    /** Die CVS-Id dieser Klasse */
    public static final String $cvsid = "$Id: WindowUtils.java,v 1.3 2004/12/16 16:52:24 thomasgoertz Exp $";

    /** Description of the Field */
    private static JFrame defaultParentFrame;

    /**
     * macht den Dialog sichbar und blocked solange, bis hide() aufgerufen wurde
     *
     * @param dialog  Description of the Parameter
     */
    public static void doModal(JDialog dialog) {
        if (dialog.getOwner() == null) throw new NullPointerException("JDialog cannot be <null>");
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getOwner());
        dialog.show();
    }

    /**
     * Description of the Method
     *
     * @param window  Description of the Parameter
     * @return        Description of the Return Value
     */
    public static Window terminateOnDispose(Window window) {
        window.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        return window;
    }

    /**
     * Description of the Method
     *
     * @param component  Description of the Parameter
     * @return           Description of the Return Value
     */
    public static JFrame wrapInFrame(JComponent component) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(component, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }

    /**
     * Gets the defaultParentFrame attribute of the Dialogs object
     *
     * @return   The defaultParentFrame value
     */
    public static JFrame getDefaultParentFrame() {
        return defaultParentFrame;
    }

    /**
     * Sets the defaultParentFrame attribute of the Dialogs object
     *
     * @param newDefaultParentFrame  The new defaultParentFrame value
     */
    public static void setDefaultParentFrame(JFrame newDefaultParentFrame) {
        defaultParentFrame = newDefaultParentFrame;
    }

    /**
     * Description of the Method
     *
     * @param component  Description of the Parameter
     * @return           Description of the Return Value
     */
    public static JDialog wrapInDialog(JComponent component) {
        JDialog dialog = new JDialog(getDefaultParentFrame());
        dialog.getContentPane().add(component, BorderLayout.CENTER);
        dialog.pack();
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return dialog;
    }

    /**
     * Description of the Method
     *
     * @param component       Description of the Parameter
     * @param millis          Description of the Parameter
     * @param doAsynchronous  Description of the Parameter
     */
    public static void showInSplashWindow(JComponent component, int millis, boolean doAsynchronous) {
        try {
            Thread splashThread = new SplashDialogThread(component, millis);
            if (doAsynchronous) {
                splashThread.start();
            } else {
                splashThread.run();
            }
        } catch (Exception ex) {
            ExceptionDialog.showExceptionDialog(ex);
        }
    }

    /**
     * Description of the Class
     *
     * @author    zandere
     * @created   23. Juli 2002
     */
    private static class SplashDialogThread extends Thread {

        /** Description of the Field */
        private JComponent component;

        /** Description of the Field */
        private int millis;

        /**
         * Constructor for the SplashDialogThread object
         *
         * @param newComponent  Description of the Parameter
         * @param newMillis     Description of the Parameter
         */
        public SplashDialogThread(JComponent newComponent, int newMillis) {
            component = newComponent;
            millis = newMillis;
        }

        /** Main processing method for the SplashDialogThread object */
        public void run() {
            JDialog dialog = null;
            try {
                dialog = new JDialog(getDefaultParentFrame());
                dialog.getContentPane().add(component);
                dialog.pack();
                dialog.show();
                Thread.sleep(millis);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dialog.dispose();
            }
        }
    }

    /**
	 * wrapInDialog
	 * 
	 * @param panel
	 * @param dialog
	 * @return
	 */
    public static Object wrapInDialog(XPanel panel, XDialog dialog) {
        dialog.getContentPane().setLayout(new BorderLayout());
        JScrollPane spane = new JScrollPane(panel);
        spane.setBorder(BorderFactory.createEmptyBorder());
        dialog.getContentPane().add(spane, BorderLayout.CENTER);
        JPanel buttonPanel = ActionUtils.createButtonPanel(dialog, SwingConstants.HORIZONTAL);
        dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        return dialog.doModal();
    }
}
