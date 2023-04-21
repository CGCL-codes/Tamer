package org.blueoxygen.obat;

import java.math.BigDecimal;
import org.blueoxygen.cimande.DefaultPersistent32;
import org.blueoxygen.cimande.function.Check;

/**
 * @author alex
 * @hibernate.class table="opportunityProduct"
 */
public class OpportunityProduct extends DefaultPersistent32 {

    private String name;

    private String productId;

    private String price;

    private String unit;

    private String sale;

    private Opportunity opportunity;

    private Check check = new Check();

    /**
	 * @return Returns the opportunity.
	 * @hibernate.many-to-one column="opportunityId"
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
	 * @return Returns the unit.
	 * 
	 * @hibernate.property
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
	 * @return Returns the productId.
	 * @hibernate.property
	 */
    public String getProductId() {
        return productId;
    }

    /**
	 * @param productId The productId to set.
	 */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
	 * @return Returns the name.
	 * @hibernate.property
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the price.
	 * @hibernate.property 
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
	 * @return Returns the sale.
	 * @hibernate.property 
	 */
    public String getSale() {
        return sale;
    }

    /**
	 * @param sale The sale to set.
	 */
    public void setSale(String sale) {
        this.sale = sale;
    }

    public String viewPrice() {
        return check.viewMoney(new BigDecimal(getPrice()));
    }

    public String getViewPrice() {
        return viewPrice();
    }

    public String viewSale() {
        return check.viewMoney(new BigDecimal(getSale()));
    }

    public String getViewSale() {
        return viewSale();
    }
}
