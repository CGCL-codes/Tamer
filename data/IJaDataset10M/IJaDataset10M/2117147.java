package net.sf.beatrix.core;

import net.sf.beatrix.core.module.preferences.PreferenceStore;

/**
 * {@link ComplexComponent} describes the basic methods of a detector component
 * as used in the actual {@link Detector} class.
 * 
 * @author Christian Wressnegger <chwress@users.sourceforge.net>
 */
public interface ComplexComponent {

    /**
   * Set the preferences for this detector component.
   * 
   * @param preferences
   *          The preferences for the component.
   */
    public void setPreferences(PreferenceStore preferences);

    /**
   * @return The last output of the complex component. The type of the result is
   *         up to the implementing class.
   */
    public Object getLastOutput();
}
