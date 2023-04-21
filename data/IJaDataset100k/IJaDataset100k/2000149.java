package com.vlee.ejb.supplier;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import javax.naming.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;

public class SuppUserPhoneNut {

    private static String strClassName = "SuppUserPhoneNut";

    public static SuppUserPhoneHome getHome() {
        try {
            Context lContext = new InitialContext();
            SuppUserPhoneHome lEJBHome = (SuppUserPhoneHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/supplier/SuppUserPhone"), SuppUserPhoneHome.class);
            return lEJBHome;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    public static SuppUserPhone getHandle(Integer pkid) {
        return (SuppUserPhone) getHandle(getHome(), pkid);
    }

    public static SuppUserPhone getHandle(SuppUserPhoneHome lEJBHome, Integer pkid) {
        try {
            return (SuppUserPhone) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static Collection getCollectionByField(String fieldName, String value) {
        Collection colObjects = null;
        SuppUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(fieldName, value);
        } catch (Exception ex) {
            Log.printDebug("SuppUserPhoneNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static SuppUserPhone getPhoneByUserId(Integer suppUserId) {
        Log.printVerbose("In SuppUserPhoneNut::getObjectByPhone(Integer suppUserId)");
        String fieldSuppUserId = new String(SuppUserPhoneBean.SUPP_USER_ID);
        String fieldPhoneType = new String(SuppUserPhoneBean.PHONETYPE);
        HashMap mapOfFields = new HashMap();
        mapOfFields.put(fieldSuppUserId, suppUserId.toString());
        mapOfFields.put(fieldPhoneType, "phone");
        Collection colObjects = null;
        SuppUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(mapOfFields);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("SuppUserPhoneNut:" + ex.getMessage());
        }
        Iterator itrSuppUserPhone = colObjects.iterator();
        SuppUserPhone rtnSuppUserPhone = null;
        if (itrSuppUserPhone.hasNext()) rtnSuppUserPhone = (SuppUserPhone) itrSuppUserPhone.next();
        return rtnSuppUserPhone;
    }

    public static SuppUserPhone getFaxByUserId(Integer suppUserId) {
        Log.printVerbose("In SuppUserPhoneNut::getObjectByPhone(Integer suppUserId)");
        String fieldSuppUserId = new String(SuppUserPhoneBean.SUPP_USER_ID);
        String fieldPhoneType = new String(SuppUserPhoneBean.PHONETYPE);
        HashMap mapOfFields = new HashMap();
        mapOfFields.put(fieldSuppUserId, suppUserId.toString());
        mapOfFields.put(fieldPhoneType, "fax");
        Collection colObjects = null;
        SuppUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(mapOfFields);
        } catch (Exception ex) {
            Log.printDebug("SuppUserPhoneNut:" + ex.getMessage());
        }
        Iterator itrSuppUserPhone = colObjects.iterator();
        SuppUserPhone rtnSuppUserPhone = null;
        if (itrSuppUserPhone.hasNext()) rtnSuppUserPhone = (SuppUserPhone) itrSuppUserPhone.next();
        return rtnSuppUserPhone;
    }

    public static Collection getAllObjects() {
        Collection colObjects = null;
        SuppUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findAllObjects();
        } catch (Exception ex) {
            Log.printDebug("SuppUserPhoneNut: " + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getActiveObjects() {
        Log.printVerbose("In SuppUserPhoneNut::getActiveObjects()");
        Collection colObjects = null;
        SuppUserPhoneHome lEJBHome = getHome();
        colObjects = getCollectionByField(SuppUserPhoneBean.STATUS, SuppUserPhoneBean.STATUS_ACTIVE);
        return colObjects;
    }

    public static Collection getInactiveObjects() {
        Log.printVerbose("In SuppUserPhoneNut::getInactiveObjects()");
        Collection colObjects = null;
        SuppUserPhoneHome lEJBHome = getHome();
        colObjects = getCollectionByField(SuppUserPhoneBean.STATUS, SuppUserPhoneBean.STATUS_INACTIVE);
        return colObjects;
    }

    public static Collection getAllByUserId(Integer suppUserId) {
        Log.printVerbose("In CustUserPhoneNut::getAllByUserId()");
        Collection colObjects = null;
        colObjects = getCollectionByField(SuppUserPhoneBean.SUPP_USER_ID, suppUserId.toString());
        return colObjects;
    }
}
