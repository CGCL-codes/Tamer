package org.pustefixframework.editor.common.exception;

/**
 * Signals a problem with the index during a search operation.  
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class EditorSearchIndexException extends EditorSearchException {

    /**
     * 
     */
    private static final long serialVersionUID = -7099684812635407162L;

    public EditorSearchIndexException() {
        super();
    }

    public EditorSearchIndexException(String arg0) {
        super(arg0);
    }

    public EditorSearchIndexException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public EditorSearchIndexException(Throwable arg0) {
        super(arg0);
    }
}
