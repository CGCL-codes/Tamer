package org.isurf.gdssu.globalregistry.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author nodari
 */
@Entity
@Table(name = "synchronizationitem")
@NamedQueries({ @NamedQuery(name = "Synchronizationitem.findAll", query = "SELECT s FROM Synchronizationitem s"), @NamedQuery(name = "Synchronizationitem.findById", query = "SELECT s FROM Synchronizationitem s WHERE s.id = :id"), @NamedQuery(name = "Synchronizationitem.findByPartyyglntarget", query = "SELECT s FROM Synchronizationitem s WHERE s.partyyglntarget = :partyyglntarget") })
public class Synchronizationitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "partyyglntarget")
    private String partyyglntarget;

    @JoinColumn(name = "datapoolglntarget", referencedColumnName = "gln")
    @ManyToOne
    private Datapools datapoolglntarget;

    @JoinColumns({ @JoinColumn(name = "partyglnproducer", referencedColumnName = "gln"), @JoinColumn(name = "gtin", referencedColumnName = "gtin"), @JoinColumn(name = "tm", referencedColumnName = "tm") })
    @ManyToOne
    private Item item;

    public Synchronizationitem() {
    }

    public Synchronizationitem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartyyglntarget() {
        return partyyglntarget;
    }

    public void setPartyyglntarget(String partyyglntarget) {
        this.partyyglntarget = partyyglntarget;
    }

    public Datapools getDatapoolglntarget() {
        return datapoolglntarget;
    }

    public void setDatapoolglntarget(Datapools datapoolglntarget) {
        this.datapoolglntarget = datapoolglntarget;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Synchronizationitem)) {
            return false;
        }
        Synchronizationitem other = (Synchronizationitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.isurf.gdssu.globalregistry.db.entities.Synchronizationitem[id=" + id + "]";
    }
}
