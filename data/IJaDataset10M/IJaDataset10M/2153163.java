package cn.myapps.core.security;

import java.security.Permission;
import java.security.Principal;
import java.util.Iterator;
import javax.security.auth.Subject;
import cn.myapps.base.dao.ValueObject;

/**
 * The resouce permission
 */
public class ResourcePermission extends Permission {

    private static final long serialVersionUID = -966377775560618317L;

    /**
	 * @param name The resouce permiss name.
	 */
    public ResourcePermission(String name) {
        super(name);
    }

    public boolean implies(Permission permission) {
        return true;
    }

    public boolean equals(Object obj) {
        return true;
    }

    public int hashCode() {
        StringBuffer value = new StringBuffer(getName());
        return value.toString().hashCode() * 10;
    }

    public String getActions() {
        return null;
    }

    /**
	 * Checks if the subject owns the resource by comparing all of the Subject's principals to 
	 * the resource.getOwner() value.
	 * @param user Ther user
	 * @param resource The resouce.
	 * @return True for the user is the owner of the resource , false otherwise.
	 */
    private boolean isResourceOwner(Subject user, ValueObject resource) {
        String owner = resource.getId();
        Iterator principalIterator = user.getPrincipals().iterator();
        while (principalIterator.hasNext()) {
            Principal principal = (Principal) principalIterator.next();
            if (principal.getName().equals(owner)) return true;
        }
        return false;
    }
}
