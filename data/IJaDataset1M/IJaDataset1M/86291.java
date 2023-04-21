package net.tourbook.mapping;

import net.tourbook.util.Util;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ActionHandlerOpenTourMapView extends AbstractHandler {

    public Object execute(final ExecutionEvent event) throws ExecutionException {
        Util.showView(TourMapView.ID);
        return null;
    }
}
