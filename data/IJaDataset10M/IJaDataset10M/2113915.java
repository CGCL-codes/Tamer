package com.vlee.servlet.inventory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.vlee.ejb.inventory.Item;
import com.vlee.ejb.inventory.ItemNut;
import com.vlee.ejb.inventory.SerialNumberIndex;
import com.vlee.ejb.inventory.SerialNumberIndexBean;
import com.vlee.ejb.inventory.SerialNumberIndexHome;
import com.vlee.ejb.inventory.SerialNumberIndexNut;
import com.vlee.ejb.inventory.SerialNumberIndexObject;
import com.vlee.servlet.main.Action;
import com.vlee.servlet.main.ActionRouter;
import com.vlee.util.Log;
import com.vlee.util.QueryObject;
import com.vlee.util.StringManup;

public class DoInvSerialNumberIndex implements Action {

    private String strClassName = "DoInvSerialNumberIndex";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        Log.printVerbose(formName);
        if (formName == null) {
            req.setAttribute("mode", "add");
        } else if (formName.equals("add")) {
            req.setAttribute("mode", "add");
            create(servlet, req, res);
        } else if (formName.equals("edit")) {
            req.setAttribute("mode", "edit");
            getEdit(servlet, req, res);
            return new ActionRouter("inv-serial-number-index-edit-page");
        } else if (formName.equals("delete")) {
            req.setAttribute("mode", "create");
            remove(servlet, req, res);
        } else if (formName.equals("update")) {
            try {
                req.setAttribute("mode", "update");
                edit(servlet, req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
            return new ActionRouter("inv-serial-number-index-edit-page");
        }
        return new ActionRouter("inv-serial-number-index-page");
    }

    private void getEdit(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        try {
            if (req.getParameter("pkid") != null && !req.getParameter("pkid").trim().equals("")) {
                QueryObject query = new QueryObject(new String[] { SerialNumberIndexBean.PKID + " = '" + (String) req.getParameter("pkid") + "' " });
                Vector v = (Vector) SerialNumberIndexNut.getObjects(query);
                if (v.size() > 0) {
                    req.setAttribute("obj", v.get(0));
                } else {
                    req.setAttribute("obj", new SerialNumberIndexObject());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void remove(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        try {
            if (req.getParameter("pkid") != null && !req.getParameter("pkid").trim().equals("")) {
                Long pkid = new Long(req.getParameter("pkid"));
                SerialNumberIndex pmEJB = SerialNumberIndexNut.getHandle(pkid);
                pmEJB.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String funcName = "create()";
        Log.printVerbose(strClassName + ": " + "In " + funcName);
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        SerialNumberIndexHome snNumberM = SerialNumberIndexNut.getHome();
        int numberNeeded = 0;
        Collection collObj;
        if (req.getParameter("numberNeeded") != null && !req.getParameter("numberNeeded").trim().equals("")) numberNeeded = new Integer((String) req.getParameter("numberNeeded")).intValue();
        if (userId != null && numberNeeded > 0) {
            collObj = new ArrayList();
            java.util.Date ldt = new java.util.Date();
            Timestamp tsCreate = new Timestamp(ldt.getTime());
            try {
                for (int x = 0; x < numberNeeded; x++) {
                    SerialNumberIndexObject obj = new SerialNumberIndexObject();
                    obj.useridCreate = userId;
                    obj.timeCreate = tsCreate;
                    obj.status = SerialNumberIndexBean.VACANT;
                    snNumberM.create(obj);
                    collObj.add(obj);
                }
                req.setAttribute("collObj", collObj);
            } catch (Exception ex) {
                Log.printDebug("Cannot create SerialNumberIndex " + ex.getMessage());
            }
        }
        Log.printVerbose("Leaving " + strClassName + "::" + funcName);
    }

    protected void edit(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String funcName = "edit()";
        Log.printVerbose(strClassName + ": " + "In " + funcName);
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            try {
                SerialNumberIndexObject obj = setObj(servlet, req, res);
                if (obj.itemid == null || obj.itemid.intValue() == 0 || obj.itemid.toString().trim().equals("")) {
                    Item item = ItemNut.getObjectByCode(obj.itemcode);
                    obj.itemid = item.getPkid();
                    obj.stockid = item.getPkid();
                }
                SerialNumberIndex serialnumber = SerialNumberIndexNut.getHandle(obj.pkid);
                serialnumber.setObject(obj);
                req.setAttribute("obj", obj);
            } catch (Exception ex) {
                Log.printDebug("Cannot create SerialNumberIndex " + ex.getMessage());
            }
        }
        Log.printVerbose("Leaving " + strClassName + "::" + funcName);
    }

    private SerialNumberIndexObject setObj(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        Log.printVerbose(strClassName + ": In setObj");
        SerialNumberIndexObject obj = new SerialNumberIndexObject();
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        try {
            if (userId != null) {
                java.util.Date ldt = new java.util.Date();
                Timestamp tsCreate = new Timestamp(ldt.getTime());
                if (req.getParameter("pkid") != null && !req.getParameter("pkid").trim().equals("")) obj.pkid = new Long((String) req.getParameter("pkid"));
                obj.serial1 = StringManup.lPad(obj.pkid.toString(), 6, '0');
                obj.guid = (String) req.getParameter("guid");
                obj.parentGuid = (String) req.getParameter("parentGuid");
                obj.namespace = (String) req.getParameter("namespace");
                if (req.getParameter("itemid") != null && !req.getParameter("itemid").trim().equals("")) obj.stockid = new Integer((String) req.getParameter("itemid"));
                if (req.getParameter("qty") != null && !req.getParameter("qty").trim().equals("")) obj.qty = new BigDecimal((String) req.getParameter("qty"));
                obj.currency = (String) req.getParameter("currency");
                if (req.getParameter("priceRetail") != null && !req.getParameter("priceRetail").trim().equals("")) obj.priceRetail = new BigDecimal((String) req.getParameter("priceRetail"));
                if (req.getParameter("priceDealer") != null && !req.getParameter("priceDealer").trim().equals("")) obj.priceDealer = new BigDecimal((String) req.getParameter("priceDealer"));
                if (req.getParameter("priceMinimum") != null && !req.getParameter("priceMinimum").trim().equals("")) obj.priceMinimum = new BigDecimal((String) req.getParameter("priceMinimum"));
                if (req.getParameter("priceOutlet") != null && !req.getParameter("priceOutlet").trim().equals("")) obj.priceOutlet = new BigDecimal((String) req.getParameter("priceOutlet"));
                if (req.getParameter("priceCost") != null && !req.getParameter("priceCost").trim().equals("")) obj.priceCost = new BigDecimal((String) req.getParameter("priceCost"));
                obj.currency2 = (String) req.getParameter("currency2");
                if (req.getParameter("xrate") != null && !req.getParameter("xrate").trim().equals("")) obj.xrate = new BigDecimal((String) req.getParameter("xrate"));
                if (req.getParameter("itemid") != null && !req.getParameter("itemid").trim().equals("")) obj.itemid = new Integer((String) req.getParameter("itemid"));
                obj.itemcode = (String) req.getParameter("itemcode");
                obj.remarks = (String) req.getParameter("remarks");
                obj.serial2 = (String) req.getParameter("serial2");
                obj.brand = (String) req.getParameter("brand");
                obj.speed = (String) req.getParameter("speed");
                obj.memory = (String) req.getParameter("memory");
                obj.diskSpace = (String) req.getParameter("diskSpace");
                obj.strReserved1 = (String) req.getParameter("strReserved1");
                obj.status = (String) req.getParameter("status");
                if (req.getParameter("strReserved2") != null) obj.strReserved2 = "checked"; else obj.strReserved2 = "";
                if (req.getParameter("strReserved3") != null) obj.strReserved3 = "checked"; else obj.strReserved3 = "";
                if (req.getParameter("strReserved4") != null) obj.strReserved4 = "checked"; else obj.strReserved4 = "";
                if (req.getParameter("specs1") != null) obj.specs1 = "checked"; else obj.specs1 = "";
                if (req.getParameter("specs2") != null) obj.specs2 = "checked"; else obj.specs2 = "";
                if (req.getParameter("specs3") != null) obj.specs3 = "checked"; else obj.specs3 = "";
                if (req.getParameter("entityTable") != null) obj.entityTable = "checked"; else obj.entityTable = "";
                if (req.getParameter("strReserved5") != null) obj.strReserved5 = "checked"; else obj.strReserved5 = "";
                if (req.getParameter("strReserved6") != null) obj.strReserved6 = "checked"; else obj.strReserved6 = "";
                if (req.getParameter("strReserved7") != null) obj.strReserved7 = "checked"; else obj.strReserved7 = "";
                if (req.getParameter("entityId") != null && !req.getParameter("entityId").trim().equals("")) obj.entityId = new Integer((String) req.getParameter("entityId"));
                obj.docTable = (String) req.getParameter("docTable");
                if (req.getParameter("docKey") != null && !req.getParameter("docKey").trim().equals("")) obj.docKey = new Long((String) req.getParameter("docKey"));
                obj.useridEdit = userId;
                obj.timeEdit = tsCreate;
            }
        } catch (Exception e) {
        }
        return obj;
    }
}
