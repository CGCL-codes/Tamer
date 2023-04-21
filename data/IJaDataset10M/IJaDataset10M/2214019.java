package com.koossery.adempiere.fe.actions.systemRule.country.city.createModifyCity;

import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.dto.country.C_CityDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.country.IC_CitySVCO;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.util.KTUtil;
import com.opensymphony.xwork2.Preparable;

public class CreateModifyCityAction extends KTAdempiereBaseAction implements Preparable {

    private Logger logger = Logger.getLogger(CreateModifyCityAction.class);

    private IC_CitySVCO citySVCO;

    private CityForm createModifyForm;

    private Object[] args;

    public CreateModifyCityAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        citySVCO = (IC_CitySVCO) svcoFinder.get(FrontEndConstants.IC_CitySVCO);
    }

    public void finalize() throws Throwable {
    }

    public String init() {
        try {
            if (getSession().getCtx().containsKey("C_Region_Name")) {
                createModifyForm.setListOfOrgAllowed(getSession().getListOfOrgAllowed());
                createModifyForm.setNomOrg(getSession().getNomOrg());
                createModifyForm.setListOfClientAllowed(getSession().getListOfClientAllowed());
                createModifyForm.setNomClient(getSession().getNomClient());
                createModifyForm.setRegionName(getSession().getCtx().getProperty("C_Region_Name"));
                createModifyForm.setCountryName(getSession().getCtx().getProperty("C_Country_Name"));
                return SUCCESS;
            } else {
                args = new Object[1];
                args[0] = "Region";
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACSUB-005"), args);
                addActionError(message);
                return ERROR;
            }
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACCity-004"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(message);
            return ERROR;
        }
    }

    private void createCity() {
        try {
            C_CityDTO cityDTO = (C_CityDTO) getMapper().map(createModifyForm.getCityBean(), C_CityDTO.class);
            int n;
            if (getSession().getCtx().get("C_Country_ID").toString().equals(null)) {
                n = 0;
            } else n = Integer.parseInt(getSession().getCtx().get("C_Country_ID").toString());
            cityDTO.setC_Country_ID(n);
            if (getSession().getCtx().get("C_Region_ID").toString().equals(null)) {
                n = 0;
            } else n = Integer.parseInt(getSession().getCtx().get("C_Region_ID").toString());
            cityDTO.setC_Region_ID(n);
            int id = citySVCO.createC_City(getSession().getCtx(), cityDTO, null);
            if (id <= 0) {
                args = new Object[1];
                args[0] = createModifyForm.getCityBean().getName();
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACCity-001"), args);
                logger.error(message);
                String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
                addActionError(humanMessage);
            } else {
                args = new Object[1];
                args[0] = createModifyForm.getCityBean().getName();
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACCity-002"), args);
                addActionMessage(message);
            }
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
        } catch (Exception e) {
            args = new Object[3];
            args[0] = createModifyForm.getCityBean().getName();
            args[1] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[2] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACCity-003"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
        }
    }

    private void modifyCity() {
        try {
            C_CityDTO cityDTO = (C_CityDTO) getMapper().map(createModifyForm.getCityBean(), C_CityDTO.class);
            citySVCO.updateC_City(getSession().getCtx(), cityDTO);
            args = new Object[1];
            args[0] = createModifyForm.getCityBean().getName();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMCity-002"), args);
            addActionMessage(message);
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMCity-003"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
        }
    }

    public String save() {
        try {
            getSession().getCtx().setProperty("#AD_Org_ID", String.valueOf(createModifyForm.getIdOrgSelected()));
            createModifyForm.getCityBean().setIsActive(KTUtil.booleanToString(createModifyForm.isActive()));
            if (createModifyForm.getCityBean().getC_City_ID() == 0) {
                createCity();
            } else {
                modifyCity();
            }
            return SUCCESS;
        } catch (Exception e) {
            addActionError(e.getMessage());
            return ERROR;
        }
    }

    public String newRecord() {
        try {
            createModifyForm.resetForm();
            createModifyForm.setDisplay(0);
            return SUCCESS;
        } catch (Exception e) {
            addActionError(e.getMessage());
            return ERROR;
        }
    }

    public CityForm getCreateModifyForm() {
        return (CityForm) getForm(CityForm.class.getName());
    }

    public void setCreateModifyForm(CityForm createModifyForm) {
        this.createModifyForm = createModifyForm;
    }
}
