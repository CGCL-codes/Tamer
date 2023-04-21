package com.sltech.gui.bindings;

import java.awt.Component;
import java.lang.reflect.Method;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * Desktop Application Helper
 * Provides common funtionality for setting up the environment and starting up a desktop application.
 * <p>
 * Usage example:
 * <pre>
 *   ApplicationHelper.setSystemLookAndFeel();
 *   LoginFrame login = new LoginFrame();
 *   MainFrame main = new MainFrame();
 *   ApplicationHelper.startSecureApplication( main, login, args);
 * </pre>
 * </p>
 * @author juanjo

 */
public class ApplicationHelper {

    public static String ENTER = System.getProperty("line.separator");

    public static final int CHARS_IN_SENTENCE = 60;

    private static JDesktopPane desktopPane = null;

    public static JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public ApplicationHelper() {
    }

    /**
   * Sets system's look and feel for the application, like Windows look and feel
   */
    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
   * Arregla las oraciones para que los mensajes no queden muy extensos y se salgan de la pantalla.
   * @param msg el mensaje a arreglar
   * @param max_size el tamaño minimo para que empiece a buscar para insertar un enter.
   * @return String con el mensaje arreglado.
   */
    public static String fitMessage(String msg, int max_size) {
        String s = "";
        int count = 0;
        for (int i = 0; i < msg.length(); i++) {
            if ((msg.charAt(i) == '\r') || (msg.charAt(i) == '\n')) {
                count = 0;
            }
            if ((msg.charAt(i) != ' ') || (count != 0)) {
                s = s + msg.charAt(i);
            }
            count++;
            if (count > max_size) {
                switch(msg.charAt(i)) {
                    case ' ':
                    case '\t':
                    case '.':
                    case ',':
                    case ':':
                        s = s + ENTER;
                        count = 0;
                        break;
                }
            }
        }
        return s;
    }

    /**
   * Arregla las oraciones para que los mensajes no queden muy extensos y se salgan de la pantalla.
   * @param msg el mensaje a arreglar
   * @return String con el mensaje arreglado.
   */
    public static String fitMessage(String msg) {
        return fitMessage(msg, CHARS_IN_SENTENCE);
    }

    public static void openURL(Component parenComponent, String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[] { browser, url });
                }
            }
        } catch (Exception e) {
            ApplicationHelper.showErrorMessage(parenComponent, "Abrir", "Error attempting to launch web browser:\n" + e.getLocalizedMessage(), e);
        }
    }

    public static void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorMessage(String title, String message, Exception ex) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), fitMessage(message + ENTER + ENTER + ex), title, JOptionPane.ERROR_MESSAGE);
    }

    /**
   * Shows an error message dialog
   * @param parentComponent
   * @param title
   * @param message
   * @param ex
   */
    public static void showErrorMessage(Component parentComponent, String title, String message, Exception ex) {
        JOptionPane.showMessageDialog(parentComponent, fitMessage(message + ENTER + ENTER + ex.getMessage()), title, JOptionPane.ERROR_MESSAGE);
    }

    /**
   * Shows an error message dialog
   * @param parentComponent
   * @param title
   * @param message
   * @param ex
   */
    public static void showErrorMessage(Component parentComponent, String title, String message) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
   * Shows an information message dialog
   * @param parentComponent
   * @param title
   * @param message
   */
    public static void showInformationMessage(Component parentComponent, String title, String message) {
        JOptionPane.showMessageDialog(parentComponent, fitMessage(message), title, JOptionPane.INFORMATION_MESSAGE);
    }
}
