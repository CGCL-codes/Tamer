package de.fzi.herakles.commons.statistic;

import org.apache.log4j.Logger;
import de.fzi.herakles.commons.Query;

/**
 * Implementation of the {@link StatisticListener} that simple logs the time needed by
 * different reasoners via log4j.
 * 
 * @author bock
 *
 */
public class LoggingStatisticListener implements StatisticListener {

    private String reasonerName = "";

    private long startTime;

    private Query currentQuery = null;

    private Logger logger;

    public LoggingStatisticListener() {
        logger = Logger.getLogger(LoggingStatisticListener.class);
    }

    public LoggingStatisticListener(String reasonerName) {
        logger = Logger.getLogger(LoggingStatisticListener.class);
        setReasonerName(reasonerName);
    }

    @Override
    public void finishTask() {
        logger.info(this.reasonerName + " finished " + this.currentQuery.getTask() + " in " + (System.currentTimeMillis() - this.startTime) + "ms");
    }

    @Override
    public void setReasonerName(String name) {
        this.reasonerName = name;
    }

    @Override
    public void startTask(Query task) {
        this.startTime = System.currentTimeMillis();
        this.currentQuery = task;
    }
}
