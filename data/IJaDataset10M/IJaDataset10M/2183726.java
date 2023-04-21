package com.validation.manager.core.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class RiskControlPK implements Serializable {

    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CauseGen")
    @TableGenerator(name = "RiskControlGen", table = "vm_id", pkColumnName = "table_name", valueColumnName = "last_id", pkColumnValue = "risk_control", allocationSize = 1, initialValue = 1000)
    @Column(name = "id")
    private int id;

    @Basic(optional = false)
    @Column(name = "risk_control_type_id")
    private int riskControlTypeId;

    public RiskControlPK() {
    }

    public RiskControlPK(int riskControlTypeId) {
        this.riskControlTypeId = riskControlTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRiskControlTypeId() {
        return riskControlTypeId;
    }

    public void setRiskControlTypeId(int riskControlTypeId) {
        this.riskControlTypeId = riskControlTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) riskControlTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RiskControlPK)) {
            return false;
        }
        RiskControlPK other = (RiskControlPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.riskControlTypeId != other.riskControlTypeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.validation.manager.core.db.RiskControlPK[ id=" + id + ", riskControlTypeId=" + riskControlTypeId + " ]";
    }
}
