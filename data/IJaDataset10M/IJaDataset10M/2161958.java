package org.koossery.adempiere.svco.impl.role;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.backend.interfaces.sisv.role.IAD_Form_AccessSISV;
import org.koossery.adempiere.core.contract.criteria.role.AD_Form_AccessCriteria;
import org.koossery.adempiere.core.contract.dto.role.AD_Form_AccessDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.role.IAD_Form_AccessSVCO;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;

public class AD_Form_AccessSVCOImpl extends AbstractCommonSVCO implements IAD_Form_AccessSVCO, InitializingBean {

    private IAD_Form_AccessSISV adformaccessSISVImpl;

    private static Logger logger = Logger.getLogger(AD_Form_AccessSVCOImpl.class);

    public AD_Form_AccessSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.adformaccessSISVImpl = (IAD_Form_AccessSISV) this.getFinder().get("SISV/AD_Form_Access");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_Form_Access(Properties ctx, AD_Form_AccessDTO aD_Form_AccessDTO, String trxname) throws KTAdempiereException {
        try {
            return adformaccessSISVImpl.createAD_Form_Access(ctx, aD_Form_AccessDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_FORM_ACCESS_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_Form_AccessDTO findOneAD_Form_Access(Properties ctx, int role_ID, int form_ID) throws KTAdempiereException {
        try {
            return adformaccessSISVImpl.getAD_Form_Access(ctx, role_ID, form_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_FORM_ACCESS_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_Form_Access(Properties ctx, AD_Form_AccessDTO aD_Form_AccessDTO) throws KTAdempiereException {
        try {
            adformaccessSISVImpl.updateAD_Form_Access(ctx, aD_Form_AccessDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_FORM_ACCESS_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_Form_AccessDTO> findAD_Form_Access(Properties ctx, AD_Form_AccessCriteria aD_Form_AccessCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_Form_AccessDTO>) adformaccessSISVImpl.findAD_Form_Access(ctx, aD_Form_AccessCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_FORM_ACCESS_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**
    *  Suppression des enregistrements
    **/
    public boolean deleteAD_Form_Access(Properties ctx, AD_Form_AccessCriteria criteria) throws KTAdempiereException {
        try {
            return adformaccessSISVImpl.deleteAD_Form_Access(ctx, criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_FORM_ACCESS_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
