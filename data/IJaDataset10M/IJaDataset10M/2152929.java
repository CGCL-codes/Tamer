package org.techytax.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.techytax.dao.BookValueDao;
import org.techytax.domain.BalanceType;
import org.techytax.domain.BookValue;
import org.techytax.domain.User;
import org.techytax.struts.form.BookValueForm;

public class InsertBookValueAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookValueForm bookValueForm = (BookValueForm) form;
        BookValue bookValue = new BookValue();
        bookValue.setId(bookValueForm.getId());
        bookValue.setSaldo(bookValueForm.getSaldo());
        bookValue.setBalanceType(BalanceType.valueOf(bookValueForm.getBalanceType()));
        bookValue.setJaar(bookValueForm.getJaar());
        User user = (User) request.getSession().getAttribute("user");
        bookValue.setUserId(user.getId());
        BookValueDao bookValueDao = new BookValueDao();
        bookValueDao.insertBookValue(bookValue);
        request.getSession().removeAttribute("overview");
        return mapping.findForward("success");
    }
}
