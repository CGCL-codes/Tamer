package com.vlee.ejb.supplier;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import javax.naming.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;
import com.vlee.bean.supplier.*;

public class GoodsReceivedNoteItemNut {

    private static String strClassName = "GoodsReceivedNoteItemNut";

    public static GoodsReceivedNoteItemHome getHome() {
        try {
            Context lContext = new InitialContext();
            GoodsReceivedNoteItemHome lEJBHome = (GoodsReceivedNoteItemHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/supplier/GoodsReceivedNoteItem"), GoodsReceivedNoteItemHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static GoodsReceivedNoteItem getHandle(Long pkid) {
        return (GoodsReceivedNoteItem) getHandle(getHome(), pkid);
    }

    public static GoodsReceivedNoteItem getHandle(GoodsReceivedNoteItemHome lEJBHome, Long pkid) {
        try {
            return (GoodsReceivedNoteItem) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static GoodsReceivedNoteItemObject getObject(Long pkid) {
        GoodsReceivedNoteItemObject valObj = null;
        GoodsReceivedNoteItem ejb = getHandle(pkid);
        try {
            valObj = ejb.getObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return valObj;
    }

    public static GoodsReceivedNoteItem fnCreate(GoodsReceivedNoteItemObject grniObj, GoodsReceivedNoteObject grnObj) {
        GoodsReceivedNoteItemHome lEJBHome = getHome();
        GoodsReceivedNoteItem ejbObject = null;
        try {
            grniObj.mGoodsReceivedNoteId = grnObj.mPkid;
            ejbObject = lEJBHome.create(grniObj);
            grniObj.setPkid(ejbObject.getPkid());
            ItemObject itemObj = ItemNut.getObject(grniObj.mItemId);
            StockNut.purchase(grnObj.mApproverId, grniObj.mItemId, grnObj.mLocationId, grnObj.mPCCenter, grniObj.mTotalQty, grniObj.mNetPrice, grniObj.mCurrency1, GoodsReceivedNoteItemBean.TABLENAME, grniObj.mPkid, grnObj.mRemarks + " " + grnObj.mReferenceNo, grnObj.mTimeComplete, grnObj.mApproverId, grniObj.vecSerialObj, "", "", "", "", grnObj.mSuppProcCtrId, itemObj.priceList, itemObj.priceSale, itemObj.priceDisc1, itemObj.priceDisc2, itemObj.priceDisc3);
            PurchaseOrderItem poiEJB = PurchaseOrderItemNut.getHandle(grniObj.mPurchaseOrderItemId);
            if (poiEJB != null) {
                PurchaseOrderItemObject poiObj = poiEJB.getObject();
                BigDecimal qty = poiObj.mOutstandingQty.subtract(grniObj.mTotalQty);
                if (qty.signum() < 0) {
                    qty = new BigDecimal(0);
                }
                poiObj.mOutstandingQty = qty;
                poiObj.mReceivingQty = new Integer(poiObj.mReceivingQty.intValue() + grniObj.mTotalQty.intValue());
                poiEJB.setObject(poiObj);
            }
            return ejbObject;
        } catch (Exception ex) {
            ex.printStackTrace();
            return (GoodsReceivedNoteItem) null;
        }
    }

    public static Collection getObjectsForGoodsReceivedNote(Long grnId) {
        Collection colObj = null;
        QueryObject query = new QueryObject(new String[] { GoodsReceivedNoteItemBean.GRN_ID + " = '" + grnId.toString() + "' " });
        query.setOrder("ORDER BY pkid");
        try {
            GoodsReceivedNoteItemHome home = getHome();
            colObj = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return colObj;
    }

    public static PurchaseRecordsBySupplierReport.Report getPurchaseRecordsBySupplierReport(PurchaseRecordsBySupplierReport.Report srbcr) {
        try {
            GoodsReceivedNoteItemHome home = getHome();
            srbcr = home.getPurchaseRecordsBySupplierReport(srbcr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return srbcr;
    }

    public static void removeAllForGRN(Long goodsReceivedNoteID) {
        GoodsReceivedNoteItemHome lEJBHome = getHome();
        try {
            Collection colObjects = getCollectionByField(GoodsReceivedNoteItemBean.GRN_ID, goodsReceivedNoteID.toString());
            int count = 1;
            for (Iterator itr = colObjects.iterator(); itr.hasNext(); ) {
                Log.printVerbose("Removing item " + count);
                GoodsReceivedNoteItem poItem = (GoodsReceivedNoteItem) itr.next();
                poItem.remove();
                count++;
            }
        } catch (Exception ex) {
            Log.printDebug("GoodsReceivedNoteItemNut:" + ex.getMessage());
        }
    }

    public static Collection getCollectionByField(String fieldName, String value) {
        Collection colObjects = null;
        GoodsReceivedNoteItemHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(fieldName, value);
        } catch (Exception ex) {
            Log.printDebug("GoodsReceivedNoteItemNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getAllObjects() {
        Collection colObjects = null;
        GoodsReceivedNoteItemHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findAllObjects();
        } catch (Exception ex) {
            Log.printDebug("GoodsReceivedNoteItemNut: " + ex.getMessage());
        }
        return colObjects;
    }

    public static BigDecimal getQtyReceived(Long purchaseOrderItemPkid) {
        BigDecimal qtyReceivedSoFar = new BigDecimal("0.0");
        try {
            GoodsReceivedNoteItemHome grnItemHome = GoodsReceivedNoteItemNut.getHome();
            qtyReceivedSoFar = grnItemHome.getQtyReceivedGivenPOItem(purchaseOrderItemPkid);
            Log.printVerbose("GRNItemNut: qtyReceived=" + qtyReceivedSoFar.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printVerbose("GoodsReceivedNoteItemNut: " + ex.getMessage());
        }
        return qtyReceivedSoFar;
    }
}
