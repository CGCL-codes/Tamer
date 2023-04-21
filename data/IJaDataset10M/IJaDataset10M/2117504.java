package org.koossery.adempiere.svco.impl.system;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.backend.interfaces.sisv.system.IAD_SystemSISV;
import org.koossery.adempiere.core.contract.criteria.system.AD_SystemCriteria;
import org.koossery.adempiere.core.contract.dto.system.AD_SystemDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.system.IAD_SystemSVCO;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;

public class AD_SystemSVCOImpl extends AbstractCommonSVCO implements IAD_SystemSVCO, InitializingBean {

    private IAD_SystemSISV adsystemSISVImpl;

    private static Logger logger = Logger.getLogger(AD_SystemSVCOImpl.class);

    public AD_SystemSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.adsystemSISVImpl = (IAD_SystemSISV) this.getFinder().get("SISV/AD_System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_System(Properties ctx, AD_SystemDTO aD_SystemDTO, String trxname) throws KTAdempiereException {
        try {
            return adsystemSISVImpl.createAD_System(ctx, aD_SystemDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_SYSTEM_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_SystemDTO findOneAD_System(Properties ctx, int aD_System_ID, int aD_Client_ID) throws KTAdempiereException {
        try {
            return adsystemSISVImpl.getAD_System(ctx, aD_System_ID, aD_Client_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_SYSTEM_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_System(Properties ctx, AD_SystemDTO aD_SystemDTO) throws KTAdempiereException {
        try {
            adsystemSISVImpl.updateAD_System(ctx, aD_SystemDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_SYSTEM_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_SystemDTO> findAD_System(Properties ctx, AD_SystemCriteria aD_SystemCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_SystemDTO>) adsystemSISVImpl.findAD_System(ctx, aD_SystemCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_SYSTEM_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**
    *  Suppression des enregistrements
    **/
    public boolean deleteAD_System(Properties ctx, AD_SystemCriteria criteria) throws KTAdempiereException {
        try {
            return adsystemSISVImpl.deleteAD_System(ctx, criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_SYSTEM_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
