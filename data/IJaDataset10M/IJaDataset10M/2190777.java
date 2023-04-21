package com.entelience.objects.asset;

import java.util.Date;
import java.io.Serializable;

/**
 * BEAN - Version 
 */
public class Version implements Serializable {

    private VersionId version_id;

    private ProductId product_id;

    private VendorId vendor_id;

    private String vendor_name;

    private String product_name;

    private String version;

    private boolean active;

    private Boolean obsoleted;

    private Boolean supported;

    private Date obsolescence_date;

    private CompositeVersionId compVersId;

    private Integer companyId;

    private String companyName;

    private Boolean currentUserCompany;

    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append(" [ProductId=").append(product_id).append("],");
        s.append(" [VendorId=").append(vendor_id).append("],");
        s.append(" [VersionId=").append(version_id).append("],");
        s.append(" [productName=").append(product_name).append("],");
        s.append(" [vendorName=").append(vendor_name).append("],");
        s.append(" [version=").append(version).append("],");
        s.append(" [active=").append(active).append("],");
        s.append(" [obsoleted=").append(obsoleted).append("],");
        s.append(" [supported=").append(supported).append("],");
        s.append(" [obsolescence_date=").append(obsolescence_date).append(']');
        return s.toString();
    }

    public Version() {
    }

    public void setVersion_id(VersionId version_id) {
        this.version_id = version_id;
    }

    public VersionId getVersion_id() {
        return version_id;
    }

    public void setProduct_id(ProductId product_id) {
        this.product_id = product_id;
    }

    public ProductId getProduct_id() {
        return product_id;
    }

    public void setVendor_id(VendorId vendor_id) {
        this.vendor_id = vendor_id;
    }

    public VendorId getVendor_id() {
        return vendor_id;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setObsoleted(Boolean obsoleted) {
        this.obsoleted = obsoleted;
    }

    public Boolean getObsoleted() {
        return obsoleted;
    }

    public void setSupported(Boolean supported) {
        this.supported = supported;
    }

    public Boolean getSupported() {
        return supported;
    }

    public void setObsolescence_date(Date obsolescence_date) {
        this.obsolescence_date = obsolescence_date;
    }

    public Date getObsolescence_date() {
        return obsolescence_date;
    }

    public CompositeVersionId getCompVersId() {
        return compVersId;
    }

    public void setCompVersId(CompositeVersionId compVersId) {
        this.compVersId = compVersId;
    }

    public Boolean getComposite() {
        return compVersId != null;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getCurrentUserCompany() {
        return currentUserCompany;
    }

    public void setCurrentUserCompany(Boolean currentUserCompany) {
        this.currentUserCompany = currentUserCompany;
    }
}
