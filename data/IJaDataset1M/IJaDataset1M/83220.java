package com.ogprover.polynomials;

import java.util.Vector;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.utilities.io.FileLogger;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Base abstract class for all terms (products of powers and coefficient)</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public abstract class Term implements Comparable<Term>, Cloneable {

    /**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
    public static final String VERSION_NUM = "1.00";

    /**
	 * Term for u-variables
	 */
    public static final short TERM_TYPE_UTERM = 1;

    /**
	 * Term for x-variables
	 */
    public static final short TERM_TYPE_XTERM = 2;

    /**
	 * Term of symbolic powers
	 */
    public static final short TERM_TYPE_SYMBOLIC = 3;

    /**
	 * List of powers that make term
	 */
    protected Vector<Power> powers;

    /**
	 * Clone method
	 * 
	 * @see java.lang.Object#clone()
	 */
    public abstract Term clone();

    /**
	 * Method that gives term type
	 * 
	 * @return	Term type (one of TERM_TYPE_xxx values)
	 */
    public abstract int getType();

    /**
	 * Method to merge this term with equal term
	 * 
	 * @param t	Term to merge with this term
	 * @return	This term which is result of operation
	 */
    public abstract Term merge(Term t);

    /**
	 * Method to multiply this term by another term
	 * 
	 * @param t	Term to multiply this term by
	 * @return	This term which is result of operation
	 */
    public abstract Term mul(Term t);

    /**
	 * Method to multiply this term by a real coefficient
	 * 
	 * @param r	A real coefficient to multiply this term by
	 * @return	This term which is result of operation
	 */
    public abstract Term mul(double r);

    /**
	 * Method to divide this term by another
	 * 
	 * @param t	Term to divide this term by
	 * @return	This term which is result of operation
	 */
    public abstract Term divide(Term t);

    /**
	 * Method for inversion of this term (changing the sign)
	 * 
	 * @return	This term which is result of operation
	 */
    public abstract Term invert();

    /**
	 * Method to check whether this term is zero constant by its variable
	 * 
	 * @return	True if term is zero and false otherwise
	 */
    public abstract boolean isZero();

    /**
	 * Method for printing this term in LaTeX format
	 * 
	 * @return	String representing this term in LaTeX format
	 */
    public abstract String printToLaTeX();

    /**
	 * Method for printing this term in XML format
	 * 
	 * @return	String representing this term in XML format
	 */
    public abstract String printToXML();

    /**
	 * Method that gives collection of powers
	 * 
	 * @return	Powers of this term
	 */
    public Vector<Power> getPowers() {
        return this.powers;
    }

    public int compareTo(Term t) {
        int size = this.powers.size(), tsize = ((t != null) ? t.getPowers().size() : 0);
        int ii = 0;
        FileLogger logger = OpenGeoProver.settings.getLogger();
        if (t == null) {
            logger.error("Null term passed in.");
            return -2;
        }
        if (this.getType() != t.getType()) return this.getType() - t.getType();
        while (ii < size && ii < tsize) {
            Power p = this.powers.get(ii);
            Power q = t.getPowers().get(ii);
            if (p == null || q == null) {
                logger.error("Found null object(s) when expected non-null value");
                return -2;
            }
            int cmp = p.compareTo(q);
            if (cmp != 0) return cmp;
            ii++;
        }
        return ((size < tsize) ? -1 : ((size > tsize) ? 1 : 0));
    }

    /**
	 * Adds power to collection and keeps the sorting order.
	 * 
	 * @param p		Power to be added into collection
	 */
    public void addPower(Power p) {
        FileLogger logger = OpenGeoProver.settings.getLogger();
        if ((this.getType() == Term.TERM_TYPE_UTERM && p.getVarType() == Variable.VAR_TYPE_UX_U) || (this.getType() == Term.TERM_TYPE_XTERM && p.getVarType() == Variable.VAR_TYPE_UX_X) || (this.getType() == Term.TERM_TYPE_SYMBOLIC && (p.getVarType() == Variable.VAR_TYPE_SYMB_X || p.getVarType() == Variable.VAR_TYPE_SYMB_Y))) {
            int left = 0, right = this.powers.size() - 1, middle = 0;
            Power middleP = null;
            long middleIndex = 0;
            long pIndex = p.getIndex();
            int pExp = p.getExponent();
            while (left <= right) {
                middle = (left + right) >> 1;
                middleP = this.powers.get(middle);
                if (middleP == null) {
                    logger.error("Found null object in collection, while in binary search algorithm in addPower() method.");
                    break;
                }
                middleIndex = middleP.getIndex();
                if (middleIndex == pIndex) {
                    middleP.addToExponent(pExp);
                    break;
                } else if (middleIndex < pIndex) {
                    right = middle - 1;
                } else {
                    left = middle + 1;
                }
            }
            if (left > right) {
                this.powers.insertElementAt(p, left);
            }
        } else logger.warn("Attempting to add power of another variable type.");
    }

    /**
	 * This method merges powers of this term with powers from another
	 * passed in term - result is stored in current term.
	 * Both terms are of same variable type and they are sorted in descending order.
	 * 
	 * @param t		Term to be merged with current term
	 * @param add	Indicator whether new powers are added to existing collection
	 * 				or they are subtracted from existing collection
	 */
    public void mergePowers(Term t, boolean add) {
        int size = this.powers.size();
        int tsize = ((t != null) ? t.getPowers().size() : 0);
        int ii = 0, jj = 0, op = ((add == true) ? 1 : -1);
        FileLogger logger = OpenGeoProver.settings.getLogger();
        if (t == null) return;
        if (this.getType() != t.getType()) {
            logger.warn("Attempt to merge two terms of different types.");
            return;
        }
        while (ii < size && jj < tsize) {
            Power pi = this.powers.get(ii);
            Power pj = t.getPowers().get(jj);
            if (pi == null || pj == null) {
                logger.error("Null object(s) read from collection");
                return;
            }
            long piInd = pi.getIndex(), pjInd = pj.getIndex();
            if (piInd == pjInd) {
                int resultExp = pi.getExponent() + op * pj.getExponent();
                if (resultExp < 0) resultExp = 0;
                if (resultExp == 0) {
                    this.powers.remove(ii);
                    size--;
                    jj++;
                } else {
                    pi.setExponent(resultExp);
                    ii++;
                    jj++;
                }
            } else if (piInd < pjInd) {
                if (add) {
                    this.powers.add(ii, pj.clone());
                    size++;
                    ii++;
                    jj++;
                } else jj++;
            } else ii++;
        }
        while (jj < tsize) {
            Power pj = t.getPowers().get(jj);
            this.powers.addElement(pj.clone());
            jj++;
        }
    }

    /**
	 * <b>
	 * Method for examining whether this term is divisible by some other term.
	 * Both terms are sorted in descending order.
	 * </b>
	 * 
	 * @param t		Passed in term to examine whether current term is divisible by this one
	 * @return		True if first term is divisible by passed in term, false otherwise
	 */
    public final boolean isDivisibleByTerm(Term t) {
        boolean result = true;
        boolean readI = true, readJ = true;
        int ii = 0, jj = 0;
        long piIndex = 0, pjIndex = 0;
        int size = this.powers.size(), tsize = ((t != null) ? t.getPowers().size() : 0);
        Power pi = null, pj = null;
        FileLogger logger = OpenGeoProver.settings.getLogger();
        if (t == null) {
            logger.error("Null term passed in.");
            return false;
        }
        if (this.getType() != t.getType()) {
            logger.error("Terms must be of same type.");
            return false;
        }
        if (tsize == 0) return true;
        if (size == 0) return false;
        if (tsize > size) return false;
        while (ii < size && jj < tsize) {
            if (readI) {
                pi = this.powers.get(ii);
                if (pi == null) {
                    logger.error("Found null object when expected non-null value");
                    return false;
                }
                piIndex = pi.getIndex();
                readI = false;
            }
            if (readJ) {
                pj = this.powers.get(jj);
                if (pj == null) {
                    logger.error("Found null object when expected non-null value");
                    return false;
                }
                pjIndex = pj.getIndex();
                readJ = false;
            }
            if (piIndex == pjIndex) {
                if (pi.getExponent() < pj.getExponent()) {
                    result = false;
                    break;
                }
                ii++;
                readI = true;
                jj++;
                readJ = true;
            } else if (piIndex > pjIndex) {
                ii++;
                readI = true;
            } else {
                result = false;
                break;
            }
        }
        if (jj < tsize) result = false;
        return result;
    }

    public int getVectorIndexOfVarIndex(long varIndex) {
        int left = 0, right = this.powers.size() - 1, middle = 0;
        long midVarIndex = 0;
        Power midP = null;
        while (left <= right) {
            middle = (left + right) >> 1;
            midP = this.powers.get(middle);
            if (midP == null) {
                OpenGeoProver.settings.getLogger().error("Found null object");
                return -1;
            }
            midVarIndex = midP.getIndex();
            if (midVarIndex == varIndex) return middle; else if (midVarIndex < varIndex) right = middle - 1; else left = middle + 1;
        }
        return -1;
    }

    public Power getPowerOfVarIndex(long varIndex) {
        int left = 0, right = this.powers.size() - 1, middle = 0;
        long midVarIndex = 0;
        Power midP = null;
        while (left <= right) {
            middle = (left + right) >> 1;
            midP = this.powers.get(middle);
            if (midP == null) {
                OpenGeoProver.settings.getLogger().error("Found null object");
                return null;
            }
            midVarIndex = midP.getIndex();
            if (midVarIndex == varIndex) return midP; else if (midVarIndex < varIndex) right = middle - 1; else left = middle + 1;
        }
        return null;
    }

    /**
	 * Increments the exponent of power of specified variable;
	 * if resulting exponent is zero, power is removed from collection.
	 * 
	 * @param varIndex		Index of variable
	 * @param expInc		Exponent increment
	 * @return				This term which is result of operation, or null in case of error
	 */
    public Term changePowerExponent(int varIndex, int expInc) {
        if (expInc != 0) {
            int ii = this.getVectorIndexOfVarIndex(varIndex);
            if (ii >= 0) {
                Power pi = this.powers.get(ii);
                if (pi == null) {
                    OpenGeoProver.settings.getLogger().error("Null object found when expected non-null value");
                    return null;
                }
                pi.addToExponent(expInc);
                if (pi.getExponent() == 0) this.powers.remove(ii);
            }
        }
        return this;
    }

    /**
	 * <b>Method that gives power exponent of specified variable from term</b>
	 * 
	 * @param varIndex		Index of variable
	 * @return				Power exponent of specified variable if found, zero otherwise
	 */
    public final int getVariableExponent(int varIndex) {
        Power p = this.getPowerOfVarIndex(varIndex);
        if (p == null) return 0;
        return p.getExponent();
    }

    public final Term gcd(Term t) {
        FileLogger logger = OpenGeoProver.settings.getLogger();
        Term thisBeforeChange = this.clone();
        this.powers = new Vector<Power>();
        if (t == null) {
            logger.error("Null term passed in");
            return null;
        }
        if (this.getType() != t.getType()) {
            logger.error("Terms must be of same type in gcd() method");
            return null;
        }
        int size1 = thisBeforeChange.getPowers().size(), size2 = t.getPowers().size();
        int ii = 0, jj = 0;
        boolean readI = true, readJ = true;
        Power pi = null, pj = null;
        long piIndex = 0, pjIndex = 0;
        int piExp = 0, pjExp = 0;
        while (ii < size1 && jj < size2) {
            if (readI) {
                pi = thisBeforeChange.getPowers().get(ii);
                if (pi == null) {
                    logger.error("Found null object when expected non-null value");
                    return null;
                }
                piIndex = pi.getIndex();
                piExp = pi.getExponent();
                readI = false;
            }
            if (readJ) {
                pj = t.getPowers().get(jj);
                if (pj == null) {
                    logger.error("Found null object when expected non-null value");
                    return null;
                }
                pjIndex = pj.getIndex();
                pjExp = pj.getExponent();
                readJ = false;
            }
            if (piIndex == pjIndex) {
                int resExp = ((piExp <= pjExp) ? piExp : pjExp);
                if (resExp > 0) {
                    Power p = new Power(pi.getVarType(), piIndex, resExp);
                    this.powers.addElement(p);
                }
                ii++;
                readI = true;
                jj++;
                readJ = true;
            } else if (piIndex < pjIndex) {
                jj++;
                readJ = true;
            } else {
                ii++;
                readI = true;
            }
        }
        return this;
    }
}
