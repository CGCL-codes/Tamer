package de.matthiasmann.twl.model;

/**
 * A generic boolean model.
 * 
 * @author Matthias Mann
 */
public interface BooleanModel {

    public boolean getValue();

    public void setValue(boolean value);

    public void addCallback(Runnable callback);

    public void removeCallback(Runnable callback);
}
