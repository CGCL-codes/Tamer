package org.kalypso.nofdpidss.measure.construction.create.handlers.hcc;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Status;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.measures.MCUtils.MEASURE;
import org.kalypso.nofdpidss.measure.construction.create.interfaces.IMeasureConstructionCreationHandler;
import org.kalypso.nofdpidss.measure.construction.create.interfaces.IMeasureCreationMenuPart;

/**
 * @author Dirk Kuch
 */
public class MeasureWeirHandler extends AbstractHandler implements IHandler {

    /**
   * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IMeasureCreationMenuPart part = (IMeasureCreationMenuPart) NofdpCorePlugin.getWindowManager().getMenuPart();
        final IMeasureConstructionCreationHandler handler = (IMeasureConstructionCreationHandler) part.getAdapter(IMeasureConstructionCreationHandler.class);
        handler.createNewMeasure(MEASURE.eHccWeir);
        return Status.OK_STATUS;
    }
}
