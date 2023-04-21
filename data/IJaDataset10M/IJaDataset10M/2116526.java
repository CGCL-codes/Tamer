package de.ibis.permoto.gui.modelling.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import de.ibis.permoto.gui.modelling.ui.ModellingGUIComponentContainer;

/**
 * An action opening an about window.
 * @author Christian Markl
 * @author Oliver H�hn
 */
public class ActionAbout extends AbstractPerMoToAction {

    /** The serialVersionUID. */
    private static final long serialVersionUID = -5025496914583215892L;

    /**
	 * Defines an <code>Action</code> object with a default
	 * description string and default icon.
	 * @param container The ComponentContainer from which this action is invoked
	 */
    public ActionAbout(final ModellingGUIComponentContainer container) {
        super("About PerMoTo...", container);
        this.putValue(SHORT_DESCRIPTION, "About PerMoTo");
        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
    }

    /**
	 * Invoked when an action occurs.
	 * @param e The event which invoked the event
	 */
    public final void actionPerformed(final ActionEvent e) {
        container.about();
    }
}
