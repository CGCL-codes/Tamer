package org.realteam.hibernate.test.model.inheritance;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class EmployeePK implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String pkName;

    private long pkTime;

    public EmployeePK() {
    }

    public EmployeePK(String pkName, long pkTime) {
        this.pkName = pkName;
        this.pkTime = pkTime;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public long getPkTime() {
        return pkTime;
    }

    public void setPkTime(long pkTime) {
        this.pkTime = pkTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof EmployeePK)) return false;
        if (obj == null) return false;
        EmployeePK pk = (EmployeePK) obj;
        return pk.getPkName().equals(this.getPkName()) && pk.getPkTime() == this.getPkTime();
    }

    @Override
    public int hashCode() {
        return (int) (pkName.hashCode() + pkTime);
    }
}
