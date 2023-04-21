package org.koossery.adempiere.svco.impl.bank;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.bank.C_BankStatementMatcherDTO;
import org.koossery.adempiere.core.contract.criteria.bank.C_BankStatementMatcherCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.bank.IC_BankStatementMatcherSISV;
import org.koossery.adempiere.core.contract.interfaces.bank.IC_BankStatementMatcherSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class C_BankStatementMatcherSVCOImpl extends AbstractCommonSVCO implements IC_BankStatementMatcherSVCO, InitializingBean {

    private IC_BankStatementMatcherSISV cbankstatementmatcherSISVImpl;

    private static Logger logger = Logger.getLogger(C_BankStatementMatcherSVCOImpl.class);

    public C_BankStatementMatcherSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.cbankstatementmatcherSISVImpl = (IC_BankStatementMatcherSISV) this.getFinder().get("SISV/C_BankStatementMatcher");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_BankStatementMatcher(Properties ctx, C_BankStatementMatcherDTO c_BankStatementMatcherDTO, String trxname) throws KTAdempiereException {
        try {
            return cbankstatementmatcherSISVImpl.createC_BankStatementMatcher(ctx, c_BankStatementMatcherDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKSTATEMENTMATCHER_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_BankStatementMatcherDTO findOneC_BankStatementMatcher(Properties ctx, int c_BankStatementMatcher_ID) throws KTAdempiereException {
        try {
            return cbankstatementmatcherSISVImpl.getC_BankStatementMatcher(ctx, c_BankStatementMatcher_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKSTATEMENTMATCHER_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_BankStatementMatcher(Properties ctx, C_BankStatementMatcherDTO c_BankStatementMatcherDTO) throws KTAdempiereException {
        try {
            cbankstatementmatcherSISVImpl.updateC_BankStatementMatcher(ctx, c_BankStatementMatcherDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKSTATEMENTMATCHER_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_BankStatementMatcherDTO> findC_BankStatementMatcher(Properties ctx, C_BankStatementMatcherCriteria c_BankStatementMatcherCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_BankStatementMatcherDTO>) cbankstatementmatcherSISVImpl.findC_BankStatementMatcher(ctx, c_BankStatementMatcherCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKSTATEMENTMATCHER_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateC_BankStatementMatcher(C_BankStatementMatcherCriteria criteria) throws KTAdempiereException {
        try {
            return cbankstatementmatcherSISVImpl.updateC_BankStatementMatcher(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANKSTATEMENTMATCHER_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
