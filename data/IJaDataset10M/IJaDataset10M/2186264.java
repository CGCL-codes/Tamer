package com.apachetune.httpserver.ui.searchserver;

import com.apachetune.core.ResourceManager;
import com.apachetune.core.WorkItem;
import com.apachetune.core.ui.Presenter;
import com.apachetune.httpserver.HttpServerManager;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import static com.apachetune.core.utils.Utils.createRuntimeException;
import static com.apachetune.httpserver.Constants.SERVER_PATH_SELECTED_EVENT;
import static java.util.Arrays.asList;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public class SearchServerPresenter implements Presenter<SearchServerDialog> {

    private final ResultListModel resultListModel = new ResultListModel();

    private final JFrame mainFrame;

    private final HttpServerManager httpServerManager;

    private SearchWorker searchWorker = new SearchWorker();

    private WorkItem workItem;

    private SearchServerDialog view;

    private File selectedServerPath;

    private boolean isSearchMode;

    private final ResourceBundle resourceBundle = ResourceManager.getInstance().getResourceBundle(SearchServerPresenter.class);

    @Inject
    public SearchServerPresenter(JFrame mainFrame, HttpServerManager httpServerManager) {
        this.mainFrame = mainFrame;
        this.httpServerManager = httpServerManager;
    }

    public void initialize(WorkItem workItem, SearchServerDialog view) {
        notNull(workItem, "Argument workItem cannot be a null");
        notNull(view, "Argument view cannot be a null");
        this.workItem = workItem;
        this.view = view;
        view.setResultListModel(resultListModel);
        view.setDrivesAvailableToSearch(getAvailableDrivesToSearch());
        updateViewToSearchMode(false);
        updateSelectButtonCtrl();
    }

    @Override
    public void dispose() {
    }

    private Collection<File> getAvailableDrivesToSearch() {
        return Collections2.filter(asList(File.listRoots()), new Predicate<File>() {

            public boolean apply(File file) {
                return file.canWrite();
            }
        });
    }

    public void onStartSearch() {
        startSearch();
    }

    public void onStopSearch() {
        stopSearch();
    }

    public void onResultSelected(int selectionIdx) {
        if ((selectionIdx != -1) && (selectionIdx < resultListModel.getSize())) {
            selectedServerPath = resultListModel.getElementAt(selectionIdx);
        } else {
            selectedServerPath = null;
        }
        updateSelectButtonCtrl();
    }

    public void onSelectServer() {
        if (isSearchMode) {
            if (JOptionPane.showConfirmDialog(mainFrame, resourceBundle.getString("searchServerPresenter.onSelectServer.confirmDialog.message"), resourceBundle.getString("searchServerPresenter.onSelectServer.confirmDialog.title"), YES_NO_OPTION) == NO_OPTION) {
                return;
            }
        }
        stopSearch();
        view.dispose();
        workItem.raiseEvent(SERVER_PATH_SELECTED_EVENT, selectedServerPath.getAbsolutePath());
    }

    public void onCancel() {
        if (isSearchMode) {
            if (JOptionPane.showConfirmDialog(mainFrame, resourceBundle.getString("searchServerPresenter.onCancel.confirmDialog.message"), resourceBundle.getString("searchServerPresenter.onCancel.confirmDialog.title"), YES_NO_OPTION) == NO_OPTION) {
                return;
            }
        }
        stopSearch();
        view.dispose();
    }

    private void updateViewToSearchMode(boolean isSearchMode) {
        view.setSourceTableEnabled(!isSearchMode);
        view.setStartSearchButtonEnabled(!isSearchMode);
        view.setStopSearchButtonEnabled(isSearchMode);
        view.setSearchProgressBarRun(isSearchMode);
        if (!isSearchMode) {
            view.setCurrentSearchLocationText("");
        }
    }

    private void updateSelectButtonCtrl() {
        view.setSelectServerButtonEnabled(selectedServerPath != null);
    }

    private void startSearch() {
        if (isSearchMode) {
            return;
        }
        view.clearSearchResults();
        view.setSelectServerButtonEnabled(false);
        updateViewToSearchMode(true);
        searchInDefaultInstallDirectory();
        searchInPathSystemVariable();
        if (view.getSelectedDrivesToSearch().size() != 0) {
            searchWorker = new SearchWorker();
            searchWorker.startSearch();
            isSearchMode = true;
        } else {
            updateViewToSearchMode(false);
            isSearchMode = false;
            if (resultListModel.getSize() == 0) {
                showServersNotFoundMessage();
            }
        }
    }

    private void stopSearch() {
        if (!isSearchMode) {
            return;
        }
        searchWorker.stopSearch();
        try {
            searchWorker.get();
        } catch (InterruptedException e) {
            throw createRuntimeException(e);
        } catch (ExecutionException e) {
            throw createRuntimeException(e);
        }
        searchWorker = null;
        updateViewToSearchMode(false);
        isSearchMode = false;
    }

    private void searchInDefaultInstallDirectory() {
        File defaultDirectory = httpServerManager.getServerDefaultInstallDirectory();
        if (httpServerManager.isHttpServerRootDirectory(defaultDirectory)) {
            addPathToResultList(defaultDirectory);
        }
    }

    private void searchInPathSystemVariable() {
        List<String> paths = asList(defaultString(System.getenv("PATH")).split("" + File.pathSeparatorChar));
        for (String path : paths) {
            path = StringUtils.removeEnd(path, File.separatorChar + "bin");
            if (path.length() > 0) {
                File pathDir = new File(path);
                if (httpServerManager.isHttpServerRootDirectory(pathDir)) {
                    addPathToResultList(pathDir);
                }
            }
        }
    }

    private void addPathToResultList(File pathDir) {
        if (!resultListModel.exists(pathDir)) {
            resultListModel.add(pathDir);
        }
        if (resultListModel.getSize() == 1) {
            view.selectFirstResult();
        }
    }

    private void showServersNotFoundMessage() {
        JOptionPane.showMessageDialog(mainFrame, resourceBundle.getString("searchServerPresenter.showServersNotFoundMessage.message"), resourceBundle.getString("searchServerPresenter.showServersNotFoundMessage.title"), INFORMATION_MESSAGE);
    }

    class SearchWorker extends SwingWorker<Boolean, Void> implements FileScannerListener {

        private List<File> selectedDrivesToSearch;

        private boolean needCancel;

        public void startSearch() {
            execute();
        }

        public void stopSearch() {
            needCancel = true;
        }

        public boolean currentDirectory(final File directory) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    view.setCurrentSearchLocationText(directory.getAbsolutePath());
                }
            });
            return !needCancel;
        }

        public boolean currentFile(File file) {
            notNull(file, "Argument file cannot be a null");
            if (file.getName().equals("httpd.exe")) {
                final File serverRoot = getServerRootByHttpdFile(file);
                if (serverRoot != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            addPathToResultList(serverRoot);
                        }
                    });
                }
            }
            return !needCancel;
        }

        protected Boolean doInBackground() throws Exception {
            boolean isCancelled = false;
            selectedDrivesToSearch = view.getSelectedDrivesToSearch();
            if (selectedDrivesToSearch.size() == 0) {
                return null;
            }
            for (File drive : selectedDrivesToSearch) {
                isCancelled = !new FileScanner(drive, this, new WildcardFileFilter("*.exe")).execute();
                if (isCancelled) {
                    break;
                }
            }
            return !isCancelled;
        }

        @Override
        protected void done() {
            updateViewToSearchMode(false);
            isSearchMode = false;
            try {
                boolean isCancelled = !get();
                if (!isCancelled && (resultListModel.getSize() == 0)) {
                    showServersNotFoundMessage();
                }
            } catch (InterruptedException e) {
                throw createRuntimeException(e);
            } catch (ExecutionException e) {
                throw createRuntimeException(e);
            }
        }

        private File getServerRootByHttpdFile(File httpdFile) {
            final File serverBinDirectory = httpdFile.getParentFile();
            if ((serverBinDirectory != null) && serverBinDirectory.getName().equals("bin")) {
                File serverRootDirectory = serverBinDirectory.getParentFile();
                if ((serverRootDirectory != null) || httpServerManager.isHttpServerRootDirectory(serverRootDirectory)) {
                    return serverRootDirectory;
                }
            }
            return null;
        }
    }
}

class FileScanner {

    private final File root;

    private final FileFilter fileFilter;

    private final FileScannerListener listener;

    public FileScanner(File root, FileScannerListener listener, FileFilter fileFilter) {
        notNull(root, "Argument root cannot be a null");
        isTrue(root.isDirectory(), "Argument root should be a directory [root = " + root + "; this = " + this + "]");
        this.root = root;
        this.fileFilter = fileFilter;
        this.listener = listener;
    }

    public boolean execute() {
        notNull(listener == null, "Argument listener cannot be a null");
        return scanDirectory(root);
    }

    private boolean scanDirectory(File directory) {
        boolean isScanCancelled = !listener.currentDirectory(directory);
        if (isScanCancelled) {
            return false;
        }
        isScanCancelled = !scanFiles(directory);
        if (isScanCancelled) {
            return false;
        }
        File[] subDirectories = directory.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        List<File> subDirectoryList = asList((subDirectories != null) ? subDirectories : new File[] {});
        for (File subDirectory : subDirectoryList) {
            isScanCancelled = !scanDirectory(subDirectory);
            if (isScanCancelled) {
                return false;
            }
        }
        return true;
    }

    private boolean scanFiles(File directory) {
        File[] files = directory.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isFile() && ((fileFilter == null) || fileFilter.accept(pathname));
            }
        });
        List<File> fileList = asList((files != null) ? files : new File[] {});
        for (File file : fileList) {
            boolean isCancelled = !listener.currentFile(file);
            if (isCancelled) {
                return false;
            }
        }
        return true;
    }
}

interface FileScannerListener {

    boolean currentDirectory(File directory);

    boolean currentFile(File file);
}
