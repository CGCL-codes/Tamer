package polysema.virtuamea.video.exceptions;

import java.awt.Component;
import javax.swing.JOptionPane;

public class AutoShotDetectionException extends Exception {

    public AutoShotDetectionException() {
    }

    public AutoShotDetectionException(String arg0) {
        super(arg0);
    }

    public AutoShotDetectionException(Throwable arg0) {
        super(arg0);
    }

    public AutoShotDetectionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public static void showErrorMessage(String msg, Component parent) {
        JOptionPane.showMessageDialog(parent, msg, "Auto Shot Detection Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
}
