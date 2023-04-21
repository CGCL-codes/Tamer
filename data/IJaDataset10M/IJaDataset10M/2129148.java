package oxygen.forum.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.persistence.*;
import oxygen.forum.ForumConstants;
import oxygen.forum.ForumLocal;
import oxygen.util.Closeable;
import oxygen.util.OxyLocal;
import oxygen.util.OxygenUtils;
import oxygen.web.OxygenResourceNotFoundException;

public class ForumDAO implements Closeable {

    private EntityManagerFactory emf;

    public ForumDAO() throws Exception {
        Properties pp = new Properties();
        Properties p = ForumLocal.getForumEngine().getProperties();
        String pfx = p.getProperty(ForumConstants.DAO_PROVIDER_PROPERTIES_PREFIX_KEY);
        OxygenUtils.extractProps(p, pp, pfx, false);
        emf = Persistence.createEntityManagerFactory("oxyforum", pp);
    }

    private EntityManager getCurrentSession() {
        EntityManager s = (EntityManager) OxyLocal.get(EntityManager.class);
        if (s == null) {
            s = emf.createEntityManager();
            s.getTransaction().begin();
            OxyLocal.set(EntityManager.class, s);
        }
        return s;
    }

    public void closeCurrentSession() {
        EntityManager s = (EntityManager) OxyLocal.get(EntityManager.class);
        if (s == null) return;
        try {
            s.flush();
        } catch (Exception exc) {
            OxygenUtils.error(exc);
        }
        try {
            s.getTransaction().commit();
        } catch (Exception exc) {
            OxygenUtils.error(exc);
        }
        try {
            s.close();
        } catch (Exception exc) {
            OxygenUtils.error(exc);
        }
        OxyLocal.set(EntityManager.class, null);
    }

    public void flush() {
        getCurrentSession().flush();
    }

    public void close() {
        closeCurrentSession();
        emf.close();
    }

    public Forum getRootForum() {
        Query query = getCurrentSession().createQuery("select p from " + Forum.class.getName() + " p where p.parentForum is NULL");
        return (Forum) query.getSingleResult();
    }

    public Object get(Class clazz, Serializable id, boolean assumeExisting) {
        EntityManager sess = getCurrentSession();
        Object obj = sess.find(clazz, id);
        if (assumeExisting && obj == null) {
            throw new OxygenResourceNotFoundException("No object of type: " + clazz.getName() + " and id: " + id + " exists");
        }
        return obj;
    }

    public void refresh(ForumEntity obj) {
        if (obj != null) {
            EntityManager sess = getCurrentSession();
            sess.refresh(obj);
        }
    }

    public ForumEntity detach(ForumEntity obj) {
        return obj;
    }

    public void saveOrUpdate(ForumEntity obj) {
        saveOrUpdate(obj, false);
    }

    private void saveOrUpdate(ForumEntity obj, boolean flush) {
        if (obj != null && obj.getId() == null) {
            EntityManager sess = getCurrentSession();
            sess.persist(obj);
            if (flush) {
                sess.flush();
            }
        }
    }

    public void delete(ForumEntity obj) {
        if (obj != null) {
            EntityManager sess = getCurrentSession();
            sess.remove(obj);
        }
    }

    public User getUser(String username) {
        EntityManager sess = getCurrentSession();
        String s = "select p from oxygen.forum.data.User p where p.name = ?1";
        User user = null;
        try {
            user = (User) sess.createQuery(s).setParameter(1, username).getSingleResult();
        } catch (NoResultException exc) {
            user = new User();
            user.setName(username);
            user.setLastvisit(new Date());
            sess.persist(user);
        }
        return user;
    }

    public Misc getMisc(String key, String defValue) {
        EntityManager sess = getCurrentSession();
        String s = "select p from oxygen.forum.data.Misc p where p.key = ?1";
        Misc m = null;
        try {
            m = (Misc) sess.createQuery(s).setParameter(1, key).getSingleResult();
        } catch (NoResultException exc) {
            if (defValue != null) {
                m = new Misc();
                m.setKey(key);
                m.setValue(defValue);
                sess.persist(m);
            }
        }
        return m;
    }

    public List getTopicsChangedSince(Date d) {
        String s = "select p.topic, max(p.id) from oxygen.forum.data.Post p where p.date > ?1 group by p.topic";
        return executeQuery(s, new Object[] { d }, true);
    }

    public List getTopicsForForumUnchangedSince(Forum f, Date d) {
        String s = "select p.topic, max(p.id) from oxygen.forum.data.Post p where p.topid.forum = ?1 and p.date < ?2 group by p.topic";
        return executeQuery(s, new Object[] { f, d }, true);
    }

    public List getAllForums() {
        return (executeQuery("select t from oxygen.forum.data.Forum t", null, false));
    }

    public List getAll(Class c) {
        return (executeQuery("select t from " + c.getName() + " t", null, false));
    }

    public int getCount(Class c, String property, Object value) {
        String s = "select count(c) from " + c.getName() + " c " + " where c." + property + " = ?1";
        return getSingleRowIntResult(s, new Object[] { value });
    }

    public int getCount(Class c) {
        String s = "select count(c) from " + c.getName() + " c";
        return getSingleRowIntResult(s, null);
    }

    public int getSum(Class c, String property) {
        String s = "select sum(c." + property + ") from " + c.getName() + " c";
        return getSingleRowIntResult(s, null);
    }

    public void setWatchTopic(User u, Topic t, boolean yesOrNo) {
        if (yesOrNo) {
            u.getWatchedTopics().add(t);
        } else {
            u.getWatchedTopics().remove(t);
        }
    }

    private List executeQuery(String s, Object[] args, boolean returnFirstOnly) {
        List cats = getQuery(s, args).getResultList();
        List list2 = cats;
        if (returnFirstOnly) {
            list2 = new ArrayList(cats.size());
            for (Object o : cats) {
                list2.add(((Object[]) o)[0]);
            }
        }
        return list2;
    }

    private Query getQuery(String s, Object[] args) {
        args = (args == null) ? new Object[0] : args;
        EntityManager sess = getCurrentSession();
        Query query = sess.createQuery(s);
        for (int i = 0; i < args.length; i++) {
            query.setParameter(i + 1, args[i]);
        }
        return query;
    }

    public void clearDBTables() {
        Query query = null;
        String[] qc = new String[] { "Topic", "Post", "User", "Forum", "Misc" };
        for (int i = 0; i < qc.length; i++) {
            query = getCurrentSession().createQuery("delete from oxygen.forum.data." + qc[i]);
            int i2 = query.executeUpdate();
            OxygenUtils.info("XXXX: Deleted " + i2 + " instances of " + qc[i]);
        }
    }

    private int getSingleRowIntResult(String s, Object[] args) {
        Object obj = getQuery(s, args).getSingleResult();
        if (obj == null) {
            throw new RuntimeException("No Result was returned from the Query: " + s);
        } else if (obj instanceof Long) {
            return ((Long) obj).intValue();
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            throw new RuntimeException("Unable to decipher result for query: " + s + " Result: " + obj);
        }
    }
}
