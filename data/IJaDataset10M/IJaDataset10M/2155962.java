package org.scopemvc.view.util;

/**
 * <P>
 *
 * Views that use an {@link ActiveBoundModel} delegate must implement this
 * interface so that ActiveBoundModel can change the state of the View through
 * this generic interface. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2003/01/17 16:33:18 $
 * @created 05 September 2002
 * @see ActiveBoundModel
 */
public interface ModelBindable {

    /**
     * Get the current value (what would be set as a property of the bound model
     * object) being presented on the View.
     *
     * @return property's value from the UI.
     * @exception IllegalArgumentException if conversion from the UI
     *      representation of the property to the typed value fails.
     */
    Object getViewValue() throws IllegalArgumentException;

    /**
     * Use the passed property value and read-only state to update the View.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    void updateFromProperty(Object inValue, boolean inReadOnly);
}
