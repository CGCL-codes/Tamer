package support.translate.parser;

@SuppressWarnings("all")
public class Translator implements TranslatorTreeConstants, TranslatorConstants {

    protected static JJTTranslatorState jjtree = new JJTTranslatorState();

    public static final SimpleNode Start() throws ParseException {
        ASTStart jjtn000 = new ASTStart(JJTSTART);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        try {
            label_1: while (true) {
                entry();
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case KEY:
                    case CLASS:
                        ;
                        break;
                    default:
                        jj_la1[0] = jj_gen;
                        break label_1;
                }
            }
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            {
                if (true) return jjtn000;
            }
        } catch (Throwable jjte000) {
            if (jjtc000) {
                jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte000;
                }
            }
            if (jjte000 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte000;
                }
            }
            {
                if (true) throw (Error) jjte000;
            }
        } finally {
            if (jjtc000) {
                jjtree.closeNodeScope(jjtn000, true);
            }
        }
        throw new Error("Missing return statement in function");
    }

    public static final void entry() throws ParseException {
        Token e;
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case KEY:
                e = jj_consume_token(KEY);
                simpleEntry(e);
                break;
            case CLASS:
                e = jj_consume_token(CLASS);
                readClass(e);
                break;
            default:
                jj_la1[1] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public static final void simpleEntry(Token e) throws ParseException {
        ASTDefinition jjtn000 = new ASTDefinition(JJTDEFINITION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        Token t = null;
        try {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case ENTRY:
                    t = jj_consume_token(ENTRY);
                    break;
                default:
                    jj_la1[2] = jj_gen;
                    ;
            }
            jjtree.closeNodeScope(jjtn000, true);
            jjtc000 = false;
            jjtn000.setKey(e);
            jjtn000.setEntry(t);
        } finally {
            if (jjtc000) {
                jjtree.closeNodeScope(jjtn000, true);
            }
        }
    }

    public static final void readClass(Token t) throws ParseException {
        ASTDefinitionClass jjtn000 = new ASTDefinitionClass(JJTDEFINITIONCLASS);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);
        try {
            jjtn000.setName(t);
            jjtn000.setLine(t.endLine);
            label_2: while (true) {
                switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                    case KEY:
                    case CLASS:
                        ;
                        break;
                    default:
                        jj_la1[3] = jj_gen;
                        break label_2;
                }
                entry();
            }
            jj_consume_token(CLOSE_CLASS);
        } catch (Throwable jjte000) {
            if (jjtc000) {
                jjtree.clearNodeScope(jjtn000);
                jjtc000 = false;
            } else {
                jjtree.popNode();
            }
            if (jjte000 instanceof RuntimeException) {
                {
                    if (true) throw (RuntimeException) jjte000;
                }
            }
            if (jjte000 instanceof ParseException) {
                {
                    if (true) throw (ParseException) jjte000;
                }
            }
            {
                if (true) throw (Error) jjte000;
            }
        } finally {
            if (jjtc000) {
                jjtree.closeNodeScope(jjtn000, true);
            }
        }
    }

    private static boolean jj_initialized_once = false;

    public static TranslatorTokenManager token_source;

    static JavaCharStream jj_input_stream;

    public static Token token, jj_nt;

    private static int jj_ntk;

    private static int jj_gen;

    private static final int[] jj_la1 = new int[4];

    private static int[] jj_la1_0;

    static {
        jj_la1_0();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[] { 0x300, 0x300, 0x400, 0x300 };
    }

    public Translator(java.io.InputStream stream) {
        this(stream, null);
    }

    public Translator(java.io.InputStream stream, String encoding) {
        if (jj_initialized_once) {
            System.out.println("ERROR: Second call to constructor of static parser.  ");
            System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        jj_initialized_once = true;
        try {
            jj_input_stream = new JavaCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new TranslatorTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 4; i++) jj_la1[i] = -1;
    }

    public static void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    public static void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jjtree.reset();
        jj_gen = 0;
        for (int i = 0; i < 4; i++) jj_la1[i] = -1;
    }

    public Translator(java.io.Reader stream) {
        if (jj_initialized_once) {
            System.out.println("ERROR: Second call to constructor of static parser. ");
            System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        jj_initialized_once = true;
        jj_input_stream = new JavaCharStream(stream, 1, 1);
        token_source = new TranslatorTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 4; i++) jj_la1[i] = -1;
    }

    public static void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jjtree.reset();
        jj_gen = 0;
        for (int i = 0; i < 4; i++) jj_la1[i] = -1;
    }

    public Translator(TranslatorTokenManager tm) {
        if (jj_initialized_once) {
            System.out.println("ERROR: Second call to constructor of static parser. ");
            System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
            System.out.println("       during parser generation.");
            throw new Error();
        }
        jj_initialized_once = true;
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 4; i++) jj_la1[i] = -1;
    }

    public void ReInit(TranslatorTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jjtree.reset();
        jj_gen = 0;
        for (int i = 0; i < 4; i++) jj_la1[i] = -1;
    }

    private static final Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    public static final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public static final Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private static final int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private static java.util.Vector<int[]> jj_expentries = new java.util.Vector<int[]>();

    private static int[] jj_expentry;

    private static int jj_kind = -1;

    public static ParseException generateParseException() {
        jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[11];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 4; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 11; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }
        }
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.elementAt(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    public static final void enable_tracing() {
    }

    public static final void disable_tracing() {
    }
}
