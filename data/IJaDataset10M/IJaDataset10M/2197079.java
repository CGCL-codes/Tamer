package org.dengues.rcp.intro;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;

public class ApplicationWorkbenchAdvisor extends IDEWorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = DenguesETLPerspective.ID;

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    public void postStartup() {
        super.postStartup();
    }
}
