package org.da.expressionj.expr.parser;

import java.io.StringReader;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import org.da.expressionj.expr.*;
import org.da.expressionj.model.*;
import org.da.expressionj.functions.ConstantsDefinitions;

/** Token Manager. */
public class EquationParserTokenManager implements EquationParserConstants {

    /** Debug output. */
    public static java.io.PrintStream debugStream = System.out;

    /** Set debug output. */
    public static void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private static final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x400000890000L) != 0L) return 12;
                if ((active0 & 0x2408000000L) != 0L) {
                    jjmatchedKind = 50;
                    return 57;
                }
                if ((active0 & 0x1200000L) != 0L) return 18;
                if ((active0 & 0x448000L) != 0L) return 8;
                if ((active0 & 0x800000000000L) != 0L) {
                    jjmatchedKind = 50;
                    return 30;
                }
                if ((active0 & 0x800000000L) != 0L) {
                    jjmatchedKind = 50;
                    return 38;
                }
                if ((active0 & 0x311d3f0000000L) != 0L) {
                    jjmatchedKind = 50;
                    return 4;
                }
                if ((active0 & 0x200000000000L) != 0L) {
                    jjmatchedKind = 50;
                    return 47;
                }
                return -1;
            case 1:
                if ((active0 & 0x800000000000L) != 0L) return 4;
                if ((active0 & 0x800000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 1;
                    return 37;
                }
                if ((active0 & 0x331f7f8000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 1;
                    return 4;
                }
                return -1;
            case 2:
                if ((active0 & 0x800000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 2;
                    return 36;
                }
                if ((active0 & 0x78000000L) != 0L) return 4;
                if ((active0 & 0x331f780000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 2;
                    return 4;
                }
                return -1;
            case 3:
                if ((active0 & 0x1007780000000L) != 0L) return 4;
                if ((active0 & 0x2318800000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 3;
                    return 4;
                }
                return -1;
            case 4:
                if ((active0 & 0x118000000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 4;
                    return 4;
                }
                if ((active0 & 0x2200800000000L) != 0L) return 4;
                return -1;
            case 5:
                if ((active0 & 0x18000000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 5;
                    return 4;
                }
                if ((active0 & 0x100000000000L) != 0L) return 4;
                return -1;
            case 6:
                if ((active0 & 0x18000000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 6;
                    return 4;
                }
                return -1;
            case 7:
                if ((active0 & 0x18000000000L) != 0L) {
                    jjmatchedKind = 50;
                    jjmatchedPos = 7;
                    return 4;
                }
                return -1;
            default:
                return -1;
        }
    }

    private static final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private static int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private static int jjMoveStringLiteralDfa0_0() {
        switch(curChar) {
            case 33:
                jjmatchedKind = 8;
                return jjMoveStringLiteralDfa1_0(0x4000L);
            case 37:
                return jjStopAtPos(0, 25);
            case 38:
                return jjMoveStringLiteralDfa1_0(0x20L);
            case 40:
                return jjStopAtPos(0, 57);
            case 41:
                return jjStopAtPos(0, 58);
            case 42:
                jjmatchedKind = 26;
                return jjMoveStringLiteralDfa1_0(0x100000L);
            case 43:
                jjmatchedKind = 22;
                return jjMoveStringLiteralDfa1_0(0x48000L);
            case 44:
                return jjStopAtPos(0, 56);
            case 45:
                jjmatchedKind = 23;
                return jjMoveStringLiteralDfa1_0(0x400000090000L);
            case 47:
                jjmatchedKind = 24;
                return jjMoveStringLiteralDfa1_0(0x200000L);
            case 59:
                return jjStopAtPos(0, 55);
            case 60:
                jjmatchedKind = 10;
                return jjMoveStringLiteralDfa1_0(0x1000L);
            case 61:
                jjmatchedKind = 17;
                return jjMoveStringLiteralDfa1_0(0x2000L);
            case 62:
                jjmatchedKind = 9;
                return jjMoveStringLiteralDfa1_0(0x800L);
            case 91:
                return jjStopAtPos(0, 59);
            case 93:
                return jjStopAtPos(0, 60);
            case 94:
                return jjStopAtPos(0, 7);
            case 97:
                return jjMoveStringLiteralDfa1_0(0x3c0000000L);
            case 98:
                return jjMoveStringLiteralDfa1_0(0x200000000000L);
            case 99:
                return jjMoveStringLiteralDfa1_0(0x1010000000L);
            case 101:
                return jjMoveStringLiteralDfa1_0(0x1004000000000L);
            case 102:
                return jjMoveStringLiteralDfa1_0(0x800000000L);
            case 105:
                return jjMoveStringLiteralDfa1_0(0x800000000000L);
            case 114:
                return jjMoveStringLiteralDfa1_0(0x100000000000L);
            case 115:
                return jjMoveStringLiteralDfa1_0(0x2408000000L);
            case 116:
                return jjMoveStringLiteralDfa1_0(0x18020000000L);
            case 119:
                return jjMoveStringLiteralDfa1_0(0x2000000000000L);
            case 123:
                return jjStopAtPos(0, 54);
            case 124:
                return jjMoveStringLiteralDfa1_0(0x40L);
            case 125:
                return jjStopAtPos(0, 61);
            default:
                return jjMoveNfa_0(2, 0);
        }
    }

    private static int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch(curChar) {
            case 38:
                if ((active0 & 0x20L) != 0L) return jjStopAtPos(1, 5);
                break;
            case 43:
                if ((active0 & 0x8000L) != 0L) return jjStopAtPos(1, 15);
                break;
            case 45:
                if ((active0 & 0x10000L) != 0L) return jjStopAtPos(1, 16);
                break;
            case 61:
                if ((active0 & 0x800L) != 0L) return jjStopAtPos(1, 11); else if ((active0 & 0x1000L) != 0L) return jjStopAtPos(1, 12); else if ((active0 & 0x2000L) != 0L) return jjStopAtPos(1, 13); else if ((active0 & 0x4000L) != 0L) return jjStopAtPos(1, 14); else if ((active0 & 0x40000L) != 0L) return jjStopAtPos(1, 18); else if ((active0 & 0x80000L) != 0L) return jjStopAtPos(1, 19); else if ((active0 & 0x100000L) != 0L) return jjStopAtPos(1, 20); else if ((active0 & 0x200000L) != 0L) return jjStopAtPos(1, 21);
                break;
            case 62:
                if ((active0 & 0x400000000000L) != 0L) return jjStopAtPos(1, 46);
                break;
            case 97:
                return jjMoveStringLiteralDfa2_0(active0, 0x20000000L);
            case 98:
                return jjMoveStringLiteralDfa2_0(active0, 0x40000000L);
            case 99:
                return jjMoveStringLiteralDfa2_0(active0, 0x4100000000L);
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x101000000000L);
            case 102:
                if ((active0 & 0x800000000000L) != 0L) return jjStartNfaWithStates_0(1, 47, 4);
                break;
            case 104:
                return jjMoveStringLiteralDfa2_0(active0, 0x2000000000000L);
            case 105:
                return jjMoveStringLiteralDfa2_0(active0, 0x2008000000L);
            case 108:
                return jjMoveStringLiteralDfa2_0(active0, 0x1000800000000L);
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x18010000000L);
            case 113:
                return jjMoveStringLiteralDfa2_0(active0, 0x400000000L);
            case 114:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000000000L);
            case 115:
                return jjMoveStringLiteralDfa2_0(active0, 0x80000000L);
            case 116:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000000L);
            case 124:
                if ((active0 & 0x40L) != 0L) return jjStopAtPos(1, 6);
                break;
            default:
                break;
        }
        return jjStartNfa_0(0, active0);
    }

    private static int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(0, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch(curChar) {
            case 68:
                return jjMoveStringLiteralDfa3_0(active0, 0x8000000000L);
            case 82:
                return jjMoveStringLiteralDfa3_0(active0, 0x10000000000L);
            case 97:
                return jjMoveStringLiteralDfa3_0(active0, 0x200000000L);
            case 101:
                return jjMoveStringLiteralDfa3_0(active0, 0x200000000000L);
            case 103:
                return jjMoveStringLiteralDfa3_0(active0, 0x2000000000L);
            case 104:
                return jjMoveStringLiteralDfa3_0(active0, 0x4000000000L);
            case 105:
                return jjMoveStringLiteralDfa3_0(active0, 0x2001080000000L);
            case 110:
                if ((active0 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(2, 27, 4); else if ((active0 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(2, 29, 4);
                break;
            case 111:
                return jjMoveStringLiteralDfa3_0(active0, 0x900000000L);
            case 114:
                return jjMoveStringLiteralDfa3_0(active0, 0x400000000L);
            case 115:
                if ((active0 & 0x10000000L) != 0L) return jjStartNfaWithStates_0(2, 28, 4); else if ((active0 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(2, 30, 4);
                return jjMoveStringLiteralDfa3_0(active0, 0x1000000000000L);
            case 116:
                return jjMoveStringLiteralDfa3_0(active0, 0x100000000000L);
            default:
                break;
        }
        return jjStartNfa_0(1, active0);
    }

    private static int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(1, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch(curChar) {
            case 97:
                return jjMoveStringLiteralDfa4_0(active0, 0x210000000000L);
            case 101:
                if ((active0 & 0x1000000000000L) != 0L) return jjStartNfaWithStates_0(3, 48, 4);
                return jjMoveStringLiteralDfa4_0(active0, 0x8000000000L);
            case 108:
                if ((active0 & 0x1000000000L) != 0L) return jjStartNfaWithStates_0(3, 36, 4);
                return jjMoveStringLiteralDfa4_0(active0, 0x2000000000000L);
            case 110:
                if ((active0 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(3, 31, 4); else if ((active0 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(3, 33, 4); else if ((active0 & 0x2000000000L) != 0L) return jjStartNfaWithStates_0(3, 37, 4);
                break;
            case 111:
                if ((active0 & 0x4000000000L) != 0L) return jjStartNfaWithStates_0(3, 38, 4);
                return jjMoveStringLiteralDfa4_0(active0, 0x800000000L);
            case 115:
                if ((active0 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(3, 32, 4);
                break;
            case 116:
                if ((active0 & 0x400000000L) != 0L) return jjStartNfaWithStates_0(3, 34, 4);
                break;
            case 117:
                return jjMoveStringLiteralDfa4_0(active0, 0x100000000000L);
            default:
                break;
        }
        return jjStartNfa_0(2, active0);
    }

    private static int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(2, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(3, active0);
            return 4;
        }
        switch(curChar) {
            case 100:
                return jjMoveStringLiteralDfa5_0(active0, 0x10000000000L);
            case 101:
                if ((active0 & 0x2000000000000L) != 0L) return jjStartNfaWithStates_0(4, 49, 4);
                break;
            case 103:
                return jjMoveStringLiteralDfa5_0(active0, 0x8000000000L);
            case 107:
                if ((active0 & 0x200000000000L) != 0L) return jjStartNfaWithStates_0(4, 45, 4);
                break;
            case 114:
                if ((active0 & 0x800000000L) != 0L) return jjStartNfaWithStates_0(4, 35, 4);
                return jjMoveStringLiteralDfa5_0(active0, 0x100000000000L);
            default:
                break;
        }
        return jjStartNfa_0(3, active0);
    }

    private static int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(3, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(4, active0);
            return 5;
        }
        switch(curChar) {
            case 105:
                return jjMoveStringLiteralDfa6_0(active0, 0x10000000000L);
            case 110:
                if ((active0 & 0x100000000000L) != 0L) return jjStartNfaWithStates_0(5, 44, 4);
                break;
            case 114:
                return jjMoveStringLiteralDfa6_0(active0, 0x8000000000L);
            default:
                break;
        }
        return jjStartNfa_0(4, active0);
    }

    private static int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(4, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(5, active0);
            return 6;
        }
        switch(curChar) {
            case 97:
                return jjMoveStringLiteralDfa7_0(active0, 0x10000000000L);
            case 101:
                return jjMoveStringLiteralDfa7_0(active0, 0x8000000000L);
            default:
                break;
        }
        return jjStartNfa_0(5, active0);
    }

    private static int jjMoveStringLiteralDfa7_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(5, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(6, active0);
            return 7;
        }
        switch(curChar) {
            case 101:
                return jjMoveStringLiteralDfa8_0(active0, 0x8000000000L);
            case 110:
                return jjMoveStringLiteralDfa8_0(active0, 0x10000000000L);
            default:
                break;
        }
        return jjStartNfa_0(6, active0);
    }

    private static int jjMoveStringLiteralDfa8_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(6, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(7, active0);
            return 8;
        }
        switch(curChar) {
            case 115:
                if ((active0 & 0x8000000000L) != 0L) return jjStartNfaWithStates_0(8, 39, 4); else if ((active0 & 0x10000000000L) != 0L) return jjStartNfaWithStates_0(8, 40, 4);
                break;
            default:
                break;
        }
        return jjStartNfa_0(7, active0);
    }

    private static int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private static int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 64;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch(jjstateSet[--i]) {
                        case 47:
                        case 4:
                            if ((0x3ff400800000000L & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 37:
                            if ((0x3ff400800000000L & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 57:
                            if ((0x3ff400800000000L & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 38:
                            if ((0x3ff400800000000L & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 30:
                            if ((0x3ff400800000000L & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 36:
                            if ((0x3ff400800000000L & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 2:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 52) kind = 52;
                                jjCheckNAddStates(0, 2);
                            } else if (curChar == 47) jjstateSet[jjnewStateCnt++] = 18; else if (curChar == 34) jjCheckNAdd(16); else if (curChar == 45) jjCheckNAdd(12); else if (curChar == 43) jjCheckNAdd(8); else if (curChar == 36) jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 5:
                            if (curChar == 36) jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 6:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 51) kind = 51;
                            jjstateSet[jjnewStateCnt++] = 5;
                            break;
                        case 7:
                            if (curChar == 43) jjCheckNAdd(8);
                            break;
                        case 8:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(8, 9);
                            break;
                        case 9:
                            if (curChar == 46) jjCheckNAdd(10);
                            break;
                        case 10:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 52) kind = 52;
                            jjCheckNAdd(10);
                            break;
                        case 11:
                            if (curChar == 45) jjCheckNAdd(12);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(12, 13);
                            break;
                        case 13:
                            if (curChar == 46) jjCheckNAdd(14);
                            break;
                        case 14:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 52) kind = 52;
                            jjCheckNAdd(14);
                            break;
                        case 15:
                            if (curChar == 34) jjCheckNAdd(16);
                            break;
                        case 16:
                            if ((0xffffff7b00000000L & l) != 0L) jjCheckNAddTwoStates(16, 17);
                            break;
                        case 17:
                            if (curChar == 34 && kind > 53) kind = 53;
                            break;
                        case 18:
                            if (curChar == 42) jjCheckNAdd(19);
                            break;
                        case 19:
                            if ((0xffffff7b00000000L & l) != 0L) jjCheckNAddTwoStates(19, 21);
                            break;
                        case 20:
                            if (curChar == 47 && kind > 62) kind = 62;
                            break;
                        case 21:
                            if (curChar == 42) jjstateSet[jjnewStateCnt++] = 20;
                            break;
                        case 22:
                            if (curChar == 47) jjstateSet[jjnewStateCnt++] = 18;
                            break;
                        case 23:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 52) kind = 52;
                            jjCheckNAddStates(0, 2);
                            break;
                        case 24:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 52) kind = 52;
                            jjCheckNAdd(24);
                            break;
                        case 25:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(25, 26);
                            break;
                        case 26:
                            if (curChar == 46) jjCheckNAdd(27);
                            break;
                        case 27:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 52) kind = 52;
                            jjCheckNAdd(27);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 47:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 50;
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 46;
                            break;
                        case 37:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 41;
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 36;
                            break;
                        case 57:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 62;
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 56;
                            break;
                        case 38:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 42;
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 37;
                            break;
                        case 30:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 33;
                            if (curChar == 110) jjCheckNAdd(29);
                            break;
                        case 36:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 40;
                            if (curChar == 97) jjCheckNAdd(29);
                            break;
                        case 2:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 50) kind = 50;
                                jjCheckNAdd(4);
                            }
                            if (curChar == 115) jjAddStates(3, 4); else if (curChar == 98) jjAddStates(5, 6); else if (curChar == 102) jjAddStates(7, 8); else if (curChar == 105) jjAddStates(9, 10); else if (curChar == 118) jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 0:
                            if (curChar == 114 && kind > 43) kind = 43;
                            break;
                        case 1:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        case 3:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 4:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 50) kind = 50;
                            jjCheckNAdd(4);
                            break;
                        case 16:
                            if ((0x7fffffe87ffffffL & l) != 0L) jjAddStates(11, 12);
                            break;
                        case 19:
                            if ((0x7fffffe87ffffffL & l) != 0L) jjAddStates(13, 14);
                            break;
                        case 28:
                            if (curChar == 105) jjAddStates(9, 10);
                            break;
                        case 29:
                            if (curChar == 116 && kind > 43) kind = 43;
                            break;
                        case 31:
                            if (curChar == 93) kind = 43;
                            break;
                        case 32:
                        case 39:
                        case 48:
                        case 58:
                            if (curChar == 91) jjCheckNAdd(31);
                            break;
                        case 33:
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 32;
                            break;
                        case 34:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 33;
                            break;
                        case 35:
                            if (curChar == 102) jjAddStates(7, 8);
                            break;
                        case 40:
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 39;
                            break;
                        case 41:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 40;
                            break;
                        case 42:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 41;
                            break;
                        case 43:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 42;
                            break;
                        case 44:
                            if (curChar == 98) jjAddStates(5, 6);
                            break;
                        case 45:
                            if (curChar == 108 && kind > 43) kind = 43;
                            break;
                        case 46:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 45;
                            break;
                        case 49:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 48;
                            break;
                        case 50:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 49;
                            break;
                        case 51:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 50;
                            break;
                        case 52:
                            if (curChar == 115) jjAddStates(3, 4);
                            break;
                        case 53:
                            if (curChar == 103 && kind > 43) kind = 43;
                            break;
                        case 54:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 53;
                            break;
                        case 55:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 54;
                            break;
                        case 56:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 55;
                            break;
                        case 59:
                            if (curChar == 103) jjstateSet[jjnewStateCnt++] = 58;
                            break;
                        case 60:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 59;
                            break;
                        case 61:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 60;
                            break;
                        case 62:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 61;
                            break;
                        case 63:
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 62;
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
            if ((i = jjnewStateCnt) == (startsAt = 64 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 24, 25, 26, 57, 63, 47, 51, 38, 43, 30, 34, 16, 17, 19, 21 };

    /** Token literal values. */
    public static final String[] jjstrLiteralImages = { "", null, null, null, null, "\46\46", "\174\174", "\136", "\41", "\76", "\74", "\76\75", "\74\75", "\75\75", "\41\75", "\53\53", "\55\55", "\75", "\53\75", "\55\75", "\52\75", "\57\75", "\53", "\55", "\57", "\45", "\52", "\163\151\156", "\143\157\163", "\164\141\156", "\141\142\163", "\141\163\151\156", "\141\143\157\163", "\141\164\141\156", "\163\161\162\164", "\146\154\157\157\162", "\143\145\151\154", "\163\151\147\156", "\145\143\150\157", "\164\157\104\145\147\162\145\145\163", "\164\157\122\141\144\151\141\156\163", null, null, null, "\162\145\164\165\162\156", "\142\162\145\141\153", "\55\76", "\151\146", "\145\154\163\145", "\167\150\151\154\145", null, null, null, null, "\173", "\73", "\54", "\50", "\51", "\133", "\135", "\175", null };

    /** Lexer state names. */
    public static final String[] lexStateNames = { "DEFAULT" };

    static final long[] jjtoToken = { 0x7ffff9ffffffffe1L };

    static final long[] jjtoSkip = { 0x1eL };

    protected static SimpleCharStream input_stream;

    private static final int[] jjrounds = new int[64];

    private static final int[] jjstateSet = new int[128];

    protected static char curChar;

    /** Constructor. */
    public EquationParserTokenManager(SimpleCharStream stream) {
        if (input_stream != null) throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
        input_stream = stream;
    }

    /** Constructor. */
    public EquationParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    /** Reinitialise parser. */
    public static void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private static void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 64; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    /** Reinitialise parser. */
    public static void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    /** Switch to specified lex state. */
    public static void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
    }

    protected static Token jjFillToken() {
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

    static int curLexState = 0;

    static int defaultLexState = 0;

    static int jjnewStateCnt;

    static int jjround;

    static int jjmatchedPos;

    static int jjmatchedKind;

    /** Get the next Token. */
    public static Token getNextToken() {
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

    private static void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private static void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private static void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private static void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }
}
