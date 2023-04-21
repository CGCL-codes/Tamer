package com.google.code.jahath.endpoint.vch;

public class MissingHeaderException extends VCHProtocolException {

    private static final long serialVersionUID = 1191970505557653345L;

    public MissingHeaderException(String headerName) {
        super("Expected a " + headerName + " header");
    }
}
