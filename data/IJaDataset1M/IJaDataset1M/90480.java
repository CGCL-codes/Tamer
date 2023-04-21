package org.koossery.adempiere.svco.impl.bank;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.bank.C_BankDTO;
import org.koossery.adempiere.core.contract.criteria.bank.C_BankCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.bank.IC_BankSISV;
import org.koossery.adempiere.core.contract.interfaces.bank.IC_BankSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class C_BankSVCOImpl extends AbstractCommonSVCO implements IC_BankSVCO, InitializingBean {

    private IC_BankSISV cbankSISVImpl;

    private static Logger logger = Logger.getLogger(C_BankSVCOImpl.class);

    public C_BankSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.cbankSISVImpl = (IC_BankSISV) this.getFinder().get("SISV/C_Bank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_Bank(Properties ctx, C_BankDTO c_BankDTO, String trxname) throws KTAdempiereException {
        try {
            return cbankSISVImpl.createC_Bank(ctx, c_BankDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANK_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_BankDTO findOneC_Bank(Properties ctx, int c_Bank_ID) throws KTAdempiereException {
        try {
            return cbankSISVImpl.getC_Bank(ctx, c_Bank_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANK_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_Bank(Properties ctx, C_BankDTO c_BankDTO) throws KTAdempiereException {
        try {
            cbankSISVImpl.updateC_Bank(ctx, c_BankDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANK_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_BankDTO> findC_Bank(Properties ctx, C_BankCriteria c_BankCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_BankDTO>) cbankSISVImpl.findC_Bank(ctx, c_BankCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANK_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateC_Bank(C_BankCriteria criteria) throws KTAdempiereException {
        try {
            return cbankSISVImpl.updateC_Bank(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BANK_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
