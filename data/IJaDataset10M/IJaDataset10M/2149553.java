package net.openchrom.chromatogram.msd.filter.supplier.rtshifter.ui.handlers;

import java.lang.reflect.InvocationTargetException;
import net.openchrom.chromatogram.msd.filter.supplier.rtshifter.ui.modifier.FilterModifier;
import net.openchrom.chromatogram.msd.model.core.support.IChromatogramSelection;
import net.openchrom.chromatogram.msd.model.notifier.IHandlerUpdater;
import net.openchrom.chromatogram.msd.model.notifier.ISelectionUpdateNotifier;
import net.openchrom.logging.core.Logger;
import net.openchrom.progress.core.InfoType;
import net.openchrom.progress.core.StatusLineLogger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

public abstract class FilterHandler extends AbstractHandler implements IMillisecondsToShift {

    private static final Logger logger = Logger.getLogger(FilterHandler.class);

    private IHandlerUpdater handlerUpdater;

    /**
	 * The constructor.
	 */
    public FilterHandler() {
        handlerUpdater = new FilterHandler.SelectionUpdateListener();
        handlerUpdater.setParent(this);
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        final IChromatogramSelection chromatogramSelection = handlerUpdater.getChromatogramSelection();
        final Display display = Display.getCurrent();
        StatusLineLogger.setInfo(InfoType.MESSAGE, "Start RTShifter Filter");
        IRunnableWithProgress runnable = new FilterModifier(chromatogramSelection, getMillisecondsToShift());
        ProgressMonitorDialog monitor = new ProgressMonitorDialog(display.getActiveShell());
        try {
            monitor.run(true, true, runnable);
        } catch (InvocationTargetException e) {
            logger.warn(e);
        } catch (InterruptedException e) {
            logger.warn(e);
        }
        StatusLineLogger.setInfo(InfoType.MESSAGE, "RTShifter Filter finished");
        return null;
    }

    public static class SelectionUpdateListener implements ISelectionUpdateNotifier, IHandlerUpdater {

        @SuppressWarnings("unused")
        private static FilterHandler parentHandler;

        private static IChromatogramSelection selection;

        @Override
        public IChromatogramSelection getChromatogramSelection() {
            return selection;
        }

        @Override
        public void setParent(AbstractHandler parent) {
            if (parent instanceof FilterHandler) {
                parentHandler = (FilterHandler) parent;
            }
        }

        @Override
        public void update(IChromatogramSelection chromatogramSelection, boolean forceReload) {
            selection = chromatogramSelection;
        }
    }
}
