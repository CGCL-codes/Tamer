package com.triplea.rolap.server;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.triplea.rolap.client.DatabaseInfoAux;
import com.triplea.rolap.client.DimensionInfoAux;
import com.triplea.rolap.client.ElementInfoAux;
import com.triplea.rolap.client.ROLAPClientAux;

public class ElementReplaceHandler {

    private static String command = "/element/replace";

    private static String sid = "";

    private static String dbName = "dbtest_dimdh";

    private static int dbId;

    private static String dimName = "dimtest";

    private static int dimId;

    private static String elemName = "test";

    private static int elemId;

    private static String elemChildName = "child";

    private static int elemChildId;

    private static String elemChild2Name = "child2";

    private static int elemChild2Id;

    private static String elemNewName = "newname";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        sid = ROLAPClientAux.login();
        String query = "/database/create?sid=" + sid + "&new_name=" + dbName;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        Hashtable<String, ArrayList<DatabaseInfoAux>> info = DatabaseInfoAux.parseResult(res);
        DatabaseInfoAux dbInfo = info.get("normal").get(0);
        dbId = dbInfo.getIdentifier();
        query = "/dimension/create?sid=" + sid + "&database=" + dbId + "&new_name=" + dimName;
        res = ROLAPClientAux.sendRequest(query);
        Hashtable<String, ArrayList<DimensionInfoAux>> info1 = DimensionInfoAux.parseResult(res);
        DimensionInfoAux dimInfo = info1.get("normal").get(0);
        dimId = dimInfo.getId();
        query = "/element/create?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&type=1&new_name=" + elemName;
        res = ROLAPClientAux.sendRequest(query);
        ArrayList<ElementInfoAux> result = ElementInfoAux.parseResult(res);
        elemId = result.get(0).getId();
        query = "/element/create?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&type=1&new_name=" + elemChildName;
        res = ROLAPClientAux.sendRequest(query);
        result = ElementInfoAux.parseResult(res);
        elemChildId = result.get(0).getId();
        query = "/element/create?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&type=1&new_name=" + elemChild2Name;
        res = ROLAPClientAux.sendRequest(query);
        result = ElementInfoAux.parseResult(res);
        elemChild2Id = result.get(0).getId();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        String query = "/database/destroy?database=" + dbId + "&sid=" + sid;
        ROLAPClientAux.sendRequest(query);
        ROLAPClientAux.logout();
    }

    @Test
    public void missingeSID() throws Exception {
        String query = command + "?database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&name_element=" + elemName + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "1015", vals[0]);
    }

    @Test
    public void incorrectSID() throws Exception {
        String query = command + "?sid=incorrect&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&name_element=" + elemName + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "1007", vals[0]);
    }

    @Test
    public void missingeDatabase() throws Exception {
        String query = command + "?sid=" + sid + "&dimension=" + dimId + "&element=" + elemId + "&name_element=" + elemName + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "2001", vals[0]);
    }

    @Test
    public void incorrectDatabase() throws Exception {
        String query = command + "?sid=" + sid + "&database=99999&dimension=" + dimId + "&element=" + elemId + "&name_element=" + elemName + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "2001", vals[0]);
    }

    @Test
    public void missingeDimension() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&element=" + elemId + "&name_element=" + elemName + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "3002", vals[0]);
    }

    @Test
    public void incorrectDimension() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=99999&element=" + elemId + "&name_element=" + elemName + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "3002", vals[0]);
    }

    @Test
    public void missingeElementAndName() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4006", vals[0]);
    }

    @Test
    public void incorrectElement() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=99999&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4006", vals[0]);
    }

    @Test
    public void incorrectElementName() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&name_element=99999&type=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4006", vals[0]);
    }

    @Test
    public void invalidType() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=10";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4008", vals[0]);
    }

    @Test
    public void missingType() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4008", vals[0]);
    }

    public ElementInfoAux replaceElement(int type) throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=" + type;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String[] vals = res.get(0).split(";");
        ArrayList<ElementInfoAux> result = ElementInfoAux.parseResult(res);
        ElementInfoAux info = result.get(0);
        return info;
    }

    @Test
    public void setElementNumeric() throws Exception {
        ElementInfoAux info = replaceElement(1);
        assertEquals("Invalid element type", 1, info.getType());
    }

    @Test
    public void setElementString() throws Exception {
        ElementInfoAux info = replaceElement(2);
        assertEquals("Invalid element type", 2, info.getType());
    }

    @Test
    public void addChildren() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4&children=" + elemChildId + "," + elemChild2Id;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        ArrayList<ElementInfoAux> result = ElementInfoAux.parseResult(res);
        ElementInfoAux info = result.get(0);
        assertEquals("Invalid children count", 2, info.getChildrenIds().size());
        assertEquals("Invalid 1st child id", elemChildId, info.getChildrenIds().get(0).intValue());
        assertEquals("Invalid 2nd child id", elemChild2Id, info.getChildrenIds().get(1).intValue());
    }

    @Test
    public void dropChildren() throws Exception {
        addChildren();
        ElementInfoAux info = replaceElement(4);
        assertEquals("Invalid children count", 0, info.getChildrenIds().size());
    }

    @Test
    public void invalidChild() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4" + "&children=" + elemChildId + ",99999";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4004", vals[0]);
    }

    @Test
    public void setWeights() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4" + "&children=" + elemChildId + "," + elemChild2Id + "&weights=3,4";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        ArrayList<ElementInfoAux> result = ElementInfoAux.parseResult(res);
        ElementInfoAux info = result.get(0);
        assertEquals("Invalid children count", 2, info.getChildrenIds().size());
        assertEquals("Invalid 1st child weight", (double) 3.0, info.getChildrenWeights().get(0).doubleValue(), 0);
        assertEquals("Invalid 2nd child weight", (double) 4.0, info.getChildrenWeights().get(1).doubleValue(), 0);
    }

    @Test
    public void noWeights() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4" + "&children=" + elemChildId + "," + elemChild2Id;
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        ArrayList<ElementInfoAux> result = ElementInfoAux.parseResult(res);
        ElementInfoAux info = result.get(0);
        assertEquals("Invalid children count", 2, info.getChildrenIds().size());
        assertEquals("Invalid 1st child weight", (double) 1.0, info.getChildrenWeights().get(0).doubleValue(), 0);
        assertEquals("Invalid 2nd child weight", (double) 1.0, info.getChildrenWeights().get(1).doubleValue(), 0);
    }

    @Test
    public void invalidWeights() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4" + "&children=" + elemChildId + "," + elemChild2Id + "&weights=3";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "1016", vals[0]);
    }

    @Test
    public void invalidChildWeight() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4" + "&children=" + elemChildId + "," + elemChild2Id + "&weights=3,a";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "1007", vals[0]);
    }

    @Test
    public void createCycle() throws Exception {
        String query = command + "?sid=" + sid + "&database=" + dbId + "&dimension=" + dimId + "&element=" + elemId + "&type=4" + "&children=" + elemId + "&weights=1";
        Vector<String> res = ROLAPClientAux.sendRequest(query);
        assertEquals("Incorrect number of response lines", 1, res.size());
        String vals[] = res.get(0).split(";");
        assertEquals("Incorrect number of result parts", 3, vals.length);
        assertEquals("Incorrect error code", "4001", vals[0]);
    }
}
