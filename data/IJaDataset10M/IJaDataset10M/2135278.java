package org.verus.ngl.sl.bprocess.administration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.verus.ngl.sl.utilities.Connections;
import org.verus.ngl.sl.utilities.NGLBeanFactory;
import org.verus.ngl.sl.objectmodel.administration.MATERIAL_CATEGORY;
import org.verus.ngl.sl.objectmodel.administration.MATERIAL_CATEGORYPK;
import org.verus.ngl.sl.utilities.MasterLog;
import org.verus.ngl.utilities.logging.NGLLogging;

/**
 *
 * @author root
 */
public class MaterialCategoryImpl implements MaterialCategory {

    @Override
    public String getMaterialName(Integer material_category_id, Integer libraryId, String databaseId) {
        String name = "";
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findById");
            query.setParameter("id", material_category_id);
            MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) query.uniqueResult();
            if (material_category != null) {
                name = material_category.getCategoryName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public int bm_saveMaterialDetails(Integer id, Integer libraryId, String databaseId, String categoryName, String status) {
        int retvalue = 0;
        try {
            String log = (new MasterLog()).bm_appendLog("", String.valueOf(libraryId), libraryId, System.currentTimeMillis(), MasterLog.ACTIVITY_CREATE);
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            MATERIAL_CATEGORY material_category = new MATERIAL_CATEGORY();
            MATERIAL_CATEGORYPK material_categorypk = new MATERIAL_CATEGORYPK(id, libraryId);
            material_category.setMATERIAL_CATEGORYPK(material_categorypk);
            material_category.setCategoryName(categoryName);
            material_category.setLog(log);
            material_category.setStatus(status);
            session.save(material_category);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return retvalue;
    }

    @Override
    public List bm_getMaterialDetails(Integer libraryId, String databaseId, String status) {
        List returnList = new ArrayList();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = (Query) session.getNamedQuery("MATERIAL_CATEGORY.findByStatus");
            query.setParameter("libraryId", libraryId);
            query.setParameter("status", status);
            List list = query.list();
            for (int i = 0; i < list.size(); i++) {
                MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) list.get(i);
                returnList.add(material_category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    @Override
    public int bm_getMaxMaterialId(Integer library_id, String databaseId) {
        int max_dept_id = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByMax_Material_Id");
            query.setParameter("libraryId", library_id);
            max_dept_id = (Integer) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max_dept_id;
    }

    @Override
    public int bm_changeStatus(Integer id, Integer libraryId, String databaseId, String status) {
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryId");
            query.setInteger("libraryId", libraryId);
            query.setInteger("id", id);
            Object val = query.uniqueResult();
            MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) val;
            material_category.setStatus(status);
            String log = material_category.getLog();
            String logStatus = "";
            if (status.equals(org.verus.ngl.sl.objectmodel.status.LOCATION.STATUS_ENABLED)) {
                logStatus = MasterLog.ACTIVITY_ENABLE;
            } else {
                logStatus = MasterLog.ACTIVITY_DISABLE;
            }
            log = (new MasterLog()).bm_appendLog(log, "" + id, libraryId, new Date().getTime(), logStatus);
            System.out.println("..................bm_changeStatus....log information....." + log);
            material_category.setLog(log);
            session.saveOrUpdate(material_category);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            retVal = 1;
        }
        return retVal;
    }

    @Override
    public int bm_deleteMaterial(Integer id, Integer libraryId, String databaseId) {
        NGLLogging.getFineLogger().fine("bm_deleteMaterial===");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryId");
            query.setInteger("libraryId", libraryId);
            query.setInteger("id", id);
            Object val = query.uniqueResult();
            MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) val;
            session.delete(material_category);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            return retVal = 1;
        }
        return retVal;
    }

    @Override
    public String bm_getLogDetails(Integer id, Integer libraryId, String databaseId) {
        String log = null;
        List<MATERIAL_CATEGORY> list = new ArrayList<MATERIAL_CATEGORY>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryId");
            query.setInteger("libraryId", libraryId);
            query.setInteger("id", id);
            list = query.list();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    MATERIAL_CATEGORY material_category = list.get(i);
                    log = material_category.getLog();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return log;
    }

    public String bm_getMaterialName(Integer id, Integer libraryId, String databaseId) {
        String materialname = null;
        List<MATERIAL_CATEGORY> list = new ArrayList<MATERIAL_CATEGORY>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryId");
            query.setInteger("libraryId", libraryId);
            query.setInteger("id", id);
            Object val = query.uniqueResult();
            MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) val;
            materialname = material_category.getCategoryName();
            System.out.println("......................materialname.................................." + materialname);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return materialname;
    }

    public int bm_checkMaterialNameDuplicatesAtEdit(Integer id, Integer libraryId, String databaseId, String categoryName) {
        NGLLogging.getFineLogger().fine("in bm_checkCourseNameDuplicatesAtEdit() of IMPL class");
        int retVal = 0;
        NGLLogging.getFineLogger().fine(" libraryId ===================: " + libraryId);
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryIdIdcategory");
            query.setInteger("libraryId", libraryId);
            query.setString("categoryName", categoryName);
            query.setInteger("id", id);
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
    public int bm_setEditCategoryName(Integer id, Integer libraryId, String databaseId, String categoryName, String status) {
        int retval = 0;
        List<MATERIAL_CATEGORY> list = new ArrayList<MATERIAL_CATEGORY>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            session.beginTransaction();
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryId");
            query.setInteger("libraryId", libraryId);
            query.setInteger("id", id);
            Object values = query.uniqueResult();
            session.beginTransaction();
            MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) values;
            String log = material_category.getLog();
            material_category.setCategoryName(categoryName);
            log = (new MasterLog()).bm_appendLog(log, String.valueOf(libraryId), libraryId, System.currentTimeMillis(), MasterLog.ACTIVITY_MODIFY);
            material_category.setLog(log);
            material_category.setStatus(status);
            session.save(material_category);
            session.getTransaction().commit();
            session.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return retval;
    }

    public int bm_checkMaterialNameDuplicates(Integer libraryId, String databaseId, String categoryName) {
        NGLLogging.getFineLogger().fine("in bm_checkCourseNameDuplicates() of IMPL class");
        int retVal = 0;
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByLibraryIdcategory");
            query.setInteger("libraryId", libraryId);
            query.setString("categoryName", categoryName);
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
    public Vector getMaterial(String libraryId, String databaseId, String status) {
        System.out.println("======================= MaterialCategoryImpl of getMaterial =======================");
        Vector vector = new Vector();
        List<MATERIAL_CATEGORY> list = new ArrayList<MATERIAL_CATEGORY>();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("MATERIALCATEGORY.findByLibIdstatus");
            query.setParameter("library_id", Integer.parseInt(libraryId));
            query.setParameter("status", status);
            list = query.list();
            System.out.println("list size is========================: " + list.size());
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    MATERIAL_CATEGORY material_category = list.get(i);
                    Hashtable hash = new Hashtable();
                    hash.put("category_name", material_category.getCategoryName());
                    hash.put("status", material_category.getStatus());
                    hash.put("id", material_category.getMATERIAL_CATEGORYPK().getId());
                    vector.addElement(hash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector;
    }

    @Override
    public String getMaterialCategoryName(Integer material_category_id, Integer libraryId, String databaseId) {
        String name = new String();
        try {
            Connections connections = (Connections) NGLBeanFactory.getInstance().getBean("connections");
            Session session = connections.getSession(databaseId);
            Query query = session.getNamedQuery("MATERIAL_CATEGORY.findByIdLibraryId");
            query.setParameter("id", material_category_id);
            query.setParameter("library_id", libraryId);
            MATERIAL_CATEGORY material_category = (MATERIAL_CATEGORY) query.uniqueResult();
            if (material_category != null) {
                name = material_category.getCategoryName();
            } else {
                name = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}
