package org.monet.kernel.model;

public interface IObserver {

    public Boolean update(IObservable oObservable, String sReason);
}
