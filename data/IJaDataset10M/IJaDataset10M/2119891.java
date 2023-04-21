package open.ipdots;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author pfares
 */
@Entity
@Table(name = "dimension")
@NamedQueries({ @NamedQuery(name = "Dimension.findByDimid", query = "SELECT d FROM Dimension d WHERE d.dimid = :dimid"), @NamedQuery(name = "Dimension.findByName", query = "SELECT d FROM Dimension d WHERE d.name = :name") })
public class Dimension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dimid", nullable = false)
    private Integer dimid;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dimension")
    private Collection<Account> accountCollection;

    public Dimension() {
    }

    public Dimension(Integer dimid) {
        this.dimid = dimid;
    }

    public Integer getDimid() {
        return dimid;
    }

    public void setDimid(Integer dimid) {
        this.dimid = dimid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Account> getAccountCollection() {
        return accountCollection;
    }

    public void setAccountCollection(Collection<Account> accountCollection) {
        this.accountCollection = accountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dimid != null ? dimid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Dimension)) {
            return false;
        }
        Dimension other = (Dimension) object;
        if ((this.dimid == null && other.dimid != null) || (this.dimid != null && !this.dimid.equals(other.dimid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "open.ipdots.Dimension[dimid=" + dimid + "]";
    }
}
