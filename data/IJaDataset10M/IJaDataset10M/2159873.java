package de.innot.avreclipse.ui.views.supportedmcu;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.ide.IDE;
import de.innot.avreclipse.AVRPlugin;
import de.innot.avreclipse.core.IMCUProvider;
import de.innot.avreclipse.core.toolinfo.Datasheets;
import de.innot.avreclipse.core.toolinfo.MCUNames;
import de.innot.avreclipse.core.util.AVRMCUidConverter;
import de.innot.avreclipse.util.URLDownloadException;
import de.innot.avreclipse.util.URLDownloadManager;

/**
 * This is an extended ColumnLabelProvider that handles URL hyperlinks.
 * <p>
 * This Class needs two {@link IMCUProvider}s, one for the Label text and one for the URL. As
 * implemented in the View these are {@link MCUNames} and {@link Datasheets} respectively.
 * </p>
 * <p>
 * As TableViewers do not support custom controls or actually anything clickable, this class is
 * implemented by adding TableEditors on top of the TableItems in this Column. The
 * {@link #updateColumn(TableViewer, TableViewerColumn)} method needs to be called to set up the
 * TableEditors. This method may only be called after the table has been filled with values (after
 * the TableViewer.setInput(model)) method has been called.
 * </p>
 * <p>
 * The TableEditors are not used as Editors, but contain an Hyperlink control each, which can be
 * clicked to download and open the URL from the given linkprovider.
 * </p>
 * 
 * @author Thomas Holland
 * @since 2.2
 */
public class URLColumnLabelProvider extends ColumnLabelProvider implements ISelectionChangedListener {

    /** The MCUProvider that provides the text to be shown in the cell */
    private final IMCUProvider fNameProvider;

    /** The IMCUProvider that provides the url to be opene */
    private final IMCUProvider fLinkProvider;

    private TableViewer fTableViewer;

    /** The last TableEditor selected. Required to de-select */
    private TableEditor fLastEditor;

    /**
	 * List of all TableEditors of this column. Required to update the TableEditors manually on a
	 * {@link SelectionChangedEvent}
	 */
    private final Map<TableItem, TableEditor> fTableEditors = new HashMap<TableItem, TableEditor>();

    /** The text color for links not yet downloaded. Value: SWT.COLOR_DARK_BLUE */
    private static Color LINK_COLOR = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);

    /** The text color for links already in the cache. Value: SWT.COLOR_MAGETA */
    private static Color LINK_IN_CACHE_COLOR = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_MAGENTA);

    /** The text color for malformed links. Value: SWT.COLOR_RED */
    private static Color LINK_MALFORMED_COLOR = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_RED);

    /**
	 * @param nameprovider
	 *            The <code>IMCUProvider</code> that returns a User readable name for a given MCU
	 *            id
	 * @param linkProvider
	 *            The <code>IMCUProvider</code> that returns the URL (as <code>String</code>)
	 *            for the datasheet for the given MCU id
	 */
    public URLColumnLabelProvider(IMCUProvider nameprovider, IMCUProvider linkProvider) {
        fNameProvider = nameprovider;
        fLinkProvider = linkProvider;
    }

    @Override
    public String getText(Object element) {
        String mcuid = (String) element;
        String info = fNameProvider.getMCUInfo(mcuid);
        return info != null ? info : "n/a";
    }

    @Override
    public void dispose() {
        if (fTableViewer != null) fTableViewer.removeSelectionChangedListener(this);
    }

    /**
	 * Set up this column for URL table cells.
	 * <p>
	 * This needs to be called <strong>after</strong> the table has been filled with rows. It will
	 * add Hyperlink Widgets on top of all cells in the column, that actually contain URLs. This is
	 * done via TableEditors for those cells.
	 * </p>
	 * <p>
	 * This also adds itself as <code>SelectionChangeListener</code> and as
	 * <code>FocusListener</code> to the given TableViewer.
	 * </p>
	 * <p>
	 * Both parameters need to refer to the same column as this ColumnLabelProvider. Passing other
	 * TableViewers or TableViewerColumns will result in undefined results.
	 * </p>
	 * 
	 * @param tableviewer
	 *            The TableViewer which contains this Column.
	 * @param viewercolumn
	 *            The TableViewerColumn for this ColumnLabelProvider
	 */
    public void updateColumn(TableViewer tableviewer, TableViewerColumn viewercolumn) {
        fTableViewer = tableviewer;
        TableColumn column = viewercolumn.getColumn();
        Table table = column.getParent();
        int index = getColumnIndex(column);
        TableItem[] allitems = table.getItems();
        for (TableItem item : allitems) {
            String mcuname = item.getText();
            String mcuid = AVRMCUidConverter.name2id(mcuname);
            if (fLinkProvider.hasMCU(mcuid)) {
                final URL url;
                final Hyperlink link = new Hyperlink(table, SWT.NONE);
                link.setText(mcuname);
                try {
                    url = new URL(fLinkProvider.getMCUInfo(mcuid));
                    link.setUnderlined(true);
                    link.setHref(url);
                    link.setToolTipText(url.toExternalForm());
                    if (URLDownloadManager.inCache(url)) {
                        link.setData(LINK_IN_CACHE_COLOR);
                    } else {
                        link.setData(LINK_COLOR);
                    }
                } catch (MalformedURLException e1) {
                    link.setUnderlined(false);
                    link.setData(LINK_MALFORMED_COLOR);
                    link.setToolTipText("Malformed Datasheet URL: " + fLinkProvider.getMCUInfo(mcuid));
                }
                link.addHyperlinkListener(new IHyperlinkListener() {

                    public void linkActivated(HyperlinkEvent event) {
                        URL url = (URL) event.getHref();
                        if (url != null) {
                            openURL(url);
                        }
                    }

                    public void linkEntered(HyperlinkEvent event) {
                    }

                    public void linkExited(HyperlinkEvent event) {
                    }
                });
                TableEditor editor = new TableEditor(table);
                editor.grabHorizontal = true;
                editor.setEditor(link, item, index);
                fTableEditors.put(item, editor);
                setEditorColors(editor, false, false);
            }
        }
        Display display = table.getDisplay();
        display.asyncExec(updateEditors);
        fTableViewer.addSelectionChangedListener(this);
        fTableViewer.getTable().addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                Table table = (Table) e.getSource();
                int index = table.getSelectionIndex();
                if (index != -1) {
                    TableItem selected = table.getItem(index);
                    TableEditor editor = fTableEditors.get(selected);
                    if (editor != null) {
                        setEditorColors(editor, true, true);
                    }
                }
            }

            public void focusLost(FocusEvent e) {
                Table table = (Table) e.getSource();
                int index = table.getSelectionIndex();
                if (index != -1) {
                    TableItem selected = table.getItem(index);
                    TableEditor editor = fTableEditors.get(selected);
                    if (editor != null) {
                        setEditorColors(editor, true, false);
                    }
                }
            }
        });
    }

    /**
	 * Sets the colors of a Hyperlink control (via the associated TableEditor).
	 * <p>
	 * A (Windows) SWT Table Cell can have three color states. These three states are covered in
	 * this method:
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * Item selected: <code>true</code>, Table has Focus: <code>true</code><br>
	 * Background: <code>SWT.COLOR_LIST_SELECTION</code><br>
	 * Foreground: <code>SWT.COLOR_LIST_SELECTION_TEXT</code>
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * Item selected: <code>true</code>, Table has Focus: <code>false</code><br>
	 * Background: <code>SWT.COLOR_WIDGET_BACKGROUND</code><br>
	 * Foreground: Link color provided by the Hyperlink Control
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * Item selected: <code>false</code>, Table has Focus: not required<br>
	 * Background: <code>SWT.COLOR_LIST_BACKGROUND</code><br>
	 * Foreground: Link color provided by the Hyperlink Control
	 * </p>
	 * </li>
	 * </ul>
	 * 
	 * @param editor
	 *            TableEdior to set the colors for.
	 * @param isselected
	 *            <code>true</code> if the editor is in a currently selected table row.
	 * @param hasfocus
	 *            <code>true</code> if the table has the focus. Not required if isselected is
	 *            <code>false</code>
	 */
    private void setEditorColors(TableEditor editor, boolean isselected, boolean hasfocus) {
        final Color background, foreground;
        final Control link = editor.getEditor();
        final Display display = link.getDisplay();
        if (isselected) {
            if (hasfocus) {
                background = display.getSystemColor(SWT.COLOR_LIST_SELECTION);
                foreground = display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
            } else {
                background = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
                foreground = (Color) link.getData();
            }
        } else {
            background = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
            foreground = (Color) link.getData();
        }
        link.setBackground(background);
        link.setForeground(foreground);
    }

    /**
	 * Gets the index of the given TableColumn in the table
	 * 
	 * @param column
	 * @return int with Table column index
	 */
    private static int getColumnIndex(TableColumn column) {
        Table table = column.getParent();
        TableColumn[] allcolumns = table.getColumns();
        int index = -1;
        for (int i = 0; i < allcolumns.length; i++) {
            if (allcolumns[i] == column) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void selectionChanged(SelectionChangedEvent event) {
        TableViewer source = (TableViewer) event.getSource();
        Table table = source.getTable();
        TableItem item = table.getItem(table.getSelectionIndex());
        if (fLastEditor != null) {
            setEditorColors(fLastEditor, false, false);
        }
        TableEditor editor = fTableEditors.get(item);
        if (editor != null) {
            setEditorColors(editor, true, item.getParent().isFocusControl());
            fLastEditor = editor;
        }
        Display display = item.getDisplay();
        display.syncExec(updateEditors);
        display.timerExec(500, updateEditors);
    }

    /**
	 * A small Runnable that will call {@link TableEditor#layout()} on all TableEditors of the
	 * column
	 */
    private final Runnable updateEditors = new Runnable() {

        public void run() {
            Collection<TableEditor> alleditors = fTableEditors.values();
            for (TableEditor e : alleditors) {
                e.layout();
            }
        }
    };

    /**
	 * Load and Display the given URL.
	 * <p>
	 * The File from the URL is first downloaded via the {@link URLDownloadManager} and then opened
	 * using the default Editor registered for this filetype.
	 * </p>
	 * <p>
	 * The download and the opening of the file is done in a Job, so this method returns immediatly.
	 * </p>
	 * <p>
	 * If a download of the same URL is still in progress, this method does nothing to avoid
	 * multiple parallel downloads of the same file by nervous users. </p
	 * 
	 * @param urlstring
	 *            A String with an URL.
	 */
    private void openURL(final URL url) {
        final Display display = PlatformUI.getWorkbench().getDisplay();
        if (URLDownloadManager.isDownloading(url)) {
            return;
        }
        Job loadandopenJob = new Job("Download and Open") {

            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                try {
                    monitor.beginTask("Download " + url.toExternalForm(), 100);
                    final File file = URLDownloadManager.download(url, new SubProgressMonitor(monitor, 95));
                    monitor.subTask("Opening Editor for " + file.getName());
                    if (display == null || display.isDisposed()) {
                        return new Status(Status.ERROR, AVRPlugin.PLUGIN_ID, "Cannot open Editor: no Display found", null);
                    }
                    openFileInEditor(file);
                    monitor.worked(5);
                } catch (URLDownloadException ude) {
                    final URLDownloadException exc = ude;
                    display.syncExec(new Runnable() {

                        public void run() {
                            Shell shell = display.getActiveShell();
                            String title = "Download Failed";
                            String message = "The requested file could not be downloaded\nFile:  " + url.getPath() + "\nHost:  " + url.getHost();
                            String reason = exc.getMessage();
                            MultiStatus status = new MultiStatus(AVRPlugin.PLUGIN_ID, 0, reason, null);
                            Throwable cause = exc.getCause();
                            while (cause != null) {
                                status.add(new Status(Status.ERROR, AVRPlugin.PLUGIN_ID, cause.getClass().getSimpleName(), cause));
                                cause = cause.getCause();
                            }
                            ErrorDialog.openError(shell, title, message, status, Status.ERROR);
                            AVRPlugin.getDefault().log(status);
                        }
                    });
                } finally {
                    monitor.done();
                }
                return Status.OK_STATUS;
            }
        };
        loadandopenJob.setUser(true);
        loadandopenJob.setPriority(Job.LONG);
        loadandopenJob.schedule();
        return;
    }

    /**
	 * Opens the given file with the standard editor.
	 * <p>
	 * An ErrorDialog is shown when the opening of the file fails.
	 * </p>
	 * 
	 * @param file
	 *            <code>java.io.File</code> with the file to open
	 * @return
	 */
    private IStatus openFileInEditor(final File file) {
        final Display display = PlatformUI.getWorkbench().getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(file.toString()));
                if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    try {
                        IDE.openEditorOnFileStore(page, fileStore);
                    } catch (PartInitException e) {
                        IStatus status = new Status(Status.ERROR, AVRPlugin.PLUGIN_ID, "Could not open " + file.toString(), e);
                        Shell shell = display.getActiveShell();
                        String title = "Can't open File";
                        String message = "The File " + file.toString() + " could not be opened";
                        ErrorDialog.openError(shell, title, message, status);
                        AVRPlugin.getDefault().log(status);
                    }
                }
            }
        });
        return Status.OK_STATUS;
    }
}
