package org.eclipse.mylyn.context.core;

/**
 * Virtual proxy for an element or relation in the context model.
 * 
 * @author Mik Kersten
 * @since 2.0
 */
public interface IInteractionObject {

    public abstract IDegreeOfInterest getInterest();

    public abstract String getContentType();
}
