package com.fisoft.phucsinh.phucsinhsrv.service.payables;

import com.fisoft.phucsinh.phucsinhsrv.entity.AcJournalDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.AcJournalEntry;
import com.fisoft.phucsinh.phucsinhsrv.entity.AcPeriodStatus;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApAdvNetting;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApAdvNettingDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApPIAllocationDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApPaying;
import com.fisoft.phucsinh.phucsinhsrv.entity.EntityStatus;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApPayingDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApPurchaseInvoice;
import com.fisoft.phucsinh.phucsinhsrv.entity.ApPurchaseInvoiceDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.EnumCollateStatus;
import com.fisoft.phucsinh.phucsinhsrv.entity.IvReceiptNote;
import com.fisoft.phucsinh.phucsinhsrv.entity.IvReceiptNoteDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.PoPurchaseOrderDetail;
import com.fisoft.phucsinh.phucsinhsrv.entity.TransactionStatus;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityExistsException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityLockedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityRemovedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPInvalidEntityException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUniqueConstraintViolationException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPUnrecoverableException;
import com.fisoft.phucsinh.phucsinhsrv.service.common.ICRUDManager;
import com.fisoft.phucsinh.phucsinhsrv.service.common.IPagingSearchManager;
import com.fisoft.phucsinh.phucsinhsrv.service.common.QuerySelector;
import com.fisoft.phucsinh.phucsinhsrv.service.glentry.AcJournalEntryQuerySelector;
import com.fisoft.phucsinh.phucsinhsrv.service.glentry.IEntryManager;
import com.fisoft.phucsinh.util.ErrorCode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * contain services to operate purchase invoice object
 * @author vantinh
 */
@Stateless
public class PurchaseInvoiceManager implements IPurchaseInvoiceManager {

    @EJB
    private ICRUDManager crudManager;

    @EJB
    private IEntryManager entryManager;

    @EJB
    private IPagingSearchManager searchManager;

    /**
     * check business constraint before create
     * period status must be open
     *
     * @param pApPurchaseInvoice
     * @param pApPurchaseInvoiceList
     * @return void
     */
    private void checkBusinessForCreate(ApPurchaseInvoice pApPurchaseInvoice, List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailList) throws ERPInvalidEntityException {
        boolean checkOK = true;
        String errorMessage = "";
        String errorCode = "";
        if (pApPurchaseInvoice.getCurrency() == null) {
            errorCode = ErrorCode.PURCHASEINVOICE_CREATE_CURRENCY_NULL;
            errorMessage = "Currency can not be null";
            checkOK = false;
        } else if (pApPurchaseInvoice.getInvoiceType() == null) {
            errorCode = ErrorCode.PURCHASEINVOICE_CREATE_INVOICETYPE_NULL;
            errorMessage = "InvoiceType can not be null";
            checkOK = false;
        } else if (pApPurchaseInvoice.getRoleType() == null || pApPurchaseInvoice.getRoleID() == null) {
            errorCode = ErrorCode.PURCHASEINVOICE_CREATE_ROLETYPE_ROLEID_NULL;
            errorMessage = "RoleType and RoleID can not be null";
            checkOK = false;
        }
        if (!pApPurchaseInvoice.getPeriodID().getPeriodStatus().equals(AcPeriodStatus.OPEN)) {
            errorCode = ErrorCode.PURCHASEINVOICE_CREATE_PERIODNOTOPEN;
            errorMessage = "Period status is not open";
            checkOK = false;
        }
        if ((checkOK) && (pApPurchaseInvoice.getPostDate() != null) && (pApPurchaseInvoice.getTransactionDate().getTime() - pApPurchaseInvoice.getPostDate().getTime() > 0)) {
            errorCode = ErrorCode.PURCHASEINVOICE_CREATE_TRANDATE_GREATERTHAN_POSTDATE;
            errorMessage = "Period status is not open";
            checkOK = false;
        }
        if (checkOK && pApPurchaseInvoiceDetailList.size() == 0) {
            errorCode = ErrorCode.PURCHASEINVOICE_CREATE_MUSTHAVEDETAIL;
            errorMessage = "Sale invoice must have detail";
            checkOK = false;
        }
        if (!checkOK) {
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
    }

    /**
     * check business constraint before create
     * period status must be open
     *
     * @param pApPurchaseInvoice
     * @param pApPurchaseInvoiceList
     * @return
     */
    private void checkBusinessForUpdate(ApPurchaseInvoice pApPurchaseInvoice, List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailListAdd, List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailListRemove) throws ERPInvalidEntityException {
        boolean checkOK = true;
        List<ApPurchaseInvoiceDetail> ApPurchaseInvoiceDetailListUpdate = new ArrayList<ApPurchaseInvoiceDetail>();
        String errorMessage = "";
        String errorCode = "";
        if (pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
            errorCode = ErrorCode.PURCHASEINVOICE_UPDATE_CANNOTUPDATE_AUTHVOUCHER;
            errorMessage = "Can not update authorised purchase invoice";
            checkOK = false;
        }
        if (checkOK && !pApPurchaseInvoice.getPeriodID().getPeriodStatus().equals(AcPeriodStatus.OPEN)) {
            errorCode = ErrorCode.PURCHASEINVOICE_UPDATE_PERIODNOTOPEN;
            errorMessage = "Period status is not open";
            checkOK = false;
        }
        if ((checkOK) && (pApPurchaseInvoice.getPostDate() != null) && (pApPurchaseInvoice.getTransactionDate().getTime() - pApPurchaseInvoice.getPostDate().getTime() > 0)) {
            errorCode = ErrorCode.PURCHASEINVOICE_UPDATE_TRANDATE_GREATERTHAN_POSTDATE;
            errorMessage = "Period status is not open";
            checkOK = false;
        }
        for (ApPurchaseInvoiceDetail ApPurchaseInvoiceDetail : pApPurchaseInvoice.getApPurchaseInvoiceDetailCollection()) {
            ApPurchaseInvoiceDetailListUpdate.add(ApPurchaseInvoiceDetail);
        }
        for (ApPurchaseInvoiceDetail ApPurchaseInvoiceDetail : pApPurchaseInvoiceDetailListRemove) {
            if (ApPurchaseInvoiceDetailListUpdate.contains(ApPurchaseInvoiceDetail)) {
                ApPurchaseInvoiceDetailListUpdate.remove(ApPurchaseInvoiceDetail);
            }
        }
        for (ApPurchaseInvoiceDetail ApPurchaseInvoiceDetail : pApPurchaseInvoiceDetailListAdd) {
            if (!ApPurchaseInvoiceDetailListUpdate.contains(ApPurchaseInvoiceDetail)) {
                ApPurchaseInvoiceDetailListUpdate.add(ApPurchaseInvoiceDetail);
            }
        }
        if (checkOK && ApPurchaseInvoiceDetailListUpdate.size() == 0) {
            errorCode = ErrorCode.PURCHASEINVOICE_UPDATE_MUSTHAVEDETAIL;
            errorMessage = "Sale invoice must have detail";
            checkOK = false;
        }
        if (!checkOK) {
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
    }

    /**
     * initialize remain and paid amount when create new purchase invoice
     * @param pApPurchaseInvoice
     */
    private void updateRemainAmount(ApPurchaseInvoice pApPurchaseInvoice) {
        pApPurchaseInvoice.setRemainedAmount(pApPurchaseInvoice.getAmount());
        pApPurchaseInvoice.setRemainedTaxAmount(pApPurchaseInvoice.getTaxAmount());
        pApPurchaseInvoice.setTotalRemainedAmount(pApPurchaseInvoice.getTotalAmount());
    }

    /**
     * create a purchase invoice with list of purchase invoice detail
     * @paramp ApPurchaseInvoice: purchase invoice entity, do not set
     * purchase invoice detail entity
     * @param pApPurchaseInvoiceDetailList: list of purchase invoice detail,
     * do not need set purchase invoice for each purchase invoice detail
     * @throws ERPInvalidEntityException
     * ErrorCode.PURCHASEINVOICE_CREATE_CURRENCY_NULL: if currency is null
     * ErrorCode.PURCHASEINVOICE_CREATE_INVOICETYPE_NULL: if InvoiceType is null
     * ErrorCode.PURCHASEINVOICE_CREATE_ROLETYPE_ROLEID_NULL: if RoleType or RoleID is null
     * ErrorCode.PURCHASEINVOICE_CREATE_PERIODNOTOPEN: if period is not open
     * ErrorCode.PURCHASEINVOICE_CREATE_TRANDATE_GREATERTHAN_POSTDATE: if transaction date greater than post date
     * ErrorCode.PURCHASEINVOICE_CREATE_MUSTHAVEDETAIL: if purchase invoice do not have purchase invoice detail
     * @throws ERPEntityExistsException
     * @throws ERPUniqueConstraintViolationException
     * @throws ERPUnrecoverableException
     */
    public void createPurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice, List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailList) throws ERPInvalidEntityException, ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException, ERPEntityLockedException, ERPEntityRemovedException {
        List<ApPIAllocationDetail> apPIAllocationDetailList = new ArrayList<ApPIAllocationDetail>();
        List<ApPIAllocationDetail> apPIAllocationDetailListAdd = new ArrayList<ApPIAllocationDetail>();
        ApPurchaseInvoiceDetail apPurchaseInvoiceDetailFind;
        Double amount;
        Double percent;
        checkBusinessForCreate(pApPurchaseInvoice, pApPurchaseInvoiceDetailList);
        crudManager.create(pApPurchaseInvoice);
        for (ApPurchaseInvoiceDetail PurchaseInvoiceDetail : pApPurchaseInvoiceDetailList) {
            apPIAllocationDetailList = new ArrayList<ApPIAllocationDetail>();
            apPIAllocationDetailList = (List) PurchaseInvoiceDetail.getApPIAllocationDetailCollection();
            apPIAllocationDetailListAdd = new ArrayList<ApPIAllocationDetail>();
            for (ApPIAllocationDetail apPI : apPIAllocationDetailList) {
                if (apPI.getPercentage() == null) {
                    percent = PurchaseInvoiceDetail.getAmount() / apPI.getAmount();
                    apPI.setPercentage(percent);
                } else if (apPI.getAmount() == null) {
                    amount = (PurchaseInvoiceDetail.getAmount() * apPI.getPercentage()) / 100;
                    apPI.setAmount(amount);
                }
                apPIAllocationDetailListAdd.add(apPI);
            }
            System.out.println("apPIAllocationDetailList.size():" + apPIAllocationDetailList.size());
            PurchaseInvoiceDetail.getApPIAllocationDetailCollection().clear();
            PurchaseInvoiceDetail.setPurchaseInvoice(pApPurchaseInvoice);
            crudManager.create(PurchaseInvoiceDetail);
            apPurchaseInvoiceDetailFind = crudManager.find(ApPurchaseInvoiceDetail.class, PurchaseInvoiceDetail.getID(), true);
            ApPurchaseInvoiceDetail_ApPIAllocationDetail_RelationController rc = new ApPurchaseInvoiceDetail_ApPIAllocationDetail_RelationController(apPurchaseInvoiceDetailFind, apPIAllocationDetailListAdd, null);
            crudManager.editAddChildrenOTM(rc);
        }
    }

    /**
     * check business for complete create purchase invoice
     * @param  ApPurchaseInvoice pApPurchaseInvoice: purchase invoice which to complete
     */
    private void checkBusinessForComplete(ApPurchaseInvoice pApPurchaseInvoice, List<AcJournalDetail> pAcJournalDetailList) throws ERPInvalidEntityException {
        boolean checkOK = true;
        String errorMessage = "";
        String errorCode = "";
        boolean bNotHaveAccountType = false;
        if (checkOK && !pApPurchaseInvoice.getPeriodID().getPeriodStatus().equals(AcPeriodStatus.OPEN)) {
            errorCode = ErrorCode.PURCHASEINVOICE_COMPLETE_PERIODNOTOPEN;
            errorMessage = "Period status is not open";
            checkOK = false;
        }
        if (checkOK && !pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.DRAFT)) {
            errorCode = ErrorCode.PURCHASEINVOICE_COMPLETE_TRANSTATUS_MUSTBE_DRAFT;
            errorMessage = "Transaction status of Sale Invoice must be DRAFT";
            checkOK = false;
        }
        if (checkOK) {
            for (AcJournalDetail acJournalDetail : pAcJournalDetailList) {
                if (acJournalDetail.getAccountType().equalsIgnoreCase("InInvoice.PHAITRA")) {
                    pApPurchaseInvoice.setPayAccount(acJournalDetail.getAccount());
                    bNotHaveAccountType = true;
                    break;
                }
            }
            if (!bNotHaveAccountType) {
                errorCode = ErrorCode.PURCHASEINVOICE_COMPLETE_NOTEXIST_ACCTTYPE;
                errorMessage = "Do not exist OutInvoice.PHAITHU account type in journal detail";
                checkOK = false;
            }
        }
        if (!checkOK) {
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
    }

    /**
     * check business for authorise create purchase invoice
     * @param  ApPurchaseInvoice pApPurchaseInvoice: purchase invoice which to complete
     */
    private void checkBusinessForAuthorise(ApPurchaseInvoice pApPurchaseInvoice) throws ERPInvalidEntityException {
        boolean checkOK = true;
        String errorMessage = "";
        String errorCode = "";
        if (!pApPurchaseInvoice.getPeriodID().getPeriodStatus().equals(AcPeriodStatus.OPEN)) {
            errorCode = ErrorCode.PURCHASEINVOICE_AUTHORISE_PERIODNOTOPEN;
            errorMessage = "Period status is not open";
            checkOK = false;
        }
        if (checkOK && !pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.CONFIRMATIVE)) {
            errorCode = ErrorCode.PURCHASEINVOICE_AUTHORISE_TRANSTATUS_MUSTBE_COMPLETED;
            errorMessage = "Transaction status of Sale Invoice must be COMPLETE";
            checkOK = false;
        }
        if (!checkOK) {
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
    }

    public void completePurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice, AcJournalEntry pAcJournalEntry, List<AcJournalDetail> pAcJournalDetailList) throws ERPEntityLockedException, ERPEntityRemovedException, ERPInvalidEntityException, ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        this.checkBusinessForComplete(pApPurchaseInvoice, pAcJournalDetailList);
        entryManager.createEntryFromVoucher(pAcJournalEntry, pAcJournalDetailList);
        pApPurchaseInvoice.setJournalEntry(pAcJournalEntry);
        pApPurchaseInvoice.setTransactionStatus(TransactionStatus.COMPLETED);
        crudManager.editSingleEntity(pApPurchaseInvoice);
    }

    public void authorisePurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice) throws ERPEntityLockedException, ERPEntityRemovedException, ERPInvalidEntityException, ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        AcJournalEntry acJournalEntry;
        this.checkBusinessForAuthorise(pApPurchaseInvoice);
        updateRemainAmount(pApPurchaseInvoice);
        acJournalEntry = pApPurchaseInvoice.getJournalEntry();
        if (acJournalEntry != null) {
            acJournalEntry.setAuthoriseDate(pApPurchaseInvoice.getAuthoriseDate());
            acJournalEntry.setAuthoriser(pApPurchaseInvoice.getAuthoriser());
            entryManager.authoriseEntry(acJournalEntry);
        }
        pApPurchaseInvoice.setTransactionStatus(TransactionStatus.AUTHORISED);
        crudManager.editSingleEntity(pApPurchaseInvoice);
        updateReceiptNoteAndPurchaseOrder(pApPurchaseInvoice, false);
    }

    private void updateReceiptNoteAndPurchaseOrder(ApPurchaseInvoice pApPurchaseInvoice, boolean flagDelete) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        List<IvReceiptNote> listReceiptNote = new ArrayList<IvReceiptNote>();
        boolean flagCollate = true;
        boolean flagNotCollate = true;
        IvReceiptNoteDetail receiptNoteDetail = null;
        for (ApPurchaseInvoiceDetail detail : pApPurchaseInvoice.getApPurchaseInvoiceDetailCollection()) {
            receiptNoteDetail = detail.getReceiptNoteDetail();
            Double quantityUpdate = 0.0;
            if (flagDelete) {
                quantityUpdate = -detail.getQuatity();
            } else {
                quantityUpdate = detail.getQuatity();
            }
            if (receiptNoteDetail != null) {
                receiptNoteDetail.setRemainQuantity(receiptNoteDetail.getRemainQuantity() - quantityUpdate);
                crudManager.editSingleEntity(receiptNoteDetail);
                listReceiptNote.add(receiptNoteDetail.getReceiptNote());
                updatePurchaseOrderDetail(receiptNoteDetail.getPurchaseOrderDetail(), quantityUpdate);
            }
        }
        for (IvReceiptNote receiptNote : listReceiptNote) {
            flagCollate = true;
            flagNotCollate = true;
            for (IvReceiptNoteDetail detail : receiptNote.getIvReceiptNoteDetailCollection()) {
                if (detail.getRemainQuantity() != 0) {
                    flagCollate = false;
                    break;
                }
            }
            for (IvReceiptNoteDetail detail : receiptNote.getIvReceiptNoteDetailCollection()) {
                if (detail.getRemainQuantity() - detail.getQuantity() != 0) {
                    flagNotCollate = false;
                    break;
                }
            }
            if (flagCollate && receiptNote.getCollateStatus() != EnumCollateStatus.DOI_CHIEU_HET) {
                receiptNote.setCollateStatus(EnumCollateStatus.DOI_CHIEU_HET);
                crudManager.editSingleEntity(receiptNote);
            } else if (!flagCollate && !flagNotCollate && receiptNote.getCollateStatus() != EnumCollateStatus.DOI_CHIEU_MOT_PHAN) {
                receiptNote.setCollateStatus(EnumCollateStatus.DOI_CHIEU_MOT_PHAN);
                crudManager.editSingleEntity(receiptNote);
            } else if (!flagCollate && flagNotCollate && receiptNote.getCollateStatus() != EnumCollateStatus.CHUA_DOI_CHIEU) {
                receiptNote.setCollateStatus(EnumCollateStatus.CHUA_DOI_CHIEU);
                crudManager.editSingleEntity(receiptNote);
            }
        }
    }

    private void updatePurchaseOrderDetail(PoPurchaseOrderDetail poDetail, Double quantity) throws ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        if (poDetail != null) {
            poDetail.setGotReceipt(poDetail.getGotReceipt() + quantity);
            crudManager.editSingleEntity(poDetail);
        }
    }

    public ApPurchaseInvoice viewPurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice, Boolean bActive) throws ERPUnrecoverableException {
        ApPurchaseInvoice ApPurchaseInvoiceResult;
        ApPurchaseInvoiceResult = crudManager.find(ApPurchaseInvoice.class, pApPurchaseInvoice.getID(), bActive);
        return ApPurchaseInvoiceResult;
    }

    public void updatePurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice, List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailListAdd, List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailListRemove, List<ApPIAllocationDetail> apPIAllocationDetailListAdd, List<ApPIAllocationDetail> apPIAllocationDetailListRemove) throws ERPInvalidEntityException, ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException, ERPEntityRemovedException, ERPEntityLockedException {
        AcJournalEntry acJournalEntry;
        List<ApPurchaseInvoiceDetail> pApPurchaseInvoiceDetailListUpdate = (List) pApPurchaseInvoice.getApPurchaseInvoiceDetailCollection();
        List<ApPIAllocationDetail> apPIAllocationDetailList = new ArrayList<ApPIAllocationDetail>();
        List<ApPIAllocationDetail> apPIAllocationDetailAddList = new ArrayList<ApPIAllocationDetail>();
        List<ApPurchaseInvoiceDetail> apPurchaseInvoiceDetailList = new ArrayList<ApPurchaseInvoiceDetail>();
        ApPurchaseInvoiceDetail apPurchaseInvoiceDetailFind;
        ApPurchaseInvoice apPurchaseInvoiceFind;
        Double percent;
        Double amount;
        this.checkBusinessForUpdate(pApPurchaseInvoice, pApPurchaseInvoiceDetailListAdd, pApPurchaseInvoiceDetailListRemove);
        acJournalEntry = pApPurchaseInvoice.getJournalEntry();
        if (pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.COMPLETED) || pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.REJECTED)) {
            if (acJournalEntry != null) {
                entryManager.deleteEntry(acJournalEntry, true);
                pApPurchaseInvoice.setJournalEntry(null);
            }
            pApPurchaseInvoice.setTransactionStatus(TransactionStatus.DRAFT);
            crudManager.editSingleEntity(pApPurchaseInvoice);
            pApPurchaseInvoice = crudManager.find(ApPurchaseInvoice.class, pApPurchaseInvoice.getID(), true);
        }
        for (ApPurchaseInvoiceDetail PurchaseInvoiceDetail : pApPurchaseInvoiceDetailListRemove) {
            apPIAllocationDetailList = new ArrayList<ApPIAllocationDetail>();
            apPIAllocationDetailList = (List) PurchaseInvoiceDetail.getApPIAllocationDetailCollection();
            for (ApPIAllocationDetail apPi : apPIAllocationDetailList) {
                crudManager.remove(apPi);
            }
            PurchaseInvoiceDetail.setApPIAllocationDetailCollection(new ArrayList<ApPIAllocationDetail>());
            crudManager.remove(PurchaseInvoiceDetail);
            pApPurchaseInvoiceDetailListUpdate.remove(PurchaseInvoiceDetail);
        }
        pApPurchaseInvoice.setApPurchaseInvoiceDetailCollection(pApPurchaseInvoiceDetailListUpdate);
        crudManager.editSingleEntity(pApPurchaseInvoice);
        pApPurchaseInvoice = crudManager.find(ApPurchaseInvoice.class, pApPurchaseInvoice.getID(), true);
        for (ApPurchaseInvoiceDetail PurchaseInvoiceDetail : pApPurchaseInvoiceDetailListAdd) {
            apPIAllocationDetailList = (List) PurchaseInvoiceDetail.getApPIAllocationDetailCollection();
            apPIAllocationDetailAddList = new ArrayList<ApPIAllocationDetail>();
            for (ApPIAllocationDetail apPI : apPIAllocationDetailList) {
                if (apPI.getPercentage() == null) {
                    percent = PurchaseInvoiceDetail.getAmount() / apPI.getAmount();
                    apPI.setPercentage(percent);
                } else if (apPI.getAmount() == null) {
                    amount = (PurchaseInvoiceDetail.getAmount() * apPI.getPercentage()) / 100;
                    apPI.setAmount(amount);
                }
                apPIAllocationDetailListAdd.add(apPI);
                apPI.setInvoiceDetailID(PurchaseInvoiceDetail);
                apPIAllocationDetailAddList.add(apPI);
            }
            PurchaseInvoiceDetail.getApPIAllocationDetailCollection().clear();
            PurchaseInvoiceDetail.setPurchaseInvoice(pApPurchaseInvoice);
            crudManager.create(PurchaseInvoiceDetail);
            ApPurchaseInvoiceDetail_ApPIAllocationDetail_RelationController rc = new ApPurchaseInvoiceDetail_ApPIAllocationDetail_RelationController(PurchaseInvoiceDetail, apPIAllocationDetailAddList, null);
            crudManager.editAddChildrenOTM(rc);
            apPurchaseInvoiceDetailFind = crudManager.find(ApPurchaseInvoiceDetail.class, PurchaseInvoiceDetail.getID(), true);
            apPurchaseInvoiceDetailFind.setPurchaseInvoice(pApPurchaseInvoice);
            crudManager.editSingleEntity(apPurchaseInvoiceDetailFind);
        }
        apPurchaseInvoiceFind = crudManager.find(ApPurchaseInvoice.class, pApPurchaseInvoice.getID(), true);
        apPurchaseInvoiceDetailList = (List) apPurchaseInvoiceFind.getApPurchaseInvoiceDetailCollection();
        List<ApPIAllocationDetail> apPIAllocationDetailListAddOne;
        List<ApPIAllocationDetail> apPIAllocationDetailListRemoveOne;
        ApPurchaseInvoiceDetail_ApPIAllocationDetail_RelationController rc;
        int index;
        for (ApPurchaseInvoiceDetail PurchaseInvoiceDetail : pApPurchaseInvoiceDetailListUpdate) {
            apPIAllocationDetailListAddOne = new ArrayList<ApPIAllocationDetail>();
            apPIAllocationDetailListRemoveOne = new ArrayList<ApPIAllocationDetail>();
            index = apPurchaseInvoiceDetailList.indexOf(PurchaseInvoiceDetail);
            for (ApPIAllocationDetail apPI : apPIAllocationDetailListAdd) {
                if (apPI.getInvoiceDetailID().getID().equals(PurchaseInvoiceDetail.getID())) {
                    apPIAllocationDetailListAddOne.add(apPI);
                }
            }
            for (ApPIAllocationDetail apPI : apPIAllocationDetailListRemove) {
                if (apPI.getInvoiceDetailID().getID().equals(PurchaseInvoiceDetail.getID())) {
                    apPIAllocationDetailListRemoveOne.add(apPI);
                }
            }
            rc = new ApPurchaseInvoiceDetail_ApPIAllocationDetail_RelationController(PurchaseInvoiceDetail, apPIAllocationDetailListAddOne, apPIAllocationDetailListRemoveOne);
            crudManager.editAddRemoveChildrenOTM(rc, Boolean.TRUE);
            apPurchaseInvoiceDetailFind = crudManager.find(ApPurchaseInvoiceDetail.class, PurchaseInvoiceDetail.getID(), true);
            apPurchaseInvoiceDetailList.set(index, apPurchaseInvoiceDetailFind);
        }
        pApPurchaseInvoice.setApPurchaseInvoiceDetailCollection(apPurchaseInvoiceDetailList);
        crudManager.editSingleEntity(pApPurchaseInvoice);
    }

    /**
     *
     * @param pPurchaseInvoice
     */
    private void checkBusinessForDelete(ApPurchaseInvoice pApPurchaseInvoice) throws ERPInvalidEntityException {
        String errorMessage = "";
        String errorCode = "";
        List<ApPayingDetail> apPayingDetailList = new ArrayList<ApPayingDetail>();
        List<ApAdvNettingDetail> apAdvNettingDetailList = new ArrayList<ApAdvNettingDetail>();
        if (!pApPurchaseInvoice.getPeriodID().getPeriodStatus().equals(AcPeriodStatus.OPEN)) {
            errorCode = ErrorCode.PURCHASEINVOICE_DELETE_PERIODNOTOPEN;
            errorMessage = "Period status is not open";
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
        if (pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
            apPayingDetailList = (List) pApPurchaseInvoice.getApPayingDetailCollection();
            for (ApPayingDetail apPayingDetail : apPayingDetailList) {
                if (apPayingDetail.getActiveStatus().equals(EntityStatus.ACTIVE.getValue())) {
                    errorCode = ErrorCode.PURCHASEINVOICE_DELETE_EXIST_PAYING_NETTING;
                    errorMessage = "Exist paying or netting which relate to this purchase invoice";
                    throw new ERPInvalidEntityException(errorCode, errorMessage);
                }
            }
            apAdvNettingDetailList = (List) pApPurchaseInvoice.getApAdvNettingDetailCollection();
            for (ApAdvNettingDetail apAdvNettingDetail : apAdvNettingDetailList) {
                if (apAdvNettingDetail.getActiveStatus().equals(EntityStatus.ACTIVE.getValue())) {
                    errorCode = ErrorCode.PURCHASEINVOICE_DELETE_EXIST_PAYING_NETTING;
                    errorMessage = "Exist paying or netting which relate to this purchase invoice";
                    throw new ERPInvalidEntityException(errorCode, errorMessage);
                }
            }
        }
    }

    public void deletePurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice) throws ERPInvalidEntityException, ERPUnrecoverableException, ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPEntityExistsException {
        AcJournalEntry acJournalEntry;
        this.checkBusinessForDelete(pApPurchaseInvoice);
        acJournalEntry = pApPurchaseInvoice.getJournalEntry();
        if (pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
            if (acJournalEntry != null) {
                entryManager.deleteEntry(acJournalEntry, false);
            }
            for (ApPurchaseInvoiceDetail detail : pApPurchaseInvoice.getApPurchaseInvoiceDetailCollection()) {
                detail.setActiveStatus(EntityStatus.DELETED.getValue());
                crudManager.editSingleEntity(detail);
            }
            pApPurchaseInvoice.setActiveStatus(EntityStatus.DELETED.getValue());
            pApPurchaseInvoice.setTransactionStatus(TransactionStatus.DELETED);
            crudManager.editSingleEntity(pApPurchaseInvoice);
            updateReceiptNoteAndPurchaseOrder(pApPurchaseInvoice, true);
        } else {
            if (acJournalEntry != null) {
                entryManager.deleteEntry(acJournalEntry, true);
                pApPurchaseInvoice.setJournalEntry(null);
            }
            crudManager.removeList(pApPurchaseInvoice.getApPurchaseInvoiceDetailCollection());
            pApPurchaseInvoice.getApPurchaseInvoiceDetailCollection().clear();
            crudManager.remove(pApPurchaseInvoice);
        }
    }

    /**
     * find journal entry by Journal No
     * @param pJournalNo
     * @return journal entry if found else return null
     * @throws ERPUnrecoverableException
     */
    private AcJournalEntry findJournalEntryByJournalNo(String pJournalNo) throws ERPUnrecoverableException {
        List<AcJournalEntry> acJournalEntryList = new ArrayList<AcJournalEntry>();
        QuerySelector acJournalQuery = new AcJournalEntryQuerySelector();
        HashMap pCriteria = new HashMap();
        pCriteria.put("jnlEntryNo", pJournalNo);
        acJournalEntryList = crudManager.findByCriteria(acJournalQuery, pCriteria);
        if (acJournalEntryList.size() > 0) {
            return acJournalEntryList.get(0);
        }
        return null;
    }

    public List<ApPurchaseInvoice> findPurchaseInvoiceByCriteria(HashMap pCriteria) throws ERPUnrecoverableException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        List<ApPurchaseInvoice> ApPurchaseInvoiceList = new ArrayList<ApPurchaseInvoice>();
        List<ApPurchaseInvoice> ApPurchaseInvoiceListFilter = new ArrayList<ApPurchaseInvoice>();
        QuerySelector qs = new ApPurchaseInvoiceQuerySelector();
        String invoiceNoFrom = "", invoiceNoTo = "";
        if (pCriteria.get("invoiceNoFrom") != null && pCriteria.get("invoiceNoTo") != null) {
            invoiceNoFrom = pCriteria.get("invoiceNoFrom").toString();
            invoiceNoTo = pCriteria.get("invoiceNoTo").toString();
        }
        if (pCriteria.containsKey("invoiceNoFrom")) {
            pCriteria.remove("invoiceNoFrom");
        }
        if (pCriteria.containsKey("invoiceNoTo")) {
            pCriteria.remove("invoiceNoTo");
        }
        ApPurchaseInvoiceList = crudManager.findByCriteria(qs, pCriteria);
        if (!invoiceNoFrom.equals("") && !invoiceNoTo.equals("")) {
            for (ApPurchaseInvoice PurchaseInvoice : ApPurchaseInvoiceList) {
                if (checkInString(PurchaseInvoice.getInvoiceNo(), invoiceNoFrom, invoiceNoTo)) {
                    ApPurchaseInvoiceListFilter.add(PurchaseInvoice);
                }
            }
            return ApPurchaseInvoiceListFilter;
        }
        return ApPurchaseInvoiceList;
    }

    /**
     * check if pStrValue >=pStrFrom and <= pStrTo
     * @param pStrValue
     * @param pStrFrom
     * @param pStrTo
     * @return true if in string
     * false if not in string
     */
    private boolean checkInString(String pStrValue, String pStrFrom, String pStrTo) {
        if (pStrValue.compareTo(pStrFrom) >= 0 && pStrValue.compareTo(pStrTo) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * check purchase invoice if have any collection for this purchase invoice which have not authorised yet: throw ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_HAVEOTHERCOLLECTION_NOTYET_AUTHORISED
     * check if this purchase invoice authorised or not: throw ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_NOTYET_AUTHORISED
     * check if this purchase invoice have remain amount: throw ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_FULLPAIED
     * check if collection date must > purchase invoice date: throw ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_LATERTHAN_COLLECTION
     * @param pApPurchaseInvoice
     * @param pApPaying
     * @return
     */
    public void checkToCreatePaying(ApPurchaseInvoice pApPurchaseInvoice, ApPaying pApPaying) throws ERPInvalidEntityException {
        String errorMessage = "";
        String errorCode = "";
        boolean checkOK = true;
        List<ApPayingDetail> apPayingDetailList = new ArrayList<ApPayingDetail>();
        if (!pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
            errorCode = ErrorCode.PURCHASEINVOICE_NOTYET_AUTHORISED;
            errorMessage = "Sale Invoice has not authorised yet";
            checkOK = false;
        }
        if (checkOK && pApPurchaseInvoice.getActiveStatus().equals(EntityStatus.DELETED.getValue())) {
            errorCode = ErrorCode.PURCHASEINVOICE_DELETED;
            errorMessage = "Sale Invoice had been deleted";
            checkOK = false;
        }
        if (checkOK && pApPurchaseInvoice.getTotalRemainedAmount() == 0) {
            errorCode = ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_FULLPAIED;
            errorMessage = "Sale Invoice has been paied fully";
            checkOK = false;
        }
        apPayingDetailList = (List) pApPurchaseInvoice.getApPayingDetailCollection();
        if (checkOK) {
            for (ApPayingDetail apPayingDetail : apPayingDetailList) {
                if (!apPayingDetail.getPayID().getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
                    if ((pApPaying.getID() == null) || (pApPaying.getID() != null && !apPayingDetail.getPayID().getPayNo().equals(pApPaying.getPayNo()))) {
                        errorCode = ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_HAVEOTHERCOLLECTION_NOTYET_AUTHORISED;
                        errorMessage = "Sale Invoice related to another collection which has not authorised yet";
                        checkOK = false;
                        break;
                    }
                }
            }
        }
        if (checkOK && pApPurchaseInvoice.getTransactionDate().getTime() - pApPaying.getTransactionDate().getTime() >= 0) {
            errorCode = ErrorCode.PURCHASEINVOICE_CHECKCREATE_COLLECTION_LATERTHAN_COLLECTION;
            errorMessage = "Transaction Date of purchase invoice later than collection's one";
            checkOK = false;
        }
        if (!checkOK) {
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
    }

    public void checkToDeletePaying(ApPurchaseInvoice pApPurchaseInvoice, ApPaying pApPaying) throws ERPInvalidEntityException {
        String errorMessage = "";
        String errorCode = "";
        List<ApPayingDetail> apPayingDetailList = new ArrayList<ApPayingDetail>();
        List<ApAdvNettingDetail> apAdvNettingDetailList = new ArrayList<ApAdvNettingDetail>();
        apPayingDetailList = (List) pApPurchaseInvoice.getApPayingDetailCollection();
        for (ApPayingDetail apPayingDetail : apPayingDetailList) {
            if (!apPayingDetail.getPayID().getPayNo().equals(pApPaying.getPayNo()) && apPayingDetail.getPayID().getTransactionStatus().equals(TransactionStatus.AUTHORISED) && !apPayingDetail.getPayID().getActiveStatus().equals(EntityStatus.DELETED.getValue()) && apPayingDetail.getPayID().getAuthoriseDate().getTime() - pApPaying.getAuthoriseDate().getTime() > 0) {
                errorCode = ErrorCode.PURCHASEINVOICE_CHECKDELETE_COLLECTION_HAVE_LATER_OTHERCOLLECTION;
                errorMessage = "Sale Invoice related to another collection which later deleting collection";
                throw new ERPInvalidEntityException(errorCode, errorMessage);
            }
        }
    }

    public void checkToCreateApNetting(ApPurchaseInvoice pApPurchaseInvoice, ApAdvNetting pApAdvNetting) throws ERPInvalidEntityException {
        String errorMessage = "";
        String errorCode = "";
        boolean checkOK = true;
        List<ApAdvNettingDetail> apAdvNettingDetailList = new ArrayList<ApAdvNettingDetail>();
        if (!pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
            errorCode = ErrorCode.PURCHASEINVOICE_NOTYET_AUTHORISED;
            errorMessage = "Sale Invoice has not authorised yet";
            checkOK = false;
        }
        if (checkOK && pApPurchaseInvoice.getActiveStatus().equals(EntityStatus.DELETED.getValue())) {
            errorCode = ErrorCode.PURCHASEINVOICE_DELETED;
            errorMessage = "Sale Invoice had been deleted";
            checkOK = false;
        }
        if (checkOK && pApPurchaseInvoice.getTotalRemainedAmount() == 0) {
            errorCode = ErrorCode.PURCHASEINVOICE_CHECKCREATE_NETTING_FULLPAIED;
            errorMessage = "Sale Invoice has been paied fully";
            checkOK = false;
        }
        apAdvNettingDetailList = (List) pApPurchaseInvoice.getApAdvNettingDetailCollection();
        if (checkOK) {
            for (ApAdvNettingDetail apAdvNettingDetail : apAdvNettingDetailList) {
                if (!apAdvNettingDetail.getNettingID().getTransactionStatus().equals(TransactionStatus.AUTHORISED)) {
                    if ((pApAdvNetting.getID()) == null || (pApAdvNetting.getID() != null && !apAdvNettingDetail.getNettingID().getNettingNo().equals(pApAdvNetting.getNettingNo()))) {
                        errorCode = ErrorCode.PURCHASEINVOICE_CHECKCREATE_NETTING_HAVEOTHERCOLLECTION_NOTYET_AUTHORISED;
                        errorMessage = "Sale Invoice related to another netting which has not authorised yet";
                        checkOK = false;
                        break;
                    }
                }
            }
        }
        if (!checkOK) {
            throw new ERPInvalidEntityException(errorCode, errorMessage);
        }
    }

    public void checkToDeleteApNetting(ApPurchaseInvoice pApPurchaseInvoice, ApAdvNetting pApAdvNetting) throws ERPInvalidEntityException {
        String errorMessage = "";
        String errorCode = "";
        List<ApPayingDetail> apPayingDetailList = new ArrayList<ApPayingDetail>();
        List<ApAdvNettingDetail> apAdvNettingDetailList = new ArrayList<ApAdvNettingDetail>();
        apPayingDetailList = (List) pApPurchaseInvoice.getApPayingDetailCollection();
        for (ApPayingDetail apPayingDetail : apPayingDetailList) {
            if (apPayingDetail.getPayID().getTransactionStatus().equals(TransactionStatus.AUTHORISED) && !apPayingDetail.getPayID().getActiveStatus().equals(EntityStatus.DELETED.getValue()) && apPayingDetail.getPayID().getAuthoriseDate().getTime() - pApAdvNetting.getAuthoriseDate().getTime() > 0) {
                errorCode = ErrorCode.PURCHASEINVOICE_CHECKDELETE_NETTING_HAVE_LATER_OTHERCOLLECTION;
                errorMessage = "Purchase Invoice related to another payment which later deleting payment";
                throw new ERPInvalidEntityException(errorCode, errorMessage);
            }
        }
        apAdvNettingDetailList = (List) pApPurchaseInvoice.getApAdvNettingDetailCollection();
        for (ApAdvNettingDetail apAdvNettingDetail : apAdvNettingDetailList) {
            if (!apAdvNettingDetail.getNettingID().getNettingNo().equals(pApAdvNetting.getNettingNo()) && apAdvNettingDetail.getNettingID().getTransactionStatus().equals(TransactionStatus.AUTHORISED) && apAdvNettingDetail.getNettingID().getActiveStatus() != EntityStatus.DELETED.getValue() && apAdvNettingDetail.getNettingID().getAuthoriseDate().getTime() - pApAdvNetting.getAuthoriseDate().getTime() > 0) {
                errorCode = ErrorCode.PURCHASEINVOICE_CHECKDELETE_NETTING_HAVE_LATER_OTHERNETTING;
                errorMessage = "Purchase Invoice related to another netting which later deleting netting";
                throw new ERPInvalidEntityException(errorCode, errorMessage);
            }
        }
    }

    public void comfirmPurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice) throws ERPEntityLockedException, ERPEntityRemovedException, ERPInvalidEntityException, ERPEntityExistsException, ERPUniqueConstraintViolationException, ERPUnrecoverableException {
        if (pApPurchaseInvoice.getTransactionStatus() != TransactionStatus.COMPLETED) {
            throw new ERPInvalidEntityException("Transaction must be COMPLETED to authorise!");
        } else {
            pApPurchaseInvoice.setTransactionStatus(TransactionStatus.CONFIRMATIVE);
            crudManager.editSingleEntity(pApPurchaseInvoice);
        }
    }

    public void rejectPurchaseInvoice(ApPurchaseInvoice pApPurchaseInvoice) throws ERPInvalidEntityException, ERPUnrecoverableException, ERPEntityLockedException, ERPEntityRemovedException, ERPUniqueConstraintViolationException, ERPEntityExistsException {
        AcJournalEntry acJournalEntry;
        this.checkBusinessForDelete(pApPurchaseInvoice);
        acJournalEntry = pApPurchaseInvoice.getJournalEntry();
        if (pApPurchaseInvoice.getTransactionStatus().equals(TransactionStatus.CONFIRMATIVE) && pApPurchaseInvoice.getActiveStatus() != EntityStatus.DELETED.getValue()) {
            if (acJournalEntry != null) {
                entryManager.deleteEntry(acJournalEntry, true);
                pApPurchaseInvoice.setJournalEntry(null);
            }
            pApPurchaseInvoice.setTransactionStatus(TransactionStatus.REJECTED);
            crudManager.editSingleEntity(pApPurchaseInvoice);
        }
    }
}
