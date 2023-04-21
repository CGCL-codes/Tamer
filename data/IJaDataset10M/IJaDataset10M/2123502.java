package vwe.application;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import vwe.Activator;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private TrayItem trayItem;

    /** ϵͳ���̵�ͼ����� */
    private Image trayImage;

    /** ����Ĳ˵��� */
    private ApplicationActionBarAdvisor actionBarAdvisor;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setTitle("Vertical Web Extractor");
        final IWorkbenchWindow window = getWindowConfigurer().getWindow();
        trayItem = initTrayItem(window);
        if (trayItem != null) {
            createPopupMenu(window);
        }
        PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
    }

    /**
	 * ����ϵͳ���̲˵�
	 * 
	 * @param window
	 *            ����̨���ڶ���
	 */
    private void createPopupMenu(final IWorkbenchWindow window) {
        trayItem.addListener(SWT.MenuDetect, new Listener() {

            public void handleEvent(Event event) {
                MenuManager trayMenu = new MenuManager();
                Menu menu = trayMenu.createContextMenu(window.getShell());
                actionBarAdvisor.fillTrayItem(trayMenu);
                menu.setVisible(true);
            }
        });
    }

    /**
	 * ��ʼ��ϵͳ���̶���
	 * 
	 * @param window
	 *            ����̨���ڶ���
	 * @return �ó������Ӧ��ϵͳ���̶���
	 */
    private TrayItem initTrayItem(IWorkbenchWindow window) {
        final Tray tray = Display.getCurrent().getSystemTray();
        if (tray == null) return null;
        TrayItem trayItem = new TrayItem(tray, SWT.NONE);
        trayImage = Activator.getImageDescriptor("icons/sample.gif").createImage();
        trayItem.setImage(trayImage);
        trayItem.setToolTipText("SuperCRM");
        return trayItem;
    }

    /** �ͷŴ��ڣ��ͷ�ϵͳ���� */
    public void dispose() {
        if (trayImage != null) {
            trayImage.dispose();
            trayItem.dispose();
        }
    }
}
