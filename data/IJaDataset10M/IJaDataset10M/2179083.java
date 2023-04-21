package com.vlee.servlet.distribution;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;
import com.vlee.ejb.customer.*;
import com.vlee.bean.distribution.*;

public class DoDeliveryLocationListing extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        String fwdPage = req.getParameter("fwdPage");
        if (fwdPage == null) {
            fwdPage = "dist-delivery-location-listing-page";
            Log.printVerbose(fwdPage);
        }
        req.setAttribute("parentAction", req.getParameter("parentAction"));
        req.setAttribute("parentFormName", req.getParameter("parentFormName"));
        if (formName == null) {
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("getList")) {
            try {
                fnGetList(servlet, req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        if (formName.equals("fuzzySearch")) {
            fnFuzzySearch(servlet, req, res);
        }
        if (formName.equals("deleteRow")) {
            fnDeleteDeliveryLocation(servlet, req, res);
        }
        return new ActionRouter(fwdPage);
    }

    private void fnDeleteDeliveryLocation(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        String dlPkid = req.getParameter("dlPkid");
        try {
            Long thePkid = new Long(dlPkid);
            DeliveryLocation dlEJB = DeliveryLocationNut.getHandle(thePkid);
            dlEJB.remove();
            fnGetList(servlet, req, res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String keyword = (String) session.getAttribute("delivery-location-listing-keyword");
        if (keyword == null || keyword.trim().length() < 1) {
            return;
        }
        QueryObject query = new QueryObject(new String[] { DeliveryLocationBean.COUNTRY + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.STATE + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.CITY + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.AREA + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.STREET + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.BUILDING + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.NAME + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.ZIP + " ~* '" + keyword + "'  " });
        Vector vecList = new Vector(DeliveryLocationNut.getObjects(query));
        session.setAttribute("delivery-location-listing-vecList", vecList);
    }

    private void fnFuzzySearch(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        String keyword = req.getParameter("keyword");
        session.setAttribute("delivery-location-listing-keyword", keyword);
        String flagState = req.getParameter("flagState");
        session.setAttribute("delivery-location-listing-flagState", flagState);
        String parentFormName = req.getParameter("formName");
        session.setAttribute("delivery-location-listing-parentFormName", parentFormName);
        Log.printVerbose("parentFormName:" + parentFormName);
        Log.printVerbose("flagState:" + flagState);
        Log.printVerbose("keyword:" + keyword);
        if (flagState != null) {
            if (keyword == null || keyword.trim().length() < 1) {
                return;
            }
            QueryObject query = new QueryObject(new String[] { DeliveryLocationBean.STATE + " ~* '" + flagState + "'  AND (" + DeliveryLocationBean.COUNTRY + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.STATE + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.CITY + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.AREA + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.STREET + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.BUILDING + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.NAME + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.ZIP + " ~* '" + keyword + "' )" });
            query.setOrder(" ORDER BY " + DeliveryLocationBean.COUNTRY + ", " + DeliveryLocationBean.STATE + ", " + DeliveryLocationBean.CITY + ", " + DeliveryLocationBean.AREA + ", " + DeliveryLocationBean.STREET + ", " + DeliveryLocationBean.BUILDING + ", " + DeliveryLocationBean.NAME + ", " + DeliveryLocationBean.ZIP);
            Vector vecList = new Vector(DeliveryLocationNut.getObjects(query));
            session.setAttribute("delivery-location-listing-vecList", vecList);
        } else {
            if (keyword == null || keyword.trim().length() < 1) {
                return;
            }
            QueryObject query = new QueryObject(new String[] { DeliveryLocationBean.COUNTRY + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.STATE + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.CITY + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.AREA + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.STREET + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.BUILDING + " ~* '" + keyword + "'  OR " + DeliveryLocationBean.ZIP + " ~* '" + keyword + "'  " });
            query.setOrder(" ORDER BY " + DeliveryLocationBean.COUNTRY + ", " + DeliveryLocationBean.STATE + ", " + DeliveryLocationBean.CITY + ", " + DeliveryLocationBean.AREA + ", " + DeliveryLocationBean.STREET + ", " + DeliveryLocationBean.BUILDING + ", " + DeliveryLocationBean.NAME + ", " + DeliveryLocationBean.ZIP);
            Vector vecList = new Vector(DeliveryLocationNut.getObjects(query));
            session.setAttribute("delivery-location-listing-vecList", vecList);
        }
    }

    private void fnGetList(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        Log.printVerbose("Start fnGetList");
        HttpSession session = req.getSession();
        QueryObject query = new QueryObject(new String[] {});
        query.setOrder(" ORDER BY " + DeliveryLocationBean.COUNTRY + ", " + DeliveryLocationBean.STATE + ", " + DeliveryLocationBean.CITY + ", " + DeliveryLocationBean.AREA + ", " + DeliveryLocationBean.STREET + ", " + DeliveryLocationBean.BUILDING + ", " + DeliveryLocationBean.NAME + ", " + DeliveryLocationBean.ZIP);
        Vector vecList = new Vector(DeliveryLocationNut.getObjects(query));
        session.setAttribute("delivery-location-listing-vecList", vecList);
        Log.printVerbose("End fnGetList");
    }
}
