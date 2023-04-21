package org.progeeks.meta.util;

import org.progeeks.meta.*;

/**
 *  MetaKit implementation combining one or more other
 *  meta-kit implementations to support compositing meta-objects.
 *
 *  @version   $Revision: 1.5 $
 *  @author    Paul Speed
 */
public class CompositeMetaKit implements MetaKit {

    /**
     *  Returns the internal object representation for the specified
     *  meta-object if one exists.  For meta-object implementations that
     *  wrap an internal object, this will return the internal object.
     *  null is returned if the meta-object implementation does not
     *  internally wrap real objects.
     */
    public Object getInternalObject(MetaObject object) {
        return (null);
    }

    /**
     *  Returns the appropriate meta-class for the specified object.
     *  If an appropriate meta-class has not been registered then
     *  this method returns null.
     */
    public MetaClass getMetaClassForObject(Object object, MetaClassRegistry classRegistry) {
        return (null);
    }

    /**
     *  Returns the appropriate meta-class for the specified object.
     *  If an appropriate meta-class has not been registered then
     *  this method returns null.  The meta-class is looked up in the 
     *  context class registry.
     */
    public MetaClass getMetaClassForObject(Object object) {
        return (getMetaClassForObject(object, MetaClassRegistry.getContextRegistry()));
    }

    /**
     *  Wraps the specified object in a meta-object adapter specific
     *  to this kit's implementation layer.  Returns null if the object
     *  cannot be wrapped.  Throws UnsupportedOperationException if this
     *  kit doesn't support wrapping.
     */
    public MetaObject wrapObject(Object object, MetaClass mClass) {
        return (null);
    }

    /**
     *  Returns a factory that can be used to create new MetaObject
     *  based values using this kit's underlying implementation.
     */
    public MetaObjectFactory getMetaObjectFactory() {
        return (new CompositeMetaObjectFactory());
    }

    private static class CompositeMetaObjectFactory implements MetaObjectFactory {

        /**
         *  Creates a new uninitialized meta-object for the specified
         *  meta-class.
         */
        public MetaObject createMetaObject(MetaClass type) {
            throw new UnsupportedOperationException("Composite instantiation is currently not supported.");
        }
    }
}
