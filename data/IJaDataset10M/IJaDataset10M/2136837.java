package jashi.languages;

import jashi.*;
import java.util.*;

public class Gettext extends LanguageData {

    public Gettext() {
        LangName = "GNUGettext";
        CommentSingle = new HashMap<Integer, String>();
        CommentSingle.put(0, "#:");
        CommentSingle.put(0, "#.");
        CommentSingle.put(0, "#");
        CommentMulti = new HashMap<String, String>();
        CommentRegexp = new HashMap<Integer, String>();
        CaseKeywords = Global.GESHI_CAPS_NO_CHANGE;
        Quotemarks = new ArrayList<String>();
        Quotemarks.add("\'");
        Quotemarks.add("\"");
        EscapeChar = '\\';
        Keywords = new HashMap<Integer, ArrayList<String>>();
        ArrayList<String> KeyStrList1 = new ArrayList<String>();
        KeyStrList1.add("msgid");
        KeyStrList1.add("msgstr");
        Keywords.put(1, KeyStrList1);
        Symbols = new HashMap<Integer, ArrayList<String>>();
        CaseSensitive = new HashMap<Integer, Boolean>();
        CaseSensitive.put(Global.GESHI_COMMENTS, false);
        CaseSensitive.put(1, true);
        Styles.Keywords.put(1, "color:#000000;font-weight:bold;");
        Styles.Comments.put(0, "color:#000099;");
        Styles.Comments.put(1, "color:#000099;");
        Styles.Comments.put(2, "color:#666666;font-style:italic;");
        Styles.EscapeChar.put(0, "color:#000099;font-weight:bold;");
        Styles.Strings.put(0, "color:#ff0000;");
        OOLANG = false;
        Regexps = new HashMap<Integer, HashMap<Integer, String>>();
        StrictModeApplies = Global.GESHI_NEVER;
        TabWidth = 4;
    }
}
