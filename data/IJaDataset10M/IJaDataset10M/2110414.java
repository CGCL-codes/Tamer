package com.papyrus.alf2java.parser.alfparser;

/** Token Manager. */
public class GrammaireTokenManager implements GrammaireConstants {

    /** Debug output. */
    public static java.io.PrintStream debugStream = System.out;

    /** Set debug output. */
    public static void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private static final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x40L) != 0L) {
                    jjmatchedKind = 7;
                    return 1;
                }
                return -1;
            case 1:
                if ((active0 & 0x40L) != 0L) {
                    jjmatchedKind = 7;
                    jjmatchedPos = 1;
                    return 1;
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
            case 58:
                return jjStopAtPos(0, 23);
            case 59:
                return jjStopAtPos(0, 22);
            case 61:
                return jjStopAtPos(0, 24);
            case 108:
                return jjMoveStringLiteralDfa1_0(0x40L);
            default:
                return jjMoveNfa_0(0, 0);
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
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x40L);
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
            case 116:
                if ((active0 & 0x40L) != 0L) return jjStartNfaWithStates_0(2, 6, 1);
                break;
            default:
                break;
        }
        return jjStartNfa_0(1, active0);
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

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private static int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 28;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x3fe000000000000L & l) != 0L) {
                                if (kind > 19) kind = 19;
                                jjCheckNAddTwoStates(11, 12);
                            } else if (curChar == 48) jjAddStates(0, 3); else if (curChar == 34) jjCheckNAddTwoStates(14, 15);
                            if (curChar == 48) {
                                if (kind > 19) kind = 19;
                                jjCheckNAddTwoStates(17, 18);
                            }
                            break;
                        case 1:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 7) kind = 7;
                            jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 10:
                            if ((0x3fe000000000000L & l) == 0L) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 13:
                            if (curChar == 34) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 14:
                            if ((0xfffffffbffffffffL & l) != 0L) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 15:
                            if (curChar == 34 && kind > 20) kind = 20;
                            break;
                        case 16:
                            if (curChar != 48) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(17, 18);
                            break;
                        case 18:
                            if ((0xff000000000000L & l) == 0L) break;
                            if (kind > 17) kind = 17;
                            jjCheckNAddTwoStates(17, 18);
                            break;
                        case 19:
                            if (curChar == 48) jjAddStates(0, 3);
                            break;
                        case 21:
                            if ((0x3000000000000L & l) == 0L) break;
                            if (kind > 13) kind = 13;
                            jjAddStates(4, 5);
                            break;
                        case 25:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjAddStates(6, 7);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 7) kind = 7;
                                jjCheckNAdd(1);
                            }
                            if (curChar == 102) jjstateSet[jjnewStateCnt++] = 8; else if (curChar == 116) jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 1:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 7) kind = 7;
                            jjCheckNAdd(1);
                            break;
                        case 2:
                            if (curChar == 101 && kind > 12) kind = 12;
                            break;
                        case 3:
                            if (curChar == 117) jjCheckNAdd(2);
                            break;
                        case 4:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 3;
                            break;
                        case 5:
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 6:
                            if (curChar == 115) jjCheckNAdd(2);
                            break;
                        case 7:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 8:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        case 9:
                            if (curChar == 102) jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 11:
                            if (curChar == 95) jjstateSet[jjnewStateCnt++] = 12;
                            break;
                        case 14:
                            if ((0xffffffffefffffffL & l) != 0L) jjAddStates(8, 9);
                            break;
                        case 17:
                            if (curChar == 95) jjstateSet[jjnewStateCnt++] = 18;
                            break;
                        case 20:
                            if (curChar == 98) jjCheckNAdd(21);
                            break;
                        case 22:
                            if (curChar == 95) jjCheckNAdd(21);
                            break;
                        case 23:
                            if (curChar == 66) jjCheckNAdd(21);
                            break;
                        case 24:
                            if (curChar == 120) jjCheckNAdd(25);
                            break;
                        case 25:
                            if ((0x7e0000007eL & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjCheckNAddTwoStates(26, 25);
                            break;
                        case 26:
                            if (curChar == 95) jjCheckNAdd(25);
                            break;
                        case 27:
                            if (curChar == 88) jjCheckNAdd(25);
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
                        case 14:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(8, 9);
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
            if ((i = jjnewStateCnt) == (startsAt = 28 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private static final int jjStopStringLiteralDfa_2(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x40L) != 0L) {
                    jjmatchedKind = 7;
                    return 1;
                }
                return -1;
            case 1:
                if ((active0 & 0x40L) != 0L) {
                    jjmatchedKind = 7;
                    jjmatchedPos = 1;
                    return 1;
                }
                return -1;
            default:
                return -1;
        }
    }

    private static final int jjStartNfa_2(int pos, long active0) {
        return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
    }

    private static int jjMoveStringLiteralDfa0_2() {
        switch(curChar) {
            case 58:
                return jjStopAtPos(0, 23);
            case 59:
                return jjStopAtPos(0, 22);
            case 61:
                return jjStopAtPos(0, 24);
            case 108:
                return jjMoveStringLiteralDfa1_2(0x40L);
            default:
                return jjMoveNfa_2(0, 0);
        }
    }

    private static int jjMoveStringLiteralDfa1_2(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_2(0, active0);
            return 1;
        }
        switch(curChar) {
            case 101:
                return jjMoveStringLiteralDfa2_2(active0, 0x40L);
            default:
                break;
        }
        return jjStartNfa_2(0, active0);
    }

    private static int jjMoveStringLiteralDfa2_2(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_2(0, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_2(1, active0);
            return 2;
        }
        switch(curChar) {
            case 116:
                if ((active0 & 0x40L) != 0L) return jjStartNfaWithStates_2(2, 6, 1);
                break;
            default:
                break;
        }
        return jjStartNfa_2(1, active0);
    }

    private static int jjStartNfaWithStates_2(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_2(state, pos + 1);
    }

    private static int jjMoveNfa_2(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 28;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x3fe000000000000L & l) != 0L) {
                                if (kind > 19) kind = 19;
                                jjCheckNAddTwoStates(11, 12);
                            } else if (curChar == 48) jjAddStates(0, 3); else if (curChar == 34) jjCheckNAddTwoStates(14, 15);
                            if (curChar == 48) {
                                if (kind > 19) kind = 19;
                                jjCheckNAddTwoStates(17, 18);
                            }
                            break;
                        case 1:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 7) kind = 7;
                            jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 10:
                            if ((0x3fe000000000000L & l) == 0L) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 13:
                            if (curChar == 34) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 14:
                            if ((0xfffffffbffffffffL & l) != 0L) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 15:
                            if (curChar == 34 && kind > 20) kind = 20;
                            break;
                        case 16:
                            if (curChar != 48) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(17, 18);
                            break;
                        case 18:
                            if ((0xff000000000000L & l) == 0L) break;
                            if (kind > 17) kind = 17;
                            jjCheckNAddTwoStates(17, 18);
                            break;
                        case 19:
                            if (curChar == 48) jjAddStates(0, 3);
                            break;
                        case 21:
                            if ((0x3000000000000L & l) == 0L) break;
                            if (kind > 13) kind = 13;
                            jjAddStates(4, 5);
                            break;
                        case 25:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjAddStates(6, 7);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 7) kind = 7;
                                jjCheckNAdd(1);
                            }
                            if (curChar == 102) jjstateSet[jjnewStateCnt++] = 8; else if (curChar == 116) jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 1:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 7) kind = 7;
                            jjCheckNAdd(1);
                            break;
                        case 2:
                            if (curChar == 101 && kind > 12) kind = 12;
                            break;
                        case 3:
                            if (curChar == 117) jjCheckNAdd(2);
                            break;
                        case 4:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 3;
                            break;
                        case 5:
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 6:
                            if (curChar == 115) jjCheckNAdd(2);
                            break;
                        case 7:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 8:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        case 9:
                            if (curChar == 102) jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 11:
                            if (curChar == 95) jjstateSet[jjnewStateCnt++] = 12;
                            break;
                        case 14:
                            if ((0xffffffffefffffffL & l) != 0L) jjAddStates(8, 9);
                            break;
                        case 17:
                            if (curChar == 95) jjstateSet[jjnewStateCnt++] = 18;
                            break;
                        case 20:
                            if (curChar == 98) jjCheckNAdd(21);
                            break;
                        case 22:
                            if (curChar == 95) jjCheckNAdd(21);
                            break;
                        case 23:
                            if (curChar == 66) jjCheckNAdd(21);
                            break;
                        case 24:
                            if (curChar == 120) jjCheckNAdd(25);
                            break;
                        case 25:
                            if ((0x7e0000007eL & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjCheckNAddTwoStates(26, 25);
                            break;
                        case 26:
                            if (curChar == 95) jjCheckNAdd(25);
                            break;
                        case 27:
                            if (curChar == 88) jjCheckNAdd(25);
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
                        case 14:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(8, 9);
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
            if ((i = jjnewStateCnt) == (startsAt = 28 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private static final int jjStopStringLiteralDfa_1(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x40L) != 0L) {
                    jjmatchedKind = 7;
                    return 1;
                }
                return -1;
            case 1:
                if ((active0 & 0x40L) != 0L) {
                    jjmatchedKind = 7;
                    jjmatchedPos = 1;
                    return 1;
                }
                return -1;
            default:
                return -1;
        }
    }

    private static final int jjStartNfa_1(int pos, long active0) {
        return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
    }

    private static int jjMoveStringLiteralDfa0_1() {
        switch(curChar) {
            case 58:
                return jjStopAtPos(0, 23);
            case 59:
                return jjStopAtPos(0, 22);
            case 61:
                return jjStopAtPos(0, 24);
            case 108:
                return jjMoveStringLiteralDfa1_1(0x40L);
            default:
                return jjMoveNfa_1(0, 0);
        }
    }

    private static int jjMoveStringLiteralDfa1_1(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_1(0, active0);
            return 1;
        }
        switch(curChar) {
            case 101:
                return jjMoveStringLiteralDfa2_1(active0, 0x40L);
            default:
                break;
        }
        return jjStartNfa_1(0, active0);
    }

    private static int jjMoveStringLiteralDfa2_1(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_1(0, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_1(1, active0);
            return 2;
        }
        switch(curChar) {
            case 116:
                if ((active0 & 0x40L) != 0L) return jjStartNfaWithStates_1(2, 6, 1);
                break;
            default:
                break;
        }
        return jjStartNfa_1(1, active0);
    }

    private static int jjStartNfaWithStates_1(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_1(state, pos + 1);
    }

    private static int jjMoveNfa_1(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 28;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x3fe000000000000L & l) != 0L) {
                                if (kind > 19) kind = 19;
                                jjCheckNAddTwoStates(11, 12);
                            } else if (curChar == 48) jjAddStates(0, 3); else if (curChar == 34) jjCheckNAddTwoStates(14, 15);
                            if (curChar == 48) {
                                if (kind > 19) kind = 19;
                                jjCheckNAddTwoStates(17, 18);
                            }
                            break;
                        case 1:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 7) kind = 7;
                            jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 10:
                            if ((0x3fe000000000000L & l) == 0L) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 13:
                            if (curChar == 34) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 14:
                            if ((0xfffffffbffffffffL & l) != 0L) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 15:
                            if (curChar == 34 && kind > 20) kind = 20;
                            break;
                        case 16:
                            if (curChar != 48) break;
                            if (kind > 19) kind = 19;
                            jjCheckNAddTwoStates(17, 18);
                            break;
                        case 18:
                            if ((0xff000000000000L & l) == 0L) break;
                            if (kind > 17) kind = 17;
                            jjCheckNAddTwoStates(17, 18);
                            break;
                        case 19:
                            if (curChar == 48) jjAddStates(0, 3);
                            break;
                        case 21:
                            if ((0x3000000000000L & l) == 0L) break;
                            if (kind > 13) kind = 13;
                            jjAddStates(4, 5);
                            break;
                        case 25:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjAddStates(6, 7);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x7fffffe87fffffeL & l) != 0L) {
                                if (kind > 7) kind = 7;
                                jjCheckNAdd(1);
                            }
                            if (curChar == 102) jjstateSet[jjnewStateCnt++] = 8; else if (curChar == 116) jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 1:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 7) kind = 7;
                            jjCheckNAdd(1);
                            break;
                        case 2:
                            if (curChar == 101 && kind > 12) kind = 12;
                            break;
                        case 3:
                            if (curChar == 117) jjCheckNAdd(2);
                            break;
                        case 4:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 3;
                            break;
                        case 5:
                            if (curChar == 116) jjstateSet[jjnewStateCnt++] = 4;
                            break;
                        case 6:
                            if (curChar == 115) jjCheckNAdd(2);
                            break;
                        case 7:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 6;
                            break;
                        case 8:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        case 9:
                            if (curChar == 102) jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 11:
                            if (curChar == 95) jjstateSet[jjnewStateCnt++] = 12;
                            break;
                        case 14:
                            if ((0xffffffffefffffffL & l) != 0L) jjAddStates(8, 9);
                            break;
                        case 17:
                            if (curChar == 95) jjstateSet[jjnewStateCnt++] = 18;
                            break;
                        case 20:
                            if (curChar == 98) jjCheckNAdd(21);
                            break;
                        case 22:
                            if (curChar == 95) jjCheckNAdd(21);
                            break;
                        case 23:
                            if (curChar == 66) jjCheckNAdd(21);
                            break;
                        case 24:
                            if (curChar == 120) jjCheckNAdd(25);
                            break;
                        case 25:
                            if ((0x7e0000007eL & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjCheckNAddTwoStates(26, 25);
                            break;
                        case 26:
                            if (curChar == 95) jjCheckNAdd(25);
                            break;
                        case 27:
                            if (curChar == 88) jjCheckNAdd(25);
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
                        case 14:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(8, 9);
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
            if ((i = jjnewStateCnt) == (startsAt = 28 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 20, 23, 24, 27, 22, 21, 26, 25, 14, 15 };

    /** Token literal values. */
    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, "\154\145\164", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "\73", "\72", "\75" };

    /** Lexer state names. */
    public static final String[] lexStateNames = { "DEFAULT", "IN_STATEMENT_ANNOTATION", "IN_IN_LINE_ANNOTATION" };

    /** Lex State array. */
    public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    static final long[] jjtoToken = { 0x1dab0c1L };

    static final long[] jjtoSkip = { 0x3eL };

    protected static SimpleCharStream input_stream;

    private static final int[] jjrounds = new int[28];

    private static final int[] jjstateSet = new int[56];

    protected static char curChar;

    /** Constructor. */
    public GrammaireTokenManager(SimpleCharStream stream) {
        if (input_stream != null) throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", TokenMgrError.STATIC_LEXER_ERROR);
        input_stream = stream;
    }

    /** Constructor. */
    public GrammaireTokenManager(SimpleCharStream stream, int lexState) {
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
        for (i = 28; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    /** Reinitialise parser. */
    public static void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    /** Switch to specified lex state. */
    public static void SwitchTo(int lexState) {
        if (lexState >= 3 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
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
                    try {
                        input_stream.backup(0);
                        while (curChar <= 32 && (0x100001200L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
                    } catch (java.io.IOException e1) {
                        continue EOFLoop;
                    }
                    jjmatchedKind = 0x7fffffff;
                    jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_1();
                    break;
                case 2:
                    try {
                        input_stream.backup(0);
                        while (curChar <= 32 && (0x100001200L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
                    } catch (java.io.IOException e1) {
                        continue EOFLoop;
                    }
                    jjmatchedKind = 0x7fffffff;
                    jjmatchedPos = 0;
                    curPos = jjMoveStringLiteralDfa0_2();
                    break;
            }
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos) input_stream.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                    return matchedToken;
                } else {
                    if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
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
}
