package de.sonivis.tool.view.menus;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * This will be the handler for an update {@link Command} that updates SONIVIS.
 * 
 * @author Benedikt Meuthrath
 * @version $Revision$, $Date$
 */
public class UpdateHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
