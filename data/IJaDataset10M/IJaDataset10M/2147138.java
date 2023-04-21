package jformulaengine.parser;

import jformulaengine.runtime.*;
import jformulaengine.runtime.functions.*;
import java.util.Vector;

public class FormulaParser implements FormulaParserConstants {

    public FormulaParser(String sExpression) {
        this(new java.io.ByteArrayInputStream(sExpression.getBytes()));
    }

    public Formula parse() throws ParseException {
        Node node = formula();
        Formula formula = new Formula();
        formula.setRoot(node);
        return formula;
    }

    protected Node buildOperatorFunction(String sOperator, Node lhs, Node rhs) throws ParseException {
        FunctionNode func = null;
        try {
            func = FunctionFactory.getInstance().getFunction(sOperator);
        } catch (FunctionException e) {
            throw new ParseException(e.getMessage());
        }
        func.addArgument(lhs);
        func.addArgument(rhs);
        return func;
    }

    public final Node formula() throws ParseException {
        Node node;
        node = expression();
        jj_consume_token(0);
        {
            if (true) return node;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expression() throws ParseException {
        Token x;
        Node lhs;
        Node rhs;
        lhs = expressionLevel1();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case EQUAL:
            case LT:
            case LTE:
            case GT:
            case GTE:
            case NE:
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case EQUAL:
                        x = jj_consume_token(EQUAL);
                        break;
                    case LT:
                        x = jj_consume_token(LT);
                        break;
                    case LTE:
                        x = jj_consume_token(LTE);
                        break;
                    case GT:
                        x = jj_consume_token(GT);
                        break;
                    case GTE:
                        x = jj_consume_token(GTE);
                        break;
                    case NE:
                        x = jj_consume_token(NE);
                        break;
                    default:
                        jj_la1[0] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                rhs = expression();
                lhs = buildOperatorFunction(x.toString(), lhs, rhs);
                break;
            default:
                jj_la1[1] = jj_gen;
                ;
        }
        {
            if (true) return lhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expressionLevel1() throws ParseException {
        Token x;
        Node lhs;
        Node rhs;
        lhs = expressionLevel2();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case CONCATONATE:
                x = jj_consume_token(CONCATONATE);
                rhs = expressionLevel1();
                lhs = buildOperatorFunction(x.toString(), lhs, rhs);
                break;
            default:
                jj_la1[2] = jj_gen;
                ;
        }
        {
            if (true) return lhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expressionLevel2() throws ParseException {
        Token x;
        Node lhs;
        Node rhs;
        lhs = expressionLevel3();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case PLUS:
            case MINUS:
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case PLUS:
                        x = jj_consume_token(PLUS);
                        break;
                    case MINUS:
                        x = jj_consume_token(MINUS);
                        break;
                    default:
                        jj_la1[3] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                rhs = expressionLevel2();
                lhs = buildOperatorFunction(x.toString(), lhs, rhs);
                break;
            default:
                jj_la1[4] = jj_gen;
                ;
        }
        {
            if (true) return lhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expressionLevel3() throws ParseException {
        Token x;
        Node lhs;
        Node rhs;
        lhs = expressionLevel4();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case MULTIPLY:
            case DIVIDE:
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case MULTIPLY:
                        x = jj_consume_token(MULTIPLY);
                        break;
                    case DIVIDE:
                        x = jj_consume_token(DIVIDE);
                        break;
                    default:
                        jj_la1[5] = jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                rhs = expressionLevel3();
                lhs = buildOperatorFunction(x.toString(), lhs, rhs);
                break;
            default:
                jj_la1[6] = jj_gen;
                ;
        }
        {
            if (true) return lhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expressionLevel4() throws ParseException {
        Token x;
        Node lhs;
        Node rhs;
        lhs = expressionLevel5();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case EXPONENT:
                x = jj_consume_token(EXPONENT);
                rhs = expressionLevel4();
                lhs = buildOperatorFunction(x.toString(), lhs, rhs);
                break;
            default:
                jj_la1[7] = jj_gen;
                ;
        }
        {
            if (true) return lhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expressionLevel5() throws ParseException {
        Token x;
        Node lhs;
        lhs = expressionLevel6();
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case PERCENT:
                x = jj_consume_token(PERCENT);
                FunctionNode func = null;
                try {
                    func = FunctionFactory.getInstance().getFunction(x.toString());
                } catch (FunctionException e) {
                    {
                        if (true) throw new ParseException(e.getMessage());
                    }
                }
                func.addArgument(lhs);
                lhs = func;
                break;
            default:
                jj_la1[8] = jj_gen;
                ;
        }
        {
            if (true) return lhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node expressionLevel6() throws ParseException {
        Token x = null;
        Node rhs;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case MINUS:
                x = jj_consume_token(MINUS);
                break;
            default:
                jj_la1[9] = jj_gen;
                ;
        }
        rhs = operand();
        if (x != null) {
            FunctionNode func = null;
            try {
                func = FunctionFactory.getInstance().getFunction("Negate");
            } catch (FunctionException e) {
                {
                    if (true) throw new ParseException(e.getMessage());
                }
            }
            func.addArgument(rhs);
            rhs = func;
        }
        {
            if (true) return rhs;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node operand() throws ParseException {
        Node op;
        if (jj_2_1(2)) {
            op = function();
        } else {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case NUMBER:
                case STRING:
                    op = constant();
                    break;
                case FUNCTION_STYLE_NAME:
                case NAME:
                case ESCAPED_NAME:
                case EXCEL_NAME:
                    op = bareword();
                    break;
                case LEFT_BRACE:
                    jj_consume_token(LEFT_BRACE);
                    op = expression();
                    jj_consume_token(RIGHT_BRACE);
                    break;
                default:
                    jj_la1[10] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        {
            if (true) return op;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node constant() throws ParseException {
        Token x;
        Node con;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case STRING:
                x = jj_consume_token(STRING);
                con = new StringNode(x.toString());
                break;
            case NUMBER:
                x = jj_consume_token(NUMBER);
                con = new NumericNode(x.toString());
                break;
            default:
                jj_la1[11] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        {
            if (true) return con;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node bareword() throws ParseException {
        Token x;
        Node name;
        String sName;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case NAME:
                x = jj_consume_token(NAME);
                sName = x.toString();
                break;
            case ESCAPED_NAME:
                x = jj_consume_token(ESCAPED_NAME);
                sName = x.toString().substring(1, x.toString().length() - 1);
                break;
            case FUNCTION_STYLE_NAME:
                x = jj_consume_token(FUNCTION_STYLE_NAME);
                sName = x.toString();
                break;
            case EXCEL_NAME:
                x = jj_consume_token(EXCEL_NAME);
                sName = x.toString();
                break;
            default:
                jj_la1[12] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        name = new NameNode(sName);
        {
            if (true) return name;
        }
        throw new Error("Missing return statement in function");
    }

    public final Node function() throws ParseException {
        Token x;
        Node first;
        Node next;
        Vector args = null;
        x = jj_consume_token(FUNCTION_STYLE_NAME);
        jj_consume_token(LEFT_BRACE);
        first = expression();
        if (args == null) {
            args = new Vector();
            args.add(first);
        }
        label_1: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case COMMA:
                    ;
                    break;
                default:
                    jj_la1[13] = jj_gen;
                    break label_1;
            }
            jj_consume_token(COMMA);
            next = expression();
            args.add(next);
        }
        jj_consume_token(RIGHT_BRACE);
        FunctionNode func = null;
        try {
            func = FunctionFactory.getInstance().getFunction(x.toString());
        } catch (FunctionException e) {
            {
                if (true) throw new ParseException(e.getMessage());
            }
        }
        func.appendArguments(args);
        {
            if (true) return func;
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

    private final boolean jj_3R_2() {
        if (jj_scan_token(FUNCTION_STYLE_NAME)) return true;
        if (jj_scan_token(LEFT_BRACE)) return true;
        return false;
    }

    private final boolean jj_3_1() {
        if (jj_3R_2()) return true;
        return false;
    }

    public FormulaParserTokenManager token_source;

    SimpleCharStream jj_input_stream;

    public Token token, jj_nt;

    private int jj_ntk;

    private Token jj_scanpos, jj_lastpos;

    private int jj_la;

    public boolean lookingAhead = false;

    private boolean jj_semLA;

    private int jj_gen;

    private final int[] jj_la1 = new int[14];

    private static int[] jj_la1_0;

    static {
        jj_la1_0();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[] { 0x7e00, 0x7e00, 0x20000, 0x60, 0x60, 0x180, 0x180, 0x10000, 0x8000, 0x40, 0x7e40000, 0x6000000, 0x1e00000, 0x100000 };
    }

    private final JJCalls[] jj_2_rtns = new JJCalls[1];

    private boolean jj_rescan = false;

    private int jj_gc = 0;

    public FormulaParser(java.io.InputStream stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new FormulaParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 14; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(java.io.InputStream stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 14; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public FormulaParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new FormulaParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 14; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 14; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public FormulaParser(FormulaParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 14; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(FormulaParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 14; i++) jj_la1[i] = -1;
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
        boolean[] la1tokens = new boolean[27];
        for (int i = 0; i < 27; i++) {
            la1tokens[i] = false;
        }
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 14; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 27; i++) {
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
