package com.googlecode.jumpnevolve.graphics.gui;

import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;

/**
 * Ein Informable kann über die Aktionen eines InterfaceObject informiert
 * werden. Dazu wird {@link InterfaceObject} verwendet.
 * 
 * @author Erik Wagner
 * 
 */
public interface Informable {

    /**
	 * Wird aufgerufen, wenn auf ein InterfaceObject geklickt wurde
	 * 
	 * @param object
	 *            Das Objekt, durch welches die Aktion ausgelöst wurden
	 */
    public void mouseClickedAction(InterfaceObject object);

    /**
	 * Wird aufgerufen, wenn sich die Maus über einem InterfaceObject befindet,
	 * ohne dass geklickt wurde
	 * 
	 * @param object
	 *            Das Objekt, durch welches die Aktion ausgelöst wurden
	 */
    public void mouseOverAction(InterfaceObject object);

    /**
	 * Wird aufgerufen, wenn das Objekt aktuell als aktiv ausgewählt ist
	 * 
	 * @param object
	 *            Das Objekt, das ausgewählt ist
	 */
    public void objectIsSelected(InterfaceObject object);
}
