package org.dancecues.data;

import java.util.Observer;

/**
 * Represents a class that provides a foot for a step.
 */
public interface FootProvider {

    /**
     * @return The foot that is available for the next step.
     */
    public Foot provideFoot();

    /**
     * Allows the provider to be told to provide the Opposite foot from what it is currently
     * providing.
     */
    public void swapFoot();

    /**
     * Change the provided foot.  I.E. if you call setProvidedFoot(Foot.LEFT), calls to provideFoot()
     * should now return Foot.LEFT.
     * @param newFoot The foot to provide from now on.
     */
    public void setProvidedFoot(Foot newFoot);

    /**
     * Hack-and-a-half, we want to force FootProviders to be observable, but
     * Observable is a class, not an interface.  So we'll just copy the methods onto
     * the interface, and anyone who implements this interface will have to either
     * extend Observable or implement the methods themselves.
     * @param addMe Observer to add to our list of observers.
     */
    public void addObserver(Observer addMe);

    /**
     * Hack-and-a-half, we want to force FootProviders to be observable, but
     * Observable is a class, not an interface.  So we'll just copy the methods onto
     * the interface, and anyone who implements this interface will have to either
     * extend Observable or implement the methods themselves.
     * @param delMe Observer to remove from our list of observers.
     */
    public void deleteObserver(Observer delMe);
}
