package org.koossery.adempiere.svco.impl.pa;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.pa.PA_GoalRestrictionDTO;
import org.koossery.adempiere.core.contract.criteria.pa.PA_GoalRestrictionCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.pa.IPA_GoalRestrictionSISV;
import org.koossery.adempiere.core.contract.interfaces.pa.IPA_GoalRestrictionSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class PA_GoalRestrictionSVCOImpl extends AbstractCommonSVCO implements IPA_GoalRestrictionSVCO, InitializingBean {

    private IPA_GoalRestrictionSISV pagoalrestrictionSISVImpl;

    private static Logger logger = Logger.getLogger(PA_GoalRestrictionSVCOImpl.class);

    public PA_GoalRestrictionSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.pagoalrestrictionSISVImpl = (IPA_GoalRestrictionSISV) this.getFinder().get("SISV/PA_GoalRestriction");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createPA_GoalRestriction(Properties ctx, PA_GoalRestrictionDTO pA_GoalRestrictionDTO, String trxname) throws KTAdempiereException {
        try {
            return pagoalrestrictionSISVImpl.createPA_GoalRestriction(ctx, pA_GoalRestrictionDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_GOALRESTRICTION_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public PA_GoalRestrictionDTO findOnePA_GoalRestriction(Properties ctx, int pA_GoalRestriction_ID) throws KTAdempiereException {
        try {
            return pagoalrestrictionSISVImpl.getPA_GoalRestriction(ctx, pA_GoalRestriction_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_GOALRESTRICTION_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updatePA_GoalRestriction(Properties ctx, PA_GoalRestrictionDTO pA_GoalRestrictionDTO) throws KTAdempiereException {
        try {
            pagoalrestrictionSISVImpl.updatePA_GoalRestriction(ctx, pA_GoalRestrictionDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_GOALRESTRICTION_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<PA_GoalRestrictionDTO> findPA_GoalRestriction(Properties ctx, PA_GoalRestrictionCriteria pA_GoalRestrictionCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<PA_GoalRestrictionDTO>) pagoalrestrictionSISVImpl.findPA_GoalRestriction(ctx, pA_GoalRestrictionCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_GOALRESTRICTION_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updatePA_GoalRestriction(PA_GoalRestrictionCriteria criteria) throws KTAdempiereException {
        try {
            return pagoalrestrictionSISVImpl.updatePA_GoalRestriction(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_GOALRESTRICTION_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
