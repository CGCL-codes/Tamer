package com.burry.services;

import java.util.List;
import java.util.Map;
import com.burry.db.SQLMachine;

public class UserService {

    public Map createUser(String userName, String password, String inviteUser) {
        String sql = null;
        if (inviteUser == null || inviteUser.equals("")) {
            sql = "insert into bu_users(username,pass) values('" + userName + "','" + password + "')";
        } else {
            sql = "insert into bu_users(username,pass,upper_one,upper_two,upper_thrd,upper_four) " + "select '" + userName + "','" + password + "',username,upper_one,upper_two,upper_thrd from bu_users where username = '" + inviteUser + "'";
        }
        SQLMachine sqlM = new SQLMachine("default");
        return sqlM.updateResult(sql);
    }

    public Map login(String username, String password) {
        String sql = "select * from bu_users where username = '" + username + "' and pass = '" + password + "'";
        SQLMachine sqlM = new SQLMachine("default");
        Map result = sqlM.getResult(sql);
        return result;
    }

    public Map setRate(String one, String two, String thd, String four) {
        String sql = "update bu_setting set one_set = " + one + ",two_set = " + two + ",trd_set = " + thd + ",for_set=" + four + "";
        SQLMachine sqlM = new SQLMachine("default");
        Map result = sqlM.updateResult(sql);
        return result;
    }

    public Map getRate() {
        String sql = "select * from bu_setting";
        SQLMachine sqlM = new SQLMachine("default");
        Map result = sqlM.getResult(sql);
        return result;
    }

    public List getUserAddress(String userId) {
        List result = null;
        String sql = "select * from bu_user_address";
        SQLMachine sqlM = new SQLMachine("default");
        result = sqlM.getResultSet(sql);
        return result;
    }

    public Map addUserAddress(Integer id, String manName, String address, String manTel, String manCode) {
        String sql = "insert into bu_user_address(userid,man_name,address,man_tel,man_code) values(" + id + ",'" + manName + "','" + address + "','" + manTel + "','" + manCode + "')";
        SQLMachine sqlM = new SQLMachine("default");
        Map result = sqlM.updateResult(sql);
        return result;
    }

    public Map delUserAddress(String id) {
        String sql = "delete from bu_user_address where id = " + id;
        SQLMachine sqlM = new SQLMachine("default");
        Map result = sqlM.updateResult(sql);
        return result;
    }
}
