package com.vlee.ejb.supplier;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import javax.naming.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;

public class SuppCompanyPhoneNut {

    private static String strClassName = "SuppCompanyPhoneNut";

    public static SuppCompanyPhoneHome getHome() {
        try {
            Context lContext = new InitialContext();
            SuppCompanyPhoneHome lEJBHome = (SuppCompanyPhoneHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/supplier/SuppCompanyPhone"), SuppCompanyPhoneHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static SuppCompanyPhone getHandle(Integer pkid) {
        return (SuppCompanyPhone) getHandle(getHome(), pkid);
    }

    public static SuppCompanyPhone getHandle(SuppCompanyPhoneHome lEJBHome, Integer pkid) {
        try {
            return (SuppCompanyPhone) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static Collection getCollectionByField(String fieldName, String value) {
        Collection colObjects = null;
        SuppCompanyPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(fieldName, value);
        } catch (Exception ex) {
            Log.printDebug("SuppCompanyPhoneNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getAllObjects() {
        Collection colObjects = null;
        SuppCompanyPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findAllObjects();
        } catch (Exception ex) {
            Log.printDebug("SuppCompanyPhoneNut: " + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getActiveObjects() {
        Log.printVerbose("In SuppCompanyPhoneNut::getActiveObjects()");
        Collection colObjects = null;
        SuppCompanyPhoneHome lEJBHome = getHome();
        colObjects = getCollectionByField(SuppCompanyPhoneBean.STATUS, SuppCompanyPhoneBean.STATUS_ACTIVE);
        return colObjects;
    }

    public static Collection getInactiveObjects() {
        Log.printVerbose("In SuppCompanyPhoneNut::getInactiveObjects()");
        Collection colObjects = null;
        SuppCompanyPhoneHome lEJBHome = getHome();
        colObjects = getCollectionByField(SuppCompanyPhoneBean.STATUS, SuppCompanyPhoneBean.STATUS_INACTIVE);
        return colObjects;
    }
}
