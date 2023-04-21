package com.ibm.tuningfork.core.importer;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import com.ibm.tuningfork.core.configuration.Configuration;
import com.ibm.tuningfork.core.dialog.ErrorHandling;
import com.ibm.tuningfork.core.figure.Figure;
import com.ibm.tuningfork.core.figure.FigureSaveRestore;
import com.ibm.tuningfork.core.figure.PlayableFigure;
import com.ibm.tuningfork.infra.Logging;
import com.ibm.tuningfork.infra.data.TimeInterval;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;
import com.ibm.tuningfork.infra.textwriter.CSV_TextWriter;
import com.ibm.tuningfork.infra.textwriter.ITextOutput;
import com.ibm.tuningfork.infra.textwriter.TextWriter;
import com.ibm.tuningfork.infra.textwriter.WrappedPrintWriter;
import com.ibm.tuningfork.infra.textwriter.XML_TextWriter;

/**
 * Support for exporting trace data.
 */
public class ExportManager {

    private ExportManager() {
    }

    protected static final String[] supportedExtensions = new String[] { "*.xml", "*.csv", "*.txt", "*.fig" };

    public static final String[] supportedTypes = new String[] { "XML", "CSV", "TXT", "FIG" };

    /**
     * Get name of file to use for exported data.
     *
     * This should eventually have its file types populated from the registry, once that is implemented.
     *
     * @return Output file name
     */
    public static String getExportFilename() {
        String defaultfile = Configuration.getExportFilename();
        defaultfile = defaultfile != null ? defaultfile.substring(1 + Math.max(defaultfile.lastIndexOf('/'), defaultfile.lastIndexOf('\\'))) : "";
        Shell shell = Display.getCurrent().getShells()[0];
        final FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setFileName(defaultfile);
        dialog.setText("Export Trace Data to File");
        dialog.setFilterExtensions(supportedExtensions);
        dialog.setFilterNames(supportedTypes);
        final String filename = dialog.open();
        return filename;
    }

    /**
     * Create a trace export writer based on the file type of the supplied filename.
     *
     * @param filename Filename to which trace will be exported.
     * @return Export writer object
     */
    public static TextWriter createDataWriter(String filename, StreamBundle sources) {
        if (filename == null || !isSupportedFileType(filename)) {
            ErrorHandling.displayError("Export Error", "File type of " + filename + " is not supported for export");
            return null;
        }
        try {
            ITextOutput out = new WrappedPrintWriter(new PrintWriter(new FileOutputStream(filename)));
            TextWriter writer = filename.endsWith("xml") ? new XML_TextWriter(out, sources) : filename.endsWith("csv") ? new CSV_TextWriter(out, sources) : new CSV_TextWriter(out, sources, "\t");
            return writer;
        } catch (Exception e) {
            Logging.errorln("Export failure: " + e);
            ErrorHandling.displayError("Export Error", "Unable to export to file " + filename);
            return null;
        }
    }

    public static boolean isSupportedFileType(String filename) {
        for (String s : supportedTypes) {
            if (filename.endsWith(s.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void setDefaultExportFilename(String filename) {
        Configuration.updateExportFilename(filename);
    }

    public static void exportFigure(String filename, Figure figure) {
        if (filename.toLowerCase().endsWith(".fig")) {
            FigureSaveRestore.saveFigure(filename, figure);
        } else {
            TextWriter writer = ExportManager.createDataWriter(filename, figure.getStreams());
            TimeInterval range = TimeInterval.ALL_TIME;
            if (figure instanceof PlayableFigure) {
                PlayableFigure playable = (PlayableFigure) figure;
                TimeInterval displayed = playable.getDisplayedTimeRange();
                if (displayed.length() > 0) {
                    range = displayed;
                }
            }
            writer.write(range);
        }
    }
}
