package net.krecan.javacrumbs.jpatest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String accountNumber;

    protected Account() {
        super();
    }

    public Account(String accountNumber) {
        super();
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Account other = (Account) obj;
        if (accountNumber == null) {
            if (other.accountNumber != null) return false;
        } else if (!accountNumber.equals(other.accountNumber)) return false;
        return true;
    }

    /**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
    public String toString() {
        final String TAB = "    ";
        String retValue = "";
        retValue = "Account ( " + "id = " + this.id + TAB + "accountNumber = " + this.accountNumber + TAB + " )";
        return retValue;
    }
}
