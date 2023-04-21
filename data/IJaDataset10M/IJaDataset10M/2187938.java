package org.melanesia.sql;

import java.sql.BatchUpdateException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A structure for holding results of the batch query execution. This class
 * holds information about update counts as returned from
 * {@link java.sql.PreparedStatement#executeBatch()}, and tries to build an
 * indexed array of exceptions, so the position of the exception on the
 * {@link #errorCauses} list corresponds to the index in {@link #updateCounts} .
 * This index is also the index of the query in the batch.
 *
 * Another difference from pure JDBC is that this class guarantees there will be
 * "some" result for every query in the batch, even if the batch fails at some
 * point, and the JBDC driver does not proceed with the remaining queries. (In
 * such case, there will be a {@link #NOT_EXECUTED} in the {@link #updateCounts}
 * , and {@link #ERROR_NOT_EXECUTED} in the {@link BatchResult#errorCauses}.
 *
 * Beside the above, the values returned by the {@link #getUpdateCounts()} are
 * as described in return clause of
 * {@link java.sql.PreparedStatement#executeBatch()}. Note - this class
 * overrides {@link java.sql.Statement#EXECUTE_FAILED} with {@link #FAILED}, and
 * {@link java.sql.Statement#SUCCESS_NO_INFO} with {@link #EXECUTED_NO_INFO} .
 * There is also a status value for situation, where query has not been executed
 * at all, due to errors on earlier position in the batch -
 * {@link #NOT_EXECUTED}.
 *
 * @author marcin.kielar
 *
 */
public class BatchResult {

    /** Update count special value for cases, where query has not been executed, because batch failed earlier. */
    public static final int NOT_EXECUTED = -10000;

    /** Override of {@link java.sql.Statement#SUCCESS_NO_INFO}. */
    public static final int EXECUTED_NO_INFO = Statement.SUCCESS_NO_INFO;

    /** Override of {@link java.sql.Statement#EXECUTE_FAILED}. */
    public static final int FAILED = Statement.EXECUTE_FAILED;

    /**
     * Custom exception for logging errors in cases of {@link #NOT_EXECUTED}
     * special value in {@link #updateCounts}.
     */
    private static final Throwable ERROR_NOT_EXECUTED = new Throwable("Query not executed due to earlier errors in the batch.") {

        private static final long serialVersionUID = 4995040704213896459L;

        @Override
        public Throwable fillInStackTrace() {
            return this;
        }

        ;
    };

    /**
     * List of update count values (including special status values:
     * {@link #FAILED}, {@link #EXECUTED_NO_INFO} and {@link #NOT_EXECUTED}.
     */
    private final List<Integer> updateCounts;

    /**
     * List of exception (causes) of errors. This list is indexed acordingly to
     * the order of queries in the batch. If the query was positively executed,
     * the list will contain null under the query's index. Also, this list will
     * contain a special pseudo-exception {@link #ERROR_NOT_EXECUTED} in case
     * the query has not been executed due to failure earlier in the batch.
     */
    private final List<Throwable> errorCauses;

    /** Flag indicating if the batch failed. */
    private final boolean isFailed;

    /**
     * Constructs a {@code BatchResult}.
     * @param submittedBatchSize
     *          size of the batch (i.e. number of queries that were batched before {@link org.melanesia.sql.Batch#execute()} was called.
     * @param updateCounts
     *          update counts array, as returned from the {@link org.melanesia.sql.Batch#execute()}
     * @param errorCauses
     *          exception iterator, in case batch execution failed with {@link java.sql.BatchUpdateException}
     *          - as returned from the {@link java.sql.BatchUpdateException#iterator()}
     */
    public BatchResult(final int submittedBatchSize, final int[] updateCounts, final Iterator<Throwable> errorCauses) {
        Integer[] updateCountsArray = new Integer[submittedBatchSize];
        Throwable[] errorCausesArray = new Throwable[submittedBatchSize];
        this.isFailed = errorCauses != null && errorCauses.hasNext();
        for (int i = 0; i < updateCounts.length; i++) {
            updateCountsArray[i] = updateCounts[i];
        }
        if (isFailed) {
            int n = 0;
            while (errorCauses.hasNext()) {
                Throwable cause = errorCauses.next();
                if (cause instanceof BatchUpdateException) {
                    continue;
                }
                int index = findNthFailedQuery(++n, updateCounts);
                errorCausesArray[index] = cause;
            }
        }
        for (int i = 0; i < updateCountsArray.length; i++) {
            if (updateCountsArray[i] == null) {
                if (errorCausesArray[i] != null) {
                    updateCountsArray[i] = FAILED;
                } else {
                    updateCountsArray[i] = NOT_EXECUTED;
                    errorCausesArray[i] = ERROR_NOT_EXECUTED;
                }
            }
        }
        this.updateCounts = Arrays.asList(updateCountsArray);
        this.errorCauses = Arrays.asList(errorCausesArray);
    }

    /**
     * Returns the batch execution failed flag.
     *
     * @return true if one or more of queries in the batch failed to execute,
     *         false if all were processed successfully
     */
    public final boolean isFailed() {
        return isFailed;
    }

    /**
     * Returns a list of update count values (including special status values:
     * {@link #FAILED}, {@link #EXECUTED_NO_INFO} and {@link #NOT_EXECUTED}.
     * This is basically the same as the array returned from
     * {@link java.sql.PreparedStatement#executeBatch()}, with the difference,
     * that it will always have size equal to the size of the batch, whereas the
     * size of the array returned from
     * {@link java.sql.PreparedStatement#executeBatch()} may vary depending on
     * failure point and implementation of JDBC driver. In case batch failed and
     * JDBC driver did not process the rest of the batch, unprocessed queries
     * will have a special status value of {@link #NOT_EXECUTED}.
     *
     * @return list of update counts or special status values
     */
    public final List<Integer> getUpdateCounts() {
        return Collections.unmodifiableList(updateCounts);
    }

    /**
     * Returns a list of exception (causes) of errors. This list is indexed
     * acordingly to the order of queries in the batch. If the query was
     * positively executed, the list will contain null under the query's index.
     * Also, this list will contain a special pseudo-exception
     * {@link #ERROR_NOT_EXECUTED} in case the query has not been executed due
     * to failure earlier in the batch.
     *
     * @return list of exceptions, indexed accordingly with query order inside
     *         the batch
     */
    public final List<Throwable> getErrorCauses() {
        return Collections.unmodifiableList(errorCauses);
    }

    /**
     * Utility method. Finds the index of N-th failed query in the batch. This
     * is used to index the exceptions in {@link #errorCauses} according to
     * query index in the batch.
     *
     * @param n
     *            number of failed query
     * @param updateCounts
     *            array of update count statuses to search in
     * @return index of the N-th failed query in the {@code updateCounts} array
     */
    private int findNthFailedQuery(final int n, final int[] updateCounts) {
        int index = -1;
        int cnt = 0;
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] == FAILED) {
                index = i;
                cnt++;
                if (cnt == n) {
                    break;
                }
            }
        }
        if (index == -1) {
            return updateCounts.length;
        } else {
            return index;
        }
    }
}
