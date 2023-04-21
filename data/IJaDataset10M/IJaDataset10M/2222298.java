package org.blueoxygen.lotion.opportunity.actions;

import org.blueoxygen.cimande.function.Check;
import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.lotion.Opportunity;
import org.blueoxygen.lotion.OpportunityProduct;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author alex
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UpDateOpportunityProduct extends ActionSupport implements PersistenceAware {

    protected PersistenceManager pm;

    protected OpportunityProduct opportunityProduct;

    protected Opportunity opportunity;

    private Check check = new Check();

    private String opportunityProductId = "";

    private String price;

    private String unit;

    private String opportunityId = "";

    public String execute() {
        opportunityProduct = (OpportunityProduct) pm.getById(OpportunityProduct.class, getOpportunityProductId());
        opportunity = (Opportunity) pm.getById(Opportunity.class, getOpportunityId());
        setPrice(getPrice().replace(',', '.'));
        if (!check.checkDecimal(getPrice()) || !check.checkInt(getUnit())) {
            addActionError("Price and unit must be number");
            return INPUT;
        }
        if (getPrice().equalsIgnoreCase("")) setPrice("0");
        if (getUnit().equalsIgnoreCase("")) setUnit("0");
        double total = Double.parseDouble(getPrice()) * Double.parseDouble(getUnit());
        opportunityProduct.setPrice(getPrice());
        opportunityProduct.setUnit(getUnit());
        opportunityProduct.setSale(String.valueOf(total));
        pm.save(opportunityProduct);
        return SUCCESS;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.pm = persistenceManager;
    }

    /**
	 * @return Returns the price.
	 */
    public String getPrice() {
        return price;
    }

    /**
	 * @param price The price to set.
	 */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
	 * @return Returns the unit.
	 */
    public String getUnit() {
        return unit;
    }

    /**
	 * @param unit The unit to set.
	 */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
	 * @return Returns the opportunity.
	 */
    public Opportunity getOpportunity() {
        return opportunity;
    }

    /**
	 * @param opportunity The opportunity to set.
	 */
    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }

    /**
	 * @return Returns the opportunityId.
	 */
    public String getOpportunityId() {
        return opportunityId;
    }

    /**
	 * @param opportunityId The opportunityId to set.
	 */
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    /**
	 * @return Returns the opportunityProduct.
	 */
    public OpportunityProduct getOpportunityProduct() {
        return opportunityProduct;
    }

    /**
	 * @param opportunityProduct The opportunityProduct to set.
	 */
    public void setOpportunityProduct(OpportunityProduct opportunityProduct) {
        this.opportunityProduct = opportunityProduct;
    }

    /**
	 * @return Returns the opportunityProductId.
	 */
    public String getOpportunityProductId() {
        return opportunityProductId;
    }

    /**
	 * @param opportunityProductId The opportunityProductId to set.
	 */
    public void setOpportunityProductId(String opportunityProductId) {
        this.opportunityProductId = opportunityProductId;
    }
}
