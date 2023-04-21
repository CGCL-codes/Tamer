package com.vlee.servlet.inventory;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;

public class DoInvCategoryAddRem implements Action {

    private String strClassName = "DoInvCategoryAddRem";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        if (formName == null) {
            Log.printVerbose(strClassName + ": formName = null");
            fnGetCategoryList(servlet, req, res);
            return new ActionRouter("inv-setup-addrem-category-page");
        }
        if (formName.equals("addCategory")) {
            Log.printVerbose(strClassName + ": formName = addCategory");
            fnAddCategory(servlet, req, res);
        }
        if (formName.equals("deactCategory")) {
            Log.printVerbose(strClassName + ": formName = deactCategory");
            fnDeactCategory(servlet, req, res);
        }
        if (formName.equals("rmCategory")) {
            Log.printVerbose(strClassName + ": formName = rmCategory");
            fnRmCategory(servlet, req, res);
        }
        if (formName.equals("activateCategory")) {
            Log.printVerbose(strClassName + ": formName = activateCategory");
            fnActivateCategory(servlet, req, res);
        }
        fnGetCategoryList(servlet, req, res);
        return new ActionRouter("inv-setup-addrem-category-page");
    }

    protected void fnDeactCategory(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String rmCategoryCode = (String) req.getParameter("categoryCode");
        if (rmCategoryCode != null) {
            Category lCategoryCode = CategoryNut.getObjectByCode(rmCategoryCode);
            if (lCategoryCode != null) {
                try {
                    lCategoryCode.setStatus(CategoryBean.STATUS_INACTIVE);
                } catch (Exception ex) {
                    Log.printDebug("Deactivate Category Failed" + ex.getMessage());
                }
            }
        }
    }

    protected void fnActivateCategory(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String rmCategoryCode = (String) req.getParameter("activateCategory");
        if (rmCategoryCode != null) {
            Category lCategoryCode = CategoryNut.getObjectByCode(rmCategoryCode);
            if (lCategoryCode != null) {
                try {
                    lCategoryCode.setStatus(CategoryBean.STATUS_ACTIVE);
                } catch (Exception ex) {
                    Log.printDebug("Activate Category Failed" + ex.getMessage());
                }
            }
        }
    }

    protected void fnGetCategoryList(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String funcName = "fnGetCategoryList()";
        Log.printVerbose(strClassName + ": " + "In " + funcName);
        Collection colActiveCategory = CategoryNut.getActiveObjects();
        Iterator itrActiveCategory = colActiveCategory.iterator();
        Log.printVerbose("Setting attribute itrActiveCategory now");
        req.setAttribute("itrActiveCategory", itrActiveCategory);
        Log.printVerbose("Leaving " + strClassName + "::" + funcName);
        Collection colInactiveCategory = CategoryNut.getInactiveObjects();
        Iterator itrInactiveCategory = colInactiveCategory.iterator();
        Log.printVerbose("Setting attribute itrInactiveCategory now");
        req.setAttribute("itrInactiveCategory", itrInactiveCategory);
        Log.printVerbose("Leaving " + strClassName + "::" + funcName);
    }

    protected void fnAddCategory(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String funcName = "fnAddCategory()";
        Log.printVerbose(strClassName + ": " + "In " + funcName);
        String categoryCode = (String) req.getParameter("categoryCode");
        String categoryName = (String) req.getParameter("categoryName");
        String categoryDesc = (String) req.getParameter("categoryDescription");
        String params = "CategoryCode = " + categoryCode + "\n";
        params += "CategoryName = " + categoryName + "\n";
        params += "CategoryDesc = " + categoryDesc;
        Log.printVerbose(strClassName + ": Params = \n" + params);
        HttpSession session = req.getSession();
        User lUsr = UserNut.getHandle((String) session.getAttribute("userName"));
        if (lUsr == null) Log.printDebug(strClassName + ": " + "WARNING - NULL userName");
        if (categoryCode == null) return;
        if (categoryName == null) return;
        if (categoryDesc == null) return;
        Category lCategory = CategoryNut.getObjectByCode(categoryCode);
        if (lCategory == null && lUsr != null) {
            Log.printVerbose("Adding new Category");
            CategoryHome lCategoryH = CategoryNut.getHome();
            java.util.Date ldt = new java.util.Date();
            Timestamp tsCreate = new Timestamp(ldt.getTime());
            Integer usrid = null;
            try {
                usrid = lUsr.getUserId();
            } catch (Exception ex) {
                Log.printAudit("User does not exist: " + ex.getMessage());
            }
            try {
                Category newCategory = (Category) lCategoryH.create(categoryCode, categoryName, categoryDesc, tsCreate, usrid);
            } catch (Exception ex) {
                Log.printDebug("Cannot create Category " + ex.getMessage());
            }
        }
        if (lCategory != null) {
            String rtnMsg = "ERROR: Item Category aleady exists for the given code, please enter a new code";
            Log.printDebug(rtnMsg);
            req.setAttribute("rtnMsg", rtnMsg);
        }
        Log.printVerbose("Leaving " + strClassName + "::" + funcName);
    }

    protected void fnRmCategory(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String rmCategoryCode = (String) req.getParameter("categoryCode");
        if (rmCategoryCode != null) {
            Category tgtCategoryEJB = CategoryNut.getObjectByCode(rmCategoryCode);
            if (tgtCategoryEJB != null) {
                String strCatCode = "";
                try {
                    Integer tgtPk = tgtCategoryEJB.getPkid();
                    Collection colTgtCategory = ItemNut.getValObjectsGiven("categoryid", tgtPk.toString(), "pkid");
                    int itemsTgtCategory = colTgtCategory.size();
                    System.out.println("TUPPENY: category " + tgtPk + " is in use by " + itemsTgtCategory + " items.");
                    if (itemsTgtCategory > 0) {
                        System.out.println("TUPPENY: category in use. can't delete.");
                    } else {
                        tgtCategoryEJB.remove();
                        System.out.println("TUPPENY: Deleting category (" + tgtPk + ")");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
