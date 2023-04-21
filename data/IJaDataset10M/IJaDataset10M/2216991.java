package org.netbeans.modules.exceptions.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Jindrich Sedek
 */
@Embeddable
public class PrefsetPK implements Serializable {

    @Column(name = "PREFNAME", nullable = false)
    private int prefname;

    @Column(name = "STATISTIC", nullable = false)
    private int statistic;

    public PrefsetPK() {
    }

    public PrefsetPK(int prefname, int statistic) {
        this.prefname = prefname;
        this.statistic = statistic;
    }

    public int getPrefname() {
        return prefname;
    }

    public void setPrefname(int prefname) {
        this.prefname = prefname;
    }

    public int getStatistic() {
        return statistic;
    }

    public void setStatistic(int statistic) {
        this.statistic = statistic;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) prefname;
        hash += (int) statistic;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PrefsetPK)) {
            return false;
        }
        PrefsetPK other = (PrefsetPK) object;
        if (this.prefname != other.prefname) {
            return false;
        }
        if (this.statistic != other.statistic) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.netbeans.modules.exceptions.entity.PrefsetPK[prefname=" + prefname + ", statistic=" + statistic + "]";
    }
}
