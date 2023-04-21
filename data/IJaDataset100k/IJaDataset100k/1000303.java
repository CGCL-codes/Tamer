package ejb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wikola
 */
@Entity
@Table(name = "libraire")
@NamedQueries({ @NamedQuery(name = "Libraire.findAll", query = "SELECT l FROM Libraire l") })
public class Libraire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "libraireid")
    private Integer libraireid;

    @Lob
    @Size(max = 65535)
    @Column(name = "librairenom")
    private String librairenom;

    @Lob
    @Size(max = 65535)
    @Column(name = "librairemdp")
    private String librairemdp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libraire")
    private List<Journal> journalList;

    public Libraire() {
    }

    public Libraire(Integer libraireid) {
        this.libraireid = libraireid;
    }

    public Integer getLibraireid() {
        return libraireid;
    }

    public void setLibraireid(Integer libraireid) {
        this.libraireid = libraireid;
    }

    public String getLibrairenom() {
        return librairenom;
    }

    public void setLibrairenom(String librairenom) {
        this.librairenom = librairenom;
    }

    public String getLibrairemdp() {
        return librairemdp;
    }

    public void setLibrairemdp(String librairemdp) {
        this.librairemdp = librairemdp;
    }

    public List<Journal> getJournalList() {
        return journalList;
    }

    public void setJournalList(List<Journal> journalList) {
        this.journalList = journalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (libraireid != null ? libraireid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Libraire)) {
            return false;
        }
        Libraire other = (Libraire) object;
        if ((this.libraireid == null && other.libraireid != null) || (this.libraireid != null && !this.libraireid.equals(other.libraireid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.Libraire[ libraireid=" + libraireid + " ]";
    }
}
