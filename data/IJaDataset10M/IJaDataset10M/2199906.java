package pl.prv.consept.gestionnaire.internalframes;

import java.util.EventListener;

/**
 * Listener to the table panel
 *
 * @author Sebastian Solnica
 * @version $14.02.2006 v1.00$
 */
public interface SelectionPanelListener extends EventListener {

    /**
	 * Called when selection in filter panel was changed.
	 *
	 * @param e event
	 */
    public void selectionChanged(SelectionPanelEvent e);
}
