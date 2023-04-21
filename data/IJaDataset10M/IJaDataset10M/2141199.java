package org.openedc.core.domain.model.support;

import org.openedc.core.domain.model.Study;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.openedc.core.persistence.Persistent;

/**
 *
 * @author openedc
 */
@Entity
@Table(name = "Study")
@NamedQueries({ @NamedQuery(name = "Study.findAll", query = "SELECT s FROM StudyEntity s"), @NamedQuery(name = "Study.findById", query = "SELECT s FROM StudyEntity s WHERE s.id = :id") })
public class StudyEntity extends PersistentEntity implements Serializable, Persistent, Study {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public StudyEntity() {
    }

    public StudyEntity(Long id) {
        this.id = id;
    }

    public Object getId() {
        return this.id;
    }

    @Override
    public void setId(Object id) {
        this.id = (Long) id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StudyEntity)) {
            return false;
        }
        StudyEntity other = (StudyEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
