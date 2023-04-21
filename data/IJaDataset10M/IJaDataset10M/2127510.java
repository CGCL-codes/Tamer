package org.cmsuite2.model.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.cmsuite2.enumeration.DocumentType;
import org.cmsuite2.enumeration.PackListType;
import org.cmsuite2.model.AbstractEntity;
import org.cmsuite2.model.employee.Employee;
import org.cmsuite2.model.packagelist.PackListRate;
import org.cmsuite2.model.packagelist.PackListStatus;
import org.cmsuite2.model.payment.Payment;
import org.cmsuite2.model.product.ProductItem;
import org.cmsuite2.model.store.Store;
import org.cmsuite2.model.vector.Vector;
import org.cmsuite2.util.exporter.IPackList;
import org.cmsuite2.util.exporter.IPrint;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cmsuite_customer_pack_list")
public class CustomerPackList extends AbstractEntity<CustomerPackList> implements IPrint, IPackList {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "i_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "i_number", length = 255, nullable = false)
    private long number;

    @Column(name = "i_packages", nullable = false)
    private int packages;

    @Column(name = "s_packagesDescription", length = 1000, nullable = true)
    private String packagesDescription;

    @Column(name = "s_description", length = 1000, nullable = true)
    private String description;

    @DateTimeFormat
    @Column(name = "d_insDate", nullable = false)
    private Date insDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "s_packListType", nullable = false)
    private PackListType packListType;

    @OneToOne(mappedBy = "customerPackList", cascade = CascadeType.ALL)
    private PackListStatus packListStatus;

    @OneToOne(mappedBy = "customerPackList", cascade = CascadeType.ALL)
    private PackListRate packListRate;

    @OneToOne
    private CustomerPackList originalPackList;

    @OneToOne
    private CustomerBill originalBill;

    @ManyToOne
    private CustomerCreditNote customerCreditNote;

    @ManyToOne
    private CustomerDeferredBill customerDeferredBill;

    @OneToMany(mappedBy = "customerPackList", cascade = CascadeType.ALL)
    private List<ProductItem> productItems = new ArrayList<ProductItem>();

    @ManyToOne
    private Store store;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Vector vector;

    @Override
    public long getId() {
        return id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getPackages() {
        return packages;
    }

    public void setPackages(int packages) {
        this.packages = packages;
    }

    public String getPackagesDescription() {
        return packagesDescription;
    }

    public void setPackagesDescription(String packagesDescription) {
        this.packagesDescription = packagesDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getInsDate() {
        return insDate;
    }

    public void setInsDate(Date insDate) {
        this.insDate = insDate;
    }

    public PackListType getPackListType() {
        return packListType;
    }

    public void setPackListType(PackListType packListType) {
        this.packListType = packListType;
    }

    public CustomerCreditNote getCustomerCreditNote() {
        return customerCreditNote;
    }

    public void setCustomerCreditNote(CustomerCreditNote customerCreditNote) {
        this.customerCreditNote = customerCreditNote;
    }

    public CustomerDeferredBill getCustomerDeferredBill() {
        return customerDeferredBill;
    }

    public void setCustomerDeferredBill(CustomerDeferredBill customerDeferredBill) {
        this.customerDeferredBill = customerDeferredBill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PackListStatus getPackListStatus() {
        return packListStatus;
    }

    public void setPackListStatus(PackListStatus packListStatus) {
        this.packListStatus = packListStatus;
    }

    public PackListRate getPackListRate() {
        return packListRate;
    }

    public void setPackListRate(PackListRate packListRate) {
        this.packListRate = packListRate;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CustomerPackList getOriginalPackList() {
        return originalPackList;
    }

    public void setOriginalPackList(CustomerPackList originalPackList) {
        this.originalPackList = originalPackList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CustomerPackList other = (CustomerPackList) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CustomerPackList [id=" + id + ", number=" + number + ", packages=" + packages + ", packagesDescription=" + packagesDescription + ", description=" + description + ", insDate=" + insDate + ", packListType=" + packListType + ", packListStatus=" + packListStatus + ", packListRate=" + packListRate + "]";
    }

    @Override
    public DocumentType getDocumentType() {
        return DocumentType.packageList;
    }

    @Override
    public double getShippingCosts() {
        return getPackListRate().getShippingCosts();
    }

    @Override
    public double getCustomsCosts() {
        return getPackListRate().getCustomsCosts();
    }

    @Override
    public double getPackCosts() {
        return getPackListRate().getPackCosts();
    }

    @Override
    public double getAdminCosts() {
        return getPackListRate().getAdminCosts();
    }

    @Override
    public double getOtherCosts() {
        return getPackListRate().getOtherCosts();
    }

    @Override
    public double getDuties() {
        return getPackListRate().getDuties();
    }

    @Override
    public double getDiscountedPrice() {
        return getPackListRate().getDiscountedPrice();
    }

    @Override
    public double getTotalPrice() {
        return getPackListRate().getTotalPrice();
    }

    @Override
    public double getTaxPrice() {
        return getPackListRate().getTaxPrice();
    }

    @Override
    public double getFinalPrice() {
        return getPackListRate().getFinalPrice();
    }

    @Override
    public Payment getPayment() {
        return null;
    }

    public CustomerBill getOriginalBill() {
        return originalBill;
    }

    public void setOriginalBill(CustomerBill originalBill) {
        this.originalBill = originalBill;
    }
}
