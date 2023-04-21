package net.tourbook.ui.views;

import net.tourbook.ui.views.tourBook.TourBookView;
import net.tourbook.util.Util;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ActionHandlerOpenViewTourBook extends AbstractHandler {

    public Object execute(final ExecutionEvent event) throws ExecutionException {
        Util.showView(TourBookView.ID);
        return null;
    }
}
