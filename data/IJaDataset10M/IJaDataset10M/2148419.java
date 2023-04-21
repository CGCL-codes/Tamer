package com.action.admin;

import com.db.admin.*;
import com.db.organization.*;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class AdminDeptOkAction implements ModelDriven<Object>, Preparable {

    AdminDeptDAO adminDept;

    private DeptVO vo;

    public void setVo(DeptVO vo) {
        this.vo = vo;
    }

    public DeptVO getVo() {
        return vo;
    }

    @Override
    public void prepare() throws Exception {
        vo = new DeptVO();
    }

    @Override
    public Object getModel() {
        return vo;
    }

    public String execute() throws Exception {
        adminDept.DeptInsert(vo);
        System.out.println(vo.toString());
        return "success";
    }

    public void setAdminDept(AdminDeptDAO adminDept) {
        this.adminDept = adminDept;
    }
}
