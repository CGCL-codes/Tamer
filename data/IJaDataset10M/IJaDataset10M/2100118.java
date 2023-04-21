package at.redcross.tacos.dbal.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

/**
 * The {@code EntityImpl} is the base class for all entity instances.
 */
@MappedSuperclass
@AuditOverrides(value = { @AuditOverride(name = "history", isAudited = false) })
public abstract class EntityImpl implements Serializable {

    private static final long serialVersionUID = -3033645633746573785L;

    @Embedded
    @Column(nullable = true)
    private History history;

    /**
     * Returns the primary key of this entity
     * 
     * @return the primary key object
     */
    public abstract Object getOid();

    /**
     * Returns a meaningful string that will be displayed in the UI to render
     * the object.
     * 
     * @param a
     *            string that describes this entity
     */
    public abstract String getDisplayString();

    public History getHistory() {
        return history;
    }
}
