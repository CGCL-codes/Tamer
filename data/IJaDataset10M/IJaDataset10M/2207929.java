package ch.romix.jsens.gui.effects;

import ch.romix.jsens.bo.EffectStructure;

/**
 * This listener interface is used to listen on selections in the
 * EffectListPresenterView.
 * 
 * @author romi
 * 
 */
public interface EffectListSelectionListener {

    /**
	 * This event is fired if the selected effect structure has changed.
	 * 
	 * @param effectStructure
	 *            The new effect structure or null if no structure is selected.
	 */
    public void selectionChanged(EffectStructure effectStructure);
}
