package com.hsbc.hbfr.ccf.at.logreader.ui.swt;

import com.hsbc.hbfr.ccf.at.logreader.model.LogEvent;
import com.hsbc.hbfr.ccf.at.logreader.model.LogProvider;
import com.hsbc.hbfr.ccf.at.logreader.model.ProvidedEvent;
import com.hsbc.hbfr.ccf.at.logreader.ui.*;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import java.io.File;
import java.util.*;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: 10 juin 2005
 * Time: 16:02:23
 * To change this template use File | Settings | File Templates.
 */
public class LogViewerFormSWT extends LogViewerForm {

    private static final Logger logger = Logger.getLogger("INFRA." + LogViewerFormSWT.class.getName());

    public static boolean bypassCloseConfirmation = false;

    private final ExitAction exitAction = new ExitAction(this);

    private final ActionOpenFile actionOpenFile = new ActionOpenFile(this);

    private final ActionOpenURL actionOpenURL = new ActionOpenURL(this);

    private final ActionRefreshProviders actionRefreshProviders = new ActionRefreshProviders(this);

    private final ActionConfigLoad actionConfigLoad = new ActionConfigLoad(this);

    private final ActionConfigSave actionConfigSave = new ActionConfigSave(this);

    private final ActionConfigSaveAs actionConfigSaveAs = new ActionConfigSaveAs(this);

    private final SpawnViewerAction spawnAction = new SpawnViewerAction(this);

    private LogEventTableViewer table;

    private Text text;

    private FilteringLogEventList model;

    protected LogViewerWindow window;

    private Menu providersMenu;

    private Menu colorsMenu;

    private MenuManager colorsMenuManager;

    private MenuManager providersMenuManager;

    private final Map providersMenuItems = new LinkedHashMap();

    private final Map colorsMenuItems = new LinkedHashMap();

    private StringFilterInputComposite categoryFilter;

    private StringFilterInputComposite threadFilter;

    private StringFilterInputComposite priorityFilter;

    private StringFilterInputComposite dateFilter;

    private StringFilterInputComposite messageFilter;

    private FilterInput[] filters = new FilterInput[5];

    private boolean bypassTableUpdate = false;

    private static String currentDir = System.getProperty("user.home");

    public boolean isLastWindow() {
        return globalOpenViewers.size() == 1;
    }

    protected class LogViewerWindow extends ApplicationWindow {

        public LogViewerWindow(Shell parentShell) {
            super(parentShell);
            addStatusLine();
            addMenuBar();
        }

        protected Control createContents(Composite parent) {
            Composite compo = new Composite(parent, SWT.NONE);
            compo.setLayout(new FormLayout());
            Composite filtersComposite = new Composite(compo, SWT.NONE);
            filtersComposite.setLayout(new GridLayout(5, true));
            categoryFilter = new StringFilterInputComposite(categoryPredicate, LogEventList.COLUMN_NAMES[LogEventList.CATEGORY], filtersComposite);
            priorityFilter = new StringFilterInputComposite(priorityPredicate, LogEventList.COLUMN_NAMES[LogEventList.PRIORITY], filtersComposite);
            threadFilter = new StringFilterInputComposite(threadPredicate, LogEventList.COLUMN_NAMES[LogEventList.THREAD], filtersComposite);
            dateFilter = new StringFilterInputComposite(datePredicate, LogEventList.COLUMN_NAMES[LogEventList.DATE], filtersComposite);
            messageFilter = new StringFilterInputComposite(messagePredicate, LogEventList.COLUMN_NAMES[LogEventList.MESSAGE], filtersComposite);
            filters[LogEventList.CATEGORY] = categoryFilter;
            filters[LogEventList.THREAD] = threadFilter;
            filters[LogEventList.PRIORITY] = priorityFilter;
            filters[LogEventList.DATE] = dateFilter;
            filters[LogEventList.MESSAGE] = messageFilter;
            GridData data;
            data = new GridData();
            data.horizontalAlignment = GridData.FILL_HORIZONTAL;
            data.horizontalAlignment = GridData.VERTICAL_ALIGN_CENTER;
            categoryFilter.setLayoutData(data);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL_HORIZONTAL;
            data.horizontalAlignment = GridData.VERTICAL_ALIGN_CENTER;
            threadFilter.setLayoutData(data);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL_HORIZONTAL;
            data.horizontalAlignment = GridData.VERTICAL_ALIGN_CENTER;
            priorityFilter.setLayoutData(data);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL_HORIZONTAL;
            data.horizontalAlignment = GridData.VERTICAL_ALIGN_CENTER;
            dateFilter.setLayoutData(data);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL_HORIZONTAL;
            data.horizontalAlignment = GridData.VERTICAL_ALIGN_CENTER;
            messageFilter.setLayoutData(data);
            filtersComposite.pack();
            FormData formData = new FormData();
            formData.top = new FormAttachment(0, 0);
            formData.left = new FormAttachment(0, 0);
            formData.right = new FormAttachment(100, 0);
            formData.bottom = new FormAttachment(0, filtersComposite.getBounds().height);
            filtersComposite.setLayoutData(formData);
            SashForm sashForm = new SashForm(compo, SWT.VERTICAL);
            createTable(sashForm);
            createTextArea(sashForm);
            formData = new FormData();
            formData.top = new FormAttachment(filtersComposite);
            formData.left = new FormAttachment(0, 0);
            formData.right = new FormAttachment(100, 0);
            formData.bottom = new FormAttachment(100, 0);
            sashForm.setLayoutData(formData);
            initEvents();
            return sashForm;
        }

        private void createTextArea(Composite parent) {
            text = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.LEFT | SWT.MULTI | SWT.WRAP);
        }

        private Table createTable(Composite parent) {
            table = new LogEventTableViewer(parent, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
            ArrayList logEvents = new ArrayList();
            LogEventProvider tableProvider = new LogEventProvider();
            for (int i = 0; i < LogEventList.COLUMN_NAMES.length; i++) {
                String columnName = LogEventList.COLUMN_NAMES[i];
                TableColumn tableColumn = new TableColumn(table.getTable(), SWT.LEFT, i);
                tableColumn.setText(columnName);
                tableColumn.setWidth(100);
            }
            table.setContentProvider(tableProvider);
            table.setLabelProvider(tableProvider);
            table.getTable().setHeaderVisible(true);
            table.refresh();
            return table.getTable();
        }

        public IProgressMonitor getProgressMonitor() {
            return getStatusLineManager().getProgressMonitor();
        }

        protected MenuManager createMenuManager() {
            logger.debug("createMenuManager");
            MenuManager barMenu = new MenuManager("");
            MenuManager fileMenu = new MenuManager(UIConstants.MENU_FILE);
            fileMenu.add(actionOpenFile);
            fileMenu.add(actionOpenURL);
            fileMenu.add(actionRefreshProviders);
            fileMenu.add(new Separator());
            fileMenu.add(actionConfigLoad);
            fileMenu.add(actionConfigSave);
            fileMenu.add(actionConfigSaveAs);
            fileMenu.add(new Separator());
            fileMenu.add(exitAction);
            colorsMenuManager = new MenuManager(UIConstants.MENU_COLORS);
            providersMenuManager = new MenuManager(UIConstants.MENU_PROVIDERS);
            colorsMenuManager.add(new Action("dummy") {
            });
            providersMenuManager.add(new Action("dummy") {
            });
            barMenu.add(fileMenu);
            barMenu.add(colorsMenuManager);
            barMenu.add(providersMenuManager);
            barMenu.add(spawnAction);
            return barMenu;
        }

        public int open() {
            int ret = super.open();
            installDynamicMenus();
            return ret;
        }

        private void installDynamicMenus() {
            logger.debug("installDynamicMenus");
            colorsMenu = colorsMenuManager.getMenu();
            providersMenu = providersMenuManager.getMenu();
            class DynamicMenuListener extends SelectionAdapter implements Listener {

                private final Map providersVsactions;

                protected final Menu menu;

                public DynamicMenuListener(Menu aMenu, Map someProvidersVsActions) {
                    menu = aMenu;
                    providersVsactions = someProvidersVsActions;
                }

                public void handleEvent(Event event) {
                    MenuItem[] menuItems = menu.getItems();
                    for (int i = 0; i < menuItems.length; i++) {
                        menuItems[i].dispose();
                    }
                    for (Iterator itOpen = providersVsactions.values().iterator(); itOpen.hasNext(); ) {
                        Action action = (Action) itOpen.next();
                        logger.debug("creating menuitem for colors>" + action.getText());
                        MenuItem menuItem = createMenuItem(action);
                        menuItem.setData(action);
                        menuItem.setText(action.getText());
                        menuItem.addSelectionListener(this);
                    }
                }

                protected MenuItem createMenuItem(Action action) {
                    MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
                    return menuItem;
                }

                public void widgetSelected(SelectionEvent selectionEvent) {
                    logger.debug("widgetSelected [source =" + selectionEvent.getSource() + ",checked=" + ((MenuItem) selectionEvent.getSource()).getData(IAction.CHECKED));
                    ((Action) ((MenuItem) selectionEvent.getSource()).getData()).run();
                }
            }
            colorsMenuManager.getMenu().addListener(SWT.Show, new DynamicMenuListener(colorsMenu, colorsMenuItems));
            providersMenuManager.getMenu().addListener(SWT.Show, new DynamicMenuListener(providersMenu, providersMenuItems) {

                protected MenuItem createMenuItem(Action action) {
                    MenuItem item = new MenuItem(menu, SWT.CHECK);
                    ToggleProviderAction toggleProviderAction = ((ToggleProviderAction) action);
                    item.setSelection(toggleProviderAction.isChecked());
                    return item;
                }
            });
        }

        protected boolean canHandleShellCloseEvent() {
            return true;
        }

        protected void handleShellCloseEvent() {
            logger.debug("handleShellCloseEvent");
            if (bypassCloseConfirmation || !isLastWindow()) {
                LogViewerFormSWT.this.close();
            } else if (isLastWindow()) {
                exitAction.run();
            }
        }

        public boolean close() {
            logger.debug("close");
            setReturnCode(SWT.CANCEL);
            logger.debug("opened = " + globalOpenViewers.size());
            return super.close();
        }
    }

    public LogViewerFormSWT(Shell aShell) {
        super();
        window = new LogViewerWindow(aShell);
        globalOpenViewers.put(window, this);
    }

    protected Shell getShell() {
        return window.getShell();
    }

    protected void close() {
        cleanup();
    }

    protected void cleanup() {
        super.cleanup();
        table = null;
        text = null;
        model = null;
        window.close();
        window = null;
        providersMenu = null;
        colorsMenu = null;
        providersMenuManager = null;
        colorsMenuManager = null;
    }

    protected Object getWindowObject() {
        return window;
    }

    protected void initEvents() {
        table.getControl().addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent mouseEvent) {
                logger.debug("mouseDoubleClick " + mouseEvent);
            }

            public void mouseDown(MouseEvent mouseEvent) {
                logger.debug("mouseDown " + mouseEvent);
                if (mouseEvent.button != 3) {
                    logger.debug("button" + mouseEvent.button + "!=" + 3);
                    return;
                }
                Point mousePoint = new Point(mouseEvent.x, mouseEvent.y);
                TableItem item = table.getTable().getItem(mousePoint);
                if (item == null) {
                    logger.debug("clicked null item");
                    return;
                }
                int columnIndex = table.getColumnAtPoint(mousePoint);
                Object data = item.getData();
                String columnText = ((ITableLabelProvider) table.getLabelProvider()).getColumnText(data, columnIndex);
                filters[columnIndex].setText(columnText);
            }

            public void mouseUp(MouseEvent mouseEvent) {
                logger.debug("mouseUp " + mouseEvent);
            }
        });
        table.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                StringBuffer buf = new StringBuffer();
                logger.debug("selection size : " + selection.size());
                window.getShell().setText(((ProvidedEvent) selection.getFirstElement()).getProvider().toString());
                Iterator iterator = selection.iterator();
                while (iterator.hasNext()) {
                    LogEvent logEvent = (LogEvent) iterator.next();
                    buf.append(logEvent.getString());
                    if (iterator.hasNext()) {
                        buf.append('\n');
                    }
                }
                text.setText(buf.toString());
            }
        });
        ActionListener tableUpdater = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                logger.debug("applying filter");
                model.applyFilter();
            }
        };
        for (int i = 0; i < filters.length; i++) {
            FilterInput input = filters[i];
            input.addActionListener(tableUpdater);
        }
    }

    protected LogViewerForm createInFrame(LogProvider provider) {
        Display display = getDisplay();
        LogViewerFormSWT formSWT = new LogViewerFormSWT(new Shell(display));
        formSWT.window.open();
        formSWT.addFromProvider(provider);
        return formSWT;
    }

    private Display getDisplay() {
        return (window.getShell() == null) ? Display.getCurrent() : window.getShell().getDisplay();
    }

    /**
     * Demande un nom de fichier et le lit
     */
    protected String requestFileFromUser() {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setFilterPath(currentDir);
        dialog.open();
        String fichier = dialog.getFileName();
        String repertoire = dialog.getFilterPath();
        currentDir = repertoire;
        return new File(repertoire, fichier).getAbsolutePath();
    }

    /**
     * redefinie pour provoquer la maj de la table (appeler setInput)
     * @param events
     */
    private void setTableInput(final List events) {
        logger.debug("setTableInput");
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    Object oldInput = table.getInput();
                    Object newInput = events;
                    logger.debug("async setTableInput oldInput==newINput =" + (newInput == oldInput) + ", equals ? " + (newInput.equals(oldInput)));
                    table.setInput(events);
                    window.setStatus("filtr�s :" + events.size() + ", total : " + model.getAllEvents().size());
                } catch (RuntimeException e) {
                    logger.error("error in async task", e);
                }
            }
        };
        if (window == null || getShell() == null) {
            return;
        }
        if (getShell().getDisplay().getThread().equals(Thread.currentThread())) {
            runnable.run();
        } else {
            getShell().getDisplay().asyncExec(runnable);
        }
    }

    protected static void closeAll() {
        try {
            LogViewerFormSWT.bypassCloseConfirmation = true;
            LogViewerFormSWT[] forms = (LogViewerFormSWT[]) LogViewerFormSWT.globalOpenViewers.values().toArray(new LogViewerFormSWT[globalOpenViewers.size()]);
            for (int i = 0; i < forms.length; i++) {
                LogViewerFormSWT form = forms[i];
                form.close();
            }
        } finally {
            LogViewerFormSWT.bypassCloseConfirmation = false;
        }
    }

    public List getSelectedEvents() {
        return ((IStructuredSelection) table.getSelection()).toList();
    }

    public static void main(String[] args) {
        URL url = LogViewerForm.class.getResource("/log4j.properties");
        org.apache.log4j.PropertyConfigurator.configure(url);
        logger.info("STARTING NEW SESSION : " + getBuildInfos());
        Display display = new Display();
        Shell aShell = new Shell(display);
        LogViewerFormSWT formSWT = new LogViewerFormSWT(aShell);
        formSWT.restorePreferences();
        if (globalOpenViewers.size() == 1) {
            formSWT.window.open();
        } else {
            formSWT.cleanup();
        }
        while (globalOpenViewers.size() > 0) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (SWTException e) {
                logger.error("error in event dispatch thread", e);
            }
        }
        logger.debug("disposing");
        aShell.dispose();
        display.dispose();
    }

    protected ProgressIndicatorNeutral createProgress(final int aMax) {
        if (window.getShell() == null) {
            return new ProgressIndicatorNeutral() {

                int max = aMax;

                int current = 0;

                public int getMax() {
                    return max;
                }

                public void setMax(int max) {
                    this.max = max;
                }

                public void setProgress(int progress) {
                    current = progress;
                }

                public int getProgress() {
                    return current;
                }

                public void start() {
                }

                public void finished() {
                }

                public void setNote(String s) {
                }
            };
        } else {
            return new SWTProgressIndicator(window.getProgressMonitor(), getDisplay(), aMax);
        }
    }

    /**
     * redefinie pour provoquer la maj de la table (oblig� d'appeler setInput)
     * @param provider
     */
    public void hideEvents(LogProvider provider) {
        super.hideEvents(provider);
        table.setInput(getLogTableModel().getAllEvents());
    }

    protected void setBounds(int x, int y, int width, int height) {
        window.getShell().setBounds(x, y, width, height);
    }

    static class ToggleProviderAction extends Action {

        private final LogProvider provider;

        private final LogViewerFormSWT form;

        public ToggleProviderAction(LogViewerFormSWT form, LogProvider aProvider) {
            super(aProvider.toString(), SWT.CHECK);
            this.form = form;
            provider = aProvider;
        }

        public void run() {
            logger.debug("ToggleProviderAction.run");
            if (isChecked()) {
                logger.debug("ToggleProviderAction.isChecked = true");
                form.hideEvents(provider);
            } else {
                logger.debug("ToggleProviderAction.isChecked = false");
                form.showEvents(provider);
            }
        }

        public boolean isChecked() {
            return form.isProviderOpened(provider);
        }
    }

    protected boolean isProviderOpened(LogProvider provider) {
        return openProviders.contains(provider);
    }

    static class ChangeProviderColorAction extends Action {

        private final LogProvider provider;

        private final LogViewerFormSWT form;

        public ChangeProviderColorAction(LogViewerFormSWT form, LogProvider aProvider) {
            super(aProvider.toString());
            this.form = form;
            provider = aProvider;
        }

        public void run() {
            ColorDialog dialog = new ColorDialog(form.getShell());
            dialog.setRGB(ProviderColorManagerSWT.instance().getColorForProvider(provider).getRGB());
            RGB rgb = dialog.open();
            if (rgb != null) {
                Color newColor = new Color(form.getShell().getDisplay(), rgb);
                ProviderColorManagerSWT.instance().setColorForProvider(provider, newColor);
                form.table.refresh();
            }
        }
    }

    protected void addProviderToProviderMenu(final LogProvider provider) {
        if (provider.isTransient() || isProviderInMenu(provider)) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("adding color to menu for [" + provider + "]");
        }
        providersMenuItems.put(provider, new ToggleProviderAction(this, provider));
        colorsMenuItems.put(provider, new ChangeProviderColorAction(this, provider));
    }

    protected void createTableModel() {
        model = new FilteringLogEventList(new ArrayList()) {

            public void setEvents(List events) {
                super.setEvents(events);
                setTableInput(getEvents());
            }
        };
        model.setCategoryPredicate(categoryPredicate);
        model.setThreadPredicate(threadPredicate);
        model.setDatePredicate(datePredicate);
        model.setMessagePredicate(messagePredicate);
        model.setPriorityPredicate(priorityPredicate);
    }

    protected FilterInput getCategoryFilter() {
        return categoryFilter;
    }

    protected File getCurrentDirectory() {
        return new File(currentDir);
    }

    protected AbstractColorManager getColorManager() {
        return ProviderColorManagerSWT.instance();
    }

    protected FilterInput getDateFilter() {
        return dateFilter;
    }

    protected FilteringLogEventList getLogTableModel() {
        return model;
    }

    protected FilterInput getMessageFilter() {
        return messageFilter;
    }

    public PreferencesSaver.PersistentLogViewerForm getPersistentLogViewerForm() {
        PreferencesSaver.PersistentLogViewerForm config = new PreferencesSaver.PersistentLogViewerForm() {

            public Set getOpenProviders() {
                return openProviders;
            }

            public Set getHiddenProviders() {
                return hiddenProviders;
            }

            public Rectangle getFrameBounds() {
                org.eclipse.swt.graphics.Rectangle bounds = window.getShell().getBounds();
                return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            public FilterInput getCategoryFilter() {
                return categoryFilter;
            }

            public FilterInput getThreadFilter() {
                return threadFilter;
            }

            public FilterInput getPriorityFilter() {
                return priorityFilter;
            }

            public FilterInput getMessageFilter() {
                return messageFilter;
            }

            public FilterInput getDateFilter() {
                return dateFilter;
            }
        };
        return config;
    }

    protected FilterInput getPriorityFilter() {
        return priorityFilter;
    }

    protected FilterInput getThreadFilter() {
        return threadFilter;
    }

    protected void initGUI() {
    }

    public boolean isProviderInMenu(final LogProvider provider) {
        return providersMenuItems.containsKey(provider);
    }

    protected String requestURLFromUser() {
        logger.debug("run");
        InputDialog inputDialog = new InputDialog(getShell(), "Ouvrir", "Quelle url ?", "", new IInputValidator() {

            public String isValid(String newText) {
                return null;
            }
        });
        boolean canceled = (inputDialog.open() != Window.OK);
        if (!canceled) {
            return inputDialog.getValue();
        } else {
            return null;
        }
    }

    protected void setCurrentDirectory(File currentDirectory) {
        currentDir = currentDirectory.getAbsolutePath();
    }

    protected void setVisible(boolean b) {
        window.getShell().setVisible(true);
    }
}
