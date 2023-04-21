package org.m4eclipse.eclipse.actions;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.m4eclipse.M4EclipsePlugin;
import org.m4eclipse.maven.Dependency;
import org.m4eclipse.maven.index.Indexer;
import org.m4eclipse.maven.index.Indexer.FileInfo;

/**
 * RepositorySearchDialog
 */
public class RepositorySearchDialog extends SelectionStatusDialog {

    private static final String DIALOG_SETTINGS = RepositorySearchDialog.class.getName();

    private static final String KEY_WIDTH = "width";

    private static final String KEY_HEIGHT = "height";

    private static final String KEY_X = "x";

    private static final String KEY_Y = "y";

    private IDialogSettings settings;

    private Point location;

    private Point size;

    Text searchText = null;

    TreeViewer searchResultViewer = null;

    Set artifacts;

    SearchJob searchJob;

    private String queryText;

    private String queryField;

    public RepositorySearchDialog(Shell parent, Collection artifacts, String queryField) {
        super(parent);
        this.artifacts = new HashSet();
        this.artifacts.addAll(artifacts);
        this.queryField = queryField;
        setShellStyle(getShellStyle() | SWT.RESIZE);
        setStatusLineAboveButtons(true);
        setTitle("Repository Search");
        IDialogSettings pluginSettings = M4EclipsePlugin.getDefault().getDialogSettings();
        IDialogSettings settings = pluginSettings.getSection(DIALOG_SETTINGS);
        if (settings == null) {
            settings = new DialogSettings(DIALOG_SETTINGS);
            settings.put(KEY_WIDTH, 480);
            settings.put(KEY_HEIGHT, 450);
            pluginSettings.addSection(settings);
        }
        this.settings = settings;
    }

    public void setQuery(String query) {
        this.queryText = query;
    }

    protected Point getInitialSize() {
        Point result = super.getInitialSize();
        if (size != null) {
            result.x = Math.max(result.x, size.x);
            result.y = Math.max(result.y, size.y);
            Rectangle display = getShell().getDisplay().getClientArea();
            result.x = Math.min(result.x, display.width);
            result.y = Math.min(result.y, display.height);
        }
        return result;
    }

    protected Point getInitialLocation(Point initialSize) {
        Point result = super.getInitialLocation(initialSize);
        if (location != null) {
            result.x = location.x;
            result.y = location.y;
            Rectangle display = getShell().getDisplay().getClientArea();
            int xe = result.x + initialSize.x;
            if (xe > display.width) {
                result.x -= xe - display.width;
            }
            int ye = result.y + initialSize.y;
            if (ye > display.height) {
                result.y -= ye - display.height;
            }
        }
        return result;
    }

    public boolean close() {
        writeSettings();
        return super.close();
    }

    /**
     * Initializes itself from the dialog settings with the same state as at the previous invocation.
     */
    private void readSettings() {
        try {
            int x = settings.getInt(KEY_X);
            int y = settings.getInt(KEY_Y);
            location = new Point(x, y);
        } catch (NumberFormatException e) {
            location = null;
        }
        try {
            int width = settings.getInt(KEY_WIDTH);
            int height = settings.getInt(KEY_HEIGHT);
            size = new Point(width, height);
        } catch (NumberFormatException e) {
            size = null;
        }
    }

    /**
     * Stores it current configuration in the dialog store.
     */
    private void writeSettings() {
        Point location = getShell().getLocation();
        settings.put(KEY_X, location.x);
        settings.put(KEY_Y, location.y);
        Point size = getShell().getSize();
        settings.put(KEY_WIDTH, size.x);
        settings.put(KEY_HEIGHT, size.y);
    }

    protected Control createDialogArea(Composite parent) {
        readSettings();
        Composite composite = (Composite) super.createDialogArea(parent);
        Label searchTextlabel = new Label(composite, SWT.NONE);
        searchTextlabel.setText("Query:");
        searchTextlabel.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        searchText = new Text(composite, SWT.BORDER);
        if (queryText != null) {
            searchText.setText(queryText);
        }
        searchText.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
        searchText.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ARROW_DOWN) {
                    searchResultViewer.getTree().setFocus();
                }
            }
        });
        searchText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                scheduleSearch(((Text) e.widget).getText());
            }
        });
        Label searchResultsLabel = new Label(composite, SWT.NONE);
        searchResultsLabel.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        searchResultsLabel.setText("Search Results:");
        Tree tree = new Tree(composite, SWT.BORDER | SWT.MULTI);
        tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final HashSet artifactKeys = new HashSet();
        for (Iterator it = artifacts.iterator(); it.hasNext(); ) {
            Dependency a = (Dependency) it.next();
            artifactKeys.add(a.getGroupId() + ":" + a.getArtifactId());
            artifactKeys.add(a.getGroupId() + ":" + a.getArtifactId() + ":" + a.getVersion());
        }
        searchResultViewer = new TreeViewer(tree);
        searchResultViewer.setContentProvider(new SearchResultContentProvider());
        searchResultViewer.setLabelProvider(new SearchResultLabelProvider(artifactKeys));
        searchResultViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (!selection.isEmpty()) {
                    Indexer.FileInfo f = getSelectedFileInfo(selection.getFirstElement());
                    int severity = artifactKeys.contains(f.group + ":" + f.artifact) ? IStatus.ERROR : IStatus.OK;
                    updateStatus(new Status(severity, M4EclipsePlugin.PLUGIN_ID, -1, f.name + " " + f.size + " " + f.date, null));
                } else {
                    updateStatus(new Status(IStatus.ERROR, M4EclipsePlugin.PLUGIN_ID, -1, "No selection", null));
                }
            }
        });
        searchResultViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                buttonPressed(IDialogConstants.OK_ID);
            }
        });
        updateStatus(new Status(IStatus.ERROR, M4EclipsePlugin.PLUGIN_ID, -1, "", null));
        scheduleSearch(queryText);
        return composite;
    }

    protected void computeResult() {
        IStructuredSelection selection = (IStructuredSelection) searchResultViewer.getSelection();
        setResult(Collections.singletonList(getSelectedFileInfo(selection.getFirstElement())));
    }

    protected FileInfo getSelectedFileInfo(Object element) {
        if (element instanceof Indexer.ArtifactInfo) {
            return (FileInfo) ((Indexer.ArtifactInfo) element).files.iterator().next();
        }
        return (FileInfo) element;
    }

    protected void scheduleSearch(String query) {
        if (query != null && query.length() > 0) {
            if (searchJob == null) {
                File[] indexes = M4EclipsePlugin.getDefault().getMavenManager().getIndexer().getIndexes();
                searchJob = new SearchJob(queryField, indexes, this);
            }
            searchJob.setQuery(query.toLowerCase());
            if (!searchJob.isRunning()) {
                searchJob.schedule();
            }
        }
    }

    protected void setSearchResult(Map searchResult) {
        if (this.searchResultViewer.getContentProvider() != null) {
            this.searchResultViewer.setInput(searchResult);
        }
    }

    private static class SearchJob extends Job {

        final RepositorySearchDialog dialog;

        final Indexer indexer;

        private File[] indexes;

        private String query;

        private String field;

        boolean isRunning = false;

        public SearchJob(String field, File[] indexes, RepositorySearchDialog dialog) {
            super("Repository search");
            this.field = field;
            this.indexer = new Indexer();
            this.indexes = indexes;
            this.dialog = dialog;
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        protected IStatus run(IProgressMonitor monitor) {
            isRunning = true;
            while (!monitor.isCanceled() && (this.query != null)) {
                String activeQuery = query;
                this.query = null;
                try {
                    Map res = indexer.search(indexes, activeQuery, field);
                    if (!res.isEmpty() || (activeQuery.length() == 0)) {
                        setResult(new Status(IStatus.OK, M4EclipsePlugin.PLUGIN_ID, -1, "Result for: " + activeQuery, null), res, monitor);
                    }
                } catch (final RuntimeException ex) {
                    setResult(new Status(IStatus.ERROR, M4EclipsePlugin.PLUGIN_ID, -1, "Search error: " + ex.toString(), null), Collections.EMPTY_MAP, monitor);
                } catch (final Exception ex) {
                    setResult(new Status(IStatus.ERROR, M4EclipsePlugin.PLUGIN_ID, -1, "Search error: " + ex.getMessage(), null), Collections.EMPTY_MAP, monitor);
                }
            }
            isRunning = false;
            return Status.OK_STATUS;
        }

        private void setResult(final Status status, final Map result, IProgressMonitor monitor) {
            if (!monitor.isCanceled()) {
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        dialog.updateStatus(status);
                        dialog.setSearchResult(result);
                    }
                });
            }
        }
    }

    public static class SearchResultLabelProvider extends LabelProvider implements IColorProvider {

        private final Set artifactKeys;

        public SearchResultLabelProvider(Set artifactKeys) {
            this.artifactKeys = artifactKeys;
        }

        public String getText(Object element) {
            if (element instanceof Indexer.ArtifactInfo) {
                Indexer.ArtifactInfo a = (Indexer.ArtifactInfo) element;
                return (a.className == null ? "" : a.className + "   " + a.packageName + "   ") + a.group + "   " + a.artifact;
            } else if (element instanceof Indexer.FileInfo) {
                Indexer.FileInfo f = (Indexer.FileInfo) element;
                return f.version + " - " + f.name + " - " + f.size + " - " + f.date + " [" + f.repository + "]";
            }
            return super.getText(element);
        }

        public Color getForeground(Object element) {
            if (element instanceof Indexer.FileInfo) {
                Indexer.FileInfo f = (FileInfo) element;
                if (artifactKeys.contains(f.group + ":" + f.artifact + ":" + f.version)) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
                return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
            } else if (element instanceof Indexer.ArtifactInfo) {
                Indexer.ArtifactInfo i = (Indexer.ArtifactInfo) element;
                if (artifactKeys.contains(i.group + ":" + i.artifact)) {
                    return Display.getDefault().getSystemColor(SWT.COLOR_RED);
                }
            }
            return null;
        }

        public Color getBackground(Object element) {
            return null;
        }
    }

    public static class SearchResultContentProvider implements ITreeContentProvider {

        private static Object[] EMPTY = new Object[0];

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof Map) {
                return ((Map) inputElement).values().toArray();
            }
            return EMPTY;
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof Indexer.ArtifactInfo) {
                Indexer.ArtifactInfo a = (Indexer.ArtifactInfo) parentElement;
                return a.files.toArray();
            }
            return EMPTY;
        }

        public boolean hasChildren(Object element) {
            return element instanceof Indexer.ArtifactInfo;
        }

        public Object getParent(Object element) {
            return null;
        }

        public void dispose() {
        }
    }
}
