package org.isi.monet.core.exceptions;

public class TeamAccessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TeamAccessException(String code, String key) {
        super(code, key);
    }

    public TeamAccessException(String code, String key, Exception oReason) {
        super(code, key, oReason);
    }
}
