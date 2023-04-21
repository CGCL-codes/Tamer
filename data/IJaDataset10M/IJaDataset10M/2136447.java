package edu.dimacs.mms.boxer;

import java.util.*;
import java.io.*;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/** An individual "prior", which may be applicable only to a single
    (discrimination.class, feature) pair, or for a whole class of
    them.  A single prior may be represented as a (type,
    mode,variance,absolute,skew) tuple.

    <p>This class is abstract; concrete classes implement particular
    types of priors (e.g., LaplacePrior), and have appropriate
    methods and data structures for those types.
*/
public abstract class Prior {

    /** XML element names 
      prior class="Denmark" feature="mermaid" type="l" mode="2.0" lambda
= "0.1" absolute="false" skew="0"
  */
    static class ATTR {

        static final String CLASS = "class", FEATURE = "feature", TYPE = "type", MODE = "mode", VAR = "var", ABSOLUTE = "absolute", SKEW = "skew";
    }

    /** The mode of the distribution */
    double mode = 0;

    /** The variance, as supplied in the input. This is a finite real
     * number, or Double.POSITIVE_INFINITY. This can be a relative
     * value, if absolute==false.  */
    private double var = 1.0;

    /** If false, the value of the variance should be multiplied by the
	base values (from L1 or L0) */
    boolean absolute = true;

    /** Must be 0, 1, or -1 */
    int skew = 0;

    public static enum Type {

        /** Laplacian (since ver 0.8.001) */
        l, /** Gaussian (since ver 0.8.015) */
        g
    }

    /** The <em>absolute</em> variance. (If this is an absolute prior,
      this is the same as the stated variance; otherwise, multiplied
      by the base prior's variance). A Double.POSITIVE_INFINITY may be
      stored here on occasions. */
    double avar;

    /** This is set once all data bits are computed */
    boolean completed = false;

    /** This is called at the end of the constructor, to initialize
	all derived members of the instance. Child methods override
	this method as needed, but keep calling super.complete().

	<p>One operation carried out here, if this prior is relative,
	is the computing its absolute variance from its relative variance 
	and the base prior's absolute variance. An exception is thrown 
	if one of these two value is 0 and the other is infinite,
	to avoid any possible confusion. (As per David Lewis' message from 
	2010-07-13)
     

    */
    void complete(Prior base) throws IllegalArgumentException {
        if (absolute) {
            avar = var;
        } else if (base == null) {
            throw new IllegalArgumentException("The top-level prior must be absolute; it cannot be specified as 'relative' to any other prior");
        } else {
            avar = var * base.avar;
            if (Double.isNaN(avar)) throw new IllegalArgumentException("Base variance=" + base.avar + " cannot be combined with the relative variance=" + var + ", because the product=" + avar);
        }
        completed = true;
    }

    /** This method is overridden by derived types (such as
     * LaplacePrior) to return the Type of this prior. */
    public abstract Type getType();

    /**
       Parses one "prior" XML element into a Prior object (of an
       appropriate derived type, such as LaplacePrior).

       <P>Expected XML element syntax:
       &lt;
       prior
       type="l" mode="2.0" var= "0.1" absolute="false" [skew="0" ]
       /&gt;

       <p>
       The string "Infinity" can be used to denote the infinite value
       of variance (i.e., a diffuse prior)

	@param e An XML Element of the type  "prior" 

	@param base The Level1 prior which will be used to scale the
	prior now being read if the latter is not absolute. Null can
	be passed here (and, in fact, it is when we're parsing the
	Level1 prior itself!), but when it is, the currently read
	prior must be absolute


     */
    public static Prior parsePrior(Element e, Prior base) throws BoxerXMLException {
        XMLUtil.assertName(e, Priors.NODE.PRIOR);
        return mkPrior(Type.valueOf(XMLUtil.getAttributeOrException(e, ATTR.TYPE)), XMLUtil.getAttributeDouble(e, ATTR.MODE), XMLUtil.getAttributeDouble(e, ATTR.VAR), XMLUtil.getAttributeBoolean(e, ATTR.ABSOLUTE), XMLUtil.getAttributeInt(e, ATTR.SKEW, 0), base);
    }

    /** Constructs a new prior (of the L or G type) as per the specifications.
	@param _mode The mode of the distribution (e.g, the position of the center of the bell curve for the Gaussian prior)
	@param _var Absolute or relative variance (see "base", below).
	@param base The "base" prior. If this is not null, _var is treated as relative, rather than absolute, variance.
     */
    public static Prior mkPrior(Type _type, double _mode, double _var, boolean _absolute, int _skew, Prior base) throws IllegalArgumentException {
        Prior p;
        if (_type == Type.l) {
            p = new LaplacePrior();
        } else if (_type == Type.g) {
            p = new GaussianPrior();
        } else {
            throw new IllegalArgumentException("Prior type " + _type + " is not currently supported");
        }
        p.mode = _mode;
        p.var = _var;
        p.absolute = _absolute;
        p.skew = _skew;
        if (p.skew < -1 || p.skew > 1) throw new IllegalArgumentException("Invalid value of skew: " + p.skew);
        p.complete(base);
        return p;
    }

    /** Constructs the built-in default prior */
    public static Prior makeL0() {
        return new LaplacePrior();
    }

    /** Constructs an XML "prior" element, describing this prior */
    public Element saveAsXML(Document xmldoc) {
        Element e = xmldoc.createElement(Priors.NODE.PRIOR);
        e.setAttribute(ATTR.TYPE, getType().toString());
        e.setAttribute(ATTR.MODE, "" + mode);
        e.setAttribute(ATTR.VAR, "" + var);
        e.setAttribute(ATTR.ABSOLUTE, "" + absolute);
        e.setAttribute(ATTR.SKEW, "" + skew);
        return e;
    }

    /** Produces a human-readable description of the prior */
    public String toString() {
        return "(" + getType().toString() + ", " + ATTR.MODE + "=" + mode + ", " + ATTR.VAR + "=" + var + ", " + ATTR.ABSOLUTE + "=" + absolute + ", " + ATTR.SKEW + "=" + skew + ")";
    }

    /** Applies the prior-based "truncation" to a particular element 

       @param val The value (a PLRM matrix element) to which the prior is to
       be applied

       @param trunc A Truncation object that encapsulates the cut-off
       threshold theta (directly, and via trunc.mode). It is OK to
       pass null here; the meaning would be, apply truncation
       to all values, i.e. the same as if trunc.theta=infinity

       @param mult "Multiplier", i.e. how many times (1 or more) then
       prior is to be applied. The value may be greater than 1 in
       "lazy truncation" situations.
     */
    public abstract double apply(double val, Truncation trunc, int mult, double eta);
}
