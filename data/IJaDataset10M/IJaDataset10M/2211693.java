package org.paralit.isf.core;

import org.paralit.isf.exceptions.ISFException;

public interface IFileDataAccess {

    public abstract void clear(int fileID) throws ISFException;

    public abstract int write(int fileID, int port, Object data, IStoreType type) throws ISFException;

    public Object read(int fileID, IStoreType type) throws ISFException;
}
