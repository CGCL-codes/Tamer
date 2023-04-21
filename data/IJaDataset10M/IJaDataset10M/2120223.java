package com.vlee.servlet.employee;

import com.vlee.ejb.employee.*;
import com.vlee.util.*;
import java.lang.String;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.vlee.servlet.main.*;

public class DoEditRemType implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String RITypePkid = (String) req.getParameter("RITypePkid");
        if (RITypePkid != null) {
            Integer intRITypePkid = new Integer(RITypePkid);
            fnGetEditRIType(servlet, req, res, intRITypePkid);
        } else {
            Log.printVerbose("RITypePkid = null");
        }
        return new ActionRouter("erm-edit-remuneration-type-page");
    }

    protected void fnGetEditRIType(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res, Integer intRITypePkid) {
        EmpRemunerationItemType RIType = EmpRemunerationItemTypeNut.getHandle(intRITypePkid);
        if (RIType != null) {
            try {
                req.setAttribute("editRIType", RIType);
            } catch (Exception ex) {
                Log.printDebug("Error in loading editRIType");
            }
        }
    }
}
