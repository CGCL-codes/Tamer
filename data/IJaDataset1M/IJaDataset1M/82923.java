package org.nightlabs.eclipse.rap.dionysos;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class DionysosClientWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public DionysosClientWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new DionysosActionBuilder(configurer);
    }

    public void preWindowOpen() {
        super.preWindowOpen();
        IWorkbenchConfigurer x;
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setTitle("Dionysos");
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowMenuBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowCoolBar(false);
        configurer.setShowPerspectiveBar(false);
    }
}
