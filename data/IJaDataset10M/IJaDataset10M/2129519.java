package com.centraview.administration.serversettings;

import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import com.centraview.common.CVDal;

/**
 * This is the EJB class for Administration-Configuration module -- Preferences
 * The Logic for methods defined in Remote interface is defined in this class
 */
public class ServerSettingsEJB implements SessionBean {

    protected SessionContext ctx;

    private String dataSource = "";

    public void setSessionContext(SessionContext ctx) {
        this.ctx = ctx;
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void updateServerSettings(ServerSettingsVOX svo) {
        CVDal cvdal = new CVDal(dataSource);
        try {
            if (svo == null) {
                return;
            }
            cvdal.setSql("administration.configuration.updateserversettings");
            cvdal.setInt(1, svo.getSessionTimeout());
            cvdal.setTime(2, svo.getWorkingHoursFrom());
            cvdal.setTime(3, svo.getWorkingHoursTo());
            cvdal.setInt(4, svo.getEmailCheckInterval());
            cvdal.setString(5, "");
            cvdal.setString(6, svo.getDefaultTimeZone());
            cvdal.executeUpdate();
            cvdal.setSqlQuery("UPDATE cvfolderlocation SET Detail=?");
            cvdal.setString(1, svo.getFileSystemStoragePath());
            cvdal.executeUpdate();
        } catch (Exception e) {
            System.out.println("[Exception][ServerSettingsEJB.updateServerSettings] Exception Thrown: " + e);
            e.printStackTrace();
        } finally {
            cvdal.destroy();
            cvdal = null;
        }
    }

    public ServerSettingsVOX getServerSettings() {
        ServerSettingsVOX svo = new ServerSettingsVOX();
        CVDal cvdal = new CVDal(dataSource);
        try {
            cvdal.setSql("administration.configuration.getserversettings");
            Collection col = cvdal.executeQuery();
            Iterator it = col.iterator();
            HashMap hm = null;
            if (it.hasNext()) {
                hm = (HashMap) it.next();
            }
            svo.setHostName((String) hm.get("hostname"));
            svo.setSessionTimeout(((Number) hm.get("sessiontimeout")).intValue());
            svo.setWorkingHoursFrom((Time) hm.get("workinghoursfrom"));
            svo.setWorkingHoursTo((Time) hm.get("workinghoursto"));
            svo.setEmailCheckInterval(((Number) hm.get("emailcheckinterval")).intValue());
            svo.setDefaultTimeZone((String) hm.get("defaulttimezone"));
            cvdal.clearParameters();
            cvdal.setSqlQueryToNull();
            cvdal.setSqlQuery("SELECT Detail AS folderlocation FROM cvfolderlocation");
            Collection folderResults = cvdal.executeQuery();
            if (folderResults != null && folderResults.size() > 0) {
                Iterator folderIter = folderResults.iterator();
                if (folderIter.hasNext()) {
                    HashMap folderRow = (HashMap) folderIter.next();
                    svo.setFileSystemStoragePath((String) folderRow.get("folderlocation"));
                }
            }
        } catch (Exception e) {
            System.out.println("[Exception][ServerSettingsEJB.getServerSettings] Exception Thrown: " + e);
            e.printStackTrace();
        } finally {
            cvdal.destroy();
            cvdal = null;
        }
        return svo;
    }

    /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
    public void setDataSource(String ds) {
        this.dataSource = ds;
    }

    /**
   * Used to set the hostname from the SettingsPlugin
   * @param hostName
   */
    public void setHostName(String hostName) {
        CVDal cvdal = new CVDal(dataSource);
        try {
            cvdal.setSql("administration.configuration.setHostName");
            cvdal.setString(1, hostName);
            cvdal.executeUpdate();
        } finally {
            cvdal.destroy();
            cvdal = null;
        }
    }
}
