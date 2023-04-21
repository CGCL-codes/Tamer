package org.koossery.adempiere.svco.impl.generated;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.generated.C_ElementDTO;
import org.koossery.adempiere.core.contract.criteria.generated.C_ElementCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.generated.IC_ElementSISV;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_ElementSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class C_ElementSVCOImpl extends AbstractCommonSVCO implements IC_ElementSVCO, InitializingBean {

    private IC_ElementSISV celementSISVImpl;

    private static Logger logger = Logger.getLogger(C_ElementSVCOImpl.class);

    public C_ElementSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.celementSISVImpl = (IC_ElementSISV) this.getFinder().get("SISV/C_Element");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_Element(Properties ctx, C_ElementDTO c_ElementDTO, String trxname) throws KTAdempiereException {
        try {
            return celementSISVImpl.createC_Element(ctx, c_ElementDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_ELEMENT_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_ElementDTO findOneC_Element(Properties ctx, int c_Element_ID) throws KTAdempiereException {
        try {
            return celementSISVImpl.getC_Element(ctx, c_Element_ID);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_ELEMENT_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_Element(Properties ctx, C_ElementDTO c_ElementDTO) throws KTAdempiereException {
        try {
            celementSISVImpl.updateC_Element(ctx, c_ElementDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_ELEMENT_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_ElementDTO> findC_Element(Properties ctx, C_ElementCriteria c_ElementCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<C_ElementDTO>) celementSISVImpl.findC_Element(c_ElementCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_ELEMENT_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateC_Element(C_ElementCriteria criteria) throws KTAdempiereException {
        try {
            return celementSISVImpl.updateC_Element(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "C_ELEMENT_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
