package de.ah7.imp.auth07;

import de.ah7.lib.authorization.Permission;
import de.ah7.lib.authorization.UserGroup;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public class PermissionBean implements Permission {

    private static Log log = LogFactory.getLog(PermissionBean.class);

    private String resource;

    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    private String right;

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    private Boolean superuser = Boolean.FALSE;

    public Boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(Boolean superuser) {
        this.superuser = superuser;
    }

    private Set<UserGroup> groups = new HashSet<UserGroup>();

    public Collection<UserGroup> getGroups() {
        return this.groups;
    }

    public PermissionBean() {
    }

    public PermissionBean(String resource, String right) {
        this.resource = resource;
        this.right = right;
    }

    public PermissionBean(String resource, String right, Boolean superuser) {
        this.resource = resource;
        this.right = right;
        this.superuser = superuser;
    }

    public PermissionBean(String resource, String right, Boolean superuser, Collection<UserGroup> groups) {
        this.resource = resource;
        this.right = right;
        this.superuser = superuser;
        this.groups.addAll(groups);
    }

    public void addGroup(UserGroup group) {
        this.groups.add(group);
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(getResource());
        result.append("#");
        if (isSuperuser()) {
            result.append("!");
        }
        result.append(getRight());
        result.append("=");
        boolean first = true;
        for (UserGroup group : getGroups()) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(group);
        }
        return result.toString();
    }
}
