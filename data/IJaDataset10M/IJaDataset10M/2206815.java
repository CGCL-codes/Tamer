package com.handy.plugin.auth.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.handy.database.DBUtil;
import com.handy.plugin.auth.bean.HAuthAction;
import com.handy.plugin.auth.bean.HAuthDyna;
import com.handy.plugin.auth.bean.HAuthDynaRoleInfo;
import com.handy.plugin.auth.bean.HAuthDynaUserInfo;
import com.handy.plugin.auth.bean.HAuthDynaValue;
import com.handy.plugin.auth.bean.HAuthKind;
import com.handy.plugin.auth.bean.HAuthRole;
import com.handy.plugin.auth.bean.HAuthRoleInfo;
import com.handy.plugin.auth.bean.HAuthUser;
import com.handy.plugin.auth.bean.HAuthUserInfo;

public class HAuthDAO {

    /**
	 * 获取当前用户所有的权限类型。
	 * @param roleid
	 * @param userid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthKind> findAllAuthKind(Integer roleid, Integer userid) {
        StringBuffer sb = new StringBuffer();
        if (roleid < 3) {
            sb.append("select * from h_auth_kinds order by id");
        } else {
            sb.append("select * from h_auth_kinds where kind in (");
            sb.append("select distinct authkind from v_auths where ");
            sb.append("roleid=").append(roleid).append(" or ");
            sb.append("userid=").append(userid);
            sb.append(") order by id");
        }
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        BeanListHandler h = new BeanListHandler(HAuthKind.class);
        List<HAuthKind> list = null;
        try {
            list = (List<HAuthKind>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
	 * 获取当前用户所有的动态权限字段的键值对。
	 * @param authid
	 * @param authkind
	 * @param roleid
	 * @param userid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthDynaValue> findAllAuthDyna(Integer authid, String authkind, Integer roleid, Integer userid) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        List<HAuthDynaValue> list = null;
        if (roleid < 3) {
            sb.append("select * from h_auth_dyna where authid=").append(authid);
            BeanHandler h = new BeanHandler(HAuthDyna.class);
            HAuthDyna authDyna = null;
            try {
                authDyna = (HAuthDyna) run.query(sb.toString(), h);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            if (authDyna != null) {
                sb = new StringBuffer();
                sb.append("select ").append(authDyna.getPcol()).append(" pcolvalue,");
                sb.append(authDyna.getVcol()).append(" vcolvalue from ");
                sb.append(authDyna.getTname()).append(" ").append(authDyna.getCondition());
                BeanListHandler h1 = new BeanListHandler(HAuthDynaValue.class);
                try {
                    list = (List<HAuthDynaValue>) run.query(sb.toString(), h1);
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
            } else {
                return null;
            }
        } else {
            sb = new StringBuffer();
            sb.append("select vcolvalue,pcolvalue from ").append("v_auth_dynas where ");
            sb.append("(roleid=").append(roleid).append(" or ");
            sb.append("userid=").append(userid).append(") and authkind=").append(authkind).append(" ");
            sb.append("group by vcolvalue,pcolvalue");
            BeanListHandler h1 = new BeanListHandler(HAuthDynaValue.class);
            try {
                list = (List<HAuthDynaValue>) run.query(sb.toString(), h1);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        return list;
    }

    /**
	 * 查找所有合条件的用户
	 * @param authid
	 * @param authkind
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthUserInfo> findAllAuthUser(Integer authid, String authkind) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        sb.append("select * from v_auth_user_infos where ");
        sb.append("authid=").append(authid).append(" and ");
        sb.append("authkind=").append(authkind).append(" order by userid");
        BeanListHandler h = new BeanListHandler(HAuthUserInfo.class);
        List<HAuthUserInfo> list = null;
        try {
            list = (List<HAuthUserInfo>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
	 * 检索所有没有分配指定权限的用户。
	 * @param authid
	 * @param authkind
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthUser> findAllAuthUserNO(Integer authid, String authkind) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        sb.append("select * from h_auth_users where id not in (");
        sb.append("select userid from v_auth_user_infos where ");
        sb.append("authid=").append(authid).append(" and ");
        sb.append("authkind=").append(authkind);
        sb.append(") and roleid>2");
        BeanListHandler h = new BeanListHandler(HAuthUserInfo.class);
        List<HAuthUser> list = null;
        try {
            list = (List<HAuthUser>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
	 * 查找所有合条件的角色
	 * @param authid
	 * @param authkind
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthRoleInfo> findAllAuthRole(Integer authid, String authkind) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        sb.append("select * from v_auth_role_infos where ");
        sb.append("authid=").append(authid).append(" and ");
        sb.append("authkind=").append(authkind);
        BeanListHandler h = new BeanListHandler(HAuthRoleInfo.class);
        List<HAuthRoleInfo> list = null;
        try {
            list = (List<HAuthRoleInfo>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<HAuthRole> findAllAuthRoleNO(Integer authid, String authkind) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        sb.append("select * from h_auth_roles where id not in (");
        sb.append("select roleid from v_auth_role_infos where ");
        sb.append("authid=").append(authid).append(" and ");
        sb.append("authkind=").append(authkind);
        sb.append(")");
        BeanListHandler h = new BeanListHandler(HAuthRoleInfo.class);
        List<HAuthRole> list = null;
        try {
            list = (List<HAuthRole>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
	 * 查找所有合条件的用户
	 * @param authid
	 * @param authkind
	 * @param pcolvalue
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthDynaUserInfo> findAllAuthDynaUser(Integer authid, String authkind, String pcolvalue) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        sb.append("select * from v_auth_users where ");
        sb.append("authid=").append(authid).append(" and ");
        sb.append("authkind=").append(authkind).append(" and ");
        sb.append("pcolvalue=").append(pcolvalue);
        BeanListHandler h = new BeanListHandler(HAuthDynaUserInfo.class);
        List<HAuthDynaUserInfo> list = null;
        try {
            list = (List<HAuthDynaUserInfo>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
	 * 查找所有合条件的角色
	 * @param authid
	 * @param authkind
	 * @param pcolvalue
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthDynaRoleInfo> findAllAuthDynaRole(Integer authid, String authkind, String pcolvalue) {
        StringBuffer sb = new StringBuffer();
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        sb.append("select * from v_auth_users where ");
        sb.append("authid=").append(authid).append(" and ");
        sb.append("authkind=").append(authkind).append(" and ");
        sb.append("pcolvalue=").append(pcolvalue);
        BeanListHandler h = new BeanListHandler(HAuthDynaRoleInfo.class);
        List<HAuthDynaRoleInfo> list = null;
        try {
            list = (List<HAuthDynaRoleInfo>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
	 * 给某个角色在某个权限节点上添加权限
	 * @param authid
	 * @param authkind
	 * @param roleid
	 */
    public void insertAuthRoleInfo(Integer authid, String authkind, Integer roleid) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into h_auth_role_infos (id,authid,authkind,roleid) values (");
        sb.append("h_auth_role_info_seq.nextval,");
        sb.append(authid).append(",");
        sb.append("'").append(authkind).append("',");
        sb.append(roleid);
        sb.append(")");
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 给多个角色在某个权限节点上添加权限
	 * @param authid
	 * @param authkind
	 * @param roleids
	 */
    public void insertAuthRoleInfos(Integer authid, String authkind, Integer[] roleids) {
        if (null == roleids || roleids.length == 0) {
            return;
        }
        for (Integer roleid : roleids) {
            insertAuthRoleInfo(authid, authkind, roleid);
        }
    }

    /**
	 * 给某个用色在某个权限节点上添加权限
	 * @param authid
	 * @param authkind
	 * @param roleid
	 */
    public void insertAuthUserInfo(Integer authid, String authkind, Integer userid) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into h_auth_user_infos (id,authid,authkind,userid) values (");
        sb.append("h_auths_user_info_seq.nextval,");
        sb.append(authid).append(",");
        sb.append("'").append(authkind).append("',");
        sb.append(userid);
        sb.append(")");
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 给多个用户在某个权限节点上添加权限
	 * @param authid
	 * @param authkind
	 * @param roleids
	 */
    public void insertAuthUserInfos(Integer authid, String authkind, Integer[] userids) {
        if (null == userids || userids.length == 0) {
            return;
        }
        boolean isall = false;
        for (Integer userid : userids) {
            if (userid == -1) {
                isall = true;
                break;
            }
        }
        if (isall) {
            insertAuthUserInfo(authid, authkind, -1);
        } else {
            for (Integer userid : userids) {
                insertAuthUserInfo(authid, authkind, userid);
            }
        }
    }

    /**
	 * 给某个用色在某个权限节点上添加动态权限
	 * @param authid
	 * @param authkind
	 * @param roleid
	 */
    public void insertAuthDynaUserInfo(Integer authid, String vcolvalue, String pcolvalue, String authkind, Integer userid) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into h_auth_dyna_user_infos (id,authid,vcolvalue,pcolvalue,authkind,userid) values (");
        sb.append("h_auth_dyna_user_info_seq.nextval,");
        sb.append(authid).append(",");
        sb.append("'").append(vcolvalue).append("',");
        sb.append("'").append(pcolvalue).append("',");
        sb.append("'").append(authkind).append("',");
        sb.append(userid);
        sb.append(")");
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 给多个用户在某个权限节点上添加权限
	 * @param authid
	 * @param authkind
	 * @param roleids
	 */
    public void insertAuthDynaUserInfos(Integer authid, String vcolvalue, String pcolvalue, String authkind, Integer[] userids) {
        if (null == userids || userids.length == 0) {
            return;
        }
        for (Integer userid : userids) {
            insertAuthDynaUserInfo(authid, vcolvalue, pcolvalue, authkind, userid);
        }
    }

    /**
	 * 给某个角色在某个权限节点上添加动态权限
	 * @param authid
	 * @param authkind
	 * @param roleid
	 */
    public void insertAuthDynaRoleInfo(Integer authid, String vcolvalue, String pcolvalue, String authkind, Integer roleid) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into h_auth_dyna_role_infos (id,authid,vcolvalue,pcolvalue,authkind,roleid) values (");
        sb.append("h_auth_dyna_role_info_seq.nextval,");
        sb.append(authid).append(",");
        sb.append("'").append(vcolvalue).append("',");
        sb.append("'").append(pcolvalue).append("',");
        sb.append("'").append(authkind).append("',");
        sb.append(roleid);
        sb.append(")");
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 给多个角色在某个权限节点上添加动态权限
	 * @param authid
	 * @param authkind
	 * @param roleids
	 */
    public void insertAuthDynaRoleInfos(Integer authid, String vcolvalue, String pcolvalue, String authkind, Integer[] roleids) {
        if (null == roleids || roleids.length == 0) {
            return;
        }
        for (Integer roleid : roleids) {
            insertAuthDynaRoleInfo(authid, vcolvalue, pcolvalue, authkind, roleid);
        }
    }

    /**
	 * 删除指定id的h_auth_role_infos表记录
	 * @param id
	 */
    public void deleteAuthRoleInfo(Integer id) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from h_auth_role_infos where ");
        sb.append("id=").append(id);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 删除指定id的h_auth_user_infos表记录
	 * @param id
	 */
    public void deleteAuthUserInfo(Integer id) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from h_auth_user_infos where ");
        sb.append("id=").append(id);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 删除指定id的h_auth_dyna_user_infos表记录
	 * @param id
	 */
    public void deleteAuthDynaUserInfo(Integer id) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from h_auth_dyna_user_infos where ");
        sb.append("id=").append(id);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 删除指定id的h_auth_dyna_role_infos表记录
	 * @param id
	 */
    public void deleteAuthDynaRoleInfo(Integer id) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from h_auth_dyna_role_infos where ");
        sb.append("id=").append(id);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
	 * 检索authid上所有actionclass。
	 * @param authid
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<HAuthAction> findAllAction(Integer authid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from h_auth_actions where authid=").append(authid);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        BeanListHandler h = new BeanListHandler(HAuthAction.class);
        List<HAuthAction> list = null;
        try {
            list = (List<HAuthAction>) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    public void insertAction(Integer authid, String actionclass) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into h_auth_actions (id,authid,actionclass) values (");
        sb.append("h_auth_action_seq.nextval,");
        sb.append(authid).append(",");
        sb.append("'").append(actionclass).append("'");
        sb.append(")");
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteAction(Integer id) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from h_auth_actions where id=").append(id);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public boolean insertAuthDyna(HAuthDyna bean) {
        boolean flag = true;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into h_auth_dyna (id,tname,vcol,pcol,condition,authid) values (");
        sb.append("h_auth_dyna_seq.nextval,");
        sb.append("'").append(bean.getTname()).append("',");
        sb.append("'").append(bean.getVcol()).append("',");
        sb.append("'").append(bean.getPcol()).append("',");
        sb.append("'").append(bean.getCondition()).append("',");
        sb.append(bean.getAuthid());
        sb.append(")");
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            flag = false;
            log.error(e.getMessage());
        }
        return flag;
    }

    public boolean updateAuthDyna(HAuthDyna bean) {
        if (null == AuthDynaFindByAuthid(bean.getAuthid())) {
            if (insertAuthDyna(bean)) return true; else return false;
        }
        boolean flag = true;
        StringBuffer sb = new StringBuffer();
        sb.append("update h_auth_dyna set ");
        sb.append("tname='").append(bean.getTname()).append("',");
        sb.append("vcol='").append(bean.getVcol()).append("',");
        sb.append("pcol='").append(bean.getPcol()).append("',");
        sb.append("condition='").append(bean.getCondition()).append("'");
        sb.append("where authid=").append(bean.getAuthid());
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            flag = false;
            log.error(e.getMessage());
        }
        return flag;
    }

    public void deleteAuthDyna(Integer authid) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from h_auth_dyna where authid=").append(authid);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            run.update(sb.toString());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public HAuthDyna AuthDynaFindByAuthid(Integer authid) {
        HAuthDyna bean = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from h_auth_dyna where authid=").append(authid);
        BeanHandler h = new BeanHandler(HAuthDyna.class);
        QueryRunner run = new QueryRunner(dbUtil.dataSource(getDataSourceName()));
        try {
            bean = (HAuthDyna) run.query(sb.toString(), h);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return bean;
    }

    private static final Log log = LogFactory.getLog(HAuthDAO.class);

    private DBUtil dbUtil = DBUtil.getInstance();

    private String dataSourceName;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
