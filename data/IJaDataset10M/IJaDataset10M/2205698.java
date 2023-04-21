package net.deytan.wofee.gae.persistence.action.query;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GqlLexer extends Lexer {

    public static final int NESTED = 16;

    public static final int PACKAGE = 14;

    public static final int WHERE = 5;

    public static final int CLASS = 15;

    public static final int CLASS_NAME = 13;

    public static final int PROPERTY = 12;

    public static final int EQUAL = 7;

    public static final int LETTER_UP = 11;

    public static final int NUMBER = 9;

    public static final int WHITESPACE = 18;

    public static final int DIGIT = 8;

    public static final int LETTER_LOW = 10;

    public static final int FROM = 4;

    public static final int DOT = 6;

    public static final int EOF = -1;

    public static final int STRING = 17;

    public GqlLexer() {
        ;
    }

    public GqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public GqlLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "/home/deytan/projects/workspace/Wofee/antlr/grammar/GqlLexer.g";
    }

    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("FROM");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("WHERE");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('.');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('=');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt1 = 0;
                loop1: do {
                    int alt1 = 2;
                    int LA1_0 = input.LA(1);
                    if (((LA1_0 >= '0' && LA1_0 <= '9'))) {
                        alt1 = 1;
                    }
                    switch(alt1) {
                        case 1:
                            {
                                mDIGIT();
                            }
                            break;
                        default:
                            if (cnt1 >= 1) break loop1;
                            EarlyExitException eee = new EarlyExitException(1, input);
                            throw eee;
                    }
                    cnt1++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mPROPERTY() throws RecognitionException {
        try {
            int _type = PROPERTY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                mLETTER_LOW();
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= '0' && LA2_0 <= '9') || (LA2_0 >= 'A' && LA2_0 <= 'Z') || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop2;
                    }
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mCLASS_NAME() throws RecognitionException {
        try {
            int _type = CLASS_NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                mLETTER_UP();
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if (((LA3_0 >= '0' && LA3_0 <= '9') || (LA3_0 >= 'A' && LA3_0 <= 'Z') || (LA3_0 >= 'a' && LA3_0 <= 'z'))) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop3;
                    }
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mPACKAGE() throws RecognitionException {
        try {
            int _type = PACKAGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt5 = 0;
                loop5: do {
                    int alt5 = 2;
                    int LA5_0 = input.LA(1);
                    if (((LA5_0 >= 'a' && LA5_0 <= 'z'))) {
                        alt5 = 1;
                    }
                    switch(alt5) {
                        case 1:
                            {
                                mLETTER_LOW();
                                loop4: do {
                                    int alt4 = 2;
                                    int LA4_0 = input.LA(1);
                                    if (((LA4_0 >= '0' && LA4_0 <= '9') || (LA4_0 >= 'a' && LA4_0 <= 'z'))) {
                                        alt4 = 1;
                                    }
                                    switch(alt4) {
                                        case 1:
                                            {
                                                if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                                                    input.consume();
                                                } else {
                                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                                    recover(mse);
                                                    throw mse;
                                                }
                                            }
                                            break;
                                        default:
                                            break loop4;
                                    }
                                } while (true);
                                mDOT();
                            }
                            break;
                        default:
                            if (cnt5 >= 1) break loop5;
                            EarlyExitException eee = new EarlyExitException(5, input);
                            throw eee;
                    }
                    cnt5++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mCLASS() throws RecognitionException {
        try {
            int _type = CLASS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int alt6 = 2;
                int LA6_0 = input.LA(1);
                if (((LA6_0 >= 'a' && LA6_0 <= 'z'))) {
                    alt6 = 1;
                }
                switch(alt6) {
                    case 1:
                        {
                            mPACKAGE();
                        }
                        break;
                }
                mCLASS_NAME();
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNESTED() throws RecognitionException {
        try {
            int _type = NESTED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                mPROPERTY();
                loop7: do {
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if ((LA7_0 == '.')) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                mDOT();
                                mPROPERTY();
                            }
                            break;
                        default:
                            break loop7;
                    }
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('\'');
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if (((LA8_0 >= ' ' && LA8_0 <= '&') || (LA8_0 >= '(' && LA8_0 <= '￿'))) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '&') || (input.LA(1) >= '(' && input.LA(1) <= '￿')) {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop8;
                    }
                } while (true);
                match('\'');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt9 = 0;
                loop9: do {
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);
                    if (((LA9_0 >= '\t' && LA9_0 <= '\n') || (LA9_0 >= '\f' && LA9_0 <= '\r') || LA9_0 == ' ')) {
                        alt9 = 1;
                    }
                    switch(alt9) {
                        case 1:
                            {
                                if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || (input.LA(1) >= '\f' && input.LA(1) <= '\r') || input.LA(1) == ' ') {
                                    input.consume();
                                } else {
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            if (cnt9 >= 1) break loop9;
                            EarlyExitException eee = new EarlyExitException(9, input);
                            throw eee;
                    }
                    cnt9++;
                } while (true);
                _channel = HIDDEN;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mDIGIT() throws RecognitionException {
        try {
            {
                matchRange('0', '9');
            }
        } finally {
        }
    }

    public final void mLETTER_UP() throws RecognitionException {
        try {
            {
                matchRange('A', 'Z');
            }
        } finally {
        }
    }

    public final void mLETTER_LOW() throws RecognitionException {
        try {
            {
                matchRange('a', 'z');
            }
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt10 = 12;
        alt10 = dfa10.predict(input);
        switch(alt10) {
            case 1:
                {
                    mFROM();
                }
                break;
            case 2:
                {
                    mWHERE();
                }
                break;
            case 3:
                {
                    mDOT();
                }
                break;
            case 4:
                {
                    mEQUAL();
                }
                break;
            case 5:
                {
                    mNUMBER();
                }
                break;
            case 6:
                {
                    mPROPERTY();
                }
                break;
            case 7:
                {
                    mCLASS_NAME();
                }
                break;
            case 8:
                {
                    mPACKAGE();
                }
                break;
            case 9:
                {
                    mCLASS();
                }
                break;
            case 10:
                {
                    mNESTED();
                }
                break;
            case 11:
                {
                    mSTRING();
                }
                break;
            case 12:
                {
                    mWHITESPACE();
                }
                break;
        }
    }

    protected DFA10 dfa10 = new DFA10(this);

    static final String DFA10_eotS = "\1￿\2\13\3￿\1\16\1\13\2￿\1\13\1￿\2\13\1￿" + "\2\16\1\25\2\13\2￿\1\24\1￿\1\33\1\13\1\24\1￿\1\35" + "\1￿";

    static final String DFA10_eofS = "\36￿";

    static final String DFA10_minS = "\1\11\2\60\3￿\1\56\1\60\2￿\1\60\1￿\2\60\1￿\2" + "\56\1\101\2\60\2￿\1\56\1￿\2\60\1\56\1￿\1\60\1￿";

    static final String DFA10_maxS = "\3\172\3￿\2\172\2￿\1\172\1￿\2\172\1￿\5\172\2" + "￿\1\172\1￿\3\172\1￿\1\172\1￿";

    static final String DFA10_acceptS = "\3￿\1\3\1\4\1\5\2￿\1\13\1\14\1￿\1\7\2￿\1\6\5" + "￿\1\12\1\10\1￿\1\11\3￿\1\1\1￿\1\2";

    static final String DFA10_specialS = "\36￿}>";

    static final String[] DFA10_transitionS = { "\2\11\1￿\2\11\22￿\1\11\6￿\1\10\6￿\1\3\1" + "￿\12\5\3￿\1\4\3￿\5\7\1\1\20\7\1\2\3\7\6￿" + "\32\6", "\12\14\7￿\21\14\1\12\10\14\6￿\32\14", "\12\14\7￿\7\14\1\15\22\14\6￿\32\14", "", "", "", "\1\21\1￿\12\17\7￿\32\20\6￿\32\17", "\12\14\7￿\32\14\6￿\32\14", "", "", "\12\14\7￿\16\14\1\22\13\14\6￿\32\14", "", "\12\14\7￿\32\14\6￿\32\14", "\12\14\7￿\4\14\1\23\25\14\6￿\32\14", "", "\1\21\1￿\12\17\7￿\32\20\6￿\32\17", "\1\24\1￿\12\20\7￿\32\20\6￿\32\20", "\32\27\6￿\32\26", "\12\14\7￿\14\14\1\30\15\14\6￿\32\14", "\12\14\7￿\21\14\1\31\10\14\6￿\32\14", "", "", "\1\21\1￿\12\32\47￿\32\32", "", "\12\14\7￿\32\14\6￿\32\14", "\12\14\7￿\4\14\1\34\25\14\6￿\32\14", "\1\21\1￿\12\32\47￿\32\32", "", "\12\14\7￿\32\14\6￿\32\14", "" };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);

    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);

    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);

    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);

    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);

    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);

    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }

        public String getDescription() {
            return "1:1: Tokens : ( FROM | WHERE | DOT | EQUAL | NUMBER | PROPERTY | CLASS_NAME | PACKAGE | CLASS | NESTED | STRING | WHITESPACE );";
        }
    }
}
