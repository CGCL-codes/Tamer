package xmitools.xmilinker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import xmitools.xmiparser.XmiFile;

/**
 * @author rlechner
 * 
 * Implementation of {@link xmitools.xmilinker.XmiLinkerReport} for HTML.
 * It creates UTF-8 files.
 */
public class XmiLinkerHtmlReport implements XmiLinkerReport {

    /**
     * Creates a HTML report file.
     * 
     * @param output The HTML report file.
     */
    public XmiLinkerHtmlReport(File output) {
        outputFile_ = output;
    }

    /**
     * Creates a HTML report file. Writes extra messages.
     * 
     * @param output The HTML report file.
     * @param debug There goes the messages.
     */
    public XmiLinkerHtmlReport(File output, PrintStream debug) {
        outputFile_ = output;
        debug_ = debug;
    }

    private File outputFile_;

    private PrintStream debug_;

    /**
     * Prints a debug message.
     */
    protected void println(String msg) {
        if (debug_ != null) {
            debug_.println(msg);
        }
    }

    /**
     * The HTML output stream.
     */
    protected PrintWriter output_;

    public void open() throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFile_);
        output_ = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));
        output_.println("<html><head>");
        output_.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        output_.print("<title>");
        output_.print(getTitle());
        output_.println("</title></head><body>");
    }

    /**
     * @return The title text of the HTML report.
     */
    protected String getTitle() {
        return "XMI-Linker report";
    }

    public void close() {
        output_.println("</body></html>");
        output_.close();
        output_ = null;
    }

    public void addLinkerInputFile(XmiFile file) {
        linkerXmiInputFileNames_.add(file.getAbsolutePath());
    }

    /**
     * Instances of {@link java.lang.String}.
     */
    protected ArrayList linkerXmiInputFileNames_ = new ArrayList();

    /**
     * Returns the name of the associated input file.
     */
    protected String getInputFileName(int index) {
        if (index == XmiLinkerElementImp.NEW_ELEMENT_INDEX) {
            return "-- NEW ELEMENT --";
        }
        if (index == XmiLinkerElementImp.NO_FILE_INDEX) {
            return "-- NO FILE ASSOCIATED --";
        }
        return linkerXmiInputFileNames_.get(index).toString();
    }

    public void reportException(Exception e) {
        output_.println("<b>Exception: </b><pre>");
        output_.println(e.getMessage());
        output_.println("</pre>");
    }

    public void start() {
        output_.println("<h1>" + getTitle() + "</h1>");
    }

    public void end() {
        output_.println("<p><tt>OK</tt>");
    }

    public void beginLoading() {
        output_.println("<p><b>input files:</b><ul>");
    }

    public void endLoading() {
        output_.println("</ul><p>");
    }

    public void reportFileLoading(int index) {
        String file = getInputFileName(index);
        output_.print("<li><tt>");
        output_.print(file);
        output_.println("</tt></li>");
        println("loading " + file);
    }

    public void reportLinking() {
        output_.println("<p><b>linking</b><br><br>");
    }

    public Object createUnresolvedExternalWarning(XmiLinkerElement x) {
        String file = getInputFileName(x.getFileIndex());
        String path = x.pathName();
        return "<tt>" + file + "</tt> : unresolved external element: <tt>" + path + "</tt>";
    }

    public Object createUnusedExternalWarning(XmiLinkerElement x) {
        String file = getInputFileName(x.getFileIndex());
        String path = x.pathName();
        return "<tt>" + file + "</tt> : unused external element: <tt>" + path + "</tt>";
    }

    public Object createMultipleDefinitionsError(XmiLinkerElement x, int nums) {
        String path = x.pathName();
        return "element <tt>" + path + "</tt> is defined " + nums + " times";
    }

    public Object createNewChildMessage(XmiLinkerElement x, XmiLinkerNode child) {
        String file = getInputFileName(x.getFileIndex());
        String path = x.pathName();
        String name = child.name();
        String tag = child.tagKind();
        return "<tt>" + file + "</tt>: element <tt>" + path + "</tt>: new child <tt>[" + tag + "]<b> " + name + "</b></tt>";
    }

    public void reportErrors(List errors) {
        if (errors.size() == 0) {
            return;
        }
        output_.println("<b>errors:</b><br><ul>");
        Iterator it = errors.iterator();
        while (it.hasNext()) {
            output_.println("<li>" + it.next().toString() + "</li>");
        }
        output_.println("</ul><p>");
    }

    public void reportWarnings(List warnings) {
        if (warnings.size() == 0) {
            return;
        }
        output_.println("<b>warnings:</b><br><ul>");
        Iterator it = warnings.iterator();
        while (it.hasNext()) {
            output_.println("<li>" + it.next().toString() + "</li>");
        }
        output_.println("</ul><p>");
    }

    public void reportMessages(List messages) {
        if (messages.size() == 0) {
            return;
        }
        output_.println("<b>messages:</b><br><ul>");
        Iterator it = sortMessages(messages).iterator();
        while (it.hasNext()) {
            output_.println("<li>" + it.next().toString() + "</li>");
        }
        output_.println("</ul><p>");
    }

    /**
     * Sorts the messages.
     * @see {@link java.util.Collections#sort(java.util.List)}
     */
    protected List sortMessages(List messages) {
        ArrayList result = new ArrayList(messages);
        Collections.sort(result);
        return result;
    }

    public void reportUnsupportedXmiVersion(String version) {
        output_.println("<p><b>unsupported XMI version: </b><tt>" + version + "</tt><p>");
    }
}
