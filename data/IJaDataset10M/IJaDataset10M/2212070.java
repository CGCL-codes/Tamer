package com.koossery.adempiere.fe.actions.accounting.gl.glFundRestriction.findGLFundRestriction;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.generated.GL_FundRestrictionCriteria;
import org.koossery.adempiere.core.contract.dto.generated.GL_FundRestrictionDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IGL_FundRestrictionSVCO;
import com.koossery.adempiere.fe.actions.accounting.gl.glFundRestriction.GLFundRestrictionForm;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.accounting.gl.gLFundRestriction.GLFundRestrictionBean;
import com.opensymphony.xwork2.Preparable;

/**
 * @version 1.0
 * @created 25-ao�t-2008 16:50:07
 */
public class FindGLFundRestrictionAction extends KTAdempiereBaseAction implements Preparable {

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FindGLFundRestrictionAction.class);

    private IGL_FundRestrictionSVCO glFundRestrictionSVCO;

    private FindGLFundRestrictionForm findForm;

    private Object[] args;

    public FindGLFundRestrictionAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        glFundRestrictionSVCO = (IGL_FundRestrictionSVCO) svcoFinder.get(FrontEndConstants.IGLFundRestrictionSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String findGLFundRestriction() {
        try {
            GL_FundRestrictionCriteria ctr = (GL_FundRestrictionCriteria) getMapper().map(findForm.getGlFundRestrictionBean(), GL_FundRestrictionCriteria.class);
            int m;
            if (getSession().getCtx().get("#AD_Client_ID").toString().equals(null)) {
                m = 0;
            } else m = Integer.parseInt(getSession().getCtx().get("#AD_Client_ID").toString());
            ctr.setAd_Client_ID(m);
            int l;
            if (getSession().getCtx().get("#AD_Org_ID").toString().equals(null)) {
                l = 0;
            } else l = Integer.parseInt(getSession().getCtx().get("#AD_Org_ID").toString());
            ctr.setAd_Org_ID(l);
            ArrayList<GL_FundRestrictionDTO> list = new ArrayList<GL_FundRestrictionDTO>();
            findForm.setGlFundRestrictionMap(new HashMap<Integer, GLFundRestrictionBean>());
            list = glFundRestrictionSVCO.findGL_FundRestriction(getSession().getCtx(), ctr);
            if (list.size() <= 0 || list == null) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFGLFR-002");
                addActionMessage(message);
                return SUCCESS;
            } else {
                findForm.setGlFundRestrictionMap1(new HashMap<Integer, GLFundRestrictionBean>());
                for (int i = 0; i < list.size(); i++) {
                    GLFundRestrictionBean glFundRestrictionBean = (GLFundRestrictionBean) getMapper().map(list.get(i), GLFundRestrictionBean.class);
                    findForm.getGlFundRestrictionMap().put(list.get(i).getGl_FundRestriction_ID(), glFundRestrictionBean);
                    findForm.getGlFundRestrictionMap1().put(list.get(i).getGl_FundRestriction_ID(), glFundRestrictionBean);
                }
                findForm.setNomClient(getSession().getNomClient());
                return SUCCESS;
            }
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
            return ERROR;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFGLFR-003"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String init() {
        try {
            findForm.resetForm();
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFGLFR-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            GLFundRestrictionForm createModifyForm = (GLFundRestrictionForm) getForm(GLFundRestrictionForm.class.getName());
            int idGlFundRestriction = findForm.getIdGLFundRestrictionSelected();
            GLFundRestrictionBean glFundRestrictionBean = (GLFundRestrictionBean) findForm.getGlFundRestrictionMap1().get(idGlFundRestriction);
            createModifyForm.setGlFundRestrictionBean(glFundRestrictionBean);
            if (createModifyForm.getGlFundRestrictionBean().getIsActive().equals("Y")) {
                createModifyForm.setActive(true);
            } else {
                createModifyForm.setActive(false);
            }
            for (int i = 0; i < createModifyForm.listOfAccountElementAllowed.size(); i++) {
                if (createModifyForm.getGlFundRestrictionBean().getC_ElementValue_ID() == createModifyForm.listOfAccountElementAllowed.get(i).getC_ElementValue_ID()) {
                    createModifyForm.setNomAcct(createModifyForm.listOfElementValue.get(i).getName());
                }
            }
            createModifyForm.setFlag(1);
            createModifyForm.setDisplay(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMGLFR-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindGLFundRestrictionForm getFindForm() {
        return (FindGLFundRestrictionForm) getForm(FindGLFundRestrictionForm.class.getName());
    }

    public void setFindForm(FindGLFundRestrictionForm findForm) {
        this.findForm = findForm;
    }
}
