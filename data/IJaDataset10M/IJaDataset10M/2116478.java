package org.dctmvfs.vfs.provider.dctm.client.operations;

public interface Operations {

    public abstract void register(Class interfaceClass, Class implementationClass);

    public abstract void register(String interfaceClassName, String implementationClassName);

    public abstract void unregister(Class interfaceClass);

    public abstract void unregister(String interfaceClassName);

    public abstract Operation getOperation(Class interfaceClass) throws OperationException;

    public abstract Operation getOperation(String interfaceClassName) throws OperationException;
}
