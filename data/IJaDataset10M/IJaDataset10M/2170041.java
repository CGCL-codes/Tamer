package org.red5.server.service;

/**
 *
 * @author The Red5 Project (red5@osflash.org)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 */
public class MethodNotFoundException extends RuntimeException {

    public MethodNotFoundException(String methodName) {
        super("Method not found: " + methodName);
    }
}
