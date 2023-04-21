package org.kommando.application.actions;

import java.io.IOException;
import org.kommando.application.core.Application;
import org.kommando.core.action.AbstractAction;
import org.kommando.core.action.Action;
import org.kommando.core.action.ActionExecutionException;
import org.kommando.core.catalog.CatalogObject;
import org.kommando.filesystem.core.FileSystemObject;

/**
 * {@link Action} for running {@link Application}s.
 * 
 * @author Peter De Bruycker
 */
public class OpenAction extends AbstractAction {

    @Override
    public CatalogObject execute(CatalogObject direct, CatalogObject indirect) throws ActionExecutionException {
        Application application = direct.getExtension(Application.class);
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", application.getFile().getAbsolutePath());
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getId() {
        return "application:open";
    }

    @Override
    public Class<?>[] getDirectExtensionTypes() {
        return new Class<?>[] { Application.class };
    }

    @Override
    public String getDescription() {
        return "Open the application";
    }

    @Override
    public String getName() {
        return "Open";
    }
}
