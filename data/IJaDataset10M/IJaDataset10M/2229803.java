package net.openchrom.progress.ui;

import net.openchrom.progress.core.StatusLineLogger;
import net.openchrom.progress.ui.internal.core.UIStatusLineLogger;
import org.eclipse.ui.IStartup;

/**
 * @author eselmeister
 */
public class PluginStartup implements IStartup {

    private static UIStatusLineLogger uiStatusLineLogger;

    @Override
    public void earlyStartup() {
        uiStatusLineLogger = new UIStatusLineLogger();
        StatusLineLogger.add(uiStatusLineLogger);
    }

    /**
	 * Returns the instance of ui status line logger or null if not exists.
	 * 
	 * @return
	 */
    public static UIStatusLineLogger getUIStatusLineLogger() {
        return uiStatusLineLogger;
    }
}
