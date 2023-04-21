package org.didicero.rap.base;

import org.didicero.rap.base.presentation.DemoPresentationWorkbenchAdvisor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class DemoWorkbench implements IEntryPoint {

    private static final String DEMO_PRESENTATION = "org.didicero.rap.base.presentation";

    public int createUI() {
        ScopedPreferenceStore prefStore = (ScopedPreferenceStore) PrefUtil.getAPIPreferenceStore();
        String keyPresentationId = IWorkbenchPreferenceConstants.PRESENTATION_FACTORY_ID;
        String presentationId = prefStore.getString(keyPresentationId);
        WorkbenchAdvisor worbenchAdvisor = new DemoWorkbenchAdvisor();
        if (DEMO_PRESENTATION.equals(presentationId)) {
            worbenchAdvisor = new DemoPresentationWorkbenchAdvisor();
        }
        Display display = PlatformUI.createDisplay();
        return PlatformUI.createAndRunWorkbench(display, worbenchAdvisor);
    }
}
