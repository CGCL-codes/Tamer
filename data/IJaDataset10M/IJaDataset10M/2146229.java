package net.sourceforge.jnsynch.event;

public interface SynchronizationListener {

    public void synchronizationEvent(SynchronizationEvent event);

    public void synchronizationEvent(String message);
}
