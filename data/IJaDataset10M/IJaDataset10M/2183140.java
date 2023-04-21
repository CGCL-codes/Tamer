package com.siteeval.auth;

import com.siteeval.common.*;
import com.siteeval.exception.NoAuthException;
import com.siteeval.system.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class LogonForm {

    private Globa globa;

    private String error;

    UserSession userSession;

    private String strAllUnits;

    String[] strArryUnitId = null;

    public String getError() {
        return error;
    }

    public LogonForm(ServletContext application, HttpServletRequest request, HttpServletResponse response) {
        globa = new Globa();
        globa.initialize(application, request, response);
        globa.setDynamicProperty(this);
        error = null;
    }

    private int getErrorLockNum() {
        int intErrorLockNum = 3;
        try {
            intErrorLockNum = Integer.parseInt(globa.getPropValue(Constants.Error_Lock_Num));
        } catch (NumberFormatException ne) {
            intErrorLockNum = 3;
        } finally {
            return intErrorLockNum;
        }
    }

    public int authenticate() {
        int value = 0;
        HttpSession session = globa.session;
        userSession = new UserSession(globa.application);
        userSession.setExculpate(this.getScreensize());
        userSession.setLoginIp(globa.loginIp);
        userSession.setdLatestLoginTime(Format.getDateTime());
        String tUserId = Format.removeNull(this.getUsername()).replaceAll(" ", "");
        String strCaNO = Format.removeNull(this.getCaNO());
        userSession.setStrCaNO(this.getCaNO());
        try {
            if (tUserId.equals(Constants.Administrator) && this.getPassword().equals(MD5.getMD5ofString(Constants.initPass))) {
                userSession.setStrId("007");
                userSession.setStrUserId(tUserId);
                userSession.setStrName("���ù���Ա");
                userSession.setStrCssType("1");
                userSession.setStrUnitCode("00");
                value = 1;
                loadRight();
            } else {
                if (strCaNO.equals("") || !tUserId.equals("")) value = userPwdAuth(tUserId); else {
                    value = authMac(strCaNO);
                }
            }
            session.setAttribute(com.siteeval.common.Constants.USER_KEY, userSession);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoAuthException(error);
        } finally {
            globa.db.closeCon();
            return value;
        }
    }

    public int userPwdAuth(String tUserId) {
        int value = 0;
        int intErrorLockNum = getErrorLockNum();
        try {
            java.sql.ResultSet rs = null;
            String strSql = "SELECT strId,strUserId,strPWD,strName,intError,intState,intType,strEmail,strMsnQQ,strCssType,intUserType,strBuildId  FROM t_sy_user WHERE strUserId='" + tUserId + "' ";
            rs = globa.db.executeQuery(strSql);
            if (!rs.next()) {
                error = new String("�û������ڣ�������������û�������");
                value = -1;
            } else {
                userSession.setStrId(rs.getString("strId"));
                userSession.setStrUserId(rs.getString("strUserId"));
                String pwd = rs.getString("strPWD");
                if (pwd == null || !pwd.equals(this.getPassword())) {
                    error = new String("���������������");
                    return -1;
                }
                userSession.setStrPWD(this.getPassword1());
                userSession.setStrName(rs.getString("strName"));
                int intError = rs.getInt("intError");
                int intState = rs.getInt("intState");
                value = rs.getInt("intType");
                userSession.setIntType(value);
                userSession.setStrEmail(rs.getString("strEmail"));
                userSession.setStrMsnQQ(rs.getString("strMsnQQ"));
                userSession.setStrCssType(rs.getString("strCssType"));
                if (intState > Constants.U_STATE_ON) {
                    error = new String("���û��Ѿ�����ǽ��ã��������Ա��ϵ!");
                    return -1;
                }
                globa.db.executeUpdate("UPDATE t_sy_user SET intLoginNum=intLoginNum+1,intError=0,dLatestLoginTime='" + Format.getDateTime() + "'  where strUserId = '" + tUserId + "'");
                User user = new User(globa);
                userSession.setStrUnitId(user.arryUnitUser(" WHERE strUserId='" + tUserId + "'"));
                if (userSession.getStrUnitId() != null && userSession.getStrUnitId().length > 0) {
                    String tUnitId = userSession.getStrUnitId()[0];
                    userSession.setStrUnitCode(tUnitId);
                    userSession.setStrUnitName(SysUserUnit.getUnitName(tUnitId));
                    userSession.setUnitIds(Format.arryToStr2(userSession.getStrUnitId()));
                    userSession.setStrRootId(SysUserUnit.getRootId(tUnitId));
                } else userSession.setStrUnitCode("");
                loadRight();
                userSession.setStrAllUnits(strAllUnits);
                userSession.setIntUserType(rs.getInt("intUserType"));
                userSession.setStrBuildId(rs.getString("strBuildId"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    private int authMac(String CaNO) throws SQLException {
        int value = -1;
        boolean isNo = false;
        ResultSet rs = globa.db.executeQuery("SELECT * from t_sy_user where strCaNO='" + CaNO + "'");
        boolean isRes = rs.next();
        if (!isRes) {
            isNo = true;
            String tUserId = Format.removeNull(this.getPassword2());
            rs = globa.db.executeQuery("SELECT * from t_sy_user WHERE strUserId='" + tUserId + "' ");
            isRes = rs.next();
        }
        if (isRes) {
            userSession.setStrId(rs.getString("strId"));
            String userId = rs.getString("strUserId");
            userSession.setStrUserId(userId);
            userSession.setStrName(rs.getString("strName"));
            value = rs.getInt("intType");
            userSession.setIntType(value);
            userSession.setStrEmail(rs.getString("strEmail"));
            userSession.setStrMsnQQ(rs.getString("strMsnQQ"));
            userSession.setStrCaNO(rs.getString("strCaNO"));
            userSession.setStrCssType(rs.getString("strCssType"));
            if (isNo) globa.db.executeUpdate("UPDATE t_sy_user  SET strCaNO='" + CaNO + "'  where strUserId = '" + userId + "'");
            globa.db.executeUpdate("UPDATE t_sy_user SET intLoginNum=intLoginNum+1,intError=0,dLatestLoginTime='" + Format.getDateTime() + "'  where strUserId = '" + userId + "'");
            User user = new User(globa);
            userSession.setStrUnitId(user.arryUnitUser(" WHERE strUserId='" + userId + "'"));
            if (userSession.getStrUnitId() != null && userSession.getStrUnitId().length > 0) {
                String tUnitId = userSession.getStrUnitId()[0];
                userSession.setStrUnitCode(tUnitId);
                userSession.setStrUnitName(SysUserUnit.getUnitName(tUnitId));
                userSession.setUnitIds(Format.arryToStr2(userSession.getStrUnitId()));
                userSession.setStrRootId(SysUserUnit.getRootId(tUnitId));
            } else userSession.setStrUnitCode("");
            loadRight();
            userSession.setStrAllUnits(strAllUnits);
            userSession.setIntUserType(rs.getInt("intUserType"));
            userSession.setStrBuildId(rs.getString("strBuildId"));
        } else error = new String("��ĵ���Կ�������û������֤ʧ�ܣ�������");
        rs.close();
        return value;
    }

    public boolean doModiPass() {
        try {
            error = null;
            String sql = "UPDATE t_sy_user SET strPWD=? WHERE  strUserId=? AND strPWD=?";
            globa.db.prepareStatement(sql);
            globa.db.setString(1, this.getPassword1());
            globa.db.setString(2, this.getUsername());
            globa.db.setString(3, this.getPassword());
            if (!this.getPassword1().equals(this.getPassword2())) {
                error = new String("两次密码输入不一致");
            }
            int j = globa.db.executeUpdate();
            if (j <= 0) error = new String("修改密码失败");
            return error == null;
        } catch (SQLException e) {
            error = new String("数据库处理失败");
            return false;
        } finally {
            globa.db.closeCon();
        }
    }

    public boolean doSetPwd(String strid) {
        try {
            String sql = "UPDATE t_sy_user SET strPWD=?,intState=? ,intError=0 WHERE  strUserId=? ";
            globa.db.prepareStatement(sql);
            globa.db.setString(1, MD5.getMD5ofString(Constants.initPass));
            globa.db.setInt(2, Constants.U_STATE_ON);
            globa.db.setString(3, strid);
            globa.db.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            globa.db.closeCon();
        }
    }

    public boolean doExit() {
        globa.session.removeAttribute(Constants.USER_KEY);
        globa.session.invalidate();
        return true;
    }

    /**
     * see UserActor.update()
     */
    public static synchronized void restartAffairs() {
        APP_AFFAIRS = new java.util.Hashtable();
    }

    /**
     * see UserActor.allocate()
     * @param strid
     */
    public static void restartAffairs(String strid) {
        APP_AFFAIRS.remove(strid);
    }

    private static java.util.Hashtable APP_AFFAIRS = new java.util.Hashtable();

    private void loadRight() {
        java.sql.ResultSet rs;
        HashMap rights = userSession.getRights();
        try {
            if (userSession.getStrUserId().equals(Constants.Administrator)) {
                userSession.setStrName("���ù���Ա");
                rights.put("90005", "true");
                rights.put("90010", "true");
                rights.put("90015", "true");
                rights.put("91005", "true");
                rights.put("91010", "true");
                rights.put("91015", "true");
                rights.put("91020", "true");
                rights.put("91025", "true");
                rights.put("91030", "true");
            } else {
                String strSql = "SELECT strRoleId FROM t_sy_userRight WHERE strUserId='" + userSession.getStrId() + "' AND intType=0";
                rs = globa.db.executeQuery(strSql);
                while (rs.next()) {
                    rights.put(rs.getString("strRoleId"), "true");
                }
                String[] arryUnit = userSession.getStrUnitId();
                strAllUnits = "u/" + userSession.getStrUserId();
                if (arryUnit != null && arryUnit.length > 0) for (int i = 0; i < arryUnit.length; i++) {
                    Unit ug = SysUserUnit.getUserGroup(arryUnit[i]);
                    strAllUnits = strAllUnits + "," + SysUserUnit.getAllRootUnitIds(arryUnit[i]);
                }
            }
            System.out.println("Begin Load [" + userSession.getStrUserId() + "] 's  All  Affairs");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rights != null) APP_AFFAIRS.put(userSession.getStrUserId(), rights);
        }
    }

    private String username;

    private String password;

    private String caNO;

    private String password1;

    private String password2;

    private int screensize;

    private String ikeyDigest;

    public String getCaNO() {
        return Format.removeNull(caNO);
    }

    public void setCaNO(String caNO) {
        this.caNO = caNO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password1 = password;
        this.password = MD5.getMD5ofString(password);
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = MD5.getMD5ofString(password1);
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = MD5.getMD5ofString(password2);
    }

    public int getScreensize() {
        return screensize;
    }

    public void setScreensize(int screensize) {
        this.screensize = screensize;
    }

    public String getIkeyDigest() {
        return ikeyDigest;
    }

    public void setIkeyDigest(String ikeyDigest) {
        this.ikeyDigest = ikeyDigest;
    }

    public void reset() {
        username = null;
        password = null;
        password1 = null;
        password2 = null;
        ikeyDigest = null;
        screensize = 800;
    }
}
