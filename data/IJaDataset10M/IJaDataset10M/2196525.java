package org.ostion.util.ui;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("objectUtils")
@Scope(ScopeType.SESSION)
@Startup
@BypassInterceptors
public class ObjectUtils {

    public boolean notSame(Object object1, Object object2) {
        return object1 != object2;
    }

    public boolean same(Object object1, Object object2) {
        return object1 == object2;
    }

    public boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }
}
