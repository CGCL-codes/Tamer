package com.vlee.ejb.inventory;

import java.math.*;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import com.vlee.bean.inventory.StockBalErrorChecking;
import com.vlee.bean.reports.*;
import com.vlee.ejb.accounting.Branch;
import com.vlee.ejb.accounting.BranchHome;
import com.vlee.ejb.customer.InvoiceHome;
import com.vlee.ejb.user.UserNut;
import com.vlee.util.*;

public class StockDeltaNut {

    public static StockDeltaHome getHome() {
        try {
            Context lContext = new InitialContext();
            StockDeltaHome lEJBHome = (StockDeltaHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/inventory/StockDelta"), StockDeltaHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static StockDelta getHandle(Long pkid) {
        return (StockDelta) getHandle(getHome(), pkid);
    }

    public static StockDelta getHandle(StockDeltaHome lEJBHome, Long pkid) {
        try {
            return (StockDelta) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static StockDelta fnCreate(StockDeltaObject valObj) {
        StockDelta ejb = null;
        StockDeltaObject valObj2 = null;
        try {
            ItemObject itmObj = ItemNut.getObject(valObj.itemId);
            valObj.unitCostReplacement = itmObj.replacementUnitCost;
            StockDeltaHome home = getHome();
            ejb = home.create(valObj);
            valObj2 = ejb.getObject();
            valObj.pkid = valObj2.pkid;
            if (valObj.vecSerialObj != null) {
                for (int cnt = 0; cnt < valObj.vecSerialObj.size(); cnt++) {
                    SerialNumberDeltaObject sndObj = (SerialNumberDeltaObject) valObj.vecSerialObj.get(cnt);
                    sndObj.quantity = new BigDecimal(valObj.quantity.signum());
                    sndObj.stockId = valObj.stockId;
                    sndObj.stockDelta = valObj.pkid;
                    sndObj.entityTable = valObj.entityTable;
                    sndObj.entityId = valObj.entityId;
                    sndObj.docTable = valObj.docTable;
                    sndObj.docKey = valObj.docKey;
                    SerialNumberDeltaNut.fnCreate(sndObj);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ejb;
    }

    public static Collection getCollectionByField(String fieldName, String value) {
        Collection colObjects = null;
        StockDeltaHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(fieldName, value);
        } catch (Exception ex) {
            Log.printDebug("StockDeltaNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static Vector getValueObjectsGiven(Integer stockId, String status) {
        try {
            return getValueObjectsGiven((String) null, (String) null, (String) null, (String) null, (Integer) null, (Integer) null, stockId, (Integer) null, (Integer) null, (String) null, (String) null, (Timestamp) null, (Timestamp) null, (String) null, (Integer) null, (String) null, (Long) null, (String) null, status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Collection getColByStkIdAndStatus(Integer stockId, String status) throws Exception {
        StockDeltaHome lEJBHome = getHome();
        Collection colEJBs = null;
        String field_StockId = new String(StockDeltaBean.STOCKID);
        String field_Status = new String(StockDeltaBean.STATUS);
        HashMap mapOfFields = new HashMap();
        mapOfFields.put(field_StockId, stockId.toString());
        mapOfFields.put(field_Status, status);
        colEJBs = (Collection) lEJBHome.findObjectsGiven(mapOfFields);
        return colEJBs;
    }

    public static Collection getFIFOByStkIdAndStatus(Integer stockId, String status) throws Exception {
        StockDeltaHome lEJBHome = getHome();
        Collection colEJBs = null;
        String field_StockId = new String(StockDeltaBean.STOCKID);
        String field_Status = new String(StockDeltaBean.STATUS);
        HashMap mapOfFields = new HashMap();
        mapOfFields.put(field_StockId, stockId.toString());
        mapOfFields.put(field_Status, status);
        colEJBs = (Collection) lEJBHome.findFIFOGiven(mapOfFields);
        return colEJBs;
    }

    public static BigDecimal getNetMovement(Integer stockId, Timestamp dateAfterOrEqual, Timestamp dateBefore, String field1, String value1, String field2, String value2, String field3, String fuzzy3, String orderBy) {
        BigDecimal bdNetMv = null;
        try {
            StockDeltaHome home = getHome();
            bdNetMv = home.getNetMovement(stockId, dateAfterOrEqual, dateBefore, field1, value1, field2, value2, field3, fuzzy3, orderBy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bdNetMv;
    }

    public static Vector getValueObjectsGiven(String namespace, String txnType, String txnCode, String serial, Integer personInCharge, Integer processNode, Integer stockId, Integer refStockId, Integer itemId, String currency1, String currency2, Timestamp txnTimeAfterOrEqual, Timestamp txnTimeBefore, String entityTable, Integer entityId, String docTable, Long docKey, String state, String status) {
        Vector vecObj = null;
        try {
            StockDeltaHome home = getHome();
            vecObj = home.getValueObjectsGiven(namespace, txnType, txnCode, serial, personInCharge, processNode, stockId, refStockId, itemId, currency1, currency2, txnTimeAfterOrEqual, txnTimeBefore, entityTable, entityId, docTable, docKey, state, status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecObj;
    }

    public static StockDeltaObject getObject(Long pkid) {
        StockDeltaObject valObj = null;
        try {
            StockDelta ejb = getHandle(pkid);
            valObj = ejb.getObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return valObj;
    }

    public static void setObject(StockDeltaObject valObj) {
        try {
            StockDelta ejb = getHandle(valObj.pkid);
            ejb.setObject(valObj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void fnRemove(Long pkid) {
        try {
            StockDeltaHome home = getHome();
            home.remove(pkid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void cancelTxn(String docTable, Long docKey, String remarks, Integer usrId) throws InventoryException {
        try {
            String field_DocTable = new String(StockDeltaBean.DOC_TABLE);
            String field_DocKey = new String(StockDeltaBean.DOC_KEY);
            String field_Status = new String(StockDeltaBean.STATUS);
            HashMap mapOfFields = new HashMap();
            mapOfFields.put(field_DocTable, docTable);
            mapOfFields.put(field_DocKey, docKey.toString());
            mapOfFields.put(field_Status, StockDeltaBean.STATUS_ACTIVE);
            Collection colStockDeltas = (Collection) getHome().findObjectsGiven(mapOfFields);
            StockDelta stkDelta = null;
            if (colStockDeltas.iterator().hasNext()) stkDelta = (StockDelta) colStockDeltas.iterator().next();
            StockDeltaObject editStkDeltaObj = stkDelta.getObject();
            String username = UserNut.getUserName(usrId);
            if (username == null) username = "";
            int remarksLen = StockDeltaBean.MAX_LEN_REMARKS;
            editStkDeltaObj.status = StockDeltaBean.STATUS_CANCELLED;
            editStkDeltaObj.remarks = remarks + "(" + username + "): " + editStkDeltaObj.remarks;
            if (editStkDeltaObj.remarks.length() > remarksLen) {
                editStkDeltaObj.remarks = editStkDeltaObj.remarks.substring(0, remarksLen);
            }
            editStkDeltaObj.timeEdit = TimeFormat.getTimestamp();
            stkDelta.setObject(editStkDeltaObj);
        } catch (Exception ex) {
            String errMsg = "Failed to cancelTxn for " + "(" + docTable + ", " + docKey.toString() + ") " + " : " + ex.getMessage();
            throw new InventoryException(errMsg);
        }
    }

    public static void fnWeightedAverageMonthlyRecalculation(String itemCode, String locationId, String remarks, Integer userId) throws Exception {
        ItemObject itmObj = null;
        try {
            Item itmEJB = ItemNut.getObjectByCode(itemCode);
            itmObj = itmEJB.getObject();
        } catch (Exception ex) {
            throw new Exception(" Invalid Item Code!! ");
        }
        Integer iLoc = new Integer(locationId);
        Stock stkEJB = StockNut.getObjectBy(itmObj.pkid, iLoc, new Integer(StockNut.STK_COND_GOOD), "");
        StockObject stkObj = null;
        try {
            stkObj = stkEJB.getObject();
        } catch (Exception ex) {
            throw new Exception(" The is no such stock at this location. ");
        }
        LocationObject locObj = null;
        try {
            Integer locId = new Integer(locationId);
            if (locId != null) locObj = LocationNut.getValueObject(locId);
        } catch (Exception ex) {
            throw new Exception("Please select a valid location");
        }
        Vector vecDelta = getValueObjectsGiven(stkObj.pkid, StockDeltaBean.STATUS_ACTIVE);
        for (int cnt1 = 0; cnt1 < vecDelta.size(); cnt1++) {
            StockDeltaObject dObj = (StockDeltaObject) vecDelta.get(cnt1);
            if (dObj.txnType.equals(StockDeltaBean.TT_FILTER_WA_MONTHLY)) {
                try {
                    StockDelta dEJB = getHandle(dObj.pkid);
                    dEJB.remove();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new Exception("Unable to delete previous Weighted Average Filter Adjustment!!");
                }
            }
        }
        if (vecDelta.size() > 0) {
            StockDeltaObject dObj1 = (StockDeltaObject) vecDelta.get(0);
            Timestamp startMonth = dObj1.txnTime;
            startMonth = TimeFormat.set(startMonth, Calendar.DATE, 1);
            Timestamp thisMonth = TimeFormat.getTimestamp();
            thisMonth = TimeFormat.set(thisMonth, Calendar.DATE, 1);
            Timestamp bufMonth = startMonth;
            do {
                WeightedAverageStockReport maRpt = new WeightedAverageStockReport(bufMonth, TimeFormat.add(bufMonth, 0, 1, 0), locObj, (Integer) null, (String) null, StockDeltaBean.TT_FILTER_WA_MONTHLY);
                maRpt.stockId = stkObj.pkid;
                maRpt = StockNut.getReport(maRpt);
                BigDecimal openAmt = (BigDecimal) maRpt.vecOpeningAmt.get(0);
                BigDecimal openQty = (BigDecimal) maRpt.vecOpeningQty.get(0);
                BigDecimal closeQty = (BigDecimal) maRpt.vecClosingQty.get(0);
                BigDecimal purchaseAmt = (BigDecimal) maRpt.vecPurchaseAmt.get(0);
                BigDecimal purchaseQty = (BigDecimal) maRpt.vecPurchaseQty.get(0);
                BigDecimal purchaseReturnAmt = (BigDecimal) maRpt.vecPurchaseReturnAmt.get(0);
                BigDecimal purchaseReturnQty = (BigDecimal) maRpt.vecPurchaseReturnQty.get(0);
                BigDecimal adjAmt = (BigDecimal) maRpt.vecAdjustmentAmt.get(0);
                BigDecimal adjQty = (BigDecimal) maRpt.vecAdjustmentQty.get(0);
                BigDecimal filterAmt = (BigDecimal) maRpt.vecFilterAmt.get(0);
                BigDecimal filterQty = (BigDecimal) maRpt.vecFilterQty.get(0);
                BigDecimal expectedClosingUAmt = openAmt.add(purchaseAmt).add(purchaseReturnAmt).add(adjAmt).add(filterAmt);
                BigDecimal purRetAdjFilQty = openQty.add(purchaseQty).add(purchaseReturnQty).add(adjQty).add(filterQty);
                if (closeQty.signum() != 0) {
                    expectedClosingUAmt = expectedClosingUAmt.divide(purRetAdjFilQty, 4, 0);
                } else {
                    expectedClosingUAmt = new BigDecimal("0.00");
                }
                fnWeightedAverageMonthlyAdjustment(TimeFormat.add(bufMonth, 0, 1, 0), itemCode, locationId, remarks, closeQty, expectedClosingUAmt, userId, StockDeltaBean.TT_FILTER_WA_MONTHLY);
                bufMonth = TimeFormat.add(bufMonth, 0, 1, 0);
            } while (bufMonth.getTime() < thisMonth.getTime());
        }
    }

    public static void fnWeightedAverageMonthlyAdjustment(Timestamp tsMonth, String itemCode, String locationId, String remarks, BigDecimal newOpenQty, BigDecimal newOpenUAmt, Integer userId, String appendFilterType) throws Exception {
        BigDecimal ZERO = new BigDecimal("0");
        remarks = "Auto Adjusted Monthly Weighted Average. " + remarks;
        Timestamp txnTimeAfterOrEqual = TimeFormat.set(tsMonth, Calendar.DATE, 1);
        Timestamp txnTimeBefore = TimeFormat.add(txnTimeAfterOrEqual, 0, 1, 0);
        ItemObject itmObj = null;
        try {
            Item itmEJB = ItemNut.getObjectByCode(itemCode);
            itmObj = itmEJB.getObject();
        } catch (Exception ex) {
            throw new Exception(" Invalid Item Code!! ");
        }
        Integer iLoc = new Integer(locationId);
        Stock stkEJB = StockNut.getObjectBy(itmObj.pkid, iLoc, new Integer(StockNut.STK_COND_GOOD), "");
        StockObject stkObj = null;
        try {
            stkObj = stkEJB.getObject();
        } catch (Exception ex) {
            throw new Exception(" The is no such stock at this location. ");
        }
        LocationObject locObj = null;
        try {
            Integer locId = new Integer(locationId);
            if (locId != null) locObj = LocationNut.getValueObject(locId);
        } catch (Exception ex) {
            throw new Exception("Please select a valid location");
        }
        String sortBy = "item_code";
        Integer qs = null;
        WeightedAverageStockReport maRpt = new WeightedAverageStockReport(txnTimeAfterOrEqual, txnTimeBefore, locObj, qs, sortBy, StockDeltaBean.TT_FILTER_WA_MONTHLY);
        maRpt.stockId = stkObj.pkid;
        maRpt = StockNut.getReport(maRpt);
        Timestamp adjDate = TimeFormat.add(maRpt.startDate, 0, 0, -1);
        BigDecimal openAmt = (BigDecimal) maRpt.vecOpeningAmt.get(0);
        BigDecimal openQty = (BigDecimal) maRpt.vecOpeningQty.get(0);
        BigDecimal ppAmt = (BigDecimal) maRpt.vecPrevPurchaseAmt.get(0);
        BigDecimal ppQty = (BigDecimal) maRpt.vecPrevPurchaseQty.get(0);
        BigDecimal dQty = newOpenQty.subtract(openQty);
        BigDecimal dAmt = newOpenUAmt.multiply(newOpenQty).subtract(openAmt);
        if (dQty.signum() == 0 && dAmt.signum() == 0) {
            return;
        }
        String ccy = null;
        try {
            Vector vecDelta = new Vector(StockDeltaNut.getCollectionByField(StockDeltaBean.STOCKID, stkObj.pkid.toString()));
            if (vecDelta.size() > 0) {
                StockDelta stkDeltaEJB = (StockDelta) vecDelta.get(0);
                StockDeltaObject stkDeltaObj = stkDeltaEJB.getObject();
                ccy = stkDeltaObj.currency;
            }
        } catch (Exception ex) {
        }
        ItemPrice itmPxEJB = ItemPriceNut.getObjectByItemAndCond(itmObj.pkid, new Integer(StockNut.STK_COND_GOOD));
        try {
            ccy = itmPxEJB.getCurrency();
        } catch (Exception ex) {
        }
        if (ccy == null) {
            ccy = "MYR";
        }
        StockDeltaObject stkDeltaObj0 = null;
        if (dQty.signum() != 0) {
            stkDeltaObj0 = new StockDeltaObject();
            stkDeltaObj0.namespace = StockDeltaBean.NS_INVENTORY;
            stkDeltaObj0.txnType = appendFilterType;
            stkDeltaObj0.personInCharge = userId;
            stkDeltaObj0.stockId = stkObj.pkid;
            stkDeltaObj0.itemId = stkObj.itemId;
            stkDeltaObj0.quantity = dQty;
            stkDeltaObj0.currency = ccy;
            stkDeltaObj0.unitPrice = ZERO;
            stkDeltaObj0.txnTime = adjDate;
            stkDeltaObj0.remarks = remarks;
            stkDeltaObj0.docTable = "";
            stkDeltaObj0.docKey = new Long("0");
            stkDeltaObj0.status = StockDeltaBean.STATUS_ACTIVE;
            stkDeltaObj0.userIdEdit = userId;
            stkDeltaObj0.timeEdit = TimeFormat.getTimestamp();
        }
        ppQty = ppQty.add(dQty);
        BigDecimal bdPriceDelta = newOpenUAmt;
        if (ppQty.signum() != 0) {
            bdPriceDelta = bdPriceDelta.subtract(ppAmt.divide(ppQty, 4, 0));
        }
        StockDeltaObject stkDeltaObj1 = null;
        StockDeltaObject stkDeltaObj2 = null;
        if (dAmt.signum() != 0) {
            stkDeltaObj1 = new StockDeltaObject();
            stkDeltaObj1.namespace = StockDeltaBean.NS_INVENTORY;
            stkDeltaObj1.txnType = appendFilterType;
            stkDeltaObj1.personInCharge = userId;
            stkDeltaObj1.stockId = stkObj.pkid;
            stkDeltaObj1.itemId = stkObj.itemId;
            stkDeltaObj1.quantity = ppQty;
            stkDeltaObj1.currency = ccy;
            stkDeltaObj1.unitPrice = bdPriceDelta;
            stkDeltaObj1.txnTime = adjDate;
            stkDeltaObj1.remarks = remarks;
            stkDeltaObj1.docTable = "";
            stkDeltaObj1.docKey = new Long("0");
            stkDeltaObj1.status = StockDeltaBean.STATUS_ACTIVE;
            stkDeltaObj1.userIdEdit = userId;
            stkDeltaObj1.timeEdit = TimeFormat.getTimestamp();
            stkDeltaObj2 = new StockDeltaObject();
            stkDeltaObj2.namespace = StockDeltaBean.NS_INVENTORY;
            stkDeltaObj2.txnType = appendFilterType;
            stkDeltaObj2.personInCharge = userId;
            stkDeltaObj2.stockId = stkObj.pkid;
            stkDeltaObj2.itemId = stkObj.itemId;
            stkDeltaObj2.quantity = ppQty.negate();
            stkDeltaObj2.currency = ccy;
            stkDeltaObj2.unitPrice = ZERO;
            stkDeltaObj2.txnTime = adjDate;
            stkDeltaObj2.remarks = remarks;
            stkDeltaObj2.docTable = "";
            stkDeltaObj2.docKey = new Long("0");
            stkDeltaObj2.status = StockDeltaBean.STATUS_ACTIVE;
            stkDeltaObj2.userIdEdit = userId;
            stkDeltaObj2.timeEdit = TimeFormat.getTimestamp();
        }
        try {
            if (stkDeltaObj0 != null) {
                StockNut.execTxn(stkDeltaObj0);
            }
            if (stkDeltaObj1 != null) {
                StockNut.execTxn(stkDeltaObj1);
            }
            if (stkDeltaObj2 != null) {
                StockNut.execTxn(stkDeltaObj2);
            }
        } catch (Exception ex) {
            throw new Exception("Unable to update balances!");
        }
    }

    public static StockSalesReportType02Session.Report getReport(StockSalesReportType02Session.Report theReport) {
        try {
            StockDeltaHome home = getHome();
            home.getReport(theReport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return theReport;
    }

    public static StockSalesReportType02Session.Report getReportForSearchBasic(StockSalesReportType02Session.Report theReport) {
        try {
            StockDeltaHome home = getHome();
            home.GetReportForSearchBasic(theReport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return theReport;
    }

    public static Vector getObjectsByDocument(String docTable, Long docId) {
        QueryObject query = new QueryObject(new String[] { StockDeltaBean.DOC_KEY + " = '" + docId.toString() + "' ", StockDeltaBean.DOC_TABLE + " = '" + docTable + "' " });
        Vector vecResult = new Vector(getObjects(query));
        return vecResult;
    }

    public static Collection getObjects(QueryObject query) {
        Collection colObj = null;
        try {
            StockDeltaHome home = getHome();
            colObj = new Vector(home.getObjects(query));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return colObj;
    }

    public static StockPurchasePODetailsReportForm.Report getPurchaseJournalReport(StockPurchasePODetailsReportForm.Report theReport) {
        try {
            StockDeltaHome home = getHome();
            home.getReport(theReport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return theReport;
    }

    public static StockPurchaseReportType01Session.Report getReport(StockPurchaseReportType01Session.Report theReport) {
        try {
            StockDeltaHome home = getHome();
            home.getReport(theReport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return theReport;
    }

    public static StockTradeInReportSession.Report getReport(StockTradeInReportSession.Report theReport) {
        try {
            StockDeltaHome home = getHome();
            home.getReport(theReport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return theReport;
    }

    public static Vector getReport(StockBalanceMovementSummaryReport theReport) {
        Vector vecRow = null;
        try {
            StockDeltaHome home = getHome();
            vecRow = home.getReport(theReport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecRow;
    }

    public static Vector getAgingReport(Integer pcCenter, Timestamp endDate) {
        Vector vecResult = new Vector();
        try {
            StockDeltaHome home = getHome();
            vecResult = home.getAgingReport(pcCenter, endDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }

    public static Vector getStockSalesByItemCode(Timestamp tsFrom, Timestamp tsTo, LocationObject locObj, boolean filterCategory0, Integer idCategory0, boolean filterCategory1, String category1, boolean filterCategory2, String category2, boolean filterCategory3, String category3, boolean filterCategory4, String category4, boolean filterCategory5, String category5, String orderBy) {
        Vector vecResult = new Vector();
        try {
            Log.printVerbose("... dt nut .. 1");
            StockDeltaHome home = getHome();
            Log.printVerbose("... dt nut .. 2");
            vecResult = home.getStockSalesByItemCode(tsFrom, tsTo, locObj, filterCategory0, idCategory0, filterCategory1, category1, filterCategory2, category2, filterCategory3, category3, filterCategory4, category4, filterCategory5, category5, orderBy);
            Log.printVerbose("... dt nut .. 3");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }

    public static Vector getHistoricalStockBalance(Timestamp asAt, Integer locationId, QueryObject itemQuery) {
        Vector vecResult = new Vector();
        try {
            StockDeltaHome home = getHome();
            vecResult = home.getHistoricalStockBalance(asAt, locationId, itemQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }

    public static TreeMap getStockBalanceByTimestamp(Timestamp asAt, Integer locationId, String itemEan) {
        TreeMap treeResult = new TreeMap();
        try {
            StockDeltaHome home = getHome();
            treeResult = home.getBalanceByTimestamp(asAt, locationId, itemEan);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return treeResult;
    }

    public static Vector getAdjustmentListing(Timestamp bsAt, Timestamp asAt, Integer locationId) {
        Vector vecResult = new Vector();
        try {
            StockDeltaHome home = getHome();
            vecResult = home.getAdjustmentListing(bsAt, asAt, locationId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }

    public static Vector getItemStockMovementReport(Timestamp dateFrom, Timestamp dateTo, Integer locationId, QueryObject itemQuery) {
        Vector vecResult = new Vector();
        try {
            StockDeltaHome home = getHome();
            vecResult = home.getItemStockMovementReport(dateFrom, dateTo, locationId, itemQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }

    public static SalesReportBySalesman getSalesReportBySalesman(SalesReportBySalesman srbs) {
        try {
            StockDeltaHome home = getHome();
            srbs = home.getSalesReportBySalesman(srbs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return srbs;
    }

    public static MultiBranchDailySalesPurchasesCollectionReport.Result getDailySalesPurchasesCollection(TreeMap treeBranches, Timestamp dateFrom, Timestamp dateTo) {
        MultiBranchDailySalesPurchasesCollectionReport.Result result = new MultiBranchDailySalesPurchasesCollectionReport.Result();
        try {
            StockDeltaHome home = getHome();
            result = home.getDailySalesPurchasesCollection(treeBranches, dateFrom, dateTo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static DailyGrossProfitBySalesmanReport.Result getDailyGrossProfitBySalesman(Timestamp dateFrom, Timestamp dateTo, boolean filterSalesman, Integer salesmanId, boolean filterBranch, Integer branchId) {
        DailyGrossProfitBySalesmanReport.Result result = new DailyGrossProfitBySalesmanReport.Result();
        try {
            StockDeltaHome home = getHome();
            result = home.getDailyGrossProfitBySalesmanReport(dateFrom, dateTo, filterSalesman, salesmanId, filterBranch, branchId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static StockBalErrorChecking getStockBalError(StockBalErrorChecking sbec) {
        try {
            StockDeltaHome home = getHome();
            sbec = home.getStockBalError(sbec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sbec;
    }

    public static StockSalesByItemCodeDetailsReport getStockSalesByItemCodeDetailsReport(StockSalesByItemCodeDetailsReport ssic) {
        try {
            StockDeltaHome home = getHome();
            ssic = home.getStockSalesByItemCodeDetailsReport(ssic);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ssic;
    }

    public static SalesReportByDailyWeeklyMonthlySession getSalesReportByDailyWeeklyMonthlySession(SalesReportByDailyWeeklyMonthlySession sr) {
        try {
            StockDeltaHome home = getHome();
            sr = home.getSalesReportByDailyWeeklyMonthlySession(sr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sr;
    }

    public static BigDecimal getNetMovementMultiLocation(Integer itemId, Timestamp dateBefore, String location) {
        BigDecimal bdNetMv = null;
        try {
            StockDeltaHome home = getHome();
            bdNetMv = home.getNetMovementMultiLocation(itemId, dateBefore, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bdNetMv;
    }

    public static Vector getStockMovementReport(Integer itemId, Timestamp dateFrom, Timestamp dateTo, String location) {
        Vector vecResult = new Vector();
        try {
            StockDeltaHome home = getHome();
            vecResult = home.getStockMovementReport(itemId, dateFrom, dateTo, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }
}
