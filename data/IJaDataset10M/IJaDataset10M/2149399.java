package net.dadajax.gui.swt;

import java.util.List;
import net.dadajax.downloadmanager.DownloadManager;
import net.dadajax.downloadmanager.EventManager;
import net.dadajax.gui.Gui;
import net.dadajax.gui.SettingsForm;
import net.dadajax.gui.Strings;
import net.dadajax.gui.TaskTable;
import net.dadajax.languagemanager.LanguageManager;
import net.dadajax.languagemanager.LanguageManagerImpl;
import net.dadajax.model.Settings;
import net.dadajax.model.SettingsStorage;
import net.dadajax.model.SettingsStorageImpl;
import net.dadajax.model.Task;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author dadajax
 *
 */
public class SwtGui implements Runnable, Gui {

    private DownloadManager downloadManager;

    private EventManager eventManager;

    private SelectionListener swtEventListener;

    private Display display;

    private Shell mainShell;

    private boolean isReady;

    private SettingsStorage settings;

    private LanguageManager lm;

    private TaskTable taskTable;

    private Tray tray;

    private Menu trayMenu;

    private static final String TRAY_IMAGE_PATH = "./res/images/tray.png";

    public SwtGui(DownloadManager dm, EventManager em) {
        downloadManager = dm;
        eventManager = em;
        isReady = false;
        swtEventListener = new SwtEventListener(em);
        settings = SettingsStorageImpl.getSettingsStorage();
        lm = LanguageManagerImpl.getInstance();
        Logger.getRootLogger().debug("instance of SwtGui was created");
    }

    @Override
    public void run() {
        init();
        isReady = true;
        mainShell.open();
        while (!mainShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private void init() {
        display = new Display();
        Logger.getRootLogger().debug("display was created");
        Thread.currentThread().setName("UI thread");
        Logger.getRootLogger().debug("thread name set to UI thread");
        mainShell = new Shell(display);
        Logger.getRootLogger().debug("main shell was created");
        lm.setLanguage(settings.getProperty("language"));
        Logger.getRootLogger().debug("language was set to " + settings.getProperty("language"));
        String title = lm.loc(Strings.MAIN_WINDOW_TITLE);
        try {
            String version = SwtGui.class.getPackage().getImplementationVersion();
            if (version != null) {
                title += "        " + version;
            }
        } catch (Exception e) {
            Logger.getRootLogger().error("cannot read version number", e);
        }
        mainShell.setText(title);
        mainShell.setLayout(new FormLayout());
        int width = settings.getPropertyInt(Settings.WIN_WIDTH);
        int height = settings.getPropertyInt(Settings.WIN_HEIGHT);
        int posX = settings.getPropertyInt(Settings.WIN_POS_X);
        int posY = settings.getPropertyInt(Settings.WIN_POS_Y);
        mainShell.setBounds(posX, posY, width, height);
        Composite compositeForToolBar = new Composite(mainShell, SWT.NONE);
        compositeForToolBar.setLayout(new FillLayout());
        new SwtToolBarBuilder(compositeForToolBar, swtEventListener);
        Composite compositeForTable = new Composite(mainShell, SWT.NONE);
        compositeForTable.setLayout(new FillLayout());
        taskTable = new SwtTaskTable(compositeForTable, downloadManager, eventManager);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 5);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        compositeForToolBar.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(compositeForToolBar, 5);
        data.bottom = new FormAttachment(100, -5);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        Logger.getRootLogger().debug("table was created");
        compositeForTable.setLayoutData(data);
        setShellCloseEvents();
        createTray();
    }

    private void createTray() {
        tray = display.getSystemTray();
        if (tray == null) {
            Logger.getRootLogger().warn("tray is not available!");
        } else {
            final TrayItem item = new TrayItem(tray, SWT.NONE);
            Image img = null;
            try {
                img = new Image(display, TRAY_IMAGE_PATH);
            } catch (Exception e) {
                img = null;
                Logger.getRootLogger().error("cannot load tray image", e);
            }
            item.setToolTipText(lm.loc(Strings.MAIN_WINDOW_TITLE));
            if (img != null) {
                item.setImage(img);
            }
            item.setVisible(false);
            item.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event event) {
                    mainShell.setVisible(true);
                    mainShell.setMinimized(false);
                    item.setVisible(false);
                }
            });
            SwtMenuBuilder mb = new SwtMenuBuilder(new SelectionListener() {

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }

                @Override
                public void widgetSelected(SelectionEvent e) {
                    Widget w = (Widget) e.getSource();
                    String command = (String) w.getData("command");
                    if (command != null) {
                        net.dadajax.downloadmanager.Event event = new net.dadajax.downloadmanager.Event(command);
                        eventManager.handleEvent(event);
                    }
                }
            }, mainShell);
            trayMenu = mb.getPopupMenu(mainShell, "menu_tray");
            item.addListener(SWT.MenuDetect, new Listener() {

                public void handleEvent(Event event) {
                    trayMenu.setVisible(true);
                }
            });
            mainShell.addListener(SWT.Iconify, new Listener() {

                @Override
                public void handleEvent(Event event) {
                    mainShell.setVisible(false);
                    item.setVisible(true);
                }
            });
        }
    }

    @Override
    public void createNewTask() {
        SwtTaskDialog dialog = new SwtTaskDialog(mainShell, null, downloadManager);
        dialog.open();
        update();
    }

    @Override
    public void saveSettings() {
        org.eclipse.swt.graphics.Rectangle rec = mainShell.getBounds();
        int shellWidth = rec.width;
        int shellHeight = rec.height;
        int shellPosX = rec.x;
        int shellPosY = rec.y;
        settings.setProperty(Settings.WIN_WIDTH, shellWidth);
        settings.setProperty(Settings.WIN_HEIGHT, shellHeight);
        settings.setProperty(Settings.WIN_POS_X, shellPosX);
        settings.setProperty(Settings.WIN_POS_Y, shellPosY);
        taskTable.prepareToClose();
    }

    @Override
    public void show() {
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                mainShell.open();
            }
        });
        Logger.getRootLogger().debug("mainShell was opened");
    }

    @Override
    public void update() {
        display.asyncExec(new Runnable() {

            @Override
            public void run() {
                taskTable.update();
            }
        });
        Logger.getRootLogger().debug("update table");
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public void editTask() {
        Task selectedTask;
        try {
            if (taskTable.getSelectedTasks().size() > 0) {
                selectedTask = taskTable.getSelectedTasks().get(0);
            } else {
                selectedTask = null;
            }
        } catch (IndexOutOfBoundsException e) {
            Logger.getRootLogger().warn("propably no task selected", e);
            selectedTask = null;
        }
        if (selectedTask == null) {
            MessageBox mb = new MessageBox(mainShell, SWT.NONE);
            mb.setMessage(lm.loc(Strings.WARNING_NO_TASK_SELECTED));
            mb.setText(lm.loc(Strings.WARNING));
            mb.open();
        } else {
            SwtTaskDialog dialog = new SwtTaskDialog(mainShell, selectedTask, downloadManager);
            dialog.open();
            update();
        }
    }

    /**
	 * Set up events for main shell closing.
	 */
    private void setShellCloseEvents() {
        mainShell.addListener(SWT.Close, new Listener() {

            @Override
            public void handleEvent(Event event) {
                downloadManager.shutDown();
            }
        });
    }

    @Override
    public void showSettings() {
        final Gui gui = this;
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                SettingsForm sf = new SwtSettingsForm(mainShell);
                sf.open();
            }
        });
    }

    @Override
    public List<Task> getSelectedTasks() {
        return taskTable.getSelectedTasks();
    }

    @Override
    public void showAbout() {
        SwtAboutScreen about = new SwtAboutScreen(mainShell);
        about.open();
    }

    @Override
    public void taskPriorityChanged() {
        taskTable.taskPriorityChanged();
    }
}
