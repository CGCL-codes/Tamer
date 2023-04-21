package com.vlee.ejb.customer;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import javax.naming.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;

public class CustUserPhoneNut {

    private static String strClassName = "CustUserPhoneNut";

    public static CustUserPhoneHome getHome() {
        try {
            Context lContext = new InitialContext();
            CustUserPhoneHome lEJBHome = (CustUserPhoneHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/customer/CustUserPhone"), CustUserPhoneHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static CustUserPhone getHandle(Integer pkid) {
        return (CustUserPhone) getHandle(getHome(), pkid);
    }

    public static CustUserPhone getHandle(CustUserPhoneHome lEJBHome, Integer pkid) {
        try {
            return (CustUserPhone) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static Collection getValObjs(Map mapCriteria) {
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.getValObjs(mapCriteria);
        } catch (Exception ex) {
            Log.printDebug("CustUserPhoneNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getCollectionByField(String fieldName, String value) {
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(fieldName, value);
        } catch (Exception ex) {
            Log.printDebug("CustUserPhoneNut:" + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getAllByUserId(Integer custUserId) {
        Log.printVerbose("In CustUserPhoneNut::getAllByUserId()");
        Collection colObjects = null;
        colObjects = getCollectionByField(CustUserPhoneBean.CUST_USER_ID, custUserId.toString());
        return colObjects;
    }

    public static CustUserPhone getPhoneByUserId(Integer custUserId) {
        Log.printVerbose("In CustUserPhoneNut::getObjectByPhone(Integer custUserId)");
        String fieldCustUserId = new String(CustUserPhoneBean.CUST_USER_ID);
        String fieldPhoneType = new String(CustUserPhoneBean.PHONETYPE);
        HashMap mapOfFields = new HashMap();
        mapOfFields.put(fieldCustUserId, custUserId.toString());
        mapOfFields.put(fieldPhoneType, "phone");
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(mapOfFields);
        } catch (Exception ex) {
            Log.printDebug("CustUserPhoneNut:" + ex.getMessage());
        }
        Iterator itrCustUserPhone = colObjects.iterator();
        CustUserPhone rtnCustUserPhone = null;
        if (itrCustUserPhone.hasNext()) rtnCustUserPhone = (CustUserPhone) itrCustUserPhone.next();
        return rtnCustUserPhone;
    }

    public static CustUserPhone getFaxByUserId(Integer custUserId) {
        Log.printVerbose("In CustUserPhoneNut::getObjectByPhone(Integer custUserId)");
        String fieldCustUserId = new String(CustUserPhoneBean.CUST_USER_ID);
        String fieldPhoneType = new String(CustUserPhoneBean.PHONETYPE);
        HashMap mapOfFields = new HashMap();
        mapOfFields.put(fieldCustUserId, custUserId.toString());
        mapOfFields.put(fieldPhoneType, "fax");
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findObjectsGiven(mapOfFields);
        } catch (Exception ex) {
            Log.printDebug("CustUserPhoneNut:" + ex.getMessage());
        }
        Iterator itrCustUserPhone = colObjects.iterator();
        CustUserPhone rtnCustUserPhone = null;
        if (itrCustUserPhone.hasNext()) rtnCustUserPhone = (CustUserPhone) itrCustUserPhone.next();
        return rtnCustUserPhone;
    }

    public static Collection getAllObjects() {
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        try {
            colObjects = (Collection) lEJBHome.findAllObjects();
        } catch (Exception ex) {
            Log.printDebug("CustUserPhoneNut: " + ex.getMessage());
        }
        return colObjects;
    }

    public static Collection getActiveObjects() {
        Log.printVerbose("In CustUserPhoneNut::getActiveObjects()");
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        colObjects = getCollectionByField(CustUserPhoneBean.STATUS, CustUserPhoneBean.STATUS_ACTIVE);
        return colObjects;
    }

    public static Collection getInactiveObjects() {
        Log.printVerbose("In CustUserPhoneNut::getInactiveObjects()");
        Collection colObjects = null;
        CustUserPhoneHome lEJBHome = getHome();
        colObjects = getCollectionByField(CustUserPhoneBean.STATUS, CustUserPhoneBean.STATUS_INACTIVE);
        return colObjects;
    }
}
