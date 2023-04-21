package org.nomadpim.core.ui.startup;

import org.eclipse.swt.widgets.Tray;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.nomadpim.core.ui.ICoreUIConstants;
import org.nomadpim.core.ui.tray.TrayItemRegistry;

public class NomadPIMWorkbenchAdvisor extends WorkbenchAdvisor {

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new NomadPIMWorkbenchWindowAdvisor(configurer);
    }

    public String getInitialWindowPerspectiveId() {
        return ICoreUIConstants.PLUGIN_PREFIX + "perspective";
    }

    public void initialize(IWorkbenchConfigurer configurer) {
        configurer.setSaveAndRestore(true);
    }

    /**
     * Shows the tray icons after the startup.
     */
    @Override
    public void postStartup() {
        Tray tray = PlatformUI.getWorkbench().getDisplay().getSystemTray();
        new TrayItemRegistry(tray);
    }
}
