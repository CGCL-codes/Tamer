package com.mtgi.analytics.jmx;

import static org.junit.Assert.*;
import javax.management.MalformedObjectNameException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.mtgi.analytics.BehaviorEvent;
import com.mtgi.analytics.aop.BehaviorTrackingAdvice;
import com.mtgi.analytics.sql.BehaviorTrackingDataSource;

public class NestedEventNamingStrategyTest {

    private NestedEventNamingStrategy inst;

    @Before
    public void setUp() {
        EventTypeNamingStrategy parent = new EventTypeNamingStrategy();
        parent.afterPropertiesSet();
        inst = (NestedEventNamingStrategy) parent.getNestedNamingStrategy();
    }

    @After
    public void tearDown() {
        inst = null;
    }

    @Test
    public void testRootEvent() throws MalformedObjectNameException {
        BehaviorEvent event = new TestEvent(null, BehaviorTrackingDataSource.DEFAULT_EVENT_TYPE, "executeUpdate", "testApp", null, null);
        event.addData().addElement("sql").setText("insert into FOO values (1, 2)");
        assertEquals("root sql event name computed correctly", "testApp:type=jdbc-monitor,name=executeUpdate", inst.getObjectName(event, null).toString());
    }

    @Test
    public void testShallowEvent() throws MalformedObjectNameException {
        BehaviorEvent parent = new TestEvent(null, BehaviorTrackingAdvice.DEFAULT_EVENT_TYPE, "com.mtgi.analytics.test.SomeType.someMethod", "testApp", null, null);
        BehaviorEvent event = new TestEvent(parent, BehaviorTrackingDataSource.DEFAULT_EVENT_TYPE, "executeUpdate", "testApp", null, null);
        event.addData().addElement("sql").setText("insert into FOO values (1, 2)");
        assertEquals("nested sql event name computed correctly", "testApp:type=method-monitor,package=com.mtgi,group=analytics.test,class=SomeType,name=someMethod,nested=jdbc_executeUpdate", inst.getObjectName(event, null).toString());
    }

    @Test
    public void testDeepNesting() throws MalformedObjectNameException {
        BehaviorEvent parent = new TestEvent(null, BehaviorTrackingAdvice.DEFAULT_EVENT_TYPE, "com.mtgi.analytics.test.SomeType.someMethod", "testApp", null, null);
        BehaviorEvent event = new TestEvent(parent, BehaviorTrackingDataSource.DEFAULT_EVENT_TYPE, "executeUpdate", "testApp", null, null);
        for (int i = 2; i <= 3; ++i) event = new TestEvent(event, BehaviorTrackingDataSource.DEFAULT_EVENT_TYPE, "executeUpdate_" + i, "testApp", null, null);
        assertEquals("deep nested sql event name computed correctly", "testApp:type=method-monitor,package=com.mtgi,group=analytics.test,class=SomeType,name=someMethod," + "nested=jdbc_executeUpdate," + "nested[2]=jdbc_executeUpdate_2," + "nested[3]=jdbc_executeUpdate_3", inst.getObjectName(event, null).toString());
    }

    @Test
    public void testNestingOverflow() throws MalformedObjectNameException {
        BehaviorEvent parent = new TestEvent(null, BehaviorTrackingAdvice.DEFAULT_EVENT_TYPE, "com.mtgi.analytics.test.SomeType.someMethod", "testApp", null, null);
        BehaviorEvent event = new TestEvent(parent, BehaviorTrackingDataSource.DEFAULT_EVENT_TYPE, "executeUpdate", "testApp", null, null);
        for (int i = 2; i <= 14; ++i) event = new TestEvent(event, BehaviorTrackingDataSource.DEFAULT_EVENT_TYPE, "executeUpdate_" + i, "testApp", null, null);
        try {
            inst.getObjectName(event, null);
            fail("overflow exception should be raised");
        } catch (IllegalStateException expected) {
        }
    }
}
