package com.hifi.plugin.library.model.libraries.impl.tags.loading;

import java.io.File;
import com.hifi.plugin.library.model.libraries.LibraryDirectory;

/**
 * The Interface FileAction.
 */
public interface RefreshAction {

    public void startRefresh(int totalFiles);

    public void onFileProcessed(File file, LibraryDirectory dir, int processedFiles);

    public void refreshComplete();
}
