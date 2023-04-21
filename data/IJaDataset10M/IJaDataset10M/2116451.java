package com.vlee.ejb.inventory;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import java.math.BigDecimal;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.inventory.*;

public class BOMNut {

    private static String strClassName = "BOMNut";

    public static BOMHome getHome() {
        try {
            Context lContext = new InitialContext();
            BOMHome lEJBHome = (BOMHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/inventory/BOM"), BOMHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static BOM getHandle(Integer pkid) {
        return (BOM) getHandle(getHome(), pkid);
    }

    public static BOM getHandle(BOMHome lEJBHome, Integer pkid) {
        try {
            return (BOM) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static BOM fnCreate(BOMObject valObj) {
        BOM ejb = null;
        BOMHome home = getHome();
        try {
            ejb = home.create(valObj);
            valObj.pkid = ejb.getPkid();
            return ejb;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("BOMNut: " + "Cannot create this BOM");
            return (BOM) null;
        }
    }

    public static BOMObject getObject(Integer lPkid) {
        BOMObject valueObj = null;
        BOM objEJB = getHandle(lPkid);
        try {
            valueObj = objEJB.getObject();
            valueObj.vecLink.clear();
            QueryObject query = new QueryObject(new String[] { BOMLinkBean.BOM_ID + " = '" + valueObj.pkid.toString() + "' " });
            query.setOrder(" ORDER BY " + BOMLinkBean.PKID);
            valueObj.vecLink = new Vector(BOMLinkNut.getObjects(query));
        } catch (Exception ex) {
            Log.printDebug(ex.getMessage());
        }
        return (BOMObject) valueObj;
    }

    public static Collection getObjects(QueryObject query) {
        Collection result = new Vector();
        try {
            BOMHome home = getHome();
            result = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void fnRemove(Integer bomPkid) {
        try {
            BOMObject bomObj = getObject(bomPkid);
            if (bomObj == null) {
                return;
            }
            for (int cnt = 0; cnt < bomObj.vecLink.size(); cnt++) {
                try {
                    BOMLinkObject blObj = (BOMLinkObject) bomObj.vecLink.get(cnt);
                    BOMLink blEJB = BOMLinkNut.getHandle(blObj.pkid);
                    blEJB.remove();
                } catch (Exception ex) {
                }
            }
            BOM bomEJB = getHandle(bomPkid);
            bomEJB.remove();
        } catch (Exception ex) {
        }
    }

    public static Vector fnFuzzySearch(String keyword) {
        Vector vecBom = new Vector();
        try {
            BOMHome home = getHome();
            vecBom = home.getFuzzySearch(keyword);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecBom;
    }

    public static BOMObject getObjectByCode(String bomCode) {
        BOMObject bomObj = null;
        try {
            QueryObject query = new QueryObject(new String[] { BOMBean.PARENT_ITEM_CODE + " = '" + bomCode + "' " });
            query.setOrder(" ORDER BY " + BOMBean.PKID);
            Vector vecBOM = new Vector(getObjects(query));
            if (vecBOM.size() > 0) {
                bomObj = (BOMObject) vecBOM.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bomObj;
    }
}
