package cn.jsprun.domain;

public class Promotions implements java.io.Serializable {

    private static final long serialVersionUID = -4862672893567210963L;

    private String ip;

    private Integer uid;

    private String username;

    public Promotions() {
    }

    public Promotions(String ip, Integer uid, String username) {
        this.ip = ip;
        this.uid = uid;
        this.username = username;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getUid() {
        return this.uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
