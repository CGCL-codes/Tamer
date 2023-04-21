package com.aide.simplification.popedom.popedom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import com.aide.simplification.global.Utils;
import com.aide.simplification.popedom.login.LoginUser;
import com.aide.simplification.popedom.login.PopSSO;
import com.aide.simplification.popedom.popedom.base.BasePop;

@RemoteProxy(name = "Dept")
public class Dept extends BasePop {

    /**
	 * 按照部门分页查询用户
	 * 
	 * @param deptid
	 * @param page
	 * @param size
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findAllUserPageByDept(String deptid, int page, int size) {
        User user = new User();
        return user.findAllUserPageByDept(deptid, page, size);
    }

    /**
	 * 按照部门统计用户数量
	 * 
	 * @param deptid
	 * @return
	 */
    @RemoteMethod
    public int findAllUserPageByDeptGetCount(String deptid) {
        String sql = "SELECT count(*) FROM pop_user_dept WHERE deptid = ?";
        return dbop.queryForInt(sql, new Object[] { deptid });
    }

    /**
	 * 按照用户编号和部门编号删除 用户-部门关系
	 * 
	 * @param useruuid
	 * @param deptid
	 * @return
	 */
    @RemoteMethod
    public int removeByUserUUIDDeptid(String useruuid, String deptid) {
        UserDept ud = new UserDept();
        int i = ud.removeByUserUUIDDeptid(useruuid, deptid);
        List<Map<String, Object>> list = ud.findByUserUUID(useruuid);
        if (i == 1 && (list == null || list.size() == 0)) {
            Map<String, Object> map = this.findByPk(deptid);
            if (map != null && map.get("mdepid") != null && !map.get("mdeptid").toString().equals("")) {
                return ud.newUserDept(deptid, map.get("mdeptid").toString());
            } else {
                return ud.newUserDept(deptid, Utils.getConfig("pop.rootdept"));
            }
        }
        return 0;
    }

    /**
	 * 分页查询所有部门
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findAllDeptPage(int page, int size) {
        String sql = dbop.initPageSql(page, size, "deptid", "select * from pop_dept");
        List<Map<String, Object>> list = dbop.queryForList(sql);
        return list;
    }

    /**
	 * 检查部门名是否已经存在 在用
	 * 
	 * @param pname
	 * @return
	 */
    @RemoteMethod
    public int checkPName(String dept, String deptid, String mdeptid) {
        if (dept.indexOf("'") != -1 || dept.length() <= 0) {
            return 0;
        }
        if (dbop.queryForInt("select count(deptid) from pop_dept where  dept=? and mdeptid=? and deptid!=?", new Object[] { dept, mdeptid, deptid }) > 0) {
            return 0;
        }
        return 1;
    }

    /**
	 * 分页查询本站点部门
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findByDeptPage(String deptid, int page, int size, HttpServletRequest request, HttpServletResponse response) {
        String sql = "";
        if (deptid.equals("")) {
            PopSSO sso = new PopSSO(request, response);
            LoginUser lu = sso.ssoSync();
            String useruuid = "";
            if (lu != null) {
                useruuid = lu.getUseruuid();
                if (useruuid.equals(Utils.getConfig("pop.admin"))) {
                    sql = dbop.initPageSql(page, size, "deptid", "select * from pop_dept where (mdeptid='" + deptid + "' or mdeptid is null)");
                } else {
                    UserDept ud = new UserDept();
                    sql = dbop.initPageSql(page, size, "deptid", "select * from pop_user_dept,pop_dept where pop_user_dept.deptid=pop_dept.deptid and pop_user_dept.useruuid='" + useruuid + "'");
                    return ud.findByUserUUIDAllInfo(useruuid);
                }
            }
        } else {
            sql = dbop.initPageSql(page, size, "deptid", "select * from pop_dept where mdeptid='" + deptid + "'");
        }
        List<Map<String, Object>> list = dbop.queryForList(sql);
        for (Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext(); ) {
            Map<String, Object> hm = iterator.next();
            String mdept = "";
            if (hm.get("mdeptid") != null) {
                String mdeptid = hm.get("mdeptid").toString();
                if (mdeptid.length() > 0) {
                    mdept = this.findByPk(mdeptid).get("dept").toString();
                }
            }
            hm.put("mdept", mdept);
        }
        return list;
    }

    /**
	 * 按照部门编号删除部门
	 * 
	 * @param deptid
	 * @return
	 */
    @RemoteMethod
    public String removeDeptById(String deptid) {
        if (this.findDeptByMdeptid(deptid).size() == 0) {
            UserDept ud = new UserDept();
            if (ud.findByDeptid(deptid).size() == 0) {
                DeptRole dr = new DeptRole();
                if (dr.findByDeptid(deptid).size() == 0) {
                    int flag = dbop.delete("pop_dept", "deptid", deptid);
                    if (flag == 1) {
                        return "删除成功";
                    }
                } else {
                    return "请先移除角色！";
                }
            } else {
                return "请先移除用户！";
            }
        } else {
            return "请先移除下级部门！";
        }
        return "删除失败";
    }

    /**
	 * 按照上级部门编号查询子部门(选择使用，仅查有效)
	 * 
	 * @param deptid
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findDeptByMdeptid(String deptid) {
        String sql = "select * from pop_dept where sign=1 and mdeptid =?";
        if (deptid.equals("")) {
            sql = "select * from pop_dept where sign=1 and (mdeptid =? or mdeptid is null)";
        }
        List<Map<String, Object>> list = dbop.queryForList(sql, new Object[] { deptid });
        return list;
    }

    /**
	 * 查询部门下的所有子部门
	 * 
	 * @param deptid
	 * @return
	 */
    public List<Map<String, Object>> findDeptByMdeptidDown(String deptid) {
        List<Map<String, Object>> deptlist = this.findDeptByMdeptid(deptid);
        List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
        if (deptlist != null && deptlist.size() > 0) {
            for (Iterator<Map<String, Object>> iterator = deptlist.iterator(); iterator.hasNext(); ) {
                Map<String, Object> hm = iterator.next();
                allList.add(hm);
                String deptidtemp = hm.get("deptid").toString();
                allList.addAll(this.findDeptByMdeptidDown(deptidtemp));
            }
        }
        return allList;
    }

    /**
	 * 查询当前用户所属部门下的所有部门
	 * 
	 * @param deptid
	 * @param request
	 * @param response
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findDeptByMdeptidDownUser(String deptid, HttpServletRequest request, HttpServletResponse response) {
        PopSSO sso = new PopSSO(request, response);
        LoginUser lu = sso.ssoSync();
        String useruuid = "";
        List<Map<String, Object>> allDept = new ArrayList<Map<String, Object>>();
        if (lu != null) {
            useruuid = lu.getUseruuid();
            if (deptid.equals("")) {
                if (useruuid.equals(Utils.getConfig("pop.admin"))) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("deptid", "");
                    map.put("dept", "根部门");
                    allDept.add(map);
                    allDept.addAll(this.findDeptByMdeptid(""));
                } else {
                    UserDept ud = new UserDept();
                    List<Map<String, Object>> userdeptList = ud.findByUserUUIDAllInfo(useruuid);
                    if (userdeptList != null) {
                        for (Iterator<Map<String, Object>> iterator = userdeptList.iterator(); iterator.hasNext(); ) {
                            Map<String, Object> udhm = iterator.next();
                            allDept.add(udhm);
                            String userdeptid = udhm.get("deptid").toString();
                            allDept.addAll(this.findDeptByMdeptid(userdeptid));
                        }
                    }
                }
            } else {
                Map<String, Object> map = this.findByPk(deptid);
                if (map != null) {
                    allDept.add(map);
                }
                allDept.addAll(this.findDeptByMdeptid(deptid));
            }
        }
        return allDept;
    }

    /**
	 * 继承上级部门角色
	 * 
	 * @param mdeptid
	 * @param deptid
	 */
    public void extendMDeptRole(String mdeptid, String deptid) {
        DeptRole dr = new DeptRole();
        List<Map<String, Object>> mlistdr = dr.findByDeptid(mdeptid);
        dr.removeByDeptid(deptid);
        for (Iterator<Map<String, Object>> iterator = mlistdr.iterator(); iterator.hasNext(); ) {
            Map<String, Object> hm = iterator.next();
            dr.newDeptRole(hm.get("roleid").toString(), deptid);
        }
        dr.reComputePop(deptid);
    }

    /**
	 * 查询根部门
	 * 
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findByMasterDept() {
        String sql = "select * from pop_dept where sign=1 and (mdeptid ='' or mdeptid is null )";
        List<Map<String, Object>> list = dbop.queryForList(sql);
        return list;
    }

    /**
	 * 查询所有可用状态部门
	 * 
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findAllDeptIsUse() {
        String sql = "select * from pop_dept where sign=1 order by deptid,dept";
        List<Map<String, Object>> list = dbop.queryForList(sql);
        return list;
    }

    /**
	 * 查询不是此部门编号的所有可用部门
	 * 
	 * @param deptid
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findAllDeptIsUseNoDeptid(String deptid) {
        String sql = "select * from pop_dept where sign=1 and deptid != ? order by deptid,dept";
        List<Map<String, Object>> list = dbop.queryForList(sql, new Object[] { deptid });
        return list;
    }

    /**
	 * 统计所有部门总数
	 * 
	 * @return
	 */
    @RemoteMethod
    public int getCount() {
        return dbop.queryForInt("select count(deptid) from pop_dept");
    }

    /**
	 * 统计某部门的子部门总数
	 * 
	 * @return
	 */
    @RemoteMethod
    public int getCountByMDept(String deptid, HttpServletRequest request, HttpServletResponse response) {
        String sql = "";
        if (deptid.equals("")) {
            PopSSO sso = new PopSSO(request, response);
            LoginUser lu = sso.ssoSync();
            String useruuid = "";
            if (lu != null) {
                useruuid = lu.getUseruuid();
                if (useruuid.equals(Utils.getConfig("pop.admin"))) {
                    sql = "select count(deptid) from pop_dept where (mdeptid='' or mdeptid is null)";
                    return dbop.queryForInt(sql);
                } else {
                    sql = "select count(deptid) from pop_user_dept where useruuid=?";
                    return dbop.queryForInt(sql, new Object[] { useruuid });
                }
            } else {
                return 0;
            }
        } else {
            sql = "select count(deptid) from pop_dept where mdeptid=?";
            return dbop.queryForInt(sql, new Object[] { deptid });
        }
    }

    /**
	 * 新建部门 在用
	 * 
	 * @param dept
	 * @param depttext
	 * @param sign
	 * @param pop
	 * @param mdeptid
	 * @return
	 */
    @RemoteMethod
    public int newDept(String dept, String depttext, String sign, String mdeptid, boolean extend) {
        Map<String, Object> hm = new HashMap<String, Object>();
        String deptid = Utils.getUUID();
        hm.put("deptid", deptid);
        hm.put("dept", dept);
        hm.put("depttext", depttext);
        hm.put("sign", sign);
        hm.put("mdeptid", mdeptid);
        hm.put("pop", "1");
        hm.put("version", "0");
        int flag = dbop.save("pop_dept", hm);
        if (flag == 1) {
            if (extend) {
                this.extendMDeptRole(mdeptid, deptid);
            }
        }
        return flag;
    }

    /**
	 * 更新部门
	 * 
	 * @param deptid
	 * @param dept
	 * @param depttext
	 * @param sign
	 * @param pop
	 * @param mdeptid
	 * @return
	 */
    @RemoteMethod
    public int updateDept(String deptid, String dept, String depttext, String sign, String mdeptid, boolean extend) {
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("dept", dept);
        hm.put("depttext", depttext);
        hm.put("sign", sign);
        hm.put("mdeptid", mdeptid);
        hm.put("version", "0");
        int flag = dbop.update("pop_dept", hm, "deptid", deptid);
        if (flag == 1) {
            if (extend) {
                this.extendMDeptRole(mdeptid, deptid);
            }
        }
        return flag;
    }

    /**
	 * 按照部门编号（主键）查询部门信息
	 * 
	 * @param pkvalue
	 * @return
	 */
    public Map<String, Object> findByPk(String pkvalue) {
        return dbop.findByPK("pop_dept", "deptid", pkvalue);
    }

    @RemoteMethod
    public String getdeptup(String deptid) {
        Map<String, Object> map = dbop.findByPK("pop_dept", "deptid", deptid);
        if (map != null) {
            return map.get("mdeptid").toString();
        }
        return "";
    }

    /**
	 * 察看部门信息
	 * 
	 * @param pkvalue
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> chakan(String pkvalue) {
        Map<String, Object> hm = findByPk(pkvalue);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (hm != null) {
            Map<String, Object> hm0 = new HashMap<String, Object>();
            hm0.put("title", "部门编号:");
            hm0.put("tname", "deptid");
            hm0.put("tvalue", hm.get("deptid").toString());
            list.add(hm0);
            Map<String, Object> hm1 = new HashMap<String, Object>();
            hm1.put("title", "部门名称:");
            hm1.put("tname", "dept");
            hm1.put("tvalue", hm.get("dept").toString());
            list.add(hm1);
            Map<String, Object> hm2 = new HashMap<String, Object>();
            hm2.put("title", "部门描述:");
            hm2.put("tname", "depttext");
            hm2.put("tvalue", hm.get("depttext").toString());
            list.add(hm2);
            Map<String, Object> hm4 = new HashMap<String, Object>();
            hm4.put("title", "上级部门:");
            hm4.put("tname", "mdeptid");
            hm4.put("tvalue", "");
            hm4.put("tval", "");
            if (hm.get("mdeptid") != null) {
                hm4.put("tval", hm.get("mdeptid").toString());
                Map<String, Object> hmdept = this.findByPk(hm.get("mdeptid").toString());
                if (hmdept != null && hmdept.get("dept") != null) {
                    hm4.put("tvalue", hmdept.get("dept").toString());
                }
            }
            list.add(hm4);
            Map<String, Object> hm3 = new HashMap<String, Object>();
            hm3.put("title", "有效标记:");
            hm3.put("tname", "sign");
            hm3.put("tval", hm.get("sign").toString());
            if (hm.get("sign").toString().equals("1")) {
                hm3.put("tvalue", "可用");
            } else {
                hm3.put("tvalue", "不可用");
            }
            list.add(hm3);
        }
        return list;
    }

    /**
	 * 添加/修改部门角色
	 * 
	 * @param deptid
	 * @param roleids
	 * @return
	 */
    @RemoteMethod
    public int addRoles(String deptid, String[] roleids, String[] nroleids) {
        DeptRole dr = new DeptRole();
        String sql = "delete from pop_user_role";
        sql += " where pop_user_role.roleid=? and pop_user_role.useruuid in ";
        sql += "(select pud.useruuid from pop_user_dept as pud where pud.deptid=?) and pop_user_role.roleid not in(";
        sql += "select pdr.roleid from pop_dept_role as pdr where pdr.deptid in(select pud1.deptid from pop_user_dept as pud1 where pud1.useruuid = pop_user_role.useruuid))";
        Object[][] params = new Object[nroleids.length][2];
        for (int i = 0; i < nroleids.length; i++) {
            String roleid = nroleids[i];
            params[i][0] = roleid;
            params[i][1] = deptid;
            UserRole ur = new UserRole();
            ur.updatePopByRole(roleid);
            dr.removeByDeptidRoleid(deptid, roleid);
        }
        dbop.executeUpdateJT(sql, params);
        for (int i = 0; i < roleids.length; i++) {
            String roleid = roleids[i];
            dr.removeByDeptidRoleid(deptid, roleid);
        }
        int rolesize = roleids.length;
        int flag = 0;
        for (int i = 0; i < rolesize; i++) {
            String roleid = roleids[i];
            flag += dr.newDeptRole(roleid, deptid);
        }
        if (flag == rolesize) {
            flag = 1;
        }
        String sqldeptLevel = "update pop_dept set pop_dept.deptLevel=(";
        sqldeptLevel += "select max(pr2.roleLevel) from pop_dept_role as pdr2,pop_role as pr2 where pdr2.roleid=pr2.roleid and pdr2.deptid=pop_dept.deptid)";
        sqldeptLevel += " where pop_dept.deptid =?";
        dbop.executeUpdate(sql, new Object[] { deptid });
        dr.reComputePop(deptid);
        return flag;
    }

    /**
	 * 查询此部门的所有角色
	 * 
	 * @param deptid
	 * @param roleids
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findRoleidByDeptid(String deptid) {
        DeptRole dr = new DeptRole();
        return dr.findByDeptid(deptid);
    }

    /**
	 * 生成根部门树
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
    @RemoteMethod
    public Map<String, Object> findDeptTree(HttpServletResponse response, HttpServletRequest request) {
        PopSSO sso = new PopSSO(request, response);
        LoginUser lu = sso.ssoSync();
        String uuid = "";
        Map<String, Object> rootmap = new HashMap<String, Object>();
        rootmap.put("id", "0");
        UserDept ud = new UserDept();
        if (lu != null) {
            uuid = lu.getUseruuid();
            if (uuid.equals(Utils.getConfig("pop.admin"))) {
                rootmap.put("item", this.findChildDept(""));
                return rootmap;
            } else {
                List<Map<String, Object>> userdeptList = ud.findByUserUUIDAllInfo(uuid);
                if (userdeptList != null) {
                    List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>();
                    for (Iterator<Map<String, Object>> iterator = userdeptList.iterator(); iterator.hasNext(); ) {
                        Map<String, Object> map = iterator.next();
                        String deptid = map.get("deptid").toString();
                        Map<String, Object> deptmap = new HashMap<String, Object>();
                        deptmap.put("id", deptid);
                        deptmap.put("text", map.get("dept").toString());
                        deptmap.put("open", false);
                        deptmap.put("im0", "folderOpen.gif");
                        deptmap.put("im1", "folderOpen.gif");
                        deptmap.put("im2", "folderClosed.gif");
                        deptmap.put("tooltip", map.get("depttext").toString());
                        deptmap.put("item", this.findChildDept(deptid));
                        rootList.add(deptmap);
                    }
                    rootmap.put("item", rootList);
                    return rootmap;
                }
            }
        }
        return null;
    }

    /**
	 * 查询子部门
	 * 
	 * @param mdeptid
	 * @param response
	 * @param request
	 * @return
	 */
    @RemoteMethod
    public List<Map<String, Object>> findChildDept(String mdeptid) {
        List<Map<String, Object>> deptList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> deptlist = this.findDeptByMdeptid(mdeptid);
        if (deptlist != null) {
            for (Iterator<Map<String, Object>> iterator = deptlist.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                String deptid = map.get("deptid").toString();
                Map<String, Object> deptmap = new HashMap<String, Object>();
                deptmap.put("id", deptid);
                deptmap.put("text", map.get("dept").toString());
                deptmap.put("im0", "folderOpen.gif");
                deptmap.put("im1", "folderOpen.gif");
                deptmap.put("im2", "folderClosed.gif");
                deptmap.put("open", false);
                deptmap.put("tooltip", map.get("depttext").toString());
                deptmap.put("item", this.findChildDept(deptid));
                deptList.add(deptmap);
            }
        }
        return deptList;
    }

    @RemoteMethod
    public Map<String, Object> findPopMenu(HttpServletResponse response, HttpServletRequest request) {
        PopSSO sso = new PopSSO(request, response);
        LoginUser lu = sso.ssoSync();
        Map<String, Object> rootmap = new HashMap<String, Object>();
        rootmap.put("id", "0");
        if (lu != null) {
            List<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>();
            Map<String, Object> deptmap = new HashMap<String, Object>();
            List<Map<String, Object>> managerList = new ArrayList<Map<String, Object>>();
            if (lu.getUseruuid().equals(Utils.getConfig("pop.admin"))) {
                managerList.add(this.getChild("角色管理", "2", "roles/list.htm"));
            }
            managerList.add(this.getChild("部门管理", "3", "depts/list.htm"));
            managerList.add(this.getChild("用户管理", "4", "users/list.htm"));
            deptmap.put("item", managerList);
            rootList.add(deptmap);
            rootmap.put("item", rootList);
            return rootmap;
        }
        return null;
    }

    public Map<String, Object> getChild(String text, String id, String url) {
        Map<String, Object> manageMap = new HashMap<String, Object>();
        Map<String, Object> userdataMap = new HashMap<String, Object>();
        List<Map<String, Object>> userdataList = new ArrayList<Map<String, Object>>();
        manageMap.put("id", id);
        manageMap.put("text", text);
        userdataMap.put("name", "url");
        userdataMap.put("content", url);
        userdataList.add(userdataMap);
        manageMap.put("userdata", userdataList);
        return manageMap;
    }

    public List<Map<String, Object>> findByLevel(int level, String opsign) {
        String sql = "select * from pop_dept where deptLevel " + opsign + "?";
        return dbop.queryForList(sql, new Object[] { level });
    }

    public List<Map<String, Object>> findByCurrUser(String useruuid) {
        String sql = "select pop_dept.* from pop_dept,pop_user_dept where pop_dept.deptid=pop_user_dept.deptid and pop_user_dept.useruuid =?";
        return dbop.queryForList(sql, new Object[] { useruuid });
    }

    public List<Map<String, Object>> findMasterDept() {
        String sql = "";
        try {
            sql = "SELECT pop_dept.deptid,pop_dept.dept,pop_dept.depttext,pop_dept.sign,pop_dept.mdeptid";
            sql += " FROM pop_dept ";
            sql += " WHERE (pop_dept.sign = 1) AND (pop_dept.mdeptid = '' or pop_dept.mdeptid is null)";
            List<Map<String, Object>> list = dbop.queryForList(sql);
            return list;
        } catch (Exception e) {
        }
        return new ArrayList<Map<String, Object>>();
    }

    public List<Map<String, Object>> findUserByDeptid(String deptid) {
        String sql = "SELECT pop_user_dept.deptid, pop_user_dept.useruuid, pop_user.name";
        sql += " FROM pop_user INNER JOIN";
        sql += " pop_user_dept ON pop_user.useruuid = pop_user_dept.useruuid INNER JOIN";
        sql += " pop_dept ON pop_user_dept.deptid = pop_dept.deptid";
        sql += " WHERE (pop_dept.deptid = ?)";
        try {
            List<Map<String, Object>> list = dbop.queryForList(sql, new Object[] { deptid });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Map<String, Object>>();
        }
    }
}
