package ca.whu.taxman.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Peter Wu <peterwu@hotmail.com>
 */
@Entity
@Table(name = "GLOBAL_DATA")
@NamedQueries({ @NamedQuery(name = "GlobalData.findAll", query = "SELECT g FROM GlobalData g"), @NamedQuery(name = "GlobalData.findByGlobalNumber", query = "SELECT g FROM GlobalData g WHERE g.globalNumber = :globalNumber"), @NamedQuery(name = "GlobalData.findByValue", query = "SELECT g FROM GlobalData g WHERE g.value = :value") })
public class GlobalData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "GLOBAL_NUMBER")
    private Integer globalNumber;

    @Column(name = "VALUE")
    private Integer value;

    @OneToMany(mappedBy = "globalNumber")
    private Set<Line> lineCollection;

    public GlobalData() {
    }

    public GlobalData(Integer globalNumber) {
        this.globalNumber = globalNumber;
    }

    public Integer getGlobalNumber() {
        return globalNumber;
    }

    public void setGlobalNumber(Integer globalNumber) {
        this.globalNumber = globalNumber;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Set<Line> getLineCollection() {
        return lineCollection;
    }

    public void setLineCollection(Set<Line> lineCollection) {
        this.lineCollection = lineCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (globalNumber != null ? globalNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GlobalData)) {
            return false;
        }
        GlobalData other = (GlobalData) object;
        if ((this.globalNumber == null && other.globalNumber != null) || (this.globalNumber != null && !this.globalNumber.equals(other.globalNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ca.whu.taxman.entity.GlobalData[globalNumber=" + globalNumber + "]";
    }
}
