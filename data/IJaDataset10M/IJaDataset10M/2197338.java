package com.koossery.adempiere.fe.actions.accounting.glJournal.journalBatch.findJournalBatch;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.generated.GL_JournalBatchCriteria;
import org.koossery.adempiere.core.contract.dto.generated.GL_JournalBatchDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IGL_JournalBatchSVCO;
import com.koossery.adempiere.fe.actions.accounting.glJournal.journalBatch.JournalBatchForm;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.accounting.glJournal.journalBatch.JournalBatchBean;
import com.koossery.adempiere.fe.util.KTDateUtil;
import com.opensymphony.xwork2.Preparable;

/**
 * Action qui permet d'afficher la vue de refind d'un JournalBatch et qui
 * permet aussi d'effectuer la refind de Journaux en lot
 * @version 1.0
 * @created 25-ao�t-2008 16:50:08
 */
public class FindJournalBatchAction extends KTAdempiereBaseAction implements Preparable {

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FindJournalBatchAction.class);

    private IGL_JournalBatchSVCO journalBatchSVCO;

    private FindJournalBatchForm findForm;

    private Object[] args;

    public FindJournalBatchAction(KTAdempiereSVCOFinder finder) throws Exception {
        svcoFinder = finder;
        journalBatchSVCO = (IGL_JournalBatchSVCO) svcoFinder.get(FrontEndConstants.IJournalBatchSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String findJournalBatch() {
        try {
            GL_JournalBatchCriteria ctr = (GL_JournalBatchCriteria) getMapper().map(findForm.getJournalBatchBean(), GL_JournalBatchCriteria.class);
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
            ArrayList<GL_JournalBatchDTO> list = new ArrayList<GL_JournalBatchDTO>();
            findForm.setJournalBatchMap(new HashMap<Integer, JournalBatchBean>());
            list = journalBatchSVCO.findGL_JournalBatch(getSession().getCtx(), ctr);
            if (list.size() <= 0 || list == null) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFJB-002");
                addActionMessage(message);
                return SUCCESS;
            } else {
                findForm.setJournalBatchMap1(new HashMap<Integer, JournalBatchBean>());
                for (int i = 0; i < list.size(); i++) {
                    JournalBatchBean journalBatchBean = (JournalBatchBean) getMapper().map(list.get(i), JournalBatchBean.class);
                    journalBatchBean.setDateAcc(KTDateUtil.convertTimeStampToBirthDate(journalBatchBean.getDateAcct()));
                    journalBatchBean.setDatedoc(KTDateUtil.convertTimeStampToBirthDate(journalBatchBean.getDateDoc()));
                    findForm.getJournalBatchMap().put(list.get(i).getGl_JournalBatch_ID(), journalBatchBean);
                    findForm.getJournalBatchMap1().put(list.get(i).getGl_JournalBatch_ID(), journalBatchBean);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFJB-003"), args);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFJB-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            JournalBatchForm createModifyForm = (JournalBatchForm) getForm(JournalBatchForm.class.getName());
            int idJournalBatch = findForm.getIdJournalBatchSelected();
            JournalBatchBean journalBatchBean = (JournalBatchBean) findForm.getJournalBatchMap1().get(idJournalBatch);
            createModifyForm.setJournalBatchBean(journalBatchBean);
            getSession().getCtx().put("Gl_JournalBatch_ID", createModifyForm.getJournalBatchBean().getGl_JournalBatch_ID());
            getSession().getCtx().put("Gl_JournalBatch_Description", createModifyForm.getJournalBatchBean().getDescription());
            createModifyForm.setDisplay(1);
            createModifyForm.setFlag(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMJB-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindJournalBatchForm getFindForm() {
        return (FindJournalBatchForm) getForm(FindJournalBatchForm.class.getName());
    }

    public void setFindForm(FindJournalBatchForm findForm) {
        this.findForm = findForm;
    }
}
