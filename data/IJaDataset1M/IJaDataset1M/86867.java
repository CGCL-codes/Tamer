package com.action.workcall;

import java.util.*;
import com.db.workcall.*;

public class ListAction {

    private int kind;

    private int sabun;

    private List<WorkCallVO> list = new ArrayList<WorkCallVO>();

    private List<WorkCallVO> temp;

    private String page;

    private int curpage;

    private int totalpage;

    private WorkCallDAO callDAO;

    public void setCallDAO(WorkCallDAO callDAO) {
        this.callDAO = callDAO;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public void setCurpage(int curpage) {
        this.curpage = curpage;
    }

    public int getCurpage() {
        return curpage;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setSabun(int sabun) {
        this.sabun = sabun;
    }

    public List<WorkCallVO> getList() {
        return list;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getKind() {
        return kind;
    }

    public String execute() throws Exception {
        curpage = 0;
        if (page == null) page = "1";
        curpage = Integer.parseInt(page);
        int count;
        if (kind == 2) {
            temp = callDAO.callSendView(sabun);
            int j = 0;
            int pagecnt = (curpage * 10) - 10;
            for (int i = 0; i < temp.size(); i++) {
                if (j < 10 && i >= pagecnt) {
                    WorkCallVO vo = temp.get(i);
                    list.add(vo);
                    j++;
                }
            }
            count = callDAO.sendAllCount(sabun);
            totalpage = count / 10;
            if (count % 10 > 0) totalpage++;
            System.out.println("totalpage:" + totalpage);
            System.out.println("curpage:" + curpage);
            System.out.println("count:" + count);
        } else if (kind == 3) {
            temp = callDAO.callGetView(sabun);
            System.out.println(temp);
            int j = 0;
            int pagecnt = (curpage * 10) - 10;
            for (int i = 0; i < temp.size(); i++) {
                if (j < 10 && i >= pagecnt) {
                    WorkCallVO vo = temp.get(i);
                    list.add(vo);
                    j++;
                }
            }
            count = callDAO.getAllCount(sabun);
            totalpage = count / 10;
            if (count % 10 > 0) totalpage++;
            System.out.println("totalpage:" + totalpage);
            System.out.println("curpage:" + curpage);
            System.out.println("count:" + count);
        } else {
            temp = callDAO.callGetTrash();
            int j = 0;
            int pagecnt = (curpage * 10) - 10;
            for (int i = 0; i < temp.size(); i++) {
                if (j < 10 && i >= pagecnt) {
                    WorkCallVO vo = temp.get(i);
                    list.add(vo);
                    j++;
                }
            }
            count = callDAO.trashAllCount();
            totalpage = count / 10;
            if (count % 10 > 0) totalpage++;
            System.out.println("totalpage:" + totalpage);
            System.out.println("curpage:" + curpage);
            System.out.println("count:" + count);
        }
        return "success";
    }
}
