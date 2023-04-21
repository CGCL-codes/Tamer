package com.koossery.adempiere.fe.actions.accounting.glJournal.journal.findJournal;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.criteria.generated.GL_JournalCriteria;
import org.koossery.adempiere.core.contract.dto.generated.GL_JournalDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IGL_JournalSVCO;
import com.koossery.adempiere.fe.actions.accounting.glJournal.journal.JournalForm;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.accounting.glJournal.journal.JournalBean;
import com.koossery.adempiere.fe.util.KTDateUtil;
import com.opensymphony.xwork2.Preparable;

/**
 * Action qui permet d'afficher la vue de refind d'un Journal et qui permet
 * aussi d'effectuer la refind de Journaux
 * @version 1.0
 * @created 25-ao�t-2008 16:50:08
 */
public class FindJournalAction extends KTAdempiereBaseAction implements Preparable {

    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FindJournalAction.class);

    private IGL_JournalSVCO journalSVCO;

    private FindJournalForm findForm;

    private Object[] args;

    public FindJournalAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        journalSVCO = (IGL_JournalSVCO) svcoFinder.get(FrontEndConstants.IJournalSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String findJournal() {
        try {
            GL_JournalCriteria ctr = (GL_JournalCriteria) getMapper().map(findForm.getJournalBean(), GL_JournalCriteria.class);
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
            ArrayList<GL_JournalDTO> list = new ArrayList<GL_JournalDTO>();
            findForm.setJournalMap(new HashMap<Integer, JournalBean>());
            list = journalSVCO.findGL_Journal(getSession().getCtx(), ctr);
            if (list.size() <= 0 || list == null) {
                String message = FrontEndConstants.getMessageCtx().getProperty("KTAFJ-002");
                addActionMessage(message);
                return SUCCESS;
            } else {
                findForm.setJournalMap1(new HashMap<Integer, JournalBean>());
                for (int i = 0; i < list.size(); i++) {
                    JournalBean journalBean = (JournalBean) getMapper().map(list.get(i), JournalBean.class);
                    journalBean.setDateAcc(KTDateUtil.convertTimeStampToBirthDate(journalBean.getDateAcct()));
                    journalBean.setDatedoc(KTDateUtil.convertTimeStampToBirthDate(journalBean.getDateDoc()));
                    findForm.getJournalMap().put(list.get(i).getGl_Journal_ID(), journalBean);
                    findForm.getJournalMap1().put(list.get(i).getGl_Journal_ID(), journalBean);
                }
                findForm.setNomBatch(getSession().getCtx().getProperty("Gl_JournalBatch_Description"));
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFJ-003"), args);
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
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAFJ-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public String launchModify() {
        try {
            JournalForm createModifyForm = (JournalForm) getForm(JournalForm.class.getName());
            int idJournal = findForm.getIdJournalSelected();
            JournalBean journalBean = (JournalBean) findForm.getJournalMap1().get(idJournal);
            createModifyForm.setJournalBean(journalBean);
            getSession().getCtx().put("Gl_Journal_ID", createModifyForm.getJournalBean().getGl_Journal_ID());
            getSession().getCtx().put("Gl_Journal_Description", createModifyForm.getJournalBean().getDescription());
            createModifyForm.setDisplay(1);
            createModifyForm.setFlag(1);
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAMJ-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
            return ERROR;
        }
    }

    public FindJournalForm getFindForm() {
        return (FindJournalForm) getForm(FindJournalForm.class.getName());
    }

    public void setFindForm(FindJournalForm findForm) {
        this.findForm = findForm;
    }
}
