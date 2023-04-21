package com.cosmos.acacia.crm.data.warehouse;

import com.cosmos.acacia.crm.data.*;
import com.cosmos.acacia.crm.data.product.Product;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.cosmos.acacia.annotation.Property;
import org.hibernate.annotations.Type;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "delivery_certificate_items")
@NamedQueries({ @NamedQuery(name = "DeliveryCertificateItem.findForCertificate", query = "select dci from DeliveryCertificateItem dci where dci.parentId=:parentId and dci.dataObject.deleted = false"), @NamedQuery(name = "DeliveryCertificateItem.findById", query = "select dci from DeliveryCertificateItem dci where dci.certificateItemId=:itemId and dci.dataObject.deleted = false") })
public class DeliveryCertificateItem extends DataObjectBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "certificate_item_id", nullable = false)
    @Type(type = "uuid")
    private UUID certificateItemId;

    @Column(name = "parent_id")
    @Type(type = "uuid")
    private UUID parentId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    @Property(title = "Product", customDisplay = "${product.productName}")
    private Product product;

    @JoinColumn(name = "measure_unit_id", referencedColumnName = "resource_id")
    @ManyToOne
    @Property(title = "Measure Unit")
    private DbResource measureUnit;

    @Column(name = "quantity", nullable = false)
    @Property(title = "Quantity")
    private BigDecimal quantity;

    @Column(name = "reference_item_id")
    @Type(type = "uuid")
    @Property(title = "Reference Item", visible = false)
    private UUID referenceItemId;

    @JoinColumn(name = "certificate_item_id", referencedColumnName = "data_object_id", insertable = false, updatable = false)
    @OneToOne
    private DataObject dataObject;

    public DeliveryCertificateItem() {
    }

    public DeliveryCertificateItem(UUID certificateItemId) {
        this.certificateItemId = certificateItemId;
    }

    public UUID getCertificateItemId() {
        return certificateItemId;
    }

    public void setCertificateItemId(UUID certificateItemId) {
        this.certificateItemId = certificateItemId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public DbResource getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(DbResource measureUnit) {
        this.measureUnit = measureUnit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (certificateItemId != null ? certificateItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DeliveryCertificateItem)) {
            return false;
        }
        DeliveryCertificateItem other = (DeliveryCertificateItem) object;
        if ((this.certificateItemId == null && other.certificateItemId != null) || (this.certificateItemId != null && !this.certificateItemId.equals(other.certificateItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cosmos.acacia.crm.data.DeliveryCertificateItem[certificateItemId=" + certificateItemId + "]";
    }

    @Override
    public UUID getId() {
        return getCertificateItemId();
    }

    @Override
    public String getInfo() {
        return this.toString();
    }

    @Override
    public void setId(UUID id) {
        setCertificateItemId(id);
    }

    public UUID getReferenceItemId() {
        return referenceItemId;
    }

    public void setReferenceItemId(UUID referenceItemId) {
        this.referenceItemId = referenceItemId;
    }
}
