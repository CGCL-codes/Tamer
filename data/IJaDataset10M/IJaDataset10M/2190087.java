package JCL;

/**
 * Implementation of a simple BackMarking solver for the Java
 * Constraint Library.
 *
 * @author Marc Torrens
 * @version Last update 14-11-96
 */
public class BMSolver extends Solver {

    boolean found = false;

    int count = 0;

    int mcl[][];

    int mbl[];

    static final String NAME = "Simple BackMarking";

    /**
   * Builds a <code>BMSolver</code> object.
   *
   * @return a <code>BMSolver</code> object.
   */
    public BMSolver() {
    }

    /**
   * Try to solve a CSP using the BackMarking algorithm.
   * 
   * @param problem the problem we want to solve.
   * @return a <code>BMSolver</code> object.
   */
    public BMSolver(CSP problem) {
        setProblem(problem);
    }

    /**
   *	Return the name of the algorithm.
   */
    public String getName() {
        return new String(NAME);
    }

    /**
   *	Try to solve the problem.
   */
    public void solve() {
        mcl = new int[size][MAX_DOMAIN];
        mbl = new int[size];
        clearSetup(size, MAX_DOMAIN);
        try {
            recursiveSolve(0, problem.getVariable(0).getSize());
        } catch (SolutionFoundException e) {
        }
    }

    private boolean recursiveSolve(int current, int k) {
        int i, h;
        int kk = 0;
        CSPVariable v;
        notifyEnterLevel(current);
        if (current >= size) {
            notifySolution();
            if (findMoreSolutions()) {
                notifyLeaveLevel(current);
                return false;
            } else throw new SolutionFoundException();
        }
        for (i = 0; i < k; i++) {
            indexes[current] = i;
            notifyInstanciation(current, i);
            if (isConsistent(current)) {
                if (current < (size - 1)) {
                    v = problem.getVariable(current + 1);
                    kk = v.getSize();
                }
                if (recursiveSolve(current + 1, kk)) {
                    notifyLeaveLevel(current);
                    return true;
                }
            }
        }
        h = current - 1;
        mbl[current] = h;
        for (i = h + 1; i < size; i++) mbl[i] = Math.min(mbl[i], h);
        notifyLeaveLevel(current);
        return false;
    }

    private boolean isConsistent(int current) {
        int i;
        if (mcl[current][indexes[current]] < mbl[current]) return false;
        for (i = mbl[current]; i < current; i++) {
            mcl[current][indexes[current]] = i;
            notifyConsistencyCheck();
            BinaryConstraint bc;
            for (int j = 0; j < problem.getNumberOfConstraints(i, current); j++) {
                bc = problem.getConstraint(i, current, j);
                if (!bc.satisfies(indexes[i], indexes[current], problem.getLabel(i), problem.getLabel(current))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void clearSetup(int n, int k) {
        int i, j;
        for (i = 0; i < n; i++) {
            mbl[i] = 0;
            for (j = 0; j < k; j++) mcl[i][j] = 0;
        }
    }
}
