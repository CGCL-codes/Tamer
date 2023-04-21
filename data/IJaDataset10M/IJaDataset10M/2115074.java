package info.gdeDengi.expense;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the expensetemplate database table.
 * 
 */
@Embeddable
public class ExpensetemplatePK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String templateid;

    @Column(unique = true, nullable = false)
    private String userid;

    public ExpensetemplatePK() {
    }

    public String getTemplateid() {
        return this.templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExpensetemplatePK)) {
            return false;
        }
        ExpensetemplatePK castOther = (ExpensetemplatePK) other;
        return this.templateid.equals(castOther.templateid) && this.userid.equals(castOther.userid);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.templateid.hashCode();
        hash = hash * prime + this.userid.hashCode();
        return hash;
    }
}
