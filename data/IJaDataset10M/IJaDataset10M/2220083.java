package org.koossery.adempiere.svco.impl.ldap;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.backend.interfaces.sisv.ldap.IAD_LdapProcessorSISV;
import org.koossery.adempiere.core.contract.criteria.ldap.AD_LdapProcessorCriteria;
import org.koossery.adempiere.core.contract.dto.ldap.AD_LdapProcessorDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.ldap.IAD_LdapProcessorSVCO;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;

public class AD_LdapProcessorSVCOImpl extends AbstractCommonSVCO implements IAD_LdapProcessorSVCO, InitializingBean {

    private IAD_LdapProcessorSISV adldapprocessorSISVImpl;

    private static Logger logger = Logger.getLogger(AD_LdapProcessorSVCOImpl.class);

    public AD_LdapProcessorSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.adldapprocessorSISVImpl = (IAD_LdapProcessorSISV) this.getFinder().get("SISV/AD_LdapProcessor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_LdapProcessor(Properties ctx, AD_LdapProcessorDTO aD_LdapProcessorDTO, String trxname) throws KTAdempiereException {
        try {
            return adldapprocessorSISVImpl.createAD_LdapProcessor(ctx, aD_LdapProcessorDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_LDAPPROCESSOR_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_LdapProcessorDTO findOneAD_LdapProcessor(Properties ctx, int aD_LdapProcessor_ID) throws KTAdempiereException {
        try {
            return adldapprocessorSISVImpl.getAD_LdapProcessor(ctx, aD_LdapProcessor_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_LDAPPROCESSOR_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_LdapProcessor(Properties ctx, AD_LdapProcessorDTO aD_LdapProcessorDTO) throws KTAdempiereException {
        try {
            adldapprocessorSISVImpl.updateAD_LdapProcessor(ctx, aD_LdapProcessorDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_LDAPPROCESSOR_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_LdapProcessorDTO> findAD_LdapProcessor(Properties ctx, AD_LdapProcessorCriteria aD_LdapProcessorCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<AD_LdapProcessorDTO>) adldapprocessorSISVImpl.findAD_LdapProcessor(ctx, aD_LdapProcessorCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_LDAPPROCESSOR_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**
    *  Suppression des enregistrements
    **/
    public boolean deleteAD_LdapProcessor(Properties ctx, AD_LdapProcessorCriteria criteria) throws KTAdempiereException {
        try {
            return adldapprocessorSISVImpl.deleteAD_LdapProcessor(ctx, criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "AD_LDAPPROCESSOR_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
