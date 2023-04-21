package org.shapelogic.filter;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import org.antlr.runtime.tree.*;

public class FilterParser extends Parser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CONSTRAINT", "STRING", "DOUBLE", "VARIABLE", "AND", "OR", "NOT", "LEFTPAR", "RIGHTPAR", "DOUBLEQUOTE", "QUOTE", "PERIOD", "MINUS", "BACKSPACE", "NEWLINE", "WS", "LETTER", "DIGIT", "NONE_END", "BACKSPACE_SEQUENCE", "ID", "NUMBER", "ARGU" };

    public static final int LETTER = 20;

    public static final int PERIOD = 15;

    public static final int RIGHTPAR = 12;

    public static final int NUMBER = 25;

    public static final int NOT = 10;

    public static final int MINUS = 16;

    public static final int ID = 24;

    public static final int AND = 8;

    public static final int EOF = -1;

    public static final int DOUBLEQUOTE = 13;

    public static final int QUOTE = 14;

    public static final int WS = 19;

    public static final int BACKSPACE = 17;

    public static final int VARIABLE = 7;

    public static final int NEWLINE = 18;

    public static final int OR = 9;

    public static final int BACKSPACE_SEQUENCE = 23;

    public static final int DOUBLE = 6;

    public static final int LEFTPAR = 11;

    public static final int ARGU = 26;

    public static final int NONE_END = 22;

    public static final int DIGIT = 21;

    public static final int CONSTRAINT = 4;

    public static final int STRING = 5;

    public FilterParser(TokenStream input) {
        super(input);
    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() {
        return tokenNames;
    }

    public String getGrammarFileName() {
        return "Filter.g";
    }

    public static class filter_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final filter_return filter() throws RecognitionException {
        filter_return retval = new filter_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        orExpr_return orExpr1 = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_orExpr_in_filter388);
                orExpr1 = orExpr();
                _fsp--;
                adaptor.addChild(root_0, orExpr1.getTree());
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static class orExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final orExpr_return orExpr() throws RecognitionException {
        orExpr_return retval = new orExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token OR3 = null;
        andExpr_return andExpr2 = null;
        andExpr_return andExpr4 = null;
        CommonTree OR3_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_andExpr_in_orExpr400);
                andExpr2 = andExpr();
                _fsp--;
                adaptor.addChild(root_0, andExpr2.getTree());
                loop1: do {
                    int alt1 = 2;
                    int LA1_0 = input.LA(1);
                    if ((LA1_0 == OR)) {
                        alt1 = 1;
                    }
                    switch(alt1) {
                        case 1:
                            {
                                OR3 = (Token) input.LT(1);
                                match(input, OR, FOLLOW_OR_in_orExpr403);
                                OR3_tree = (CommonTree) adaptor.create(OR3);
                                root_0 = (CommonTree) adaptor.becomeRoot(OR3_tree, root_0);
                                pushFollow(FOLLOW_andExpr_in_orExpr406);
                                andExpr4 = andExpr();
                                _fsp--;
                                adaptor.addChild(root_0, andExpr4.getTree());
                            }
                            break;
                        default:
                            break loop1;
                    }
                } while (true);
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static class andExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final andExpr_return andExpr() throws RecognitionException {
        andExpr_return retval = new andExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token AND6 = null;
        notExpr_return notExpr5 = null;
        notExpr_return notExpr7 = null;
        CommonTree AND6_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                pushFollow(FOLLOW_notExpr_in_andExpr417);
                notExpr5 = notExpr();
                _fsp--;
                adaptor.addChild(root_0, notExpr5.getTree());
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if ((LA2_0 == AND)) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                AND6 = (Token) input.LT(1);
                                match(input, AND, FOLLOW_AND_in_andExpr420);
                                AND6_tree = (CommonTree) adaptor.create(AND6);
                                root_0 = (CommonTree) adaptor.becomeRoot(AND6_tree, root_0);
                                pushFollow(FOLLOW_notExpr_in_andExpr423);
                                notExpr7 = notExpr();
                                _fsp--;
                                adaptor.addChild(root_0, notExpr7.getTree());
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
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static class notExpr_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final notExpr_return notExpr() throws RecognitionException {
        notExpr_return retval = new notExpr_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token NOT8 = null;
        atom_return atom9 = null;
        atom_return atom10 = null;
        CommonTree NOT8_tree = null;
        RewriteRuleTokenStream stream_NOT = new RewriteRuleTokenStream(adaptor, "token NOT");
        RewriteRuleSubtreeStream stream_atom = new RewriteRuleSubtreeStream(adaptor, "rule atom");
        try {
            int alt3 = 2;
            int LA3_0 = input.LA(1);
            if ((LA3_0 == NOT)) {
                alt3 = 1;
            } else if ((LA3_0 == LEFTPAR || LA3_0 == ID)) {
                alt3 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("62:1: notExpr : ( NOT atom -> ^( NOT atom ) | atom -> atom );", 3, 0, input);
                throw nvae;
            }
            switch(alt3) {
                case 1:
                    {
                        NOT8 = (Token) input.LT(1);
                        match(input, NOT, FOLLOW_NOT_in_notExpr441);
                        stream_NOT.add(NOT8);
                        pushFollow(FOLLOW_atom_in_notExpr443);
                        atom9 = atom();
                        _fsp--;
                        stream_atom.add(atom9.getTree());
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(stream_NOT.next(), root_1);
                                adaptor.addChild(root_1, stream_atom.next());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                    }
                    break;
                case 2:
                    {
                        pushFollow(FOLLOW_atom_in_notExpr461);
                        atom10 = atom();
                        _fsp--;
                        stream_atom.add(atom10.getTree());
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            adaptor.addChild(root_0, stream_atom.next());
                        }
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static class argument_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final argument_return argument() throws RecognitionException {
        argument_return retval = new argument_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token ARGU11 = null;
        Token NUMBER12 = null;
        Token ID13 = null;
        CommonTree ARGU11_tree = null;
        CommonTree NUMBER12_tree = null;
        CommonTree ID13_tree = null;
        RewriteRuleTokenStream stream_ARGU = new RewriteRuleTokenStream(adaptor, "token ARGU");
        RewriteRuleTokenStream stream_ID = new RewriteRuleTokenStream(adaptor, "token ID");
        RewriteRuleTokenStream stream_NUMBER = new RewriteRuleTokenStream(adaptor, "token NUMBER");
        try {
            int alt4 = 3;
            switch(input.LA(1)) {
                case ARGU:
                    {
                        alt4 = 1;
                    }
                    break;
                case NUMBER:
                    {
                        alt4 = 2;
                    }
                    break;
                case ID:
                    {
                        alt4 = 3;
                    }
                    break;
                default:
                    NoViableAltException nvae = new NoViableAltException("67:1: argument : ( ARGU -> ^( STRING ARGU ) | NUMBER -> ^( DOUBLE NUMBER ) | ID -> ^( VARIABLE ID ) );", 4, 0, input);
                    throw nvae;
            }
            switch(alt4) {
                case 1:
                    {
                        ARGU11 = (Token) input.LT(1);
                        match(input, ARGU, FOLLOW_ARGU_in_argument479);
                        stream_ARGU.add(ARGU11);
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(adaptor.create(STRING, "STRING"), root_1);
                                adaptor.addChild(root_1, stream_ARGU.next());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                    }
                    break;
                case 2:
                    {
                        NUMBER12 = (Token) input.LT(1);
                        match(input, NUMBER, FOLLOW_NUMBER_in_argument497);
                        stream_NUMBER.add(NUMBER12);
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(adaptor.create(DOUBLE, "DOUBLE"), root_1);
                                adaptor.addChild(root_1, stream_NUMBER.next());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                    }
                    break;
                case 3:
                    {
                        ID13 = (Token) input.LT(1);
                        match(input, ID, FOLLOW_ID_in_argument513);
                        stream_ID.add(ID13);
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(adaptor.create(VARIABLE, "VARIABLE"), root_1);
                                adaptor.addChild(root_1, stream_ID.next());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static class constraint_return extends ParserRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final constraint_return constraint() throws RecognitionException {
        constraint_return retval = new constraint_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token name = null;
        Token LEFTPAR14 = null;
        Token RIGHTPAR16 = null;
        argument_return argument15 = null;
        CommonTree name_tree = null;
        CommonTree LEFTPAR14_tree = null;
        CommonTree RIGHTPAR16_tree = null;
        RewriteRuleTokenStream stream_LEFTPAR = new RewriteRuleTokenStream(adaptor, "token LEFTPAR");
        RewriteRuleTokenStream stream_ID = new RewriteRuleTokenStream(adaptor, "token ID");
        RewriteRuleTokenStream stream_RIGHTPAR = new RewriteRuleTokenStream(adaptor, "token RIGHTPAR");
        RewriteRuleSubtreeStream stream_argument = new RewriteRuleSubtreeStream(adaptor, "rule argument");
        try {
            int alt5 = 2;
            int LA5_0 = input.LA(1);
            if ((LA5_0 == ID)) {
                int LA5_1 = input.LA(2);
                if ((LA5_1 == LEFTPAR)) {
                    alt5 = 1;
                } else if ((LA5_1 == EOF || (LA5_1 >= AND && LA5_1 <= OR) || LA5_1 == RIGHTPAR)) {
                    alt5 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("72:1: constraint : (name= ID LEFTPAR argument RIGHTPAR -> ^( CONSTRAINT $name argument ) | name= ID -> ^( CONSTRAINT $name) );", 5, 1, input);
                    throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("72:1: constraint : (name= ID LEFTPAR argument RIGHTPAR -> ^( CONSTRAINT $name argument ) | name= ID -> ^( CONSTRAINT $name) );", 5, 0, input);
                throw nvae;
            }
            switch(alt5) {
                case 1:
                    {
                        name = (Token) input.LT(1);
                        match(input, ID, FOLLOW_ID_in_constraint547);
                        stream_ID.add(name);
                        LEFTPAR14 = (Token) input.LT(1);
                        match(input, LEFTPAR, FOLLOW_LEFTPAR_in_constraint549);
                        stream_LEFTPAR.add(LEFTPAR14);
                        pushFollow(FOLLOW_argument_in_constraint551);
                        argument15 = argument();
                        _fsp--;
                        stream_argument.add(argument15.getTree());
                        RIGHTPAR16 = (Token) input.LT(1);
                        match(input, RIGHTPAR, FOLLOW_RIGHTPAR_in_constraint553);
                        stream_RIGHTPAR.add(RIGHTPAR16);
                        retval.tree = root_0;
                        RewriteRuleTokenStream stream_name = new RewriteRuleTokenStream(adaptor, "token name", name);
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(adaptor.create(CONSTRAINT, "CONSTRAINT"), root_1);
                                adaptor.addChild(root_1, stream_name.next());
                                adaptor.addChild(root_1, stream_argument.next());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                    }
                    break;
                case 2:
                    {
                        name = (Token) input.LT(1);
                        match(input, ID, FOLLOW_ID_in_constraint576);
                        stream_ID.add(name);
                        retval.tree = root_0;
                        RewriteRuleTokenStream stream_name = new RewriteRuleTokenStream(adaptor, "token name", name);
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(adaptor.create(CONSTRAINT, "CONSTRAINT"), root_1);
                                adaptor.addChild(root_1, stream_name.next());
                                adaptor.addChild(root_0, root_1);
                            }
                        }
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
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

    public final atom_return atom() throws RecognitionException {
        atom_return retval = new atom_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        Token LEFTPAR17 = null;
        Token RIGHTPAR19 = null;
        orExpr_return orExpr18 = null;
        constraint_return constraint20 = null;
        CommonTree LEFTPAR17_tree = null;
        CommonTree RIGHTPAR19_tree = null;
        RewriteRuleTokenStream stream_LEFTPAR = new RewriteRuleTokenStream(adaptor, "token LEFTPAR");
        RewriteRuleTokenStream stream_RIGHTPAR = new RewriteRuleTokenStream(adaptor, "token RIGHTPAR");
        RewriteRuleSubtreeStream stream_orExpr = new RewriteRuleSubtreeStream(adaptor, "rule orExpr");
        try {
            int alt6 = 2;
            int LA6_0 = input.LA(1);
            if ((LA6_0 == LEFTPAR)) {
                alt6 = 1;
            } else if ((LA6_0 == ID)) {
                alt6 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("76:1: atom : ( LEFTPAR orExpr RIGHTPAR -> orExpr | constraint );", 6, 0, input);
                throw nvae;
            }
            switch(alt6) {
                case 1:
                    {
                        LEFTPAR17 = (Token) input.LT(1);
                        match(input, LEFTPAR, FOLLOW_LEFTPAR_in_atom599);
                        stream_LEFTPAR.add(LEFTPAR17);
                        pushFollow(FOLLOW_orExpr_in_atom601);
                        orExpr18 = orExpr();
                        _fsp--;
                        stream_orExpr.add(orExpr18.getTree());
                        RIGHTPAR19 = (Token) input.LT(1);
                        match(input, RIGHTPAR, FOLLOW_RIGHTPAR_in_atom603);
                        stream_RIGHTPAR.add(RIGHTPAR19);
                        retval.tree = root_0;
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        {
                            adaptor.addChild(root_0, stream_orExpr.next());
                        }
                    }
                    break;
                case 2:
                    {
                        root_0 = (CommonTree) adaptor.nil();
                        pushFollow(FOLLOW_constraint_in_atom618);
                        constraint20 = constraint();
                        _fsp--;
                        adaptor.addChild(root_0, constraint20.getTree());
                    }
                    break;
            }
            retval.stop = input.LT(-1);
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }

    public static final BitSet FOLLOW_orExpr_in_filter388 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_andExpr_in_orExpr400 = new BitSet(new long[] { 0x0000000000000202L });

    public static final BitSet FOLLOW_OR_in_orExpr403 = new BitSet(new long[] { 0x0000000001000C00L });

    public static final BitSet FOLLOW_andExpr_in_orExpr406 = new BitSet(new long[] { 0x0000000000000202L });

    public static final BitSet FOLLOW_notExpr_in_andExpr417 = new BitSet(new long[] { 0x0000000000000102L });

    public static final BitSet FOLLOW_AND_in_andExpr420 = new BitSet(new long[] { 0x0000000001000C00L });

    public static final BitSet FOLLOW_notExpr_in_andExpr423 = new BitSet(new long[] { 0x0000000000000102L });

    public static final BitSet FOLLOW_NOT_in_notExpr441 = new BitSet(new long[] { 0x0000000001000800L });

    public static final BitSet FOLLOW_atom_in_notExpr443 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_atom_in_notExpr461 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ARGU_in_argument479 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_NUMBER_in_argument497 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_argument513 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_constraint547 = new BitSet(new long[] { 0x0000000000000800L });

    public static final BitSet FOLLOW_LEFTPAR_in_constraint549 = new BitSet(new long[] { 0x0000000007000000L });

    public static final BitSet FOLLOW_argument_in_constraint551 = new BitSet(new long[] { 0x0000000000001000L });

    public static final BitSet FOLLOW_RIGHTPAR_in_constraint553 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_constraint576 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_LEFTPAR_in_atom599 = new BitSet(new long[] { 0x0000000001000C00L });

    public static final BitSet FOLLOW_orExpr_in_atom601 = new BitSet(new long[] { 0x0000000000001000L });

    public static final BitSet FOLLOW_RIGHTPAR_in_atom603 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_constraint_in_atom618 = new BitSet(new long[] { 0x0000000000000002L });
}
