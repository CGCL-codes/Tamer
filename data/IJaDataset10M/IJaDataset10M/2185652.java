package cwi;

/**
 * This class holds a monetary value. The currency and the amount can be modified separately.
 * @author Mathis Dirksen-Thedens <zephyrsoft@users.sourceforge.net>
 * License GPL 2
 * @version $Id: Costs.java,v 1.8 2007/07/12 12:30:10 dirkhillbrecht Exp $
 */
public class Costs {

    private double amount = 0;

    private String currency = "�";

    public Costs(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Costs(double amount) {
        super();
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String toString() {
        return String.valueOf(Math.round(amount * 100) / 100) + " " + currency;
    }
}
