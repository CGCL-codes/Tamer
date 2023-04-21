package p2p.web.domainmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author S.Timofiychuk
 * 
 */
@Entity
@Table(name = "authorities")
public class Authority extends MainDomain {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1364891654548529372L;

    private String userRole;

    private Integer priorityRole;

    /**
	 * @return the userRole
	 */
    @Column(name = "user_role", unique = true)
    public String getUserRole() {
        return userRole;
    }

    /**
	 * @param userRole
	 *            the userRole to set
	 */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
	 * @return the priorityRole
	 */
    @Column(name = "priority_role")
    public Integer getPriorityRole() {
        return priorityRole;
    }

    /**
	 * @param priorityRole
	 *            the priorityRole to set
	 */
    public void setPriorityRole(Integer priorityRole) {
        this.priorityRole = priorityRole;
    }
}
