package ru.spbspu.staub.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The <code>Role</code> class represents the Role entity.
 *
 * @author Alexander V. Elagin
 */
@Entity
@Table(schema = "staub", name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 8199473375360902017L;

    private RoleEnum id;

    private String description;

    @Id
    @Enumerated
    @Column(name = "id", nullable = false)
    public RoleEnum getId() {
        return id;
    }

    public void setId(RoleEnum id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description", length = 256, nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Role)) {
            return false;
        }
        Role other = (Role) otherObject;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User");
        sb.append("{id=").append(id);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
