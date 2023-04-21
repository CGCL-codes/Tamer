package com.shenming.sms.struts.action;

import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.shenming.sms.dc.manager.ProductManager;
import com.shenming.sms.module.hibernateOrm.SmTbProduct;
import com.shenming.sms.struts.form.ProductForm;
import com.shenming.sms.struts.form.QueryProductForm;

public class QueryProductAction extends Action {

    private static final String SUCCESS = "success";

    private QueryProductForm prdForm;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prdForm = (QueryProductForm) form;
        String queryType = prdForm.getFormType();
        if (QueryProductForm.QUERY.equalsIgnoreCase(queryType)) {
            return doQuery(mapping, form, request, response);
        } else if (QueryProductForm.QUERY_ALL.equalsIgnoreCase(queryType)) {
            return doQueryAll(mapping, form, request, response);
        } else if (QueryProductForm.QUERY_GRP_PRD.equalsIgnoreCase(queryType)) {
            return doQueryGrpPrd(mapping, form, request, response);
        }
        request.setAttribute("queryProductForm", prdForm);
        return mapping.findForward(SUCCESS);
    }

    private ActionForward doQueryGrpPrd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        TreeMap<Integer, String> prdMap = ProductManager.retrieveProductsMapByGrpId(prdForm.getPrdGroupId());
        System.out.println("==>" + prdMap);
        prdForm.setPrdMap(prdMap);
        return mapping.findForward(SUCCESS);
    }

    /**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
    private ActionForward doQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        List<SmTbProduct> products = ProductManager.retrieveProducts(new Integer((prdForm.getName() != null && !"".equalsIgnoreCase(prdForm.getName())) ? prdForm.getName() : "0").intValue(), prdForm.getPrdGroupId(), null, prdForm.getPriceMin(), prdForm.getPriceMax(), 0, 0, prdForm.getSuplyStatus(), null, null, null, 0);
        prdForm.setProducts(products);
        return mapping.findForward(SUCCESS);
    }

    private ActionForward doQueryAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        List<SmTbProduct> products = ProductManager.retrieveAllProducts();
        prdForm.setProducts(products);
        return mapping.findForward(SUCCESS);
    }
}
