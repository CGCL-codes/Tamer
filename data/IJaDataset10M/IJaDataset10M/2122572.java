package org.plazmaforge.studio.reportdesigner.rcp;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.plazmaforge.studio.reportdesigner.perspectives.ReportDesignerPerspective;

/** 
 * @author Oleh Hapon
 * $Id: ReportDesignerWorkbenchAdvisor.java,v 1.2 2010/04/28 06:43:11 ohapon Exp $
 */
public class ReportDesignerWorkbenchAdvisor extends WorkbenchAdvisor {

    /**
	 * Allow SQL Explorer to save and restore layout and views
	 */
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    }

    /**
	 * Get unique id for our sql explorer perspective.
	 * This should match a perspective defined in the plugin.xml
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
    public String getInitialWindowPerspectiveId() {
        return ReportDesignerPerspective.class.getName();
    }

    /**
	 * Instantiate our own window advisor.
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ReportDesignerWorkbenchWindowAdvisor(configurer);
    }
}
