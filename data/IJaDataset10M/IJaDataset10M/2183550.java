package org.gocha.text.parser.lex;

import org.gocha.text.parser.Token;
import org.gocha.text.parser.TokenParser;

/**
 * @author gocha
 */
public class TextConstParser implements TokenParser {

    protected String id = null;

    public TextConstParser() {
    }

    public TextConstParser(String id) {
        this.id = id;
    }

    @Override
    public Token parse(String source, int offset) {
        int i = offset - 1;
        int state = 0;
        char c = 0;
        int slen = source.length();
        StringBuilder txt = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        while (true) {
            i++;
            if (!(i < slen)) break;
            c = source.charAt(i);
            switch(state) {
                case 0:
                    switch(c) {
                        case '"':
                            state = 1;
                            break;
                        default:
                            state = -1;
                    }
                    break;
                case 1:
                    switch(c) {
                        case '\\':
                            state = 2;
                            break;
                        case '"':
                            state = 99;
                            break;
                        default:
                            txt.append(c);
                    }
                    break;
                case 2:
                    switch(c) {
                        case '\\':
                            state = 1;
                            txt.append("\\");
                            break;
                        case '"':
                            state = 1;
                            txt.append('"');
                            break;
                        case '\'':
                            state = 1;
                            txt.append('\'');
                            break;
                        case 'n':
                            state = 1;
                            txt.append("\n");
                            break;
                        case 'r':
                            state = 1;
                            txt.append("\r");
                            break;
                        case 'b':
                            state = 1;
                            txt.append("\b");
                            break;
                        case 'f':
                            state = 1;
                            txt.append("\f");
                            break;
                        case 't':
                            state = 1;
                            txt.append("\t");
                            break;
                        case 'u':
                            state = 3;
                            break;
                        default:
                            state = -1;
                            break;
                    }
                    break;
                case 3:
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            hex.setLength(0);
                            hex.append(c);
                            state = 4;
                            break;
                        default:
                            state = -1;
                            break;
                    }
                    break;
                case 4:
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            hex.append(c);
                            state = 5;
                            break;
                        default:
                            state = -1;
                            break;
                    }
                    break;
                case 5:
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            hex.append(c);
                            state = 6;
                            break;
                        default:
                            state = -1;
                            break;
                    }
                    break;
                case 6:
                    switch(c) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            hex.append(c);
                            int hexVal = Integer.parseInt(hex.toString(), 16);
                            char[] hexChars = Character.toChars(hexVal);
                            for (char hc : hexChars) txt.append(hc);
                            state = 1;
                            break;
                        default:
                            state = -1;
                            break;
                    }
                    break;
            }
            if (state == -1) return null;
            if (state == 99) break;
        }
        if (state != 99) return null;
        int len = (i - offset) + 1;
        TextConst tct = new TextConst();
        tct.setSource(source);
        tct.setBegin(offset);
        tct.setLength(len);
        tct.setDecodedText(txt.toString());
        if (id != null) tct.setId(id);
        return tct;
    }
}
