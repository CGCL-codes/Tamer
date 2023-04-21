package org.springframework.richclient.factory;

import javax.swing.JComponent;

/**
 * A factory for producing GUI control components.
 *
 * @author Keith Donald
 */
public interface ControlFactory {

    /**
	 * Returns the control that this factory is responsible for producing.
	 * Depending on the implementation, this control may or not be a singleton
	 * instance.
	 *
	 * @return The control, never <code>null</code>.
	 */
    public JComponent getControl();
}
