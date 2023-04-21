package com.zzsoft.app.cadrems.login;

import java.util.ArrayList;
import com.zzsoft.app.cadrems.C_ATIMS;
import framework.zze2p.C;
import framework.zze2p.db.information.Pojo_Tab_I;
import framework.zze2p.mod.pojodb.PojoDB;

public class LoginBO implements LoginBOI {

    public static final int SUCCESS = 0;

    public static final int PASSWORD_IS_WRONG = 1;

    public static final int USERNAME_IS_NOFIND = 2;

    public static final int USER_IS_NOTCANUSE = 3;

    private boolean checkPassword(PojoDB pojoDb, String password) {
        if (password == null) return false;
        if (pojoDb.getString(C_ATIMS.worker_base_t.passwd) == null) return false;
        return pojoDb.getString(C_ATIMS.worker_base_t.passwd).equals(password);
    }

    public int login(PojoDB pojo4) {
        pojo4.set(C.Pojo.Pojo4.Tab_Full_Name, C_ATIMS.worker_base_t.$Full_TabName);
        String password = pojo4.getString(C_ATIMS.worker_base_t.passwd);
        Pojo_Tab_I pojo_tab = (Pojo_Tab_I) pojo4.getPojo1Class();
        ArrayList arr = new ArrayList();
        arr.add(new String[] { C_ATIMS.worker_base_t.user_id });
        pojo_tab.set(C.DBS.Tab.Tab_UNI_VALUE_GROUP_NAMES, arr);
        pojo4.d$dbCMD().setConditionMK$No();
        boolean flag = pojo4.d$find();
        if (!flag) {
            return USERNAME_IS_NOFIND;
        }
        if (checkPassword(pojo4, password)) {
            return SUCCESS;
        } else {
            return PASSWORD_IS_WRONG;
        }
    }
}
