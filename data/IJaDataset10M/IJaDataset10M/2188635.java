package corpodatrecordsbackend;

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

/**
 *
 * @author luis
 */
@Entity
@Table(name = "formativeActions")
@NamedQueries({ @NamedQuery(name = "FormativeAction.findAll", query = "SELECT f FROM FormativeAction f"), @NamedQuery(name = "FormativeAction.findById", query = "SELECT f FROM FormativeAction f WHERE f.id = :id"), @NamedQuery(name = "FormativeAction.findByName", query = "SELECT f FROM FormativeAction f WHERE f.name = :name"), @NamedQuery(name = "FormativeAction.findByDescription", query = "SELECT f FROM FormativeAction f WHERE f.description = :description") })
public class FormativeAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public FormativeAction() {
        this.id = 0;
        this.name = "";
        this.description = "";
    }

    public FormativeAction(Integer id) {
        this.id = id;
        this.name = "";
        this.description = "";
    }

    public FormativeAction(String name, String description) {
        this.id = 0;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof FormativeAction)) {
            return false;
        }
        FormativeAction other = (FormativeAction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "corpodatrecordsbackend.FormativeAction[id=" + id + "]";
    }
}
