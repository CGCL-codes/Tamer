package net.grinder.messages.console;

import net.grinder.communication.Message;
import net.grinder.statistics.TestStatisticsMap;

/**
 * Message used to report test statistics to the console.
 *
 * @author Philip Aston
 * @version $Revision: 3824 $
 */
public final class ReportStatisticsMessage implements Message {

    private static final long serialVersionUID = 5659643598627115683L;

    private final TestStatisticsMap m_statisticsDelta;

    /**
   * Constructor.
   *
   * @param statisticsDelta The test statistics.
   */
    public ReportStatisticsMessage(TestStatisticsMap statisticsDelta) {
        m_statisticsDelta = statisticsDelta;
    }

    /**
   * Get the test statistics.
   *
   * @return The test statistics.
   */
    public TestStatisticsMap getStatisticsDelta() {
        return m_statisticsDelta;
    }
}
