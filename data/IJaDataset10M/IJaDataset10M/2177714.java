package org.cmsuite2.model.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.cmsuite2.enumeration.DocumentType;
import org.cmsuite2.model.AbstractEntity;
import org.cmsuite2.model.employee.Employee;
import org.cmsuite2.model.payment.Payment;
import org.cmsuite2.model.product.ProductItem;
import org.cmsuite2.model.store.Store;
import org.cmsuite2.model.supplier.Supplier;
import org.cmsuite2.model.vector.Vector;
import org.cmsuite2.util.document.IDocument;
import org.cmsuite2.util.exporter.IDeferredBill;
import org.cmsuite2.util.exporter.IPrint;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cmsuite_customer_deferred_bill")
public class CustomerDeferredBill extends AbstractEntity<CustomerDeferredBill> implements IPrint, IDocument, IDeferredBill {

    private static final long serialVersionUID = 1L;

    @Transient
    private Date expireDate;

    @Transient
    private boolean expiring;

    @Transient
    private boolean expired;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "i_number", length = 255, nullable = false)
    private long number;

    @Column(name = "s_description", length = 1000, nullable = true)
    private String description;

    @DateTimeFormat
    @Column(name = "d_insDate", nullable = false)
    private Date insDate;

    @Basic
    @Type(type = "true_false")
    @Column(name = "s_paid", nullable = true)
    private boolean paid;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Payment payment;

    @OneToMany(mappedBy = "customerDeferredBill")
    private List<CustomerPackList> customerPackLists = new ArrayList<CustomerPackList>();

    @ManyToOne
    private Store store;

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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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

    public List<CustomerPackList> getCustomerPackLists() {
        return customerPackLists;
    }

    public void setCustomerPackLists(List<CustomerPackList> customerPackLists) {
        this.customerPackLists = customerPackLists;
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
        CustomerDeferredBill other = (CustomerDeferredBill) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CustomerDeferredBill [id=" + id + ", number=" + number + ", description=" + description + ", insDate=" + insDate + ", paid=" + paid + "]";
    }

    @Override
    public DocumentType getDocumentType() {
        return DocumentType.deferredBill;
    }

    @Override
    public Vector getVector() {
        return null;
    }

    @Override
    public List<ProductItem> getProductItems() {
        List<ProductItem> productItems = new ArrayList<ProductItem>();
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) for (ProductItem productItem : packList.getProductItems()) productItems.add(productItem);
        return productItems;
    }

    @Override
    public double getShippingCosts() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getShippingCosts();
        return total;
    }

    @Override
    public double getCustomsCosts() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getCustomsCosts();
        return total;
    }

    @Override
    public double getPackCosts() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getPackCosts();
        return total;
    }

    @Override
    public double getAdminCosts() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getAdminCosts();
        return total;
    }

    @Override
    public double getOtherCosts() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getOtherCosts();
        return total;
    }

    @Override
    public double getDuties() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getDuties();
        return total;
    }

    @Override
    public double getDiscountedPrice() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getDiscountedPrice();
        return total;
    }

    @Override
    public double getTotalPrice() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getTotalPrice();
        return total;
    }

    @Override
    public double getTaxPrice() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getTaxPrice();
        return total;
    }

    @Override
    public double getFinalPrice() {
        double total = 0;
        List<CustomerPackList> packLists = getCustomerPackLists();
        for (CustomerPackList packList : packLists) total += packList.getFinalPrice();
        return total;
    }

    @Override
    public Supplier getSupplier() {
        return null;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isExpiring() {
        return expiring;
    }

    public void setExpiring(boolean expiring) {
        this.expiring = expiring;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
