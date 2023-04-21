package com.akivasoftware.misc.domain.persistance;

/**
 *  Interface that specifies Memento [Go4xx] persistance methods.
 *  @auhtor J.Varner
 */
public interface IMemento {

    public void setState(AState state) throws Exception;

    public AState getState() throws Exception;
}
