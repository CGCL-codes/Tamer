package com.vlee.ejb.customer;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import java.math.BigDecimal;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.accounting.*;

public class CustQuotationIndexNut {

    private static String strClassName = "CustQuotationIndexNut";

    public static CustQuotationIndexHome getHome() {
        try {
            Context lContext = new InitialContext();
            CustQuotationIndexHome lEJBHome = (CustQuotationIndexHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/customer/CustQuotationIndex"), CustQuotationIndexHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static CustQuotationIndex getHandle(Long pkid) {
        return (CustQuotationIndex) getHandle(getHome(), pkid);
    }

    public static CustQuotationIndex getHandle(CustQuotationIndexHome lEJBHome, Long pkid) {
        try {
            return (CustQuotationIndex) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
        }
        return null;
    }

    public static CustQuotationIndex fnCreate(CustQuotationIndexObject valObj) {
        CustQuotationIndex ejb = null;
        CustQuotationIndexHome home = getHome();
        try {
            ejb = home.create(valObj);
            valObj.pkid = ejb.getPkid();
            valObj.stmtNo = ejb.getStmtNo();
            Log.printVerbose("Inside CustQuotationIndex fnCreate");
            for (int cnt = 0; cnt < valObj.vecItem.size(); cnt++) {
                CustQuotationItemObject itmObj = (CustQuotationItemObject) valObj.vecItem.get(cnt);
                itmObj.indexId = valObj.pkid;
                itmObj.currency1 = valObj.currency;
                CustQuotationItem itmEJB = CustQuotationItemNut.fnCreate(itmObj);
            }
            Log.printVerbose("Inside CustQuotationIndex fnCreate: Assigne values to CustQuotationItemObject");
            return ejb;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("CustQuotationIndexNut: " + "Cannot create this CustQuotationIndex");
            return (CustQuotationIndex) null;
        }
    }

    public static CustQuotationIndexObject getObject(Long lPkid) {
        CustQuotationIndexObject valueObj = null;
        CustQuotationIndex objEJB = getHandle(lPkid);
        try {
            valueObj = objEJB.getObject();
        } catch (Exception ex) {
            Log.printDebug(ex.getMessage());
        }
        return (CustQuotationIndexObject) valueObj;
    }

    public static CustQuotationIndexObject getObjectTree(Long lPkid) {
        CustQuotationIndexObject valueObj = getObject(lPkid);
        if (valueObj == null) {
            return (CustQuotationIndexObject) null;
        }
        QueryObject query = new QueryObject(new String[] { CustQuotationItemBean.INDEX_ID + " = '" + lPkid.toString() + "' " });
        query.setOrder(" ORDER BY " + CustQuotationItemBean.PKID);
        valueObj.vecItem = new Vector(CustQuotationItemNut.getObjects(query));
        return valueObj;
    }

    public static Collection getObjects(QueryObject query) {
        Collection result = new Vector();
        try {
            CustQuotationIndexHome home = getHome();
            result = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Vector getObjectsWithDocumentProcessingItem(String dateFilter, Timestamp dateFrom, Timestamp dateTo, String processType, String category, String docRef) {
        Vector vecResult = new Vector();
        try {
            CustQuotationIndexHome home = getHome();
            vecResult = home.getObjectsWithDocumentProcessingItem(dateFilter, dateFrom, dateTo, processType, category, docRef);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }
}
