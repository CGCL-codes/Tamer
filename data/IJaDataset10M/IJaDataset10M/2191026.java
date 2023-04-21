package org.astrogrid.clustertool;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;

/**
 *  
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 6 Jan 2010 From original by   "Real Gagnon - edited by William Denniss"
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class LogFrame extends JFrame {

    private boolean catchErrors;

    private boolean logFile;

    private String fileName;

    TextArea aTextArea = new TextArea();

    PrintStream aPrintStream = new PrintStream(new FilteredStream(new ByteArrayOutputStream()));

    /** Creates a new RedirectFrame.
     *  From the moment it is created,
     *  all System.out messages and error messages (if requested)
     *  are diverted to this frame and appended to the log file 
     *  (if requested)
     *
     * for example:
     *  RedirectedFrame outputFrame =
     *       new RedirectedFrame
                (false, false, null, 700, 600, JFrame.DO_NOTHING_ON_CLOSE);
     * this will create a new RedirectedFrame that doesn't catch errors,
     * nor logs to the file, with the dimentions 700x600 and it doesn't 
     * close this frame can be toggled to visible, hidden by a controlling 
     * class by(using the example) outputFrame.setVisible(true|false)
     *  @param catchErrors set this to true if you want the errors to 
     *         also be caught
     *  @param logFile set this to true if you want the output logged
     *  @param fileName the name of the file it is to be logged to
     *  @param width the width of the frame
     *  @param height the height of the frame
     *  @param closeOperation the default close operation
     *        (this must be one of the WindowConstants)
     */
    public LogFrame(boolean catchErrors, boolean logFile, String fileName, int width, int height, int closeOperation) {
        this.catchErrors = catchErrors;
        this.logFile = logFile;
        this.fileName = fileName;
        Container c = getContentPane();
        setTitle("Output Frame");
        setSize(width, height);
        c.setLayout(new BorderLayout());
        c.add("Center", aTextArea);
        displayLog();
        this.logFile = logFile;
        System.setOut(aPrintStream);
        if (catchErrors) System.setErr(aPrintStream);
        setDefaultCloseOperation(closeOperation);
    }

    class FilteredStream extends FilterOutputStream {

        public FilteredStream(OutputStream aStream) {
            super(aStream);
        }

        public void write(byte b[]) throws IOException {
            String aString = new String(b);
            aTextArea.append(aString);
        }

        public void write(byte b[], int off, int len) throws IOException {
            String aString = new String(b, off, len);
            aTextArea.append(aString);
            if (logFile) {
                FileWriter aWriter = new FileWriter(fileName, true);
                aWriter.write(aString);
                aWriter.close();
            }
        }
    }

    private void displayLog() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        Dimension dd = getSize();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
        setVisible(true);
        requestFocus();
    }
}
