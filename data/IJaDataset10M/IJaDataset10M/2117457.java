package fr.ign.cogit.geoxygene.util.conversion;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IAggregate;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_Aggregate;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiPoint;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Ring;

/** Token Manager. */
public class WktGeOxygeneTokenManager implements WktGeOxygeneConstants {

    /** Debug output. */
    public java.io.PrintStream debugStream = System.out;

    /** Set debug output. */
    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch(pos) {
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
            case 10:
                return jjStopAtPos(0, 6);
            case 40:
                return jjStopAtPos(0, 9);
            case 41:
                return jjStopAtPos(0, 10);
            case 44:
                return jjStopAtPos(0, 12);
            case 59:
                return jjStopAtPos(0, 23);
            case 69:
                return jjMoveStringLiteralDfa1_0(0x800L);
            case 71:
                return jjMoveStringLiteralDfa1_0(0x200000L);
            case 76:
                return jjMoveStringLiteralDfa1_0(0x20000L);
            case 77:
                jjmatchedKind = 15;
                return jjMoveStringLiteralDfa1_0(0x150000L);
            case 80:
                return jjMoveStringLiteralDfa1_0(0x82000L);
            case 83:
                return jjMoveStringLiteralDfa1_0(0x400000L);
            case 90:
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
            case 69:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000L);
            case 73:
                return jjMoveStringLiteralDfa2_0(active0, 0x20000L);
            case 77:
                return jjMoveStringLiteralDfa2_0(active0, 0x800L);
            case 79:
                return jjMoveStringLiteralDfa2_0(active0, 0x82000L);
            case 82:
                return jjMoveStringLiteralDfa2_0(active0, 0x400000L);
            case 85:
                return jjMoveStringLiteralDfa2_0(active0, 0x150000L);
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
            case 73:
                return jjMoveStringLiteralDfa3_0(active0, 0x402000L);
            case 76:
                return jjMoveStringLiteralDfa3_0(active0, 0x1d0000L);
            case 78:
                return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
            case 79:
                return jjMoveStringLiteralDfa3_0(active0, 0x200000L);
            case 80:
                return jjMoveStringLiteralDfa3_0(active0, 0x800L);
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
            case 68:
                return jjMoveStringLiteralDfa4_0(active0, 0x400000L);
            case 69:
                return jjMoveStringLiteralDfa4_0(active0, 0x20000L);
            case 77:
                return jjMoveStringLiteralDfa4_0(active0, 0x200000L);
            case 78:
                return jjMoveStringLiteralDfa4_0(active0, 0x2000L);
            case 84:
                return jjMoveStringLiteralDfa4_0(active0, 0x150800L);
            case 89:
                return jjMoveStringLiteralDfa4_0(active0, 0x80000L);
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
            case 61:
                if ((active0 & 0x400000L) != 0L) return jjStopAtPos(4, 22);
                break;
            case 69:
                return jjMoveStringLiteralDfa5_0(active0, 0x200000L);
            case 71:
                return jjMoveStringLiteralDfa5_0(active0, 0x80000L);
            case 73:
                return jjMoveStringLiteralDfa5_0(active0, 0x150000L);
            case 83:
                return jjMoveStringLiteralDfa5_0(active0, 0x20000L);
            case 84:
                if ((active0 & 0x2000L) != 0L) return jjStopAtPos(4, 13);
                break;
            case 89:
                if ((active0 & 0x800L) != 0L) return jjStopAtPos(4, 11);
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
            case 76:
                return jjMoveStringLiteralDfa6_0(active0, 0x40000L);
            case 79:
                return jjMoveStringLiteralDfa6_0(active0, 0x80000L);
            case 80:
                return jjMoveStringLiteralDfa6_0(active0, 0x110000L);
            case 84:
                return jjMoveStringLiteralDfa6_0(active0, 0x220000L);
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
            case 73:
                return jjMoveStringLiteralDfa7_0(active0, 0x40000L);
            case 78:
                if ((active0 & 0x80000L) != 0L) return jjStopAtPos(6, 19);
                break;
            case 79:
                return jjMoveStringLiteralDfa7_0(active0, 0x110000L);
            case 82:
                return jjMoveStringLiteralDfa7_0(active0, 0x220000L);
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
            case 73:
                return jjMoveStringLiteralDfa8_0(active0, 0x30000L);
            case 76:
                return jjMoveStringLiteralDfa8_0(active0, 0x100000L);
            case 78:
                return jjMoveStringLiteralDfa8_0(active0, 0x40000L);
            case 89:
                return jjMoveStringLiteralDfa8_0(active0, 0x200000L);
            default:
                break;
        }
        return jjStartNfa_0(6, active0);
    }

    private int jjMoveStringLiteralDfa8_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(6, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(7, active0);
            return 8;
        }
        switch(curChar) {
            case 67:
                return jjMoveStringLiteralDfa9_0(active0, 0x200000L);
            case 69:
                return jjMoveStringLiteralDfa9_0(active0, 0x40000L);
            case 78:
                return jjMoveStringLiteralDfa9_0(active0, 0x30000L);
            case 89:
                return jjMoveStringLiteralDfa9_0(active0, 0x100000L);
            default:
                break;
        }
        return jjStartNfa_0(7, active0);
    }

    private int jjMoveStringLiteralDfa9_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(7, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(8, active0);
            return 9;
        }
        switch(curChar) {
            case 71:
                if ((active0 & 0x20000L) != 0L) return jjStopAtPos(9, 17);
                return jjMoveStringLiteralDfa10_0(active0, 0x100000L);
            case 79:
                return jjMoveStringLiteralDfa10_0(active0, 0x200000L);
            case 83:
                return jjMoveStringLiteralDfa10_0(active0, 0x40000L);
            case 84:
                if ((active0 & 0x10000L) != 0L) return jjStopAtPos(9, 16);
                break;
            default:
                break;
        }
        return jjStartNfa_0(8, active0);
    }

    private int jjMoveStringLiteralDfa10_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(8, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(9, active0);
            return 10;
        }
        switch(curChar) {
            case 76:
                return jjMoveStringLiteralDfa11_0(active0, 0x200000L);
            case 79:
                return jjMoveStringLiteralDfa11_0(active0, 0x100000L);
            case 84:
                return jjMoveStringLiteralDfa11_0(active0, 0x40000L);
            default:
                break;
        }
        return jjStartNfa_0(9, active0);
    }

    private int jjMoveStringLiteralDfa11_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(9, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(10, active0);
            return 11;
        }
        switch(curChar) {
            case 76:
                return jjMoveStringLiteralDfa12_0(active0, 0x200000L);
            case 78:
                if ((active0 & 0x100000L) != 0L) return jjStopAtPos(11, 20);
                break;
            case 82:
                return jjMoveStringLiteralDfa12_0(active0, 0x40000L);
            default:
                break;
        }
        return jjStartNfa_0(10, active0);
    }

    private int jjMoveStringLiteralDfa12_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(10, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(11, active0);
            return 12;
        }
        switch(curChar) {
            case 69:
                return jjMoveStringLiteralDfa13_0(active0, 0x200000L);
            case 73:
                return jjMoveStringLiteralDfa13_0(active0, 0x40000L);
            default:
                break;
        }
        return jjStartNfa_0(11, active0);
    }

    private int jjMoveStringLiteralDfa13_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(11, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(12, active0);
            return 13;
        }
        switch(curChar) {
            case 67:
                return jjMoveStringLiteralDfa14_0(active0, 0x200000L);
            case 78:
                return jjMoveStringLiteralDfa14_0(active0, 0x40000L);
            default:
                break;
        }
        return jjStartNfa_0(12, active0);
    }

    private int jjMoveStringLiteralDfa14_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(12, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(13, active0);
            return 14;
        }
        switch(curChar) {
            case 71:
                if ((active0 & 0x40000L) != 0L) return jjStopAtPos(14, 18);
                break;
            case 84:
                return jjMoveStringLiteralDfa15_0(active0, 0x200000L);
            default:
                break;
        }
        return jjStartNfa_0(13, active0);
    }

    private int jjMoveStringLiteralDfa15_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(13, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(14, active0);
            return 15;
        }
        switch(curChar) {
            case 73:
                return jjMoveStringLiteralDfa16_0(active0, 0x200000L);
            default:
                break;
        }
        return jjStartNfa_0(14, active0);
    }

    private int jjMoveStringLiteralDfa16_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(14, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(15, active0);
            return 16;
        }
        switch(curChar) {
            case 79:
                return jjMoveStringLiteralDfa17_0(active0, 0x200000L);
            default:
                break;
        }
        return jjStartNfa_0(15, active0);
    }

    private int jjMoveStringLiteralDfa17_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(15, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(16, active0);
            return 17;
        }
        switch(curChar) {
            case 78:
                if ((active0 & 0x200000L) != 0L) return jjStopAtPos(17, 21);
                break;
            default:
                break;
        }
        return jjStartNfa_0(16, active0);
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 51;
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
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 1) kind = 1;
                                jjCheckNAddStates(0, 15);
                            } else if ((0x280000000000L & l) != 0L) jjAddStates(16, 19); else if (curChar == 46) jjCheckNAddStates(20, 23);
                            break;
                        case 1:
                            if (curChar == 46) jjCheckNAddTwoStates(2, 23);
                            break;
                        case 2:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(24, 26);
                            break;
                        case 4:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(5);
                            break;
                        case 5:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(5, 6);
                            break;
                        case 6:
                            if (curChar == 32) jjCheckNAddStates(27, 30);
                            break;
                        case 7:
                            if ((0x280000000000L & l) != 0L) jjCheckNAddTwoStates(8, 14);
                            break;
                        case 8:
                            if (curChar == 46) jjCheckNAddTwoStates(9, 13);
                            break;
                        case 9:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddStates(31, 33);
                            break;
                        case 11:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(12);
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddTwoStates(6, 12);
                            break;
                        case 13:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddTwoStates(6, 13);
                            break;
                        case 14:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddStates(34, 41);
                            break;
                        case 15:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddStates(42, 44);
                            break;
                        case 16:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddStates(45, 48);
                            break;
                        case 17:
                            if (curChar == 46) jjCheckNAdd(18);
                            break;
                        case 18:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddStates(49, 51);
                            break;
                        case 19:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddStates(52, 54);
                            break;
                        case 20:
                            if (curChar == 46) jjCheckNAdd(21);
                            break;
                        case 21:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddTwoStates(6, 21);
                            break;
                        case 22:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 5) kind = 5;
                            jjCheckNAddTwoStates(6, 22);
                            break;
                        case 23:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(23, 6);
                            break;
                        case 24:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(55, 62);
                            break;
                        case 25:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(63, 65);
                            break;
                        case 26:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(66, 69);
                            break;
                        case 27:
                            if (curChar == 46) jjCheckNAdd(28);
                            break;
                        case 28:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(70, 72);
                            break;
                        case 29:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(73, 75);
                            break;
                        case 30:
                            if (curChar == 46) jjCheckNAdd(31);
                            break;
                        case 31:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(31, 6);
                            break;
                        case 32:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(32, 6);
                            break;
                        case 33:
                            if (curChar == 46) jjCheckNAddTwoStates(34, 38);
                            break;
                        case 34:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddTwoStates(34, 35);
                            break;
                        case 36:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(37);
                            break;
                        case 37:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAdd(37);
                            break;
                        case 38:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAdd(38);
                            break;
                        case 39:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddStates(76, 82);
                            break;
                        case 40:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddTwoStates(40, 35);
                            break;
                        case 41:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddStates(83, 85);
                            break;
                        case 42:
                            if (curChar == 46) jjCheckNAdd(43);
                            break;
                        case 43:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddTwoStates(43, 35);
                            break;
                        case 44:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddTwoStates(44, 45);
                            break;
                        case 45:
                            if (curChar == 46) jjCheckNAdd(46);
                            break;
                        case 46:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAdd(46);
                            break;
                        case 47:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAdd(47);
                            break;
                        case 48:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 1) kind = 1;
                            jjCheckNAddStates(0, 15);
                            break;
                        case 49:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 3) kind = 3;
                            jjCheckNAdd(49);
                            break;
                        case 50:
                            if (curChar == 46) jjCheckNAddStates(20, 23);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch(jjstateSet[--i]) {
                        case 3:
                            if ((0x2000000020L & l) != 0L) jjAddStates(86, 87);
                            break;
                        case 10:
                            if ((0x2000000020L & l) != 0L) jjAddStates(88, 89);
                            break;
                        case 35:
                            if ((0x2000000020L & l) != 0L) jjAddStates(90, 91);
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
            if ((i = jjnewStateCnt) == (startsAt = 51 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 40, 41, 42, 35, 44, 45, 47, 49, 25, 26, 27, 3, 29, 30, 32, 6, 1, 24, 33, 39, 34, 38, 2, 23, 2, 3, 6, 6, 7, 8, 14, 6, 9, 10, 6, 15, 16, 17, 10, 19, 20, 22, 6, 15, 10, 6, 16, 17, 10, 6, 18, 10, 6, 19, 20, 25, 26, 27, 3, 29, 30, 32, 6, 25, 3, 6, 26, 27, 3, 6, 28, 3, 6, 29, 30, 6, 40, 41, 42, 35, 44, 45, 47, 41, 42, 35, 4, 5, 11, 12, 36, 37 };

    /** Token literal values. */
    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, "\12", null, null, "\50", "\51", "\105\115\120\124\131", "\54", "\120\117\111\116\124", "\132", "\115", "\115\125\114\124\111\120\117\111\116\124", "\114\111\116\105\123\124\122\111\116\107", "\115\125\114\124\111\114\111\116\105\123\124\122\111\116\107", "\120\117\114\131\107\117\116", "\115\125\114\124\111\120\117\114\131\107\117\116", "\107\105\117\115\105\124\122\131\103\117\114\114\105\103\124\111\117\116", "\123\122\111\104\75", "\73" };

    /** Lexer state names. */
    public static final String[] lexStateNames = { "DEFAULT" };

    static final long[] jjtoToken = { 0xfffe6bL };

    static final long[] jjtoSkip = { 0x180L };

    protected SimpleCharStream input_stream;

    private final int[] jjrounds = new int[51];

    private final int[] jjstateSet = new int[102];

    protected char curChar;

    /** Constructor. */
    public WktGeOxygeneTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    /** Constructor. */
    public WktGeOxygeneTokenManager(SimpleCharStream stream, int lexState) {
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
        for (i = 51; i-- > 0; ) jjrounds[i] = 0x80000000;
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
                while (curChar <= 32 && (0x100000200L & (1L << curChar)) != 0L) curChar = input_stream.BeginToken();
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
