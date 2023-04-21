package org.jaffa.components.maint;

import org.jaffa.presentation.portlet.component.IComponent;

/** This interface will be implemented by all Create components.
 * @author  GautamJ
 */
public interface ICreateComponent extends IComponent {

    /** Adds a listener.
     * @param listener the listener.
     */
    public void addCreateListener(ICreateListener listener);

    /** Removes a listener.
     * @param listener the listener.
     * @return true if the listener was removed.
     */
    public boolean removeCreateListener(ICreateListener listener);
}
