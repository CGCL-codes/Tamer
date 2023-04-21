package org.nakedobjects.ide.eclipse.logging;

import org.eclipse.ui.IStartup;
import org.nakedobjects.ide.eclipse.core.Activator;

public class Startup implements IStartup {

    public void earlyStartup() {
        Activator.getDefault();
    }
}
