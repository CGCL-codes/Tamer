package org.wtc.eclipse.platform.shellhandlers;

import com.windowtester.runtime.condition.IHandler;

/**
 * Shell handler for the Shell Monitor that will handle a shell that may or may not pop
 * and is to be disposed without failing the test.
 * 
 * @since 3.8.0
 */
public interface IWorkbenchShellHandler extends IHandler {

    /**
     * @return  String - The title of the dialog that this handler reacts to
     */
    public String getTitle();

    /**
     * @return  boolean - True if this dialog is modal when it is shown; False otherwise
     */
    public boolean isModal();
}
