package org.mapache.business;

public class MapacheException extends Exception {

    public MapacheException(String message, Exception e) {
        super(message, e);
    }
}
