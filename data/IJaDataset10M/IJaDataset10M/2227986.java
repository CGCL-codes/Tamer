package com.vlee.servlet.inventory;

import java.util.Collection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.vlee.ejb.inventory.SerialNumberIndexBean;
import com.vlee.ejb.inventory.SerialNumberIndexNut;
import com.vlee.servlet.main.Action;
import com.vlee.servlet.main.ActionRouter;
import com.vlee.util.QueryObject;

public class DoListAllSerialNumber implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String searchmode = req.getParameter("searchmode");
        preserveFormData(servlet, req, res);
        if (searchmode == null) {
            return new ActionRouter("inv-showall-serial-number-index-page");
        } else if (searchmode.equals("searchCode")) {
            searchItemCode(servlet, req, res);
        } else if (searchmode.equals("listAll")) {
            listAll(servlet, req, res);
        }
        return new ActionRouter("inv-showall-serial-number-index-page");
    }

    private void preserveFormData(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("code", req.getParameter("code"));
        req.setAttribute("pkid", req.getParameter("pkid"));
    }

    private void searchItemCode(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String keyword = req.getParameter("keyword");
        if (keyword != null) {
            QueryObject query = new QueryObject(new String[] { SerialNumberIndexBean.PKID + " ~* '" + keyword + "' " + " OR " + SerialNumberIndexBean.ITEMCODE + " ~* '" + keyword + "' " + " OR " + SerialNumberIndexBean.BRAND + " ~* '" + keyword + "' " });
            Collection coll = (Collection) SerialNumberIndexNut.getObjects(query);
            req.setAttribute("coll", coll);
        }
    }

    private void listAll(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        QueryObject query = new QueryObject(new String[] { "" });
        Collection coll = (Collection) SerialNumberIndexNut.getObjects(query);
        req.setAttribute("coll", coll);
    }
}
