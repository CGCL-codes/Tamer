package com.pr.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.vo.QueryTemplateVO;
import com.be.bo.GlobalParameter;
import com.be.bo.QueryParameterBO;
import com.be.bo.QueryParameterContainer;
import com.be.bo.QueryTemplateBO;
import com.be.bo.UserObject;
import com.pr.http.forms.JournalParameterForm;
import com.pr.param.*;

public class JournalParameterAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String paramIdentifier = GlobalParameter.paramPrJournal;
        JournalParameterForm fForm = (JournalParameterForm) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("param".equals(fForm.getAction())) {
            JournalParameter vo = new JournalParameter();
            if (fForm.getId() != null && !"".equals(fForm.getId())) vo.setId(fForm.getId());
            if (fForm.getOrderID() != null && !"".equals(fForm.getOrderID())) vo.setOrderID(fForm.getOrderID());
            if (fForm.getBookText() != null && !"".equals(fForm.getBookText())) vo.setBookText(fForm.getBookText());
            if (fForm.getBookDateFrom() != null && !"".equals(fForm.getBookDateFrom())) vo.setBookDateFrom(fForm.getBookDateFrom());
            if (fForm.getBookDateTo() != null && !"".equals(fForm.getBookDateTo())) vo.setBookDateTo(fForm.getBookDateTo());
            if (fForm.getValueDateFrom() != null && !"".equals(fForm.getValueDateFrom())) vo.setValueDateFrom(fForm.getValueDateFrom());
            if (fForm.getValueDateTo() != null && !"".equals(fForm.getValueDateTo())) vo.setValueDateTo(fForm.getValueDateTo());
            if (fForm.getAmount() != null && !"".equals(fForm.getAmount())) vo.setAmount(fForm.getAmount());
            if (fForm.getCurrencyID() != null && !"".equals(fForm.getCurrencyID())) vo.setCurrencyID(fForm.getCurrencyID());
            if (fForm.getCategoryID() != null && !"".equals(fForm.getCategoryID())) vo.setCategoryID(fForm.getCategoryID());
            if (fForm.getEmployeeID() != null && !"".equals(fForm.getEmployeeID())) vo.setEmployeeID(fForm.getEmployeeID());
            if (fForm.getSearchString() != null && !"".equals(fForm.getSearchString())) vo.setSearchString(fForm.getSearchString());
            uo.setParameter(paramIdentifier, vo);
        }
        if (request.getParameter("saveTemplate") != null) {
            String templateName = request.getParameter("templateName");
            QueryTemplateBO bo = new QueryTemplateBO(uo.getConnectionPropertiesVO(), uo);
            QueryTemplateVO vo = bo.insertQueryTemplateVO(templateName, paramIdentifier);
            QueryParameterBO qpBO = new QueryParameterBO(uo.getConnectionPropertiesVO(), uo);
            QueryParameterContainer qpPC = new QueryParameterContainer(vo.getId());
            if (fForm.getId() != null && !"".equals(fForm.getId())) qpPC.addParameter("id", "Text", fForm.getId());
            if (fForm.getOrderID() != null && !"".equals(fForm.getOrderID())) qpPC.addParameter("orderID", "Text", fForm.getOrderID());
            if (fForm.getBookText() != null && !"".equals(fForm.getBookText())) qpPC.addParameter("bookText", "Text", fForm.getBookText());
            if (fForm.getBookDateFrom() != null && !"".equals(fForm.getBookDateFrom())) qpPC.addParameter("bookDateFrom", "Text", fForm.getBookDateFrom());
            if (fForm.getBookDateTo() != null && !"".equals(fForm.getBookDateTo())) qpPC.addParameter("bookDateTo", "Text", fForm.getBookDateTo());
            if (fForm.getValueDateFrom() != null && !"".equals(fForm.getValueDateFrom())) qpPC.addParameter("valueDateFrom", "Text", fForm.getValueDateFrom());
            if (fForm.getValueDateTo() != null && !"".equals(fForm.getValueDateTo())) qpPC.addParameter("valueDateTo", "Text", fForm.getValueDateTo());
            if (fForm.getAmount() != null && !"".equals(fForm.getAmount())) qpPC.addParameter("amount", "Text", fForm.getAmount());
            if (fForm.getCurrencyID() != null && !"".equals(fForm.getCurrencyID())) qpPC.addParameter("currencyID", "Text", fForm.getCurrencyID());
            if (fForm.getCategoryID() != null && !"".equals(fForm.getCategoryID())) qpPC.addParameter("categoryID", "Text", fForm.getCategoryID());
            if (fForm.getEmployeeID() != null && !"".equals(fForm.getEmployeeID())) qpPC.addParameter("employeeID", "Text", fForm.getEmployeeID());
            if (fForm.getSearchString() != null && !"".equals(fForm.getSearchString())) qpPC.addParameter("searchString", "Text", fForm.getSearchString());
            qpBO.insertParameter(qpPC.getParameterArray());
            return (mapping.findForward("templatesaved"));
        } else if (request.getParameter("deleteTemplate") != null) {
            String templateName = request.getParameter("templateName");
            QueryTemplateBO bo = new QueryTemplateBO(uo.getConnectionPropertiesVO(), uo);
            QueryTemplateVO[] vo = bo.getQueryTemplateVO(templateName, paramIdentifier);
            if (vo != null && vo.length == 1) {
                try {
                    uo.log("JournalParameterAction.execute(): " + vo[0].getId(), GlobalParameter.logTypeSQLDelete);
                    bo.deleteQueryTemplateVO((int) vo[0].getId());
                } catch (NumberFormatException e) {
                    uo.log("JournalParameterAction.execute(): " + e, GlobalParameter.logTypeException);
                }
            }
            return (mapping.findForward("templatedeleted"));
        }
        return (mapping.findForward("success"));
    }
}
