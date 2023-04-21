package com.action.commute;

import com.db.commute.CommuteDAO;

public class CommuteDWR {

    private CommuteDAO cdao;

    public void setCdao(CommuteDAO cdao) {
        this.cdao = cdao;
    }

    public boolean checkExist(String type, int cno) {
        boolean check = false;
        if (type.equals("cwork")) {
            check = cdao.CwExist(cno);
        } else {
            check = cdao.LwExist(cno);
        }
        return check;
    }

    public void writeRecord(String type, int cno) {
        if (type.equals("cwork")) {
            cdao.UpdateCwork(cno);
        } else {
            cdao.UpdateLwork(cno);
        }
    }
}
