package net.sf.mzmine.modules.peaklistmethods.identification.formulaprediction.elements;

import java.util.Comparator;

public class ElementRuleSorterByMass implements Comparator<ElementRule> {

    public int compare(ElementRule rule1, ElementRule rule2) {
        Double myMass = rule1.getMass();
        Double otherMass = rule2.getMass();
        return myMass.compareTo(otherMass);
    }
}
