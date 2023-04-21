package de.hpi.eworld.networkview.objects;

import java.util.Observable;

/**
 * A volatile model element is not serialized when saving a file but can
 * nevertheless be displayed on the NetworkView.
 * 
 * @author Matthias Kleine
 * 
 */
public abstract class VolatileModelElement extends Observable {

    @SuppressWarnings("rawtypes")
    public abstract VolatileGraphicsView createGraphicsItem();
}
