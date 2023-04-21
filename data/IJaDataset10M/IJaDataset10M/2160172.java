package org.verus.ngl.sl.bprocess.administration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.hibernate.Query;
import org.hibernate.Session;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.sl.objectmodel.administration.COURSE;
import org.verus.ngl.sl.utilities.MasterLog;
import org.verus.ngl.utilities.logging.NGLLogging;

/**
 *
 * @author root
 */
public class CourseImpl implements Course {

    @Override
    public String getCourseName(Integer course_id, Integer library_id, String databaseId) {
        String courseName = "";
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            if (course_id != null && library_id != null) {
                Query query = session.getNamedQuery("COURSE.findByCourseIdLibraryId");
                query.setInteger("courseId", course_id);
                query.setInteger("libraryId", library_id);
                COURSE course = (COURSE) query.uniqueResult();
                if (course != null) {
                    courseName = course.getCourseName();
                } else {
                    courseName = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseName;
    }

    @Override
    public List bm_getCourseName(String databaseId, Integer libraryId, String status) {
        NGLLogging.getFineLogger().fine("in getCourseName of CourseIMPL class");
        List list = new ArrayList();
        Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
        Session session = connections.getSession(databaseId);
        Query query = (Query) session.getNamedQuery("COURSE.findByLibraryIdStatus");
        NGLLogging.getFineLogger().fine("after Query in getCourseName()");
        query.setParameter("libraryId", libraryId);
        query.setParameter("status", status);
        NGLLogging.getFineLogger().fine("before list=query.list()");
        list = query.list();
        for (int i = 0; i < list.size(); i++) {
            COURSE course = (COURSE) list.get(i);
            NGLLogging.getFineLogger().fine("in CourseImpl of getCourseName-----" + course.getCourseName());
        }
        return list;
    }

    @Override
    public int bm_saveCourseName(String databaseId, String userId, Integer libraryId, String courseName, String log, String status) {
        NGLLogging.getFineLogger().fine("in bm_saveCourseName() of IMPL class");
        int returnValue = 0;
        COURSE course = new COURSE();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("COURSE.getMaximumId");
            query.setParameter("libraryId", libraryId.intValue());
            Object val = query.uniqueResult();
            int maxId = 0;
            if (val != null) {
                maxId = ((Integer) val).intValue();
            }
            maxId++;
            NGLLogging.getFineLogger().fine("in bm_saveCourseName() of IMPL class of maxid++ =======" + maxId);
            session.beginTransaction();
            NGLLogging.getFineLogger().fine("in saveCourseName() of courseImpl class");
            org.verus.ngl.sl.objectmodel.administration.COURSEPK primaryKey = new org.verus.ngl.sl.objectmodel.administration.COURSEPK(libraryId, maxId);
            course.setCOURSEPK(primaryKey);
            course.setCourseName(courseName);
            NGLLogging.getFineLogger().fine("in bm_saveCourseName" + libraryId);
            course.setLog(log);
            course.setStatus(status);
            session.save(course);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = 1;
        }
        return returnValue;
    }

    @Override
    public int bm_editCourseName(String databaseId, String userId, Integer libraryId, Integer courseId, String courseName, String log, String status) {
        NGLLogging.getFineLogger().fine("in bm_editCourseName of IMPL class");
        int returnValue = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("COURSE.findByPrimaryKey");
            query.setParameter("libraryId", libraryId);
            query.setParameter("courseId", courseId);
            NGLLogging.getFineLogger().fine("in bm_editCourseName of IMPL class" + libraryId + "-------" + courseId);
            Object val = query.uniqueResult();
            COURSE course = (COURSE) val;
            course.setCourseName(courseName);
            NGLLogging.getFineLogger().fine("in bm_editCourseName of IMPL class after course.setcourseName<---->" + (courseName));
            course.setStatus(status);
            course.setLog(log);
            session.save(course);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            returnValue = 1;
        }
        return returnValue;
    }

    @Override
    public String bm_getLogCourseName(String databaseId, Integer libraryId, Integer courseId) {
        String log = new String();
        List<COURSE> list = new ArrayList<COURSE>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("COURSE.UpdateByStatus");
            query.setParameter("libraryId", libraryId);
            query.setParameter("courseId", courseId);
            list = query.list();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    COURSE course = list.get(i);
                    log = course.getLog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log;
    }

    @Override
    public int bm_checkCourseNameDuplicates(String databaseId, Integer libraryId, String courseName) {
        NGLLogging.getFineLogger().fine("in bm_checkCourseNameDuplicates() of IMPL class");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("COURSE.findByLibraryIdCourse");
            query.setInteger("libraryId", libraryId);
            query.setString("courseName", courseName);
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
    public int bm_checkCourseNameDuplicatesAtEdit(String databaseId, Integer libraryId, Integer courseId, String courseName) {
        NGLLogging.getFineLogger().fine("in bm_checkCourseNameDuplicatesAtEdit() of IMPL class");
        int retVal = 0;
        NGLLogging.getFineLogger().fine(" libraryId ===================: " + libraryId);
        NGLLogging.getFineLogger().fine(" courseId===================: " + courseId);
        NGLLogging.getFineLogger().fine(" courseName ===================: " + courseName);
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("COURSE.findByLibraryIdcourseIdCourse");
            query.setInteger("libraryId", libraryId);
            query.setString("courseName", courseName);
            query.setInteger("courseId", courseId);
            List val = query.list();
            retVal = val.size();
            NGLLogging.getFineLogger().fine(" on edit list size is ===================: " + retVal);
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 0;
        }
        return retVal;
    }

    @Override
    public int bm_changeStatus(String databaseId, String userId, Integer libraryId, Integer courseId, String status) {
        NGLLogging.getFineLogger().fine("in bm_changeStatus() of IMPL class");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("COURSE.findByPrimaryKey");
            query.setParameter("libraryId", libraryId);
            query.setParameter("courseId", courseId);
            Object val = query.uniqueResult();
            COURSE course = (COURSE) val;
            course.setStatus(status);
            String log = course.getLog();
            String logStatus = "";
            if (status.equals(org.verus.ngl.sl.objectmodel.status.COURSE.STATUS_ENABLED)) {
                logStatus = MasterLog.ACTIVITY_ENABLE;
            } else {
                logStatus = MasterLog.ACTIVITY_DISABLE;
            }
            log = (new MasterLog()).bm_appendLog(log, userId, libraryId, System.currentTimeMillis(), logStatus);
            course.setLog(log);
            session.saveOrUpdate(course);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 1;
        }
        return retVal;
    }

    @Override
    public Vector bm_getCourseDetails(String databaseId, Integer libraryId, Integer courseId) {
        Vector vector = new Vector();
        List<COURSE> list = new ArrayList<COURSE>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("COURSE.UpdateByStatus");
            query.setParameter("libraryId", libraryId);
            query.setParameter("courseId", courseId);
            list = query.list();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    COURSE course = list.get(i);
                    Hashtable hash = new Hashtable();
                    hash.put("libraryId", course.getCOURSEPK().getLibraryId());
                    hash.put("courseId", course.getCOURSEPK().getCourseId());
                    hash.put("courseName", course.getCourseName());
                    hash.put("status", course.getStatus());
                    vector.addElement(hash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector;
    }

    @Override
    public int bm_deleteCourse(String databaseId, String userId, Integer libraryId, Integer courseId) {
        NGLLogging.getFineLogger().fine("in bm_deleteServer() of IMPL class");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("COURSE.findByPrimaryKey");
            query.setInteger("libraryId", libraryId);
            query.setInteger("courseId", courseId);
            Object val = query.uniqueResult();
            COURSE course = (COURSE) val;
            try {
                session.delete(course);
            } catch (Exception exception) {
            }
            session.getTransaction().commit();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 1;
        }
        return retVal;
    }

    @Override
    public List bm_getCourseNames(Integer libraryId, String databaseId) {
        List list = new ArrayList();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("COURSE.findByLibraryId");
            query.setInteger("libraryId", libraryId);
            list = query.list();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
