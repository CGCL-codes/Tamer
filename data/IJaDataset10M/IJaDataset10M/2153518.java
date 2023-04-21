package org.sepa.commons.grammar.importers.edu;

import java.io.IOException;
import java.io.StringReader;
import org.sepa.commons.exceptions.SepaRuleNotAllowedException;
import java.util.TreeSet;
import java.util.Set;
import org.sepa.commons.grammar.SymbolSequence;
import org.sepa.commons.grammar.SepaSymbolSequence;
import org.sepa.commons.grammar.SepaRule;
import org.sepa.commons.grammar.SepaTerminalSymbol;
import org.sepa.commons.grammar.SepaNonTerminalSymbol;
import org.sepa.commons.grammar.Grammar;
import org.sepa.commons.grammar.importers.Importer;
import org.sepa.commons.grammar.importers.SepaSyntaxErrorException;

public class EduImporter implements Importer {

    boolean yydebug;

    int yynerrs;

    int yyerrflag;

    int yychar;

    void debug(String msg) {
        if (yydebug) System.out.println(msg);
    }

    static final int YYSTACKSIZE = 500;

    int statestk[] = new int[YYSTACKSIZE];

    int stateptr;

    int stateptrmax;

    int statemax;

    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }

    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }

    void dump_stacks(int count) {
        int i;
        System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
        for (i = 0; i < count; i++) System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
        System.out.println("======================");
    }

    String yytext;

    Object yyval;

    Object yylval;

    Object valstk[] = new Object[YYSTACKSIZE];

    int valptr;

    final void val_init() {
        yyval = new Object();
        yylval = new Object();
        valptr = -1;
    }

    final void val_push(Object val) {
        try {
            valptr++;
            valstk[valptr] = val;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = valstk.length;
            int newsize = oldsize * 2;
            Object[] newstack = new Object[newsize];
            System.arraycopy(valstk, 0, newstack, 0, oldsize);
            valstk = newstack;
            valstk[valptr] = val;
        }
    }

    final Object val_pop() {
        return valstk[valptr--];
    }

    final void val_drop(int cnt) {
        valptr -= cnt;
    }

    final Object val_peek(int relative) {
        return valstk[valptr - relative];
    }

    final Object dup_yyval(Object val) {
        return val;
    }

    public static final short TERMINAL = 257;

    public static final short NONTERMINAL = 258;

    public static final short ARROW = 259;

    public static final short PIPE = 260;

    public static final short YYERRCODE = 256;

    static final short yylhs[] = { -1, 0, 0, 1, 3, 4, 4, 2, 2, 2 };

    static final short yylen[] = { 2, 0, 2, 3, 3, 2, 0, 2, 2, 0 };

    static final short yydefred[] = { 0, 0, 0, 0, 2, 7, 8, 9, 0, 3, 0, 9, 4, 5 };

    static final short yydgoto[] = { 1, 2, 3, 9, 12 };

    static final short yysindex[] = { 0, 0, 0, -250, 0, 0, 0, 0, -10, 0, -257, 0, 0, 0 };

    static final short yyrindex[] = { 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0 };

    static final short yygindex[] = { 3, 0, -5, -1, 0 };

    static final int YYTABLESIZE = 263;

    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[] { 10, 1, 8, 11, 6, 4, 8, 5, 6, 7, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 6, 6, 6 };
    }

    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[] { 10, 0, 7, 260, 0, 2, 11, 257, 258, 259, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 257, 258, -1, -1, -1, -1, -1, -1, -1, -1, -1, 257, 258, 259, 257, 258, 259 };
    }

    static final short YYFINAL = 1;

    static final short YYMAXTOKEN = 260;

    static final String yyname[] = { "end-of-file", null, null, null, null, null, null, null, null, null, "'\\n'", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "TERMINAL", "NONTERMINAL", "ARROW", "PIPE" };

    static final String yyrule[] = { "$accept : grammar", "grammar :", "grammar : rule grammar", "rule : side ARROW right", "right : side '\\n' rightrest", "rightrest : PIPE right", "rightrest :", "side : side TERMINAL", "side : side NONTERMINAL", "side :" };

    Yylex lex;

    Grammar grammar;

    String input;

    int yylex() throws IOException {
        return lex.yylex();
    }

    void yyerror(String error) throws SepaSyntaxErrorException {
        throw new SepaSyntaxErrorException(lex.yyRow(), lex.yyCol(), input.split("\n")[lex.yyRow()], yytext);
    }

    public Grammar getGrammar(String grammarText, Grammar grammar) throws SepaRuleNotAllowedException, IOException, SepaSyntaxErrorException {
        lex = new Yylex(new StringReader(grammarText));
        this.grammar = grammar;
        this.input = grammarText;
        this.yyparse();
        return this.grammar;
    }

    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) ch = 0;
        if (ch <= YYMAXTOKEN) s = yyname[ch];
        if (s == null) s = "illegal-symbol";
        debug("state " + state + ", reading " + ch + " (" + s + ")");
    }

    int yyn;

    int yym;

    int yystate;

    String yys;

    int yyparse() throws IOException, SepaRuleNotAllowedException, SepaSyntaxErrorException {
        boolean doaction;
        init_stacks();
        yynerrs = 0;
        yyerrflag = 0;
        yychar = -1;
        yystate = 0;
        state_push(yystate);
        val_push(yylval);
        while (true) {
            doaction = true;
            if (yydebug) debug("loop");
            for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
                if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
                if (yychar < 0) {
                    yychar = yylex();
                    if (yydebug) debug(" next yychar:" + yychar);
                    if (yychar < 0) {
                        yychar = 0;
                        if (yydebug) yylexdebug(yystate, yychar);
                    }
                }
                yyn = yysindex[yystate];
                if ((yyn != 0) && (yyn += yychar) >= 0 && yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug) debug("state " + yystate + ", shifting to state " + yytable[yyn]);
                    yystate = yytable[yyn];
                    state_push(yystate);
                    val_push(yylval);
                    yychar = -1;
                    if (yyerrflag > 0) --yyerrflag;
                    doaction = false;
                    break;
                }
                yyn = yyrindex[yystate];
                if ((yyn != 0) && (yyn += yychar) >= 0 && yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
                    if (yydebug) debug("reduce");
                    yyn = yytable[yyn];
                    doaction = true;
                    break;
                } else {
                    if (yyerrflag == 0) {
                        yyerror("syntax error");
                        yynerrs++;
                    }
                    if (yyerrflag < 3) {
                        yyerrflag = 3;
                        while (true) {
                            if (stateptr < 0) {
                                yyerror("stack underflow. aborting...");
                                return 1;
                            }
                            yyn = yysindex[state_peek(0)];
                            if ((yyn != 0) && (yyn += YYERRCODE) >= 0 && yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                                if (yydebug) debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                                yystate = yytable[yyn];
                                state_push(yystate);
                                val_push(yylval);
                                doaction = false;
                                break;
                            } else {
                                if (yydebug) debug("error recovery discarding state " + state_peek(0) + " ");
                                if (stateptr < 0) {
                                    yyerror("Stack underflow. aborting...");
                                    return 1;
                                }
                                state_pop();
                                val_pop();
                            }
                        }
                    } else {
                        if (yychar == 0) return 1;
                        if (yydebug) {
                            yys = null;
                            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                            if (yys == null) yys = "illegal-symbol";
                            debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
                        }
                        yychar = -1;
                    }
                }
            }
            if (!doaction) continue;
            yym = yylen[yyn];
            if (yydebug) debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
            if (yym > 0) yyval = val_peek(yym - 1);
            yyval = dup_yyval(yyval);
            switch(yyn) {
                case 3:
                    {
                        SymbolSequence lhs = (SymbolSequence) val_peek(2);
                        for (SymbolSequence rhs : (Set<SymbolSequence>) val_peek(0)) {
                            grammar.addRule(new SepaRule((SymbolSequence) lhs.clone(), rhs));
                        }
                    }
                    break;
                case 4:
                    {
                        yyval = val_peek(0);
                        ((Set<SymbolSequence>) yyval).add((SymbolSequence) val_peek(2));
                    }
                    break;
                case 5:
                    {
                        yyval = val_peek(0);
                    }
                    break;
                case 6:
                    {
                        yyval = new TreeSet<SymbolSequence>();
                    }
                    break;
                case 7:
                    {
                        ((SymbolSequence) val_peek(1)).add(new SepaTerminalSymbol(yytext));
                        yyval = val_peek(1);
                    }
                    break;
                case 8:
                    {
                        ((SymbolSequence) val_peek(1)).add(new SepaNonTerminalSymbol(yytext));
                        yyval = val_peek(1);
                    }
                    break;
                case 9:
                    {
                        yyval = new SepaSymbolSequence();
                    }
                    break;
            }
            if (yydebug) debug("reduce");
            state_drop(yym);
            yystate = state_peek(0);
            val_drop(yym);
            yym = yylhs[yyn];
            if (yystate == 0 && yym == 0) {
                if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
                yystate = YYFINAL;
                state_push(YYFINAL);
                val_push(yyval);
                if (yychar < 0) {
                    yychar = yylex();
                    if (yychar < 0) yychar = 0;
                    if (yydebug) yylexdebug(yystate, yychar);
                }
                if (yychar == 0) break;
            } else {
                yyn = yygindex[yym];
                if ((yyn != 0) && (yyn += yystate) >= 0 && yyn <= YYTABLESIZE && yycheck[yyn] == yystate) yystate = yytable[yyn]; else yystate = yydgoto[yym];
                if (yydebug) debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
                state_push(yystate);
                val_push(yyval);
            }
        }
        return 0;
    }

    /**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
    public EduImporter() {
    }

    /**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
    public EduImporter(boolean debugMe) {
        yydebug = debugMe;
    }
}
