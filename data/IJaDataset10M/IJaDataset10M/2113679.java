package database.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: tereschenko
 * Date: 25.11.11
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class IPdate {

    private String ip;

    private Date date;

    public IPdate() {
    }

    public IPdate(String ip, Date ipDate) {
        this.ip = ip;
        this.date = ipDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date ipDate) {
        this.date = ipDate;
    }
}
