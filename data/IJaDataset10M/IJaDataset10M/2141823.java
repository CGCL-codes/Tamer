package com.dotmarketing.factories;

import com.dotmarketing.beans.Inode;
import com.dotmarketing.beans.MultiTree;
import com.dotmarketing.db.DotConnect;
import com.dotmarketing.db.DotHibernate;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Logger;

/**
 *
 * @author  will
 */
public class MultiTreeFactory {

    public static void deleteMultiTree(Object o1, Object o2, Object o3) {
        Inode inode1 = (Inode) o1;
        Inode inode2 = (Inode) o2;
        Inode inode3 = (Inode) o3;
        try {
            DotConnect db = new DotConnect();
            db.setSQL("delete from multi_tree where parent1 =? and parent2 = ? and child = ? ");
            db.addParam(inode1.getInode());
            db.addParam(inode2.getInode());
            db.addParam(inode3.getInode());
            db.getResult();
        } catch (Exception e) {
            throw new DotRuntimeException(e.getMessage());
        }
    }

    public static boolean existsMultiTree(Object o1, Object o2, Object o3) {
        Inode inode1 = (Inode) o1;
        Inode inode2 = (Inode) o2;
        Inode inode3 = (Inode) o3;
        try {
            DotConnect db = new DotConnect();
            db.setSQL("select count(*) mycount from multi_tree where parent1 =? and parent2 = ? and child = ? ");
            db.addParam(inode1.getInode());
            db.addParam(inode2.getInode());
            db.addParam(inode3.getInode());
            int count = db.getInt("mycount");
            return (count > 0);
        } catch (Exception e) {
            throw new DotRuntimeException(e.getMessage());
        }
    }

    public static void deleteMultiTree(MultiTree multiTree) {
        DotHibernate.delete(multiTree);
    }

    public static MultiTree getMultiTree(Inode parent1, Inode parent2, Inode child) {
        try {
            DotHibernate dh = new DotHibernate(MultiTree.class);
            dh.setQuery("from multi_tree in class com.dotmarketing.beans.MultiTree where parent1 = ? and parent2 = ? and child = ?");
            dh.setParam(parent1.getInode());
            dh.setParam(parent2.getInode());
            dh.setParam(child.getInode());
            return (MultiTree) dh.load();
        } catch (Exception e) {
            Logger.warn(MultiTreeFactory.class, "getMultiTree failed:" + e, e);
        }
        return new MultiTree();
    }

    public static java.util.List getMultiTree(Inode parent) {
        try {
            DotHibernate dh = new DotHibernate(MultiTree.class);
            dh.setQuery("from multi_tree in class com.dotmarketing.beans.MultiTree where parent1 = ? or parent2 = ? ");
            dh.setParam(parent.getInode());
            dh.setParam(parent.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getMultiTree failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getMultiTreeByChild(Inode child) {
        try {
            DotHibernate dh = new DotHibernate(MultiTree.class);
            dh.setQuery("from multi_tree in class com.dotmarketing.beans.MultiTree where child = ? ");
            dh.setParam(child.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getMultiTreeByChild failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static void saveMultiTree(MultiTree o) {
        if (o.getChild() == 0 | o.getParent1() == 0 || o.getParent2() == 0) throw new DotRuntimeException("Make sure your Multitree is set!");
        DotHibernate.saveOrUpdate(o);
    }

    public static java.util.List getChildrenClass(Inode p1, Inode p2, Class c) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ? and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode order by multi_tree.tree_order";
            Logger.debug(MultiTreeFactory.class, "getChildrenClass\n " + sql + "\n");
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2.getInode() + "\n");
            dh.setParam(p1.getInode());
            dh.setParam(p2.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClass failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getChildrenClass(Inode p1, Inode p2, Class c, String orderBy) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ?  and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode order by " + orderBy;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getChildrenClass\n " + sql + "\n");
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2.getInode() + "\n");
            dh.setParam(p1.getInode());
            dh.setParam(p2.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClass failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getChildrenClassByCondition(Inode p1, Inode p2, Class c, String condition) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ? and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode and " + condition;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getChildrenClassByCondition\n " + sql);
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2.getInode() + "\n");
            dh.setParam(p1.getInode());
            dh.setParam(p2.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClassByCondition failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getChildrenClassByCondition(long p1, long p2, Class c, String condition) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ? and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode and " + condition;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getChildrenClassByCondition\n " + sql);
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1 + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2 + "\n");
            dh.setParam(p1);
            dh.setParam(p2);
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClassByCondition failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getChildrenClassByConditionAndOrderBy(Inode p1, Inode p2, Class c, String condition, String orderby) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ? and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode and " + condition + " order by " + orderby;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getChildrenClassByConditionAndOrderBy\n " + sql + "\n");
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2.getInode() + "\n");
            dh.setParam(p1.getInode());
            dh.setParam(p2.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClassByConditionAndOrderBy failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getChildrenClassByConditionAndOrderBy(long p1, long p2, Class c, String condition, String orderby) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ? and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode and " + condition + " order by " + orderby;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getChildrenClassByConditionAndOrderBy\n " + sql + "\n");
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1 + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2 + "\n");
            dh.setParam(p1);
            dh.setParam(p2);
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClassByConditionAndOrderBy failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getChildrenClassByOrder(Inode p1, Inode p2, Class c, String order) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.parent1 = ? and multi_tree.parent2 = ? and multi_tree.child = " + tableName + ".inode and " + tableName + "_1_.inode = " + tableName + ".inode order by  " + order;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getChildrenClassByOrder\n " + sql);
            dh.setSQLQuery(sql);
            Logger.debug(MultiTreeFactory.class, "inode p1:  " + p1.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "inode p2:  " + p2.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "order:  " + order + "\n");
            dh.setParam(p1.getInode());
            dh.setParam(p2.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getChildrenClassByOrder failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getParentsOfClassByCondition(Inode p, Class c, String condition) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.child = ? and (multi_tree.parent1 = " + tableName + ".inode or multi_tree.parent2 = " + tableName + ".inode) and " + tableName + "_1_.inode = " + tableName + ".inode and " + condition;
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getParentsOfClassByCondition\n " + sql);
            Logger.debug(MultiTreeFactory.class, "inode:  " + p.getInode() + "\n");
            Logger.debug(MultiTreeFactory.class, "condition:  " + condition + "\n");
            dh.setSQLQuery(sql);
            dh.setParam(p.getInode());
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getParentsOfClassByCondition failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }

    public static java.util.List getParentsOfClass(Inode p, Class c) {
        try {
            String tableName = ((Inode) c.newInstance()).getType();
            DotHibernate dh = new DotHibernate(c);
            String sql = "SELECT {" + tableName + ".*} from " + tableName + " " + tableName + ", multi_tree multi_tree, inode " + tableName + "_1_ where multi_tree.child = ? and (multi_tree.parent1 = " + tableName + ".inode or multi_tree.parent2 = " + tableName + ".inode) and " + tableName + "_1_.inode = " + tableName + ".inode ";
            Logger.debug(MultiTreeFactory.class, "dotHibernateSQL:getParentOfClass:\n " + sql + "\n");
            dh.setSQLQuery(sql);
            dh.setParam(p.getInode());
            Logger.debug(MultiTreeFactory.class, "inode:  " + p.getInode() + "\n");
            return dh.list();
        } catch (Exception e) {
            Logger.error(MultiTreeFactory.class, "getParentsOfClass failed:" + e, e);
            throw new DotRuntimeException(e.toString());
        }
    }
}
