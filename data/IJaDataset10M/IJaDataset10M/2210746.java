package org.swana.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Utils for AccessLog.
 *
 * @author Wang Yuxing
 */
public class AccessLogUtils {

    private static AccessLogUtils instance = new AccessLogUtils();

    private AccessLogUtils() {
    }

    public static AccessLogUtils getInstance() {
        return instance;
    }

    /**
	 * Get current table by current time and time offset.
	 * @param offset by date
	 * @return
	 */
    public String getCurrentTableName(int offset) {
        Calendar time = Calendar.getInstance();
        time.add(Calendar.DATE, offset);
        return getTableNameByDate(time);
    }

    /**
	 * Retrive date info and generate table name for access log.
	 */
    public String getTableNameByDate(Calendar date) {
        String tableName = "SWANA_ACCESS_LOG_";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        tableName += sdf.format(date.getTime());
        return tableName;
    }
}
