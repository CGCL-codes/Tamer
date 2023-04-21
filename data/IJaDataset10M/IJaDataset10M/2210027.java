package org.gvsig.gui.beans.colorbutton;

import java.util.EventListener;

/**
 *
 * @version 07/08/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public interface ColorButtonListener extends EventListener {

    /**
	 * Evento que se dispara cuando cambia el valor del componente.
	 * @param e
	 */
    public void actionValueChanged(ColorButtonEvent e);

    /**
	 * Evento que se dispara cuando cambia el valor del componente.
	 * @param e
	 */
    public void actionValueDragged(ColorButtonEvent e);
}
