package sto.orz.json;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrMatcher;
import sto.orz.lang.text.StrHoldTokenTokenizer;

public class JSONStringFormater {

    private final String KEY_STRING = ":{}[],";

    private final String BR = "\r\n";

    private final String INDENT_STRING = " ";

    private final Integer INDENT_WIDTH = 4;

    private Integer indent = 0;

    private String input = "";

    private String output = "";

    public JSONStringFormater(String input) {
        this.input = input;
    }

    private int tokenPos;

    private String[] tokens;

    public String format(String jsonString) {
        StrMatcher keyMatcher;
        StrHoldTokenTokenizer jsonTokener;
        keyMatcher = StrMatcher.charSetMatcher(KEY_STRING.toCharArray());
        StrMatcher qmatcher = StrMatcher.charMatcher('"');
        jsonTokener = new StrHoldTokenTokenizer(jsonString, keyMatcher);
        jsonTokener.setTrimmerMatcher(StrMatcher.trimMatcher());
        String formatString = "";
        tokens = jsonTokener.getTokenArray();
        for (tokenPos = 0; tokenPos < tokens.length; tokenPos++) {
            formatString += addToken(tokens[tokenPos], keyMatcher);
        }
        return formatString;
    }

    @Override
    public String toString() {
        output = format(input);
        return output;
    }

    private boolean isKey(String token, StrMatcher keyMatcher) {
        if (token == null) return false;
        if (keyMatcher.isMatch(token.toCharArray(), 0, 0, token.length()) > 0) return true;
        return false;
    }

    private String getPadding() {
        String padding = StringUtils.repeat(INDENT_STRING, indent * INDENT_WIDTH);
        return padding;
    }

    private String getPreviousToken() {
        if (tokenPos > 0) return tokens[tokenPos - 1];
        return null;
    }

    private String getNextToken() {
        if (tokenPos + 1 < tokens.length) return tokens[tokenPos + 1];
        return null;
    }

    public String addToken(String token, StrMatcher keyMatcher) {
        String out = "";
        String previousToken = getPreviousToken();
        String nextToken = getNextToken();
        boolean beforeKey = isKey(nextToken, keyMatcher);
        if (keyMatcher.isMatch(token.toCharArray(), 0, 0, token.length()) == 0) {
            out = "";
            if (previousToken.equals(":")) out = token; else out = getPadding() + token;
            if (beforeKey && !nextToken.equals(",") && !nextToken.endsWith(":")) out += BR;
            return out;
        }
        if (token.equals("{") || token.equals("[")) {
            out = getPadding() + token + BR;
            indent++;
            return out;
        }
        if (token.equals("}") || token.equals("]")) {
            indent--;
            out = getPadding() + token;
            if (beforeKey && !nextToken.equals(",")) out += BR;
            return out;
        }
        if (token.equals(",")) {
            out = token + BR;
            return out;
        }
        if (token.equals(":")) {
            out = token;
            if (beforeKey && !nextToken.equals(",")) out += BR;
            if (nextToken.equals(",")) out += "\"\"";
            return out;
        }
        return "";
    }
}
