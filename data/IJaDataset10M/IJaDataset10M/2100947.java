package com.iver.cit.gvsig.project.documents.layout.tools.listener;

import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;

/**
 * Interfaz listener de rect�ngulo.
 *
 * @author Vicente Caballero Navarro
 */
public interface LayoutRectangleListener extends LayoutToolListener {

    /**
	 * Invocado cuando el usuario selecciona un rect�ngulo en la vista.
	 *
	 * @param event Rectangle.
	 *
	 * @throws BehaviorException
	 */
    public void rectangle(RectangleEvent event) throws BehaviorException;
}
