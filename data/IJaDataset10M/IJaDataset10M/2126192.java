package org.sump.mysql;

import java.util.Set;
import org.sump.data.Grant;
import org.sump.data.HasGrants;
import org.sump.data.Subject;

/**
 * Subject (user) from MySQL.
 * @author Sergey Grigorchuk (sergey.grigorchuk@gmail.com)
 */
public class MySQLSubject implements Subject, HasGrants {

    private String name;

    private Set<Grant> grants;

    public MySQLSubject(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final MySQLSubject other = (MySQLSubject) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    public Set<Grant> getGrants() {
        return grants;
    }

    public void setGrants(Set<Grant> grants) {
        this.grants = grants;
    }
}
