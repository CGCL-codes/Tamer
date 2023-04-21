package gate.mimir.search;

import gate.mimir.index.IndexException;
import gate.mimir.search.query.Binding;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link QueryRunner} that presents a set of sub-indexes (represented by
 * their own QueryRunners) as a single index.
 */
public class FederatedQueryRunner implements QueryRunner {

    /**
   * The total number of result documents (or -1 if not yet known).
   */
    private int documentsCount = -1;

    /**
   * The query runners for the sub-indexes.
   */
    protected QueryRunner[] subRunners;

    /**
   * The next rank that needs to be merged from each sub runner.
   */
    protected int[] nextSubRunnerRank;

    /**
   * For each result document rank, this list supplies the index for the
   * sub-runner that supplied the document.
   */
    protected IntList rank2runnerIndex;

    /**
   * Which of the sub-runners has provided the previous document. This is an
   * instance field so that we can rotate the sub-runners (when the scores are
   * equal)
   */
    private int bestSubRunnerIndex = -1;

    /**
   * For each result document rank, this list supplies the rank of the document
   * in sub-runner that supplied it.
   */
    protected IntList rank2subRank;

    public FederatedQueryRunner(QueryRunner[] subrunners) {
        this.subRunners = subrunners;
        this.nextSubRunnerRank = null;
        this.rank2runnerIndex = new IntArrayList();
        this.rank2subRank = new IntArrayList();
    }

    @Override
    public int getDocumentsCount() {
        if (documentsCount < 0) {
            int newDocumentsCount = 0;
            for (QueryRunner subRunner : subRunners) {
                int subDocumentsCount = subRunner.getDocumentsCount();
                if (subDocumentsCount < 0) {
                    return -1;
                } else {
                    newDocumentsCount += subDocumentsCount;
                }
            }
            synchronized (this) {
                nextSubRunnerRank = new int[subRunners.length];
                for (int i = 0; i < nextSubRunnerRank.length; i++) {
                    if (subRunners[i].getDocumentsCount() == 0) {
                        nextSubRunnerRank[i] = -1;
                    }
                }
                documentsCount = newDocumentsCount;
            }
        }
        return documentsCount;
    }

    @Override
    public int getDocumentsCurrentCount() {
        if (documentsCount >= 0) {
            return documentsCount;
        } else {
            int newDocumentsCount = 0;
            for (QueryRunner subRunner : subRunners) {
                newDocumentsCount += subRunner.getDocumentsCurrentCount();
            }
            return newDocumentsCount;
        }
    }

    /**
   * Ensure that the given rank is resolved to the appropriate sub-runner rank.
   * @throws IndexOutOfBoundsException if rank is beyond the last document.
   */
    private final synchronized void checkRank(int rank) throws IndexOutOfBoundsException, IOException {
        if (rank < rank2runnerIndex.size()) {
            return;
        }
        for (int nextRank = rank2runnerIndex.size(); nextRank <= rank; nextRank++) {
            boolean allOut = true;
            bestSubRunnerIndex = (bestSubRunnerIndex + 1) % subRunners.length;
            double maxScore = Double.NEGATIVE_INFINITY;
            if (nextSubRunnerRank[bestSubRunnerIndex] >= 0) {
                maxScore = subRunners[bestSubRunnerIndex].getDocumentScore(nextSubRunnerRank[bestSubRunnerIndex]);
                allOut = false;
            }
            final int from = bestSubRunnerIndex + 1;
            final int to = bestSubRunnerIndex + subRunners.length;
            for (int bigI = from; bigI < to; bigI++) {
                int i = bigI % subRunners.length;
                if (nextSubRunnerRank[i] >= 0) {
                    allOut = false;
                    if (subRunners[i].getDocumentScore(nextSubRunnerRank[i]) > maxScore) {
                        bestSubRunnerIndex = i;
                        maxScore = subRunners[i].getDocumentScore(nextSubRunnerRank[i]);
                    }
                }
            }
            if (allOut) {
                throw new IndexOutOfBoundsException("Requested rank was " + rank + " but ran out of documents at " + nextRank + "!");
            }
            rank2runnerIndex.add(bestSubRunnerIndex);
            rank2subRank.add(nextSubRunnerRank[bestSubRunnerIndex]);
            if (nextSubRunnerRank[bestSubRunnerIndex] < subRunners[bestSubRunnerIndex].getDocumentsCount() - 1) {
                nextSubRunnerRank[bestSubRunnerIndex]++;
            } else {
                nextSubRunnerRank[bestSubRunnerIndex] = -1;
            }
        }
    }

    @Override
    public int getDocumentID(int rank) throws IndexOutOfBoundsException, IOException {
        checkRank(rank);
        int subId = subRunners[rank2runnerIndex.getInt(rank)].getDocumentID(rank2subRank.getInt(rank));
        return subId * subRunners.length + rank2runnerIndex.getInt(rank);
    }

    @Override
    public double getDocumentScore(int rank) throws IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentScore(rank2subRank.getInt(rank));
    }

    @Override
    public List<Binding> getDocumentHits(int rank) throws IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentHits(rank2subRank.getInt(rank));
    }

    @Override
    public String[][] getDocumentText(int rank, int termPosition, int length) throws IndexException, IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentText(rank2subRank.getInt(rank), termPosition, length);
    }

    @Override
    public String getDocumentURI(int rank) throws IndexException, IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentURI(rank2subRank.getInt(rank));
    }

    @Override
    public String getDocumentTitle(int rank) throws IndexException, IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentTitle(rank2subRank.getInt(rank));
    }

    @Override
    public Serializable getDocumentMetadataField(int rank, String fieldName) throws IndexException, IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentMetadataField(rank2subRank.getInt(rank), fieldName);
    }

    @Override
    public Map<String, Serializable> getDocumentMetadataFields(int rank, Set<String> fieldNames) throws IndexException, IndexOutOfBoundsException, IOException {
        checkRank(rank);
        return subRunners[rank2runnerIndex.getInt(rank)].getDocumentMetadataFields(rank2subRank.getInt(rank), fieldNames);
    }

    @Override
    public void renderDocument(int rank, Appendable out) throws IOException, IndexException {
        checkRank(rank);
        subRunners[rank2runnerIndex.getInt(rank)].renderDocument(rank2subRank.getInt(rank), out);
    }

    @Override
    public void close() throws IOException {
        for (QueryRunner r : subRunners) {
            r.close();
        }
    }
}
