package com.ecomponentes.formularios.mailing.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ecomponentes.formularios.mailing.bo.MailingBO;
import com.ecomponentes.formularios.mailing.from.MailingListForm;

public class MailingListAction extends Action {

    /**
	 * Method loads book from DB
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MailingListForm mailingListForm = new MailingListForm();
        mailingListForm = (MailingListForm) form;
        MailingBO mailingBO = new MailingBO();
        if (mailingListForm.getCampoBusca() != null && !mailingListForm.getCampoBusca().equals("")) {
            mailingListForm.setMailingTOs(mailingBO.getMailings(mailingListForm.getCampoBusca(), mailingListForm.getValorBusca()));
        } else {
            mailingListForm.setMailingTOs(mailingBO.selecionarTodos());
        }
        return mapping.findForward("showList");
    }
}
