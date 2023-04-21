package cn.jsprun.dao.user;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.jsprun.domain.Admingroups;
import cn.jsprun.domain.Projects;
import cn.jsprun.domain.Ranks;
import cn.jsprun.domain.Settings;
import cn.jsprun.domain.Usergroups;
import cn.jsprun.utils.HibernateUtil;

public class UserGroupDaoImpl implements UserGroupDao {

    public boolean deleteRanks(Ranks rank) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.delete(rank);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteUserGroup(Short groupId) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("delete from Usergroups where groupid = ?");
            query.setShort(0, groupId);
            query.executeUpdate();
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<Ranks> findAllRanks() {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Ranks");
            List<Ranks> rankList = query.list();
            tr.commit();
            return rankList;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Usergroups> findAdminGroups() {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Usergroups as ugp where ugp.radminid != 0 and ugp.groupid!=1");
            List<Usergroups> adminGroups = query.list();
            tr.commit();
            return adminGroups;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Usergroups> findUserGroupsByType(String type) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            String sql = "from Usergroups as ugp where ugp.type=? order by ugp.creditslower";
            if ("system".equals(type)) {
                sql = "from Usergroups as ugp where ugp.type=? order by ugp.groupid";
            }
            Query query = session.createQuery(sql);
            query.setString(0, type);
            if (!"member".equals(type)) {
                query.setMaxResults(5);
            }
            List<Usergroups> userGroups = query.list();
            tr.commit();
            return userGroups;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public Usergroups findUserGroupById(Short userGroupId) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Usergroups userGroup = (Usergroups) session.get(Usergroups.class, userGroupId);
            tr.commit();
            return userGroup;
        } catch (Exception e) {
            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertRanks(Ranks rank) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.save(rank);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertUserGroup(Usergroups userGroup) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.save(userGroup);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean modifyRanks(Ranks rank) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.update(rank);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean modifyUserGroup(Usergroups userGroup) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.update(userGroup);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<Usergroups> findAllGroups() {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Usergroups order by radminid desc,groupid desc");
            List<Usergroups> allGroups = query.list();
            tr.commit();
            return allGroups;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Usergroups findUserGroupByCredits(int credits) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Usergroups u where u.creditshigher > ? and u.creditslower < ?");
            query.setInteger(0, credits);
            query.setInteger(1, credits);
            List<Usergroups> groupList = query.list();
            tr.commit();
            if (groupList != null && groupList.size() == 1) {
                return groupList.get(0);
            }
            return null;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public Admingroups findAdminGroupById(Short adminGid) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Admingroups adminGroup = (Admingroups) session.get(Admingroups.class, adminGid);
            tr.commit();
            return adminGroup;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public boolean modifyAdminGroup(Admingroups adminGroup) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.update(adminGroup);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProjects(Projects projects) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.delete(projects);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<Projects> findAllProjects() {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Projects");
            List<Projects> list = query.list();
            tr.commit();
            return list;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public Projects findProjectsById(Short projectsId) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Projects project = (Projects) session.get(Projects.class, projectsId);
            tr.commit();
            return project;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertProjects(Projects projects) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.save(projects);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean modifyProjects(Projects projects) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.update(projects);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public Settings findSettingByVariable(String variable) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Settings s where s.variable=?");
            query.setParameter(0, variable);
            List<Settings> settlist = query.list();
            tr.commit();
            if (settlist != null && settlist.size() == 1) {
                return settlist.get(0);
            }
            return null;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Usergroups> findUserGroupByAddMember() {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("FROM Usergroups as u WHERE u.type='member' AND u.creditshigher='0' OR (u.groupid NOT IN ('5', '6', '7') AND u.radminid<>'1' AND u.type<>'member') ORDER BY u.type DESC");
            List<Usergroups> grouplist = query.list();
            tr.commit();
            return grouplist;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Usergroups> findUsergroupInCredits(int Credits, short groupid) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            Query query = session.createQuery("from Usergroups as u where u.type='member' and ((" + Credits + " >= u.creditshigher and u.creditslower > " + Credits + ") or (u.groupid = " + groupid + "))");
            List<Usergroups> list = query.list();
            tr.commit();
            return list;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteUsergroups(Usergroups usergroup) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            session.delete(usergroup);
            tr.commit();
            return true;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public void updateUsergroups(List<Usergroups> updateUsergroupList) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            for (int i = 0; i < updateUsergroupList.size(); i++) {
                Usergroups userGroup = updateUsergroupList.get(i);
                session.update(userGroup);
            }
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Usergroups> findUsergropsByHql(String hql) {
        Session session = null;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query query = session.createQuery(hql);
            List<Usergroups> grouplist = query.list();
            tr.commit();
            return grouplist;
        } catch (HibernateException e) {
            tr.rollback();
            e.printStackTrace();
        }
        return null;
    }

    public List<Usergroups> getUsergroupsList(List<Short> usergroupIdList) {
        Session session = null;
        Transaction tr = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            tr = session.beginTransaction();
            List<Usergroups> list = new ArrayList<Usergroups>();
            for (int i = 0; i < usergroupIdList.size(); i++) {
                Usergroups userGroup = (Usergroups) session.get(Usergroups.class, usergroupIdList.get(i));
                if (userGroup != null) {
                    list.add(userGroup);
                } else {
                    try {
                        throw new Exception("û�л���� usergroupId ƥ���Usergroupʵ��");
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            tr.commit();
            return list;
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }
}
