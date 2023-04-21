package com.triplea.rolap.server;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.triplea.rolap.client.CubeInfoAux;
import com.triplea.rolap.client.DatabaseInfoAux;
import com.triplea.rolap.client.DimensionInfoAux;
import com.triplea.rolap.client.ROLAPClientAux;

/**
 * @author kononyhin
 *
 */
public class CubeUnloadHandler {

    private static String command = "/cube/unload";

    private static String sid = "";

    private static String dbName = "dbtest_cube_uh";

    private static int dbId = -1;

    private static String cubeName = "cube_uh";

    private static int cubeId = -1;

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String login = ROLAPClientAux.loadProperties().getProperty("user");
        String password = ROLAPClientAux.loadProperties().getProperty("password");
        String query = "/server/login?user=" + login + "&extern_password=" + password;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        String vals[] = res.get(0).split(";");
        sid = vals[0];
        query = "/database/create?sid=" + sid + "&new_name=" + dbName;
        res = ROLAPClientAux.sendRequest(query);
        Hashtable<String, ArrayList<DatabaseInfoAux>> info = DatabaseInfoAux.parseResult(res);
        DatabaseInfoAux dbInfo = info.get("normal").get(0);
        dbId = dbInfo.getIdentifier();
        query = "/dimension/create?sid=" + sid + "&database=" + dbId + "&new_name=dim1";
        res = ROLAPClientAux.sendRequest(query);
        Hashtable<String, ArrayList<DimensionInfoAux>> info1 = DimensionInfoAux.parseResult(res);
        DimensionInfoAux dimInfo = info1.get("normal").get(0);
        int dim1Id = dimInfo.getId();
        query = "/dimension/create?sid=" + sid + "&database=" + dbId + "&new_name=dim2";
        res = ROLAPClientAux.sendRequest(query);
        info1 = DimensionInfoAux.parseResult(res);
        dimInfo = info1.get("normal").get(0);
        int dim2Id = dimInfo.getId();
        query = "/cube/create?sid=" + sid + "&database=" + dbId + "&dimensions=" + dim1Id + "," + dim2Id + "&new_name=" + cubeName;
        res = ROLAPClientAux.sendRequest(query);
        Hashtable<String, ArrayList<CubeInfoAux>> info3 = CubeInfoAux.parseResult(res);
        CubeInfoAux cubeInfo = info3.get("normal").get(0);
        cubeId = cubeInfo.getId();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        String query = "";
        if (-1 != dbId) {
            query = "/database/destroy?database=" + dbId + "&sid=" + sid;
            ROLAPClientAux.sendRequest(query);
        }
        query = "/server/logout?sid=" + sid;
        ROLAPClientAux.sendRequest(query);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void incorrectSID() throws Exception {
        String query = command + "?sid=XXXX&database=" + dbId;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "1015", vals[0]);
    }

    @Test
    public void incorrectDatabase() throws Exception {
        String query = command + "?sid=" + sid + "&database=incorrect";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "2001", vals[0]);
    }

    @Test
    public void incorrectCube() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&cube=incorrect";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "5000", vals[0]);
    }

    @Test
    public void correctParams() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&cube=" + cubeId;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 1, vals.length);
        assertEquals("Incorrect result", "1", vals[0]);
        CubeInfoAux cubeInfo = CubeInfoAux.getFromServer(sid, dbId, cubeId);
        assertEquals("Incorrect cube status", CubeInfoAux.STATUS_UNLOADED, cubeInfo.getStatus());
    }
}
