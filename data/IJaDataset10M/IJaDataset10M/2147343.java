package org.paradise.dms.dao.impl;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.paradise.dms.pojo.SystemUser;
import org.paradise.dms.pojo.SystemUserGroup;
import org.paradise.dms.pojo.SystemUserTask;
import org.paradise.dms.pojo.TaskStatus;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import com.dheaven.framework.dao.BaseHibernateDao;
import com.dheaven.framework.dao.DaoException;

/**
 * 
 * Description: 用户组及用户任务分配 Copyright (c) 2008-2009 paraDise sTudio(DT). All
 * Rights Reserved.
 * 
 * @version 1.0 Mar 30, 2009 9:42:56 PM 郑旭（zhengxu2006@gmail.com）created
 */
@SuppressWarnings("unchecked")
@Service
public class SystemUserGroupTaskAllocationDAOImpl extends BaseHibernateDao {

    private static Logger log = Logger.getLogger(SystemUserGroupTaskAllocationDAOImpl.class);

    /**
	 * Description:
	 * 
	 * @Version1.0 Apr 5, 2009 6:10:13 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemusertask
	 * @return
	 */
    public boolean addTask(SystemUserTask systemusertask) {
        try {
            this.create(systemusertask);
            log.info("DMS_info:" + "增加一条任务记录:" + ReflectionToStringBuilder.toString(systemusertask).toString() + "成功");
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setSystemusergroupid(systemusertask.getTaskacceptergroupid());
            taskStatus.setSystemuserid(systemusertask.getSystemusertaskaccepterid());
            taskStatus.setSystemuserstatus("未完成");
            taskStatus.setSystemusertaskid(Integer.parseInt(systemusertask.getSystemusertaskid()));
            taskStatus.setTaskstatusind(1);
            if (systemusertask.getSystemusertaskaccepterid() == 0) {
                List<SystemUser> userList = this.find("from SystemUser where systemuserind = 1 and systemusergroupid = " + systemusertask.getTaskacceptergroupid());
                for (SystemUser user : userList) {
                    TaskStatus ts = new TaskStatus();
                    ts.setSystemusergroupid(systemusertask.getTaskacceptergroupid());
                    ts.setSystemuserstatus("未完成");
                    ts.setSystemuserid(Integer.parseInt(user.getSystemuserid()));
                    ts.setSystemusertaskid(Integer.parseInt(systemusertask.getSystemusertaskid()));
                    ts.setTaskstatusind(1);
                    this.create(ts);
                    log.info("DMS_info:" + "增加一条任务状态记录:" + ReflectionToStringBuilder.toString(ts).toString() + "成功");
                }
            } else this.create(taskStatus);
            return true;
        } catch (DaoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * 
	 * Description: 查询所有应接受的任务
	 * 
	 * @Version1.0 Apr 1, 2009 11:30:56 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemusergroupvalue
	 * @param systemusergroupid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List listSystemUserReceivedTask(final int systemuserid, final int taskacceptergroupid, final int taskstatusid, final int pageSize, final int startRow) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                String sql = "select t.systemuserstatus,s.systemusertaskid, s.systemusertaskassigner, s.systemusertaskaccepter, s.systemusertaskassigntime, s.systemusertasktitle, s.systemusetaskcontent, s.systemusertaskduetime, s.systemusertaskmisc, s.systemusertaskaccepterid, s.systemusertaskind, s.taskacceptergroupid, s.systemusertaskassignerid, s.systemusertaskacceptergroupname,t.taskstatusid from  systemusertask as s,taskstatus as t where s.systemusertaskid = t.systemusertaskid and  t.systemuserid = " + systemuserid + " and  s.systemusertaskind = 1";
                if (taskstatusid != 0) {
                    sql += (" and t.taskstatusid = " + taskstatusid);
                }
                Query query = s.createSQLQuery(sql);
                query.setFirstResult(startRow);
                query.setMaxResults(pageSize);
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description: 查询收到的任务的总数
	 * 
	 * @Version1.0 Apr 19, 2009 12:53:24 AM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemuserid
	 * @param taskacceptergroupid
	 * @param taskstatusid
	 * @return
	 */
    public int getRowsForListReicivedTask(final int systemuserid, final int taskacceptergroupid, final int taskstatusid) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                String sql = "select count(t.taskstatusid) from  systemusertask as s,taskstatus as t where s.systemusertaskid = t.systemusertaskid and  t.systemuserid = " + systemuserid + " and  s.systemusertaskind = 1";
                if (taskstatusid != 0) {
                    sql += (" and t.taskstatusid = " + taskstatusid);
                }
                BigInteger count = (BigInteger) s.createSQLQuery(sql).uniqueResult();
                return count.intValue();
            }
        });
    }

    /**
	 * 
	 * Description: 查询所有的用户组
	 * 
	 * @Version1.0 Apr 1, 2009 11:31:17 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<SystemUserGroup> listAllUserGroup(int value) {
        return this.find("from SystemUserGroup where systemusergroupind = 1 and systemusergroupvalue <= " + value);
    }

    /**
	 * 
	 * Description: 查出所有级别(value)在我所在的用户组之下的所有用户
	 * 
	 * @Version1.0 Apr 2, 2009 3:37:28 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemusergroupid
	 * @param systemusergroupvalue
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<SystemUser> listAllUserBehindValue(final int systemusergroupid, final int systemusergroupvalue) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                Query query = s.createSQLQuery("select u.* from  systemuser as u,systemusergroup as g where u.systemuserind = 1 and g.systemusergroupind = 1 and u.systemusergroupid = g.systemusergroupid  and g.systemusergroupvalue <= " + systemusergroupvalue).addEntity(SystemUser.class);
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description: 通过用户组id查出所有的用户
	 * 
	 * @Version1.0 Apr 2, 2009 9:24:46 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemusergroupid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<SystemUser> findUserByGroupid(int systemusergroupid) {
        return this.find("from SystemUser where  systemuserind = 1 and systemusergroupid = " + systemusergroupid);
    }

    /**
	 * 根据发送者ID查找所有自己已发出的任务 Description:
	 * 
	 * @Version1.0 Apr 4, 2009 6:36:40 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemusertaskassignerid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<SystemUserTask> listSentTask(final int systemusertaskassignerid, final int pageSize, final int startRow) {
        return this.getHibernateTemplate().executeFind(new HibernateCallback() {

            public List doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from SystemUserTask where systemusertaskassignerid = " + systemusertaskassignerid + " and systemusertaskind = 1");
                query.setFirstResult(startRow);
                query.setMaxResults(pageSize);
                log.info("查出来的size是多少啊：" + query.list().size());
                return query.list();
            }
        });
    }

    /**
	 * 
	 * Description: 获得发送的任务数
	 * 
	 * @Version1.0 Apr 18, 2009 11:15:12 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param systemusertaskassignerid
	 * @return
	 * @throws DaoException
	 */
    public int getRowsForListSentTask(int systemusertaskassignerid) throws DaoException {
        List<SystemUserTask> list = this.find("from SystemUserTask where systemusertaskassignerid = " + systemusertaskassignerid + " and systemusertaskind = 1 ");
        return list.size();
    }

    /**
	 * 
	 * Description: 查询将要用于修改的任务
	 * 
	 * @Version1.0 Apr 4, 2009 8:40:09 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param taksid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<SystemUserTask> selectTaskForUpdate(int taskid) {
        return this.find("from SystemUserTask where systemusertaskid = " + taskid + " and systemusertaskind = 1");
    }

    /**
	 * 
	 * Description: 修改自己发布的任务,先假删除,然后修改
	 * 
	 * @Version1.0 Apr 4, 2009 9:51:26 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param sytemusertask
	 * @throws DaoException
	 */
    public void updateTask(SystemUserTask systemusertask, int oldSystemusertaskid) {
        try {
            List<SystemUserTask> tasklist = this.find("from SystemUserTask where systemusertaskid = " + oldSystemusertaskid);
            tasklist.get(0).setSystemusertaskind("0");
            this.update(tasklist.get(0));
            List<TaskStatus> tslist = this.find("from TaskStatus where systemusertaskid = " + oldSystemusertaskid);
            for (TaskStatus ts : tslist) {
                ts.setTaskstatusind(0);
                this.update(ts);
            }
            this.addTask(systemusertask);
            log.info("DMS_info:更新一条id为" + oldSystemusertaskid + "的任务记录成功,新的任务为:" + systemusertask);
        } catch (Exception e) {
            log.error("DMS_error:更新一条id为" + oldSystemusertaskid + "的任务记录失败,失败原因:" + e);
        }
    }

    /**
	 * 
	 * Description: 删除一条任务
	 * 
	 * @Version1.0 Apr 7, 2009 4:26:54 PM 郑旭（zhengxu2006@gmail.com）创建
	 * @param oldSystemusertaskid
	 */
    public String deleteTask(int oldSystemusertaskid) {
        try {
            List<SystemUserTask> tasklist = this.find("from SystemUserTask where systemusertaskid = " + oldSystemusertaskid);
            tasklist.get(0).setSystemusertaskind("0");
            this.update(tasklist.get(0));
            log.info("DMS_info:" + "删除id为:" + oldSystemusertaskid + "的任务记录成功");
            List<TaskStatus> tslist = this.find("from TaskStatus where systemusertaskid = " + oldSystemusertaskid);
            for (TaskStatus ts : tslist) {
                ts.setTaskstatusind(0);
                this.update(ts);
                log.info("DMS_info:" + "删除一条任务状态记录:" + ts + "成功!");
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("DMS_error:" + "删除id为:" + oldSystemusertaskid + "的任务记录失败,失败原因:" + e);
            return "failure";
        }
    }

    /**
 * 
 * Description: 更改用户任务的状态
 * @Version1.0 Apr 23, 2009 7:50:42 PM 郑旭（zhengxu2006@gmail.com）创建
 * @param systemuserstatus
 * @param taskstatusid
 * @return
 */
    public String changeStatus(final String systemuserstatus, final int taskstatusid) {
        return (String) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(org.hibernate.Session s) throws HibernateException, SQLException {
                SQLQuery query = (SQLQuery) s.createSQLQuery("update taskstatus set systemuserstatus = '" + systemuserstatus + "' where taskstatusid = " + taskstatusid);
                int num = query.executeUpdate();
                log.info("DMS_info:" + "更改id为" + taskstatusid + "的状态为" + systemuserstatus + "成功!影响的行数为" + num);
                return num + "";
            }
        });
    }
}
