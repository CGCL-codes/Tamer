package hu.xsolutions.xcsirip.csiriplist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CsiripEntry {

    private String id, time, user, text, fv;

    public CsiripEntry() {
    }

    public void setId(String string) {
        id = string;
    }

    public void setTime(String string) {
        time = string;
    }

    public void setUser(String string) {
        user = string;
    }

    public void setText(String string) {
        text = new String(string);
    }

    public void setMasodlagos(String string) {
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd. HH:mm.ss");

    private String toString;

    public String toHTML() {
        if (toString == null) {
            Date d = new Date(getTime() * 1000);
            StringBuffer sb = new StringBuffer();
            sb.append("<table border=0>");
            sb.append("<tr>");
            sb.append("<td valign=top width='50'>");
            String img = "<img align=left src='http://avatar.turulcsirip.hu/avatar/" + user + "/normal.jpg'>";
            sb.append(img);
            sb.append("</td>");
            sb.append("<td valign=top align=left>");
            sb.append("<b><a href='http://turulcsirip.hu/user/" + user + "/'>");
            sb.append(user);
            sb.append("</a>");
            sb.append(" - ");
            sb.append("<a href='http://turulcsirip.hu/perma/" + id + "'>" + df.format(d) + "</a>");
            sb.append("</b><br>");
            sb.append(fv);
            sb.append("<br><a href='re://" + getUser() + "'>" + "Reply" + "</a>");
            sb.append("</td></tr>");
            sb.append("</table>");
            toString = sb.toString();
        }
        return toString;
    }

    public void setFV(String string) {
        fv = string;
    }

    public long getTime() {
        return Long.valueOf(time).longValue();
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getFormattedTime() {
        Date d = new Date(getTime() * 1000);
        return df.format(d);
    }

    public String getText() {
        return text;
    }
}
