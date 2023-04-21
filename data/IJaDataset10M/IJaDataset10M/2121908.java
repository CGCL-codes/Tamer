package nps.core;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;
import com.microfly.core.common.Constants;
import com.microfly.core.db.Database;
import nps.exception.NpsException;
import nps.exception.ErrorHelper;
import nps.util.tree.TreeNode;

/**
 * �û���
 * a new publishing system
 * Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 */
public class User implements TreeNode, Serializable, Constants {

    private String id;

    private String name;

    private String account;

    private String telephone;

    private String fax;

    private String email;

    private String mobile;

    private String face;

    private int index;

    private int type;

    private String password = null;

    private Locale locale = Config.LOCALE;

    private Unit unit = null;

    private Dept dept = null;

    private Hashtable roles_by_name = null;

    private Hashtable roles_by_id = null;

    private Hashtable sites_owners = null;

    private Hashtable sites_myunit = null;

    private Hashtable sites = null;

    private String default_site = null;

    protected User(String id, String name, String account, int type) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.type = type;
    }

    public static User Login(String account, String password) throws NpsException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        User u = null;
        String u_account = account.trim().toUpperCase();
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            sql = "select a.id,a.name,a.password,a.telephone,a.fax,a.email,a.mobile,a.utype,a.cx,a.face,b.id deptid,b.name deptname,b.code deptcode,b.cx deptcx,b.parentid parentdept,c.id unitid from users a,dept b,unit c where a.dept=b.id and b.unit=c.id and a.account=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u_account);
            rs = pstmt.executeQuery();
            if (!rs.next()) return null;
            if (password == null && rs.getString("password") != null) return null;
            if (password != null && !password.equals(rs.getString("password"))) return null;
            u = new User(rs.getString("id"), rs.getString("name"), u_account, rs.getInt("utype"));
            u.email = rs.getString("email");
            u.fax = rs.getString("fax");
            u.mobile = rs.getString("mobile");
            u.telephone = rs.getString("telephone");
            u.face = rs.getString("face");
            u.index = rs.getInt("cx");
            u.unit = Unit.GetUnit(conn, rs.getString("unitid"));
            u.dept = u.unit.GetDeptTree(conn).GetDept(rs.getString("deptid"));
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            sql = "select b.* from UserRole a,Role b where a.roleid = b.id and a.userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u.id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (u.roles_by_name == null) u.roles_by_name = new Hashtable();
                if (u.roles_by_id == null) u.roles_by_id = new Hashtable();
                u.roles_by_name.put(rs.getString("name"), rs.getString("id"));
                u.roles_by_id.put(rs.getString("id"), rs.getString("name"));
            }
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (u.type == USER_SYSADMIN) {
                sql = "select id,name from site";
                pstmt = conn.prepareStatement(sql);
            } else {
                sql = "select b.id,b.name from site b,site_owner a where a.siteid=b.id and a.userid=? and b.state=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, u.id);
                pstmt.setInt(2, SITE_NORMAL);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (u.default_site == null) u.default_site = rs.getString("id"); else u.default_site = null;
                if (u.sites_owners == null) u.sites_owners = new Hashtable();
                u.sites_owners.put(rs.getString("id"), rs.getString("name"));
                if (u.type == USER_SYSADMIN) {
                    if (u.sites_myunit == null) u.sites_myunit = new Hashtable();
                    u.sites_myunit.put(rs.getString("id"), rs.getString("name"));
                }
            }
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (u.type != USER_SYSADMIN) {
                sql = "select id,name from site where unit=? and state=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, u.GetUnitId());
                pstmt.setInt(2, SITE_NORMAL);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (u.sites_myunit == null) u.sites_myunit = new Hashtable();
                    u.sites_myunit.put(rs.getString("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return u;
    }

    public User GetUser(String id) throws NpsException {
        if (id == null) return null;
        if (id.equalsIgnoreCase(this.id)) return this;
        Connection conn = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            return GetUser(conn, id);
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public User GetUser(Connection conn, String id) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        User u = null;
        try {
            sql = "select a.name,a.account,a.telephone,a.fax,a.email,a.mobile,a.utype,a.face,b.id deptid,b.name deptname,b.code deptcode,b.cx deptcx,b.parentid parentdept,b.unit unitid from users a,dept b where a.dept=b.id and a.id=?";
            if (!IsSysAdmin()) sql += " and b.unit=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            if (!IsSysAdmin()) pstmt.setString(2, GetUnitId());
            rs = pstmt.executeQuery();
            if (!rs.next()) return null;
            u = new User(id, rs.getString("name"), rs.getString("account"), rs.getInt("utype"));
            u.email = rs.getString("email");
            u.fax = rs.getString("fax");
            u.mobile = rs.getString("mobile");
            u.telephone = rs.getString("telephone");
            u.face = rs.getString("face");
            u.unit = Unit.GetUnit(conn, rs.getString("unitid"));
            u.dept = u.unit.GetDeptTree(conn).GetDept(rs.getString("deptid"));
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            sql = "select b.* from UserRole a,Role b where a.roleid = b.id and a.userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (u.roles_by_name == null) u.roles_by_name = new Hashtable();
                if (u.roles_by_id == null) u.roles_by_id = new Hashtable();
                u.roles_by_name.put(rs.getString("name"), rs.getString("id"));
                u.roles_by_id.put(rs.getString("id"), rs.getString("name"));
            }
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (u.type == USER_SYSADMIN) {
                sql = "select id,name from site";
                pstmt = conn.prepareStatement(sql);
            } else {
                sql = "select b.id,b.name from site b,site_owner a where a.siteid=b.id and a.userid=? and b.state=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, u.id);
                pstmt.setInt(2, SITE_NORMAL);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (u.default_site == null) u.default_site = rs.getString("id"); else u.default_site = null;
                if (u.sites_owners == null) u.sites_owners = new Hashtable();
                u.sites_owners.put(rs.getString("id"), rs.getString("name"));
                if (u.type == USER_SYSADMIN) {
                    if (u.sites_myunit == null) u.sites_myunit = new Hashtable();
                    u.sites_myunit.put(rs.getString("id"), rs.getString("name"));
                }
            }
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (u.type != USER_SYSADMIN) {
                sql = "select id,name from site where unit=? and state=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, u.GetUnitId());
                pstmt.setInt(2, SITE_NORMAL);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (u.sites_myunit == null) u.sites_myunit = new Hashtable();
                    u.sites_myunit.put(rs.getString("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
        return u;
    }

    public String GetFullname() {
        return name + "(" + dept.GetName() + "/" + unit.GetName() + ")";
    }

    public User NewUser(String name, String account, String password, String unitid, String deptid, int utype) throws NpsException {
        if (name == null || account == null || unitid == null || deptid == null) {
            throw new NpsException("�û�����Ϊ��", ErrorHelper.INPUT_ERROR);
        }
        if (account == null) {
            throw new NpsException("�ʺŲ���Ϊ��", ErrorHelper.INPUT_ERROR);
        }
        if (unitid == null) {
            throw new NpsException("��λ����Ϊ��", ErrorHelper.INPUT_ERROR);
        }
        if (deptid == null) {
            throw new NpsException("���Ų���Ϊ��", ErrorHelper.INPUT_ERROR);
        }
        if (this.type != USER_SYSADMIN) {
            if (utype == USER_SYSADMIN) {
                throw new NpsException("��û�д��������ʺŵ�Ȩ��", ErrorHelper.ACCESS_NOPRIVILEGE);
            }
            if (!unit.GetId().equalsIgnoreCase(unitid)) {
                throw new NpsException("�㲻���ڱ���λ�����ܴ����û�", ErrorHelper.ACCESS_NOPRIVILEGE);
            }
            if (!IsLocalAdmin()) {
                throw new NpsException("�㲻�Ǳ���λ����Ա�����ܴ����û�", ErrorHelper.ACCESS_NOPRIVILEGE);
            }
        }
        Connection conn = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            String id = GenerateUserID(conn);
            User user = new User(id, name, account.toUpperCase(), utype);
            user.password = password;
            user.unit = Unit.GetUnit(conn, unitid);
            user.dept = user.unit.GetDeptTree(conn).GetDept(deptid);
            return user;
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    private String GenerateUserID(Connection conn) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select seq_user.nextval userid from dual");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("userid");
            }
            return null;
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e1) {
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
        return null;
    }

    public void Save(Connection conn, boolean bNew) throws NpsException {
        try {
            if (bNew) Save(conn); else Update(conn);
        } catch (NpsException e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
            }
            throw e;
        }
    }

    private void Save(Connection conn) throws NpsException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("insert into users(id,name,account,password,telephone,fax,email,mobile,face,cx,dept,utype) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, account);
            pstmt.setString(4, password);
            pstmt.setString(5, telephone);
            pstmt.setString(6, fax);
            pstmt.setString(7, email);
            pstmt.setString(8, mobile);
            pstmt.setString(9, face);
            pstmt.setInt(10, index);
            pstmt.setString(11, dept.GetId());
            pstmt.setInt(12, type);
            pstmt.executeUpdate();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    private void Update(Connection conn) throws NpsException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("update users set name=?,account=?,telephone=?,fax=?,email=?,mobile=?,face=?,cx=?,dept=?,utype=? where id=?");
            pstmt.setString(1, name);
            pstmt.setString(2, account);
            pstmt.setString(3, telephone);
            pstmt.setString(4, fax);
            pstmt.setString(5, email);
            pstmt.setString(6, mobile);
            pstmt.setString(7, face);
            pstmt.setInt(8, index);
            pstmt.setString(9, dept.GetId());
            pstmt.setInt(10, type);
            pstmt.setString(11, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public void ResetPassword(String uid) throws NpsException {
        ResetPassword(uid, DEFAULT_PASSWORD);
    }

    public void ResetPassword(String uid, String pass) throws NpsException {
        if (!IsSysAdmin() && !IsLocalAdmin()) throw new NpsException(ErrorHelper.ACCESS_NOPRIVILEGE);
        Connection conn = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            if (!IsSysAdmin()) {
                User aUser = GetUser(conn, uid);
                if (aUser == null) throw new NpsException("û���ҵ�ָ���û���Ϣ���ǹ�λ���û���������������", ErrorHelper.SYS_NOUSER);
            }
            ChangePassword(conn, uid, pass);
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (Exception e1) {
            }
        }
    }

    public void ChangePassword(String oldpass, String newpass) throws NpsException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            String sql = "select password from users where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (!rs.next()) throw new NpsException("û���ҵ�" + name + "���û���Ϣ", ErrorHelper.SYS_NOUSER);
            String dbpass = rs.getString("password");
            if (oldpass != null && dbpass != null) {
                if (!oldpass.equals(dbpass)) throw new NpsException("ԭ���벻��ȷ", ErrorHelper.SYS_PASSWORD_ERROR);
            } else if ((oldpass != null && dbpass == null) || (oldpass == null && dbpass != null)) {
                throw new NpsException("ԭ���벻��ȷ", ErrorHelper.SYS_PASSWORD_ERROR);
            }
            try {
                rs.close();
            } catch (Exception e1) {
            }
            try {
                pstmt.close();
            } catch (Exception e2) {
            }
            ChangePassword(conn, id, newpass);
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (Exception e1) {
            }
        }
    }

    private void ChangePassword(Connection conn, String uid, String pass) throws NpsException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("update users set password=? where id=?");
            pstmt.setString(1, pass);
            pstmt.setString(2, uid);
            pstmt.executeUpdate();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public List Delete(String[] uids) throws NpsException {
        if (uids == null || uids.length == 0) return null;
        if (!IsSysAdmin() && !IsLocalAdmin()) throw new NpsException(ErrorHelper.ACCESS_NOPRIVILEGE);
        ArrayList list = new ArrayList(uids.length);
        Connection conn = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            conn.setAutoCommit(false);
            for (Object obj : uids) {
                String uid = (String) obj;
                if (uid == null || uid.length() == 0) continue;
                User aUser = null;
                String owner = null;
                try {
                    aUser = GetUser(conn, uid);
                    if (aUser == null) continue;
                    owner = GetDefaultOwner(conn, aUser.GetUnit(), uids);
                } catch (Exception e) {
                    nps.util.DefaultLog.error_noexception(e);
                    continue;
                }
                Delete(conn, aUser, owner);
                list.add(uid);
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
            }
            list.clear();
            nps.util.DefaultLog.error(e);
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (Exception e1) {
            }
        }
        return list;
    }

    public void Delete() throws NpsException {
        Delete(new String[] { id });
    }

    private String GetDefaultOwner(Connection conn, Unit aunit, String[] excepts) throws NpsException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String owner = null;
        try {
            String sql = "Select a.userid From site_owner a,users b,dept c Where a.userid=b.Id and b.dept=c.id and c.unit=?";
            String clause_where = "";
            if (excepts != null && excepts.length > 0) {
                for (Object obj : excepts) {
                    String except = (String) obj;
                    clause_where = " and b.id<>'" + except + "'";
                }
            }
            pstmt = conn.prepareStatement(sql + clause_where);
            pstmt.setString(1, aunit.GetId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                owner = rs.getString("userid");
            } else {
                try {
                    rs.close();
                } catch (Exception e1) {
                }
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
                sql = "select id from users b where utype=9 " + clause_where;
                pstmt = conn.prepareStatement(sql + clause_where);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    owner = rs.getString("id");
                }
            }
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e1) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
        return owner;
    }

    private void Delete(Connection conn, User user, String owner) throws NpsException {
        PreparedStatement pstmt = null;
        String sql = null;
        try {
            sql = "update article set creator=? where creator=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, owner);
            pstmt.setString(2, user.GetId());
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "update template set creator=? where creator=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, owner);
            pstmt.setString(2, user.GetId());
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "delete from userrole where userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.GetId());
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "delete from topic_owner where userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.GetId());
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "delete from site_owner where userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.GetId());
            pstmt.executeUpdate();
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "delete from users where id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.GetId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public void SetRoles(Connection conn, User user, String[] roles) throws NpsException {
        if (!IsSysAdmin() && !IsLocalAdmin()) throw new NpsException(ErrorHelper.ACCESS_NOPRIVILEGE);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "delete from userrole where userid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.id);
            pstmt.executeUpdate();
            if (roles != null && roles.length > 0) {
                try {
                    pstmt.close();
                } catch (Exception e1) {
                }
                sql = "insert into userrole(userid,roleid) values(?,?)";
                pstmt = conn.prepareStatement(sql);
                for (int i = 0; i < roles.length; i++) {
                    if (roles[i] != null && roles[i].length() > 0) {
                        pstmt.setString(1, user.GetId());
                        pstmt.setString(2, roles[i]);
                        pstmt.executeUpdate();
                    }
                }
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
            if (user.roles_by_name != null) user.roles_by_name.clear();
            if (user.roles_by_id != null) user.roles_by_id.clear();
            if (roles != null && roles.length > 0) {
                sql = "select b.* from UserRole a,Role b where a.roleid = b.id and a.userid=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.id);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (user.roles_by_name == null) user.roles_by_name = new Hashtable();
                    if (user.roles_by_id == null) user.roles_by_id = new Hashtable();
                    user.roles_by_name.put(rs.getString("name"), rs.getString("id"));
                    user.roles_by_id.put(rs.getString("id"), rs.getString("name"));
                }
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
            }
            nps.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }

    public boolean HasRole(String roleid) {
        if (roles_by_id != null && roles_by_id.containsKey(roleid)) return true;
        return false;
    }

    public boolean HasRoleName(String rolename) {
        if (roles_by_name != null && roles_by_name.containsKey(rolename)) return true;
        return false;
    }

    public boolean IsSysAdmin() {
        return type == USER_SYSADMIN;
    }

    public boolean IsSiteAdmin(String site_id) {
        if (sites_owners == null || sites_owners.isEmpty()) return false;
        return sites_owners.containsKey(site_id);
    }

    public boolean IsLocalAdmin() {
        if (type == USER_SYSADMIN) return true;
        if (sites_owners == null || sites_owners.isEmpty()) return false;
        return true;
    }

    public boolean IsNormalUser() {
        return type == USER_NORMAL;
    }

    public void RemoveSite(Site site) {
        if (sites_owners != null) sites_owners.remove(site.GetId());
        if (sites_myunit != null) sites_myunit.remove(site.GetId());
    }

    public void Add2OwnSite(Site site) {
        if (sites_owners == null) sites_owners = new Hashtable();
        if (sites_owners.containsKey(site.GetId())) sites_owners.remove(site.GetId());
        sites_owners.put(site.GetId(), site.GetName());
    }

    public void Add2UnitSite(Site site) {
        if (sites_myunit == null) sites_myunit = new Hashtable();
        if (sites_myunit.containsKey(site.GetId())) sites_myunit.remove(site.GetId());
        sites_myunit.put(site.GetId(), site.GetName());
    }

    public void SetName(String s) {
        this.name = s;
    }

    public void SetAccount(String s) {
        this.account = s.toUpperCase();
    }

    public void SetDept(Dept dept) {
        this.dept = dept;
        this.unit = dept.GetUnit();
    }

    public void SetType(int utype) {
        if (IsSysAdmin()) type = utype;
    }

    public void SetIndex(int i) {
        index = i;
    }

    public void SetTelephone(String tele) {
        this.telephone = tele;
    }

    public void SetFax(String fax) {
        this.fax = fax;
    }

    public void SetEmail(String email) {
        this.email = email;
    }

    public void SetMobile(String mobile) {
        this.mobile = mobile;
    }

    public Dept GetDept() {
        return dept;
    }

    public List GetUnits() throws NpsException {
        ArrayList units = new ArrayList();
        if (type != USER_SYSADMIN) {
            if (unit == null) return units;
            units.add(unit);
            return units;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            String sql = "select * from unit";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Unit aUnit = new Unit(rs.getString("id"), rs.getString("name"), rs.getString("code"));
                aUnit.SetAddress(rs.getString("address"));
                aUnit.SetAttachman(rs.getString("attachman"));
                aUnit.SetMobile(rs.getString("mobile"));
                aUnit.SetPhonenum(rs.getString("phonenum"));
                aUnit.SetZipcode(rs.getString("zipcode"));
                aUnit.SetEmail(rs.getString("email"));
                units.add(aUnit);
            }
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return units;
    }

    public Unit GetUnit() {
        return unit;
    }

    public Unit GetUnit(String unitid) throws NpsException {
        if (unitid == null) return null;
        if (unitid.equals(unit.GetId())) return unit;
        if (type != USER_SYSADMIN) return null;
        return Unit.GetUnit(unitid);
    }

    public String GetUnitId() {
        if (unit == null) return null;
        return unit.GetId();
    }

    public String GetUnitName() {
        if (unit == null) return null;
        return unit.GetName();
    }

    public String GetUnitCode() {
        if (unit == null) return null;
        return unit.GetCode();
    }

    public String GetDeptName() {
        if (dept == null) return null;
        return dept.GetName();
    }

    public String GetDeptId() {
        if (dept == null) return null;
        return dept.GetId();
    }

    public String GetDeptCode() {
        if (dept == null) return null;
        return dept.GetCode();
    }

    public String GetTelephone() {
        return telephone;
    }

    public String GetFax() {
        return fax;
    }

    public String GetEmail() {
        return email;
    }

    public String GetMobile() {
        return mobile;
    }

    public String GetUID() {
        return id;
    }

    public String GetName() {
        return name;
    }

    public String GetAccount() {
        return account;
    }

    public String GetId() {
        return id;
    }

    public String GetParentId() {
        return dept.GetId();
    }

    public void SetLocale(Locale local) {
        this.locale = local;
    }

    public Locale GetLocale() {
        return locale;
    }

    public String GetFace() {
        return face;
    }

    public void SetFace(String s) {
        face = s;
    }

    public int GetIndex() {
        return index;
    }

    public String GetDefaultSiteId() {
        return default_site;
    }

    public void SetDefaultSite(String id) {
        if (sites_owners.containsKey(id)) default_site = id;
    }

    public Site GetDefaultSite() {
        if (default_site == null) return null;
        return GetSite(default_site);
    }

    public Hashtable GetOwnSites() {
        return sites_owners;
    }

    public Hashtable GetUnitSites() throws NpsException {
        return sites_myunit;
    }

    public Site GetSite(String siteid) {
        if (sites != null && sites.containsKey(siteid)) return (Site) sites.get(siteid);
        if (sites_owners == null || !sites_owners.containsKey(siteid)) return null;
        if (sites_myunit == null || !sites_myunit.containsKey(siteid)) return null;
        Connection conn = null;
        NpsContext ctxt = null;
        try {
            conn = Database.GetDatabase("nps").GetConnection();
            ctxt = new NpsContext(conn, this);
            return GetSite(ctxt, siteid);
        } catch (Exception e) {
            nps.util.DefaultLog.error_noexception(e);
        } finally {
            if (ctxt != null) ctxt.Clear();
        }
        return null;
    }

    public Site GetSite(NpsContext ctxt, String siteid) {
        if (sites != null && sites.containsKey(siteid)) return (Site) sites.get(siteid);
        if (sites_owners == null || !sites_owners.containsKey(siteid)) return null;
        if (sites_myunit == null || !sites_myunit.containsKey(siteid)) return null;
        try {
            Site site = ctxt.GetSite(siteid);
            if (site != null) {
                if (sites == null) sites = new Hashtable();
                sites.put(siteid, site);
            }
            return site;
        } catch (Exception e) {
            nps.util.DefaultLog.error_noexception(e);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.init_test();
        User user = User.Login("system", "manager");
        Unit unit = user.GetUnit("1");
        unit.Delete();
    }
}
