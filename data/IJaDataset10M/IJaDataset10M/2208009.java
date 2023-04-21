package com.showdown.ui.dialog;

import java.text.MessageFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.showdown.resource.Messages;
import com.showdown.update.IUpdateCallback;
import com.showdown.update.UpdateFeeds;
import com.showdown.update.UpdateFile;
import com.showdown.update.UpdateFilesForDelete;
import com.showdown.update.UpdateJob;
import com.showdown.update.UpdateOverwriteFiles;
import com.showdown.update.UpdateParsers;
import com.showdown.update.UpdateShows;
import com.showdown.update.UpdateSite;

/**
 * Dialog for updating elements of ShowDown, from show lists to the showdown.jar file itself.
 * @author Mat DeLong
 */
public class UpdateDialog implements SelectionListener, IUpdateCallback {

    protected UpdateSite updateSite;

    protected Shell shell;

    protected Button updateButton;

    protected Button cancelButton;

    protected Composite stack;

    protected StackLayout stackLayout;

    protected Composite noUpdatesComp;

    protected TabFolder updatesTabs;

    protected Composite downloadingComp;

    protected Label downloadingLabel;

    protected UpdateJob updateJob;

    private Button parsersUseYours;

    private Button parsersUseTheirs;

    private Button feedsUseYours;

    private Button feedsUseTheirs;

    private Button showsUseYours;

    private Button showsUseTheirs;

    private Label noUpdatesLabel;

    /**
    * Constructor which specifies the URL of the update site.
    * @param updateSite the update site URL to use.
    */
    public UpdateDialog(String updateSite) {
        this.updateSite = new UpdateSite(updateSite);
        this.updateJob = new UpdateJob(this.updateSite, this);
    }

    /**
    * Returns the title to be used for this dialog
    * @return the title to use for the dialog
    */
    protected String getTitle() {
        return Messages.UpdateDialogTitle;
    }

    /**
    * Close the dialog
    */
    protected void closeDialog() {
        if (updateJob != null) {
            updateJob.cancel();
        }
        if (shell != null && !shell.isDisposed()) {
            shell.dispose();
        }
    }

    /**
    * Opens the dialog, creating the shell if required. If the shell already exists
    * then this method simply makes sure it is visible and in focus.
    */
    public final void open() {
        if (shell == null || shell.isDisposed()) {
            GridLayout layout = new GridLayout();
            layout.marginWidth = 0;
            layout.marginHeight = 0;
            layout.verticalSpacing = 0;
            shell = new Shell(Display.getDefault(), SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.RESIZE);
            shell.setText(getTitle());
            shell.setSize(500, 400);
            shell.setMinimumSize(450, 350);
            shell.setLayout(layout);
            createDialogBody(shell);
            createBottomBar(shell);
            shell.open();
            updateJob.findAvailableUpdates();
        } else {
            shell.setVisible(true);
            shell.forceActive();
            shell.forceFocus();
        }
    }

    /**
    * Creates the body of the dialog.
    * @param parent the parent composite. Assume this has a GridLayout with 1 column.
    */
    protected void createDialogBody(Composite parent) {
        this.stackLayout = new StackLayout();
        stack = new Composite(parent, SWT.NONE);
        stack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack.setLayout(this.stackLayout);
        stack.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        Composite loadingComp = createLoadingComposite(stack);
        noUpdatesComp = createNoUpdatesComposite(stack);
        updatesTabs = createUpdateTabFolder(stack);
        downloadingComp = createDownloadingComp(stack);
        stackLayout.topControl = loadingComp;
    }

    /**
    * Create composite for saying which file is being downloaded
    * @param parent the parent composite
    * @return the composite for showing what file is currently being downloaded
    */
    protected Composite createDownloadingComp(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setBackground(parent.getBackground());
        comp.setLayout(new GridLayout());
        GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        data.widthHint = 400;
        Composite inner = new Composite(comp, SWT.NONE);
        inner.setBackground(parent.getBackground());
        inner.setLayout(new GridLayout());
        inner.setLayoutData(data);
        downloadingLabel = new Label(inner, SWT.NONE);
        downloadingLabel.setBackground(parent.getBackground());
        downloadingLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        ProgressBar bar = new ProgressBar(inner, SWT.INDETERMINATE);
        bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        return comp;
    }

    /**
    * Create composite for saying no updates were found
    * @param parent the parent composite
    * @return the composite for showing no updates were found
    */
    protected Composite createNoUpdatesComposite(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setBackground(parent.getBackground());
        comp.setLayout(new GridLayout());
        GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        Composite inner = new Composite(comp, SWT.NONE);
        inner.setBackground(parent.getBackground());
        inner.setLayout(new GridLayout());
        inner.setLayoutData(data);
        noUpdatesLabel = new Label(inner, SWT.NONE);
        noUpdatesLabel.setBackground(parent.getBackground());
        noUpdatesLabel.setText(Messages.NoUpdatesFound);
        return comp;
    }

    /**
    * Creates the tab folder for showing available updates
    * @param parent the parent composite
    * @return the TabFolder for displaying updates
    */
    protected TabFolder createUpdateTabFolder(Composite parent) {
        TabFolder tabs = new TabFolder(parent, SWT.NONE);
        return tabs;
    }

    /**
    * Add the tabs for update
    */
    protected void createUpdateTabs() {
        if (updateJob != null) {
            createOverwriteTab(updateJob.getOverwriteFiles());
            createFileDeleteTab(updateJob.getFilesForDelete());
            createShowsTab(updateJob.getUpdateShows());
            createFeedsTab(updateJob.getUpdateFeeds());
            createParsersTab(updateJob.getUpdateParsers());
        }
    }

    private void createParsersTab(final UpdateParsers updateParsers) {
        if (updateParsers != null) {
            TabItem item = new TabItem(updatesTabs, SWT.NONE);
            item.setText(Messages.TabParsers);
            Composite comp = new Composite(updatesTabs, SWT.NONE);
            comp.setLayout(new GridLayout(2, false));
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.widthHint = 200;
            data.horizontalSpan = 2;
            Label label = new Label(comp, SWT.WRAP);
            label.setText(Messages.UpdateTabMessageParsers);
            label.setLayoutData(data);
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.horizontalSpan = 2;
            label = new Label(comp, SWT.NONE);
            label.setText("");
            label.setLayoutData(data);
            Button mergeButton = new Button(comp, SWT.CHECK | SWT.WRAP);
            mergeButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            mergeButton.setSelection(updateParsers.isDoMerge());
            mergeButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateParsers.setDoMerge(((Button) e.getSource()).getSelection());
                    parsersUseYours.setEnabled(updateParsers.isDoMerge());
                    parsersUseTheirs.setEnabled(updateParsers.isDoMerge());
                    parsersUseYours.setSelection(updateParsers.isUseYours());
                    parsersUseTheirs.setSelection(!updateParsers.isUseYours());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(comp, SWT.WRAP);
            label.setLayoutData(data);
            label.setText(Messages.UpdateMerge);
            data = new GridData(SWT.FILL, SWT.FILL, true, false);
            data.horizontalSpan = 2;
            GridLayout layout = new GridLayout(2, false);
            layout.marginLeft = 15;
            Composite radioComp = new Composite(comp, SWT.NONE);
            radioComp.setLayout(layout);
            radioComp.setLayoutData(data);
            parsersUseYours = new Button(radioComp, SWT.RADIO | SWT.WRAP);
            parsersUseYours.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            parsersUseYours.setEnabled(updateParsers.isDoMerge());
            parsersUseYours.setSelection(updateParsers.isUseYours());
            parsersUseYours.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateParsers.setUseYours(((Button) e.getSource()).getSelection());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(radioComp, SWT.WRAP);
            label.setText(Messages.UpdateOptionYours);
            label.setLayoutData(data);
            parsersUseTheirs = new Button(radioComp, SWT.RADIO);
            parsersUseTheirs.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            parsersUseTheirs.setEnabled(updateParsers.isDoMerge());
            parsersUseTheirs.setSelection(!updateParsers.isUseYours());
            parsersUseTheirs.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateParsers.setUseYours(!((Button) e.getSource()).getSelection());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(radioComp, SWT.WRAP);
            label.setText(Messages.UpdateOptionTheirs);
            label.setLayoutData(data);
            item.setControl(comp);
        }
    }

    private void createFeedsTab(final UpdateFeeds updateFeeds) {
        if (updateFeeds != null) {
            TabItem item = new TabItem(updatesTabs, SWT.NONE);
            item.setText(Messages.TabFees);
            Composite comp = new Composite(updatesTabs, SWT.NONE);
            comp.setLayout(new GridLayout(2, false));
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.widthHint = 200;
            data.horizontalSpan = 2;
            Label label = new Label(comp, SWT.WRAP);
            label.setText(Messages.UpdateTabMessageFeeds);
            label.setLayoutData(data);
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.horizontalSpan = 2;
            label = new Label(comp, SWT.NONE);
            label.setText("");
            label.setLayoutData(data);
            Button mergeButton = new Button(comp, SWT.CHECK | SWT.WRAP);
            mergeButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            mergeButton.setSelection(updateFeeds.isDoMerge());
            mergeButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateFeeds.setDoMerge(((Button) e.getSource()).getSelection());
                    feedsUseYours.setEnabled(updateFeeds.isDoMerge());
                    feedsUseTheirs.setEnabled(updateFeeds.isDoMerge());
                    feedsUseYours.setSelection(updateFeeds.isUseYours());
                    feedsUseTheirs.setSelection(!updateFeeds.isUseYours());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(comp, SWT.WRAP);
            label.setLayoutData(data);
            label.setText(Messages.UpdateMerge);
            data = new GridData(SWT.FILL, SWT.FILL, true, false);
            data.horizontalSpan = 2;
            GridLayout layout = new GridLayout(2, false);
            layout.marginLeft = 15;
            Composite radioComp = new Composite(comp, SWT.NONE);
            radioComp.setLayout(layout);
            radioComp.setLayoutData(data);
            feedsUseYours = new Button(radioComp, SWT.RADIO | SWT.WRAP);
            feedsUseYours.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            feedsUseYours.setEnabled(updateFeeds.isDoMerge());
            feedsUseYours.setSelection(updateFeeds.isUseYours());
            feedsUseYours.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateFeeds.setUseYours(((Button) e.getSource()).getSelection());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(radioComp, SWT.WRAP);
            label.setText(Messages.UpdateOptionYours);
            label.setLayoutData(data);
            feedsUseTheirs = new Button(radioComp, SWT.RADIO);
            feedsUseTheirs.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            feedsUseTheirs.setEnabled(updateFeeds.isDoMerge());
            feedsUseTheirs.setSelection(!updateFeeds.isUseYours());
            feedsUseTheirs.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateFeeds.setUseYours(!((Button) e.getSource()).getSelection());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(radioComp, SWT.WRAP);
            label.setText(Messages.UpdateOptionTheirs);
            label.setLayoutData(data);
            item.setControl(comp);
        }
    }

    private void createShowsTab(final UpdateShows updateShows) {
        if (updateShows != null) {
            TabItem item = new TabItem(updatesTabs, SWT.NONE);
            item.setText(Messages.TabShows);
            Composite comp = new Composite(updatesTabs, SWT.NONE);
            comp.setLayout(new GridLayout(2, false));
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.widthHint = 200;
            data.horizontalSpan = 2;
            Label label = new Label(comp, SWT.WRAP);
            label.setText(Messages.UpdateTabMessageShows);
            label.setLayoutData(data);
            data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.horizontalSpan = 2;
            label = new Label(comp, SWT.NONE);
            label.setText("");
            label.setLayoutData(data);
            Button mergeButton = new Button(comp, SWT.CHECK | SWT.WRAP);
            mergeButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            mergeButton.setSelection(updateShows.isDoMerge());
            mergeButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateShows.setDoMerge(((Button) e.getSource()).getSelection());
                    showsUseYours.setEnabled(updateShows.isDoMerge());
                    showsUseTheirs.setEnabled(updateShows.isDoMerge());
                    showsUseYours.setSelection(updateShows.isUseYours());
                    showsUseTheirs.setSelection(!updateShows.isUseYours());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(comp, SWT.WRAP);
            label.setLayoutData(data);
            label.setText(Messages.UpdateMerge);
            data = new GridData(SWT.FILL, SWT.FILL, true, false);
            data.horizontalSpan = 2;
            GridLayout layout = new GridLayout(2, false);
            layout.marginLeft = 15;
            Composite radioComp = new Composite(comp, SWT.NONE);
            radioComp.setLayout(layout);
            radioComp.setLayoutData(data);
            showsUseYours = new Button(radioComp, SWT.RADIO | SWT.WRAP);
            showsUseYours.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            showsUseYours.setEnabled(updateShows.isDoMerge());
            showsUseYours.setSelection(updateShows.isUseYours());
            showsUseYours.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateShows.setUseYours(((Button) e.getSource()).getSelection());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(radioComp, SWT.WRAP);
            label.setText(Messages.UpdateOptionYours);
            label.setLayoutData(data);
            showsUseTheirs = new Button(radioComp, SWT.RADIO);
            showsUseTheirs.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false));
            showsUseTheirs.setEnabled(updateShows.isDoMerge());
            showsUseTheirs.setSelection(!updateShows.isUseYours());
            showsUseTheirs.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    updateShows.setUseYours(!((Button) e.getSource()).getSelection());
                }
            });
            data = new GridData(SWT.FILL, SWT.TOP, true, false);
            data.widthHint = 200;
            label = new Label(radioComp, SWT.WRAP);
            label.setText(Messages.UpdateOptionTheirs);
            label.setLayoutData(data);
            item.setControl(comp);
        }
    }

    private void createFileDeleteTab(final UpdateFilesForDelete filesForDelete) {
        if (filesForDelete != null && !filesForDelete.getPaths().isEmpty()) {
            TabItem item = new TabItem(updatesTabs, SWT.NONE);
            item.setText(Messages.TabFilesToDelete);
            ScrolledComposite scroll = new ScrolledComposite(updatesTabs, SWT.H_SCROLL | SWT.V_SCROLL);
            Composite comp = new Composite(scroll, SWT.NONE);
            comp.setLayout(new GridLayout());
            scroll.setContent(comp);
            scroll.setExpandHorizontal(true);
            scroll.setExpandVertical(true);
            scroll.setMinSize(200, 200);
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.widthHint = 200;
            Label label = new Label(comp, SWT.WRAP);
            label.setText(Messages.UpdateTabMessageDelete);
            label.setLayoutData(data);
            data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.heightHint = 200;
            final Table table = new Table(comp, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
            table.setSize(200, 200);
            table.setLayoutData(data);
            table.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event event) {
                    if (event.detail == SWT.CHECK) {
                        if (event.item.getData() instanceof String) {
                            String path = event.item.getData().toString();
                            boolean checked = ((TableItem) event.item).getChecked();
                            filesForDelete.setFileValue(path, checked);
                        }
                    }
                }
            });
            Menu menu = new Menu(Display.getDefault().getActiveShell(), SWT.POP_UP);
            table.setMenu(menu);
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(Messages.SelectAll);
            menuItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    for (String s : filesForDelete.getPaths()) {
                        filesForDelete.setFileValue(s, true);
                    }
                    for (TableItem ti : table.getItems()) {
                        ti.setChecked(true);
                    }
                }
            });
            menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(Messages.SelectNone);
            menuItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    for (String s : filesForDelete.getPaths()) {
                        filesForDelete.setFileValue(s, false);
                    }
                    for (TableItem ti : table.getItems()) {
                        ti.setChecked(false);
                    }
                }
            });
            for (String path : filesForDelete.getPaths()) {
                TableItem row = new TableItem(table, SWT.NONE);
                row.setText(path);
                row.setChecked(filesForDelete.getValue(path));
                row.setData(path);
            }
            item.setControl(scroll);
        }
    }

    private void createOverwriteTab(final UpdateOverwriteFiles overwriteFiles) {
        if (overwriteFiles != null && !overwriteFiles.getFiles().isEmpty()) {
            TabItem item = new TabItem(updatesTabs, SWT.NONE);
            item.setText(Messages.TabFilesToReplace);
            ScrolledComposite scroll = new ScrolledComposite(updatesTabs, SWT.H_SCROLL | SWT.V_SCROLL);
            Composite comp = new Composite(scroll, SWT.NONE);
            comp.setLayout(new GridLayout());
            scroll.setContent(comp);
            scroll.setExpandHorizontal(true);
            scroll.setExpandVertical(true);
            scroll.setMinSize(200, 200);
            GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
            data.widthHint = 200;
            Label label = new Label(comp, SWT.WRAP);
            label.setText(Messages.UpdateTabMessageOverwrite);
            label.setLayoutData(data);
            data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.heightHint = 200;
            final Table table = new Table(comp, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
            table.setSize(200, 200);
            table.setLayoutData(data);
            table.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event event) {
                    if (event.detail == SWT.CHECK) {
                        if (event.item.getData() instanceof UpdateFile) {
                            UpdateFile uf = (UpdateFile) event.item.getData();
                            boolean checked = ((TableItem) event.item).getChecked();
                            overwriteFiles.setFileValue(uf, checked);
                        }
                    }
                }
            });
            Menu menu = new Menu(Display.getDefault().getActiveShell(), SWT.POP_UP);
            table.setMenu(menu);
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(Messages.SelectAll);
            menuItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    for (UpdateFile f : overwriteFiles.getFiles()) {
                        overwriteFiles.setFileValue(f, true);
                    }
                    for (TableItem ti : table.getItems()) {
                        ti.setChecked(true);
                    }
                }
            });
            menuItem = new MenuItem(menu, SWT.PUSH);
            menuItem.setText(Messages.SelectNone);
            menuItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    for (UpdateFile f : overwriteFiles.getFiles()) {
                        overwriteFiles.setFileValue(f, false);
                    }
                    for (TableItem ti : table.getItems()) {
                        ti.setChecked(false);
                    }
                }
            });
            for (UpdateFile uf : overwriteFiles.getFiles()) {
                TableItem row = new TableItem(table, SWT.NONE);
                row.setText(uf.getPath());
                row.setChecked(overwriteFiles.getValue(uf));
                row.setData(uf);
            }
            item.setControl(scroll);
        }
    }

    /**
    * Creates the composite for showing a loading bar, showing that a job is
    * running checking for updates.
    * @param parent the parent composite
    * @return the created composite
    */
    protected Composite createLoadingComposite(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setBackground(parent.getBackground());
        comp.setLayout(new GridLayout());
        GridData data = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        data.widthHint = 300;
        Composite inner = new Composite(comp, SWT.NONE);
        inner.setBackground(parent.getBackground());
        inner.setLayout(new GridLayout());
        inner.setLayoutData(data);
        Label messageLabel = new Label(inner, SWT.NONE);
        messageLabel.setBackground(parent.getBackground());
        messageLabel.setText(Messages.CheckingForUpdates);
        ProgressBar bar = new ProgressBar(inner, SWT.INDETERMINATE);
        bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        return comp;
    }

    /**
    * Creates the bottom button bar of the dialog
    * @param parent the parent composite
    */
    protected void createBottomBar(Composite parent) {
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.marginTop = 1;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        Composite bottomWrap = new Composite(parent, SWT.NONE);
        bottomWrap.setLayoutData(data);
        bottomWrap.setLayout(layout);
        bottomWrap.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
        Composite bottomBar = new Composite(bottomWrap, SWT.NONE);
        bottomBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        bottomBar.setLayout(new GridLayout(2, false));
        cancelButton = new Button(bottomBar, SWT.PUSH);
        cancelButton.setText(Messages.Cancel);
        cancelButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        cancelButton.addSelectionListener(this);
        updateButton = new Button(bottomBar, SWT.PUSH);
        updateButton.setText(Messages.DoUpdate);
        updateButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        updateButton.addSelectionListener(this);
        updateButton.setEnabled(false);
    }

    /**
    * {@inheritDoc}
    */
    public void widgetDefaultSelected(SelectionEvent event) {
    }

    /**
    * {@inheritDoc}
    */
    public void widgetSelected(SelectionEvent event) {
        Object source = event.getSource();
        if (source == cancelButton) {
            closeDialog();
        } else if (source == updateButton) {
            if (updateJob != null) {
                stackLayout.topControl = downloadingComp;
                stack.layout();
                updateJob.downloadSelectedUpdates();
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    public void updateCheckComplete(final boolean hasUpdates) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                if (!hasUpdates) {
                    String msg = (updateSite == null || !updateSite.isValid()) ? Messages.InvalidUpdateSite : Messages.NoUpdatesFound;
                    noUpdatesLabel.setText(msg);
                    noUpdatesLabel.getParent().getParent().layout();
                    stackLayout.topControl = noUpdatesComp;
                } else {
                    createUpdateTabs();
                    stackLayout.topControl = updatesTabs;
                }
                stack.layout();
                updateButton.setEnabled(hasUpdates);
            }
        });
    }

    /**
    * {@inheritDoc}
    */
    public void downloadingFile(final String filePath) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                if (downloadingLabel != null && !downloadingLabel.isDisposed()) {
                    downloadingLabel.setText(MessageFormat.format(Messages.DownloadFile, filePath));
                }
            }
        });
    }

    /**
    * {@inheritDoc}
    */
    public void updatesDownloadComplete(final boolean successful) {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                if (downloadingLabel != null && !downloadingLabel.isDisposed()) {
                    String message = successful ? Messages.UpdateDownloadSuccess : Messages.UpdateDownloadFailure;
                    downloadingLabel.setText(message);
                    if (!successful) {
                        updateButton.setEnabled(false);
                    }
                }
            }
        });
    }
}
