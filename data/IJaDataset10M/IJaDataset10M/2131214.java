package jashi.languages;

import jashi.*;
import java.util.*;

public class Ini extends LanguageData {

    public Ini() {
        LangName = "INI";
        CommentSingle = new HashMap<Integer, String>();
        CommentSingle.put(0, ";");
        CommentMulti = new HashMap<String, String>();
        CaseKeywords = Global.GESHI_CAPS_NO_CHANGE;
        Quotemarks = new ArrayList<String>();
        Quotemarks.add("\"");
        EscapeChar = null;
        Keywords = new HashMap<Integer, ArrayList<String>>();
        Symbols = new HashMap<Integer, ArrayList<String>>();
        ArrayList<String> SymStrList = new ArrayList<String>();
        SymStrList.add("[");
        SymStrList.add("]");
        SymStrList.add("=");
        Symbols.put(Global.JASHI_STUBINDEX, SymStrList);
        CaseSensitive = new HashMap<Integer, Boolean>();
        CaseSensitive.put(Global.GESHI_COMMENTS, false);
        Styles.Comments.put(0, "color:#666666;font-style:italic;");
        Styles.EscapeChar.put(0, "");
        Styles.Brackets.put(0, "");
        Styles.Strings.put(0, "color:#933;");
        Styles.Numbers.put(0, "");
        Styles.Methods.put(0, "");
        Styles.Symbols.put(0, "color:#000066;font-weight:bold;");
        Styles.Regexps.put(0, "color:#000066;font-weight:bold;");
        Styles.Regexps.put(1, "color:#000099;");
        Styles.Regexps.put(2, "color:#660066;");
        Styles.Script.put(0, "");
        URLS = new HashMap<Integer, String>();
        OOLANG = false;
        ObjectSplitters = new HashMap<Integer, String>();
        Regexps = new HashMap<Integer, HashMap<Integer, String>>();
        HashMap<Integer, String> RegStrMap0 = new HashMap<Integer, String>();
        RegStrMap0.put(Global.JASHI_STUBINDEX, "\\[.+\\]");
        Regexps.put(0, RegStrMap0);
        HashMap<Integer, String> RegStrMap1 = new HashMap<Integer, String>();
        RegStrMap1.put(Global.GESHI_SEARCH, "^(\\s*)([a-zA-Z0-9_]+)(\\s*=)");
        RegStrMap1.put(Global.GESHI_REPLACE, "\\2");
        RegStrMap1.put(Global.GESHI_MODIFIERS, "m");
        RegStrMap1.put(Global.GESHI_BEFORE, "\\1");
        RegStrMap1.put(Global.GESHI_AFTER, "\\3");
        Regexps.put(1, RegStrMap1);
        HashMap<Integer, String> RegStrMap2 = new HashMap<Integer, String>();
        RegStrMap2.put(Global.GESHI_SEARCH, "([<>\";a-zA-Z0-9_]+\\s*)=(.*)");
        RegStrMap2.put(Global.GESHI_REPLACE, "\\2");
        RegStrMap2.put(Global.GESHI_MODIFIERS, "");
        RegStrMap2.put(Global.GESHI_BEFORE, "\\1=");
        RegStrMap2.put(Global.GESHI_AFTER, "");
        Regexps.put(2, RegStrMap2);
        StrictModeApplies = Global.GESHI_NEVER;
        ScriptDelimiters = new HashMap<Integer, HashMap<String, String>>();
        HighlightStrictBlock = new HashMap<Integer, Boolean>();
    }
}
