package com.vlee.ejb.inventory;

import java.util.Collection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import com.vlee.util.Log;
import com.vlee.util.QueryObject;

public class PricingMatrixNut {

    public static PricingMatrixHome getHome() {
        try {
            Context lContext = new InitialContext();
            PricingMatrixHome lEJBHome = (PricingMatrixHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/inventory/PricingMatrix"), PricingMatrixHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static PricingMatrix getHandle(Integer pkid) {
        return (PricingMatrix) getHandle(getHome(), pkid);
    }

    public static PricingMatrix getHandle(PricingMatrixHome lEJBHome, Integer pkid) {
        try {
            return (PricingMatrix) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static PricingMatrix fnCreate(PricingMatrixObject valObj) {
        PricingMatrix ejb = null;
        PricingMatrixHome home = getHome();
        try {
            ejb = home.create(valObj);
            valObj.pkid = ejb.getPkid();
            return ejb;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("PricingMatrixNut: " + "Cannot create this PricingMatrix");
            return (PricingMatrix) null;
        }
    }

    public static PricingMatrixObject getObject(Integer pkid) {
        PricingMatrix ejb = getHandle(pkid);
        PricingMatrixObject valObj = null;
        try {
            valObj = ejb.getObject();
        } catch (Exception ex) {
        }
        return valObj;
    }

    public static Collection getObjects(QueryObject query) {
        Collection col = null;
        try {
            PricingMatrixHome home = getHome();
            col = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
    }

    public static PricingMatrixObject getObjectByCode(String code) {
        PricingMatrixObject valObj = null;
        try {
            PricingMatrix ejb;
            PricingMatrixHome lEJBHome = getHome();
            Collection coll = lEJBHome.getPkIdByPricingCode(code);
            Integer pkid = (Integer) coll.iterator().next();
            ejb = (PricingMatrix) lEJBHome.findByPrimaryKey(pkid);
            valObj = ejb.getObject();
        } catch (Exception ex) {
            Log.printDebug("PricingMatrixNut:" + ex.getMessage());
        }
        return valObj;
    }
}
