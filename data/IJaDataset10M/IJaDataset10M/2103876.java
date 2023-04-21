package no.ugland.utransprod.model;

import java.util.Date;
import java.util.List;
import no.ugland.utransprod.gui.model.Applyable;
import no.ugland.utransprod.gui.model.TextRenderable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Modellklasse for view FRONT_PRODUCTION_V
 * @author atle.brekka
 */
public class FrontProductionV extends BaseObject implements TextRenderable, Produceable {

    private static final long serialVersionUID = 1L;

    private Integer orderLineId;

    private String customerDetails;

    private String orderNr;

    private String address;

    private String info;

    private String constructionTypeName;

    private String articleName;

    private String attributeInfo;

    private Integer numberOfItems;

    private Date loadingDate;

    private Date produced;

    private String transportDetails;

    private String comment;

    private Integer transportYear;

    private Integer transportWeek;

    private Integer customerNr;

    private String loadTime;

    private String orderStatus;

    private Integer postShipmentId;

    private String productAreaGroupName;

    private Date actionStarted;

    private Date productionDate;

    private String productionUnitName;

    /**
     * Brukes bare for � sette info fra visma
     */
    private Ordln ordln;

    public FrontProductionV() {
        super();
    }

    public FrontProductionV(final Integer aOrderLineId, final String someCustomerDetails, final String aOrderNr, final String aAddress, final String aInfo, final String aConstructionTypeName, final String aArticleName, final String aAttributeInfo, final Integer aNumberOfItems, final Date aLoadingDate, final Date producedDate, final String someTransportDetails, final String aComment, final Integer aTransportYear, final Integer aTransportWeek, final Integer aCustomerNr, final String aLoadTime, final String aOrderStatus, final Integer aPostShipmentId, final String aProductAreaGroupName, final Date actionStartedDate, final Date aProductionDate, final String aProductionUnitName) {
        super();
        this.orderLineId = aOrderLineId;
        this.customerDetails = someCustomerDetails;
        this.orderNr = aOrderNr;
        this.address = aAddress;
        this.info = aInfo;
        this.constructionTypeName = aConstructionTypeName;
        this.articleName = aArticleName;
        this.attributeInfo = aAttributeInfo;
        this.numberOfItems = aNumberOfItems;
        this.loadingDate = aLoadingDate;
        this.produced = producedDate;
        this.transportDetails = someTransportDetails;
        this.comment = aComment;
        this.transportYear = aTransportYear;
        this.transportWeek = aTransportWeek;
        this.customerNr = aCustomerNr;
        this.loadTime = aLoadTime;
        this.orderStatus = aOrderStatus;
        this.postShipmentId = aPostShipmentId;
        this.productAreaGroupName = aProductAreaGroupName;
        this.actionStarted = actionStartedDate;
        this.productionDate = aProductionDate;
        this.productionUnitName = aProductionUnitName;
    }

    /**
     * @return adresse
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return artikkelnavn
     */
    public String getArticleName() {
        return articleName;
    }

    /**
     * @param articleName
     */
    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    /**
     * @see no.ugland.utransprod.model.Produceable#getAttributeInfo()
     */
    public String getAttributeInfo() {
        return attributeInfo;
    }

    /**
     * @param attributeInfo
     */
    public void setAttributeInfo(String attributeInfo) {
        this.attributeInfo = attributeInfo;
    }

    /**
     * @see no.ugland.utransprod.gui.model.TextRenderable#getComment()
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return garasjetype
     */
    public String getConstructionTypeName() {
        return constructionTypeName;
    }

    /**
     * @param constructionTypeName
     */
    public void setConstructionTypeName(String constructionTypeName) {
        this.constructionTypeName = constructionTypeName;
    }

    /**
     * @return kundedetaljer
     */
    public String getCustomerDetails() {
        return customerDetails;
    }

    /**
     * @param customerDetails
     */
    public void setCustomerDetails(String customerDetails) {
        this.customerDetails = customerDetails;
    }

    /**
     * @return kundenummer
     */
    public Integer getCustomerNr() {
        return customerNr;
    }

    /**
     * @param customerNr
     */
    public void setCustomerNr(Integer customerNr) {
        this.customerNr = customerNr;
    }

    /**
     * @return info om bredde og h�yde
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @see no.ugland.utransprod.model.Produceable#getLoadingDate()
     */
    public Date getLoadingDate() {
        return loadingDate;
    }

    /**
     * @param loadingDate
     */
    public void setLoadingDate(Date loadingDate) {
        this.loadingDate = loadingDate;
    }

    /**
     * @see no.ugland.utransprod.model.Produceable#getNumberOfItems()
     */
    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * @param numberOfItems
     */
    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    /**
     * @see no.ugland.utransprod.model.Produceable#getOrderLineId()
     */
    public Integer getOrderLineId() {
        return orderLineId;
    }

    /**
     * @param orderLineId
     */
    public void setOrderLineId(Integer orderLineId) {
        this.orderLineId = orderLineId;
    }

    /**
     * @see no.ugland.utransprod.gui.model.Applyable#getOrderNr()
     */
    public String getOrderNr() {
        return orderNr;
    }

    /**
     * @param orderNr
     */
    public void setOrderNr(String orderNr) {
        this.orderNr = orderNr;
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

    /**
     * @see no.ugland.utransprod.model.Produceable#getTransportDetails()
     */
    public String getTransportDetails() {
        return transportDetails;
    }

    /**
     * @param transportDetails
     */
    public void setTransportDetails(String transportDetails) {
        this.transportDetails = transportDetails;
    }

    /**
     * @return transportuke
     */
    public Integer getTransportWeek() {
        return transportWeek;
    }

    /**
     * @param transportWeek
     */
    public void setTransportWeek(Integer transportWeek) {
        this.transportWeek = transportWeek;
    }

    /**
     * @return transport�r
     */
    public Integer getTransportYear() {
        return transportYear;
    }

    /**
     * @param transportYear
     */
    public void setTransportYear(Integer transportYear) {
        this.transportYear = transportYear;
    }

    /**
     * @see no.ugland.utransprod.model.BaseObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof FrontProductionV)) return false;
        FrontProductionV castOther = (FrontProductionV) other;
        return new EqualsBuilder().append(orderLineId, castOther.orderLineId).isEquals();
    }

    /**
     * @see no.ugland.utransprod.model.BaseObject#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(orderLineId).toHashCode();
    }

    /**
     * @see no.ugland.utransprod.model.BaseObject#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("orderLineId", orderLineId).toString();
    }

    /**
     * @see no.ugland.utransprod.gui.model.TextRenderable#getOrderString()
     */
    public String getOrderString() {
        StringBuffer buffer = new StringBuffer(customerDetails);
        buffer.append(" - ").append(orderNr).append("\n").append(address).append(" ,").append(constructionTypeName).append(",").append(info);
        return buffer.toString();
    }

    /**
     * @return opplastingstid
     */
    public String getLoadTime() {
        return loadTime;
    }

    /**
     * @param loadTime
     */
    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    /**
     * @return ordrestatus
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return etterleveringid
     */
    public Integer getPostShipmentId() {
        return postShipmentId;
    }

    /**
     * @param postShipmentId
     */
    public void setPostShipmentId(Integer postShipmentId) {
        this.postShipmentId = postShipmentId;
    }

    /**
     * @see no.ugland.utransprod.gui.model.Applyable#isForPostShipment()
     */
    public Boolean isForPostShipment() {
        if (postShipmentId != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @see no.ugland.utransprod.model.Produceable#getProductAreaGroupName()
     */
    public String getProductAreaGroupName() {
        return productAreaGroupName;
    }

    /**
     * @param productAreaGroupName
     */
    public void setProductAreaGroupName(String productAreaGroupName) {
        this.productAreaGroupName = productAreaGroupName;
    }

    public Date getActionStarted() {
        return actionStarted;
    }

    public void setActionStarted(Date actionStarted) {
        this.actionStarted = actionStarted;
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

    public Ordln getOrdln() {
        return ordln;
    }

    public void setOrdln(Ordln ordln) {
        this.ordln = ordln;
    }

    public Colli getColli() {
        return null;
    }

    public List<Applyable> getRelatedArticles() {
        return null;
    }

    public boolean isRelatedArticlesComplete() {
        return false;
    }

    public void setColli(Colli colli) {
    }

    public void setRelatedArticles(List<Applyable> relatedArticles) {
    }

    public boolean isRelatedArticlesStarted() {
        return false;
    }

    public Date getRelatedStartedDate() {
        return null;
    }

    public void setProductionUnit(ProductionUnit productionUnit) {
    }

    public Integer getProbability() {
        return null;
    }

    public String getTrossDrawer() {
        return null;
    }
}
