package com.mtgi.analytics.aop.config.v11;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sql.DataSource;
import javax.xml.stream.XMLOutputFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;
import com.mtgi.analytics.BehaviorEvent;
import com.mtgi.analytics.BehaviorTrackingManagerImpl;
import com.mtgi.analytics.EventDataElementSerializer;
import com.mtgi.analytics.sql.BehaviorTrackingDataSource;

@SpringApplicationContext("com/mtgi/analytics/aop/config/v11/DataSourceDecoratorTest-applicationContext.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class DataSourceDecoratorTest {

    @SpringBeanByName
    private DataSource unitilsDS;

    @SpringBeanByName
    private Service testBean;

    @SpringBeanByName
    private TestPersister testPersister;

    @SpringBeanByName
    private BehaviorTrackingManagerImpl defaultTrackingManager;

    @Before
    public void initDB() throws SQLException {
        exec(unitilsDS, "create table test (id numeric primary key, data varchar)");
    }

    @After
    public void destroyDB() throws SQLException {
        exec(unitilsDS, "drop table test");
    }

    @Test
    public void testTracking() throws Exception {
        assertTrue("datasource has been wrapped in a proxy", unitilsDS instanceof BehaviorTrackingDataSource);
        assertTrue("original datasource is wrapped", ((BehaviorTrackingDataSource) unitilsDS).getTargetDataSource() instanceof DataSource);
        assertSame("inner persister is promoted to global scope", testPersister, defaultTrackingManager.getPersister());
        int count = testPersister.count();
        assertEquals("done", testBean.thisMethodIsTracked());
        long start = System.currentTimeMillis();
        while (testPersister.count() == count && (System.currentTimeMillis() - start < 10000)) Thread.sleep(100);
        assertTrue("events logged when method called", testPersister.count() - count >= 2);
        ArrayList<BehaviorEvent> events = testPersister.events();
        for (Iterator<BehaviorEvent> it = events.iterator(); it.hasNext(); ) if ("behavior-tracking".equals(it.next().getType())) it.remove();
        BehaviorEvent be = events.get(events.size() - 1);
        assertEquals("testApp", be.getApplication());
        assertEquals("method", be.getType());
        assertEquals(Service.class.getName() + ".thisMethodIsTracked", be.getName());
        BehaviorEvent child = events.get(events.size() - 2);
        assertEquals("testApp", child.getApplication());
        assertEquals("jdbc", child.getType());
        assertEquals("execute", child.getName());
        assertSame(be, child.getParent());
        String data = new EventDataElementSerializer(XMLOutputFactory.newInstance()).serialize(child.getData(), false);
        assertEquals("<event-data><sql>insert into test values (1,'hello')</sql></event-data>", data);
    }

    public static class Service {

        private DataSource dataSource;

        private int count;

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public String thisMethodIsTracked() throws SQLException {
            exec(dataSource, "insert into test values (" + ++count + ",'hello')");
            return "done";
        }
    }

    private static void exec(DataSource ds, String sql) throws SQLException {
        Connection conn = ds.getConnection();
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.execute(sql);
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
    }
}
