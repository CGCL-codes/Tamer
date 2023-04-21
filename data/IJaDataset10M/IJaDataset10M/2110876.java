package cn.jsprun.dao.posts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.domain.Imagetypes;
import cn.jsprun.domain.Smilies;
import cn.jsprun.utils.*;

public class ImagetypesDaoImple implements ImagetypesDao {

    public Integer deleteImagetypesAll(Short[] ids) {
        Transaction tr = null;
        Integer num = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Smilies as s where s.typeid in (:typeids)");
            query.setParameterList("typeids", ids);
            query.executeUpdate();
            query = session.createQuery("delete from Imagetypes as i where i.typeid in (:typeids)");
            query.setParameterList("typeids", ids);
            query.executeUpdate();
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
        }
        return num;
    }

    public Integer updateNameImagetypes(List<Imagetypes> list) {
        Integer num = -1;
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("update Imagetypes as i set i.name = :name,i.displayorder=:displayorder where i.typeid = :typeid");
            for (int i = 0; i < list.size(); i++) {
                query.setString("name", list.get(i).getName());
                query.setShort("displayorder", list.get(i).getDisplayorder());
                query.setShort("typeid", list.get(i).getTypeid());
                num += query.executeUpdate();
            }
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }

    public Integer saveList(List<Imagetypes> list) {
        Transaction tr = null;
        Integer num = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                session.save(list.get(i));
                num++;
            }
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
            num = 0;
        }
        return num;
    }

    @SuppressWarnings("unchecked")
    public Map showImagesToID(Short typeid, Integer page) {
        Transaction tr = null;
        Map map = new HashMap();
        Integer totalSize = 1;
        Integer currentPage = page;
        Integer totalPage = 1;
        List<Smilies> list = new ArrayList<Smilies>();
        String directory = null;
        int startid = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("From Imagetypes as i where i.typeid = :typeid");
            query.setShort("typeid", typeid);
            List<Imagetypes> lists = query.list();
            Imagetypes types = lists.get(0);
            directory = types.getDirectory();
            map.put("name", types.getName());
            if (FormDataCheck.isValueString(directory)) {
                query = session.createQuery("select count(*) from Smilies as s where s.typeid = :typeid and type='smiley'");
                query.setShort("typeid", typeid);
                totalSize = Integer.valueOf(query.list().get(0).toString());
                if (totalSize > ImagetypesDao.PAGESIZE) {
                    if (totalSize % ImagetypesDao.PAGESIZE == 0) {
                        totalPage = totalSize / ImagetypesDao.PAGESIZE;
                    } else {
                        double pages = (double) totalSize / (double) ImagetypesDao.PAGESIZE;
                        totalPage = (int) pages + 1;
                    }
                } else {
                    totalPage = 1;
                }
                if (currentPage > 0) {
                    if (currentPage > totalPage) {
                        currentPage = totalPage;
                    }
                    startid = ImagetypesDao.PAGESIZE * (currentPage - 1);
                }
                query = session.createQuery("from Smilies as s WHERE s.typeid = :typeid and type='smiley'");
                query.setShort("typeid", typeid);
                query.setFetchSize(ImagetypesDao.PAGESIZE);
                query.setFirstResult(startid);
                query.setMaxResults(ImagetypesDao.PAGESIZE);
                list = query.list();
            }
            session.flush();
            tr.commit();
        } catch (HibernateException he) {
            if (tr != null) tr.rollback();
            tr = null;
            he.printStackTrace();
        }
        map.put(ImagetypesDao.SMILIES, list);
        map.put(ImagetypesDao.CURRENTPAGE, currentPage);
        map.put(ImagetypesDao.DIRECTORY, directory);
        map.put(ImagetypesDao.TOTALPAGE, totalPage);
        map.put(ImagetypesDao.TOTALSIZE, totalSize);
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<Imagetypes> findImagetypeBytype(String type) {
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Imagetypes as s WHERE s.type='smiley'");
            List<Imagetypes> typelist = query.list();
            tr.commit();
            return typelist;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public Imagetypes findImagetypeById(short id) {
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Imagetypes types = (Imagetypes) session.get(Imagetypes.class, id);
            tr.commit();
            return types;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Imagetypes> findImagetypeByName(String name) {
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Imagetypes as i where i.name = ?");
            query.setParameter(0, name);
            List<Imagetypes> types = query.list();
            tr.commit();
            return types;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public boolean addImagetype(Imagetypes imagetype) {
        Transaction tr = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.save(imagetype);
            tr.commit();
            return true;
        } catch (HibernateException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
}
