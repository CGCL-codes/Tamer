package org.cmsuite2.model.supplier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.cmsuite2.model.AbstractEntity;
import org.cmsuite2.model.employee.Employee;
import org.cmsuite2.model.order.OrderRate;
import org.cmsuite2.model.order.OrderStatus;
import org.cmsuite2.model.payment.Payment;
import org.cmsuite2.model.product.ProductItem;
import org.cmsuite2.model.store.Store;
import org.cmsuite2.model.vector.Vector;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cmsuite_supplier_order")
public class SupplierOrder extends AbstractEntity<SupplierOrder> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "i_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "i_number", length = 255, nullable = false)
    private long number;

    @DateTimeFormat
    @Column(name = "d_insDate", nullable = false)
    private Date insDate;

    @OneToOne(mappedBy = "supplierOrder", cascade = CascadeType.ALL)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "supplierOrder", cascade = CascadeType.ALL)
    private OrderRate orderRate;

    @OneToOne(mappedBy = "supplierOrder")
    private SupplierBill supplierBill;

    @OneToMany(mappedBy = "supplierOrder", cascade = CascadeType.ALL)
    private List<ProductItem> productItems = new ArrayList<ProductItem>();

    @ManyToOne
    private Store store;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Vector vector;

    @ManyToOne
    private Payment payment;

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

    public Date getInsDate() {
        return insDate;
    }

    public void setInsDate(Date insDate) {
        this.insDate = insDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderRate getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(OrderRate orderRate) {
        this.orderRate = orderRate;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SupplierBill getSupplierBill() {
        return supplierBill;
    }

    public void setSupplierBill(SupplierBill supplierBill) {
        this.supplierBill = supplierBill;
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
        SupplierOrder other = (SupplierOrder) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "SupplierOrder [id=" + id + ", number=" + number + ", insDate=" + insDate + ", orderStatus=" + orderStatus + ", orderRate=" + orderRate + "]";
    }
}
