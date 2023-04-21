package com.koossery.adempiere.fe.actions.accounting.accountSchema.accSchemaElement.findAccSchemaElt;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.generated.C_AcctSchema_ElementCriteria;
import org.koossery.adempiere.core.contract.dto.generated.C_AcctSchema_ElementDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_AcctSchema_ElementSVCO;
import com.koossery.adempiere.fe.actions.accounting.accountSchema.accSchemaElement.AccSchemaElementForm;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.accounting.accountSchema.accSchemaElement.AccSchemaElementBean;
import com.opensymphony.xwork2.Preparable;

/**
 * @version 1.0
 * @created 25-ao�t-2008 16:50:04
 */
public class FindAccSchemaEltAction extends KTAdempiereBaseAction implements Preparable {

    private static final long serialVersionUID = 1L;

    Object[] args;

    private Logger logger = Logger.getLogger(FindAccSchemaEltAction.class);

    private FindAccSchemaEltForm findForm;

    IC_AcctSchema_ElementSVCO schemaElementSVCO;

    public FindAccSchemaEltAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        schemaElementSVCO = (IC_AcctSchema_ElementSVCO) svcoFinder.get(FrontEndConstants.IAccSchemaElementSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String findAccSchemaElement() {
        try {
            C_AcctSchema_ElementCriteria ctr = (C_AcctSchema_ElementCriteria) getMapper().map(findForm.getAccSchemaElementBean(), C_AcctSchema_ElementCriteria.class);
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
            ArrayList<C_AcctSchema_ElementDTO> list = new ArrayList<C_AcctSchema_ElementDTO>();
            findForm.setAccSchemaElementMap(new HashMap<Integer, AccSchemaElementBean>());
            list = schemaElementSVCO.findC_AcctSchema_Element(getSession().getCtx(), ctr);
            if (list.size() <= 0 || list == null) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFCEC-002");
                addActionMessage(message);
                return SUCCESS;
            } else {
                findForm.setAccSchemaElementMap1(new HashMap<Integer, AccSchemaElementBean>());
                for (int i = 0; i < list.size(); i++) {
                    AccSchemaElementBean accBean = (AccSchemaElementBean) getMapper().map(list.get(i), AccSchemaElementBean.class);
                    findForm.getAccSchemaElementMap().put(list.get(i).getC_AcctSchema_Element_ID(), accBean);
                    findForm.getAccSchemaElementMap1().put(list.get(i).getC_AcctSchema_Element_ID(), accBean);
                }
                findForm.setNomClient(getSession().getNomClient());
                findForm.setNomOrg(getSession().getNomOrg());
                findForm.setNomSchema(getSession().getCtx().getProperty("C_AcctSchema_Name"));
                return SUCCESS;
            }
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
            logger.error(e.getHumanMessage());
            return ERROR;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFCEC-003"), args);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFCEC-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            AccSchemaElementForm createModifyForm = (AccSchemaElementForm) getForm(AccSchemaElementForm.class.getName());
            int idSchema = findForm.getIdAccountElementSelected();
            AccSchemaElementBean schemaBean = (AccSchemaElementBean) findForm.getAccSchemaElementMap1().get(idSchema);
            createModifyForm.setAccSchemaElementBean(schemaBean);
            if (schemaBean.getIsActive().equals("Y")) {
                createModifyForm.setActive(true);
            } else createModifyForm.setActive(false);
            if (schemaBean.getIsBalanced().equals("Y")) {
                createModifyForm.setBalance(true);
            } else createModifyForm.setBalance(false);
            if (schemaBean.getIsMandatory().equals("Y")) {
                createModifyForm.setMandatory(true);
            } else createModifyForm.setMandatory(false);
            getSession().getCtx().put("C_AcctSchema_Element_ID", schemaBean.getC_AcctSchema_Element_ID());
            getSession().getCtx().put("C_AcctSchema_Element_Name", schemaBean.getName());
            createModifyForm.setDisplay(1);
            createModifyForm.setFlag(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMCEC-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindAccSchemaEltForm getFindForm() {
        return (FindAccSchemaEltForm) getForm(FindAccSchemaEltForm.class.getName());
    }

    public void setFindForm(FindAccSchemaEltForm findForm) {
        this.findForm = findForm;
    }
}
