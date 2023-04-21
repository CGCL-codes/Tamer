package org.codehaus.groovy.grails.web.pages;

/**
 * NOTE: Based on work done by on the GSP standalone project (https://gsp.dev.java.net/)
 *
 * Lexer for GroovyPagesServlet.
 *
 * @author Troy Heninger
 * @author Graeme Rocher
 *
 * Date: Jan 10, 2004
 *
 */
class Scan implements Tokens {

    private String text;

    private int end1, begin1, end2, begin2, state = HTML, len, level;

    private boolean str1, str2;

    private String lastNamespace;

    private int exprBracketCount = 0;

    Scan(String text) {
        Strip strip = new Strip(text);
        strip.strip(0);
        this.text = strip.toString();
        len = this.text.length();
        this.lastNamespace = null;
    }

    private int found(int newState, int skip) {
        begin2 = begin1;
        end2 = --end1;
        begin1 = end1 += skip;
        int lastState = state;
        state = newState;
        return lastState;
    }

    private int foundStartOrEndTag(int newState, int skip, String namespace) {
        begin2 = begin1;
        end2 = --end1;
        begin1 = end1 += skip;
        int lastState = state;
        state = newState;
        lastNamespace = namespace;
        return lastState;
    }

    String getToken() {
        return text.substring(begin2, end2);
    }

    String getNamespace() {
        return lastNamespace;
    }

    int nextToken() {
        for (; ; ) {
            int left = len - end1;
            if (left == 0) {
                end1++;
                return found(EOF, 0);
            }
            char c = text.charAt(end1++);
            char c1 = left > 1 ? text.charAt(end1) : 0;
            char c2 = left > 2 ? text.charAt(end1 + 1) : 0;
            char c3 = left > 3 ? text.charAt(end1 + 2) : 0;
            StringBuffer chars = new StringBuffer().append(c).append(c1).append(c2);
            String startTag = chars.toString();
            String endTag = chars.append(c3).toString();
            if (str1) {
                if (c == '\\') end1++; else if (c == '\'') str1 = false;
                continue;
            } else if (str2) {
                if (c == '\\') end1++; else if (c == '"') str2 = false;
                continue;
            } else if (level > 0 && (c == ')' || c == '}' || c == ']')) {
                level--;
                continue;
            }
            switch(state) {
                case HTML:
                    if (c == '<' && left > 3) {
                        if (c1 == '%') {
                            if (c2 == '=') {
                                return found(JEXPR, 3);
                            } else if (c2 == '@') {
                                return found(JDIRECT, 3);
                            } else if (c2 == '!') {
                                return found(JDECLAR, 3);
                            } else if (c2 == '-' && left > 3 && text.charAt(end1 + 2) == '-') {
                                if (skipJComment()) continue;
                            }
                            return found(JSCRIPT, 2);
                        } else {
                            boolean bStartTag = true;
                            int fromIndex = end1;
                            if (c1 == '/') {
                                bStartTag = false;
                                fromIndex = end1 + 1;
                            }
                            int foundColonIdx = text.indexOf(":", fromIndex);
                            if (foundColonIdx > -1) {
                                String tagNameSpace = text.substring(fromIndex, foundColonIdx);
                                if (tagNameSpace.matches("^\\p{Alpha}\\w*$")) {
                                    if (bStartTag) {
                                        return foundStartOrEndTag(GSTART_TAG, tagNameSpace.length() + 2, tagNameSpace);
                                    } else {
                                        return foundStartOrEndTag(GEND_TAG, tagNameSpace.length() + 3, tagNameSpace);
                                    }
                                }
                            }
                        }
                    } else if (c == '$' && c1 == '{') {
                        return found(GEXPR, 2);
                    } else if (c == '%' && c1 == '{') {
                        if (c2 == '-' && left > 3 && text.charAt(end1 + 2) == '-') {
                            if (skipGComment()) continue;
                        }
                        return found(GSCRIPT, 2);
                    } else if (c == '!' && c1 == '{') {
                        return found(GDECLAR, 2);
                    } else if (c == '@' && c1 == '{') {
                        return found(GDIRECT, 2);
                    }
                    break;
                case JEXPR:
                case JSCRIPT:
                case JDIRECT:
                case JDECLAR:
                    if (c == '%' && c1 == '>') {
                        return found(HTML, 2);
                    }
                    break;
                case GSTART_TAG:
                    if (c == '$' && c1 == '{') {
                        return found(GTAG_EXPR, 2);
                    }
                    if (c == '>') {
                        return found(HTML, 1);
                    } else if (c == '/' && c1 == '>') {
                        return found(GEND_TAG, 1);
                    }
                    break;
                case GEND_TAG:
                    if (c == '>') {
                        return found(HTML, 1);
                    }
                    break;
                case GTAG_EXPR:
                    if (c == '{') exprBracketCount++; else if (c == '}') {
                        if (exprBracketCount > 0) {
                            exprBracketCount--;
                        } else {
                            return found(GSTART_TAG, 1);
                        }
                    }
                    break;
                case GEXPR:
                case GDIRECT:
                    if (c == '}' && !str1 && !str2 && level == 0) {
                        return found(HTML, 1);
                    }
                    break;
                case GSCRIPT:
                    if (c == '}' && c1 == '%' && !str1 && !str2 && level == 0) {
                        return found(HTML, 2);
                    }
                    break;
                case GDECLAR:
                    if (c == '}' && (c1 == '!' || c1 == '%') && !str1 && !str2 && level == 0) {
                        return found(HTML, 2);
                    }
                    break;
            }
        }
    }

    private boolean skipComment(char c3, char c4) {
        int ix = end1 + 3;
        for (int ixz = len - 4; ; ix++) {
            if (ix >= ixz) return false;
            if (text.charAt(ix) == '-' && text.charAt(ix + 1) == '-' && text.charAt(ix + 2) == c3 && text.charAt(ix + 3) == c4) break;
        }
        text = text.substring(0, --end1) + text.substring(ix + 4);
        len = text.length();
        return true;
    }

    private boolean skipGComment() {
        return skipComment('}', '%');
    }

    private boolean skipJComment() {
        return skipComment('%', '>');
    }

    void reset() {
        end1 = begin1 = end2 = begin2 = level = 0;
        state = HTML;
        lastNamespace = null;
    }
}
