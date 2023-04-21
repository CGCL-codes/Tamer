package org.ist.contract.implementation;

import java.util.ArrayList;
import java.util.List;
import org.ist.contract.interfaces.InstantiatedNormTrace;
import org.ist.contract.interfaces.NormTraceElement;

/**
 *
 * @author noren
 */
public class InstantiatedNormTraceImpl implements InstantiatedNormTrace {

    List<NormTraceElement> normTraceElements = new ArrayList<NormTraceElement>();

    public NormTraceElement getNormTraceElementAtTime(int time) {
        for (NormTraceElement nte : normTraceElements) {
            if (nte.getTime() == time) return nte;
        }
        return null;
    }

    public List<NormTraceElement> getNormTraceElements() {
        return normTraceElements;
    }

    public void addNormTraceElement(NormTraceElement nte) {
        normTraceElements.add(nte);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trace for " + normTraceElements);
        return sb.toString();
    }
}
