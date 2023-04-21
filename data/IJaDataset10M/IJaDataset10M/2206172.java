package oxygen.markup;

public class MarkupLink {

    public String text;

    public String text2;

    public boolean explicitLink;

    public String urlpart;

    public boolean image;

    public MarkupLink() {
    }

    public MarkupLink(String s, boolean explicitLink0) {
        explicitLink = explicitLink0;
        reset(s);
    }

    public void reset(String s) {
        if (s.length() > 2 && s.charAt(0) == 'i' && s.charAt(1) == ' ') {
            s = s.substring(2);
            image = true;
        }
        urlpart = s.trim();
        text = urlpart;
        int idx = urlpart.indexOf("|");
        if (idx >= 0) {
            text = urlpart.substring(0, idx).trim();
            urlpart = urlpart.substring(idx + 1).trim();
        }
        idx = urlpart.indexOf("|");
        if (idx >= 0) {
            text2 = urlpart.substring(idx + 1);
            urlpart = urlpart.substring(0, idx).trim();
        }
    }
}
