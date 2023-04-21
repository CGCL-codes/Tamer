package oscript.util;

import javax.swing.JOptionPane;

/**
 * An implementation of {@link ErrorHandler} that uses swing to display
 * an error dialog.
 * 
 * @author Rob Clark (rob@ti.com)
 * @version 1
 */
public class SwingErrorHandler extends ErrorHandler {

    /**
   * Display an error message to the user.
   * 
   * @param  str     a string describing the error, how the user may
   *    correct the error, etc
   */
    public void showMessage(String str) {
        JOptionPane.showMessageDialog(null, str, "Fatal Error", JOptionPane.ERROR_MESSAGE);
    }
}
