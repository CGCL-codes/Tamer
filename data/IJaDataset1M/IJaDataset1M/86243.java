package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.graphviz.options;

import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import org.openide.util.NbPreferences;

/**
 * Utility class for Graphviz layout binary.
 *
 * @author sviro
 */
public final class GraphvizUtils {

    private GraphvizUtils() {
    }

    ;

    /**
   * Get path to graphviz dot binary saved in global options. If there is no path saved, this
   * method returns empty {@link String}.
   * @return Path to graphviz binary if it's saved in options, othervise returns empty String.
   */
    public static String getPath() {
        return NbPreferences.forModule(GraphvizLayoutPanel.class).get(GraphvizLayoutPanel.BINARY_PATH_PROP, GraphvizLayoutPanel.BINARY_PATH_DEFAULT);
    }

    /**
   * Check if path to graphviz dot binary is valid i.e. this path points to existing dot binary.
   * @return <tt>true</tt> if path points to existing dot binary, otherwise return <tt>false</tt>.
   */
    public static boolean isBinaryValid() {
        return FileUtils.isBinaryValid(getPath(), "-V", "dot - graphviz version", false);
    }

    /**
   * Check if provided path is valid i.e. this path points to existing dot binary. If isEmptyValid flag
   * is <tt>true</tt> and provided path is empty {@link String},
   * this path is also considered as valid.
   * @param pathToBinary Path to be checked.
   * @param isEmptyValid flag to indicate if empty String is valid path.
   * @return <tt>true</tt> if provided path is valid, otherwise return <tt>false</tt>.
   */
    public static boolean isBinaryValid(final String pathToBinary, final boolean isEmptyValid) {
        return FileUtils.isBinaryValid(getPath(), "-V", "dot - graphviz version", isEmptyValid);
    }
}
