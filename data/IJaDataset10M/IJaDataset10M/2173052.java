package cspfj.problem;

import java.util.Arrays;
import cspfj.LocalSolver;
import cspfj.TabuManager;
import cspfj.constraint.Constraint;
import cspfj.util.TieManager;

public final class ConflictsManager {

    private final Variable variable;

    private final int vid;

    private int bestIndex;

    private final TieManager tieManager;

    private final int[] nbConflicts;

    private final Domain domain;

    private final Constraint[] constraints;

    private final boolean[][] check;

    private int currentConflicts;

    private int assignedIndex;

    private boolean critic;

    public final boolean[] criticConstraints;

    private final LocalSolver solver;

    public ConflictsManager(final Variable variable, final TieManager tieManager, final LocalSolver solver) {
        super();
        this.variable = variable;
        vid = variable.getId();
        this.domain = variable.getDomain();
        this.constraints = variable.getInvolvingConstraints();
        this.tieManager = tieManager;
        nbConflicts = new int[variable.getDomain().maxSize()];
        check = new boolean[constraints.length][domain.maxSize()];
        criticConstraints = new boolean[constraints.length];
        assignedIndex = variable.getFirst();
        this.solver = solver;
    }

    public Variable getVariable() {
        return variable;
    }

    public int getBestIndex() {
        return bestIndex;
    }

    public int getBestImprovment() {
        return getImprovment(bestIndex);
    }

    public int getImprovment(final int index) {
        return nbConflicts[index] - currentConflicts;
    }

    public void assignBestInitialIndex() {
        if (variable.isAssigned()) {
            bestIndex = assignedIndex;
        } else {
            final TieManager tieManager = this.tieManager;
            final Constraint[] constraints = this.constraints;
            tieManager.clear();
            final Variable variable = this.variable;
            for (int i = domain.length; --i >= 0; ) {
                if (variable.getRemovedLevel(i) >= 0) {
                    continue;
                }
                variable.assignBoolean(i);
                int indexConflicts = 0;
                for (int c = constraints.length; --c >= 0; ) {
                    final Constraint constraint = constraints[c];
                    if (!constraint.checkFirst()) {
                        indexConflicts += solver.getWeight(constraint);
                    }
                }
                tieManager.newValue(i, indexConflicts);
            }
            bestIndex = assignedIndex = tieManager.getBestValue();
            variable.firstAssign(bestIndex);
        }
        currentConflicts = nbConflicts[bestIndex];
    }

    public void initNbConflicts() {
        final int[] nbConflicts = this.nbConflicts;
        Arrays.fill(nbConflicts, 0);
        final Constraint[] constraints = this.constraints;
        final Variable variable = this.variable;
        final int domain = this.domain.length;
        for (int c = constraints.length; --c >= 0; ) {
            final boolean[] check = this.check[c];
            final Constraint constraint = constraints[c];
            final int position = constraint.getPosition(variable);
            for (int i = domain; --i >= 0; ) {
                if (variable.getRemovedLevel(i) >= 0) {
                    continue;
                }
                check[i] = constraint.checkFirstWith(position, i);
                if (!check[i]) {
                    nbConflicts[i] += solver.getWeight(constraint);
                }
            }
        }
        currentConflicts = nbConflicts[assignedIndex];
        initCritic();
    }

    private void initCritic() {
        critic = false;
        final boolean[] criticConstraints = this.criticConstraints;
        for (int c = constraints.length; --c >= 0; ) {
            if (criticConstraints[c] = critic(c)) {
                critic = true;
                assert currentConflicts > 0;
                break;
            }
        }
    }

    private boolean critic(final int constraintPos) {
        final boolean[] check = this.check[constraintPos];
        final boolean currentCheck = check[assignedIndex];
        if (currentCheck) {
            return false;
        }
        for (int i = domain.length; --i >= 0; ) {
            if (check[i]) {
                assert currentConflicts > 0;
                return true;
            }
        }
        return false;
    }

    public int getCurrentConflicts() {
        return currentConflicts;
    }

    public void update(final Constraint c, final int variablePos) {
        final TieManager tieManager = this.tieManager;
        tieManager.clear();
        final int[] nbConflicts = this.nbConflicts;
        final Variable variable = this.variable;
        final int constraintPos = c.getPositionInVariable(variablePos);
        assert variable.getInvolvingConstraints()[constraintPos] == c;
        for (int i = domain.length; --i >= 0; ) {
            if (variable.getRemovedLevel(i) >= 0) {
                continue;
            }
            final boolean check = c.checkFirstWith(variablePos, i);
            if (check ^ this.check[constraintPos][i]) {
                if (check) {
                    nbConflicts[i] -= solver.getWeight(c);
                } else {
                    nbConflicts[i] += solver.getWeight(c);
                }
                this.check[constraintPos][i] ^= true;
            }
            tieManager.newValue(i, nbConflicts[i]);
        }
        bestIndex = tieManager.getBestValue();
        currentConflicts = nbConflicts[assignedIndex];
        initCritic();
    }

    public boolean updateAfterIncrement(final Constraint c, final int pos) {
        final TieManager tieManager = this.tieManager;
        tieManager.clear();
        final int[] nbConflicts = this.nbConflicts;
        final Variable variable = this.variable;
        assert c.getPosition(variable) == pos;
        assert variable.getInvolvingConstraints()[c.getPositionInVariable(pos)] == c;
        final boolean[] check = this.check[c.getPositionInVariable(pos)];
        assert !check[assignedIndex];
        for (int i = domain.length; --i >= 0; ) {
            if (variable.getRemovedLevel(i) >= 0) {
                continue;
            }
            if (!check[i]) {
                nbConflicts[i]++;
            }
            tieManager.newValue(i, nbConflicts[i]);
        }
        currentConflicts = nbConflicts[assignedIndex];
        if (bestIndex == tieManager.getBestValue()) {
            return false;
        }
        bestIndex = tieManager.getBestValue();
        return true;
    }

    public int getBestIndex(final TabuManager tabuManager, final int aspiration, final int nbIt) {
        final int limit = currentConflicts - aspiration;
        final int vid = this.vid;
        if (!tabuManager.isTabu(vid, bestIndex, nbIt) || nbConflicts[bestIndex] < limit) {
            return bestIndex;
        }
        final TieManager tieManager = this.tieManager;
        tieManager.clear();
        final int[] nbConflicts = this.nbConflicts;
        final Variable variable = this.variable;
        for (int i = domain.length; --i >= 0; ) {
            if (variable.getRemovedLevel(i) < 0 && (!tabuManager.isTabu(vid, i, nbIt) || nbConflicts[i] < limit)) {
                tieManager.newValue(i, nbConflicts[i]);
            }
        }
        return tieManager.getBestValue();
    }

    public void reAssign(final int index) {
        variable.reAssign(index);
        assignedIndex = index;
        currentConflicts = nbConflicts[index];
        initCritic();
    }

    public int getAssignedIndex() {
        return assignedIndex;
    }

    public boolean isCritic() {
        return critic;
    }

    public void shuffleBest() {
        final TieManager tieManager = this.tieManager;
        tieManager.clear();
        for (int i = domain.length; --i >= 0; ) {
            if (variable.getRemovedLevel(i) < 0) {
                tieManager.newValue(i, nbConflicts[i]);
            }
        }
        bestIndex = tieManager.getBestValue();
    }
}
