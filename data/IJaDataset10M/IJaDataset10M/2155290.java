package uk.ac.ebi.intact.util;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.accesslayer.LookupException;
import uk.ac.ebi.intact.application.statisticView.business.data.NoDataException;
import uk.ac.ebi.intact.application.statisticView.business.data.StatisticsBean;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.business.IntactHelper;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Holds statistics data.
 *
 * @author Samuel Kerrien
 */
public final class StatisticsDataSet {

    public static final long DATA_NOT_LOADED = -1;

    public static final long NONE = -1;

    public static final long MAXIMUM_DATA_AVAILABILITY = 1000 * 60 * 60 * 24;

    public static final SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MMM-yyyy");

    /**
     * single Instance of this class
     */
    private static StatisticsDataSet ourInstance;

    /**
     * Data loading logger
     */
    private static Logger logger;

    /**
     * the statistics
     */
    private static List<IntactStatistics> statistics;

    /**
     * Information related to the origin of the loaded data
     */
    private static String databaseName;

    private static String userName;

    /**
     * When the last data have been loaded ?
     */
    private static long timestamp = DATA_NOT_LOADED;

    public static synchronized StatisticsDataSet getInstance(final String loggerName) {
        if (ourInstance == null) {
            ourInstance = new StatisticsDataSet(loggerName);
        } else {
            if (dataOutDated()) {
                try {
                    collectStatistics();
                } catch (IntactException e) {
                    logger.error("Error when trying to refresh the existing data.", e);
                }
            } else {
                logger.info("Data are not out dated, reuse them.");
            }
        }
        return ourInstance;
    }

    public static synchronized StatisticsDataSet getInstance() {
        return getInstance(StatisticsDataSet.class.getName());
    }

    private StatisticsDataSet(final String loggerName) {
        logger = Logger.getLogger(loggerName);
        try {
            collectStatistics();
        } catch (IntactException e) {
            logger.error("Error when trying to collect the data.", e);
        }
    }

    private static boolean dataOutDated() {
        final long currentTime = System.currentTimeMillis();
        final boolean outdated = ((timestamp + MAXIMUM_DATA_AVAILABILITY) < currentTime);
        logger.info("Data out dated: " + outdated);
        return outdated;
    }

    /**
     * This private method is called by the others public method of this class.
     * <p/>
     * Get all data of the IA_Statistics table in the IntAct database, thanks to the search method managing an
     * IntactHelper object. The null parameter in the search method means to retrieve all the data from a table via OJB
     * and JDBC.
     */
    private static void collectStatistics() throws IntactException {
        logger.info("retreiving all statistics...");
        try {
            logger.info("creating IntactHelper...");
            final IntactHelper helper = new IntactHelper();
            try {
                userName = helper.getDbUserName();
                databaseName = helper.getDbName();
                logger.info("Helper created - access to database " + databaseName + " as " + userName);
            } catch (LookupException e) {
                logger.error("Error when trying to get the database and username.", e);
            } catch (SQLException e) {
                logger.error("Error when trying to get the database and username.", e);
            }
            logger.info("Look for statistics...");
            final Collection<IntactStatistics> intactStatistics = helper.search(IntactStatistics.class, "ac", null);
            logger.info("closing IntactHelper...");
            helper.closeStore();
            timestamp = System.currentTimeMillis();
            logger.info("Content of the table before sorting");
            for (IntactStatistics singleItem : intactStatistics) {
                logger.info(singleItem);
            }
            if (intactStatistics.size() > 0) {
                statistics = new ArrayList<IntactStatistics>(intactStatistics);
                Collections.sort(statistics);
            } else {
                statistics = Collections.EMPTY_LIST;
            }
            logger.info("Content of the table after sorting");
            for (IntactStatistics singleItem : statistics) {
                logger.info(singleItem);
            }
        } catch (IntactException ie) {
            if (null != logger) {
                logger.error("when trying to get all statistics, cause: " + ie.getRootCause(), ie);
            }
            throw ie;
        }
    }

    /**
     * Return the data collected, this is the only public method.
     *
     * @return a bean containing the statistics and the database source details.
     */
    public final synchronized StatisticsBean getStatisticBean() throws NoDataException {
        return getStatisticBean(null, null);
    }

    /**
     * Return the data collected, this is the only public method.
     *
     * @param startDate the date from which we want to display the statistics. null indicated that there is no start
     *                  date. That date has to respect the format defined by the SimpleDateFormater (DD-MMM-YYYY)
     * @param stopDate  the date until which we want to display the statistics. null indicated that there is no end
     *                  date. That date has to respect the format defined by the SimpleDateFormater (DD-MMM-YYYY)
     *
     * @return a bean containing the statistics and the database source details.
     */
    public final synchronized StatisticsBean getStatisticBean(final String startDate, final String stopDate) throws NoDataException {
        if (statistics == null || statistics.size() == 0) {
            throw new NoDataException();
        }
        long start = NONE;
        if (startDate != null) {
            try {
                start = dateFormater.parse(startDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        long stop = NONE;
        if (stopDate != null) {
            try {
                stop = dateFormater.parse(stopDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (start != NONE || stop != NONE) {
            final List<IntactStatistics> filteredStatistics = new ArrayList<IntactStatistics>();
            for (IntactStatistics statistic : statistics) {
                final long timestamp = statistic.getTimestamp().getTime();
                boolean keepIt = true;
                if (start != NONE) {
                    if (timestamp < start) {
                        keepIt = false;
                    }
                }
                if (stop != NONE) {
                    if (timestamp > stop) {
                        keepIt = false;
                    }
                }
                if (keepIt) {
                    filteredStatistics.add(statistic);
                }
            }
            return new StatisticsBean(filteredStatistics, databaseName, userName);
        }
        return new StatisticsBean(statistics, databaseName, userName);
    }
}
