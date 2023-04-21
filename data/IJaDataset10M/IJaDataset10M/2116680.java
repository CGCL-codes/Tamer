package com.vlee.servlet.procurement;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import com.vlee.ejb.supplier.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.vlee.bean.procurement.PurchaseOrderStockSession;
import com.vlee.ejb.customer.SalesOrderIndexNut;
import com.vlee.ejb.customer.SalesOrderIndexObject;
import com.vlee.ejb.inventory.Item;
import com.vlee.ejb.inventory.ItemBean;
import com.vlee.ejb.inventory.ItemNut;
import com.vlee.ejb.inventory.ItemObject;
import com.vlee.ejb.inventory.SerialNumberDeltaBean;
import com.vlee.ejb.inventory.SerialNumberDeltaNut;
import com.vlee.ejb.inventory.SerialNumberIndexBean;
import com.vlee.ejb.inventory.SerialNumberIndexHome;
import com.vlee.ejb.inventory.SerialNumberIndexNut;
import com.vlee.ejb.inventory.SerialNumberIndexObject;
import com.vlee.ejb.inventory.StockObject;
import com.vlee.servlet.main.Action;
import com.vlee.servlet.main.ActionRouter;
import com.vlee.util.Log;
import com.vlee.util.QueryObject;
import com.vlee.util.StringManup;

public class DoWidgetReceiveStock implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        System.out.println("formName :" + formName);
        fnGetSetParams(servlet, req, res);
        if (formName.equals("newForm")) {
            fnNewForm(servlet, req, res);
            try {
                if (fnReceiveForm(servlet, req, res).equals("serialized")) {
                    return new ActionRouter("procurement-widget-receive-stock-serial-page");
                } else {
                    return new ActionRouter("procurement-widget-receive-stock-non-serial-page");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (formName.equals("setDetailsSerial")) {
            try {
                fnSetDetailsSerial(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
                req.setAttribute("errMsg", ex.getMessage());
            }
            return new ActionRouter("procurement-widget-receive-stock-serial-page");
        } else if (formName.equals("setDetailsNonSerial")) {
            try {
                fnSetDetailsNonSerial(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
                req.setAttribute("errMsg", ex.getMessage());
            }
            return new ActionRouter("procurement-widget-receive-stock-non-serial-page");
        } else if (formName.equals("addSerial")) {
            try {
                fnAddSerial(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new ActionRouter("procurement-widget-receive-stock-serial-page");
        } else if (formName.equals("clearSerial")) {
            fnClearSerialNumber(servlet, req, res);
            return new ActionRouter("procurement-widget-receive-stock-serial-page");
        } else if (formName.equals("dropSelectedSerial")) {
            fnDropSelectedSerial(servlet, req, res);
            return new ActionRouter("procurement-widget-receive-stock-serial-page");
        }
        return new ActionRouter("procurement-widget-receive-stock-non-serial-page");
    }

    private void fnNewForm(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        Integer branchId = new Integer(req.getParameter("branch"));
        PurchaseOrderStockSession efif = new PurchaseOrderStockSession(userId);
        String action = req.getParameter("action");
        efif.setParentAction(action);
        efif.setBranch(branchId);
        String foreignCcy = req.getParameter("foreignCcy");
        String xrate = req.getParameter("xrate");
        if (foreignCcy != null && xrate != null) {
            try {
                BigDecimal bdRate = new BigDecimal(xrate);
                efif.setForex(foreignCcy, bdRate);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            efif.setForex("", new BigDecimal(0));
        }
        session.setAttribute(action + PurchaseOrderStockSession.OBJNAME, efif);
        session.setAttribute(efif.getGuid(), efif);
    }

    private String fnReceiveForm(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String essKey = req.getParameter("essKey");
        String parentAction = req.getParameter("action");
        System.out.println("action " + action.toString());
        System.out.println("essKey " + essKey.toString());
        System.out.println("parentAction " + parentAction.toString());
        PurchaseOrderStockSession efif = (PurchaseOrderStockSession) session.getAttribute(essKey);
        efif.setParentAction(parentAction);
        session.setAttribute(action + PurchaseOrderStockSession.OBJNAME, efif);
        session.setAttribute(efif.getGuid(), efif);
        req.setAttribute("autoClose", req.getParameter("autoClose"));
        if (efif.getSerialized()) {
            Log.printVerbose("######- DoWidgetReceiveStock - serialized");
            return "serialized";
        } else {
            Log.printVerbose("######- DoWidgetReceiveStock - non-serialized");
            return "not serialized";
        }
    }

    protected void fnDropSelectedSerial(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        String[] serialNum = req.getParameterValues("serialNum");
        if (serialNum == null) return;
        for (int cnt1 = 0; cnt1 < serialNum.length; cnt1++) {
            if (serialNum[cnt1] == null || serialNum[cnt1].length() == 0) {
                continue;
            }
            ess.dropSerialNumber(serialNum[cnt1].toUpperCase());
        }
    }

    protected void fnClearSerialNumber(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        ess.clearSerialNumber();
    }

    protected void fnAddSerial(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        TreeMap treeExistingSerial = new TreeMap();
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        String serial1 = req.getParameter("serial");
        String first = req.getParameter("first");
        String last = req.getParameter("last");
        if (serial1 != null) {
            serial1 = serial1.trim().toUpperCase();
            QueryObject query = new QueryObject(new String[] { SerialNumberDeltaBean.SERIAL + "='" + serial1 + "' " });
            Vector vecSerial = new Vector(SerialNumberDeltaNut.getObjects(query));
            if (vecSerial.size() > 0) {
                treeExistingSerial.put(serial1, vecSerial);
            }
            ess.addSerialNumber(serial1);
        }
        if (first != null && last != null) {
            try {
                first = first.trim().toUpperCase();
                last = last.trim().toUpperCase();
                String[] serial = SerialNumberDeltaNut.fnGetSequence(first, last);
                for (int cnt1 = 0; cnt1 < serial.length; cnt1++) {
                    QueryObject query = new QueryObject(new String[] { SerialNumberDeltaBean.SERIAL + "='" + serial[cnt1] + "' " });
                    Vector vecSerial = new Vector(SerialNumberDeltaNut.getObjects(query));
                    if (vecSerial.size() > 0) {
                        treeExistingSerial.put(serial[cnt1], vecSerial);
                    }
                    ess.addSerialNumber(serial[cnt1]);
                }
            } catch (Exception ex) {
                req.setAttribute("errMsg", "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        req.setAttribute("treeExistingSerial", treeExistingSerial);
    }

    protected void fnSetDetailsSerial(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        String remarks = req.getParameter("remarks");
        String status = req.getParameter("status");
        ess.setRemarks(remarks);
        BigDecimal bdPrice = null;
        try {
            bdPrice = new BigDecimal(req.getParameter("price"));
        } catch (Exception ex) {
            throw new Exception("Invalid Price");
        }
        ess.setReceivingPrice(bdPrice);
        ess.setReceivingRemarks(remarks);
        if (status == null) {
            status = "";
        }
        ess.setReceivingStatus(status);
        ess.setReceivingQty(new Integer(ess.getSerialNumbers().size()));
    }

    protected void fnSetDetailsNonSerial(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        String remarks = req.getParameter("remarks");
        String status = req.getParameter("status");
        if (status == null) {
            throw new Exception("To save the process details, please set status to Process. Else please close this pop-up");
        }
        ess.setReceivingRemarks(remarks);
        BigDecimal bdPrice = null;
        BigDecimal bdQty = null;
        try {
            bdPrice = new BigDecimal(req.getParameter("price"));
        } catch (Exception ex) {
            throw new Exception("Invalid Price");
        }
        ess.setReceivingPrice(bdPrice);
        try {
            bdQty = new BigDecimal(req.getParameter("qty"));
        } catch (Exception ex) {
            throw new Exception("Invalid Quantity");
        }
        ess.setReceivingQty(new Integer(bdQty.intValue()));
        if (status == null) {
            status = "";
        }
        ess.setReceivingStatus(status);
        session.setAttribute(action + PurchaseOrderStockSession.OBJNAME, ess);
    }

    protected ActionRouter fnSelectCode(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String itemCode = req.getParameter("itemCode");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        if (itemCode != null && itemCode.length() > 1) {
            try {
                ItemObject itemObj = ItemNut.getValueObjectByCode(itemCode);
                if (itemObj != null) {
                    System.out.println("status : " + itemObj.status);
                    if (!ItemBean.STATUS_ACTIVE.equals(itemObj.status)) {
                        req.setAttribute("errMsg", "Item is inactive");
                        return new ActionRouter("procurement-widget-receive-stock-page");
                    }
                }
                ess.setItemCode(itemCode, "buy");
                {
                    return new ActionRouter("procurement-widget-receive-stock-non-serial-page");
                }
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return new ActionRouter("procurement-widget-receive-stock-page");
    }

    protected void fnGetSetParams(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String action = req.getParameter("action");
        req.setAttribute("action", action);
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        if (ess == null) {
            System.out.println("----------------------------------- ess is null");
            Integer userId = (Integer) session.getAttribute("userId");
            ess = new PurchaseOrderStockSession(userId);
            session.setAttribute(action + "PurchaseOrderStockSession", ess);
            session.setAttribute(ess.getGuid(), ess);
            String foreignCcy = req.getParameter("foreignCcy");
            String xrate = req.getParameter("xrate");
            if (foreignCcy != null && xrate != null) {
                try {
                    BigDecimal bdRate = new BigDecimal(xrate);
                    ess.setForex(foreignCcy, bdRate);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                ess.setForex("", new BigDecimal(0));
            }
        }
        String branch = req.getParameter("branch");
        if (branch != null) {
            ess.setBranch(new Integer(branch));
        }
        String criteria = req.getParameter("criteria");
        if (criteria != null) {
            req.setAttribute("criteria", criteria);
        }
        String eanCode = req.getParameter("eanCode");
        if (eanCode != null) {
            req.setAttribute("eanCode", eanCode);
        }
    }

    protected void fnGetSearchResults(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        Vector vecItemObj = null;
        String criteria = (String) req.getParameter("criteria");
        criteria = criteria.trim();
        QueryObject query = null;
        if (criteria != null && criteria.length() >= 1) {
            criteria = "%" + criteria + "%";
            query = new QueryObject(new String[] { ItemBean.ITEM_CODE + " ILIKE '" + criteria + "' " + " OR " + ItemBean.NAME + " ILIKE '" + criteria + "' " + " OR " + ItemBean.DESCRIPTION + " ILIKE '" + criteria + "' " });
            query.setOrder(" ORDER BY " + ItemBean.ITEM_CODE);
            vecItemObj = new Vector(ItemNut.getObjects(query));
        } else {
            vecItemObj = new Vector();
        }
        if (vecItemObj == null) {
            throw new Exception("Unable to retrieve the supplier objects");
        }
        req.setAttribute("vecItemObj", vecItemObj);
        return;
    }

    protected void fnSetOrder(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String orderId = req.getParameter("orderId");
        String priceOption = req.getParameter("priceOption");
        try {
            Long iOrderId = new Long(orderId);
            SalesOrderIndexObject soObj = SalesOrderIndexNut.getObjectTree(iOrderId);
            if (soObj == null) {
                throw new Exception("Invalid Order Id!");
            }
            req.setAttribute("the-sales-order-obj", soObj);
            req.setAttribute("priceOption", priceOption);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Invalid Order Id!");
        }
    }

    protected ActionRouter fnSelectOrderedItem(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String orderId = req.getParameter("orderId");
        String itemCode = req.getParameter("itemCode");
        String itemQty = req.getParameter("itemQty");
        String itemRemarks = req.getParameter("itemRemarks");
        String action = req.getParameter("action");
        String priceOption = req.getParameter("priceOption");
        HttpSession session = req.getSession();
        PurchaseOrderStockSession ess = (PurchaseOrderStockSession) session.getAttribute(action + PurchaseOrderStockSession.OBJNAME);
        if (itemCode != null && itemCode.length() > 1) {
            try {
                if ("list".equals(priceOption)) {
                    ess.setItemCode(itemCode, "");
                } else {
                    ess.setItemCode(itemCode, "buy");
                }
                ess.setRemarks(itemRemarks);
                BigDecimal bdQty = new BigDecimal(itemQty);
                ess.setQty(bdQty);
                return new ActionRouter("procurement-widget-receive-stock-non-serial-page");
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return new ActionRouter("procurement-widget-add-ordered-item-select-page");
    }
}
