package org.dbe.kb.qee.parser;

public class KeywordParser implements KeywordParserConstants {

    public static void main(String args[]) throws ParseException {
        KeywordParser parser = new KeywordParser(System.in);
        parser.ExpressionList();
    }

    public static java.util.Vector parse(String input) throws ParseException {
        KeywordParser parser = new KeywordParser(new java.io.StringReader(input));
        return parser.Expression();
    }

    public final void ExpressionList() throws ParseException {
        java.util.Vector s;
        System.out.println("Please type in an expression followed by a \";\" or ^D to quit:");
        System.out.println("");
        label_1: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case ID:
                case NUM:
                case FLOAT:
                case STR:
                case 11:
                    ;
                    break;
                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }
            s = Expression();
            jj_consume_token(11);
            System.out.println(s.toString());
            System.out.println("");
            System.out.println("Please type in another expression followed by a \";\" or ^D to quit:");
            System.out.println("");
        }
        jj_consume_token(0);
    }

    public final java.util.Vector Expression() throws ParseException {
        java.util.Vector termimage = new java.util.Vector();
        QueryTerm queryTerm;
        label_2: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case ID:
                case NUM:
                case FLOAT:
                case STR:
                    ;
                    break;
                default:
                    jj_la1[1] = jj_gen;
                    break label_2;
            }
            queryTerm = Term();
            termimage.addElement(queryTerm);
        }
        {
            if (true) return termimage;
        }
        throw new Error("Missing return statement in function");
    }

    public final QueryTerm Term() throws ParseException {
        java.util.Vector path = null;
        Token op = null;
        Object value = null;
        Token w = null;
        if (jj_2_1(2)) {
            path = path();
            op = jj_consume_token(OPERATOR);
            value = Factor();
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 12:
                    jj_consume_token(12);
                    w = jj_consume_token(FLOAT);
                    break;
                default:
                    jj_la1[2] = jj_gen;
                    ;
            }
            String oper = (op == null) ? null : op.image;
            String imp = (w == null) ? null : w.image;
            QueryTerm result = new QueryTerm(path, oper, value, imp);
            {
                if (true) return result;
            }
        } else {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case ID:
                case NUM:
                case FLOAT:
                case STR:
                    value = Factor();
                    switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                        case 12:
                            jj_consume_token(12);
                            w = jj_consume_token(FLOAT);
                            break;
                        default:
                            jj_la1[3] = jj_gen;
                            ;
                    }
                    String imp = (w == null) ? null : w.image;
                    QueryTerm result = new QueryTerm(null, null, value, imp);
                    {
                        if (true) return result;
                    }
                    break;
                default:
                    jj_la1[4] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        throw new Error("Missing return statement in function");
    }

    public final java.util.Vector path() throws ParseException {
        java.util.Vector factorimage = new java.util.Vector();
        Token t;
        t = jj_consume_token(ID);
        factorimage.addElement(t.image);
        label_3: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case SEPARATOR:
                    ;
                    break;
                default:
                    jj_la1[5] = jj_gen;
                    break label_3;
            }
            jj_consume_token(SEPARATOR);
            t = jj_consume_token(ID);
            factorimage.addElement(t.image);
        }
        {
            if (true) return factorimage;
        }
        throw new Error("Missing return statement in function");
    }

    public final Object Factor() throws ParseException {
        java.util.Vector factorimage = new java.util.Vector();
        String s;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ID:
            case NUM:
            case FLOAT:
                s = Simple();
                {
                    if (true) return s;
                }
                break;
            case STR:
                jj_consume_token(STR);
                s = Simple();
                factorimage.add(s);
                label_4: while (true) {
                    switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                        case ID:
                        case NUM:
                        case FLOAT:
                            ;
                            break;
                        default:
                            jj_la1[6] = jj_gen;
                            break label_4;
                    }
                    s = Simple();
                    factorimage.add(s);
                }
                jj_consume_token(STR);
                {
                    if (true) return factorimage;
                }
                break;
            default:
                jj_la1[7] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        throw new Error("Missing return statement in function");
    }

    public final String Simple() throws ParseException {
        Token t;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ID:
                t = jj_consume_token(ID);
                {
                    if (true) return t.image;
                }
                break;
            case NUM:
                t = jj_consume_token(NUM);
                {
                    if (true) return t.image;
                }
                break;
            case FLOAT:
                t = jj_consume_token(FLOAT);
                {
                    if (true) return t.image;
                }
                break;
            default:
                jj_la1[8] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        throw new Error("Missing return statement in function");
    }

    private final boolean jj_2_1(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_1();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(0, xla);
        }
    }

    private final boolean jj_3R_5() {
        if (jj_scan_token(ID)) return true;
        Token xsp;
        while (true) {
            xsp = jj_scanpos;
            if (jj_3R_6()) {
                jj_scanpos = xsp;
                break;
            }
        }
        return false;
    }

    private final boolean jj_3_1() {
        if (jj_3R_5()) return true;
        if (jj_scan_token(OPERATOR)) return true;
        return false;
    }

    private final boolean jj_3R_6() {
        if (jj_scan_token(SEPARATOR)) return true;
        return false;
    }

    public KeywordParserTokenManager token_source;

    SimpleCharStream jj_input_stream;

    public Token token, jj_nt;

    private int jj_ntk;

    private Token jj_scanpos, jj_lastpos;

    private int jj_la;

    public boolean lookingAhead = false;

    private boolean jj_semLA;

    private int jj_gen;

    private final int[] jj_la1 = new int[9];

    private static int[] jj_la1_0;

    static {
        jj_la1_0();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[] { 0xce0, 0x4e0, 0x1000, 0x1000, 0x4e0, 0x200, 0xe0, 0x4e0, 0xe0 };
    }

    private final JJCalls[] jj_2_rtns = new JJCalls[1];

    private boolean jj_rescan = false;

    private int jj_gc = 0;

    public KeywordParser(java.io.InputStream stream) {
        this(stream, null);
    }

    public KeywordParser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new KeywordParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 9; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    public void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 9; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public KeywordParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new KeywordParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 9; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 9; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public KeywordParser(KeywordParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 9; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(KeywordParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 9; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    private final Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            if (++jj_gc > 100) {
                jj_gc = 0;
                for (int i = 0; i < jj_2_rtns.length; i++) {
                    JJCalls c = jj_2_rtns[i];
                    while (c != null) {
                        if (c.gen < jj_gen) c.first = null;
                        c = c.next;
                    }
                }
            }
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    private static final class LookaheadSuccess extends java.lang.Error {
    }

    private final LookaheadSuccess jj_ls = new LookaheadSuccess();

    private final boolean jj_scan_token(int kind) {
        if (jj_scanpos == jj_lastpos) {
            jj_la--;
            if (jj_scanpos.next == null) {
                jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
            } else {
                jj_lastpos = jj_scanpos = jj_scanpos.next;
            }
        } else {
            jj_scanpos = jj_scanpos.next;
        }
        if (jj_rescan) {
            int i = 0;
            Token tok = token;
            while (tok != null && tok != jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null) jj_add_error_token(kind, i);
        }
        if (jj_scanpos.kind != kind) return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
        return false;
    }

    public final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public final Token getToken(int index) {
        Token t = lookingAhead ? jj_scanpos : token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private final int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private java.util.Vector jj_expentries = new java.util.Vector();

    private int[] jj_expentry;

    private int jj_kind = -1;

    private int[] jj_lasttokens = new int[100];

    private int jj_endpos;

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100) return;
        if (pos == jj_endpos + 1) {
            jj_lasttokens[jj_endpos++] = kind;
        } else if (jj_endpos != 0) {
            jj_expentry = new int[jj_endpos];
            for (int i = 0; i < jj_endpos; i++) {
                jj_expentry[i] = jj_lasttokens[i];
            }
            boolean exists = false;
            for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements(); ) {
                int[] oldentry = (int[]) (e.nextElement());
                if (oldentry.length == jj_expentry.length) {
                    exists = true;
                    for (int i = 0; i < jj_expentry.length; i++) {
                        if (oldentry[i] != jj_expentry[i]) {
                            exists = false;
                            break;
                        }
                    }
                    if (exists) break;
                }
            }
            if (!exists) jj_expentries.addElement(jj_expentry);
            if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
        }
    }

    public ParseException generateParseException() {
        jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[13];
        for (int i = 0; i < 13; i++) {
            la1tokens[i] = false;
        }
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 9; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 13; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }
        }
        jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = (int[]) jj_expentries.elementAt(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }

    private final void jj_rescan_token() {
        jj_rescan = true;
        for (int i = 0; i < 1; i++) {
            try {
                JJCalls p = jj_2_rtns[i];
                do {
                    if (p.gen > jj_gen) {
                        jj_la = p.arg;
                        jj_lastpos = jj_scanpos = p.first;
                        switch(i) {
                            case 0:
                                jj_3_1();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess ls) {
            }
        }
        jj_rescan = false;
    }

    private final void jj_save(int index, int xla) {
        JJCalls p = jj_2_rtns[index];
        while (p.gen > jj_gen) {
            if (p.next == null) {
                p = p.next = new JJCalls();
                break;
            }
            p = p.next;
        }
        p.gen = jj_gen + xla - jj_la;
        p.first = token;
        p.arg = xla;
    }

    static final class JJCalls {

        int gen;

        Token first;

        int arg;

        JJCalls next;
    }
}
