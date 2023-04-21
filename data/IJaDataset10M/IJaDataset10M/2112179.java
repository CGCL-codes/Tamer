package org.targol.warfocdamanager.ui.advisors;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.targol.warfocdamanager.ui.Application;
import org.targol.warfocdamanager.ui.perspective.WarfoPerspective;

/**
 * This workbench advisor creates the window advisor, and specifies the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String DEFAULT_PERSPECTIVE_ID = WarfoPerspective.ID;

    /**
     * {@inheritDoc}
     */
    @Override
    public final WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
        configurer.setTitle(Application.APPLICATION_TITLE);
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getInitialWindowPerspectiveId() {
        return DEFAULT_PERSPECTIVE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void postStartup() {
        super.postStartup();
    }
}
