package org.pustefixframework.editor.common.exception;

/**
 * Exception signaling an action was triggered, that is not allowed in the 
 * current authentication context.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class EditorSecurityException extends EditorException {

    /**
     * 
     */
    private static final long serialVersionUID = -5918039873109287345L;

    public EditorSecurityException() {
        super();
    }

    public EditorSecurityException(String arg0) {
        super(arg0);
    }

    public EditorSecurityException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public EditorSecurityException(Throwable arg0) {
        super(arg0);
    }
}
