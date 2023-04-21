package net.sf.uibuilder;

import java.util.Hashtable;
import java.util.List;

/**  
 * This interface defines the common methods used to build the component.
 *
 * @version   1.0 2002-7-9
 * @author <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public interface ComponentBuilder {

    /**
     * Build the exact component.
     * 
     * @return The exact component.
     */
    public Object build();

    /**
     * Builds the specified component.
     * 
     * @param componentObj The specified component object.
     * 
     * @return The built component.
     */
    public Object build(Object componentObj);

    /**
     * Returns a hash table of components in this component, if current
     * component is a container.  Otherwise it returns null.
     * 
     * @return The hashtable of components.
     */
    public Hashtable getComponents();
}
