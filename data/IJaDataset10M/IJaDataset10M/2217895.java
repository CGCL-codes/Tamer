package com.microfly.formula;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import org.antlr.runtime.tree.*;

public class FormulaParser extends Parser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "POS", "NEG", "CALL", "EQ", "AND", "OR", "LT", "LTEQ", "GT", "GTEQ", "NOTEQ", "CONCAT", "SUB", "ADD", "DIV", "MULT", "EXP", "NOT", "VARIABLE", "LPAREN", "RPAREN", "FUNCNAME", "COMMA", "NUMBER", "STRING", "TRUE", "FALSE", "PERCENT", "ESCAPE_SEQUENCE", "WHITESPACE", "LETTER", "DIGIT" };

    public static final int LT = 10;

    public static final int PERCENT = 31;

    public static final int CONCAT = 15;

    public static final int LETTER = 34;

    public static final int NUMBER = 27;

    public static final int LTEQ = 11;

    public static final int WHITESPACE = 33;

    public static final int SUB = 16;

    public static final int NOT = 21;

    public static final int GTEQ = 13;

    public static final int MULT = 19;

    public static final int AND = 8;

    public static final int EOF = -1;

    public static final int TRUE = 29;

    public static final int LPAREN = 23;

    public static final int ESCAPE_SEQUENCE = 32;

    public static final int RPAREN = 24;

    public static final int NOTEQ = 14;

    public static final int VARIABLE = 22;

    public static final int EXP = 20;

    public static final int COMMA = 26;

    public static final int POS = 4;

    public static final int NEG = 5;

    public static final int OR = 9;

    public static final int GT = 12;

    public static final int CALL = 6;

    public static final int DIGIT = 35;

    public static final int FUNCNAME = 25;

    public static final int EQ = 7;

    public static final int DIV = 18;

    public static final int FALSE = 30;

    public static final int STRING = 28;

    public static final int ADD = 17;

    public FormulaParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public FormulaParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() {
        return FormulaParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "D:\\workspace\\microfly\\src\\com\\microfly\\formula\\Formula.g";
    }

    private FormulaContext ctxt = null;

    public FormulaParser(FormulaContext ctxt, TokenStream input) {
        this(input, new RecognizerSharedState());
        this.ctxt = ctxt;
    }

    public FormulaParser(FormulaContext ctxt, TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.ctxt = ctxt;
    }

    protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
        throw e;
    }

    private boolean isDefineSymbol(String name) {
        return ctxt.HasSymbol(name.substring(1, name.length() - 1));
    }

    private boolean isDefineFunction(String name) {
        return ctxt.HasFunction(name);
    }

    public static class formula_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.formula_return formula() throws RecognitionException {
        FormulaParser.formula_return retval = new FormulaParser.formula_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token EQ1 = null;
        FormulaParser.expression_return expression2 = null;
        CommonTree EQ1_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                int alt1 = 2;
                int LA1_0 = input.LA(1);
                if ((LA1_0 == EQ)) {
                    alt1 = 1;
                }
                switch(alt1) {
                    case 1:
                        {
                            EQ1 = (Token) match(input, EQ, FOLLOW_EQ_in_formula112);
                        }
                        break;
                }
                pushFollow(FOLLOW_expression_in_formula117);
                expression2 = expression();
                state._fsp--;
                adaptor.addChild(root_0, expression2.getTree());
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class expression_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.expression_return expression() throws RecognitionException {
        FormulaParser.expression_return retval = new FormulaParser.expression_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        FormulaParser.boolExpr_return boolExpr3 = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_boolExpr_in_expression137);
                boolExpr3 = boolExpr();
                state._fsp--;
                adaptor.addChild(root_0, boolExpr3.getTree());
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class boolExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.boolExpr_return boolExpr() throws RecognitionException {
        FormulaParser.boolExpr_return retval = new FormulaParser.boolExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token set5 = null;
        FormulaParser.concatExpr_return concatExpr4 = null;
        FormulaParser.concatExpr_return concatExpr6 = null;
        CommonTree set5_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_concatExpr_in_boolExpr153);
                concatExpr4 = concatExpr();
                state._fsp--;
                adaptor.addChild(root_0, concatExpr4.getTree());
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= EQ && LA2_0 <= NOTEQ))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                set5 = (Token) input.LT(1);
                                set5 = (Token) input.LT(1);
                                if ((input.LA(1) >= EQ && input.LA(1) <= NOTEQ)) {
                                    input.consume();
                                    root_0 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(set5), root_0);
                                    state.errorRecovery = false;
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    throw mse;
                                }
                                pushFollow(FOLLOW_concatExpr_in_boolExpr189);
                                concatExpr6 = concatExpr();
                                state._fsp--;
                                adaptor.addChild(root_0, concatExpr6.getTree());
                            }
                            break;
                        default:
                            break loop2;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class concatExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.concatExpr_return concatExpr() throws RecognitionException {
        FormulaParser.concatExpr_return retval = new FormulaParser.concatExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token CONCAT8 = null;
        FormulaParser.sumExpr_return sumExpr7 = null;
        FormulaParser.sumExpr_return sumExpr9 = null;
        CommonTree CONCAT8_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_sumExpr_in_concatExpr207);
                sumExpr7 = sumExpr();
                state._fsp--;
                adaptor.addChild(root_0, sumExpr7.getTree());
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if ((LA3_0 == CONCAT)) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                CONCAT8 = (Token) match(input, CONCAT, FOLLOW_CONCAT_in_concatExpr210);
                                CONCAT8_tree = (CommonTree) adaptor.create(CONCAT8);
                                root_0 = (CommonTree) adaptor.becomeRoot(CONCAT8_tree, root_0);
                                pushFollow(FOLLOW_sumExpr_in_concatExpr213);
                                sumExpr9 = sumExpr();
                                state._fsp--;
                                adaptor.addChild(root_0, sumExpr9.getTree());
                            }
                            break;
                        default:
                            break loop3;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class sumExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.sumExpr_return sumExpr() throws RecognitionException {
        FormulaParser.sumExpr_return retval = new FormulaParser.sumExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token set11 = null;
        FormulaParser.productExpr_return productExpr10 = null;
        FormulaParser.productExpr_return productExpr12 = null;
        CommonTree set11_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_productExpr_in_sumExpr231);
                productExpr10 = productExpr();
                state._fsp--;
                adaptor.addChild(root_0, productExpr10.getTree());
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);
                    if (((LA4_0 >= SUB && LA4_0 <= ADD))) {
                        alt4 = 1;
                    }
                    switch(alt4) {
                        case 1:
                            {
                                set11 = (Token) input.LT(1);
                                set11 = (Token) input.LT(1);
                                if ((input.LA(1) >= SUB && input.LA(1) <= ADD)) {
                                    input.consume();
                                    root_0 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(set11), root_0);
                                    state.errorRecovery = false;
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    throw mse;
                                }
                                pushFollow(FOLLOW_productExpr_in_sumExpr243);
                                productExpr12 = productExpr();
                                state._fsp--;
                                adaptor.addChild(root_0, productExpr12.getTree());
                            }
                            break;
                        default:
                            break loop4;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class productExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.productExpr_return productExpr() throws RecognitionException {
        FormulaParser.productExpr_return retval = new FormulaParser.productExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token set14 = null;
        FormulaParser.expExpr_return expExpr13 = null;
        FormulaParser.expExpr_return expExpr15 = null;
        CommonTree set14_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_expExpr_in_productExpr261);
                expExpr13 = expExpr();
                state._fsp--;
                adaptor.addChild(root_0, expExpr13.getTree());
                loop5: do {
                    int alt5 = 2;
                    int LA5_0 = input.LA(1);
                    if (((LA5_0 >= DIV && LA5_0 <= MULT))) {
                        alt5 = 1;
                    }
                    switch(alt5) {
                        case 1:
                            {
                                set14 = (Token) input.LT(1);
                                set14 = (Token) input.LT(1);
                                if ((input.LA(1) >= DIV && input.LA(1) <= MULT)) {
                                    input.consume();
                                    root_0 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(set14), root_0);
                                    state.errorRecovery = false;
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    throw mse;
                                }
                                pushFollow(FOLLOW_expExpr_in_productExpr273);
                                expExpr15 = expExpr();
                                state._fsp--;
                                adaptor.addChild(root_0, expExpr15.getTree());
                            }
                            break;
                        default:
                            break loop5;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class expExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.expExpr_return expExpr() throws RecognitionException {
        FormulaParser.expExpr_return retval = new FormulaParser.expExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token EXP17 = null;
        FormulaParser.unaryOperation_return unaryOperation16 = null;
        FormulaParser.unaryOperation_return unaryOperation18 = null;
        CommonTree EXP17_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_unaryOperation_in_expExpr291);
                unaryOperation16 = unaryOperation();
                state._fsp--;
                adaptor.addChild(root_0, unaryOperation16.getTree());
                loop6: do {
                    int alt6 = 2;
                    int LA6_0 = input.LA(1);
                    if ((LA6_0 == EXP)) {
                        alt6 = 1;
                    }
                    switch(alt6) {
                        case 1:
                            {
                                EXP17 = (Token) match(input, EXP, FOLLOW_EXP_in_expExpr294);
                                EXP17_tree = (CommonTree) adaptor.create(EXP17);
                                root_0 = (CommonTree) adaptor.becomeRoot(EXP17_tree, root_0);
                                pushFollow(FOLLOW_unaryOperation_in_expExpr297);
                                unaryOperation18 = unaryOperation();
                                state._fsp--;
                                adaptor.addChild(root_0, unaryOperation18.getTree());
                            }
                            break;
                        default:
                            break loop6;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class unaryOperation_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.unaryOperation_return unaryOperation() throws RecognitionException {
        FormulaParser.unaryOperation_return retval = new FormulaParser.unaryOperation_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token NOT19 = null;
        Token ADD21 = null;
        Token SUB22 = null;
        FormulaParser.operand_return o = null;
        FormulaParser.operand_return operand20 = null;
        FormulaParser.operand_return operand23 = null;
        CommonTree NOT19_tree = null;
        CommonTree ADD21_tree = null;
        CommonTree SUB22_tree = null;
        RewriteRuleTokenStream stream_SUB = new RewriteRuleTokenStream(adaptor, "token SUB");
        RewriteRuleTokenStream stream_ADD = new RewriteRuleTokenStream(adaptor, "token ADD");
        RewriteRuleSubtreeStream stream_operand = new RewriteRuleSubtreeStream(adaptor, "rule operand");
        try {
            int alt7 = 4;
            switch(input.LA(1)) {
                case NOT:
                    {
                        alt7 = 1;
                    }
                    break;
                case ADD:
                    {
                        alt7 = 2;
                    }
                    break;
                case SUB:
                    {
                        alt7 = 3;
                    }
                    break;
                case VARIABLE:
                case LPAREN:
                case FUNCNAME:
                case NUMBER:
                case STRING:
                case TRUE:
                case FALSE:
                    {
                        alt7 = 4;
                    }
                    break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 7, 0, input);
                    throw nvae;
            }
            switch(alt7) {
                case 1:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        NOT19 = (Token) match(input, NOT, FOLLOW_NOT_in_unaryOperation315);
                        NOT19_tree = (CommonTree) adaptor.create(NOT19);
                        root_0 = (CommonTree) adaptor.becomeRoot(NOT19_tree, root_0);
                        pushFollow(FOLLOW_operand_in_unaryOperation318);
                        operand20 = operand();
                        state._fsp--;
                        adaptor.addChild(root_0, operand20.getTree());
                    }
                    break;
                case 2:
                    {
                        ADD21 = (Token) match(input, ADD, FOLLOW_ADD_in_unaryOperation326);
                        stream_ADD.add(ADD21);
                        pushFollow(FOLLOW_operand_in_unaryOperation330);
                        o = operand();
                        state._fsp--;
                        stream_operand.add(o.getTree());
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        RewriteRuleSubtreeStream stream_o = new RewriteRuleSubtreeStream(adaptor, "token o", o != null ? o.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(POS, "POS"), root_1);
                                adaptor.addChild(root_1, stream_o.nextTree());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                        retval.tree = root_0;
                        retval.tree = root_0;
                    }
                    break;
                case 3:
                    {
                        SUB22 = (Token) match(input, SUB, FOLLOW_SUB_in_unaryOperation347);
                        stream_SUB.add(SUB22);
                        pushFollow(FOLLOW_operand_in_unaryOperation351);
                        o = operand();
                        state._fsp--;
                        stream_operand.add(o.getTree());
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        RewriteRuleSubtreeStream stream_o = new RewriteRuleSubtreeStream(adaptor, "token o", o != null ? o.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(NEG, "NEG"), root_1);
                                adaptor.addChild(root_1, stream_o.nextTree());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                        retval.tree = root_0;
                        retval.tree = root_0;
                    }
                    break;
                case 4:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        pushFollow(FOLLOW_operand_in_unaryOperation368);
                        operand23 = operand();
                        state._fsp--;
                        adaptor.addChild(root_0, operand23.getTree());
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class operand_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.operand_return operand() throws RecognitionException {
        FormulaParser.operand_return retval = new FormulaParser.operand_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token VARIABLE27 = null;
        Token LPAREN28 = null;
        Token RPAREN30 = null;
        FormulaParser.literal_return literal24 = null;
        FormulaParser.functionExpr_return functionExpr25 = null;
        FormulaParser.percent_return percent26 = null;
        FormulaParser.expression_return expression29 = null;
        CommonTree VARIABLE27_tree = null;
        CommonTree LPAREN28_tree = null;
        CommonTree RPAREN30_tree = null;
        RewriteRuleTokenStream stream_RPAREN = new RewriteRuleTokenStream(adaptor, "token RPAREN");
        RewriteRuleTokenStream stream_LPAREN = new RewriteRuleTokenStream(adaptor, "token LPAREN");
        RewriteRuleSubtreeStream stream_expression = new RewriteRuleSubtreeStream(adaptor, "rule expression");
        RewriteRuleSubtreeStream stream_functionExpr = new RewriteRuleSubtreeStream(adaptor, "rule functionExpr");
        try {
            int alt8 = 5;
            switch(input.LA(1)) {
                case NUMBER:
                    {
                        int LA8_1 = input.LA(2);
                        if ((LA8_1 == PERCENT)) {
                            alt8 = 3;
                        } else if ((LA8_1 == EOF || (LA8_1 >= EQ && LA8_1 <= EXP) || LA8_1 == RPAREN || LA8_1 == COMMA)) {
                            alt8 = 1;
                        } else {
                            NoViableAltException nvae = new NoViableAltException("", 8, 1, input);
                            throw nvae;
                        }
                    }
                    break;
                case FUNCNAME:
                    {
                        alt8 = 2;
                    }
                    break;
                case STRING:
                case TRUE:
                case FALSE:
                    {
                        alt8 = 1;
                    }
                    break;
                case VARIABLE:
                    {
                        alt8 = 4;
                    }
                    break;
                case LPAREN:
                    {
                        alt8 = 5;
                    }
                    break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 8, 0, input);
                    throw nvae;
            }
            switch(alt8) {
                case 1:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        pushFollow(FOLLOW_literal_in_operand385);
                        literal24 = literal();
                        state._fsp--;
                        adaptor.addChild(root_0, literal24.getTree());
                    }
                    break;
                case 2:
                    {
                        pushFollow(FOLLOW_functionExpr_in_operand393);
                        functionExpr25 = functionExpr();
                        state._fsp--;
                        stream_functionExpr.add(functionExpr25.getTree());
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(CALL, "CALL"), root_1);
                                adaptor.addChild(root_1, stream_functionExpr.nextTree());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                        retval.tree = root_0;
                        retval.tree = root_0;
                    }
                    break;
                case 3:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        pushFollow(FOLLOW_percent_in_operand409);
                        percent26 = percent();
                        state._fsp--;
                        adaptor.addChild(root_0, percent26.getTree());
                    }
                    break;
                case 4:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        VARIABLE27 = (Token) match(input, VARIABLE, FOLLOW_VARIABLE_in_operand417);
                        VARIABLE27_tree = (CommonTree) adaptor.create(VARIABLE27);
                        adaptor.addChild(root_0, VARIABLE27_tree);
                        if (!(isDefineSymbol((VARIABLE27 != null ? VARIABLE27.getText() : null)))) {
                            throw new FailedPredicateException(input, "operand", "isDefineSymbol($VARIABLE.text)");
                        }
                    }
                    break;
                case 5:
                    {
                        LPAREN28 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_operand427);
                        stream_LPAREN.add(LPAREN28);
                        pushFollow(FOLLOW_expression_in_operand429);
                        expression29 = expression();
                        state._fsp--;
                        stream_expression.add(expression29.getTree());
                        RPAREN30 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_operand431);
                        stream_RPAREN.add(RPAREN30);
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(stream_expression.nextNode(), root_1);
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                        retval.tree = root_0;
                        retval.tree = root_0;
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class functionExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.functionExpr_return functionExpr() throws RecognitionException {
        FormulaParser.functionExpr_return retval = new FormulaParser.functionExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token FUNCNAME31 = null;
        Token LPAREN32 = null;
        Token COMMA34 = null;
        Token RPAREN36 = null;
        FormulaParser.expression_return expression33 = null;
        FormulaParser.expression_return expression35 = null;
        CommonTree FUNCNAME31_tree = null;
        CommonTree LPAREN32_tree = null;
        CommonTree COMMA34_tree = null;
        CommonTree RPAREN36_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                FUNCNAME31 = (Token) match(input, FUNCNAME, FOLLOW_FUNCNAME_in_functionExpr453);
                FUNCNAME31_tree = (CommonTree) adaptor.create(FUNCNAME31);
                adaptor.addChild(root_0, FUNCNAME31_tree);
                LPAREN32 = (Token) match(input, LPAREN, FOLLOW_LPAREN_in_functionExpr455);
                int alt10 = 2;
                int LA10_0 = input.LA(1);
                if (((LA10_0 >= SUB && LA10_0 <= ADD) || (LA10_0 >= NOT && LA10_0 <= LPAREN) || LA10_0 == FUNCNAME || (LA10_0 >= NUMBER && LA10_0 <= FALSE))) {
                    alt10 = 1;
                }
                switch(alt10) {
                    case 1:
                        {
                            pushFollow(FOLLOW_expression_in_functionExpr459);
                            expression33 = expression();
                            state._fsp--;
                            adaptor.addChild(root_0, expression33.getTree());
                            loop9: do {
                                int alt9 = 2;
                                int LA9_0 = input.LA(1);
                                if ((LA9_0 == COMMA)) {
                                    alt9 = 1;
                                }
                                switch(alt9) {
                                    case 1:
                                        {
                                            COMMA34 = (Token) match(input, COMMA, FOLLOW_COMMA_in_functionExpr462);
                                            pushFollow(FOLLOW_expression_in_functionExpr465);
                                            expression35 = expression();
                                            state._fsp--;
                                            adaptor.addChild(root_0, expression35.getTree());
                                        }
                                        break;
                                    default:
                                        break loop9;
                                }
                            } while (true);
                        }
                        break;
                }
                RPAREN36 = (Token) match(input, RPAREN, FOLLOW_RPAREN_in_functionExpr471);
                if (!(isDefineFunction((FUNCNAME31 != null ? FUNCNAME31.getText() : null)))) {
                    throw new FailedPredicateException(input, "functionExpr", "isDefineFunction($FUNCNAME.text)");
                }
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class literal_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.literal_return literal() throws RecognitionException {
        FormulaParser.literal_return retval = new FormulaParser.literal_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token set37 = null;
        CommonTree set37_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                set37 = (Token) input.LT(1);
                if ((input.LA(1) >= NUMBER && input.LA(1) <= FALSE)) {
                    input.consume();
                    adaptor.addChild(root_0, (CommonTree) adaptor.create(set37));
                    state.errorRecovery = false;
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static class percent_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final FormulaParser.percent_return percent() throws RecognitionException {
        FormulaParser.percent_return retval = new FormulaParser.percent_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token NUMBER38 = null;
        Token PERCENT39 = null;
        CommonTree NUMBER38_tree = null;
        CommonTree PERCENT39_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                NUMBER38 = (Token) match(input, NUMBER, FOLLOW_NUMBER_in_percent537);
                NUMBER38_tree = (CommonTree) adaptor.create(NUMBER38);
                adaptor.addChild(root_0, NUMBER38_tree);
                PERCENT39 = (Token) match(input, PERCENT, FOLLOW_PERCENT_in_percent539);
                PERCENT39_tree = (CommonTree) adaptor.create(PERCENT39);
                root_0 = (CommonTree) adaptor.becomeRoot(PERCENT39_tree, root_0);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public static final BitSet FOLLOW_EQ_in_formula112 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_expression_in_formula117 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_boolExpr_in_expression137 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_concatExpr_in_boolExpr153 = new BitSet(new long[] { 0x0000000000007F82L });

    public static final BitSet FOLLOW_set_in_boolExpr156 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_concatExpr_in_boolExpr189 = new BitSet(new long[] { 0x0000000000007F82L });

    public static final BitSet FOLLOW_sumExpr_in_concatExpr207 = new BitSet(new long[] { 0x0000000000008002L });

    public static final BitSet FOLLOW_CONCAT_in_concatExpr210 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_sumExpr_in_concatExpr213 = new BitSet(new long[] { 0x0000000000008002L });

    public static final BitSet FOLLOW_productExpr_in_sumExpr231 = new BitSet(new long[] { 0x0000000000030002L });

    public static final BitSet FOLLOW_set_in_sumExpr234 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_productExpr_in_sumExpr243 = new BitSet(new long[] { 0x0000000000030002L });

    public static final BitSet FOLLOW_expExpr_in_productExpr261 = new BitSet(new long[] { 0x00000000000C0002L });

    public static final BitSet FOLLOW_set_in_productExpr264 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_expExpr_in_productExpr273 = new BitSet(new long[] { 0x00000000000C0002L });

    public static final BitSet FOLLOW_unaryOperation_in_expExpr291 = new BitSet(new long[] { 0x0000000000100002L });

    public static final BitSet FOLLOW_EXP_in_expExpr294 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_unaryOperation_in_expExpr297 = new BitSet(new long[] { 0x0000000000100002L });

    public static final BitSet FOLLOW_NOT_in_unaryOperation315 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_operand_in_unaryOperation318 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ADD_in_unaryOperation326 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_operand_in_unaryOperation330 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_SUB_in_unaryOperation347 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_operand_in_unaryOperation351 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_operand_in_unaryOperation368 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_literal_in_operand385 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_functionExpr_in_operand393 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_percent_in_operand409 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_VARIABLE_in_operand417 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_LPAREN_in_operand427 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_expression_in_operand429 = new BitSet(new long[] { 0x0000000001000000L });

    public static final BitSet FOLLOW_RPAREN_in_operand431 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_FUNCNAME_in_functionExpr453 = new BitSet(new long[] { 0x0000000000800000L });

    public static final BitSet FOLLOW_LPAREN_in_functionExpr455 = new BitSet(new long[] { 0x000000007BE30000L });

    public static final BitSet FOLLOW_expression_in_functionExpr459 = new BitSet(new long[] { 0x0000000005000000L });

    public static final BitSet FOLLOW_COMMA_in_functionExpr462 = new BitSet(new long[] { 0x000000007AE30000L });

    public static final BitSet FOLLOW_expression_in_functionExpr465 = new BitSet(new long[] { 0x0000000005000000L });

    public static final BitSet FOLLOW_RPAREN_in_functionExpr471 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_set_in_literal0 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_NUMBER_in_percent537 = new BitSet(new long[] { 0x0000000080000000L });

    public static final BitSet FOLLOW_PERCENT_in_percent539 = new BitSet(new long[] { 0x0000000000000002L });
}
