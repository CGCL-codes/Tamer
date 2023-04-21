package exmld.editor;

/**
 * An instance of this class is thrown if a validation error occurs in the course 
 * of the execution of validate in class AdapterNode. It indicates that the 
 * XML-document does not match the DTD.
 * @author Johannes Dieterich
 * @see exmld.editor.AdapterNode#validate()
 */
public class ValidationException extends ElementInsertionException {

    /**
	 * The Valdtion message can be sent with this method.
	 * @param s	the message
	 */
    public ValidationException(String s) {
        super(s, null, null);
    }

    /**
     * The parent of the <code>Node</code> is used for this method.
     * @param	parent	should be the parent of the <code>Node</code> that the exception occurs for.
     */
    public ValidationException(org.w3c.dom.Node parent) {
        super(null, null, parent);
    }

    /**
     * Both message and parent will be used.
     * @param s	the message
     * @param parent	the parent.
     */
    public ValidationException(String s, org.w3c.dom.Node parent) {
        super(s, null, parent);
    }
}
