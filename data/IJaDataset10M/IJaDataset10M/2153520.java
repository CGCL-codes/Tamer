package org.koossery.adempiere.svco.impl.org.recurring;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.backend.interfaces.sisv.org.recurring.IC_Recurring_RunSISV;
import org.koossery.adempiere.core.contract.criteria.org.recurring.C_Recurring_RunCriteria;
import org.koossery.adempiere.core.contract.dto.org.recurring.C_Recurring_RunDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.org.recurring.IC_Recurring_RunSVCO;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;

public class C_Recurring_RunSVCOImpl extends AbstractCommonSVCO implements IC_Recurring_RunSVCO, InitializingBean {

    private IC_Recurring_RunSISV crecurringrunSISVImpl;

    private static Logger logger = Logger.getLogger(C_Recurring_RunSVCOImpl.class);

    public C_Recurring_RunSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.crecurringrunSISVImpl = (IC_Recurring_RunSISV) this.getFinder().get("SISV/C_Recurring_Run");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_Recurring_Run(Properties ctx, C_Recurring_RunDTO c_Recurring_RunDTO, String trxname) throws KTAdempiereException {
        try {
            return crecurringrunSISVImpl.createC_Recurring_Run(ctx, c_Recurring_RunDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_RECURRING_RUN_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_Recurring_RunDTO findOneC_Recurring_Run(Properties ctx, int c_Recurring_Run_ID) throws KTAdempiereException {
        try {
            return crecurringrunSISVImpl.getC_Recurring_Run(ctx, c_Recurring_Run_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_RECURRING_RUN_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_Recurring_Run(Properties ctx, C_Recurring_RunDTO c_Recurring_RunDTO) throws KTAdempiereException {
        try {
            crecurringrunSISVImpl.updateC_Recurring_Run(ctx, c_Recurring_RunDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_RECURRING_RUN_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_Recurring_RunDTO> findC_Recurring_Run(Properties ctx, C_Recurring_RunCriteria c_Recurring_RunCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_Recurring_RunDTO>) crecurringrunSISVImpl.findC_Recurring_Run(ctx, c_Recurring_RunCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_RECURRING_RUN_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**
    *  Suppression des enregistrements
    **/
    public boolean deleteC_Recurring_Run(Properties ctx, C_Recurring_RunCriteria criteria) throws KTAdempiereException {
        try {
            return crecurringrunSISVImpl.deleteC_Recurring_Run(ctx, criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_RECURRING_RUN_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
