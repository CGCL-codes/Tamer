package admin;

/**
 *
 * @author Lucek
 */
public class MenuItem extends Menu {

    private String url;

    private String name;

    /** Creates a new instance of MenuItem */
    public MenuItem(String name, String url) {
        this.setName(name);
        this.setUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url + ".jsp";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String t = "";
        t += "<table>\n";
        t += "<tr><td><a href='" + url + "'>" + name + "</a></td></tr>\n";
        for (MenuItem item : items) {
            t += "<tr><td>&nbsp;&nbsp;</td><td>" + item.toString() + "</td></tr>\n";
        }
        t += "</table>\n";
        return (t);
    }
}
