package purej.dao.processor.crud;

import purej.exception.NestedRuntimeException;

/**
 * �ڵ� CRUD ����
 * 
 * @author leesangboo
 */
public class AutoCRUDException extends NestedRuntimeException {

    private static final long serialVersionUID = 6917050304913971142L;

    public AutoCRUDException(String msg) {
        super(msg);
    }

    public AutoCRUDException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
