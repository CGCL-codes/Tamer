package no.ugland.utransprod.model;

import java.util.Date;
import java.util.List;
import no.ugland.utransprod.gui.model.Applyable;
import no.ugland.utransprod.gui.model.TextRenderable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Domeneklasse for view PACKLIST_V
 * 
 * @author atle.brekka
 * 
 */
public class PacklistV extends BaseObject implements Comparable<PacklistV>, TextRenderable, Applyable {

    private static final long serialVersionUID = 1L;

    private Integer orderId;

    private Integer customerNr;

    private String customerDetails;

    private String orderNr;

    private String address;

    private String info;

    private String constructionTypeName;

    private Date loadingDate;

    private Date packlistReady;

    private String comment;

    private Integer numberOfTakstol;

    private Integer hasGulvspon;

    private Integer numberOfGulvspon;

    private String productAreaGroupName;

    private String transportDetails;

    private Date trossReady;

    private String trossDrawer;

    private Integer transportYear;

    private Integer transportWeek;

    private String orderStatus;

    private Integer productionBasis;

    public PacklistV() {
        super();
    }

    public PacklistV(Integer orderId, Integer customerNr, String customerDetails, String orderNr, String address, String info, String constructionTypeName, Date loadingDate, Date packlistReady, String comment, Integer numberOfTakstol, Integer hasGulvspon, Integer numberOfGulvspon, String productAreaGroupName, final String someTransportDetails) {
        super();
        this.orderId = orderId;
        this.customerNr = customerNr;
        this.customerDetails = customerDetails;
        this.orderNr = orderNr;
        this.address = address;
        this.info = info;
        this.constructionTypeName = constructionTypeName;
        this.loadingDate = loadingDate;
        this.packlistReady = packlistReady;
        this.comment = comment;
        this.numberOfTakstol = numberOfTakstol;
        this.hasGulvspon = hasGulvspon;
        this.numberOfGulvspon = numberOfGulvspon;
        this.productAreaGroupName = productAreaGroupName;
        this.transportDetails = someTransportDetails;
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
	 * @return konstruksjonstype
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
	 * @return opplastningsdato
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
	 * @return ordreid
	 */
    public Integer getOrderId() {
        return orderId;
    }

    /**
	 * @param orderId
	 */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
	 * @return dato for pakkliste klar
	 */
    public Date getPacklistReady() {
        return packlistReady;
    }

    /**
	 * @param packlistReady
	 */
    public void setPacklistReady(Date packlistReady) {
        this.packlistReady = packlistReady;
    }

    /**
	 * @see no.ugland.utransprod.model.BaseObject#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PacklistV)) return false;
        PacklistV castOther = (PacklistV) other;
        return new EqualsBuilder().append(orderId, castOther.orderId).isEquals();
    }

    /**
	 * @see no.ugland.utransprod.model.BaseObject#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(orderId).toHashCode();
    }

    /**
	 * @see no.ugland.utransprod.model.BaseObject#toString()
	 */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("orderId", orderId).append("customerNr", customerNr).append("customerDetails", customerDetails).append("orderNr", orderNr).append("address", address).append("info", info).append("constructionTypeName", constructionTypeName).append("loadingDate", loadingDate).append("packlistReady", packlistReady).append("comment", comment).toString();
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
	 * @return 1 dersom gulvspon
	 */
    public Integer getHasGulvspon() {
        return hasGulvspon;
    }

    /**
	 * @param hasGulvspon
	 */
    public void setHasGulvspon(Integer hasGulvspon) {
        this.hasGulvspon = hasGulvspon;
    }

    /**
	 * @return antall gulvspon
	 */
    public Integer getNumberOfGulvspon() {
        return numberOfGulvspon;
    }

    /**
	 * @param numberOfGulvspon
	 */
    public void setNumberOfGulvspon(Integer numberOfGulvspon) {
        this.numberOfGulvspon = numberOfGulvspon;
    }

    /**
	 * @return antall takstoler
	 */
    public Integer getNumberOfTakstol() {
        return numberOfTakstol;
    }

    /**
	 * @param numberOfTakstol
	 */
    public void setNumberOfTakstol(Integer numberOfTakstol) {
        this.numberOfTakstol = numberOfTakstol;
    }

    /**
	 * @see no.ugland.utransprod.gui.model.Applyable#isForPostShipment()
	 */
    public Boolean isForPostShipment() {
        return Boolean.FALSE;
    }

    /**
	 * @return produktomr�degruppenavn
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

    public Integer getOrderLineId() {
        return null;
    }

    public Ordln getOrdln() {
        return null;
    }

    public void setOrdln(Ordln ordln) {
    }

    public String getTransportDetails() {
        return transportDetails;
    }

    public void setTransportDetails(String transportDetails) {
        this.transportDetails = transportDetails;
    }

    public String getArticleName() {
        return null;
    }

    public Colli getColli() {
        return null;
    }

    public Date getProduced() {
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

    public void setProduced(Date produced) {
    }

    public void setRelatedArticles(List<Applyable> relatedArticles) {
    }

    public Integer getNumberOfItems() {
        return null;
    }

    public String getProductionUnitName() {
        return null;
    }

    public Date getTrossReady() {
        return trossReady;
    }

    public void setTrossReady(Date trossReady) {
        this.trossReady = trossReady;
    }

    public String getTrossDrawer() {
        return trossDrawer;
    }

    public void setTrossDrawer(String trossDrawer) {
        this.trossDrawer = trossDrawer;
    }

    public Integer getTransportYear() {
        return transportYear;
    }

    public void setTransportYear(Integer transportYear) {
        this.transportYear = transportYear;
    }

    public Integer getTransportWeek() {
        return transportWeek;
    }

    public void setTransportWeek(Integer transportWeek) {
        this.transportWeek = transportWeek;
    }

    public int compareTo(final PacklistV other) {
        Integer thisYear = transportYear != null ? transportYear : 9999;
        Integer otherYear = other.transportYear != null ? other.transportYear : 9999;
        Integer thisWeek = transportWeek != null ? transportWeek : 9999;
        Integer otherWeek = other.transportWeek != null ? other.transportWeek : 9999;
        return new CompareToBuilder().append(thisYear, otherYear).append(thisWeek, otherWeek).append(loadingDate, other.loadingDate).toComparison();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getProductionBasis() {
        return productionBasis;
    }

    public void setProductionBasis(Integer productionBasis) {
        this.productionBasis = productionBasis;
    }
}
