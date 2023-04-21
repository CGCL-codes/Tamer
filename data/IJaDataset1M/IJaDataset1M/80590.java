package org.opensourcephysics.ejs.control.swing;

/**
 * An interface for children elements that need to be preupdated
 * before its parent gets updated.
 * For instance, certain drawables need to be changed before a
 * render of its parent drawing panel takes place.
 */
public interface NeedsPreUpdate {

    public void preupdate();
}
