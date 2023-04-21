package com.google.gdt.eclipse.designer.hosted.tdt;

import com.google.gdt.eclipse.designer.hosted.IBrowserShell;
import com.google.gwt.dev.shell.designtime.ModuleSpace;

/**
 * Represents an individual browser window and all of its controls.
 * @coverage gwtHosted
 */
public abstract class BrowserShell implements IBrowserShell {

    private IBrowserShellHost host;

    protected ModuleSpace moduleSpace;

    public final void setHost(IBrowserShellHost host) {
        this.host = host;
    }

    public final IBrowserShellHost getHost() {
        return host;
    }

    public void dispose() {
        host = null;
    }

    public final ModuleSpace getModuleSpace() {
        return moduleSpace;
    }
}
