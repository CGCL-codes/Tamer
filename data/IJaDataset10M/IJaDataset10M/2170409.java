package net.sf.pim.action;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

/**
 * @author lzhang
 * 显示菜单及状态栏的开关
 */
public class ToggleMenuAction extends UiAction {

    private IWorkbenchWindowConfigurer configurer;

    private static ToggleMenuAction instance;

    public ToggleMenuAction() {
        super();
        name = "开/关(菜单)";
        gif = "menu.gif";
        instance = this;
    }

    public void run() {
        super.run();
        if (configurer.getShowMenuBar()) {
            configurer.setShowMenuBar(false);
        } else {
            configurer.setShowMenuBar(true);
        }
        Display.getDefault().getActiveShell().layout();
        Display.getDefault().getActiveShell().redraw();
    }

    public IWorkbenchWindowConfigurer getConfigurer() {
        return configurer;
    }

    public void setConfigurer(IWorkbenchWindowConfigurer configurer) {
        this.configurer = configurer;
    }

    public static ToggleMenuAction getInstance() {
        return instance;
    }
}
