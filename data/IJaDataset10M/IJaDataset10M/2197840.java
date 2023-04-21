package com.cosmos.acacia.crm.data.purchase;

import com.cosmos.acacia.crm.data.warehouse.DeliveryCertificateItem;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "goods_receipt_dc_items", catalog = "acacia", schema = "public")
@DiscriminatorValue(value = GoodsReceiptDeliveryCertificateItem.DISCRIMINATOR_VALUE)
@PrimaryKeyJoinColumn(name = "receipt_item_id", referencedColumnName = "receipt_item_id")
@NamedQueries({ @NamedQuery(name = "GoodsReceiptDcItem.findAll", query = "SELECT g FROM GoodsReceiptDeliveryCertificateItem g") })
public class GoodsReceiptDeliveryCertificateItem extends GoodsReceiptItem implements Serializable {

    private static final long serialVersionUID = 1L;

    static final String DISCRIMINATOR_VALUE = "DC";

    @JoinColumn(name = "delivery_certificate_item_id", referencedColumnName = "certificate_item_id", nullable = false)
    @ManyToOne(optional = false)
    private DeliveryCertificateItem deliveryCertificateItem;

    public GoodsReceiptDeliveryCertificateItem() {
        super(DISCRIMINATOR_VALUE);
    }

    public GoodsReceiptDeliveryCertificateItem(UUID receiptItemId) {
        super(DISCRIMINATOR_VALUE, receiptItemId);
    }

    public DeliveryCertificateItem getDeliveryCertificateItem() {
        return deliveryCertificateItem;
    }

    public void setDeliveryCertificateItem(DeliveryCertificateItem deliveryCertificateItem) {
        this.deliveryCertificateItem = deliveryCertificateItem;
    }
}
