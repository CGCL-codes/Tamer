package org.jlense.uiworks.workbench;

import org.eclipse.core.runtime.IExecutableExtension;

/**
 * ISessionAttributeManagers are strategy objects used by implementations 
 * if IWorkbenchSession that create and/or manage workbench session 
 * attribute values.
 * 
 * @see IWorkbenchSession
 * 
 * @author  ted stockwell [emorning@sourceforge.net]
 */
public interface ISessionAttributeManager extends IExecutableExtension {

    /**
     * Returns the value associated by this manager with the given attribute 
     * name in the workbench context associated with the given UI component.
     * Returns null if there is no such attribute value.
     * 
     * @param part the UI component that denotes the UI context in which the attribute is being requested.
     */
    public Object getAttribute(IWorkbenchPart part, String attributeName);

    /**
     * Sets the value associated by this manager with the given attribute 
     * name in the workbench context associated with the given UI component.
     * 
     * @param part the UI component that denotes the UI context from which the attribute is being set.
     */
    public void setAttribute(IWorkbenchPart part, String attributeName, Object attributeValue);

    /**
     * Returns a list of the attribute names recognised by this manager.
     */
    public String[] getAttributeList();
}
