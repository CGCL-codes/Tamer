package saf.syntax;

import saf.ast.nodes.*;
import saf.bot.GameBot;
import java.lang.String;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked" })
public class SAFParser extends Parser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "INT", "WS", "'('", "')'", "'='", "'['", "']'", "'and'", "'choose'", "'or'", "'{'", "'}'" };

    public static final int EOF = -1;

    public static final int T__7 = 7;

    public static final int T__8 = 8;

    public static final int T__9 = 9;

    public static final int T__10 = 10;

    public static final int T__11 = 11;

    public static final int T__12 = 12;

    public static final int T__13 = 13;

    public static final int T__14 = 14;

    public static final int T__15 = 15;

    public static final int T__16 = 16;

    public static final int ID = 4;

    public static final int INT = 5;

    public static final int WS = 6;

    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    public SAFParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public SAFParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() {
        return SAFParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "SAF.g";
    }

    public final GameBot fighter() throws RecognitionException {
        GameBot fighter = null;
        Token name = null;
        Strength strength1 = null;
        Rule rule2 = null;
        try {
            {
                name = (Token) match(input, ID, FOLLOW_ID_in_fighter60);
                fighter = new GameBot((name != null ? name.getText() : null));
                match(input, 15, FOLLOW_15_in_fighter88);
                loop1: do {
                    int alt1 = 3;
                    int LA1_0 = input.LA(1);
                    if ((LA1_0 == ID)) {
                        int LA1_2 = input.LA(2);
                        if ((LA1_2 == 9)) {
                            alt1 = 1;
                        } else if ((LA1_2 == 10 || LA1_2 == 12 || LA1_2 == 14)) {
                            alt1 = 2;
                        }
                    }
                    switch(alt1) {
                        case 1:
                            {
                                pushFollow(FOLLOW_strength_in_fighter107);
                                strength1 = strength();
                                state._fsp--;
                                fighter.addStrength(strength1);
                            }
                            break;
                        case 2:
                            {
                                pushFollow(FOLLOW_rule_in_fighter136);
                                rule2 = rule();
                                state._fsp--;
                                fighter.addRule(rule2);
                            }
                            break;
                        default:
                            break loop1;
                    }
                } while (true);
                match(input, 16, FOLLOW_16_in_fighter172);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return fighter;
    }

    public final Strength strength() throws RecognitionException {
        Strength s = null;
        Token ID3 = null;
        Token INT4 = null;
        try {
            {
                ID3 = (Token) match(input, ID, FOLLOW_ID_in_strength199);
                match(input, 9, FOLLOW_9_in_strength201);
                INT4 = (Token) match(input, INT, FOLLOW_INT_in_strength203);
                s = new Strength((ID3 != null ? ID3.getText() : null), (INT4 != null ? Integer.valueOf(INT4.getText()) : 0));
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return s;
    }

    public final Rule rule() throws RecognitionException {
        Rule r = null;
        Token m0 = null;
        Token m1 = null;
        Token m2 = null;
        Token a0 = null;
        Token a1 = null;
        Token a2 = null;
        Condition condition5 = null;
        try {
            {
                r = new Rule();
                pushFollow(FOLLOW_condition_in_rule285);
                condition5 = condition();
                state._fsp--;
                r.setCondition(condition5);
                match(input, 10, FOLLOW_10_in_rule318);
                int alt2 = 2;
                int LA2_0 = input.LA(1);
                if ((LA2_0 == ID)) {
                    alt2 = 1;
                } else if ((LA2_0 == 13)) {
                    alt2 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 2, 0, input);
                    throw nvae;
                }
                switch(alt2) {
                    case 1:
                        {
                            m0 = (Token) match(input, ID, FOLLOW_ID_in_rule332);
                            r.addMove(new Move((m0 != null ? m0.getText() : null)));
                        }
                        break;
                    case 2:
                        {
                            match(input, 13, FOLLOW_13_in_rule375);
                            match(input, 7, FOLLOW_7_in_rule377);
                            m1 = (Token) match(input, ID, FOLLOW_ID_in_rule381);
                            m2 = (Token) match(input, ID, FOLLOW_ID_in_rule385);
                            match(input, 8, FOLLOW_8_in_rule387);
                            r.addMove(new Move((m1 != null ? m1.getText() : null)));
                            r.addMove(new Move((m2 != null ? m2.getText() : null)));
                        }
                        break;
                }
                int alt3 = 2;
                int LA3_0 = input.LA(1);
                if ((LA3_0 == ID)) {
                    alt3 = 1;
                } else if ((LA3_0 == 13)) {
                    alt3 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 3, 0, input);
                    throw nvae;
                }
                switch(alt3) {
                    case 1:
                        {
                            a0 = (Token) match(input, ID, FOLLOW_ID_in_rule417);
                            r.addAttack(new Attack((a0 != null ? a0.getText() : null)));
                        }
                        break;
                    case 2:
                        {
                            match(input, 13, FOLLOW_13_in_rule460);
                            match(input, 7, FOLLOW_7_in_rule462);
                            a1 = (Token) match(input, ID, FOLLOW_ID_in_rule466);
                            a2 = (Token) match(input, ID, FOLLOW_ID_in_rule470);
                            match(input, 8, FOLLOW_8_in_rule472);
                            r.addAttack(new Attack((a1 != null ? a1.getText() : null)));
                            r.addAttack(new Attack((a2 != null ? a2.getText() : null)));
                        }
                        break;
                }
                match(input, 11, FOLLOW_11_in_rule495);
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return r;
    }

    public final Condition condition() throws RecognitionException {
        Condition c = null;
        Token ID6 = null;
        Condition c1 = null;
        Condition c2 = null;
        try {
            {
                ID6 = (Token) match(input, ID, FOLLOW_ID_in_condition517);
                int alt4 = 3;
                switch(input.LA(1)) {
                    case 12:
                        {
                            alt4 = 1;
                        }
                        break;
                    case 14:
                        {
                            alt4 = 2;
                        }
                        break;
                    case 10:
                        {
                            alt4 = 3;
                        }
                        break;
                    default:
                        NoViableAltException nvae = new NoViableAltException("", 4, 0, input);
                        throw nvae;
                }
                switch(alt4) {
                    case 1:
                        {
                            match(input, 12, FOLLOW_12_in_condition532);
                            pushFollow(FOLLOW_conditionTail_in_condition536);
                            c1 = conditionTail();
                            state._fsp--;
                            c = new ConditionOperator("and");
                            c.addCondition(new ConditionName((ID6 != null ? ID6.getText() : null)));
                            c.addCondition(c1);
                        }
                        break;
                    case 2:
                        {
                            match(input, 14, FOLLOW_14_in_condition547);
                            pushFollow(FOLLOW_conditionTail_in_condition551);
                            c2 = conditionTail();
                            state._fsp--;
                            c = new ConditionOperator("or");
                            c.addCondition(new ConditionName((ID6 != null ? ID6.getText() : null)));
                            c.addCondition(c2);
                        }
                        break;
                    case 3:
                        {
                            c = new ConditionName((ID6 != null ? ID6.getText() : null));
                        }
                        break;
                }
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return c;
    }

    public final Condition conditionTail() throws RecognitionException {
        Condition c = null;
        Condition condition7 = null;
        try {
            {
                pushFollow(FOLLOW_condition_in_conditionTail595);
                condition7 = condition();
                state._fsp--;
                c = condition7;
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return c;
    }

    public final Move move() throws RecognitionException {
        Move m = null;
        Token ID8 = null;
        try {
            {
                ID8 = (Token) match(input, ID, FOLLOW_ID_in_move643);
                m = new Move((ID8 != null ? ID8.getText() : null));
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return m;
    }

    public final Attack attack() throws RecognitionException {
        Attack a = null;
        Token ID9 = null;
        try {
            {
                ID9 = (Token) match(input, ID, FOLLOW_ID_in_attack668);
                a = new Attack((ID9 != null ? ID9.getText() : null));
            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return a;
    }

    public static final BitSet FOLLOW_ID_in_fighter60 = new BitSet(new long[] { 0x0000000000008000L });

    public static final BitSet FOLLOW_15_in_fighter88 = new BitSet(new long[] { 0x0000000000010010L });

    public static final BitSet FOLLOW_strength_in_fighter107 = new BitSet(new long[] { 0x0000000000010010L });

    public static final BitSet FOLLOW_rule_in_fighter136 = new BitSet(new long[] { 0x0000000000010010L });

    public static final BitSet FOLLOW_16_in_fighter172 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_strength199 = new BitSet(new long[] { 0x0000000000000200L });

    public static final BitSet FOLLOW_9_in_strength201 = new BitSet(new long[] { 0x0000000000000020L });

    public static final BitSet FOLLOW_INT_in_strength203 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_condition_in_rule285 = new BitSet(new long[] { 0x0000000000000400L });

    public static final BitSet FOLLOW_10_in_rule318 = new BitSet(new long[] { 0x0000000000002010L });

    public static final BitSet FOLLOW_ID_in_rule332 = new BitSet(new long[] { 0x0000000000002010L });

    public static final BitSet FOLLOW_13_in_rule375 = new BitSet(new long[] { 0x0000000000000080L });

    public static final BitSet FOLLOW_7_in_rule377 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_ID_in_rule381 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_ID_in_rule385 = new BitSet(new long[] { 0x0000000000000100L });

    public static final BitSet FOLLOW_8_in_rule387 = new BitSet(new long[] { 0x0000000000002010L });

    public static final BitSet FOLLOW_ID_in_rule417 = new BitSet(new long[] { 0x0000000000000800L });

    public static final BitSet FOLLOW_13_in_rule460 = new BitSet(new long[] { 0x0000000000000080L });

    public static final BitSet FOLLOW_7_in_rule462 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_ID_in_rule466 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_ID_in_rule470 = new BitSet(new long[] { 0x0000000000000100L });

    public static final BitSet FOLLOW_8_in_rule472 = new BitSet(new long[] { 0x0000000000000800L });

    public static final BitSet FOLLOW_11_in_rule495 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_condition517 = new BitSet(new long[] { 0x0000000000005002L });

    public static final BitSet FOLLOW_12_in_condition532 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_conditionTail_in_condition536 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_14_in_condition547 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_conditionTail_in_condition551 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_condition_in_conditionTail595 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_move643 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ID_in_attack668 = new BitSet(new long[] { 0x0000000000000002L });
}
