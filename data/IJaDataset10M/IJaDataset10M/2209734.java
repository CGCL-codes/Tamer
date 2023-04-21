package com.dasberg.gwt.command;

import java.io.Serializable;

/**
 * An Action is sent to the Dispatcher.
 * The Dispatcher returns the given generic type parameter that extends Result.
 * @author mischa
 */
public interface Action<R extends Result> extends Serializable {
}
