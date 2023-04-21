package com.action.admin;

import com.db.admin.AdminDAO;

public class AdminJobDelete {

    private AdminDAO adminDAO;

    private int jobno;

    public void setJobno(int jobno) {
        this.jobno = jobno;
    }

    public void setAdminDAO(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public String execute() {
        String result = "";
        try {
            adminDAO.jobDelete(jobno);
            System.out.println("액션!");
            result = "success";
        } catch (Exception e) {
            result = "error";
        }
        return result;
    }
}
