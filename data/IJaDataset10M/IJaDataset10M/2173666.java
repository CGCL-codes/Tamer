package com.vlee.servlet.distribution;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;
import com.vlee.ejb.customer.*;
import com.vlee.bean.distribution.*;

public class DoDriverCreate extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        String action = req.getParameter("action");
        String pageToken = req.getParameter("pageToken");
        String tripPkid = req.getParameter("tripPkid");
        req.setAttribute("action", action);
        req.setAttribute("pageToken", pageToken);
        req.setAttribute("tripPkid", tripPkid);
        System.out.println("tripPkid" + tripPkid);
        if (formName == null) {
            return new ActionRouter("dist-driver-create-page");
        }
        if (formName.equals("createDriver")) {
            try {
                fnCreateDriver(servlet, req, res);
                req.setAttribute("notifySuccess", "Successfully created a new driver");
            } catch (Exception ex) {
                fnPreserveParam(servlet, req, res);
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        return new ActionRouter("dist-driver-create-page");
    }

    private void fnCreateDriver(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String username = req.getParameter("username");
        if (GeneralEntityIndexNut.countUsername(username) > 0) {
            throw new Exception("Username exists!");
        }
        GeneralEntityIndexObject geiObj = new GeneralEntityIndexObject();
        geiObj.entityType = GeneralEntityIndexBean.ET_DIST_DRIVER;
        geiObj.username = username;
        geiObj.name = req.getParameter("name");
        geiObj.icNumber = req.getParameter("icNumber");
        geiObj.phoneFix = req.getParameter("phoneFix_Prefix") + "-" + req.getParameter("phoneFix");
        geiObj.phoneMobile = req.getParameter("phoneMobile_Prefix") + "-" + req.getParameter("phoneMobile");
        geiObj.phoneFax = req.getParameter("phoneFax_Prefix") + "-" + req.getParameter("phoneFax");
        geiObj.mainAddress1 = req.getParameter("mainAddress1");
        geiObj.mainAddress2 = req.getParameter("mainAddress2");
        geiObj.mainAddress3 = req.getParameter("mainAddress3");
        geiObj.option1 = req.getParameter("option1");
        GeneralEntityIndex geiEJB = GeneralEntityIndexNut.fnCreate(geiObj);
        if (geiEJB == null) {
            throw new Exception("An internal error occured while creating the driver!");
        }
    }

    private void fnPreserveParam(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        GeneralEntityIndexObject geiObj = new GeneralEntityIndexObject();
        geiObj.username = req.getParameter("username");
        geiObj.name = req.getParameter("name");
        geiObj.icNumber = req.getParameter("icNumber");
        geiObj.phoneFix = req.getParameter("phoneFix_Prefix") + "-" + req.getParameter("phoneFix");
        geiObj.phoneMobile = req.getParameter("phoneMobile_Prefix") + "-" + req.getParameter("phoneMobile");
        geiObj.phoneFax = req.getParameter("phoneFax_Prefix") + "-" + req.getParameter("phoneFax");
        geiObj.mainAddress1 = req.getParameter("mainAddress1");
        geiObj.mainAddress2 = req.getParameter("mainAddress2");
        geiObj.mainAddress3 = req.getParameter("mainAddress3");
        geiObj.option1 = req.getParameter("option1");
        req.setAttribute("dist-driver-create-previous-value", geiObj);
    }
}
