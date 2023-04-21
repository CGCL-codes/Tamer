package dopisolver.tasks;

import java.util.*;
import dopisolver.*;

/**
 * Base implementation of GreedyMin
 */
abstract class BaseGreedyMinTask extends BaseSolverTask {

    protected abstract boolean isDOPI();

    /**
     * @see dopisolver.tasks.BaseSolverTask#executeImpl()
     */
    @Override
    protected final int executeImpl() throws SolverException {
        int i0 = 0, j0 = 1;
        int cost = input.getMatrixValue(i0, j0);
        for (int i = 0; i < input.n() - 1; i++) {
            for (int j = i + 1; j < input.n(); j++) {
                if (input.getMatrixValue(i, j) < cost) {
                    i0 = i;
                    j0 = j;
                    cost = input.getMatrixValue(i0, j0);
                }
                if (isDOPI() && input.k() - input.getMatrixValue(i, j) < cost) {
                    i0 = i;
                    j0 = j;
                    cost = input.k() - input.getMatrixValue(i0, j0);
                }
            }
        }
        this.checkIfCancelled();
        Deque<Integer> pAux = new ArrayDeque<Integer>();
        pAux.addLast(i0);
        pAux.addLast(j0);
        Deque<Boolean> bAux = null;
        if (isDOPI()) {
            bAux = new ArrayDeque<Boolean>();
            bAux.addLast(false);
            bAux.addLast(input.getMatrixValue(i0, j0) != cost);
        }
        while (pAux.size() < input.n()) {
            int next = -1;
            int aux = input.k() + 1;
            this.checkIfCancelled();
            for (int i = 0; i < input.n(); i++) {
                Integer iObj = null;
                for (Integer itPAux : pAux) {
                    if (itPAux.intValue() == i) {
                        iObj = itPAux;
                        break;
                    }
                }
                if (iObj == null) {
                    if (input.getMatrixValue(j0, i) < aux) {
                        aux = input.getMatrixValue(j0, i);
                        next = i;
                    }
                    if (input.getMatrixValue(i0, i) < aux) {
                        aux = input.getMatrixValue(i0, i);
                        next = i;
                    }
                    if (isDOPI() && input.k() - input.getMatrixValue(j0, i) < aux) {
                        aux = input.k() - input.getMatrixValue(j0, i);
                        next = i;
                    }
                    if (isDOPI() && input.k() - input.getMatrixValue(i0, i) < aux) {
                        aux = input.k() - input.getMatrixValue(i0, i);
                        next = i;
                    }
                }
            }
            cost += aux;
            if (input.getMatrixValue(j0, next) == aux || (isDOPI() && input.k() - input.getMatrixValue(j0, next) == aux)) {
                pAux.addLast(next);
                if (isDOPI()) {
                    bAux.addLast(!(aux == input.getMatrixValue(j0, next)) ^ bAux.getLast());
                }
                j0 = next;
            } else {
                pAux.addFirst(next);
                if (isDOPI()) {
                    bAux.addFirst(!(aux == input.getMatrixValue(i0, next)) ^ bAux.getFirst());
                }
                i0 = next;
            }
        }
        TaskSupportDOPI support = new TaskSupportDOPI(input.n());
        Iterator<Integer> pAuxIt = pAux.iterator();
        for (int i = 0; i < input.n(); i++) {
            support.permutation[i] = pAuxIt.next();
        }
        if (isDOPI()) {
            Iterator<Boolean> bAuxIt = bAux.iterator();
            for (int i = 0; i < input.n(); i++) {
                support.bitString.set(i, bAuxIt.next());
            }
        }
        return cost;
    }
}
