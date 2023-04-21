package activator;

import java.util.*;

/**
 * Sorts plugins in PluginList.  Sort first by type, with library jars
 * after plugins, then by name, so plugins are listed first and sorted
 * by name, followed by library jars sorted by name.
 */
class PluginComparator implements Comparator {

    public int compare(Object alpha, Object beta) {
        PluginList.Plugin a = (PluginList.Plugin) alpha;
        PluginList.Plugin b = (PluginList.Plugin) beta;
        if (a.isLibrary() && !b.isLibrary()) {
            return 1;
        } else if (b.isLibrary() && !a.isLibrary()) {
            return -1;
        }
        return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
    }
}
