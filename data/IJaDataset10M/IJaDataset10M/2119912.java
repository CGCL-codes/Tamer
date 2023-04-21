package org.koossery.adempiere.svco.impl.bank;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.bank.C_BankAccount_AcctDTO;
import org.koossery.adempiere.core.contract.criteria.bank.C_BankAccount_AcctCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.bank.IC_BankAccount_AcctSISV;
import org.koossery.adempiere.core.contract.interfaces.bank.IC_BankAccount_AcctSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class C_BankAccount_AcctSVCOImpl extends AbstractCommonSVCO implements IC_BankAccount_AcctSVCO, InitializingBean {

    private IC_BankAccount_AcctSISV cbankaccountacctSISVImpl;

    private static Logger logger = Logger.getLogger(C_BankAccount_AcctSVCOImpl.class);

    public C_BankAccount_AcctSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.cbankaccountacctSISVImpl = (IC_BankAccount_AcctSISV) this.getFinder().get("SISV/C_BankAccount_Acct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_BankAccount_Acct(Properties ctx, C_BankAccount_AcctDTO c_BankAccount_AcctDTO, String trxname) throws KTAdempiereException {
        try {
            return cbankaccountacctSISVImpl.createC_BankAccount_Acct(ctx, c_BankAccount_AcctDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKACCOUNT_ACCT_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_BankAccount_AcctDTO findOneC_BankAccount_Acct(Properties ctx, int c_BankAccount_Acct_ID) throws KTAdempiereException {
        try {
            return cbankaccountacctSISVImpl.getC_BankAccount_Acct(ctx, c_BankAccount_Acct_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKACCOUNT_ACCT_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_BankAccount_Acct(Properties ctx, C_BankAccount_AcctDTO c_BankAccount_AcctDTO) throws KTAdempiereException {
        try {
            cbankaccountacctSISVImpl.updateC_BankAccount_Acct(ctx, c_BankAccount_AcctDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKACCOUNT_ACCT_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_BankAccount_AcctDTO> findC_BankAccount_Acct(Properties ctx, C_BankAccount_AcctCriteria c_BankAccount_AcctCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_BankAccount_AcctDTO>) cbankaccountacctSISVImpl.findC_BankAccount_Acct(ctx, c_BankAccount_AcctCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKACCOUNT_ACCT_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateC_BankAccount_Acct(C_BankAccount_AcctCriteria criteria) throws KTAdempiereException {
        try {
            return cbankaccountacctSISVImpl.updateC_BankAccount_Acct(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKACCOUNT_ACCT_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
