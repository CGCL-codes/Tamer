package org.jgistools.gui.mappane.impl.swing;

import javax.swing.JPanel;

/**
 * The public interface for accessing the functionality
 * provided by a map pane implementation.
 * @author Teodor Baciu
 *
 */
public interface IMapPane {

    /**
	 * Returns the implementation specific widget that supports
	 * this component. For a Swing implementation
	 * a {@link JPanel} will returned, for a SWT implementation a Shell 
	 * instance should be returned and so on.
	 * @return an implementation specific widget
	 */
    public Object getWidget();
}
