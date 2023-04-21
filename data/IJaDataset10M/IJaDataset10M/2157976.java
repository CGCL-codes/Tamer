package org.progeeks.util.el;

import org.progeeks.util.*;

/**
 *  Abstract base class providing some additional base functionality
 *  for PropertyAccess objects that delegate to other PropertyAccess
 *  implementations.
 *
 *  @version   $Revision: 3545 $
 *  @author    Paul Speed
 */
public abstract class AbstractDelegatingPropertyAccess extends AbstractPropertyAccess implements PropertyAccessRootAware {

    private PropertyAccess rootAccess;

    private PropertyAccess defaultAccess;

    public void setRootAccess(PropertyAccess access) {
        this.rootAccess = access;
        attachRoot(defaultAccess);
    }

    /**
     *  Returns the root access if set or this property access if not.
     *  This is used by the attachRoot() method and therefore should
     *  always resolve to something.  In general, delegating access implementations
     *  will not be re-delegating.  If they do, then the need to check to
     *  make sure rootAccess is not themselves.
     */
    protected PropertyAccess getRootAccess() {
        return (rootAccess != null ? rootAccess : this);
    }

    protected void attachRoot(PropertyAccess access) {
        if (access instanceof PropertyAccessRootAware) ((PropertyAccessRootAware) access).setRootAccess(getRootAccess());
    }

    public void setDefaultPropertyAccess(PropertyAccess defaultAccess) {
        this.defaultAccess = defaultAccess;
        attachRoot(defaultAccess);
    }

    public PropertyAccess getDefaultPropertyAccess() {
        return (defaultAccess);
    }
}
