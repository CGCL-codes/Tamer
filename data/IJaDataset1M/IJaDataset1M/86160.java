package org.simplextensions.graph;

/**
 * 
 * @author Tomasz Krzyzak, <a
 *         href="mailto:tomasz.krzyzak@gmail.com">tomasz.krzyzak@gmail.com</a>
 * @since 2010-04-01 22:54:00
 */
public class NodeAlreadyExistsException extends GraphException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8894495738130212266L;

    public NodeAlreadyExistsException(String message) {
        super(message);
    }
}
