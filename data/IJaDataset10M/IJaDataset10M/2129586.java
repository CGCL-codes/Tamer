package wdp.entities.raw;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author robson
 */
@Entity
@Table(name = "POST", catalog = "", schema = "", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@NamedQueries({ @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"), @NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id"), @NamedQuery(name = "Post.findByName", query = "SELECT p FROM Post p WHERE p.name = :name") })
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPost")
    private Collection<Assignment> assignmentCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPost")
    private Collection<Worker> workerCollection;

    public Post() {
    }

    public Post(Integer id) {
        this.id = id;
    }

    public Post(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public Collection<Assignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    public void setAssignmentCollection(Collection<Assignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
    }

    public Collection<Worker> getWorkerCollection() {
        return workerCollection;
    }

    public void setWorkerCollection(Collection<Worker> workerCollection) {
        this.workerCollection = workerCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wdp.entities.raw.Post[id=" + id + "]";
    }
}
