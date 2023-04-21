package com.action.admin;

import java.util.*;
import com.db.admin.*;
import com.db.organization.*;
import com.db.user.*;

public class AdminSawonUpdate {

    private AdminDAO adminDAO;

    public void setAdminDAO(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    private List<UserVO> list = new ArrayList<UserVO>();

    private List<DeptVO> dlist = new ArrayList<DeptVO>();

    private List<JobVO> jlist = new ArrayList<JobVO>();

    private UserVO vo = new UserVO();

    public UserVO getVo() {
        return vo;
    }

    private int sabun;

    public void setSabun(int sabun) {
        this.sabun = sabun;
    }

    public List<UserVO> getList() {
        return list;
    }

    public List<JobVO> getJlist() {
        return jlist;
    }

    public List<DeptVO> getDlist() {
        return dlist;
    }

    public String execute() throws Exception {
        jlist = adminDAO.JobList();
        dlist = adminDAO.DeptList();
        vo = adminDAO.Update(sabun);
        return "success";
    }
}
