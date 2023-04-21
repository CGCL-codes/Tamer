package com.sparkit.extracta.builder.model;

import java.util.*;

/**
 * This class provides a way to filter the elements that should not be visible in
 * the FilteredTree.
 *
 * @version 1.0
 * @author Dejan Pazin
 * @author Dominik Roblek
 * @author Bostjan Vester
 */
public class TagFilter {

    /** Basic elements */
    public static final String HMTL = "html";

    public static final String BODY = "body";

    public static final String HEAD = "head";

    public static final String TITLE = "title";

    /** Structural definition */
    public static final String H1 = "h1";

    public static final String H2 = "h2";

    public static final String H3 = "h3";

    public static final String H4 = "h4";

    public static final String H5 = "h5";

    public static final String H6 = "h6";

    public static final String DIV = "div";

    public static final String SPAN = "span";

    public static final String BLOCKQUOTE = "blockquote";

    public static final String Q = "q";

    public static final String EM = "em";

    public static final String STRONG = "strong";

    public static final String CITE = "cite";

    public static final String CODE = "code";

    public static final String SAMP = "samp";

    public static final String KBD = "kbd";

    public static final String VAR = "var";

    public static final String DFN = "dfn";

    public static final String ADDRESS = "address";

    public static final String BIG = "big";

    public static final String SMALL = "small";

    public static final String INS = "ins";

    public static final String DEL = "del";

    public static final String ACRONYM = "acronym";

    public static final String ABBR = "abbr";

    /** Presentation formatting */
    public static final String B = "b";

    public static final String I = "i";

    public static final String U = "u";

    public static final String STRIKE = "strike";

    public static final String S = "s";

    public static final String SUB = "sub";

    public static final String SUP = "sup";

    public static final String TT = "tt";

    public static final String PRE = "pre";

    public static final String CENTER = "center";

    public static final String BLINK = "blink";

    public static final String FONT = "font";

    public static final String BASEFONT = "basefont";

    public static final String MARQUEE = "marquee";

    /** Positioning */
    public static final String MULTICOLL = "multicoll";

    public static final String SPACER = "spacer";

    public static final String LAYER = "layer";

    public static final String ILAYER = "ilayer";

    public static final String NOLAYER = "nolayer";

    /** Links, graphics and sounds */
    public static final String A = "a";

    public static final String IMG = "img";

    public static final String BGSOUND = "bgsound";

    public static final String MAP = "map";

    public static final String AREA = "area";

    public static final String META = "meta";

    public static final String EMBED = "embed";

    public static final String OBJECT = "object";

    public static final String PARAM = "param";

    public static final String LINK = "link";

    /** Dividers */
    public static final String P = "p";

    public static final String BR = "br";

    public static final String HR = "hr";

    public static final String NOBR = "nobr";

    public static final String WBR = "wbr";

    /** Lists */
    public static final String UL = "ul";

    public static final String LI = "li";

    public static final String OL = "ol";

    public static final String DL = "dl";

    public static final String DT = "dt";

    public static final String DD = "dd";

    public static final String MENU = "menu";

    public static final String DIR = "dir";

    /** Forms */
    public static final String FORM = "form";

    public static final String INPUT = "input";

    public static final String BUTTON = "button";

    public static final String LABEL = "label";

    public static final String SELECT = "select";

    public static final String OPTION = "option";

    public static final String OPTGROUP = "optgroup";

    public static final String TEXTAREA = "textarea";

    public static final String FIELDSET = "fieldset";

    public static final String LEGEND = "legend";

    /** Tables */
    public static final String TABLE = "table";

    public static final String TR = "tr";

    public static final String TD = "td";

    public static final String TH = "th";

    public static final String TBODY = "tbody";

    public static final String TFOOT = "tfoot";

    public static final String THEAD = "thead";

    public static final String CAPTION = "caption";

    public static final String COL = "col";

    public static final String COLGROUP = "colgroup";

    /** Frames */
    public static final String FRAMESET = "frameset";

    public static final String FRAME = "frame";

    public static final String NOFRAMES = "noframes";

    public static final String IFRAME = "iframe";

    /** Scripts and Java */
    public static final String SCRIPT = "script";

    public static final String NOSCRIPT = "noscript";

    public static final String APPLET = "applet";

    public static final String SERVER = "server";

    /** Miscellaneous */
    public static final String DOCTYPE = "doctype";

    public static final String ISINDEX = "isindex";

    public static final String STYLE = "style";

    public static final String BDO = "bdo";

    /** List of filtered tags */
    private List m_tagList;

    /**
   * The constructor takes a list of tags to filter.
   *
   * @param tagList
   */
    public TagFilter(List tagList) {
        m_tagList = tagList;
    }

    /**
   * Returns all available tags.
   *
   * @return List of tags
   */
    public static List getAllTags() {
        String[] allTags = { A, APPLET, AREA, B, BASEFONT, BDO, BGSOUND, BIG, BLINK, BLOCKQUOTE, BODY, BR, BUTTON, CAPTION, CENTER, CITE, CODE, COL, COLGROUP, DD, DEL, DFN, DIR, DIV, DL, DOCTYPE, DT, EM, EMBED, FIELDSET, FONT, FORM, FRAME, FRAMESET, H1, H2, H3, H4, H5, H6, HEAD, HMTL, HR, I, IFRAME, ILAYER, IMG, INPUT, INS, ISINDEX, KBD, LABEL, LAYER, LEGEND, LI, LINK, MAP, MARQUEE, MENU, META, MULTICOLL, NOBR, NOFRAMES, NOLAYER, NOSCRIPT, OBJECT, OL, OPTGROUP, OPTION, P, PARAM, PRE, Q, S, SAMP, SCRIPT, SELECT, SERVER, SMALL, SPACER, SPAN, STRIKE, STRONG, STYLE, SUB, SUP, TABLE, TBODY, TD, TEXTAREA, TFOOT, TH, THEAD, TITLE, TR, TT, U, UL, VAR, WBR };
        return new ArrayList(Arrays.asList(allTags));
    }

    /**
   * This method returns all tags which are not in hidden list.
   *
   * @return List of visible tags
   */
    public List getVisibleTags() {
        List list = getAllTags();
        list.removeAll(m_tagList);
        return list;
    }

    /**
   * Returns true if the tag is in the list of tags that are filtered.
   *
   * @param tag The element in question
   * @return boolean
   */
    public boolean isTagFiltered(String tag) {
        return (m_tagList != null) && (m_tagList.contains(tag));
    }
}
