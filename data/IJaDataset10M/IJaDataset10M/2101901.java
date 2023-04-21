package com.hisham.parking.permits.web.touchnet;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hisham.parking.web.*;
import com.hisham.creditcard.touchnet.util.*;
import com.hisham.transaction.*;
import org.apache.struts.actions.*;
import com.hisham.creditcard.CcTransactionItemInfoList;
import com.hisham.parking.permits.touchnet.TnPsTransactionModerator;
import com.hisham.parking.permitsales.PsMenu;
import com.hisham.parking.permitsales.PsTransactionItemInfo;
import com.hisham.parking.permitsales.PsTransactionItemInfoList;
import com.hisham.parking.permitsales.web.PsTransactionAction;
import com.hisham.parking.permitsales.web.PsTransactionInfoForm;
import com.hisham.parking.sql.DbGeneralAccess;
import com.hisham.creditcard.CcTransactionItemInfo;
import com.hisham.powerpark.util.PermitSeriesInfo;

public class TnPsAction extends MappingDispatchAction {

    /**
	 * Logger for this class
	 */
    private static final Logger LOG = Logger.getLogger(TnPsAction.class);

    public ActionForward buyPermits(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PsTransactionInfoForm psForm = (PsTransactionInfoForm) form;
        if (psForm.getUserRequest().equals(PsTransactionAction.DISPLAY_PERMITS)) {
            psForm.setUserRequest("");
            ActionForward actionForward = this.findPermitsAvailable(mapping, psForm.getPermitSalesMenu(), request, response);
            return actionForward;
        }
        ActionMessages errors;
        if (psForm.getPsTransactionItemInfos().size() > 0) {
            errors = initPsTransactionInfo(psForm);
            if (errors.isEmpty()) {
                psForm.setUserRequest("");
                TnTransactionResources tnTransactionResources = TnTransactionResources.getInstance();
                errors = tnTransactionResources.allOk();
                if (errors.isEmpty()) {
                    request.setAttribute("UPAY_SITE_ID", tnTransactionResources.getUpaySiteId());
                    String transactionId = psForm.getPsTransactionItemInfos().getPermitSeriesIds().toString();
                    request.setAttribute("EXT_TRANS_ID", transactionId);
                    request.setAttribute("EXT_TRANS_ID_LABEL", "Permit Sale");
                    request.setAttribute("VALIDATION_KEY", tnTransactionResources.getValidationKey(transactionId, psForm.getPsTransactionInfo().getTransactionAmount()));
                    request.setAttribute("AMT", psForm.getPsTransactionInfo().getTransactionAmount());
                    return mapping.findForward(ActionConstants.payTouchnet);
                }
            }
        } else {
            errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.citations.items.required"));
        }
        this.saveErrors(request, errors);
        return this.findPermitsAvailable(mapping, psForm.getPermitSalesMenu(), request, response);
    }

    /**
	 * Note that the Action form passed for this method is considered to be
	 * CitationInfoForm.
	 * Find details of payable citation based on citation numbers or
	 * license plate provided.
	 * @param mapping ActionMapping
	 * @param form ActionForm CitationInfoForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 */
    protected ActionForward findPermitsAvailable(ActionMapping mapping, PsMenu psMenu, HttpServletRequest request, HttpServletResponse response) {
        ActionMessages errors = new ActionMessages();
        if (errors.isEmpty()) {
            return mapping.findForward(ActionConstants.choosePermit);
        } else {
            this.saveErrors(request, errors);
            return mapping.getInputForward();
        }
    }

    /**
	 * @todo for now we assume that any transaction posted is being posted here
	 * from touchnet and the citations are always paid in full, meanin
	 * we don't need to reconcile the amount paid at touchnet before
	 * marking the citation as paid
	 *
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 */
    public ActionForward processTouchnetPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        TnPsInfoForm psForm = (TnPsInfoForm) form;
        if (request.getRemoteAddr().startsWith(TnTransactionResources.getInstance().getRemoteAddrStart())) {
            if (psForm.getTnPsTransactionResponse().getCcTransactionResponse().isApprovedTransaction()) {
                psForm.setIpAddress(request.getRemoteAddr());
                ActionMessages errors = initPsTransactionInfo(psForm);
                if (errors.isEmpty()) {
                    if (psForm.getTnPsTransactionResponse().getCcTransactionResponse().getCcTransactionAmount() == psForm.getPsTransactionInfo().getTransactionAmount()) {
                        TnPsTransactionModerator moderator = new TnPsTransactionModerator(psForm.getTnPsTransactionInfo(), psForm.getTnPsTransactionResponse());
                        ITransactionResponse transactionResponse = moderator.processTransaction();
                        if (!transactionResponse.getErrors().isEmpty()) {
                            System.out.println("Transaction from touchnet could not be completed - transactionID: " + psForm.getOrderNumber());
                        }
                        this.saveErrors(request, transactionResponse.getErrors());
                    } else {
                        LOG.warn("Transaction from touchnet could not be completed - transactionID: " + psForm.getOrderNumber() + ": Amount to be paid: " + psForm.getPsTransactionInfo().getTransactionAmount() + ", Amount paid: " + psForm.getPmt_amt());
                    }
                } else {
                    LOG.error("Transaction from touchnet could not be completed - transactionID: " + psForm.getOrderNumber() + ": Error: " + errors.toString());
                    this.saveErrors(request, errors);
                }
            }
        } else {
            LOG.error("Illegal attempt for posting detected:");
            LOG.error(java.text.DateFormat.getInstance().format(java.util.Calendar.getInstance().getTime()) + "-" + request.getRemoteAddr() + "-" + request.getRemoteHost() + "-" + psForm.getPmt_status() + "-" + psForm.getPmt_amt() + "-" + psForm.getOrderNumber() + "-" + psForm.getPsTransactionItemInfos().getPermitSeriesIds().toString());
        }
        return mapping.findForward(ActionConstants.displayTransactionResponse);
    }

    /**
	 * This method prepares the citations chosen for payment. i.e., it
	 * grabs the citations balances for the database and puts the citation number
	 * and citation balance in the payment form.
	 * sets balances for the citations
	 * accordingly
	 *
	 * @param psForm CitationPaymentInfoForm
	 */
    private static ActionMessages initPsTransactionInfo(PsTransactionInfoForm psForm) {
        PsTransactionItemInfoList psTransactionItemInfos = psForm.getPsTransactionInfo().getPsTransactionItemInfos();
        CcTransactionItemInfoList ccTransactionInfos = psForm.getPsTransactionInfo().getCcTransactionInfo().getCcTransactionItemInfos();
        DbGeneralAccess db = new DbGeneralAccess();
        PsTransactionItemInfo psTransactionItemInfo;
        CcTransactionItemInfo ccTransactionItemInfo;
        PermitSeriesInfo permitSeriesInfo;
        ActionMessages errors = new ActionMessages();
        for (int i = 0; i < psTransactionItemInfos.size(); i++) {
            psTransactionItemInfo = psTransactionItemInfos.getPsTransactionItemInfo(i);
            ccTransactionItemInfo = ccTransactionInfos.getCcTransactionItemInfo(i);
            permitSeriesInfo = db.getPermitSeriesInfo(psTransactionItemInfos.getPsTransactionItemInfo(i).getPermitSeriesId());
            psTransactionItemInfo.setItemPrice(permitSeriesInfo.getPermitSeriesFee());
            ccTransactionItemInfo.setItemId("" + permitSeriesInfo.getPermitSeriesId());
            if (permitSeriesInfo.getPermitSeriesFee() > 0) {
                ccTransactionItemInfo.setItemPrice(permitSeriesInfo.getPermitSeriesFee());
            } else {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.citations.citation.amount.due", permitSeriesInfo.getPermitSeriesFee()));
            }
        }
        return errors;
    }
}
