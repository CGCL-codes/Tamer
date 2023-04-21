package com.koossery.adempiere.fe.actions.systemRule.task.findTask;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.task.AD_TaskCriteria;
import org.koossery.adempiere.core.contract.dto.task.AD_TaskDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.task.IAD_TaskSVCO;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.actions.systemRule.task.createModifyTask.TaskForm;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.systemRules.AD_TaskBean;
import com.koossery.adempiere.fe.util.KTUtil;
import com.opensymphony.xwork2.Preparable;

public class FindTaskAction extends KTAdempiereBaseAction implements Preparable {

    private Logger logger = Logger.getLogger(FindTaskAction.class);

    private FindTaskForm findForm;

    private IAD_TaskSVCO taskSVCO;

    private Object[] args;

    public FindTaskAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        taskSVCO = (IAD_TaskSVCO) svcoFinder.get(FrontEndConstants.IAD_TaskSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String find() {
        try {
            AD_TaskCriteria ctr = (AD_TaskCriteria) getMapper().map(findForm.getTaskBean(), AD_TaskCriteria.class);
            ArrayList<AD_TaskDTO> list = new ArrayList<AD_TaskDTO>();
            findForm.setTaskMap(new HashMap<Integer, AD_TaskBean>());
            findForm.setTaskMap1(new HashMap<Integer, AD_TaskBean>());
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
            list = taskSVCO.findAD_Task(getSession().getCtx(), ctr);
            if (list.size() <= 0 || list == null) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFTask-002");
                addActionMessage(message);
                return SUCCESS;
            } else {
                for (int i = 0; i < list.size(); i++) {
                    AD_TaskBean taskBean = (AD_TaskBean) getMapper().map(list.get(i), AD_TaskBean.class);
                    findForm.getTaskMap().put(list.get(i).getAd_Task_ID(), taskBean);
                    findForm.getTaskMap1().put(list.get(i).getAd_Task_ID(), taskBean);
                }
                findForm.setNomClient(getSession().getNomClient());
                findForm.setNomOrg(getSession().getNomOrg());
                return SUCCESS;
            }
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
            return ERROR;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFTask-003"), args);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFTask-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            TaskForm createModifyForm = (TaskForm) getForm(TaskForm.class.getName());
            int idTask = findForm.getIdTaskSelected();
            AD_TaskBean taskBean = (AD_TaskBean) getFindForm().getTaskMap1().get(idTask);
            createModifyForm.setTaskBean(taskBean);
            createModifyForm.setActive(KTUtil.stringToBoolean(taskBean.getIsActive()));
            createModifyForm.getTaskBean().setIsServerProcess(KTUtil.booleanToString(createModifyForm.isServerProcess()));
            createModifyForm.setDisplay(1);
            createModifyForm.setFlag(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMTask-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindTaskForm getFindForm() {
        return (FindTaskForm) getForm(FindTaskForm.class.getName());
    }

    public void setFindForm(FindTaskForm findForm) {
        this.findForm = findForm;
    }
}
