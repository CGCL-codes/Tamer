package com.vlee.ejb.ecommerce;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;

public class EHomepageNut {

    private static String strClassName = "EHomepageNut";

    public static EHomepageHome getHome() {
        try {
            Context lContext = new InitialContext();
            EHomepageHome lEJBHome = (EHomepageHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/ecommerce/EHomepage"), EHomepageHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static EHomepage getHandle(Long pkid) {
        return (EHomepage) getHandle(getHome(), pkid);
    }

    public static EHomepage getHandle(EHomepageHome lEJBHome, Long pkid) {
        try {
            return (EHomepage) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static EHomepage fnCreate(EHomepageObject valObj) {
        EHomepage ejb = null;
        EHomepageHome home = getHome();
        try {
            ejb = home.create(valObj);
            valObj.pkid = ejb.getPkid();
            return ejb;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("EHomepageNut: " + "Cannot create this EHomepage");
            return (EHomepage) null;
        }
    }

    public static EHomepageObject getObject(Long lPkid) {
        EHomepageObject valueObj = new EHomepageObject();
        EHomepage objEJB = getHandle(lPkid);
        try {
            valueObj = objEJB.getObject();
        } catch (Exception ex) {
            Log.printDebug(ex.getMessage());
        }
        return (EHomepageObject) valueObj;
    }

    public static Collection getObjects(QueryObject query) {
        Collection result = new Vector();
        try {
            EHomepageHome home = getHome();
            result = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void saveListSection(boolean visible, String title, String itemid, String section) {
        try {
            EHomepageHome home = getHome();
            home.saveListSection(visible, title, itemid, section);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static EHomepageObject getListSection(String section) {
        EHomepageObject theObj = null;
        try {
            EHomepageHome home = getHome();
            theObj = home.getListSection(section);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return theObj;
    }
}
