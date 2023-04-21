package com.companyname.common.sysframe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import com.companyname.common.sysframe.define.RoleStatus;

/**
 * <p>Title: 角色 </p>
 * <p>Description: 角色 Role </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public class Role extends Unit implements Serializable {

    private Role superRole;

    private Set subRoles;

    private Set posts;

    private String name;

    private String remark;

    private int status;

    /**
         * 构造函数
         */
    public Role() {
    }

    public Role(String code, String name, String pri) {
        this.code = code;
        this.name = name;
        this.pri = pri;
        this.status = RoleStatus.VALID;
        this.remark = "";
    }

    public Set getPosts() {
        return posts;
    }

    public void setPosts(Set posts) {
        this.posts = posts;
    }

    public Set getSubRoles() {
        return subRoles;
    }

    public void setSubRoles(Set subRoles) {
        this.subRoles = subRoles;
    }

    public Role getSuperRole() {
        return superRole;
    }

    public void setSuperRole(Role superRole) {
        this.superRole = superRole;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullNameWithLength(int length) {
        if (length > 1 && this.superRole != null) {
            length--;
            return this.superRole.getFullNameWithLength(length) + this.name;
        } else {
            return this.name;
        }
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return RoleStatus.NAMES[this.status];
    }

    public void addRole(Role role) {
        if (this.subRoles == null) {
            this.subRoles = new LinkedHashSet();
        }
        this.subRoles.add(role);
        role.setSuperRole(this);
    }

    public void addSubRoles(Set subRoles) {
        if (this.subRoles == null) {
            this.subRoles = new LinkedHashSet();
        }
        Object[] roles = subRoles.toArray();
        for (int i = 0; i < roles.length; i++) {
            Role role = (Role) roles[i];
            this.subRoles.add(role);
            role.setSuperRole(this);
        }
    }

    public Set getValidSubRoles() {
        Set vSubRoles = new LinkedHashSet();
        Object[] roles = this.subRoles.toArray();
        for (int i = 0; i < roles.length; i++) {
            Role role = (Role) roles[i];
            if (role.getStatus() == RoleStatus.VALID) {
                vSubRoles.add(role);
            }
        }
        return vSubRoles;
    }

    /** 获取所有子孙节点 */
    public List getAllSubRoles() {
        return this.getAllSubRoles(this);
    }

    /** 递归获取子孙节点model */
    private List getAllSubRoles(Role parentRole) {
        List tsubRoles = new Vector(parentRole.getSubRoles());
        List allSubRoles = new Vector();
        if (tsubRoles.size() != 0) {
            for (int i = 0; i < tsubRoles.size(); i++) {
                Role role = (Role) tsubRoles.get(i);
                if (role.getStatus() == RoleStatus.VALID) {
                    allSubRoles.add(role);
                    allSubRoles.addAll(this.getAllSubRoles(role));
                }
            }
        }
        return allSubRoles;
    }

    public List getUnits2Root() {
        return this.getParentRoles(new ArrayList());
    }

    public Role getSuperRoleByCode(String superRoleCode) {
        List superRoles = this.getUnits2Root();
        for (int i = 0; i < superRoles.size(); i++) {
            Role superRole = (Role) superRoles.get(i);
            if (superRole.getCode().equals(superRoleCode)) {
                return superRole;
            }
        }
        return null;
    }

    private List getParentRoles(List roles) {
        roles.add(this);
        if (this.superRole == null) {
            return roles;
        } else {
            return this.superRole.getParentRoles(roles);
        }
    }

    /** 采用框架提供的操作日志必须填写的项目 */
    public void setModeldetail() {
        this.setModelFieldDetail("id", this.getId());
        this.setModelFieldDetail("代码", this.code);
        if (this.superRole != null) {
            this.setModelFieldDetail("父角色", this.superRole.getName());
        }
        this.setModelFieldDetail("名称", this.name);
        this.setModelFieldDetail("优先级", this.pri);
        this.setModelFieldDetail("状态", this.getStatusName());
    }
}
