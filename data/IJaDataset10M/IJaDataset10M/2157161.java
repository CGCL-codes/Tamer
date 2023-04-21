package org.opennms.netmgt.provision.service;

public class ModelImportException extends Exception {

    private static final long serialVersionUID = 6520991163434052156L;

    public ModelImportException(String msg) {
        super(msg);
    }

    public ModelImportException(String msg, Throwable t) {
        super(msg, t);
    }
}
