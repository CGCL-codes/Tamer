package com.bluebrim.gui.client;

/**
 * Interface f�r klasser som lyssnar efter event av typen
 * CoPostBuildEvent.
 * @author Lars Svad�ngs
 */
public interface CoPostBuildListener extends java.util.EventListener {

    /**
 * @param anEvent CoPostBuildEvent
 */
    public void postBuild(CoPostBuildEvent anEvent);
}
