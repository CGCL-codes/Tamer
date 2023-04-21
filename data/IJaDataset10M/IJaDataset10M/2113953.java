package org.qtitools.qti.node.test.flow;

import org.qtitools.qti.node.test.ControlObject;

/**
 * End node is node on end of test, test part, section or item reference.
 * 
 * @author Jiri Kajaba
 */
public class EndNode extends BorderNode {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs node.
	 *
	 * @param prev previous node in linked list
	 * @param object assessment object (test, test part, section or item reference)
	 */
    public EndNode(Node prev, ControlObject object) {
        super(prev, object);
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}
