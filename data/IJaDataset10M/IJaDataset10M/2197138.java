package API;

import Core.SectionRepresentation;
import Core.XMLRepresentation;

/**
 * @author SungHun Park (dr.superchamp@gmail.com)
 *
 */
public interface SITable extends XMLRepresentation, SectionRepresentation, Cloneable {

    /**
	 * return a unique id of SI table.
	 * This unique id is to distinguish identity of each table with same table_id.
	 * @return
	 */
    int getUniqueID();

    /**
	 * return a 'table_id'
	 * @return table_id
	 */
    TableID getTableID();

    /**
	 * return 'PID'
	 * @return table_PID
	 */
    int getTablePID();

    /**
	 * return a table version_number.
	 * @return version_number
	 */
    int getTableVersion();

    /**
	 * return start time offset(in milliseconds) of this table in a stream.
	 * This 'offset' indicates when this table starts being transferred in a stream.
	 * @return start time offset
	 */
    long getStartTime();

    /**
	 * return end time offset(in milliseconds) of this table in a stream.
	 * This 'offset' indicates when this table stops being transferred in a stream.
	 * @return end time offset
	 */
    long getEndTime();

    /**
	 * return a duration of this table in a stream. 
	 * @return duration in milliseconds
	 */
    long getDuration();

    /**
	 * return interval of this table in a stream. (in milliseconds)
	 * @return interval in milliseconds
	 */
    long getIntervalMillis();

    /**
	 * set start time offset
	 * @param stime new start time offset
	 */
    void setStartTime(long stime);

    /**
	 * set end time offset
	 * @param etime new end time offset
	 */
    void setEndTime(long etime);

    /**
	 * set interval
	 * @param millisec new interval in milliseconds
	 */
    void setIntervalMillis(long millisec);

    /**
	 * make a clone.
	 * @return a clone instance
	 */
    SITable clone();
}
