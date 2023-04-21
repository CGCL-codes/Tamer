package de.xirp.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import de.xirp.profile.Robot;
import de.xirp.ui.Application;
import de.xirp.ui.event.ContentChangedEvent;
import de.xirp.ui.event.ContentChangedListener;
import de.xirp.ui.util.ApplicationManager;
import de.xirp.ui.util.SWTUtil;
import de.xirp.ui.util.ressource.ImageManager;
import de.xirp.ui.util.ressource.ImageManager.SystemImage;
import de.xirp.ui.widgets.custom.XCoolItem;
import de.xirp.ui.widgets.custom.XMenuItem;
import de.xirp.ui.widgets.custom.XToolItem;

/**
 * This user interface class represents the applications tool bar.
 * 
 * @author Matthias Gernand
 */
public final class ApplicationToolBar {

    /**
	 * The parent shell.
	 */
    private Shell parent;

    /**
	 * A tool bar object.
	 */
    private ToolBar toolBar;

    /**
	 * A pop up menu.
	 */
    private Menu toolBarPopUp;

    /**
	 * The application itself.
	 */
    private Application application;

    /**
	 * A button to stop the watch.
	 */
    private XToolItem toolItemStopTimer;

    /**
	 * A button to pause the watch.
	 */
    private XToolItem toolItemPauseTimer;

    /**
	 * A button to start the watch.
	 */
    private XToolItem toolItemStartTimer;

    /**
	 * A content change listener.
	 * 
	 * @see de.xirp.ui.event.ContentChangedListener
	 */
    private ContentChangedListener contentListener;

    /**
	 * A cool bar.
	 */
    private CoolBar coolBar;

    /**
	 * A menu item.
	 */
    private XMenuItem toolBarLock;

    /**
	 * A vector of cool items.
	 */
    private List<XCoolItem> coolItems = new ArrayList<XCoolItem>();

    /**
	 * A cool item.
	 */
    private XCoolItem coolItem;

    /**
	 * Creates a new application tool bar.
	 */
    public ApplicationToolBar() {
        this.application = Application.getApplication();
        this.parent = application.getShell();
        init();
    }

    /**
	 * Creates the tool bar and the tool item on it. This tool bar has
	 * a context menu, where the visibility can be set.
	 */
    private void init() {
        contentListener = new ContentChangedListener() {

            /**
			 * @param event
			 */
            public void contentChanged(ContentChangedEvent event) {
                rearrangeToolBar(event.getRobot());
            }
        };
        ApplicationManager.addContentChangedListener(contentListener);
        createToolBar();
        createItems();
        createContextMenu();
    }

    /**
	 * Creates the tool bar.
	 */
    private void createToolBar() {
        coolBar = new CoolBar(parent, SWT.FLAT);
        coolBar.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                ((GridData) coolBar.getLayoutData()).heightHint = coolBar.getSize().y;
                application.getShell().layout();
            }
        });
        GridData gd = SWTUtil.setGridData(coolBar, true, false, SWT.FILL, SWT.FILL, 1, 1);
        gd.heightHint = 30;
        coolBar.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                ApplicationManager.removeContentChangedListener(contentListener);
            }
        });
        coolItem = new XCoolItem(coolBar, SWT.DROP_DOWN);
        toolBar = new ToolBar(coolBar, SWT.FLAT);
        coolItem.setControl(toolBar);
        Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point coolSize = coolItem.computeSize(size.x, size.y);
        coolItem.setSize(coolSize);
    }

    /**
	 * Creates the tool items.
	 */
    private void createItems() {
        XToolItem toolItemQuit = new XToolItem(toolBar, SWT.PUSH);
        toolItemQuit.setToolTipTextForLocaleKey("Application.gui.tooltip.quit");
        toolItemQuit.setImage(ImageManager.getSystemImage(SystemImage.QUIT));
        toolItemQuit.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        parent.close();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        new XToolItem(toolBar, SWT.SEPARATOR);
        XToolItem toolItemSettings = new XToolItem(toolBar, SWT.PUSH);
        toolItemSettings.setToolTipTextForLocaleKey("Application.gui.tooltip.settings");
        toolItemSettings.setImage(ImageManager.getSystemImage(SystemImage.PREFERENCES));
        toolItemSettings.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        ApplicationManager.showPreferencesDialog();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        new XToolItem(toolBar, SWT.SEPARATOR);
        toolItemStartTimer = new XToolItem(toolBar, SWT.PUSH);
        toolItemStartTimer.setToolTipTextForLocaleKey("ApplicationToolBar.gui.toolTip.startTimer");
        final Image startTimer = ImageManager.getSystemImage(SystemImage.START);
        final Image startTimerDis = ImageManager.getSystemImage(SystemImage.START_DISABLED);
        toolItemStartTimer.setImage(startTimer);
        toolItemStartTimer.setDisabledImage(startTimerDis);
        toolItemStartTimer.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        application.getAppContent().startWatches();
                        toolItemStartTimer.setEnabled(false);
                        toolItemPauseTimer.setEnabled(true);
                        toolItemStopTimer.setEnabled(true);
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        toolItemPauseTimer = new XToolItem(toolBar, SWT.PUSH);
        toolItemPauseTimer.setToolTipTextForLocaleKey("ApplicationToolBar.gui.toolTip.pauseTimer");
        final Image pauseTimer = ImageManager.getSystemImage(SystemImage.PAUSE);
        final Image pauseTimerDis = ImageManager.getSystemImage(SystemImage.PAUSE_DISABLED);
        toolItemPauseTimer.setImage(pauseTimer);
        toolItemPauseTimer.setDisabledImage(pauseTimerDis);
        toolItemPauseTimer.setEnabled(false);
        toolItemPauseTimer.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        application.getAppContent().pauseWatches();
                        toolItemStartTimer.setEnabled(true);
                        toolItemPauseTimer.setEnabled(false);
                        toolItemStopTimer.setEnabled(true);
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        toolItemStopTimer = new XToolItem(toolBar, SWT.PUSH);
        toolItemStopTimer.setToolTipTextForLocaleKey("ApplicationToolBar.gui.toolTip.stopResetTimer");
        final Image stopTimer = ImageManager.getSystemImage(SystemImage.STOP);
        final Image stopTimerDis = ImageManager.getSystemImage(SystemImage.STOP_DISABLED);
        toolItemStopTimer.setImage(stopTimer);
        toolItemStopTimer.setDisabledImage(stopTimerDis);
        toolItemStopTimer.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        application.getAppContent().stopWatches();
                        toolItemStartTimer.setEnabled(true);
                        toolItemPauseTimer.setEnabled(false);
                        toolItemStopTimer.setEnabled(true);
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        new XToolItem(toolBar, SWT.SEPARATOR);
        XToolItem toolItemMail = new XToolItem(toolBar, SWT.PUSH);
        toolItemMail.setToolTipTextForLocaleKey("ApplicationToolBar.tool.item.sendMail");
        toolItemMail.setImage(ImageManager.getSystemImage(SystemImage.SEND_MAIL));
        toolItemMail.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        ApplicationManager.showMailDialog();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        XToolItem toolItemContacts = new XToolItem(toolBar, SWT.PUSH);
        toolItemContacts.setToolTipTextForLocaleKey("ApplicationToolBar.tool.item.contacts");
        toolItemContacts.setImage(ImageManager.getSystemImage(SystemImage.CONTACTS));
        toolItemContacts.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        ApplicationManager.showContactDialog();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        XToolItem toolItemCharts = new XToolItem(toolBar, SWT.PUSH);
        toolItemCharts.setToolTipTextForLocaleKey("ApplicationToolBar.tool.item.charts");
        toolItemCharts.setImage(ImageManager.getSystemImage(SystemImage.CHART));
        toolItemCharts.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        ApplicationManager.showChartDialog();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
    }

    /**
	 * Creates the context menu which is available on the tool bar.
	 */
    private void createContextMenu() {
        toolBarPopUp = new Menu(toolBar);
        final XMenuItem toolBarShow = new XMenuItem(toolBarPopUp, SWT.CHECK);
        toolBarShow.setTextForLocaleKey("Application.gui.toolbar.toolsVisible");
        toolBarShow.setSelection(true);
        toolBarShow.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Runnable runnable = new Runnable() {

                    public void run() {
                        toolBarShow.setSelection(true);
                        setToolBarVisible(false);
                        application.getAppMenu().getSubMenuViewItemTools().setSelection(false);
                        parent.layout();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        toolBarLock = new XMenuItem(toolBarPopUp, SWT.CHECK);
        toolBarLock.setTextForLocaleKey("ApplicationToolBar.gui.contectmenu.lockTools");
        toolBarLock.setSelection(false);
        toolBarLock.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                final XMenuItem i = (XMenuItem) e.widget;
                Runnable runnable = new Runnable() {

                    public void run() {
                        toolBarLock.setSelection(i.getSelection());
                        coolBar.setLocked(i.getSelection());
                        application.getAppMenu().getSubMenuViewItemLocked().setSelection(i.getSelection());
                        parent.layout();
                    }
                };
                SWTUtil.showBusyWhile(parent, runnable);
            }
        });
        toolBar.setMenu(toolBarPopUp);
    }

    /**
	 * This method rearranges the tool bar. Is is called, when a robot
	 * change event occurred. The first step is to remove all items
	 * from the tool bar and then create the necessary ones for the
	 * new robot.
	 * 
	 * @param robot
	 *            Robot, the new robot.
	 */
    private void rearrangeToolBar(Robot robot) {
        removePluginItems();
        if (robot != null) {
            Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Point coolSize = coolItem.computeSize(size.x, size.y);
            coolItem.setSize(coolSize);
            for (XCoolItem itm : coolItems) {
                Point locSize = itm.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
                Point locCoolSize = coolItem.computeSize(locSize.x, locSize.y);
                itm.setSize(locCoolSize);
            }
        }
    }

    /**
	 * Removes the plugin specific items.
	 */
    private void removePluginItems() {
        for (XCoolItem itm : coolItems) {
            SWTUtil.secureDispose(itm);
        }
        coolItems.clear();
    }

    /**
	 * Sets the tool bar visible or invisible.
	 * 
	 * @param visible
	 *            <code>true</code>: the tool bar is visible.<br>
	 *            <code>false</code>: the tool bar is invisible.
	 */
    public void setToolBarVisible(boolean visible) {
        if (visible) {
            ((GridData) coolBar.getLayoutData()).heightHint = 30;
        } else {
            ((GridData) coolBar.getLayoutData()).heightHint = 0;
        }
        coolBar.setVisible(visible);
        coolBar.setEnabled(visible);
        parent.layout();
    }

    /**
	 * Locks or unlocks the tool bar, corresponding to the given
	 * boolean locked.
	 * 
	 * @param locked
	 *            <br>
	 *            <code>boolean</code>, the new lock state of the
	 *            tool bar.<br>
	 *            <code>true</code> - locked.<br>
	 *            <code>false</code> - unlocked<br>
	 */
    public void setToolBarLocked(boolean locked) {
        toolBarLock.setSelection(locked);
        coolBar.setLocked(locked);
    }
}
