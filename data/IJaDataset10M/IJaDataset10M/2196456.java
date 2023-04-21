package org.dance.parser;

import java.util.*;
import java.io.*;
import java.util.regex.*;
import org.dance.score.*;
import org.dance.parser.i18n.*;

/** Token Manager. */
public class ModernRockParserTokenManager implements ModernRockParserConstants {

    /** Debug output. */
    public java.io.PrintStream debugStream = System.out;

    /** Set debug output. */
    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x1000000000L) != 0L) return 0;
                if ((active0 & 0x10000000000L) != 0L) return 101;
                if ((active0 & 0x1000900000000L) != 0L) return 35;
                if ((active0 & 0x10000000L) != 0L) return 25;
                if ((active0 & 0x2000L) != 0L) return 184;
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
            case 33:
                return jjMoveStringLiteralDfa1_0(0x1000900000000L);
            case 38:
                return jjStopAtPos(0, 10);
            case 40:
                return jjStartNfaWithStates_0(0, 13, 184);
            case 41:
                return jjStopAtPos(0, 14);
            case 47:
                return jjStartNfaWithStates_0(0, 36, 0);
            case 61:
                return jjMoveStringLiteralDfa1_0(0x200000000L);
            case 67:
                return jjMoveStringLiteralDfa1_0(0x10000000000L);
            case 70:
                return jjStopAtPos(0, 7);
            case 76:
                return jjStopAtPos(0, 8);
            case 78:
                return jjMoveStringLiteralDfa1_0(0x10000000L);
            case 123:
                return jjStopAtPos(0, 11);
            case 125:
                return jjStopAtPos(0, 12);
            default:
                return jjMoveNfa_0(5, 0);
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
            case 33:
                if ((active0 & 0x800000000L) != 0L) return jjStopAtPos(1, 35);
                break;
            case 47:
                return jjMoveStringLiteralDfa2_0(active0, 0x1000000000000L);
            case 62:
                if ((active0 & 0x100000000L) != 0L) return jjStopAtPos(1, 32); else if ((active0 & 0x200000000L) != 0L) return jjStopAtPos(1, 33);
                break;
            case 108:
                if ((active0 & 0x10000000000L) != 0L) return jjStopAtPos(1, 40);
                break;
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x10000000L);
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
            case 33:
                if ((active0 & 0x1000000000000L) != 0L) return jjStopAtPos(2, 48);
                break;
            case 110:
                return jjMoveStringLiteralDfa3_0(active0, 0x10000000L);
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
            case 101:
                if ((active0 & 0x10000000L) != 0L) return jjStopAtPos(3, 28);
                break;
            default:
                break;
        }
        return jjStartNfa_0(2, active0);
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
        jjnewStateCnt = 281;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch(jjstateSet[--i]) {
                        case 101:
                        case 31:
                            if ((0x488000000000L & l) != 0L && kind > 27) kind = 27;
                            break;
                        case 5:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(0, 6); else if ((0x488000000000L & l) != 0L) jjAddStates(7, 8); else if (curChar == 60) jjAddStates(9, 18); else if (curChar == 40) jjAddStates(19, 20); else if (curChar == 45) jjAddStates(21, 22); else if (curChar == 42) jjstateSet[jjnewStateCnt++] = 46; else if (curChar == 58) jjCheckNAdd(38); else if (curChar == 33) jjstateSet[jjnewStateCnt++] = 35; else if (curChar == 35) jjstateSet[jjnewStateCnt++] = 22; else if (curChar == 47) jjstateSet[jjnewStateCnt++] = 0;
                            if ((0x488000000000L & l) != 0L) jjAddStates(23, 24); else if (curChar == 50) jjAddStates(25, 26); else if (curChar == 45) jjstateSet[jjnewStateCnt++] = 7;
                            if ((0x408000000000L & l) != 0L) jjstateSet[jjnewStateCnt++] = 9;
                            if (curChar == 39) jjCheckNAddStates(27, 31);
                            if (curChar == 39) jjstateSet[jjnewStateCnt++] = 51;
                            break;
                        case 0:
                            if (curChar == 47) jjCheckNAddStates(32, 34);
                            break;
                        case 1:
                            if ((0xffffffffffffdbffL & l) != 0L) jjCheckNAddStates(32, 34);
                            break;
                        case 2:
                            if ((0x2400L & l) != 0L && kind > 6) kind = 6;
                            break;
                        case 3:
                            if (curChar == 10 && kind > 6) kind = 6;
                            break;
                        case 4:
                            if (curChar == 13) jjstateSet[jjnewStateCnt++] = 3;
                            break;
                        case 6:
                            if (curChar == 45) jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        case 7:
                            if (curChar == 50 && kind > 9) kind = 9;
                            break;
                        case 8:
                            if ((0x408000000000L & l) != 0L) jjstateSet[jjnewStateCnt++] = 9;
                            break;
                        case 16:
                            if ((0x408000000000L & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjAddStates(35, 36);
                            break;
                        case 17:
                            if (curChar == 39 && kind > 16) kind = 16;
                            break;
                        case 20:
                            if (curChar == 39 && kind > 18) kind = 18;
                            break;
                        case 22:
                            if (curChar == 39 && kind > 19) kind = 19;
                            break;
                        case 23:
                            if (curChar == 35) jjstateSet[jjnewStateCnt++] = 22;
                            break;
                        case 25:
                            if ((0x488000000000L & l) != 0L && kind > 20) kind = 20;
                            break;
                        case 26:
                            if ((0x488000000000L & l) != 0L) jjAddStates(23, 24);
                            break;
                        case 34:
                            if (curChar == 33) jjstateSet[jjnewStateCnt++] = 35;
                            break;
                        case 35:
                            if ((0x3ff000000000000L & l) != 0L) jjstateSet[jjnewStateCnt++] = 36;
                            break;
                        case 36:
                            if (curChar == 33 && kind > 34) kind = 34;
                            break;
                        case 37:
                            if (curChar == 58) jjCheckNAdd(38);
                            break;
                        case 38:
                            if ((0x3ff000000000000L & l) != 0L && kind > 38) kind = 38;
                            break;
                        case 39:
                            if (curChar == 50) jjAddStates(25, 26);
                            break;
                        case 40:
                            if (curChar == 39) jjstateSet[jjnewStateCnt++] = 42;
                            break;
                        case 44:
                            if (curChar == 39 && kind > 39) kind = 39;
                            break;
                        case 47:
                            if (curChar == 42) jjstateSet[jjnewStateCnt++] = 46;
                            break;
                        case 48:
                            if (curChar == 42 && kind > 43) kind = 43;
                            break;
                        case 50:
                            if (curChar == 39) jjstateSet[jjnewStateCnt++] = 51;
                            break;
                        case 52:
                            jjAddStates(37, 38);
                            break;
                        case 55:
                            if (curChar == 58 && kind > 50) kind = 50;
                            break;
                        case 59:
                            if (curChar == 45) jjstateSet[jjnewStateCnt++] = 58;
                            break;
                        case 67:
                            if (curChar == 58 && kind > 49) kind = 49;
                            break;
                        case 72:
                            if (curChar == 45) jjAddStates(21, 22);
                            break;
                        case 75:
                            if ((0x488000000000L & l) != 0L) jjAddStates(7, 8);
                            break;
                        case 78:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(0, 6);
                            break;
                        case 79:
                            if (curChar == 44) jjstateSet[jjnewStateCnt++] = 80;
                            break;
                        case 80:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(39, 41);
                            break;
                        case 81:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(8, 15);
                            break;
                        case 82:
                            if (curChar == 45) jjCheckNAdd(38);
                            break;
                        case 83:
                            if (curChar == 44) jjstateSet[jjnewStateCnt++] = 84;
                            break;
                        case 84:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(42, 44);
                            break;
                        case 85:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(88, 91);
                            break;
                        case 88:
                            if (curChar == 39) jjCheckNAdd(87);
                            break;
                        case 89:
                            if (curChar == 39 && kind > 42) kind = 42;
                            break;
                        case 92:
                            if (curChar == 39) jjCheckNAddStates(27, 31);
                            break;
                        case 98:
                            if (curChar == 35 && kind > 19) kind = 19;
                            break;
                        case 100:
                            if (curChar == 39 && kind > 17) kind = 17;
                            break;
                        case 103:
                            if (curChar == 62 && kind > 23) kind = 23;
                            break;
                        case 112:
                            if (curChar == 60) jjstateSet[jjnewStateCnt++] = 111;
                            break;
                        case 113:
                            if (curChar == 39 && kind > 30) kind = 30;
                            break;
                        case 117:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(45, 47);
                            break;
                        case 118:
                            if (curChar == 44) jjstateSet[jjnewStateCnt++] = 119;
                            break;
                        case 119:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(48, 50);
                            break;
                        case 120:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(123, 126);
                            break;
                        case 123:
                            if (curChar == 39) jjstateSet[jjnewStateCnt++] = 122;
                            break;
                        case 124:
                            if (curChar == 39 && kind > 41) kind = 41;
                            break;
                        case 127:
                            if (curChar == 39) jjAddStates(51, 52);
                            break;
                        case 130:
                            if (curChar == 60) jjAddStates(53, 56);
                            break;
                        case 156:
                            if (curChar == 60) jjAddStates(57, 60);
                            break;
                        case 157:
                            if (curChar == 62 && kind > 22) kind = 22;
                            break;
                        case 183:
                            if (curChar == 40) jjAddStates(19, 20);
                            break;
                        case 185:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(61, 63);
                            break;
                        case 186:
                            if (curChar == 44) jjstateSet[jjnewStateCnt++] = 187;
                            break;
                        case 187:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(64, 66);
                            break;
                        case 188:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(191, 193);
                            break;
                        case 190:
                            if (curChar == 41 && kind > 31) kind = 31;
                            break;
                        case 191:
                            if (curChar == 39) jjstateSet[jjnewStateCnt++] = 189;
                            break;
                        case 192:
                            if (curChar == 39) jjstateSet[jjnewStateCnt++] = 190;
                            break;
                        case 195:
                            jjAddStates(67, 68);
                            break;
                        case 197:
                            if (curChar == 41 && kind > 46) kind = 46;
                            break;
                        case 199:
                            if (curChar == 58 && kind > 51) kind = 51;
                            break;
                        case 205:
                            jjAddStates(69, 70);
                            break;
                        case 207:
                            if (curChar == 39 && kind > 45) kind = 45;
                            break;
                        case 208:
                            jjAddStates(71, 72);
                            break;
                        case 210:
                            if (curChar == 60) jjAddStates(9, 18);
                            break;
                        case 219:
                            if (curChar == 62) jjstateSet[jjnewStateCnt++] = 220;
                            break;
                        case 254:
                            if (curChar == 62) jjstateSet[jjnewStateCnt++] = 255;
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 184:
                            if ((0x104000000000L & l) != 0L) jjAddStates(73, 75); else if (curChar == 91) jjCheckNAddTwoStates(195, 196);
                            break;
                        case 101:
                            if (curChar == 114) {
                                if (kind > 39) kind = 39;
                            } else if (curChar == 97) jjstateSet[jjnewStateCnt++] = 100;
                            break;
                        case 5:
                            if ((0x104000000000L & l) != 0L) jjAddStates(76, 84); else if (curChar == 91) jjCheckNAddStates(85, 88); else if (curChar == 83) jjCheckNAddStates(89, 91); else if (curChar == 67) jjCheckNAddTwoStates(101, 41); else if (curChar == 126) jjAddStates(7, 8); else if (curChar == 84) jjCheckNAddTwoStates(67, 71); else if (curChar == 89) jjCheckNAddTwoStates(55, 65); else if (curChar == 111) jjstateSet[jjnewStateCnt++] = 48; else if (curChar == 120) jjstateSet[jjnewStateCnt++] = 30; else if (curChar == 78) jjCheckNAdd(25); else if (curChar == 86) jjstateSet[jjnewStateCnt++] = 20; else if (curChar == 66) jjstateSet[jjnewStateCnt++] = 18; else if (curChar == 64) jjstateSet[jjnewStateCnt++] = 16;
                            if ((0x104000000000L & l) != 0L) jjAddStates(25, 26); else if (curChar == 67) jjCheckNAdd(31);
                            break;
                        case 1:
                            jjAddStates(32, 34);
                            break;
                        case 7:
                            if ((0x104000000000L & l) != 0L && kind > 9) kind = 9;
                            break;
                        case 9:
                            if (curChar != 64) break;
                            if (kind > 15) kind = 15;
                            jjCheckNAddTwoStates(10, 14);
                            break;
                        case 10:
                            if ((0x40000040000000L & l) == 0L) break;
                            if (kind > 15) kind = 15;
                            jjCheckNAdd(14);
                            break;
                        case 11:
                            if (curChar == 122 && kind > 15) kind = 15;
                            break;
                        case 12:
                            if (curChar == 122) jjstateSet[jjnewStateCnt++] = 11;
                            break;
                        case 13:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 12;
                            break;
                        case 14:
                            if (curChar == 106) jjstateSet[jjnewStateCnt++] = 13;
                            break;
                        case 15:
                            if (curChar == 64) jjstateSet[jjnewStateCnt++] = 16;
                            break;
                        case 18:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 17;
                            break;
                        case 19:
                            if (curChar == 66) jjstateSet[jjnewStateCnt++] = 18;
                            break;
                        case 21:
                            if (curChar == 86) jjstateSet[jjnewStateCnt++] = 20;
                            break;
                        case 24:
                        case 77:
                            if (curChar == 78) jjCheckNAdd(25);
                            break;
                        case 25:
                            if (curChar == 126 && kind > 20) kind = 20;
                            break;
                        case 27:
                            if (curChar == 67 && kind > 27) kind = 27;
                            break;
                        case 28:
                            if (curChar == 120 && kind > 27) kind = 27;
                            break;
                        case 29:
                            if (curChar == 67) jjstateSet[jjnewStateCnt++] = 28;
                            break;
                        case 30:
                            if (curChar == 67) jjCheckNAdd(31);
                            break;
                        case 32:
                            if (curChar == 120) jjstateSet[jjnewStateCnt++] = 30;
                            break;
                        case 33:
                            if (curChar == 67) jjCheckNAdd(31);
                            break;
                        case 39:
                            if ((0x104000000000L & l) != 0L) jjAddStates(25, 26);
                            break;
                        case 40:
                            if (curChar == 124) jjstateSet[jjnewStateCnt++] = 42;
                            break;
                        case 41:
                            if (curChar == 114 && kind > 39) kind = 39;
                            break;
                        case 42:
                            if (curChar == 67) jjCheckNAdd(41);
                            break;
                        case 43:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 44;
                            break;
                        case 44:
                            if (curChar == 124 && kind > 39) kind = 39;
                            break;
                        case 45:
                            if (curChar == 67) jjstateSet[jjnewStateCnt++] = 43;
                            break;
                        case 46:
                            if (curChar == 111 && kind > 43) kind = 43;
                            break;
                        case 49:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 48;
                            break;
                        case 51:
                            if (curChar == 91) jjCheckNAddTwoStates(52, 53);
                            break;
                        case 52:
                            if ((0xffffffffdfffffffL & l) != 0L) jjCheckNAddTwoStates(52, 53);
                            break;
                        case 53:
                            if (curChar == 93 && kind > 45) kind = 45;
                            break;
                        case 54:
                            if (curChar == 89) jjCheckNAddTwoStates(55, 65);
                            break;
                        case 56:
                            if (curChar == 76) jjCheckNAdd(55);
                            break;
                        case 57:
                            if (curChar == 82) jjstateSet[jjnewStateCnt++] = 56;
                            break;
                        case 58:
                            if (curChar == 85) jjstateSet[jjnewStateCnt++] = 57;
                            break;
                        case 60:
                            if (curChar == 69) jjstateSet[jjnewStateCnt++] = 59;
                            break;
                        case 61:
                            if (curChar == 66) jjstateSet[jjnewStateCnt++] = 60;
                            break;
                        case 62:
                            if (curChar == 85) jjstateSet[jjnewStateCnt++] = 61;
                            break;
                        case 63:
                            if (curChar == 84) jjstateSet[jjnewStateCnt++] = 62;
                            break;
                        case 64:
                            if (curChar == 85) jjstateSet[jjnewStateCnt++] = 63;
                            break;
                        case 65:
                            if (curChar == 79) jjstateSet[jjnewStateCnt++] = 64;
                            break;
                        case 66:
                            if (curChar == 84) jjCheckNAddTwoStates(67, 71);
                            break;
                        case 68:
                            if (curChar == 69) jjCheckNAdd(67);
                            break;
                        case 69:
                            if (curChar == 76) jjstateSet[jjnewStateCnt++] = 68;
                            break;
                        case 70:
                            if (curChar == 84) jjstateSet[jjnewStateCnt++] = 69;
                            break;
                        case 71:
                            if (curChar == 73) jjstateSet[jjnewStateCnt++] = 70;
                            break;
                        case 73:
                            if (curChar == 105 && kind > 44) kind = 44;
                            break;
                        case 74:
                            if (curChar == 115 && kind > 44) kind = 44;
                            break;
                        case 75:
                            if (curChar == 126) jjAddStates(7, 8);
                            break;
                        case 76:
                            if (curChar == 78 && kind > 20) kind = 20;
                            break;
                        case 86:
                            if (curChar == 112 && kind > 42) kind = 42;
                            break;
                        case 87:
                            if (curChar == 83) jjstateSet[jjnewStateCnt++] = 86;
                            break;
                        case 90:
                            if (curChar == 112) jjstateSet[jjnewStateCnt++] = 89;
                            break;
                        case 91:
                            if (curChar == 83) jjCheckNAdd(90);
                            break;
                        case 93:
                            if (curChar == 108 && kind > 16) kind = 16;
                            break;
                        case 94:
                            if (curChar == 66) jjstateSet[jjnewStateCnt++] = 93;
                            break;
                        case 95:
                            if (curChar == 97 && kind > 17) kind = 17;
                            break;
                        case 96:
                            if (curChar == 67) jjstateSet[jjnewStateCnt++] = 95;
                            break;
                        case 97:
                            if (curChar == 86 && kind > 18) kind = 18;
                            break;
                        case 99:
                            if (curChar == 67) jjCheckNAddTwoStates(101, 41);
                            break;
                        case 102:
                            if ((0x104000000000L & l) != 0L) jjAddStates(76, 84);
                            break;
                        case 104:
                            if (curChar == 97) jjCheckNAdd(103);
                            break;
                        case 105:
                            if (curChar == 118) jjstateSet[jjnewStateCnt++] = 104;
                            break;
                        case 106:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 105;
                            break;
                        case 107:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 106;
                            break;
                        case 108:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 107;
                            break;
                        case 109:
                            if (curChar == 115) jjstateSet[jjnewStateCnt++] = 108;
                            break;
                        case 110:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 109;
                            break;
                        case 111:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 110;
                            break;
                        case 114:
                        case 115:
                            if (curChar == 66) jjCheckNAdd(113);
                            break;
                        case 116:
                            if (curChar == 67) jjstateSet[jjnewStateCnt++] = 115;
                            break;
                        case 121:
                            if (curChar == 99 && kind > 41) kind = 41;
                            break;
                        case 122:
                            if (curChar == 87) jjstateSet[jjnewStateCnt++] = 121;
                            break;
                        case 125:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 124;
                            break;
                        case 126:
                            if (curChar == 87) jjstateSet[jjnewStateCnt++] = 125;
                            break;
                        case 128:
                            if (curChar == 66 && kind > 30) kind = 30;
                            break;
                        case 129:
                            if (curChar == 67) jjstateSet[jjnewStateCnt++] = 128;
                            break;
                        case 131:
                            if (curChar == 98) jjCheckNAdd(103);
                            break;
                        case 132:
                            if (curChar == 109) jjstateSet[jjnewStateCnt++] = 131;
                            break;
                        case 133:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 132;
                            break;
                        case 134:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 133;
                            break;
                        case 135:
                        case 148:
                            if (curChar == 101) jjCheckNAdd(103);
                            break;
                        case 136:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 135;
                            break;
                        case 137:
                            if (curChar == 103) jjstateSet[jjnewStateCnt++] = 136;
                            break;
                        case 138:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 137;
                            break;
                        case 139:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 138;
                            break;
                        case 140:
                            if (curChar == 112) jjstateSet[jjnewStateCnt++] = 139;
                            break;
                        case 141:
                            if (curChar == 114) jjCheckNAdd(103);
                            break;
                        case 142:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 141;
                            break;
                        case 143:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 142;
                            break;
                        case 144:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 143;
                            break;
                        case 145:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 144;
                            break;
                        case 146:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 145;
                            break;
                        case 147:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 146;
                            break;
                        case 149:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 148;
                            break;
                        case 150:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 149;
                            break;
                        case 151:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 150;
                            break;
                        case 152:
                            if (curChar == 107) jjstateSet[jjnewStateCnt++] = 151;
                            break;
                        case 153:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 152;
                            break;
                        case 154:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 153;
                            break;
                        case 155:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 154;
                            break;
                        case 158:
                            if (curChar == 98) jjCheckNAdd(157);
                            break;
                        case 159:
                            if (curChar == 109) jjstateSet[jjnewStateCnt++] = 158;
                            break;
                        case 160:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 159;
                            break;
                        case 161:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 160;
                            break;
                        case 162:
                        case 175:
                            if (curChar == 101) jjCheckNAdd(157);
                            break;
                        case 163:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 162;
                            break;
                        case 164:
                            if (curChar == 103) jjstateSet[jjnewStateCnt++] = 163;
                            break;
                        case 165:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 164;
                            break;
                        case 166:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 165;
                            break;
                        case 167:
                            if (curChar == 112) jjstateSet[jjnewStateCnt++] = 166;
                            break;
                        case 168:
                            if (curChar == 114) jjCheckNAdd(157);
                            break;
                        case 169:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 168;
                            break;
                        case 170:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 169;
                            break;
                        case 171:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 170;
                            break;
                        case 172:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 171;
                            break;
                        case 173:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 172;
                            break;
                        case 174:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 173;
                            break;
                        case 176:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 175;
                            break;
                        case 177:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 176;
                            break;
                        case 178:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 177;
                            break;
                        case 179:
                            if (curChar == 107) jjstateSet[jjnewStateCnt++] = 178;
                            break;
                        case 180:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 179;
                            break;
                        case 181:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 180;
                            break;
                        case 182:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 181;
                            break;
                        case 189:
                            if (curChar == 64) jjstateSet[jjnewStateCnt++] = 190;
                            break;
                        case 193:
                            if (curChar == 64) jjstateSet[jjnewStateCnt++] = 192;
                            break;
                        case 194:
                            if (curChar == 91) jjCheckNAddTwoStates(195, 196);
                            break;
                        case 195:
                            if ((0xffffffffdfffffffL & l) != 0L) jjCheckNAddTwoStates(195, 196);
                            break;
                        case 196:
                            if (curChar == 93) jjstateSet[jjnewStateCnt++] = 197;
                            break;
                        case 198:
                            if (curChar == 83) jjCheckNAddStates(89, 91);
                            break;
                        case 200:
                            if (curChar == 69) jjCheckNAdd(199);
                            break;
                        case 201:
                            if (curChar == 82) jjstateSet[jjnewStateCnt++] = 200;
                            break;
                        case 202:
                            if (curChar == 79) jjstateSet[jjnewStateCnt++] = 201;
                            break;
                        case 203:
                            if (curChar == 67) jjstateSet[jjnewStateCnt++] = 202;
                            break;
                        case 204:
                            if (curChar == 91) jjCheckNAddStates(85, 88);
                            break;
                        case 205:
                            if ((0xffffffffdfffffffL & l) != 0L) jjCheckNAddTwoStates(205, 206);
                            break;
                        case 206:
                            if (curChar == 93) jjstateSet[jjnewStateCnt++] = 207;
                            break;
                        case 208:
                            if ((0xffffffffdfffffffL & l) != 0L) jjCheckNAddTwoStates(208, 209);
                            break;
                        case 209:
                            if (curChar == 93 && kind > 47) kind = 47;
                            break;
                        case 211:
                            if (curChar == 111) jjCheckNAdd(157);
                            break;
                        case 212:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 211;
                            break;
                        case 213:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 212;
                            break;
                        case 214:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 213;
                            break;
                        case 215:
                            if (curChar == 98) jjstateSet[jjnewStateCnt++] = 214;
                            break;
                        case 216:
                            if (curChar == 109) jjstateSet[jjnewStateCnt++] = 215;
                            break;
                        case 217:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 216;
                            break;
                        case 218:
                            if (curChar == 115) jjstateSet[jjnewStateCnt++] = 217;
                            break;
                        case 220:
                            if ((0x104000000000L & l) != 0L && kind > 22) kind = 22;
                            break;
                        case 221:
                            if (curChar == 98) jjCheckNAdd(219);
                            break;
                        case 222:
                            if (curChar == 109) jjstateSet[jjnewStateCnt++] = 221;
                            break;
                        case 223:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 222;
                            break;
                        case 224:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 223;
                            break;
                        case 225:
                        case 238:
                            if (curChar == 101) jjCheckNAdd(219);
                            break;
                        case 226:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 225;
                            break;
                        case 227:
                            if (curChar == 103) jjstateSet[jjnewStateCnt++] = 226;
                            break;
                        case 228:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 227;
                            break;
                        case 229:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 228;
                            break;
                        case 230:
                            if (curChar == 112) jjstateSet[jjnewStateCnt++] = 229;
                            break;
                        case 231:
                            if (curChar == 114) jjCheckNAdd(219);
                            break;
                        case 232:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 231;
                            break;
                        case 233:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 232;
                            break;
                        case 234:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 233;
                            break;
                        case 235:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 234;
                            break;
                        case 236:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 235;
                            break;
                        case 237:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 236;
                            break;
                        case 239:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 238;
                            break;
                        case 240:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 239;
                            break;
                        case 241:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 240;
                            break;
                        case 242:
                            if (curChar == 107) jjstateSet[jjnewStateCnt++] = 241;
                            break;
                        case 243:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 242;
                            break;
                        case 244:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 243;
                            break;
                        case 245:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 244;
                            break;
                        case 246:
                            if (curChar == 111) jjCheckNAdd(103);
                            break;
                        case 247:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 246;
                            break;
                        case 248:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 247;
                            break;
                        case 249:
                            if (curChar == 114) jjstateSet[jjnewStateCnt++] = 248;
                            break;
                        case 250:
                            if (curChar == 98) jjstateSet[jjnewStateCnt++] = 249;
                            break;
                        case 251:
                            if (curChar == 109) jjstateSet[jjnewStateCnt++] = 250;
                            break;
                        case 252:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 251;
                            break;
                        case 253:
                            if (curChar == 115) jjstateSet[jjnewStateCnt++] = 252;
                            break;
                        case 255:
                            if ((0x104000000000L & l) != 0L && kind > 23) kind = 23;
                            break;
                        case 256:
                            if (curChar == 98) jjCheckNAdd(254);
                            break;
                        case 257:
                            if (curChar == 109) jjstateSet[jjnewStateCnt++] = 256;
                            break;
                        case 258:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 257;
                            break;
                        case 259:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 258;
                            break;
                        case 260:
                        case 273:
                            if (curChar == 101) jjCheckNAdd(254);
                            break;
                        case 261:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 260;
                            break;
                        case 262:
                            if (curChar == 103) jjstateSet[jjnewStateCnt++] = 261;
                            break;
                        case 263:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 262;
                            break;
                        case 264:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 263;
                            break;
                        case 265:
                            if (curChar == 112) jjstateSet[jjnewStateCnt++] = 264;
                            break;
                        case 266:
                            if (curChar == 114) jjCheckNAdd(254);
                            break;
                        case 267:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 266;
                            break;
                        case 268:
                            if (curChar == 105) jjstateSet[jjnewStateCnt++] = 267;
                            break;
                        case 269:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 268;
                            break;
                        case 270:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 269;
                            break;
                        case 271:
                            if (curChar == 111) jjstateSet[jjnewStateCnt++] = 270;
                            break;
                        case 272:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 271;
                            break;
                        case 274:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 273;
                            break;
                        case 275:
                            if (curChar == 97) jjstateSet[jjnewStateCnt++] = 274;
                            break;
                        case 276:
                            if (curChar == 108) jjstateSet[jjnewStateCnt++] = 275;
                            break;
                        case 277:
                            if (curChar == 107) jjstateSet[jjnewStateCnt++] = 276;
                            break;
                        case 278:
                            if (curChar == 99) jjstateSet[jjnewStateCnt++] = 277;
                            break;
                        case 279:
                            if (curChar == 101) jjstateSet[jjnewStateCnt++] = 278;
                            break;
                        case 280:
                            if (curChar == 110) jjstateSet[jjnewStateCnt++] = 279;
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
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(32, 34);
                            break;
                        case 52:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(37, 38);
                            break;
                        case 195:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(67, 68);
                            break;
                        case 205:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(69, 70);
                            break;
                        case 208:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(71, 72);
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
            if ((i = jjnewStateCnt) == (startsAt = 281 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 79, 8, 15, 82, 83, 88, 91, 76, 77, 218, 224, 230, 237, 245, 253, 259, 265, 272, 280, 184, 194, 73, 74, 27, 29, 40, 45, 94, 96, 97, 98, 87, 1, 2, 4, 10, 14, 52, 53, 81, 8, 15, 85, 88, 91, 118, 123, 126, 120, 123, 126, 128, 129, 134, 140, 147, 155, 161, 167, 174, 182, 186, 191, 193, 188, 191, 193, 195, 196, 205, 206, 208, 209, 185, 191, 193, 112, 114, 116, 117, 123, 126, 127, 130, 156, 205, 206, 208, 209, 90, 199, 203 };

    /** Token literal values. */
    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, "\106", "\114", null, "\46", "\173", "\175", "\50", "\51", null, null, null, null, null, null, null, null, null, null, null, null, null, "\116\157\156\145", null, null, null, "\41\76", "\75\76", null, "\41\41", "\57", null, null, null, "\103\154", null, null, null, null, null, null, null, "\41\57\41", null, null, null };

    /** Lexer state names. */
    public static final String[] lexStateNames = { "DEFAULT" };

    static final long[] jjtoToken = { 0xfffdfd8dfff81L };

    static final long[] jjtoSkip = { 0x7eL };

    static final long[] jjtoSpecial = { 0x40L };

    protected SimpleCharStream input_stream;

    private final int[] jjrounds = new int[281];

    private final int[] jjstateSet = new int[562];

    protected char curChar;

    /** Constructor. */
    public ModernRockParserTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    /** Constructor. */
    public ModernRockParserTokenManager(SimpleCharStream stream, int lexState) {
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
        for (i = 281; i-- > 0; ) jjrounds[i] = 0x80000000;
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
            try {
                input_stream.backup(0);
                while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
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
                    matchedToken.specialToken = specialToken;
                    return matchedToken;
                } else {
                    if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        if (specialToken == null) specialToken = matchedToken; else {
                            matchedToken.specialToken = specialToken;
                            specialToken = (specialToken.next = matchedToken);
                        }
                    }
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
