package org.eclipse.rap.demo;

import org.eclipse.ui.application.*;

public class DemoWorbenchAdvisor extends WorkbenchAdvisor {

    public String getInitialWindowPerspectiveId() {
        return "org.eclipse.rap.demo.perspective";
    }

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer windowConfigurer) {
        return new DemoWorkbenchWindowAdvisor(windowConfigurer);
    }
}
