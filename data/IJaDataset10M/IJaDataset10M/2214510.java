package uia.alumni.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * One User can have one role. A Role Can have many users. 
 * @author Even Åby Larsen (even.larsen@uia.no)
 */
@Entity
@Table(name = "is202_alumni_user_role")
public class UserRole {

    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private int uid;

    @Id
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** Entities must have default constructor */
    public UserRole() {
    }

    public UserRole(User user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof UserRole) return roleName.equals(((UserRole) that).roleName); else return false;
    }

    @Override
    public String toString() {
        return user.getEmail() + "-" + roleName;
    }

    public static final long serialVersionUID = 1;
}
