package it.unical.inf.wsportal.client.util;

import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 *
 * @author Simone Spaccarotella {spa.simone@gmail.com}, Carmine Dodaro {carminedodaro@gmail.com}
 */
public class Toolkit {

    /**
     *
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        return true;
    }

    /**
     *
     * @param ex
     */
    public static void showException(Exception ex) {
        MessageBox.alert("Exception", ex.getMessage(), null).setIcon(MessageBox.ERROR);
    }

    /**
     *
     * @param message
     */
    public static void showWarning(String message) {
        MessageBox.alert("Warning", message, null);
    }

    /**
     *
     * @param ex
     */
    public static void showServerError(Throwable ex) {
        MessageBox.alert("Server Error", ex.getMessage(), null).setIcon(MessageBox.ERROR);
    }

    /**
     *
     * @param message
     */
    public static void showInfo(String message) {
        MessageBox.info("Info", message, null);
    }
}
