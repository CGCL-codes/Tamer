package gpsxml.xml;

import java.util.Calendar;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class User {

    private String id;

    private String organization;

    private String role = "gps";

    private String create_day;

    /** Creates a new instance of User */
    public User() {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = Math.min((calendar.get(Calendar.MONTH) + 1), 12);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        create_day = year + "-";
        stringBuilder.append(year);
        stringBuilder.append("-");
        if (month < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(month);
        stringBuilder.append("-");
        if (day < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(day);
        create_day = stringBuilder.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreate_day() {
        return create_day;
    }

    public void setCreate_day(String create_day) {
        this.create_day = create_day;
    }

    public String toString() {
        return getId();
    }
}
