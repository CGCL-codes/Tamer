package it.battlehorse.rcp.tools.dbbase.preferences;

import it.battlehorse.rcp.sl.annot.Injected;
import it.battlehorse.rcp.sl.annot.Serviceable;
import it.battlehorse.rcp.tools.dbbase.DatasourceConfig;
import it.battlehorse.rcp.tools.dbbase.DbBaseActivator;
import it.battlehorse.rcp.tools.dbbase.DbConfig;
import it.battlehorse.rcp.tools.dbbase.IDbManager;
import it.battlehorse.rcp.tools.dbbase.test.ConnectionTester;
import java.util.List;
import java.util.concurrent.Executors;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * The preference page used to choose the DbBase plugin Preferences.
 * 
 * @author battlehorse
 * @since Nov 20, 2005
 */
@Serviceable
public class DbPreferences extends PreferencePage implements IWorkbenchPreferencePage {

    private DbTableContentProvider dbTableContentProvider;

    private TableViewer dbTable;

    private Image keyImage;

    private IDbManager dbManager;

    /**
	 * Injects a db manager into this object
	 * 
	 * @param manager a db manager
	 */
    @Injected
    public void setDbManager(IDbManager manager) {
        this.dbManager = manager;
    }

    /**
	 * Creates a new instance of this preference page
	 */
    public DbPreferences() {
        super();
    }

    /**
	 * Creates a new instance of this preference page with the given title
	 * 
	 * @param title the page title
	 */
    public DbPreferences(String title) {
        super(title);
    }

    /**
	 * Creates a new instance of this preference page with the given title and icon
	 * 
	 * @param title the page title
	 * @param image the page icon
	 */
    public DbPreferences(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    protected Control createContents(Composite parent) {
        IPreferenceStore store = DbBaseActivator.getDefault().getPreferenceStore();
        boolean isDbIdle = dbManager.isIdle();
        Composite c = new Composite(parent, SWT.NONE);
        c.setLayout(new GridLayout(1, true));
        new Label(c, SWT.NONE).setText("The following databases are supported:");
        Table t = new Table(c, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        t.setHeaderVisible(true);
        t.setLinesVisible(true);
        t.setEnabled(isDbIdle);
        new TableColumn(t, SWT.LEFT).setText("Icon");
        new TableColumn(t, SWT.LEFT).setText("Database");
        new TableColumn(t, SWT.LEFT).setText("JDBC Driver");
        boolean hasSupportedDb = false;
        List<DbConfig> configs = dbManager.getDbConfigList();
        if (configs != null) for (DbConfig cfg : configs) {
            TableItem item = new TableItem(t, SWT.NONE);
            item.setImage(0, DbBaseActivator.getDefault().getDatabaseImage(cfg.getId(), cfg.getIcon()));
            item.setText(1, cfg.getName());
            item.setText(2, cfg.getDriverClassName());
            hasSupportedDb = true;
        }
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumn(i).pack();
        }
        new Label(c, SWT.NONE).setText("If you want to add more drivers, " + "please refer to the platform help pages");
        Composite spacer = new Composite(c, SWT.NONE);
        GridData gd = new GridData(SWT.DEFAULT, 10);
        spacer.setLayoutData(gd);
        new Label(c, SWT.NONE).setText("The following datasources are configured:");
        Composite tableBar = new Composite(c, SWT.NONE);
        tableBar.setLayout(new GridLayout(2, false));
        dbTable = new TableViewer(tableBar, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        Table innerT = dbTable.getTable();
        TableColumn tc = new TableColumn(innerT, SWT.LEFT);
        tc.setText("Datasource name");
        keyImage = DbBaseActivator.getImageDescriptor("resources/key.png").createImage();
        tc.setImage(keyImage);
        new TableColumn(innerT, SWT.LEFT).setText("Database");
        new TableColumn(innerT, SWT.LEFT).setText("Connection URL");
        new TableColumn(innerT, SWT.LEFT).setText("User");
        new TableColumn(innerT, SWT.LEFT).setText("Password");
        for (TableColumn col : innerT.getColumns()) col.pack();
        innerT.setLinesVisible(true);
        innerT.setHeaderVisible(true);
        innerT.setEnabled(isDbIdle);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        innerT.setLayoutData(gd);
        dbTableContentProvider = new DbTableContentProvider(this);
        dbTable.setContentProvider(dbTableContentProvider);
        dbTable.setLabelProvider(new DbTableLabelProvider());
        dbTable.setInput(store);
        Composite buttonBar = new Composite(tableBar, SWT.NONE);
        buttonBar.setLayout(new GridLayout(1, true));
        Button b;
        b = new Button(buttonBar, SWT.PUSH);
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        b.setText("&New Datasource");
        b.setLayoutData(gd);
        b.setEnabled(isDbIdle);
        b.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DatasourceEditDialog dialog = new DatasourceEditDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), true, dbTableContentProvider.getSourceNames());
                dialog.open();
                if (dialog.getDatasourceConfig() != null) {
                    dbTableContentProvider.addDatasourceConfig(dialog.getDatasourceConfig(), false);
                }
            }
        });
        b = new Button(buttonBar, SWT.PUSH);
        b.setText("&Edit Selected");
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        b.setLayoutData(gd);
        b.setEnabled(isDbIdle);
        b.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = (IStructuredSelection) dbTable.getSelection();
                if (sel != null && sel.getFirstElement() != null) {
                    DatasourceEditDialog dialog = new DatasourceEditDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, (DatasourceConfig) sel.getFirstElement(), dbTableContentProvider.getSourceNames());
                    dialog.open();
                    if (dialog.getDatasourceConfig() != null) {
                        dbTableContentProvider.addDatasourceConfig(dialog.getDatasourceConfig(), true);
                    }
                }
            }
        });
        b = new Button(buttonBar, SWT.PUSH);
        b.setText("&Delete Selected");
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        b.setLayoutData(gd);
        b.setEnabled(isDbIdle);
        b.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = (IStructuredSelection) dbTable.getSelection();
                if (sel != null && sel.getFirstElement() != null) dbTableContentProvider.removeDatasourceConfig((DatasourceConfig) sel.getFirstElement());
            }
        });
        b = new Button(buttonBar, SWT.PUSH);
        b.setText("&Test Connection");
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        b.setLayoutData(gd);
        b.setEnabled(isDbIdle);
        b.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = (IStructuredSelection) dbTable.getSelection();
                if (sel != null && sel.getFirstElement() != null) {
                    DatasourceConfig cfg = (DatasourceConfig) sel.getFirstElement();
                    Executors.newSingleThreadExecutor().submit(new ConnectionTester(cfg));
                }
            }
        });
        if (!hasSupportedDb) super.setMessage("There isn't any supported database. Check the platform plug-ins and fragments", WARNING);
        if (!isDbIdle) super.setMessage("Some connections are in use. Release them to enable preferences", WARNING);
        return parent;
    }

    @Override
    public void dispose() {
        dbTableContentProvider = null;
        dbTable = null;
        if (keyImage != null) keyImage.dispose();
        super.dispose();
    }

    @Override
    public boolean performOk() {
        if (dbManager.isIdle()) {
            dbManager.storeDatasourceConfig(DbBaseActivator.getDefault().getPreferenceStore(), dbTableContentProvider.getSources());
        }
        return true;
    }

    public void init(IWorkbench workbench) {
    }

    /**
	 * Refreshes the table viewer and ensures that the last inserted element
	 * is visible (if not null)
	 * 
	 * @param lastInsertedElement the last inserted element, or {@code null} if
	 * 	the last operation was not an insert.
	 */
    void refresh(final Object lastInsertedElement) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                if (dbTable != null && !dbTable.getControl().isDisposed()) {
                    dbTable.refresh(true);
                    if (lastInsertedElement != null) dbTable.reveal(lastInsertedElement);
                    Table t = dbTable.getTable();
                    for (TableColumn col : t.getColumns()) col.pack();
                }
            }
        });
    }
}
