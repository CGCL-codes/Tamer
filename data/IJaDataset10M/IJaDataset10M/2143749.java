package org.koossery.adempiere.svco.impl.pa;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.pa.PA_ColorSchemaDTO;
import org.koossery.adempiere.core.contract.criteria.pa.PA_ColorSchemaCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.pa.IPA_ColorSchemaSISV;
import org.koossery.adempiere.core.contract.interfaces.pa.IPA_ColorSchemaSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class PA_ColorSchemaSVCOImpl extends AbstractCommonSVCO implements IPA_ColorSchemaSVCO, InitializingBean {

    private IPA_ColorSchemaSISV pacolorschemaSISVImpl;

    private static Logger logger = Logger.getLogger(PA_ColorSchemaSVCOImpl.class);

    public PA_ColorSchemaSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.pacolorschemaSISVImpl = (IPA_ColorSchemaSISV) this.getFinder().get("SISV/PA_ColorSchema");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createPA_ColorSchema(Properties ctx, PA_ColorSchemaDTO pA_ColorSchemaDTO, String trxname) throws KTAdempiereException {
        try {
            return pacolorschemaSISVImpl.createPA_ColorSchema(ctx, pA_ColorSchemaDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_COLORSCHEMA_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public PA_ColorSchemaDTO findOnePA_ColorSchema(Properties ctx, int pA_ColorSchema_ID) throws KTAdempiereException {
        try {
            return pacolorschemaSISVImpl.getPA_ColorSchema(ctx, pA_ColorSchema_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_COLORSCHEMA_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updatePA_ColorSchema(Properties ctx, PA_ColorSchemaDTO pA_ColorSchemaDTO) throws KTAdempiereException {
        try {
            pacolorschemaSISVImpl.updatePA_ColorSchema(ctx, pA_ColorSchemaDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_COLORSCHEMA_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<PA_ColorSchemaDTO> findPA_ColorSchema(Properties ctx, PA_ColorSchemaCriteria pA_ColorSchemaCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<PA_ColorSchemaDTO>) pacolorschemaSISVImpl.findPA_ColorSchema(ctx, pA_ColorSchemaCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_COLORSCHEMA_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updatePA_ColorSchema(PA_ColorSchemaCriteria criteria) throws KTAdempiereException {
        try {
            return pacolorschemaSISVImpl.updatePA_ColorSchema(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "PA_COLORSCHEMA_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
