package com.koossery.adempiere.fe.actions.accounting.taxes.category.findCategory;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.generated.C_TaxCategoryCriteria;
import org.koossery.adempiere.core.contract.dto.generated.C_TaxCategoryDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_TaxCategorySVCO;
import com.koossery.adempiere.fe.actions.accounting.taxes.category.CategoryForm;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.accounting.taxes.category.TaxCategoryBean;
import com.opensymphony.xwork2.Preparable;

/**
 * @version 1.0
 * @created 25-ao�t-2008 16:50:05
 */
public class FindCategoryAction extends KTAdempiereBaseAction implements Preparable {

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FindCategoryAction.class);

    private IC_TaxCategorySVCO taxCategorieSVCO;

    private FindCategoryForm findForm;

    private Object[] args;

    public FindCategoryAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        taxCategorieSVCO = (IC_TaxCategorySVCO) svcoFinder.get(FrontEndConstants.IC_TAXCATEGORYSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String findCategory() {
        try {
            C_TaxCategoryCriteria ctr = (C_TaxCategoryCriteria) getMapper().map(findForm.getTaxCategoryBean(), C_TaxCategoryCriteria.class);
            int m;
            if (getSession().getCtx().get("#AD_Client_ID").toString().equals(null)) {
                m = 0;
            } else m = Integer.parseInt(getSession().getCtx().get("#AD_Client_ID").toString());
            ctr.setAd_Client_ID(m);
            ArrayList<C_TaxCategoryDTO> list = new ArrayList<C_TaxCategoryDTO>();
            findForm.setCategorieMap(new HashMap<Integer, TaxCategoryBean>());
            list = taxCategorieSVCO.findC_TaxCategory(getSession().getCtx(), ctr);
            if (list.size() <= 0 || list == null) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFTC-002");
                addActionMessage(message);
                return SUCCESS;
            } else {
                findForm.setCategorieMap1(new HashMap<Integer, TaxCategoryBean>());
                for (int i = 0; i < list.size(); i++) {
                    TaxCategoryBean taxCategoryBean = (TaxCategoryBean) getMapper().map(list.get(i), TaxCategoryBean.class);
                    findForm.getCategorieMap().put(list.get(i).getC_TaxCategory_ID(), taxCategoryBean);
                    findForm.getCategorieMap1().put(list.get(i).getC_TaxCategory_ID(), taxCategoryBean);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFTC-003"), args);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFTC-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            CategoryForm createModifyForm = (CategoryForm) getForm(CategoryForm.class.getName());
            int idTaxCategorie = findForm.getIdCategorieSelected();
            TaxCategoryBean taxCategoryBean = (TaxCategoryBean) findForm.getCategorieMap1().get(idTaxCategorie);
            createModifyForm.setTaxCategoryBean(taxCategoryBean);
            if (createModifyForm.getTaxCategoryBean().getIsActive().equals("Y")) {
                createModifyForm.setActive(true);
            } else createModifyForm.setActive(false);
            if (createModifyForm.getTaxCategoryBean().getIsDefault().equals("Y")) {
                createModifyForm.setDefaut(true);
            } else createModifyForm.setDefaut(false);
            createModifyForm.setDisplay(1);
            createModifyForm.setFlag(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMTC-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindCategoryForm getFindForm() {
        return (FindCategoryForm) getForm(FindCategoryForm.class.getName());
    }

    public void setFindForm(FindCategoryForm findForm) {
        this.findForm = findForm;
    }
}
