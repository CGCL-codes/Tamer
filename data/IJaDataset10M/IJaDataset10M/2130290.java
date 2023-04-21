package org.koossery.adempiere.svco.impl.generated;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.generated.C_DocTypeCounterDTO;
import org.koossery.adempiere.core.contract.criteria.generated.C_DocTypeCounterCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.generated.IC_DocTypeCounterSISV;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_DocTypeCounterSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class C_DocTypeCounterSVCOImpl extends AbstractCommonSVCO implements IC_DocTypeCounterSVCO, InitializingBean {

    private IC_DocTypeCounterSISV cdoctypecounterSISVImpl;

    private static Logger logger = Logger.getLogger(C_DocTypeCounterSVCOImpl.class);

    public C_DocTypeCounterSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.cdoctypecounterSISVImpl = (IC_DocTypeCounterSISV) this.getFinder().get("SISV/C_DocTypeCounter");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_DocTypeCounter(Properties ctx, C_DocTypeCounterDTO c_DocTypeCounterDTO, String trxname) throws KTAdempiereException {
        try {
            return cdoctypecounterSISVImpl.createC_DocTypeCounter(ctx, c_DocTypeCounterDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_DOCTYPECOUNTER_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_DocTypeCounterDTO findOneC_DocTypeCounter(Properties ctx, int c_DocTypeCounter_ID) throws KTAdempiereException {
        try {
            return cdoctypecounterSISVImpl.getC_DocTypeCounter(ctx, c_DocTypeCounter_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_DOCTYPECOUNTER_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_DocTypeCounter(Properties ctx, C_DocTypeCounterDTO c_DocTypeCounterDTO) throws KTAdempiereException {
        try {
            cdoctypecounterSISVImpl.updateC_DocTypeCounter(ctx, c_DocTypeCounterDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_DOCTYPECOUNTER_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_DocTypeCounterDTO> findC_DocTypeCounter(Properties ctx, C_DocTypeCounterCriteria c_DocTypeCounterCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_DocTypeCounterDTO>) cdoctypecounterSISVImpl.findC_DocTypeCounter(ctx, c_DocTypeCounterCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_DOCTYPECOUNTER_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateC_DocTypeCounter(C_DocTypeCounterCriteria criteria) throws KTAdempiereException {
        try {
            return cdoctypecounterSISVImpl.updateC_DocTypeCounter(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_DOCTYPECOUNTER_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
