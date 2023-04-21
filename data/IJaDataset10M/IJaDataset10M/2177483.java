package org.eclipse.xtext.example.parser.antlr.internal;

import org.eclipse.xtext.parser.antlr.Lexer;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalFJLexer extends Lexer {

    public static final int RULE_ID = 4;

    public static final int T__27 = 27;

    public static final int T__26 = 26;

    public static final int T__25 = 25;

    public static final int T__24 = 24;

    public static final int T__23 = 23;

    public static final int T__22 = 22;

    public static final int RULE_ANY_OTHER = 10;

    public static final int T__21 = 21;

    public static final int T__20 = 20;

    public static final int EOF = -1;

    public static final int RULE_SL_COMMENT = 8;

    public static final int RULE_ML_COMMENT = 7;

    public static final int T__19 = 19;

    public static final int RULE_STRING = 5;

    public static final int T__16 = 16;

    public static final int T__15 = 15;

    public static final int T__18 = 18;

    public static final int T__17 = 17;

    public static final int T__12 = 12;

    public static final int T__11 = 11;

    public static final int T__14 = 14;

    public static final int T__13 = 13;

    public static final int RULE_INT = 6;

    public static final int RULE_WS = 9;

    public InternalFJLexer() {
        ;
    }

    public InternalFJLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public InternalFJLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "../org.eclipse.xtext.example.fj/src-gen/org/eclipse/xtext/example/parser/antlr/internal/InternalFJ.g";
    }

    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("int");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("boolean");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("String");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("class");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("extends");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('{');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('}');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(';');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('(');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(',');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(')');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("return");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('.');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("this");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("new");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("true");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("false");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int alt1 = 2;
                int LA1_0 = input.LA(1);
                if ((LA1_0 == '^')) {
                    alt1 = 1;
                }
                switch(alt1) {
                    case 1:
                        {
                            match('^');
                        }
                        break;
                }
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= '0' && LA2_0 <= '9') || (LA2_0 >= 'A' && LA2_0 <= 'Z') || LA2_0 == '_' || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
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

    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt3 = 0;
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if (((LA3_0 >= '0' && LA3_0 <= '9'))) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                matchRange('0', '9');
                            }
                            break;
                        default:
                            if (cnt3 >= 1) break loop3;
                            EarlyExitException eee = new EarlyExitException(3, input);
                            throw eee;
                    }
                    cnt3++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int alt6 = 2;
                int LA6_0 = input.LA(1);
                if ((LA6_0 == '\"')) {
                    alt6 = 1;
                } else if ((LA6_0 == '\'')) {
                    alt6 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 6, 0, input);
                    throw nvae;
                }
                switch(alt6) {
                    case 1:
                        {
                            match('\"');
                            loop4: do {
                                int alt4 = 3;
                                int LA4_0 = input.LA(1);
                                if ((LA4_0 == '\\')) {
                                    alt4 = 1;
                                } else if (((LA4_0 >= ' ' && LA4_0 <= '!') || (LA4_0 >= '#' && LA4_0 <= '[') || (LA4_0 >= ']' && LA4_0 <= '￿'))) {
                                    alt4 = 2;
                                }
                                switch(alt4) {
                                    case 1:
                                        {
                                            match('\\');
                                            if (input.LA(1) == '\"' || input.LA(1) == '\'' || input.LA(1) == '\\' || input.LA(1) == 'b' || input.LA(1) == 'f' || input.LA(1) == 'n' || input.LA(1) == 'r' || (input.LA(1) >= 't' && input.LA(1) <= 'u')) {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            if ((input.LA(1) >= ' ' && input.LA(1) <= '!') || (input.LA(1) >= '#' && input.LA(1) <= '[') || (input.LA(1) >= ']' && input.LA(1) <= '￿')) {
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
                            match('\"');
                        }
                        break;
                    case 2:
                        {
                            match('\'');
                            loop5: do {
                                int alt5 = 3;
                                int LA5_0 = input.LA(1);
                                if ((LA5_0 == '\\')) {
                                    alt5 = 1;
                                } else if (((LA5_0 >= ' ' && LA5_0 <= '&') || (LA5_0 >= '(' && LA5_0 <= '[') || (LA5_0 >= ']' && LA5_0 <= '￿'))) {
                                    alt5 = 2;
                                }
                                switch(alt5) {
                                    case 1:
                                        {
                                            match('\\');
                                            if (input.LA(1) == '\"' || input.LA(1) == '\'' || input.LA(1) == '\\' || input.LA(1) == 'b' || input.LA(1) == 'f' || input.LA(1) == 'n' || input.LA(1) == 'r' || (input.LA(1) >= 't' && input.LA(1) <= 'u')) {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    case 2:
                                        {
                                            if ((input.LA(1) >= ' ' && input.LA(1) <= '&') || (input.LA(1) >= '(' && input.LA(1) <= '[') || (input.LA(1) >= ']' && input.LA(1) <= '￿')) {
                                                input.consume();
                                            } else {
                                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                                recover(mse);
                                                throw mse;
                                            }
                                        }
                                        break;
                                    default:
                                        break loop5;
                                }
                            } while (true);
                            match('\'');
                        }
                        break;
                }
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("/*");
                loop7: do {
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if ((LA7_0 == '*')) {
                        int LA7_1 = input.LA(2);
                        if ((LA7_1 == '/')) {
                            alt7 = 2;
                        } else if (((LA7_1 >= ' ' && LA7_1 <= '.') || (LA7_1 >= '0' && LA7_1 <= '￿'))) {
                            alt7 = 1;
                        }
                    } else if (((LA7_0 >= ' ' && LA7_0 <= ')') || (LA7_0 >= '+' && LA7_0 <= '￿'))) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                matchAny();
                            }
                            break;
                        default:
                            break loop7;
                    }
                } while (true);
                match("*/");
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("//");
                loop8: do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if (((LA8_0 >= ' ' && LA8_0 <= '\t') || (LA8_0 >= '' && LA8_0 <= '\f') || (LA8_0 >= '' && LA8_0 <= '￿'))) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '\t') || (input.LA(1) >= '' && input.LA(1) <= '\f') || (input.LA(1) >= '' && input.LA(1) <= '￿')) {
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
                int alt10 = 2;
                int LA10_0 = input.LA(1);
                if ((LA10_0 == '\n' || LA10_0 == '\r')) {
                    alt10 = 1;
                }
                switch(alt10) {
                    case 1:
                        {
                            int alt9 = 2;
                            int LA9_0 = input.LA(1);
                            if ((LA9_0 == '\r')) {
                                alt9 = 1;
                            }
                            switch(alt9) {
                                case 1:
                                    {
                                        match('\r');
                                    }
                                    break;
                            }
                            match('\n');
                        }
                        break;
                }
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int cnt11 = 0;
                loop11: do {
                    int alt11 = 2;
                    int LA11_0 = input.LA(1);
                    if (((LA11_0 >= '\t' && LA11_0 <= '\n') || LA11_0 == '\r' || LA11_0 == ' ')) {
                        alt11 = 1;
                    }
                    switch(alt11) {
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
                            if (cnt11 >= 1) break loop11;
                            EarlyExitException eee = new EarlyExitException(11, input);
                            throw eee;
                    }
                    cnt11++;
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                matchAny();
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt12 = 24;
        alt12 = dfa12.predict(input);
        switch(alt12) {
            case 1:
                {
                    mT__11();
                }
                break;
            case 2:
                {
                    mT__12();
                }
                break;
            case 3:
                {
                    mT__13();
                }
                break;
            case 4:
                {
                    mT__14();
                }
                break;
            case 5:
                {
                    mT__15();
                }
                break;
            case 6:
                {
                    mT__16();
                }
                break;
            case 7:
                {
                    mT__17();
                }
                break;
            case 8:
                {
                    mT__18();
                }
                break;
            case 9:
                {
                    mT__19();
                }
                break;
            case 10:
                {
                    mT__20();
                }
                break;
            case 11:
                {
                    mT__21();
                }
                break;
            case 12:
                {
                    mT__22();
                }
                break;
            case 13:
                {
                    mT__23();
                }
                break;
            case 14:
                {
                    mT__24();
                }
                break;
            case 15:
                {
                    mT__25();
                }
                break;
            case 16:
                {
                    mT__26();
                }
                break;
            case 17:
                {
                    mT__27();
                }
                break;
            case 18:
                {
                    mRULE_ID();
                }
                break;
            case 19:
                {
                    mRULE_INT();
                }
                break;
            case 20:
                {
                    mRULE_STRING();
                }
                break;
            case 21:
                {
                    mRULE_ML_COMMENT();
                }
                break;
            case 22:
                {
                    mRULE_SL_COMMENT();
                }
                break;
            case 23:
                {
                    mRULE_WS();
                }
                break;
            case 24:
                {
                    mRULE_ANY_OTHER();
                }
                break;
        }
    }

    protected DFA12 dfa12 = new DFA12(this);

    static final String DFA12_eotS = "\1￿\5\32\6￿\1\32\1￿\3\32\1\30\2￿\3\30\2￿" + "\1\32\1￿\4\32\6￿\1\32\1￿\4\32\5￿\1\72\7\32\1" + "\102\1\32\1￿\5\32\1\111\1\112\1￿\3\32\1\116\2\32\2￿" + "\1\121\1\32\1\123\1￿\1\32\1\125\1￿\1\126\1￿\1\127" + "\3￿";

    static final String DFA12_eofS = "\130￿";

    static final String DFA12_minS = "\1\0\1\156\1\157\1\164\1\154\1\170\6￿\1\145\1￿\1\150\1" + "\145\1\141\1\101\2￿\2\0\1\52\2￿\1\164\1￿\1\157\1" + "\162\1\141\1\164\6￿\1\164\1￿\1\151\1\165\1\167\1\154\5" + "￿\1\60\1\154\1\151\1\163\1\145\1\165\1\163\1\145\1\60\1\163" + "\1￿\1\145\1\156\1\163\1\156\1\162\2\60\1￿\1\145\1\141" + "\1\147\1\60\1\144\1\156\2￿\1\60\1\156\1\60\1￿\1\163\1" + "\60\1￿\1\60\1￿\1\60\3￿";

    static final String DFA12_maxS = "\1￿\1\156\1\157\1\164\1\154\1\170\6￿\1\145\1￿\1\162" + "\1\145\1\141\1\172\2￿\2￿\1\57\2￿\1\164\1￿\1" + "\157\1\162\1\141\1\164\6￿\1\164\1￿\1\151\1\165\1\167\1" + "\154\5￿\1\172\1\154\1\151\1\163\1\145\1\165\1\163\1\145\1\172" + "\1\163\1￿\1\145\1\156\1\163\1\156\1\162\2\172\1￿\1\145" + "\1\141\1\147\1\172\1\144\1\156\2￿\1\172\1\156\1\172\1￿" + "\1\163\1\172\1￿\1\172\1￿\1\172\3￿";

    static final String DFA12_acceptS = "\6￿\1\6\1\7\1\10\1\11\1\12\1\13\1￿\1\15\4￿\1\22\1" + "\23\3￿\1\27\1\30\1￿\1\22\4￿\1\6\1\7\1\10\1\11\1\12" + "\1\13\1￿\1\15\4￿\1\23\1\24\1\25\1\26\1\27\12￿\1\1" + "\7￿\1\17\6￿\1\16\1\20\3￿\1\4\2￿\1\21\1￿" + "\1\3\1￿\1\14\1\2\1\5";

    static final String DFA12_specialS = "\1\1\23￿\1\0\1\2\102￿}>";

    static final String[] DFA12_transitionS = { "\11\30\2\27\2\30\1\27\22\30\1\27\1\30\1\24\4\30\1\25\1\11\1" + "\13\2\30\1\12\1\30\1\15\1\26\12\23\1\30\1\10\5\30\22\22\1\3" + "\7\22\3\30\1\21\1\22\1\30\1\22\1\2\1\4\1\22\1\5\1\20\2\22\1" + "\1\4\22\1\17\3\22\1\14\1\22\1\16\6\22\1\6\1\30\1\7ﾂ\30", "\1\31", "\1\33", "\1\34", "\1\35", "\1\36", "", "", "", "", "", "", "\1\45", "", "\1\47\11￿\1\50", "\1\51", "\1\52", "\32\32\4￿\1\32\1￿\32\32", "", "", "\0\54", "\0\54", "\1\55\4￿\1\56", "", "", "\1\60", "", "\1\61", "\1\62", "\1\63", "\1\64", "", "", "", "", "", "", "\1\65", "", "\1\66", "\1\67", "\1\70", "\1\71", "", "", "", "", "", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "\1\73", "\1\74", "\1\75", "\1\76", "\1\77", "\1\100", "\1\101", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "\1\103", "", "\1\104", "\1\105", "\1\106", "\1\107", "\1\110", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "", "\1\113", "\1\114", "\1\115", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "\1\117", "\1\120", "", "", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "\1\122", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "", "\1\124", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "", "\12\32\7￿\32\32\4￿\1\32\1￿\32\32", "", "", "" };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);

    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);

    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);

    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);

    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);

    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);

    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }

        public String getDescription() {
            return "1:1: Tokens : ( T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
            int _s = s;
            switch(s) {
                case 0:
                    int LA12_20 = input.LA(1);
                    s = -1;
                    if (((LA12_20 >= ' ' && LA12_20 <= '￿'))) {
                        s = 44;
                    } else s = 24;
                    if (s >= 0) return s;
                    break;
                case 1:
                    int LA12_0 = input.LA(1);
                    s = -1;
                    if ((LA12_0 == 'i')) {
                        s = 1;
                    } else if ((LA12_0 == 'b')) {
                        s = 2;
                    } else if ((LA12_0 == 'S')) {
                        s = 3;
                    } else if ((LA12_0 == 'c')) {
                        s = 4;
                    } else if ((LA12_0 == 'e')) {
                        s = 5;
                    } else if ((LA12_0 == '{')) {
                        s = 6;
                    } else if ((LA12_0 == '}')) {
                        s = 7;
                    } else if ((LA12_0 == ';')) {
                        s = 8;
                    } else if ((LA12_0 == '(')) {
                        s = 9;
                    } else if ((LA12_0 == ',')) {
                        s = 10;
                    } else if ((LA12_0 == ')')) {
                        s = 11;
                    } else if ((LA12_0 == 'r')) {
                        s = 12;
                    } else if ((LA12_0 == '.')) {
                        s = 13;
                    } else if ((LA12_0 == 't')) {
                        s = 14;
                    } else if ((LA12_0 == 'n')) {
                        s = 15;
                    } else if ((LA12_0 == 'f')) {
                        s = 16;
                    } else if ((LA12_0 == '^')) {
                        s = 17;
                    } else if (((LA12_0 >= 'A' && LA12_0 <= 'R') || (LA12_0 >= 'T' && LA12_0 <= 'Z') || LA12_0 == '_' || LA12_0 == 'a' || LA12_0 == 'd' || (LA12_0 >= 'g' && LA12_0 <= 'h') || (LA12_0 >= 'j' && LA12_0 <= 'm') || (LA12_0 >= 'o' && LA12_0 <= 'q') || LA12_0 == 's' || (LA12_0 >= 'u' && LA12_0 <= 'z'))) {
                        s = 18;
                    } else if (((LA12_0 >= '0' && LA12_0 <= '9'))) {
                        s = 19;
                    } else if ((LA12_0 == '\"')) {
                        s = 20;
                    } else if ((LA12_0 == '\'')) {
                        s = 21;
                    } else if ((LA12_0 == '/')) {
                        s = 22;
                    } else if (((LA12_0 >= '\t' && LA12_0 <= '\n') || LA12_0 == '\r' || LA12_0 == ' ')) {
                        s = 23;
                    } else if (((LA12_0 >= ' ' && LA12_0 <= '\b') || (LA12_0 >= '' && LA12_0 <= '\f') || (LA12_0 >= '' && LA12_0 <= '') || LA12_0 == '!' || (LA12_0 >= '#' && LA12_0 <= '&') || (LA12_0 >= '*' && LA12_0 <= '+') || LA12_0 == '-' || LA12_0 == ':' || (LA12_0 >= '<' && LA12_0 <= '@') || (LA12_0 >= '[' && LA12_0 <= ']') || LA12_0 == '`' || LA12_0 == '|' || (LA12_0 >= '~' && LA12_0 <= '￿'))) {
                        s = 24;
                    }
                    if (s >= 0) return s;
                    break;
                case 2:
                    int LA12_21 = input.LA(1);
                    s = -1;
                    if (((LA12_21 >= ' ' && LA12_21 <= '￿'))) {
                        s = 44;
                    } else s = 24;
                    if (s >= 0) return s;
                    break;
            }
            NoViableAltException nvae = new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
}
