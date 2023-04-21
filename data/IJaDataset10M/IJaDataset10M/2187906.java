package org.gello.client;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(900, 700));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
    }

    @Override
    public void postWindowOpen() {
        initStatusLine();
    }

    private void initStatusLine() {
        IStatusLineManager statusline = getWindowConfigurer().getActionBarConfigurer().getStatusLineManager();
        statusline.add(new StatusLine("UserStatus"));
        statusline.update(true);
    }

    @Override
    public void dispose() {
    }
}
