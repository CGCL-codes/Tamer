package net.wgen.op;

import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.mock.web.MockFilterConfig;
import net.wgen.op.logging.TraceTrigger;
import net.wgen.op.db.dbunit.DBUnitParamTrigger;
import net.wgen.op.util.ReflectionUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.commons.dbcp.DriverConnectionFactory;
import org.apache.commons.dbcp.ConnectionFactory;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.DataSetException;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.sql.Driver;
import junit.framework.TestCase;

/**
 * @author paulf
 * @version $Id: OpTestServices.java 30 2007-07-01 17:34:44Z paulfeuer $
 */
public class OpTestServices {

    private static final String ORACLE_DS = "oracle.jdbc.pool.OracleConnectionPoolDataSource";

    private static final String MYSQL_DS = "com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";

    private static final String HSQL_DS = "org.hsqldb.jdbcDriver";

    public static final String[] ORA_CX = { ORACLE_DS, "jdbc:oracle:thin:@oradb.mycompany.net:1521:DEV", "scott", "tiger" };

    public static final String[] MYSQL_CX = { MYSQL_DS, "jdbc:mysql://localhost:3306/opexample", "opproduct", "abc123" };

    public static final String[] HSQL_CX = { HSQL_DS, "jdbc:hsqldb:mem:mycache", null, null };

    public static String[] WHICH_DB_CX = MYSQL_CX;

    public static final String TRACE_PARAM = "trace";

    public static final String TRACE_LOGGER_PATH = "net.wgen";

    static {
        BasicConfigurator.configure();
        new ExampleModule();
    }

    public static void initDataSource(String dataSourceName, String[] whichCx) {
        OpExecutorFactory fact = OpExecutorFactory.getDelegatingFactory();
        if (!((fact instanceof DirectExecutorFactory))) {
            fact = new DirectExecutorFactory();
        }
        try {
            if (HSQL_DS.equals(whichCx[0])) {
                Driver driver = (Driver) Class.forName(whichCx[0]).newInstance();
                ConnectionFactory cxFactory = new DriverConnectionFactory(driver, whichCx[1], null);
                ((DirectExecutorFactory) fact).mapConnectionFactory(dataSourceName, cxFactory);
            } else {
                DataSource ds = (DataSource) ReflectionUtils.instantiate(whichCx[0]);
                ;
                ReflectionUtils.invoke(ds, "setURL", new Class[] { String.class }, new Object[] { whichCx[1] });
                ((DirectExecutorFactory) fact).mapConnectionFactory(dataSourceName, whichCx[2], whichCx[3], ds);
            }
            OpExecutorFactory.setDelegatingFactory(fact);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertEquals(List resultsFromDb, ITable resultsFromXml) throws DataSetException {
        TestCase.assertEquals(resultsFromDb.size(), resultsFromXml.getRowCount());
        final int len = resultsFromXml.getRowCount();
        for (int i = 0; i < len; i++) {
            Map record = (Map) resultsFromDb.get(i);
            Iterator it = record.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String columnNameFromDb = (String) entry.getKey();
                Object valueFromDb = entry.getValue();
                Object valueFromXml = resultsFromXml.getValue(i, columnNameFromDb);
                TestCase.assertEquals(valueFromDb, valueFromXml);
            }
        }
        Logger.getLogger(OpTestServices.class).debug("Validated " + len + " rows equal: " + resultsFromDb);
    }

    public static WebMockObjectFactory createNewWebMockObjectFactory(WebMockObjectFactory fact) {
        if (fact == null) {
            fact = new WebMockObjectFactory() {

                public MockFilterConfig createMockFilterConfig() {
                    MockFilterConfig cfg = new MockFilterConfig();
                    cfg.setInitParameter("initializer.1", DBUnitParamTrigger.class.getName());
                    cfg.setInitParameter("initializer.2", TraceTrigger.class.getName());
                    cfg.setInitParameter(TraceTrigger.INIT_PARAM_LOGGER_PATH, TRACE_LOGGER_PATH);
                    cfg.setInitParameter(TraceTrigger.INIT_PARAM_TRIGGER_PARAM, TRACE_PARAM);
                    return cfg;
                }
            };
            return fact;
        } else {
            return new WebMockObjectFactory(fact, false) {

                public MockFilterConfig createMockFilterConfig() {
                    MockFilterConfig cfg = new MockFilterConfig();
                    cfg.setInitParameter("initializer.1", DBUnitParamTrigger.class.getName());
                    cfg.setInitParameter("initializer.2", TraceTrigger.class.getName());
                    cfg.setInitParameter(TraceTrigger.INIT_PARAM_LOGGER_PATH, TRACE_LOGGER_PATH);
                    cfg.setInitParameter(TraceTrigger.INIT_PARAM_TRIGGER_PARAM, TRACE_PARAM);
                    return cfg;
                }
            };
        }
    }
}
