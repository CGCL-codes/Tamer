package com.vlee.servlet.distribution;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;
import java.math.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;
import com.vlee.bean.distribution.*;

public class DoDeliveryItemTracking extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("dist-delivery-item-tracking-page");
        }
        if (formName.equals("getList")) {
            fnGetList(servlet, req, res);
            return new ActionRouter("dist-delivery-item-tracking-page");
        }
        return new ActionRouter("dist-delivery-item-tracking-page");
    }

    public void fnGetList(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String searchDateFrom = req.getParameter("searchDateFrom");
        String searchDateTo = req.getParameter("searchDateTo");
        String searchDeliveryStatus = req.getParameter("searchDeliveryStatus");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        DeliveryTrackingForm dtForm = new DeliveryTrackingForm(userId);
        dtForm.setDateRange(searchDateFrom, searchDateTo);
        dtForm.setDeliveryStatus(searchDeliveryStatus);
        dtForm.getList();
        req.setAttribute("delivery-tracking-form", dtForm);
    }
}
