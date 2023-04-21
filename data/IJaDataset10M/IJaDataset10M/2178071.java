package com.pioneer.app.proview;

import java.io.Serializable;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Iterator;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import java.io.Serializable;

public class RoleAction implements Action, Serializable {

    private java.lang.Integer id;

    private java.lang.Integer[] ids;

    private Role role;

    private java.lang.String name;

    private java.lang.String remark;

    private List objects = null;

    private String condition = null;

    private String fromTree = null;

    private String rlid = null;

    private String returnpage = null;

    private String errorInfo = null;

    /** 默认构造方法 */
    public RoleAction() {
    }

    /** full constructor */
    public RoleAction(java.lang.String name, java.lang.String remar) {
        this.name = name;
        this.remark = remark;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setIds(java.lang.Integer[] ids) {
        this.ids = ids;
    }

    public java.lang.Integer[] getIds() {
        return ids;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    public java.lang.String getRemark() {
        return remark;
    }

    public List getObjects() {
        return objects;
    }

    public void setObjects(List objects) {
        this.objects = objects;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRlid() {
        return rlid;
    }

    public void setRlid(String rlid) {
        this.rlid = rlid;
    }

    public String getReturnpage() {
        return returnpage;
    }

    public void setReturnpage(String returnpage) {
        this.returnpage = returnpage;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String execute() throws Exception {
        return "SUCESS";
    }

    public String doList() {
        try {
            RoleDAO dao = new RoleDAO();
            this.objects = dao.findByCondition(condition);
        } catch (Exception e) {
            this.errorInfo = "取列表数据出错";
        }
        return "list";
    }

    public String doAdd() {
        return "add";
    }

    public String doEdit() {
        try {
            RoleDAO dao = new RoleDAO();
            this.role = dao.findById(id);
            this.sedObj();
            ;
        } catch (Exception e) {
            this.errorInfo = "取列表数据出错";
        }
        return "edit";
    }

    public String doSave() {
        Transaction tx = null;
        try {
            this.buildObj();
            RoleDAO dao = new RoleDAO();
            tx = dao.getSession().beginTransaction();
            if (null == this.role.getId()) {
                try {
                    dao.add(role);
                } catch (Exception e) {
                    tx.rollback();
                    this.errorInfo = "添加数据出错";
                }
            } else {
                try {
                    dao.getSession().clear();
                    dao.update(role);
                } catch (Exception e) {
                    tx.rollback();
                    this.errorInfo = "保存数据出错";
                }
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            this.errorInfo = "系统出错";
        }
        if (null == this.returnpage) {
            this.returnpage = "list";
        }
        return returnpage;
    }

    /**************显示记录的详细信息**************/
    public String doDetail() {
        RoleDAO dao = new RoleDAO();
        this.role = dao.findById(id);
        this.sedObj();
        ;
        if (null == this.returnpage) {
            this.returnpage = "detail";
        }
        return "returnpage";
    }

    public String doDelete() {
        try {
            RoleDAO dao = new RoleDAO();
            dao.deleteObjs(ids);
        } catch (Exception e) {
            this.errorInfo = "删除记录,系统出错";
        }
        if (null == this.returnpage) {
            this.returnpage = "list";
        }
        return returnpage;
    }

    /*******************取到选择列表*************************/
    public String doCheckList() {
        try {
            RoleDAO dao = new RoleDAO();
            this.objects = dao.findByCondition(condition);
            if (null == rlid) {
                rlid = (String) ActionContext.getContext().getSession().get("rlid");
            }
            if (null != this.objects) {
                Role role = null;
                List checkList = getrelationList("userId", new Integer(rlid));
                Iterator it = this.objects.iterator();
                while (it.hasNext()) {
                    role = (Role) it.next();
                    if (isCheck(role.getId(), checkList)) role.setChecked("checked");
                }
            }
        } catch (Exception e) {
            this.errorInfo = "系统出错";
        }
        return "list";
    }

    /*****************判断是否被选中 ,开发人员要修改此处******************/
    private boolean isCheck(java.lang.Integer roleId, List checkList) {
        Iterator it = checkList.iterator();
        RoleUser rlObj = null;
        while (it.hasNext()) {
            rlObj = (RoleUser) it.next();
            if (roleId.intValue() == rlObj.getRoleId().intValue()) {
                return true;
            }
        }
        return false;
    }

    /************************根据相管理对象的id取到同本类型对象关联的关系list***************************/
    private List getrelationList(String key, Object value) {
        RoleUserDAO dao = new RoleUserDAO();
        List objs = dao.findByProperty(key, value);
        return objs;
    }

    /*************************保存关联操作结果********************************/
    public String doRoleCheckSave() {
        if (null != this.ids) {
            java.lang.Integer id = null;
            RoleUserDAO rlDao = new RoleUserDAO();
            rlDao.deleteByCondition("userId=" + rlid);
            RoleUser rlObj = null;
            Transaction tx = rlDao.getSession().beginTransaction();
            for (int i = 0; i < this.ids.length; i++) {
                rlObj = new RoleUser();
                rlObj.setRoleId(ids[i]);
                rlObj.setUserId(new Integer(rlid));
                rlDao.add(rlObj);
            }
            tx.commit();
            rlDao.getSession().close();
        }
        if (null == this.returnpage) {
            ActionContext.getContext().getSession().put("rlid", this.rlid);
            returnpage = "list";
        }
        return returnpage;
    }

    private void buildObj() {
        this.role = new Role();
        this.role.setId(this.id);
        this.role.setName(this.name);
        this.role.setRemark(this.remark);
    }

    private void sedObj() {
        if (null != this.role) {
            this.id = this.role.getId();
            this.name = this.role.getName();
            this.remark = this.role.getRemark();
        }
    }
}
