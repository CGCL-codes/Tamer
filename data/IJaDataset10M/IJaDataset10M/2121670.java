package org.stikiweb.translations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.stikiweb.contentMagnager.AttachmentManager;
import org.stikiweb.contentMagnager.PageInfoNode;
import org.stikiweb.contentMagnager.PageManager;
import org.stikiweb.contentMagnager.PageManagerException;
import org.stikiweb.contentMagnager.PermissionManager;

/**
 * @author Allen L (BigLee) Haslup
 *
 */
public class StikiWebTranslator {

    protected class TranslatorState {

        public static final int NO_TAG = 0;

        public static final int P_TAG = 1;

        public static final int TABLE_TAG = 2;

        public static final int TABLE_END = -TABLE_TAG;

        public static final int TR_TAG = 3;

        public static final int TD_TAG = 4;

        public static final int TH_TAG = 5;

        public static final int B_TAG = 6;

        public static final int I_TAG = 7;

        public static final int OL_TAG = 8;

        public static final int UL_TAG = 9;

        public static final int FONT_TAG = 10;

        public static final int H1_TAG = 11;

        public static final int H2_TAG = 12;

        public static final int H3_TAG = 13;

        public static final int PRE_TAG = 14;

        public static final int HR_TAG = 15;

        protected Stack tagStack = new Stack();

        protected Stack attrStack = new Stack();

        public int tableNestingLevel = 0;

        private boolean inBold = false;

        private boolean inItalics = false;

        private boolean inFont = false;

        private boolean inPara = false;

        private boolean inAutoTable = false;

        public void push(int tag, String attributes) {
            tagStack.push(new Integer(tag));
            attrStack.push(attributes);
            switch(tag) {
                case TABLE_TAG:
                    tableNestingLevel++;
                    break;
                case B_TAG:
                    inBold = true;
                    break;
                case I_TAG:
                    inItalics = true;
                    break;
                case FONT_TAG:
                    inFont = true;
                    break;
            }
        }

        public void pop() {
            if (!tagStack.isEmpty()) {
                int poppedTag = ((Integer) tagStack.pop()).intValue();
                attrStack.pop();
                switch(poppedTag) {
                    case TABLE_TAG:
                        tableNestingLevel--;
                        break;
                    case B_TAG:
                        inBold = false;
                        break;
                    case I_TAG:
                        inItalics = false;
                        break;
                    case FONT_TAG:
                        inFont = false;
                }
            }
        }

        public int peekTag() {
            if (tagStack.isEmpty()) {
                return -1;
            }
            return ((Integer) tagStack.peek()).intValue();
        }

        public String peekAttr() {
            if (attrStack.isEmpty()) {
                return "";
            }
            return (String) attrStack.peek();
        }

        /**
		 * @return
		 */
        public boolean isInBold() {
            return inBold;
        }

        /**
		 * @return
		 */
        public boolean isInFont() {
            return inFont;
        }

        /**
		 * @return
		 */
        public boolean isInItalics() {
            return inItalics;
        }

        /**
		 * @return
		 */
        public boolean isInPara() {
            return inPara;
        }

        /**
		 * @return
		 */
        public int getTableNestingLevel() {
            return tableNestingLevel;
        }

        public int stackSize() {
            return tagStack.size();
        }

        /**
		 * @return
		 */
        public boolean isInAutoTable() {
            return inAutoTable;
        }

        /**
		 * @param b
		 */
        public void setInAutoTable(boolean b) {
            inAutoTable = b;
        }
    }

    public String translateText(String rawText, String currentPage, String nestedPage, HttpServletRequest req) throws IOException {
        StringBuffer htmlTextBuffer = new StringBuffer();
        StringReader rawTextReader = new StringReader(rawText);
        BufferedReader rawBR = new BufferedReader(rawTextReader);
        TranslatorState state = new TranslatorState();
        String rawLine = rawBR.readLine();
        while (rawLine != null) {
            htmlTextBuffer.append(translateLine(rawLine, currentPage, nestedPage, state, req, false));
            rawLine = rawBR.readLine();
        }
        int tag = state.peekTag();
        while (tag > 0) {
            htmlTextBuffer.append(closeTagFor(tag));
            state.pop();
            tag = state.peekTag();
        }
        return htmlTextBuffer.toString();
    }

    protected String openTagFor(int tag, String atts) {
        switch(tag) {
            case TranslatorState.P_TAG:
                return "<p " + atts + " >\n";
            case TranslatorState.TABLE_TAG:
                return "<table " + atts + " >\n";
            case TranslatorState.TR_TAG:
                return "<tr " + atts + " >\n";
            case TranslatorState.TD_TAG:
                return "<td " + atts + " >\n";
            case TranslatorState.TH_TAG:
                return "<th " + atts + " >\n";
            case TranslatorState.B_TAG:
                return "<b " + atts + " >\n";
            case TranslatorState.I_TAG:
                return "<i " + atts + " >\n";
            case TranslatorState.OL_TAG:
                return "<ol " + atts + " >\n";
            case TranslatorState.UL_TAG:
                return "<ul " + atts + " >\n";
            case TranslatorState.FONT_TAG:
                return "<font " + atts + " >\n";
            case TranslatorState.H1_TAG:
                return "<h1 " + atts + " >\n";
            case TranslatorState.H2_TAG:
                return "<h2 " + atts + " >\n";
            case TranslatorState.H3_TAG:
                return "<h3 " + atts + " >\n";
            case TranslatorState.PRE_TAG:
                return "<pre " + atts + " >\n";
        }
        return "";
    }

    protected String closeTagFor(int tag) {
        switch(tag) {
            case TranslatorState.P_TAG:
                return "</p>\n";
            case TranslatorState.TABLE_TAG:
                return "</table>\n";
            case TranslatorState.TR_TAG:
                return "</tr>\n";
            case TranslatorState.TD_TAG:
                return "</td>\n";
            case TranslatorState.TH_TAG:
                return "</th>\n";
            case TranslatorState.B_TAG:
                return "</b>\n";
            case TranslatorState.I_TAG:
                return "</i>\n";
            case TranslatorState.OL_TAG:
                return "</ol>\n";
            case TranslatorState.UL_TAG:
                return "</ul>\n";
            case TranslatorState.FONT_TAG:
                return "</font>\n";
            case TranslatorState.H1_TAG:
                return "</h1>\n";
            case TranslatorState.H2_TAG:
                return "</h2>\n";
            case TranslatorState.H3_TAG:
                return "</h3>\n";
            case TranslatorState.PRE_TAG:
                return "</pre>\n";
        }
        return "";
    }

    protected String translateLine(String rawLine, String currentPage, String nestedPage, TranslatorState state, HttpServletRequest req, boolean recursed) {
        if (rawLine.startsWith("~~>")) {
            return "";
        }
        int tagThisLine = TranslatorState.NO_TAG;
        if (rawLine.trim().length() == 0) {
            tagThisLine = TranslatorState.P_TAG;
        } else if (rawLine.startsWith(" ")) {
            tagThisLine = TranslatorState.PRE_TAG;
        } else if (rawLine.startsWith("!!!")) {
            tagThisLine = TranslatorState.H1_TAG;
        } else if (rawLine.startsWith("!!")) {
            tagThisLine = TranslatorState.H2_TAG;
        } else if (rawLine.startsWith("!")) {
            tagThisLine = TranslatorState.H3_TAG;
        } else if (rawLine.startsWith("---")) {
            tagThisLine = TranslatorState.HR_TAG;
        } else if (rawLine.startsWith("{|")) {
            tagThisLine = TranslatorState.TABLE_TAG;
        } else if (rawLine.startsWith("|}")) {
            tagThisLine = TranslatorState.TABLE_END;
        } else if (rawLine.startsWith("|-")) {
            tagThisLine = TranslatorState.TR_TAG;
        } else if (rawLine.startsWith("||")) {
            tagThisLine = TranslatorState.TD_TAG;
        } else if (rawLine.startsWith("|!")) {
            tagThisLine = TranslatorState.TH_TAG;
        }
        String retval = "";
        if (state.tableNestingLevel == 0 && (tagThisLine == TranslatorState.TR_TAG || tagThisLine == TranslatorState.TH_TAG || tagThisLine == TranslatorState.TD_TAG)) {
            retval += "<table  border>\n";
            state.push(TranslatorState.TABLE_TAG, "border");
            state.setInAutoTable(true);
        }
        if (state.tableNestingLevel == 1 && state.inAutoTable && tagThisLine != TranslatorState.TR_TAG && tagThisLine != TranslatorState.TH_TAG && tagThisLine != TranslatorState.TD_TAG && tagThisLine != TranslatorState.TABLE_END) {
            while (state.peekTag() != TranslatorState.TABLE_TAG) {
                int tag = state.peekTag();
                retval += closeTagFor(tag);
                state.pop();
            }
            retval += "</table>\n";
            state.pop();
            state.setInAutoTable(false);
        }
        if (state.inAutoTable && state.peekTag() != TranslatorState.TABLE_TAG && tagThisLine != TranslatorState.TR_TAG && tagThisLine != TranslatorState.TABLE_END && state.tableNestingLevel == 1 && !recursed) {
            while (state.peekTag() != TranslatorState.TABLE_TAG) {
                retval += closeTagFor(state.peekTag());
                state.pop();
            }
            retval += "<tr>\n";
            state.push(TranslatorState.TR_TAG, "");
        }
        if (state.peekTag() == TranslatorState.TABLE_TAG && tagThisLine != TranslatorState.TR_TAG) {
            state.push(TranslatorState.TR_TAG, "");
            retval += "<tr>\n";
        }
        if (state.peekTag() == TranslatorState.TR_TAG && tagThisLine != TranslatorState.TD_TAG && tagThisLine != TranslatorState.TH_TAG) {
            state.push(TranslatorState.TD_TAG, "");
            retval += "<td>\n";
        }
        if (state.peekTag() == TranslatorState.PRE_TAG && tagThisLine != TranslatorState.PRE_TAG) {
            state.pop();
            retval += "</pre>\n";
        }
        if (tagThisLine == TranslatorState.NO_TAG && state.peekTag() != TranslatorState.P_TAG) {
            state.push(TranslatorState.P_TAG, "");
            retval += "<p>\n";
        }
        if (tagThisLine != TranslatorState.NO_TAG && state.peekTag() == TranslatorState.P_TAG) {
            state.pop();
            retval += "</p>\n";
        }
        if (tagThisLine == TranslatorState.TD_TAG || tagThisLine == TranslatorState.TH_TAG || tagThisLine == TranslatorState.TR_TAG || tagThisLine == TranslatorState.TABLE_END) {
            int curtag = state.peekTag();
            if (curtag == TranslatorState.TH_TAG) {
                state.pop();
                retval += "</th>\n";
            } else if (curtag == TranslatorState.TD_TAG) {
                state.pop();
                retval += "</td>\n";
            }
        }
        if (tagThisLine == TranslatorState.TABLE_END && state.peekTag() == TranslatorState.TR_TAG) {
            state.pop();
            retval += "</tr>\n";
        }
        if (tagThisLine == TranslatorState.TR_TAG && state.peekTag() == TranslatorState.TR_TAG) {
            state.pop();
            retval += "</tr>\n";
        }
        String atts;
        switch(tagThisLine) {
            case TranslatorState.NO_TAG:
                retval += translateRun(rawLine, currentPage, nestedPage, state, req);
                break;
            case TranslatorState.P_TAG:
                if (state.peekTag() == TranslatorState.P_TAG) {
                    retval += "</p><p>\n";
                } else {
                    retval += "<p>\n";
                    state.push(TranslatorState.P_TAG, "");
                }
                break;
            case TranslatorState.TABLE_TAG:
                atts = sanitizeParms(rawLine.substring(2).trim());
                retval += "<table " + atts + " >\n";
                state.push(TranslatorState.TABLE_TAG, atts);
                break;
            case TranslatorState.TABLE_END:
                if (state.tableNestingLevel > 0) {
                    retval += "</table>\n";
                    state.pop();
                }
                break;
            case TranslatorState.TR_TAG:
                int len = rawLine.length();
                int rover = 1;
                while (rover < len && rawLine.charAt(rover) == '-') {
                    rover++;
                }
                atts = sanitizeParms(rawLine.substring(rover).trim());
                retval += "<tr " + atts + " >\n";
                state.push(TranslatorState.TR_TAG, atts);
                break;
            case TranslatorState.TD_TAG:
            case TranslatorState.TH_TAG:
                int parmEnd = findUnescaped('|', rawLine, 2);
                if (parmEnd < 0) {
                    parmEnd = 2;
                }
                atts = sanitizeParms(rawLine.substring(2, parmEnd));
                if (tagThisLine == TranslatorState.TD_TAG) {
                    retval += "<td " + atts + ">\n";
                    state.push(TranslatorState.TD_TAG, atts);
                } else {
                    retval += "<th " + atts + ">\n";
                    state.push(TranslatorState.TH_TAG, atts);
                }
                int runEnd = findUnescaped('|', rawLine, parmEnd + 1);
                while (runEnd > 0 && runEnd < rawLine.length() - 1 && "|!".indexOf(rawLine.charAt(runEnd + 1)) < 0) {
                    runEnd = findUnescaped('|', rawLine, runEnd + 1);
                }
                if (runEnd < 0) {
                    runEnd = rawLine.length();
                }
                String run = rawLine.substring(parmEnd + 1, runEnd).trim();
                if (run.length() > 0) {
                    retval += "<p>\n";
                    state.push(TranslatorState.P_TAG, "");
                    retval += translateRun(run, currentPage, nestedPage, state, req);
                    retval += "</p>\n";
                    state.pop();
                }
                if (runEnd < rawLine.length()) {
                    retval += translateLine(rawLine.substring(runEnd), currentPage, nestedPage, state, req, true);
                }
                break;
            case TranslatorState.PRE_TAG:
                if (state.peekTag() != TranslatorState.PRE_TAG) {
                    retval += "<pre>\n";
                    state.push(TranslatorState.PRE_TAG, "");
                }
                retval += recodePrecode(rawLine.substring(1)) + "\n";
                break;
            case TranslatorState.H1_TAG:
                retval += "<h1>\n";
                state.push(TranslatorState.H1_TAG, "");
                retval += translateRun(rawLine.substring(3), currentPage, nestedPage, state, req);
                retval += "</h1>\n";
                state.pop();
                break;
            case TranslatorState.H2_TAG:
                retval += "<h2>\n";
                state.push(TranslatorState.H2_TAG, "");
                retval += translateRun(rawLine.substring(2), currentPage, nestedPage, state, req);
                retval += "</h2>\n";
                state.pop();
                break;
            case TranslatorState.H3_TAG:
                retval += "<h3>\n";
                state.push(TranslatorState.H3_TAG, "");
                retval += translateRun(rawLine.substring(1), currentPage, nestedPage, state, req);
                retval += "</h1>\n";
                state.pop();
                break;
            case TranslatorState.HR_TAG:
                retval += "<hr />\n";
                break;
        }
        return retval;
    }

    protected String recodePrecode(String in) {
        StringBuffer sb = new StringBuffer();
        int len = in.length();
        for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
            switch(c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    protected static final int ESCSCORE_2 = '\\' * 1000 + '_';

    protected static final int BOLD_2 = '_' * 1000 + '_';

    protected static final int ESCQUOTE_2 = '\\' * 1000 + '\'';

    protected static final int ITALIC_2 = '\'' * 1000 + '\'';

    protected static final int ESCBRAK_2 = '[' * 1000 + '[';

    protected static final int ESCBRAK_2B = '\\' * 1000 + '[';

    protected static final int BRAK_1 = '[';

    protected static final int ESCSLASH_2 = '\\' * 1000 + '\\';

    protected static final int SPACE_2 = '\\' * 1000 + 'b';

    protected static final int SPACES_2 = '\\' * 1000 + 's';

    protected static final int ESCBRACE_2 = '\\' * 1000 + '{';

    protected static final int FORCELINE_2 = '\\' * 1000 + 'n';

    protected static final int PLUG_2 = '{' * 1000 + '{';

    protected static final int WICKIT_1 = '<';

    protected static final int AMP_1 = '&';

    protected static final int NIL = 0;

    protected String translateRun(String raw, String currentPage, String nestedPage, TranslatorState state, HttpServletRequest req) {
        int len = raw.length();
        if (len == 0) return "";
        int initSize = state.stackSize();
        int rover = 0;
        int c1 = raw.charAt(0);
        int c2 = (len == 1) ? 0 : raw.charAt(1);
        int c12 = c1 * 1000 + c2;
        StringBuffer sb = new StringBuffer();
        while (c1 != NIL) {
            int roll = 0;
            switch(c12) {
                case BOLD_2:
                    if (state.isInBold()) {
                        Stack stag = new Stack();
                        Stack sapp = new Stack();
                        while (state.peekTag() != TranslatorState.B_TAG) {
                            stag.push(new Integer(state.peekTag()));
                            sapp.push(state.peekAttr());
                            sb.append(closeTagFor(state.peekTag()));
                            state.pop();
                        }
                        sb.append("</b>");
                        state.pop();
                        while (stag.size() > 0) {
                            int restoreTag = ((Integer) stag.pop()).intValue();
                            String restoreAtts = (String) sapp.pop();
                            sb.append(openTagFor(restoreTag, restoreAtts));
                            state.push(restoreTag, restoreAtts);
                        }
                    } else {
                        sb.append("<b>");
                        state.push(TranslatorState.B_TAG, "");
                    }
                    roll = 2;
                    break;
                case ITALIC_2:
                    if (state.isInItalics()) {
                        Stack stag = new Stack();
                        Stack sapp = new Stack();
                        while (state.peekTag() != TranslatorState.I_TAG) {
                            stag.push(new Integer(state.peekTag()));
                            sapp.push(state.peekAttr());
                            sb.append(closeTagFor(state.peekTag()));
                            state.pop();
                        }
                        sb.append("</i>");
                        state.pop();
                        while (stag.size() > 0) {
                            int restoreTag = ((Integer) stag.pop()).intValue();
                            String restoreAtts = (String) sapp.pop();
                            sb.append(openTagFor(restoreTag, restoreAtts));
                            state.push(restoreTag, restoreAtts);
                        }
                    } else {
                        sb.append("<i>");
                        state.push(TranslatorState.I_TAG, "");
                    }
                    roll = 2;
                    break;
                case ESCBRACE_2:
                case ESCBRAK_2:
                case ESCBRAK_2B:
                case ESCQUOTE_2:
                case ESCSCORE_2:
                case ESCSLASH_2:
                    sb.append((char) c2);
                    roll = 2;
                    break;
                case FORCELINE_2:
                    sb.append("<br />");
                    roll = 2;
                    break;
                case SPACE_2:
                    sb.append("&nbsp;");
                    roll = 2;
                    break;
                case SPACES_2:
                    int runsize = 0;
                    int rover2 = rover + 2;
                    roll = 2;
                    while (rover2 < len && Character.isDigit(raw.charAt(rover2))) {
                        runsize = runsize * 10 + raw.charAt(rover2) - '0';
                        rover2++;
                        roll++;
                    }
                    if (runsize > 200) {
                        runsize = 200;
                    }
                    for (int i = 0; i < runsize; i++) {
                        sb.append("&nbsp;");
                    }
                    break;
            }
            if (roll == 0) {
                roll = 1;
                switch(c1) {
                    case AMP_1:
                        sb.append("&amp;");
                        roll = 1;
                        break;
                    case WICKIT_1:
                        sb.append("&lt;");
                        roll = 1;
                        break;
                    case BRAK_1:
                        int rover2 = rover + 1;
                        while (rover2 < len && raw.charAt(rover2) != ']') {
                            rover2++;
                        }
                        if (rover2 < len) {
                            sb.append(translateLinkBody(raw.substring(rover + 1, rover2), currentPage, nestedPage, req));
                            roll = rover2 + 1 - rover;
                        } else {
                            sb.append("[");
                            roll = 1;
                        }
                        break;
                    default:
                        sb.append((char) c1);
                        roll = 1;
                        break;
                }
            }
            rover += roll;
            c1 = (rover < len) ? raw.charAt(rover) : NIL;
            c2 = (rover + 1 < len) ? raw.charAt(rover + 1) : NIL;
            c12 = c1 * 1000 + c2;
        }
        while (state.stackSize() > initSize) {
            int tag = state.peekTag();
            sb.append(closeTagFor(tag));
            state.pop();
        }
        return sb.toString() + "\n";
    }

    protected String translateLinkBody(String body, String currentPage, String nestedPage, HttpServletRequest req) {
        String text, link;
        PermissionManager pm = PermissionManager.solo();
        String user = pm.getUserName(req);
        HttpSession session = req.getSession();
        HashMap vars = (HashMap) session.getAttribute("vars");
        if (vars == null) {
            vars = new HashMap();
            session.setAttribute("vars", vars);
        }
        int len = body.length();
        int split = body.indexOf('|');
        boolean linkIsText = false;
        if (split >= 0) {
            text = body.substring(0, split).trim();
            link = body.substring(split + 1, len).trim();
        } else {
            text = body.trim();
            link = text;
            linkIsText = true;
        }
        boolean tentative = false;
        boolean inline = false;
        boolean relative = false;
        String popper = "";
        String sider = "";
        while (link.length() > 0 && "?@^_<>".indexOf(link.charAt(0)) >= 0) {
            switch(link.charAt(0)) {
                case '?':
                    tentative = true;
                    break;
                case '_':
                    relative = true;
                    break;
                case '^':
                    popper = " target='_blank'";
                    break;
                case '@':
                    inline = true;
                    break;
                case '<':
                    sider = " align='left'";
                    break;
                case '>':
                    sider = " align='right'";
                    break;
            }
            link = link.substring(1).trim();
            if (linkIsText) {
                text = text.substring(1).trim();
            }
        }
        String liner = "";
        if (text.endsWith("\\n")) {
            liner = "<br />";
            text = text.substring(0, text.length() - 2);
            if (linkIsText) {
                link = link.substring(0, link.length() - 2);
            }
        }
        int parenloc = link.indexOf('(');
        HashMap linkparms = null;
        if (parenloc > 0) {
            linkparms = parseLinkParms(link);
            link = link.substring(0, parenloc).trim();
            if (linkIsText) {
                text = link;
            }
        }
        if (link.toLowerCase().startsWith("var?")) {
            String varn = link.substring(4).trim();
            int varsplit = varn.indexOf(',');
            String deftLink = null;
            if (varsplit > 0) {
                deftLink = varn.substring(varsplit + 1);
                varn = varn.substring(0, varsplit);
            }
            String newLink = (String) vars.get(varn);
            if (newLink != null && newLink.length() > 0) {
                link = newLink;
                if (link.charAt(0) == '_') {
                    link = link.substring(1).trim();
                    relative = true;
                }
                if (linkIsText) {
                    text = link;
                }
            } else if (deftLink != null) {
                link = deftLink;
                if (link.charAt(0) == '_') {
                    link = link.substring(1).trim();
                    relative = true;
                }
                if (linkIsText) {
                    text = link;
                }
            }
        }
        if (relative) {
            String dot = link.startsWith("/") ? "" : ".";
            link = nestedPage + dot + capitalize(link);
            if (linkIsText) {
                text = capitalize(text);
            }
        }
        if (link.length() == 0) {
            return "[" + body + "]";
        }
        boolean textIsImage = false;
        if (!linkIsText && text.startsWith("*")) {
            textIsImage = true;
            text = text.substring(1).trim();
            if (text.indexOf(':') < 0 && text.indexOf('/') >= 0) {
                text = "viewAttachment.do?name=" + convertName(text);
            }
        }
        if (text.length() == 0) {
            text = "(link)";
        }
        text = sanitizeText(text);
        if (link.charAt(0) == '$') {
            String sayNoIcon = "";
            if (link.equalsIgnoreCase("$login")) {
                if (tentative && !"Visitor".equalsIgnoreCase(pm.getUserName(req))) {
                    return "";
                }
                if (linkIsText) {
                    text = "Login";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:login.do'" + popper + ">" + text + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$logout")) {
                if (tentative && "Visitor".equalsIgnoreCase(pm.getUserName(req))) {
                    return "";
                }
                if (linkIsText) {
                    text = "Logout";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:logout.do'" + popper + ">" + text + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$edit")) {
                if (currentPage.length() == 0 || pm.userPermLevelForPage(pm.getUserName(req), currentPage) < PermissionManager.EDIT) {
                    if (tentative) {
                        return "";
                    }
                    sayNoIcon = "<img src='images/stop.png' border='0' />";
                }
                if (linkIsText) {
                    text = "Edit page";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:editPage.do?name=" + currentPage + "&operation=EDIT'" + popper + " >" + text + sayNoIcon + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$audit")) {
                if (currentPage.length() == 0 || pm.userPermLevelForPage(pm.getUserName(req), currentPage) < PermissionManager.AUDIT) {
                    if (tentative) {
                        return "";
                    }
                    sayNoIcon = "<img src='images/stop.png' border='0' />";
                }
                if (linkIsText) {
                    text = "View page source";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:editPage.do?name=" + currentPage + "&operation=AUDIT'" + popper + " >" + text + sayNoIcon + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$comment")) {
                if (currentPage.length() == 0 || pm.userPermLevelForPage(pm.getUserName(req), currentPage) < PermissionManager.COMMENT) {
                    if (tentative) {
                        return "";
                    }
                    sayNoIcon = "<img src='images/stop.png' border='0' />";
                }
                if (linkIsText) {
                    text = "Leave comment";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:editPage.do?name=" + currentPage + "&operation=COMMENT'" + popper + " >" + text + sayNoIcon + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$admin")) {
                if (pm.userPermLevelForPage(pm.getUserName(req), currentPage) < PermissionManager.ADMIN) {
                    if (tentative) {
                        return "";
                    }
                    sayNoIcon = "<img src='images/stop.png' border='0' />";
                }
                if (linkIsText) {
                    text = "Administer Page";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:adminPage.do?name=" + currentPage + "' " + popper + ">" + text + sayNoIcon + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$delete")) {
                if (!pm.canDelete(pm.getUserName(req), currentPage)) {
                    if (tentative) {
                        return "";
                    }
                    sayNoIcon = "<img src='images/stop.png' border='0' />";
                }
                if (linkIsText) {
                    text = "Delete Page";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:deletePage.do?name=" + currentPage + "' " + popper + ">" + text + sayNoIcon + "</a>" + liner;
            } else if (link.equalsIgnoreCase("$trail")) {
                String retval = "";
                String sep = "";
                Vector v = (Vector) session.getAttribute("pageTrail");
                if (v == null) return "";
                Iterator it = v.iterator();
                String uname = pm.getUserName(req);
                while (it.hasNext()) {
                    String pname = (String) it.next();
                    if (PageManager.solo().pageExists(pname) && pm.userPermLevelForPage(uname, pname) >= PermissionManager.READ) {
                        if (pname.equals(currentPage)) {
                            retval += sep + "<b>" + pname + "</b>";
                        } else {
                            retval += sep + "<a href='viewPage.do?name=" + pname + "'" + popper + ">" + pname + "</a>";
                        }
                        sep = "&gt; ";
                    }
                }
                retval += liner;
                return retval;
            } else if (link.equalsIgnoreCase("$attach")) {
                if (currentPage.length() == 0 || pm.userPermLevelForPage(pm.getUserName(req), currentPage) < PermissionManager.ATTACH || pm.maxStoragePerAttachFor(currentPage) == 0) {
                    if (tentative) {
                        return "";
                    }
                    sayNoIcon = "<img src='images/stop.png' border='0' />";
                }
                if (linkIsText) {
                    text = "Attach file";
                }
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' />";
                }
                return "<a href='http:attach.do?name=" + currentPage + "'" + popper + " >" + text + sayNoIcon + "</a>" + liner;
            } else if (link.toLowerCase().startsWith("$showvar=")) {
                String varn = link.substring(9).trim();
                String showval = (String) vars.get(varn);
                if (showval == null) return ".null.";
                return showval;
            } else if (link.equalsIgnoreCase("$page")) {
                return currentPage;
            } else if (link.equalsIgnoreCase("$user")) {
                String userName = pm.getUserName(req);
                String guestName = pm.getGuestName(req);
                link = "UserProfiles." + userName;
                if (guestName.length() > 0) {
                    text = userName + " (" + guestName + ")";
                } else {
                    text = userName;
                }
            } else if (link.equalsIgnoreCase("$children")) {
                if (pm.userPermLevelForPage(pm.getUserName(req), currentPage) < PermissionManager.EXTEND) {
                    if (tentative) {
                        return "";
                    }
                }
                Vector children = PageManager.solo().listChildren(currentPage, false);
                Iterator it = children.iterator();
                String retval = "<b>Child pages:</b><br />&nbsp;&nbsp;&nbsp;";
                int childCount = 0;
                String sep = "";
                while (it.hasNext()) {
                    String child = (String) it.next();
                    if (pm.userPermLevelForPage(pm.getUserName(req), child) >= PermissionManager.READ) {
                        childCount++;
                        retval += sep + "<a href='viewPage.do?name=" + child + "'>" + child + "</a>";
                        sep = ", ";
                    }
                }
                if (childCount == 0) {
                    return "";
                }
                return retval;
            } else if (link.equalsIgnoreCase("$attachments")) {
                Vector attachments = PageManager.solo().listAttachments(currentPage);
                if (attachments.size() == 0) {
                    return "";
                }
                Iterator it = attachments.iterator();
                String retval = "<b>Attachments:</b><br />&nbsp;&nbsp;&nbsp;";
                String sep = "";
                while (it.hasNext()) {
                    String attachment = (String) it.next();
                    String fullName = currentPage + "/" + attachment;
                    retval += sep;
                    retval += "<a href='viewAttachment.do?name=" + fullName + "'>" + attachment + "</a>";
                    retval += " (" + PageInfoNode.lookUpAttSize(currentPage, attachment) + " bytes)";
                    if (pm.userPermLevelForPage(pm.getUserName(req), currentPage) >= PermissionManager.ATTACH) {
                        retval += "&nbsp;&nbsp;[<a href='deletePage.do?name=" + fullName + "&targetIsAttachment=YES'>" + "<img src='http:images/delete.png' border='0' align='middle'/>Delete" + "</a>&nbsp;]";
                    }
                    sep = "<br clear='all' />&nbsp;&nbsp;&nbsp;";
                }
                return retval;
            } else {
                return link + "?";
            }
        }
        if (link.indexOf(':') >= 0) {
            if (inline) {
                int dot = link.lastIndexOf('.');
                if (dot >= 0) {
                    String ext = link.substring(dot + 1).trim().toLowerCase();
                    if (extIsImage(ext)) {
                        return "<img src='" + link + "' alt='" + text + "'" + sider + popper + ">" + liner;
                    }
                }
            }
            if (textIsImage) {
                text = "<img src='" + text + "' border='0'" + sider + " />";
            }
            return "<a href='" + link + "'" + popper + ">" + text + "&nbsp;<img src='images/out.png' border='0' " + popper + "></a>" + liner;
        } else {
            if (link.trim().equals(".")) {
                link = currentPage;
            }
            if (link.trim().equals("..")) {
                link = "";
            }
            link = crushName(link);
            boolean isAttachment = link.indexOf('/') >= 0;
            boolean pageFound = isAttachment ? AttachmentManager.solo().attachmentExists(link) : PageManager.solo().pageExists(link);
            boolean parentFound = !link.startsWith(".") && PageManager.solo().pageExists(pm.parentOf(link));
            int userPerm = pm.userPermLevelForPage(user, link);
            if (link.startsWith("Null")) {
                if (link.equals("NullPageOne")) {
                    userPerm = PermissionManager.EXTEND;
                    pageFound = false;
                } else if (link.equals("NullPageTwo")) {
                    userPerm = PermissionManager.NO_ACCESS;
                    pageFound = true;
                }
            }
            if (pageFound && userPerm >= PermissionManager.READ) {
                if (isAttachment) {
                    if (inline) {
                        int dot = link.lastIndexOf('.');
                        if (dot >= 0) {
                            String ext = link.substring(dot + 1);
                            if (extIsRawText(ext)) {
                                try {
                                    return "<pre>\n" + AttachmentManager.solo().getAttachment(link) + "</pre>\n";
                                } catch (PageManagerException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (extIsImage(ext)) {
                                return "<img src='viewAttachment.do?name=" + link + "' alt='" + text + "'" + sider + popper + ">" + liner;
                            }
                        }
                    }
                    if (textIsImage) {
                        text = "<img src='" + text + "' border='0' " + sider + "/>";
                    }
                    return "<a href='viewAttachment.do?name=" + link + "'" + popper + ">" + text + "</a>" + liner;
                } else {
                    if (inline) {
                        HashMap ancestors = (HashMap) req.getAttribute("ancestors");
                        if (ancestors == null) {
                            ancestors = new HashMap();
                            req.setAttribute("ancestors", ancestors);
                        }
                        if (ancestors.containsKey(link)) {
                            return "<font color='red'>Recursive Inline Link</font> to " + link + "\n";
                        }
                        ancestors.put(link, link);
                        try {
                            String retval = (new StikiWebTranslator()).translateText(PageManager.solo().getPage(link), currentPage, link, req);
                            ancestors.remove(link);
                            return retval;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ancestors.remove(link);
                    }
                    if (textIsImage) {
                        text = "<img src='" + text + "' border='0' " + sider + "/>";
                    }
                    if (linkparms != null) {
                        Iterator it = linkparms.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            link += "&var_" + entry.getKey() + "=" + entry.getValue();
                        }
                    }
                    return "<a href='viewPage.do?name=" + link + "'" + popper + ">" + text + "</a>" + liner;
                }
            } else if (!isAttachment && userPerm >= PermissionManager.EXTEND && !pageFound && parentFound) {
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' " + sider + "/>";
                }
                if (linkparms != null) {
                    Iterator it = linkparms.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        link += "&var_" + entry.getKey() + "=" + entry.getValue();
                    }
                }
                return "<a href='createPage.do?name=" + link + "'" + popper + ">" + text + "&nbsp;<img src='images/add.png' border='0' /></a>" + liner;
            } else if (!tentative) {
                if (textIsImage) {
                    text = "<img src='" + text + "' border='0' " + sider + "/>";
                }
                return "<a href='noPage.jsp?name=" + link + "'" + popper + ">" + text + "<img src='images/stop.png' border='0' /></a>" + liner;
            }
        }
        return "";
    }

    public HashMap parseLinkParms(String link) {
        int parenloc = link.indexOf('(');
        if (parenloc < 0) return null;
        String body = link.substring(parenloc + 1).trim();
        if (body.endsWith(")")) {
            body = body.substring(0, body.length() - 1);
        }
        HashMap retval = new HashMap();
        StringTokenizer tok = new StringTokenizer(body, ",");
        while (tok.hasMoreTokens()) {
            String segment = tok.nextToken().trim();
            int equloc = segment.indexOf('=');
            String var = segment;
            String val = "true";
            if (equloc >= 0) {
                var = segment.substring(0, equloc).trim();
                val = segment.substring(equloc + 1).trim();
            }
            try {
                var = URLEncoder.encode(var, "UTF-8");
                val = URLEncoder.encode(val, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (var.length() > 0) retval.put(var, val);
        }
        return (retval.size() == 0) ? null : retval;
    }

    public boolean extIsRawText(String ext) {
        return ("txt".equals(ext) || "asc".equals(ext));
    }

    public boolean extIsImage(String ext) {
        return ("gif".equals(ext) || "jpg".equals(ext) || "bmp".equals(ext) || "png".equals(ext) || "jpeg".equals(ext));
    }

    /**
	 * Converts a string from the markup language to a Wiki Page name.  
	 * Spaces are removed and the first character of each word is capitalized.
	 * If the string contains a colon it is treated as a URL and these 
	 * transformations are not applied.  If it is not a URL and it contains
	 * a slash character, anything after the slash is treated as an attachment
	 * name and also not translated.
	 *
	 * Note that two translations are applied to a markup string for a page
	 * to get to the actual file name.  First, this routine is called to create
	 * the internal wiki page name (that resembles a java package name).  
	 * The second step (see convertName below) translates that to a file name
	 * and path for the actual disk file reference.
	 * 
	 * @param in - String in markup that represents a page
	 * @return - String containing canonical wiki page name.
	 */
    public static String crushName(String in) {
        if (in.indexOf(':') > 0) {
            return fixQuotes(in);
        } else {
            StringBuffer sb = new StringBuffer();
            int len = in.length();
            boolean foundSlash = false;
            boolean forceCaps = true;
            for (int i = 0; i < len; i++) {
                char c = in.charAt(i);
                if (Character.isJavaIdentifierPart(c)) {
                    if (forceCaps && !foundSlash) {
                        sb.append(Character.toUpperCase(c));
                        forceCaps = false;
                    } else {
                        sb.append(c);
                    }
                } else if (Character.isWhitespace(c)) {
                    if (foundSlash) {
                        sb.append(' ');
                    }
                    forceCaps = true;
                } else if (c == '.') {
                    sb.append(c);
                    forceCaps = true;
                } else if (c == '/') {
                    sb.append(c);
                    foundSlash = true;
                }
            }
            return sb.toString();
        }
    }

    public static String fixQuotes(String in) {
        StringBuffer sb = new StringBuffer();
        int len = in.length();
        for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
            if (c == '\'') {
                sb.append('\'');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
	 * Converts a name in the general form [[ancestor.]*parent.]page[/attachment]
	 * to its file path under WikiBase.  The new string will be in the general form -
	 * [[Ancestor/]*Parent/]Page[/attachment | .wiki ]. 
	 * 
	 * Or to put it another way, wiki pages are generally structured like java packages
	 * where the name is a series of identifiers separated by dots to represent the 
	 * nesting of the directories.  (Note: I am referring to the typical implementation 
	 * of Java packages as nested directories -- the definition of packages allows for
	 * other storage schemes.)  The dots separating the nesting levels are translated 
	 * to slashes to form a file path.  Punctuation (except as noted here) is discarded. 
	 * 
	 *  The page name can be followed by a slash  and the name of a page attachment.  
	 * Attachments names are the page name followed by a dot and the name of the 
	 * attachment.
	 * 
	 * The conversion will ensure that all words in the Wiki page name are capitalized
	 * and will remove all spaces from the page name.  The capitalization of the 
	 * attachment name is not changed.
	 * 
	 * Examples:
	 * "main page/ hello.html"  >>  "MainPage/hello.html"
	 * "main . Page 123.footnotes" >> Main/Page123/Footnotes"
	 * "MY(cool)page.stuff" >> "MyCoolPage.Stuff"
	 * foo. bar. moo / biff.pow.zip" >> "Foo/Bar/Moo/biff.pow.zip"
	 * 
	 * The last example above can be read as "an attachment named biff.pow.zip attached
	 *  to a wiki page named Moo which, in turn is a child of a page named Bar which
	 * is a child of a page named Foo."
	 * @param in - String containing the name to convert
	 * @return - the converted name as a file path string.
	 */
    public static String convertName(String in) {
        StringBuffer sb = new StringBuffer();
        int len = in.length();
        boolean slashFound = false;
        boolean forceCap = true;
        for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                if (forceCap && !slashFound) {
                    sb.append(Character.toUpperCase(c));
                    forceCap = false;
                } else {
                    sb.append(c);
                }
            } else if (c == '/') {
                sb.append('/');
                slashFound = true;
            } else if (c == '.') {
                if (slashFound) {
                    sb.append('.');
                } else {
                    sb.append('/');
                }
                forceCap = true;
            } else if (c == ' ') {
                if (slashFound) {
                    sb.append(' ');
                } else {
                    forceCap = true;
                }
            } else {
                forceCap = true;
            }
        }
        if (!slashFound) {
            sb.append(".wiki");
        }
        return sb.toString();
    }

    protected String sanitizeParms(String in) {
        return in;
    }

    protected String sanitizeText(String in) {
        StringBuffer sb = new StringBuffer();
        int len = in.length();
        for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
            switch(c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    protected int findUnescaped(char c, String s, int start) {
        boolean escaped = false;
        int len = s.length();
        for (int i = start; i < len; i++) {
            char sc = s.charAt(i);
            if (sc == '\\') {
                escaped = !escaped;
            } else if (sc == c && !escaped) {
                return i;
            } else {
                escaped = false;
            }
        }
        return -1;
    }

    public static String capitalize(String in) {
        if (in == null) return in;
        if (in.length() < 2) return in.toUpperCase();
        return in.substring(0, 1).toUpperCase() + in.substring(1);
    }
}
