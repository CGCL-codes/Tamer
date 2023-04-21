package uk.ac.imperial.ma.metric.apps.htmlViewer;

import javax.swing.JEditorPane;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.IOException;
import java.io.BufferedReader;
import javax.swing.ProgressMonitorInputStream;
import uk.ac.imperial.ma.metric.util.Task;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * This class extends <code>JEditorPane</code> to add the ability to 
 * monitor the progress of loading.
 * 
 * @version 0.0.1 8 May 2003
 * @author Daniel J. R. May
 */
public class ExtendedJEditorPane extends JEditorPane implements Task {

    /** The default stylesheet path. */
    private static final String STYLE_SHEET_RELATIVE_PATH = "./../../html/css/default.css";

    private static final File STYLE_SHEET_FILE = new File(STYLE_SHEET_RELATIVE_PATH);

    private HTMLEditorKit htmlEditorKit;

    private StyleSheet styleSheet;

    private URL url;

    private String strName;

    private String strStatus;

    private int min;

    private int value;

    private int max;

    private boolean isAlive;

    private boolean cancel;

    /**
     * The constructor for a new instance of <code>ExtendedJEditorPane</code>.
     */
    public ExtendedJEditorPane() throws Exception {
        super();
        htmlEditorKit = new HTMLEditorKit();
        setEditorKit(htmlEditorKit);
        styleSheet = new StyleSheet();
        styleSheet.importStyleSheet(STYLE_SHEET_FILE.toURL());
        strName = "Undefined.";
        strStatus = "Undefined.";
        min = 0;
        max = 100;
        value = 0;
        isAlive = false;
        cancel = false;
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public void reload() throws Exception {
        styleSheet = new StyleSheet();
        styleSheet.importStyleSheet(STYLE_SHEET_FILE.toURL());
    }

    /**
     * Called by the <code>Thread.start()</code> method.
     */
    public void run() {
        try {
            isAlive = true;
            cancel = false;
            strName = "Loading: " + url.toString();
            strStatus = "Opening connection.";
            value = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            strStatus = "Creating new document.";
            value = 1;
            Thread.yield();
            HTMLDocument htmlDocument = new HTMLDocument(styleSheet);
            strStatus = "Reading into document.";
            value = 2;
            Thread.yield();
            htmlEditorKit.read(bufferedReader, htmlDocument, 0);
            strStatus = "Closing connection.";
            value = 3;
            Thread.yield();
            bufferedReader.close();
            strStatus = "Setting document base.";
            value = 4;
            Thread.yield();
            htmlDocument.setBase(url);
            strStatus = "Displaying document.";
            value = 5;
            Thread.yield();
            setDocument(htmlDocument);
            strStatus = "Finishing up.";
            value = 6;
            Thread.yield();
            strStatus = "Done";
            isAlive = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean getCanceled() {
        return cancel;
    }

    public void cancel() {
        strStatus = "Canceling.";
        cancel = true;
    }

    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getName() {
        return strName;
    }

    public String getStatus() {
        return strStatus;
    }
}
