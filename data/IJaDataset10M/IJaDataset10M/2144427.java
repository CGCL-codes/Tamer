package uk.co.nimp.scard;

import javax.smartcardio.CardException;

public class ScardException extends CardException {

    static final long serialVersionUID = 3650740145456473039L;

    int code;

    public ScardException(String msg) {
        super(msg);
        code = -1;
    }

    public ScardException(String msg, Throwable e) {
        super(msg, e);
        code = -1;
    }

    public ScardException(Throwable e) {
        super("", e);
        code = -1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
