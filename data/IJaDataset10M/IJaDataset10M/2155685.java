package com.debitors.http.actions;

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
import com.debitors.http.forms.OrderItemBatchParameterForm;
import com.debitors.param.*;

public class OrderItemBatchParameterAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String paramIdentifier = GlobalParameter.paramDebitorsOrderItemBatch;
        OrderItemBatchParameterForm fForm = (OrderItemBatchParameterForm) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("param".equals(fForm.getAction())) {
            OrderItemBatchParameter vo = new OrderItemBatchParameter();
            if (fForm.getId() != null && !"".equals(fForm.getId())) vo.setId(fForm.getId());
            if (fForm.getOrderID() != null && !"".equals(fForm.getOrderID())) vo.setOrderID(fForm.getOrderID());
            if (fForm.getItemID() != null && !"".equals(fForm.getItemID())) vo.setItemID(fForm.getItemID());
            if (fForm.getItemDetailID() != null && !"".equals(fForm.getItemDetailID())) vo.setItemDetailID(fForm.getItemDetailID());
            if (fForm.getItemBatchNr() != null && !"".equals(fForm.getItemBatchNr())) vo.setItemBatchNr(fForm.getItemBatchNr());
            if (fForm.getBatchStart() != null && !"".equals(fForm.getBatchStart())) vo.setBatchStart(fForm.getBatchStart());
            if (fForm.getBatchEnd() != null && !"".equals(fForm.getBatchEnd())) vo.setBatchEnd(fForm.getBatchEnd());
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
            if (fForm.getItemID() != null && !"".equals(fForm.getItemID())) qpPC.addParameter("itemID", "Text", fForm.getItemID());
            if (fForm.getItemDetailID() != null && !"".equals(fForm.getItemDetailID())) qpPC.addParameter("itemDetailID", "Text", fForm.getItemDetailID());
            if (fForm.getItemBatchNr() != null && !"".equals(fForm.getItemBatchNr())) qpPC.addParameter("itemBatchNr", "Text", fForm.getItemBatchNr());
            if (fForm.getBatchStart() != null && !"".equals(fForm.getBatchStart())) qpPC.addParameter("batchStart", "Text", fForm.getBatchStart());
            if (fForm.getBatchEnd() != null && !"".equals(fForm.getBatchEnd())) qpPC.addParameter("batchEnd", "Text", fForm.getBatchEnd());
            if (fForm.getSearchString() != null && !"".equals(fForm.getSearchString())) qpPC.addParameter("searchString", "Text", fForm.getSearchString());
            qpBO.insertParameter(qpPC.getParameterArray());
            return (mapping.findForward("templatesaved"));
        } else if (request.getParameter("deleteTemplate") != null) {
            String templateName = request.getParameter("templateName");
            QueryTemplateBO bo = new QueryTemplateBO(uo.getConnectionPropertiesVO(), uo);
            QueryTemplateVO[] vo = bo.getQueryTemplateVO(templateName, paramIdentifier);
            if (vo != null && vo.length == 1) {
                try {
                    uo.log("OrderItemBatchParameterAction.execute(): " + vo[0].getId(), GlobalParameter.logTypeSQLDelete);
                    bo.deleteQueryTemplateVO((int) vo[0].getId());
                } catch (NumberFormatException e) {
                    uo.log("OrderItemBatchParameterAction.execute(): " + e, GlobalParameter.logTypeException);
                }
            }
            return (mapping.findForward("templatedeleted"));
        }
        return (mapping.findForward("success"));
    }
}
