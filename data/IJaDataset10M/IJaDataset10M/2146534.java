package edu.ucla.mbi.xml.MIF.elements.interactionElements;

import edu.ucla.mbi.xml.MIF.elements.adminElements.Displayable;
import edu.ucla.mbi.xml.MIF.elements.MIFElement;
import edu.ucla.mbi.xml.MIF.elements.XMLParentage;
import edu.ucla.mbi.xml.MIF.elements.referencing.InternalReference;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Nov 2, 2005
 * Time: 5:17:31 PM
 * This class defines a numeric parameter, e.g. for kinetic data
 */
public class Parameter extends MIFElement implements XMLParentage {

    private String term;

    private String termAc;

    private String unit;

    private String unitAc;

    private int base;

    private int exponent;

    private float factor;

    private InternalReference experimentRef;

    public InternalReference getExperimentRef() {
        return experimentRef;
    }

    public void setExperimentRef(InternalReference experimentRef) {
        this.experimentRef = experimentRef;
    }

    /**
     * Want to disable the willy-nilly instantiatin of Parameters...
     */
    private Parameter() {
    }

    /**
     * Want to force everyone to use this constructor. In the default
     * @param term
     * @param termAc
     * @param unit
     * @param unitAc
     * @param base
     * @param exponent
     * @param factor
     */
    public Parameter(String term, String termAc, String unit, String unitAc, int base, int exponent, float factor) {
        this.term = (term != null) ? term : "";
        this.termAc = (termAc != null) ? termAc : "";
        this.unit = (unit != null) ? unit : "";
        this.unitAc = (unitAc != null) ? unitAc : "";
        this.base = (base != 0) ? base : 10;
        this.exponent = exponent;
        this.factor = factor;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTermAc() {
        return termAc;
    }

    public void setTermAc(String termAc) {
        this.termAc = termAc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitAc() {
        return unitAc;
    }

    public void setUnitAc(String unitAc) {
        this.unitAc = unitAc;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public boolean hasData() {
        if (term != null && term.length() > 0) return true;
        if (termAc != null && termAc.length() > 0) return true;
        if (unit != null && unit.length() > 0) return true;
        if (unitAc != null && unitAc.length() > 0) return true;
        if (base > 0 && base != 10) return true;
        if (exponent > 0) return true;
        return factor != 0;
    }

    protected String pre() {
        StringBuffer buf = new StringBuffer();
        buf.append("<parameter term=\"").append(term).append("\"");
        if (termAc.length() > 0) buf.append(" termAc=\"").append(termAc).append("\"");
        if (unit.length() > 0) buf.append(" unit=\"").append(unit).append("\"");
        if (unitAc.length() > 0) buf.append(" unitAc=\"").append(unitAc).append("\"");
        if (base > 0) buf.append(" base=\"").append(base).append("\"");
        if (exponent != 0) buf.append(" exponent=\"").append(exponent).append("\"");
        buf.append(" factor=\"").append(factor).append("\">");
        return buf.toString();
    }

    /**
     * @return an XML representation of the sub-schema which this Object represents
     *         in the greater context of the MIF2.5 file.
     */
    public String toXML() {
        if (hasData()) {
            return pre() + "</parameter>";
        }
        return "";
    }

    /**
     * @todo implement Compartors and override equals, if necessary
     */
    public void addChild(Object child) {
        if (child instanceof InternalReference) setExperimentRef((InternalReference) child); else throw new ClassCastException("cce");
    }

    public Parameter shallowClone() {
        Parameter parameter = new Parameter();
        parameter.setExponent(exponent);
        parameter.setUnit(unit);
        parameter.setFactor(factor);
        parameter.setUnitAc(unitAc);
        parameter.setBase(base);
        parameter.setTerm(term);
        parameter.setTermAc(termAc);
        return parameter;
    }
}
