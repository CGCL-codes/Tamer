package com.koossery.adempiere.fe.actions.payroll.list.list.createModifyList;

import org.apache.log4j.Logger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.humanResources.department.Departmentbean;
import com.koossery.adempiere.fe.beans.payroll.listType.HR_ListTypeBean;
import com.koossery.adempiere.fe.beans.payroll.payroll.HR_PayrollBean;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.opensymphony.xwork2.Preparable;
import com.koossery.adempiere.fe.util.KTDateUtil;
import com.koossery.adempiere.fe.util.KTUtil;
import com.koossery.adempiere.fe.actions.payroll.list.list.createModifyList.ListForm;
import org.koossery.adempiere.core.contract.dto.payroll.HR_ListDTO;
import org.koossery.adempiere.core.contract.interfaces.payroll.IHR_ListSVCO;
import java.math.BigDecimal;

public class CreateModifyListAction extends KTAdempiereBaseAction implements Preparable {

    private Logger logger = Logger.getLogger(CreateModifyListAction.class);

    private ListForm createModifyForm;

    private IHR_ListSVCO tableSVCO;

    private Object[] args;

    public CreateModifyListAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        tableSVCO = (IHR_ListSVCO) svcoFinder.get(FrontEndConstants.IHR_ListSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String init() {
        try {
            createModifyForm.setListOfOrgAllowed(getSession().getListOfOrgAllowed());
            createModifyForm.setNomOrg(getSession().getNomOrg());
            createModifyForm.setListOfClientAllowed(getSession().getListOfClientAllowed());
            createModifyForm.setNomClient(getSession().getNomClient());
            createModifyForm.listOfDepartementAllowed = new ArrayList<Departmentbean>();
            createModifyForm.listOfDepartementAllowed = getListDepartement();
            createModifyForm.listOfPayrollAllowed = new ArrayList<HR_PayrollBean>();
            createModifyForm.listOfTableTypeAllowed = new ArrayList<HR_ListTypeBean>();
            createModifyForm.listOfTableTypeAllowed = getListOfTableTypeAllowed();
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACList-004"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(message);
            return ERROR;
        }
    }

    private void createList() {
        try {
            HR_ListDTO listDTO = (HR_ListDTO) getMapper().map(createModifyForm.getTableBean(), HR_ListDTO.class);
            int id = tableSVCO.createHR_List(getSession().getCtx(), listDTO, null);
            if (id <= 0) {
                args = new Object[1];
                args[0] = createModifyForm.getTableBean().getName();
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACList-001"), args);
                logger.error(message);
                String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
                addActionError(humanMessage);
            } else {
                args = new Object[1];
                args[0] = createModifyForm.getTableBean().getName();
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACList-002"), args);
                addActionMessage(message);
                getSession().getCtx().put("HR_List_ID", id);
                getSession().getCtx().put("HR_List_Name", createModifyForm.getTableBean().getName());
            }
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
        } catch (Exception e) {
            args = new Object[3];
            args[0] = createModifyForm.getTableBean().getName();
            args[1] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[2] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACList-003"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
        }
    }

    private void modifyList() {
        try {
            HR_ListDTO listDTO = (HR_ListDTO) getMapper().map(createModifyForm.getTableBean(), HR_ListDTO.class);
            tableSVCO.updateHR_List(getSession().getCtx(), listDTO);
            args = new Object[1];
            args[0] = createModifyForm.getTableBean().getName();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMList-002"), args);
            addActionMessage(message);
            getSession().getCtx().put("Hr_List_ID", createModifyForm.getTableBean().getHr_List_ID());
            getSession().getCtx().put("Hr_List_Name", createModifyForm.getTableBean().getName());
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMList-003"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
        }
    }

    public String save() {
        try {
            getSession().getCtx().setProperty("#AD_Org_ID", String.valueOf(createModifyForm.getIdOrgSelected()));
            createModifyForm.getTableBean().setIsActive(KTUtil.booleanToString(createModifyForm.isActive()));
            createModifyForm.getTableBean().setIsEmployee(KTUtil.booleanToString(createModifyForm.isEmployee()));
            if ((createModifyForm.getValidFrom() != null)) {
                createModifyForm.getTableBean().setValidFrom(KTDateUtil.getTimestamp(createModifyForm.getValidFrom(), KTDateUtil.BIRTH_DATE1));
            }
            if (createModifyForm.getTableBean().getHr_List_ID() == 0) {
                createList();
            } else {
                modifyList();
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

    public ListForm getCreateModifyForm() {
        return (ListForm) getForm(ListForm.class.getName());
    }

    public void setCreateModifyForm(ListForm createModifyForm) {
        this.createModifyForm = createModifyForm;
    }
}
