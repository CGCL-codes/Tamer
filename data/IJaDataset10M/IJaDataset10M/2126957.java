package html;

/**
 *
 * @author jhierrot
 */
public class MenuItem {

    private String Text;

    private String Url;

    private String Target;

    private boolean Separator = false;

    private static MenuItem MISeparator = null;

    public MenuItem(String pText, String pUrl) {
        Text = pText;
        Url = pUrl;
        Target = null;
    }

    public MenuItem() {
        Separator = true;
    }

    public MenuItem(String pText, String pUrl, String pTarget) {
        Text = pText;
        Url = pUrl;
        Target = pTarget;
    }

    protected String HtmlPropio() {
        if (Separator) return ("<hr>"); else return ("<a href=\"" + Url + "\" " + ((Target == null) ? "" : "target=\"" + Target + "\"") + "onMouseOver=\"stopTime();\" onMouseOut=\"startTime();\">" + Text + "</a><br>\n");
    }

    /**
 * @return the MISeparator
 */
    public static MenuItem getMISeparator() {
        if (MISeparator == null) MISeparator = new MenuItem();
        return MISeparator;
    }
}
