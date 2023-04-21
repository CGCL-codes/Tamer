package com.docum.dao.impl;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.docum.dao.InvoiceDao;
import com.docum.domain.po.common.Invoice;

@Repository
public class InvoiceDaoImpl extends BaseDaoImpl implements InvoiceDao {

    private static final long serialVersionUID = 8418761484297199308L;

    @Override
    public List<Invoice> getInvoicesByVoyage(Long voyageId) {
        Query query = entityManager.createNamedQuery(GET_INVOICES_BY_VOYAGE_QUERY);
        query.setParameter("voyageId", voyageId);
        @SuppressWarnings("unchecked") List<Invoice> result = query.getResultList();
        return result;
    }

    @Override
    public List<Invoice> getInvoicesByPurchaseOrder(Long orderId) {
        Query query = entityManager.createNamedQuery(GET_INVOICES_BY_PURCHASE_ORDER_QUERY);
        query.setParameter("orderId", orderId);
        @SuppressWarnings("unchecked") List<Invoice> result = query.getResultList();
        return result;
    }

    @Override
    public List<Invoice> getInvoicesByBillOfLading(Long billOfLadingId) {
        Query query = entityManager.createNamedQuery(GET_INVOICES_BY_BILL_OF_LADING_QUERY);
        query.setParameter("billOfLadingId", billOfLadingId);
        @SuppressWarnings("unchecked") List<Invoice> result = query.getResultList();
        return result;
    }
}
