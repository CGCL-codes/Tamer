package org.gvsig.spatialindex.b2dtree;

class KeyMissingException extends Exception {

    public KeyMissingException() {
        super("Key not found");
    }
}
