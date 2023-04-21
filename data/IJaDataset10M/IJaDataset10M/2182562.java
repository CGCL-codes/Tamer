package statechum.analysis.learning.rpnicore;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import statechum.Configuration;
import statechum.Configuration.GDScoreComputationEnum;
import statechum.Helper;
import statechum.DeterministicDirectedSparseGraph.CmpVertex;
import statechum.analysis.learning.PairScore;
import statechum.analysis.learning.StatePair;
import statechum.analysis.learning.rpnicore.AMEquivalenceClass.IncompatibleStatesException;
import statechum.analysis.learning.rpnicore.AbstractLearnerGraph.PairCompatibility;
import statechum.analysis.learning.rpnicore.AbstractLearnerGraph.StatesToConsider;
import statechum.model.testset.PTASequenceEngine;
import statechum.model.testset.PTA_computePrecisionRecall;
import cern.colt.bitvector.BitVector;
import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

/** Many routines in Linear and GD operate on potentially non-deterministic 
 * state machines, represented with the class below.
 */
public class GDLearnerGraph {

    /** An alphabet of the considered graphs. */
    final Set<String> alphabet;

    final Configuration config;

    final StatesToConsider filter;

    final PairCompatibility<CmpVertex> pairCompatibility;

    /** Associates this object to LinearGraph it is using for data to operate on. 
	 * Important: the constructor should not access any data in computeStateScores 
	 * because it is usually invoked during the construction phase of ComputeStateScores 
	 * when no data is yet available.
	 * 
	 * @param coregraph the graph from which to build this graph
	 * @param stateFilter the filter to use when deciding which states to consider and which to throw away.
	 * @param buildForward true to build a forward graph, false for reverse. This is supposed
	 * to be an opposite of the direction in which linear should work, so in order to compute
	 * linear forward, you need to pass false here.
	 */
    public <TARGET_A_TYPE, CACHE_A_TYPE extends CachedData<TARGET_A_TYPE, CACHE_A_TYPE>> GDLearnerGraph(AbstractLearnerGraph<TARGET_A_TYPE, CACHE_A_TYPE> coregraph, StatesToConsider stateFilter, boolean direction) {
        alphabet = coregraph.learnerCache.getAlphabet();
        config = coregraph.config;
        filter = stateFilter;
        matrixInverse = new LearnerGraphND(config);
        matrixInverse.initEmpty();
        matrixForward = new LearnerGraphND(config);
        matrixForward.initEmpty();
        pairCompatibility = coregraph.pairCompatibility;
        stateToNumberMap = new TreeMap<CmpVertex, Integer>();
        numberToStateArray = coregraph.buildStateToIntegerMap(filter, stateToNumberMap);
        assert numberToStateArray.length == stateToNumberMap.size();
        if (direction) {
            LearnerGraphND.buildForward(coregraph, filter, matrixInverse);
            LearnerGraphND.buildInverse(coregraph, LearnerGraphND.ignoreNone, matrixForward);
        } else {
            LearnerGraphND.buildForward(coregraph, LearnerGraphND.ignoreNone, matrixForward);
            LearnerGraphND.buildInverse(matrixForward, filter, matrixInverse);
        }
        matrixForward.pathroutines.checkConsistency(coregraph);
        matrixInverse.pathroutines.checkConsistency(coregraph);
        expectedIncomingPerPairOfStates = estimatePairIndegree();
        findDirectlyIncompatiblePairs(coregraph);
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    /** Transition matrices, has to be TreeMap to ensure traversal through entry sets 
	 * in the order of CmpVertex's IDs.
	 */
    public LearnerGraphND matrixInverse, matrixForward;

    /** Returns the number of states to be considered. 
	 * All others are filtered out by the filter.
	 * 
	 * @return number of states to compute compatibility of.
	 */
    public int getStateNumber() {
        return getStatesToNumber().size();
    }

    /** Returns the number of pairs of states to be considered.
	 * All others contain states filtered out.
	 */
    public int getPairNumber() {
        return getStateNumber() * (getStateNumber() + 1) / 2;
    }

    /** Consider every state and a map from inputs to transitions leading into
	 * those states with those inputs (<em>sortaInverse</em>).
	 * For a pair of states (A,B), there may be some inputs for which both states
	 * have incoming transitions - this is an intersection
	 * <pre> 
	 * cmnInputs = sortaInverse.get(A).getKeys() INTERSECT sortaInverse.get(B).getKeys().
	 * </pre>
	 * If we consider the number of incoming states in 
	 * <pre>
	 * vertexToInt(getIntsortaInverse.get(cmnInputs).getValues(),getIntsortaInverse.get(cmnInputs).getValues())
	 * </pre>
	 * then these states (plus perhaps one, for a diagonal element) should be included
	 * in a row submitted to UMFPACK.
	 * We aim to estimate this number in order not to reallocate a target array
	 * many times.
	 * <p>
	 * This variable is set during the construction of the <em>transitionMatrixND</em> matrix.  
	 */
    private int expectedIncomingPerPairOfStates = -1;

    public int getExpectedIncomingPerPairOfStates() {
        assert expectedIncomingPerPairOfStates >= 0;
        return expectedIncomingPerPairOfStates;
    }

    private int estimatePairIndegree() {
        int indegreeSum = 0, incomingCnt = 0, maxInDegree = -1;
        for (Entry<CmpVertex, Map<String, List<CmpVertex>>> entry : matrixInverse.transitionMatrix.entrySet()) for (Entry<String, List<CmpVertex>> transition : entry.getValue().entrySet()) {
            ++incomingCnt;
            int size = transition.getValue().size() * entry.getValue().size();
            indegreeSum += size;
            if (size > maxInDegree) maxInDegree = size;
        }
        int expectedIncomingPerPair = 2;
        if (incomingCnt > 0) expectedIncomingPerPair = 2 + indegreeSum / incomingCnt;
        return expectedIncomingPerPair;
    }

    /** The map from vertices to the corresponding numbers, excluding reject-vertices. 
	 * Used for computation of state-similarity.
	 */
    private Map<CmpVertex, Integer> stateToNumberMap = null;

    /** An inverse map to the above, excluding reject-vertices. 
	 * Used for computation of state-similarity.
	 */
    private CmpVertex numberToStateArray[] = null;

    public Map<CmpVertex, Integer> getStatesToNumber() {
        assert stateToNumberMap != null;
        return stateToNumberMap;
    }

    public CmpVertex[] getNumberToState() {
        assert numberToStateArray != null;
        return numberToStateArray;
    }

    public int vertexToIntNR(CmpVertex vertexA, CmpVertex vertexB) {
        int x = getStatesToNumber().get(vertexA), y = getStatesToNumber().get(vertexB);
        if (x <= y) return x + y * (y + 1) / 2;
        return y + x * (x + 1) / 2;
    }

    /** Used to designate incompatible pairs of states. */
    public static final int PAIR_INCOMPATIBLE = -1;

    /** A temporary designation for compatible pairs of states, before they are numbered. */
    public static final int PAIR_OK = -2;

    public interface HandleRow<TARGET_TYPE> {

        /** Initialises this job. 
		 * @throws IllegalAccessException 
		 * @throws InstantiationException */
        public void init(int threadNo) throws InstantiationException, IllegalAccessException;

        /** Called for each row of our transition matrix. This should be a "forward" transition matrix.
		 * 
		 * @param entry the row to operate on
		 * @param threadNo the number of this thread- used when threads need to store 
		 * results somewhere, so I create an array indexed by threadNo.
		 */
        public void handleEntry(Entry<CmpVertex, Map<String, TARGET_TYPE>> entry, int threadNo);
    }

    public static class Job<TARGET_TYPE> implements Callable<Integer> {

        private final int[] workLoad;

        private final int threadNo;

        private final HandleRow<TARGET_TYPE> handler;

        private final Map<CmpVertex, Map<String, TARGET_TYPE>> matrix;

        private final StatesToConsider filter;

        public Job(final int[] wLoad, int thNo, final HandleRow<TARGET_TYPE> h, Map<CmpVertex, Map<String, TARGET_TYPE>> m, StatesToConsider f) {
            workLoad = wLoad;
            threadNo = thNo;
            handler = h;
            matrix = m;
            filter = f;
        }

        @Override
        public Integer call() throws Exception {
            if (workLoad[threadNo] < workLoad[threadNo + 1]) {
                handler.init(threadNo);
                int currentRow = 0;
                Iterator<Entry<CmpVertex, Map<String, TARGET_TYPE>>> stateB_It = matrix.entrySet().iterator();
                while (stateB_It.hasNext() && currentRow < workLoad[threadNo]) {
                    Entry<CmpVertex, Map<String, TARGET_TYPE>> entry = stateB_It.next();
                    if (filter.stateToConsider(entry.getKey())) ++currentRow;
                }
                while (stateB_It.hasNext() && currentRow < workLoad[threadNo + 1]) {
                    Entry<CmpVertex, Map<String, TARGET_TYPE>> stateB = stateB_It.next();
                    if (filter.stateToConsider(stateB.getKey())) {
                        handler.handleEntry(stateB, threadNo);
                        ++currentRow;
                    }
                }
            }
            return 0;
        }
    }

    /** Processing of data in a triangular matrix using multiple CPUs has to be done by
	 * identifying subsets of rows and columns to handle. Note that the 
	 * number of states to be used does not have to be related to the current graph - 
	 * if I only process a subset of states (such as only accept states), that's good enough.
	 * 
	 * @param totalStateNumber the number of states to partition for
	 * @param ThreadNumber number of threads to parallelise for.
	 * @return the row to start from for each thread
	 */
    public static int[] partitionWorkLoadTriangular(int ThreadNumber, int totalStateNumber) {
        if (ThreadNumber <= 0) throw new IllegalArgumentException("invalid processor number");
        int result[] = new int[ThreadNumber + 1];
        result[0] = 0;
        double a = 0;
        for (int count = 1; count < ThreadNumber; ++count) {
            double valueOfNewRow = a + (-2 * a - 1 + Math.sqrt((2 * a + 1) * (2 * a + 1) + 4 * (double) totalStateNumber * (totalStateNumber + 1) / ThreadNumber)) / 2;
            result[count] = (int) valueOfNewRow;
            a = valueOfNewRow;
            assert result[count] >= 0 && result[count] <= totalStateNumber && result[count] >= result[count - 1] : "obtained row " + result[count] + " while the range is 0.." + totalStateNumber;
        }
        result[ThreadNumber] = totalStateNumber;
        return result;
    }

    /** Processing of data in a square matrix by a number of processors is done
	 * by chopping the work into a number of rows, so that each thread is given 
	 * the same number of rows to process.
	 * 
	 * @param totalStateNumber the number of states to partition for
	 * @param ThreadNumber number of threads to parallelise for.
	 * @return the row to start from for each thread
	 */
    public static int[] partitionWorkLoadLinear(int ThreadNumber, int totalStateNumber) {
        if (ThreadNumber <= 0) throw new IllegalArgumentException("invalid processor number");
        int result[] = new int[ThreadNumber + 1];
        result[0] = 0;
        for (int count = 1; count < ThreadNumber; ++count) {
            result[count] = (int) ((double) count * totalStateNumber / ThreadNumber);
            assert result[count] >= 0 && result[count] <= totalStateNumber && result[count] >= result[count - 1] : "obtained row " + result[count] + " while the range is 0.." + totalStateNumber;
        }
        result[ThreadNumber] = totalStateNumber;
        return result;
    }

    /** Runs the supplied handler on all the rows in our matrix, using the specified number of threads.
	 * 
	 * @param handlerList A list of handlers. Each instance of this class is associated with a collection of rows and the appropriate method is called for each row.
	 * The reason we are not using a single instance is to make it possible for different handlers to have instance variables,
	 * i.e. variables shared between different handlers (and hence different threads).
	 * 
	 * @param ThreadNumber the number of threads to create. If this is one, no 
	 * <em>ExecutorService</em> is created and the handler is called directly.
	 * 
	 * @param matrix transition matrix to run tasks on
	 * @param workLoad the which rows to be processed by which threads.
	  */
    protected static <TARGET_TYPE> void performRowTasks(List<? extends HandleRow<TARGET_TYPE>> handlerList, int ThreadNumber, final Map<CmpVertex, Map<String, TARGET_TYPE>> matrix, final StatesToConsider filter, final int[] workLoad) {
        ExecutorService executorService = null;
        try {
            if (ThreadNumber > 1) {
                executorService = Executors.newFixedThreadPool(ThreadNumber);
                CompletionService<Integer> runner = new ExecutorCompletionService<Integer>(executorService);
                for (int count = 0; count < ThreadNumber; ++count) runner.submit(new Job<TARGET_TYPE>(workLoad, count, handlerList.get(count), matrix, filter));
                for (int count = 0; count < ThreadNumber; ++count) runner.take().get();
            } else new Job<TARGET_TYPE>(workLoad, 0, handlerList.get(0), matrix, filter).call();
        } catch (Exception ex) {
            IllegalArgumentException e = new IllegalArgumentException("failed to compute, the problem is: " + ex);
            e.initCause(ex);
            throw e;
        } finally {
            if (executorService != null) executorService.shutdown();
        }
    }

    /** Maps states to inputs accepted and rejected from each of them. */
    protected Map<CmpVertex, BitVector> inputsAccepted = null, inputsRejected = null;

    /** When this is set to true, various performance warnings are emitted. */
    protected boolean linearWarningsEnabled = Boolean.valueOf(statechum.GlobalConfiguration.getConfiguration().getProperty(statechum.GlobalConfiguration.G_PROPERTIES.LINEARWARNINGS));

    /** Checks the supplied bit-vectors for an intersection.
	 * Note that the same thing could be accomplished with
	 * <code>
	 * 	BitVector intersectionOfAB = A.copy();intersectionOfAB.and(B);
	 *  return intersectionOfAB.cardinality() > 0
	 *	</code>
	 * I think my code is more efficient as it does not make a clone.
	 * @param A
	 * @param B
	 * @return true if there is any bit in common between these two bit vectors.
	 */
    public static boolean intersects(BitVector A, BitVector B) {
        long[] bufA = A.elements(), bufB = B.elements();
        assert bufA.length == bufB.length;
        for (int i = 0; i < bufA.length; ++i) if ((bufA[i] & bufB[i]) != 0) return true;
        return false;
    }

    /** A number of pairs of states will not be compatible, hence we do not need to include 
	 * them in a matrix for computation of compatibility scores. This method fills in
	 * the fields responsible for inputs accepted and rejected from every state of the
	 * graph.
	 * <p>
	 * This one only works forward on a deterministic graph - this does not appear to be
	 * a limitation because 
	 * <ul>
	 * <li>the only case where we hit nondeterminism involves working backwards 
	 * for computation of similarity between states where 
     * reject states are not accessible because
	 * they have no outgoing transitions (and hence no incoming transitions 
	 * in the backwards case) and </li>
	 * <li>if a pair of states are not compatible,
	 * we give them a negative score - this is all that is needed because pairs of even very
	 * similar states may have paths which are not compatible - it does not matter because
	 * we are not doing merging of such states when working backwards.</li>
	 * </ul>
	 * 
	 * @param graph the graph to process
	 * @param filter which states to consider
	 */
    protected <TARGET_A_TYPE, CACHE_A_TYPE extends CachedData<TARGET_A_TYPE, CACHE_A_TYPE>> void findDirectlyIncompatiblePairs(AbstractLearnerGraph<TARGET_A_TYPE, CACHE_A_TYPE> graph) {
        inputsAccepted = new TreeMap<CmpVertex, BitVector>();
        inputsRejected = new TreeMap<CmpVertex, BitVector>();
        int num = 0;
        Map<String, Integer> inputToInt = new TreeMap<String, Integer>();
        for (String str : getAlphabet()) inputToInt.put(str, num++);
        for (Entry<CmpVertex, Map<String, TARGET_A_TYPE>> entry : graph.transitionMatrix.entrySet()) if (filter.stateToConsider(entry.getKey())) {
            BitVector acceptVector = new BitVector(getAlphabet().size()), rejectVector = new BitVector(getAlphabet().size());
            for (Entry<String, TARGET_A_TYPE> transition : entry.getValue().entrySet()) for (CmpVertex vert : graph.getTargets(transition.getValue())) {
                if (!vert.isAccept()) rejectVector.set(inputToInt.get(transition.getKey())); else acceptVector.set(inputToInt.get(transition.getKey()));
            }
            inputsAccepted.put(entry.getKey(), acceptVector);
            inputsRejected.put(entry.getKey(), rejectVector);
        }
    }

    /** A number of pairs of states will not be compatible, hence we do not need to include 
	 * them in a matrix for computation of compatibility scores. This method updates the set of  
	 * incompatible pairs of states.
	 * 
	 * @param coregraph
	 * @param incompatiblePairs pairs currently considered incompatible
	 * @param ThreadNumber number of CPUs to use.
	 * @return
	 */
    int findIncompatiblePairs(final int[] incompatiblePairs, int ThreadNumber) {
        final int pairsNumber = getPairNumber();
        if (incompatiblePairs.length != pairsNumber) throw new IllegalArgumentException("invalid array length");
        final Queue<StatePair> currentExplorationBoundary = new LinkedList<StatePair>();
        List<HandleRow<List<CmpVertex>>> handlerList = new LinkedList<HandleRow<List<CmpVertex>>>();
        for (int threadCnt = 0; threadCnt < ThreadNumber; ++threadCnt) handlerList.add(new HandleRow<List<CmpVertex>>() {

            @Override
            public void init(@SuppressWarnings("unused") int threadNo) {
            }

            /** This set is different for different threads hence no need to acquire/release locks. */
            Set<Integer> sourceData = new TreeSet<Integer>();

            /** Used to detect non-consecutive state pair numbers - in this case an internal error should be reported. */
            int prevStatePairNumber = -1;

            @Override
            public void handleEntry(Entry<CmpVertex, Map<String, List<CmpVertex>>> entryA, @SuppressWarnings("unused") int threadNo) {
                Collection<Entry<String, List<CmpVertex>>> rowA_collection = matrixInverse.transitionMatrix.get(entryA.getKey()).entrySet();
                BitVector inputsAcceptedFromA = inputsAccepted.get(entryA.getKey()), inputsRejectedFromA = inputsRejected.get(entryA.getKey());
                Iterator<Entry<CmpVertex, Map<String, List<CmpVertex>>>> stateB_It = matrixInverse.transitionMatrix.entrySet().iterator();
                while (stateB_It.hasNext()) {
                    Entry<CmpVertex, Map<String, List<CmpVertex>>> stateB = stateB_It.next();
                    int currentStatePair = vertexToIntNR(stateB.getKey(), entryA.getKey());
                    assert prevStatePairNumber < 0 || currentStatePair == prevStatePairNumber + 1;
                    prevStatePairNumber = currentStatePair;
                    BitVector B_accepted = inputsAccepted.get(stateB.getKey()), B_rejected = inputsRejected.get(stateB.getKey());
                    if (!AbstractLearnerGraph.checkCompatible(stateB.getKey(), entryA.getKey(), pairCompatibility) || intersects(inputsAcceptedFromA, B_rejected) || intersects(inputsRejectedFromA, B_accepted)) {
                        sourceData.clear();
                        incompatiblePairs[currentStatePair] = PAIR_INCOMPATIBLE;
                        Map<String, List<CmpVertex>> rowB = stateB.getValue();
                        for (Entry<String, List<CmpVertex>> outLabel : rowA_collection) {
                            List<CmpVertex> to = rowB.get(outLabel.getKey());
                            if (to != null) {
                                for (CmpVertex srcA : outLabel.getValue()) for (CmpVertex srcB : to) {
                                    int sourcePair = vertexToIntNR(srcB, srcA);
                                    synchronized (currentExplorationBoundary) {
                                        if (!sourceData.contains(sourcePair)) {
                                            sourceData.add(sourcePair);
                                            currentExplorationBoundary.add(new StatePair(srcB, srcA));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (incompatiblePairs[currentStatePair] != PAIR_INCOMPATIBLE) incompatiblePairs[currentStatePair] = PAIR_OK;
                    if (stateB.getKey().equals(entryA.getKey())) break;
                }
            }
        });
        performRowTasks(handlerList, ThreadNumber, matrixForward.transitionMatrix, filter, GDLearnerGraph.partitionWorkLoadTriangular(ThreadNumber, matrixForward.transitionMatrix.size()));
        while (!currentExplorationBoundary.isEmpty()) {
            StatePair pair = currentExplorationBoundary.remove();
            int currentStatePair = vertexToIntNR(pair.firstElem, pair.secondElem);
            incompatiblePairs[currentStatePair] = PAIR_INCOMPATIBLE;
            Map<String, List<CmpVertex>> rowB = matrixInverse.transitionMatrix.get(pair.secondElem);
            for (Entry<String, List<CmpVertex>> outLabel : matrixInverse.transitionMatrix.get(pair.firstElem).entrySet()) {
                List<CmpVertex> to = rowB.get(outLabel.getKey());
                if (to != null) {
                    for (CmpVertex srcA : outLabel.getValue()) for (CmpVertex srcB : to) {
                        int sourcePair = vertexToIntNR(srcA, srcB);
                        if (incompatiblePairs[sourcePair] == PAIR_OK) {
                            currentExplorationBoundary.offer(new StatePair(srcA, srcB));
                            incompatiblePairs[sourcePair] = PAIR_INCOMPATIBLE;
                        }
                    }
                }
            }
        }
        return numberNonNegativeElements(incompatiblePairs);
    }

    protected void printOK(int[] data) {
        int cnt = 0;
        for (int i = 0; i < data.length; ++i) if (data[i] == PAIR_OK) ++cnt;
        System.out.println("OK pairs: " + cnt);
    }

    /** Sequentially number elements in the array which are not negative. */
    public static int numberNonNegativeElements(int data[]) {
        int num = 0;
        for (int i = 0; i < data.length; ++i) if (data[i] == PAIR_OK) data[i] = num++;
        return num;
    }

    public interface DetermineDiagonalAndRightHandSideInterface {

        /** Counts the number of outgoing transitions from a pair of states with the same label.
		 * Hence if A has a,b,c outgoing and B has b,c,e,f, the outcome will be 2 because 
		 * there are two transitions, b,c in common.
		 * Important: this does not ignore states to be ignored in the process of computation
		 * because we actually have to consider them at this point - otherwise 
		 * different filters will affect scores obtained; filter should only impact efficiency
		 * with Zero filter best performing. 
		 * <p>
		 * The reason we use rows here is because we usually have rows handy
		 * when using this method and hence would not wish to do a .get again to retrieve them
		 * based on state values. States are needed to ensure that (a) incompatible vertices get
		 * appropriate scores and (b) to use alternative methods for computation of scores such as BCR.
		 */
        void compute(CmpVertex stateA, CmpVertex stateB, Map<String, List<CmpVertex>> rowA, Map<String, List<CmpVertex>> rowB);

        /** Returns the diagonal value (before it is reduced by
		 *  <em>coregraph.config.getAttenuationK()</em> for every single-transition loop).
		 *  If value returned is zero, it is assumed to be one. 
		 */
        double getDiagonal();

        /** Returns a value for the right-hand side. */
        double getRightHandSide();
    }

    /** Makes it possible to use pluggable objects to determine default values 
	 * of a diagonal and the right-hand side (aka b in Ax=b).
	 * 
	 */
    public abstract class DetermineDiagonalAndRightHandSide implements DetermineDiagonalAndRightHandSideInterface {

        /** The number of shared transitions which lead to states with the same <em>isHightlight()</em> value
		 * (this can be used to interpret an arbitrary subset of states as pseudo-reject ones; 
		 * in practice useful when I make <em>isHightlight() = !isAccept()</em>).
		 */
        int sharedSameHighlight = 0;

        /** For a specific pair of states, this one contains the number of 
		 * transitions shared between these two states which also lead to 
		 * accept states. 
		 */
        int sharedOutgoing = 0;

        /** For a specific pair of states, this one contains the 
		 *  number of elements of an alphabet used on outgoing transitions 
		 *  from these two states. 
		 */
        int totalOutgoing = 0;

        /** Counts the number of outgoing transitions from a pair of states with the same label.
		 * Hence if A has a,b,c outgoing and B has b,c,e,f, the outcome will be 2 because 
		 * there are two transitions, b,c in common.
		 * Important: this does not ignore states to be ignored in the process of computation
		 * because we actually have to consider them at this point - otherwise 
		 * different filters will affect scores obtained; filter should only impact efficiency
		 * with Zero filter best performing. 
		 * <p>
		 * The reason we use rows here is because we usually have rows handy
		 * when using this method and hence would not wish to do a .get again to retrieve them
		 * based on state values. States are needed to ensure that (a) incompatible vertices get
		 * appropriate scores and (b) to use alternative methods for computation of scores such as BCR.
		 */
        @Override
        public void compute(CmpVertex stateA, CmpVertex stateB, Map<String, List<CmpVertex>> rowA, Map<String, List<CmpVertex>> rowB) {
            boolean incompatible = !AbstractLearnerGraph.checkCompatible(stateA, stateB, pairCompatibility);
            sharedSameHighlight = 0;
            sharedOutgoing = 0;
            totalOutgoing = 0;
            for (Entry<String, List<CmpVertex>> entry : rowA.entrySet()) {
                List<CmpVertex> to_list = rowB.get(entry.getKey());
                if (to_list != null) for (CmpVertex targetA : entry.getValue()) for (CmpVertex targetB : to_list) {
                    ++totalOutgoing;
                    ++sharedOutgoing;
                    if (targetA.isHighlight() == targetB.isHighlight()) ++sharedSameHighlight;
                } else totalOutgoing += entry.getValue().size();
            }
            for (Entry<String, List<CmpVertex>> entry : rowB.entrySet()) if (!rowA.containsKey(entry.getKey())) totalOutgoing += entry.getValue().size();
            if (incompatible) {
                sharedOutgoing = getRightHandSideForIncompatible();
            }
        }

        int getRightHandSideForIncompatible() {
            return PAIR_INCOMPATIBLE * (totalOutgoing > 0 ? totalOutgoing : 1);
        }

        /** Returns the diagonal value (before it is reduced by
		 *  <em>coregraph.config.getAttenuationK()</em> for every single-transition loop).
		 *  If value returned is zero, it is assumed to be one. 
		 */
        @Override
        public abstract double getDiagonal();

        /** Returns a value for the right-hand side. */
        @Override
        public abstract double getRightHandSide();
    }

    /** Interprets highlighted states as reject ones in the course of computation of a diagonal;
	 * if a pair of states is incompatible for a particular matched transition,
	 * such a matched transition is ignored. 
	 */
    public class DDRH_highlight extends DetermineDiagonalAndRightHandSide {

        @Override
        public double getDiagonal() {
            return sharedSameHighlight;
        }

        @Override
        public double getRightHandSide() {
            return sharedSameHighlight;
        }
    }

    public class DDRH_default extends DetermineDiagonalAndRightHandSide {

        @Override
        public double getDiagonal() {
            return totalOutgoing * 2;
        }

        @Override
        public double getRightHandSide() {
            return sharedOutgoing;
        }
    }

    /** For each state of the graph, we generate walks and the corresponding deterministic graph.
	 * 
	 * They are stored in this structure.
	 */
    public class GraphAndWalk {

        public GraphAndWalk(LearnerGraph gr, PTASequenceEngine a) {
            graph = gr;
            testSequences = a;
        }

        public final PTASequenceEngine testSequences;

        public final LearnerGraph graph;
    }

    /** It may be necessary to generate random walks and apply them to some of the states, this means 
	 * grCombined has to be turned into a series of graphs, starting from each state of grCombined
	 * (the state-comparison relation to be symmetric) and those graphs have to be
	 * deterministic for the generation of walks and application of them (if we trace a path randomly
	 * it will most likely fail to reach an accept-state but we'd like to stick to standard automata theory
	 * in this instance).
	 * This map associates states with the corresponding graphs. Walks will be stored in caches of those graphs.
	 */
    protected Map<CmpVertex, GraphAndWalk> stateToCorrespondingGraph = null;

    /** This one is potentially capable to give a specific sequence of pseudo-random numbers
	 * for a supplied pair of states. This is important when testing with multiple threads which
	 * might run through pairs of states in a different order but the choice of random walks
	 * affects BCR and easily affects scores if we choose few walk sequences.
	 */
    public static class StateBasedRandom {

        private Map<CmpVertex, Random> rnd = new TreeMap<CmpVertex, Random>();

        private final int seed;

        public StateBasedRandom(int s) {
            seed = s;
        }

        public synchronized Random getRandom(CmpVertex A) {
            Random r = rnd.get(A);
            if (r == null) {
                r = new Random(seed);
                rnd.put(A, r);
            }
            return r;
        }
    }

    /** Given the number of cores to parallelise over, this method computes test/walk sequences 
	 * for each state of the supplied graph. Results are stored in the <em>stateToCorrespondingGraph</em> map
	 * which is accessible to the inner instances of <em>DDRH_BCR</em>.
	 * The graph to work from is the forward matrix.
	 * 
	 * @param randomGenerator the random number generator to be used for the construction of random walks.
	 * @param ThreadNumber the number of threads to use when building deterministic graphs and generating random walks. 
	 */
    public void computeWalkSequences(final StateBasedRandom randomGenerator, int ThreadNumber) {
        stateToCorrespondingGraph = new TreeMap<CmpVertex, GraphAndWalk>();
        final Map<CmpVertex, GraphAndWalk> workerMap[] = new Map[ThreadNumber];
        List<HandleRow<List<CmpVertex>>> handlerList = new LinkedList<HandleRow<List<CmpVertex>>>();
        for (int threadCnt = 0; threadCnt < ThreadNumber; ++threadCnt) {
            workerMap[threadCnt] = new TreeMap<CmpVertex, GraphAndWalk>();
            handlerList.add(new HandleRow<List<CmpVertex>>() {

                Map<CmpVertex, GraphAndWalk> stateToGraph = null;

                @Override
                public void init(int threadNo) {
                    stateToGraph = workerMap[threadNo];
                }

                int cnt = 0;

                @Override
                public void handleEntry(Entry<CmpVertex, Map<String, List<CmpVertex>>> entryA, @SuppressWarnings("unused") int threadNo) {
                    ++cnt;
                    TestDiagnostics.getDiagnostics().setStatus("starting on state " + (100. * cnt / matrixForward.getStateNumber()));
                    LearnerGraph deterministicGraph = new LearnerGraph(config);
                    try {
                        deterministicGraph = matrixForward.pathroutines.buildDeterministicGraph(entryA.getKey());
                    } catch (IncompatibleStatesException e) {
                        Helper.throwUnchecked("failed to build a deterministic graph due to inconsistent state labelling starting from state " + entryA.getKey(), e);
                    }
                    CmpVertex state = deterministicGraph.findVertex(entryA.getKey().getID());
                    GraphAndWalk graphwalk = null;
                    switch(config.getGdScoreComputationAlgorithm()) {
                        case SCORE_RANDOMPATHS:
                            graphwalk = new GraphAndWalk(deterministicGraph, null);
                            if (!deterministicGraph.transitionMatrix.get(deterministicGraph.getInit()).isEmpty()) {
                                int extraLength = 0;
                                if (deterministicGraph.getStateNumber() == 1) extraLength = 1;
                                RandomPathGenerator randomPaths = new RandomPathGenerator(deterministicGraph, randomGenerator.getRandom(entryA.getKey()), extraLength + config.getGdScoreComputationAlgorithm_RandomWalk_ExtraLength(), state, matrixForward.pathroutines.computeAlphabet());
                                if (config.getGdScoreComputationAlgorithm_RandomWalk_PathLength() > 0) randomPaths.setPathLength(config.getGdScoreComputationAlgorithm_RandomWalk_PathLength());
                                randomPaths.generateRandomPosNeg(config.getGdScoreComputationAlgorithm_RandomWalk_NumberOfSequences(), 1, false);
                                graphwalk = new GraphAndWalk(deterministicGraph, randomPaths.getAllSequences(0));
                            }
                            break;
                        case SCORE_TESTSET:
                            graphwalk = new GraphAndWalk(deterministicGraph, deterministicGraph.wmethod.computeNewTestSet(state, config.getGdScoreComputationAlgorithm_TestSet_ExtraStates()));
                            deterministicGraph.learnerCache.invalidate();
                            break;
                        default:
                            break;
                    }
                    stateToGraph.put(state, graphwalk);
                }
            });
        }
        GDLearnerGraph.performRowTasks(handlerList, ThreadNumber, matrixForward.transitionMatrix, new LearnerGraphND.ignoreNoneClass(), GDLearnerGraph.partitionWorkLoadLinear(ThreadNumber, matrixForward.getStateNumber()));
        for (int th = 0; th < ThreadNumber; ++th) stateToCorrespondingGraph.putAll(workerMap[th]);
        assert stateToCorrespondingGraph.size() == matrixForward.getStateNumber();
    }

    public class DDRH_BCR extends DetermineDiagonalAndRightHandSide {

        @Override
        public double getDiagonal() {
            if (config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH) return totalOutgoing * 2;
            return 1;
        }

        double rightHandSide = -1;

        @Override
        public double getRightHandSide() {
            return rightHandSide;
        }

        @Override
        public void compute(CmpVertex stateA, CmpVertex stateB, Map<String, List<CmpVertex>> rowA, Map<String, List<CmpVertex>> rowB) {
            totalOutgoing = 0;
            if (config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH) super.compute(stateA, stateB, rowA, rowB);
            boolean incompatible = !AbstractLearnerGraph.checkCompatible(stateA, stateB, pairCompatibility);
            if (incompatible) rightHandSide = getRightHandSideForIncompatible(); else {
                switch(config.getGdScoreComputationAlgorithm()) {
                    case SCORE_RANDOMPATHS:
                    case SCORE_TESTSET:
                        break;
                    default:
                        throw new IllegalArgumentException("invalid type " + config.getGdScoreComputationAlgorithm() + " of score computation algorithm used");
                }
                PTASequenceEngine testSequencesA = stateToCorrespondingGraph.get(stateA).testSequences, testSequencesB = stateToCorrespondingGraph.get(stateB).testSequences;
                double bcrA = 0., bcrB = 0.;
                {
                    PTA_computePrecisionRecall precRec = new PTA_computePrecisionRecall(stateToCorrespondingGraph.get(stateA).graph);
                    if (testSequencesB != null) precRec.crossWith(testSequencesB);
                    bcrA = precRec.getBCR();
                }
                {
                    PTA_computePrecisionRecall precRec = new PTA_computePrecisionRecall(stateToCorrespondingGraph.get(stateB).graph);
                    if (testSequencesA != null) precRec.crossWith(testSequencesA);
                    bcrB = precRec.getBCR();
                }
                double multiplier = 100.;
                if (config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH) rightHandSide = multiplier * (bcrA + bcrB) * totalOutgoing / 2.; else rightHandSide = multiplier * (bcrA + bcrB) / 2.;
            }
        }
    }

    /** This routine is used for testing of buildMatrix_internal. */
    public LSolver buildMatrix(final int ThreadNumber) {
        final int[] incompatiblePairs = new int[getPairNumber()];
        for (int i = 0; i < incompatiblePairs.length; ++i) incompatiblePairs[i] = PAIR_OK;
        final int pairsNumber = findIncompatiblePairs(incompatiblePairs, ThreadNumber);
        return buildMatrix_internal(incompatiblePairs, pairsNumber, ThreadNumber, null);
    }

    /** Given a number associated with some state pair in a collection <em>incompatiblePairs</em>,
	 * this method finds which pair the supplied number is associated with.
	 * 
	 * @param incompatiblePairs collection mapping pairs to numbers.
	 * @param key number to look for
	 * @return pair associated with the number.
	 */
    public static int findIndexOf(final int[] incompatiblePairs, int key) {
        int i = 0;
        for (; i < incompatiblePairs.length && incompatiblePairs[i] != key; ++i) ;
        if (i < incompatiblePairs.length) return i;
        return -1;
    }

    /** Used by <em>dumpEquations</em>. */
    public static void appendPair(StringBuffer buffer, PairScore pair, Map<CmpVertex, CmpVertex> newToOrig) {
        CmpVertex Q = pair.getQ(), R = pair.getR(), origQ = null, origR = null;
        if (newToOrig != null) {
            origQ = newToOrig.get(Q);
            origR = newToOrig.get(R);
        }
        if (origQ != null) Q = origQ;
        if (origR != null) R = origR;
        buffer.append("[");
        buffer.append(Q);
        buffer.append(",");
        buffer.append(R);
        buffer.append("]");
    }

    /** Returns a human-readable form of a transition matrix, where state names have been converted
	 * using the supplied map.
	 * 
	 * @param solver what to dump
	 * @param incompatiblePairs map from vertex pairs to their IDs in the matrices of the solver.
	 * @param newToOrig translation of state names. This is needed if this method is used to 
	 * display equations computed as a part of GD where vertices are renamed but one 
	 * would like to see equations in terms of the original vertices. No translation 
	 * is performed for vertices not found in this map or if <em>newToOrig</em> is null.
	 * @return the textual representation of the system of equations stored in <em>solver</em>.
	 */
    public String dumpEquations(LSolver solver, final int[] incompatiblePairs, Map<CmpVertex, CmpVertex> newToOrig) {
        DoubleMatrix2D matrix = solver.toDoubleMatrix2D();
        DoubleMatrix1D b = solver.toDoubleMatrix1D();
        StringBuffer result = new StringBuffer();
        for (int row = 0; row < matrix.rows(); ++row) {
            int indexRow = findIndexOf(incompatiblePairs, row);
            assert indexRow >= 0;
            PairScore pairRow = getPairScore(indexRow, 0, 0);
            boolean firstValue = true;
            for (int column = 0; column < matrix.columns(); ++column) {
                double value = matrix.getQuick(row, column);
                if (value != 0) {
                    int indexColumn = findIndexOf(incompatiblePairs, column);
                    assert indexColumn >= 0;
                    PairScore pairColumn = getPairScore(indexColumn, 0, 0);
                    if (!firstValue) result.append(" + "); else firstValue = false;
                    result.append(value);
                    result.append("(");
                    appendPair(result, pairRow, newToOrig);
                    result.append(":");
                    appendPair(result, pairColumn, newToOrig);
                    result.append(")");
                }
            }
            result.append(" = ");
            result.append(b.getQuick(row));
            result.append("\n");
        }
        return result.toString();
    }

    public LSolver buildMatrix_internal(final int[] incompatiblePairs, final int pairsNumber, final int ThreadNumber, final Class<? extends DetermineDiagonalAndRightHandSideInterface> ddrh) {
        final int expectedMatrixSize = getExpectedIncomingPerPairOfStates() * pairsNumber;
        final int Ap[] = config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH ? new int[pairsNumber + 1] : null;
        final int Ap_threadStart[] = new int[ThreadNumber + 1];
        for (int i = 0; i < Ap_threadStart.length; ++i) Ap_threadStart[i] = -1;
        final IntArrayList Ai_array[] = new IntArrayList[ThreadNumber];
        final DoubleArrayList Ax_array[] = new DoubleArrayList[ThreadNumber];
        final double b[] = new double[pairsNumber];
        final int currentPosition[] = new int[ThreadNumber];
        final double k = config.getAttenuationK();
        List<HandleRow<List<CmpVertex>>> handlerList = new LinkedList<HandleRow<List<CmpVertex>>>();
        for (int threadCnt = 0; threadCnt < ThreadNumber; ++threadCnt) handlerList.add(new HandleRow<List<CmpVertex>>() {

            IntArrayList tmpAi = null;

            /** Used to detect non-consecutive state pair numbers - in this case an internal error should be reported. */
            int prevStatePairNumber = -1;

            final int debugThread = -1;

            DetermineDiagonalAndRightHandSideInterface ddrhInstance = null;

            @Override
            public void init(int threadNo) {
                if (ddrh == null) ddrhInstance = new DDRH_default(); else try {
                    ddrhInstance = ddrh.getDeclaredConstructor(new Class[] { GDLearnerGraph.class }).newInstance(new Object[] { GDLearnerGraph.this });
                } catch (Exception e) {
                    Helper.throwUnchecked("failed to create an instance of ddrh", e);
                }
                if (config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH) {
                    tmpAi = new IntArrayList(getExpectedIncomingPerPairOfStates() * pairsNumber);
                    Ai_array[threadNo] = new IntArrayList(expectedMatrixSize / ThreadNumber + getExpectedIncomingPerPairOfStates());
                    Ax_array[threadNo] = new DoubleArrayList(expectedMatrixSize / ThreadNumber + getExpectedIncomingPerPairOfStates());
                }
                currentPosition[threadNo] = 0;
            }

            Set<Integer> sourceData = new TreeSet<Integer>();

            @Override
            public void handleEntry(Entry<CmpVertex, Map<String, List<CmpVertex>>> entryA, int threadNo) {
                IntArrayList Ai = Ai_array[threadNo];
                DoubleArrayList Ax = Ax_array[threadNo];
                Collection<Entry<String, List<CmpVertex>>> rowA_collection = matrixInverse.transitionMatrix.get(entryA.getKey()).entrySet();
                Iterator<Entry<CmpVertex, Map<String, List<CmpVertex>>>> stateB_It = matrixInverse.transitionMatrix.entrySet().iterator();
                while (stateB_It.hasNext()) {
                    Entry<CmpVertex, Map<String, List<CmpVertex>>> stateB = stateB_It.next();
                    Map<String, List<CmpVertex>> rowB = stateB.getValue();
                    int currentStatePair = incompatiblePairs[vertexToIntNR(stateB.getKey(), entryA.getKey())];
                    if (currentStatePair >= 0) {
                        assert prevStatePairNumber < 0 || currentStatePair == prevStatePairNumber + 1;
                        prevStatePairNumber = currentStatePair;
                        if (Ap_threadStart[threadNo] < 0) Ap_threadStart[threadNo] = currentStatePair;
                        if (debugThread == threadNo) System.out.println("thread " + threadNo + " is considering states: (" + entryA + "," + stateB + "), with state pair number " + currentStatePair);
                        int colEntriesNumber = 0;
                        ddrhInstance.compute(entryA.getKey(), stateB.getKey(), entryA.getValue(), matrixForward.transitionMatrix.get(stateB.getKey()));
                        b[currentStatePair] = ddrhInstance.getRightHandSide();
                        if (debugThread == threadNo) System.out.println("shared outgoing: " + ddrhInstance.getRightHandSide());
                        if (config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH) {
                            tmpAi.setQuick(colEntriesNumber++, currentStatePair);
                            for (Entry<String, List<CmpVertex>> outLabel : rowA_collection) {
                                List<CmpVertex> to = rowB.get(outLabel.getKey());
                                if (to != null) {
                                    sourceData.clear();
                                    int maxSize = colEntriesNumber + outLabel.getValue().size() * to.size();
                                    if (tmpAi.elements().length < maxSize) {
                                        if (linearWarningsEnabled) System.out.println("buildMatrix: warning - resizing arrays tmpAi[thread " + threadNo + "] from " + tmpAi.elements().length + " to " + maxSize);
                                        tmpAi.ensureCapacity(maxSize);
                                    }
                                    if (debugThread == threadNo) System.out.println("matched " + outLabel.getKey());
                                    for (CmpVertex srcA : outLabel.getValue()) for (CmpVertex srcB : to) {
                                        int sourcePair = incompatiblePairs[vertexToIntNR(srcB, srcA)];
                                        if (sourcePair >= 0 && !sourceData.contains(sourcePair)) {
                                            sourceData.add(sourcePair);
                                            if (debugThread == threadNo) System.out.println(outLabel.getKey() + " : " + srcB + "," + srcA);
                                            tmpAi.setQuick(colEntriesNumber++, sourcePair);
                                        }
                                    }
                                }
                            }
                            cern.colt.Sorting.quickSort(tmpAi.elements(), 0, colEntriesNumber, new cern.colt.function.IntComparator() {

                                @Override
                                public int compare(int o1, int o2) {
                                    return o1 - o2;
                                }
                            });
                            if (debugThread == threadNo) {
                                for (int i = 0; i < colEntriesNumber; ++i) System.out.print(tmpAi.getQuick(i) + " ");
                                System.out.println();
                            }
                            int pos = currentPosition[threadNo] - 1;
                            Ap[currentStatePair] = pos + 1;
                            int prev = -1;
                            boolean diagonalSet = false;
                            int expectedMaxSize = pos + colEntriesNumber + 1;
                            if (Ax.elements().length < expectedMaxSize) {
                                if (linearWarningsEnabled) System.out.println("buildMatrix: warning - resizing arrays Ax[thread " + threadNo + "] and Ai[thread " + threadNo + "] from " + Ax.elements().length + " to " + expectedMaxSize);
                                Ax.ensureCapacity(expectedMaxSize);
                                Ai.ensureCapacity(expectedMaxSize);
                            }
                            for (int i = 0; i < colEntriesNumber; ++i) {
                                int currentValue = tmpAi.getQuick(i);
                                if (currentValue != prev) {
                                    prev = currentValue;
                                    ++pos;
                                    if (!diagonalSet && currentValue == currentStatePair) {
                                        double rightHandSide = ddrhInstance.getDiagonal();
                                        if (rightHandSide == 0) rightHandSide = 1;
                                        if (debugThread == threadNo) System.out.println("setting diagonal to " + rightHandSide);
                                        Ax.setQuick(pos, rightHandSide);
                                        Ai.setQuick(pos, prev);
                                        diagonalSet = true;
                                    } else {
                                        Ax.setQuick(pos, -k);
                                        Ai.setQuick(pos, prev);
                                    }
                                } else Ax.setQuick(pos, Ax.getQuick(pos) - k);
                            }
                            ++pos;
                            if (debugThread == threadNo) {
                                System.out.println("thread " + threadNo + " results:");
                                for (int i = currentPosition[threadNo]; i < pos; ++i) System.out.println(i + ": " + Ai.getQuick(i) + " , " + Ax.getQuick(i));
                            }
                            currentPosition[threadNo] = pos;
                        }
                    }
                    if (stateB.getKey().equals(entryA.getKey())) break;
                }
            }
        });
        performRowTasks(handlerList, ThreadNumber, matrixForward.transitionMatrix, filter, GDLearnerGraph.partitionWorkLoadTriangular(ThreadNumber, matrixForward.transitionMatrix.size()));
        LSolver result = null;
        if (config.getGdScoreComputation() == GDScoreComputationEnum.GD_RH) {
            int size = 0;
            for (int thread = 0; thread < ThreadNumber; ++thread) size += currentPosition[thread];
            int Ai[] = new int[size];
            double Ax[] = new double[size];
            int currentLastStatePair = pairsNumber;
            for (int th = ThreadNumber; th >= 0; --th) if (Ap_threadStart[th] < 0) Ap_threadStart[th] = currentLastStatePair; else currentLastStatePair = Ap_threadStart[th];
            int prevLastPos = 0;
            for (int thread = 0; thread < ThreadNumber; ++thread) {
                int lastPair = Ap_threadStart[thread + 1];
                for (int i = Ap_threadStart[thread]; i < lastPair; ++i) Ap[i] += prevLastPos;
                if (currentPosition[thread] > 0) {
                    System.arraycopy(Ai_array[thread].elements(), 0, Ai, prevLastPos, currentPosition[thread]);
                    System.arraycopy(Ax_array[thread].elements(), 0, Ax, prevLastPos, currentPosition[thread]);
                    prevLastPos += currentPosition[thread];
                }
            }
            Ap[pairsNumber] = prevLastPos;
            result = new LSolver(Ap, Ai, Ax, b, new double[pairsNumber]);
        } else {
            result = new LSolver(pairsNumber) {

                {
                    System.arraycopy(b, 0, j_x, 0, pairsNumber);
                }

                @Override
                public void solve() {
                }
            };
        }
        return result;
    }

    /** Computes the compatibility between pairs of states.
	 * 
	 * @param ThreadNumber how many CPUs to use
	 * @return a map from numbers returned by <em>wmethod.vertexToIntNR(A,B)</em>
	 * to compatibility score of states <em>A</em> and <em>B</em>.
	 */
    public double[] computeStateCompatibility(int ThreadNumber, final Class<? extends DetermineDiagonalAndRightHandSideInterface> ddrh) {
        final int[] incompatiblePairs = new int[getPairNumber()];
        for (int i = 0; i < incompatiblePairs.length; ++i) incompatiblePairs[i] = PAIR_OK;
        final int pairsNumber = findIncompatiblePairs(incompatiblePairs, ThreadNumber);
        LSolver solver = buildMatrix_internal(incompatiblePairs, pairsNumber, ThreadNumber, ddrh);
        solver.solve();
        solver.freeAllButResult();
        double statePairScores[] = new double[incompatiblePairs.length];
        for (int i = 0; i < incompatiblePairs.length; ++i) if (incompatiblePairs[i] >= 0) statePairScores[i] = solver.j_x[incompatiblePairs[i]]; else statePairScores[i] = incompatiblePairs[i];
        return statePairScores;
    }

    /** This is a kind of an inverse of vertexToIntNR, takes a number and a score returns a corresponding <em>PairScore</em>.
	 */
    public PairScore getPairScore(int pair, int score, int compat) {
        int row = (int) (-1 + Math.sqrt(pair * 8 + 1)) / 2;
        int column = pair - row * (row + 1) / 2;
        assert row >= 0 && column >= 0;
        assert row < getStateNumber() && column < getStateNumber();
        assert column <= row;
        return new PairScore(getNumberToState()[row], getNumberToState()[column], score, compat);
    }
}
