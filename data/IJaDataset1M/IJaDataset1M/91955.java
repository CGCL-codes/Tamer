package com.threerings.jpkg;

/**
 * An exception, most likely wrapping another, thrown when the package building fails.
 */
public class PackageBuilderException extends Exception {

    public PackageBuilderException(String reason) {
        super(reason);
    }

    public PackageBuilderException(Throwable reason) {
        super(reason);
    }
}
