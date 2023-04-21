package info.gdeDengi.expense;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the expense_category_user database table.
 * 
 */
@Embeddable
public class ExpenseCategoryUserPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ecu_id")
    @SequenceGenerator(name = "ecu_id", sequenceName = "ecu_id")
    private Integer ecuid;

    @Basic
    @Column(unique = true, nullable = false)
    private Integer userid;

    public ExpenseCategoryUserPK() {
    }

    public Integer getEcuid() {
        return this.ecuid;
    }

    public void setEcuid(Integer ecuid) {
        this.ecuid = ecuid;
    }

    public Integer getUserid() {
        return this.userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExpenseCategoryUserPK)) {
            return false;
        }
        ExpenseCategoryUserPK castOther = (ExpenseCategoryUserPK) other;
        return this.ecuid.equals(castOther.ecuid) && this.userid.equals(castOther.userid);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.ecuid.hashCode();
        hash = hash * prime + this.userid.hashCode();
        return hash;
    }
}
