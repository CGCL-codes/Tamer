package org.sosy_lab.ccvisu.readers;

import java.io.BufferedReader;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.filechooser.FileFilter;
import org.sosy_lab.ccvisu.Options.InFormat;
import org.sosy_lab.ccvisu.Options.OutFormat;
import org.sosy_lab.ccvisu.Options.Verbosity;
import org.sosy_lab.ccvisu.graph.GraphData;

/**
 * Reader for input data.
 */
public abstract class ReaderData {

    /** Input stream reader object. */
    protected BufferedReader reader;

    protected Verbosity verbosity;

    /**
   * Constructor.
   * @param reader      Stream reader object.
   * @param verbosity   level of verbosity.
   */
    public ReaderData(BufferedReader reader, Verbosity verbosity) {
        this.reader = reader;
        this.verbosity = verbosity;
    }

    /**
   * Reads the graph or layout data from stream reader <code>reader</code>.
   * @param graph  <code>GraphData</code> object to store the read graph or
   *               layout data in.
   */
    public abstract void read(GraphData graph);

    protected String readEntry(StringTokenizer st) {
        String result = st.nextToken();
        if (result.charAt(0) == '"') {
            while (result.charAt(result.length() - 1) != '"') {
                result = result + ' ' + st.nextToken();
            }
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }

    public static FileFilter mkExtensionFileFilter(final InFormat inFormat) {
        return ReaderData.mkExtensionFileFilter(inFormat.getFileExtension(), inFormat.getShortDescription());
    }

    public static FileFilter mkExtensionFileFilter(final OutFormat outFormat) {
        return ReaderData.mkExtensionFileFilter(outFormat.getFileExtension(), outFormat.getShortDescription());
    }

    public static FileFilter mkExtensionFileFilter(final String extension, final String description) {
        return new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(extension);
            }

            @Override
            public String getDescription() {
                return description;
            }
        };
    }
}
