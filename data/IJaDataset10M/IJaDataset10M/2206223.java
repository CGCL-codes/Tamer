package shotgun.metrics;

import shotgun.Predictions;

/**
 * Interface for custom specified performance metrics that measure the
 * "goodness" of a set of predictions.
 */
public interface MetricBundle {

    /**
   * Initialize the metrics from zero or more argument strings.
   * @param arg The argument string specified on the command line
   *    to initialize the custom metrics.
   * @return True if initialization succeeds, false otherwise.
   */
    public boolean init(String arg);

    /**
   * Gets the number of performance metrics implemented by the class.
   * @return The number of metrics implemented. Always >= 1.
   */
    public int count();

    /**
   * Gets the name of indexed metric, for use in displaying results.
   * @param i The zero-based index of the queried metric. Must be < size().
   * @return The non-null name of the indexed metric.
   */
    public String name(int i);

    /**
   * Compute the performance of the given predictions using the indexed metric.
   * @param i The zero-based index of the queried metric. Must be <
   * size().
   * @param pred The predictions to use for measuring performance.  Non-null.
   * @return The computed performance.  Some metrics will define a low
   * number as "good".  Use the {@link #smallerIsBetter
   * smallerIsBetter} method to check if a metric uses low numbers for
   * good performance.  */
    public double performance(int i, Predictions pred);

    /**
   * Compute the loss of the given predictions with respect to the indexed
   * metric.
   * @param i The zero-based index of the queried metric; i < size().
   * @param pred The predictions used for measuring performance. Non-null.
   * @param perf The pre-calculated performance for the indexed metric.
   * @return The computed loss. 0 indicates perfect performance.
   */
    public double loss(int i, Predictions pred, double perf);

    /**
   * Checks if the indexed metric gives low numbers for good performance.
   * @param i The zero-based index of the queried metric. Must be <
   * size().
   * @return True iff lower numbers are better than high numbers for
   * the metric.  */
    public boolean smallerIsBetter(int i);

    /**
   * Invalidate any cached results for the given prediction values.
   * If the metric does no caching this is a no-op.
   * @param pred The object encapsulating the predictions that have changed.
   */
    public void invalidateCache(Predictions pred);

    /**
   * Check if indexed metric expects the order of instances in test
   * sets to be strictly maintained.  This is false unless the metric
   * assumes the ordering to be able to align the predictions with
   * external data needed to compute performance.  If this is true,
   * it means that ensemble selection should not allow the optional
   * bootstrap or cross-validation modes.
   * @param i The zero-based index of the queried metric; i < size()
   * @return True if instance-order must be maintained.
   */
    public boolean requiresStrictOrder(int i);

    /**
   * Check if indexed metric's performance is sensitive to the
   * threshold between positive and negative predictions (as
   * determined by the Predictions class).
   * @param i The zero-based index of the queried metric; i < size()
   * @return True if changing the threshold changes performance.
   */
    public boolean thresholdSensitive(int i);
}
