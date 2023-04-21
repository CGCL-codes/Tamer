package mw.client.utils.error;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_S;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static mw.client.utils.lang.Localization.getLocalized;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import mw.client.constants.NewConstants;

/**
 * The class ErrorViewer. Enables showing and saving error messages that occured in forge.
 * 
 * @version V1.0 02.08.2009
 * @author Clemens Koza
 */
public class ErrorViewer implements NewConstants, NewConstants.ERROR_VIEWER {

    private static final String nameOS = "os.name";

    private static final String versionOS = "os.version";

    private static final String architectureOS = "os.arch";

    private static final String versionJava = "java.version";

    private static final String vendorJava = "java.vendor";

    public static final Action ALL_THREADS_ACTION = new ShowAllThreadsAction();

    /**
     * Shows an error dialog taking the exception's message as the error message.
     */
    public static void showError(Throwable ex) {
        showError(ex, null);
    }

    /**
     * Shows an error dialog creating the error message by a formatting operation.
     */
    public static void showError(Throwable ex, String format, Object... args) {
        if (ex == null) return;
        showError(ex, String.format(format, args));
    }

    /**
     * Shows an error dialog with the specified error message.
     */
    public static void showError(final Throwable ex, String message) {
        if (ex == null) return;
        final StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        printError(pw, ex, message);
        showDialog(sw.toString());
    }

    /**
     * Shows an error without an exception that caused it.
     */
    public static void showError(String format, Object... args) {
        showError(String.format(format, args));
    }

    /**
     * Shows an error without an exception that caused it.
     */
    public static void showError(String message) {
        showError(new Exception(), message);
    }

    /**
     * Shows an error message for all running threads.
     */
    public static void showErrorAllThreads(String format, Object... args) {
        showErrorAllThreads(String.format(format, args));
    }

    /**
     * Shows an error message for all running threads.
     */
    public static void showErrorAllThreads(String message) {
        final StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        printError(pw, message);
        showDialog(sw.toString());
    }

    private static void showDialog(String fullMessage) {
        JTextArea area = new JTextArea(fullMessage, 40, 90);
        area.setFont(new Font("Monospaced", Font.PLAIN, 10));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        Object[] options = { new JButton(new SaveAction(area)), getLocalized(BUTTON_CLOSE), new JButton(new ExitAction()) };
        JOptionPane pane = new JOptionPane(new JScrollPane(area), ERROR_MESSAGE, DEFAULT_OPTION, null, options, options[1]);
        JDialog dlg = pane.createDialog(null, getLocalized(TITLE));
        dlg.setResizable(true);
        dlg.setVisible(true);
        dlg.dispose();
    }

    /**
     * Prints the error message for the specified exception to the print writer
     */
    private static void printError(PrintWriter pw, Throwable ex, String message) {
        if (message != null) System.err.println(message);
        ex.printStackTrace();
        pw.printf(getLocalized(MESSAGE), FORUM, MAIL, message != null ? message : ex.getMessage(), VERSION, System.getProperty(nameOS), System.getProperty(versionOS), System.getProperty(architectureOS), System.getProperty(versionJava), System.getProperty(vendorJava));
        ex.printStackTrace(pw);
    }

    /**
     * Prints the error message to the print writer, showing all running threads' stack traces.
     */
    private static void printError(PrintWriter pw, String message) {
        System.err.println(message);
        pw.printf(getLocalized(MESSAGE), FORUM, MAIL, message, VERSION, System.getProperty(nameOS), System.getProperty(versionOS), System.getProperty(architectureOS), System.getProperty(versionJava), System.getProperty(vendorJava));
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        for (Entry<Thread, StackTraceElement[]> e : traces.entrySet()) {
            pw.println();
            pw.printf("%s (%s):%n", e.getKey().getName(), e.getKey().getId());
            for (StackTraceElement el : e.getValue()) {
                pw.println(el);
            }
        }
    }

    private static class SaveAction extends AbstractAction {

        private static final long serialVersionUID = 9146834661273525959L;

        private static JFileChooser c;

        private JTextArea area;

        public SaveAction(JTextArea area) {
            super(getLocalized(BUTTON_SAVE));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(VK_S, CTRL_DOWN_MASK));
            this.area = area;
        }

        public void actionPerformed(ActionEvent e) {
            if (c == null) c = new JFileChooser();
            File f;
            for (int i = 0; ; i++) {
                String name = String.format("%TF-%02d.txt", System.currentTimeMillis(), i);
                f = new File(name);
                if (!f.exists()) break;
            }
            c.setSelectedFile(f);
            c.showSaveDialog(null);
            f = c.getSelectedFile();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                bw.write(area.getText());
                bw.close();
            } catch (IOException ex) {
                showError(ex, getLocalized(SAVE_MESSAGE));
            }
        }
    }

    private static class ExitAction extends AbstractAction {

        private static final long serialVersionUID = 276202595758381626L;

        public ExitAction() {
            super(getLocalized(BUTTON_EXIT));
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private static class ShowAllThreadsAction extends AbstractAction {

        private static final long serialVersionUID = 5638147106706803363L;

        public ShowAllThreadsAction() {
            super(getLocalized(SHOW_ERROR));
        }

        public void actionPerformed(ActionEvent e) {
            showErrorAllThreads(getLocalized(SHOW_MESSAGE));
        }
    }
}
