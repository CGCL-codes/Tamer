package org.hsqldb.test;

import java.sql.Connection;

public class TestScript extends TestBase {

    String path = "TestTemp.txt";

    public TestScript(String name) {
        super(name);
    }

    public void test() throws java.lang.Exception {
        Connection conn = newConnection();
        TestUtil.testScript(conn, path);
    }

    public static void main(String[] Args) throws Exception {
        TestScript ts = new TestScript("test");
        ts.test();
    }
}
