package furniture.buy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import furniture.core.model.IdEntity;
import furniture.core.model.annotation.FieldView;

/**
 * TSupplier entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_supplier_")
@FieldView(subpackage = "foundation", label = "供应商")
public class SupplierEntity extends IdEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @FieldView(label = "供应商名称", sortable = true, width = 200)
    @Column(name = "supplier_name", nullable = false, length = 512)
    private String supplierName;

    @FieldView(label = "供应商地址", width = 250)
    @Column(name = "address", nullable = false, length = 1024)
    private String address;

    @FieldView(label = "手机号码", width = 100)
    @Column(name = "mobile", nullable = false, length = 11)
    private String mobile;

    @FieldView(label = "电话号码", width = 100)
    @Column(name = "phone", length = 30)
    private String phone;

    @FieldView(label = "传真号码", width = 100)
    @Column(name = "fax", length = 30)
    private String fax;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
