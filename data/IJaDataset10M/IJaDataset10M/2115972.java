package joelib.smarts.atomexpr;

import cformat.PrintfStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.log4j.Category;
import joelib.molecule.Element;
import joelib.smarts.ParseSmart;
import joelib.smarts.Pattern;

/**
 * Atom expression in SMARTS substructure search.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.16 $, $Date: 2004/07/25 20:43:26 $
 */
public class AtomExpr implements java.io.Serializable {

    private static Category logger = Category.getInstance("joelib.smarts.atomexpr.AtomExpr");

    /**
    *  Description of the Field
    */
    public static final boolean OLDCODE = false;

    /**
    *  Description of the Field
    */
    public static final int AE_LEAF = 0x01;

    /**
     *  Description of the Field
     */
    public static final int AE_RECUR = 0x02;

    /**
     *  Description of the Field
     */
    public static final int AE_NOT = 0x03;

    /**
     *  Description of the Field
     */
    public static final int AE_ANDHI = 0x04;

    /**
     *  Description of the Field
     */
    public static final int AE_OR = 0x05;

    /**
     *  Description of the Field
     */
    public static final int AE_ANDLO = 0x06;

    /**
     *  Description of the Field
     */
    public static final int AL_CONST = 0x01;

    /**
     *  Description of the Field
     */
    public static final int AL_MASS = 0x02;

    /**
     *  Description of the Field
     */
    public static final int AL_AROM = 0x03;

    /**
     *  Description of the Field
     */
    public static final int AL_ELEM = 0x04;

    /**
     *  Description of the Field
     */
    public static final int AL_HCOUNT = 0x05;

    /**
     * Degree of an atom regarding only heavy atoms, in SMARTS: Q.
     */
    public static final int AL_HEAVY_CONNECT = 0x06;

    /**
     *  Description of the Field
     */
    public static final int AL_NEGATIVE = 0x07;

    /**
     *  Description of the Field
     */
    public static final int AL_POSITIVE = 0x08;

    /**
     *  Description of the Field
     */
    public static final int AL_CONNECT = 0x09;

    /**
     * Degree of an atom (including explicit H), in SMARTS: D.
     */
    public static final int AL_DEGREE = 0x0a;

    /**
     *  Description of the Field
     */
    public static final int AL_IMPLICIT = 0x0b;

    /**
     *  Description of the Field
     */
    public static final int AL_RINGS = 0x0c;

    /**
     *  Description of the Field
     */
    public static final int AL_SIZE = 0x0d;

    /**
     *  Description of the Field
     */
    public static final int AL_VALENCE = 0x0e;

    /**
     *  Description of the Field
     */
    public static final int AL_CHIRAL = 0x0f;

    /**
     *  Description of the Field
     */
    public static final int AL_HYB = 0x10;

    /**
     *  Description of the Field
     */
    public static final int AL_GROUP = 0x11;

    /**
     *  Description of the Field
     */
    public static final int AL_ELECTRONEGATIVE = 0x12;

    /**
     *  Description of the Field
     */
    public static final int AL_CLOCKWISE = 1;

    /**
     *  Description of the Field
     */
    public static final int AL_ANTICLOCKWISE = 2;

    /**
     *  Description of the Field
     */
    public int type;

    /**
     *  Constructor for the AtomExpr object
     */
    public AtomExpr() {
    }

    /**
     *  Constructor for the AtomExpr object
     *
     * @param  dataType  Description of the Parameter
     */
    public AtomExpr(int _type) {
        type = _type;
    }

    /**
     *  Gets the booleanAtomLeaf attribute of the ParseSmart class
     *
     * @param  expr  Description of the Parameter
     * @return       The booleanAtomLeaf value
     */
    public static boolean isBooleanAtomLeaf(AtomExpr expr) {
        LeafAtomExpr lAE = (LeafAtomExpr) expr;
        return (lAE.prop == AL_AROM) || (lAE.prop == AL_CONST);
    }

    /**
     *  Gets the invalidAtom attribute of the ParseSmart class
     *
     * @param  expr  Description of the Parameter
     * @return       The invalidAtom value
     */
    public static boolean isInvalidAtom(AtomExpr expr) {
        if (expr == null) {
            return true;
        }
        if (expr.type == AE_LEAF) {
            LeafAtomExpr lAE = (LeafAtomExpr) expr;
            return ((lAE.prop == AL_CONST) && (lAE.value == 0));
        }
        return false;
    }

    /**
     *  Gets the negatingAtomLeaf attribute of the ParseSmart class
     *
     * @param  expr  Description of the Parameter
     * @return       The negatingAtomLeaf value
     */
    public static boolean isNegatingAtomLeaf(AtomExpr expr) {
        LeafAtomExpr lAE = (LeafAtomExpr) expr;
        return (lAE.prop == AL_RINGS);
    }

    /**
     *  Description of the Method
     *
     * @param  type  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr allocAtomExpr(int type) {
        AtomExpr result = null;
        switch(type) {
            case (AE_ANDHI):
            case (AE_ANDLO):
            case (AE_OR):
                result = new BinAtomExpr(type);
                break;
            case (AE_NOT):
                result = new MonAtomExpr(type);
                break;
            case (AE_RECUR):
                result = new RecurAtomExpr(type);
                break;
            case (AE_LEAF):
                result = new LeafAtomExpr(type);
                break;
        }
        return result;
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr andAtomExpr(AtomExpr lft, AtomExpr rgt) {
        AtomExpr expr;
        int order;
        LeafAtomExpr lAELft = (LeafAtomExpr) lft;
        LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
        if (equalAtomExpr(lft, rgt)) {
            freeAtomExpr(rgt);
            return lft;
        }
        if ((lft.type == AE_LEAF) && (lAELft.prop == AL_CONST)) {
            if (lAELft.value != 0) {
                freeAtomExpr(lft);
                return rgt;
            } else {
                freeAtomExpr(rgt);
                return lft;
            }
        }
        if ((rgt.type == AE_LEAF) && (lAERgt.prop == AL_CONST)) {
            if (lAERgt.value != 0) {
                freeAtomExpr(rgt);
                return lft;
            } else {
                freeAtomExpr(lft);
                return rgt;
            }
        }
        BinAtomExpr bAELft = (BinAtomExpr) lft;
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        if (lft.type == AE_OR) {
            expr = copyAtomExpr(rgt);
            expr = orAtomExpr(andAtomExpr(expr, bAELft.lft), andAtomExpr(rgt, bAELft.rgt));
            bAELft.lft = null;
            bAELft.rgt = null;
            freeAtomExpr(lft);
            return expr;
        }
        if (rgt.type == AE_OR) {
            expr = copyAtomExpr(lft);
            expr = orAtomExpr(andAtomExpr(expr, bAERgt.lft), andAtomExpr(lft, bAERgt.rgt));
            bAERgt.lft = null;
            bAERgt.rgt = null;
            freeAtomExpr(rgt);
            return expr;
        }
        if ((rgt.type == AE_RECUR) && (lft.type != AE_RECUR)) {
            return constrainRecursion(rgt, lft);
        }
        if ((rgt.type != AE_RECUR) && (lft.type == AE_RECUR)) {
            return constrainRecursion(lft, rgt);
        }
        order = orderAtomExpr(lft, rgt);
        if (order > 0) {
            expr = lft;
            lft = rgt;
            rgt = expr;
        }
        bAELft = (BinAtomExpr) lft;
        bAERgt = (BinAtomExpr) rgt;
        if (lft.type == AE_ANDHI) {
            expr = andAtomExpr(bAELft.rgt, rgt);
            expr = andAtomExpr(bAELft.lft, expr);
            bAELft.lft = null;
            bAELft.rgt = null;
            freeAtomExpr(lft);
            return expr;
        }
        if (rgt.type == AE_ANDHI) {
            if (orderAtomExpr(lft, bAERgt.lft) > 0) {
                expr = andAtomExpr(lft, bAERgt.rgt);
                expr = andAtomExpr(bAERgt.lft, expr);
                bAERgt.lft = null;
                bAERgt.rgt = null;
                freeAtomExpr(rgt);
                return expr;
            }
            if (equalAtomExpr(lft, bAERgt.lft)) {
                freeAtomExpr(lft);
                return rgt;
            }
        }
        return andAtomExprLeaf(lft, rgt);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr andAtomExprLeaf(AtomExpr lft, AtomExpr rgt) {
        if (atomExprConflict(lft, rgt)) {
            freeAtomExpr(lft);
            freeAtomExpr(rgt);
            return buildAtomLeaf(AL_CONST, ParseSmart.IFALSE);
        }
        if (atomExprImplied(lft, rgt)) {
            freeAtomExpr(lft);
            return rgt;
        }
        rgt = atomExprImplies(lft, rgt);
        if (rgt == null) {
            return lft;
        }
        return buildAtomBin(AE_ANDHI, lft, rgt);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static boolean atomExprConflict(AtomExpr lft, AtomExpr rgt) {
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        while (rgt.type == AE_ANDHI) {
            if (atomLeafConflict(lft, bAERgt.lft)) {
                return true;
            }
            rgt = bAERgt.rgt;
        }
        return atomLeafConflict(lft, rgt);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static boolean atomExprImplied(AtomExpr lft, AtomExpr rgt) {
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        while (rgt.type == AE_ANDHI) {
            if (atomLeafImplies(bAERgt.lft, lft)) {
                return true;
            }
            rgt = bAERgt.rgt;
        }
        return atomLeafImplies(rgt, lft);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr atomExprImplies(AtomExpr lft, AtomExpr rgt) {
        AtomExpr tmp;
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        if (rgt.type != AE_ANDHI) {
            if (atomLeafImplies(lft, rgt)) {
                freeAtomExpr(rgt);
                return null;
            }
            return rgt;
        }
        tmp = atomExprImplies(lft, bAERgt.rgt);
        if (tmp != null) {
            if (atomLeafImplies(lft, bAERgt.lft)) {
                bAERgt.rgt = null;
                freeAtomExpr(rgt);
                return tmp;
            }
            bAERgt.rgt = tmp;
            return rgt;
        } else {
            bAERgt.rgt = null;
            if (atomLeafImplies(lft, bAERgt.lft)) {
                freeAtomExpr(rgt);
                return null;
            }
            tmp = bAERgt.lft;
            bAERgt.lft = null;
            freeAtomExpr(rgt);
            return tmp;
        }
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static boolean atomLeafConflict(AtomExpr lft, AtomExpr rgt) {
        AtomExpr tmp;
        LeafAtomExpr lAELft = null;
        LeafAtomExpr lAERgt = null;
        if ((lft.type == AE_LEAF) && (rgt.type == AE_LEAF)) {
            lAELft = (LeafAtomExpr) lft;
            lAERgt = (LeafAtomExpr) rgt;
            if (lAELft.prop == lAERgt.prop) {
                if (isNegatingAtomLeaf(lft)) {
                    if (lAELft.value == 0) {
                        return lAERgt.value != 0;
                    } else if (lAELft.value == -1) {
                        return lAERgt.value == 0;
                    }
                    if (lAERgt.value == 0) {
                        return lAELft.value != 0;
                    } else if (lAERgt.value == -1) {
                        return lAELft.value == 0;
                    }
                }
                return lAELft.value != lAERgt.value;
            }
            if (lAELft.prop > lAERgt.prop) {
                tmp = lft;
                lft = rgt;
                rgt = tmp;
                lAELft = (LeafAtomExpr) lft;
                lAERgt = (LeafAtomExpr) rgt;
            }
            if ((lAELft.prop == AL_AROM) && (lAERgt.prop == AL_RINGS)) {
                return ((lAELft.value != 0) && (lAERgt.value == 0));
            }
            if ((lAELft.prop == AL_NEGATIVE) && (lAERgt.prop == AL_POSITIVE)) {
                return ((lAELft.value != 0) || (lAERgt.value != 0));
            }
            if ((lAELft.prop == AL_HCOUNT) && (lAERgt.prop == AL_IMPLICIT)) {
                return (lAELft.value < lAERgt.value);
            }
        }
        if ((lft.type == AE_LEAF) && (rgt.type == AE_NOT)) {
            rgt = ((MonAtomExpr) rgt).arg;
            lAERgt = (LeafAtomExpr) rgt;
            if ((lAELft.prop == AL_NEGATIVE) && (lAERgt.prop == AL_POSITIVE)) {
                return ((lAELft.value == 0) && (lAERgt.value == 0));
            }
            if ((lAELft.prop == AL_POSITIVE) && (lAERgt.prop == AL_NEGATIVE)) {
                return ((lAELft.value == 0) && (lAERgt.value == 0));
            }
            return false;
        }
        if ((lft.type == AE_NOT) && (rgt.type == AE_LEAF)) {
            lft = ((MonAtomExpr) lft).arg;
            lAELft = (LeafAtomExpr) lft;
            if ((lAELft.prop == AL_NEGATIVE) && (lAERgt.prop == AL_POSITIVE)) {
                return ((lAELft.value == 0) && (lAERgt.value == 0));
            }
            if ((lAELft.prop == AL_POSITIVE) && (lAERgt.prop == AL_NEGATIVE)) {
                return ((lAELft.value == 0) && (lAERgt.value == 0));
            }
            return false;
        }
        return false;
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static boolean atomLeafImplies(AtomExpr lft, AtomExpr rgt) {
        LeafAtomExpr lAELft = (LeafAtomExpr) lft;
        LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
        if ((lft.type == AE_LEAF) && (rgt.type == AE_LEAF)) {
            if ((lAERgt.prop == AL_RINGS) && (lAERgt.value == -1)) {
                if (lAELft.prop == AL_AROM) {
                    return lAELft.value != 0;
                }
                if (lAELft.prop == AL_RINGS) {
                    return lAELft.value > 0;
                }
                if (lAELft.prop == AL_SIZE) {
                    return lAELft.value > 0;
                }
            }
            if ((lAELft.prop == AL_POSITIVE) && (lAERgt.prop == AL_NEGATIVE)) {
                return (lAELft.value == 0) && (lAERgt.value == 0);
            }
            return false;
        }
        if ((lft.type == AE_LEAF) && (rgt.type == AE_NOT)) {
            rgt = ((MonAtomExpr) rgt).arg;
            lAERgt = (LeafAtomExpr) rgt;
            if (lAELft.prop == lAERgt.prop) {
                return lAELft.value != lAERgt.value;
            }
            if ((lAELft.prop == AL_POSITIVE) && (lAERgt.prop == AL_NEGATIVE)) {
                return true;
            }
            if ((lAELft.prop == AL_NEGATIVE) && (lAERgt.prop == AL_POSITIVE)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     *  Description of the Method
     *
     * @param  op   Description of the Parameter
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr buildAtomBin(int op, AtomExpr lft, AtomExpr rgt) {
        AtomExpr result;
        result = allocAtomExpr(op);
        BinAtomExpr bAE = (BinAtomExpr) result;
        bAE.lft = lft;
        bAE.rgt = rgt;
        return result;
    }

    /**
     *  Description of the Method
     *
     * @param  prop  Description of the Parameter
     * @param  val   Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr buildAtomLeaf(int prop, int val) {
        AtomExpr result;
        result = allocAtomExpr(AE_LEAF);
        LeafAtomExpr lAE = (LeafAtomExpr) result;
        lAE.prop = prop;
        lAE.value = val;
        return lAE;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr buildAtomNot(AtomExpr expr) {
        AtomExpr result;
        result = allocAtomExpr(AE_NOT);
        MonAtomExpr mAE = (MonAtomExpr) result;
        mAE.arg = expr;
        return result;
    }

    /**
     *  Description of the Method
     *
     * @param  pat  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr buildAtomRecurs(Pattern pat) {
        AtomExpr result;
        result = allocAtomExpr(AE_RECUR);
        RecurAtomExpr rAE = (RecurAtomExpr) result;
        rAE.recur = pat;
        return result;
    }

    /**
     *  Description of the Method
     *
     * @param  recur  Description of the Parameter
     * @param  expr   Description of the Parameter
     * @return        Description of the Return Value
     */
    public static AtomExpr constrainRecursion(AtomExpr recur, AtomExpr expr) {
        AtomExpr head;
        Pattern pat;
        RecurAtomExpr rAE = (RecurAtomExpr) recur;
        pat = (Pattern) rAE.recur;
        head = andAtomExpr(pat.atom[0].expr, expr);
        pat.atom[0].expr = head;
        if (isInvalidAtom(head)) {
            Pattern.freePattern(pat);
            return buildAtomLeaf(AL_CONST, 0);
        }
        return recur;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr copyAtomExpr(AtomExpr expr) {
        AtomExpr result;
        result = allocAtomExpr(expr.type);
        switch(expr.type) {
            case (AE_ANDHI):
            case (AE_ANDLO):
            case (AE_OR):
                BinAtomExpr bAE = (BinAtomExpr) expr;
                BinAtomExpr binResult = (BinAtomExpr) result;
                binResult.lft = copyAtomExpr(bAE.lft);
                binResult.rgt = copyAtomExpr(bAE.rgt);
                break;
            case (AE_NOT):
                MonAtomExpr mAE = (MonAtomExpr) expr;
                MonAtomExpr monResult = (MonAtomExpr) result;
                monResult.arg = copyAtomExpr(mAE.arg);
                break;
            case (AE_RECUR):
                RecurAtomExpr recurResult = (RecurAtomExpr) result;
                recurResult.recur = Pattern.copyPattern((Pattern) recurResult.recur);
                break;
            case (AE_LEAF):
                LeafAtomExpr lAE = (LeafAtomExpr) expr;
                LeafAtomExpr leafResult = (LeafAtomExpr) result;
                leafResult.prop = lAE.prop;
                leafResult.value = lAE.value;
                break;
        }
        return result;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     * @return       Description of the Return Value
     */
    public static boolean displayAndAromElem(AtomExpr expr, OutputStream os) {
        PrintfStream fp = new PrintfStream(os);
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
        AtomExpr lft;
        AtomExpr rgt;
        Element elem = null;
        BinAtomExpr bAE = (BinAtomExpr) expr;
        lft = bAE.lft;
        if ((lft.type != AE_LEAF)) {
            return false;
        }
        LeafAtomExpr lAELft = (LeafAtomExpr) lft;
        if ((lAELft.prop != AL_AROM)) {
            return false;
        }
        rgt = bAE.rgt;
        if ((rgt.type == AE_ANDHI) || (rgt.type == AE_ANDLO)) {
            BinAtomExpr bAERgt = (BinAtomExpr) rgt;
            rgt = bAERgt.lft;
        }
        if ((rgt.type != AE_LEAF)) {
            return false;
        }
        LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
        if ((lAERgt.prop != AL_ELEM)) {
            return false;
        }
        if (elem != null) {
            if (lAELft.value != 0) {
                fp.print(Character.toLowerCase(elem.symbol.charAt(0)));
            } else {
                fp.print(elem.symbol.charAt(0));
            }
            if (elem.symbol.charAt(1) != ' ') {
                fp.print(elem.symbol.charAt(1));
            }
        }
        return true;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     */
    public static void displayAtomExpr(AtomExpr expr, OutputStream os) {
        PrintfStream fp = new PrintfStream(os);
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
        AtomExpr next;
        BinAtomExpr bAE = null;
        switch(expr.type) {
            case (AE_LEAF):
                displayAtomLeaf(expr, fp);
                break;
            case (AE_RECUR):
                RecurAtomExpr rAE = (RecurAtomExpr) expr;
                fp.print("$(");
                ParseSmart.generateSMARTSString((Pattern) rAE.recur, fp);
                fp.print(')');
                break;
            case (AE_NOT):
                MonAtomExpr mAE = (MonAtomExpr) expr;
                fp.print('!');
                displayAtomExpr(mAE.arg, fp);
                break;
            case (AE_ANDHI):
                bAE = (BinAtomExpr) expr;
                if (displayAndAromElem(expr, fp)) {
                    next = bAE.rgt;
                    if (next.type == AE_ANDHI) {
                        BinAtomExpr bAENext = (BinAtomExpr) next;
                        if (needExplicitAnd2(expr)) {
                            fp.print('&');
                        }
                        displayAtomExpr(bAENext.rgt, fp);
                    }
                } else {
                    displayAtomExpr(bAE.lft, fp);
                    if (needExplicitAnd(expr)) {
                        fp.print('&');
                    }
                    displayAtomExpr(bAE.rgt, fp);
                }
                break;
            case (AE_OR):
                bAE = (BinAtomExpr) expr;
                if (displayOrAromElem(expr, fp)) {
                    break;
                }
                displayAtomExpr(bAE.lft, fp);
                fp.print(',');
                displayAtomExpr(bAE.rgt, fp);
                break;
            case (AE_ANDLO):
                bAE = (BinAtomExpr) expr;
                displayAtomExpr(bAE.lft, fp);
                fp.print(';');
                displayAtomExpr(bAE.rgt, fp);
                break;
        }
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     */
    public static void displayAtomLeaf(AtomExpr expr, OutputStream os) {
        PrintfStream fp = new PrintfStream(os);
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
        LeafAtomExpr lAE = (LeafAtomExpr) expr;
        switch(lAE.prop) {
            case (AL_AROM):
                if (lAE.value != 0) {
                    fp.print('a');
                } else {
                    fp.print('A');
                }
                break;
            case (AL_CONNECT):
                fp.printf("X%d", lAE.value);
                break;
            case (AL_CONST):
                if (lAE.value != 0) {
                    fp.print("!*");
                } else {
                    fp.print('*');
                }
                break;
            case (AL_DEGREE):
                fp.printf("D%d", lAE.value);
                break;
            case (AL_ELEM):
                break;
            case (AL_HCOUNT):
                fp.print('H');
                if (lAE.value != 1) {
                    fp.printf("%d", lAE.value);
                }
                break;
            case (AL_HEAVY_CONNECT):
                fp.printf("Q%d", lAE.value);
                break;
            case (AL_IMPLICIT):
                fp.print('h');
                if (lAE.value != 1) {
                    fp.printf("%d", lAE.value);
                }
                break;
            case (AL_MASS):
                fp.printf("%d", lAE.value);
                break;
            case (AL_NEGATIVE):
                fp.print('-');
                if (lAE.value != 1) {
                    fp.printf("%d", lAE.value);
                }
                break;
            case (AL_POSITIVE):
                fp.print('+');
                if (lAE.value != 1) {
                    fp.printf("%d", lAE.value);
                }
                break;
            case (AL_RINGS):
                fp.print('R');
                if (lAE.value != -1) {
                    fp.printf("%d", lAE.value);
                }
                break;
            case (AL_SIZE):
                fp.print('r');
                fp.printf("%d", lAE.value);
                break;
            case (AL_VALENCE):
                fp.printf("v%d", lAE.value);
                break;
        }
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     * @return       Description of the Return Value
     */
    public static boolean displayOrAromElem(AtomExpr expr, OutputStream os) {
        PrintfStream fp = new PrintfStream(os);
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
        AtomExpr lft;
        AtomExpr rgt;
        AtomExpr arg;
        if (expr.type != AE_OR) {
            return false;
        }
        BinAtomExpr bAE = (BinAtomExpr) expr;
        lft = bAE.lft;
        if ((lft.type != AE_LEAF)) {
            return false;
        }
        LeafAtomExpr lAELft = (LeafAtomExpr) lft;
        if ((lAELft.prop != AL_AROM)) {
            return false;
        }
        rgt = bAE.rgt;
        if (rgt.type != AE_NOT) {
            return false;
        }
        MonAtomExpr mAERgt = (MonAtomExpr) rgt;
        arg = mAERgt.arg;
        if ((arg.type != AE_LEAF)) {
            return false;
        }
        LeafAtomExpr lAEArg = (LeafAtomExpr) arg;
        if ((lAEArg.prop != AL_ELEM)) {
            return false;
        }
        return true;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     */
    public static void displaySMARTSAtom(AtomExpr expr, OutputStream os) {
        PrintfStream fp = new PrintfStream(os);
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     * @return       Description of the Return Value
     */
    public static boolean displaySimpleAtomExpr(AtomExpr expr, OutputStream os) {
        PrintfStream fp = new PrintfStream(os);
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
        if (OLDCODE) {
            AtomExpr lft;
            AtomExpr rgt;
            Element elem = null;
            if ((expr.type != AE_ANDHI) && (expr.type != AE_ANDLO)) {
                return false;
            }
            BinAtomExpr bAE = (BinAtomExpr) expr;
            lft = bAE.lft;
            rgt = bAE.rgt;
            LeafAtomExpr lAELft = (LeafAtomExpr) lft;
            LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
            if ((lft.type != AE_LEAF) || (lAELft.prop != AL_AROM) || (rgt.type != AE_LEAF) || (lAERgt.prop != AL_ELEM)) {
                return false;
            }
            if (elem != null) {
                if (lAELft.value != 0) {
                    fp.print(Character.toLowerCase(elem.symbol.charAt(0)));
                } else {
                    fp.print(elem.symbol.charAt(0));
                }
            }
            return true;
        } else {
            logger.error("ParseSmart.displaySimpleAtomExpr not defined.");
        }
        return false;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @param  os    Description of the Parameter
     */
    public static void dumpAtomExpr(AtomExpr expr, OutputStream os) {
        PrintfStream fp;
        BinAtomExpr bAE = null;
        if (os instanceof PrintfStream) {
            fp = (PrintfStream) os;
        } else {
            fp = new PrintfStream(os);
        }
        if (expr != null) {
            switch(expr.type) {
                case AE_LEAF:
                    LeafAtomExpr lAE = (LeafAtomExpr) expr;
                    fp.print("LEAF(");
                    switch(lAE.prop) {
                        case AL_CONST:
                            fp.print("CONST");
                            break;
                        case AL_MASS:
                            fp.print("MASS");
                            break;
                        case AL_AROM:
                            fp.print("AROM");
                            break;
                        case AL_ELEM:
                            fp.print("ELEM");
                            break;
                        case AL_HCOUNT:
                            fp.print("HCOUNT");
                            break;
                        case AL_NEGATIVE:
                            fp.print("NEGATIVE");
                            break;
                        case AL_POSITIVE:
                            fp.print("POSITIVE");
                            break;
                        case AL_CONNECT:
                            fp.print("CONNECT");
                            break;
                        case AL_HEAVY_CONNECT:
                            fp.print("HEAVY_CONNECT");
                            break;
                        case AL_GROUP:
                            fp.print("GROUP");
                            break;
                        case AL_DEGREE:
                            fp.print("DEGREE");
                            break;
                        case AL_IMPLICIT:
                            fp.print("IMPLICIT");
                            break;
                        case AL_RINGS:
                            fp.print("RINGS");
                            break;
                        case AL_SIZE:
                            fp.print("SIZE");
                            break;
                        case AL_VALENCE:
                            fp.print("VALENCE");
                            break;
                        case AL_HYB:
                            fp.print("HYBRIDISATION");
                            break;
                        case AL_ELECTRONEGATIVE:
                            fp.print("ELECTRONEGATIVE");
                            break;
                        default:
                            fp.print("???");
                            break;
                    }
                    fp.printf(",%d)", lAE.value);
                    break;
                case AE_RECUR:
                    fp.println("RECUR(");
                    RecurAtomExpr recur = (RecurAtomExpr) expr;
                    fp.print(recur.recur.toString("  "));
                    fp.print(")");
                    break;
                case AE_NOT:
                    MonAtomExpr mAE = (MonAtomExpr) expr;
                    fp.print("NOT(");
                    dumpAtomExpr(mAE.arg, fp);
                    fp.print(')');
                    break;
                case AE_ANDHI:
                    bAE = (BinAtomExpr) expr;
                    fp.print("ANDHI(");
                    dumpAtomExpr(bAE.lft, fp);
                    fp.print(',');
                    dumpAtomExpr(bAE.rgt, fp);
                    fp.print(')');
                    break;
                case AE_OR:
                    bAE = (BinAtomExpr) expr;
                    fp.print("OR(");
                    dumpAtomExpr(bAE.lft, fp);
                    fp.print(',');
                    dumpAtomExpr(bAE.rgt, fp);
                    fp.print(')');
                    break;
                case AE_ANDLO:
                    bAE = (BinAtomExpr) expr;
                    fp.print("ANDLO(");
                    dumpAtomExpr(bAE.lft, fp);
                    fp.print(',');
                    dumpAtomExpr(bAE.rgt, fp);
                    fp.print(')');
                    break;
                default:
                    fp.print("???(...)");
                    break;
            }
        }
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static boolean equalAtomExpr(AtomExpr lft, AtomExpr rgt) {
        if (lft.type != rgt.type) {
            return false;
        }
        if (lft.type == AE_LEAF) {
            LeafAtomExpr lAELft = (LeafAtomExpr) lft;
            LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
            return ((lAELft.prop == lAERgt.prop) && (lAELft.value == lAERgt.value));
        } else if (lft.type == AE_NOT) {
            MonAtomExpr mAELft = (MonAtomExpr) lft;
            MonAtomExpr mAERgt = (MonAtomExpr) rgt;
            return equalAtomExpr(mAELft.arg, mAERgt.arg);
        } else if (lft.type == AE_RECUR) {
            return false;
        }
        BinAtomExpr bAELft = (BinAtomExpr) lft;
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        return equalAtomExpr(bAELft.lft, bAERgt.lft) && equalAtomExpr(bAELft.rgt, bAERgt.rgt);
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     */
    public static void freeAtomExpr(AtomExpr expr) {
        if (expr != null) {
            switch(expr.type) {
                case (AE_ANDHI):
                case (AE_ANDLO):
                case (AE_OR):
                    BinAtomExpr bAE = (BinAtomExpr) expr;
                    freeAtomExpr(bAE.lft);
                    freeAtomExpr(bAE.rgt);
                    break;
                case (AE_NOT):
                    MonAtomExpr mAE = (MonAtomExpr) expr;
                    freeAtomExpr(mAE.arg);
                    break;
                case (AE_RECUR):
                    RecurAtomExpr rAE = (RecurAtomExpr) expr;
                    Pattern.freePattern((Pattern) rAE.recur);
                    break;
            }
            if (expr != null) {
                expr = null;
            }
        }
    }

    /**
     *  Description of the Method
     *
     * @param  elem  Description of the Parameter
     * @param  flag  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr generateAromElem(int elem, int flag) {
        AtomExpr expr1;
        AtomExpr expr2;
        expr1 = buildAtomLeaf(AL_AROM, flag);
        expr2 = buildAtomLeaf(AL_ELEM, elem);
        return buildAtomBin(AE_ANDHI, expr1, expr2);
    }

    /**
     *  Description of the Method
     *
     * @param  elem  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr generateElement(int elem) {
        return buildAtomLeaf(AL_ELEM, elem);
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static boolean needExplicitAnd(AtomExpr expr) {
        AtomExpr rgt;
        AtomExpr lft;
        BinAtomExpr bAE = (BinAtomExpr) expr;
        lft = bAE.lft;
        rgt = bAE.rgt;
        if (rgt.type == AE_ANDHI) {
            rgt = bAE.lft;
        }
        if ((lft.type == AE_LEAF) && (rgt.type == AE_LEAF)) {
            LeafAtomExpr lAELft = (LeafAtomExpr) lft;
            LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
            if ((lAELft.prop == AL_ELEM) && (lAERgt.prop == AL_SIZE)) {
                return (lAELft.value == 5);
            }
        }
        return false;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static boolean needExplicitAnd2(AtomExpr expr) {
        AtomExpr arom;
        AtomExpr elem;
        AtomExpr next;
        BinAtomExpr bAE = (BinAtomExpr) expr;
        arom = bAE.lft;
        expr = bAE.rgt;
        elem = bAE.lft;
        next = bAE.rgt;
        BinAtomExpr bAENext = (BinAtomExpr) next;
        if (next.type == AE_ANDHI) {
            next = bAENext.lft;
        }
        if ((next.type == AE_LEAF)) {
            LeafAtomExpr lAENext = (LeafAtomExpr) next;
            if (lAENext.prop == AL_SIZE) {
                LeafAtomExpr lAEArom = (LeafAtomExpr) arom;
                LeafAtomExpr lAEElem = (LeafAtomExpr) elem;
                if ((lAEArom.value == 0) && (lAEElem.value == 6)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  Description of the Method
     *
     * @param  expr  Description of the Parameter
     * @return       Description of the Return Value
     */
    public static AtomExpr notAtomExpr(AtomExpr expr) {
        AtomExpr result;
        AtomExpr lft;
        AtomExpr rgt;
        if (expr.type == AE_LEAF) {
            LeafAtomExpr lAE = (LeafAtomExpr) expr;
            if (isBooleanAtomLeaf(expr)) {
                lAE.value = ((lAE.value == 0) ? 1 : 0);
                return expr;
            } else if (isNegatingAtomLeaf(expr)) {
                if (lAE.value == -1) {
                    lAE.value = 0;
                    return expr;
                } else if (lAE.value == 0) {
                    lAE.value = -1;
                    return expr;
                }
            }
        } else if (expr.type == AE_NOT) {
            MonAtomExpr mAE = (MonAtomExpr) expr;
            result = mAE.arg;
            mAE.arg = null;
            freeAtomExpr(expr);
            return result;
        } else if ((expr.type == AE_ANDHI) || (expr.type == AE_ANDLO)) {
            BinAtomExpr bAE = (BinAtomExpr) expr;
            lft = notAtomExpr(bAE.lft);
            rgt = notAtomExpr(bAE.rgt);
            bAE.lft = null;
            bAE.rgt = null;
            freeAtomExpr(expr);
            return orAtomExpr(lft, rgt);
        } else if (expr.type == AE_OR) {
            BinAtomExpr bAE = (BinAtomExpr) expr;
            lft = notAtomExpr(bAE.lft);
            rgt = notAtomExpr(bAE.rgt);
            bAE.lft = null;
            bAE.rgt = null;
            freeAtomExpr(expr);
            return andAtomExpr(lft, rgt);
        }
        return buildAtomNot(expr);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr orAtomExpr(AtomExpr lft, AtomExpr rgt) {
        AtomExpr expr;
        int order;
        LeafAtomExpr lAELft = (LeafAtomExpr) lft;
        LeafAtomExpr lAERgt = (LeafAtomExpr) rgt;
        if (equalAtomExpr(lft, rgt)) {
            freeAtomExpr(rgt);
            return lft;
        }
        if ((lft.type == AE_LEAF) && (lAELft.prop == AL_CONST)) {
            if (lAELft.value != 0) {
                freeAtomExpr(rgt);
                return lft;
            } else {
                freeAtomExpr(lft);
                return rgt;
            }
        }
        if ((rgt.type == AE_LEAF) && (lAERgt.prop == AL_CONST)) {
            if (lAERgt.value != 0) {
                freeAtomExpr(lft);
                return rgt;
            } else {
                freeAtomExpr(rgt);
                return lft;
            }
        }
        order = orderAtomExpr(lft, rgt);
        if (order > 0) {
            expr = lft;
            lft = rgt;
            rgt = expr;
        }
        BinAtomExpr bAELft = (BinAtomExpr) lft;
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        if (lft.type == AE_OR) {
            expr = orAtomExpr(bAERgt.rgt, rgt);
            expr = orAtomExpr(bAELft.lft, expr);
            bAELft.lft = null;
            bAERgt.rgt = null;
            freeAtomExpr(lft);
            return expr;
        }
        if (rgt.type == AE_OR) {
            if (orderAtomExpr(lft, bAERgt.lft) > 0) {
                expr = orAtomExpr(lft, bAERgt.rgt);
                expr = orAtomExpr(bAERgt.lft, expr);
                bAERgt.lft = null;
                bAERgt.rgt = null;
                freeAtomExpr(rgt);
                return expr;
            }
            if (equalAtomExpr(lft, bAERgt.lft)) {
                freeAtomExpr(lft);
                return rgt;
            }
        }
        return orAtomExprLeaf(lft, rgt);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static AtomExpr orAtomExprLeaf(AtomExpr lft, AtomExpr rgt) {
        return buildAtomBin(AE_OR, lft, rgt);
    }

    /**
     *  Description of the Method
     *
     * @param  lft  Description of the Parameter
     * @param  rgt  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static int orderAtomExpr(AtomExpr lft, AtomExpr rgt) {
        AtomExpr larg;
        AtomExpr rarg;
        int stat;
        if (lft.type == AE_NOT) {
            MonAtomExpr mAELft = (MonAtomExpr) lft;
            larg = mAELft.arg;
        } else {
            larg = lft;
        }
        if (rgt.type == AE_NOT) {
            MonAtomExpr mAERgt = (MonAtomExpr) rgt;
            rarg = mAERgt.arg;
        } else {
            rarg = rgt;
        }
        if (larg.type > rarg.type) {
            return 1;
        } else if (larg.type < rarg.type) {
            return -1;
        }
        LeafAtomExpr lAELarg = (LeafAtomExpr) larg;
        LeafAtomExpr lAERarg = (LeafAtomExpr) rarg;
        if (larg.type == AE_LEAF) {
            if (lAELarg.prop > lAERarg.prop) {
                return 1;
            }
            if (lAELarg.prop < lAERarg.prop) {
                return -1;
            }
            return (lAELarg.value - lAERarg.value);
        }
        BinAtomExpr bAELft = (BinAtomExpr) lft;
        BinAtomExpr bAERgt = (BinAtomExpr) rgt;
        stat = orderAtomExpr(bAELft.lft, bAERgt.lft);
        if (stat != 0) {
            return stat;
        }
        return orderAtomExpr(bAELft.rgt, bAERgt.rgt);
    }

    public String toString() {
        ByteArrayOutputStream bs = new ByteArrayOutputStream(1000);
        PrintStream ps = new PrintStream(bs);
        AtomExpr.dumpAtomExpr(this, ps);
        return bs.toString();
    }
}
