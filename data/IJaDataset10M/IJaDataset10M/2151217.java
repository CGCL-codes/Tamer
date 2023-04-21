package com.sitescape.team.ssfs;

public class NoSuchObjectException extends RuntimeException {

    public NoSuchObjectException() {
    }

    public NoSuchObjectException(String msg) {
        super(msg);
    }
}
