package org.posterita.struts.pos;

import java.util.ArrayList;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.posterita.Constants;
import org.posterita.businesslogic.ProductManager;
import org.posterita.core.TmkJSPEnv;

public class SearchPOSProductAction extends POSDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward fwd = init(mapping, form, request, response);
        if (fwd != null) return fwd;
        String productName = request.getParameter("productName");
        Properties ctx = TmkJSPEnv.getCtx(request);
        ArrayList productList = ProductManager.getProductList(ctx, productName, true, true);
        request.getSession().setAttribute(Constants.PRODUCT_LIST, productList);
        return mapping.findForward("displayProducts");
    }
}
