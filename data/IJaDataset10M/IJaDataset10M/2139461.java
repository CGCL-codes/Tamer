package org.koossery.adempiere.svco.impl.generated;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.koossery.adempiere.svco.impl.AbstractCommonSVCO;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.generated.GL_FundDTO;
import org.koossery.adempiere.core.contract.criteria.generated.GL_FundCriteria;
import org.koossery.adempiere.core.backend.interfaces.sisv.generated.IGL_FundSISV;
import org.koossery.adempiere.core.contract.interfaces.generated.IGL_FundSVCO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public class GL_FundSVCOImpl extends AbstractCommonSVCO implements IGL_FundSVCO, InitializingBean {

    private IGL_FundSISV glfundSISVImpl;

    private static Logger logger = Logger.getLogger(GL_FundSVCOImpl.class);

    public GL_FundSVCOImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getFinder() == null) System.out.println("finder null");
            this.glfundSISVImpl = (IGL_FundSISV) this.getFinder().get("SISV/GL_Fund");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createGL_Fund(Properties ctx, GL_FundDTO gL_FundDTO, String trxname) throws KTAdempiereException {
        try {
            return glfundSISVImpl.createGL_Fund(ctx, gL_FundDTO, trxname);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "GL_FUND_SVCO_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public GL_FundDTO findOneGL_Fund(Properties ctx, int gL_Fund_ID) throws KTAdempiereException {
        try {
            return glfundSISVImpl.getGL_Fund(ctx, gL_Fund_ID, null);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "GL_FUND_SVCO_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateGL_Fund(Properties ctx, GL_FundDTO gL_FundDTO) throws KTAdempiereException {
        try {
            glfundSISVImpl.updateGL_Fund(ctx, gL_FundDTO);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "GL_FUND_SVCO_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<GL_FundDTO> findGL_Fund(Properties ctx, GL_FundCriteria gL_FundCriteria) throws KTAdempiereException {
        try {
            return (ArrayList<GL_FundDTO>) glfundSISVImpl.findGL_Fund(ctx, gL_FundCriteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "GL_FUND_SVCO_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    /**Modification : recommandée pour l'activation et désactivation
    *
    **/
    public boolean updateGL_Fund(GL_FundCriteria criteria) throws KTAdempiereException {
        try {
            return glfundSISVImpl.updateGL_Fund(criteria);
        } catch (KTAdempiereException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereException ktadappe = new KTAdempiereException(SVCOErrorMsgXmlFileName, "GL_FUND_SVCO_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
