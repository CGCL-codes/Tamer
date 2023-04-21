package com.koossery.adempiere.fe.actions.Requisition_to_invoice.RfQ.RfQ.createModifyRfQ;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.koossery.adempiere.core.contract.KTAdempiereSVCOFinder;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ.C_RfQDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.Requisition_to_invoice.RfQ.IC_RfQSVCO;
import com.koossery.adempiere.fe.actions.Requisition_to_invoice.RfQ_Topic.topic.createModifyTopic.CreateModifyTopicAction;
import com.koossery.adempiere.fe.actions.base.KTAdempiereBaseAction;
import com.koossery.adempiere.fe.beans.FrontEndConstants;
import com.koossery.adempiere.fe.beans.Requisition_to_invoice.RfQ_Topic.C_RfQ_TopicBean;
import com.koossery.adempiere.fe.beans.accounting.currency.CurrencyBean;
import com.koossery.adempiere.fe.beans.clientRules.AD_ClientBean;
import com.koossery.adempiere.fe.beans.humanResources.C_BPartner_LocationBean;
import com.koossery.adempiere.fe.beans.order.C_OrderBean;
import com.koossery.adempiere.fe.beans.referentiel.org.OrgBean;
import com.koossery.adempiere.fe.beans.security.user.AD_UserBean;
import com.koossery.adempiere.fe.beans.util.AD_Ref_ListBean;
import com.koossery.adempiere.fe.beans.util.BPartnerBean;
import com.koossery.adempiere.fe.util.KTDateUtil;
import com.koossery.adempiere.fe.util.KTUtil;
import com.opensymphony.xwork2.Preparable;

public class CreateModifyRfQAction extends KTAdempiereBaseAction implements Preparable {

    private static final long serialVersionUID = 1L;

    protected Object[] args = null;

    private IC_RfQSVCO rfQSVCO;

    private Logger logger = Logger.getLogger(CreateModifyTopicAction.class);

    public CreateModifyRfQAction(KTAdempiereSVCOFinder finder) throws Exception {
        this.svcoFinder = finder;
        this.rfQSVCO = (IC_RfQSVCO) this.svcoFinder.get(FrontEndConstants.IC_RfQSVCO);
    }

    public void finalize() throws Throwable {
    }

    public String save() {
        try {
            getSession().getCtx().setProperty("#AD_Org_ID", String.valueOf(getCreateModifyForm().getC_RfQDTO().getAd_Org_ID()));
            getCreateModifyForm().getC_RfQDTO().setIsActive(KTUtil.booleanToString(getCreateModifyForm().isActive()));
            System.out.println("IsActive==" + getCreateModifyForm().isActive());
            getCreateModifyForm().getC_RfQDTO().setIsSelfService(KTUtil.booleanToString(getCreateModifyForm().isSelfService()));
            getCreateModifyForm().getC_RfQDTO().setIsInvitedVendorsOnly(KTUtil.booleanToString(getCreateModifyForm().isSelfService()));
            getCreateModifyForm().getC_RfQDTO().setIsRfQResponseAccepted(KTUtil.booleanToString(getCreateModifyForm().isSelfService()));
            getCreateModifyForm().getC_RfQDTO().setIsProcessed(KTUtil.booleanToString(getCreateModifyForm().isSelfService()));
            getCreateModifyForm().getC_RfQDTO().setDateResponse(KTDateUtil.getTimestamp(getCreateModifyForm().getDateResponse(), KTDateUtil.BIRTH_DATE1));
            getCreateModifyForm().getC_RfQDTO().setDateWorkStart(KTDateUtil.getTimestamp(getCreateModifyForm().getDateWorkStart(), KTDateUtil.BIRTH_DATE1));
            getCreateModifyForm().getC_RfQDTO().setDateWorkComplete(KTDateUtil.getTimestamp(getCreateModifyForm().getDateWorkComplete(), KTDateUtil.BIRTH_DATE1));
            if (getCreateModifyForm().getC_RfQDTO().getC_RfQ_ID() == 0) {
                createRfQ();
            } else {
                modifyRfQ();
            }
            return SUCCESS;
        } catch (Exception e) {
            addActionError(e.getMessage());
            return ERROR;
        }
    }

    public void createRfQ() {
        try {
            int idrfq = rfQSVCO.createC_RfQ(getSession().getCtx(), getCreateModifyForm().getC_RfQDTO(), null);
            if (idrfq <= 0) {
                args = new Object[1];
                args[0] = getCreateModifyForm().getC_RfQDTO().getName();
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACRfQ-001"), args);
                logger.error(message);
                String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
                addActionError(humanMessage);
            } else {
                args = new Object[1];
                args[0] = getCreateModifyForm().getC_RfQDTO().getName();
                String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACRfQ-002"), args);
                addActionMessage(message);
                getSession().getCtx().put("C_RfQ_ID", idrfq);
                getSession().getCtx().put("C_RfQ_DocNo", getCreateModifyForm().getC_RfQDTO().getDocumentNo());
            }
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
        } catch (Exception e) {
            args = new Object[3];
            args[0] = getCreateModifyForm().getC_RfQDTO().getName();
            args[1] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[2] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACRfQ-003"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
        }
    }

    public void modifyRfQ() {
        try {
            rfQSVCO.updateC_RfQ(getSession().getCtx(), getCreateModifyForm().getC_RfQDTO());
            getSession().getCtx().put("C_RfQ_ID", getCreateModifyForm().getC_RfQDTO().getC_RfQ_ID());
            getSession().getCtx().put("C_RfQ_DocNo", getCreateModifyForm().getC_RfQDTO().getDocumentNo());
            args = new Object[1];
            args[0] = getCreateModifyForm().getC_RfQDTO().getName();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAURfQ-002"), args);
            addActionMessage(message);
        } catch (KTAdempiereException e) {
            addActionError(e.getHumanMessage());
        } catch (Exception e) {
            args = new Object[2];
            args[1] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[2] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTAURfQ-002"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(humanMessage);
        }
    }

    public String init() {
        try {
            if (getCreateModifyForm().INITIALISE_ON_INIT == true) {
                getCreateModifyForm().setListOfOrgAllowed(new ArrayList<OrgBean>());
                getCreateModifyForm().setListOfOrgAllowed(getSession().getListOfOrgAllowed());
                getCreateModifyForm().setListOfClientAllowed(new ArrayList<AD_ClientBean>());
                getCreateModifyForm().setListOfClientAllowed(getSession().getListOfClientAllowed());
                getCreateModifyForm().setNomClient(getSession().getNomClient());
                getCreateModifyForm().setNomOrg(getSession().getNomOrg());
                getCreateModifyForm().setListOfSalesRepresentative(new ArrayList<AD_UserBean>());
                getCreateModifyForm().setListOfSalesRepresentative(getListOfUserAllowed());
                getCreateModifyForm().setListOfTopic(new ArrayList<C_RfQ_TopicBean>());
                getCreateModifyForm().setListOfTopic(getListOfRfQ_Topic());
                getCreateModifyForm().setListOfRfQ_Type(new ArrayList<AD_Ref_ListBean>());
                getCreateModifyForm().setListOfRfQ_Type(getListOfRfQType());
                getCreateModifyForm().setListOfCurrency(new ArrayList<CurrencyBean>());
                getCreateModifyForm().setListOfCurrency(getListOfCurrency());
                getCreateModifyForm().setListOfBPartner(new ArrayList<BPartnerBean>());
                getCreateModifyForm().setListOfBPartner(getListOfBPartnerAllowed());
                getCreateModifyForm().setListOfPartnerLocation(new ArrayList<C_BPartner_LocationBean>());
                getCreateModifyForm().setListOfPartnerLocation(getListOfPartnerLocation());
                getCreateModifyForm().setListOfUserContact(new ArrayList<AD_UserBean>());
                getCreateModifyForm().setListOfUserContact(getListOfUserContactAllowed());
                getCreateModifyForm().setListOfOrder(new ArrayList<C_OrderBean>());
                getCreateModifyForm().setListOfOrder(getListOfOrderAllowed());
            }
            return SUCCESS;
        } catch (Exception e) {
            args = new Object[2];
            args[0] = FrontEndConstants.DETAILS_TECHNIQUES;
            args[1] = e.getMessage();
            String message = String.format(FrontEndConstants.getMessageCtx().getProperty("KTACRfQ-001"), args);
            logger.error(message);
            String humanMessage = message.substring(0, message.indexOf(FrontEndConstants.DETAILS_TECHNIQUES));
            addActionError(message);
            return ERROR;
        }
    }

    public RfQForm getCreateModifyForm() {
        return (RfQForm) getForm(RfQForm.class.getName());
    }
}
