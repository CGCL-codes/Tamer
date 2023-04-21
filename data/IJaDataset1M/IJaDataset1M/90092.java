package com.webobjects.eocontrol;

public interface EOFaulting {

    public void clearFault();

    public boolean isFault();

    public void willRead();
}
