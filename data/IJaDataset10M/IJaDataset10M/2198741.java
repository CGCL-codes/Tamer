package com.vlee.servlet.portal.supplier;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.user.*;
import com.vlee.bean.user.*;
import com.vlee.util.*;
import com.vlee.bean.portal.supplier.*;

public class DoPurchaseOrderIndex extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        return new ActionRouter("portal-supplier-purchase-order-index-page");
    }
}
