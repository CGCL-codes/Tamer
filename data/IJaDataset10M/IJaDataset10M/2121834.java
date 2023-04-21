package org.colony.antlr.copy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;

public class DictParserClass extends antlr.LLkParser implements DictLexerTokenTypes {

    protected DictParserClass(TokenBuffer tokenBuf, int k) {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }

    public DictParserClass(TokenBuffer tokenBuf) {
        this(tokenBuf, 1);
    }

    protected DictParserClass(TokenStream lexer, int k) {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    public DictParserClass(TokenStream lexer) {
        this(lexer, 1);
    }

    public DictParserClass(ParserSharedInputState state) {
        super(state, 1);
        tokenNames = _tokenNames;
    }

    public final Object process() throws RecognitionException, TokenStreamException {
        Object result;
        Object o = null;
        result = null;
        try {
            result = value();
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_0);
            } else {
                throw ex;
            }
        }
        return result;
    }

    public final Object value() throws RecognitionException, TokenStreamException {
        Object result;
        result = null;
        try {
            switch(LA(1)) {
                case POINTY_BRA:
                    {
                        match(POINTY_BRA);
                        result = typed_value();
                        break;
                    }
                case NUM_FLOAT:
                case NUM_LONG:
                case NUM_DOUBLE:
                case CURLY_BRA:
                case SQUARE_BRA:
                case STRING_LITERAL:
                case IDENT:
                case NUM_INT:
                    {
                        result = untyped_value();
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_1);
            } else {
                throw ex;
            }
        }
        return result;
    }

    public final Object klass() throws RecognitionException, TokenStreamException {
        Object o;
        Token w = null;
        Token w1 = null;
        o = null;
        StringBuffer buf = new StringBuffer();
        try {
            w = LT(1);
            match(IDENT);
            if (inputState.guessing == 0) {
                buf.append(w.getText());
            }
            {
                _loop77: do {
                    if ((LA(1) == CLASS_SEP)) {
                        match(CLASS_SEP);
                        if (inputState.guessing == 0) {
                            buf.append('.');
                        }
                        w1 = LT(1);
                        match(IDENT);
                        if (inputState.guessing == 0) {
                            buf.append(w1.getText());
                        }
                    } else {
                        break _loop77;
                    }
                } while (true);
            }
            match(POINTY_KET);
            if (inputState.guessing == 0) {
                Class k = null;
                try {
                    k = Class.forName(buf.toString());
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                try {
                    o = k.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_2);
            } else {
                throw ex;
            }
        }
        return o;
    }

    public final Map map(Map container) throws RecognitionException, TokenStreamException {
        Map m;
        m = (container != null) ? container : new HashMap();
        try {
            match(CURLY_BRA);
            {
                _loop80: do {
                    if ((_tokenSet_3.member(LA(1)))) {
                        pairs(m);
                    } else {
                        break _loop80;
                    }
                } while (true);
            }
            match(CURLY_KET);
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_1);
            } else {
                throw ex;
            }
        }
        return m;
    }

    public final void pairs(Map m) throws RecognitionException, TokenStreamException {
        try {
            pair(m);
            {
                _loop101: do {
                    if ((LA(1) == ENTRY_SEP)) {
                        match(ENTRY_SEP);
                        pair(m);
                    } else {
                        break _loop101;
                    }
                } while (true);
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_4);
            } else {
                throw ex;
            }
        }
    }

    public final List list(List container) throws RecognitionException, TokenStreamException {
        List l;
        l = (container != null) ? container : new ArrayList();
        Object v = null;
        try {
            match(SQUARE_BRA);
            {
                _loop85: do {
                    if ((_tokenSet_5.member(LA(1)))) {
                        v = value();
                        if (inputState.guessing == 0) {
                            l.add(v);
                        }
                        {
                            _loop84: do {
                                if ((LA(1) == ENTRY_SEP)) {
                                    match(ENTRY_SEP);
                                    v = value();
                                    if (inputState.guessing == 0) {
                                        l.add(v);
                                    }
                                } else {
                                    break _loop84;
                                }
                            } while (true);
                        }
                    } else {
                        break _loop85;
                    }
                } while (true);
            }
            match(SQUARE_KET);
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_1);
            } else {
                throw ex;
            }
        }
        return l;
    }

    public final Object typed_value() throws RecognitionException, TokenStreamException {
        Object result;
        result = null;
        Object o = null;
        Object a = null;
        try {
            boolean synPredMatched91 = false;
            if (((LA(1) == IDENT))) {
                int _m91 = mark();
                synPredMatched91 = true;
                inputState.guessing++;
                try {
                    {
                        klass();
                        match(CURLY_BRA);
                    }
                } catch (RecognitionException pe) {
                    synPredMatched91 = false;
                }
                rewind(_m91);
                inputState.guessing--;
            }
            if (synPredMatched91) {
                {
                    o = klass();
                }
                result = map((Map) o);
            } else {
                boolean synPredMatched94 = false;
                if (((LA(1) == IDENT))) {
                    int _m94 = mark();
                    synPredMatched94 = true;
                    inputState.guessing++;
                    try {
                        {
                            klass();
                            match(SQUARE_BRA);
                        }
                    } catch (RecognitionException pe) {
                        synPredMatched94 = false;
                    }
                    rewind(_m94);
                    inputState.guessing--;
                }
                if (synPredMatched94) {
                    {
                        o = klass();
                    }
                    result = list((List) o);
                } else if ((LA(1) == IDENT)) {
                    {
                        o = klass();
                    }
                    a = atom();
                    if (inputState.guessing == 0) {
                        Class hackClass = o.getClass();
                        Class[] type = { a.getClass() };
                        Object[] arg = { a };
                        try {
                            return hackClass.getConstructor(type).newInstance(arg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    throw new NoViableAltException(LT(1), getFilename());
                }
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_1);
            } else {
                throw ex;
            }
        }
        return result;
    }

    public final Object untyped_value() throws RecognitionException, TokenStreamException {
        Object result;
        result = null;
        Object o = null;
        try {
            switch(LA(1)) {
                case NUM_FLOAT:
                case NUM_LONG:
                case NUM_DOUBLE:
                case STRING_LITERAL:
                case IDENT:
                case NUM_INT:
                    {
                        result = atom();
                        break;
                    }
                case CURLY_BRA:
                    {
                        result = map((Map) o);
                        break;
                    }
                case SQUARE_BRA:
                    {
                        result = list((List) o);
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_1);
            } else {
                throw ex;
            }
        }
        return result;
    }

    public final Object atom() throws RecognitionException, TokenStreamException {
        Object o;
        Token n = null;
        Token n1 = null;
        Token n2 = null;
        Token n3 = null;
        Token n4 = null;
        Token n5 = null;
        o = null;
        try {
            switch(LA(1)) {
                case NUM_INT:
                    {
                        n = LT(1);
                        match(NUM_INT);
                        if (inputState.guessing == 0) {
                            o = new Integer(n.getText());
                        }
                        break;
                    }
                case STRING_LITERAL:
                    {
                        n1 = LT(1);
                        match(STRING_LITERAL);
                        if (inputState.guessing == 0) {
                            o = n1.getText().substring(1, n1.getText().length() - 1);
                        }
                        break;
                    }
                case NUM_FLOAT:
                    {
                        n2 = LT(1);
                        match(NUM_FLOAT);
                        if (inputState.guessing == 0) {
                            String s = n2.getText();
                            o = (s.endsWith("f") || s.endsWith("F")) ? new Float(s.substring(0, s.length() - 1)) : new Float(s);
                        }
                        break;
                    }
                case NUM_LONG:
                    {
                        n3 = LT(1);
                        match(NUM_LONG);
                        if (inputState.guessing == 0) {
                            o = new Long(n3.getText().substring(0, n3.getText().length() - 1));
                        }
                        break;
                    }
                case NUM_DOUBLE:
                    {
                        n4 = LT(1);
                        match(NUM_DOUBLE);
                        if (inputState.guessing == 0) {
                            String s = n4.getText();
                            o = (s.endsWith("f") || s.endsWith("F")) ? new Double(s.substring(0, s.length() - 1)) : new Double(s);
                        }
                        break;
                    }
                case IDENT:
                    {
                        n5 = LT(1);
                        match(IDENT);
                        if (inputState.guessing == 0) {
                            o = n5.getText();
                        }
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_6);
            } else {
                throw ex;
            }
        }
        return o;
    }

    public final void pair(Map m) throws RecognitionException, TokenStreamException {
        Object k;
        Object v;
        try {
            k = atom();
            match(KV_SEP);
            v = value();
            if (inputState.guessing == 0) {
                m.put(k, v);
            }
        } catch (RecognitionException ex) {
            if (inputState.guessing == 0) {
                reportError(ex);
                recover(ex, _tokenSet_7);
            } else {
                throw ex;
            }
        }
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "NUM_FLOAT", "NUM_LONG", "NUM_DOUBLE", "WS", "CURLY_BRA", "CURLY_KET", "SQUARE_BRA", "SQUARE_KET", "POINTY_BRA", "POINTY_KET", "KV_SEP", "ENTRY_SEP", "CLASS_SEP", "P_BRA", "P_KET", "WHITESPACE", "ESC", "HEX_DIGIT", "STRING_LITERAL", "IDENT", "NUM_INT", "EXPONENT", "FLOAT_SUFFIX", "DOT" };

    private static final long[] mk_tokenSet_0() {
        long[] data = { 2L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1() {
        long[] data = { 29400946L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2() {
        long[] data = { 29361520L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3() {
        long[] data = { 29360240L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

    private static final long[] mk_tokenSet_4() {
        long[] data = { 29360752L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

    private static final long[] mk_tokenSet_5() {
        long[] data = { 29365616L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

    private static final long[] mk_tokenSet_6() {
        long[] data = { 29417330L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());

    private static final long[] mk_tokenSet_7() {
        long[] data = { 29393520L, 0L };
        return data;
    }

    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
}
