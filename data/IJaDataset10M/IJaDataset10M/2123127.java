package net.grinder.statistics;

/**
 * Statistics services.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 * @see StatisticsServicesImplementation
 */
public interface StatisticsServices {

    /**
   * Get the common detail {@link StatisticsView}.
   *
   * @return The {@link StatisticsView}.
   */
    StatisticsView getDetailStatisticsView();

    /**
   * Get the common summary {@link StatisticsView}.
   *
   * @return The {@link StatisticsView}.
   */
    StatisticsView getSummaryStatisticsView();

    /**
   * Return a {@link StatisticExpression} factory.
   *
   * @return A {@link StatisticExpressionFactoryImplementation}.
   */
    StatisticExpressionFactory getStatisticExpressionFactory();

    /**
   * Return a {@link StatisticsSet} factory.
   *
   * @return A {@link StatisticExpressionFactoryImplementation}.
   */
    StatisticsSetFactory getStatisticsSetFactory();

    /**
   * Return the {@link StatisticsIndexMap} for the current process.
   *
   * @return The {@link StatisticsIndexMap}.
   */
    StatisticsIndexMap getStatisticsIndexMap();

    /**
   * Return an object allowing access to common functions of test statistics.
   *
   * @return The {@link TestStatisticsQueries}.
   */
    TestStatisticsQueries getTestStatisticsQueries();
}
