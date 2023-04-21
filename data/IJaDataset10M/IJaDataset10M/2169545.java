package br.jabuti.instrumenter;

public class ASMParseTokenManager implements ASMParseConstants {

    public java.io.PrintStream debugStream = System.out;

    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2, long active3) {
        switch(pos) {
            case 0:
                if ((active0 & 0xfffffff7c0000000L) != 0L || (active1 & 0xffffffffffffffffL) != 0L || (active2 & 0xffffffffffffffffL) != 0L || (active3 & 0x3ffffffL) != 0L) {
                    jjmatchedKind = 221;
                    return 10;
                }
                return -1;
            case 1:
                if ((active0 & 0xfffffff7c0000000L) != 0L || (active1 & 0xffffffffffffffffL) != 0L || (active2 & 0xffffffffffffffffL) != 0L || (active3 & 0x3ffffffL) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 1;
                    return 10;
                }
                return -1;
            case 2:
                if ((active0 & 0x7000000000000000L) != 0L || (active1 & 0x3f000007fc000L) != 0L || (active2 & 0x41c03e0020000000L) != 0L || (active3 & 0xc7c00L) != 0L) return 10;
                if ((active0 & 0x8ffffff7c0000000L) != 0L || (active1 & 0xfffc0fffff803fffL) != 0L || (active2 & 0xbe3fc1ffdfffffffL) != 0L || (active3 & 0x3f383ffL) != 0L) {
                    if (jjmatchedPos != 2) {
                        jjmatchedKind = 221;
                        jjmatchedPos = 2;
                    }
                    return 10;
                }
                return -1;
            case 3:
                if ((active0 & 0x8000002000000000L) != 0L || (active1 & 0x40140c9d008e2e80L) != 0L || (active2 & 0x9a05414340c09f80L) != 0L || (active3 & 0x280c056L) != 0L) return 10;
                if ((active0 & 0xfffffd7c0000000L) != 0L || (active1 & 0xbfe80362ff01917fL) != 0L || (active2 & 0x25ba84bc9f3f607fL) != 0L || (active3 & 0x17b0ba9L) != 0L) {
                    if (jjmatchedPos != 3) {
                        jjmatchedKind = 221;
                        jjmatchedPos = 3;
                    }
                    return 10;
                }
                return -1;
            case 4:
                if ((active0 & 0x1e00c0000000L) != 0L || (active1 & 0x20c00010cL) != 0L || (active2 & 0x4800480000f0000L) != 0L || (active3 & 0x20L) != 0L) return 10;
                if ((active0 & 0xfffe1d700000000L) != 0L || (active1 & 0xbfe80b60f30d9073L) != 0L || (active2 & 0x213a803c9f30607fL) != 0L || (active3 & 0x17b0b89L) != 0L) {
                    if (jjmatchedPos != 4) {
                        jjmatchedKind = 221;
                        jjmatchedPos = 4;
                    }
                    return 10;
                }
                return -1;
            case 5:
                if ((active0 & 0x2bf004400000000L) != 0L || (active1 & 0x3fc80840f1018071L) != 0L || (active2 & 0x138803c00004000L) != 0L || (active3 & 0x580008L) != 0L) return 10;
                if ((active0 & 0xd40fd9300000000L) != 0L || (active1 & 0x80200320020c1002L) != 0L || (active2 & 0x200200009f3e207fL) != 0L || (active3 & 0x1230b81L) != 0L) {
                    if (jjmatchedPos != 5) {
                        jjmatchedKind = 221;
                        jjmatchedPos = 5;
                    }
                    return 10;
                }
                return -1;
            case 6:
                if ((active0 & 0x4405c9100000000L) != 0L || (active1 & 0x200020020c1002L) != 0L || (active2 & 0x20000800e0000L) != 0L || (active3 & 0x200001L) != 0L) return 10;
                if ((active0 & 0x90ea10200000000L) != 0L || (active1 & 0xbf800300e0000060L) != 0L || (active2 & 0x203000381f30207fL) != 0L || (active3 & 0x1030b80L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 6;
                    return 10;
                }
                return -1;
            case 7:
                if ((active0 & 0xe000000000000L) != 0L || (active1 & 0x1f800100e0000060L) != 0L || (active2 & 0x30003800300000L) != 0L || (active3 & 0x10800L) != 0L) return 10;
                if ((active0 & 0x900a10200000000L) != 0L || (active1 & 0xa000020000000000L) != 0L || (active2 & 0x200000001f00207fL) != 0L || (active3 & 0x1020380L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 7;
                    return 10;
                }
                return -1;
            case 8:
                if ((active0 & 0x800200200000000L) != 0L || (active1 & 0xa000020000000000L) != 0L || (active2 & 0x207fL) != 0L || (active3 & 0x20000L) != 0L) return 10;
                if ((active0 & 0x100810000000000L) != 0L || (active2 & 0x200000001f000000L) != 0L || (active3 & 0x1000380L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 8;
                    return 10;
                }
                return -1;
            case 9:
                if ((active0 & 0x100000000000000L) != 0L || (active2 & 0x1000000L) != 0L) return 10;
                if ((active0 & 0x810000000000L) != 0L || (active2 & 0x200000001e000000L) != 0L || (active3 & 0x1000380L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 9;
                    return 10;
                }
                return -1;
            case 10:
                if ((active0 & 0x810000000000L) != 0L || (active3 & 0x1000100L) != 0L) return 10;
                if ((active2 & 0x200000001e000000L) != 0L || (active3 & 0x280L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 10;
                    return 10;
                }
                return -1;
            case 11:
                if ((active2 & 0x2000000008000000L) != 0L || (active3 & 0x80L) != 0L) return 10;
                if ((active2 & 0x16000000L) != 0L || (active3 & 0x200L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 11;
                    return 10;
                }
                return -1;
            case 12:
                if ((active2 & 0x14000000L) != 0L) return 10;
                if ((active2 & 0x2000000L) != 0L || (active3 & 0x200L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 12;
                    return 10;
                }
                return -1;
            case 13:
                if ((active3 & 0x200L) != 0L) return 10;
                if ((active2 & 0x2000000L) != 0L) {
                    jjmatchedKind = 221;
                    jjmatchedPos = 13;
                    return 10;
                }
                return -1;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0, long active1, long active2, long active3) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1, active2, active3), pos + 1);
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
            case 33:
                return jjMoveStringLiteralDfa1_0(0x800000L, 0x0L, 0x0L, 0x0L);
            case 37:
                return jjStopAtPos(0, 28);
            case 40:
                return jjStopAtPos(0, 6);
            case 41:
                return jjStopAtPos(0, 7);
            case 42:
                jjmatchedKind = 26;
                return jjMoveStringLiteralDfa1_0(0x20000000L, 0x0L, 0x0L, 0x0L);
            case 43:
                return jjStopAtPos(0, 24);
            case 44:
                return jjStopAtPos(0, 13);
            case 45:
                return jjStopAtPos(0, 25);
            case 46:
                return jjStopAtPos(0, 14);
            case 47:
                return jjStopAtPos(0, 27);
            case 58:
                return jjStopAtPos(0, 19);
            case 59:
                return jjStopAtPos(0, 12);
            case 60:
                jjmatchedKind = 17;
                return jjMoveStringLiteralDfa1_0(0x800200000L, 0x0L, 0x0L, 0x0L);
            case 61:
                jjmatchedKind = 15;
                return jjMoveStringLiteralDfa1_0(0x100000L, 0x0L, 0x0L, 0x0L);
            case 62:
                jjmatchedKind = 16;
                return jjMoveStringLiteralDfa1_0(0x400000L, 0x0L, 0x0L, 0x0L);
            case 63:
                return jjStopAtPos(0, 18);
            case 91:
                return jjStopAtPos(0, 10);
            case 93:
                return jjStopAtPos(0, 11);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa1_0(0x1fffc000000000L, 0x0L, 0x0L, 0x0L);
            case 66:
            case 98:
                return jjMoveStringLiteralDfa1_0(0x1e0000000000000L, 0x0L, 0x0L, 0x0L);
            case 67:
            case 99:
                return jjMoveStringLiteralDfa1_0(0xe00002000000000L, 0x0L, 0x0L, 0x0L);
            case 68:
            case 100:
                return jjMoveStringLiteralDfa1_0(0xf000001000000000L, 0xfffffL, 0x0L, 0x0L);
            case 70:
            case 102:
                return jjMoveStringLiteralDfa1_0(0x40000000L, 0xfffff00000L, 0x0L, 0x0L);
            case 71:
            case 103:
                return jjMoveStringLiteralDfa1_0(0x0L, 0xf0000000000L, 0x0L, 0x0L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa1_0(0x0L, 0xfffff00000000000L, 0x1ffffffffffL, 0x0L);
            case 74:
            case 106:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x60000000000L, 0x0L);
            case 76:
            case 108:
                return jjMoveStringLiteralDfa1_0(0x80000000L, 0x0L, 0xfffff80000000000L, 0x7fL);
            case 77:
            case 109:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x0L, 0x380L);
            case 78:
            case 110:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x0L, 0x1c00L);
            case 80:
            case 112:
                return jjMoveStringLiteralDfa1_0(0x700000000L, 0x0L, 0x0L, 0x3e000L);
            case 82:
            case 114:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x0L, 0xc0000L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x0L, 0xf00000L);
            case 84:
            case 116:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x0L, 0x1000000L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x0L, 0x2000000L);
            case 123:
                return jjStopAtPos(0, 8);
            case 125:
                return jjStopAtPos(0, 9);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2, long active3) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0, active1, active2, active3);
            return 1;
        }
        switch(curChar) {
            case 42:
                if ((active0 & 0x20000000L) != 0L) return jjStopAtPos(1, 29);
                break;
            case 50:
                return jjMoveStringLiteralDfa2_0(active0, 0x7000000000000000L, active1, 0x3f00000700000L, active2, 0x380000000000L, active3, 0L);
            case 61:
                if ((active0 & 0x100000L) != 0L) return jjStopAtPos(1, 20); else if ((active0 & 0x200000L) != 0L) return jjStopAtPos(1, 21); else if ((active0 & 0x400000L) != 0L) return jjStopAtPos(1, 22); else if ((active0 & 0x800000L) != 0L) return jjStopAtPos(1, 23);
                break;
            case 65:
            case 97:
                return jjMoveStringLiteralDfa2_0(active0, 0x866000e000000000L, active1, 0x3c000003800003L, active2, 0x3c00000000000L, active3, 0x1300000L);
            case 67:
            case 99:
                return jjMoveStringLiteralDfa2_0(active0, 0x10000000000L, active1, 0x3fc00000fc00007cL, active2, 0x3c000000000000L, active3, 0L);
            case 68:
            case 100:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x4000000100000080L, active2, 0x3c0000000000000L, active3, 0L);
            case 69:
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x1000000000L, active1, 0x30000000000L, active2, 0L, active3, 0xc0c00L);
            case 70:
            case 102:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x8000000000000000L, active2, 0x7fffL, active3, 0L);
            case 72:
            case 104:
                return jjMoveStringLiteralDfa2_0(active0, 0x800000000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa2_0(active0, 0x80000840000000L, active1, 0L, active2, 0x8000L, active3, 0x2400000L);
            case 76:
            case 108:
                return jjMoveStringLiteralDfa2_0(active0, 0x1e0000000000L, active1, 0x200000100L, active2, 0x4000000000f0000L, active3, 0L);
            case 77:
            case 109:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x400000200L, active2, 0x800000000700000L, active3, 0L);
            case 78:
            case 110:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000000000L, active1, 0x800000400L, active2, 0x100000001f800000L, active3, 0L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x80000000L, active1, 0xc0000000000L, active2, 0x6000000020000000L, active3, 0x7180L);
            case 82:
            case 114:
                return jjMoveStringLiteralDfa2_0(active0, 0x100c00300000000L, active1, 0x3000001800L, active2, 0x80000000c0000000L, active3, 0x1L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa2_0(active0, 0xf000000000000L, active1, 0xc000002000L, active2, 0x67f00000000L, active3, 0x1eL);
            case 84:
            case 116:
                return jjMoveStringLiteralDfa2_0(active0, 0x10000000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa2_0(active0, 0x400000000L, active1, 0xfc000L, active2, 0x8000000000L, active3, 0x38220L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x800000L);
            case 88:
            case 120:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 0x10000000000L, active3, 0x40L);
            default:
                break;
        }
        return jjStartNfa_0(0, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(0, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(1, active0, active1, active2, active3);
            return 2;
        }
        switch(curChar) {
            case 95:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x8000000000000000L, active2, 0x7fL, active3, 0L);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x800000L);
            case 66:
            case 98:
                if ((active1 & 0x100000000000L) != 0L) return jjStartNfaWithStates_0(2, 108, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x400000000L, active1, 0L, active2, 0L, active3, 0x1000000L);
            case 67:
            case 99:
                if ((active1 & 0x200000000000L) != 0L) return jjStartNfaWithStates_0(2, 109, 10); else if ((active2 & 0x40000000000000L) != 0L) {
                    jjmatchedKind = 182;
                    jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0x80000000L, active1, 0L, active2, 0x180000000000000L, active3, 0L);
            case 68:
            case 100:
                if ((active1 & 0x100000L) != 0L) return jjStartNfaWithStates_0(2, 84, 10); else if ((active1 & 0x400000000000L) != 0L) return jjStartNfaWithStates_0(2, 110, 10); else if ((active2 & 0x80000000000L) != 0L) return jjStartNfaWithStates_0(2, 171, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x8000000000000000L, active1, 0x4000000800000L, active2, 0x400000000000L, active3, 0x2000000L);
            case 69:
            case 101:
                return jjMoveStringLiteralDfa3_0(active0, 0x900600040000000L, active1, 0x3800001c00L, active2, 0x90000000c0800080L, active3, 0x1L);
            case 70:
            case 102:
                if ((active0 & 0x1000000000000000L) != 0L) return jjStartNfaWithStates_0(2, 60, 10); else if ((active1 & 0x800000000000L) != 0L) return jjStartNfaWithStates_0(2, 111, 10); else if ((active2 & 0x100000000000L) != 0L) return jjStartNfaWithStates_0(2, 172, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x1000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 71:
            case 103:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 0x300L, active3, 0L);
            case 72:
            case 104:
                return jjMoveStringLiteralDfa3_0(active0, 0x10000000000000L, active1, 0L, active2, 0x300000000L, active3, 0x6L);
            case 73:
            case 105:
                if ((active0 & 0x2000000000000000L) != 0L) return jjStartNfaWithStates_0(2, 61, 10); else if ((active1 & 0x200000L) != 0L) return jjStartNfaWithStates_0(2, 85, 10); else if ((active2 & 0x200000000000L) != 0L) return jjStartNfaWithStates_0(2, 173, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x100000000L, active1, 0x4000000100000080L, active2, 0x200000000000000L, active3, 0L);
            case 76:
            case 108:
                if ((active0 & 0x4000000000000000L) != 0L) return jjStartNfaWithStates_0(2, 62, 10); else if ((active1 & 0x400000L) != 0L) return jjStartNfaWithStates_0(2, 86, 10); else if ((active1 & 0x1000000000000L) != 0L) return jjStartNfaWithStates_0(2, 112, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x220004000000000L, active1, 0x8000001000001L, active2, 0x800000000c00L, active3, 0x100200L);
            case 77:
            case 109:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0xc00000cL, active2, 0x4000000000000L, active3, 0L);
            case 78:
            case 110:
                return jjMoveStringLiteralDfa3_0(active0, 0x800000000L, active1, 0x10000000000000L, active2, 0x100000000f000L, active3, 0x180L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa3_0(active0, 0x1f0200000000L, active1, 0x3fc00002f0000170L, active2, 0x24380100000f0000L, active3, 0x40L);
            case 80:
            case 112:
                if ((active1 & 0x4000L) != 0L) {
                    jjmatchedKind = 78;
                    jjmatchedPos = 2;
                } else if ((active3 & 0x1000L) != 0L) return jjStartNfaWithStates_0(2, 204, 10); else if ((active3 & 0x2000L) != 0L) {
                    jjmatchedKind = 205;
                    jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0x80000000000000L, active1, 0xf8000L, active2, 0x300000L, active3, 0x404000L);
            case 82:
            case 114:
                if ((active2 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(2, 157, 10); else if ((active2 & 0x20000000000L) != 0L) {
                    jjmatchedKind = 169;
                    jjmatchedPos = 2;
                } else if ((active2 & 0x4000000000000000L) != 0L) return jjStartNfaWithStates_0(2, 190, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x800000000000L, active1, 0L, active2, 0x40000000000L, active3, 0L);
            case 83:
            case 115:
                if ((active1 & 0x2000000000000L) != 0L) return jjStartNfaWithStates_0(2, 113, 10);
                return jjMoveStringLiteralDfa3_0(active0, 0x44000a000000000L, active1, 0x20000002000002L, active2, 0x2008001000000L, active3, 0x208020L);
            case 84:
            case 116:
                if ((active3 & 0x40000L) != 0L) {
                    jjmatchedKind = 210;
                    jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0xf000000000000L, active1, 0xf4000000000L, active2, 0x3c00000000L, active3, 0xb0008L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x8400002200L, active2, 0x800004000400000L, active3, 0x10L);
            case 86:
            case 118:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 0x1e000000L, active3, 0L);
            case 87:
            case 119:
                if ((active3 & 0x400L) != 0L) {
                    jjmatchedKind = 202;
                    jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x800L);
            default:
                break;
        }
        return jjStartNfa_0(1, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(1, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(2, active0, active1, active2, active3);
            return 3;
        }
        switch(curChar) {
            case 50:
                if ((active1 & 0x20000L) != 0L) {
                    jjmatchedKind = 81;
                    jjmatchedPos = 3;
                } else if ((active3 & 0x4000L) != 0L) return jjStartNfaWithStates_0(3, 206, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0xc0000L, active2, 0x100000000000000L, active3, 0L);
            case 95:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x18000L, active2, 0x80040000000000L, active3, 0L);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa4_0(active0, 0x1009e1080000000L, active1, 0x8000000200000100L, active2, 0x4000000000f0001L, active3, 0x800L);
            case 66:
            case 98:
                if ((active1 & 0x2000L) != 0L) return jjStartNfaWithStates_0(3, 77, 10); else if ((active1 & 0x8000000000L) != 0L) return jjStartNfaWithStates_0(3, 103, 10); else if ((active2 & 0x4000000000L) != 0L) return jjStartNfaWithStates_0(3, 166, 10); else if ((active3 & 0x10L) != 0L) return jjStartNfaWithStates_0(3, 196, 10);
                break;
            case 67:
            case 99:
                if ((active2 & 0x8000L) != 0L) return jjStartNfaWithStates_0(3, 143, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0x800000000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 68:
            case 100:
                if ((active0 & 0x8000000000000000L) != 0L) return jjStartNfaWithStates_0(3, 63, 10); else if ((active1 & 0x800000L) != 0L) return jjStartNfaWithStates_0(3, 87, 10); else if ((active1 & 0x4000000000000L) != 0L) return jjStartNfaWithStates_0(3, 114, 10); else if ((active1 & 0x10000000000000L) != 0L) return jjStartNfaWithStates_0(3, 116, 10); else if ((active2 & 0x400000000000L) != 0L) return jjStartNfaWithStates_0(3, 174, 10); else if ((active2 & 0x1000000000000L) != 0L) return jjStartNfaWithStates_0(3, 176, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0L, active2, 0x300000L, active3, 0L);
            case 69:
            case 101:
                if ((active0 & 0x2000000000L) != 0L) return jjStartNfaWithStates_0(3, 37, 10); else if ((active2 & 0x100L) != 0L) return jjStartNfaWithStates_0(3, 136, 10); else if ((active2 & 0x400L) != 0L) return jjStartNfaWithStates_0(3, 138, 10); else if ((active2 & 0x1000L) != 0L) return jjStartNfaWithStates_0(3, 140, 10); else if ((active3 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(3, 217, 10);
                break;
            case 70:
            case 102:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x10000000000L, active2, 0L, active3, 0x10000L);
            case 71:
            case 103:
                if ((active1 & 0x400L) != 0L) return jjStartNfaWithStates_0(3, 74, 10); else if ((active1 & 0x800000000L) != 0L) return jjStartNfaWithStates_0(3, 99, 10); else if ((active2 & 0x800000L) != 0L) return jjStartNfaWithStates_0(3, 151, 10); else if ((active2 & 0x1000000000000000L) != 0L) return jjStartNfaWithStates_0(3, 188, 10);
                break;
            case 72:
            case 104:
                if ((active3 & 0x8000L) != 0L) return jjStartNfaWithStates_0(3, 207, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0L, active2, 0x8000000000L, active3, 0x20L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa4_0(active0, 0x800000000L, active1, 0L, active2, 0x7eL, active3, 0x180L);
            case 75:
            case 107:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0L, active2, 0x2000000000000000L, active3, 0L);
            case 76:
            case 108:
                if ((active1 & 0x200L) != 0L) return jjStartNfaWithStates_0(3, 73, 10); else if ((active1 & 0x400000000L) != 0L) return jjStartNfaWithStates_0(3, 98, 10); else if ((active2 & 0x400000L) != 0L) return jjStartNfaWithStates_0(3, 150, 10); else if ((active2 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(3, 160, 10); else if ((active2 & 0x800000000000000L) != 0L) return jjStartNfaWithStates_0(3, 187, 10); else if ((active3 & 0x2L) != 0L) return jjStartNfaWithStates_0(3, 193, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0x440000000L, active1, 0L, active2, 0L, active3, 0x1000000L);
            case 77:
            case 109:
                if ((active1 & 0x800L) != 0L) return jjStartNfaWithStates_0(3, 75, 10); else if ((active1 & 0x1000000000L) != 0L) return jjStartNfaWithStates_0(3, 100, 10); else if ((active2 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(3, 158, 10); else if ((active2 & 0x8000000000000000L) != 0L) return jjStartNfaWithStates_0(3, 191, 10);
                break;
            case 78:
            case 110:
                return jjMoveStringLiteralDfa4_0(active0, 0x10000000000L, active1, 0x3fc00000f0000070L, active2, 0x38000000000000L, active3, 0L);
            case 79:
            case 111:
                if ((active1 & 0x40000000000L) != 0L) {
                    jjmatchedKind = 106;
                    jjmatchedPos = 3;
                }
                return jjMoveStringLiteralDfa4_0(active0, 0x22f004000000000L, active1, 0x8084001000001L, active2, 0x803c1e002000L, active3, 0x100008L);
            case 80:
            case 112:
                if ((active2 & 0x4000000000000L) != 0L) return jjStartNfaWithStates_0(3, 178, 10); else if ((active3 & 0x800000L) != 0L) return jjStartNfaWithStates_0(3, 215, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0xc00000cL, active2, 0L, active3, 0L);
            case 81:
            case 113:
                if ((active2 & 0x80L) != 0L) return jjStartNfaWithStates_0(3, 135, 10);
                break;
            case 82:
            case 114:
                if ((active2 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(3, 161, 10); else if ((active2 & 0x10000000000L) != 0L) return jjStartNfaWithStates_0(3, 168, 10); else if ((active3 & 0x4L) != 0L) return jjStartNfaWithStates_0(3, 194, 10); else if ((active3 & 0x40L) != 0L) return jjStartNfaWithStates_0(3, 198, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0x10000000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x20000000000L, active2, 0L, active3, 0x20000L);
            case 84:
            case 116:
                if ((active2 & 0x200L) != 0L) return jjStartNfaWithStates_0(3, 137, 10); else if ((active2 & 0x800L) != 0L) return jjStartNfaWithStates_0(3, 139, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0x440408200000000L, active1, 0x20002002001002L, active2, 0x2000081000000L, active3, 0x200201L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa4_0(active0, 0x80000000000000L, active1, 0L, active2, 0x4000L, active3, 0x480000L);
            case 86:
            case 118:
                if ((active1 & 0x80L) != 0L) return jjStartNfaWithStates_0(3, 71, 10); else if ((active1 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(3, 96, 10); else if ((active1 & 0x4000000000000000L) != 0L) return jjStartNfaWithStates_0(3, 126, 10); else if ((active2 & 0x200000000000000L) != 0L) return jjStartNfaWithStates_0(3, 185, 10);
                return jjMoveStringLiteralDfa4_0(active0, 0x100000000L, active1, 0L, active2, 0L, active3, 0L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa4_0(active0, 0x200000000000L, active1, 0L, active2, 0L, active3, 0L);
            default:
                break;
        }
        return jjStartNfa_0(2, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(2, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(3, active0, active1, active2, active3);
            return 4;
        }
        switch(curChar) {
            case 95:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x800000c0000L, active2, 0x100000000000000L, active3, 0L);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa5_0(active0, 0x220204100000000L, active1, 0x8000001000001L, active2, 0x800001000000L, active3, 0x100000L);
            case 67:
            case 99:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x8000000000000000L, active2, 0x7fL, active3, 0L);
            case 68:
            case 100:
                if ((active0 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(4, 30, 10); else if ((active0 & 0x20000000000L) != 0L) {
                    jjmatchedKind = 41;
                    jjmatchedPos = 4;
                } else if ((active1 & 0x100L) != 0L) return jjStartNfaWithStates_0(4, 72, 10); else if ((active1 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(4, 97, 10); else if ((active2 & 0x10000L) != 0L) {
                    jjmatchedKind = 144;
                    jjmatchedPos = 4;
                } else if ((active2 & 0x400000000000000L) != 0L) return jjStartNfaWithStates_0(4, 186, 10);
                return jjMoveStringLiteralDfa5_0(active0, 0x1c0000000000L, active1, 0L, active2, 0xe0000L, active3, 0L);
            case 69:
            case 101:
                return jjMoveStringLiteralDfa5_0(active0, 0x200000000L, active1, 0L, active2, 0x300000L, active3, 0x1000000L);
            case 71:
            case 103:
                if ((active1 & 0x4L) != 0L) return jjStartNfaWithStates_0(4, 66, 10); else if ((active1 & 0x4000000L) != 0L) return jjStartNfaWithStates_0(4, 90, 10);
                break;
            case 73:
            case 105:
                return jjMoveStringLiteralDfa5_0(active0, 0x400000000L, active1, 0x10000000000L, active2, 0L, active3, 0x10200L);
            case 75:
            case 107:
                return jjMoveStringLiteralDfa5_0(active0, 0x900000000000000L, active1, 0L, active2, 0x1e000000L, active3, 0L);
            case 76:
            case 108:
                if ((active0 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(4, 31, 10); else if ((active1 & 0x8L) != 0L) return jjStartNfaWithStates_0(4, 67, 10); else if ((active1 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(4, 91, 10);
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0L, active2, 0x4000L, active3, 0L);
            case 78:
            case 110:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0L, active2, 0x2000L, active3, 0L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa5_0(active0, 0x450008000000000L, active1, 0x20000002000002L, active2, 0x2000000000000L, active3, 0x200000L);
            case 82:
            case 114:
                if ((active2 & 0x8000000000L) != 0L) return jjStartNfaWithStates_0(4, 167, 10); else if ((active3 & 0x20L) != 0L) return jjStartNfaWithStates_0(4, 197, 10);
                return jjMoveStringLiteralDfa5_0(active0, 0xf000000000000L, active1, 0x4000000000L, active2, 0x3c00000000L, active3, 0x80808L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa5_0(active0, 0x80010000000000L, active1, 0x3fc00000f0000070L, active2, 0x38000000000000L, active3, 0x400000L);
            case 84:
            case 116:
                return jjMoveStringLiteralDfa5_0(active0, 0x800000000L, active1, 0x20000000000L, active2, 0L, active3, 0x20180L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa5_0(active0, 0x401000000000L, active1, 0x2000001000L, active2, 0x2000000080000000L, active3, 0x1L);
            case 87:
            case 119:
                if ((active2 & 0x40000000000L) != 0L) return jjStartNfaWithStates_0(4, 170, 10); else if ((active2 & 0x80000000000000L) != 0L) return jjStartNfaWithStates_0(4, 183, 10);
                break;
            case 88:
            case 120:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x18000L, active2, 0L, active3, 0L);
            case 89:
            case 121:
                return jjMoveStringLiteralDfa5_0(active0, 0x800000000000L, active1, 0L, active2, 0L, active3, 0L);
            default:
                break;
        }
        return jjStartNfa_0(3, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(3, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(4, active0, active1, active2, active3);
            return 5;
        }
        switch(curChar) {
            case 49:
                if ((active1 & 0x8000L) != 0L) return jjStartNfaWithStates_0(5, 79, 10);
                break;
            case 50:
                if ((active1 & 0x10000L) != 0L) return jjStartNfaWithStates_0(5, 80, 10);
                break;
            case 62:
                if ((active0 & 0x800000000L) != 0L) return jjStopAtPos(5, 35);
                break;
            case 95:
                return jjMoveStringLiteralDfa6_0(active0, 0x1c0000000000L, active1, 0L, active2, 0xe0000L, active3, 0L);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x20000000000L, active2, 0L, active3, 0x20200L);
            case 67:
            case 99:
                if ((active0 & 0x400000000L) != 0L) return jjStartNfaWithStates_0(5, 34, 10);
                return jjMoveStringLiteralDfa6_0(active0, 0x800000200000000L, active1, 0L, active2, 0L, active3, 0L);
            case 68:
            case 100:
                if ((active0 & 0x4000000000L) != 0L) return jjStartNfaWithStates_0(5, 38, 10); else if ((active0 & 0x20000000000000L) != 0L) return jjStartNfaWithStates_0(5, 53, 10); else if ((active0 & 0x200000000000000L) != 0L) return jjStartNfaWithStates_0(5, 57, 10); else if ((active1 & 0x1L) != 0L) return jjStartNfaWithStates_0(5, 64, 10); else if ((active1 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(5, 88, 10); else if ((active1 & 0x8000000000000L) != 0L) return jjStartNfaWithStates_0(5, 115, 10); else if ((active2 & 0x800000000000L) != 0L) return jjStartNfaWithStates_0(5, 175, 10); else if ((active3 & 0x100000L) != 0L) return jjStartNfaWithStates_0(5, 212, 10);
                break;
            case 69:
            case 101:
                if ((active0 & 0x1000000000000L) != 0L) {
                    jjmatchedKind = 48;
                    jjmatchedPos = 5;
                } else if ((active1 & 0x4000000000L) != 0L) return jjStartNfaWithStates_0(5, 102, 10); else if ((active2 & 0x400000000L) != 0L) {
                    jjmatchedKind = 162;
                    jjmatchedPos = 5;
                } else if ((active3 & 0x8L) != 0L) return jjStartNfaWithStates_0(5, 195, 10);
                return jjMoveStringLiteralDfa6_0(active0, 0xe000000000000L, active1, 0x10000000000L, active2, 0x381e000000L, active3, 0x10000L);
            case 72:
            case 104:
                if ((active0 & 0x80000000000000L) != 0L) return jjStartNfaWithStates_0(5, 55, 10); else if ((active3 & 0x400000L) != 0L) return jjStartNfaWithStates_0(5, 214, 10);
                break;
            case 76:
            case 108:
                if ((active2 & 0x4000L) != 0L) return jjStartNfaWithStates_0(5, 142, 10);
                return jjMoveStringLiteralDfa6_0(active0, 0x801000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 77:
            case 109:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x8000000000000000L, active2, 0x7fL, active3, 0L);
            case 78:
            case 110:
                if ((active3 & 0x80000L) != 0L) return jjStartNfaWithStates_0(5, 211, 10);
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0L, active2, 0x1002000L, active3, 0L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x180L);
            case 80:
            case 112:
                return jjMoveStringLiteralDfa6_0(active0, 0x100000000000000L, active1, 0L, active2, 0x2000000000300000L, active3, 0L);
            case 82:
            case 114:
                return jjMoveStringLiteralDfa6_0(active0, 0x440608000000000L, active1, 0x20002002001002L, active2, 0x2000080000000L, active3, 0x200801L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x1000000L);
            case 84:
            case 116:
                if ((active1 & 0x10L) != 0L) {
                    jjmatchedKind = 68;
                    jjmatchedPos = 5;
                } else if ((active1 & 0x10000000L) != 0L) {
                    jjmatchedKind = 92;
                    jjmatchedPos = 5;
                } else if ((active1 & 0x40000000000000L) != 0L) {
                    jjmatchedKind = 118;
                    jjmatchedPos = 5;
                } else if ((active2 & 0x8000000000000L) != 0L) {
                    jjmatchedKind = 179;
                    jjmatchedPos = 5;
                }
                return jjMoveStringLiteralDfa6_0(active0, 0x10100000000L, active1, 0x3f800000e0000060L, active2, 0x30000000000000L, active3, 0L);
            case 87:
            case 119:
                if ((active0 & 0x10000000000000L) != 0L) return jjStartNfaWithStates_0(5, 52, 10); else if ((active1 & 0x80000000000L) != 0L) return jjStartNfaWithStates_0(5, 107, 10); else if ((active2 & 0x100000000000000L) != 0L) return jjStartNfaWithStates_0(5, 184, 10);
                break;
            case 88:
            case 120:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0xc0000L, active2, 0L, active3, 0L);
            default:
                break;
        }
        return jjStartNfa_0(4, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(4, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(5, active0, active1, active2, active3);
            return 6;
        }
        switch(curChar) {
            case 48:
                if ((active0 & 0x40000000000L) != 0L) return jjStartNfaWithStates_0(6, 42, 10); else if ((active2 & 0x20000L) != 0L) return jjStartNfaWithStates_0(6, 145, 10);
                break;
            case 49:
                if ((active0 & 0x80000000000L) != 0L) return jjStartNfaWithStates_0(6, 43, 10); else if ((active1 & 0x40000L) != 0L) return jjStartNfaWithStates_0(6, 82, 10); else if ((active2 & 0x40000L) != 0L) return jjStartNfaWithStates_0(6, 146, 10);
                break;
            case 50:
                if ((active0 & 0x100000000000L) != 0L) return jjStartNfaWithStates_0(6, 44, 10); else if ((active1 & 0x80000L) != 0L) return jjStartNfaWithStates_0(6, 83, 10); else if ((active2 & 0x80000L) != 0L) return jjStartNfaWithStates_0(6, 147, 10);
                break;
            case 95:
                return jjMoveStringLiteralDfa7_0(active0, 0xe010000000000L, active1, 0x3f800000e0000060L, active2, 0x30003800300000L, active3, 0L);
            case 65:
            case 97:
                return jjMoveStringLiteralDfa7_0(active0, 0x800000000000000L, active1, 0L, active2, 0L, active3, 0x800L);
            case 67:
            case 99:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0x1000000L, active3, 0L);
            case 69:
            case 101:
                if ((active0 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(6, 32, 10); else if ((active0 & 0x8000000000L) != 0L) return jjStartNfaWithStates_0(6, 39, 10); else if ((active0 & 0x40000000000000L) != 0L) return jjStartNfaWithStates_0(6, 54, 10); else if ((active0 & 0x400000000000000L) != 0L) return jjStartNfaWithStates_0(6, 58, 10); else if ((active1 & 0x2L) != 0L) return jjStartNfaWithStates_0(6, 65, 10); else if ((active1 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(6, 89, 10); else if ((active1 & 0x20000000000000L) != 0L) return jjStartNfaWithStates_0(6, 117, 10); else if ((active2 & 0x2000000000000L) != 0L) return jjStartNfaWithStates_0(6, 177, 10); else if ((active3 & 0x200000L) != 0L) return jjStartNfaWithStates_0(6, 213, 10);
                return jjMoveStringLiteralDfa7_0(active0, 0x800000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0x2000000L, active3, 0L);
            case 76:
            case 108:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x10000000000L, active2, 0L, active3, 0x10000L);
            case 78:
            case 110:
                if ((active0 & 0x400000000000L) != 0L) return jjStartNfaWithStates_0(6, 46, 10); else if ((active1 & 0x1000L) != 0L) return jjStartNfaWithStates_0(6, 76, 10); else if ((active1 & 0x2000000000L) != 0L) return jjStartNfaWithStates_0(6, 101, 10); else if ((active2 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(6, 159, 10); else if ((active3 & 0x1L) != 0L) return jjStartNfaWithStates_0(6, 192, 10);
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x200L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa7_0(active0, 0x100000000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 80:
            case 112:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x8000000000000000L, active2, 0x7fL, active3, 0L);
            case 82:
            case 114:
                return jjMoveStringLiteralDfa7_0(active0, 0x200000000000L, active1, 0L, active2, 0L, active3, 0x180L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0x200000000c000000L, active3, 0L);
            case 84:
            case 116:
                if ((active0 & 0x1000000000L) != 0L) return jjStartNfaWithStates_0(6, 36, 10);
                return jjMoveStringLiteralDfa7_0(active0, 0x200000000L, active1, 0x20000000000L, active2, 0L, active3, 0x20000L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0x2000L, active3, 0L);
            case 86:
            case 118:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0x10000000L, active3, 0L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x1000000L);
            default:
                break;
        }
        return jjStartNfa_0(5, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(5, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(6, active0, active1, active2, active3);
            return 7;
        }
        switch(curChar) {
            case 48:
                if ((active0 & 0x2000000000000L) != 0L) return jjStartNfaWithStates_0(7, 49, 10); else if ((active1 & 0x20L) != 0L) return jjStartNfaWithStates_0(7, 69, 10); else if ((active1 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(7, 93, 10); else if ((active1 & 0x80000000000000L) != 0L) return jjStartNfaWithStates_0(7, 119, 10); else if ((active2 & 0x800000000L) != 0L) return jjStartNfaWithStates_0(7, 163, 10); else if ((active2 & 0x10000000000000L) != 0L) return jjStartNfaWithStates_0(7, 180, 10);
                break;
            case 49:
                if ((active0 & 0x4000000000000L) != 0L) return jjStartNfaWithStates_0(7, 50, 10); else if ((active1 & 0x40L) != 0L) return jjStartNfaWithStates_0(7, 70, 10); else if ((active1 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(7, 94, 10); else if ((active1 & 0x100000000000000L) != 0L) return jjStartNfaWithStates_0(7, 120, 10); else if ((active2 & 0x100000L) != 0L) return jjStartNfaWithStates_0(7, 148, 10); else if ((active2 & 0x1000000000L) != 0L) return jjStartNfaWithStates_0(7, 164, 10); else if ((active2 & 0x20000000000000L) != 0L) return jjStartNfaWithStates_0(7, 181, 10);
                break;
            case 50:
                if ((active0 & 0x8000000000000L) != 0L) return jjStartNfaWithStates_0(7, 51, 10); else if ((active1 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(7, 95, 10); else if ((active1 & 0x200000000000000L) != 0L) return jjStartNfaWithStates_0(7, 121, 10); else if ((active2 & 0x200000L) != 0L) return jjStartNfaWithStates_0(7, 149, 10); else if ((active2 & 0x2000000000L) != 0L) return jjStartNfaWithStates_0(7, 165, 10);
                break;
            case 51:
                if ((active1 & 0x400000000000000L) != 0L) return jjStartNfaWithStates_0(7, 122, 10);
                break;
            case 52:
                if ((active1 & 0x800000000000000L) != 0L) return jjStartNfaWithStates_0(7, 123, 10);
                break;
            case 53:
                if ((active1 & 0x1000000000000000L) != 0L) return jjStartNfaWithStates_0(7, 124, 10);
                break;
            case 65:
            case 97:
                return jjMoveStringLiteralDfa8_0(active0, 0x200000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 68:
            case 100:
                if ((active1 & 0x10000000000L) != 0L) return jjStartNfaWithStates_0(7, 104, 10); else if ((active3 & 0x10000L) != 0L) return jjStartNfaWithStates_0(7, 208, 10);
                break;
            case 69:
            case 101:
                return jjMoveStringLiteralDfa8_0(active0, 0x200000000L, active1, 0x8000000000000000L, active2, 0x1000002L, active3, 0x380L);
            case 71:
            case 103:
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0L, active2, 0xcL, active3, 0L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa8_0(active0, 0x100000000000000L, active1, 0x20000000000L, active2, 0x10000000L, active3, 0x1020000L);
            case 76:
            case 108:
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0L, active2, 0x2030L, active3, 0L);
            case 77:
            case 109:
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x2000000000000000L, active2, 0L, active3, 0L);
            case 78:
            case 110:
                return jjMoveStringLiteralDfa8_0(active0, 0x810000000000L, active1, 0L, active2, 0x2000041L, active3, 0L);
            case 80:
            case 112:
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0L, active2, 0x4000000L, active3, 0L);
            case 83:
            case 115:
                return jjMoveStringLiteralDfa8_0(active0, 0x800000000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 84:
            case 116:
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0L, active2, 0x8000000L, active3, 0L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0L, active2, 0x2000000000000000L, active3, 0L);
            case 89:
            case 121:
                if ((active3 & 0x800L) != 0L) return jjStartNfaWithStates_0(7, 203, 10);
                break;
            default:
                break;
        }
        return jjStartNfa_0(6, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(6, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(7, active0, active1, active2, active3);
            return 8;
        }
        switch(curChar) {
            case 49:
                if ((active1 & 0x2000000000000000L) != 0L) return jjStartNfaWithStates_0(8, 125, 10);
                break;
            case 65:
            case 97:
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0x8000000L, active3, 0L);
            case 67:
            case 99:
                if ((active1 & 0x20000000000L) != 0L) return jjStartNfaWithStates_0(8, 105, 10); else if ((active3 & 0x20000L) != 0L) return jjStartNfaWithStates_0(8, 209, 10);
                break;
            case 68:
            case 100:
                if ((active0 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(8, 33, 10);
                break;
            case 69:
            case 101:
                if ((active2 & 0x1L) != 0L) return jjStartNfaWithStates_0(8, 128, 10); else if ((active2 & 0x4L) != 0L) return jjStartNfaWithStates_0(8, 130, 10); else if ((active2 & 0x10L) != 0L) return jjStartNfaWithStates_0(8, 132, 10); else if ((active2 & 0x40L) != 0L) return jjStartNfaWithStates_0(8, 134, 10);
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0x4000000L, active3, 0L);
            case 71:
            case 103:
                return jjMoveStringLiteralDfa9_0(active0, 0x800000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 73:
            case 105:
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0x2000000000000000L, active3, 0L);
            case 76:
            case 108:
                if ((active2 & 0x2000L) != 0L) return jjStartNfaWithStates_0(8, 141, 10);
                break;
            case 78:
            case 110:
                return jjMoveStringLiteralDfa9_0(active0, 0x100000000000000L, active1, 0L, active2, 0L, active3, 0x80L);
            case 79:
            case 111:
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0x1000000L, active3, 0L);
            case 81:
            case 113:
                if ((active1 & 0x8000000000000000L) != 0L) return jjStartNfaWithStates_0(8, 127, 10); else if ((active2 & 0x2L) != 0L) return jjStartNfaWithStates_0(8, 129, 10);
                break;
            case 82:
            case 114:
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0x10000000L, active3, 0L);
            case 84:
            case 116:
                if ((active0 & 0x800000000000000L) != 0L) return jjStartNfaWithStates_0(8, 59, 10); else if ((active2 & 0x8L) != 0L) return jjStartNfaWithStates_0(8, 131, 10); else if ((active2 & 0x20L) != 0L) return jjStartNfaWithStates_0(8, 133, 10);
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0x2000000L, active3, 0x1000000L);
            case 85:
            case 117:
                return jjMoveStringLiteralDfa9_0(active0, 0x10000000000L, active1, 0L, active2, 0L, active3, 0L);
            case 87:
            case 119:
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x200L);
            case 88:
            case 120:
                return jjMoveStringLiteralDfa9_0(active0, 0L, active1, 0L, active2, 0L, active3, 0x100L);
            case 89:
            case 121:
                if ((active0 & 0x200000000000L) != 0L) return jjStartNfaWithStates_0(8, 45, 10);
                break;
            default:
                break;
        }
        return jjStartNfa_0(7, active0, active1, active2, active3);
    }

    private final int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(7, old0, old1, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(8, active0, 0L, active2, active3);
            return 9;
        }
        switch(curChar) {
            case 65:
            case 97:
                return jjMoveStringLiteralDfa10_0(active0, 0L, active2, 0L, active3, 0x200L);
            case 67:
            case 99:
                return jjMoveStringLiteralDfa10_0(active0, 0L, active2, 0x4000000L, active3, 0x1000000L);
            case 69:
            case 101:
                return jjMoveStringLiteralDfa10_0(active0, 0L, active2, 0x2000000L, active3, 0L);
            case 70:
            case 102:
                if ((active2 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(9, 152, 10);
                break;
            case 73:
            case 105:
                return jjMoveStringLiteralDfa10_0(active0, 0L, active2, 0L, active3, 0x100L);
            case 76:
            case 108:
                return jjMoveStringLiteralDfa10_0(active0, 0x10000000000L, active2, 0L, active3, 0L);
            case 84:
            case 116:
                if ((active0 & 0x100000000000000L) != 0L) return jjStartNfaWithStates_0(9, 56, 10);
                return jjMoveStringLiteralDfa10_0(active0, 0x800000000000L, active2, 0x2000000018000000L, active3, 0x80L);
            default:
                break;
        }
        return jjStartNfa_0(8, active0, 0L, active2, active3);
    }

    private final int jjMoveStringLiteralDfa10_0(long old0, long active0, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(8, old0, 0L, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(9, active0, 0L, active2, active3);
            return 10;
        }
        switch(curChar) {
            case 67:
            case 99:
                return jjMoveStringLiteralDfa11_0(active0, 0L, active2, 0x2000000000000000L, active3, 0L);
            case 69:
            case 101:
                return jjMoveStringLiteralDfa11_0(active0, 0L, active2, 0L, active3, 0x80L);
            case 72:
            case 104:
                if ((active0 & 0x800000000000L) != 0L) return jjStartNfaWithStates_0(10, 47, 10); else if ((active3 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(10, 216, 10);
                break;
            case 73:
            case 105:
                return jjMoveStringLiteralDfa11_0(active0, 0L, active2, 0xc000000L, active3, 0L);
            case 76:
            case 108:
                if ((active0 & 0x10000000000L) != 0L) return jjStartNfaWithStates_0(10, 40, 10);
                break;
            case 82:
            case 114:
                return jjMoveStringLiteralDfa11_0(active0, 0L, active2, 0x2000000L, active3, 0x200L);
            case 84:
            case 116:
                if ((active3 & 0x100L) != 0L) return jjStartNfaWithStates_0(10, 200, 10);
                break;
            case 85:
            case 117:
                return jjMoveStringLiteralDfa11_0(active0, 0L, active2, 0x10000000L, active3, 0L);
            default:
                break;
        }
        return jjStartNfa_0(9, active0, 0L, active2, active3);
    }

    private final int jjMoveStringLiteralDfa11_0(long old0, long active0, long old2, long active2, long old3, long active3) {
        if (((active0 &= old0) | (active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(9, old0, 0L, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(10, 0L, 0L, active2, active3);
            return 11;
        }
        switch(curChar) {
            case 65:
            case 97:
                return jjMoveStringLiteralDfa12_0(active2, 0x14000000L, active3, 0L);
            case 67:
            case 99:
                if ((active2 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(11, 155, 10);
                break;
            case 70:
            case 102:
                return jjMoveStringLiteralDfa12_0(active2, 0x2000000L, active3, 0L);
            case 72:
            case 104:
                if ((active2 & 0x2000000000000000L) != 0L) return jjStartNfaWithStates_0(11, 189, 10);
                break;
            case 82:
            case 114:
                if ((active3 & 0x80L) != 0L) return jjStartNfaWithStates_0(11, 199, 10);
                return jjMoveStringLiteralDfa12_0(active2, 0L, active3, 0x200L);
            default:
                break;
        }
        return jjStartNfa_0(10, 0L, 0L, active2, active3);
    }

    private final int jjMoveStringLiteralDfa12_0(long old2, long active2, long old3, long active3) {
        if (((active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(10, 0L, 0L, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(11, 0L, 0L, active2, active3);
            return 12;
        }
        switch(curChar) {
            case 65:
            case 97:
                return jjMoveStringLiteralDfa13_0(active2, 0x2000000L, active3, 0x200L);
            case 76:
            case 108:
                if ((active2 & 0x4000000L) != 0L) return jjStartNfaWithStates_0(12, 154, 10); else if ((active2 & 0x10000000L) != 0L) return jjStartNfaWithStates_0(12, 156, 10);
                break;
            default:
                break;
        }
        return jjStartNfa_0(11, 0L, 0L, active2, active3);
    }

    private final int jjMoveStringLiteralDfa13_0(long old2, long active2, long old3, long active3) {
        if (((active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(11, 0L, 0L, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(12, 0L, 0L, active2, active3);
            return 13;
        }
        switch(curChar) {
            case 67:
            case 99:
                return jjMoveStringLiteralDfa14_0(active2, 0x2000000L, active3, 0L);
            case 89:
            case 121:
                if ((active3 & 0x200L) != 0L) return jjStartNfaWithStates_0(13, 201, 10);
                break;
            default:
                break;
        }
        return jjStartNfa_0(12, 0L, 0L, active2, active3);
    }

    private final int jjMoveStringLiteralDfa14_0(long old2, long active2, long old3, long active3) {
        if (((active2 &= old2) | (active3 &= old3)) == 0L) return jjStartNfa_0(12, 0L, 0L, old2, old3);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(13, 0L, 0L, active2, 0L);
            return 14;
        }
        switch(curChar) {
            case 69:
            case 101:
                if ((active2 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(14, 153, 10);
                break;
            default:
                break;
        }
        return jjStartNfa_0(13, 0L, 0L, active2, 0L);
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

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private final int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 18;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 219) kind = 219;
                                jjCheckNAddStates(0, 2);
                            } else if (curChar == 35) jjCheckNAdd(12); else if (curChar == 34) jjCheckNAddStates(3, 5);
                            break;
                        case 1:
                            if ((0xfffffffbffffdbffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 3:
                            if ((0x8400000000L & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 4:
                            if (curChar == 34 && kind > 220) kind = 220;
                            break;
                        case 5:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAddStates(6, 9);
                            break;
                        case 6:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 7:
                            if ((0xf000000000000L & l) != 0L) jjstateSet[jjnewStateCnt++] = 8;
                            break;
                        case 8:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAdd(6);
                            break;
                        case 10:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 221) kind = 221;
                            jjstateSet[jjnewStateCnt++] = 10;
                            break;
                        case 11:
                            if (curChar == 35) jjCheckNAdd(12);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 224) kind = 224;
                            jjCheckNAdd(12);
                            break;
                        case 13:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 219) kind = 219;
                            jjCheckNAddStates(0, 2);
                            break;
                        case 14:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(14, 15);
                            break;
                        case 15:
                            if (curChar == 46) jjCheckNAdd(16);
                            break;
                        case 16:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 218) kind = 218;
                            jjCheckNAdd(16);
                            break;
                        case 17:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 219) kind = 219;
                            jjCheckNAdd(17);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                        case 10:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 221) kind = 221;
                            jjCheckNAdd(10);
                            break;
                        case 1:
                            if ((0xffffffffefffffffL & l) != 0L) jjCheckNAddStates(3, 5);
                            break;
                        case 2:
                            if (curChar == 92) jjAddStates(10, 12);
                            break;
                        case 3:
                            if ((0x14404410144044L & l) != 0L) jjCheckNAddStates(3, 5);
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
                        case 1:
                            if ((jjbitVec0[i2] & l2) != 0L) jjAddStates(3, 5);
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
            if ((i = jjnewStateCnt) == (startsAt = 18 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 14, 15, 17, 1, 2, 4, 1, 2, 6, 4, 3, 5, 7 };

    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, "\50", "\51", "\173", "\175", "\133", "\135", "\73", "\54", "\56", "\75", "\76", "\74", "\77", "\72", "\75\75", "\74\75", "\76\75", "\41\75", "\53", "\55", "\52", "\57", "\45", "\52\52", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };

    public static final String[] lexStateNames = { "DEFAULT" };

    static final long[] jjtoToken = { 0xffffffffffffffc1L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0x13fffffffL };

    static final long[] jjtoSkip = { 0x3eL, 0x0L, 0x0L, 0x0L };

    private SimpleCharStream input_stream;

    private final int[] jjrounds = new int[18];

    private final int[] jjstateSet = new int[36];

    protected char curChar;

    public ASMParseTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    public ASMParseTokenManager(SimpleCharStream stream, int lexState) {
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
        for (i = 18; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
    }

    private final Token jjFillToken() {
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

    public final Token getNextToken() {
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
}
