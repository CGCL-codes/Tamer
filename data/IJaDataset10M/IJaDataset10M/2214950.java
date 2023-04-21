package poweria.guia.tools;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cody Stoutenburg
 */
public class MyGuiLogger {

    private static MyGuiLogger _instance = null;

    private MyGuiLogger() {
    }

    public static MyGuiLogger getInstance() {
        if (_instance == null) {
            _instance = new MyGuiLogger();
        }
        return _instance;
    }

    public void userError(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void userInfo(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
