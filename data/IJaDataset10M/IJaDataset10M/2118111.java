package dopisolver.tasks;

import dopisolver.*;

/**
 * Exact DOPI
 */
public final class ExactDOPITask extends BaseSolverTask {

    /**
     * @see dopisolver.tasks.BaseSolverTask#executeImpl()
     */
    @Override
    protected int executeImpl() throws SolverCancelledException {
        int n = this.input.n();
        TaskSupportDOPI best = new TaskSupportDOPI(n);
        TaskSupportDOPI current = new TaskSupportDOPI(n);
        int t = best.getCost(input);
        while (current.nextPermutation()) {
            this.checkIfCancelled();
            current.bitString.clear();
            do {
                int aux = current.getCost(input);
                if (aux < t) {
                    t = aux;
                    System.arraycopy(current.permutation, 0, best.permutation, 0, n);
                    best.bitString.clear();
                    best.bitString.or(current.bitString);
                }
            } while (current.nextBitSet());
        }
        return t;
    }

    /**
     * @see dopisolver.ISolverTask#getName()
     */
    @Override
    public String getName() {
        return "ExactDOPI";
    }
}
