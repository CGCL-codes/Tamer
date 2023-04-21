package com.acv.common.model.entity;

import java.io.Serializable;

/**
 * Class to encapsulate a commission available to an agent.
 * @author pmartin
 *
 */
public class Commission implements Serializable {

    private static final long serialVersionUID = 3458667609250168937L;

    /** The dollar amount of the commission available to the agent */
    protected Price amount = new Price(0f);

    protected Taxes taxes = new Taxes();

    public Commission() {
        setPrice(0f);
    }

    public Commission(float amount) {
        setPrice(amount);
    }

    public Price getTotalAmount() {
        return getTaxesAmount().add(amount);
    }

    public Price getPrice() {
        return amount;
    }

    public void setPrice(float amount) {
        this.amount = new Price(amount);
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    public Price getTaxesAmount() {
        float taxSum = 0f;
        for (Tax tax : getTaxes()) {
            taxSum += tax.getAmount();
        }
        return new Price(taxSum);
    }
}
