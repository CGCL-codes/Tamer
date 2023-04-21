package org.jgistools.gui.mappane.impl.swing.loaders;

import java.util.List;
import javax.swing.AbstractAction;

/**
 * Defines the interface that will be used to load the user GUI actions
 * for the map pane.
 * @author Teodor Baciu
 *
 */
public interface IMapUserActionsLoader {

    /**
	 * Loads the user actions to be displayed in the map pane.
	 * @return a list containing {@link AbstractAction} objects
	 * @throws Exception if an error occurs during loading
	 */
    public List<AbstractAction> loadUserActions() throws Exception;
}
