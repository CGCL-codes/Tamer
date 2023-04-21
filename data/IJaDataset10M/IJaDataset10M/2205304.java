package de.fraunhofer.isst.axbench.timing.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import de.fraunhofer.isst.axbench.timing.model.Task;
import de.fraunhofer.isst.axbench.timing.model.Transaction;
import de.fraunhofer.isst.axbench.timing.model.TransactionSet;

public abstract class AbstractAlgorithm implements IAlgorithm {

    protected final TransactionSet gamma;

    private final boolean tight;

    private boolean abort = false;

    private IdentityHashMap<Task, Transaction> transactionCache = new IdentityHashMap<Task, Transaction>();

    protected static final double EPSILON = 1e-12;

    public AbstractAlgorithm(TransactionSet gamma, boolean tight) {
        this.gamma = gamma;
        this.tight = tight;
    }

    public abstract void checkAlgorithmCompatibility() throws IncompatibleDataException;

    public void checkSystemLoad() throws IncompatibleDataException {
        Integer overLoadResource = gamma.findOverloadResource();
        if (overLoadResource != null) throw new ResourceLoadTooHighException(overLoadResource);
    }

    public void checkSpecialPredecessor() throws IncompatibleDataException {
        Task tauLast = null;
        for (Transaction gammaI : gamma) {
            for (Task tau : gammaI) {
                if (tau.getPred() != 0) {
                    if (gammaI.predecessorOf(tau) != tauLast) {
                        throw new IncompatibleDataException("PRED (predecessor) is not supported in this algorithm");
                    }
                }
                tauLast = tau;
            }
        }
    }

    public void checkDifferentPids() throws IncompatibleDataException {
        if (gamma.allTaskCount() == 0) {
            return;
        }
        int firstPid = gamma.allTasks().iterator().next().getPid();
        for (Task tau : gamma.allTasks()) {
            if (tau.getPid() != firstPid) {
                throw new IncompatibleDataException("Different PIDs (Processor/Ressource IDs) are not supported in this algorithm");
            }
        }
    }

    public ResultList calculateAllWcrt() throws AbortedException {
        ResultList wcsoResult = new WcsoResultList(gamma);
        for (Transaction gammaU : gamma) {
            for (Task tauUA : gammaU) {
                double wcrt = calculateSingleWCRT(tauUA);
                wcsoResult.setWcrt(tauUA, wcrt);
            }
        }
        return wcsoResult;
    }

    public double calculateSingleWCRT(int u_OneBased, int a_OneBased) throws AbortedException {
        return calculateSingleWCRT(tau(u_OneBased - 1, a_OneBased - 1));
    }

    /**
	 * @param tauUA &#964;<sub>ua</sub>, Task under analysis
	 */
    public double calculateSingleWCRT(Task tauUA) throws AbortedException {
        return R(tauUA);
    }

    protected abstract double R(Task tauUA) throws AbortedException;

    /**
	 * Searches for tasks of transaction gamma_i with higher or equal priority than task tau_ua,
	 * but excludes comarison task tau_ua. 
	 * Difference to other implementation: Includes other Tasks of same priority
	 */
    public Collection<Task> hp(Transaction gamma_i, Task tau_ua) {
        List<Task> hp = new LinkedList<Task>();
        for (Task task : gamma_i) {
            if (task.P() >= tau_ua.P() && task.Pid() == tau_ua.Pid() && !task.equals(tau_ua)) {
                hp.add(task);
            }
        }
        return hp;
    }

    /** Eq. 5.4 **/
    public double PHI_ijc(Task tau_ij, Task tau_ic) {
        Transaction gamma_i = transactionOf(tau_ij);
        assert gamma_i.equals(transactionOf(tau_ic)) : "tau_ij and tau_ic must belong to the same gamma_i!";
        double absShift = tau_ic.O() + tau_ic.J() - tau_ij.O();
        double phi = gamma_i.T() - mod(absShift, gamma_i.T());
        return phi;
    }

    /** Eq. 5.7 **/
    public double W_ic(Task tau_ic, Task tau_ua, double t) throws AbortedException {
        double w = 0.0;
        Transaction gammaI = transactionOf(tau_ic);
        Collection<Task> hp = hp(gammaI, tau_ua);
        for (Task tauIJ : hp) {
            if (isAbort()) throw new AbortedException();
            double sum1 = (floor((tauIJ.J() + PHI_ijc(tauIJ, tau_ic)) / gammaI.T())) * tauIJ.C();
            double tStar = t - PHI_ijc(tauIJ, tau_ic);
            double sum2 = (ceil((tStar) / gammaI.T())) * tauIJ.C();
            if (tight && tStar > 1e-10) {
                double tStarMod = mod(tStar, gammaI.T());
                if (0 < tStarMod && tStarMod < tauIJ.C()) {
                    double x = tauIJ.C() - tStarMod;
                    sum2 -= x;
                }
            }
            w += (sum1 + sum2);
        }
        return w;
    }

    /** Eq. 5.9 **/
    public int p_0(Task tau_ua, Task tau_uc) {
        Transaction gamma_u = transactionOf(tau_ua);
        assert gamma_u.equals(transactionOf(tau_uc)) : "tau_ua and tau_uc must belong to the same gamma_u!";
        return -(floor((tau_ua.J() + PHI_ijc(tau_ua, tau_uc)) / gamma_u.T()) - 1);
    }

    public final TransactionSet gamma() {
        return gamma;
    }

    /**
	 * @param transactionIndex i
	 * @return &#915;<sub>i</sub>
	 */
    public final Transaction gamma(int transactionIndex) {
        return gamma.get(transactionIndex);
    }

    public final Task tau(int transactionIndex, int taskIndex) {
        return gamma.get(transactionIndex).getTau(taskIndex);
    }

    protected final Task tau(Transaction transaction, int taskIndex) {
        return transaction.getTau(taskIndex);
    }

    /**
	 * @param tauXY &#964;<sub>xy</sub>
	 * @return
	 */
    protected final Transaction transactionOf(Task tauXY) {
        if (transactionCache.containsKey(tauXY)) {
            return transactionCache.get(tauXY);
        }
        Transaction ret = gamma.findTransactionOf(tauXY);
        transactionCache.put(tauXY, ret);
        return ret;
    }

    /**
	 * @param tauXY &#964;<sub>xy</sub>
	 * @return
	 */
    protected final int taskIndex(Task tauXY) {
        return gamma.findTaskIndexOf(tauXY);
    }

    protected final int transactionIndex(Task tauXY) {
        return gamma.findTransactionIndexOf(tauXY);
    }

    protected final int transactionIndex(Transaction gammaI) {
        return gamma.indexOf(gammaI);
    }

    protected boolean equals(Object obj1, Object obj2) {
        return obj1.equals(obj2);
    }

    protected static final double mod(double divider, double quotient) {
        if (quotient == 0.0) {
            return divider;
        }
        return divider - quotient * floor(murksRundung(divider / quotient));
    }

    protected static final double max(double double1, double double2) {
        return (double1 > double2) ? double1 : double2;
    }

    protected static final int max(int int1, int int2) {
        return (int1 > int2) ? int1 : int2;
    }

    protected static final double min(double double1, double double2) {
        return (double1 > double2) ? double2 : double1;
    }

    private static double murksRundung(double a) {
        final double STEP = 8388608;
        return Math.floor(0.5 + STEP * a) / STEP;
    }

    protected static final int floor(double a) {
        int floor = (int) Math.floor(murksRundung(a));
        return floor;
    }

    protected static final int ceil(double a) {
        int ceil = (int) Math.ceil(murksRundung(a));
        return ceil;
    }

    protected static final int posCeil(double a) {
        int i = ceil(a);
        return (i > 0) ? i : 0;
    }

    public interface ICondition<T> {

        boolean evaluate(final T elementToEvaluate);
    }

    public static <T extends Object> List<T> filterASet(Iterable<T> baseSet, ICondition<T> forCondition) {
        List<T> ret = new ArrayList<T>();
        for (T t : baseSet) {
            if (forCondition.evaluate(t)) {
                ret.add(t);
            }
        }
        return ret;
    }

    public static <T extends Object> boolean elementExists(Iterable<T> baseSet, ICondition<T> withCondition) {
        for (T t : baseSet) {
            if (withCondition.evaluate(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Object> boolean forAll(Iterable<T> baseSet, ICondition<T> withCondition) {
        for (T t : baseSet) {
            if (!withCondition.evaluate(t)) {
                return false;
            }
        }
        return true;
    }

    public abstract static class ConditionFilter<T> implements ICondition<T> {

        final Iterable<T> baseSet;

        public ConditionFilter(Iterable<T> baseSet) {
            this.baseSet = baseSet;
        }

        public List<T> evaluate() {
            return filterASet(baseSet, this);
        }
    }

    private abstract static class BooleanCondition<T> implements ICondition<T> {

        final Iterable<T> baseSet;

        public BooleanCondition(Iterable<T> baseSet) {
            this.baseSet = baseSet;
        }

        public boolean evaluate() {
            return elementExists(baseSet, this);
        }
    }

    public abstract static class ExistenceCondition<T> extends BooleanCondition<T> {

        public ExistenceCondition(Iterable<T> baseSet) {
            super(baseSet);
        }

        public boolean evaluate() {
            return elementExists(baseSet, this);
        }
    }

    public abstract static class ForAllCondition<T> extends BooleanCondition<T> {

        public ForAllCondition(Iterable<T> baseSet) {
            super(baseSet);
        }

        public boolean evaluate() {
            return forAll(baseSet, this);
        }
    }

    public void requestAbort(boolean abort) {
        this.abort = abort;
    }

    public boolean isAbort() {
        return abort;
    }

    public List<Integer> taskIndices(Collection<Task> taskList) {
        List<Integer> ret = new LinkedList<Integer>();
        for (Task tau : taskList) {
            ret.add(taskIndex(tau));
        }
        return ret;
    }

    public Set<Integer> taskIndicesSet(Collection<Task> taskList) {
        Set<Integer> ret = new TreeSet<Integer>();
        for (Task tau : taskList) {
            ret.add(taskIndex(tau));
        }
        return ret;
    }

    public boolean isTight() {
        return tight;
    }
}
