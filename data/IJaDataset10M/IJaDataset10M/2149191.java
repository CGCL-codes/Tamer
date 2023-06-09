package org.eclipse.ui.internal.services;

import java.util.Map;
import java.util.TreeMap;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * <p>
 * This listens to changes to the current selection, and propagates them through
 * the <code>ISourceProvider</code> framework (a common language in which
 * events are communicated to expression-based services).
 * </p>
 * <p>
 * This class is not intended for use outside of the
 * <code>org.eclipse.ui.workbench</code> plug-in.
 * </p>
 * 
 * @since 3.2
 */
public final class CurrentSelectionSourceProvider extends AbstractSourceProvider implements INullSelectionListener {

    /**
	 * The names of the sources supported by this source provider.
	 */
    private static final String[] PROVIDED_SOURCE_NAMES = new String[] { ISources.ACTIVE_CURRENT_SELECTION_NAME };

    /**
	 * Monitors changes to the active workbench window, and swaps the selection
	 * listener to the active workbench window.
	 */
    private final IWindowListener windowListener = new IWindowListener() {

        public final void windowActivated(final IWorkbenchWindow window) {
            swapListeners(window, false);
        }

        public final void windowClosed(final IWorkbenchWindow window) {
            swapListeners(window, true);
        }

        public final void windowDeactivated(final IWorkbenchWindow window) {
            swapListeners(window, true);
        }

        public final void windowOpened(final IWorkbenchWindow window) {
            swapListeners(window, false);
        }
    };

    /**
	 * The workbench on which this source provider is acting. This value is
	 * never <code>null</code>.
	 */
    private final IWorkbench workbench;

    /**
	 * Constructs a new instance of <code>CurrentSelectionSourceProvider</code>.
	 * 
	 * @param workbench
	 *            The workbench on which this source provider should act; this
	 *            value must not be <code>null</code>.
	 */
    public CurrentSelectionSourceProvider(final IWorkbench workbench) {
        this.workbench = workbench;
        workbench.addWindowListener(windowListener);
    }

    public final void dispose() {
        workbench.removeWindowListener(windowListener);
    }

    public final Map getCurrentState() {
        final Map currentState = new TreeMap();
        final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null) {
            final ISelectionService service = window.getSelectionService();
            final ISelection selection = service.getSelection();
            currentState.put(ISources.ACTIVE_CURRENT_SELECTION_NAME, selection);
        } else {
            currentState.put(ISources.ACTIVE_CURRENT_SELECTION_NAME, null);
        }
        return currentState;
    }

    public final String[] getProvidedSourceNames() {
        return PROVIDED_SOURCE_NAMES;
    }

    public final void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (DEBUG) {
            logDebuggingInfo("Selection changed to " + selection);
        }
        fireSourceChanged(ISources.ACTIVE_CURRENT_SELECTION, ISources.ACTIVE_CURRENT_SELECTION_NAME, selection);
    }

    /**
	 * Swaps the selection listener. This either adds or removes a selection
	 * listener from the given window's selection service.
	 * 
	 * @param window
	 *            The workbench window to which the listener should be added or
	 *            from which the listener should be removed; must not be
	 *            <code>null</code>.
	 * @param remove
	 *            Whether the selection listener should be removed; otherwise,
	 *            it should be added.
	 */
    private final void swapListeners(final IWorkbenchWindow window, final boolean remove) {
        final ISelectionService selectionService = window.getSelectionService();
        if (remove) {
            window.getSelectionService().removeSelectionListener(CurrentSelectionSourceProvider.this);
            selectionChanged(null, null);
        } else {
            window.getSelectionService().addSelectionListener(CurrentSelectionSourceProvider.this);
            selectionChanged(null, selectionService.getSelection());
        }
    }
}
