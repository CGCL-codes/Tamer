package com.vlee.ejb.supplier;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import java.sql.*;
import javax.naming.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;

public class SuppUserDetailsNut {

    private static String strClassName = "SuppUserDetailsNut";

    public static SuppUserDetailsHome getHome() {
        try {
            Context lContext = new InitialContext();
            SuppUserDetailsHome lEJBHome = (SuppUserDetailsHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/supplier/SuppUserDetails"), SuppUserDetailsHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static SuppUserDetails getHandle(Integer pkid) {
        return (SuppUserDetails) getHandle(getHome(), pkid);
    }

    public static SuppUserDetails getHandle(SuppUserDetailsHome lEJBHome, Integer pkid) {
        try {
            return (SuppUserDetails) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static Collection getCollectionByField(String fieldName, String value) {
        Collection colObjects = null;
        SuppUserDetailsHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(fieldName, value);
        } catch (Exception ex) {
            Log.printDebug("SuppUserDetailsNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getAllObjects() {
        Collection colObjects = null;
        SuppUserDetailsHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findAllObjects();
        } catch (Exception ex) {
            Log.printDebug("SuppUserDetailsNut: " + ex.getMessage());
        }
        return colObjects;
    }

    public static SuppUserDetails getObjectByUserId(Integer usrId) {
        Log.printVerbose("In SuppUserNut::getObjectByName(String name)");
        String fieldName = new String(SuppUserDetailsBean.SUPP_USERID);
        Collection colSuppUserDetails = getCollectionByField(fieldName, usrId.toString());
        Iterator itrSuppUserDetails = colSuppUserDetails.iterator();
        SuppUserDetails rtnSuppUserDetails = null;
        if (itrSuppUserDetails.hasNext()) rtnSuppUserDetails = (SuppUserDetails) itrSuppUserDetails.next();
        return rtnSuppUserDetails;
    }
}
