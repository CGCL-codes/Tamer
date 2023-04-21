package protopeer.measurement;

import org.apache.log4j.*;

/**
 * 
 * Aggregates all <code>MeasurementLog</code>s from all the peers that correspond to a specific measurement epoch number. 
 *
 */
public class MeasurementEpochSink {

    public static final long UNKNOWN_EPOCH_NUM = -1000;

    private static final Logger logger = Logger.getLogger(Logger.class);

    private MeasurementLog aggregateLog;

    private int numAppendedLogs;

    private long epochNum;

    /**
	 * The epoch number 
	 * @param epochNum
	 */
    public MeasurementEpochSink(long epochNum) {
        this.epochNum = epochNum;
        this.aggregateLog = new MeasurementLog(epochNum);
    }

    /**
	 * Appends the <code>log</code> to this sink, unless the <code>log.getEpochNum() != this.getEpochNum()</code> then logs an error.
	 * @param log
	 */
    public void appendMeasurementLog(MeasurementLog log) {
        if (log.getEpochNum() != epochNum) {
            logger.error("Attempting to add an epoch " + log.getEpochNum() + " MeasurementLog to an epoch " + epochNum + " MeasurementEpoch");
            return;
        }
        aggregateLog.mergeWith(log);
        numAppendedLogs++;
    }

    /**
	 * Returns the number of logs appended so far.
	 * @return
	 */
    public int getNumAppendedLogs() {
        return numAppendedLogs;
    }

    /**
	 * Returns this sink's measurement epoch number
	 * @return
	 */
    public long getEpochNum() {
        return epochNum;
    }

    /**
	 * Returns the aggregate log containting all the data from the logs appended with <code>appendMeasurementLog</code>. 
	 * @return
	 */
    public MeasurementLog getAggregateLog() {
        return aggregateLog;
    }
}
