package org.cmsuite2.model.vector;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.cmsuite2.model.AbstractEntity;
import org.cmsuite2.model.customer.CustomerOrder;
import org.cmsuite2.model.customer.CustomerPackList;
import org.cmsuite2.model.quote.Quote;
import org.cmsuite2.model.supplier.SupplierOrder;
import org.cmsuite2.model.supplier.SupplierPackList;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "cmsuite_vector")
public class Vector extends AbstractEntity<Vector> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "i_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "s_name", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "s_mail", length = 255, nullable = true)
    private String mail;

    @Column(name = "s_phone", length = 16, nullable = true)
    private String phone;

    @Column(name = "f_baseCost", nullable = true)
    private double baseCost;

    @Basic
    @Type(type = "true_false")
    @Column(name = "s_active", nullable = true)
    private boolean active;

    @OneToMany(mappedBy = "vector")
    private List<Quote> quoteList = new ArrayList<Quote>();

    @OneToMany(mappedBy = "vector")
    private List<CustomerOrder> customerOrder = new ArrayList<CustomerOrder>();

    @OneToMany(mappedBy = "vector")
    private List<CustomerPackList> customerPackList = new ArrayList<CustomerPackList>();

    @OneToMany(mappedBy = "vector")
    private List<SupplierPackList> supplierPackList = new ArrayList<SupplierPackList>();

    @OneToMany(mappedBy = "vector")
    private List<SupplierOrder> supplierOrder = new ArrayList<SupplierOrder>();

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<Quote> quoteList) {
        this.quoteList = quoteList;
    }

    public List<CustomerOrder> getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(List<CustomerOrder> customerOrder) {
        this.customerOrder = customerOrder;
    }

    public List<CustomerPackList> getCustomerPackList() {
        return customerPackList;
    }

    public void setCustomerPackList(List<CustomerPackList> customerPackList) {
        this.customerPackList = customerPackList;
    }

    public List<SupplierPackList> getSupplierPackList() {
        return supplierPackList;
    }

    public void setSupplierPackList(List<SupplierPackList> supplierPackList) {
        this.supplierPackList = supplierPackList;
    }

    public List<SupplierOrder> getSupplierOrder() {
        return supplierOrder;
    }

    public void setSupplierOrder(List<SupplierOrder> supplierOrder) {
        this.supplierOrder = supplierOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        Vector other = (Vector) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Vector [id=" + id + ", name=" + name + ", mail=" + mail + ", phone=" + phone + ", baseCost=" + baseCost + ", active=" + active + "]";
    }
}
