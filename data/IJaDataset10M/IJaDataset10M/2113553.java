package de.javatt.testrunner.finder;

import java.awt.Component;

/**
 * The ComponentFinder defines rules and regulations, which AWT or Swing
 * Components shall be selected (for robot test).
 * Different kinds of finders can be implemented. As an Example: a name
 * finder finds a component by it's name.
 * <br><br>
 * JavaTT passes ALL components found to the Finder. The finder then
 * needs to match the component if it matches the defined conditions.
 * 
 * @author Matthias Kempa
 *
 */
public interface ComponentFinder {

    /**
     * Does the component match the defined rules?
     * @param toCheck
     * @return
     */
    public boolean matches(Component toCheck);
}
