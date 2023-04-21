package hu.sztaki.dsd.abilities.invitation.ejb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * Entity class ToolEntity
 *
 * @author DSD
 */
@Entity
public class ToolEntity implements Serializable {

    @TableGenerator(name = "toolEntityGen", table = "SEQUENCE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "TOOLENTITY_ID", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "toolEntityGen")
    private Long id;

    @Basic
    private String toolName;

    /** Creates a new instance of ToolEntity */
    public ToolEntity() {
    }

    public ToolEntity(String toolName) {
        this.setToolName(toolName);
    }

    public ToolEntity(Long id, String toolName) {
        this.setToolName(toolName);
        this.setId(id);
    }

    /**
     * Returns a hash code value for the object.  This implementation computes
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this ToolEntity.  The result is
     * <code>true</code> if and only if the argument is not null and is a ToolEntity object that
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ToolEntity)) {
            return false;
        }
        ToolEntity other = (ToolEntity) object;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ToolEntity[Id=" + getId() + "]\n" + " toolName: " + this.getToolName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
}
