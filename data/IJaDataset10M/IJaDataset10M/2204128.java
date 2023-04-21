package BA.Solver.Parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class PresburgerLexer extends Lexer {

    public static final int EOF = -1;

    public static final int T__30 = 30;

    public static final int T__31 = 31;

    public static final int T__32 = 32;

    public static final int T__33 = 33;

    public static final int T__34 = 34;

    public static final int T__35 = 35;

    public static final int T__36 = 36;

    public static final int T__37 = 37;

    public static final int T__38 = 38;

    public static final int T__39 = 39;

    public static final int T__40 = 40;

    public static final int T__41 = 41;

    public static final int T__42 = 42;

    public static final int T__43 = 43;

    public static final int T__44 = 44;

    public static final int T__45 = 45;

    public static final int T__46 = 46;

    public static final int T__47 = 47;

    public static final int T__48 = 48;

    public static final int T__49 = 49;

    public static final int PLUS = 4;

    public static final int MINUS = 5;

    public static final int NEG = 6;

    public static final int AND = 7;

    public static final int OR = 8;

    public static final int IMP = 9;

    public static final int BIIMP = 10;

    public static final int ALL = 11;

    public static final int EX = 12;

    public static final int EQ = 13;

    public static final int NEQ = 14;

    public static final int GEQ = 15;

    public static final int LEQ = 16;

    public static final int GT = 17;

    public static final int LT = 18;

    public static final int L = 19;

    public static final int R = 20;

    public static final int PRED = 21;

    public static final int FORAND = 22;

    public static final int FOROR = 23;

    public static final int CONSTNUM = 24;

    public static final int MACRO = 25;

    public static final int VAR = 26;

    public static final int QUANT = 27;

    public static final int INT = 28;

    public static final int WS = 29;

    public PresburgerLexer() {
        ;
    }

    public PresburgerLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public PresburgerLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "/Users/moritzfuchs/Documents/Studium/6. Semester/Bachelorarbeit/workspace/Presburger Solver/src/BA/Solver/Parser/Presburger.g";
    }

    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('!');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("&&");
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
                match('[');
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
                match('=');
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
                match('.');
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
                match(']');
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
                match("||");
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

    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(')');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("->");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("<->");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('(');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('+');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('-');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("==");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("!=");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(">=");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("<=");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('>');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('<');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mQUANT() throws RecognitionException {
        try {
            int _type = QUANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt3 = 2;
            int LA3_0 = input.LA(1);
            if ((LA3_0 == 'A')) {
                alt3 = 1;
            } else if ((LA3_0 == 'E')) {
                alt3 = 2;
            } else {
                NoViableAltException nvae = new NoViableAltException("", 3, 0, input);
                throw nvae;
            }
            switch(alt3) {
                case 1:
                    {
                        match('A');
                        int cnt1 = 0;
                        loop1: do {
                            int alt1 = 2;
                            int LA1_0 = input.LA(1);
                            if (((LA1_0 >= 'a' && LA1_0 <= 'z'))) {
                                alt1 = 1;
                            }
                            switch(alt1) {
                                case 1:
                                    {
                                        mVAR();
                                    }
                                    break;
                                default:
                                    if (cnt1 >= 1) break loop1;
                                    EarlyExitException eee = new EarlyExitException(1, input);
                                    throw eee;
                            }
                            cnt1++;
                        } while (true);
                        match(':');
                    }
                    break;
                case 2:
                    {
                        match('E');
                        int cnt2 = 0;
                        loop2: do {
                            int alt2 = 2;
                            int LA2_0 = input.LA(1);
                            if (((LA2_0 >= 'a' && LA2_0 <= 'z'))) {
                                alt2 = 1;
                            }
                            switch(alt2) {
                                case 1:
                                    {
                                        mVAR();
                                    }
                                    break;
                                default:
                                    if (cnt2 >= 1) break loop2;
                                    EarlyExitException eee = new EarlyExitException(2, input);
                                    throw eee;
                            }
                            cnt2++;
                        } while (true);
                        match(':');
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mMACRO() throws RecognitionException {
        try {
            int _type = MACRO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                {
                    matchRange('A', 'Z');
                }
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);
                    if (((LA4_0 >= 'a' && LA4_0 <= 'z'))) {
                        alt4 = 1;
                    }
                    switch(alt4) {
                        case 1:
                            {
                                matchRange('a', 'z');
                            }
                            break;
                        default:
                            break loop4;
                    }
                } while (true);
                match('(');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mVAR() throws RecognitionException {
        try {
            int _type = VAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                {
                    matchRange('a', 'z');
                }
                loop7: do {
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if ((LA7_0 == '[')) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                match('[');
                                int alt6 = 2;
                                int LA6_0 = input.LA(1);
                                if (((LA6_0 >= 'a' && LA6_0 <= 'z'))) {
                                    alt6 = 1;
                                } else if (((LA6_0 >= '0' && LA6_0 <= '9'))) {
                                    alt6 = 2;
                                } else {
                                    NoViableAltException nvae = new NoViableAltException("", 6, 0, input);
                                    throw nvae;
                                }
                                switch(alt6) {
                                    case 1:
                                        {
                                            {
                                                matchRange('a', 'z');
                                            }
                                            int alt5 = 2;
                                            int LA5_0 = input.LA(1);
                                            if ((LA5_0 == '+')) {
                                                alt5 = 1;
                                            }
                                            switch(alt5) {
                                                case 1:
                                                    {
                                                        match('+');
                                                        mINT();
                                                    }
                                                    break;
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            mINT();
                                        }
                                        break;
                                }
                                match(']');
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

    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt8 = 0;
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if (((LA8_0 >= '0' && LA8_0 <= '9'))) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                matchRange('0', '9');
                            }
                            break;
                        default:
                            if (cnt8 >= 1) break loop8;
                            EarlyExitException eee = new EarlyExitException(8, input);
                            throw eee;
                    }
                    cnt8++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt9 = 0;
                loop9: do {
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);
                    if (((LA9_0 >= '\t' && LA9_0 <= '\n') || LA9_0 == '\r' || LA9_0 == ' ')) {
                        alt9 = 1;
                    }
                    switch(alt9) {
                        case 1:
                            {
                                if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || input.LA(1) == '\r' || input.LA(1) == ' ') {
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
                skip();
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt10 = 25;
        alt10 = dfa10.predict(input);
        switch(alt10) {
            case 1:
                {
                    mT__30();
                }
                break;
            case 2:
                {
                    mT__31();
                }
                break;
            case 3:
                {
                    mT__32();
                }
                break;
            case 4:
                {
                    mT__33();
                }
                break;
            case 5:
                {
                    mT__34();
                }
                break;
            case 6:
                {
                    mT__35();
                }
                break;
            case 7:
                {
                    mT__36();
                }
                break;
            case 8:
                {
                    mT__37();
                }
                break;
            case 9:
                {
                    mT__38();
                }
                break;
            case 10:
                {
                    mT__39();
                }
                break;
            case 11:
                {
                    mT__40();
                }
                break;
            case 12:
                {
                    mT__41();
                }
                break;
            case 13:
                {
                    mT__42();
                }
                break;
            case 14:
                {
                    mT__43();
                }
                break;
            case 15:
                {
                    mT__44();
                }
                break;
            case 16:
                {
                    mT__45();
                }
                break;
            case 17:
                {
                    mT__46();
                }
                break;
            case 18:
                {
                    mT__47();
                }
                break;
            case 19:
                {
                    mT__48();
                }
                break;
            case 20:
                {
                    mT__49();
                }
                break;
            case 21:
                {
                    mQUANT();
                }
                break;
            case 22:
                {
                    mMACRO();
                }
                break;
            case 23:
                {
                    mVAR();
                }
                break;
            case 24:
                {
                    mINT();
                }
                break;
            case 25:
                {
                    mWS();
                }
                break;
        }
    }

    protected DFA10 dfa10 = new DFA10(this);

    static final String DFA10_eotS = "\1￿\1\26\2￿\1\30\5￿\1\32\1\35\2￿\1\37\24￿";

    static final String DFA10_eofS = "\43￿";

    static final String DFA10_minS = "\1\11\1\75\2￿\1\75\5￿\1\76\1\55\2￿\1\75\2\50\17￿" + "\2\50\1￿";

    static final String DFA10_maxS = "\1\174\1\75\2￿\1\75\5￿\1\76\1\75\2￿\1\75\2\172\17" + "￿\2\172\1￿";

    static final String DFA10_acceptS = "\2￿\1\2\1\3\1￿\1\5\1\6\1\7\1\10\1\11\2￿\1\14\1\15" + "\3￿\1\26\1\27\1\30\1\31\1\20\1\1\1\17\1\4\1\12\1\16\1\13\1" + "\22\1\24\1\21\1\23\2￿\1\25";

    static final String DFA10_specialS = "\43￿}>";

    static final String[] DFA10_transitionS = { "\2\24\2￿\1\24\22￿\1\24\1\1\4￿\1\2\1￿\1\14" + "\1\11\1￿\1\15\1\10\1\12\1\5\1￿\12\23\2￿\1\13" + "\1\4\1\16\2￿\1\17\3\21\1\20\25\21\1\3\1￿\1\6\3￿" + "\32\22\1￿\1\7", "\1\25", "", "", "\1\27", "", "", "", "", "", "\1\31", "\1\33\17￿\1\34", "", "", "\1\36", "\1\21\70￿\32\40", "\1\21\70￿\32\41", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "\1\21\21￿\1\42\40￿\1\42\5￿\32\40", "\1\21\21￿\1\42\40￿\1\42\5￿\32\41", "" };

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
            return "1:1: Tokens : ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | QUANT | MACRO | VAR | INT | WS );";
        }
    }
}
