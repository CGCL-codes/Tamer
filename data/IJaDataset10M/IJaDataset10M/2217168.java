package org.uk2005.swing.dialog;

import org.uk2005.dialog.*;

/**
 * Interface for swing components.
 *
 * @author	<a href="mailto:des@ofug.org">Dag-Erling Sm�rgrav</a>
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @version	$Id: Component.java,v 1.1 2002/04/23 11:44:25 niklasjs Exp $
 */
public interface Component {

    /**
	 * Prepare for presentation.
	 */
    void compose();

    /**
	 * Resets the component to the initial value.
	 */
    void reset();
}
