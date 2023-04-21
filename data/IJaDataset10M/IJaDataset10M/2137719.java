package org.t2framework.confeito.exception;

public class ResourceNotFoundRuntimeException extends T2BaseRuntimeException {

    private static final long serialVersionUID = 1L;

    private String path;

    public ResourceNotFoundRuntimeException(String path) {
        super("ECMN0025", new Object[] { path });
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
