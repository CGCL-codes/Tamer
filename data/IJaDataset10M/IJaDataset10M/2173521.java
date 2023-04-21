package com.ecs.etrade.bo.accounting;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ecs.etrade.bo.constants.Constants;
import com.ecs.etrade.bo.utils.Fault;
import com.ecs.etrade.da.AccountType;
import com.ecs.etrade.da.AccountTypeDAO;
import com.ecs.etrade.da.Adj;
import com.ecs.etrade.da.BatchId;
import com.ecs.etrade.da.GenlJrnl;
import com.ecs.etrade.da.GenlJrnlDAO;
import com.ecs.etrade.da.GenlJrnlId;
import com.ecs.etrade.da.PymtRcpt;

/**
 * @author Alok Ranjan
 *
 */
public class GenlJrnlManagerImpl implements GenlJrnlManager {

    private static final Log log = LogFactory.getLog(GenlJrnlManagerImpl.class);

    private AccountTypeDAO accountTypeDAO;

    private GenlJrnlDAO genlJrnlDAO;

    /**
	 * 
	 */
    public GenlJrnlManagerImpl() {
    }

    public void createGenlJrnlEntry(AccountType accountType, PymtRcpt pymtRcpt, Adj adj) throws AccountingException {
        try {
            if ((pymtRcpt == null && adj == null) || (pymtRcpt != null && adj != null)) {
                throw new Exception("You must pass a valid Payment/Receipt or Adjustment details to create Journal entry!");
            }
            BatchId batchId = null;
            if (pymtRcpt != null) {
                batchId = new BatchId();
                batchId.setBatchCd(pymtRcpt.getId().getBatchCd());
                batchId.setBatchDt(pymtRcpt.getId().getBatchDt());
                batchId.setBatchNum(pymtRcpt.getId().getBatchNum());
                batchId.setBatchSeqNbr(pymtRcpt.getId().getBatchSeqNbr());
            } else {
                batchId = adj.getBatch().getId();
            }
            String crDBInd = "";
            String remarks = " ";
            float jrnlAmount = 0.0f;
            if (adj != null) {
                crDBInd = adj.getCrDbInd();
                remarks = adj.getRemarks();
                jrnlAmount = adj.getAdjAmt();
            } else {
                if (pymtRcpt.getPymtRcptInd().equals(Constants.PYMT_IND)) {
                    crDBInd = Constants.ADJ_IND_CREDIT;
                    jrnlAmount = pymtRcpt.getPymtAmt();
                } else {
                    crDBInd = Constants.ADJ_IND_DEBIT;
                    jrnlAmount = pymtRcpt.getRcptAmt();
                }
                remarks = pymtRcpt.getRemarks();
            }
            createJrnlEntry(batchId, accountType, crDBInd, jrnlAmount, remarks);
            if (crDBInd.equals(Constants.ADJ_IND_CREDIT)) {
                crDBInd = Constants.ADJ_IND_DEBIT;
            } else {
                crDBInd = Constants.ADJ_IND_CREDIT;
            }
            AccountType offsetAccountType = accountTypeDAO.findById(accountType.getOffsetAccountTypeId());
            if (offsetAccountType == null) {
                throw new Exception(" Could not find offset account type for main account type with ACCOUNT_TYPE_ID = " + accountType.getAccountTypeId());
            }
            createJrnlEntry(batchId, offsetAccountType, crDBInd, jrnlAmount, remarks);
        } catch (AccountingException e) {
            throw e;
        } catch (Exception e) {
            Fault fault = new Fault("FAULT_CREATE_JOURNAL_ENTRY");
            String logString = "Failed to create journal entry for ACCOUNT_TYPE_ID = " + accountType.getAccountTypeId();
            log.error(logString + " : " + e.getMessage());
            throw new AccountingException(fault.getFaultMessage() + logString + e.getMessage(), fault.getFaultCode(), fault.getFaultType());
        }
    }

    private void createJrnlEntry(BatchId batchId, AccountType accountType, String crDBInd, float jrnlAmount, String remarks) throws AccountingException {
        try {
            GenlJrnl genlJrnl = new GenlJrnl();
            genlJrnl.setAccountType(accountType);
            GenlJrnlId genlJrnlId = new GenlJrnlId(batchId.getBatchCd(), batchId.getBatchDt(), batchId.getBatchNum(), batchId.getBatchSeqNbr(), crDBInd);
            genlJrnl.setId(genlJrnlId);
            genlJrnl.setJrnlAmt(jrnlAmount);
            genlJrnl.setJrnlDt(new Date());
            genlJrnl.setRemarks(remarks);
            genlJrnlDAO.save(genlJrnl);
        } catch (Exception e) {
            Fault fault = new Fault("FAULT_CREATE_JOURNAL_ENTRY");
            String logString = "Failed to create journal entry for ACCOUNT_TYPE_ID = " + accountType.getAccountTypeId() + ", CR_DB_IND = " + crDBInd + ", BATCH_CD = " + batchId.getBatchCd() + ", BATCH_DT = " + batchId.getBatchDt() + ", BATCH_NBR = " + batchId.getBatchNum();
            log.error(logString + " : " + e.getMessage());
            throw new AccountingException(fault.getFaultMessage() + logString + e.getMessage(), fault.getFaultCode(), fault.getFaultType());
        }
    }

    public GenlJrnl[] getGenlJrnlEntries(AccountType accountType) throws AccountingException {
        try {
            GenlJrnl genlJrnlExample = new GenlJrnl();
            genlJrnlExample.setAccountType(accountType);
            List<GenlJrnl> genlJrnlList = genlJrnlDAO.findByExample(genlJrnlExample);
            if (genlJrnlList != null) {
                return genlJrnlList.toArray(new GenlJrnl[genlJrnlList.size()]);
            } else {
                return null;
            }
        } catch (Exception e) {
            Fault fault = new Fault("FAULT_RETRIEVE_JOURNAL_ENTRY");
            String logString = "Failed to create journal entry for ACCOUNT_TYPE_ID = " + accountType.getAccountTypeId();
            log.error(logString + " : " + e.getMessage());
            throw new AccountingException(fault.getFaultMessage() + logString + e.getMessage(), fault.getFaultCode(), fault.getFaultType());
        }
    }

    /**
	 * @return the accountTypeDAO
	 */
    public AccountTypeDAO getAccountTypeDAO() {
        return accountTypeDAO;
    }

    /**
	 * @param accountTypeDAO the accountTypeDAO to set
	 */
    public void setAccountTypeDAO(AccountTypeDAO accountTypeDAO) {
        this.accountTypeDAO = accountTypeDAO;
    }

    /**
	 * @return the genlJrnlDAO
	 */
    public GenlJrnlDAO getGenlJrnlDAO() {
        return genlJrnlDAO;
    }

    /**
	 * @param genlJrnlDAO the genlJrnlDAO to set
	 */
    public void setGenlJrnlDAO(GenlJrnlDAO genlJrnlDAO) {
        this.genlJrnlDAO = genlJrnlDAO;
    }
}
