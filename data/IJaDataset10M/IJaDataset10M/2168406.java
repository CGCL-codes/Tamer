package ostf.test.data.array;

import org.springframework.core.NestedRuntimeException;

public class DataArrayException extends NestedRuntimeException {

    private static final long serialVersionUID = -2574241549297082059L;

    public DataArrayException(String msg) {
        super(msg);
    }

    public DataArrayException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
