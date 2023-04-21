package org.progeeks.meta.swing;

import org.progeeks.util.Inspector;

/**
 *  Simple factory that will just instantiate a renderer of a
 *  particular class.
 *
 *  @version   $Revision: 1.6 $
 *  @author    Paul Speed
 *  @author    Ray A. Conner
 */
public class DefaultRendererFactory implements RendererFactory {

    private Class rendererType;

    public DefaultRendererFactory(Class rendererType) {
        this.rendererType = rendererType;
    }

    /**
     *  Creates a renderer for a specific set of property types.
     */
    public MetaPropertyRenderer createPropertyRenderer(MetaPropertyContext viewContext) {
        if (Inspector.hasConstructor(rendererType, new Class[] { MetaPropertyContext.class })) {
            return ((MetaPropertyRenderer) Inspector.newInstance(rendererType, new Object[] { viewContext }));
        }
        return ((MetaPropertyRenderer) Inspector.newInstance(rendererType));
    }
}
