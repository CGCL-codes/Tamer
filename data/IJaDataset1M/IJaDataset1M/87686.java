package org.processmining.mining.logabstraction;

import java.io.IOException;
import org.processmining.framework.log.LogEvents;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

/**
 * @author not attributable
 * @version 1.0
 */
public class MinValueLogRelationBuilder implements LogRelationBuilder {

    private LogRelations relations;

    public MinValueLogRelationBuilder(LogAbstraction abstraction, int minValue, LogEvents events) {
        DoubleMatrix2D causalRelations, parallelRelations, directSuccession, twoStepCloseIn;
        DoubleMatrix1D startModelElements, finalModelElements, oneLoop;
        int s;
        try {
            startModelElements = abstraction.getStartInfo();
            finalModelElements = abstraction.getEndInfo();
            directSuccession = abstraction.getFollowerInfo(1);
            twoStepCloseIn = abstraction.getCloseInInfo(2);
            s = directSuccession.columns();
        } catch (IOException ex) {
            return;
        }
        oneLoop = DoubleFactory1D.sparse.make(s);
        for (int i = 0; i < s; i++) {
            oneLoop.set(i, (directSuccession.get(i, i) > minValue ? directSuccession.get(i, i) : 0));
        }
        causalRelations = DoubleFactory2D.sparse.make(s, s, 0);
        parallelRelations = DoubleFactory2D.sparse.make(s, s, 0);
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (i == j) {
                    continue;
                }
                if ((directSuccession.get(i, j) > minValue) && (directSuccession.get(j, i) <= minValue)) {
                    causalRelations.set(i, j, causalRelations.get(i, j) + 1);
                }
                if ((directSuccession.get(i, j) > minValue) && (directSuccession.get(j, i) > minValue) && ((twoStepCloseIn.get(i, j) > 0) || (twoStepCloseIn.get(j, i) > 0)) && (oneLoop.get(i) == 0) && (oneLoop.get(j) == 0)) {
                    causalRelations.set(i, j, causalRelations.get(i, j) + 1);
                }
            }
        }
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if ((directSuccession.get(i, j) > minValue) && (directSuccession.get(j, i) > minValue) && (causalRelations.get(i, j) == 0) && (causalRelations.get(j, i) == 0)) {
                    parallelRelations.set(i, j, parallelRelations.get(i, j) + 1);
                }
            }
        }
        for (int i = 0; i < s; i++) {
            if (startModelElements.get(i) > minValue) {
                boolean b = true;
                for (int j = 0; j < s; j++) {
                    b = b && (causalRelations.get(j, i) == 0);
                }
                if (!b) {
                    startModelElements.set(i, 0);
                }
            }
        }
        for (int i = 0; i < s; i++) {
            if (finalModelElements.get(i) > minValue) {
                boolean b = true;
                for (int j = 0; j < s; j++) {
                    b = b && (causalRelations.get(i, j) == 0);
                }
                if (!b) {
                    finalModelElements.set(i, 0);
                }
            }
        }
        relations = new LogRelationsImpl(causalRelations, parallelRelations, finalModelElements, startModelElements, oneLoop, events);
    }

    public LogRelations getLogRelations() {
        return relations;
    }
}
