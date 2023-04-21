package gr.demokritos.iit.jinsect.gui.utils;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/** A JTextArea wrapper to be used as PrintStream for redirection of System.err or System.out.
 *
 * @author ggianna
 */
public class JTextAreaPrintStream extends PrintStream {

    JTextArea OutputArea;

    /** Creates a new instance of JTextAreaPrintStream.
     *@param taOut The target textarea.
     */
    public JTextAreaPrintStream(JTextArea taOut) {
        super(System.out, true);
        OutputArea = taOut;
    }

    public void println(final String x) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                OutputArea.append(x + "\n");
            }
        });
    }

    public void print(final String s) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                OutputArea.cut();
                OutputArea.append(s.replaceAll("\r", "\n"));
            }
        });
    }

    public void println() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                OutputArea.append("\n");
            }
        });
    }
}
