package net.tourbook.ui.views;

import net.tourbook.ui.views.rawData.RawDataView;
import net.tourbook.util.Util;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ActionHandlerOpenViewTourImport extends AbstractHandler {

    public Object execute(final ExecutionEvent event) throws ExecutionException {
        Util.showView(RawDataView.ID);
        return null;
    }
}
