package jamsa.rcp.downloader;

import jamsa.rcp.downloader.models.TaskModel;
import jamsa.rcp.downloader.monitor.ClipBoardMonitor;
import jamsa.rcp.downloader.preference.PreferenceManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private TrayItem trayItem;

    private ApplicationActionBarAdvisor actionBarAdvisor;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        this.actionBarAdvisor = new ApplicationActionBarAdvisor(configurer);
        return this.actionBarAdvisor;
    }

    /**
	 * 退出前检查是否有任务在运行
	 */
    public boolean preWindowShellClose() {
        if (TaskModel.getInstance().isSomeTaskRun()) {
            boolean confirm = MessageDialog.openConfirm(getWindowConfigurer().getWindow().getShell(), "退出", "下载任务运行中，确定要退出吗？");
            if (confirm) TaskModel.getInstance().stopAll(); else return false;
        }
        return super.preWindowShellClose();
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(640, 480));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowMenuBar(true);
        configurer.setTitle("RCP Get");
    }

    private void hookMinimize(final IWorkbenchWindow window) {
        window.getShell().addShellListener(new ShellAdapter() {

            public void shellIconified(ShellEvent e) {
                if (PreferenceManager.getInstance().isMinimizeToTray()) window.getShell().setVisible(false);
            }
        });
        trayItem.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event event) {
                if (PreferenceManager.getInstance().isMinimizeToTray()) {
                    Shell shell = window.getShell();
                    if (!shell.isVisible()) {
                        shell.setVisible(true);
                        window.getShell().setMinimized(false);
                    }
                }
            }
        });
    }

    private void hookPopupMenu(final IWorkbenchWindow window) {
        trayItem.addListener(SWT.MenuDetect, new Listener() {

            public void handleEvent(Event event) {
                MenuManager trayMenu = new MenuManager();
                Menu menu = trayMenu.createContextMenu(window.getShell());
                actionBarAdvisor.fillTrayItem(trayMenu);
                menu.setVisible(true);
            }
        });
    }

    public void postWindowOpen() {
        final IWorkbenchWindow window = getWindowConfigurer().getWindow();
        trayItem = initTaskItem(window);
        if (trayItem != null) {
            hookPopupMenu(window);
            hookMinimize(window);
        }
        if (PreferenceManager.getInstance().isMonitorClipboard()) {
            ClipBoardMonitor.getInstance().start();
        }
    }

    private TrayItem initTaskItem(IWorkbenchWindow window) {
        final Tray tray = window.getShell().getDisplay().getSystemTray();
        if (tray == null) return null;
        TrayItem trayItem = new TrayItem(tray, SWT.NONE);
        Image trayImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, IImageKeys.RCP_GET).createImage();
        trayItem.setImage(trayImage);
        trayItem.setToolTipText("RCP Get");
        return trayItem;
    }
}
