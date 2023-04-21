package sifter.rcfile;

import sifter.*;
import sifter.rcfile.*;
import sifter.translation.*;
import sifter.ui.*;
import sifter.messages.*;
import sifter.connection.*;
import net.sourceforge.jmisc.Debug;
import net.sourceforge.jmisc.ProgrammerError;
import java.io.*;
import java.util.Vector;
import java.util.Properties;
import gnu.regexp.*;

public class RCFileTokenManager implements RCFileConstants {

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
            case 0:
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    return 19;
                }
                if ((active0 & 0x40000000000L) != 0L) {
                    jjmatchedKind = 18;
                    return 20;
                }
                if ((active0 & 0x40003ffc0L) != 0L) {
                    jjmatchedKind = 18;
                    return 14;
                }
                return -1;
            case 1:
                if ((active0 & 0x40003ffc0L) != 0L) {
                    jjmatchedKind = 18;
                    jjmatchedPos = 1;
                    return 21;
                }
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 1;
                    return 22;
                }
                return -1;
            case 2:
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 2;
                    return 22;
                }
                if ((active0 & 0x21100L) != 0L) return 21;
                if ((active0 & 0x1eec0L) != 0L) {
                    jjmatchedKind = 18;
                    jjmatchedPos = 2;
                    return 21;
                }
                return -1;
            case 3:
                if ((active0 & 0x18c80L) != 0L) {
                    jjmatchedKind = 18;
                    jjmatchedPos = 3;
                    return 21;
                }
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 3;
                    return 22;
                }
                if ((active0 & 0x6240L) != 0L) return 21;
                return -1;
            case 4:
                if ((active0 & 0x18800L) != 0L) {
                    jjmatchedKind = 18;
                    jjmatchedPos = 4;
                    return 21;
                }
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 4;
                    return 19;
                }
                if ((active0 & 0x480L) != 0L) return 21;
                return -1;
            case 5:
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 5;
                    return 22;
                }
                if ((active0 & 0x18000L) != 0L) return 21;
                if ((active0 & 0x800L) != 0L) {
                    jjmatchedKind = 18;
                    jjmatchedPos = 5;
                    return 21;
                }
                return -1;
            case 6:
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 6;
                    return 22;
                }
                if ((active0 & 0x800L) != 0L) return 21;
                return -1;
            case 7:
                if ((active0 & 0x20L) != 0L) {
                    jjmatchedKind = 20;
                    jjmatchedPos = 7;
                    return 22;
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
            case 33:
                jjmatchedKind = 52;
                return jjMoveStringLiteralDfa1_0(0x20000000000000L);
            case 34:
                return jjStopAtPos(0, 26);
            case 35:
                return jjStopAtPos(0, 23);
            case 38:
                jjmatchedKind = 48;
                return jjMoveStringLiteralDfa1_0(0x2000000000000L);
            case 40:
                return jjStopAtPos(0, 50);
            case 41:
                return jjStopAtPos(0, 51);
            case 44:
                return jjStopAtPos(0, 39);
            case 45:
                return jjMoveStringLiteralDfa1_0(0x40000000000L);
            case 47:
                return jjMoveStringLiteralDfa1_0(0x20L);
            case 58:
                return jjStopAtPos(0, 43);
            case 59:
                return jjStopAtPos(0, 40);
            case 61:
                return jjStopAtPos(0, 38);
            case 64:
                return jjStopAtPos(0, 41);
            case 96:
                return jjStopAtPos(0, 30);
            case 97:
                return jjMoveStringLiteralDfa1_0(0x80L);
            case 99:
                return jjMoveStringLiteralDfa1_0(0x800L);
            case 100:
                return jjMoveStringLiteralDfa1_0(0x1100L);
            case 102:
                return jjMoveStringLiteralDfa1_0(0xc000L);
            case 103:
                return jjMoveStringLiteralDfa1_0(0x400L);
            case 104:
                return jjMoveStringLiteralDfa1_0(0x40L);
            case 108:
                return jjMoveStringLiteralDfa1_0(0x2000L);
            case 114:
                return jjMoveStringLiteralDfa1_0(0x400020000L);
            case 116:
                return jjMoveStringLiteralDfa1_0(0x10000L);
            case 117:
                return jjMoveStringLiteralDfa1_0(0x200L);
            case 123:
                return jjStopAtPos(0, 44);
            case 124:
                jjmatchedKind = 46;
                return jjMoveStringLiteralDfa1_0(0x800000000000L);
            case 125:
                return jjStopAtPos(0, 45);
            default:
                return jjMoveNfa_0(0, 0);
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
            case 38:
                if ((active0 & 0x2000000000000L) != 0L) return jjStopAtPos(1, 49);
                break;
            case 61:
                if ((active0 & 0x20000000000000L) != 0L) return jjStopAtPos(1, 53);
                break;
            case 62:
                if ((active0 & 0x40000000000L) != 0L) return jjStopAtPos(1, 42);
                break;
            case 100:
                return jjMoveStringLiteralDfa2_0(active0, 0x20L);
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x400010100L);
            case 105:
                return jjMoveStringLiteralDfa2_0(active0, 0x5000L);
            case 108:
                return jjMoveStringLiteralDfa2_0(active0, 0x8080L);
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x2840L);
            case 114:
                return jjMoveStringLiteralDfa2_0(active0, 0x400L);
            case 115:
                return jjMoveStringLiteralDfa2_0(active0, 0x20200L);
            case 124:
                if ((active0 & 0x800000000000L) != 0L) return jjStopAtPos(1, 47);
                break;
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
            case 34:
                if ((active0 & 0x400000000L) != 0L) return jjStopAtPos(2, 34);
                break;
            case 99:
                return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
            case 101:
                return jjMoveStringLiteralDfa3_0(active0, 0x220L);
            case 102:
                if ((active0 & 0x100L) != 0L) return jjStartNfaWithStates_0(2, 8, 21);
                break;
            case 104:
                if ((active0 & 0x20000L) != 0L) return jjStartNfaWithStates_0(2, 17, 21);
                break;
            case 105:
                return jjMoveStringLiteralDfa3_0(active0, 0x80L);
            case 108:
                return jjMoveStringLiteralDfa3_0(active0, 0x14000L);
            case 110:
                return jjMoveStringLiteralDfa3_0(active0, 0x800L);
            case 111:
                return jjMoveStringLiteralDfa3_0(active0, 0x8400L);
            case 114:
                if ((active0 & 0x1000L) != 0L) return jjStartNfaWithStates_0(2, 12, 21);
                break;
            case 115:
                return jjMoveStringLiteralDfa3_0(active0, 0x40L);
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
                return jjMoveStringLiteralDfa4_0(active0, 0x80L);
            case 101:
                if ((active0 & 0x4000L) != 0L) return jjStartNfaWithStates_0(3, 14, 21);
                break;
            case 107:
                if ((active0 & 0x2000L) != 0L) return jjStartNfaWithStates_0(3, 13, 21);
                break;
            case 110:
                return jjMoveStringLiteralDfa4_0(active0, 0x10800L);
            case 112:
                return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
            case 114:
                if ((active0 & 0x200L) != 0L) return jjStartNfaWithStates_0(3, 9, 21);
                break;
            case 116:
                if ((active0 & 0x40L) != 0L) return jjStartNfaWithStates_0(3, 6, 21);
                break;
            case 117:
                return jjMoveStringLiteralDfa4_0(active0, 0x400L);
            case 118:
                return jjMoveStringLiteralDfa4_0(active0, 0x20L);
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
            case 47:
                return jjMoveStringLiteralDfa5_0(active0, 0x20L);
            case 101:
                return jjMoveStringLiteralDfa5_0(active0, 0x10800L);
            case 112:
                if ((active0 & 0x400L) != 0L) return jjStartNfaWithStates_0(4, 10, 21);
                return jjMoveStringLiteralDfa5_0(active0, 0x8000L);
            case 115:
                if ((active0 & 0x80L) != 0L) return jjStartNfaWithStates_0(4, 7, 21);
                break;
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
            case 99:
                return jjMoveStringLiteralDfa6_0(active0, 0x800L);
            case 110:
                return jjMoveStringLiteralDfa6_0(active0, 0x20L);
            case 116:
                if ((active0 & 0x10000L) != 0L) return jjStartNfaWithStates_0(5, 16, 21);
                break;
            case 121:
                if ((active0 & 0x8000L) != 0L) return jjStartNfaWithStates_0(5, 15, 21);
                break;
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
            case 116:
                if ((active0 & 0x800L) != 0L) return jjStartNfaWithStates_0(6, 11, 21);
                break;
            case 117:
                return jjMoveStringLiteralDfa7_0(active0, 0x20L);
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
            case 108:
                return jjMoveStringLiteralDfa8_0(active0, 0x20L);
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
            case 108:
                if ((active0 & 0x20L) != 0L) return jjStartNfaWithStates_0(8, 5, 22);
                break;
            default:
                break;
        }
        return jjStartNfa_0(7, active0);
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

    private final int jjMoveNfa_0(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 19;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 22:
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 42) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 47) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 58) jjstateSet[jjnewStateCnt++] = 7;
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            } else if (curChar == 47) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            break;
                        case 19:
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 42) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 47) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            } else if (curChar == 47) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            break;
                        case 21:
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 42) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 47) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 58) jjstateSet[jjnewStateCnt++] = 7;
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            } else if (curChar == 42) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            } else if (curChar == 47) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAdd(1);
                            }
                            break;
                        case 14:
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 42) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 47) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 58) jjstateSet[jjnewStateCnt++] = 7;
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            } else if (curChar == 42) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            } else if (curChar == 47) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            } else if (curChar == 58) jjstateSet[jjnewStateCnt++] = 15;
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            } else if (curChar == 58) jjstateSet[jjnewStateCnt++] = 13;
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAdd(1);
                            }
                            break;
                        case 20:
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 42) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            } else if (curChar == 47) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            } else if (curChar == 42) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            } else if (curChar == 47) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAdd(1);
                            }
                            break;
                        case 0:
                            if ((0x3ff681000000000L & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAddStates(4, 12);
                            } else if (curChar == 42) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddStates(13, 18);
                            } else if (curChar == 47) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddStates(19, 24);
                            }
                            break;
                        case 1:
                            if ((0x3ff681000000000L & l) == 0L) break;
                            if (kind > 18) kind = 18;
                            jjCheckNAdd(1);
                            break;
                        case 2:
                            if ((0x3ff681000000000L & l) == 0L) break;
                            if (kind > 20) kind = 20;
                            jjCheckNAddTwoStates(2, 3);
                            break;
                        case 3:
                            if (curChar != 47) break;
                            if (kind > 20) kind = 20;
                            jjCheckNAddTwoStates(2, 3);
                            break;
                        case 4:
                            if ((0x3ff681000000000L & l) == 0L) break;
                            if (kind > 21) kind = 21;
                            jjCheckNAddTwoStates(4, 5);
                            break;
                        case 5:
                            if (curChar != 42) break;
                            if (kind > 21) kind = 21;
                            jjCheckNAddTwoStates(4, 5);
                            break;
                        case 7:
                            if (curChar == 47) jjCheckNAddTwoStates(8, 9);
                            break;
                        case 8:
                            if ((0x3ff681000000000L & l) == 0L) break;
                            if (kind > 22) kind = 22;
                            jjCheckNAddStates(0, 3);
                            break;
                        case 9:
                            if (curChar != 47) break;
                            if (kind > 22) kind = 22;
                            jjCheckNAddStates(0, 3);
                            break;
                        case 10:
                            if (curChar != 42) break;
                            if (kind > 22) kind = 22;
                            jjCheckNAddStates(0, 3);
                            break;
                        case 11:
                            if (curChar == 58) jjstateSet[jjnewStateCnt++] = 7;
                            break;
                        case 13:
                            if (curChar == 47 && kind > 19) kind = 19;
                            break;
                        case 15:
                            if (curChar == 47) jjCheckNAddTwoStates(2, 3);
                            break;
                        case 16:
                            if (curChar == 58) jjstateSet[jjnewStateCnt++] = 15;
                            break;
                        case 17:
                            if (curChar != 47) break;
                            if (kind > 20) kind = 20;
                            jjCheckNAddStates(19, 24);
                            break;
                        case 18:
                            if (curChar != 42) break;
                            if (kind > 21) kind = 21;
                            jjCheckNAddStates(13, 18);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 22:
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 19:
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 21:
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAdd(1);
                            }
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 14:
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAdd(1);
                            }
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 20:
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 22) kind = 22;
                                jjCheckNAddStates(0, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 21) kind = 21;
                                jjCheckNAddTwoStates(4, 5);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 20) kind = 20;
                                jjCheckNAddTwoStates(2, 3);
                            }
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAdd(1);
                            }
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 0:
                            if ((0x47fffffeaffffffeL & l) != 0L) {
                                if (kind > 18) kind = 18;
                                jjCheckNAddStates(4, 12);
                            }
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAddStates(25, 27);
                            break;
                        case 1:
                            if ((0x47fffffeaffffffeL & l) == 0L) break;
                            if (kind > 18) kind = 18;
                            jjCheckNAdd(1);
                            break;
                        case 2:
                            if ((0x47fffffeaffffffeL & l) == 0L) break;
                            if (kind > 20) kind = 20;
                            jjCheckNAddTwoStates(2, 3);
                            break;
                        case 4:
                            if ((0x47fffffeaffffffeL & l) == 0L) break;
                            if (kind > 21) kind = 21;
                            jjCheckNAddTwoStates(4, 5);
                            break;
                        case 6:
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 8:
                            if ((0x47fffffeaffffffeL & l) == 0L) break;
                            if (kind > 22) kind = 22;
                            jjCheckNAddStates(0, 3);
                            break;
                        case 12:
                            if ((0x7fffffe07fffffeL & l) != 0L) jjCheckNAddStates(25, 27);
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
            if ((i = jjnewStateCnt) == (startsAt = 19 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjStopStringLiteralDfa_2(int pos, long active0) {
        switch(pos) {
            default:
                return -1;
        }
    }

    private final int jjStartNfa_2(int pos, long active0) {
        return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_2(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_2(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_2() {
        switch(curChar) {
            case 34:
                return jjStopAtPos(0, 29);
            default:
                return jjMoveNfa_2(0, 0);
        }
    }

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private final int jjMoveNfa_2(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 3;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0xfffffffbffffffffL & l) != 0L && kind > 27) kind = 27;
                            break;
                        case 2:
                            if ((0xfffffffeffffffffL & l) != 0L && kind > 28) kind = 28;
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
                            if ((0xffffffffefffffffL & l) != 0L) {
                                if (kind > 27) kind = 27;
                            } else if (curChar == 92) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 1:
                            if (curChar == 92) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 2:
                            if (kind > 28) kind = 28;
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
                        case 0:
                            if ((jjbitVec0[i2] & l2) != 0L && kind > 27) kind = 27;
                            break;
                        case 2:
                            if ((jjbitVec0[i2] & l2) != 0L && kind > 28) kind = 28;
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
            if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjStopStringLiteralDfa_4(int pos, long active0) {
        switch(pos) {
            default:
                return -1;
        }
    }

    private final int jjStartNfa_4(int pos, long active0) {
        return jjMoveNfa_4(jjStopStringLiteralDfa_4(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_4(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_4(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_4() {
        switch(curChar) {
            case 34:
                return jjStopAtPos(0, 37);
            default:
                return jjMoveNfa_4(0, 0);
        }
    }

    private final int jjMoveNfa_4(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 3;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0xfffffffbffffffffL & l) != 0L) kind = 35;
                            break;
                        case 2:
                            if (curChar == 34) kind = 36;
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
                            if ((0xffffffffefffffffL & l) != 0L) {
                                if (kind > 35) kind = 35;
                            } else if (curChar == 92) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 1:
                            if (curChar == 92) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 2:
                            if ((0x10400010000000L & l) != 0L && kind > 36) kind = 36;
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
                        case 0:
                            if ((jjbitVec0[i2] & l2) != 0L && kind > 35) kind = 35;
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
            if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjStopStringLiteralDfa_3(int pos, long active0) {
        switch(pos) {
            default:
                return -1;
        }
    }

    private final int jjStartNfa_3(int pos, long active0) {
        return jjMoveNfa_3(jjStopStringLiteralDfa_3(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_3(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_3(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_3() {
        switch(curChar) {
            case 96:
                return jjStopAtPos(0, 33);
            default:
                return jjMoveNfa_3(0, 0);
        }
    }

    private final int jjMoveNfa_3(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 3;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if (kind > 31) kind = 31;
                            break;
                        case 2:
                            if ((0xfffffffeffffffffL & l) != 0L && kind > 32) kind = 32;
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
                            if ((0xfffffffeefffffffL & l) != 0L) {
                                if (kind > 31) kind = 31;
                            } else if (curChar == 92) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 1:
                            if (curChar == 92) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 2:
                            if (kind > 32) kind = 32;
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
                        case 0:
                            if ((jjbitVec0[i2] & l2) != 0L && kind > 31) kind = 31;
                            break;
                        case 2:
                            if ((jjbitVec0[i2] & l2) != 0L && kind > 32) kind = 32;
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
            if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjMoveStringLiteralDfa0_1() {
        return jjMoveNfa_1(0, 0);
    }

    private final int jjMoveNfa_1(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 4;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0xffffffffffffdbffL & l) != 0L) {
                                if (kind > 24) kind = 24;
                            } else if ((0x2400L & l) != 0L) {
                                if (kind > 25) kind = 25;
                            }
                            if (curChar == 13) jjstateSet[jjnewStateCnt++] = 2;
                            break;
                        case 1:
                            if ((0x2400L & l) != 0L && kind > 25) kind = 25;
                            break;
                        case 2:
                            if (curChar == 10 && kind > 25) kind = 25;
                            break;
                        case 3:
                            if (curChar == 13) jjstateSet[jjnewStateCnt++] = 2;
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
                            kind = 24;
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
                        case 0:
                            if ((jjbitVec0[i2] & l2) != 0L && kind > 24) kind = 24;
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
            if ((i = jjnewStateCnt) == (startsAt = 4 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 6, 8, 9, 10, 1, 2, 3, 4, 5, 6, 8, 9, 10, 4, 5, 6, 8, 9, 10, 2, 3, 6, 8, 9, 10, 14, 16, 11 };

    public static final String[] jjstrLiteralImages = { "", null, null, null, null, "\57\144\145\166\57\156\165\154\154", "\150\157\163\164", "\141\154\151\141\163", "\144\145\146", "\165\163\145\162", "\147\162\157\165\160", "\143\157\156\156\145\143\164", "\144\151\162", "\154\157\143\153", "\146\151\154\145", "\146\154\157\160\160\171", "\164\145\154\156\145\164", "\162\163\150", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "\75", "\54", "\73", "\100", "\55\76", "\72", "\173", "\175", "\174", "\174\174", "\46", "\46\46", "\50", "\51", "\41", "\41\75" };

    public static final String[] lexStateNames = { "DEFAULT", "IN_COMMENT", "IN_STRING", "IN_COMMAND", "IN_RE" };

    public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 0, 2, -1, -1, 0, 3, -1, -1, 0, 4, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    static final long[] jjtoToken = { 0x3fffe2207fffe1L };

    static final long[] jjtoSkip = { 0x280001eL };

    static final long[] jjtoMore = { 0x1ddd000000L };

    private ASCII_CharStream input_stream;

    private final int[] jjrounds = new int[19];

    private final int[] jjstateSet = new int[38];

    StringBuffer image;

    int jjimageLen;

    int lengthOfMatch;

    protected char curChar;

    public RCFileTokenManager(ASCII_CharStream stream) {
        if (ASCII_CharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    public RCFileTokenManager(ASCII_CharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(ASCII_CharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 19; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    public void ReInit(ASCII_CharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 5 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
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
                return matchedToken;
            }
            image = null;
            jjimageLen = 0;
            for (; ; ) {
                switch(curLexState) {
                    case 0:
                        try {
                            input_stream.backup(0);
                            while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
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
                        break;
                    case 2:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_2();
                        break;
                    case 3:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_3();
                        break;
                    case 4:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_4();
                        break;
                }
                if (jjmatchedKind != 0x7fffffff) {
                    if (jjmatchedPos + 1 < curPos) input_stream.backup(curPos - jjmatchedPos - 1);
                    if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        TokenLexicalActions(matchedToken);
                        if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                        return matchedToken;
                    } else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                        continue EOFLoop;
                    }
                    MoreLexicalActions();
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

    final void MoreLexicalActions() {
        jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
        switch(jjmatchedKind) {
            case 26:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(new String(input_stream.GetSuffix(jjimageLen)));
                jjimageLen = 0;
                image.deleteCharAt(0);
                break;
            case 28:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(new String(input_stream.GetSuffix(jjimageLen)));
                jjimageLen = 0;
                int start = image.length() - 2;
                char c = image.charAt(start + 1);
                image.deleteCharAt(start + 1);
                switch(c) {
                    case 'n':
                        c = '\n';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    default:
                        c = c;
                        break;
                    case '$':
                        c = RCFile.ESCAPED_DOLLAR;
                        break;
                }
                image.setCharAt(start, c);
                break;
            case 30:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(new String(input_stream.GetSuffix(jjimageLen)));
                jjimageLen = 0;
                image.deleteCharAt(0);
                break;
            case 32:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(new String(input_stream.GetSuffix(jjimageLen)));
                jjimageLen = 0;
                int start2 = image.length() - 2;
                char c2 = image.charAt(start2 + 1);
                image.deleteCharAt(start2 + 1);
                switch(c2) {
                    case 'n':
                        c2 = '\n';
                        break;
                    case 't':
                        c2 = '\t';
                        break;
                    default:
                        c2 = c2;
                        break;
                    case '$':
                        c2 = RCFile.ESCAPED_DOLLAR;
                        break;
                }
                image.setCharAt(start2, c2);
                break;
            case 34:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(new String(input_stream.GetSuffix(jjimageLen)));
                jjimageLen = 0;
                image.delete(0, 2);
                break;
            case 36:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(new String(input_stream.GetSuffix(jjimageLen)));
                jjimageLen = 0;
                start = image.length() - 2;
                c = image.charAt(start + 1);
                image.deleteCharAt(start + 1);
                switch(c) {
                    case 'n':
                        c = '\n';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case '\"':
                        c = '\"';
                        break;
                    case '\\':
                        c = '\\';
                        break;
                }
                image.setCharAt(start, c);
                break;
            default:
                break;
        }
    }

    final void TokenLexicalActions(Token matchedToken) {
        switch(jjmatchedKind) {
            case 29:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1)))); else image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                image.deleteCharAt(image.length() - 1);
                matchedToken.image = image.toString();
                break;
            case 33:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1)))); else image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                image.deleteCharAt(image.length() - 1);
                matchedToken.image = image.toString();
                break;
            case 37:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1)))); else image.append(new String(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1))));
                image.deleteCharAt(image.length() - 1);
                matchedToken.image = image.toString();
                break;
            default:
                break;
        }
    }
}
