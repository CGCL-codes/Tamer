package com.dcivision.webservice.samples.example4;

import java.sql.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.webservice.samples.example4.UserException;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.user.bean.UserRecord;

public class ListUserDAO {

    protected Log log = LogFactory.getLog(this.getClass().getName());

    private Connection con;

    public ListUserDAO(Connection con) {
        this.con = con;
    }

    public UserRecord[] listAll() {
        log.debug("ListUserDAO begin!");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection list = new ArrayList();
        String sql = "SELECT A.ID, A.LOGIN_NAME, A.LOGIN_PWD, A.FIRST_NAME, A.LAST_NAME, A.FULL_NAME, A.EMAIL, A.FAILED_ATTEMPT, A.MAX_ATTEMPT, A.PWD_EXPIRY_DAY, A.LAST_PWD_UPDATE_DATE, A.STATUS, A.USER_TYPE, A.USER_LINK_ID, A.PREFERENCE, A.LOCALE, A.USER_DEF_1, A.USER_DEF_2, A.USER_DEF_3, A.USER_DEF_4, A.USER_DEF_5, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE FROM  USER_RECORD A";
        try {
            if (con.isClosed()) {
                log.debug("connection is closed!");
                throw new IllegalStateException("error.unexpected");
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                log.debug("rs is not empty!");
                UserRecord tmpUserRecord = new UserRecord();
                tmpUserRecord.setID(new Integer(rs.getInt("ID")));
                tmpUserRecord.setLoginName(rs.getString("LOGIN_NAME"));
                tmpUserRecord.setLoginPwd(rs.getString("LOGIN_PWD"));
                tmpUserRecord.setFirstName(rs.getString("FIRST_NAME"));
                tmpUserRecord.setLastName(rs.getString("LAST_NAME"));
                tmpUserRecord.setFullName(rs.getString("FULL_NAME"));
                tmpUserRecord.setEmail(rs.getString("EMAIL"));
                tmpUserRecord.setFailedAttempt(new Integer(rs.getInt("FAILED_ATTEMPT")));
                tmpUserRecord.setMaxAttempt(new Integer(rs.getInt("MAX_ATTEMPT")));
                tmpUserRecord.setPwdExpiryDay(new Integer(rs.getInt("PWD_EXPIRY_DAY")));
                tmpUserRecord.setStatus(rs.getString("STATUS"));
                tmpUserRecord.setUserType(rs.getString("USER_TYPE"));
                list.add(tmpUserRecord);
                log.debug("list number " + list.size());
            }
            Iterator ite = list.iterator();
            UserRecord[] users = new UserRecord[list.size()];
            for (int i = 0; i < list.size(); i++) {
                users[i] = (UserRecord) ite.next();
            }
            log.debug("ListUserDAO end!");
            log.debug("users's number is " + users.length);
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("error.unexcted");
        } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("error.unexpected");
            }
        }
    }
}
