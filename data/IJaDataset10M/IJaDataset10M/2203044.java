package com.microfly.core;

import com.microfly.util.tree.Tree;
import com.microfly.util.tree.Node;
import com.microfly.exception.NpsException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import com.microfly.core.db.Database;

/**
 * �û����ͽṹ����Unit/Dept/User����
 * ��DeptTree�Ĳ����Dept�ڵ㶼������UserProfile��Ϣ
 *     Dept ID����dept��ͷ
 *     User ID����user��ͷ
 *   ʾ��
 *      "-1"   -->unit name
 *         "dept"+id   -->dept name
 *             "user"+id   -->user name
 * a new publishing system
 * Copyright (c) 2007

 * @author jialin
 * @version 1.0
 */
public class UserTree {

    private Unit unit = null;

    private String treename = null;

    private Tree tree = null;

    public UserTree(Unit unit) {
        this.unit = unit;
        this.treename = unit.GetName();
        tree = Tree.GetTree("-1");
    }

    public UserTree(Unit unit, String treename) {
        this.unit = unit;
        if (treename == null) this.treename = unit.GetName(); else this.treename = treename;
        tree = Tree.GetTree("-1");
    }

    public void AddDept(Dept t) {
        if (tree == null) tree = Tree.GetTree("-1");
        String parent_node_id = (t.GetParentId() == null || "-1".equalsIgnoreCase(t.GetParentId())) ? "-1" : t.GetParentId();
        tree.AddNode(t.GetId(), tree.GetNode(parent_node_id), t);
    }

    public Dept GetDept(String id) {
        if (tree == null) return null;
        return (Dept) tree.GetNode(id).GetValue();
    }

    public Unit GetUnit() {
        return unit;
    }

    public static UserTree LoadTree(Unit unit, String treename) throws NpsException {
        if (treename == null) treename = unit.GetName();
        UserTree aUserTree = new UserTree(unit, treename);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt_user = null;
        ResultSet rs_user = null;
        try {
            conn = Database.GetDatabase("fly").GetConnection();
            String sql = "select * from dept a where a.unit=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, unit.GetId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String parent_dept_id = rs.getString("parentid");
                parent_dept_id = parent_dept_id == null ? "-1" : parent_dept_id;
                Dept aDept = new Dept(unit, parent_dept_id, rs.getString("id"), rs.getString("name"), rs.getString("code"), rs.getInt("cx"));
                sql = "select a.id,a.name,a.account from users a where a.dept=? order by cx";
                pstmt_user = conn.prepareStatement(sql);
                pstmt_user.setString(1, aDept.GetId());
                rs_user = pstmt_user.executeQuery();
                while (rs_user.next()) {
                    aDept.AddUser(rs_user.getString("id"), rs_user.getString("name"), rs_user.getString("account"));
                }
                try {
                    rs_user.close();
                } catch (Exception e) {
                }
                try {
                    pstmt_user.close();
                } catch (Exception e) {
                }
                aUserTree.AddDept(aDept);
            }
        } catch (Exception e) {
            aUserTree = null;
            com.microfly.util.DefaultLog.error(e);
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
        return aUserTree;
    }

    public String toDHXTree(String dhxtree, String rootId) {
        String unit_nodeid = "unit" + unit.GetId();
        String jstree = dhxtree + ".insertNewItem('" + rootId + "'," + "'" + unit_nodeid + "'," + "'" + unit.GetName() + "');";
        jstree += dhxtree + ".setUserData('" + unit_nodeid + "'," + "'unitid'," + "'" + unit.GetId() + "');";
        jstree += dhxtree + ".setUserData('" + unit_nodeid + "'," + "'unitname'," + "'" + unit.GetName() + "');";
        jstree += dhxtree + ".setUserData('" + unit_nodeid + "'," + "'deptid'," + "'');";
        jstree += dhxtree + ".setUserData('" + unit_nodeid + "'," + "'deptname'," + "'');";
        jstree += PaintDHXTree(dhxtree, unit_nodeid, tree.GetChilds());
        jstree += dhxtree + ".closeAllItems('" + unit_nodeid + "');";
        jstree += dhxtree + ".openItem('" + unit_nodeid + "');";
        return jstree;
    }

    private String PaintDHXTree(String dhxtree, String parentid, Iterator childs) {
        String jstree = "";
        while (childs.hasNext()) {
            Node node = (Node) childs.next();
            String id = node.GetId();
            if (!"-1".equalsIgnoreCase(id)) {
                Dept dept = (Dept) node.GetValue();
                String dept_nodeid = "dept" + id;
                jstree += dhxtree + ".insertNewItem('" + parentid + "'," + "'" + dept_nodeid + "'," + "'" + dept.GetName() + "');";
                jstree += dhxtree + ".setUserData('" + dept_nodeid + "'," + "'unitid'," + "'" + unit.GetId() + "');";
                jstree += dhxtree + ".setUserData('" + dept_nodeid + "'," + "'unitname'," + "'" + unit.GetName() + "');";
                jstree += dhxtree + ".setUserData('" + dept_nodeid + "'," + "'deptid'," + "'" + id + "');";
                jstree += dhxtree + ".setUserData('" + dept_nodeid + "'," + "'deptname'," + "'" + dept.GetName() + "');";
                List users = dept.GetUsers();
                if (users != null && !users.isEmpty()) {
                    for (Object user_obj : users) {
                        Dept.UserProfile user = (Dept.UserProfile) user_obj;
                        String user_nodeid = "user" + user.GetId();
                        jstree += dhxtree + ".insertNewItem('" + dept_nodeid + "'," + "'" + user_nodeid + "'," + "'" + user.GetName() + "');";
                        jstree += dhxtree + ".setUserData('" + user_nodeid + "'," + "'unitid'," + "'" + unit.GetId() + "');";
                        jstree += dhxtree + ".setUserData('" + user_nodeid + "'," + "'unitname'," + "'" + unit.GetName() + "');";
                        jstree += dhxtree + ".setUserData('" + user_nodeid + "'," + "'deptid'," + "'" + id + "');";
                        jstree += dhxtree + ".setUserData('" + user_nodeid + "'," + "'deptname'," + "'" + dept.GetName() + "');";
                        jstree += dhxtree + ".setUserData('" + user_nodeid + "'," + "'userid'," + "'" + user.GetId() + "');";
                        jstree += dhxtree + ".setUserData('" + user_nodeid + "'," + "'username'," + "'" + user.GetName() + "');";
                    }
                }
                if (node.HasChilds()) jstree += PaintDHXTree(dhxtree, dept_nodeid, node.GetChilds());
            } else {
                if (node.HasChilds()) jstree += PaintDHXTree(dhxtree, parentid, node.GetChilds());
            }
        }
        return jstree;
    }
}
