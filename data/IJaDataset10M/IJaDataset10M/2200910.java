package com.timk.goserver.client.sgf;

import java.util.*;
import com.timk.goserver.client.compatibility.Util;

/**
 * Parses SGF files.
 * @author TKington
 *
 */
public class SGFParser {

    /**
	 * Parses the SGF Tags for a node, and stores them in the SGFNode object.
	 * @param s the SGF string for the node
	 * @param node the SGFNode the tags should be added to
	 * @throws SGFParseException if the SGF is illegal
	 */
    static void parseTags(String tags, SGFNode node) throws SGFParseException {
        String str = tags;
        while (true) {
            str = str.trim();
            if (str.length() == 0) {
                break;
            }
            int index = 0;
            while (index < str.length() && Character.isLetter(str.charAt(index))) {
                index++;
            }
            if (index == 0) {
                throw new SGFParseException("Illegal token: " + str);
            }
            String tag = str.substring(0, index);
            ArrayList propVals = new ArrayList();
            while (index < str.length() && str.charAt(index) == '[') {
                index++;
                StringBuffer val = new StringBuffer();
                int bdepth = 1;
                while (true) {
                    char chr = str.charAt(index);
                    if (chr == '[') {
                        bdepth++;
                    } else if (chr == ']') {
                        bdepth--;
                        if (bdepth == 0) {
                            break;
                        }
                    }
                    val.append(chr);
                    index++;
                }
                propVals.add(val.toString());
                if (index >= str.length() || str.charAt(index) != ']') {
                    throw new SGFParseException(str);
                }
                index++;
                while (index < str.length() && Util.isWhitespace(str.charAt(index))) {
                    index++;
                }
            }
            node.addTag(tag, propVals);
            str = str.substring(index);
        }
    }

    static StringPair grabToken(String sgf) throws SGFParseException {
        int index;
        String str = sgf.trim();
        String tok = "";
        int start = 0, end = 0;
        if (str.charAt(0) == '(') {
            int dpth = 1;
            start = 1;
            for (index = 1; index < str.length(); index++) {
                if (str.charAt(index) == '[') {
                    int bdepth = 1;
                    index++;
                    while (true) {
                        if (str.charAt(index) == '[') {
                            bdepth++;
                        } else if (str.charAt(index) == ']') {
                            bdepth--;
                            if (bdepth == 0) {
                                break;
                            }
                        }
                        index++;
                    }
                }
                if (str.charAt(index) == '(') {
                    dpth++;
                }
                if (str.charAt(index) == ')') {
                    dpth--;
                }
                if (dpth == 0) {
                    break;
                }
            }
            if (dpth != 0) {
                throw new SGFParseException("Illegal token(bad depth):" + " " + str);
            }
            end = index;
        } else if (str.charAt(0) == ';') {
            int dpth = 0;
            start = 1;
            for (index = 1; index < str.length(); index++) {
                if (str.charAt(index) == '[') {
                    int bdepth = 1;
                    index++;
                    while (true) {
                        if (str.charAt(index) == '[') {
                            bdepth++;
                        } else if (str.charAt(index) == ']') {
                            bdepth--;
                            if (bdepth == 0) {
                                break;
                            }
                        }
                        index++;
                    }
                }
                if ((str.charAt(index) == '(' || str.charAt(index) == ';')) {
                    break;
                }
            }
            if (dpth != 0) {
                throw new SGFParseException("Illegal ; token" + " " + str);
            }
            end = index;
        } else {
            throw new SGFParseException("Illegal start of token: " + " " + str.charAt(0) + " " + "in" + " " + str);
        }
        tok = str.substring(start, end);
        tok = tok.trim();
        str = str.substring(end, str.length());
        str = str.trim();
        if (str.length() > 0 && str.charAt(0) == ')') {
            str = str.substring(1);
        }
        return new StringPair(tok, str);
    }

    /**
     * Parses an SGF string representing a single node, and creates an SGFNode
     * representing that string.
     * @param sgf the SGF string
     * @param parent the parent of the new node
     * @return the new SGFNode
     * @throws SGFParseException
     */
    public static SGFNode parseNode(String sgf, SGFNode parent) throws SGFParseException {
        StringPair tok = grabToken(sgf);
        SGFNode node = new SGFNode(tok.first, parent);
        if (tok.second.length() > 0) {
            if (tok.second.charAt(0) == ';') {
                node.addChild(parseNode(tok.second, node));
            } else {
                while (tok.second.length() > 0) {
                    tok = grabToken(tok.second);
                    node.addChild(parseNode(tok.first, node));
                }
            }
        }
        return node;
    }

    /**
     * Parses an SGF file, and returns a list of SGFNodes that represent the game
     * trees found in the file.
     * @param sgf the SGF file
     * @return a list of SGFNodes
     * @throws SGFParseException
     */
    public static List parse(String sgf) throws SGFParseException {
        List ret = new ArrayList();
        StringPair tok = grabToken(sgf);
        while (true) {
            ret.add(parseNode(tok.first, null));
            if (tok.second.length() == 0) {
                break;
            }
            tok = grabToken(tok.second);
        }
        return ret;
    }

    /**
     * Class to hold a pair of strings. Used to hold the text of the current
     * node, and the rest of the SGF file.
     */
    static class StringPair {

        /** First string */
        public String first;

        /** Second string */
        public String second;

        /**
         * Creates a StringPair.
         * @param first the first string
         * @param second the second string
         */
        StringPair(String first, String second) {
            this.first = first;
            this.second = second;
        }
    }
}
