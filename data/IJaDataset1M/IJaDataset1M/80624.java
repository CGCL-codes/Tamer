package org.cishell.testing.convertertester.core.tester2.reportgen.faultanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cishell.testing.convertertester.core.converter.graph.Converter;
import org.cishell.testing.convertertester.core.tester2.reportgen.results.FilePassResult;

public class ErrorProximityHeuristic implements ChanceAtFaultHeuristic {

    public static final float FAULT_REDUCTION_PER_DEGREE_REMOVED = .33333f;

    public static final float FAULT_REDUCTION_FOR_TRUST = .5f;

    public ChanceAtFault[] determine(FilePassResult failFP, Converter[] involvedCs, Converter[] trustedCs) {
        List trustedCList = Arrays.asList(trustedCs);
        Map convToFaultScore = new HashMap();
        if (failFP.failedWhileConverting()) {
            final float startingFaultScore = 1.0f;
            float currentFaultScore = startingFaultScore;
            for (int ii = 0; ii < involvedCs.length; ii++) {
                Converter involvedC = (Converter) involvedCs[ii];
                Float oldFaultScore = (Float) convToFaultScore.get(involvedC);
                Float newFaultScore;
                if (oldFaultScore == null) {
                    newFaultScore = new Float(currentFaultScore);
                } else {
                    newFaultScore = new Float(currentFaultScore);
                }
                convToFaultScore.put(involvedC, newFaultScore);
                currentFaultScore /= FAULT_REDUCTION_PER_DEGREE_REMOVED;
            }
        } else if (failFP.failedWhileComparingGraphs()) {
            Float faultScore = new Float(1);
            for (int ii = 0; ii < involvedCs.length; ii++) {
                Converter involvedC = (Converter) involvedCs[ii];
                Float oldFaultScore = (Float) convToFaultScore.get(involvedC);
                Float newFaultScore;
                if (oldFaultScore == null) {
                    newFaultScore = faultScore;
                } else {
                    newFaultScore = faultScore;
                }
                convToFaultScore.put(involvedC, newFaultScore);
            }
        }
        Set convsInvolved = convToFaultScore.keySet();
        Iterator convIter1 = convsInvolved.iterator();
        while (convIter1.hasNext()) {
            Converter convInvolved = (Converter) convIter1.next();
            Float convFaultScore = (Float) convToFaultScore.get(convInvolved);
            if (trustedCList.contains(convInvolved)) {
                Float newConvFaultScore = new Float(convFaultScore.floatValue() * FAULT_REDUCTION_FOR_TRUST);
                convToFaultScore.put(convInvolved, newConvFaultScore);
            }
        }
        float faultScoresTotal = 0.0f;
        Iterator convIter2 = convsInvolved.iterator();
        while (convIter2.hasNext()) {
            Converter convInvolved = (Converter) convIter2.next();
            Float convFaultScore = (Float) convToFaultScore.get(convInvolved);
            faultScoresTotal += convFaultScore.floatValue();
        }
        Iterator convIter3 = convsInvolved.iterator();
        while (convIter3.hasNext()) {
            Converter convInvolved = (Converter) convIter3.next();
            Float convFaultScore = (Float) convToFaultScore.get(convInvolved);
            float normalizedFaultScore;
            if (faultScoresTotal != 0.0f) {
                normalizedFaultScore = convFaultScore.floatValue() / faultScoresTotal;
            } else {
                normalizedFaultScore = 0.0f;
            }
            Float newConvFaultScore = new Float(normalizedFaultScore);
            convToFaultScore.put(convInvolved, newConvFaultScore);
        }
        List resultCAFList = new ArrayList();
        for (int ii = 0; ii < involvedCs.length; ii++) {
            Converter involvedC = involvedCs[ii];
            Float convFaultScore = (Float) convToFaultScore.get(involvedC);
            ChanceAtFault resultCAF = new ChanceAtFault(failFP, involvedC, convFaultScore.floatValue());
            resultCAFList.add(resultCAF);
        }
        return (ChanceAtFault[]) resultCAFList.toArray(new ChanceAtFault[0]);
    }
}
