package no.ugland.utransprod.model;

import java.util.Date;

public class Production extends PackageProduction {

    protected Date produced;

    private Date productionDate;

    private String productionUnitName;

    public Production() {
        super();
    }

    public Production(Integer orderLineId, Integer customerNr, String someCustomerDetails, String orderNr, String address, String someInfo, String constructionTypeName, String articleName, String someAttributeInfo, Integer numberOfItems, Date loadingDate, String someTransportDetails, String comment, Integer transportYear, Integer transportWeek, String loadTime, Integer postShipmentId, String productAreaGroupName, Date dateActionStarted, final Date aProducedDate, final Date aProductionDate, final String aProductionUnitName, Colli aColli) {
        super(orderLineId, customerNr, someCustomerDetails, orderNr, address, someInfo, constructionTypeName, articleName, someAttributeInfo, numberOfItems, loadingDate, someTransportDetails, comment, transportYear, transportWeek, loadTime, postShipmentId, productAreaGroupName, dateActionStarted, aColli);
        this.produced = aProducedDate;
        this.productionDate = aProductionDate;
        this.productionUnitName = aProductionUnitName;
    }

    /**
     * @see no.ugland.utransprod.model.Produceable#getProduced()
     */
    public Date getProduced() {
        return produced;
    }

    /**
     * @param produced
     */
    public void setProduced(Date produced) {
        this.produced = produced;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getProductionUnitName() {
        return productionUnitName;
    }

    public void setProductionUnitName(String productionUnitName) {
        this.productionUnitName = productionUnitName;
    }
}
