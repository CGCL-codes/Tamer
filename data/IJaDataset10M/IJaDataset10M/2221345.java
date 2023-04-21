package org.gdbi.jlifelines.interp;

import java.io.*;

public class LinesParserTokenManager implements LinesParserConstants {

    public java.io.PrintStream debugStream = System.out;

    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x7fffffe00L) != 0L) {
                    jjmatchedKind = 35;
                    return 8;
                }
                if ((active0 & 0x2000000000000L) != 0L) {
                    jjmatchedKind = 39;
                    return 1;
                }
                return -1;
            case 1:
                if ((active0 & 0x7ffffde00L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 1;
                    return 8;
                }
                if ((active0 & 0x2000L) != 0L) return 8;
                return -1;
            case 2:
                if ((active0 & 0x7ffffde00L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 2;
                    return 8;
                }
                return -1;
            case 3:
                if ((active0 & 0x7fffb9800L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 3;
                    return 8;
                }
                if ((active0 & 0x44600L) != 0L) return 8;
                return -1;
            case 4:
                if ((active0 & 0x40028000L) != 0L) return 8;
                if ((active0 & 0x7bff91800L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 4;
                    return 8;
                }
                return -1;
            case 5:
                if ((active0 & 0x73df91800L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 5;
                    return 8;
                }
                if ((active0 & 0x82000000L) != 0L) return 8;
                return -1;
            case 6:
                if ((active0 & 0x71d181000L) != 0L) return 8;
                if ((active0 & 0x20e10800L) != 0L) {
                    if (jjmatchedPos != 6) {
                        jjmatchedKind = 35;
                        jjmatchedPos = 6;
                    }
                    return 8;
                }
                return -1;
            case 7:
                if ((active0 & 0x20e10800L) != 0L) return 8;
                if ((active0 & 0x80000L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 7;
                    return 8;
                }
                return -1;
            case 8:
                if ((active0 & 0x80000L) != 0L) {
                    jjmatchedKind = 35;
                    jjmatchedPos = 8;
                    return 8;
                }
                return -1;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0() {
        switch(curChar) {
            case 40:
                return jjStopAtPos(0, 41);
            case 41:
                return jjStopAtPos(0, 42);
            case 44:
                return jjStopAtPos(0, 48);
            case 46:
                return jjStartNfaWithStates_0(0, 39, 1);
            case 47:
                return jjMoveStringLiteralDfa1_0(0x40L);
            case 59:
                return jjStopAtPos(0, 47);
            case 80:
                return jjMoveStringLiteralDfa1_0(0x400000000L);
            case 91:
                return jjStopAtPos(0, 45);
            case 93:
                return jjStopAtPos(0, 46);
            case 98:
                return jjMoveStringLiteralDfa1_0(0x40000000L);
            case 99:
                return jjMoveStringLiteralDfa1_0(0x20040800L);
            case 101:
                return jjMoveStringLiteralDfa1_0(0xc000L);
            case 102:
                return jjMoveStringLiteralDfa1_0(0x11fb90400L);
            case 105:
                return jjMoveStringLiteralDfa1_0(0x2000L);
            case 109:
                return jjMoveStringLiteralDfa1_0(0x200000000L);
            case 112:
                return jjMoveStringLiteralDfa1_0(0x200L);
            case 114:
                return jjMoveStringLiteralDfa1_0(0x80000000L);
            case 115:
                return jjMoveStringLiteralDfa1_0(0x1000L);
            case 116:
                return jjMoveStringLiteralDfa1_0(0x400000L);
            case 119:
                return jjMoveStringLiteralDfa1_0(0x20000L);
            case 123:
                return jjStopAtPos(0, 43);
            case 125:
                return jjStopAtPos(0, 44);
            default:
                return jjMoveNfa_0(2, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch(curChar) {
            case 42:
                if ((active0 & 0x40L) != 0L) return jjStopAtPos(1, 6);
                break;
            case 97:
                return jjMoveStringLiteralDfa2_0(active0, 0x500050000L);
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x80000000L);
            case 102:
                if ((active0 & 0x2000L) != 0L) return jjStartNfaWithStates_0(1, 13, 8);
                break;
            case 104:
                return jjMoveStringLiteralDfa2_0(active0, 0x20800L);
            case 108:
                return jjMoveStringLiteralDfa2_0(active0, 0xc000L);
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x23fb80000L);
            case 112:
                return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
            case 114:
                return jjMoveStringLiteralDfa2_0(active0, 0x40400200L);
            case 117:
                return jjMoveStringLiteralDfa2_0(active0, 0x400L);
            default:
                break;
        }
        return jjStartNfa_0(0, active0);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(0, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch(curChar) {
            case 97:
                return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
            case 101:
                return jjMoveStringLiteralDfa3_0(active0, 0x40000000L);
            case 105:
                return jjMoveStringLiteralDfa3_0(active0, 0x20800L);
            case 108:
                return jjMoveStringLiteralDfa3_0(active0, 0x40000L);
            case 109:
                return jjMoveStringLiteralDfa3_0(active0, 0x10000L);
            case 110:
                return jjMoveStringLiteralDfa3_0(active0, 0x20000400L);
            case 111:
                return jjMoveStringLiteralDfa3_0(active0, 0x1200L);
            case 114:
                return jjMoveStringLiteralDfa3_0(active0, 0x41fb80000L);
            case 115:
                return jjMoveStringLiteralDfa3_0(active0, 0xc000L);
            case 116:
                return jjMoveStringLiteralDfa3_0(active0, 0x380000000L);
            default:
                break;
        }
        return jjStartNfa_0(1, active0);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(1, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch(curChar) {
            case 97:
                return jjMoveStringLiteralDfa4_0(active0, 0x40000000L);
            case 99:
                if ((active0 & 0x200L) != 0L) return jjStartNfaWithStates_0(3, 9, 8); else if ((active0 & 0x400L) != 0L) return jjStartNfaWithStates_0(3, 10, 8);
                break;
            case 101:
                if ((active0 & 0x4000L) != 0L) return jjStartNfaWithStates_0(3, 14, 8);
                return jjMoveStringLiteralDfa4_0(active0, 0x410000000L);
            case 102:
                return jjMoveStringLiteralDfa4_0(active0, 0x2000000L);
            case 104:
                return jjMoveStringLiteralDfa4_0(active0, 0x300000000L);
            case 105:
                return jjMoveStringLiteralDfa4_0(active0, 0x198000L);
            case 108:
                if ((active0 & 0x40000L) != 0L) return jjStartNfaWithStates_0(3, 18, 8);
                return jjMoveStringLiteralDfa4_0(active0, 0x1020800L);
            case 110:
                return jjMoveStringLiteralDfa4_0(active0, 0xa00000L);
            case 111:
                return jjMoveStringLiteralDfa4_0(active0, 0x8000000L);
            case 115:
                return jjMoveStringLiteralDfa4_0(active0, 0x4000000L);
            case 116:
                return jjMoveStringLiteralDfa4_0(active0, 0x20000000L);
            case 117:
                return jjMoveStringLiteralDfa4_0(active0, 0x80001000L);
            case 118:
                return jjMoveStringLiteralDfa4_0(active0, 0x400000L);
            default:
                break;
        }
        return jjStartNfa_0(2, active0);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(2, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(3, active0);
            return 4;
        }
        switch(curChar) {
            case 97:
                return jjMoveStringLiteralDfa5_0(active0, 0x2000000L);
            case 100:
                return jjMoveStringLiteralDfa5_0(active0, 0x800L);
            case 101:
                if ((active0 & 0x20000L) != 0L) return jjStartNfaWithStates_0(4, 17, 8);
                return jjMoveStringLiteralDfa5_0(active0, 0x300400000L);
            case 102:
                if ((active0 & 0x8000L) != 0L) return jjStartNfaWithStates_0(4, 15, 8);
                break;
            case 105:
                return jjMoveStringLiteralDfa5_0(active0, 0x21000000L);
            case 107:
                if ((active0 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(4, 30, 8);
                break;
            case 108:
                return jjMoveStringLiteralDfa5_0(active0, 0x10000L);
            case 110:
                return jjMoveStringLiteralDfa5_0(active0, 0x400180000L);
            case 111:
                return jjMoveStringLiteralDfa5_0(active0, 0x4a00000L);
            case 114:
                return jjMoveStringLiteralDfa5_0(active0, 0x80000000L);
            case 115:
                return jjMoveStringLiteralDfa5_0(active0, 0x1000L);
            case 116:
                return jjMoveStringLiteralDfa5_0(active0, 0x8000000L);
            case 118:
                return jjMoveStringLiteralDfa5_0(active0, 0x10000000L);
            default:
                break;
        }
        return jjStartNfa_0(3, active0);
    }

    private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(3, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(4, active0);
            return 5;
        }
        switch(curChar) {
            case 100:
                return jjMoveStringLiteralDfa6_0(active0, 0x980000L);
            case 101:
                return jjMoveStringLiteralDfa6_0(active0, 0x10001000L);
            case 104:
                return jjMoveStringLiteralDfa6_0(active0, 0x8000000L);
            case 105:
                return jjMoveStringLiteralDfa6_0(active0, 0x10000L);
            case 109:
                if ((active0 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(5, 25, 8);
                break;
            case 110:
                if ((active0 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(5, 31, 8);
                return jjMoveStringLiteralDfa6_0(active0, 0x20000000L);
            case 114:
                return jjMoveStringLiteralDfa6_0(active0, 0x300400800L);
            case 115:
                return jjMoveStringLiteralDfa6_0(active0, 0x1000000L);
            case 116:
                return jjMoveStringLiteralDfa6_0(active0, 0x400200000L);
            case 117:
                return jjMoveStringLiteralDfa6_0(active0, 0x4000000L);
            default:
                break;
        }
        return jjStartNfa_0(4, active0);
    }

    private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(4, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(5, active0);
            return 6;
        }
        switch(curChar) {
            case 101:
                return jjMoveStringLiteralDfa7_0(active0, 0xa10800L);
            case 105:
                if ((active0 & 0x100000L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 6;
                }
                return jjMoveStringLiteralDfa7_0(active0, 0x80000L);
            case 110:
                if ((active0 & 0x10000000L) != 0L) return jjStartNfaWithStates_0(6, 28, 8);
                break;
            case 114:
                if ((active0 & 0x4000000L) != 0L) return jjStartNfaWithStates_0(6, 26, 8); else if ((active0 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(6, 27, 8);
                break;
            case 115:
                if ((active0 & 0x1000L) != 0L) return jjStartNfaWithStates_0(6, 12, 8); else if ((active0 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(6, 32, 8); else if ((active0 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(6, 33, 8); else if ((active0 & 0x400000000L) != 0L) return jjStartNfaWithStates_0(6, 34, 8);
                return jjMoveStringLiteralDfa7_0(active0, 0x400000L);
            case 116:
                if ((active0 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(6, 24, 8);
                break;
            case 117:
                return jjMoveStringLiteralDfa7_0(active0, 0x20000000L);
            default:
                break;
        }
        return jjStartNfa_0(5, active0);
    }

    private final int jjMoveStringLiteralDfa7_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(5, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(6, active0);
            return 7;
        }
        switch(curChar) {
            case 101:
                if ((active0 & 0x400000L) != 0L) return jjStartNfaWithStates_0(7, 22, 8); else if ((active0 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(7, 29, 8);
                break;
            case 110:
                if ((active0 & 0x800L) != 0L) return jjStartNfaWithStates_0(7, 11, 8);
                break;
            case 115:
                if ((active0 & 0x10000L) != 0L) return jjStartNfaWithStates_0(7, 16, 8); else if ((active0 & 0x200000L) != 0L) return jjStartNfaWithStates_0(7, 21, 8); else if ((active0 & 0x800000L) != 0L) return jjStartNfaWithStates_0(7, 23, 8);
                return jjMoveStringLiteralDfa8_0(active0, 0x80000L);
            default:
                break;
        }
        return jjStartNfa_0(6, active0);
    }

    private final int jjMoveStringLiteralDfa8_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(6, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(7, active0);
            return 8;
        }
        switch(curChar) {
            case 101:
                return jjMoveStringLiteralDfa9_0(active0, 0x80000L);
            default:
                break;
        }
        return jjStartNfa_0(7, active0);
    }

    private final int jjMoveStringLiteralDfa9_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(7, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(8, active0);
            return 9;
        }
        switch(curChar) {
            case 116:
                if ((active0 & 0x80000L) != 0L) return jjStartNfaWithStates_0(9, 19, 8);
                break;
            default:
                break;
        }
        return jjStartNfa_0(8, active0);
    }

    private final void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private final void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }

    private final void jjCheckNAddStates(int start) {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private final int jjMoveNfa_0(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 13;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 2:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 38) kind = 38;
                                jjCheckNAddStates(0, 2);
                            } else if (curChar == 45) jjCheckNAddStates(0, 2); else if (curChar == 34) jjCheckNAddStates(3, 5); else if (curChar == 46) {
                                if (kind > 39) kind = 39;
                                jjCheckNAdd(1);
                            }
                            break;
                        case 0:
                            if (curChar != 46) break;
                            if (kind > 39) kind = 39;
                            jjCheckNAdd(1);
                            break;
                        case 1:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 39) kind = 39;
                            jjCheckNAdd(1);
                            break;
                        case 3:
                            if ((0xfffffffbffffdbffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 5:
                            if ((0xffffffffffffdbffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 6:
                            if (curChar == 34 && kind > 40) kind = 40;
                            break;
                        case 8:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 35) kind = 35;
                            jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 9:
                            if (curChar == 45) jjCheckNAddStates(0, 2);
                            break;
                        case 10:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 38) kind = 38;
                            jjCheckNAdd(10);
                            break;
                        case 11:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(11, 0);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 38) kind = 38;
                            jjCheckNAddStates(0, 2);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 2:
                        case 8:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 35) kind = 35;
                            jjCheckNAdd(8);
                            break;
                        case 3:
                            if ((0xffffffffefffffffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 4:
                            if (curChar == 92) jjstateSet[jjnewStateCnt++] = 5;
                            break;
                        case 5:
                            jjCheckNAddStates(3, 5);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 3:
                        case 5:
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
            if ((i = jjnewStateCnt) == (startsAt = 13 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjMoveStringLiteralDfa0_1() {
        switch(curChar) {
            case 42:
                return jjMoveStringLiteralDfa1_1(0x80L);
            default:
                return 1;
        }
    }

    private final int jjMoveStringLiteralDfa1_1(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return 1;
        }
        switch(curChar) {
            case 47:
                if ((active0 & 0x80L) != 0L) return jjStopAtPos(1, 7);
                break;
            default:
                return 2;
        }
        return 2;
    }

    static final int[] jjnextStates = { 10, 11, 0, 3, 4, 6 };

    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, "\160\162\157\143", "\146\165\156\143", "\143\150\151\154\144\162\145\156", "\163\160\157\165\163\145\163", "\151\146", "\145\154\163\145", "\145\154\163\151\146", "\146\141\155\151\154\151\145\163", "\167\150\151\154\145", "\143\141\154\154", "\146\157\162\151\156\144\151\163\145\164", "\146\157\162\151\156\144\151", "\146\157\162\156\157\164\145\163", "\164\162\141\166\145\162\163\145", "\146\157\162\156\157\144\145\163", "\146\157\162\154\151\163\164", "\146\157\162\146\141\155", "\146\157\162\163\157\165\162", "\146\157\162\157\164\150\162", "\146\157\162\145\166\145\156", "\143\157\156\164\151\156\165\145", "\142\162\145\141\153", "\162\145\164\165\162\156", "\146\141\164\150\145\162\163", "\155\157\164\150\145\162\163", "\120\141\162\145\156\164\163", null, null, null, null, null, null, "\50", "\51", "\173", "\175", "\133", "\135", "\73", "\54", "\56" };

    public static final String[] lexStateNames = { "DEFAULT", "IN_MULTI_LINE_COMMENT" };

    public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, -1, 1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    static final long[] jjtoToken = { 0x3ffdffffffe01L };

    static final long[] jjtoSkip = { 0xbeL };

    static final long[] jjtoSpecial = { 0x80L };

    static final long[] jjtoMore = { 0x140L };

    protected SimpleCharStream input_stream;

    private final int[] jjrounds = new int[13];

    private final int[] jjstateSet = new int[26];

    StringBuffer image;

    int jjimageLen;

    int lengthOfMatch;

    protected char curChar;

    public LinesParserTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    public LinesParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 13; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 2 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
    }

    protected Token jjFillToken() {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        String im = jjstrLiteralImages[jjmatchedKind];
        t.image = (im == null) ? input_stream.GetImage() : im;
        t.beginLine = input_stream.getBeginLine();
        t.beginColumn = input_stream.getBeginColumn();
        t.endLine = input_stream.getEndLine();
        t.endColumn = input_stream.getEndColumn();
        return t;
    }

    int curLexState = 0;

    int defaultLexState = 0;

    int jjnewStateCnt;

    int jjround;

    int jjmatchedPos;

    int jjmatchedKind;

    public Token getNextToken() {
        int kind;
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;
        EOFLoop: for (; ; ) {
            try {
                curChar = input_stream.BeginToken();
            } catch (java.io.IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }
            image = null;
            jjimageLen = 0;
            for (; ; ) {
                switch(curLexState) {
                    case 0:
                        try {
                            input_stream.backup(0);
                            while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
                        } catch (java.io.IOException e1) {
                            continue EOFLoop;
                        }
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_0();
                        break;
                    case 1:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_1();
                        if (jjmatchedPos == 0 && jjmatchedKind > 8) {
                            jjmatchedKind = 8;
                        }
                        break;
                }
                if (jjmatchedKind != 0x7fffffff) {
                    if (jjmatchedPos + 1 < curPos) input_stream.backup(curPos - jjmatchedPos - 1);
                    if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        matchedToken.specialToken = specialToken;
                        if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                        return matchedToken;
                    } else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                            matchedToken = jjFillToken();
                            if (specialToken == null) specialToken = matchedToken; else {
                                matchedToken.specialToken = specialToken;
                                specialToken = (specialToken.next = matchedToken);
                            }
                            SkipLexicalActions(matchedToken);
                        } else SkipLexicalActions(null);
                        if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                        continue EOFLoop;
                    }
                    jjimageLen += jjmatchedPos + 1;
                    if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                    curPos = 0;
                    jjmatchedKind = 0x7fffffff;
                    try {
                        curChar = input_stream.readChar();
                        continue;
                    } catch (java.io.IOException e1) {
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
    }

    void SkipLexicalActions(Token matchedToken) {
        switch(jjmatchedKind) {
            default:
                break;
        }
    }
}
