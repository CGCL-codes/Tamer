package com.xebia.jarep.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import com.xebia.jarep.CounterDatum;
import com.xebia.jarep.persister.Persister;

/**
 * Run this bean scheduled
 * 
 * @author shautvast
 */
public class DataCompressionUtil extends JdbcDaoSupport {

    private static Log log = LogFactory.getLog("jarep.DataCompressionUtil");

    private TransactionTemplate txTemplate;

    class counterCollectingRowCallbackHandler implements RowCallbackHandler {

        private java.sql.Timestamp timestamp;

        public counterCollectingRowCallbackHandler(Timestamp ts) {
            super();
            this.timestamp = ts;
        }

        private ArrayList newCounters = new ArrayList();

        public ArrayList getNewCounters() {
            return newCounters;
        }

        public void processRow(ResultSet rs) throws SQLException {
            CounterDatum c = new CounterDatum();
            c.setCounterId(rs.getInt(1));
            c.setHits(rs.getDouble(2));
            c.setAverageValue(rs.getDouble(3));
            c.setMaxValue(rs.getDouble(4));
            c.setTimestamp(timestamp);
            newCounters.add(c);
        }
    }

    public static final int HOURLY = 1;

    public static final int DAILY = 2;

    private static Log logger = LogFactory.getLog(DataCompressionUtil.class.getName());

    /**
   * 
   */
    private Persister persister;

    private String selectCompressableDatesStatement;

    /**
   * 
   */
    private String calculateCountersStatement;

    /**
   * The number of days from current date into the past that should not be
   * touched.
   */
    private int daysToIgnore;

    private String hourDatePattern;

    private String javaHourDatePattern;

    private String dayDatePattern;

    private String javaDayDatePattern;

    private String deleteStatement;

    /**
   * Aggregates data to hourly values.
   */
    public void compressDataHourly() {
        compressData(HOURLY);
    }

    /**
   * Aggregates data to daily values.
   */
    public void compressDataDaily() {
        compressData(DAILY);
    }

    /**
   * Aggregates data to hourly or daily values.
   */
    private void compressData(int modus) {
        if (logger.isInfoEnabled()) {
            logger.info("compressData started");
        }
        String datePattern;
        if (modus == HOURLY) {
            datePattern = hourDatePattern;
        } else if (modus == DAILY) {
            datePattern = dayDatePattern;
        } else {
            throw new RuntimeException("unknown modus");
        }
        calculateCountersStatement = calculateCountersStatement.replace("[datePattern]", "'" + datePattern + "'");
        deleteStatement = deleteStatement.replace("[datePattern]", "'" + datePattern + "'");
        SimpleDateFormat dateFormat;
        if (modus == DAILY) {
            dateFormat = new SimpleDateFormat(javaDayDatePattern);
        } else {
            dateFormat = new SimpleDateFormat(javaHourDatePattern);
        }
        log.debug("calculateCounters:" + calculateCountersStatement);
        for (Iterator dates = getDatesToCompress(datePattern).iterator(); dates.hasNext(); ) {
            final String dateString = (String) dates.next();
            Timestamp ts;
            try {
                java.util.Date date = dateFormat.parse(dateString);
                ts = new Timestamp(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("internal error: datepattern wrong");
            }
            counterCollectingRowCallbackHandler cb = new counterCollectingRowCallbackHandler(ts);
            getJdbcTemplate().query(calculateCountersStatement, new Object[] { dateString }, new int[] { Types.VARCHAR }, cb);
            for (Iterator counters = cb.getNewCounters().iterator(); counters.hasNext(); ) {
                try {
                    final CounterDatum counter = (CounterDatum) counters.next();
                    txTemplate.execute(new TransactionCallbackWithoutResult() {

                        public void doInTransactionWithoutResult(TransactionStatus status) {
                            getJdbcTemplate().update(deleteStatement, new Object[] { dateString, counter.getCounterId() }, new int[] { Types.VARCHAR, Types.BIGINT });
                        }
                    });
                    persister.store(counter);
                    log.debug(counter + " updated");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
   * @return
   */
    public List getDaysToCompress() {
        return getDatesToCompress(dayDatePattern);
    }

    /**
   * 
   * @return a list of dates as full hours that are suitable for compression
   * 
   */
    List getHoursToCompress() {
        return getDatesToCompress(hourDatePattern);
    }

    /**
   * @param datePattern
   * @return All dates that match the criteria. If you run this every day, there
   *         will be only one.
   */
    private List getDatesToCompress(String datePattern) {
        if (logger.isDebugEnabled()) {
            logger.debug("days to ignore:" + daysToIgnore);
            logger.debug(datePattern);
        }
        String formattedSql = selectCompressableDatesStatement.replace("[datePattern]", "'" + datePattern + "'");
        if (logger.isDebugEnabled()) {
            logger.debug("select dates " + formattedSql);
            logger.debug(daysToIgnore);
        }
        List result = getJdbcTemplate().query(formattedSql, new Object[] { new Integer(daysToIgnore) }, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                String date = rs.getString(1);
                return date;
            }
        });
        return result;
    }

    public int getDaysToIgnore() {
        return daysToIgnore;
    }

    /**
   * If you want to keep details until for example 30 days (not including
   * today), then set this to 30. All older data will be touched. If you need
   * more than 1 run<br>
   * for example:<br>
   * <li>older than 30 days: aggregate to hourly data</li>
   * <li>older than 180 days: aggregate to daily data</li>
   * then instantiate 2 non-singleton beans using the correct properties.
   * 
   * @param daysToIgnore
   */
    public void setDaysToIgnore(int daysToIgnore) {
        this.daysToIgnore = daysToIgnore;
    }

    public String getSelectCompressableDatesStatement() {
        return selectCompressableDatesStatement;
    }

    public void setSelectCompressableDatesStatement(String selectCompressableDatesStatement) {
        this.selectCompressableDatesStatement = selectCompressableDatesStatement;
    }

    /**
   * @return the calculateAverage
   */
    public String getCalculateCountersStatement() {
        return calculateCountersStatement;
    }

    /**
   * @param calculateAverage
   *          the calculateAverage to set
   */
    public void setCalculateCountersStatement(String calculateCountersStatement) {
        this.calculateCountersStatement = calculateCountersStatement;
    }

    public Persister getPersister() {
        return persister;
    }

    public void setPersister(Persister persister) {
        this.persister = persister;
    }

    public String getHourDatePattern() {
        return hourDatePattern;
    }

    public void setHourDatePattern(String hourDatePattern) {
        this.hourDatePattern = hourDatePattern;
    }

    public String getDayDatePattern() {
        return dayDatePattern;
    }

    public void setDayDatePattern(String dayDatePattern) {
        this.dayDatePattern = dayDatePattern;
    }

    public String getDeleteStatement() {
        return deleteStatement;
    }

    public void setDeleteStatement(String deleteStatement) {
        this.deleteStatement = deleteStatement;
    }

    public String getJavaHourDatePattern() {
        return javaHourDatePattern;
    }

    public void setJavaHourDatePattern(String javaHourDatePattern) {
        this.javaHourDatePattern = javaHourDatePattern;
    }

    public String getJavaDayDatePattern() {
        return javaDayDatePattern;
    }

    public void setJavaDayDatePattern(String javaDayDatePattern) {
        this.javaDayDatePattern = javaDayDatePattern;
    }

    public void setTransactionManager(PlatformTransactionManager txManager) {
        this.txTemplate = new TransactionTemplate(txManager);
        this.txTemplate.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_NEVER);
    }
}
