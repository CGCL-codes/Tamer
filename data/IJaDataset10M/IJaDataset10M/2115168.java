package org.qedeq.kernel.se.dto.module;

import org.apache.commons.lang.ArrayUtils;
import org.qedeq.base.utility.EqualsUtility;
import org.qedeq.kernel.se.base.list.Element;
import org.qedeq.kernel.se.base.module.SubstPred;

/**
 * Usage of rule for substitute predicate variable.
 *
 * @author  Michael Meyling
 */
public class SubstPredVo implements SubstPred {

    /** Reference to previously proven formula. */
    private String reference;

    /** Function variable that will be substituted. */
    private Element predicateVariable;

    /** Replacement formula. */
    private Element substituteFormula;

    /**
     * Constructs an reason.
     *
     * @param   reference                   Reference to a valid formula.
     * @param   predicateVariable           Predicate variable that will be substituted.
     * @param   substituteFormula           Replacement formula.
     */
    public SubstPredVo(final String reference, final Element predicateVariable, final Element substituteFormula) {
        this.reference = reference;
        this.predicateVariable = predicateVariable;
        this.substituteFormula = substituteFormula;
    }

    /**
     * Default constructor.
     */
    public SubstPredVo() {
    }

    public SubstPred getSubstPred() {
        return this;
    }

    public String getReference() {
        return reference;
    }

    /**
     * Set formula reference.
     *
     * @param   reference   Reference to formula.
     */
    public void setReference(final String reference) {
        this.reference = reference;
    }

    public String[] getReferences() {
        if (reference == null || reference.length() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        } else {
            return new String[] { reference };
        }
    }

    public Element getPredicateVariable() {
        return predicateVariable;
    }

    /**
     * Set predicate variable that will be substituted.
     *
     * @param   predicateVariable   Function variable that will be replaced.
     */
    public void setPredicateVariable(final Element predicateVariable) {
        this.predicateVariable = predicateVariable;
    }

    public Element getSubstituteFormula() {
        return substituteFormula;
    }

    /**
     * Set substitution formula.
     *
     * @param   substituteFormula   New formula.
     */
    public void setSubstituteFormula(final Element substituteFormula) {
        this.substituteFormula = substituteFormula;
    }

    public String getName() {
        return "SubstPred";
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof SubstPred)) {
            return false;
        }
        final SubstPred other = (SubstPred) obj;
        return EqualsUtility.equals(getReference(), other.getReference()) && EqualsUtility.equals(predicateVariable, other.getPredicateVariable()) && EqualsUtility.equals(substituteFormula, other.getSubstituteFormula());
    }

    public int hashCode() {
        return (getReference() != null ? getReference().hashCode() : 0) ^ (getPredicateVariable() != null ? 2 ^ getPredicateVariable().hashCode() : 0) ^ (getSubstituteFormula() != null ? 3 ^ getSubstituteFormula().hashCode() : 0);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("SubstPred");
        if (getReference() != null || getPredicateVariable() != null || getSubstituteFormula() != null) {
            result.append(" (");
            boolean w = false;
            if (getReference() != null) {
                result.append(getReference());
                w = true;
            }
            if (getPredicateVariable() != null) {
                if (w) {
                    result.append(", ");
                }
                result.append(getPredicateVariable());
                w = true;
            }
            if (getSubstituteFormula() != null) {
                if (w) {
                    result.append(", ");
                }
                result.append("by ");
                result.append(getSubstituteFormula());
                w = true;
            }
            result.append(")");
        }
        return result.toString();
    }
}
