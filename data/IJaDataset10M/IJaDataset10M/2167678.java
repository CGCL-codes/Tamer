package com.google.web.bindery.requestfactory.server;

/**
 * Used to test covariant return types.
 */
public interface HasId {

    Object getId();

    Object persistAndReturnSelf();
}
