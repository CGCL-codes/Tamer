package com.narirelays.ems.utils.Interpreters.Excel;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.tree.*;

@SuppressWarnings({ "all", "warnings", "unchecked" })
public class ExcelTestParser extends Parser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "BINARY", "CALL", "CELL", "CELLRANGE", "DECIMAL", "E", "EscapeSequence", "FIXEDCELL", "FIXEDCELLRANGE", "HEXADECIMAL", "IDENTIFIER", "NOT", "OCTAL", "PI", "REAL", "REMOTECELL", "REMOTECELLRANGE", "STRING", "WS", "'%'", "'('", "')'", "'*'", "'+'", "','", "'-'", "'/'", "';'", "'^'" };

    public static final int EOF = -1;

    public static final int T__23 = 23;

    public static final int T__24 = 24;

    public static final int T__25 = 25;

    public static final int T__26 = 26;

    public static final int T__27 = 27;

    public static final int T__28 = 28;

    public static final int T__29 = 29;

    public static final int T__30 = 30;

    public static final int T__31 = 31;

    public static final int T__32 = 32;

    public static final int BINARY = 4;

    public static final int CALL = 5;

    public static final int CELL = 6;

    public static final int CELLRANGE = 7;

    public static final int DECIMAL = 8;

    public static final int E = 9;

    public static final int EscapeSequence = 10;

    public static final int FIXEDCELL = 11;

    public static final int FIXEDCELLRANGE = 12;

    public static final int HEXADECIMAL = 13;

    public static final int IDENTIFIER = 14;

    public static final int NOT = 15;

    public static final int OCTAL = 16;

    public static final int PI = 17;

    public static final int REAL = 18;

    public static final int REMOTECELL = 19;

    public static final int REMOTECELLRANGE = 20;

    public static final int STRING = 21;

    public static final int WS = 22;

    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    public ExcelTestParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public ExcelTestParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[29 + 1];
    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() {
        return ExcelTestParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "E:\\workspace\\java\\ems\\src\\com\\narirelays\\ems\\utils\\Interpreters\\Excel\\ExcelTest.g";
    }

    public static class expressions_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final ExcelTestParser.expressions_return expressions() throws RecognitionException {
        ExcelTestParser.expressions_return retval = new ExcelTestParser.expressions_return();
        retval.start = input.LT(1);
        int expressions_StartIndex = input.index();
        CommonTree root_0 = null;
        Token EOF2 = null;
        ExcelTestParser.expr_return expr1 = null;
        CommonTree EOF2_tree = null;
        RewriteRuleTokenStream stream_EOF = new RewriteRuleTokenStream(adaptor, "token EOF");
        RewriteRuleSubtreeStream stream_expr = new RewriteRuleSubtreeStream(adaptor, "rule expr");
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 1)) {
                return retval;
            }
            {
                pushFollow(FOLLOW_expr_in_expressions127);
                expr1 = expr();
                state._fsp--;
                if (state.failed) return retval;
                if (state.backtracking == 0) stream_expr.add(expr1.getTree());
                EOF2 = (Token) match(input, EOF, FOLLOW_EOF_in_expressions129);
                if (state.failed) return retval;
                if (state.backtracking == 0) stream_EOF.add(EOF2);
                if (state.backtracking == 0) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.tree : null);
                    root_0 = (CommonTree) adaptor.nil();
                    {
                        adaptor.addChild(root_0, stream_expr.nextTree());
                    }
                    retval.tree = root_0;
                }
            }
            retval.stop = input.LT(-1);
            if (state.backtracking == 0) {
                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 1, expressions_StartIndex);
            }
        }
        return retval;
    }

    public static class expr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final ExcelTestParser.expr_return expr() throws RecognitionException {
        ExcelTestParser.expr_return retval = new ExcelTestParser.expr_return();
        retval.start = input.LT(1);
        int expr_StartIndex = input.index();
        CommonTree root_0 = null;
        Token char_literal4 = null;
        Token char_literal5 = null;
        ExcelTestParser.multExpr_return multExpr3 = null;
        ExcelTestParser.multExpr_return multExpr6 = null;
        CommonTree char_literal4_tree = null;
        CommonTree char_literal5_tree = null;
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 2)) {
                return retval;
            }
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_multExpr_in_expr144);
                multExpr3 = multExpr();
                state._fsp--;
                if (state.failed) return retval;
                if (state.backtracking == 0) adaptor.addChild(root_0, multExpr3.getTree());
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if ((LA2_0 == 27 || LA2_0 == 29)) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                int alt1 = 2;
                                int LA1_0 = input.LA(1);
                                if ((LA1_0 == 27)) {
                                    alt1 = 1;
                                } else if ((LA1_0 == 29)) {
                                    alt1 = 2;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException("", 1, 0, input);
                                    throw nvae;
                                }
                                switch(alt1) {
                                    case 1:
                                        {
                                            char_literal4 = (Token) match(input, 27, FOLLOW_27_in_expr148);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal4_tree = (CommonTree) adaptor.create(char_literal4);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal4_tree, root_0);
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            char_literal5 = (Token) match(input, 29, FOLLOW_29_in_expr151);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal5_tree = (CommonTree) adaptor.create(char_literal5);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal5_tree, root_0);
                                            }
                                        }
                                        break;
                                }
                                pushFollow(FOLLOW_multExpr_in_expr155);
                                multExpr6 = multExpr();
                                state._fsp--;
                                if (state.failed) return retval;
                                if (state.backtracking == 0) adaptor.addChild(root_0, multExpr6.getTree());
                            }
                            break;
                        default:
                            break loop2;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            if (state.backtracking == 0) {
                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 2, expr_StartIndex);
            }
        }
        return retval;
    }

    public static class multExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final ExcelTestParser.multExpr_return multExpr() throws RecognitionException {
        ExcelTestParser.multExpr_return retval = new ExcelTestParser.multExpr_return();
        retval.start = input.LT(1);
        int multExpr_StartIndex = input.index();
        CommonTree root_0 = null;
        Token char_literal8 = null;
        Token char_literal9 = null;
        Token char_literal10 = null;
        ExcelTestParser.powe_return powe7 = null;
        ExcelTestParser.powe_return powe11 = null;
        CommonTree char_literal8_tree = null;
        CommonTree char_literal9_tree = null;
        CommonTree char_literal10_tree = null;
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 3)) {
                return retval;
            }
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_powe_in_multExpr166);
                powe7 = powe();
                state._fsp--;
                if (state.failed) return retval;
                if (state.backtracking == 0) adaptor.addChild(root_0, powe7.getTree());
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);
                    if ((LA4_0 == 23 || LA4_0 == 26 || LA4_0 == 30)) {
                        alt4 = 1;
                    }
                    switch(alt4) {
                        case 1:
                            {
                                int alt3 = 3;
                                switch(input.LA(1)) {
                                    case 26:
                                        {
                                            alt3 = 1;
                                        }
                                        break;
                                    case 30:
                                        {
                                            alt3 = 2;
                                        }
                                        break;
                                    case 23:
                                        {
                                            alt3 = 3;
                                        }
                                        break;
                                    default:
                                        if (state.backtracking > 0) {
                                            state.failed = true;
                                            return retval;
                                        }
                                        NoViableAltException nvae = new NoViableAltException("", 3, 0, input);
                                        throw nvae;
                                }
                                switch(alt3) {
                                    case 1:
                                        {
                                            char_literal8 = (Token) match(input, 26, FOLLOW_26_in_multExpr170);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal8_tree = (CommonTree) adaptor.create(char_literal8);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal8_tree, root_0);
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            char_literal9 = (Token) match(input, 30, FOLLOW_30_in_multExpr173);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal9_tree = (CommonTree) adaptor.create(char_literal9);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal9_tree, root_0);
                                            }
                                        }
                                        break;
                                    case 3:
                                        {
                                            char_literal10 = (Token) match(input, 23, FOLLOW_23_in_multExpr176);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal10_tree = (CommonTree) adaptor.create(char_literal10);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal10_tree, root_0);
                                            }
                                        }
                                        break;
                                }
                                pushFollow(FOLLOW_powe_in_multExpr180);
                                powe11 = powe();
                                state._fsp--;
                                if (state.failed) return retval;
                                if (state.backtracking == 0) adaptor.addChild(root_0, powe11.getTree());
                            }
                            break;
                        default:
                            break loop4;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            if (state.backtracking == 0) {
                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 3, multExpr_StartIndex);
            }
        }
        return retval;
    }

    public static class powe_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final ExcelTestParser.powe_return powe() throws RecognitionException {
        ExcelTestParser.powe_return retval = new ExcelTestParser.powe_return();
        retval.start = input.LT(1);
        int powe_StartIndex = input.index();
        CommonTree root_0 = null;
        Token char_literal13 = null;
        ExcelTestParser.atom_return atom12 = null;
        ExcelTestParser.atom_return atom14 = null;
        CommonTree char_literal13_tree = null;
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 4)) {
                return retval;
            }
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_atom_in_powe193);
                atom12 = atom();
                state._fsp--;
                if (state.failed) return retval;
                if (state.backtracking == 0) adaptor.addChild(root_0, atom12.getTree());
                loop5: do {
                    int alt5 = 2;
                    int LA5_0 = input.LA(1);
                    if ((LA5_0 == 32)) {
                        alt5 = 1;
                    }
                    switch(alt5) {
                        case 1:
                            {
                                char_literal13 = (Token) match(input, 32, FOLLOW_32_in_powe196);
                                if (state.failed) return retval;
                                if (state.backtracking == 0) {
                                    char_literal13_tree = (CommonTree) adaptor.create(char_literal13);
                                    root_0 = (CommonTree) adaptor.becomeRoot(char_literal13_tree, root_0);
                                }
                                pushFollow(FOLLOW_atom_in_powe199);
                                atom14 = atom();
                                state._fsp--;
                                if (state.failed) return retval;
                                if (state.backtracking == 0) adaptor.addChild(root_0, atom14.getTree());
                            }
                            break;
                        default:
                            break loop5;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            if (state.backtracking == 0) {
                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 4, powe_StartIndex);
            }
        }
        return retval;
    }

    public static class atom_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final ExcelTestParser.atom_return atom() throws RecognitionException {
        ExcelTestParser.atom_return retval = new ExcelTestParser.atom_return();
        retval.start = input.LT(1);
        int atom_StartIndex = input.index();
        CommonTree root_0 = null;
        Token BINARY15 = null;
        Token OCTAL16 = null;
        Token DECIMAL17 = null;
        Token HEXADECIMAL18 = null;
        Token REAL19 = null;
        Token E20 = null;
        Token STRING21 = null;
        Token IDENTIFIER22 = null;
        Token char_literal23 = null;
        Token char_literal24 = null;
        Token IDENTIFIER25 = null;
        Token char_literal26 = null;
        Token char_literal28 = null;
        Token char_literal29 = null;
        Token char_literal31 = null;
        Token CELL32 = null;
        Token CELLRANGE33 = null;
        Token FIXEDCELL34 = null;
        Token FIXEDCELLRANGE35 = null;
        Token REMOTECELL36 = null;
        Token REMOTECELLRANGE37 = null;
        ExcelTestParser.expseq_return expseq27 = null;
        ExcelTestParser.expr_return expr30 = null;
        CommonTree BINARY15_tree = null;
        CommonTree OCTAL16_tree = null;
        CommonTree DECIMAL17_tree = null;
        CommonTree HEXADECIMAL18_tree = null;
        CommonTree REAL19_tree = null;
        CommonTree E20_tree = null;
        CommonTree STRING21_tree = null;
        CommonTree IDENTIFIER22_tree = null;
        CommonTree char_literal23_tree = null;
        CommonTree char_literal24_tree = null;
        CommonTree IDENTIFIER25_tree = null;
        CommonTree char_literal26_tree = null;
        CommonTree char_literal28_tree = null;
        CommonTree char_literal29_tree = null;
        CommonTree char_literal31_tree = null;
        CommonTree CELL32_tree = null;
        CommonTree CELLRANGE33_tree = null;
        CommonTree FIXEDCELL34_tree = null;
        CommonTree FIXEDCELLRANGE35_tree = null;
        CommonTree REMOTECELL36_tree = null;
        CommonTree REMOTECELLRANGE37_tree = null;
        RewriteRuleTokenStream stream_24 = new RewriteRuleTokenStream(adaptor, "token 24");
        RewriteRuleTokenStream stream_IDENTIFIER = new RewriteRuleTokenStream(adaptor, "token IDENTIFIER");
        RewriteRuleTokenStream stream_25 = new RewriteRuleTokenStream(adaptor, "token 25");
        RewriteRuleSubtreeStream stream_expr = new RewriteRuleSubtreeStream(adaptor, "rule expr");
        RewriteRuleSubtreeStream stream_expseq = new RewriteRuleSubtreeStream(adaptor, "rule expseq");
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 5)) {
                return retval;
            }
            int alt6 = 16;
            switch(input.LA(1)) {
                case BINARY:
                    {
                        alt6 = 1;
                    }
                    break;
                case OCTAL:
                    {
                        alt6 = 2;
                    }
                    break;
                case DECIMAL:
                    {
                        alt6 = 3;
                    }
                    break;
                case HEXADECIMAL:
                    {
                        alt6 = 4;
                    }
                    break;
                case REAL:
                    {
                        alt6 = 5;
                    }
                    break;
                case E:
                    {
                        alt6 = 6;
                    }
                    break;
                case STRING:
                    {
                        alt6 = 7;
                    }
                    break;
                case IDENTIFIER:
                    {
                        int LA6_8 = input.LA(2);
                        if ((LA6_8 == 24)) {
                            int LA6_16 = input.LA(3);
                            if ((LA6_16 == 25)) {
                                alt6 = 8;
                            } else if ((LA6_16 == BINARY || (LA6_16 >= CELL && LA6_16 <= E) || (LA6_16 >= FIXEDCELL && LA6_16 <= IDENTIFIER) || LA6_16 == OCTAL || (LA6_16 >= REAL && LA6_16 <= STRING) || LA6_16 == 24)) {
                                alt6 = 9;
                            } else {
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return retval;
                                }
                                NoViableAltException nvae = new NoViableAltException("", 6, 16, input);
                                throw nvae;
                            }
                        } else {
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return retval;
                            }
                            NoViableAltException nvae = new NoViableAltException("", 6, 8, input);
                            throw nvae;
                        }
                    }
                    break;
                case 24:
                    {
                        alt6 = 10;
                    }
                    break;
                case CELL:
                    {
                        alt6 = 11;
                    }
                    break;
                case CELLRANGE:
                    {
                        alt6 = 12;
                    }
                    break;
                case FIXEDCELL:
                    {
                        alt6 = 13;
                    }
                    break;
                case FIXEDCELLRANGE:
                    {
                        alt6 = 14;
                    }
                    break;
                case REMOTECELL:
                    {
                        alt6 = 15;
                    }
                    break;
                case REMOTECELLRANGE:
                    {
                        alt6 = 16;
                    }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 6, 0, input);
                    throw nvae;
            }
            switch(alt6) {
                case 1:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        BINARY15 = (Token) match(input, BINARY, FOLLOW_BINARY_in_atom211);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            BINARY15_tree = (CommonTree) adaptor.create(BINARY15);
                            adaptor.addChild(root_0, BINARY15_tree);
                        }
                    }
                    break;
                case 2:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        OCTAL16 = (Token) match(input, OCTAL, FOLLOW_OCTAL_in_atom216);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            OCTAL16_tree = (CommonTree) adaptor.create(OCTAL16);
                            adaptor.addChild(root_0, OCTAL16_tree);
                        }
                    }
                    break;
                case 3:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        DECIMAL17 = (Token) match(input, DECIMAL, FOLLOW_DECIMAL_in_atom221);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            DECIMAL17_tree = (CommonTree) adaptor.create(DECIMAL17);
                            adaptor.addChild(root_0, DECIMAL17_tree);
                        }
                    }
                    break;
                case 4:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        HEXADECIMAL18 = (Token) match(input, HEXADECIMAL, FOLLOW_HEXADECIMAL_in_atom226);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            HEXADECIMAL18_tree = (CommonTree) adaptor.create(HEXADECIMAL18);
                            adaptor.addChild(root_0, HEXADECIMAL18_tree);
                        }
                    }
                    break;
                case 5:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        REAL19 = (Token) match(input, REAL, FOLLOW_REAL_in_atom231);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            REAL19_tree = (CommonTree) adaptor.create(REAL19);
                            adaptor.addChild(root_0, REAL19_tree);
                        }
                    }
                    break;
                case 6:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        E20 = (Token) match(input, E, FOLLOW_E_in_atom236);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            E20_tree = (CommonTree) adaptor.create(E20);
                            adaptor.addChild(root_0, E20_tree);
                        }
                    }
                    break;
                case 7:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        STRING21 = (Token) match(input, STRING, FOLLOW_STRING_in_atom241);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            STRING21_tree = (CommonTree) adaptor.create(STRING21);
                            adaptor.addChild(root_0, STRING21_tree);
                        }
                    }
                    break;
                case 8:
                    {
                        IDENTIFIER22 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_atom246);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_IDENTIFIER.add(IDENTIFIER22);
                        char_literal23 = (Token) match(input, 24, FOLLOW_24_in_atom248);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_24.add(char_literal23);
                        char_literal24 = (Token) match(input, 25, FOLLOW_25_in_atom250);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_25.add(char_literal24);
                        if (state.backtracking == 0) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.tree : null);
                            root_0 = (CommonTree) adaptor.nil();
                            {
                                {
                                    CommonTree root_1 = (CommonTree) adaptor.nil();
                                    root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(CALL, "CALL"), root_1);
                                    adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                                    adaptor.addChild(root_0, root_1);
                                }
                            }
                            retval.tree = root_0;
                        }
                    }
                    break;
                case 9:
                    {
                        IDENTIFIER25 = (Token) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_atom264);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_IDENTIFIER.add(IDENTIFIER25);
                        char_literal26 = (Token) match(input, 24, FOLLOW_24_in_atom266);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_24.add(char_literal26);
                        pushFollow(FOLLOW_expseq_in_atom268);
                        expseq27 = expseq();
                        state._fsp--;
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_expseq.add(expseq27.getTree());
                        char_literal28 = (Token) match(input, 25, FOLLOW_25_in_atom270);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_25.add(char_literal28);
                        if (state.backtracking == 0) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.tree : null);
                            root_0 = (CommonTree) adaptor.nil();
                            {
                                {
                                    CommonTree root_1 = (CommonTree) adaptor.nil();
                                    root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(CALL, "CALL"), root_1);
                                    adaptor.addChild(root_1, stream_IDENTIFIER.nextNode());
                                    adaptor.addChild(root_1, stream_expseq.nextTree());
                                    adaptor.addChild(root_0, root_1);
                                }
                            }
                            retval.tree = root_0;
                        }
                    }
                    break;
                case 10:
                    {
                        char_literal29 = (Token) match(input, 24, FOLLOW_24_in_atom285);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_24.add(char_literal29);
                        pushFollow(FOLLOW_expr_in_atom287);
                        expr30 = expr();
                        state._fsp--;
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_expr.add(expr30.getTree());
                        char_literal31 = (Token) match(input, 25, FOLLOW_25_in_atom289);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) stream_25.add(char_literal31);
                        if (state.backtracking == 0) {
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "rule retval", retval != null ? retval.tree : null);
                            root_0 = (CommonTree) adaptor.nil();
                            {
                                adaptor.addChild(root_0, stream_expr.nextTree());
                            }
                            retval.tree = root_0;
                        }
                    }
                    break;
                case 11:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        CELL32 = (Token) match(input, CELL, FOLLOW_CELL_in_atom300);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            CELL32_tree = (CommonTree) adaptor.create(CELL32);
                            adaptor.addChild(root_0, CELL32_tree);
                        }
                    }
                    break;
                case 12:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        CELLRANGE33 = (Token) match(input, CELLRANGE, FOLLOW_CELLRANGE_in_atom305);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            CELLRANGE33_tree = (CommonTree) adaptor.create(CELLRANGE33);
                            adaptor.addChild(root_0, CELLRANGE33_tree);
                        }
                    }
                    break;
                case 13:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        FIXEDCELL34 = (Token) match(input, FIXEDCELL, FOLLOW_FIXEDCELL_in_atom310);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            FIXEDCELL34_tree = (CommonTree) adaptor.create(FIXEDCELL34);
                            adaptor.addChild(root_0, FIXEDCELL34_tree);
                        }
                    }
                    break;
                case 14:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        FIXEDCELLRANGE35 = (Token) match(input, FIXEDCELLRANGE, FOLLOW_FIXEDCELLRANGE_in_atom315);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            FIXEDCELLRANGE35_tree = (CommonTree) adaptor.create(FIXEDCELLRANGE35);
                            adaptor.addChild(root_0, FIXEDCELLRANGE35_tree);
                        }
                    }
                    break;
                case 15:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        REMOTECELL36 = (Token) match(input, REMOTECELL, FOLLOW_REMOTECELL_in_atom320);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            REMOTECELL36_tree = (CommonTree) adaptor.create(REMOTECELL36);
                            adaptor.addChild(root_0, REMOTECELL36_tree);
                        }
                    }
                    break;
                case 16:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        REMOTECELLRANGE37 = (Token) match(input, REMOTECELLRANGE, FOLLOW_REMOTECELLRANGE_in_atom325);
                        if (state.failed) return retval;
                        if (state.backtracking == 0) {
                            REMOTECELLRANGE37_tree = (CommonTree) adaptor.create(REMOTECELLRANGE37);
                            adaptor.addChild(root_0, REMOTECELLRANGE37_tree);
                        }
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            if (state.backtracking == 0) {
                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 5, atom_StartIndex);
            }
        }
        return retval;
    }

    public static class expseq_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final ExcelTestParser.expseq_return expseq() throws RecognitionException {
        ExcelTestParser.expseq_return retval = new ExcelTestParser.expseq_return();
        retval.start = input.LT(1);
        int expseq_StartIndex = input.index();
        CommonTree root_0 = null;
        Token char_literal39 = null;
        Token char_literal40 = null;
        ExcelTestParser.expr_return expr38 = null;
        ExcelTestParser.expr_return expr41 = null;
        CommonTree char_literal39_tree = null;
        CommonTree char_literal40_tree = null;
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 6)) {
                return retval;
            }
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_expr_in_expseq335);
                expr38 = expr();
                state._fsp--;
                if (state.failed) return retval;
                if (state.backtracking == 0) adaptor.addChild(root_0, expr38.getTree());
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if ((LA8_0 == 28 || LA8_0 == 31)) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                int alt7 = 2;
                                int LA7_0 = input.LA(1);
                                if ((LA7_0 == 31)) {
                                    alt7 = 1;
                                } else if ((LA7_0 == 28)) {
                                    alt7 = 2;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return retval;
                                    }
                                    NoViableAltException nvae = new NoViableAltException("", 7, 0, input);
                                    throw nvae;
                                }
                                switch(alt7) {
                                    case 1:
                                        {
                                            char_literal39 = (Token) match(input, 31, FOLLOW_31_in_expseq339);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal39_tree = (CommonTree) adaptor.create(char_literal39);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal39_tree, root_0);
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            char_literal40 = (Token) match(input, 28, FOLLOW_28_in_expseq342);
                                            if (state.failed) return retval;
                                            if (state.backtracking == 0) {
                                                char_literal40_tree = (CommonTree) adaptor.create(char_literal40);
                                                root_0 = (CommonTree) adaptor.becomeRoot(char_literal40_tree, root_0);
                                            }
                                        }
                                        break;
                                }
                                pushFollow(FOLLOW_expr_in_expseq346);
                                expr41 = expr();
                                state._fsp--;
                                if (state.failed) return retval;
                                if (state.backtracking == 0) adaptor.addChild(root_0, expr41.getTree());
                            }
                            break;
                        default:
                            break loop8;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            if (state.backtracking == 0) {
                retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
            retval.tree = (CommonTree) adaptor.errorNode(input, retval.start, input.LT(-1), re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 6, expseq_StartIndex);
            }
        }
        return retval;
    }

    public static final BitSet FOLLOW_expr_in_expressions127 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_expressions129 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_multExpr_in_expr144 = new BitSet(new long[] { 0x0000000028000002L });

    public static final BitSet FOLLOW_27_in_expr148 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_29_in_expr151 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_multExpr_in_expr155 = new BitSet(new long[] { 0x0000000028000002L });

    public static final BitSet FOLLOW_powe_in_multExpr166 = new BitSet(new long[] { 0x0000000044800002L });

    public static final BitSet FOLLOW_26_in_multExpr170 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_30_in_multExpr173 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_23_in_multExpr176 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_powe_in_multExpr180 = new BitSet(new long[] { 0x0000000044800002L });

    public static final BitSet FOLLOW_atom_in_powe193 = new BitSet(new long[] { 0x0000000100000002L });

    public static final BitSet FOLLOW_32_in_powe196 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_atom_in_powe199 = new BitSet(new long[] { 0x0000000100000002L });

    public static final BitSet FOLLOW_BINARY_in_atom211 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_OCTAL_in_atom216 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_DECIMAL_in_atom221 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_HEXADECIMAL_in_atom226 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_REAL_in_atom231 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_E_in_atom236 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_STRING_in_atom241 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_IDENTIFIER_in_atom246 = new BitSet(new long[] { 0x0000000001000000L });

    public static final BitSet FOLLOW_24_in_atom248 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_atom250 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_IDENTIFIER_in_atom264 = new BitSet(new long[] { 0x0000000001000000L });

    public static final BitSet FOLLOW_24_in_atom266 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_expseq_in_atom268 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_atom270 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_24_in_atom285 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_expr_in_atom287 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_atom289 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_CELL_in_atom300 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_CELLRANGE_in_atom305 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_FIXEDCELL_in_atom310 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_FIXEDCELLRANGE_in_atom315 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_REMOTECELL_in_atom320 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_REMOTECELLRANGE_in_atom325 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_expr_in_expseq335 = new BitSet(new long[] { 0x0000000090000002L });

    public static final BitSet FOLLOW_31_in_expseq339 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_28_in_expseq342 = new BitSet(new long[] { 0x00000000013D7BD0L });

    public static final BitSet FOLLOW_expr_in_expseq346 = new BitSet(new long[] { 0x0000000090000002L });
}
