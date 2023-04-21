package gatchan.phpparser.methodlist;

import java.io.*;
import java.util.*;

/** Token Manager. */
public class FunctionListParserTokenManager implements FunctionListParserConstants {

    /** Debug output. */
    public java.io.PrintStream debugStream = System.out;

    /** Set debug output. */
    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x800L) != 0L) return 30;
                if ((active0 & 0xfff00000L) != 0L) {
                    jjmatchedKind = 34;
                    return 6;
                }
                return -1;
            case 1:
                if ((active0 & 0xfff00000L) != 0L) {
                    jjmatchedKind = 34;
                    jjmatchedPos = 1;
                    return 6;
                }
                return -1;
            case 2:
                if ((active0 & 0x4800000L) != 0L) return 6;
                if ((active0 & 0xfb700000L) != 0L) {
                    if (jjmatchedPos != 2) {
                        jjmatchedKind = 34;
                        jjmatchedPos = 2;
                    }
                    return 6;
                }
                return -1;
            case 3:
                if ((active0 & 0x10500000L) != 0L) return 6;
                if ((active0 & 0xeba00000L) != 0L) {
                    jjmatchedKind = 34;
                    jjmatchedPos = 3;
                    return 6;
                }
                return -1;
            case 4:
                if ((active0 & 0x88000000L) != 0L) return 6;
                if ((active0 & 0x63a00000L) != 0L) {
                    jjmatchedKind = 34;
                    jjmatchedPos = 4;
                    return 6;
                }
                return -1;
            case 5:
                if ((active0 & 0x43200000L) != 0L) return 6;
                if ((active0 & 0x20800000L) != 0L) {
                    jjmatchedKind = 34;
                    jjmatchedPos = 5;
                    return 6;
                }
                return -1;
            case 6:
                if ((active0 & 0x800000L) != 0L) return 6;
                if ((active0 & 0x20000000L) != 0L) {
                    jjmatchedKind = 34;
                    jjmatchedPos = 6;
                    return 6;
                }
                return -1;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch(curChar) {
            case 36:
                return jjMoveStringLiteralDfa1_0(0x800L);
            case 38:
                return jjStopAtPos(0, 13);
            case 40:
                return jjStopAtPos(0, 16);
            case 41:
                return jjStopAtPos(0, 17);
            case 42:
                return jjStopAtPos(0, 9);
            case 43:
                return jjStopAtPos(0, 7);
            case 44:
                return jjStopAtPos(0, 15);
            case 45:
                return jjStopAtPos(0, 8);
            case 46:
                return jjMoveStringLiteralDfa1_0(0x1000L);
            case 47:
                return jjStopAtPos(0, 6);
            case 61:
                return jjStopAtPos(0, 10);
            case 91:
                return jjStopAtPos(0, 18);
            case 93:
                return jjStopAtPos(0, 19);
            case 97:
                return jjMoveStringLiteralDfa1_0(0x8000000L);
            case 98:
                return jjMoveStringLiteralDfa1_0(0x400000L);
            case 100:
                return jjMoveStringLiteralDfa1_0(0x2000000L);
            case 105:
                return jjMoveStringLiteralDfa1_0(0x4800000L);
            case 108:
                return jjMoveStringLiteralDfa1_0(0x10000000L);
            case 109:
                return jjMoveStringLiteralDfa1_0(0x80000000L);
            case 110:
                return jjMoveStringLiteralDfa1_0(0x1000000L);
            case 111:
                return jjMoveStringLiteralDfa1_0(0x40000000L);
            case 114:
                return jjMoveStringLiteralDfa1_0(0x20000000L);
            case 115:
                return jjMoveStringLiteralDfa1_0(0x200000L);
            case 118:
                return jjMoveStringLiteralDfa1_0(0x100000L);
            case 124:
                return jjStopAtPos(0, 14);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }

    private int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch(curChar) {
            case 46:
                return jjMoveStringLiteralDfa2_0(active0, 0x1800L);
            case 98:
                return jjMoveStringLiteralDfa2_0(active0, 0x40000000L);
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x20000000L);
            case 105:
                return jjMoveStringLiteralDfa2_0(active0, 0x80000000L);
            case 110:
                return jjMoveStringLiteralDfa2_0(active0, 0x4800000L);
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x12500000L);
            case 114:
                return jjMoveStringLiteralDfa2_0(active0, 0x8000000L);
            case 116:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000L);
            case 117:
                return jjMoveStringLiteralDfa2_0(active0, 0x1000000L);
            default:
                break;
        }
        return jjStartNfa_0(0, active0);
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(0, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch(curChar) {
            case 46:
                if ((active0 & 0x1000L) != 0L) return jjStopAtPos(2, 12);
                return jjMoveStringLiteralDfa3_0(active0, 0x800L);
            case 105:
                return jjMoveStringLiteralDfa3_0(active0, 0x100000L);
            case 106:
                return jjMoveStringLiteralDfa3_0(active0, 0x40000000L);
            case 109:
                return jjMoveStringLiteralDfa3_0(active0, 0x1000000L);
            case 110:
                return jjMoveStringLiteralDfa3_0(active0, 0x10000000L);
            case 111:
                return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
            case 114:
                return jjMoveStringLiteralDfa3_0(active0, 0x8200000L);
            case 115:
                return jjMoveStringLiteralDfa3_0(active0, 0x20000000L);
            case 116:
                if ((active0 & 0x4000000L) != 0L) {
                    jjmatchedKind = 26;
                    jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0x800000L);
            case 117:
                return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
            case 120:
                return jjMoveStringLiteralDfa3_0(active0, 0x80000000L);
            default:
                break;
        }
        return jjStartNfa_0(1, active0);
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(1, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch(curChar) {
            case 46:
                if ((active0 & 0x800L) != 0L) return jjStopAtPos(3, 11);
                break;
            case 97:
                return jjMoveStringLiteralDfa4_0(active0, 0x8000000L);
            case 98:
                return jjMoveStringLiteralDfa4_0(active0, 0x3000000L);
            case 100:
                if ((active0 & 0x100000L) != 0L) return jjStartNfaWithStates_0(3, 20, 6);
                break;
            case 101:
                return jjMoveStringLiteralDfa4_0(active0, 0xc0800000L);
            case 103:
                if ((active0 & 0x10000000L) != 0L) return jjStartNfaWithStates_0(3, 28, 6);
                break;
            case 105:
                return jjMoveStringLiteralDfa4_0(active0, 0x200000L);
            case 108:
                if ((active0 & 0x400000L) != 0L) return jjStartNfaWithStates_0(3, 22, 6);
                break;
            case 111:
                return jjMoveStringLiteralDfa4_0(active0, 0x20000000L);
            default:
                break;
        }
        return jjStartNfa_0(2, active0);
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(2, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(3, active0);
            return 4;
        }
        switch(curChar) {
            case 99:
                return jjMoveStringLiteralDfa5_0(active0, 0x40000000L);
            case 100:
                if ((active0 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(4, 31, 6);
                break;
            case 101:
                return jjMoveStringLiteralDfa5_0(active0, 0x1000000L);
            case 103:
                return jjMoveStringLiteralDfa5_0(active0, 0x800000L);
            case 108:
                return jjMoveStringLiteralDfa5_0(active0, 0x2000000L);
            case 110:
                return jjMoveStringLiteralDfa5_0(active0, 0x200000L);
            case 117:
                return jjMoveStringLiteralDfa5_0(active0, 0x20000000L);
            case 121:
                if ((active0 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(4, 27, 6);
                break;
            default:
                break;
        }
        return jjStartNfa_0(3, active0);
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(3, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(4, active0);
            return 5;
        }
        switch(curChar) {
            case 101:
                if ((active0 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(5, 25, 6);
                return jjMoveStringLiteralDfa6_0(active0, 0x800000L);
            case 103:
                if ((active0 & 0x200000L) != 0L) return jjStartNfaWithStates_0(5, 21, 6);
                break;
            case 114:
                if ((active0 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(5, 24, 6);
                return jjMoveStringLiteralDfa6_0(active0, 0x20000000L);
            case 116:
                if ((active0 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(5, 30, 6);
                break;
            default:
                break;
        }
        return jjStartNfa_0(4, active0);
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(4, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(5, active0);
            return 6;
        }
        switch(curChar) {
            case 99:
                return jjMoveStringLiteralDfa7_0(active0, 0x20000000L);
            case 114:
                if ((active0 & 0x800000L) != 0L) return jjStartNfaWithStates_0(6, 23, 6);
                break;
            default:
                break;
        }
        return jjStartNfa_0(5, active0);
    }

    private int jjMoveStringLiteralDfa7_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(5, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(6, active0);
            return 7;
        }
        switch(curChar) {
            case 101:
                if ((active0 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(7, 29, 6);
                break;
            default:
                break;
        }
        return jjStartNfa_0(6, active0);
    }

    private int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 30;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch(jjstateSet[--i]) {
                        case 30:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 35) kind = 35;
                                jjCheckNAddTwoStates(11, 12);
                            } else if (curChar == 42) jjCheckNAddTwoStates(10, 11);
                            break;
                        case 0:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 32) kind = 32;
                                jjCheckNAddStates(0, 2);
                            } else if (curChar == 39) jjCheckNAddStates(3, 5); else if (curChar == 34) jjCheckNAddStates(6, 8); else if (curChar == 36) jjCheckNAddTwoStates(10, 11); else if (curChar == 35) jjCheckNAddStates(9, 11);
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 34) kind = 34;
                                jjCheckNAddTwoStates(5, 6);
                            }
                            break;
                        case 6:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 34) kind = 34;
                                jjCheckNAddTwoStates(5, 6);
                            } else if (curChar == 45) jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        case 1:
                            if ((0xffffffffffffdbffL & l) != 0L) jjCheckNAddStates(9, 11);
                            break;
                        case 2:
                            if ((0x2400L & l) != 0L && kind > 5) kind = 5;
                            break;
                        case 3:
                            if (curChar == 10 && kind > 5) kind = 5;
                            break;
                        case 4:
                            if (curChar == 13) jjstateSet[jjnewStateCnt++] = 3;
                            break;
                        case 5:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 34) kind = 34;
                            jjCheckNAddTwoStates(5, 6);
                            break;
                        case 8:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 34) kind = 34;
                            jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 9:
                            if (curChar == 36) jjCheckNAddTwoStates(10, 11);
                            break;
                        case 10:
                            if (curChar == 42) jjCheckNAddTwoStates(10, 11);
                            break;
                        case 11:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 35) kind = 35;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 12:
                            if (curChar == 45) jjstateSet[jjnewStateCnt++] = 13;
                            break;
                        case 14:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 35) kind = 35;
                            jjstateSet[jjnewStateCnt++] = 14;
                            break;
                        case 15:
                            if (curChar == 34) jjCheckNAddStates(6, 8);
                            break;
                        case 16:
                            if ((0xfffffffbffffdbffL & l) != 0L) jjCheckNAddStates(6, 8);
                            break;
                        case 18:
                            jjCheckNAddStates(6, 8);
                            break;
                        case 19:
                            if (curChar == 34 && kind > 38) kind = 38;
                            break;
                        case 20:
                            if (curChar == 39) jjCheckNAddStates(3, 5);
                            break;
                        case 21:
                            if ((0xffffff7fffffdbffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 23:
                            jjCheckNAddStates(3, 5);
                            break;
                        case 24:
                            if (curChar == 39 && kind > 39) kind = 39;
                            break;
                        case 25:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 32) kind = 32;
                            jjCheckNAddStates(0, 2);
                            break;
                        case 26:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 32) kind = 32;
                            jjCheckNAdd(26);
                            break;
                        case 27:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(27, 28);
                            break;
                        case 28:
                            if (curChar == 46) jjCheckNAdd(29);
                            break;
                        case 29:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 33) kind = 33;
                            jjCheckNAdd(29);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 30:
                        case 11:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 35) kind = 35;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 0:
                        case 5:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 34) kind = 34;
                            jjCheckNAddTwoStates(5, 6);
                            break;
                        case 6:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 34) kind = 34;
                            jjCheckNAddTwoStates(5, 6);
                            break;
                        case 1:
                            jjAddStates(9, 11);
                            break;
                        case 7:
                        case 8:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 34) kind = 34;
                            jjCheckNAdd(8);
                            break;
                        case 13:
                        case 14:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 35) kind = 35;
                            jjCheckNAdd(14);
                            break;
                        case 16:
                            if ((0xffffffffefffffffL & l) != 0L) jjCheckNAddStates(6, 8);
                            break;
                        case 17:
                            if (curChar == 92) jjstateSet[jjnewStateCnt++] = 18;
                            break;
                        case 18:
                            jjCheckNAddStates(6, 8);
                            break;
                        case 21:
                            if ((0xffffffffefffffffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 22:
                            if (curChar == 92) jjstateSet[jjnewStateCnt++] = 23;
                            break;
                        case 23:
                            jjCheckNAddStates(3, 5);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 1:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(9, 11);
                            break;
                        case 16:
                        case 18:
                            if ((jjbitVec0[i2] & l2) != 0L) jjCheckNAddStates(6, 8);
                            break;
                        case 21:
                        case 23:
                            if ((jjbitVec0[i2] & l2) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 30 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 26, 27, 28, 21, 22, 24, 16, 17, 19, 1, 2, 4 };

    /** Token literal values. */
    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, "\57", "\53", "\55", "\52", "\75", "\44\56\56\56", "\56\56\56", "\46", "\174", "\54", "\50", "\51", "\133", "\135", "\166\157\151\144", "\163\164\162\151\156\147", "\142\157\157\154", "\151\156\164\145\147\145\162", "\156\165\155\142\145\162", "\144\157\165\142\154\145", "\151\156\164", "\141\162\162\141\171", "\154\157\156\147", "\162\145\163\157\165\162\143\145", "\157\142\152\145\143\164", "\155\151\170\145\144", null, null, null, null, null, null, null, null };

    /** Lexer state names. */
    public static final String[] lexStateNames = { "DEFAULT" };

    static final long[] jjtoToken = { 0xcfffffffc1L };

    static final long[] jjtoSkip = { 0x3eL };

    protected SimpleCharStream input_stream;

    private final int[] jjrounds = new int[30];

    private final int[] jjstateSet = new int[60];

    protected char curChar;

    /** Constructor. */
    public FunctionListParserTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    /** Constructor. */
    public FunctionListParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    /** Reinitialise parser. */
    public void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 30; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    /** Reinitialise parser. */
    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    /** Switch to specified lex state. */
    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
    }

    protected Token jjFillToken() {
        final Token t;
        final String curTokenImage;
        final int beginLine;
        final int endLine;
        final int beginColumn;
        final int endColumn;
        String im = jjstrLiteralImages[jjmatchedKind];
        curTokenImage = (im == null) ? input_stream.GetImage() : im;
        beginLine = input_stream.getBeginLine();
        beginColumn = input_stream.getBeginColumn();
        endLine = input_stream.getEndLine();
        endColumn = input_stream.getEndColumn();
        t = Token.newToken(jjmatchedKind, curTokenImage);
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }

    int curLexState = 0;

    int defaultLexState = 0;

    int jjnewStateCnt;

    int jjround;

    int jjmatchedPos;

    int jjmatchedKind;

    /** Get the next Token. */
    public Token getNextToken() {
        Token matchedToken;
        int curPos = 0;
        EOFLoop: for (; ; ) {
            try {
                curChar = input_stream.BeginToken();
            } catch (java.io.IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                return matchedToken;
            }
            try {
                input_stream.backup(0);
                while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
            } catch (java.io.IOException e1) {
                continue EOFLoop;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos) input_stream.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    return matchedToken;
                } else {
                    continue EOFLoop;
                }
            }
            int error_line = input_stream.getEndLine();
            int error_column = input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;
            try {
                input_stream.readChar();
                input_stream.backup(1);
            } catch (java.io.IOException e1) {
                EOFSeen = true;
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
                if (curChar == '\n' || curChar == '\r') {
                    error_line++;
                    error_column = 0;
                } else error_column++;
            }
            if (!EOFSeen) {
                input_stream.backup(1);
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
            }
            throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
        }
    }

    private void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }
}
