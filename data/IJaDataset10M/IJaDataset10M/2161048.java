package com.jiexplorer.transferable;

public interface ITransferableDestination {

    public void paste(Object[] source, boolean opperation, Object monitor);

    public String getName();
}
