package com.vlee.ejb.customer;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.supplier.*;

public class VehicleNut {

    private static String strClassName = "VehicleNut";

    public static VehicleHome getHome() {
        try {
            Context lContext = new InitialContext();
            VehicleHome lEJBHome = (VehicleHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/customer/Vehicle"), VehicleHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Vehicle getHandle(Integer pkid) {
        return (Vehicle) getHandle(getHome(), pkid);
    }

    public static Vehicle getHandle(VehicleHome lEJBHome, Integer pkid) {
        try {
            return (Vehicle) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static Vehicle fnCreate(VehicleObject valObj) {
        VehicleHome home = getHome();
        Vehicle jsItmEJB = null;
        Log.printVerbose(" Inside VehicleNut!! ");
        try {
            jsItmEJB = home.create(valObj);
            valObj.pkid = jsItmEJB.getPkid();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsItmEJB;
    }

    public static VehicleObject getObject(Integer pkid) {
        Vehicle ejb = getHandle(pkid);
        VehicleObject valObj = null;
        try {
            valObj = ejb.getObject();
        } catch (Exception ex) {
        }
        return valObj;
    }

    public static Collection getObjects(QueryObject query) {
        Collection col = null;
        try {
            VehicleHome home = getHome();
            col = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
    }

    public static VehicleObject getObjectByRegnum(String regnum) {
        QueryObject query = new QueryObject(new String[] { VehicleBean.REGNUM + " = '" + regnum + "' " });
        query.setOrder(" ORDER BY " + VehicleBean.PKID);
        Vector result = new Vector(getObjects(query));
        if (result.size() > 0) {
            VehicleObject vehObj = (VehicleObject) result.get(0);
            return vehObj;
        }
        return (VehicleObject) null;
    }

    public static Collection getObjectsForCustAccount(Integer accountId) {
        QueryObject query = new QueryObject(new String[] { VehicleBean.ACCOUNT_ID + " = '" + accountId.toString() + "' " });
        query.setOrder(" ORDER BY " + VehicleBean.REGNUM);
        Collection result = getObjects(query);
        return result;
    }
}
