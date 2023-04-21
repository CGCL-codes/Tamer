package com.bones.query.sysexpert_new.dao.jdbc;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import com.bones.core.dao.BaseDaoJdbc;
import com.bones.core.utils.DaoUtils;
import com.bones.core.utils.JdbcDaoUtils;
import com.bones.core.utils.exception.DaoAccessException;
import com.bones.core.web.tags.DefaultPagination;
import com.bones.core.web.tags.IPagination;
import com.bones.query.sysexpert_new.dao.IUserLoginLogDao;
import com.bones.query.sysexpert_new.dao.entity.UserLoginLog;

/** DAO层Jdbc实现 */
public class UserLoginLogDaoJdbcImpl extends BaseDaoJdbc implements IUserLoginLogDao {

    /** 分页查询 USER_LOGIN_LOG列表 */
    public IPagination queryUserLoginLogList(UserLoginLog userLoginLog, int first, int max) throws DaoAccessException {
        StringBuffer querySql = new StringBuffer("");
        if (userLoginLog.getId() != null) {
            querySql.append(" and t.id = '").append(userLoginLog.getId()).append("' ");
        }
        if (userLoginLog.getVisitUser() != null && userLoginLog.getVisitUser().length() > 0) {
            querySql.append(" and lower(trim(t.visit_user)) like '%").append(userLoginLog.getVisitUser().toLowerCase()).append("%' ");
        }
        if (userLoginLog.getVisitIp() != null && userLoginLog.getVisitIp().length() > 0) {
            querySql.append(" and lower(trim(t.visit_ip)) like '%").append(userLoginLog.getVisitIp().toLowerCase()).append("%' ");
        }
        if (userLoginLog.getAppSystem() != null && userLoginLog.getAppSystem().length() > 0) {
            querySql.append(" and lower(trim(t.app_system)) like '%").append(userLoginLog.getAppSystem().toLowerCase()).append("%' ");
        }
        if (userLoginLog.getLoginTimeStr() != null && userLoginLog.getLoginTimeStr().length() > 0) {
            querySql.append(" and t.login_time >= to_date('").append(userLoginLog.getLoginTime()).append("', 'yyyy-MM-dd hh24:mi:ss') ");
        }
        if (userLoginLog.getIsSuccess() != null) {
            querySql.append(" and t.is_success = '").append(userLoginLog.getIsSuccess()).append("' ");
        }
        if (userLoginLog.getReason() != null && userLoginLog.getReason().length() > 0) {
            querySql.append(" and lower(trim(t.reason)) like '%").append(userLoginLog.getReason().toLowerCase()).append("%' ");
        }
        String infoSql = " select * from user_login_log t" + " where 1=1" + querySql.toString() + " order by id";
        StringBuffer countSql = new StringBuffer();
        countSql.append(" select count(*) from (");
        countSql.append(infoSql);
        countSql.append(" ) tcount ");
        Long count = JdbcDaoUtils.getCount(countSql.toString(), true);
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from (").append(infoSql).append(") tpagination limit ?, ?");
        int start = first == 0 ? 0 : first + max;
        List resultList = this.getJdbcTemplate().queryForList(sb.toString(), new Object[] { start, 12 });
        IPagination pagination = new DefaultPagination(first, max);
        pagination.setAllCount(count.intValue());
        if (resultList != null && resultList.size() > 0) {
            List<UserLoginLog> retList = new ArrayList<UserLoginLog>();
            Iterator it = resultList.iterator();
            while (it.hasNext()) {
                Map objMap = (Map) it.next();
                UserLoginLog vo = new UserLoginLog();
                if (objMap.get("id") != null) {
                    vo.setId(Integer.valueOf(String.valueOf(objMap.get("id"))));
                }
                vo.setVisitUser(String.valueOf(objMap.get("visit_user")));
                vo.setVisitIp(String.valueOf(objMap.get("visit_ip")));
                vo.setAppSystem(String.valueOf(objMap.get("app_system")));
                if (objMap.get("login_time") != null) {
                    vo.setLoginTime(Timestamp.valueOf(String.valueOf(objMap.get("login_time"))));
                    String str = String.valueOf(objMap.get("login_time"));
                    if (str.indexOf(" 00:00:00.0") != -1) {
                        str = str.substring(0, str.indexOf(" 00:00:00.0"));
                    }
                    if (str.substring(str.length() - 2, str.length()).equals(".0")) {
                        str = str.substring(0, str.length() - 2);
                    }
                    vo.setLoginTimeStr(str);
                }
                if (objMap.get("is_success") != null) {
                    vo.setIsSuccess(Integer.valueOf(String.valueOf(objMap.get("is_success"))));
                }
                vo.setReason(String.valueOf(objMap.get("reason")));
                vo = (UserLoginLog) DaoUtils.cleanObject(vo);
                retList.add(vo);
            }
            pagination.setResults(retList);
        }
        return pagination;
    }

    /** 查询单条—— USER_LOGIN_LOG详情 */
    public UserLoginLog viewUserLoginLog(UserLoginLog userLoginLog) throws DaoAccessException {
        StringBuffer querySql = new StringBuffer();
        if (userLoginLog.getId() != null) {
            querySql.append(" and t.id = '").append(userLoginLog.getId()).append("' ");
        }
        if (userLoginLog.getVisitUser() != null) {
            querySql.append(" and trim(t.visit_user) = '").append(userLoginLog.getVisitUser()).append("' ");
        }
        if (userLoginLog.getVisitIp() != null) {
            querySql.append(" and trim(t.visit_ip) = '").append(userLoginLog.getVisitIp()).append("' ");
        }
        if (userLoginLog.getAppSystem() != null) {
            querySql.append(" and trim(t.app_system) = '").append(userLoginLog.getAppSystem()).append("' ");
        }
        if (userLoginLog.getLoginTime() != null) {
            querySql.append(" and trim(t.login_time) = '").append(userLoginLog.getLoginTimeStr()).append("' ");
        }
        if (userLoginLog.getIsSuccess() != null) {
            querySql.append(" and t.is_success = '").append(userLoginLog.getIsSuccess()).append("' ");
        }
        String sql = " select " + "		t.id," + "		t.visit_user," + "		t.visit_ip," + "		t.app_system," + "		t.login_time," + "		t.is_success," + "		t.reason" + " from user_login_log t\n" + " where 1=1\n" + querySql.toString() + " ";
        List list = getJdbcTemplate().query(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserLoginLog vo = new UserLoginLog();
                if (rs.getObject("id") != null) {
                    vo.setId(Integer.valueOf(String.valueOf(rs.getObject("id"))));
                }
                vo.setVisitUser(String.valueOf(rs.getObject("visit_user")));
                vo.setVisitIp(String.valueOf(rs.getObject("visit_ip")));
                vo.setAppSystem(String.valueOf(rs.getObject("app_system")));
                if (rs.getObject("login_time") != null) {
                    vo.setLoginTime(rs.getDate("login_time"));
                    String str = String.valueOf(rs.getDate("login_time"));
                    if (str.indexOf(" 00:00:00.0") != -1) {
                        str = str.substring(0, str.indexOf(" 00:00:00.0"));
                    }
                    if (str.substring(str.length() - 2, str.length()).equals(".0")) {
                        str = str.substring(0, str.length() - 2);
                    }
                    vo.setLoginTimeStr(str);
                }
                if (rs.getObject("is_success") != null) {
                    vo.setIsSuccess(Integer.valueOf(String.valueOf(rs.getObject("is_success"))));
                }
                vo.setReason(String.valueOf(rs.getObject("reason")));
                return DaoUtils.cleanObject(vo);
            }
        });
        if (null != list && list.size() > 0) {
            return (UserLoginLog) list.get(0);
        }
        return null;
    }

    /** 删除单条—— USER_LOGIN_LOG记录 */
    public void delUserLoginLog(UserLoginLog userLoginLog) throws DaoAccessException {
        StringBuffer querySql = new StringBuffer();
        if (userLoginLog.getId() != null) {
            querySql.append(" and id = '").append(userLoginLog.getId()).append("' ");
        }
        if (userLoginLog.getVisitUser() != null && userLoginLog.getVisitUser().length() > 0) {
            querySql.append(" and trim(visit_user) = '").append(userLoginLog.getVisitUser()).append("' ");
        }
        if (userLoginLog.getVisitIp() != null && userLoginLog.getVisitIp().length() > 0) {
            querySql.append(" and trim(visit_ip) = '").append(userLoginLog.getVisitIp()).append("' ");
        }
        if (userLoginLog.getAppSystem() != null && userLoginLog.getAppSystem().length() > 0) {
            querySql.append(" and trim(app_system) = '").append(userLoginLog.getAppSystem()).append("' ");
        }
        if (userLoginLog.getLoginTime() != null) {
            String dateFormat = "yyyy-MM-dd";
            String dateStr = String.valueOf(userLoginLog.getLoginTime());
            if (dateStr.length() > 13) {
                if (dateStr.charAt(dateStr.length() - 2) == '.') {
                    dateStr = dateStr.substring(0, dateStr.length() - 2);
                }
                dateFormat = "yyyy-MM-dd hh24:mi:ss";
            }
            querySql.append(" and login_time = to_date('").append(dateStr).append("','" + dateFormat + "') ");
        }
        if (userLoginLog.getIsSuccess() != null) {
            querySql.append(" and is_success = '").append(userLoginLog.getIsSuccess()).append("' ");
        }
        String sql = " delete " + " from user_login_log \n" + " where 1=1 \n" + querySql.toString() + " ";
        getJdbcTemplate().execute(sql);
    }
}
