package org.verus.ngl.sl.bprocess.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.verus.ngl.sl.objectmodel.administration.LOCATION;
import org.verus.ngl.sl.objectmodel.administration.LOCATIONPK;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.MasterLog;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.utilities.NGLUtility;
import org.verus.ngl.utilities.logging.NGLLogging;

/**
 *
 * @author root
 */
public class ShelvingLocationsImpl implements ShelvingLocations {

    @Override
    public List bm_getLocations(Integer libraryId, String status, String databaseId) {
        NGLLogging.getFineLogger().fine("in bm_getLocations....");
        List list = new ArrayList();
        Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
        Session session = connections.getSession(databaseId);
        Query query = (Query) session.getNamedQuery("LOCATION.findByLibraryIdStatus");
        NGLLogging.getFineLogger().fine("after Query in bm_getLocation()");
        query.setParameter("libraryId", libraryId);
        query.setParameter("status", status);
        NGLLogging.getFineLogger().fine("before list=query.list()");
        list = query.list();
        for (int i = 0; i < list.size(); i++) {
            LOCATION location = (LOCATION) list.get(i);
            NGLLogging.getFineLogger().fine("locationID" + location.getLOCATIONPK().getLibraryId());
            NGLLogging.getFineLogger().fine("status" + location.getStatus());
            NGLLogging.getFineLogger().fine("coordianted" + location.getCoordinates());
        }
        return list;
    }

    @Override
    public int bm_editLocation(String databaseId, String userId, Integer libraryId, Integer locationId, String location, String coordinates) {
        NGLLogging.getFineLogger().fine("bm_editLocation=====");
        int returnValue = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("LOCATION.findByPrimaryKey");
            query.setParameter("libraryId", libraryId);
            query.setParameter("locationId", locationId);
            Object val = query.uniqueResult();
            LOCATION location1 = (LOCATION) val;
            location1.setCoordinates(coordinates);
            location1.setLocation(location);
            location1.setStatus(org.verus.ngl.sl.objectmodel.status.LOCATION.STATUS_ENABLED);
            String log = location1.getLog();
            log = (new MasterLog()).bm_appendLog(log, userId, libraryId, new Date().getTime(), MasterLog.ACTIVITY_MODIFY);
            location1.setLog(log);
            NGLLogging.getFineLogger().fine("===" + location1);
            session.saveOrUpdate(location1);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            returnValue = 1;
        }
        return returnValue;
    }

    @Override
    public int bm_saveLocationName(String databaseId, String userId, Integer libraryId, String location) {
        NGLLogging.getFineLogger().fine("in bm_saveLocationName() of IMPL class");
        int returnValue = 0;
        LOCATION location1 = new LOCATION();
        try {
            String log = (new MasterLog()).bm_appendLog("", userId, libraryId, System.currentTimeMillis(), MasterLog.ACTIVITY_CREATE);
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("LOCATION.getMaximumId");
            query.setParameter("libraryId", libraryId.intValue());
            Object val = query.uniqueResult();
            int maxId = 0;
            if (val != null) {
                maxId = ((Integer) val).intValue();
            }
            maxId++;
            NGLLogging.getFineLogger().fine("in bm_saveLocationName() of IMPL class of maxid++ =======" + maxId);
            session.beginTransaction();
            NGLLogging.getFineLogger().fine("in saveCourseName() of shelvingLocationIMPL class");
            LOCATIONPK primaryKey = new LOCATIONPK(libraryId, maxId);
            location1.setLOCATIONPK(primaryKey);
            location1.setLocation(location);
            NGLLogging.getFineLogger().fine("in bm_saveLocationName ----- " + libraryId);
            location1.setLog(log);
            location1.setStatus(org.verus.ngl.sl.objectmodel.status.LOCATION.STATUS_ENABLED);
            session.save(location1);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = 1;
        }
        return returnValue;
    }

    @Override
    public String[] bm_getLocationDetails(String databaseId, Integer locationId, Integer libraryId) {
        NGLLogging.getFineLogger().fine("bm_getLocationDetails===");
        Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
        Session session = connections.getSession(databaseId);
        Query query = session.getNamedQuery("LOCATION.findByPrimaryKey");
        query.setInteger("libraryId", libraryId);
        query.setInteger("locationId", locationId);
        Object val = query.uniqueResult();
        String[] retResult = new String[5];
        LOCATION location = (LOCATION) val;
        retResult[0] = String.valueOf(location.getLOCATIONPK().getLocationId());
        retResult[1] = String.valueOf(location.getLOCATIONPK().getLibraryId());
        retResult[2] = NGLUtility.getInstance().getTestedString(location.getLocation());
        retResult[3] = NGLUtility.getInstance().getTestedString(location.getCoordinates());
        retResult[4] = NGLUtility.getInstance().getTestedString(location.getStatus());
        return retResult;
    }

    @Override
    public int bm_changeStatus(String databaseId, String userId, Integer libraryId, Integer locationId, String status) {
        NGLLogging.getFineLogger().fine("bm_changeStatus===");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("LOCATION.findByPrimaryKey");
            query.setInteger("libraryId", libraryId);
            query.setInteger("locationId", locationId);
            Object val = query.uniqueResult();
            LOCATION location = (LOCATION) val;
            location.setStatus(status);
            String log = location.getLog();
            String logStatus = "";
            if (status.equals(org.verus.ngl.sl.objectmodel.status.LOCATION.STATUS_ENABLED)) {
                logStatus = MasterLog.ACTIVITY_ENABLE;
            } else {
                logStatus = MasterLog.ACTIVITY_DISABLE;
            }
            log = (new MasterLog()).bm_appendLog(log, userId, libraryId, new Date().getTime(), logStatus);
            location.setLog(log);
            session.saveOrUpdate(location);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 1;
        }
        return retVal;
    }

    @Override
    public int bm_checkLocationNameDuplicates(String databaseId, Integer libraryId, String location) {
        NGLLogging.getFineLogger().fine("===");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("LOCATION.findByLibraryIdLocation");
            query.setInteger("libraryId", libraryId);
            query.setString("location", location);
            List val = query.list();
            retVal = val.size();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 0;
        }
        return retVal;
    }

    @Override
    public int bm_checkLocationNameDuplicatesAtEdit(String databaseId, Integer libraryId, Integer locationId, String location) {
        NGLLogging.getFineLogger().fine("bm_checkLocationNameDuplicatesAtEdit===");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("LOCATION.findByLibraryIdLocationIdLocation");
            query.setInteger("libraryId", libraryId);
            query.setString("location", location);
            query.setInteger("locationId", locationId);
            List val = query.list();
            retVal = val.size();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 0;
        }
        return retVal;
    }

    @Override
    public int bm_deleteLocation(String databaseId, String userId, Integer libraryId, Integer locationId) {
        NGLLogging.getFineLogger().fine("bm_deleteLocation===");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("LOCATION.findByPrimaryKey");
            query.setInteger("libraryId", libraryId);
            query.setInteger("locationId", locationId);
            Object val = query.uniqueResult();
            LOCATION location = (LOCATION) val;
            session.delete(location);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 1;
        }
        return retVal;
    }

    @Override
    public String bm_getLogLocationName(String databaseId, Integer libraryId, Integer locationId) {
        NGLLogging.getFineLogger().fine("bm_getLogLocationName=====");
        String log = new String();
        List<LOCATION> list = new ArrayList<LOCATION>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("LOCATION.UpdateByStatus");
            query.setParameter("libraryId", libraryId);
            query.setParameter("locationId", locationId);
            NGLLogging.getFineLogger().fine("bm_getLogLocationName=====" + libraryId + "========" + locationId);
            list = query.list();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LOCATION location = list.get(i);
                    log = location.getLog();
                    NGLLogging.getFineLogger().fine("after -==-=-=-log = course.getLog()-=-=-" + log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log;
    }
}
