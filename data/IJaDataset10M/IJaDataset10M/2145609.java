package org.waterlanguage;

import wb.Seg;
import static wb.Han.*;
import wb.Han;
import static wb.Segs.*;
import static wb.Db.*;
import static wb.Ents.*;
import static org.waterlanguage.Arithmetic.*;
import static org.waterlanguage.Convert.*;
import static org.waterlanguage.Inport.*;
import static org.waterlanguage.Logicals.*;
import static org.waterlanguage.Caps.*;
import static org.waterlanguage.Utils.*;
import static org.waterlanguage.Symcodes.*;
import static org.waterlanguage.Utf8.*;
import static org.waterlanguage.Wwprint.*;

public class Wbinfra {

    public static final int wwi_WobClass = 0;

    public static final byte[] ww_Wob = bytes((byte) (0x20), (byte) (0));

    public static final int wwi_ClassClass = 1;

    public static final byte[] ww_Class = bytes((byte) (0x20), (byte) (1));

    public static boolean ww_Class_P(Caps caps, byte[] subj) {
        return a2b(subj) && 8 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0xb && bytesEqual_P(ww_InstanceToParent(caps, subj), ww_PrimClass(caps, wwi_ClassClass));
    }

    public static final int wwi_BooleanClass = 2;

    public static final byte[] ww_Boolean = bytes((byte) (0x20), (byte) (2));

    public static final int wwi_StringClass = 3;

    public static final byte[] ww_String = bytes((byte) (0x20), (byte) (3));

    public static boolean ww_String_P(byte[] obj) {
        if (a2b(obj)) {
            int byt0 = (obj[0] & 0xFF);
            return (0xf0 <= (byt0) && (byt0) <= 0xff || 0x20 <= (byt0) && (byt0) <= 0x23 && 0 < ((obj[1] & 0xFF)) || 0xc <= (byt0) && (byt0) <= 0x13);
        } else return false;
    }

    public static final int wwi_DoubleClass = 4;

    public static final byte[] ww_Double = bytes((byte) (0x20), (byte) (4));

    public static boolean ww_Double_P(byte[] byts) {
        return 0xcb == ((byts[0] & 0xFF));
    }

    public static final int wwi_IntegerClass = 5;

    public static final byte[] ww_Integer = bytes((byte) (0x20), (byte) (5));

    public static boolean ww_Integer_P(byte[] byts) {
        return 0xcd <= ((byts[0] & 0xFF)) && ((byts[0] & 0xFF)) <= 0xdf;
    }

    public static boolean ww_Natural_P(byte[] byts) {
        return 0xd6 <= ((byts[0] & 0xFF)) && ((byts[0] & 0xFF)) <= 0xdf;
    }

    public static final int wwi_NumberClass = 6;

    public static final byte[] ww_Number = bytes((byte) (0x20), (byte) (6));

    public static boolean ww_Number_P(byte[] byts) {
        return 0xcb <= ((byts[0] & 0xFF)) && ((byts[0] & 0xFF)) <= 0xdf;
    }

    public static final int wwi_VectorClass = 7;

    public static final byte[] ww_Vector = bytes((byte) (0x20), (byte) (7));

    public static boolean ww_Vector_P(Caps caps, byte[] subj) {
        return a2b(subj) && 8 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0xb && bytesEqual_P(ww_InstanceToParent(caps, subj), ww_PrimClass(caps, wwi_VectorClass));
    }

    public static final int wwi_BytesClass = 8;

    public static final byte[] ww_Bytes = bytes((byte) (0x20), (byte) (8));

    public static final int wwi_ThingClass = 9;

    public static final byte[] ww_Thing = bytes((byte) (0x20), (byte) (9));

    public static final int wwi_MethodClass = 0xa;

    public static final byte[] ww_Method = bytes((byte) (0x20), (byte) (0xa));

    public static final int wwi_CharClass = 0xb;

    public static final byte[] ww_Char = bytes((byte) (0x20), (byte) (0xb));

    public static byte[] ww_ToChar(Caps caps, byte[] vbyts, byte[] xpr, String caller) {
        int jdx;
        Bfin: while (true) {
            if (escape_P(vbyts)) return vbyts; else if (7 == ((vbyts[0] & 0xFF))) return vbyts; else if (ww_Natural_P(vbyts)) return bytesAppend(bytes((byte) (7)), vbyts); else if (ww_Integer_P(vbyts)) return ww_Err(caps, xpr, caller, "ec101_n", vbyts, null); else if (ww_String_P(vbyts)) if (1 == (ilength(caps, vbyts))) {
                byte[] rbyts = ww_DoStringGet(caps, vbyts, 0);
                if (a2b(rbyts)) return rbyts; else return ww_Err(caps, xpr, caller, "ei208_c", vbyts, null);
            } else {
                String strng = sbytsToString(caps, vbyts);
                if (strng.startsWith("&#") && strng.endsWith(";")) {
                    jdx = atoi(strng.substring(2, (-1 + (strng.length()))));
                    break Bfin;
                } else if (strng.equals("&lt;")) {
                    jdx = ('<');
                    break Bfin;
                } else if (strng.equals("&gt;")) {
                    jdx = ('>');
                    break Bfin;
                } else if (strng.equals("&amp;")) {
                    jdx = ('&');
                    break Bfin;
                } else if (strng.equals("&quot;")) {
                    jdx = ('"');
                    break Bfin;
                } else if (strng.equals("&apos;")) {
                    jdx = ('\'');
                    break Bfin;
                } else if (strng.equals("&nbsp;")) {
                    jdx = (' ');
                    break Bfin;
                } else return ww_Err(caps, xpr, caller, ('&' == (strng.charAt(0)) ? "ec102" : "ec103"), vbyts, null);
            } else return ww_Err(caps, xpr, caller, "ec103", vbyts, null);
        }
        if (a2b(jdx) && 0 <= (jdx) && (jdx) <= 0x1fffff) return bytesAppend(bytes((byte) (7)), integerToIbyts(jdx)); else return ww_Err(caps, xpr, caller, "ec101", vbyts, null);
    }

    public static final int wwi_ExceptionClass = 0xc;

    public static final byte[] ww_Exception = bytes((byte) (0x20), (byte) (0xc));

    public static byte[] ww_GetFloc(Caps caps, byte[] xpr) {
        Han ahan = han_Annote(caps);
        if (a2b(xpr)) if (a2b(ahan)) if (0x14 <= ((xpr[0] & 0xFF)) && ((xpr[0] & 0xFF)) <= 0x1f) return bt_Get(ahan, xpr); else return null; else return null; else return null;
    }

    public static byte[] ww_ErrFill(Caps caps, byte[] xpr, String caller, String msgcode, byte[] obj, byte[] env) {
        byte[] lbyts = (a2b(xpr) ? (0x14 <= ((xpr[0] & 0xFF)) && ((xpr[0] & 0xFF)) <= 0x1f ? ww_GetFloc(caps, xpr) : (0xf0 <= ((xpr[0] & 0xFF)) && ((xpr[0] & 0xFF)) <= 0xff ? xpr : null)) : null);
        {
            int fdx = (a2b(lbyts) ? ww_Next(lbyts, 0) : 0);
            byte[] fbyts = (a2b(lbyts) ? subbytes(lbyts, 0, fdx) : null);
            int ndx = (a2b(lbyts) ? ww_Next(lbyts, fdx) : 0);
            byte[] nbyts = (a2b(lbyts) ? subbytes(lbyts, fdx, ndx) : null);
            int ldx = (a2b(lbyts) ? ww_Next(lbyts, ndx) : 0);
            byte[] sbyts = (a2b(lbyts) ? subbytes(lbyts, ndx, ldx) : null);
            int edx = (a2b(lbyts) ? ww_Next(lbyts, ldx) : 0);
            byte[] ebyts = (a2b(lbyts) ? ((ldx) < (edx) ? subbytes(lbyts, ldx, edx) : null) : null);
            byte[] inst = ww_NewInstance(caps, ww_PrimClass(caps, wwi_ExceptionClass));
            Han han = han_Data(caps);
            if (a2b(lbyts) && (edx) != (lbyts.length)) dprintf("WARNING: Junk at end of locative '" + (subbytes(lbyts, edx, lbyts.length)) + "'\n");
            if (a2b(fbyts)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "source")), fbyts);
            if (a2b(nbyts)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "line_number")), nbyts);
            if (a2b(sbyts)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "start")), sbyts);
            if (a2b(ebyts)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "end")), ebyts);
            if (a2b(caller)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "context")), stringToImsbyts(caller));
            if (a2b(msgcode)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "id")), stringToImsbyts(msgcode));
            if (a2b(obj)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "datum")), obj);
            return inst;
        }
    }

    public static byte[] ww_FillMessage(Caps caps, byte[] inst, byte[] cls, String msgcode, String msgstring) {
        Han han = han_Data(caps);
        if (a2b(cls)) bt_Put(han, inst, cls);
        if (a2b(msgstring)) bt_Put(han, bytesAppend(inst, ww_InternString(caps, "message")), stringToImsbyts(msgstring));
        return inst;
    }

    public static final int wwi_W = 0xd;

    public static final byte[] ww_W = bytes((byte) (0x20), (byte) (0xd));

    public static final int wwi__W = 0xe;

    public static final byte[] ww__W = bytes((byte) (0x20), (byte) (0xe));

    public static final int wwi__W_uri_path = 0xf;

    public static final byte[] ww__W_uri_path = bytes((byte) (0x20), (byte) (0xf));

    public static final int wwi__Process_browser_request = 0x10;

    public static final byte[] ww__Process_browser_request = bytes((byte) (0x20), (byte) (0x10));

    public static final int wwi__Format_browser_object = 0x11;

    public static final byte[] ww__Format_browser_object = bytes((byte) (0x20), (byte) (0x11));

    public static final int wwi_HighestIndex = 0x11;

    public static byte[][] ww_PrimClassSyms = { (ww_Wob), (ww_Class), (ww_Boolean), (ww_String), (ww_Double), (ww_Integer), (ww_Number), (ww_Vector), (ww_Bytes), (ww_Thing), (ww_Method), (ww_Char), (ww_Exception), (ww_W), (ww__W), (ww__W_uri_path), (ww__Process_browser_request), (ww__Format_browser_object) };

    public static String[] ww_PrimClassNames = { "wob", "class", "boolean", "string", "double", "integer", "number", "vector", "bytes", "thing", "method", "char", "exception", "w", "_w", "_w_uri_path", "_process_browser_request", "_format_browser_object" };

    public static byte[] ww_PrimClass(Caps caps, int idx) {
        return bt_Get(han_Stack(caps), bytesAppend(ww_Zero, ww_PrimClassSyms[idx]));
    }

    public static void ww_InstallClasses(Caps caps) {
        {
            int idx = 0;
            while (!((idx) > (wwi_HighestIndex))) {
                {
                    byte[] byts = ww_PrimClassSyms[idx];
                    byte[] sbyts = stringToBytes(ww_PrimClassNames[idx]);
                    bt_Put(han_Symbols(caps), sbyts, byts);
                    bt_Put(han_Slobmys(caps), byts, sbyts);
                }
                {
                    idx = 1 + (idx);
                }
            }
            bt_Put(han_Symbols(caps), emptyBytes, integerToIbyts(idx));
            return;
        }
    }

    public static final byte[] ww_Null = bytes((byte) (sym_Null));

    public static final byte[] ww_False = bytes((byte) (sym_False));

    public static final byte[] ww_True = bytes((byte) (sym_True));

    public static final byte[] ww_Opt = bytes((byte) (sym_Opt));

    public static final byte[] ww__Name = bytes((byte) (sym__Name));

    public static final byte[] ww__Subject = bytes((byte) (sym__Subject));

    public static final byte[] ww__Subject_of_call = bytes((byte) (sym__Subject_of_call));

    public static final byte[] ww__Other_unkeyed = bytes((byte) (sym__Other_unkeyed));

    public static final byte[] ww__Other_keyed = bytes((byte) (sym__Other_keyed));

    public static final byte[] ww__Lookup = bytes((byte) (sym__Lookup));

    public static final byte[] ww_To_char = bytes((byte) (sym_To_char));

    public static final byte[] ww_Insert = bytes((byte) (sym_Insert));

    public static final byte[] ww_Remove = bytes((byte) (sym_Remove));

    public static final byte[] ww_To_cxs = bytes((byte) (sym_To_cxs));

    public static final byte[] ww_Equal = bytes((byte) (sym_Equal));

    public static final byte[] ww_Make = bytes((byte) (sym_Make));

    public static final byte[] ww_Vector_key = bytes((byte) (sym_Vector_key));

    public static byte[] ww_One = integerToIbyts(1);

    public static byte[] ww_Zero = integerToIbyts(0);

    public static final byte[] ww_BytsFf00 = bytes((byte) (0xff), (byte) (0));

    public static final byte[] ww_BiggestInstance = bytes((byte) (0xb), (byte) (0xff), (byte) (0xff), (byte) (0xff), (byte) (0xff));

    public static final byte[] emptyBytes = bytes();

    public static final byte[] emptySbyts = bytes((byte) (0xf0));

    public static final byte[] dotSbyts = bytes((byte) (0xf1), (byte) (0x2e));

    public static byte[] ww_ExpressionContinue = bytes((byte) (pc_ExpressionContinue));

    public static final byte[] ww_SuperstringIdCtr = ww_Zero;

    public static boolean vicinitySuffix_P(int chr) {
        switch(chr) {
            case ':':
                return (true);
            case '\\':
                return (true);
            case '/':
                return (true);
            default:
                return false;
        }
    }

    public static String filenameToVicinity(String filename) {
        {
            int i = (filename.length()) - 1;
            Lloop: while (true) {
                if (0 > (i)) return ""; else if (vicinitySuffix_P(filename.charAt(i))) return filename.substring(0, ((i) + 1)); else {
                    i = (i) - 1;
                    continue Lloop;
                }
            }
        }
    }

    public static String loadingFolder(Caps caps) {
        String filename = getLoadingFilename(caps);
        if (a2b(filename)) return filenameToVicinity(filename); else return null;
    }

    public static String removePrefix(String str, String prefix) {
        int plen = prefix.length();
        int slen = str.length();
        if ((slen) < (plen)) return null; else if (prefix.equals(str.substring(0, (plen)))) return str.substring((plen), (slen)); else return null;
    }

    public static String inVicinity(String prefix, String str) {
        LinVicinity: while (true) {
            {
                int plen = prefix.length();
                int slen = str.length();
                if ((plen) > 3 && vicinitySuffix_P(prefix.charAt(-1 + (plen))) && (slen) > 3 && '.' == (str.charAt(0)) && '.' == (str.charAt(1)) && vicinitySuffix_P(str.charAt(2))) {
                    int idx = -2 + (plen);
                    while (!((0 > (idx) || vicinitySuffix_P(prefix.charAt(idx))))) {
                        {
                            idx = -1 + (idx);
                        }
                    }
                    if (vicinitySuffix_P(prefix.charAt(idx))) {
                        prefix = prefix.substring(0, (1 + (idx)));
                        str = str.substring(3, (slen));
                        continue LinVicinity;
                    } else return stringAppend(prefix, str);
                } else return stringAppend(prefix, str);
            }
        }
    }

    public static int strFlushWs(String str, int sdx) {
        int slen = str.length();
        {
            int idx = sdx;
            Lloop: while (true) {
                if ((idx) >= (slen)) return idx; else if (charWhitespace_P(str.charAt(idx))) {
                    idx = 1 + (idx);
                    continue Lloop;
                } else return idx;
            }
        }
    }

    public static String strReadString(String str, int sdx) {
        int slen = str.length();
        if ((2 + (sdx)) > (slen)) return null; else if (('"' == (str.charAt(sdx)) || '\'' == (str.charAt(sdx)))) {
            int chr = str.charAt(sdx);
            {
                int jdx = 1 + (sdx);
                Lsloop: while (true) {
                    if ((jdx) >= (slen)) return null; else if ((chr) == (str.charAt(jdx))) return str.substring((1 + (sdx)), (jdx)); else {
                        jdx = 1 + (jdx);
                        continue Lsloop;
                    }
                }
            }
        } else return null;
    }

    public static String strReadToCloseBracket(String str, int sdx) {
        int edx = str.indexOf("/>", (sdx));
        if (0 < (edx)) return str.substring((sdx), (edx)); else return null;
    }

    public static String trimTrailingWs(String str) {
        {
            int idx = -1 + (str.length());
            Lloop: while (true) {
                if (0 > (idx)) return ""; else if (charWhitespace_P(str.charAt(idx))) {
                    idx = -1 + (idx);
                    continue Lloop;
                } else return str.substring(0, (1 + (idx)));
            }
        }
    }

    public static Logicals parseLogicals(String str) {
        int slen = str.length();
        int sdx = strFlushWs(str, 0);
        Logicals logicals = null;
        if (str.startsWith("<logical", (sdx))) {
            sdx = strFlushWs(str, (sdx) + ("<logical".length()));
            {
                int idx = sdx;
                Loloop: while (true) {
                    {
                        int edx = str.indexOf("=", (idx));
                        if ((idx) >= (slen)) {
                            dprintf(">>>>ERROR<<<< missing value after '=' in logical attribute\n");
                            return null;
                        } else if (0 < (edx)) {
                            String fname = str.substring((idx), (edx));
                            fname = trimTrailingWs(fname);
                            edx = strFlushWs(str, 1 + (edx));
                            if (str.startsWith("<resource", (edx))) {
                                edx = strFlushWs(str, (edx) + ("<resource".length()));
                                {
                                    String filename = strReadString(str, edx);
                                    if (a2b(filename)) edx = strFlushWs(str, (edx) + 2 + (filename.length()));
                                    if (!(a2b(filename))) {
                                        dprintf(">>>>ERROR<<<< missing filename in resource\n");
                                        return null;
                                    } else if (0 == (filename.length())) {
                                        dprintf(">>>>ERROR<<<< null filename in resource\n");
                                        return null;
                                    } else if (((edx) + 2) >= (slen)) {
                                        dprintf(">>>>ERROR<<<< missing permissions in resource\n");
                                        return null;
                                    } else {
                                        String permstring = strReadToCloseBracket(str, edx);
                                        if (a2b(permstring)) {
                                            int chr0 = filename.charAt(-1 + (filename.length()));
                                            if (('/' == (chr0) || '\\' == (chr0))) fname = stringAppend(fname, "/");
                                            edx = 2 + (edx) + (permstring.length());
                                            permstring = stringAppend(" ", trimTrailingWs(permstring), " ");
                                            logicals = makeLogicals(logicals, fname, filename, permstring);
                                            edx = strFlushWs(str, edx);
                                        }
                                        if (!(a2b(permstring))) {
                                            dprintf(">>>>ERROR<<<< could not read permissions in resource\n");
                                            return null;
                                        } else if (((edx) + 1) >= (slen)) {
                                            dprintf(">>>>ERROR<<<< missing close after resource\n");
                                            return null;
                                        } else if (str.startsWith("/>", (edx))) return logicals; else {
                                            idx = edx;
                                            continue Loloop;
                                        }
                                    }
                                }
                            } else {
                                dprintf(">>>>ERROR<<<< missing resource after '=' in logical attribute\n");
                                return null;
                            }
                        } else {
                            dprintf(">>>>ERROR<<<< missing '=' in logical attribute\n");
                            return null;
                        }
                    }
                }
            }
        } else {
            dprintf(">>>>ERROR<<<< logicals string must begin with '<logical'\n");
            return null;
        }
    }

    public static String logicalResolve(Caps caps, String lstr, String permStr, String caller) {
        if ((permStr.length()) <= 3) {
            if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": unknown permission '" + (permStr) + "' for capability " + (lstr) + "\n");
            return null;
        } else {
            String sstr = removePrefix(lstr, "logical://");
            boolean vetted_P = false;
            if (a2b(sstr)) {
                Logicals logicals = getLogicals(caps);
                String fname = "loading_folder/";
                String filename = loadingFolder(caps);
                String permstring = " read ";
                Lloop: while (true) {
                    {
                        int sidx = sstr.indexOf('/');
                        String pstr = null;
                        String rstr = null;
                        if (!(0 > (sidx))) {
                            pstr = sstr.substring(0, (1 + (sidx)));
                            rstr = sstr.substring((1 + (sidx)), (sstr.length()));
                        } else pstr = sstr;
                        if (pstr.equals(fname)) {
                            String nstr = (a2b(filename) ? (0 > (sidx) ? filename : stringAppend(filename, rstr)) : null);
                            String tstr = (a2b(nstr) ? removePrefix(nstr, "logical://") : null);
                            if (!(vetted_P) && 0 > (permstring.indexOf((stringAppend(" ", permStr, " ")), 0))) {
                                if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": capability for " + (lstr) + " lacks " + (permStr) + " permission\n");
                                return null;
                            } else if (!(a2b(filename))) {
                                if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": loading_folder unset (to resolve " + (lstr) + ")\n");
                                return null;
                            } else if (!(a2b(tstr))) return nstr; else if (a2b(logicals)) {
                                vetted_P = (true);
                                sstr = tstr;
                                {
                                    Logicals T_logicals = logicalsNext(logicals);
                                    fname = logicalsFname(logicals);
                                    filename = logicalsFilename(logicals);
                                    permstring = logicalsPermstring(logicals);
                                    logicals = T_logicals;
                                    continue Lloop;
                                }
                            } else {
                                if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": no capability matches " + (lstr) + "\n");
                                return null;
                            }
                        } else if (a2b(logicals)) {
                            Logicals T_logicals = logicalsNext(logicals);
                            fname = logicalsFname(logicals);
                            filename = logicalsFilename(logicals);
                            permstring = logicalsPermstring(logicals);
                            logicals = T_logicals;
                            continue Lloop;
                        } else {
                            if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": no capability matches " + (pstr) + " (" + (lstr) + ")\n");
                            return null;
                        }
                    }
                }
            } else if (!("read".equals(permStr))) {
                if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": default capability (for " + (lstr) + ") lacks " + (permStr) + " permission; only 'read' allowed\n");
                return null;
            } else if (!(a2b(loadingFolder(caps)))) {
                if (a2b(caller)) dprintf(">>>>ERROR<<<< " + (caller) + ": loading_folder not set (to resolve " + (lstr) + ")\n");
                return null;
            } else return inVicinity(loadingFolder(caps), lstr);
        }
    }

    public static void spinLocked(String caller) {
        dprintf(">>>>ERROR<<<< Spin-locked in " + (caller) + "\n");
        System.exit(1);
    }

    public static byte[] ww_Intern(Caps caps, byte[] bts) {
        {
            byte[] T_2ww_Intern = bt_Get(han_Symbols(caps), bts);
            if (a2b(T_2ww_Intern)) return T_2ww_Intern; else {
                byte[] symIdxByts = bt_Rem(han_Symbols(caps), emptyBytes);
                int cnt = 0x63;
                Lloop: while (true) {
                    if (0 > (cnt)) {
                        spinLocked("ww_Intern");
                        return null;
                    } else if (!(a2b(symIdxByts))) {
                        symIdxByts = bt_Rem(han_Symbols(caps), emptyBytes);
                        cnt = -1 + (cnt);
                        continue Lloop;
                    } else {
                        int symIdx = ibytsToInteger(symIdxByts);
                        bt_Put(han_Symbols(caps), emptyBytes, integerToIbyts(1 + (symIdx)));
                        {
                            byte[] symidxByts = naturalToNbyts(0x20, symIdx);
                            bt_Put(han_Symbols(caps), bts, symidxByts);
                            bt_Put(han_Slobmys(caps), symidxByts, bts);
                            return symidxByts;
                        }
                    }
                }
            }
        }
    }

    public static void ww_InstallSymbols(Caps caps, String filename) {
        Inport iprt = null;
        if (!(fileExists_P(filename))) {
            dprintf(">>>>ERROR<<<< File missing: \"" + (filename) + "\"\n");
            return;
        } else {
            iprt = openInputFile(filename);
            {
                int chr0 = ('0' == (readChar(iprt)) ? ('x' == (readChar(iprt)) ? readChar(iprt) : 0) : 0);
                while (!((eofObject_P(chr0) || !(a2b(chr0))))) {
                    {
                        int hex = (0x10 * (hexDigitToInteger(chr0))) + (hexDigitToInteger(readChar(iprt)));
                        byte[] bts = new byte[0x32];
                        readChar(iprt);
                        {
                            int idx = 0;
                            int chr = readChar(iprt);
                            while (!((eofObject_P(chr) || charWhitespace_P(chr) || (idx) >= 0x32))) {
                                bts[idx] = (byte) (0xff & ((chr)));
                                {
                                    idx = 1 + (idx);
                                    chr = readChar(iprt);
                                }
                            }
                            bts = subbytes(bts, 0, idx);
                        }
                        if ('\n' == (peekChar(iprt))) readChar(iprt);
                        {
                            byte[] byts = bytes((byte) (hex));
                            bt_Put(han_Symbols(caps), bts, byts);
                            bt_Put(han_Slobmys(caps), byts, bts);
                        }
                    }
                    {
                        chr0 = ('0' == (readChar(iprt)) ? ('x' == (readChar(iprt)) ? readChar(iprt) : 0) : 0);
                    }
                }
                if (!(atEof_P(iprt))) {
                    if (a2b(chr0)) {
                        dprintf("chr0 = " + ((chr0)) + " (" + (chr0) + "); peek = " + ((peekChar(iprt))) + " (" + (peekChar(iprt)) + ")\n");
                    } else dprintf("chr0 = #f; peek = " + ((peekChar(iprt))) + " (" + (peekChar(iprt)) + ")\n");
                    dprintf(">>>>ERROR<<<< ww:install-symbols! did not reach end of \"" + (filename) + "\"\n");
                }
                closeInputPort(iprt);
                ww_InstallClasses(caps);
                return;
            }
        }
    }

    public static boolean keyable_P(byte[] obj) {
        int byt0 = (obj[0] & 0xFF);
        return (0x20 <= (byt0) && (byt0) <= (sym_TopPrim) || 0xd6 <= (byt0) && (byt0) <= 0xde || 0xf0 <= (byt0) && (byt0) <= 0xff);
    }

    public static boolean bindable_P(byte[] obj) {
        return 0x20 <= ((obj[0] & 0xFF)) && ((obj[0] & 0xFF)) <= (sym_TopBindable);
    }

    public static boolean wobable_P(byte[] obj) {
        int byt0 = (obj[0] & 0xFF);
        return (0x20 <= (byt0) && (byt0) <= 0x23 || (sym_BottomWobable) <= (byt0) && (byt0) <= (sym_TopMeth));
    }

    public static boolean isSym_P(byte[] obj, int code) {
        return a2b(obj) && (code) == ((obj[0] & 0xFF));
    }

    public static boolean escape_P(byte[] obj) {
        return (!(a2b(obj)) || (pfx_Error) <= ((obj[0] & 0xFF)) && ((obj[0] & 0xFF)) <= (pfx_Break));
    }

    public static boolean bytsPrefix_P(byte[] pbyts, byte[] byts) {
        int plen = pbyts.length;
        return (plen) <= (byts.length) && bytesEqual_P(pbyts, subbytes(byts, 0, plen));
    }

    public static byte[] ww_DoHas(Caps caps, byte[] subj, byte[] key) {
        int byt0 = (subj[0] & 0xFF);
        if (bytesEqual_P(ww_Wob, subj)) if (isSym_P(key, sym__Name)) return ww_True; else if (a2b(bt_Get(han_Stack(caps), bytesAppend(ww_Zero, key)))) return ww_True; else return ww_False; else if (8 <= (byt0) && (byt0) <= 0xb) if (isSym_P(key, sym__Parent)) return ww_True; else if (isSym_P(key, sym__Container)) return ww_True; else if (a2b(bt_Get(han_Data(caps), bytesAppend(subj, key)))) return ww_True; else return ww_False; else if (0x14 <= (byt0) && (byt0) <= 0x17) if (isSym_P(key, sym__Parent)) return ww_True; else if (isSym_P(key, sym__Container)) return ww_True; else if (a2b(bt_Get(han_Program(caps), bytesAppend(subj, key)))) return ww_True; else return ww_False; else if (ww_Natural_P(key) && (ibytsToInteger(key)) < (ilength(caps, subj))) return ww_True; else return ww_False;
    }

    public static byte[] ww_DoStringGet(Caps caps, byte[] subj, int idx) {
        Lww_DoStringGet: while (true) {
            {
                int byt0 = (subj[0] & 0xFF);
                int slen = ilength(caps, subj);
                byte[] key = integerToIbyts(idx);
                int cdpt;
                Bfinish: while (true) {
                    if ((idx) >= (slen)) return null; else if (0xf0 <= (byt0) && (byt0) <= 0xff) {
                        cdpt = offBytsUtf8Codepoint(subj, (0xff == (byt0) ? 2 : 1), idx);
                        break Bfinish;
                    } else if (0xc <= (byt0) && (byt0) <= 0x13) {
                        Han bt = (0xc <= (byt0) && (byt0) <= 0xf ? han_Data(caps) : han_Program(caps));
                        byte[] nbyts = bt_Next(bt, bytesAppend(subj, key));
                        byte[] shbts = bt_Get(bt, nbyts);
                        {
                            byte[] T_subj = shbts;
                            idx = (idx) - ((ibytsToInteger(subbytes(nbyts, subj.length, nbyts.length))) - (ilength(caps, shbts)));
                            subj = T_subj;
                            continue Lww_DoStringGet;
                        }
                    } else {
                        byte[] bts = bt_Get(han_Slobmys(caps), subj);
                        if (!(a2b(bts))) return null; else if ((idx) < (utf8Length(bts))) {
                            cdpt = offBytsUtf8Codepoint(bts, 0, idx);
                            break Bfinish;
                        } else return null;
                    }
                }
                if (0 > (cdpt)) return null; else return bytesAppend(bytes((byte) (7)), integerToIbyts(cdpt));
            }
        }
    }

    public static byte[] ww_DoGet(Caps caps, byte[] subj, byte[] key) {
        int byt0 = (subj[0] & 0xFF);
        if (bytesEqual_P(ww_Wob, subj)) return bt_Get(han_Stack(caps), bytesAppend(ww_Zero, key)); else if (isSym_P(key, sym__Name)) return ww_GetName(caps, subj); else if (isSym_P(key, sym__Parent)) {
            byte[] T_4ww_DoGet = ww_GetParent(caps, subj);
            if (a2b(T_4ww_DoGet)) return T_4ww_DoGet; else return ww_False;
        } else if (isSym_P(key, sym__Container)) return ww_GetContainer(caps, subj); else if (8 <= (byt0) && (byt0) <= 0xb) return bt_Get(han_Data(caps), bytesAppend(subj, key)); else if (0x14 <= (byt0) && (byt0) <= 0x17) return bt_Get(han_Program(caps), bytesAppend(subj, key)); else if (!(ww_Integer_P(key))) return null; else if (ww_String_P(subj)) return ww_DoStringGet(caps, subj, ibytsToInteger(key)); else return null;
    }

    public static byte[] ww_GetName(Caps caps, byte[] subj) {
        {
            int typ = (subj[0] & 0xFF);
            if (8 <= (typ) && (typ) <= 0xb) return bt_Get(han_Data(caps), bytesAppend(subj, bytes((byte) (sym__Name)))); else if (0x14 <= (typ) && (typ) <= 0x17) return bt_Get(han_Program(caps), bytesAppend(subj, bytes((byte) (sym__Name)))); else return null;
        }
    }

    public static byte[] ww_GetExistingInstance(Caps caps, byte[] prnt, byte[] name) {
        return bt_Get(han_Data(caps), bytesAppend(prnt, name));
    }

    public static byte[] ww_InstanceToParent(Caps caps, byte[] subj) {
        byte[] prnt = bt_Get(han_Data(caps), subj);
        if (a2b(prnt)) return subbytes(prnt, 0, ww_Next(prnt, 0)); else return null;
    }

    public static byte[] ww_InstanceToContainer(Caps caps, byte[] subj) {
        byte[] prnt = bt_Get(han_Data(caps), subj);
        {
            int bdx = ww_Next(prnt, 0);
            if (!(a2b(prnt))) return prnt; else if ((bdx) == (prnt.length)) return subbytes(prnt, 0, bdx); else return subbytes(prnt, bdx, ww_Next(prnt, bdx));
        }
    }

    public static byte[] ww_GetParent(Caps caps, byte[] subj) {
        {
            int typ = (subj[0] & 0xFF);
            if (2 <= (typ) && (typ) <= 3) return ww_PrimClass(caps, wwi_BooleanClass); else if (7 == (typ)) return ww_PrimClass(caps, wwi_CharClass); else if (8 <= (typ) && (typ) <= 0xb) return ww_InstanceToParent(caps, subj); else if (0xc <= (typ) && (typ) <= 0x13) return ww_PrimClass(caps, wwi_StringClass); else if (0x14 <= (typ) && (typ) <= 0x17) return ww_PrimClass(caps, wwi_MethodClass); else if (bytesEqual_P(ww_Wob, subj)) return null; else if (0x20 <= (typ) && (typ) <= 0x23) return ww_PrimClass(caps, wwi_StringClass); else if ((sym_BotMeth) <= (typ) && (typ) <= (sym_TopMeth)) return ww_PrimClass(caps, wwi_MethodClass); else if (0xcb == (typ)) return ww_PrimClass(caps, wwi_DoubleClass); else if (0xcd <= (typ) && (typ) <= 0xdf) return ww_PrimClass(caps, wwi_IntegerClass); else if (0xf0 <= (typ) && (typ) <= 0xff) return ww_PrimClass(caps, wwi_StringClass); else return ww_Wob;
        }
    }

    public static byte[] ww_GetContainer(Caps caps, byte[] subj) {
        {
            int typ = (subj[0] & 0xFF);
            if (8 <= (typ) && (typ) <= 0xb) {
                byte[] prnt = ww_InstanceToContainer(caps, subj);
                byte[] name = ww_GetName(caps, subj);
                if (a2b(prnt) && a2b(name)) {
                    byte[] obj = ww_DoGet(caps, prnt, name);
                    if (a2b(obj) && bytesEqual_P(obj, subj)) return prnt; else return ww_False;
                } else return ww_False;
            } else if (0x14 <= (typ) && (typ) <= 0x17) {
                byte[] body = bt_Get(han_Program(caps), subj);
                int bdx = ww_Next(body, 0);
                return subbytes(body, bdx, ww_Next(body, bdx));
            } else if ((sym_Even) <= (typ) && (typ) <= (sym_Odd)) return ww_PrimClass(caps, wwi_IntegerClass); else if (((sym_Sin) <= (typ) && (typ) <= (sym_Abs) || (sym_Plus) <= (typ) && (typ) <= (sym_Atan2))) return ww_PrimClass(caps, wwi_NumberClass); else if ((sym_BotMeth) <= (typ) && (typ) <= (sym_TopPrim)) return ww_Wob; else return ww_False;
        }
    }

    public static byte[] ww_IsA(Caps caps, byte[] subj, byte[] cls) {
        {
            byte[] prnt = ww_GetParent(caps, subj);
            Lloop: while (true) {
                if (!(a2b(prnt))) return ww_False; else if (bytesEqual_P(prnt, cls)) return ww_True; else if (bytesEqual_P(ww_Wob, prnt)) return ww_False; else if (8 <= ((prnt[0] & 0xFF)) && ((prnt[0] & 0xFF)) <= 0xb) {
                    prnt = ww_InstanceToParent(caps, prnt);
                    continue Lloop;
                } else return ww_False;
            }
        }
    }

    public static int ww_SameTypeNumericCmp2(byte[] byts1, byte[] byts2) {
        if (bytesEqual_P(byts2, byts1)) return 0; else if (bytesMore_P(byts2, byts1)) return -1; else return 1;
    }

    public static int ilength(Caps caps, byte[] subj) {
        int subjlen = (a2b(subj) ? subj.length : 0);
        if ((!(a2b(subjlen)) || 0 == (subjlen))) return 0; else if (0xf0 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0xfe) return 0xf & ((subj[0] & 0xFF)); else if (0xff == ((subj[0] & 0xFF)) && 1 < (subjlen)) return (subj[1] & 0xFF); else if (8 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0x13) {
            byte[] endbyts = bt_Prev((0x10 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0x13 ? han_Program(caps) : han_Data(caps)), bytesAppend(subj, bytes((byte) (0xdf))));
            int len = (a2b(endbyts) ? endbyts.length : 0);
            if (!(a2b(endbyts))) return 0; else if ((len) > (subjlen) && bytesEqual_P(subj, subbytes(endbyts, 0, subjlen))) {
                byte[] key = subbytes(endbyts, subjlen, len);
                if (ww_Integer_P(key)) if (0xc <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0x13) return ibytsToInteger(key); else return 1 + (ibytsToInteger(key)); else return 0;
            } else return 0;
        } else if (0x20 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0x23) {
            byte[] bts = bt_Get(han_Slobmys(caps), subj);
            if (!(a2b(bts))) return 0; else return utf8Length(bts);
        } else return 0;
    }

    public static byte[] ww_Length(Caps caps, byte[] subj) {
        return integerToIbyts(ilength(caps, subj));
    }

    public static byte[] ww_SymbolToSbyts(Caps caps, byte[] obj) {
        byte[] vbyts = bt_Get(han_Slobmys(caps), obj);
        if (a2b(vbyts)) {
            int vlen = utf8Length(vbyts);
            if ((vlen) <= 0xe) {
                byte[] nbyts = new byte[1];
                nbyts[0] = (byte) (0xff & (0xf0 | (vlen)));
                return bytesAppend(nbyts, vbyts);
            } else {
                byte[] nbyts = new byte[2];
                nbyts[0] = (byte) (0xff & 0xff);
                nbyts[1] = (byte) (0xff & (vlen));
                return bytesAppend(nbyts, vbyts);
            }
        } else return null;
    }

    public static byte[] ww_Dekeyify(Caps caps, byte[] obj) {
        int byt0 = (a2b(obj) ? (obj[0] & 0xFF) : 0);
        if (escape_P(obj)) return obj; else if ((byt0) == (sym_Null)) return obj; else if (7 == (byt0)) return obj; else if ((0xc <= (byt0) && (byt0) <= 0x13 || 0xf0 <= (byt0) && (byt0) <= 0xff)) return obj; else if (ww_Number_P(obj)) return obj; else {
            byte[] T_2ww_Dekeyify = ww_SymbolToSbyts(caps, obj);
            if (a2b(T_2ww_Dekeyify)) return T_2ww_Dekeyify; else {
                dprintf(">>>>ERROR<<<< strange key type: " + (ww_ObjectToString(caps, obj)) + "\n");
                return null;
            }
        }
    }

    public static byte[] ww_DekeyifyBody(Caps caps, byte[] body) {
        int blen = body.length;
        {
            int bdx = 0;
            byte[] bts = emptyBytes;
            int tlen = 0;
            Lloop: while (true) {
                if ((bdx) < (blen)) {
                    int ndx = ww_Next(body, bdx);
                    byte[] obj = ww_Dekeyify(caps, subbytes(body, bdx, ndx));
                    int nlen = (a2b(obj) ? obj.length : 0);
                    if (!(a2b(obj))) return null; else if (((tlen) + (nlen)) > 0xfd) return null; else {
                        bdx = ndx;
                        bts = bytesAppend(bts, obj);
                        tlen = (tlen) + (nlen);
                        continue Lloop;
                    }
                } else return bts;
            }
        }
    }

    public static byte[] ww_KeyToSbyts(Caps caps, byte[] obj) {
        int byt0 = (a2b(obj) ? (obj[0] & 0xFF) : 0);
        if (escape_P(obj)) return obj; else if ((byt0) == (sym_Null)) return emptySbyts; else if ((0xc <= (byt0) && (byt0) <= 0x13 || 0xf0 <= (byt0) && (byt0) <= 0xff)) return obj; else if (7 == (byt0)) return ww_CodepointToSbyts(ibytsToInteger(subbytes(obj, 1, obj.length))); else if (0xcd <= (byt0) && (byt0) <= 0xdf) return stringToImsbyts(ibytsToString(obj)); else if (0xcb == (byt0)) return stringToImsbyts(dbytsToString(obj)); else {
            byte[] T_2ww_KeyToSbyts = ww_SymbolToSbyts(caps, obj);
            if (a2b(T_2ww_KeyToSbyts)) return T_2ww_KeyToSbyts; else {
                dprintf(">>>>ERROR<<<< to_string could not convert " + (ww_ObjectToString(caps, obj)) + "\n");
                return null;
            }
        }
    }

    public static byte[] ww_NormalizeSbyts(byte[] sbyts) {
        if (0xff == ((sbyts[0] & 0xFF)) && ((sbyts[1] & 0xFF)) <= 0xe) return bytesAppend(bytes((byte) (0xf0 | ((sbyts[1] & 0xFF)))), subbytes(sbyts, 2, sbyts.length)); else return sbyts;
    }

    public static byte[] ww_DenormalizeSbyts(byte[] sbyts) {
        if (0xf0 <= ((sbyts[0] & 0xFF)) && ((sbyts[0] & 0xFF)) <= 0xfe) {
            byte[] nbyts = bytesAppend(bytes((byte) (0xff)), sbyts);
            nbyts[1] = (byte) (0xff & (0xf & ((sbyts[0] & 0xFF))));
            return nbyts;
        } else return sbyts;
    }

    public static byte[] ww_JoinVector(Caps caps, byte[] subj, byte[] xpr, byte[] env) {
        Han ihan = han_Data(caps);
        byte[] ebyts = ww_Length(caps, subj);
        byte[] vbyts = emptySbyts;
        {
            byte[] ukbyts = ww_Zero;
            while (!((escape_P(vbyts) || !(bytesMore_P(ebyts, ukbyts))))) {
                {
                    byte[] rbyts = bt_Get(ihan, bytesAppend(subj, ukbyts));
                    if (escape_P(rbyts)) {
                        vbyts = rbyts;
                    } else {
                        rbyts = ww_KeyToSbyts(caps, rbyts);
                        if (escape_P(rbyts)) {
                            vbyts = rbyts;
                        } else vbyts = ww_DoJoin2(caps, vbyts, rbyts, "join", xpr, env);
                    }
                }
                {
                    ukbyts = incrementIbyts(ukbyts);
                }
            }
            return vbyts;
        }
    }

    public static byte[] ww_KeyAppend2(Caps caps, byte[] subj, byte[] sbyts) {
        subj = ww_KeyToSbyts(caps, subj);
        sbyts = ww_KeyToSbyts(caps, sbyts);
        {
            int udx = (0xff == ((subj[0] & 0xFF)) ? 2 : 1);
            int vdx = (0xff == ((sbyts[0] & 0xFF)) ? 2 : 1);
            int rlen = (subj.length) - (udx);
            int clen = (rlen) + ((sbyts.length) - (vdx));
            byte[] rbyts = null;
            if ((clen) > 0xfd) {
                dprintf(">>>>ERROR<<<< path too long " + (ww_ExpressionToString(caps, subj)) + "." + (ww_ExpressionToString(caps, sbyts)) + "\n");
                return null;
            } else {
                rbyts = bytesAppend(bytesAppend(ww_BytsFf00, subbytes(subj, udx, (udx) + (rlen))), subbytes(sbyts, vdx, (vdx) + ((clen) - (rlen))));
                rbyts[1] = (byte) (0xff & (clen));
                return ww_NormalizeSbyts(rbyts);
            }
        }
    }

    public static byte[] ww_ToPath(Caps caps, byte[] subj) {
        byte[] pbyts = null;
        if (bytesEqual_P(ww_Wob, subj)) return ww_Dekeyify(caps, subj); else {
            byte[] sbyts = subj;
            Lloop: while (true) {
                {
                    byte[] name = ww_GetName(caps, sbyts);
                    if (a2b(name)) {
                        pbyts = (a2b(pbyts) ? ww_KeyAppend2(caps, name, ww_KeyAppend2(caps, dotSbyts, pbyts)) : ww_Dekeyify(caps, name));
                        {
                            byte[] prnt = ww_GetContainer(caps, sbyts);
                            if (bytesEqual_P(ww_Wob, prnt)) return pbyts; else {
                                sbyts = prnt;
                                continue Lloop;
                            }
                        }
                    } else return null;
                }
            }
        }
    }

    public static byte[] ww_DoLookup(Caps caps, byte[] subj, byte[] key) {
        {
            byte[] T_2ww_DoLookup = ww_DoGet(caps, subj, key);
            if (a2b(T_2ww_DoLookup)) return T_2ww_DoLookup; else if (ww_Integer_P(key)) return null; else {
                byte[] prnt = ww_GetParent(caps, subj);
                Lloop: while (true) {
                    if (!(a2b(prnt))) return null; else {
                        byte[] T_6ww_DoLookup = ww_DoGet(caps, prnt, key);
                        if (a2b(T_6ww_DoLookup)) return T_6ww_DoLookup; else {
                            prnt = ww_GetParent(caps, prnt);
                            continue Lloop;
                        }
                    }
                }
            }
        }
    }

    public static byte[] ww_AppendAssoc(Caps caps, byte[] lbyts, byte[] inst, byte[] val) {
        {
            byte[] curbyts = bt_Get(han_Program(caps), inst);
            if (!(a2b(curbyts))) {
                bt_Put(han_Program(caps), inst, val);
                return ww_Null;
            } else if (0xfd >= ((curbyts.length) + (val.length))) {
                bt_Put(han_Program(caps), inst, bytesAppend(curbyts, val));
                return ww_Null;
            } else return ww_Err(caps, lbyts, ww_ExpressionToString(caps, subbytes(curbyts, 0, ww_Next(curbyts, 0))), "es125", val, null);
        }
    }

    public static void ww_Bash0Assoc(Caps caps, byte[] inst, int byt) {
        {
            byte[] curbyts = bt_Get(han_Program(caps), inst);
            curbyts[0] = (byte) (0xff & (byt));
            bt_Put(han_Program(caps), inst, curbyts);
            return;
        }
    }

    public static void ww_MakeAssoc(Caps caps, byte[] inst, byte[] key, byte[] val) {
        bt_Put(han_Program(caps), bytesAppend(inst, key), val);
        return;
    }

    public static byte[] ww_FixupWCall(Caps caps, byte[] obj, boolean top_P) {
        int byt0 = (obj[0] & 0xFF);
        if (0x18 <= (byt0) && (byt0) <= 0x1c) {
            byte[] body = bt_Get(han_Program(caps), obj);
            if (a2b(body)) {
                int bdx = ww_Next(body, 0);
                int olen = body.length;
                byte[] meth = subbytes(body, 0, bdx);
                byte[] nobj = subbytes(body, bdx, olen);
                if (isSym_P(meth, sym__Lookup)) nobj = ww_DekeyifyBody(caps, nobj);
                if (escape_P(meth)) return meth; else if (0xfd <= ((ww__W_uri_path.length) + (meth.length) + (nobj.length))) return null; else {
                    bt_Put(han_Program(caps), obj, bytesAppend(ww__W_uri_path, bytesAppend(meth, nobj)));
                    return obj;
                }
            } else return body;
        } else if (0x1c <= (byt0) && (byt0) <= 0x1f) {
            byte[] body = bt_Get(han_Program(caps), obj);
            if (a2b(body)) {
                int bdx = ww_Next(body, 0);
                int olen = body.length;
                int ndx = ww_Next(body, bdx);
                byte[] meth = ww_Dekeyify(caps, subbytes(body, 0, bdx));
                {
                    byte[] subj = ww_FixupWCall(caps, subbytes(body, bdx, ndx), !((true)));
                    byte[] nobj = subbytes(body, ndx, olen);
                    if (isSym_P(body, sym__Lookup)) nobj = ww_DekeyifyBody(caps, nobj);
                    if (escape_P(meth)) return meth; else if (a2b(nobj) && 0 == (nobj.length)) {
                        bt_Put(han_Program(caps), obj, bytesAppend(ww__W_uri_path, bytesAppend(subj, meth)));
                        return obj;
                    } else if (escape_P(nobj)) return nobj; else if (0xfd <= ((ww__W_uri_path.length) + (subj.length) + (meth.length) + (nobj.length))) return null; else {
                        bt_Put(han_Program(caps), obj, bytesAppend(ww__W_uri_path, bytesAppend(bytesAppend(subj, meth), nobj)));
                        return obj;
                    }
                }
            } else return body;
        } else return obj;
    }

    public static byte[] ww_NewSuperstring(Caps caps, boolean literal_P) {
        Han han = (literal_P ? han_Program(caps) : han_Data(caps));
        {
            byte[] sstrIdxByts = bt_Rem(han, ww_SuperstringIdCtr);
            int cnt = 0x63;
            Lloop: while (true) {
                if (0 > (cnt)) {
                    spinLocked("ww_NewSuperstring");
                    return null;
                } else if (!(a2b(sstrIdxByts))) {
                    sstrIdxByts = bt_Rem(han, ww_SuperstringIdCtr);
                    cnt = -1 + (cnt);
                    continue Lloop;
                } else {
                    int sstrIdx = ibytsToInteger(sstrIdxByts);
                    bt_Put(han, ww_SuperstringIdCtr, integerToIbyts(1 + (sstrIdx)));
                    {
                        byte[] sstridxByts = naturalToNbyts((literal_P ? 0x10 : 0xc), sstrIdx);
                        bt_Put(han, sstridxByts, ww_PrimClass(caps, wwi_StringClass));
                        return sstridxByts;
                    }
                }
            }
        }
    }

    public static boolean addSuperstringChunk(Han han, byte[] sstrid, byte[] rbyts, int tlen) {
        return bt_Insert(han, bytesAppend(sstrid, integerToIbyts(tlen)), ww_NormalizeSbyts(rbyts));
    }

    public static byte[] ww_SetPart(Caps caps, byte[] obj, String caller, byte[] subj, byte[] name, byte[] inst) {
        boolean wob_P = bytesEqual_P(ww_Wob, subj);
        if (!(8 <= ((inst[0] & 0xFF)) && ((inst[0] & 0xFF)) <= 0xb)) {
            dprintf(">>>>ERROR<<<< " + ("ww_SetPart") + ": arg2 is not an instance: " + (ww_ObjectToString(caps, inst)) + ".\n");
            return null;
        } else if (!(wob_P) && !(8 <= ((subj[0] & 0xFF)) && ((subj[0] & 0xFF)) <= 0xb)) return ww_Err(caps, obj, caller, "es132", subj, null); else {
            byte[] nbyts = (!(bootingPrimitives_P(caps)) ? ww_GetName(caps, inst) : null);
            byte[] pbyts = ww_InstanceToParent(caps, inst);
            byte[] sbyts = (a2b(nbyts) ? (wob_P ? bt_Get(han_Stack(caps), bytesAppend(ww_Zero, name)) : bt_Get(han_Data(caps), bytesAppend(subj, name))) : null);
            if (!(a2b(pbyts))) {
                dprintf(">>>>ERROR<<<< " + ("ww_SetPart") + ": " + (ww_ObjectToString(caps, inst)) + " lacks _parent.\n");
                return null;
            } else if (a2b(sbyts) && !(wob_P) && !(bytesEqual_P(sbyts, subj))) {
                dprintf(">>>>ERROR<<<< " + ("ww_SetPart") + ": " + (ww_ObjectToString(caps, subj)) + "." + (ww_ObjectToString(caps, name)) + " is already set.\n");
                return null;
            } else if (a2b(nbyts) && !(bytesEqual_P(nbyts, name))) {
                dprintf(">>>>ERROR<<<< " + ("ww_SetPart") + ": " + (ww_ObjectToString(caps, inst)) + " already has _name: " + (ww_ObjectToString(caps, nbyts)) + ".\n");
                return null;
            } else {
                bt_Put(han_Data(caps), inst, (bytesEqual_P(pbyts, subj) ? subj : bytesAppend(pbyts, subj)));
                bt_Put(han_Data(caps), bytesAppend(inst, bytes((byte) (sym__Name))), name);
                if (wob_P) {
                    bt_Put(han_Stack(caps), bytesAppend(ww_Zero, name), inst);
                    return inst;
                } else {
                    bt_Put(han_Data(caps), bytesAppend(subj, name), inst);
                    return inst;
                }
            }
        }
    }

    public static byte[] ww_NewClass(Caps caps, byte[] obj, byte[] name, byte[] prnt) {
        if (!(a2b(prnt))) prnt = ww_Wob;
        return ww_SetPart(caps, obj, "class", prnt, name, ww_NewInstance(caps, prnt));
    }

    public static byte[] ww_NewMethod(Caps caps, byte[] lbyts, byte[] cls, byte[] name) {
        {
            byte[] exprIdxByts = bt_Rem(han_Program(caps), emptyBytes);
            int cnt = 0x63;
            Lloop: while (true) {
                if (0 > (cnt)) {
                    spinLocked("ww_NewMethod");
                    return null;
                } else if (!(a2b(exprIdxByts))) {
                    exprIdxByts = bt_Rem(han_Program(caps), emptyBytes);
                    cnt = -1 + (cnt);
                    continue Lloop;
                } else {
                    int exprIdx = ibytsToInteger(exprIdxByts);
                    bt_Put(han_Program(caps), emptyBytes, integerToIbyts(1 + (exprIdx)));
                    {
                        byte[] meth = naturalToNbyts(0x14, exprIdx);
                        byte[] abyts = ww_AppendAssoc(caps, lbyts, meth, name);
                        if (a2b(lbyts)) bt_Put(han_Annote(caps), exprIdxByts, lbyts);
                        if (escape_P(abyts)) return abyts; else if (!(a2b(ww_AppendAssoc(caps, lbyts, meth, (a2b(cls) ? cls : ww_BiggestInstance))))) return null; else if (!(isSym_P(name, sym_Null))) {
                            bt_Put(han_Program(caps), bytesAppend(meth, bytes((byte) (sym__Name))), name);
                            return meth;
                        } else return meth;
                    }
                }
            }
        }
    }

    public static byte[] ww_NewExpression(Caps caps, byte[] lbyts, byte[] subject, byte[] parent) {
        {
            byte[] exprIdxByts = bt_Rem(han_Program(caps), emptyBytes);
            int cnt = 0x63;
            Lloop: while (true) {
                if (0 > (cnt)) {
                    spinLocked("ww_NewExpression");
                    return null;
                } else if (!(a2b(exprIdxByts))) {
                    exprIdxByts = bt_Rem(han_Program(caps), emptyBytes);
                    cnt = -1 + (cnt);
                    continue Lloop;
                } else {
                    int exprIdx = ibytsToInteger(exprIdxByts);
                    bt_Put(han_Program(caps), emptyBytes, integerToIbyts(1 + (exprIdx)));
                    {
                        byte[] expridxByts = naturalToNbyts((a2b(subject) ? 0x1c : 0x18), exprIdx);
                        byte[] abyts = ww_AppendAssoc(caps, lbyts, expridxByts, parent);
                        if (a2b(lbyts)) bt_Put(han_Annote(caps), expridxByts, lbyts);
                        if (escape_P(abyts)) return abyts; else if (!(a2b(subject))) return expridxByts; else {
                            ww_AppendAssoc(caps, lbyts, expridxByts, subject);
                            return expridxByts;
                        }
                    }
                }
            }
        }
    }

    public static byte[] ww_NewInstance(Caps caps, byte[] parent) {
        {
            byte[] instIdxByts = bt_Rem(han_Data(caps), emptyBytes);
            int cnt = 0x63;
            Lloop: while (true) {
                if (0 > (cnt)) {
                    spinLocked("ww_NewInstance");
                    return null;
                } else if (!(a2b(instIdxByts))) {
                    instIdxByts = bt_Rem(han_Data(caps), emptyBytes);
                    cnt = -1 + (cnt);
                    continue Lloop;
                } else {
                    int instIdx = ibytsToInteger(instIdxByts);
                    bt_Put(han_Data(caps), emptyBytes, integerToIbyts(1 + (instIdx)));
                    {
                        byte[] instidxByts = naturalToNbyts(8, instIdx);
                        bt_Put(han_Data(caps), instidxByts, parent);
                        return instidxByts;
                    }
                }
            }
        }
    }

    public static byte[] ww_PushEnv(Caps caps, byte[] cenv, byte[] penv) {
        byte[] nenv = incrementIbyts(cenv);
        bt_Delete(han_Stack(caps), nenv, emptyBytes);
        bt_Put(han_Stack(caps), nenv, penv);
        return nenv;
    }

    public static void wobSelfSet(Caps caps, byte[] kbyts) {
        bt_Insert(han_Stack(caps), bytesAppend(ww_Zero, kbyts), kbyts);
        return;
    }

    public static boolean ww_BuildDb(Caps caps, String dbname, String symcodesTsv) {
        Seg seg = makeSeg(dbname, 0x800);
        dprintf("wb.make-seg(\"" + (dbname) + "\", 2048)\n");
        if (!(a2b(seg))) {
            dprintf(">>>>ERROR<<<< Could not create \"" + (dbname) + "\"\n");
            return false;
        } else {
            {
                Han data = createDb(seg, 'T', "data");
                if (a2b(data)) {
                    bt_Put(data, emptyBytes, ww_One);
                    bt_Put(data, ww_SuperstringIdCtr, ww_One);
                }
                setHans(caps, createDb(seg, 'T', "symbols"), createDb(seg, 'T', "slobmys"), createDb(seg, 'T', "program"), createDb(seg, 'T', "stack"), data, createDb(seg, 'T', "annote"));
            }
            if (a2b(han_Data(caps)) && a2b(han_Stack(caps)) && a2b(han_Program(caps)) && a2b(han_Symbols(caps)) && a2b(han_Slobmys(caps))) {
                ww_InstallSymbols(caps, symcodesTsv);
                bt_Insert(han_Program(caps), emptyBytes, ww_One);
                bt_Insert(han_Program(caps), ww_SuperstringIdCtr, ww_One);
                wobSelfSet(caps, ww_Wob);
                bt_Insert(han_Stack(caps), bytesAppend(ww_Zero, bytes((byte) (sym__Name))), ww_Wob);
                wobSelfSet(caps, ww_Insert);
                wobSelfSet(caps, ww_Remove);
                wobSelfSet(caps, ww_To_cxs);
                wobSelfSet(caps, ww_Class);
                wobSelfSet(caps, ww_Equal);
                wobSelfSet(caps, ww_Opt);
                return (true);
            } else {
                dprintf(">>>>ERROR<<<< Error creating \"" + (dbname) + "\"\n");
                closeSeg(seg, true);
                clearHans(caps);
                return false;
            }
        }
    }

    public static boolean ww_OpenTrees(Seg seg, Caps caps) {
        setHans(caps, openDb(seg, "symbols"), openDb(seg, "slobmys"), openDb(seg, "program"), openDb(seg, "stack"), openDb(seg, "data"), openDb(seg, "annote"));
        if (a2b(han_Symbols(caps)) && a2b(han_Slobmys(caps)) && a2b(han_Program(caps)) && a2b(han_Stack(caps)) && a2b(han_Data(caps))) return (true); else return false;
    }

    public static boolean initWw(String logpath) {
        {
            int cnt = initWb(0x190, 0x320, 0x800);
            dprintf("" + ("initWb") + "(400, 800, 2048) --> " + (cnt) + "\n");
            return (cnt) > 0;
        }
    }

    public static boolean finalWw(String logpath) {
        {
            int retcode = finalWb();
            dprintf("" + ("finalWw") + "(\"" + ((a2b(logpath) ? logpath : "stdout")) + "\") --> " + (retcode) + "\n");
            return !(wb_Err_P(retcode));
        }
    }

    public static void importLogicals(Caps caps, String lstring) {
        clearHans(caps);
        dprintf("" + (lstring) + "\n");
        setLogicals(caps, parseLogicals(lstring));
        return;
    }

    public static boolean ww_OpenDb(Caps caps, String filename) {
        if (!(fileExists_P(filename))) {
            dprintf(">>>>ERROR<<<< File missing: \"" + (filename) + "\"\n");
            return false;
        } else {
            Seg seg = openSeg(filename, true);
            if (!(a2b(seg))) {
                closeSeg(seg, true);
                clearHans(caps);
                dprintf(">>>>ERROR<<<< Could not open \"" + (filename) + "\"\n");
                return false;
            } else if (a2b(ww_OpenTrees(seg, caps))) return (true); else {
                closeSeg(seg, true);
                clearHans(caps);
                dprintf(">>>>ERROR<<<< Could not open symbol tables\n");
                return false;
            }
        }
    }

    public static boolean ww_CloseDb(Caps caps) {
        Han han = han_Data(caps);
        if ((!(a2b(han)) || !(a2b(han_Seg(han))))) return (true); else {
            boolean failed_P = wb_Err_P(closeSeg(han_Seg(han), true));
            clearHans(caps);
            return !(failed_P);
        }
    }
}
