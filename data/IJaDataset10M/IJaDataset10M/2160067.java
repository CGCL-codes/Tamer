package cn.vlabs.duckling.vwb.services.dlog.rdi.data;

public class LoginRecord {

    String userip;

    String time;

    public void setUserip(String ip) {
        userip = ip;
    }

    public String getUserip() {
        return userip;
    }

    public void setTime(String access) {
        time = access;
    }

    public String getTime() {
        return time;
    }
}
