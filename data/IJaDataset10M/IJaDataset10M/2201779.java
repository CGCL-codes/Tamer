package org.vikamine.kernel.cbr;

import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.util.KnowledgeBaseUtilities;

/**
 * @author Atzmueller
 * 
 */
public class NumQAttributeComparator {

    private OntoKMProxy ontoKMProxy;

    public NumQAttributeComparator(OntoKMProxy ontoKMProxy) {
        this.ontoKMProxy = ontoKMProxy;
    }

    public double attCompare(Attribute attribute, double queryCaseValue, double retrievedCaseValue, boolean useKnowledge) {
        if (!useKnowledge) {
            return questionComparatorNumDivision(queryCaseValue, retrievedCaseValue);
        } else {
            QComparatorType type = ontoKMProxy.getQComparisonType(attribute);
            if ((type == null) || (type.equals(QComparatorType.NUM_DIVISION))) {
                return questionComparatorNumDivision(queryCaseValue, retrievedCaseValue);
            } else if (type.equals(QComparatorType.NUM_INDIVIDUAL)) {
                return questionComparatorNumIndividual(queryCaseValue, retrievedCaseValue);
            } else if (type.equals(QComparatorType.NUM_DIVISION_DENOMINATOR)) return questionComparatorNumDivisionDenominator(attribute, queryCaseValue, retrievedCaseValue); else if (type.equals(QComparatorType.NUM_SECTION)) return questionComparatorNumSection(attribute, queryCaseValue, retrievedCaseValue); else if (type.equals(QComparatorType.NUM_SECTION_INTERPOLATE)) return questionComparatorNumSectionInterpolate(attribute, queryCaseValue, retrievedCaseValue); else return 0;
        }
    }

    private double questionComparatorNumDivision(double queryCaseValue, double retrievedCaseValue) {
        if (queryCaseValue > retrievedCaseValue) return retrievedCaseValue / queryCaseValue; else return (queryCaseValue / retrievedCaseValue);
    }

    private double questionComparatorNumDivisionDenominator(Attribute att, double queryCaseValue, double retrievedCaseValue) {
        double denominator = KnowledgeBaseUtilities.getNumDivisionDenominatorQComparisonTypeDenominator(att);
        if (queryCaseValue > retrievedCaseValue) {
            return 1 - ((queryCaseValue - retrievedCaseValue) / denominator);
        } else {
            return 1 - ((retrievedCaseValue - queryCaseValue) / denominator);
        }
    }

    private double questionComparatorNumIndividual(double queryCaseValue, double retrievedCaseValue) {
        if (queryCaseValue == retrievedCaseValue) return 1; else return 0;
    }

    private double questionComparatorNumSection(Attribute att, double queryCaseValue, double retrievedCaseValue) {
        double qC = KnowledgeBaseUtilities.getNumSectionQComparisonTypeValue(att, queryCaseValue);
        double rC = KnowledgeBaseUtilities.getNumSectionQComparisonTypeValue(att, retrievedCaseValue);
        if (qC < rC) {
            return qC / rC;
        } else {
            return rC / qC;
        }
    }

    private double questionComparatorNumSectionInterpolate(Attribute att, double queryCaseValue, double retrievedCaseValue) {
        double qC = KnowledgeBaseUtilities.getNumSectionIterpolateQComparisonTypeValue(att, queryCaseValue);
        double rC = KnowledgeBaseUtilities.getNumSectionIterpolateQComparisonTypeValue(att, retrievedCaseValue);
        if (qC < rC) {
            return qC / rC;
        } else {
            return rC / qC;
        }
    }
}
