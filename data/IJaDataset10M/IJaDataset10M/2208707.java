package org.koossery.adempiere.svco.impl.user;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.user.AD_UserDef_FieldDTO;
import org.koossery.adempiere.core.contract.criteria.user.AD_UserDef_FieldCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.user.IAD_UserDef_FieldSISV;
import org.koossery.adempiere.core.contract.interfaces.user.IAD_UserDef_FieldSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class AD_UserDef_FieldSVCOImpl extends AbstractCommonSVCO implements IAD_UserDef_FieldSVCO, InitializingBean {

    private IAD_UserDef_FieldSISV aduserdeffieldSISVImpl;

    private static Logger logger = Logger.getLogger(AD_UserDef_FieldSVCOImpl.class);

    public AD_UserDef_FieldSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.aduserdeffieldSISVImpl = (IAD_UserDef_FieldSISV) this.getFinder().get("SISV/AD_UserDef_Field");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_UserDef_Field(Properties ctx, AD_UserDef_FieldDTO aD_UserDef_FieldDTO, String trxname) throws KTAdempiereException {
        try {
            return aduserdeffieldSISVImpl.createAD_UserDef_Field(ctx, aD_UserDef_FieldDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_USERDEF_FIELD_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_UserDef_FieldDTO findOneAD_UserDef_Field(Properties ctx, int aD_UserDef_Field_ID) throws KTAdempiereException {
        try {
            return aduserdeffieldSISVImpl.getAD_UserDef_Field(ctx, aD_UserDef_Field_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_USERDEF_FIELD_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_UserDef_Field(Properties ctx, AD_UserDef_FieldDTO aD_UserDef_FieldDTO) throws KTAdempiereException {
        try {
            aduserdeffieldSISVImpl.updateAD_UserDef_Field(ctx, aD_UserDef_FieldDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_USERDEF_FIELD_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_UserDef_FieldDTO> findAD_UserDef_Field(Properties ctx, AD_UserDef_FieldCriteria aD_UserDef_FieldCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_UserDef_FieldDTO>) aduserdeffieldSISVImpl.findAD_UserDef_Field(ctx, aD_UserDef_FieldCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_USERDEF_FIELD_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**
    *  Suppression des enregistrements
    **/
    public boolean deleteAD_UserDef_Field(Properties ctx, AD_UserDef_FieldCriteria criteria) throws KTAdempiereException {
        try {
            return aduserdeffieldSISVImpl.deleteAD_UserDef_Field(ctx, criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_USERDEF_FIELD_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
