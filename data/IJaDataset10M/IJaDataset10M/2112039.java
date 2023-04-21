package org.itsnat.comp.inc;

import org.itsnat.comp.*;

/**
 * Is the base interface of "include" components. A "include" is used to insert/remove
 * dynamically a markup fragment below the associated DOM element of the component.
 *
 * <p>Only a free version is defined.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatInclude extends ItsNatElementComponent {

    /**
     * Informs whether a fragment was inserted using this component.
     *
     * @return true if a fragment was inserted.
     * @see #includeFragment(String,boolean)
     */
    public boolean isIncluded();

    /**
     * Returns the name of the current included fragment.
     *
     * @return the name of the current included fragment. Null if no fragment was inserted.
     * @see #includeFragment(String,boolean)
     */
    public String getIncludedFragmentName();

    /**
     * Includes a new markup fragment. If a fragment was included before is removed first.
     *
     * @param name the name of the markup fragment to insert.
     * @param buildComp if true markup defined components are built automatically.
     * @see #removeFragment()
     * @see ItsNatComponentManager#buildItsNatComponents(Node)
     */
    public void includeFragment(String name, boolean buildComp);

    /**
     * Removes the current included markup fragment. If no fragment was included nothing is done.
     *
     * @see #includeFragment(String,boolean)
     */
    public void removeFragment();
}
