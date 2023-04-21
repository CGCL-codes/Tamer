package quickfinance.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author epal
 */
@Entity
@Table(name = "FINGROUPS", catalog = "", schema = "APP")
@NamedQueries({ @NamedQuery(name = "Fingroups.findAll", query = "SELECT f FROM Fingroups f"), @NamedQuery(name = "Fingroups.findById", query = "SELECT f FROM Fingroups f WHERE f.id = :id"), @NamedQuery(name = "Fingroups.findByName", query = "SELECT f FROM Fingroups f WHERE f.name = :name"), @NamedQuery(name = "Fingroups.findByDescription", query = "SELECT f FROM Fingroups f WHERE f.description = :description") })
public class Fingroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;

    @JoinColumn(name = "CURRENCYTYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Currencytypes currencytypeId;

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private User userId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fingroupId")
    private List<Reminders> remindersCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "financegroupId")
    private List<Accounts> accountsCollection;

    public Fingroups() {
    }

    public Fingroups(Long id) {
        this.id = id;
    }

    public Fingroups(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Currencytypes getCurrencytypeId() {
        return currencytypeId;
    }

    public void setCurrencytypeId(Currencytypes currencytypeId) {
        this.currencytypeId = currencytypeId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public List<Reminders> getRemindersCollection() {
        return remindersCollection;
    }

    public void setRemindersCollection(List<Reminders> remindersCollection) {
        this.remindersCollection = remindersCollection;
    }

    public List<Accounts> getAccountsCollection() {
        return accountsCollection;
    }

    public void setAccountsCollection(List<Accounts> accountsCollection) {
        this.accountsCollection = accountsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Fingroups)) {
            return false;
        }
        Fingroups other = (Fingroups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "quickfinance.Fingroups[id=" + id + "]";
    }
}
