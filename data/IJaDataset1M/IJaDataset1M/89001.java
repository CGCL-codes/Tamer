package com.xebia.jarep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.xebia.jarep.Counter;
import com.xebia.jarep.CounterDatum;

/**
 * Default implementation of the {@link CounterDao}. Database actions for counters and counterdata
 *
 * @author sander
 */
public class CounterDaoImpl implements CounterDao {

    private JdbcTemplate jdbcTemplate;

    private String selectIdNameFromCountersQuery;

    private String selectAllFromCounterDataQuery;

    private String selectOrderedCountersQuery;

    /**
   * {@inheritDoc}
   */
    @SuppressWarnings("unchecked")
    public Set<Counter> getCounters(String filter) {
        if (filter == null || filter.equals("")) {
            return getCounters();
        }
        filter = filter.replace('*', '%');
        return new TreeSet<Counter>(jdbcTemplate.query(selectIdNameFromCountersQuery + " where name like ? order by name", new Object[] { filter }, new RowMapper() {

            public Object mapRow(ResultSet dbresult, int rownum) throws SQLException {
                return new Counter(dbresult.getInt("id"), dbresult.getString("name"));
            }
        }));
    }

    /**
   * {@inheritDoc}
   */
    @SuppressWarnings("unchecked")
    public Set<Counter> getCounters(String filter, Date from, Date until) {
        if (filter == null || filter.equals("")) {
            filter = "%";
        }
        filter = filter.replace('*', '%');
        int argsCount = 1;
        StringBuilder queryBuilder = new StringBuilder(250);
        queryBuilder.append(selectIdNameFromCountersQuery);
        queryBuilder.append(" where name like ? ");
        if (from != null) {
            argsCount++;
        }
        if (until != null) {
            argsCount++;
        }
        if (argsCount > 1) {
            queryBuilder.append(" and id in (");
            queryBuilder.append(" select distinct(counter) from counterdata where ");
            if (from != null) {
                queryBuilder.append(" ts >= ? ");
                if (until != null) {
                    queryBuilder.append(" and ");
                }
            }
            if (until != null) {
                queryBuilder.append(" ts <= ? ");
            }
            queryBuilder.append(") ");
        }
        queryBuilder.append(" order by name ");
        Object[] queryArgs = new Object[argsCount];
        queryArgs[0] = filter;
        int argsIndex = 1;
        if (from != null) {
            queryArgs[argsIndex] = from;
            argsIndex++;
        }
        if (until != null) {
            queryArgs[argsIndex] = until;
        }
        return new TreeSet<Counter>(jdbcTemplate.query(queryBuilder.toString(), queryArgs, new RowMapper() {

            public Object mapRow(ResultSet dbresult, int rownum) throws SQLException {
                return new Counter(dbresult.getInt("id"), dbresult.getString("name"));
            }
        }));
    }

    /**
   * {@inheritDoc}
   */
    public Counter getCounter(int id) {
        return (Counter) jdbcTemplate.queryForObject(selectIdNameFromCountersQuery + " where id=?", new Object[] { id }, new RowMapper() {

            public Object mapRow(ResultSet dbresult, int index) throws SQLException {
                return new Counter(dbresult.getInt("id"), dbresult.getString("name"));
            }
        });
    }

    /**
   * {@inheritDoc}
   */
    @SuppressWarnings("unchecked")
    public List<Counter> getCounters(List<Integer> counterIds) {
        StringBuilder builder = new StringBuilder(selectIdNameFromCountersQuery + " where id in(");
        appendCSIdsStringToStringBuilder(counterIds, builder);
        builder.append(") order by id");
        return (List<Counter>) jdbcTemplate.query(builder.toString(), new RowMapper() {

            public Object mapRow(ResultSet dbresult, int rsindex) throws SQLException {
                return new Counter(dbresult.getInt("id"), dbresult.getString("name"));
            }
        });
    }

    /**
   * Added a CS list of the supplied list of counterIds to the supplied builder.
   * 
   * @param counterIds
   *          the ids to add to the CS list
   * @param builder
   *          the builder to append the CS list to
   */
    private void appendCSIdsStringToStringBuilder(List<Integer> counterIds, StringBuilder builder) {
        for (Iterator<Integer> countersIt = counterIds.iterator(); countersIt.hasNext(); ) {
            Integer counterId = (Integer) countersIt.next();
            builder.append(counterId);
            if (countersIt.hasNext()) {
                builder.append(",");
            }
        }
    }

    /**
   * {@InheritDoc}
   */
    @SuppressWarnings("unchecked")
    public Set<Counter> getCounters() {
        return new TreeSet<Counter>(jdbcTemplate.query(selectIdNameFromCountersQuery, new RowMapper() {

            public Object mapRow(ResultSet dbresult, int rownum) throws SQLException {
                return new Counter(dbresult.getInt(1), dbresult.getString(2));
            }
        }));
    }

    /**
   * {@InheritDoc}
   */
    public Map<Counter, List<CounterDatum>> getCounterData(final List<Counter> counters) {
        return getCounterData(counters, null, null);
    }

    /**
   * {@InheritDoc}
   */
    public Map<Counter, List<CounterDatum>> getCounterData(final List<Counter> counters, Date start, Date end) {
        if (counters.size() == 0) {
            return new HashMap<Counter, List<CounterDatum>>();
        }
        StringBuilder builder = new StringBuilder(selectAllFromCounterDataQuery + " where counter in(");
        List<Integer> counterIds = new ArrayList<Integer>();
        for (Counter counter : counters) {
            counterIds.add(counter.getId());
        }
        appendCSIdsStringToStringBuilder(counterIds, builder);
        builder.append(")");
        if (start != null) {
            builder.append(" and ts>?");
        }
        if (end != null) {
            builder.append(" and ts<?");
        }
        builder.append(" order by counter, ts");
        Object[] parms = null;
        if (start != null) {
            if (end != null) {
                parms = new Object[] { start, end };
            } else {
                parms = new Object[] { start };
            }
        } else if (end != null) {
            parms = new Object[] { end };
        }
        CounterDataRowCallbackHandler counterRowCallbackHandler = new CounterDataRowCallbackHandler(counters);
        jdbcTemplate.query(builder.toString(), parms, counterRowCallbackHandler);
        return counterRowCallbackHandler.getResult();
    }

    /**
   * {@inheritDoc}
   */
    @SuppressWarnings("unchecked")
    public List<Counter> getCountersForTopList(String filter, String order, Date start, Date end, int size) {
        Object[] parameters;
        String select = selectOrderedCountersQuery;
        if (filter != null) {
            if ("%".equals(filter)) {
                filter = "";
            }
            if (!"".equals(filter)) {
                if (!filter.endsWith("%")) {
                    filter = filter + "%";
                }
                filter = "where name like '" + filter + "'";
            }
        } else {
            filter = "";
        }
        select = select.replace("$order$", order);
        select = select.replace("$wherelike$", filter);
        String andStart;
        if (start != null) {
            andStart = "and ts>?";
        } else {
            andStart = "";
        }
        select = select.replace("$start$", andStart);
        String andEnd;
        if (end != null) {
            andEnd = "and ts<?";
        } else {
            andEnd = "";
        }
        select = select.replace("$end$", andEnd);
        if (start == null) {
            if (end == null) {
                parameters = new Object[] { size };
            } else {
                parameters = new Object[] { end, size };
            }
        } else {
            if (end == null) {
                parameters = new Object[] { start, size };
            } else {
                parameters = new Object[] { start, end, size };
            }
        }
        return jdbcTemplate.query(select, parameters, new RowMapper() {

            public Object mapRow(ResultSet dbresult, int rownum) throws SQLException {
                return getCounter(dbresult.getInt("counter"));
            }
        });
    }

    /**
   * @param dataSource
   *          the dataSource to set
   */
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
   * @see com.xebia.jarep.dao.CounterDao#getSelectIdNameFromCountersQuery()
   */
    public String getSelectIdNameFromCountersQuery() {
        return selectIdNameFromCountersQuery;
    }

    /**
   * @param selectIdNameFromCounters
   *          the selectIdNameFromCounters to set
   */
    public void setSelectIdNameFromCountersQuery(String selectIdNameFromCounters) {
        this.selectIdNameFromCountersQuery = selectIdNameFromCounters;
    }

    /**
   * @see com.xebia.jarep.dao.CounterDao#getSelectAllFromCounterDataQuery()
   */
    public String getSelectAllFromCounterDataQuery() {
        return selectAllFromCounterDataQuery;
    }

    /**
   * @param selectAllFromCounterData
   *          the selectAllFromCounterData to set
   */
    public void setSelectAllFromCounterDataQuery(String selectAllFromCounterData) {
        this.selectAllFromCounterDataQuery = selectAllFromCounterData;
    }

    /**
   * @see com.xebia.jarep.dao.CounterDao#getSelectOrderedCountersQuery()
   */
    public String getSelectOrderedCountersQuery() {
        return selectOrderedCountersQuery;
    }

    /**
   * @param selectOrderedCounters
   *          the selectOrderedCounters to set
   */
    public void setSelectOrderedCountersQuery(String selectOrderedCounters) {
        this.selectOrderedCountersQuery = selectOrderedCounters;
    }
}
