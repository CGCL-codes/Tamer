package com.fisoft.phucsinh.phucsinhsrv.service.receivable;

import com.fisoft.phucsinh.phucsinhsrv.entity.SiSaleInvoice;
import com.fisoft.phucsinh.phucsinhsrv.entity.SiSaleInvoiceDetail;
import com.fisoft.phucsinh.phucsinhsrv.service.common.RelationController;
import java.util.Collection;

/**
 *
 * @author vantinh
 */
public class SiSaleInvoice_SiSaleInvoiceDetail_RelationController extends RelationController<SiSaleInvoice, SiSaleInvoiceDetail> {

    public SiSaleInvoice_SiSaleInvoiceDetail_RelationController(SiSaleInvoice parentEntity, Collection<SiSaleInvoiceDetail> childrenToAdd, Collection<SiSaleInvoiceDetail> childrenToRemove) {
        super(parentEntity, childrenToAdd, childrenToRemove);
    }

    @Override
    protected Collection<SiSaleInvoiceDetail> getChildrenCollection() {
        return this.parentEntity.getSiSaleInvoiceDetailCollection();
    }

    @Override
    protected void setJoinProperty(SiSaleInvoiceDetail pChild) {
        pChild.setSaleInvoice(this.parentEntity);
    }
}
