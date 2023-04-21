package org.databene.regex.antlr;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class RegexLexer extends Lexer {

    public static final int QUANT = 22;

    public static final int CLASS = 18;

    public static final int SIMPLEQUANTIFIER = 12;

    public static final int RBRACE = 28;

    public static final int LETTER = 23;

    public static final int ESCAPEDCHARACTER = 7;

    public static final int SPECIALCHARACTER = 6;

    public static final int LBRACE = 27;

    public static final int HEXCHAR = 10;

    public static final int RANGE = 19;

    public static final int EXCL = 21;

    public static final int INT = 13;

    public static final int NONTYPEABLECHARACTER = 8;

    public static final int EOF = -1;

    public static final int ALPHANUM = 5;

    public static final int LBRACKET = 29;

    public static final int SEQUENCE = 16;

    public static final int T__31 = 31;

    public static final int GROUP = 14;

    public static final int CODEDCHAR = 11;

    public static final int T__32 = 32;

    public static final int T__33 = 33;

    public static final int T__34 = 34;

    public static final int T__35 = 35;

    public static final int T__36 = 36;

    public static final int T__37 = 37;

    public static final int PREDEFINEDCLASS = 4;

    public static final int FACTOR = 17;

    public static final int OCTALDIGIT = 25;

    public static final int INCL = 20;

    public static final int DIGIT = 24;

    public static final int RBRACKET = 30;

    public static final int CHOICE = 15;

    public static final int OCTALCHAR = 9;

    public static final int HEXDIGIT = 26;

    boolean numQuantifierMode = false;

    boolean classMode = false;

    @Override
    public Token nextToken() {
        while (true) {
            state.token = null;
            state.channel = Token.DEFAULT_CHANNEL;
            state.tokenStartCharIndex = input.index();
            state.tokenStartCharPositionInLine = input.getCharPositionInLine();
            state.tokenStartLine = input.getLine();
            state.text = null;
            if (input.LA(1) == CharStream.EOF) {
                return Token.EOF_TOKEN;
            }
            try {
                mTokens();
                if (state.token == null) {
                    emit();
                } else if (state.token == Token.SKIP_TOKEN) {
                    continue;
                }
                return state.token;
            } catch (RecognitionException re) {
                reportError(re);
                throw new RuntimeException(getClass().getSimpleName() + " error", re);
            }
        }
    }

    public RegexLexer() {
        ;
    }

    public RegexLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public RegexLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "/Users/volker/Documents/databene/webdecs/src/main/resources/org/databene/regex/antlr/Regex.g";
    }

    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('^');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('$');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('|');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('-');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('(');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(')');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(',');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mGROUP() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mCHOICE() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mSEQUENCE() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mFACTOR() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mCLASS() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mRANGE() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mINCL() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mEXCL() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mQUANT() throws RecognitionException {
        try {
            {
            }
        } finally {
        }
    }

    public final void mPREDEFINEDCLASS() throws RecognitionException {
        try {
            int _type = PREDEFINEDCLASS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt1 = 7;
            int LA1_0 = input.LA(1);
            if ((LA1_0 == '.')) {
                alt1 = 1;
            } else if ((LA1_0 == '\\')) {
                switch(input.LA(2)) {
                    case 'd':
                        {
                            alt1 = 2;
                        }
                        break;
                    case 'D':
                        {
                            alt1 = 3;
                        }
                        break;
                    case 's':
                        {
                            alt1 = 4;
                        }
                        break;
                    case 'S':
                        {
                            alt1 = 5;
                        }
                        break;
                    case 'w':
                        {
                            alt1 = 6;
                        }
                        break;
                    case 'W':
                        {
                            alt1 = 7;
                        }
                        break;
                    default:
                        NoViableAltException nvae = new NoViableAltException("", 1, 2, input);
                        throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("", 1, 0, input);
                throw nvae;
            }
            switch(alt1) {
                case 1:
                    {
                        match('.');
                    }
                    break;
                case 2:
                    {
                        match("\\d");
                    }
                    break;
                case 3:
                    {
                        match("\\D");
                    }
                    break;
                case 4:
                    {
                        match("\\s");
                    }
                    break;
                case 5:
                    {
                        match("\\S");
                    }
                    break;
                case 6:
                    {
                        match("\\w");
                    }
                    break;
                case 7:
                    {
                        match("\\W");
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mALPHANUM() throws RecognitionException {
        try {
            int _type = ALPHANUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt2 = 2;
            int LA2_0 = input.LA(1);
            if (((LA2_0 >= 'A' && LA2_0 <= 'Z') || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
                alt2 = 1;
            } else if (((LA2_0 >= '0' && LA2_0 <= '9')) && ((!numQuantifierMode))) {
                alt2 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("", 2, 0, input);
                throw nvae;
            }
            switch(alt2) {
                case 1:
                    {
                        mLETTER();
                    }
                    break;
                case 2:
                    {
                        if (!((!numQuantifierMode))) {
                            throw new FailedPredicateException(input, "ALPHANUM", "!numQuantifierMode");
                        }
                        mDIGIT();
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSPECIALCHARACTER() throws RecognitionException {
        try {
            int _type = SPECIALCHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt3 = 19;
            alt3 = dfa3.predict(input);
            switch(alt3) {
                case 1:
                    {
                        match(' ');
                    }
                    break;
                case 2:
                    {
                        match('!');
                    }
                    break;
                case 3:
                    {
                        match('\'');
                    }
                    break;
                case 4:
                    {
                        match('\"');
                    }
                    break;
                case 5:
                    {
                        match('%');
                    }
                    break;
                case 6:
                    {
                        match('&');
                    }
                    break;
                case 7:
                    {
                        match('/');
                    }
                    break;
                case 8:
                    {
                        match(':');
                    }
                    break;
                case 9:
                    {
                        match(';');
                    }
                    break;
                case 10:
                    {
                        match('<');
                    }
                    break;
                case 11:
                    {
                        match('=');
                    }
                    break;
                case 12:
                    {
                        match('>');
                    }
                    break;
                case 13:
                    {
                        match('@');
                    }
                    break;
                case 14:
                    {
                        match('_');
                    }
                    break;
                case 15:
                    {
                        match('`');
                    }
                    break;
                case 16:
                    {
                        match('~');
                    }
                    break;
                case 17:
                    {
                        match('#');
                    }
                    break;
                case 18:
                    {
                        if (!((!numQuantifierMode))) {
                            throw new FailedPredicateException(input, "SPECIALCHARACTER", "!numQuantifierMode");
                        }
                        match(',');
                    }
                    break;
                case 19:
                    {
                        if (!((classMode))) {
                            throw new FailedPredicateException(input, "SPECIALCHARACTER", "classMode");
                        }
                        if ((input.LA(1) >= '*' && input.LA(1) <= '+') || input.LA(1) == '?') {
                            input.consume();
                        } else {
                            MismatchedSetException mse = new MismatchedSetException(null, input);
                            recover(mse);
                            throw mse;
                        }
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mESCAPEDCHARACTER() throws RecognitionException {
        try {
            int _type = ESCAPEDCHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt4 = 18;
            alt4 = dfa4.predict(input);
            switch(alt4) {
                case 1:
                    {
                        match("\\$");
                    }
                    break;
                case 2:
                    {
                        match("\\&");
                    }
                    break;
                case 3:
                    {
                        match("\\(");
                    }
                    break;
                case 4:
                    {
                        match("\\)");
                    }
                    break;
                case 5:
                    {
                    }
                    break;
                case 6:
                    {
                        match("\\,");
                    }
                    break;
                case 7:
                    {
                        match("\\-");
                    }
                    break;
                case 8:
                    {
                        match("\\.");
                    }
                    break;
                case 9:
                    {
                        match("\\[");
                    }
                    break;
                case 10:
                    {
                        match("\\]");
                    }
                    break;
                case 11:
                    {
                        match("\\^");
                    }
                    break;
                case 12:
                    {
                        match("\\{");
                    }
                    break;
                case 13:
                    {
                        match("\\}");
                    }
                    break;
                case 14:
                    {
                        match("\\\\");
                    }
                    break;
                case 15:
                    {
                        match("\\|");
                    }
                    break;
                case 16:
                    {
                        match("\\*");
                    }
                    break;
                case 17:
                    {
                        match("\\+");
                    }
                    break;
                case 18:
                    {
                        match("\\?");
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNONTYPEABLECHARACTER() throws RecognitionException {
        try {
            int _type = NONTYPEABLECHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt5 = 6;
            int LA5_0 = input.LA(1);
            if ((LA5_0 == '\\')) {
                switch(input.LA(2)) {
                    case 'r':
                        {
                            alt5 = 1;
                        }
                        break;
                    case 'n':
                        {
                            alt5 = 2;
                        }
                        break;
                    case 't':
                        {
                            alt5 = 3;
                        }
                        break;
                    case 'f':
                        {
                            alt5 = 4;
                        }
                        break;
                    case 'a':
                        {
                            alt5 = 5;
                        }
                        break;
                    case 'e':
                        {
                            alt5 = 6;
                        }
                        break;
                    default:
                        NoViableAltException nvae = new NoViableAltException("", 5, 1, input);
                        throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("", 5, 0, input);
                throw nvae;
            }
            switch(alt5) {
                case 1:
                    {
                        match("\\r");
                    }
                    break;
                case 2:
                    {
                        match("\\n");
                    }
                    break;
                case 3:
                    {
                        match("\\t");
                    }
                    break;
                case 4:
                    {
                        match("\\f");
                    }
                    break;
                case 5:
                    {
                        match("\\a");
                    }
                    break;
                case 6:
                    {
                        match("\\e");
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mOCTALCHAR() throws RecognitionException {
        try {
            int _type = OCTALCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("\\0");
                mOCTALDIGIT();
                mOCTALDIGIT();
                mOCTALDIGIT();
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mHEXCHAR() throws RecognitionException {
        try {
            int _type = HEXCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt6 = 2;
            int LA6_0 = input.LA(1);
            if ((LA6_0 == '\\')) {
                int LA6_1 = input.LA(2);
                if ((LA6_1 == 'x')) {
                    alt6 = 1;
                } else if ((LA6_1 == 'u')) {
                    alt6 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 6, 1, input);
                    throw nvae;
                }
            } else {
                NoViableAltException nvae = new NoViableAltException("", 6, 0, input);
                throw nvae;
            }
            switch(alt6) {
                case 1:
                    {
                        match("\\x");
                        mHEXDIGIT();
                        mHEXDIGIT();
                    }
                    break;
                case 2:
                    {
                        match("\\u");
                        mHEXDIGIT();
                        mHEXDIGIT();
                        mHEXDIGIT();
                        mHEXDIGIT();
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mCODEDCHAR() throws RecognitionException {
        try {
            int _type = CODEDCHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("\\c");
                mLETTER();
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSIMPLEQUANTIFIER() throws RecognitionException {
        try {
            int _type = SIMPLEQUANTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                if (!((!classMode))) {
                    throw new FailedPredicateException(input, "SIMPLEQUANTIFIER", "!classMode");
                }
                if ((input.LA(1) >= '*' && input.LA(1) <= '+') || input.LA(1) == '?') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                if (!((numQuantifierMode))) {
                    throw new FailedPredicateException(input, "INT", "numQuantifierMode");
                }
                int cnt7 = 0;
                loop7: do {
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if (((LA7_0 >= '0' && LA7_0 <= '9'))) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                mDIGIT();
                            }
                            break;
                        default:
                            if (cnt7 >= 1) break loop7;
                            EarlyExitException eee = new EarlyExitException(7, input);
                            throw eee;
                    }
                    cnt7++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('{');
                numQuantifierMode = true;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('}');
                numQuantifierMode = false;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('[');
                classMode = true;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(']');
                classMode = false;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mLETTER() throws RecognitionException {
        try {
            {
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mHEXDIGIT() throws RecognitionException {
        try {
            {
                if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'F') || (input.LA(1) >= 'a' && input.LA(1) <= 'f')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
            }
        } finally {
        }
    }

    public final void mOCTALDIGIT() throws RecognitionException {
        try {
            {
                matchRange('0', '7');
            }
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

    public void mTokens() throws RecognitionException {
        int alt8 = 21;
        alt8 = dfa8.predict(input);
        switch(alt8) {
            case 1:
                {
                    mT__31();
                }
                break;
            case 2:
                {
                    mT__32();
                }
                break;
            case 3:
                {
                    mT__33();
                }
                break;
            case 4:
                {
                    mT__34();
                }
                break;
            case 5:
                {
                    mT__35();
                }
                break;
            case 6:
                {
                    mT__36();
                }
                break;
            case 7:
                {
                    mT__37();
                }
                break;
            case 8:
                {
                    mPREDEFINEDCLASS();
                }
                break;
            case 9:
                {
                    mALPHANUM();
                }
                break;
            case 10:
                {
                    mSPECIALCHARACTER();
                }
                break;
            case 11:
                {
                    mESCAPEDCHARACTER();
                }
                break;
            case 12:
                {
                    mNONTYPEABLECHARACTER();
                }
                break;
            case 13:
                {
                    mOCTALCHAR();
                }
                break;
            case 14:
                {
                    mHEXCHAR();
                }
                break;
            case 15:
                {
                    mCODEDCHAR();
                }
                break;
            case 16:
                {
                    mSIMPLEQUANTIFIER();
                }
                break;
            case 17:
                {
                    mINT();
                }
                break;
            case 18:
                {
                    mLBRACE();
                }
                break;
            case 19:
                {
                    mRBRACE();
                }
                break;
            case 20:
                {
                    mLBRACKET();
                }
                break;
            case 21:
                {
                    mRBRACKET();
                }
                break;
        }
    }

    protected DFA3 dfa3 = new DFA3(this);

    protected DFA4 dfa4 = new DFA4(this);

    protected DFA8 dfa8 = new DFA8(this);

    static final String DFA3_eotS = "\24￿";

    static final String DFA3_eofS = "\24￿";

    static final String DFA3_minS = "\1\40\23￿";

    static final String DFA3_maxS = "\1\176\23￿";

    static final String DFA3_acceptS = "\1￿\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1" + "\15\1\16\1\17\1\20\1\21\1\22\1\23";

    static final String DFA3_specialS = "\1\0\23￿}>";

    static final String[] DFA3_transitionS = { "\1\1\1\2\1\4\1\21\1￿\1\5\1\6\1\3\2￿\2\23\1\22\2￿" + "\1\7\12￿\1\10\1\11\1\12\1\13\1\14\1\23\1\15\36￿\1" + "\16\1\17\35￿\1\20", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);

    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);

    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);

    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);

    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);

    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);

    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }

        public String getDescription() {
            return "210:1: SPECIALCHARACTER : ( ' ' | '!' | '\\'' | '\"' | '%' | '&' | '/' | ':' | ';' | '<' | '=' | '>' | '@' | '_' | '`' | '~' | '#' | {...}? => ',' | {...}? => ( '?' | '*' | '+' ) );";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
            int _s = s;
            switch(s) {
                case 0:
                    int LA3_0 = input.LA(1);
                    int index3_0 = input.index();
                    input.rewind();
                    s = -1;
                    if ((LA3_0 == ' ')) {
                        s = 1;
                    } else if ((LA3_0 == '!')) {
                        s = 2;
                    } else if ((LA3_0 == '\'')) {
                        s = 3;
                    } else if ((LA3_0 == '\"')) {
                        s = 4;
                    } else if ((LA3_0 == '%')) {
                        s = 5;
                    } else if ((LA3_0 == '&')) {
                        s = 6;
                    } else if ((LA3_0 == '/')) {
                        s = 7;
                    } else if ((LA3_0 == ':')) {
                        s = 8;
                    } else if ((LA3_0 == ';')) {
                        s = 9;
                    } else if ((LA3_0 == '<')) {
                        s = 10;
                    } else if ((LA3_0 == '=')) {
                        s = 11;
                    } else if ((LA3_0 == '>')) {
                        s = 12;
                    } else if ((LA3_0 == '@')) {
                        s = 13;
                    } else if ((LA3_0 == '_')) {
                        s = 14;
                    } else if ((LA3_0 == '`')) {
                        s = 15;
                    } else if ((LA3_0 == '~')) {
                        s = 16;
                    } else if ((LA3_0 == '#')) {
                        s = 17;
                    } else if ((LA3_0 == ',') && ((!numQuantifierMode))) {
                        s = 18;
                    } else if (((LA3_0 >= '*' && LA3_0 <= '+') || LA3_0 == '?') && ((classMode))) {
                        s = 19;
                    }
                    input.seek(index3_0);
                    if (s >= 0) return s;
                    break;
            }
            NoViableAltException nvae = new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }

    static final String DFA4_eotS = "\1\2\23￿";

    static final String DFA4_eofS = "\24￿";

    static final String DFA4_minS = "\1\134\1\44\22￿";

    static final String DFA4_maxS = "\1\134\1\175\22￿";

    static final String DFA4_acceptS = "\2￿\1\5\1\1\1\2\1\3\1\4\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1" + "\15\1\16\1\17\1\20\1\21\1\22";

    static final String DFA4_specialS = "\24￿}>";

    static final String[] DFA4_transitionS = { "\1\1", "\1\3\1￿\1\4\1￿\1\5\1\6\1\21\1\22\1\7\1\10\1\11\20" + "￿\1\23\33￿\1\12\1\17\1\13\1\14\34￿\1\15\1\20" + "\1\16", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);

    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);

    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);

    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);

    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);

    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);

    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }

        public String getDescription() {
            return "218:1: ESCAPEDCHARACTER : ( '\\\\$' | '\\\\&' | '\\\\(' | '\\\\)' | | '\\\\,' | '\\\\-' | '\\\\.' | '\\\\[' | '\\\\]' | '\\\\^' | '\\\\{' | '\\\\}' | '\\\\\\\\' | '\\\\|' | '\\\\*' | '\\\\+' | '\\\\?' );";
        }
    }

    static final String DFA8_eotS = "\1\16\6￿\1\23\3￿\1\30\1￿\1\32\17￿";

    static final String DFA8_eofS = "\35￿";

    static final String DFA8_minS = "\1\40\6￿\1\0\1￿\1\44\1￿\1\60\1￿\1\0\5￿" + "\1\0\4￿\1\0\1￿\1\0\2￿";

    static final String DFA8_maxS = "\1\176\6￿\1\0\1￿\1\175\1￿\1\71\1￿\1\0\5￿" + "\1\0\4￿\1\0\1￿\1\0\2￿";

    static final String DFA8_acceptS = "\1￿\1\1\1\2\1\3\1\4\1\5\1\6\1￿\1\10\1￿\1\11\1￿" + "\1\12\1￿\1\13\1\22\1\23\1\24\1\25\1￿\1\14\1\15\1\16\1" + "\17\1￿\1\21\1￿\1\7\1\20";

    static final String DFA8_specialS = "\1\2\12￿\1\5\1￿\1\4\5￿\1\3\4￿\1\1\1￿\1" + "\0\2￿}>";

    static final String[] DFA8_transitionS = { "\4\14\1\2\3\14\1\5\1\6\2\15\1\7\1\4\1\10\1\14\12\13\5\14\1\15" + "\1\14\32\12\1\21\1\11\1\22\1\1\2\14\32\12\1\17\1\3\1\20\1\14", "", "", "", "", "", "", "\1￿", "", "\1\16\1￿\1\16\1￿\7\16\1￿\1\25\16￿\1\16" + "\4￿\1\10\16￿\1\10\3￿\1\10\3￿\4\16\2￿" + "\1\24\1￿\1\27\1\10\2\24\7￿\1\24\3￿\1\24\1\10" + "\1\24\1\26\1￿\1\10\1\26\2￿\3\16", "", "\12\31", "", "\1￿", "", "", "", "", "", "\1￿", "", "", "", "", "\1￿", "", "\1￿", "", "" };

    static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);

    static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);

    static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);

    static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);

    static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);

    static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);

    static final short[][] DFA8_transition;

    static {
        int numStates = DFA8_transitionS.length;
        DFA8_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
        }
    }

    class DFA8 extends DFA {

        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA8_eot;
            this.eof = DFA8_eof;
            this.min = DFA8_min;
            this.max = DFA8_max;
            this.accept = DFA8_accept;
            this.special = DFA8_special;
            this.transition = DFA8_transition;
        }

        public String getDescription() {
            return "1:1: Tokens : ( T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | PREDEFINEDCLASS | ALPHANUM | SPECIALCHARACTER | ESCAPEDCHARACTER | NONTYPEABLECHARACTER | OCTALCHAR | HEXCHAR | CODEDCHAR | SIMPLEQUANTIFIER | INT | LBRACE | RBRACE | LBRACKET | RBRACKET );";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
            int _s = s;
            switch(s) {
                case 0:
                    int LA8_26 = input.LA(1);
                    int index8_26 = input.index();
                    input.rewind();
                    s = -1;
                    if (((classMode))) {
                        s = 12;
                    } else if (((!classMode))) {
                        s = 28;
                    }
                    input.seek(index8_26);
                    if (s >= 0) return s;
                    break;
                case 1:
                    int LA8_24 = input.LA(1);
                    int index8_24 = input.index();
                    input.rewind();
                    s = -1;
                    if (((!numQuantifierMode))) {
                        s = 10;
                    } else if (((numQuantifierMode))) {
                        s = 25;
                    }
                    input.seek(index8_24);
                    if (s >= 0) return s;
                    break;
                case 2:
                    int LA8_0 = input.LA(1);
                    int index8_0 = input.index();
                    input.rewind();
                    s = -1;
                    if ((LA8_0 == '^')) {
                        s = 1;
                    } else if ((LA8_0 == '$')) {
                        s = 2;
                    } else if ((LA8_0 == '|')) {
                        s = 3;
                    } else if ((LA8_0 == '-')) {
                        s = 4;
                    } else if ((LA8_0 == '(')) {
                        s = 5;
                    } else if ((LA8_0 == ')')) {
                        s = 6;
                    } else if ((LA8_0 == ',')) {
                        s = 7;
                    } else if ((LA8_0 == '.')) {
                        s = 8;
                    } else if ((LA8_0 == '\\')) {
                        s = 9;
                    } else if (((LA8_0 >= 'A' && LA8_0 <= 'Z') || (LA8_0 >= 'a' && LA8_0 <= 'z'))) {
                        s = 10;
                    } else if (((LA8_0 >= '0' && LA8_0 <= '9')) && (((!numQuantifierMode) || (numQuantifierMode)))) {
                        s = 11;
                    } else if (((LA8_0 >= ' ' && LA8_0 <= '#') || (LA8_0 >= '%' && LA8_0 <= '\'') || LA8_0 == '/' || (LA8_0 >= ':' && LA8_0 <= '>') || LA8_0 == '@' || (LA8_0 >= '_' && LA8_0 <= '`') || LA8_0 == '~')) {
                        s = 12;
                    } else if (((LA8_0 >= '*' && LA8_0 <= '+') || LA8_0 == '?') && (((classMode) || (!classMode)))) {
                        s = 13;
                    } else if ((LA8_0 == '{')) {
                        s = 15;
                    } else if ((LA8_0 == '}')) {
                        s = 16;
                    } else if ((LA8_0 == '[')) {
                        s = 17;
                    } else if ((LA8_0 == ']')) {
                        s = 18;
                    } else s = 14;
                    input.seek(index8_0);
                    if (s >= 0) return s;
                    break;
                case 3:
                    int LA8_19 = input.LA(1);
                    int index8_19 = input.index();
                    input.rewind();
                    s = -1;
                    if ((!(((!numQuantifierMode))))) {
                        s = 27;
                    } else if (((!numQuantifierMode))) {
                        s = 12;
                    }
                    input.seek(index8_19);
                    if (s >= 0) return s;
                    break;
                case 4:
                    int LA8_13 = input.LA(1);
                    int index8_13 = input.index();
                    input.rewind();
                    s = -1;
                    s = 26;
                    input.seek(index8_13);
                    if (s >= 0) return s;
                    break;
                case 5:
                    int LA8_11 = input.LA(1);
                    int index8_11 = input.index();
                    input.rewind();
                    s = -1;
                    if (((LA8_11 >= '0' && LA8_11 <= '9')) && ((numQuantifierMode))) {
                        s = 25;
                    } else s = 24;
                    input.seek(index8_11);
                    if (s >= 0) return s;
                    break;
            }
            NoViableAltException nvae = new NoViableAltException(getDescription(), 8, _s, input);
            error(nvae);
            throw nvae;
        }
    }
}
