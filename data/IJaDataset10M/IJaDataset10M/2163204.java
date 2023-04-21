package net.grinder.statistics;

/**
 * A {@link StatisticExpression} that tracks the peak value of another
 * {@link StatisticExpression}. The monitored {@link
 * StatisticExpression} is specified when the {@link
 * PeakStatisticExpression} is created, see {@link
 * StatisticExpressionFactoryImplementation}.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 * @see StatisticExpressionFactoryImplementation
 **/
public interface PeakStatisticExpression extends StatisticExpression {

    /**
   * When called, the peak value of monitored expression applied to
   * <code>monitoredStatistics</code> is calculated and stored in the
   * given <code>peakStorageStatistics</code>.
   *
   * @param monitoredStatistics The monitored <code>StatisticsSet</code>.
   * @param peakStorageStatistics The <code>StatisticsSet</code> in
   * which to store the result.
   */
    void update(StatisticsSet monitoredStatistics, StatisticsSet peakStorageStatistics);
}
