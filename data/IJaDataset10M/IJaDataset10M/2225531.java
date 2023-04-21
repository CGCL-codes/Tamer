package org.infoeng.ictp.exceptions;

public class PermissionException extends Exception {

    public PermissionException() {
        super();
    }

    public PermissionException(String str) {
        super(str);
    }

    public PermissionException(String str, Throwable e) {
        super(str, e);
    }

    public PermissionException(Throwable e) {
        super(e);
    }
}
