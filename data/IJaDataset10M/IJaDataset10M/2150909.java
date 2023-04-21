package org.python.compiler.sea;

import org.thobe.compiler.sea.Value;

public abstract class SelectionCallback {

    protected abstract Value onTrue() throws Exception;

    protected abstract Value onFalse() throws Exception;
}
