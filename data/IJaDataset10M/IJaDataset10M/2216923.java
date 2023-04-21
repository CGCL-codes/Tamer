package net.sourceforge.olduvai.lrac.darkstarinterfacedataservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.olduvai.lrac.darkstardataservice.records.CellSwatchRecord;
import net.sourceforge.olduvai.lrac.genericdataservice.queries.QueryInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.queries.TemporalQuery;
import net.sourceforge.olduvai.lrac.genericdataservice.records.SwatchRecordInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.InputChannelInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.SourceInterface;

/**
 * Specific code for retrieving swatch values from various Daytona tables.  
 * @author peter
 *
 */
public class DaytonaSwatchQuery extends DaytonaQuery {

    private Set<InputChannelInterface> channels = new HashSet<InputChannelInterface>();

    /**
	 * This utility class has a list of channels for which the query retrieves 
	 * swatch values.  The list must be passed into the constructor.  The 
	 * {@link InputChannelInterface}.getInternalName() method is used to derive
	 * the column names returned from the Daytona cymbol query.  Therefore the 
	 * cymbol query must return columns of the form: 
	 * 
	 * Router, Min_<Channel internal name>, Max_<Channel internal name>, Sum_<Channel internal name>, Count 
	 *  
	 * Note that it is valid for there to be more than one set of channels per table for instance in 
	 * the case of the memory table where Memory_used and Memory_free are considered independent channels.
	 * 
	 * In this case the Min_, Max_, Sum_ are repeated for each passed in channel internal name.  
	 * 
	 * @param queryFilePath
	 * @param channel There is always at least one channel per query.  
	 */
    public DaytonaSwatchQuery(String name, String queryFilePath, InputChannelInterface channel) {
        super(name, queryFilePath);
        channels.add(channel);
    }

    /**
	 * Adds a channel to those managed by this daytona query.  
	 * 
	 * @param channel
	 */
    public void addChannel(InputChannelInterface channel) {
        channels.add(channel);
    }

    /**
	 * Takes a result set and builds a set of SwatchRecords out of it.  Note that the results of 
	 * the getInternalName() method of the channel list for this query is used to determine 
	 * which min/max/sum columns to search for.  
	 * 
	 * @param jdbcResultSet
	 * @param query
	 * @return
	 * @throws SQLException
	 */
    public List<SwatchRecordInterface> readSwatchResult(ResultSet jdbcResultSet, TemporalQuery query) throws SQLException {
        List<SwatchRecordInterface> result = new ArrayList<SwatchRecordInterface>();
        while (jdbcResultSet.next()) {
            final String sourceId = jdbcResultSet.getString("Router");
            final String interfaceId = jdbcResultSet.getString("Interface");
            final Calendar cal = Calendar.getInstance();
            final Date timeStampDate = jdbcResultSet.getDate("Timestamp_date");
            cal.setTime(timeStampDate);
            final int timeStampHour = Integer.parseInt(jdbcResultSet.getString("Timestamp_hour"));
            cal.set(Calendar.HOUR_OF_DAY, timeStampHour);
            final Date entryTime = cal.getTime();
            if (entryTime.compareTo(query.getBeginDate()) < 0 && entryTime.compareTo(query.getEndDate()) > 0) {
                continue;
            }
            final SourceInterface source = DataServiceDispatcher.getInstance().getResultInterface().getActiveSource(DaytonaJDBCReader.getSourceInterfacePair(sourceId, interfaceId));
            final int count = jdbcResultSet.getInt("Total_count");
            for (final InputChannelInterface channel : channels) {
                final float min = jdbcResultSet.getFloat("Min_" + channel.getInternalName());
                final float max = jdbcResultSet.getFloat("Max_" + channel.getInternalName());
                final float sum = jdbcResultSet.getFloat("Sum_" + channel.getInternalName());
                final SwatchRecordInterface record = CellSwatchRecord.createSwatchRecord(source, channel, min, max, sum, count, query.getQueryId());
                result.add(record);
            }
        }
        return result;
    }
}
