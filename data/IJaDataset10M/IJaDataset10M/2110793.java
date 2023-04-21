package org.xteam.cs.runtime.semantic;

public abstract class Result<T> {

    public boolean isError() {
        return false;
    }

    public abstract T value();
}
