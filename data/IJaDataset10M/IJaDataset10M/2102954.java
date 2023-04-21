package net.sourceforge.javo2.example.business.entity;

/**
 * @author Nicolás Di Benedetto
 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net}.<br>
 * Created on 26/09/2007.<br>
 */
public class Account {

    /**
	 * Instance constructor.
	 */
    public Account() {
    }

    /**
	 * Entity's id.
	 */
    private Long id = null;

    /**
	 * Account's balance.
	 */
    private Double balance = null;

    /**
	 * Getter method for the id attribute.
	 * @return the id attribute.
	 */
    public Long getId() {
        return (null == this.id) ? this.id = new Long(-1) : this.id;
    }

    /**
	 * Getter method for the balance attribute.
	 * @return the balance attribute.
	 */
    public Double getBalance() {
        return (null == this.balance) ? this.balance = new Double(-99.99) : this.balance;
    }

    /**
	 * Setter method for the id attribute.
	 * @param id the id attribute to set.
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Setter method for the balance attribute.
	 * @param balance the balance attribute to set.
	 */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
