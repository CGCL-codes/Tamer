package org.jmlspecs.jir.openjml.exception;

import java.io.File;

public class CompileErrorException extends Exception {

    private static final long serialVersionUID = -8541227659517416791L;

    public CompileErrorException(final File source) {
        super("The following source failed to be compiled: " + source.getName());
    }
}
