package sisc.reader;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import sisc.compiler.CompilerConstants;
import sisc.compiler.Syntax;
import sisc.data.Box;
import sisc.data.Expression;
import sisc.data.ImmutablePair;
import sisc.data.ImmutableString;
import sisc.data.ImmutableVector;
import sisc.data.Pair;
import sisc.data.Quantity;
import sisc.data.SchemeCharacter;
import sisc.data.SchemeString;
import sisc.data.SchemeVector;
import sisc.data.Symbol;
import sisc.data.Value;
import sisc.exprs.AnnotatedExpr;
import sisc.util.Defaults;
import sisc.util.Util;

/**
 * Receives tokens from the Lexer and parses them into valid
 * s-expressions.
 */
public class Parser extends Util implements Tokens {

    public static boolean isPeculiarIdentifier(String s) {
        return (s.equals("+") || s.equals("-") || s.equals("..."));
    }

    public static final int PRODUCE_IMMUTABLES = 0x1, PRODUCE_ANNOTATIONS = 0x2, STRICT_R5RS = 0x4, CASE_SENSITIVE = 0x8, READING_VECTOR = 0x10, PERMISSIVE_PARSING = 0x20, GUILE_MODE = 0x40;

    public boolean annotate = Defaults.EMIT_ANNOTATIONS;

    public Lexer lexer;

    static final Object DOT = new Object() {

        @Override
        public String toString() {
            return ".";
        }
    };

    static final Object ENDPAIR = new Object() {

        @Override
        public String toString() {
            return ")";
        }
    };

    static final Symbol SYNTAX = Symbol.get("syntax"), ANNOTATION = Symbol.get("make-annotation");

    static final HashMap chars = new HashMap(8);

    static {
        chars.put("space", new SchemeCharacter(' '));
        chars.put("backspace", new SchemeCharacter(''));
        chars.put("rubout", new SchemeCharacter(''));
        chars.put("page", new SchemeCharacter(''));
        chars.put("tab", new SchemeCharacter('\t'));
        chars.put("return", new SchemeCharacter('\r'));
        chars.put("newline", new SchemeCharacter('\n'));
        chars.put("nul", new SchemeCharacter((char) 0));
    }

    public Parser(Lexer l) {
        this.lexer = l;
    }

    void warn(String messageClass, PushbackReader is) {
        if (is instanceof SourceReader) {
            SourceReader sp = (SourceReader) is;
            System.err.println(warn(messageClass, sp.sourceFile, sp.line, sp.column));
        } else {
            System.err.println(warn(messageClass));
        }
    }

    public final Value nextExpression(PushbackReader is) throws IOException {
        return nextExpression(is, PRODUCE_IMMUTABLES, EMPTYLIST);
    }

    public final Value nextExpression(PushbackReader is, int flags, Pair anns) throws IOException {
        return nextExpression(is, 10, flags, anns);
    }

    public final Value nextExpression(PushbackReader is, int radix, int flags) throws IOException {
        return nextExpression(is, radix, flags, EMPTYLIST);
    }

    /**
     * Reads an s-expression from the given input port.
     *
     * @param is PushbackReader from which to read
     * @param radix Specifies the radix of any numbers that are read
     * @param flags Specifies attributes for the returned values (PRODUCE_IMMUTABLES, PRODUCE_ANNOTATIONS, STRICT_R5RS)
     * @param anns additional annotations
     * @return the read expression
     * @exception IOException if an error occurs
     */
    public Value nextExpression(PushbackReader is, int radix, int flags, Pair anns) throws IOException {
        Object n = VOID;
        try {
            n = _nextExpression(is, new HashMap(), null, radix, flags, anns);
            return (Value) n;
        } catch (ClassCastException ce) {
            if (n == ENDPAIR) {
                potentialError(flags, "orphanedparen", is);
                return nextExpression(is, radix, flags, anns);
            } else if (n == DOT) {
                throw new IOException(liMessage(SISCB, "unexpecteddot"));
            }
        }
        return (Value) n;
    }

    protected final Value nextExpression(PushbackReader is, HashMap state, int flags, Pair anns) throws IOException {
        return (Value) _nextExpression(is, state, null, flags, anns);
    }

    protected void potentialError(int flags, String message, PushbackReader is) throws IOException {
        if (permissiveParsing(flags)) {
            if (is == null) {
                System.err.println(warn(message));
            } else {
                warn(message, is);
            }
        } else {
            throw new IOException(liMessage(SISCB, message));
        }
    }

    protected void potentialError(int flags, String message, String arg, PushbackReader is) throws IOException {
        if (permissiveParsing(flags)) {
            if (is == null) {
                System.err.println(warn(message, arg));
            } else {
                System.err.println(warn(liMessage(SISCB, message, arg)));
            }
        } else {
            throw new IOException(liMessage(SISCB, message, arg));
        }
    }

    private Value lastValue(HashMap state, Object l) {
        return (Value) (l instanceof Integer ? state.get(l) : l);
    }

    protected Object _nextExpression(PushbackReader is, HashMap state, Integer def, int flags, Pair anns) throws IOException {
        return _nextExpression(is, state, def, 10, flags, anns);
    }

    protected Quantity numberCheck(Object o, PushbackReader is, int flags) throws IOException {
        try {
            return (Quantity) o;
        } catch (ClassCastException cce) {
            potentialError(flags, "badtokennotnumber", is);
            return Quantity.ZERO;
        }
    }

    protected Object listSpecial(Symbol car, PushbackReader is, HashMap state, Integer def, int flags, Pair anns) throws IOException {
        boolean produceImmutables = produceImmutables(flags);
        Pair t = (produceImmutables ? new ImmutablePair(EMPTYLIST, EMPTYLIST, false) : new Pair(EMPTYLIST, EMPTYLIST));
        Pair p = (produceImmutables ? new ImmutablePair(car, t) : new Pair(car, t));
        if (def != null) {
            state.put(def, p);
        }
        t.setCar(nextExpression(is, state, flags, anns));
        if (produceImmutables) {
            ((ImmutablePair) t).makeImmutable();
        }
        return p;
    }

    protected Object _nextExpression(PushbackReader is, HashMap state, Integer def, int radix, int flags, Pair anns) throws IOException {
        int line = -1, col = -1;
        String file = null;
        int token = lexer.nextToken(is, radix);
        Object o;
        switch(token) {
            case TT_EOF:
                return EOF;
            case TT_DOT:
                o = DOT;
                break;
            case TT_UNQUOTE:
                o = listSpecial(UNQUOTE, is, state, def, flags, anns);
                break;
            case TT_UNQUOTE_SPLICING:
                o = listSpecial(UNQUOTE_SPLICING, is, state, def, flags, anns);
                break;
            case TT_QUOTE:
                o = listSpecial(QUOTESYM, is, state, def, flags & (~PRODUCE_ANNOTATIONS), anns);
                break;
            case TT_BACKQUOTE:
                o = listSpecial(BACKQUOTE, is, state, def, flags, anns);
                break;
            case TT_NUMBER:
                o = lexer.nval;
                break;
            case TT_STRING:
                o = new ImmutableString(lexer.sval);
                break;
            case TT_PAIR:
                if (is instanceof SourceReader) {
                    SourceReader sip = (SourceReader) is;
                    line = sip.line;
                    col = sip.column - 1;
                    file = sip.sourceFile;
                }
                if (annotate && produceAnnotations(flags) && line >= 0) {
                    AnnotatedExpr aexp = new AnnotatedExpr(null, sourceAnnotations(file, line, col, anns));
                    if (def != null) {
                        state.put(def, aexp);
                        def = null;
                    }
                    aexp.expr = readList(is, state, def, flags, anns);
                    return aexp;
                } else {
                    return readList(is, state, def, flags, anns);
                }
            case TT_ENDPAIR:
                o = ENDPAIR;
                break;
            case TT_PIPE:
                Symbol sym = Symbol.intern(lexer.readToBreak(is, Lexer.protected_literal_barrier, true, true));
                lexer.readChar(is);
                return sym;
            case TT_SYMBOL:
                if (lexer.strictR5RS && !isPeculiarIdentifier(lexer.sval) && lexer.sval.length() >= 1 && !(Character.isLetter(lexer.sval.charAt(0)) || Lexer.in(lexer.sval.charAt(0), Lexer.special_initials))) {
                    potentialError(flags, "invalididentifier", lexer.sval, is);
                }
                o = Symbol.get(lexer.sval, caseSensitive(flags));
                break;
            case TT_SHARP:
                int c = is.read();
                char dc = Character.toLowerCase((char) c);
                switch(dc) {
                    case 't':
                        o = TRUE;
                        break;
                    case 'f':
                        o = FALSE;
                        break;
                    case ';':
                        nextExpression(is);
                        o = _nextExpression(is, state, def, flags, anns);
                        break;
                    case '\\':
                        c = is.read();
                        if (Lexer.in((char) c, Lexer.special)) {
                            o = new SchemeCharacter((char) c);
                            break;
                        }
                        is.unread(c);
                        String cn = lexer.readToBreak(is, Lexer.special, false, false);
                        String cnl = cn.toLowerCase();
                        Object cs = CharUtil.namedConstToChar(cnl);
                        try {
                            if (cs != null) {
                                o = cs;
                            } else if (cn.length() == 1) {
                                o = new SchemeCharacter(cn.charAt(0));
                            } else if (cn.charAt(0) == 'u') {
                                o = new SchemeCharacter(CharUtil.hexToChar(cnl.substring(1)));
                            } else if ("nl".equals(cn)) {
                                o = new SchemeCharacter('\n');
                            } else {
                                o = new SchemeCharacter(CharUtil.octToChar(cnl));
                            }
                        } catch (NumberFormatException nfe) {
                            potentialError(flags, "invalidcharconst", is);
                            o = new SchemeCharacter(' ');
                        }
                        break;
                    case 'b':
                        o = numberCheck(_nextExpression(is, state, null, 2, flags, anns), is, flags);
                        break;
                    case 'o':
                        o = numberCheck(_nextExpression(is, state, null, 8, flags, anns), is, flags);
                        break;
                    case 'x':
                        o = numberCheck(_nextExpression(is, state, null, 16, flags, anns), is, flags);
                        break;
                    case 'd':
                        o = numberCheck(_nextExpression(is, state, null, flags, anns), is, flags);
                        break;
                    case '&':
                        o = new Box();
                        if (def != null) {
                            state.put(def, o);
                        }
                        ((Box) o).val = (Value) _nextExpression(is, state, null, flags, anns);
                        break;
                    case 'i':
                        o = numberCheck(_nextExpression(is, state, null, radix, flags, anns), is, flags).toInexact();
                        break;
                    case 'e':
                        o = numberCheck(_nextExpression(is, state, null, radix, flags, anns), is, flags).toExact();
                        break;
                    case '!':
                        if (!guileMode(flags)) {
                            String bv = lexer.readToBreak(is, Lexer.special, false, false);
                            if (bv.equals("eof")) {
                                o = EOF;
                            } else if (bv.equals("void")) {
                                o = VOID;
                            } else if (bv.equals("+inf")) {
                                o = Quantity.POSITIVE_INFINITY;
                            } else if (bv.equals("-inf")) {
                                o = Quantity.NEGATIVE_INFINITY;
                            } else if (bv.equals("nan")) {
                                o = Quantity.NaN;
                            } else {
                                potentialError(flags, "invalidsharpc", bv, is);
                                o = VOID;
                            }
                        } else {
                            return skipMultilineComment(is, state, def, radix, flags, anns);
                        }
                        break;
                    case '%':
                        String bv = lexer.readToBreak(is, Lexer.special, false, false).toLowerCase();
                        Syntax s = (Syntax) CompilerConstants.SYNTACTIC_TOKENS.get(bv);
                        if (s == null) {
                            potentialError(flags, "invalidsyntoken", bv, is);
                            s = (Syntax) CompilerConstants.SYNTACTIC_TOKENS.get("unknown");
                        }
                        o = s;
                        break;
                    case '\'':
                        o = listSpecial(SYNTAX, is, state, def, flags, anns);
                        break;
                    case '@':
                        Pair p = (Pair) nextExpression(is, state, flags, anns);
                        o = new AnnotatedExpr(p.cdr(), p.car());
                        break;
                    case '|':
                        return skipMultilineComment(is, state, def, radix, flags, anns);
                    case '{':
                        return readHashExtension(is, state, def, radix, flags, anns, '{', '}');
                    case '[':
                        return readHashExtension(is, state, def, radix, flags, anns, '[', ']');
                    case ':':
                        if (guileMode(flags)) {
                            return nextExpression(is, state, flags, anns);
                        }
                    default:
                        Value[] v = null;
                        is.unread(c);
                        if (Character.isDigit((char) c)) {
                            Integer ref = new Integer(Integer.parseInt(lexer.readToBreak(is, Lexer.sharp_special, false, false)));
                            c = is.read();
                            if (c == '=') {
                                o = _nextExpression(is, state, ref, flags, anns);
                                break;
                            } else if (c == '#') {
                                o = state.get(ref);
                                break;
                            } else {
                                is.unread(c);
                                v = new Value[ref.intValue()];
                            }
                        }
                        SchemeVector iv;
                        if (produceImmutables(flags)) {
                            iv = new ImmutableVector();
                        } else {
                            iv = new SchemeVector();
                        }
                        o = iv;
                        if (def != null) {
                            state.put(def, iv);
                        }
                        def = null;
                        Object expr = _nextExpression(is, state, def, flags | READING_VECTOR, anns);
                        if (expr instanceof AnnotatedExpr) {
                            o = new AnnotatedExpr(iv, ((AnnotatedExpr) expr).annotation);
                            expr = ((AnnotatedExpr) expr).expr;
                        }
                        if (expr == null && v == null) {
                            iv.vals = ZV;
                            break;
                        } else if (expr instanceof Pair) {
                            if (v == null) {
                                v = new Value[length((Pair) expr)];
                            } else if (v.length < length((Pair) expr)) {
                                warn("veclengthtooshort", is);
                                v = new Value[length((Pair) expr)];
                            }
                        } else if (expr != null) {
                            throw new IOException(liMessage(SISCB, "invalidsharp", expr.toString()));
                        }
                        p = (Pair) expr;
                        Object lastObject = Quantity.ZERO;
                        for (int i = 0; i < v.length; i++) {
                            if (p != EMPTYLIST) {
                                lastObject = p.car();
                                p = (Pair) p.cdr();
                            }
                            v[i] = lastValue(state, lastObject);
                        }
                        iv.vals = v;
                        break;
                }
                break;
            default:
                potentialError(flags, "unknowntoken", is);
                o = VOID;
        }
        if (def != null) {
            state.put(def, o);
        }
        return o;
    }

    private Object skipMultilineComment(PushbackReader is, HashMap state, Integer def, int radix, int flags, Pair anns) throws IOException {
        lexer.skipMultilineComment(is, flags);
        return _nextExpression(is, state, def, radix, flags, anns);
    }

    private Object readHashExtension(PushbackReader is, HashMap state, Integer def, int radix, int flags, Pair anns, char extStart, char extEnd) throws IOException {
        return new SchemeString("#" + extStart + lexer.readHashExtension(is, flags, extEnd));
    }

    private Value readAfterDot(PushbackReader is, HashMap state, int flags, Pair anns) throws IOException {
        Object l = _nextExpression(is, state, null, flags, anns);
        Value v = VOID;
        try {
            v = lastValue(state, l);
        } catch (ClassCastException cce) {
            potentialError(flags, "expectedexprincdr", is);
            if (l == ENDPAIR) {
                return EMPTYLIST;
            }
        }
        if (_nextExpression(is, state, null, flags, anns) == ENDPAIR) {
            return v;
        }
        potentialError(flags, "toomanyafterdot", is);
        while (_nextExpression(is, state, null, flags, anns) != ENDPAIR) {
        }
        return v;
    }

    public Value readList(PushbackReader is, HashMap state, Integer def, int flags, Pair anns) throws IOException {
        Pair h = null;
        Pair p = null;
        boolean readingVector = readingVector(flags);
        boolean produceImmutables = produceImmutables(flags);
        flags &= ~READING_VECTOR;
        try {
            Object l = _nextExpression(is, state, null, flags, anns);
            if (l == ENDPAIR) {
                return EMPTYLIST;
            }
            if (l == DOT) {
                l = _nextExpression(is, state, null, flags, anns);
            }
            Value v = VOID;
            try {
                v = lastValue(state, l);
            } catch (ClassCastException cce) {
                potentialError(flags, "expectedexprincar", is);
            }
            h = p = (produceImmutables ? new ImmutablePair(v, EMPTYLIST, false) : new Pair(v, EMPTYLIST));
            if (def != null) {
                state.put(def, p);
            }
            while (true) {
                l = _nextExpression(is, state, null, flags, anns);
                if (l == ENDPAIR) {
                    break;
                } else if (l == DOT) {
                    if (readingVector) {
                        potentialError(flags, "dotwhenreadingvector", is);
                    }
                    p.setCdr(readAfterDot(is, state, flags, anns));
                    if (produceImmutables) {
                        ((ImmutablePair) p).makeImmutable();
                    }
                    break;
                } else {
                    try {
                        v = lastValue(state, l);
                    } catch (ClassCastException cce) {
                        potentialError(flags, "expectedexprincar", is);
                        v = VOID;
                    }
                    Pair pp = (produceImmutables ? new ImmutablePair(v, EMPTYLIST, false) : new Pair(v, EMPTYLIST));
                    p.setCdr(pp);
                    if (produceImmutables) {
                        ((ImmutablePair) p).makeImmutable();
                    }
                    p = pp;
                }
            }
            if (produceImmutables) {
                ((ImmutablePair) p).makeImmutable();
            }
        } catch (EOFException e) {
            potentialError(flags, "unexpectedeof", is);
            return VOID;
        }
        return h;
    }

    protected final boolean caseSensitive(int flags) {
        return (flags & CASE_SENSITIVE) != 0;
    }

    protected final boolean produceAnnotations(int flags) {
        return (flags & PRODUCE_ANNOTATIONS) != 0;
    }

    protected final boolean produceImmutables(int flags) {
        return (flags & PRODUCE_IMMUTABLES) != 0;
    }

    protected final boolean readingVector(int flags) {
        return (flags & READING_VECTOR) != 0;
    }

    protected final boolean permissiveParsing(int flags) {
        return (flags & PERMISSIVE_PARSING) != 0;
    }

    public static final boolean guileMode(int flags) {
        return (flags & GUILE_MODE) != 0;
    }

    public static void main(String[] args) throws Exception {
        Parser p = new Parser(new Lexer());
        PushbackReader is = new PushbackReader(new InputStreamReader(System.in));
        Expression e;
        while (EOF != (e = p.nextExpression(is, PERMISSIVE_PARSING, EMPTYLIST))) {
            System.err.println(e);
        }
    }
}
