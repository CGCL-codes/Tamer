package de.fraunhofer.isst.axbench.utilities;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import de.fraunhofer.isst.axbench.editors.axlmultipage.AXLMultiPageEditor;

/**
 * @brief could realize the startup
 * @author skaegebein
 * @author ekleinod
 * @version 0.9.0
 * @since 0.8.0
 */
public class Startup implements IStartup {

    public void earlyStartup() {
        IWorkbenchPage activePage = null;
        IWorkbenchWindow[] windows = null;
    }
}
