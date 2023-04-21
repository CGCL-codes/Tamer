package com.hotye.school.jbean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.hotye.school.dao.DaoBase;
import com.hotye.school.dbmgr.DBConnect;
import com.hotye.school.server.ServerUtil;
import com.hotye.school.util.GetDate;
import com.hotye.school.util.MD5;
import com.hotye.school.util.StrFun;

public class User extends DaoBase {

    private static final Logger logger = Logger.getLogger(User.class);

    private static final String SUPPER_ADMINISTRATOR_USER_NO = "administrator";

    private static final String SUPPER_ADMINISTRATOR_PASSWORD_MD5 = "E7F0091C12AC7436212046BA9B2D5C87";

    public int id;

    /**�ʺ�*/
    public String userNo;

    /**����*/
    public String userPassword;

    /**���*/
    public String userName;

    /**ע������*/
    public Date userRegDate;

    /**���һ�εĵ�¼ʱ��*/
    public Date userLastLogin;

    /**Ȩ�޼���*/
    public int userPurview;

    public User() {
    }

    public List<User> findUserList() {
        return findPojoList(User.class);
    }

    public boolean login(HttpServletRequest request) throws Exception {
        boolean isLogin = false;
        userNo = StrFun.getString(request, "userNo");
        userPassword = StrFun.getString(request, "userPassword");
        userLastLogin = new Date();
        if (SUPPER_ADMINISTRATOR_USER_NO.equals(userNo)) {
            if (SUPPER_ADMINISTRATOR_PASSWORD_MD5.equals(MD5.toMD5(userPassword))) {
                User user = new User();
                user.id = -1;
                user.userName = "��������Ա";
                user.userNo = SUPPER_ADMINISTRATOR_USER_NO;
                user.userPassword = SUPPER_ADMINISTRATOR_PASSWORD_MD5;
                user.userPurview = -1;
                ServerUtil.setUserFromSession(request, user);
                logger.warn("��������Ա��¼! ��¼��IP��ַ:" + request.getRemoteAddr());
                return true;
            }
        }
        String sql = "SELECT * FROM SS_User WHERE userNo='" + userNo + "' AND userPassword='" + userPassword + "'";
        String upSql = "UPDATE SS_User set userLastLogin='" + GetDate.getStringDateFormat(userLastLogin, 7) + "' WHERE userNo='" + userNo + "'";
        logger.debug("�û���¼SQL:" + sql);
        logger.debug("�û��޸ĵ�¼ʱ��SQL:" + sql);
        DBConnect dbConnect = new DBConnect();
        Connection conn = dbConnect.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = null;
        try {
            rs = st.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserNo(userNo);
                user.setUserPassword(userPassword);
                user.setUserName(rs.getString("userName"));
                user.setUserRegDate(rs.getDate("userRegDate"));
                user.setUserLastLogin(rs.getDate("userLastLogin"));
                user.setUserPurview(rs.getInt("userPurview"));
                StrFun.putSession(request, "usesr", user);
                dbConnect.executeUpdate(upSql);
                isLogin = true;
                ServerUtil.setUserFromSession(request, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
            dbConnect.close();
        }
        return isLogin;
    }

    public void logout(HttpServletRequest request) {
        ServerUtil.cleanUserFromSession(request);
    }

    /**
	 * �����û�
	 * @param request
	 * @throws Exception
	 */
    public void addUser(HttpServletRequest request) throws Exception {
        userNo = StrFun.getString(request, "userNo");
        userPassword = StrFun.getString(request, "userPassword");
        userName = StrFun.getString(request, "userName");
        userPurview = StrFun.getInt(request, "userPurview");
        if ("".equals(userPurview) || userPurview == 0) {
            userPurview = 1;
        }
        String sql = "INSERT INTO SS_User(userNo, userPassword, userName, userPurview)" + " VALUES('" + userNo + "', '" + userPassword + "', '" + userName + "'" + " ," + userPurview + ")";
        System.out.println("�����û�SQL:" + sql);
        DBConnect dbConnect = new DBConnect();
        try {
            dbConnect.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnect.close();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUserRegDate() {
        return userRegDate;
    }

    public void setUserRegDate(Date userRegDate) {
        this.userRegDate = userRegDate;
    }

    public Date getUserLastLogin() {
        return userLastLogin;
    }

    public void setUserLastLogin(Date userLastLogin) {
        this.userLastLogin = userLastLogin;
    }

    public int getUserPurview() {
        return userPurview;
    }

    public void setUserPurview(int userPurview) {
        this.userPurview = userPurview;
    }

    public static boolean isSupperAdmin(User user) {
        if (user == null) return false;
        if (user.id == -1 && SUPPER_ADMINISTRATOR_USER_NO.equals(user.userNo) && SUPPER_ADMINISTRATOR_PASSWORD_MD5.equals(user.userPassword)) return true;
        return false;
    }
}
