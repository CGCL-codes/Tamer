package ch.epfl.lbd.database.providers.oracle.olap.test;

import java.util.ArrayList;
import ch.epfl.lbd.benchmark.Tester;
import ch.epfl.lbd.database.providers.oracle.connection.OracleConnection;
import ch.epfl.lbd.database.providers.oracle.olap.OracleCube;
import ch.epfl.lbd.database.providers.oracle.olap.OracleDataWarehouse;
import oracle.olapi.session.UserSession;

public class TestCube extends Tester {

    @org.junit.Test
    public void run() throws Exception {
        OracleConnection connection = new OracleConnection("src/connections.properties", "connection1");
        try {
            connection.multiDimensional = true;
            connection.openConnection();
            UserSession session = connection.openSession();
            OracleDataWarehouse dw = new OracleDataWarehouse("owner", "NEWJAVA_AW_TEST");
            dw.loadObject(connection);
            ArrayList<String> tbls = connection.getAnalyticalWorkspaces();
            for (String table : tbls) logger.info("AWS: " + table);
            connection.closeSession(session);
            connection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
