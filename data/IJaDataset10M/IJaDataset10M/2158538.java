package com.vlee.servlet.test;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.BigDecimal;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;

/**
 * 
 * DoTopConFixStkAmt ================= There are discrepancies between the
 * migrated Stock Balance Amount and TOPCON's excel stock report. The problem is
 * that some data (esp. purchases) were missing in Topcon's DB Hence the Moving
 * Average Stock Amount derived is different.
 * 
 * There is no way to recover missing information. The best we could do, since
 * we have the Stock Balances and Stock Amounts from TOPCON (assumed to be
 * CORRECT !!), is to programmatically insert an MA adjustment to make up for
 * the difference in Stock Amount.
 * 
 */
public class DoTopConFixStkAmt implements Action {

    private String strClassName = "DoTopConFixStkAmt";

    private static Task curTask = null;

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        if (curTask != null) {
            req.setAttribute("curTask", curTask);
            return new ActionRouter("test-migrate-topcondb-pending-page");
        }
        Timestamp tsStart = TimeFormat.getTimestamp();
        try {
            Integer iDefLocId = new Integer(1000);
            Integer iDefSvcCtrId = new Integer(1);
            Integer iDefPCCenterId = new Integer(1000);
            Integer iDefCondId = new Integer(StockNut.STK_COND_GOOD);
            Integer iDefCashAccId = new Integer(1000);
            String strDefCurr = "MYR";
            HashMap hmCurr = new HashMap();
            hmCurr.put(new Integer(1), "MYR");
            hmCurr.put(new Integer(2), "USD");
            hmCurr.put(new Integer(3), "SGD");
            hmCurr.put(new Integer(4), "4");
            hmCurr.put(new Integer(5), "5");
            hmCurr.put(new Integer(6), "6");
            hmCurr.put(new Integer(7), "7");
            HttpSession session = req.getSession();
            User lUsr = UserNut.getHandle((String) session.getAttribute("userName"));
            Integer usrid = null;
            if (lUsr == null) {
                Log.printDebug(strClassName + ": " + "WARNING - NULL userName");
                return new ActionRouter("test-migrate-topcondb-page");
            }
            try {
                usrid = lUsr.getUserId();
            } catch (Exception ex) {
                Log.printAudit("User does not exist: " + ex.getMessage());
            }
            String topconURL = "jdbc:postgresql://localhost:5432/topcon";
            Connection topconCon = DriverManager.getConnection(topconURL, "jboss", "jboss");
            String empURL = "jdbc:postgresql://localhost:5432/wsemp";
            Connection empCon = DriverManager.getConnection(empURL, "jboss", "jboss");
            Statement topconStmt = topconCon.createStatement();
            Statement empStmt = empCon.createStatement();
            String stockRptPath = req.getParameter("stockRptPath");
            if (stockRptPath != null && stockRptPath.trim().equals("")) throw new Exception("Invalid Stock Report Path / FileName");
            Log.printVerbose("Input file name = " + stockRptPath);
            StkRptReader stkRptReader = new StkRptReader(stockRptPath);
            String queryStkBal = "select bal,unit_cost_ma from inv_stock where itemid = " + "(select pkid from inv_item where item_code = ?) and locationid=1000";
            PreparedStatement stkBalStmt = empCon.prepareStatement(queryStkBal);
            String queryStkNonZeroBal = "select i.item_code,s.bal,s.unit_cost_ma from inv_stock s inner join inv_item i on (i.pkid=s.itemid)" + " where s.locationid=1000 and s.bal != 0 ";
            Log.printVerbose("*** TOPCON -> EMP ***");
            int countBalDiff = 0;
            int countCostDiff = 0;
            curTask = new Task("Fix Stock Amount", stkRptReader.stockTable.size());
            int count = 0;
            for (Enumeration e = stkRptReader.stockTable.elements(); e.hasMoreElements(); ) {
                curTask.increment();
                curTask.setTimeElapsed(TimeFormat.getTimestamp().getTime() - tsStart.getTime());
                Log.printDebug("Processing Row " + ++count);
                StkRptReader.StkRow thisStk = (StkRptReader.StkRow) e.nextElement();
                stkBalStmt.setString(1, thisStk.code);
                ResultSet rsStkBal = stkBalStmt.executeQuery();
                if (rsStkBal.next()) {
                    BigDecimal bdStkBal = rsStkBal.getBigDecimal("bal");
                    BigDecimal bdUCostMA = rsStkBal.getBigDecimal("unit_cost_ma");
                    BigDecimal actualBal = new BigDecimal(thisStk.qty);
                    BigDecimal bdBalDiff = bdStkBal.subtract(actualBal);
                    BigDecimal bdCostDiff = bdUCostMA.multiply(bdStkBal).subtract(thisStk.totalCost);
                    BigDecimal bdUCostDiff = new BigDecimal(0);
                    if (actualBal.signum() > 0) {
                        bdUCostDiff = bdUCostMA.subtract(thisStk.totalCost.divide(actualBal, 4, BigDecimal.ROUND_HALF_UP));
                    }
                    if (bdBalDiff.signum() != 0 || bdUCostDiff.signum() != 0) {
                        if (bdBalDiff.signum() != 0) countBalDiff++;
                        if (bdUCostDiff.signum() != 0) countCostDiff++;
                        Log.printDebug(thisStk.code + ": " + bdBalDiff + ", " + bdUCostDiff);
                        Item thisItem = ItemNut.getObjectByCode(thisStk.code);
                        if (thisItem == null) {
                            Log.printDebug("!!!! Cannot find Item " + thisStk.code);
                        } else {
                            Log.printDebug("Adjusting stock " + thisStk.code);
                        }
                    } else {
                    }
                } else {
                    Log.printDebug(thisStk.code + ": NOT FOUND IN DB");
                }
            }
            Log.printVerbose("countBalDiff = " + countBalDiff);
            Log.printVerbose("countCostDiff = " + countCostDiff);
            Log.printVerbose("*** EMP -> TOPCON ***");
            countBalDiff = 0;
            countCostDiff = 0;
            curTask = new Task("Fix Stock Amount (EMP->TOPCON)", stkRptReader.stockTable.size());
            ResultSet rsStkNonZeroBal = empStmt.executeQuery(queryStkNonZeroBal);
            count = 0;
            while (rsStkNonZeroBal.next()) {
                curTask.increment();
                curTask.setTimeElapsed(TimeFormat.getTimestamp().getTime() - tsStart.getTime());
                Log.printVerbose("Processing Row " + ++count);
                String thisItemCode = rsStkNonZeroBal.getString("item_code");
                BigDecimal thisBal = rsStkNonZeroBal.getBigDecimal("bal");
                BigDecimal thisUnitPriceMA = rsStkNonZeroBal.getBigDecimal("unit_cost_ma");
                if (thisBal.signum() < 0) {
                    Log.printDebug("!!!!! WARNING: -ve bal for " + thisItemCode + ", Skipping ...");
                    continue;
                }
                StkRptReader.StkRow thisStk = (StkRptReader.StkRow) stkRptReader.stockTable.get(thisItemCode);
                if (thisStk != null) {
                    Log.printVerbose("thisStk != null. Skipping " + thisItemCode + " ...");
                    continue;
                }
                if (thisBal.signum() == 0) {
                    Log.printVerbose("thisBal = 0. Skipping " + thisItemCode + " ...");
                    continue;
                }
                if (thisBal.signum() > 0) {
                    Log.printVerbose("thisBal > 0 ... ");
                    Item thisItem = ItemNut.getObjectByCode(thisItemCode);
                    if (thisItem == null) {
                        Log.printDebug("!!!! Cannot find Item " + thisItemCode);
                    } else {
                    }
                } else {
                    Log.printVerbose("thisBal > 0 ... ");
                    Item thisItem = ItemNut.getObjectByCode(thisItemCode);
                    if (thisItem == null) {
                        Log.printDebug("!!!! Cannot find Item " + thisItemCode);
                    } else {
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("strErrMsg", "Error occurred while fixing stock bal: " + ex.getMessage());
        }
        Log.printVerbose("***** END: FIX STOCK BAL AMOUNT *****");
        Timestamp tsEnd = TimeFormat.getTimestamp();
        req.setAttribute("task", "FIX STOCK AMOUNT");
        req.setAttribute("tsStart", tsStart);
        req.setAttribute("tsEnd", tsEnd);
        curTask = null;
        return new ActionRouter("test-migrate-topcondb-page");
    }
}
