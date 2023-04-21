package org.apache.zookeeper.server.jersey;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ WadlTest.class, GetTest.class, GetChildrenTest.class, CreateTest.class, SetTest.class, ExistsTest.class, DeleteTest.class })
public class RestTestSuite {

    @BeforeClass
    public static void setUp() {
    }

    @AfterClass
    public static void tearDown() {
    }
}
