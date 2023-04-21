package org.progeeks.util;

/**
 *  Context specialization denoting a view context that holds
 *  a focused or targeted object.  Useful for generally abstracting
 *  view-models that contain a selection.
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public interface FocusedContext extends ViewContext {

    public static final String PROP_FOCUS = "focus";

    /**
     *  Sets the specified object as the current focus object
     *  for this context.
     */
    public void setFocus(Object obj);

    /**
     *  Returns the object that is the current focus of this
     *  context.
     */
    public Object getFocus();
}
