package org.remus.infomngmnt.search.startup;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.remus.infomngmnt.search.Search;
import org.remus.infomngmnt.search.SearchPackage;
import org.remus.infomngmnt.search.preferences.SearchPreferenceInitializer;
import org.remus.infomngmnt.search.provider.SearchPlugin;
import org.remus.infomngmnt.search.save.SavedSearchesHandler;

/**
 * <p>
 * The local startup of the search plugin loads the
 * latest searches and deletes old files.
 * </p>
 * <p>
 * Dependend on how many searches are kept, see
 *  {@link SearchPreferenceInitializer#KEEP_X_SEARCHES_IN_HISTORY}
 * </p>
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class SearchStartup implements IStartup {

    public void earlyStartup() {
        SavedSearchesHandler handler = new SavedSearchesHandler();
        IPreferenceStore store = SearchPlugin.getPlugin().getPreferenceStore();
        int int1 = store.getInt(SearchPreferenceInitializer.KEEP_X_SEARCHES_IN_HISTORY);
        File file = SearchPlugin.getPlugin().getStateLocation().append(store.getString(SearchPreferenceInitializer.LOCAL_SEARCH_FOLDER)).toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        List<File> listFilesCollection = Arrays.asList(listFiles);
        Collections.sort(listFilesCollection, new Comparator<File>() {

            public int compare(File o1, File o2) {
                return ((Long) o2.lastModified()).compareTo(o2.lastModified());
            }
        });
        int counter = 0;
        if (listFilesCollection.size() > int1) {
            counter = int1;
        } else {
            counter = listFilesCollection.size();
        }
        for (int i = 0; i < counter; i++) {
            Search search = handler.getObjectFromUri(new Path(listFilesCollection.get(i).getAbsolutePath()), SearchPackage.Literals.SEARCH);
            SearchPlugin.getPlugin().getSearchHistory().getSearch().add(search);
        }
    }
}
