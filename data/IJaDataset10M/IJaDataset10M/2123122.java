package org.jcryptool.visual.secretsharing.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.visual.secretsharing.SecretSharingPlugin;

/**
 * This handler displays the dynamic help identified by the given context help id.
 *
 * @author Dominik Schadow
 * @version 0.9.2
 */
public class HelpHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(SecretSharingPlugin.PLUGIN_ID + ".view");
        return null;
    }
}
