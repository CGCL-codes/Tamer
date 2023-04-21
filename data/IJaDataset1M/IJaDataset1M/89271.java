package foo.bar.forum.domain;

import foo.bar.forum.security.PermissionType;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a {@link foo.bar.forum.domain.User}'s Permission.
 *
 * @author tmjee
 * @version $Date$ $Id$
 */
@Entity
@Table(name = "TBL_PERMISSION")
@EntityListeners({ EntityAuditListener.class })
public class Permission implements Identifiable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "PERMISSION_TYPE")
    private PermissionType type;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID")
    private User owner;

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        if (type != that.type) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
