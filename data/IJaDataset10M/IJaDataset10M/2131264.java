package org.eclipse.help.internal.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Plugins with prebuilt search indexes.
 * 
 */
public class PrebuiltIndexes {

    private SearchIndex targetIndex;

    /**
	 * Set of PluginIndex
	 */
    private Set set = new HashSet();

    PrebuiltIndexes(SearchIndex targetIndex) {
        super();
        this.targetIndex = targetIndex;
    }

    void add(String plugin, String path) {
        set.add(new PluginIndex(plugin, path, targetIndex));
    }

    /**
	 * Removes Plugin indexes with no index
	 */
    private void trim() {
        List indexes = new ArrayList(set);
        for (int i = 0; i < indexes.size(); ) {
            PluginIndex index = (PluginIndex) indexes.get(i);
            if (index.getPaths().size() == 0) {
                set.remove(index);
            }
            i++;
        }
    }

    public PluginIndex[] getIndexes() {
        trim();
        return (PluginIndex[]) set.toArray(new PluginIndex[set.size()]);
    }
}
