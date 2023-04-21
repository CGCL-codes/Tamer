package com.docum.view;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.docum.domain.po.IdentifiedEntity;
import com.docum.domain.po.common.BillOfLading;
import com.docum.domain.po.common.Container;
import com.docum.domain.po.common.Invoice;
import com.docum.domain.po.common.PurchaseOrder;
import com.docum.domain.po.common.Voyage;
import com.docum.service.BillOfLadingService;
import com.docum.service.InvoiceService;
import com.docum.service.PurchaseOrderService;

@Controller("invoiceBean")
@Scope("session")
public class InvoiceView extends AbstractDocumentView {

    private static final long serialVersionUID = 7608376610068348483L;

    private static final String sign = "Инвойс";

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private BillOfLadingService billOfLadingService;

    private Invoice invoice = new Invoice();

    private Integer containersAmount;

    @Override
    public void saveObject() {
        if (this.invoice.getId() == null) {
            this.invoice.setVoyage(getSelectedVoyage());
        }
        getBaseService().save(this.invoice);
        refreshObjects();
    }

    @Override
    public void refreshObjects() {
        Voyage voyage = getSelectedVoyage();
        if (voyage != null) {
            List<Invoice> invoices = invoiceService.getInvoicesByVoyage(voyage.getId());
            super.setObjects(invoices);
            if (invoices != null && invoices.size() > 0) {
                this.invoice = invoices.get(0);
            }
        }
    }

    @Override
    public void newObject() {
        super.newObject();
        this.invoice = new Invoice();
    }

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public String getBriefInfo() {
        return invoice.getNumber();
    }

    @Override
    public IdentifiedEntity getBeanObject() {
        return this.invoice != null ? this.invoice : new Invoice();
    }

    public List<Container> getContainers() {
        List<Container> result = null;
        if (this.invoice == null || this.invoice.getId() == null) {
            return result;
        } else {
            result = containerService.getContainersByInvoice(this.invoice.getId());
            this.containersAmount = result.size();
            return result;
        }
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        if (this.invoice == null || this.invoice.getId() == null) {
            return null;
        } else {
            return purchaseOrderService.getOrdersByInvoice(this.invoice.getId());
        }
    }

    public List<BillOfLading> getBillOfLadings() {
        if (this.invoice == null || this.invoice.getId() == null) {
            return null;
        } else {
            return billOfLadingService.getBillsByInvoice(this.invoice.getId());
        }
    }

    public List<Voyage> getVoyages() {
        if (this.invoice == null || this.invoice.getId() == null) {
            return null;
        } else {
            return voyageService.getVoyagesByInvoice(this.invoice.getId());
        }
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Integer getContainersAmount() {
        return containersAmount;
    }
}
