package org.uk2005.dialog;

/**
 * Superclass for labels.
 *
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @author	<a href="mailto:des@ofug.org">Dag-Erling Sm�rgrav</a>
 * @version	$Id: AbstractLabel.java,v 1.1 2002/05/02 16:09:25 decs Exp $
 */
public class AbstractLabel extends AbstractElement implements Label {

    /**
	 * Default constructor.
	 */
    public AbstractLabel() {
        super();
    }

    /**
	 * Constructs a label with the given name.
	 */
    public AbstractLabel(String name) {
        super(name);
    }

    /**
	 * Constructs a label with the given name and label.
	 */
    public AbstractLabel(String name, String label) {
        super(name, label);
    }
}
