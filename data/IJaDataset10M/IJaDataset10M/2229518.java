package weblife.datamodel;

/**
 * Einfaches Datenmodel einer PN
 * @author Knoll
 *
 */
public class Pn {

    private String pnid;

    private String userid;

    private String name;

    private String type;

    private String isread;

    private String message;

    private String time;

    private String ishtml;

    public Pn(String pnid, String userid, String name, String type, String isread, String message, String ishtml, String time) {
        this.pnid = pnid;
        this.userid = userid;
        this.name = name;
        this.type = type;
        this.isread = isread;
        this.message = message;
        this.time = time;
        this.ishtml = ishtml;
    }

    public String getPnid() {
        return pnid;
    }

    public String getType() {
        return type;
    }

    public String getIsread() {
        return isread;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getUserid() {
        return userid;
    }

    public void setIsread() {
        this.isread = "1";
    }

    public String getIshtml() {
        return ishtml;
    }
}
