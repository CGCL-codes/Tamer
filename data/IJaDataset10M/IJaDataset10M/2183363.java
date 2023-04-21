package org.az.tb.common.vo.client.exceptions;

import org.az.tb.common.vo.client.ErrorsSetVO;
import com.google.gwt.user.client.rpc.IsSerializable;

public class NoSuchUserException extends BusinessException implements IsSerializable {

    private static final long serialVersionUID = -2644900271044334478L;

    public NoSuchUserException() {
    }

    public NoSuchUserException(ErrorsSetVO errors) {
        super(errors);
    }

    public NoSuchUserException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public NoSuchUserException(String arg0) {
        super(arg0);
    }

    public NoSuchUserException(Throwable arg0) {
        super(arg0);
    }
}
