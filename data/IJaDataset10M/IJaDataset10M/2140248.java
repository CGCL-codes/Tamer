package com.koossery.adempiere.fe.actions.hr.employee.employeeAcct.findEmployeeAcct;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.hr.HR_EmployeeCriteria;
import org.koossery.adempiere.core.contract.dto.hr.HR_EmployeeDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.hr.IHR_EmployeeSVCO;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.actions.hr.employee.createModifyEmployee.EmployeForm;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.humanResources.employe.EmployeeBean;
import com.koossery.adempiere.fe.util.KTUtil;

/**
 * @author Cedrick Essale
 *
 */
public class FindEmployeeAcctAction extends KTAdempiereBaseAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private IHR_EmployeeSVCO employeeSVCO;

    private Logger logger = Logger.getLogger(FindEmployeeAcctAction.class);

    private Object[] args = null;

    FindEmployeeAcctForm findForm;

    public FindEmployeeAcctAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        employeeSVCO = (IHR_EmployeeSVCO) this.svcoFinder.get(FrontEndConstants.IHR_EmployeeSVCO);
    }

    public String find() {
        try {
            HR_EmployeeCriteria employeeCriteria = new HR_EmployeeCriteria();
            int m;
            if (getSession().getCtx().get("#AD_Client_ID").toString().equals(null)) {
                m = 0;
            } else m = Integer.parseInt(getSession().getCtx().get("#AD_Client_ID").toString());
            employeeCriteria.setAd_Client_ID(m);
            int l;
            if (getSession().getCtx().get("#AD_Org_ID").toString().equals(null)) {
                l = 0;
            } else l = Integer.parseInt(getSession().getCtx().get("#AD_Org_ID").toString());
            employeeCriteria.setAd_Org_ID(l);
            ArrayList<HR_EmployeeDTO> listOfEmployeeDTO = employeeSVCO.findHR_Employee(getSession().getCtx(), employeeCriteria);
            if (listOfEmployeeDTO == null || listOfEmployeeDTO.size() == 0) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFE-002");
                addActionMessage(message);
                return SUCCESS;
            }
            findForm.setEmployeMap(new Hashtable<Integer, EmployeeBean>());
            findForm.setEmployeMap1(new Hashtable<Integer, EmployeeBean>());
            for (int i = 0; i < listOfEmployeeDTO.size(); i++) {
                EmployeeBean emp = (EmployeeBean) getMapper().map(listOfEmployeeDTO.get(i), EmployeeBean.class);
                findForm.getEmployeMap().put(emp.getHr_Employee_ID(), emp);
                findForm.getEmployeMap1().put(emp.getHr_Employee_ID(), emp);
            }
            findForm.setNomOrg(getSession().getNomOrg());
            findForm.setNomClient(getSession().getNomClient());
            findForm.setNomBPartner(getSession().getCtx().getProperty("C_BPartner_Name"));
            return SUCCESS;
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
            return ERROR;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFE-003"), args);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFE-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            EmployeForm createModifyForm = (EmployeForm) getForm(EmployeForm.class.toString());
            int idEmployeeSelected = findForm.getIdEmployeSelected();
            EmployeeBean employe = (EmployeeBean) findForm.getEmployeMap1().get(idEmployeeSelected);
            createModifyForm.setEmployeBean(employe);
            createModifyForm.setActive(KTUtil.stringToBoolean(employe.getIsActive()));
            createModifyForm.setFlag(1);
            createModifyForm.setNomOrg(getSession().getNomOrg());
            createModifyForm.setNomClient(getSession().getNomClient());
            ;
            createModifyForm.setDisplay(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAME-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindEmployeeAcctForm getFindForm() {
        return (FindEmployeeAcctForm) getForm(FindEmployeeAcctForm.class.getName());
    }

    public void setFindForm(FindEmployeeAcctForm findForm) {
        this.findForm = findForm;
    }
}
