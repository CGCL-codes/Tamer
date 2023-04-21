package com.ohioedge.j2ee.api.org.vendor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.j2eebuilder.util.*;
import com.ohioedge.j2ee.api.address.StateBean;
import com.ohioedge.j2ee.api.org.prod.ProductBean;
import org.j2eebuilder.util.LogManager;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * VendorBean is a java bean used for communication between JSPs and Vendor EJB.
 * 
 * @author Sandeep Dixit
 * @version 1.3.1
 */
@XmlRootElement()
public class VendorBean extends org.j2eebuilder.model.ManagedTransientObjectImpl implements org.j2eebuilder.model.CompanyVO {

    private transient LogManager log = new LogManager(this.getClass());

    private Integer organizationID;

    private Integer vendorID;

    private String externalID;

    private String reference;

    private String vicinity;

    private String vendorName;

    private String dunsNumber;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String county;

    private String suggestedState;

    private Integer stateID;

    private String region;

    private String country;

    private String sales;

    private java.sql.Date controlDate;

    private Integer employeesHere;

    private Integer employeesTotal;

    private String email;

    private String fax;

    private String exchange1;

    private String exchange2;

    private String phone;

    private Integer phoneAreaCode;

    private String lineOfBusiness;

    private String organizationType;

    private String companyTypeID;

    private String parentHQDunsNumber;

    private String parentHQName;

    private Integer primarySIC;

    private Integer secondarySIC;

    private String publicOrPrivate;

    private String stockExchange;

    private String tickerSymbol;

    private String tradeStyle;

    private String url;

    private String zip;

    private String name;

    private String description;

    private String status;

    private String layout;

    private StateBean stateVO;

    private String username;

    private String password;

    private String suggestedLineOfBusiness;

    private String vendorPassword;

    private ProductBean productVO;

    public String getName() {
        return this.getDefaultName();
    }

    public void setName(String name) {
    }

    public String getDescription() {
        return this.getDefaultDescription();
    }

    public String getDefaultName() {
        if (!UtilityBean.getCurrentInstance().isNullOrEmpty(this.vendorName)) {
            return UtilityBean.getCurrentInstance().truncate(this.vendorName, 10);
        }
        return this.getDefaultDescription();
    }

    public String getDefaultDescription() {
        StringBuilder str = new StringBuilder();
        if (!UtilityBean.getCurrentInstance().isNullOrEmpty(this.vendorName)) {
            str.append(this.vendorName);
            str.append(", ");
        }
        if (!UtilityBean.getCurrentInstance().isNullOrEmpty(this.phone)) {
            str.append(" (");
            str.append(this.phone);
            str.append(") ");
        }
        if (!UtilityBean.getCurrentInstance().isNullOrEmpty(this.city)) {
            str.append(this.city);
            str.append(", ");
        }
        StateBean stateVO = this.getStateVO();
        if (stateVO != null) {
            str.append(this.getStateVO().getName());
        }
        if (!UtilityBean.getCurrentInstance().isNullOrEmpty(this.zip)) {
            str.append(" ");
            str.append(this.zip);
        }
        return UtilityBean.getCurrentInstance().truncate(str.toString(), 100);
    }

    public Integer getOrganizationID() {
        return this.organizationID;
    }

    public void setOrganizationID(Integer orgID) {
        this.organizationID = orgID;
    }

    public Integer getVendorID() {
        return this.vendorID;
    }

    public void setVendorID(Integer vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getCompanyID() {
        return this.getVendorID();
    }

    public String getCompanyName() {
        return this.getVendorName();
    }

    public String getOrganizationName() {
        return "NOT_YET_IMPLEMENTED";
    }

    public String getDunsNumber() {
        return this.dunsNumber;
    }

    public void setDunsNumber(String dunsNumber) {
        this.dunsNumber = dunsNumber;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public Integer getStateID() {
        return this.stateID;
    }

    public void setStateID(Integer stateID) {
        this.stateID = stateID;
    }

    public StateBean getStateVO() {
        return this.stateVO;
    }

    public void setStateVO(StateBean stateVO) {
        this.stateVO = stateVO;
    }

    public String getSuggestedState() {
        return suggestedState;
    }

    public void setSuggestedState(String suggestedState) {
        this.suggestedState = suggestedState;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSales() {
        return this.sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    @XmlJavaTypeAdapter(SqlDateAdapter.class)
    public java.sql.Date getControlDate() {
        return this.controlDate;
    }

    public void setControlDate(java.sql.Date controlDate) {
        this.controlDate = controlDate;
    }

    public Integer getEmployeesHere() {
        return this.employeesHere;
    }

    public void setEmployeesHere(Integer employeesHere) {
        this.employeesHere = employeesHere;
    }

    public Integer getEmployeesTotal() {
        return this.employeesTotal;
    }

    public void setEmployeesTotal(Integer employeesTotal) {
        this.employeesTotal = employeesTotal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getExchange1() {
        return this.exchange1;
    }

    public void setExchange1(String exchange1) {
        this.exchange1 = exchange1;
    }

    public String getExchange2() {
        return this.exchange2;
    }

    public void setExchange2(String exchange2) {
        this.exchange2 = exchange2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
	 * fullPhone impl is kept for backword compatibility with CompanyVO interface
	 * update CompanyVO to use phone instead - requires refactoring of org, customer
	 * @return
	 */
    public String getFullPhone() {
        return this.getPhone();
    }

    public void setFullPhone(String phoneNumber) {
        this.setPhone(phoneNumber);
    }

    public Integer getPhoneAreaCode() {
        return this.phoneAreaCode;
    }

    public void setPhoneAreaCode(Integer phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
    }

    public String getLineOfBusiness() {
        return this.lineOfBusiness;
    }

    public void setLineOfBusiness(String lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    public String getOrganizationType() {
        return this.organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getParentHQDunsNumber() {
        return this.parentHQDunsNumber;
    }

    /**
	 * @return the companyTypeID
	 */
    public String getCompanyTypeID() {
        return companyTypeID;
    }

    /**
	 * @param companyTypeID the companyTypeID to set
	 */
    public void setCompanyTypeID(String companyTypeID) {
        this.companyTypeID = companyTypeID;
    }

    public void setParentHQDunsNumber(String parentHQDunsNumber) {
        this.parentHQDunsNumber = parentHQDunsNumber;
    }

    public String getParentHQName() {
        return this.parentHQName;
    }

    public void setParentHQName(String parentHQName) {
        this.parentHQName = parentHQName;
    }

    public Integer getPrimarySIC() {
        return this.primarySIC;
    }

    public void setPrimarySIC(Integer primarySIC) {
        this.primarySIC = primarySIC;
    }

    public Integer getSecondarySIC() {
        return this.secondarySIC;
    }

    public void setSecondarySIC(Integer secondarySIC) {
        this.secondarySIC = secondarySIC;
    }

    public String getPublicOrPrivate() {
        return this.publicOrPrivate;
    }

    public void setPublicOrPrivate(String publicOrPrivate) {
        this.publicOrPrivate = publicOrPrivate;
    }

    public String getStockExchange() {
        return this.stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public String getTickerSymbol() {
        return this.tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTradeStyle() {
        return this.tradeStyle;
    }

    public void setTradeStyle(String tradeStyle) {
        this.tradeStyle = tradeStyle;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getSuggestedLineOfBusiness() {
        return suggestedLineOfBusiness;
    }

    public void setSuggestedLineOfBusiness(String suggestedLineOfBusiness) {
        this.suggestedLineOfBusiness = suggestedLineOfBusiness;
    }

    public String getVendorPassword() {
        return vendorPassword;
    }

    public void setVendorPassword(String vendorPassword) {
        this.vendorPassword = vendorPassword;
    }

    public ProductBean getProductVO() {
        return productVO;
    }

    public void setProductVO(ProductBean productVO) {
        this.productVO = productVO;
    }

    public VendorBean() {
    }
}
