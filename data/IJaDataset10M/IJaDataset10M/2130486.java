package html;

import java.util.*;

public class HtmlParser {

    private static Vector taglist = new Vector();

    private String original;

    private StringBuffer result = new StringBuffer();

    private int depth = 0;

    boolean valid = true;

    static {
        TagInfo html, temp, body;
        taglist.add(html = new TagInfo("HTML", "", null));
        taglist.add(temp = new TagInfo("HEAD", "", html));
        taglist.add(new TagInfo("TITLE", "Titel ", temp));
        taglist.add(body = new TagInfo("BODY", "", html));
        taglist.add(new TagInfo("BR", "\n", body));
        taglist.add(temp = new TagInfo("TABLE", "Tabelle ", body));
        taglist.add(temp = new TagInfo("TBODY", "", temp));
        taglist.add(temp = new TagInfo("TR", "", temp));
        taglist.add(new TagInfo("TD", "", temp));
        taglist.add(new TagInfo("h2", "Text_sehr_gross ", body));
        taglist.add(new TagInfo("h3", "Text_gross ", body));
        taglist.add(new TagInfo("h4", "Text_mittel ", body));
        taglist.add(new TagInfo("h5", "Text_normal ", body));
    }

    public HtmlParser(String s) {
        original = s.trim();
        parse(original);
        if (valid) result.append("Ende");
    }

    private boolean parse(String s) {
        int startFrom = 0, indend = 0;
        int TDcount = 0, TRcount = 0;
        while (true) {
            int ind = s.indexOf('>', startFrom);
            if (ind == -1) {
                if (s.indexOf('<', startFrom) != -1) {
                    result = new StringBuffer("FALSCHE EINGABE");
                    return false;
                } else {
                    if (startFrom == 0 && s.length() > 0) result.append(s).append("; ");
                    return true;
                }
            }
            while (s.charAt(startFrom) == ' ') startFrom++;
            String tagname = s.substring(startFrom + 1, ind);
            if (!tagname.equals("BR")) {
                if (depth == 0 && !tagname.equals("HTML") || depth != 0 && tagname.equals("HTML")) {
                    result = new StringBuffer("FALSCHE EINGABE");
                    return false;
                }
                if (s.charAt(s.length() - 1) != '>') {
                    result = new StringBuffer("FALSCHE EINGABE");
                    return false;
                }
                String closeTag = "</" + tagname + ">";
                indend = s.indexOf(closeTag, startFrom);
                startFrom = indend + closeTag.length();
                if (indend == -1) {
                    result = new StringBuffer("FALSCHE EINGABE");
                    return false;
                }
            } else {
                startFrom = ind + 1;
            }
            TagInfo tag = null;
            for (Iterator i = taglist.iterator(); i.hasNext(); ) {
                tag = (TagInfo) i.next();
                if (tag.tagname.equals(tagname)) {
                    if (tag.occured || (tag.requires != null && tag.requires.occured == false)) {
                        result = new StringBuffer("FALSCHE EINGABE");
                        return false;
                    }
                    break;
                }
            }
            if (!tag.tagname.equals(tagname)) {
                result = new StringBuffer("FALSCHE EINGABE");
                return false;
            }
            result.append(tag.replacement);
            if (tagname.equals("BR")) continue;
            if (tagname.equals("TD")) {
                TDcount += 1;
                result.append(TDcount + " ");
            }
            if (tagname.equals("TR")) {
                TRcount += 1;
                result.append(TRcount + " ");
            }
            tag.occured = true;
            depth++;
            valid = parse(s.substring(ind + 1, indend).trim());
            if (!valid) return false;
            tag.occured = false;
            depth--;
        }
    }

    public String toString() {
        return result.toString();
    }
}
