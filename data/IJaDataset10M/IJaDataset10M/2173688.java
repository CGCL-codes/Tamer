package org.cleanbuild.projectmetadata.spi;

public class ModuleLocatorException extends Exception {

    public ModuleLocatorException(String message) {
        super(message);
    }

    public ModuleLocatorException(String message, Exception cause) {
        super(message, cause);
    }
}
