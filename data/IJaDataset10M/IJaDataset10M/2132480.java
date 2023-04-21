package org.koossery.adempiere.svco.impl.bpartner;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.bpartner.C_BPartnerDTO;
import org.koossery.adempiere.core.contract.criteria.bpartner.C_BPartnerCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.bpartner.IC_BPartnerSISV;
import org.koossery.adempiere.core.contract.interfaces.bpartner.IC_BPartnerSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class C_BPartnerSVCOImpl extends AbstractCommonSVCO implements IC_BPartnerSVCO, InitializingBean {

    private IC_BPartnerSISV cbpartnerSISVImpl;

    private static Logger logger = Logger.getLogger(C_BPartnerSVCOImpl.class);

    public C_BPartnerSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.cbpartnerSISVImpl = (IC_BPartnerSISV) this.getFinder().get("SISV/C_BPartner");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_BPartner(Properties ctx, C_BPartnerDTO c_BPartnerDTO, String trxname) throws KTAdempiereException {
        try {
            return cbpartnerSISVImpl.createC_BPartner(ctx, c_BPartnerDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BPARTNER_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_BPartnerDTO findOneC_BPartner(Properties ctx, int c_BPartner_ID) throws KTAdempiereException {
        try {
            return cbpartnerSISVImpl.getC_BPartner(ctx, c_BPartner_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BPARTNER_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_BPartner(Properties ctx, C_BPartnerDTO c_BPartnerDTO) throws KTAdempiereException {
        try {
            cbpartnerSISVImpl.updateC_BPartner(ctx, c_BPartnerDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BPARTNER_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_BPartnerDTO> findC_BPartner(Properties ctx, C_BPartnerCriteria c_BPartnerCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_BPartnerDTO>) cbpartnerSISVImpl.findC_BPartner(ctx, c_BPartnerCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BPARTNER_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateC_BPartner(C_BPartnerCriteria criteria) throws KTAdempiereException {
        try {
            return cbpartnerSISVImpl.updateC_BPartner(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_BPARTNER_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
