package org.poset.server.parser.antlr;

import java.util.HashSet;
import java.util.Set;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Copyright 2008 Eric Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: QueryTreeP3.g 259 2008-08-16 18:17:21Z eric.walker $
 */
public class QueryTreeP3 extends TreeParser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PAIR", "TYPE", "WORD", "NODE", "GROUP", "STRING", "STATEMENT", "AND", "ORDER_BY", "ORS", "LESSES", "WORDS", "STRINGS", "TYPES", "PAIRS", "STATEMENTS", "DIGIT", "LOWER_ALPHA", "UPPER_ALPHA", "QUOTE", "QUOTES_AND_STRING", "SPACE", "TYPE_KEYWORD", "NODE_KEYWORD", "IDEAL_KEYWORD", "FILTER_KEYWORD", "LOCALITY_KEYWORD", "ID_KEYWORD", "LESS", "INCL_STOPWORDS", "SORT_UP", "SORT_DOWN", "OR", "STRING_LITERAL", "TYPE_LITERAL", "HEX_LITERAL", "WORD_LITERAL", "NEWLINE", "'/'", "'('", "')'" };

    public static final int NODE_KEYWORD = 27;

    public static final int FILTER_KEYWORD = 29;

    public static final int PAIRS = 18;

    public static final int ORS = 13;

    public static final int STRINGS = 16;

    public static final int QUOTES_AND_STRING = 24;

    public static final int INCL_STOPWORDS = 33;

    public static final int TYPES = 17;

    public static final int IDEAL_KEYWORD = 28;

    public static final int AND = 11;

    public static final int SPACE = 25;

    public static final int EOF = -1;

    public static final int SORT_DOWN = 35;

    public static final int STATEMENT = 10;

    public static final int TYPE = 5;

    public static final int WORD = 6;

    public static final int QUOTE = 23;

    public static final int STRING_LITERAL = 37;

    public static final int LESSES = 14;

    public static final int WORDS = 15;

    public static final int UPPER_ALPHA = 22;

    public static final int LESS = 32;

    public static final int PAIR = 4;

    public static final int HEX_LITERAL = 39;

    public static final int DIGIT = 20;

    public static final int T__42 = 42;

    public static final int TYPE_KEYWORD = 26;

    public static final int T__43 = 43;

    public static final int LOCALITY_KEYWORD = 30;

    public static final int NODE = 7;

    public static final int T__44 = 44;

    public static final int SORT_UP = 34;

    public static final int TYPE_LITERAL = 38;

    public static final int WORD_LITERAL = 40;

    public static final int GROUP = 8;

    public static final int NEWLINE = 41;

    public static final int LOWER_ALPHA = 21;

    public static final int ID_KEYWORD = 31;

    public static final int OR = 36;

    public static final int STATEMENTS = 19;

    public static final int ORDER_BY = 12;

    public static final int STRING = 9;

    public QueryTreeP3(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }

    public QueryTreeP3(TreeNodeStream input, RecognizerSharedState state) {
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
        return QueryTreeP3.tokenNames;
    }

    public String getGrammarFileName() {
        return "src/org/poset/server/parser/QueryTreeP3.g";
    }

    @Override
    protected void mismatch(IntStream input, int tokenType, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(tokenType, input);
    }

    @Override
    public Object recoverFromMismatchedSet(IntStream input, RecognitionException ex, BitSet follow) throws RecognitionException {
        throw ex;
    }

    public static class p3_query_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.p3_query_return p3_query() throws RecognitionException {
        QueryTreeP3.p3_query_return retval = new QueryTreeP3.p3_query_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        QueryTreeP3.p3_statements_return p3_statements1 = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                _last = (CommonTree) input.LT(1);
                pushFollow(FOLLOW_p3_statements_in_p3_query83);
                p3_statements1 = p3_statements();
                state._fsp--;
                adaptor.addChild(root_0, p3_statements1.getTree());
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    protected static class p3_statements_scope {

        Set strings;

        Set types;

        Set words;
    }

    protected Stack p3_statements_stack = new Stack();

    public static class p3_statements_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.p3_statements_return p3_statements() throws RecognitionException {
        p3_statements_stack.push(new p3_statements_scope());
        QueryTreeP3.p3_statements_return retval = new QueryTreeP3.p3_statements_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        List list_w = null;
        List list_s = null;
        List list_t = null;
        List list_p = null;
        List list_o = null;
        List list_l = null;
        RuleReturnScope w = null;
        RuleReturnScope s = null;
        RuleReturnScope t = null;
        RuleReturnScope p = null;
        RuleReturnScope o = null;
        RuleReturnScope l = null;
        RewriteRuleSubtreeStream stream_p3_word = new RewriteRuleSubtreeStream(adaptor, "rule p3_word");
        RewriteRuleSubtreeStream stream_pair = new RewriteRuleSubtreeStream(adaptor, "rule pair");
        RewriteRuleSubtreeStream stream_string = new RewriteRuleSubtreeStream(adaptor, "rule string");
        RewriteRuleSubtreeStream stream_type = new RewriteRuleSubtreeStream(adaptor, "rule type");
        RewriteRuleSubtreeStream stream_p3_or_list = new RewriteRuleSubtreeStream(adaptor, "rule p3_or_list");
        RewriteRuleSubtreeStream stream_p3_less = new RewriteRuleSubtreeStream(adaptor, "rule p3_less");
        ((p3_statements_scope) p3_statements_stack.peek()).strings = new HashSet();
        ((p3_statements_scope) p3_statements_stack.peek()).types = new HashSet();
        ((p3_statements_scope) p3_statements_stack.peek()).words = new HashSet();
        try {
            {
                int cnt1 = 0;
                loop1: do {
                    int alt1 = 7;
                    switch(input.LA(1)) {
                        case WORD:
                            {
                                alt1 = 1;
                            }
                            break;
                        case STRING:
                            {
                                alt1 = 2;
                            }
                            break;
                        case TYPE:
                            {
                                alt1 = 3;
                            }
                            break;
                        case PAIR:
                            {
                                alt1 = 4;
                            }
                            break;
                        case OR:
                            {
                                alt1 = 5;
                            }
                            break;
                        case LESS:
                            {
                                alt1 = 6;
                            }
                            break;
                    }
                    switch(alt1) {
                        case 1:
                            {
                                pushFollow(FOLLOW_p3_word_in_p3_statements117);
                                w = p3_word();
                                state._fsp--;
                                stream_p3_word.add(w.getTree());
                                if (list_w == null) list_w = new ArrayList();
                                list_w.add(w);
                            }
                            break;
                        case 2:
                            {
                                pushFollow(FOLLOW_string_in_p3_statements133);
                                s = string();
                                state._fsp--;
                                stream_string.add(s.getTree());
                                if (list_s == null) list_s = new ArrayList();
                                list_s.add(s);
                            }
                            break;
                        case 3:
                            {
                                pushFollow(FOLLOW_type_in_p3_statements149);
                                t = type();
                                state._fsp--;
                                stream_type.add(t.getTree());
                                if (list_t == null) list_t = new ArrayList();
                                list_t.add(t);
                            }
                            break;
                        case 4:
                            {
                                pushFollow(FOLLOW_pair_in_p3_statements165);
                                p = pair();
                                state._fsp--;
                                stream_pair.add(p.getTree());
                                if (list_p == null) list_p = new ArrayList();
                                list_p.add(p);
                            }
                            break;
                        case 5:
                            {
                                pushFollow(FOLLOW_p3_or_list_in_p3_statements181);
                                o = p3_or_list();
                                state._fsp--;
                                stream_p3_or_list.add(o.getTree());
                                if (list_o == null) list_o = new ArrayList();
                                list_o.add(o);
                            }
                            break;
                        case 6:
                            {
                                pushFollow(FOLLOW_p3_less_in_p3_statements197);
                                l = p3_less();
                                state._fsp--;
                                stream_p3_less.add(l.getTree());
                                if (list_l == null) list_l = new ArrayList();
                                list_l.add(l);
                            }
                            break;
                        default:
                            if (cnt1 >= 1) break loop1;
                            EarlyExitException eee = new EarlyExitException(1, input);
                            throw eee;
                    }
                    cnt1++;
                } while (true);
                retval.tree = root_0;
                RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                RewriteRuleSubtreeStream stream_w = new RewriteRuleSubtreeStream(adaptor, "token w", list_w);
                RewriteRuleSubtreeStream stream_t = new RewriteRuleSubtreeStream(adaptor, "token t", list_t);
                RewriteRuleSubtreeStream stream_s = new RewriteRuleSubtreeStream(adaptor, "token s", list_s);
                RewriteRuleSubtreeStream stream_p = new RewriteRuleSubtreeStream(adaptor, "token p", list_p);
                RewriteRuleSubtreeStream stream_o = new RewriteRuleSubtreeStream(adaptor, "token o", list_o);
                RewriteRuleSubtreeStream stream_l = new RewriteRuleSubtreeStream(adaptor, "token l", list_l);
                root_0 = (CommonTree) adaptor.nil();
                {
                    if (stream_w.hasNext()) {
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(WORDS, "WORDS"), root_1);
                            if (!(stream_w.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_w.hasNext()) {
                                adaptor.addChild(root_1, ((TreeRuleReturnScope) stream_w.nextTree()).getTree());
                            }
                            stream_w.reset();
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    stream_w.reset();
                    if (stream_s.hasNext()) {
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(STRINGS, "STRINGS"), root_1);
                            if (!(stream_s.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_s.hasNext()) {
                                adaptor.addChild(root_1, ((TreeRuleReturnScope) stream_s.nextTree()).getTree());
                            }
                            stream_s.reset();
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    stream_s.reset();
                    if (stream_t.hasNext()) {
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(TYPES, "TYPES"), root_1);
                            if (!(stream_t.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_t.hasNext()) {
                                adaptor.addChild(root_1, ((TreeRuleReturnScope) stream_t.nextTree()).getTree());
                            }
                            stream_t.reset();
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    stream_t.reset();
                    if (stream_p.hasNext()) {
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(PAIRS, "PAIRS"), root_1);
                            if (!(stream_p.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_p.hasNext()) {
                                adaptor.addChild(root_1, ((TreeRuleReturnScope) stream_p.nextTree()).getTree());
                            }
                            stream_p.reset();
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    stream_p.reset();
                    if (stream_o.hasNext()) {
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(ORS, "ORS"), root_1);
                            if (!(stream_o.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_o.hasNext()) {
                                adaptor.addChild(root_1, ((TreeRuleReturnScope) stream_o.nextTree()).getTree());
                            }
                            stream_o.reset();
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    stream_o.reset();
                    if (stream_l.hasNext()) {
                        {
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            root_1 = (CommonTree) adaptor.becomeRoot((CommonTree) adaptor.create(LESSES, "LESSES"), root_1);
                            if (!(stream_l.hasNext())) {
                                throw new RewriteEarlyExitException();
                            }
                            while (stream_l.hasNext()) {
                                adaptor.addChild(root_1, ((TreeRuleReturnScope) stream_l.nextTree()).getTree());
                            }
                            stream_l.reset();
                            adaptor.addChild(root_0, root_1);
                        }
                    }
                    stream_l.reset();
                }
                retval.tree = root_0;
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
            p3_statements_stack.pop();
        }
        return retval;
    }

    public static class p3_word_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.p3_word_return p3_word() throws RecognitionException {
        QueryTreeP3.p3_word_return retval = new QueryTreeP3.p3_word_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree w = null;
        CommonTree o = null;
        List list_o = null;
        CommonTree w_tree = null;
        CommonTree o_tree = null;
        RewriteRuleNodeStream stream_WORD = new RewriteRuleNodeStream(adaptor, "token WORD");
        RewriteRuleNodeStream stream_INCL_STOPWORDS = new RewriteRuleNodeStream(adaptor, "token INCL_STOPWORDS");
        RewriteRuleNodeStream stream_SORT_UP = new RewriteRuleNodeStream(adaptor, "token SORT_UP");
        RewriteRuleNodeStream stream_SORT_DOWN = new RewriteRuleNodeStream(adaptor, "token SORT_DOWN");
        try {
            int alt3 = 2;
            int LA3_0 = input.LA(1);
            if ((LA3_0 == WORD)) {
                int LA3_1 = input.LA(2);
                if ((LA3_1 == DOWN)) {
                    alt3 = 1;
                } else if ((LA3_1 == EOF || (LA3_1 >= UP && LA3_1 <= WORD) || LA3_1 == STRING || LA3_1 == LESS || LA3_1 == OR)) {
                    alt3 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 3, 1, input);
                    throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("", 3, 0, input);
                throw nvae;
            }
            switch(alt3) {
                case 1:
                    {
                        _last = (CommonTree) input.LT(1);
                        {
                            CommonTree _save_last_1 = _last;
                            CommonTree _first_1 = null;
                            CommonTree root_1 = (CommonTree) adaptor.nil();
                            _last = (CommonTree) input.LT(1);
                            w = (CommonTree) match(input, WORD, FOLLOW_WORD_in_p3_word326);
                            stream_WORD.add(w);
                            match(input, Token.DOWN, null);
                            int alt2 = 3;
                            switch(input.LA(1)) {
                                case INCL_STOPWORDS:
                                    {
                                        alt2 = 1;
                                    }
                                    break;
                                case SORT_UP:
                                    {
                                        alt2 = 2;
                                    }
                                    break;
                                case SORT_DOWN:
                                    {
                                        alt2 = 3;
                                    }
                                    break;
                                default:
                                    NoViableAltException nvae = new NoViableAltException("", 2, 0, input);
                                    throw nvae;
                            }
                            switch(alt2) {
                                case 1:
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        o = (CommonTree) match(input, INCL_STOPWORDS, FOLLOW_INCL_STOPWORDS_in_p3_word331);
                                        stream_INCL_STOPWORDS.add(o);
                                        if (list_o == null) list_o = new ArrayList();
                                        list_o.add(o);
                                    }
                                    break;
                                case 2:
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        o = (CommonTree) match(input, SORT_UP, FOLLOW_SORT_UP_in_p3_word337);
                                        stream_SORT_UP.add(o);
                                        if (list_o == null) list_o = new ArrayList();
                                        list_o.add(o);
                                    }
                                    break;
                                case 3:
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        o = (CommonTree) match(input, SORT_DOWN, FOLLOW_SORT_DOWN_in_p3_word343);
                                        stream_SORT_DOWN.add(o);
                                        if (list_o == null) list_o = new ArrayList();
                                        list_o.add(o);
                                    }
                                    break;
                            }
                            match(input, Token.UP, null);
                            adaptor.addChild(root_0, root_1);
                            _last = _save_last_1;
                        }
                        retval.tree = root_0;
                        RewriteRuleNodeStream stream_w = new RewriteRuleNodeStream(adaptor, "token w", w);
                        RewriteRuleNodeStream stream_o = new RewriteRuleNodeStream(adaptor, "token o", list_o);
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        if (!((p3_statements_scope) p3_statements_stack.peek()).words.contains((w != null ? w.getText() : null))) {
                            {
                                CommonTree root_1 = (CommonTree) adaptor.nil();
                                root_1 = (CommonTree) adaptor.becomeRoot(stream_w.nextNode(), root_1);
                                if (!(stream_o.hasNext())) {
                                    throw new RewriteEarlyExitException();
                                }
                                while (stream_o.hasNext()) {
                                    adaptor.addChild(root_1, stream_o.nextNode());
                                }
                                stream_o.reset();
                                adaptor.addChild(root_0, root_1);
                            }
                        } else {
                            root_0 = null;
                        }
                        retval.tree = root_0;
                    }
                    break;
                case 2:
                    {
                        _last = (CommonTree) input.LT(1);
                        w = (CommonTree) match(input, WORD, FOLLOW_WORD_in_p3_word390);
                        stream_WORD.add(w);
                        retval.tree = root_0;
                        RewriteRuleNodeStream stream_w = new RewriteRuleNodeStream(adaptor, "token w", w);
                        RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                        root_0 = (CommonTree) adaptor.nil();
                        if (!((p3_statements_scope) p3_statements_stack.peek()).words.contains((w != null ? w.getText() : null))) {
                            adaptor.addChild(root_0, stream_w.nextNode());
                        } else {
                            root_0 = null;
                        }
                        retval.tree = root_0;
                    }
                    break;
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            ((p3_statements_scope) p3_statements_stack.peek()).words.add((w != null ? w.getText() : null));
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static class p3_less_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.p3_less_return p3_less() throws RecognitionException {
        QueryTreeP3.p3_less_return retval = new QueryTreeP3.p3_less_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree LESS2 = null;
        QueryTreeP3.p3_statements_return p3_statements3 = null;
        CommonTree LESS2_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    LESS2 = (CommonTree) match(input, LESS, FOLLOW_LESS_in_p3_less436);
                    LESS2_tree = (CommonTree) adaptor.dupNode(LESS2);
                    root_1 = (CommonTree) adaptor.becomeRoot(LESS2_tree, root_1);
                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_p3_statements_in_p3_less438);
                    p3_statements3 = p3_statements();
                    state._fsp--;
                    adaptor.addChild(root_1, p3_statements3.getTree());
                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static class p3_or_list_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.p3_or_list_return p3_or_list() throws RecognitionException {
        QueryTreeP3.p3_or_list_return retval = new QueryTreeP3.p3_or_list_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree OR4 = null;
        CommonTree AND5 = null;
        QueryTreeP3.p3_statements_return p3_statements6 = null;
        QueryTreeP3.p3_less_return p3_less7 = null;
        QueryTreeP3.p3_or_list_return p3_or_list8 = null;
        CommonTree OR4_tree = null;
        CommonTree AND5_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    OR4 = (CommonTree) match(input, OR, FOLLOW_OR_in_p3_or_list460);
                    OR4_tree = (CommonTree) adaptor.dupNode(OR4);
                    root_1 = (CommonTree) adaptor.becomeRoot(OR4_tree, root_1);
                    match(input, Token.DOWN, null);
                    int cnt4 = 0;
                    loop4: do {
                        int alt4 = 4;
                        switch(input.LA(1)) {
                            case AND:
                                {
                                    alt4 = 1;
                                }
                                break;
                            case LESS:
                                {
                                    alt4 = 2;
                                }
                                break;
                            case OR:
                                {
                                    alt4 = 3;
                                }
                                break;
                        }
                        switch(alt4) {
                            case 1:
                                {
                                    _last = (CommonTree) input.LT(1);
                                    {
                                        CommonTree _save_last_2 = _last;
                                        CommonTree _first_2 = null;
                                        CommonTree root_2 = (CommonTree) adaptor.nil();
                                        _last = (CommonTree) input.LT(1);
                                        AND5 = (CommonTree) match(input, AND, FOLLOW_AND_in_p3_or_list464);
                                        AND5_tree = (CommonTree) adaptor.dupNode(AND5);
                                        root_2 = (CommonTree) adaptor.becomeRoot(AND5_tree, root_2);
                                        match(input, Token.DOWN, null);
                                        _last = (CommonTree) input.LT(1);
                                        pushFollow(FOLLOW_p3_statements_in_p3_or_list466);
                                        p3_statements6 = p3_statements();
                                        state._fsp--;
                                        adaptor.addChild(root_2, p3_statements6.getTree());
                                        match(input, Token.UP, null);
                                        adaptor.addChild(root_1, root_2);
                                        _last = _save_last_2;
                                    }
                                }
                                break;
                            case 2:
                                {
                                    _last = (CommonTree) input.LT(1);
                                    pushFollow(FOLLOW_p3_less_in_p3_or_list471);
                                    p3_less7 = p3_less();
                                    state._fsp--;
                                    adaptor.addChild(root_1, p3_less7.getTree());
                                }
                                break;
                            case 3:
                                {
                                    _last = (CommonTree) input.LT(1);
                                    pushFollow(FOLLOW_p3_or_list_in_p3_or_list475);
                                    p3_or_list8 = p3_or_list();
                                    state._fsp--;
                                    adaptor.addChild(root_1, p3_or_list8.getTree());
                                }
                                break;
                            default:
                                if (cnt4 >= 1) break loop4;
                                EarlyExitException eee = new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);
                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static class string_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.string_return string() throws RecognitionException {
        QueryTreeP3.string_return retval = new QueryTreeP3.string_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree s = null;
        CommonTree s_tree = null;
        RewriteRuleNodeStream stream_STRING = new RewriteRuleNodeStream(adaptor, "token STRING");
        try {
            {
                _last = (CommonTree) input.LT(1);
                s = (CommonTree) match(input, STRING, FOLLOW_STRING_in_string504);
                stream_STRING.add(s);
                retval.tree = root_0;
                RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                root_0 = (CommonTree) adaptor.nil();
                if (!((p3_statements_scope) p3_statements_stack.peek()).strings.contains((s != null ? s.getText() : null))) {
                    adaptor.addChild(root_0, stream_STRING.nextNode());
                } else {
                    root_0 = null;
                }
                retval.tree = root_0;
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            ((p3_statements_scope) p3_statements_stack.peek()).strings.add((s != null ? s.getText() : null));
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static class type_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.type_return type() throws RecognitionException {
        QueryTreeP3.type_return retval = new QueryTreeP3.type_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree t = null;
        CommonTree t_tree = null;
        RewriteRuleNodeStream stream_TYPE = new RewriteRuleNodeStream(adaptor, "token TYPE");
        try {
            {
                _last = (CommonTree) input.LT(1);
                t = (CommonTree) match(input, TYPE, FOLLOW_TYPE_in_type555);
                stream_TYPE.add(t);
                retval.tree = root_0;
                RewriteRuleNodeStream stream_t = new RewriteRuleNodeStream(adaptor, "token t", t);
                RewriteRuleSubtreeStream stream_retval = new RewriteRuleSubtreeStream(adaptor, "token retval", retval != null ? retval.tree : null);
                root_0 = (CommonTree) adaptor.nil();
                if (!((p3_statements_scope) p3_statements_stack.peek()).types.contains((t != null ? t.getText() : null))) {
                    adaptor.addChild(root_0, stream_t.nextNode());
                } else {
                    root_0 = null;
                }
                retval.tree = root_0;
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
            ((p3_statements_scope) p3_statements_stack.peek()).types.add((t != null ? t.getText() : null));
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static class pair_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.pair_return pair() throws RecognitionException {
        QueryTreeP3.pair_return retval = new QueryTreeP3.pair_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree PAIR9 = null;
        CommonTree NODE10 = null;
        CommonTree GROUP12 = null;
        QueryTreeP3.node_return node11 = null;
        QueryTreeP3.node_return node13 = null;
        CommonTree PAIR9_tree = null;
        CommonTree NODE10_tree = null;
        CommonTree GROUP12_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    PAIR9 = (CommonTree) match(input, PAIR, FOLLOW_PAIR_in_pair596);
                    PAIR9_tree = (CommonTree) adaptor.dupNode(PAIR9);
                    root_1 = (CommonTree) adaptor.becomeRoot(PAIR9_tree, root_1);
                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        NODE10 = (CommonTree) match(input, NODE, FOLLOW_NODE_in_pair599);
                        NODE10_tree = (CommonTree) adaptor.dupNode(NODE10);
                        root_2 = (CommonTree) adaptor.becomeRoot(NODE10_tree, root_2);
                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_node_in_pair601);
                        node11 = node();
                        state._fsp--;
                        adaptor.addChild(root_2, node11.getTree());
                        match(input, Token.UP, null);
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }
                    int alt5 = 2;
                    int LA5_0 = input.LA(1);
                    if ((LA5_0 == GROUP)) {
                        alt5 = 1;
                    }
                    switch(alt5) {
                        case 1:
                            {
                                _last = (CommonTree) input.LT(1);
                                {
                                    CommonTree _save_last_2 = _last;
                                    CommonTree _first_2 = null;
                                    CommonTree root_2 = (CommonTree) adaptor.nil();
                                    _last = (CommonTree) input.LT(1);
                                    GROUP12 = (CommonTree) match(input, GROUP, FOLLOW_GROUP_in_pair606);
                                    GROUP12_tree = (CommonTree) adaptor.dupNode(GROUP12);
                                    root_2 = (CommonTree) adaptor.becomeRoot(GROUP12_tree, root_2);
                                    match(input, Token.DOWN, null);
                                    _last = (CommonTree) input.LT(1);
                                    pushFollow(FOLLOW_node_in_pair608);
                                    node13 = node();
                                    state._fsp--;
                                    adaptor.addChild(root_2, node13.getTree());
                                    match(input, Token.UP, null);
                                    adaptor.addChild(root_1, root_2);
                                    _last = _save_last_2;
                                }
                            }
                            break;
                    }
                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static class node_return extends TreeRuleReturnScope {

        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    public final QueryTreeP3.node_return node() throws RecognitionException {
        QueryTreeP3.node_return retval = new QueryTreeP3.node_return();
        retval.start = input.LT(1);
        CommonTree root_0 = null;
        CommonTree _first_0 = null;
        CommonTree _last = null;
        CommonTree set14 = null;
        CommonTree HEX_LITERAL15 = null;
        CommonTree set14_tree = null;
        CommonTree HEX_LITERAL15_tree = null;
        try {
            {
                root_0 = (CommonTree) adaptor.nil();
                _last = (CommonTree) input.LT(1);
                set14 = (CommonTree) input.LT(1);
                if ((input.LA(1) >= NODE_KEYWORD && input.LA(1) <= ID_KEYWORD)) {
                    input.consume();
                    set14_tree = (CommonTree) adaptor.dupNode(set14);
                    adaptor.addChild(root_0, set14_tree);
                    state.errorRecovery = false;
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }
                _last = (CommonTree) input.LT(1);
                HEX_LITERAL15 = (CommonTree) match(input, HEX_LITERAL, FOLLOW_HEX_LITERAL_in_node700);
                HEX_LITERAL15_tree = (CommonTree) adaptor.dupNode(HEX_LITERAL15);
                adaptor.addChild(root_0, HEX_LITERAL15_tree);
            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);
        } catch (RecognitionException e) {
            System.out.println("Throwing an exception" + QueryTreeP3.class);
            throw e;
        } finally {
        }
        return retval;
    }

    public static final BitSet FOLLOW_p3_statements_in_p3_query83 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_p3_word_in_p3_statements117 = new BitSet(new long[] { 0x0000001100000272L });

    public static final BitSet FOLLOW_string_in_p3_statements133 = new BitSet(new long[] { 0x0000001100000272L });

    public static final BitSet FOLLOW_type_in_p3_statements149 = new BitSet(new long[] { 0x0000001100000272L });

    public static final BitSet FOLLOW_pair_in_p3_statements165 = new BitSet(new long[] { 0x0000001100000272L });

    public static final BitSet FOLLOW_p3_or_list_in_p3_statements181 = new BitSet(new long[] { 0x0000001100000272L });

    public static final BitSet FOLLOW_p3_less_in_p3_statements197 = new BitSet(new long[] { 0x0000001100000272L });

    public static final BitSet FOLLOW_WORD_in_p3_word326 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_INCL_STOPWORDS_in_p3_word331 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_SORT_UP_in_p3_word337 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_SORT_DOWN_in_p3_word343 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_WORD_in_p3_word390 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_LESS_in_p3_less436 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_p3_statements_in_p3_less438 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_OR_in_p3_or_list460 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_AND_in_p3_or_list464 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_p3_statements_in_p3_or_list466 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_p3_less_in_p3_or_list471 = new BitSet(new long[] { 0x0000001100000A78L });

    public static final BitSet FOLLOW_p3_or_list_in_p3_or_list475 = new BitSet(new long[] { 0x0000001100000A78L });

    public static final BitSet FOLLOW_STRING_in_string504 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_TYPE_in_type555 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_PAIR_in_pair596 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_NODE_in_pair599 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_node_in_pair601 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_GROUP_in_pair606 = new BitSet(new long[] { 0x0000000000000004L });

    public static final BitSet FOLLOW_node_in_pair608 = new BitSet(new long[] { 0x0000000000000008L });

    public static final BitSet FOLLOW_set_in_node626 = new BitSet(new long[] { 0x0000008000000000L });

    public static final BitSet FOLLOW_HEX_LITERAL_in_node700 = new BitSet(new long[] { 0x0000000000000002L });
}
