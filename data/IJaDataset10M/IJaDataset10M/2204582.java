package net.sf.doolin.sqm.test.service;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractTestServiceDB extends AbstractTestService {

    @Override
    protected void runInitScripts(Connection c) throws SQLException {
        super.runInitScripts(c);
        runScript(c, "/net/sf/doolin/sqm/test/service/Test.sql");
    }
}
