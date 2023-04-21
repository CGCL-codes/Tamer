package com.varaneckas.hawkscope.menu;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import com.varaneckas.hawkscope.Version;
import com.varaneckas.hawkscope.cfg.Configuration;
import com.varaneckas.hawkscope.cfg.ConfigurationFactory;
import com.varaneckas.hawkscope.command.AboutCommand;
import com.varaneckas.hawkscope.command.Command;
import com.varaneckas.hawkscope.command.ExitCommand;
import com.varaneckas.hawkscope.command.HelpCommand;
import com.varaneckas.hawkscope.command.HideCommand;
import com.varaneckas.hawkscope.command.SettingsCommand;
import com.varaneckas.hawkscope.command.UpdateCommand;
import com.varaneckas.hawkscope.menu.state.MenuClosedState;
import com.varaneckas.hawkscope.menu.state.State;
import com.varaneckas.hawkscope.plugin.PluginManager;
import com.varaneckas.hawkscope.tray.TrayManager;
import com.varaneckas.hawkscope.util.IconFactory;
import com.varaneckas.hawkscope.util.OSUtils;
import com.varaneckas.hawkscope.util.PathUtils;

/**
 * {@link MainMenu} - SWT implementation
 * 
 * @author Tomas Varaneckas
 * @version $Id: MainMenu.java 578 2009-06-06 16:05:54Z tomas.varaneckas $
 */
public class MainMenu {

    /**
     * Singleton instance
     */
    private static MainMenu instance = null;

    /**
     * SWT Menu object
     */
    private final Menu menu;

    /**
     * Marks that menu is reloading
     */
    private boolean isReloading;

    /** 
     * Is icon reload enqueued after changing display theme?
     */
    private boolean isIconReloadEnqueued = false;

    /**
     * Unix timestamp since when menu was hidden. Used for enhancing the 
     * reloading performance.
     */
    private static long hiddenSince;

    /**
     * Instance of configuration
     */
    private Configuration cfg = ConfigurationFactory.getConfigurationFactory().getConfiguration();

    /**
     * Initializing singleton constructor
     */
    private MainMenu() {
        menu = new Menu(TrayManager.getInstance().getShell(), SWT.POP_UP);
        menu.addListener(SWT.Hide, new Listener() {

            public void handleEvent(final Event event) {
                new Thread(new Runnable() {

                    public void run() {
                        if (!menu.isDisposed()) {
                            menu.getDisplay().asyncExec(new Runnable() {

                                public void run() {
                                    try {
                                        Thread.sleep(10l);
                                        hiddenSince = System.currentTimeMillis();
                                    } catch (InterruptedException e) {
                                        log.warn("Could not sleep", e);
                                    }
                                    if (!(state instanceof MenuClosedState)) {
                                        setState(MenuClosedState.getInstance());
                                    }
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * Singleton instance getter
     * 
     * @return instance of SWTMainMenu
     */
    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    public void clearMenu() {
        for (org.eclipse.swt.widgets.MenuItem item : menu.getItems()) {
            if (!item.isDisposed()) {
                item.dispose();
            }
        }
        if (isIconReloadEnqueued) {
            IconFactory.getInstance().cleanup();
            IconFactory.getInstance().loadResources();
            isIconReloadEnqueued = false;
        }
    }

    public void forceHide() {
        hiddenSince = System.currentTimeMillis();
        setState(MenuClosedState.getInstance());
        if (!menu.isDisposed()) {
            menu.setVisible(false);
        }
    }

    public void showMenu(final int x, final int y) {
        hiddenSince = 0L;
        menu.setLocation(x, y);
        menu.setVisible(true);
    }

    public void addMenuItem(final com.varaneckas.hawkscope.menu.MenuItem item) {
        item.createMenuItem(menu);
    }

    public Menu getSwtMenuObject() {
        return menu;
    }

    public void addSeparator() {
        new org.eclipse.swt.widgets.MenuItem(menu, SWT.SEPARATOR);
    }

    public void setHiddenSince(final long timestamp) {
        hiddenSince = timestamp;
    }

    public void reloadMenu(final boolean canWait) {
        if (!canWait && log.isDebugEnabled()) {
            log.debug("Forcing menu reload now.");
        }
        if (!isReloading || !canWait) {
            isReloading = true;
            new Thread(new Runnable() {

                public void run() {
                    try {
                        if (canWait) {
                            Thread.sleep(ConfigurationFactory.getConfigurationFactory().getConfiguration().getMenuReloadDelay());
                        }
                    } catch (final InterruptedException e1) {
                        log.warn("Could not sleep", e1);
                    }
                    doReload(canWait);
                }
            }).start();
        }
    }

    /**
     * Does the actual reload of Main Menu
     */
    private void doReload(final boolean canWait) {
        if (menu.getDisplay().isDisposed()) {
            log.warn("Menu disposed - won't reload");
            return;
        }
        menu.getDisplay().asyncExec(new Runnable() {

            public void run() {
                try {
                    if (canWait) {
                        if (hiddenSince == -1) {
                            log.debug("Skipping planned reload as it was forced a while ago.");
                            return;
                        }
                        if (hiddenSince == 0L) {
                            log.debug("Menu now open, reload skipped");
                            isReloading = false;
                            return;
                        } else if (System.currentTimeMillis() - hiddenSince < cfg.getMenuReloadDelay()) {
                            if (log.isDebugEnabled()) {
                                log.debug("Reloading later, menu is not sleeping " + "long enough: (" + ((System.currentTimeMillis() - hiddenSince) / 1000.0) + " seconds)");
                            }
                            isReloading = false;
                            reloadMenu(true);
                            return;
                        }
                    } else {
                        hiddenSince = -1;
                    }
                    clearMenu();
                    loadMenu();
                    Runtime.getRuntime().gc();
                } catch (final Exception e) {
                    log.debug("Failed reloading menu", e);
                }
                isReloading = false;
                hiddenSince = System.currentTimeMillis();
                enqueueIdleReload();
            }
        });
    }

    /**
     * Enqueues idle reload
     */
    private void enqueueIdleReload() {
        if (isReloading) {
            log.warn("Can't enqueue idle reload on a reloading menu");
            return;
        }
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(Math.max(cfg.getMenuReloadDelay(), 180000));
                    if (!isReloading) {
                        log.debug("Idle reload.");
                        doReload(true);
                    } else {
                        log.debug("Idle reload cancelled");
                    }
                } catch (final InterruptedException e) {
                    log.warn("Idle reload interrupted", e);
                }
            }
        }).start();
    }

    /**
     * Current menu state
     */
    protected State state = MenuClosedState.getInstance();

    /**
     * Logger
     */
    protected Log log = LogFactory.getLog(getClass());

    /**
     * Root partitions
     */
    protected List<File> roots = OSUtils.getFileSystemRoots();

    /**
     * Gets current {@link State}
     * 
     * @return current state
     */
    public State getState() {
        return state;
    }

    /**
     * Sets menu {@link State}
     * 
     * @param state new state
     */
    public void setState(final State state) {
        if (!this.state.equals(state)) {
            this.state = state;
            state.init();
        }
    }

    /**
     * Loads the menu
     */
    public void loadMenu() {
        PluginManager.getInstance().beforeQuickAccess(this);
        loadQuickAccessMenu();
        boolean addSeparator = false;
        for (final File root : roots) {
            if (cfg.getBlackList().contains(root)) {
                continue;
            }
            if (cfg.isFloppyDrivesDisplayed() || !PathUtils.isFloppy(root)) {
                if (log.isDebugEnabled()) {
                    log.debug("Generating menu for: " + root.getAbsolutePath());
                }
                final FolderMenu item = MenuFactory.newFolderMenu(root);
                item.setText(PathUtils.getFileName(root));
                item.setIcon(IconFactory.getInstance().getIcon(root));
                addMenuItem(item);
                addSeparator = true;
            }
        }
        if (addSeparator) {
            addSeparator();
        }
        addStaticItems();
        reloadRoots();
    }

    /**
     * Lazy reload of root partitions
     */
    protected void reloadRoots() {
        log.debug("Performing root partition listing");
        roots = OSUtils.getFileSystemRoots();
        log.debug("Root partition listing complete");
    }

    /**
     * Loads quick access menu
     */
    private void loadQuickAccessMenu() {
        final List<File> quick = cfg.getQuickAccessList();
        if (quick != null && quick.size() > 0) {
            final List<String> quickRaw = cfg.getRawQuickAccessList();
            final Map<String, String> qaNames = cfg.getQuickAccessNames();
            for (int i = 0; i < quick.size(); i++) {
                File custom = quick.get(i);
                String customEntry = quickRaw.get(i);
                try {
                    final FolderMenu fm = MenuFactory.newFolderMenu(custom);
                    if (qaNames.containsKey(customEntry)) {
                        fm.setText(qaNames.get(customEntry));
                    }
                    PluginManager.getInstance().enhanceQuickAccessItem(fm, custom);
                    addMenuItem(fm);
                } catch (final Exception e) {
                    log.warn("Skipping unloadable Quick Access List item", e);
                }
            }
            addSeparator();
        }
    }

    /**
     * Adds static menu items
     */
    private void addStaticItems() {
        if (cfg.checkForUpdates()) {
            if (Version.isUpdateAvailable() != null && Version.isUpdateAvailable()) {
                addExecutableMenuItem("update", "Update Available! (" + Version.VERSION_NUMBER + " to " + Version.updateVersion + ")", new UpdateCommand(Version.DOWNLOAD_URL));
            }
            for (final Entry<String, String> plugUp : PluginManager.getInstance().getAvailableUpdates().entrySet()) {
                addExecutableMenuItem("update", "Plugin Update! (" + plugUp.getKey() + " " + plugUp.getValue() + ")", new UpdateCommand(Version.PLUGINS_URL));
            }
        }
        addExecutableMenuItem("hide", "Hide", new HideCommand());
        PluginManager.getInstance().beforeAboutMenuItem(this);
        addExecutableMenuItem("about", "About", new AboutCommand());
        addExecutableMenuItem("settings", "Settings", new SettingsCommand());
        addExecutableMenuItem("help", "Help", new HelpCommand());
        addExecutableMenuItem("exit", "Exit", new ExitCommand());
    }

    /**
     * Adds an executable menu item 
     * 
     * @param name Item name
     * @param text Text on menu item
     * @param command Command to execute
     */
    public void addExecutableMenuItem(final String name, final String text, final Command command) {
        final ExecutableMenuItem item = MenuFactory.newExecutableMenuItem();
        item.setCommand(command);
        item.setText(text);
        item.setIcon(IconFactory.getInstance().getIcon(name));
        addMenuItem(item);
    }

    /**
     * Enqueues icons reload 
     */
    public void enqueueIconsReload() {
        isIconReloadEnqueued = true;
    }
}
