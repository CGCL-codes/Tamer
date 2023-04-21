package org.mc4j.console.swing.editor.xml;

import java.util.regex.Matcher;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Nov 16, 2004
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class RETokenizer {

    protected StyleTokens types;

    protected Matcher matcher;

    public RETokenizer(StyleTokens types, String text) {
        this.types = types;
        matcher = types.getMatcher(text);
    }

    protected Token getToken(int pos) {
        int count = types.getTokens().length;
        for (int i = 0; i < types.getTokens().length; i++) {
            StyleTokens.StyleToken styleToken = types.getTokens()[i];
            String token = matcher.group(i + 1);
            if (token != null) {
                String type = styleToken.name;
                return new Token(token, type, pos);
            }
        }
        return null;
    }

    public Token nextToken() {
        if (matcher.find()) {
            return getToken(matcher.start());
        }
        return null;
    }

    public static class Token {

        public String token;

        public String type;

        protected int position;

        public Token(String token, String type, int getPosition) {
            this.token = token;
            this.type = type;
            this.position = getPosition;
        }

        public String getText() {
            return token;
        }

        public String getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        public String toString() {
            return type + "(" + token + ", " + position + ')';
        }
    }
}
