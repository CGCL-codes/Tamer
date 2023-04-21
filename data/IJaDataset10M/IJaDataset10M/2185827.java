package com.action.workreport;

import java.util.*;
import com.db.user.*;
import com.db.workreport.*;
import com.opensymphony.xwork2.ActionContext;

public class ListAction_send {

    private int type;

    private List<WorkReportVO> list = new ArrayList<WorkReportVO>();

    private WorkReportVO vo = new WorkReportVO();

    private int sabun;

    private WorkReportDAO reportDAO;

    public void setReportDAO(WorkReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public void setSabun(int sabun) {
        this.sabun = sabun;
    }

    public void setVo(WorkReportVO vo) {
        this.vo = vo;
    }

    public int getSabun() {
        return sabun;
    }

    public List<WorkReportVO> getList() {
        return list;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String execute() throws Exception {
        list = reportDAO.reportAllData_send(sabun);
        return "success";
    }
}
