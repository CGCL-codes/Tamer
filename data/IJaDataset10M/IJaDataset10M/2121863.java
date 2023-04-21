package com.c4j.java.impl;

import java.util.List;
import java.util.Vector;

@SuppressWarnings("all")
public class Parser implements ParserConstants {

    public final Package parsePackage() throws ParseException {
        Token t;
        String identifier = null;
        Package pkg = null;
        t = jj_consume_token(IDENTIFIER);
        identifier = token.image;
        label_1: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 5:
                    ;
                    break;
                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }
            jj_consume_token(5);
            t = jj_consume_token(IDENTIFIER);
            pkg = new Package(pkg, identifier);
            identifier = t.image;
        }
        jj_consume_token(0);
        {
            if (true) return new Package(pkg, identifier);
        }
        throw new Error("Missing return statement in function");
    }

    public final Type parseType() throws ParseException {
        Type type;
        type = parseTypeN();
        jj_consume_token(0);
        {
            if (true) return type;
        }
        throw new Error("Missing return statement in function");
    }

    public final Type parseTypeN() throws ParseException {
        Token t;
        String identifier = null;
        Package pkg = null;
        List<Generic> generics = null;
        int dimension = 0;
        t = jj_consume_token(IDENTIFIER);
        identifier = token.image;
        label_2: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 5:
                    ;
                    break;
                default:
                    jj_la1[1] = jj_gen;
                    break label_2;
            }
            jj_consume_token(5);
            t = jj_consume_token(IDENTIFIER);
            pkg = new Package(pkg, identifier);
            identifier = t.image;
        }
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case 8:
                generics = genericdef();
                break;
            default:
                jj_la1[2] = jj_gen;
                ;
        }
        label_3: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 6:
                    ;
                    break;
                default:
                    jj_la1[3] = jj_gen;
                    break label_3;
            }
            jj_consume_token(6);
            jj_consume_token(7);
            dimension++;
        }
        {
            if (true) return new Type(pkg, identifier, generics, dimension);
        }
        throw new Error("Missing return statement in function");
    }

    public final List<Generic> genericdef() throws ParseException {
        List<Generic> generics;
        jj_consume_token(8);
        generics = genericlist();
        jj_consume_token(9);
        {
            if (true) return generics;
        }
        throw new Error("Missing return statement in function");
    }

    public final List<Generic> genericlist() throws ParseException {
        Generic generic;
        List<Generic> generics = new Vector<Generic>();
        generic = generic();
        generics.add(generic);
        label_4: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 10:
                    ;
                    break;
                default:
                    jj_la1[4] = jj_gen;
                    break label_4;
            }
            jj_consume_token(10);
            generic = generic();
            generics.add(generic);
        }
        {
            if (true) return generics;
        }
        throw new Error("Missing return statement in function");
    }

    public final Generic generic() throws ParseException {
        boolean wildcard = false;
        Type type = null;
        if (jj_2_1(2)) {
            jj_consume_token(11);
            wildcard = true;
            jj_consume_token(EXTENDS);
            type = parseTypeN();
        } else {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case 11:
                    jj_consume_token(11);
                    wildcard = true;
                    break;
                case IDENTIFIER:
                    type = parseTypeN();
                    break;
                default:
                    jj_la1[5] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        {
            if (true) return new Generic(wildcard, type);
        }
        throw new Error("Missing return statement in function");
    }

    private boolean jj_2_1(int xla) {
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

    private boolean jj_3_1() {
        if (jj_scan_token(11)) return true;
        if (jj_scan_token(EXTENDS)) return true;
        return false;
    }

    /** Generated Token Manager. */
    public ParserTokenManager token_source;

    SimpleCharStream jj_input_stream;

    /** Current token. */
    public Token token;

    /** Next token. */
    public Token jj_nt;

    private int jj_ntk;

    private Token jj_scanpos, jj_lastpos;

    private int jj_la;

    private int jj_gen;

    private final int[] jj_la1 = new int[6];

    private static int[] jj_la1_0;

    static {
        jj_la1_init_0();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[] { 0x20, 0x20, 0x100, 0x40, 0x400, 0x810 };
    }

    private final JJCalls[] jj_2_rtns = new JJCalls[1];

    private boolean jj_rescan = false;

    private int jj_gc = 0;

    /** Constructor with InputStream. */
    public Parser(java.io.InputStream stream) {
        this(stream, null);
    }

    /** Constructor with InputStream and supplied encoding */
    public Parser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new ParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 6; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    /** Reinitialise. */
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
        for (int i = 0; i < 6; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Constructor. */
    public Parser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new ParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 6; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 6; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Constructor with generated Token Manager. */
    public Parser(ParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 6; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(ParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 6; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    private Token jj_consume_token(int kind) throws ParseException {
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

    private boolean jj_scan_token(int kind) {
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

    /** Get the next Token. */
    public final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /** Get the specific Token. */
    public final Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();

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
            jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext(); ) {
                int[] oldentry = (int[]) (it.next());
                if (oldentry.length == jj_expentry.length) {
                    for (int i = 0; i < jj_expentry.length; i++) {
                        if (oldentry[i] != jj_expentry[i]) {
                            continue jj_entries_loop;
                        }
                    }
                    jj_expentries.add(jj_expentry);
                    break jj_entries_loop;
                }
            }
            if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
        }
    }

    /** Generate ParseException. */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[12];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 6; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.get(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    /** Enable tracing. */
    public final void enable_tracing() {
    }

    /** Disable tracing. */
    public final void disable_tracing() {
    }

    private void jj_rescan_token() {
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

    private void jj_save(int index, int xla) {
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
