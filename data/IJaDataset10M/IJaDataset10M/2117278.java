package com.rccloud.inproxy.utils;

/**
 * Runtime exception during scanning package
 */
public class PackageScanException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PackageScanException() {
    }

    public PackageScanException(String msg) {
        super(msg);
    }

    public PackageScanException(Throwable throwable) {
        super(throwable);
    }
}
