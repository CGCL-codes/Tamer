package com.dustedpixels.dcel.parser;

import java_cup.runtime.Symbol;

class Yylex implements java_cup.runtime.Scanner {

    private final int YY_BUFFER_SIZE = 512;

    private final int YY_F = -1;

    private final int YY_NO_STATE = -1;

    private final int YY_NOT_ACCEPT = 0;

    private final int YY_START = 1;

    private final int YY_END = 2;

    private final int YY_NO_ANCHOR = 4;

    private final int YY_BOL = 128;

    private final int YY_EOF = 129;

    private java.io.BufferedReader yy_reader;

    private int yy_buffer_index;

    private int yy_buffer_read;

    private int yy_buffer_start;

    private int yy_buffer_end;

    private char yy_buffer[];

    private boolean yy_at_bol;

    private int yy_lexical_state;

    Yylex(java.io.Reader reader) {
        this();
        if (null == reader) {
            throw (new Error("Error: Bad input stream initializer."));
        }
        yy_reader = new java.io.BufferedReader(reader);
    }

    Yylex(java.io.InputStream instream) {
        this();
        if (null == instream) {
            throw (new Error("Error: Bad input stream initializer."));
        }
        yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
    }

    private Yylex() {
        yy_buffer = new char[YY_BUFFER_SIZE];
        yy_buffer_read = 0;
        yy_buffer_index = 0;
        yy_buffer_start = 0;
        yy_buffer_end = 0;
        yy_at_bol = true;
        yy_lexical_state = YYINITIAL;
    }

    private boolean yy_eof_done = false;

    private final int YYINITIAL = 0;

    private final int yy_state_dtrans[] = { 0 };

    private void yybegin(int state) {
        yy_lexical_state = state;
    }

    private int yy_advance() throws java.io.IOException {
        int next_read;
        int i;
        int j;
        if (yy_buffer_index < yy_buffer_read) {
            return yy_buffer[yy_buffer_index++];
        }
        if (0 != yy_buffer_start) {
            i = yy_buffer_start;
            j = 0;
            while (i < yy_buffer_read) {
                yy_buffer[j] = yy_buffer[i];
                ++i;
                ++j;
            }
            yy_buffer_end = yy_buffer_end - yy_buffer_start;
            yy_buffer_start = 0;
            yy_buffer_read = j;
            yy_buffer_index = j;
            next_read = yy_reader.read(yy_buffer, yy_buffer_read, yy_buffer.length - yy_buffer_read);
            if (-1 == next_read) {
                return YY_EOF;
            }
            yy_buffer_read = yy_buffer_read + next_read;
        }
        while (yy_buffer_index >= yy_buffer_read) {
            if (yy_buffer_index >= yy_buffer.length) {
                yy_buffer = yy_double(yy_buffer);
            }
            next_read = yy_reader.read(yy_buffer, yy_buffer_read, yy_buffer.length - yy_buffer_read);
            if (-1 == next_read) {
                return YY_EOF;
            }
            yy_buffer_read = yy_buffer_read + next_read;
        }
        return yy_buffer[yy_buffer_index++];
    }

    private void yy_move_end() {
        if (yy_buffer_end > yy_buffer_start && '\n' == yy_buffer[yy_buffer_end - 1]) yy_buffer_end--;
        if (yy_buffer_end > yy_buffer_start && '\r' == yy_buffer[yy_buffer_end - 1]) yy_buffer_end--;
    }

    private boolean yy_last_was_cr = false;

    private void yy_mark_start() {
        yy_buffer_start = yy_buffer_index;
    }

    private void yy_mark_end() {
        yy_buffer_end = yy_buffer_index;
    }

    private void yy_to_mark() {
        yy_buffer_index = yy_buffer_end;
        yy_at_bol = (yy_buffer_end > yy_buffer_start) && ('\r' == yy_buffer[yy_buffer_end - 1] || '\n' == yy_buffer[yy_buffer_end - 1] || 2028 == yy_buffer[yy_buffer_end - 1] || 2029 == yy_buffer[yy_buffer_end - 1]);
    }

    private java.lang.String yytext() {
        return (new java.lang.String(yy_buffer, yy_buffer_start, yy_buffer_end - yy_buffer_start));
    }

    private int yylength() {
        return yy_buffer_end - yy_buffer_start;
    }

    private char[] yy_double(char buf[]) {
        int i;
        char newbuf[];
        newbuf = new char[2 * buf.length];
        for (i = 0; i < buf.length; ++i) {
            newbuf[i] = buf[i];
        }
        return newbuf;
    }

    private final int YY_E_INTERNAL = 0;

    private final int YY_E_MATCH = 1;

    private java.lang.String yy_error_string[] = { "Error: Internal error.\n", "Error: Unmatched input.\n" };

    private void yy_error(int code, boolean fatal) {
        java.lang.System.out.print(yy_error_string[code]);
        java.lang.System.out.flush();
        if (fatal) {
            throw new Error("Fatal Error.\n");
        }
    }

    private int[][] unpackFromString(int size1, int size2, String st) {
        int colonIndex = -1;
        String lengthString;
        int sequenceLength = 0;
        int sequenceInteger = 0;
        int commaIndex;
        String workString;
        int res[][] = new int[size1][size2];
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                if (sequenceLength != 0) {
                    res[i][j] = sequenceInteger;
                    sequenceLength--;
                    continue;
                }
                commaIndex = st.indexOf(',');
                workString = (commaIndex == -1) ? st : st.substring(0, commaIndex);
                st = st.substring(commaIndex + 1);
                colonIndex = workString.indexOf(':');
                if (colonIndex == -1) {
                    res[i][j] = Integer.parseInt(workString);
                    continue;
                }
                lengthString = workString.substring(colonIndex + 1);
                sequenceLength = Integer.parseInt(lengthString);
                workString = workString.substring(0, colonIndex);
                sequenceInteger = Integer.parseInt(workString);
                res[i][j] = sequenceInteger;
                sequenceLength--;
            }
        }
        return res;
    }

    private int yy_acpt[] = { YY_NOT_ACCEPT, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR, YY_NO_ANCHOR };

    private int yy_cmap[] = unpackFromString(1, 130, "36:9,35:2,36,35:2,36:18,35,31,36:4,22,36,26,27,36,18,36,19,32,36,34:10,36,1" + "7,20,30,21,36:2,33:26,36:3,24,33,36,2,33,3,33,6,12,5,33,9,33,4,15,14,8,11,1" + ",33,13,16,10,7,33:5,28,23,29,25,36,0:2")[0];

    private int yy_rmap[] = unpackFromString(1, 71, "0,1,2,1:3,3,1:8,4,1,5,1:5,6:8,7,8,9,10,11,1,12,13,14,15,16,17,18,19,20,21,2" + "2,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,6,38,39,40,41,42,43,44")[0];

    private int yy_nxt[][] = unpackFromString(45, 37, "1,2,63,65,63:3,66,63:2,67,63:4,68,69,3,4,5,6,32,7,8,9,10,11,12,13,14,15,34," + "16,63,17,18,36,-1:38,63,70,63:8,42,63:5,-1:16,63:2,-1:22,19,-1:46,21,-1:40," + "17,-1:3,63:16,-1:16,63:2,-1:3,63:9,23,63:6,-1:16,63:2,-1:23,20,-1:16,63:9,2" + "4,63:6,-1:16,63:2,-1:32,22,-1:7,63:2,25,63:13,-1:16,63:2,-1:3,63:4,26,63:11" + ",-1:16,63:2,-1:3,63:9,27,63:6,-1:16,63:2,-1:3,63:5,28,63:10,-1:16,63:2,-1:3" + ",63:12,29,63:3,-1:16,63:2,-1:3,63:9,30,63:6,-1:16,63:2,-1:3,63:12,31,63:3,-" + "1:16,63:2,-1:3,63:7,49,63:5,50,63:2,-1:16,63:2,-1:3,63:8,33,63:7,-1:16,63:2" + ",-1:3,63:8,64,63:7,-1:16,63:2,-1:3,63:4,51,63:11,-1:16,63:2,-1:3,63:12,52,6" + "3:3,-1:16,63:2,-1:3,63:3,53,63:12,-1:16,63:2,-1:3,63:11,54,63:4,-1:16,63:2," + "-1:3,55,63:15,-1:16,63:2,-1:3,63:8,35,63:7,-1:16,63:2,-1:3,63:8,57,63:7,-1:" + "16,63:2,-1:3,63,58,63:14,-1:16,63:2,-1:3,63:8,37,63:7,-1:16,63:2,-1:3,63:10" + ",59,63:5,-1:16,63:2,-1:3,63:4,60,63:11,-1:16,63:2,-1:3,38,63:15,-1:16,63:2," + "-1:3,63:4,39,63:11,-1:16,63:2,-1:3,63:7,61,63:8,-1:16,63:2,-1:3,63:5,40,63:" + "10,-1:16,63:2,-1:3,63:5,62,63:10,-1:16,63:2,-1:3,63:7,41,63:8,-1:16,63:2,-1" + ":3,63:4,56,63:11,-1:16,63:2,-1:3,63:10,43,63:5,-1:16,63:2,-1:3,63:7,44,63:8" + ",-1:16,63:2,-1:3,63:12,45,63:3,-1:16,63:2,-1:3,63:10,46,63:5,-1:16,63:2,-1:" + "3,63:2,47,63:13,-1:16,63:2,-1:3,63:2,48,63:13,-1:16,63:2,-1:2");

    public java_cup.runtime.Symbol next_token() throws java.io.IOException {
        int yy_lookahead;
        int yy_anchor = YY_NO_ANCHOR;
        int yy_state = yy_state_dtrans[yy_lexical_state];
        int yy_next_state = YY_NO_STATE;
        int yy_last_accept_state = YY_NO_STATE;
        boolean yy_initial = true;
        int yy_this_accept;
        yy_mark_start();
        yy_this_accept = yy_acpt[yy_state];
        if (YY_NOT_ACCEPT != yy_this_accept) {
            yy_last_accept_state = yy_state;
            yy_mark_end();
        }
        while (true) {
            if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL; else yy_lookahead = yy_advance();
            yy_next_state = YY_F;
            yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
            if (YY_EOF == yy_lookahead && true == yy_initial) {
                return null;
            }
            if (YY_F != yy_next_state) {
                yy_state = yy_next_state;
                yy_initial = false;
                yy_this_accept = yy_acpt[yy_state];
                if (YY_NOT_ACCEPT != yy_this_accept) {
                    yy_last_accept_state = yy_state;
                    yy_mark_end();
                }
            } else {
                if (YY_NO_STATE == yy_last_accept_state) {
                    throw (new Error("Lexical Error: Unmatched Input."));
                } else {
                    yy_anchor = yy_acpt[yy_last_accept_state];
                    if (0 != (YY_END & yy_anchor)) {
                        yy_move_end();
                    }
                    yy_to_mark();
                    switch(yy_last_accept_state) {
                        case 1:
                        case -2:
                            break;
                        case 2:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -3:
                            break;
                        case 3:
                            {
                                return new Symbol(sym.SEMI);
                            }
                        case -4:
                            break;
                        case 4:
                            {
                                return new Symbol(sym.PLUS);
                            }
                        case -5:
                            break;
                        case 5:
                            {
                                return new Symbol(sym.MINUS);
                            }
                        case -6:
                            break;
                        case 6:
                            {
                                System.err.println("Illegal character: " + yytext());
                            }
                        case -7:
                            break;
                        case 7:
                            {
                                return new Symbol(sym.AND);
                            }
                        case -8:
                            break;
                        case 8:
                            {
                                return new Symbol(sym.OR);
                            }
                        case -9:
                            break;
                        case 9:
                            {
                                return new Symbol(sym.XOR);
                            }
                        case -10:
                            break;
                        case 10:
                            {
                                return new Symbol(sym.NOT);
                            }
                        case -11:
                            break;
                        case 11:
                            {
                                return new Symbol(sym.LPAREN);
                            }
                        case -12:
                            break;
                        case 12:
                            {
                                return new Symbol(sym.RPAREN);
                            }
                        case -13:
                            break;
                        case 13:
                            {
                                return new Symbol(sym.LCURLY);
                            }
                        case -14:
                            break;
                        case 14:
                            {
                                return new Symbol(sym.RCURLY);
                            }
                        case -15:
                            break;
                        case 15:
                            {
                                return new Symbol(sym.ASSIGN);
                            }
                        case -16:
                            break;
                        case 16:
                            {
                                return new Symbol(sym.DOT);
                            }
                        case -17:
                            break;
                        case 17:
                            {
                                return new Symbol(sym.NUMBER, new Integer(yytext()));
                            }
                        case -18:
                            break;
                        case 18:
                            {
                            }
                        case -19:
                            break;
                        case 19:
                            {
                                return new Symbol(sym.SHL);
                            }
                        case -20:
                            break;
                        case 20:
                            {
                                return new Symbol(sym.SHR);
                            }
                        case -21:
                            break;
                        case 21:
                            {
                                return new Symbol(sym.EQ);
                            }
                        case -22:
                            break;
                        case 22:
                            {
                                return new Symbol(sym.NEQ);
                            }
                        case -23:
                            break;
                        case 23:
                            {
                                return new Symbol(sym.PORT);
                            }
                        case -24:
                            break;
                        case 24:
                            {
                                return new Symbol(sym.UNIT);
                            }
                        case -25:
                            break;
                        case 25:
                            {
                                return new Symbol(sym.LOGIC);
                            }
                        case -26:
                            break;
                        case 26:
                            {
                                return new Symbol(sym.CONFIG);
                            }
                        case -27:
                            break;
                        case 27:
                            {
                                return new Symbol(sym.SCRIPT);
                            }
                        case -28:
                            break;
                        case 28:
                            {
                                return new Symbol(sym.PACKAGE);
                            }
                        case -29:
                            break;
                        case 29:
                            {
                                return new Symbol(sym.TRIGGER);
                            }
                        case -30:
                            break;
                        case 30:
                            {
                                return new Symbol(sym.COMPONENT);
                            }
                        case -31:
                            break;
                        case 31:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -32:
                            break;
                        case 32:
                            {
                                System.err.println("Illegal character: " + yytext());
                            }
                        case -33:
                            break;
                        case 33:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -34:
                            break;
                        case 34:
                            {
                                System.err.println("Illegal character: " + yytext());
                            }
                        case -35:
                            break;
                        case 35:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -36:
                            break;
                        case 36:
                            {
                                System.err.println("Illegal character: " + yytext());
                            }
                        case -37:
                            break;
                        case 37:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -38:
                            break;
                        case 38:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -39:
                            break;
                        case 39:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -40:
                            break;
                        case 40:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -41:
                            break;
                        case 41:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -42:
                            break;
                        case 42:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -43:
                            break;
                        case 43:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -44:
                            break;
                        case 44:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -45:
                            break;
                        case 45:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -46:
                            break;
                        case 46:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -47:
                            break;
                        case 47:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -48:
                            break;
                        case 48:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -49:
                            break;
                        case 49:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -50:
                            break;
                        case 50:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -51:
                            break;
                        case 51:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -52:
                            break;
                        case 52:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -53:
                            break;
                        case 53:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -54:
                            break;
                        case 54:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -55:
                            break;
                        case 55:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -56:
                            break;
                        case 56:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -57:
                            break;
                        case 57:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -58:
                            break;
                        case 58:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -59:
                            break;
                        case 59:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -60:
                            break;
                        case 60:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -61:
                            break;
                        case 61:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -62:
                            break;
                        case 62:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -63:
                            break;
                        case 63:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -64:
                            break;
                        case 64:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -65:
                            break;
                        case 65:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -66:
                            break;
                        case 66:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -67:
                            break;
                        case 67:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -68:
                            break;
                        case 68:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -69:
                            break;
                        case 69:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -70:
                            break;
                        case 70:
                            {
                                return new Symbol(sym.IDENT, yytext());
                            }
                        case -71:
                            break;
                        default:
                            yy_error(YY_E_INTERNAL, false);
                        case -1:
                    }
                    yy_initial = true;
                    yy_state = yy_state_dtrans[yy_lexical_state];
                    yy_next_state = YY_NO_STATE;
                    yy_last_accept_state = YY_NO_STATE;
                    yy_mark_start();
                    yy_this_accept = yy_acpt[yy_state];
                    if (YY_NOT_ACCEPT != yy_this_accept) {
                        yy_last_accept_state = yy_state;
                        yy_mark_end();
                    }
                }
            }
        }
    }
}
