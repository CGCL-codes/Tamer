package magoffin.matt.ieat.dao.hbm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import magoffin.matt.dao.hbm.GenericHibernateDao;
import magoffin.matt.ieat.dao.UnitDao;
import magoffin.matt.ieat.domain.System;
import magoffin.matt.ieat.domain.Unit;
import magoffin.matt.ieat.domain.impl.UnitImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * UnitDao using Hibernate.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 39 $ $Date: 2009-05-05 23:30:47 -0400 (Tue, 05 May 2009) $
 */
public class UnitDaoImpl extends GenericHibernateDao<Unit, Integer> implements UnitDao {

    /** The query to find all Unit objects. */
    public static final String FIND_ALL = "UnitsAll";

    /** Find all units for a particular measurment system. */
    public static final String FIND_UNITS_FOR_SYSTEM = "UnitsForSystem";

    private String sqlIndexAll;

    private JdbcTemplate jdbcTemplate;

    /**
	 * Method to call after all dependency injection has occured.
	 */
    public void init() {
        if (jdbcTemplate == null) {
            throw new RuntimeException("jdbcTemplate not configured");
        }
        if (sqlIndexAll == null) {
            throw new RuntimeException("sqlIndexAll not configured");
        }
    }

    /**
	 * Default constructor.
	 */
    public UnitDaoImpl() {
        super(UnitImpl.class);
    }

    public List<Unit> getUnits() {
        return findByNamedQuery(FIND_ALL);
    }

    @Override
    protected Integer getPrimaryKey(Unit domainObject) {
        if (domainObject == null) return null;
        return domainObject.getUnitId();
    }

    public List<Unit> getUnitsForSystem(System sys) {
        return findByNamedQuery(FIND_UNITS_FOR_SYSTEM, new Object[] { sys.getSystemId() });
    }

    public void index(final UnitIndexCallback callback) {
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL for index all units: " + sqlIndexAll);
        }
        final UnitIndexCallbackDataImpl callbackData = new UnitIndexCallbackDataImpl();
        jdbcTemplate.query(sqlIndexAll, new RowCallbackHandler() {

            private ResultSet myRs;

            public void processRow(ResultSet rs) throws SQLException {
                if (myRs == null) {
                    myRs = rs;
                }
                callbackData.unitId = new Integer(rs.getInt(1));
                callbackData.name = rs.getString(2);
                callbackData.abbreviation = rs.getString(3);
                callback.handle(callbackData);
            }
        });
    }

    private static class UnitIndexCallbackDataImpl implements UnitIndexCallbackData {

        private Integer unitId;

        private String name;

        private String abbreviation;

        public String getAbbreviation() {
            return abbreviation;
        }

        public String getName() {
            return name;
        }

        public Integer getUnitId() {
            return unitId;
        }
    }

    /**
	 * @return the jdbcTemplate
	 */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
	 * @return the sqlIndexAll
	 */
    public String getSqlIndexAll() {
        return sqlIndexAll;
    }

    /**
	 * @param sqlIndexAll the sqlIndexAll to set
	 */
    public void setSqlIndexAll(String sqlIndexAll) {
        this.sqlIndexAll = sqlIndexAll;
    }
}
