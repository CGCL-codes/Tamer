package org.qtitools.qti.node.item.interaction;

import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.node.content.basic.Flow;
import org.qtitools.qti.node.content.basic.Inline;

/**
 * An interaction that appears inline.
 * 
 * @author Jonathon Hare
 */
public abstract class InlineInteraction extends Interaction implements Inline, Flow {

    private static final long serialVersionUID = 1L;

    /**
	 * Construct new interaction.
	 *  
	 * @param parent Parent node
	 */
    public InlineInteraction(XmlObject parent) {
        super(parent);
    }
}
