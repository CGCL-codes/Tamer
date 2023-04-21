package net.tourbook.ui.action;

import net.tourbook.tour.TourManager;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ActionHandlerCreateNewTour extends AbstractHandler {

    public Object execute(final ExecutionEvent event) throws ExecutionException {
        if (TourManager.isTourEditorModified()) {
            return null;
        }
        TourManager.openTourEditor(true).actionCreateTour();
        return null;
    }
}
