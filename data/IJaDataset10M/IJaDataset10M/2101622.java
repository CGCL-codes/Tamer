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

public class CustUserNut {

    private static String strClassName = "CustUserNut";

    public static CustUserHome getHome() {
        try {
            Context lContext = new InitialContext();
            CustUserHome lEJBHome = (CustUserHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/customer/CustUser"), CustUserHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static CustUser getHandle(Integer pkid) {
        return (CustUser) getHandle(getHome(), pkid);
    }

    public static CustUser getHandle(CustUserHome lEJBHome, Integer pkid) {
        try {
            return (CustUser) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static CustUser fnCreate(CustUserObject valObj) {
        CustUser ejb = null;
        CustUserHome home = getHome();
        try {
            ejb = home.create(valObj);
            valObj.pkid = ejb.getPkid();
            return ejb;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("CustUserNut: " + "Cannot create this CustUser");
            return (CustUser) null;
        }
    }

    public static CustUserObject getObject(Integer lPkid) {
        CustUserObject valueObj = new CustUserObject();
        CustUser objEJB = getHandle(lPkid);
        try {
            valueObj = objEJB.getObject();
        } catch (Exception ex) {
            Log.printDebug(ex.getMessage());
        }
        return (CustUserObject) valueObj;
    }

    public static Vector getObjectsByAccount(Integer accId) {
        QueryObject query = new QueryObject(new String[] { CustUserBean.ACC_ID + " = '" + accId.toString() + "' " });
        query.setOrder(" ORDER BY " + CustUserBean.NAMEFIRST + ", " + CustUserBean.NAMELAST);
        Vector vecUser = new Vector(getObjects(query));
        return vecUser;
    }

    public static Collection getObjects(QueryObject query) {
        Collection result = new Vector();
        try {
            CustUserHome home = getHome();
            result = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Vector memberLoginValidate(String uname, String pwd) {
        Vector result = new Vector();
        try {
            CustUserHome home = getHome();
            result = home.memberLoginValidate(uname, pwd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Integer memberExist(Integer accType, String uname) {
        Integer pkid = new Integer(0);
        try {
            CustUserHome home = getHome();
            pkid = home.memberExist(accType, uname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pkid;
    }

    public static void savePassword(Integer pkid, String password) {
        try {
            CustUserHome home = getHome();
            home.savePassword(pkid, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Vector getContactListing(String name, String ethnic, boolean filterEthnic, String DOB, boolean filterDOB, String sortBy) {
        Vector vecResult = new Vector();
        try {
            CustUserHome home = getHome();
            vecResult = home.getContactListing(name, ethnic, filterEthnic, DOB, filterDOB, sortBy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
    }

    public static boolean hasIdenticalName(String val) {
        CustUserHome home = getHome();
        boolean result = false;
        Collection vecResult = null;
        try {
            QueryObject query = new QueryObject(new String[] { CustUserBean.NAMEFIRST + " = '" + val + "' " });
            vecResult = home.getObjects(query);
            if (vecResult.size() > 0) {
                result = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
