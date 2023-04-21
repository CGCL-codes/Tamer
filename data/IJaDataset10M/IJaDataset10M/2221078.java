package com.vlee.ejb.customer;

import javax.servlet.ServletContext;
import javax.rmi.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.math.BigDecimal;
import javax.naming.*;
import com.vlee.util.*;
import com.vlee.bean.loyalty.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.customer.*;

public class CustMembershipCampaignIndexNut {

    private static String strClassName = "CustMembershipCampaignIndexNut";

    public static CustMembershipCampaignIndexHome getHome() {
        try {
            Context lContext = new InitialContext();
            CustMembershipCampaignIndexHome lEJBHome = (CustMembershipCampaignIndexHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/customer/CustMembershipCampaignIndex"), CustMembershipCampaignIndexHome.class);
            return lEJBHome;
        } catch (Exception e) {
            Log.printDebug("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static CustMembershipCampaignIndex getHandle(Integer pkid) {
        return (CustMembershipCampaignIndex) getHandle(getHome(), pkid);
    }

    public static CustMembershipCampaignIndex getHandle(CustMembershipCampaignIndexHome lEJBHome, Integer pkid) {
        try {
            return (CustMembershipCampaignIndex) lEJBHome.findByPrimaryKey(pkid);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static CustMembershipCampaignIndex fnCreate(CustMembershipCampaignIndexObject valObj) {
        CustMembershipCampaignIndexHome home = getHome();
        CustMembershipCampaignIndex cmcIndexEJB = null;
        Log.printVerbose(" Inside CustMembershipCampaignIndexNut!! ");
        try {
            cmcIndexEJB = home.create(valObj);
            valObj.pkid = cmcIndexEJB.getPkid();
            CustMembershipCampaignEngine.reloadCampaign();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cmcIndexEJB;
    }

    public static CustMembershipCampaignIndexObject getObject(Integer lPkid) {
        CustMembershipCampaignIndexObject valueObj = new CustMembershipCampaignIndexObject();
        CustMembershipCampaignIndex objEJB = getHandle(lPkid);
        try {
            valueObj = objEJB.getObject();
        } catch (Exception ex) {
            Log.printDebug(ex.getMessage());
        }
        return (CustMembershipCampaignIndexObject) valueObj;
    }

    public static CustMembershipCampaignIndexObject getObjectTree(Integer lPkid) {
        CustMembershipCampaignIndexObject valueObj = getObject(lPkid);
        if (valueObj != null) {
            QueryObject queryRules = new QueryObject(new String[] { CustMembershipCampaignRulesBean.INDEX_ID + " = '" + lPkid.toString() + "' " });
            queryRules.setOrder(" ORDER BY " + CustMembershipCampaignRulesBean.SEQUENCE + ", " + CustMembershipCampaignRulesBean.PKID);
            valueObj.vecRules = new Vector(CustMembershipCampaignRulesNut.getObjects(queryRules));
        }
        return valueObj;
    }

    public static Collection getObjects(QueryObject query) {
        Collection col = null;
        try {
            CustMembershipCampaignIndexHome home = getHome();
            col = home.getObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
    }
}
