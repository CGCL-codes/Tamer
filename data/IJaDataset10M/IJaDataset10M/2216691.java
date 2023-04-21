package com.fisoft.phucsinh.phucsinhsrv.service.fixasset.massaddition;

import com.fisoft.phucsinh.phucsinhsrv.entity.AcJournalDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.AcJournalEntry;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApPurchaseInvoiceDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.IvIssueNoteDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.JournalSource;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityLockedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityRemovedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUniqueConstraintViolationException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUnrecoverableException;
import com.fisoft.phucsinh.phucsinhsrv.service.common.CRUDManager;
import com.fisoft.phucsinh.phucsinhsrv.service.common.ICRUDManager;
import javax.ejb.EJB;

/**
 *
 * @author truong ha
 */
public class TransferableFactory {

    public static Transferable getInstance(AcJournalDetail obj, ICRUDManager crudManager) throws ERPUnrecoverableException, ERPEntityLockedException, ERPUniqueConstraintViolationException, ERPEntityRemovedException {
        AcJournalDetail detail = obj;
        AcJournalEntry entry = detail.getJnlEntry();
        Transferable transferable = null;
        if (entry.getJournalSource() == JournalSource.AP_INVOICE) {
            ApPurchaseInvoiceDetail ap = crudManager.find(ApPurchaseInvoiceDetail.class, detail.getDetailID(), false);
            transferable = new TransferablePurchaseInvoiceDetail(ap, detail.getAccount().getAccountNo(), entry);
        } else if (entry.getJournalSource() == JournalSource.INV_ISSUE_NOTE) {
            IvIssueNoteDetail iv = crudManager.find(IvIssueNoteDetail.class, detail.getDetailID(), false);
            transferable = new TransferableIssueNoteDetail(iv, detail.getAccount().getAccountNo(), entry);
        }
        return transferable;
    }
}
