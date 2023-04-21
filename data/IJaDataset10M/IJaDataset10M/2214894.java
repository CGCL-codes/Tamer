package com.action.admin;

import java.util.ArrayList;
import java.util.List;
import com.db.admin.AdminDAO;
import com.db.user.UserVO;

public class AdminSawonSerch {

    private List<UserVO> list = new ArrayList<UserVO>();

    private AdminDAO adminDAO;

    private int curpage;

    private int totalpage;

    private String page;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getCurpage() {
        return curpage;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setAdminDAO(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public List<UserVO> getList() {
        return list;
    }

    public String execute() throws Exception {
        if (page == null) page = "1";
        curpage = Integer.parseInt(page);
        List<UserVO> temp = adminDAO.sawonSerch(name);
        int j = 0;
        int pagecnt = (curpage * 10) - 10;
        for (int i = 0; i < temp.size(); i++) {
            if (j < 10 && i >= pagecnt) {
                UserVO vo = temp.get(i);
                list.add(vo);
                j++;
            }
        }
        int count = adminDAO.UserCount();
        int total = count / 10;
        if (count % 10 > 0) total++;
        totalpage = total;
        System.out.println(curpage + "|" + totalpage);
        return "success";
    }
}
